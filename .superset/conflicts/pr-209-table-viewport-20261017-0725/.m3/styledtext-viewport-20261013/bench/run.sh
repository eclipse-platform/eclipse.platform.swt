#!/bin/sh
# Runs one StyledTextViewportBench measurement in its own JVM (one JVM at a time).
# usage: run.sh <classesDir> <label> <sizeMiB> <fixed|wrap> <xmx> [idleTimeoutMs] [extra JVM args...]
# env:   SWT_JAR (unsigned SWT win32 jar), JAVA_HOME (JDK 21), BENCH_OUT (TSV to append to)
set -eu
classes=$1; label=$2; size=$3; mode=$4; xmx=$5; timeout=${6:-60000}
shift 5; [ $# -gt 0 ] && shift
here=$(cd "$(dirname "$0")" && pwd)
out=${BENCH_OUT:-$here/results.tsv}
bin=$here/target/bench-classes
mkdir -p "$bin"
w() { cygpath -w "$1" 2>/dev/null || echo "$1"; }
"$JAVA_HOME/bin/javac" -nowarn -d "$(w "$bin")" -cp "$(w "$SWT_JAR")" "$(w "$here/StyledTextViewportBench.java")" "$(w "$here/org/eclipse/swt/custom/StyledTextViewportProbe.java")"
hdr=""
[ -f "$out" ] || hdr=header
"$JAVA_HOME/bin/java" -Xmx"$xmx" -Xms256m -XX:+UseG1GC "$@" \
	-cp "$(w "$classes");$(w "$bin");$(w "$SWT_JAR")" StyledTextViewportBench "$label" "$size" "$mode" "$timeout" $hdr >> "$out"
tail -1 "$out"
