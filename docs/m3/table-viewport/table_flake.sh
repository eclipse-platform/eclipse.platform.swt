#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
#
# Repeatability of the Table differential: <runs> times, on the invisible desktop and without the JaCoCo agent, the
# official jar and the original mode record the selected variants and each recording is compared with the official
# recording line for line (header excluded), and the default mode runs against the official recording (its JUnit
# result: the accepted upstream defects of UPSTREAM_DEFECTS.tsv apply). One line per run. Run it under the desktop lock:
#   bash C:/m3-superset/tools/ui_lock.sh <agent> bash table_flake.sh <out-dir> <stamp> <official-recording-dir> <dest-dir> <runs> [variants]
# <out-dir> holds tests/ and dist/ of a build (build_and_tests.sh); [variants] defaults to 7,8 (the reentrancy scripts).
O=$1; STAMP=$2; OFFICIAL_REC=$3; D=$4; N=$5; VARIANTS=${6:-7,8}
CS=$(cygpath -w ~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
OFFICIAL=$(cygpath -w ~/.m2/repository/org/eclipse/platform/org.eclipse.swt.win32.win32.x86_64/3.135.0/org.eclipse.swt.win32.win32.x86_64-3.135.0.jar)
JAR=$(cygpath -w $O/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar)
TESTS=$(cygpath -w $O/tests)
H=C:/m3-superset/tools/hidden_java.sh
DEFECTS=$(cygpath -m "$(dirname "$0")")/UPSTREAM_DEFECTS.tsv
XA="--enable-preview --enable-native-access=ALL-UNNAMED -Dswt.viewport.variants=$VARIANTS"
DIFF="--select-class org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Table_ViewportDifferential --disable-banner --details=tree"
# compares every recording of a directory with the official recording: "<same> same, differs: <file>:<lines> ..."
compare () {
  local s=0 x="" f n
  for f in $1/table-*.txt; do
    n=$(basename $f)
    if cmp -s <(tail -n +2 $OFFICIAL_REC/$n) <(tail -n +2 $f); then s=$((s+1)); else x="$x $n:$(diff <(tail -n +2 $OFFICIAL_REC/$n) <(tail -n +2 $f) | grep -c '^>')lines"; fi
  done
  echo "$s same, differs:${x:- none}"
}
for r in $(seq 1 $N); do
  mkdir -p $D/r$r/recording-official $D/r$r/recording-original $D/r$r/evidence
  bash $H 900 $D/r$r/official.log -Xmx1g $XA -Dswt.viewport.recording=$(cygpath -w $D/r$r/recording-official) -Dswt.viewport.evidence=$(cygpath -w $D/r$r/evidence) -jar "$CS" execute -cp "$TESTS;$OFFICIAL" $DIFF
  bash $H 900 $D/r$r/original.log -Xmx1g $XA -Dswt.widget.mode.Table=original -Dswt.viewport.recording=$(cygpath -w $D/r$r/recording-original) -Dswt.viewport.evidence=$(cygpath -w $D/r$r/evidence) -jar "$CS" execute -cp "$TESTS;$JAR" $DIFF
  bash $H 900 $D/r$r/viewport.log -Xmx1g $XA -Dswt.viewport.recording=$(cygpath -w $OFFICIAL_REC) -Dswt.viewport.upstreamDefects=$DEFECTS -Dswt.viewport.evidence=$(cygpath -w $D/r$r/evidence) -jar "$CS" execute -cp "$TESTS;$JAR" $DIFF
  v=$(sed 's/\x1b\[[0-9;]*m//g' $D/r$r/viewport.log | grep -oE "[0-9]+ tests successful|[1-9][0-9]* tests (failed|aborted)|pixels after seed [0-9]+|step [0-9]+ differs|insufficient memory" | tr '\n' ' ')
  echo "run $r official: $(compare $D/r$r/recording-official) ($(ls $D/r$r/recording-official | wc -l) files) | original: $(compare $D/r$r/recording-original) ($(ls $D/r$r/recording-original | wc -l) files) | viewport: ${v:-no result}"
done
