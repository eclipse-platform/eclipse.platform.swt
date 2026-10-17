#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
#
# JaCoCo coverage of the Table viewport classes (rule 15 of the viewport agent brief) over the Table suites in both
# modes, the acceptance tests, the differential of the default mode against the official recording and the
# differential recording of the original mode. Every run is also a gate: its JUnit summary is printed, and the
# original recording must equal the official one line for line (rule 12). Run it under the desktop lock:
#   bash C:/m3-superset/tools/ui_lock.sh <agent> bash table_coverage.sh <out-dir> <stamp> <official-recording-dir>
# <out-dir> holds tests/ and dist/ of a build (build_and_tests.sh); results go to <out-dir>/coverage.
O=$1; STAMP=$2; OFFICIAL_REC=$3
export JAVA_HOME="C:/Program Files/Java/jdk-21"
M=~/.m2/repository/org/jacoco
AGENT=$(cygpath -w $M/org.jacoco.agent/0.8.15/org.jacoco.agent-0.8.15-runtime.jar)
CLI=$(cygpath -w $M/org.jacoco.cli/0.8.15/org.jacoco.cli-0.8.15-nodeps.jar)
CS=$(cygpath -w ~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
JAR=$(cygpath -w $O/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar)
CP="$(cygpath -w $O/tests);$JAR"
H=C:/m3-superset/tools/hidden_java.sh
C=$O/coverage; mkdir -p $C $C/recording-original
EXEC=$(cygpath -w $C/table.exec)
INC='org.eclipse.swt.widgets.Table*:org.eclipse.swt.widgets.VirtualListWidth*:org.eclipse.swt.widgets.VirtualCellLanes*:org.eclipse.swt.widgets.VirtualBitLanes*:org.eclipse.swt.widgets.ScrollBar*'
JA="-javaagent:$AGENT=destfile=$EXEC,append=true,includes=$INC"
DEFECTS=$(cygpath -m "$(dirname "$0")")/UPSTREAM_DEFECTS.tsv
XA="--enable-preview --enable-native-access=ALL-UNNAMED"
T=org.eclipse.swt.tests.junit
SUITES="--select-class $T.Test_org_eclipse_swt_widgets_Table --select-class $T.Test_org_eclipse_swt_widgets_TableItem --select-class $T.Test_org_eclipse_swt_widgets_TableColumn --select-class $T.Test_org_eclipse_swt_widgets_Table_Viewport --disable-banner --details=tree"
DIFF="--select-class $T.Test_org_eclipse_swt_widgets_Table_ViewportDifferential --disable-banner --details=tree"
bash $H 1500 $C/suites-viewport.log -Xmx1g "$JA" -jar "$CS" execute -cp "$CP" $SUITES
bash $H 1500 $C/suites-original.log -Xmx1g "$JA" -Dswt.widget.mode.Table=original -jar "$CS" execute -cp "$CP" $SUITES
bash $H 2400 $C/differential-viewport-vs-official.log -Xmx1g "$JA" $XA -Dswt.viewport.recording=$(cygpath -w $OFFICIAL_REC) -Dswt.viewport.upstreamDefects=$DEFECTS -Dswt.viewport.evidence=$(cygpath -w $C/evidence) -jar "$CS" execute -cp "$CP" $DIFF
bash $H 2400 $C/differential-record-original.log -Xmx1g "$JA" $XA -Dswt.widget.mode.Table=original -Dswt.viewport.recording=$(cygpath -w $C/recording-original) -Dswt.viewport.evidence=$(cygpath -w $C/evidence) -jar "$CS" execute -cp "$CP" $DIFF
"$JAVA_HOME/bin/java" -jar "$CLI" report "$EXEC" --classfiles "$JAR" --csv "$(cygpath -w $C/coverage.csv)" --xml "$(cygpath -w $C/coverage.xml)" --html "$(cygpath -w $C/html)" > $C/report.log 2>&1
for f in suites-viewport suites-original differential-viewport-vs-official differential-record-original; do
  echo "== $f"
  sed 's/\x1b\[[0-9;]*m//g' $C/$f.log | grep -E "✘|tests (successful|failed|aborted)" | head -8
done
echo "== original equals official (rule 12)"
for f in $OFFICIAL_REC/table-*.txt; do
  n=$(basename $f)
  if [ -f $C/recording-original/$n ]; then cmp -s <(tail -n +2 $f) <(tail -n +2 $C/recording-original/$n) && echo "same" || echo "DIFFERS $n"; else echo "MISSING $n"; fi
done | sort | uniq -c
