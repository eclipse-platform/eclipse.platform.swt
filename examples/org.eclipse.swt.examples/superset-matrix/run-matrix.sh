#!/usr/bin/env bash
# Viewport Test Matrix: one widget, one scale, one JVM at a time.
#
# usage: run-matrix.sh <swt-jar> <nebula-jars-dir> <label> [concept-or-key-regex]
#
#   <swt-jar>          SWT jar under test (baseline: Maven Central 3.135.0; a PR: its built jar)
#   <nebula-jars-dir>  directory of Nebula jars (build-nebula.sh output); jars in its deps/
#                      subdirectory (jface, draw2d, ...) are added too. Use an empty dir for SWT only.
#   <label>            run label, e.g. baseline-swt3.135.0 or swt-pr117
#   [regex]            optional: only concepts / demo keys (concept__Class) matching it
#
# env: MATRIX_OUT (default ./matrix-out), JAVA_HOME, MEM_FREE_MB (4096), COMMIT_FREE_MB (8192),
#      CELL_TIMEOUT (120 s; 1M and above get 2x), NEBULA_COMMIT (recorded in meta.tsv)
#
# out: $MATRIX_OUT/<label>/VIEWPORT_TEST_MATRIX.tsv, screenshots/<label>/*.png, logs/, meta.tsv
set -u
HERE=$(cd "$(dirname "$0")" && pwd)
SRC=$(cd "$HERE/../src" && pwd)
SWT_JAR=$1; NEB=$2; LABEL=$3; FILTER=${4:-}
OUT=${MATRIX_OUT:-$PWD/matrix-out}/$LABEL
JAVA_BIN=${JAVA_HOME:+$JAVA_HOME/bin/}
MEM_FREE_MB=${MEM_FREE_MB:-4096}; COMMIT_FREE_MB=${COMMIT_FREE_MB:-8192}; CELL_TIMEOUT=${CELL_TIMEOUT:-120}
mkdir -p "$OUT/classes" "$OUT/logs" "$OUT/screenshots/$LABEL"
TSV=$OUT/VIEWPORT_TEST_MATRIX.tsv
SEP=';'; case "$(uname -s)" in Linux*|Darwin*) SEP=':';; esac
wpath() { if command -v cygpath >/dev/null; then cygpath -w "$1"; else echo "$1"; fi; }
log() { echo "$(date +%H:%M:%S) $*" | tee -a "$OUT/run.log"; }

free_mb() {  # prints "<free physical MB> <free commit MB>"
  if command -v powershell.exe >/dev/null; then
    powershell.exe -NoProfile -Command '$o=Get-CimInstance Win32_OperatingSystem; "{0} {1}" -f [int]($o.FreePhysicalMemory/1024),[int]($o.FreeVirtualMemory/1024)' | tr -d '\r'
  else awk '/MemAvailable/{a=int($2/1024)} /CommitLimit/{l=$2} /Committed_AS/{c=$2} END{print a, int((l-c)/1024)}' /proc/meminfo; fi
}
gate() {  # memory gate before every JVM: wait until both thresholds hold
  local f c
  while true; do
    read -r f c < <(free_mb)
    if [ "${f:-0}" -ge "$MEM_FREE_MB" ] && [ "${c:-0}" -ge "$COMMIT_FREE_MB" ]; then return 0; fi
    log "memory gate: free=${f}MB commit=${c}MB, waiting 30 s"; sleep 30
  done
}

# 1. compile the showcase package against the SWT jar under test
gate
find "$SRC/org/eclipse/swt/examples/superset" -name '*.java' | while IFS= read -r f; do wpath "$f"; done > "$OUT/sources.lst"
"${JAVA_BIN}javac" -J-Xmx512m -nowarn -encoding UTF-8 --release 21 -cp "$(wpath "$SWT_JAR")" \
  -d "$(wpath "$OUT/classes")" @"$(wpath "$OUT/sources.lst")" > "$OUT/logs/javac.log" 2>&1 || { log "javac failed"; cat "$OUT/logs/javac.log"; exit 1; }
cp "$SRC"/org/eclipse/swt/examples/superset/*.tsv "$OUT/classes/org/eclipse/swt/examples/superset/"

# 2. optional native handle probe (Windows)
PROBE=""
CSC=/c/Windows/Microsoft.NET/Framework64/v4.0.30319/csc.exe
if [ -x "$CSC" ]; then
  "$CSC" -nologo -out:"$(wpath "$OUT/HandleProbe.exe")" "$(wpath "$HERE/HandleProbe.cs")" > "$OUT/logs/csc.log" 2>&1 && PROBE=$(wpath "$OUT/HandleProbe.exe")
fi

CP="$(wpath "$OUT/classes")$SEP$(wpath "$SWT_JAR")$SEP$(wpath "$NEB")/*$SEP$(wpath "$NEB/deps")/*"
MAIN=org.eclipse.swt.examples.superset.MatrixRunner
{
  printf 'key\tvalue\n'
  printf 'label\t%s\n' "$LABEL"
  printf 'swt_jar\t%s\n' "$(basename "$SWT_JAR")"
  printf 'swt_jar_sha256\t%s\n' "$(sha256sum "$SWT_JAR" | cut -d' ' -f1)"
  printf 'nebula_dir\t%s\n' "$NEB"
  printf 'nebula_commit\t%s\n' "${NEBULA_COMMIT:-unrecorded}"
  printf 'java\t%s\n' "$("${JAVA_BIN}java" -version 2>&1 | head -1)"
  printf 'os\t%s\n' "$(uname -srm)"
  printf 'started\t%s\n' "$(date -u +%Y-%m-%dT%H:%M:%SZ)"
  printf 'frames\t250 (125 to the end, 125 back)\nscreenshots\tControl.print into an Image, downscaled to <=480 px PNG\n'
} > "$OUT/meta.tsv"

# 3. plan: static rows go straight to the TSV, runnable cells are queued
gate
"${JAVA_BIN}java" -Xmx256m -cp "$CP" $MAIN --plan ${FILTER:+"$FILTER"} > "$OUT/plan.txt" 2> "$OUT/logs/plan.err" || { log "plan failed"; cat "$OUT/logs/plan.err"; exit 1; }
[ -f "$TSV" ] || printf 'concept\twidget_class\tscale\tfirst_paint_ms\tframe_p50\tframe_p95\tframe_max\tmaterialized\tbound\twithin_bound\theap_mb\tprivate_mb\thandles\thidden_paints\thidden_layouts\tscreenshot_top\tscreenshot_end\tresult\trepro_api_equal\trepro_pixel_equal\trepro_states_tested\tnotes\n' > "$TSV"
grep '^ROW' "$OUT/plan.txt" | cut -f2- | while IFS= read -r row; do
  key=$(printf '%s' "$row" | cut -f1-3)
  grep -qF "$key	" "$TSV" || printf '%s\n' "$row" >> "$TSV"
done
log "plan: $(grep -c '^PLAN' "$OUT/plan.txt") cells to run, $(grep -c '^ROW' "$OUT/plan.txt") static rows"

# 4. run each cell in its own JVM, under timeout, after the memory gate
grep '^PLAN' "$OUT/plan.txt" | while IFS=$'\t' read -r _ concept widget scale xmx; do
  s=$scale; [ "$s" = 0 ] && s=-
  if grep -qP "^\Q$concept\E\t\Q$widget\E\t$s\t" "$TSV"; then continue; fi   # resume: cell already recorded
  if [ "$scale" = 10000000 ]; then  # 10M: project private memory from the 1M row
    p1=$(awk -F'\t' -v c="$concept" -v w="$widget" '$1==c && $2==w && $3=="1000000"{print $12}' "$TSV" | tail -1)
    read -r f _ < <(free_mb)
    if [ -n "$p1" ] && [ "$p1" != "-" ] && awk -v p="$p1" -v f="$f" 'BEGIN{exit !(p*10 > f-1024)}'; then
      printf '%s\t%s\t%s\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\tNOT_RUN\t-\t-\t-\tprojected private memory %.0f MB (10x the 1M run) exceeds free memory %s MB - 1 GB\n' \
        "$concept" "$widget" "$scale" "$(awk -v p="$p1" 'BEGIN{print p*10}')" "$f" >> "$TSV"
      continue
    fi
  fi
  t=$CELL_TIMEOUT; [ "$scale" -ge 1000000 ] && t=$((CELL_TIMEOUT * 2))
  gate
  cell="$concept.${widget##*.}.$scale"
  log "run $cell xmx=${xmx}m timeout=${t}s"
  timeout $((t + 20)) "${JAVA_BIN}java" -Xmx${xmx}m -Dsuperset.handleProbe="$PROBE" -cp "$CP" $MAIN \
    --run "$concept" "$widget" "$scale" "$(wpath "$OUT")" "$LABEL" "$t" > "$OUT/logs/$cell.log" 2>&1
  rc=$?
  row=$(grep '^ROW' "$OUT/logs/$cell.log" | tail -1 | cut -f2-)
  if [ -z "$row" ]; then
    why=$(grep -m1 -E 'Exception|Error|SIGSEGV|EXCEPTION_' "$OUT/logs/$cell.log" | tr '\t' ' ' | cut -c1-300)
    row=$(printf '%s\t%s\t%s\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\t-\tFAIL\t-\t-\t-\texit %s (124=timeout); %s' "$concept" "$widget" "$s" "$rc" "${why:-no output}")
  fi
  printf '%s\n' "$row" >> "$TSV"
  log "done $cell rc=$rc $(printf '%s' "$row" | cut -f18)"
done
log "matrix complete: $(tail -n +2 "$TSV" | cut -f18 | sort | uniq -c | tr '\n' ' ')"
