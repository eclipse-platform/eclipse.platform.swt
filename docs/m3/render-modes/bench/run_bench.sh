#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
#
# Cross-mode benchmark driver (docs/m3/RENDER_MODES.md, leaf R2). One JVM per widget x mode x operation on the
# invisible test desktop; each JVM appends one row to the TSV, a JVM that exceeds the budget gets a timeout row.
# Run it under the desktop lock:
#   bash C:/m3-superset/tools/ui_lock.sh <agent> bash run_bench.sh <swt-jar> <tests-dir> <out.tsv>
# Environment: MODES (default "official viewport original custom m3 gpu"), WIDGETS (default "Tree Table List"),
# OPS (default all), BUDGET seconds per JVM (default 90), EXTRA_CP (Skija jars for the m3 and gpu modes).
JAR=$1; TESTS=$2; TSV=$3
MODES=${MODES:-official viewport original custom m3 gpu}
WIDGETS=${WIDGETS:-Tree Table List}
OPS=${OPS:-create setItemCount scrollPage scroll10k paint selectAll expand10k collapse10k selectFar}
BUDGET=${BUDGET:-90}
OFFICIAL=$(cygpath -w ~/.m2/repository/org/eclipse/platform/org.eclipse.swt.win32.win32.x86_64/3.135.0/org.eclipse.swt.win32.win32.x86_64-3.135.0.jar)
LOGS=$(dirname "$TSV")/logs; mkdir -p "$LOGS"
WTSV=$(cygpath -w "$TSV")
for widget in $WIDGETS; do
  for mode in $MODES; do
    for op in $OPS; do
      if [ "$mode" = official ]; then CP="$(cygpath -w "$TESTS");$OFFICIAL"; PROPS=""
      else CP="$(cygpath -w "$TESTS");$(cygpath -w "$JAR")${EXTRA_CP:+;$EXTRA_CP}"; PROPS="-Dswt.widget.mode=$mode"; fi
      log="$LOGS/$widget-$mode-$op.log"
      bash C:/m3-superset/tools/hidden_java.sh "$BUDGET" "$log" -Xmx1g $PROPS -cp "$CP" org.eclipse.swt.tests.junit.RenderModeBenchmark "$widget" "$op" "$WTSV"
      rc=$?
      if [ $rc = 124 ]; then
        printf '%s\t%s\t%s\ttimeout (> %s s per JVM)\t0\t-\t-\t-\t-\t-\t-\t%s\n' "$widget" "$mode" "$op" "$BUDGET" "$(date)" >> "$TSV"
      elif [ $rc != 0 ]; then
        printf '%s\t%s\t%s\texit %s (see logs/%s)\t0\t-\t-\t-\t-\t-\t-\t%s\n' "$widget" "$mode" "$op" "$rc" "$(basename "$log")" "$(date)" >> "$TSV"
      fi
      echo "$widget $mode $op rc=$rc"
    done
  done
done
