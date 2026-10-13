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
LOCK="$OUT/.run.lock"   # one run per output label: a second invocation exits instead of interleaving JVMs
if ! mkdir "$LOCK" 2>/dev/null; then
  owner=$(cat "$LOCK/pid" 2>/dev/null)
  if [ -n "$owner" ] && kill -0 "$owner" 2>/dev/null; then echo "run-matrix: pid $owner already runs $OUT" >&2; exit 3; fi
fi
echo $$ > "$LOCK/pid"
# Desktop lock shared by every agent: one UI run (windows, Control.print, display capture) at a time.
# Taken per cell and released after it, so other agents interleave between cells.
UI_LOCK=${UI_LOCK:-/c/m3-work-c/ui-test.lock}
ui_lock() {
  until mkdir "$UI_LOCK" 2>/dev/null; do
    log "ui lock held by $(cat "$UI_LOCK/owner.txt" 2>/dev/null || echo unknown), retry in 30 s"; sleep 30
  done
  printf 'superset-matrix pid=%s label=%s since=%s\n' "$$" "$LABEL" "$(date -u +%Y-%m-%dT%H:%M:%SZ)" > "$UI_LOCK/owner.txt"
}
ui_unlock() {  # only our own lock
  grep -q "superset-matrix pid=$$ " "$UI_LOCK/owner.txt" 2>/dev/null && rm -rf "$UI_LOCK"
}
trap 'ui_unlock; rm -rf "$LOCK"' EXIT
trap 'exit 143' TERM INT
SEP=';'; case "$(uname -s)" in Linux*|Darwin*) SEP=':';; esac
wpath() { if command -v cygpath >/dev/null; then cygpath -w "$1"; else echo "$1"; fi; }
log() { echo "$(date +%H:%M:%S) $*" | tee -a "$OUT/run.log"; }

free_mb() {  # prints "<free physical MB> <free commit MB>"
  if command -v powershell.exe >/dev/null; then
    powershell.exe -NoProfile -Command '$o=Get-CimInstance Win32_OperatingSystem; "{0} {1}" -f [int]($o.FreePhysicalMemory/1024),[int]($o.FreeVirtualMemory/1024)' < /dev/null | tr -d '\r'
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
[ -f "$TSV" ] || "${JAVA_BIN}java" -Xmx64m -cp "$CP" $MAIN --header > "$TSV"
NCOL=$(head -1 "$TSV" | awk -F'	' '{print NF}')
blank_row() {  # concept widget scale result notes -> one row, every metric "-"
  awk -v OFS='	' -v n="$NCOL" -v c="$1" -v w="$2" -v s="$3" -v r="$4" -v t="$5" \
    'BEGIN{for(i=1;i<=n;i++)f[i]="-"; f[1]=c; f[2]=w; f[3]=s; f[18]=r; f[n]=t; for(i=1;i<=n;i++) printf "%s%s", f[i], (i<n?OFS:ORS)}'
}
grep '^ROW' "$OUT/plan.txt" | cut -f2- | while IFS= read -r row; do
  key=$(printf '%s' "$row" | cut -f1-3)
  grep -qF "$key	" "$TSV" || printf '%s\n' "$row" >> "$TSV"
done
log "plan: $(grep -c '^PLAN' "$OUT/plan.txt") cells to run, $(grep -c '^ROW' "$OUT/plan.txt") static rows"

# 4. run each cell in its own JVM, under timeout, after the memory gate
grep '^PLAN' "$OUT/plan.txt" | while IFS=$'\t' read -r _ concept widget scale xmx; do
  s=$scale; [ "$s" = 0 ] && s=-
  if awk -F'\t' -v c="$concept" -v w="$widget" -v s="$s" '$1==c && $2==w && $3==s{f=1} END{exit !f}' "$TSV"; then continue; fi   # resume
  if [ "$scale" -gt 1000000 ]; then
    # above 1M: project native (non-heap) private memory linearly from the 1K and 1M rows,
    # add this cell's -Xmx, and skip the cell when that would not fit in free memory minus 1 GB
    read -r p0 h0 < <(awk -F'\t' -v c="$concept" -v w="$widget" '$1==c && $2==w && $3=="1000"{print $12, $11}' "$TSV" | tail -1)
    read -r p1 h1 < <(awk -F'\t' -v c="$concept" -v w="$widget" '$1==c && $2==w && $3=="1000000"{print $12, $11}' "$TSV" | tail -1)
    read -r f _ < <(free_mb)
    if [ -n "${p1:-}" ] && [ "$p1" != "-" ] && [ -n "${p0:-}" ] && [ "$p0" != "-" ] && [ "${h1:-}" != "-" ] && [ "${h0:-}" != "-" ]; then
      proj=$(awk -v a="$p0" -v b="$p1" -v ha="$h0" -v hb="$h1" -v s="$scale" -v x="$xmx" \
        'BEGIN{d=(b-a)-(hb-ha); if (d<0) d=0; printf "%.0f", a + d * (s/1000000.0) + x}')
      if [ "$proj" -gt $((f - 1024)) ]; then
        blank_row "$concept" "$widget" "$scale" NOT_RUN "projected private memory $proj MB (native slope from the 1K and 1M runs plus -Xmx${xmx}m) exceeds free memory $f MB - 1 GB" >> "$TSV"
        continue
      fi
    fi
  fi
  t=$CELL_TIMEOUT; [ "$scale" -ge 1000000 ] && t=$((CELL_TIMEOUT * 2)); [ "$scale" -ge 100000000 ] && t=$((CELL_TIMEOUT * 3))
  gate
  ui_lock
  cell="$concept.${widget##*.}.$scale"
  log "run $cell xmx=${xmx}m timeout=${t}s"
  timeout $((t + 20)) "${JAVA_BIN}java" -Xmx${xmx}m -Dsuperset.handleProbe="$PROBE" -cp "$CP" $MAIN \
    --run "$concept" "$widget" "$scale" "$(wpath "$OUT")" "$LABEL" "$t" < /dev/null > "$OUT/logs/$cell.log" 2>&1
  rc=$?
  ui_unlock
  row=$(grep '^ROW' "$OUT/logs/$cell.log" | tail -1 | cut -f2-)
  if [ -z "$row" ]; then
    why=$(grep -m1 -E 'Exception|Error|SIGSEGV|EXCEPTION_' "$OUT/logs/$cell.log" | tr '\t' ' ' | cut -c1-300)
    row=$(blank_row "$concept" "$widget" "$s" FAIL "exit $rc (124=timeout); ${why:-no output}")
  fi
  printf '%s\n' "$row" >> "$TSV"
  log "done $cell rc=$rc $(printf '%s' "$row" | cut -f18)"
done
# 5. cross-scale columns (size_independence, memory_flatness), computed in Java
gate
"${JAVA_BIN}java" -Xmx256m -cp "$CP" $MAIN --postprocess "$(wpath "$TSV")" || log "postprocess failed"

# 6. independent persisted-pixel acceptance.  The Java runner is the producer;
# OpenCV is a separate observer and must reject stale/blank/forged evidence.
if [ "${MATRIX_OPENCV:-1}" != "0" ]; then
  if [ -n "${MATRIX_PYTHON:-}" ]; then
    PYTHON_BIN="$MATRIX_PYTHON"
  elif command -v python3 >/dev/null 2>&1; then
    PYTHON_BIN=python3
  elif command -v python >/dev/null 2>&1; then
    PYTHON_BIN=python
  else
    log "OpenCV required but neither python3 nor python is available"
    exit 1
  fi
  VISION_PY=${MATRIX_OPENCV_PYTHON:-"$OUT/.vision/bin/python"}
  if [ ! -x "$VISION_PY" ]; then
    "$PYTHON_BIN" -m venv "$OUT/.vision"
    "$VISION_PY" -m pip install --disable-pip-version-check --quiet \
      opencv-python-headless==4.12.0.88 numpy==2.2.6
  fi
  "$VISION_PY" "$HERE/verify-superset-matrix.py" --selftest
  "$VISION_PY" "$HERE/verify-superset-matrix.py" "$OUT"
fi
log "matrix complete: $(tail -n +2 "$TSV" | cut -f18 | sort | uniq -c | tr '\n' ' ')"

# 7. optional: the showcase tour (every category, concept and widget tab printed), TOUR=1
if [ -n "${TOUR:-}" ]; then
  gate
  ui_lock
  timeout 1800 "${JAVA_BIN}java" -Xmx768m -cp "$CP" org.eclipse.swt.examples.superset.SupersetShowcase --tour "$(wpath "$OUT")" \
    < /dev/null > "$OUT/logs/tour.log" 2>&1
  rc=$?
  ui_unlock
  log "tour rc=$rc $(tail -1 "$OUT/logs/tour.log")"
fi
