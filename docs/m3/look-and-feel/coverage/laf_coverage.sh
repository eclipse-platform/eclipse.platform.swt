#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# JaCoCo coverage of org.eclipse.swt.internal.laf (brief rule 15): runs the look and feel suite on the hidden
# desktop with the JaCoCo agent, then writes the CSV and HTML report next to this script (coverage/report).
# Run the jar build first (build_and_tests.sh <wt> <out> <stamp>). Usage: laf_coverage.sh <worktree> <out-dir> <stamp>
WT=$1; OUT=$2; STAMP=$3
JDK="C:/Program Files/Java/jdk-21"
M2=$(cygpath -w "$HOME/.m2/repository")
J="$M2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar"
AGENT="$M2/org/jacoco/org.jacoco.agent/0.8.11/org.jacoco.agent-0.8.11-runtime.jar"
CLI="$M2/org/jacoco/org.jacoco.cli/0.8.15/org.jacoco.cli-0.8.15-nodeps.jar"
JAR="$OUT/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar"
COV="$OUT/laf-coverage"; DOC="$WT/docs/m3/look-and-feel/coverage"
rm -rf "$COV" && mkdir -p "$COV/classes"
(cd "$COV/classes" && "$JDK/bin/jar" xf "$(cygpath -w "$JAR")" org/eclipse/swt/internal/laf)
bash C:/m3-superset/tools/ui_lock.sh swt-allwidgets bash C:/m3-superset/tools/hidden_java.sh 300 "$COV/run.log" \
  -javaagent:"$AGENT"=destfile="$COV/laf.exec",includes="org.eclipse.swt.internal.laf.*" \
  -jar "$J" execute -cp "$OUT/tests;$JAR" \
  --select-class org.eclipse.swt.tests.junit.Test_org_eclipse_swt_internal_laf_LookAndFeel --disable-banner --details=summary
grep -E "tests (successful|failed|found)" "$COV/run.log"
"$JDK/bin/java" -jar "$CLI" report "$COV/laf.exec" --classfiles "$COV/classes" \
  --sourcefiles "$WT/bundles/org.eclipse.swt/Eclipse SWT/common" \
  --csv "$DOC/jacoco-laf.csv" --html "$COV/report" --name "org.eclipse.swt.internal.laf ($STAMP)"
awk -F, 'NR > 1 {
	il = $8 + $9; ib = $6 + $7;
	printf "%-22s lines %4d/%-4d (%5.1f%%)  branches %3d/%-3d (%5.1f%%)\n", $3, $9, il, il ? 100 * $9 / il : 100, $7, ib, ib ? 100 * $7 / ib : 100;
	ml += $8; cl += $9; mb += $6; cb += $7;
} END {
	printf "%-22s lines %4d/%-4d (%5.1f%%)  branches %3d/%-3d (%5.1f%%)\n", "TOTAL", cl, ml + cl, 100 * cl / (ml + cl), cb, mb + cb, 100 * cb / (mb + cb);
}' "$DOC/jacoco-laf.csv"
