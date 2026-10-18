#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
#
# TableBench of the official 3.135.0 jar and of <jar> in the original and viewport modes, interleaved per round, on
# the invisible test desktop. Run it under the desktop lock:
#   STYLE=virtual|plain ROWS=<n> bash C:/m3-superset/tools/ui_lock.sh <agent> bash table_bench.sh <jar> <work-dir> <out.tsv> [rounds]
# Each line of <out.tsv>: round, style, mode, operation, repetitions, min, median and max ms.
JAR=$1; W=$2; OUT=$3; ROUNDS=${4:-3}
STYLE=${STYLE:-virtual}; ROWS=${ROWS:-1000000}
OPS=${OPS:-scrollPage selectAll scroll10k paint selectFar}
JDK="C:/Program Files/Java/jdk-21"
OFFICIAL=$(cygpath -m ~/.m2/repository/org/eclipse/platform/org.eclipse.swt.win32.win32.x86_64/3.135.0/org.eclipse.swt.win32.win32.x86_64-3.135.0.jar)
H=C:/m3-superset/tools/hidden_java.sh
mkdir -p $W/cls
cp "$(dirname "$0")/TableBench.java.txt" $W/TableBench.java
"$JDK/bin/javac" --release 21 -cp "$OFFICIAL" -d $W/cls $W/TableBench.java || exit 1
for r in $(seq 1 $ROUNDS); do
  for m in official original viewport; do
    if [ $m = official ]; then CP="$W/cls;$OFFICIAL"; X=""; else CP="$W/cls;$JAR"; X="-Dswt.widget.mode=$m"; fi
    bash $H 300 $W/run-$m-$r.log -Xmx1g $X -cp "$CP" TableBench $STYLE $ROWS 40 $OPS
    grep -v "^WARNING" $W/run-$m-$r.log | grep "^$STYLE" | sed "s/^/$r\t/" >> $OUT
  done
done
