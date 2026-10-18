#!/usr/bin/env bash
# SPDX-License-Identifier: Apache-2.0
set -euo pipefail

here="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
repo="$(cd "$here/../.." && pwd)"
target="$repo/.m3/target/atomize-patternize"
inventory="$here/inventory.tsv"
manifest="$target/source-files.tsv"
receipt="$target/execution-receipt.tsv"
recipe="${SYNEXIA_ATOM_PATTERN_RECIPE:-com.synexia.m3.EveryModuleAtomPatternMastery}"

fail() {
  printf '%s\n' "$1" >&2
  exit "${2:-4}"
}

[[ -f "$inventory" && ! -L "$inventory" ]] || fail "M3_PLAN_INVENTORY_REQUIRED"
mkdir -p "$target"
body="$target/source-files.body"
sorted="$target/source-files.sorted"
: > "$body"

while IFS= read -r -d '' file; do
  case "$file" in
    "$repo"/*) rel="${file#"$repo"/}" ;;
    *) fail "M3_SOURCE_PATH_ESCAPE:$file" ;;
  esac
  case "$rel" in
    *$'\n'*|*$'\r'*|*$'\t'*) fail "M3_SOURCE_PATH_CONTROL_CHARACTER" ;;
  esac
  [[ -f "$file" && ! -L "$file" ]] || fail "M3_SOURCE_NOT_ORDINARY:$rel"
  if [[ "${file##*/}" == pom.xml ]]; then
    kind=POM
  else
    kind=JAVA
  fi
  bytes="$(wc -c < "$file" | tr -d '[:space:]')"
  sha="$(sha256sum -- "$file" | awk '{print $1}')"
  bytes_again="$(wc -c < "$file" | tr -d '[:space:]')"
  sha_again="$(sha256sum -- "$file" | awk '{print $1}')"
  [[ "$bytes" == "$bytes_again" && "$sha" == "$sha_again" ]] || \
    fail "M3_SOURCE_CHANGED_DURING_SCAN:$rel"
  printf '%s\t%s\t%s\t%s\n' "$kind" "$rel" "$bytes_again" "$sha_again" >> "$body"
done < <(
  find "$repo" \
    \( -type d \( -name .git -o -name .gradle -o -name .idea -o -name .mvn -o -name target -o -name build -o -name out -o -name node_modules -o -name generated-sources -o -name generated-test-sources -o -name vendor -o -name site-packages \) -prune \) -o \
    \( -type f \( -name '*.java' -o -name pom.xml \) -print0 \)
)

LC_ALL=C sort -t $'\t' -k2,2 -k1,1 "$body" > "$sorted"
{
  printf 'kind\tpath\tbytes\tsha256\n'
  cat "$sorted"
} > "$manifest"
rm -f -- "$body" "$sorted"

content_root="$(sha256sum -- "$manifest" | awk '{print $1}')"
content_files="$(( $(wc -l < "$manifest") - 1 ))"
expected_content_root="$(awk -F $'\t' '$1=="contentRoot" {print $2}' "$inventory")"
expected_content_files="$(awk -F $'\t' '$1=="contentFiles" {print $2}' "$inventory")"
expected_mode="$(awk -F $'\t' '$1=="mode" {print $2}' "$inventory")"
[[ "$expected_content_root" =~ ^[0-9a-f]{64}$ ]] || fail "M3_CONTENT_ROOT_REQUIRED"
[[ "$expected_content_files" =~ ^[0-9]+$ ]] || fail "M3_CONTENT_FILE_COUNT_REQUIRED"
[[ "$content_root" == "$expected_content_root" ]] || fail "M3_SOURCE_CONTENT_DRIFT expected=$expected_content_root actual=$content_root"
[[ "$content_files" == "$expected_content_files" ]] || fail "M3_SOURCE_FILE_SET_DRIFT expected=$expected_content_files actual=$content_files"

mapfile -t pom_rel < <(awk -F $'\t' 'NR>1 && $1=="POM" {print $2}' "$manifest")
java_files="$(awk -F $'\t' 'NR>1 && $1=="JAVA" {n++} END {print n+0}' "$manifest")"
if ((java_files == 0)); then
  mode=NO_JAVA_SOURCE_ROOTS
elif ((${#pom_rel[@]})); then
  mode=MAVEN_REACTOR
else
  mode=EXTERNAL_ENVELOPE
fi
[[ "$mode" == "$expected_mode" ]] || fail "M3_PLAN_MODE_DRIFT expected=$expected_mode actual=$mode"

{
  printf 'kind\tvalue\n'
  printf 'mode\t%s\n' "$mode"
  printf 'contentRoot\t%s\n' "$content_root"
  printf 'contentFiles\t%s\n' "$content_files"
  printf 'inventoryRoot\t%s\n' "$(awk -F $'\t' '$1=="root" {print $2}' "$inventory")"
  printf 'boundRoot\t%s\n' "$(awk -F $'\t' '$1=="boundRoot" {print $2}' "$inventory")"
  printf 'recipe\t%s\n' "$recipe"
} > "$receipt"

if ((java_files == 0)); then
  printf 'analysisStatus\tNO_JAVA_SOURCE_ROOTS\n' >> "$receipt"
  echo "NO_JAVA_SOURCE_ROOTS"
  exit 0
fi

version="${SYNEXIA_RECIPE_VERSION:?set SYNEXIA_RECIPE_VERSION to an installed synexia-openrewrite-recipes version}"
plugin="${SYNEXIA_REWRITE_PLUGIN_VERSION:-5.23.1}"
artifact="com.synexia:synexia-openrewrite-recipes:${version}"
printf 'recipeArtifact\t%s\npluginVersion\t%s\n' "$artifact" "$plugin" >> "$receipt"

if [[ -x "$repo/mvnw" ]]; then
  maven=("$repo/mvnw")
elif command -v mvn >/dev/null 2>&1; then
  maven=("$(command -v mvn)")
else
  fail "MAVEN_REQUIRED" 2
fi

rewrite() {
  "${maven[@]}" -B -ntp "$@" \
    "org.openrewrite.maven:rewrite-maven-plugin:${plugin}:dryRunNoFork" \
    "-Drewrite.recipeArtifactCoordinates=${artifact}" \
    "-Drewrite.activeRecipes=${recipe}" \
    -Drewrite.exportDatatables=true \
    -Drewrite.failOnInvalidActiveRecipes=true \
    -Drewrite.failOnDryRunResults=false
}

if ((${#pom_rel[@]} > 4096)); then
  fail "MAVEN_POM_LIMIT" 3
fi

if ((${#pom_rel[@]})); then
  for rel in "${pom_rel[@]}"; do
    rewrite -N -f "$repo/$rel"
  done
  printf 'analysisStatus\tPASS\n' >> "$receipt"
  exit 0
fi

roots_file="$target/java-roots.txt"
: > "$roots_file"
while IFS= read -r rel; do
  case "$rel" in
    src/main/java/*) root='src/main/java' ;;
    */src/main/java/*) root="${rel%%/src/main/java/*}/src/main/java" ;;
    src/test/java/*) root='src/test/java' ;;
    */src/test/java/*) root="${rel%%/src/test/java/*}/src/test/java" ;;
    src/it/java/*) root='src/it/java' ;;
    */src/it/java/*) root="${rel%%/src/it/java/*}/src/it/java" ;;
    src/integrationTest/java/*) root='src/integrationTest/java' ;;
    */src/integrationTest/java/*) root="${rel%%/src/integrationTest/java/*}/src/integrationTest/java" ;;
    *) root="$(dirname "$rel")" ;;
  esac
  printf '%s\n' "$root" >> "$roots_file"
done < <(awk -F $'\t' 'NR>1 && $1=="JAVA" {print $2}' "$manifest")
mapfile -t roots < <(LC_ALL=C sort -u "$roots_file")
rm -f -- "$roots_file"

if ((${#roots[@]} == 0)); then
  fail "NO_JAVA_SOURCE_ROOTS"
fi
if ((${#roots[@]} > 4096)); then
  fail "REPOSITORY_SOURCE_ROOT_LIMIT" 3
fi

pom="$target/pom.xml"
cat > "$pom" <<'POM'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.synexia.analysis</groupId>
  <artifactId>external-donor-analysis-envelope</artifactId>
  <version>1.0-SNAPSHOT</version>
  <properties>
    <maven.compiler.release>21</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>
  <build>
    <sourceDirectory>${m3.source.root}</sourceDirectory>
  </build>
</project>
POM

for root in "${roots[@]}"; do
  rewrite -f "$pom" "-Dm3.source.root=$repo/$root"
done
printf 'analysisStatus\tPASS\n' >> "$receipt"
