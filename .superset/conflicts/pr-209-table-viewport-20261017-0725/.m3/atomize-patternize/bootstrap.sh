#!/usr/bin/env bash
# SPDX-License-Identifier: Apache-2.0
set -euo pipefail

here="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
repo="$(cd "$here/../.." && pwd)"
target="$repo/.m3/target/atomize-patternize"
inventory="$here/inventory.tsv"
manifest="$target/source-files.tsv"

fail() {
  printf '%s\n' "$1" >&2
  exit "${2:-4}"
}

if [[ -e "$inventory" && "${SYNEXIA_REBIND_INVENTORY:-0}" != 1 ]]; then
  fail "M3_PLAN_INVENTORY_ALREADY_EXISTS set SYNEXIA_REBIND_INVENTORY=1 to replace"
fi
[[ ! -L "$here" ]] || fail "M3_ROLLOUT_DIRECTORY_SYMLINK"
mkdir -p "$target"

body="$target/source-files.body"
sorted="$target/source-files.sorted"
all_files="$target/all-files.txt"
poms_file="$target/poms.txt"
roots_file="$target/java-roots.txt"
: > "$body"
: > "$all_files"
: > "$poms_file"
: > "$roots_file"

while IFS= read -r -d '' file; do
  case "$file" in
    "$repo"/.m3/atomize-patternize/*|"$repo"/.m3/target/*) continue ;;
    "$repo"/*) rel="${file#"$repo"/}" ;;
    *) fail "M3_SOURCE_PATH_ESCAPE:$file" ;;
  esac
  case "$rel" in
    *$'\n'*|*$'\r'*|*$'\t'*) fail "M3_SOURCE_PATH_CONTROL_CHARACTER" ;;
  esac
  [[ -f "$file" && ! -L "$file" ]] || continue
  printf '%s\n' "$rel" >> "$all_files"
  if [[ "${file##*/}" == pom.xml ]]; then
    kind=POM
    printf '%s\n' "$rel" >> "$poms_file"
  elif [[ "$file" == *.java ]]; then
    kind=JAVA
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
  else
    continue
  fi

  bytes="$(wc -c < "$file" | tr -d '[:space:]')"
  sha="$(sha256sum -- "$file" | awk '{print $1}')"
  bytes_again="$(wc -c < "$file" | tr -d '[:space:]')"
  sha_again="$(sha256sum -- "$file" | awk '{print $1}')"
  [[ "$bytes" == "$bytes_again" && "$sha" == "$sha_again" ]] ||     fail "M3_SOURCE_CHANGED_DURING_SCAN:$rel"
  printf '%s\t%s\t%s\t%s\n' "$kind" "$rel" "$bytes_again" "$sha_again" >> "$body"
done < <(
  find "$repo"     \( -type d \( -name .git -o -name .gradle -o -name .idea -o -name .mvn -o -name target -o -name build -o -name out -o -name node_modules -o -name generated-sources -o -name generated-test-sources -o -name vendor -o -name site-packages \) -prune \) -o     \( -type f -print0 \)
)

LC_ALL=C sort -t $'\t' -k2,2 -k1,1 "$body" > "$sorted"
{
  printf 'kind\tpath\tbytes\tsha256\n'
  cat "$sorted"
} > "$manifest"

LC_ALL=C sort -u "$poms_file" -o "$poms_file"
LC_ALL=C sort -u "$roots_file" -o "$roots_file"
LC_ALL=C sort -u "$all_files" -o "$all_files"

content_root="$(sha256sum -- "$manifest" | awk '{print $1}')"
content_files="$(( $(wc -l < "$manifest") - 1 ))"
java_files="$(awk -F $'\t' 'NR>1 && $1=="JAVA" {n++} END {print n+0}' "$manifest")"
pom_files="$(awk -F $'\t' 'NR>1 && $1=="POM" {n++} END {print n+0}' "$manifest")"
inspected_files="$(wc -l < "$all_files" | tr -d '[:space:]')"

if ((java_files == 0)); then
  mode=NO_JAVA_SOURCE_ROOTS
elif ((pom_files > 0)); then
  mode=MAVEN_REACTOR
else
  mode=EXTERNAL_ENVELOPE
fi

join_us() {
  local file="$1" first=1 value
  while IFS= read -r value; do
    [[ -n "$value" ]] || continue
    if ((first)); then
      printf '%s' "$value"
      first=0
    else
      printf '\037%s' "$value"
    fi
  done < "$file"
}

pom_join="$(join_us "$poms_file")"
root_join="$(join_us "$roots_file")"
plan_root="$(
  printf '%s\n%s\n%s\n%s\n%s'     'M3-REPOSITORY-ATOM-PATTERN-BOOTSTRAP/1'     "$mode" "$pom_join" "$root_join" "$inspected_files" |
  sha256sum | awk '{print $1}'
)"
bound_root="$(
  printf '%s\n%s\n%s'     'M3-REPOSITORY-ATOM-PATTERN-BOOTSTRAP-BOUND/1'     "$plan_root" "$content_root" |
  sha256sum | awk '{print $1}'
)"

tmp="$target/inventory.tsv.new"
{
  printf 'kind\tvalue\n'
  printf 'schema\tBOOTSTRAP_V1\n'
  printf 'mode\t%s\n' "$mode"
  printf 'root\t%s\n' "$plan_root"
  printf 'inspectedFiles\t%s\n' "$inspected_files"
  while IFS= read -r value; do [[ -n "$value" ]] && printf 'pom\t%s\n' "$value"; done < "$poms_file"
  while IFS= read -r value; do [[ -n "$value" ]] && printf 'javaRoot\t%s\n' "$value"; done < "$roots_file"
  printf 'contentRoot\t%s\n' "$content_root"
  printf 'boundRoot\t%s\n' "$bound_root"
  printf 'contentFiles\t%s\n' "$content_files"
  printf 'javaFiles\t%s\n' "$java_files"
  printf 'pomFiles\t%s\n' "$pom_files"
} > "$tmp"

mv -f -- "$tmp" "$inventory"
rm -f -- "$body" "$sorted" "$all_files" "$poms_file" "$roots_file"

printf 'M3_ATOM_PATTERN_BOOTSTRAP_PASS mode=%s contentFiles=%s contentRoot=%s boundRoot=%s\n'   "$mode" "$content_files" "$content_root" "$bound_root"
