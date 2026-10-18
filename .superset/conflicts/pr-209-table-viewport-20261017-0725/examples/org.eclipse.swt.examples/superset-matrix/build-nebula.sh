#!/usr/bin/env bash
# Builds one jar per Nebula widget bundle with plain javac (no Tycho), so the
# showcase and the matrix runner can consume Nebula as jars.
#
# usage: build-nebula.sh <nebula-checkout> <swt-jar> <platform-deps-dir> <out-dir>
#   <nebula-checkout>    a Nebula working tree or `git archive` export (widgets/ is read)
#   <swt-jar>            the SWT jar to compile against
#   <platform-deps-dir>  directory of jars: jface, core.commands, equinox.common,
#                        core.runtime, osgi, ui.forms, ui.workbench, databinding,
#                        draw2d, emf (see README.md for the exact versions)
#   <out-dir>            receives <bundle>.jar, build-nebula.tsv and logs/
#
# Bundles are compiled in rounds: a bundle that fails because another Nebula
# bundle is missing is retried after the others are built. A bundle that still
# fails after the last round is recorded FAILED with its first javac error.
set -u
NEB=$1; SWT=$2; DEPS=$3; OUT=$4
JAVAC=${JAVAC:-javac}
mkdir -p "$OUT/logs" "$OUT/work"
SEP=';'; case "$(uname -s)" in Linux*|Darwin*) SEP=':';; esac
wpath() { if command -v cygpath >/dev/null; then cygpath -w "$1"; else echo "$1"; fi; }
deps_cp() { local cp; cp=$(wpath "$SWT"); for j in "$DEPS"/*.jar "$OUT"/*.jar; do [ -f "$j" ] && cp="$cp$SEP$(wpath "$j")"; done; echo "$cp"; }
bundles() {
  find "$NEB/widgets" -mindepth 2 -maxdepth 4 -name build.properties \
    | sed 's|/build.properties$||' \
    | grep -vE '\.tests?$|\.examples?$|\.example\.e4$|\.exampleview$|\.snippets$|\.feature$|\.css$|\.documentation|\.site$|\.integration$' | sort
}
source_folders() {  # the source.. entry of build.properties (default src/)
  local f; f=$(awk '/^source\.\.[ \t]*=/{on=1} on{l=l $0; if ($0 !~ /\\$/) on=0} END{print l}' "$1/build.properties" \
    | sed -e 's/^source\.\.[ \t]*=//' -e 's/\\//g' -e 's/,/ /g' -e 's|/||g')
  echo "${f:-src}"
}
build_one() {
  local dir=$1 name; name=$(basename "$dir")
  [ "$name" = nebulatoolbar ] && name=org.eclipse.nebula.widgets.nebulatoolbar
  local folders; folders=$(source_folders "$dir")
  local cls="$OUT/work/$name"; rm -rf "$cls"; mkdir -p "$cls"
  : > "$OUT/work/$name.files"
  for src in $folders; do [ -d "$dir/$src" ] && find "$dir/$src" -name '*.java' | while IFS= read -r f; do wpath "$f"; done >> "$OUT/work/$name.files"; done
  [ -s "$OUT/work/$name.files" ] || return 2
  "$JAVAC" -J-Xmx512m -nowarn -encoding UTF-8 --release 21 -proc:none \
    -cp "$(deps_cp)" -d "$(wpath "$cls")" @"$(wpath "$OUT/work/$name.files")" > "$OUT/logs/$name.log" 2>&1 || return 1
  for src in $folders; do [ -d "$dir/$src" ] && (cd "$dir/$src" && find . -type f ! -name '*.java' -exec cp --parents {} "$cls" \; 2>/dev/null); done
  (cd "$cls" && "${JAR:-jar}" cf "$(wpath "$OUT/$name.jar")" .) && echo "$name" >> "$OUT/work/built.lst"
}
: > "$OUT/work/built.lst"   # existing jars count as built unless FORCE=1
[ -z "${FORCE:-}" ] && for j in "$OUT"/*.jar; do [ -f "$j" ] && basename "$j" .jar >> "$OUT/work/built.lst"; done
mapfile -t ALL < <(bundles)
for round in 1 2 3 4; do
  progress=0
  for dir in "${ALL[@]}"; do
    name=$(basename "$dir"); [ "$name" = nebulatoolbar ] && name=org.eclipse.nebula.widgets.nebulatoolbar
    grep -qx "$name" "$OUT/work/built.lst" && continue
    if build_one "$dir"; then progress=1; echo "round $round BUILT $name"; fi
  done
  [ $progress = 0 ] && break
done
{
  printf 'bundle\tstatus\tfirst_error\n'
  for dir in "${ALL[@]}"; do
    name=$(basename "$dir"); [ "$name" = nebulatoolbar ] && name=org.eclipse.nebula.widgets.nebulatoolbar
    if grep -qx "$name" "$OUT/work/built.lst"; then printf '%s\tBUILT\t-\n' "$name"
    else err=$(grep -m1 -E 'error:' "$OUT/logs/$name.log" 2>/dev/null | tr '\t' ' ' | cut -c1-240)
         printf '%s\tFAILED\t%s\n' "$name" "${err:-no source}"; fi
  done
} > "$OUT/build-nebula.tsv"
grep -c BUILT "$OUT/build-nebula.tsv"
