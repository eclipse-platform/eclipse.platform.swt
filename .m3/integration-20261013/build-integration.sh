#!/usr/bin/env bash
# SPDX-License-Identifier: EPL-2.0
# build-integration.sh - re-runnable forward-merge integration of the 2026-10-13
# viewport / flicker / lazy / residency PRs into one SWT branch and one Nebula branch.
#
# Usage:
#   build-integration.sh merge  [swt|nebula|all]   fetch PR heads, forward-merge them (default)
#   build-integration.sh push   [swt|nebula|all]   push m3/integration-20261013 to origin
#   build-integration.sh status [swt|nebula|all]   print PR head vs. integration ancestry
#   build-integration.sh open-prs                  list open PRs that are NOT in the merge lists
#
# Rules this script keeps:
#   * forward merges only (git merge --no-ff), never rebase or reset an existing integration branch;
#   * merge order is the order of the lists below (CONSOLIDATION_20261013.md "Merge order");
#   * conflicts are replayed with git rerere (enabled per command, not in repo config); an
#     unresolved conflict stops the run with exit 2 and lists the files, so a human or agent
#     resolves it, commits, records it in INTEGRATION.md and re-runs;
#   * a PR that is no longer OPEN is skipped (merged PRs are already in origin/master).
#
# Environment (defaults are the 2026-10-13 workstation layout):
#   SWT_REPO, NEBULA_REPO  existing clones of hsoliwal/eclipse.platform.swt and hsoliwal/nebula
#   WT_ROOT                where the integration worktrees live
#   LOG_DIR                fetch/merge logs
#   INCLUDE_REPROOF=1      also merge Nebula #149 (optional current-master reproof) last
#   EXTRA_SWT_PRS / EXTRA_NEBULA_PRS   space-separated PR numbers appended after the lists
set -euo pipefail
export MSYS_NO_PATHCONV=1 GIT_TERMINAL_PROMPT=0

STAMP=20261013
BRANCH="m3/integration-${STAMP}"
SWT_GH=hsoliwal/eclipse.platform.swt
NEBULA_GH=hsoliwal/nebula
SWT_REPO=${SWT_REPO:-C:/m3-work-c/swt}
NEBULA_REPO=${NEBULA_REPO:-C:/m3-work-c/nebula}
WT_ROOT=${WT_ROOT:-C:/m3-work-c/wt}
LOG_DIR=${LOG_DIR:-C:/m3-work-c/integ-logs}
SWT_WT="${WT_ROOT}/swt-integration"
NEBULA_WT="${WT_ROOT}/nebula-integration"
RECORD_DIR=".m3/integration-${STAMP}"

# Merge order. Entries are PR numbers; "branch:<name>" merges a pushed branch that has no PR.
SWT_ORDER=(120 123 121 124 122 119 126 125 127 128 129 130 131)
NEBULA_ORDER=(151 150 152 153 154 155 156 157 158)
[ "${INCLUDE_REPROOF:-0}" = 1 ] && NEBULA_ORDER+=(149)
read -r -a _xs <<<"${EXTRA_SWT_PRS:-}";    SWT_ORDER+=("${_xs[@]}")
read -r -a _xn <<<"${EXTRA_NEBULA_PRS:-}"; NEBULA_ORDER+=("${_xn[@]}")

mkdir -p "$LOG_DIR"
log() { printf '%s %s\n' "$(date -u +%H:%M:%SZ)" "$*" | tee -a "$LOG_DIR/build-integration.log" >&2; }

# pr_info <gh-repo> <entry> -> "number<TAB>state<TAB>headRefName"
pr_info() {
  case "$2" in
    branch:*) printf -- '-\tBRANCH\t%s\n' "${2#branch:}" ;;
    *) gh pr view "$2" -R "$1" --json number,state,headRefName \
         --jq '[(.number|tostring), .state, .headRefName] | @tsv' ;;
  esac
}

ensure_worktree() { # <repo> <wt>
  local repo=$1 wt=$2
  git -C "$repo" fetch origin master >>"$LOG_DIR/fetch.log" 2>&1
  if [ -d "$wt" ]; then return 0; fi
  if git -C "$repo" ls-remote --exit-code origin "refs/heads/$BRANCH" >/dev/null 2>&1; then
    git -C "$repo" fetch origin "$BRANCH:refs/remotes/origin/$BRANCH" >>"$LOG_DIR/fetch.log" 2>&1
    git -C "$repo" worktree add -B "$BRANCH" "$wt" "origin/$BRANCH"
  else
    git -C "$repo" worktree add -b "$BRANCH" "$wt" origin/master
  fi
}

merge_one() { # <wt> <gh-repo> <entry>
  local wt=$1 gh=$2 entry=$3 num state head sha
  IFS=$'\t' read -r num state head < <(pr_info "$gh" "$entry")
  if [ "$state" != OPEN ] && [ "$state" != BRANCH ]; then log "skip #$num ($state)"; return 0; fi
  git -C "$wt" fetch origin "+refs/heads/$head:refs/remotes/origin/$head" >>"$LOG_DIR/fetch.log" 2>&1
  sha=$(git -C "$wt" rev-parse "origin/$head")
  if git -C "$wt" merge-base --is-ancestor "$sha" HEAD; then log "up to date #$num $head @ ${sha:0:10}"; return 0; fi
  log "merge #$num $head @ ${sha:0:10}"
  if git -C "$wt" -c rerere.enabled=true -c rerere.autoUpdate=true merge --no-ff --no-edit \
       -m "Merge #$num ($head @ ${sha:0:10}) into $BRANCH" "origin/$head" >>"$LOG_DIR/merge.log" 2>&1; then
    return 0
  fi
  if [ -z "$(git -C "$wt" diff --name-only --diff-filter=U)" ]; then
    log "rerere resolved every conflict for #$num; committing"
    git -C "$wt" -c rerere.enabled=true commit --no-edit >>"$LOG_DIR/merge.log" 2>&1
    return 0
  fi
  log "CONFLICT merging #$num $head; resolve in $wt, commit, record in $RECORD_DIR/INTEGRATION.md, re-run"
  git -C "$wt" diff --name-only --diff-filter=U >&2
  exit 2
}

record_merges() { # <wt> <gh-repo> <entries...> -> rewrites $RECORD_DIR/MERGES.tsv, commits when changed
  local wt=$1 gh=$2; shift 2
  local out="$wt/$RECORD_DIR/MERGES.tsv" entry num state head sha mc
  mkdir -p "$wt/$RECORD_DIR"
  {
    printf 'order\tpr\tstate\thead_branch\thead_sha\tmerge_commit\n'
    printf '0\t-\tBASE\tmaster\t%s\t-\n' "$(git -C "$wt" rev-parse origin/master)"
    local i=0
    for entry in "$@"; do
      i=$((i + 1))
      IFS=$'\t' read -r num state head < <(pr_info "$gh" "$entry")
      sha=$(git -C "$wt" rev-parse "origin/$head" 2>/dev/null || echo -)
      mc=$(git -C "$wt" log --merges --format=%H --grep="^Merge #$num ($head @" -1 HEAD 2>/dev/null)
      printf '%s\t%s\t%s\t%s\t%s\t%s\n' "$i" "$num" "$state" "$head" "$sha" "${mc:--}"
    done
  } >"$out"
  if [ -n "$(git -C "$wt" status --porcelain -- "$RECORD_DIR/MERGES.tsv")" ]; then
    git -C "$wt" add -- "$RECORD_DIR/MERGES.tsv"
    git -C "$wt" commit -q -m "m3: integration $STAMP: record merged PR heads (MERGES.tsv)" \
      -m "Co-Authored-By: Claude Opus 5.5 <noreply@anthropic.com>"
  fi
}

run_merge() { # <repo> <wt> <gh-repo> <entries...>
  local repo=$1 wt=$2 gh=$3; shift 3
  ensure_worktree "$repo" "$wt"
  [ "$(git -C "$wt" branch --show-current)" = "$BRANCH" ] || { log "$wt is not on $BRANCH"; exit 3; }
  if git -C "$wt" rev-parse -q --verify MERGE_HEAD >/dev/null; then log "$wt has a merge in progress"; exit 2; fi
  local e; for e in "$@"; do merge_one "$wt" "$gh" "$e"; done
  record_merges "$wt" "$gh" "$@"
}

run_status() { # <wt> <gh-repo> <entries...>
  local wt=$1 gh=$2; shift 2; local e num state head sha
  for e in "$@"; do
    IFS=$'\t' read -r num state head < <(pr_info "$gh" "$e")
    sha=$(git -C "$wt" rev-parse "origin/$head" 2>/dev/null || echo missing)
    if git -C "$wt" merge-base --is-ancestor "$sha" HEAD 2>/dev/null; then s=IN; else s=BEHIND; fi
    printf '#%s\t%s\t%s\t%s\t%s\n' "$num" "$state" "$head" "${sha:0:10}" "$s"
  done
}

open_prs() {
  local listed
  listed=" ${SWT_ORDER[*]} "
  gh pr list -R "$SWT_GH" --state open --json number,headRefName \
    --jq '.[] | [(.number|tostring), .headRefName] | @tsv' | while IFS=$'\t' read -r n h; do
      case "$listed" in *" $n "*) ;; *) printf 'SWT #%s %s NOT_IN_ORDER\n' "$n" "$h" ;; esac; done
  listed=" ${NEBULA_ORDER[*]} "
  gh pr list -R "$NEBULA_GH" --state open --json number,headRefName \
    --jq '.[] | [(.number|tostring), .headRefName] | @tsv' | while IFS=$'\t' read -r n h; do
      case "$listed" in *" $n "*) ;; *) printf 'Nebula #%s %s NOT_IN_ORDER\n' "$n" "$h" ;; esac; done
}

# ---------------------------------------------------------------- build stages
JDK=${M3_JDK:-C:/Program Files/Java/jdk-21}   # JDK 21; JAVA_HOME on this host points at 17
OUT=${OUT:-C:/m3-work-c/integ-build}
SWT_OFFICIAL_VERSION=${SWT_OFFICIAL_VERSION:-3.135.0}
SWT_OFFICIAL_JAR=${SWT_OFFICIAL_JAR:-$HOME/.m2/repository/org/eclipse/platform/org.eclipse.swt.win32.win32.x86_64/$SWT_OFFICIAL_VERSION/org.eclipse.swt.win32.win32.x86_64-$SWT_OFFICIAL_VERSION.jar}
SWT_JAR_NAME="org.eclipse.swt.win32.win32.x86_64-m3-${STAMP}.jar"
# Source folders of the win32 x86_64 fragment (binaries/org.eclipse.swt.win32.win32.x86_64/build.properties).
SWT_SRC_DIRS=("Eclipse SWT/win32" "Eclipse SWT/common" "Eclipse SWT PI/common" "Eclipse SWT PI/win32"
  "Eclipse SWT OLE Win32/win32" "Eclipse SWT Accessibility/win32" "Eclipse SWT Accessibility/common"
  "Eclipse SWT AWT/win32" "Eclipse SWT AWT/common" "Eclipse SWT Drag and Drop/win32" "Eclipse SWT Drag and Drop/common"
  "Eclipse SWT Printing/win32" "Eclipse SWT Printing/common" "Eclipse SWT Program/win32" "Eclipse SWT Program/common"
  "Eclipse SWT Custom Widgets/common" "Eclipse SWT Browser/common" "Eclipse SWT Browser/win32"
  "Eclipse SWT OpenGL/win32" "Eclipse SWT OpenGL/common")

memory_gate() { # refuse to start a JVM unless commit headroom >= 8 GB
  local kb
  kb=$(powershell -NoProfile -Command '(Get-CimInstance Win32_OperatingSystem).FreeVirtualMemory' | tr -d '\r ')
  log "memory gate: commit headroom $((kb / 1048576)) GB"
  [ "$kb" -ge 8388608 ] || { log "memory gate FAILED (< 8 GB commit headroom)"; exit 4; }
}

build_swt() {
  local b="$SWT_WT/bundles/org.eclipse.swt" w="$OUT/swt" lib ver
  rm -rf "$w"; mkdir -p "$w/classes" "$w/stage" "$OUT/dist"
  ( cd "$b" && find "${SWT_SRC_DIRS[@]}" -name '*.java' -type f ) | sed "s#^#$b/#; s#.*#\"&\"#" >"$w/sources.txt"
  log "build-swt: $(wc -l <"$w/sources.txt") sources; javac --release 21"
  memory_gate
  "$JDK/bin/javac" -J-Xmx1536m --release 21 -encoding UTF-8 -nowarn -proc:none -g \
    -d "$w/classes" @"$w/sources.txt" >"$w/javac.log" 2>&1 || { tail -40 "$w/javac.log" >&2; exit 5; }
  ( cd "$w/stage" && unzip -q -o "$SWT_OFFICIAL_JAR" )
  find "$w/stage" -name '*.class' -type f | sed "s#^$w/stage/##" | sort >"$w/official-classes.txt"
  find "$w/stage" -name '*.class' -type f -delete
  rm -f "$w/stage"/META-INF/*.SF "$w/stage"/META-INF/*.RSA "$w/stage"/META-INF/*.DSA \
        "$w/stage/META-INF/MANIFEST.MF" "$w/stage/.api_description"
  cp -r "$w/classes/." "$w/stage/"
  cp "$b/Eclipse SWT/common/version.txt" "$w/stage/version.txt"
  ver=$(sed -n 's/.*MAJOR_VERSION = \([0-9]*\);.*/\1/p; s/.*MINOR_VERSION = \([0-9]*\);.*/\1/p; s/.*REVISION = \([0-9]*\);.*/\1/p' \
        "$b/Eclipse SWT PI/common/org/eclipse/swt/internal/Library.java" | tr '\n' ' ' | awk '{printf "%s%03dr%s", $1, $2, $3}')
  for lib in "$w/stage"/swt-*win32-*.dll; do # official DLL bytes under the name the fork's Library.java loads
    local nn; nn=$(echo "$lib" | sed -E "s/-win32-[0-9]+r[0-9]+\.dll$/-win32-$ver.dll/")
    [ "$lib" = "$nn" ] || mv "$lib" "$nn"
  done
  if ls "$w/stage"/swt-*.dll | grep -v -- "-$ver.dll\$"; then log "build-swt: DLL rename incomplete"; exit 7; fi
  find "$w/stage" -name '*.class' -type f | sed "s#^$w/stage/##" | sort >"$w/fork-classes.txt"
  comm -23 "$w/official-classes.txt" "$w/fork-classes.txt" >"$w/official-only-classes.txt"
  write_swt_manifest "$w/MANIFEST.MF" "$w/classes"
  rm -f "$OUT/dist/$SWT_JAR_NAME"
  "$JDK/bin/jar" --create --file "$OUT/dist/$SWT_JAR_NAME" --manifest "$w/MANIFEST.MF" -C "$w/stage" .
  log "build-swt: $OUT/dist/$SWT_JAR_NAME ($(wc -l <"$w/fork-classes.txt") classes; natives $ver from official $SWT_OFFICIAL_VERSION)"
}

write_swt_manifest() { # <out> <classes-dir>: fork fragment manifest + every package that has classes
  local out=$1 classes=$2 pk
  {
    printf 'Manifest-Version: 1.0\nBundle-ManifestVersion: 2\n'
    printf 'Bundle-Name: Standard Widget Toolkit for Windows 64 (M3 integration %s)\n' "$STAMP"
    printf 'Bundle-SymbolicName: org.eclipse.swt.win32.win32.x86_64; singleton:=true\n'
    printf 'Bundle-Version: 3.137.0.m3-%s\n' "$STAMP"
    printf 'Bundle-Vendor: Eclipse.org (M3 integration build, unsigned)\n'
    printf 'Fragment-Host: org.eclipse.swt;bundle-version="[3.128.0,4.0.0)"\n'
    printf 'Bundle-RequiredExecutionEnvironment: JavaSE-21\n'
    printf 'Eclipse-PlatformFilter: (& (osgi.ws=win32) (osgi.os=win32) (osgi.arch=x86_64))\n'
    printf 'SWT-WS: win32\nSWT-OS: win32\nSWT-Arch: x86_64\n'
    printf 'Automatic-Module-Name: org.eclipse.swt.win32.win32.x86_64\n'
    printf 'Implementation-Version: m3/integration-%s %s\n' "$STAMP" "$(git -C "$SWT_WT" rev-parse HEAD)"
    printf 'Export-Package: '
    ( cd "$classes" && find . -name '*.class' -type f | sed 's#^\./##; s#/[^/]*$##' | sort -u | tr '/' '.' ) \
      | awk 'BEGIN{n=0} {p=$0; if (p ~ /^org\.eclipse\.swt\.internal/) p=p ";x-internal:=true";
             printf "%s%s", (n++ ? ",\n " : ""), p} END{printf "\n"}'
  } >"$out"
}

# Nebula: the bundles the 2026-10-13 PRs touch, in dependency order (compiled against the SWT build above).
NEBULA_BUNDLES=(grid/org.eclipse.nebula.widgets.grid
  xviewer/org.eclipse.nebula.widgets.xviewer.core xviewer/org.eclipse.nebula.widgets.xviewer
  ctree/org.eclipse.nebula.widgets.ctree ctreecombo/org.eclipse.nebula.widgets.ctreecombo
  tablecombo/org.eclipse.nebula.widgets.tablecombo calendarcombo/org.eclipse.nebula.widgets.calendarcombo
  compositetable/org.eclipse.nebula.widgets.compositetable
  visualization/org.eclipse.nebula.visualization.xygraph visualization/org.eclipse.nebula.visualization.widgets
  oscilloscope/org.eclipse.nebula.widgets.oscilloscope)
M2=${M2:-$HOME/.m2/repository}
DRAW2D_JAR=${DRAW2D_JAR:-C:/m3-work-c/showcase-build/platform-deps/org.eclipse.draw2d_3.23.100.202608071137.jar}
NEBULA_DEPS=(platform/org.eclipse.jface/3.40.0 platform/org.eclipse.core.commands/3.13.0
  platform/org.eclipse.equinox.common/3.21.0 platform/org.eclipse.ui.workbench/3.140.0
  platform/org.eclipse.ui.forms/3.14.200 platform/org.eclipse.core.runtime/3.35.0
  platform/org.eclipse.core.jobs/3.15.900 platform/org.eclipse.osgi/3.24.300
  platform/org.eclipse.equinox.registry/3.12.600 platform/org.eclipse.core.databinding.observable/1.13.500
  platform/org.eclipse.core.expressions/3.9.700 platform/org.eclipse.equinox.preferences/3.12.100)

bp_get() { # <build.properties> <key-regex>: comma list of a (continued) property, one item per line
  tr -d '\r' <"$1" | awk -v k="$2" '
    { line = cont ? line $0 : $0; cont = (substr(line, length(line), 1) == "\\")
      if (cont) { line = substr(line, 1, length(line) - 1); next }
      if (line ~ "^" k "[ \t]*=") { sub("^" k "[ \t]*=[ \t]*", "", line); n = split(line, a, ",")
        for (i = 1; i <= n; i++) { gsub(/^[ \t]+|[ \t]+$/, "", a[i]); if (a[i] != "") print a[i] } } }'
}

nebula_cp() {
  local cp e
  cp="$OUT/dist/$SWT_JAR_NAME"
  for e in "${NEBULA_DEPS[@]}"; do
    local g=${e%%/*} rest=${e#*/}; local a=${rest%/*} v=${rest##*/}
    cp="$cp;$(cygpath -m "$M2/org/eclipse/$g/$a/$v/$a-$v.jar")"
  done
  cp="$cp;$(cygpath -m "$DRAW2D_JAR")"
  for e in "$OUT"/nebula/*/classes; do [ -d "$e" ] && cp="$cp;$e"; done
  printf '%s' "$cp"
}

build_nebula_bundle() { # <widget>/<bundle>
  local rel=$1 d="$NEBULA_WT/widgets/$1" name=${1#*/} w s inc ver base jarname
  w="$OUT/nebula/$name"; rm -rf "$w"; mkdir -p "$w/classes"
  mapfile -t srcs < <(bp_get "$d/build.properties" 'source[.][.]')
  for s in "${srcs[@]}"; do find "$d/${s%/}" -name '*.java' -type f; done | sed 's#.*#"&"#' >"$w/sources.txt"
  "$JDK/bin/javac" -J-Xmx1g --release 21 -encoding UTF-8 -nowarn -proc:none -g -cp "$(nebula_cp)" \
    -d "$w/classes" @"$w/sources.txt" >"$w/javac.log" 2>&1 || { log "javac FAILED $name"; tail -30 "$w/javac.log" >&2; return 5; }
  for s in "${srcs[@]}"; do ( cd "$d/${s%/}" && find . -type f ! -name '*.java' ! -name 'package.html' | while read -r f; do
      mkdir -p "$w/classes/$(dirname "$f")" && cp "$f" "$w/classes/$f"; done ); done
  while read -r inc; do # only plain relative names inside the bundle (no globs, no '..', no absolute paths)
    inc=${inc%/}
    case "$inc" in ''|.|META-INF) continue ;; esac
    if [[ ! "$inc" =~ ^[A-Za-z0-9_][A-Za-z0-9_./-]*$ || "$inc" == *..* ]]; then
      log "skip bin.includes entry '$inc' in $name"; continue
    fi
    [ -e "$d/$inc" ] && cp -r "$d/$inc" "$w/classes/"
  done < <(bp_get "$d/build.properties" 'bin[.]includes')
  ver=$(tr -d '\r' <"$d/META-INF/MANIFEST.MF" | sed -n 's/^Bundle-Version: *//p'); base=${ver%.qualifier}
  { tr -d '\r' <"$d/META-INF/MANIFEST.MF" | sed "s/^Bundle-Version: .*/Bundle-Version: $base.m3-$STAMP/" | sed '/^$/d'
    printf 'Implementation-Version: m3/integration-%s %s\n' "$STAMP" "$(git -C "$NEBULA_WT" rev-parse HEAD)"; } >"$w/MANIFEST.MF"
  jarname="$name-$base-m3-$STAMP.jar"; rm -f "$OUT/dist/$jarname"
  "$JDK/bin/jar" --create --file "$OUT/dist/$jarname" --manifest "$w/MANIFEST.MF" -C "$w/classes" .
  log "build-nebula: $jarname ($(wc -l <"$w/sources.txt") sources)"
}

build_nebula() {
  [ -f "$OUT/dist/$SWT_JAR_NAME" ] || { log "build-nebula needs $OUT/dist/$SWT_JAR_NAME (run build-swt)"; exit 6; }
  rm -rf "$OUT/nebula"; mkdir -p "$OUT/nebula"
  memory_gate
  local b rc=0; for b in "${NEBULA_BUNDLES[@]}"; do build_nebula_bundle "$b" || rc=5; done
  return $rc
}

checksums() { ( cd "$OUT/dist" && sha256sum *.jar >SHA256SUMS.txt && cat SHA256SUMS.txt ); }

cmd=${1:-merge}; which=${2:-all}
case "$cmd" in
  merge)
    [ "$which" = nebula ] || run_merge "$SWT_REPO" "$SWT_WT" "$SWT_GH" "${SWT_ORDER[@]}"
    [ "$which" = swt ]    || run_merge "$NEBULA_REPO" "$NEBULA_WT" "$NEBULA_GH" "${NEBULA_ORDER[@]}" ;;
  push)
    [ "$which" = nebula ] || git -C "$SWT_WT" push -u origin "$BRANCH"
    [ "$which" = swt ]    || git -C "$NEBULA_WT" push -u origin "$BRANCH" ;;
  status)
    [ "$which" = nebula ] || run_status "$SWT_WT" "$SWT_GH" "${SWT_ORDER[@]}"
    [ "$which" = swt ]    || run_status "$NEBULA_WT" "$NEBULA_GH" "${NEBULA_ORDER[@]}" ;;
  open-prs) open_prs ;;
  build-swt) build_swt ;;
  build-nebula) build_nebula ;;
  checksums) checksums ;;
  *) sed -n '2,30p' "$0"; exit 64 ;;
esac
