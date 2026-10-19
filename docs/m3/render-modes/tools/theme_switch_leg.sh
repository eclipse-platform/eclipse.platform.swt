#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# Runtime look-and-feel switch leg of the Table differential (environment STAMP, TESTSET; OUT, WT). Run under ui_lock.sh.
# Per mode: A switches native -> swing at step 40 of every seed (-Dswt.viewport.themeSwitch=40:swing); B starts in
# swing (-Dswt.widget.theme=swing). Both must pass against stock (non-pixel probes); then the captures of A after the
# switch must equal B's pixel for pixel (compare_switch.py).
S=${OUT:-S:/m3-scratch/render-modes}
RUN=$S/table-diff; REC=$RUN/recording; TESTS=$S/tests-${TESTSET:?}; STAMP=${STAMP:?}
JAR=$S/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar
M2=$(cygpath -m ~/.m2/repository)
CS=$M2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar
SK=C:/m3-superset/i31/binaries/org.eclipse.swt.win32.win32.x86_64/lib
EXTRA="$S/dist/swt-render-m3-$STAMP.jar;$SK/skija-shared-0.116.3.jar;$SK/skija-windows-x64-0.116.3.jar;$SK/types-0.1.1.jar"
BASE="--enable-preview --enable-native-access=ALL-UNNAMED -Dswt.viewport.upstreamDefects=K:/m3-work/agents/render-modes/wt/docs/m3/table-viewport/UPSTREAM_DEFECTS.tsv"
SEL=org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Table_ViewportDifferential
wait_memory () { for i in $(seq 1 90); do [ "$(bash C:/m3-superset/tools/commit_free_kb.sh)" -ge 6291456 ] && return 0; sleep 20; done; }
result () { sed 's/\x1b\[[0-9;]*m//g' "$1" | grep -aE 'tests (successful|failed|aborted)' | tr -s ' ' | tr '\n' ' '; }
one () {
  local log=$1; shift
  for try in 1 2 3 4 5; do
    rm -f $log; wait_memory
    echo "== $(basename $log .log) try $try $(date +%H:%M:%S)"
    bash C:/m3-superset/tools/hidden_java.sh 1200 $log "$@"
    grep -aq "tests successful" $log 2>/dev/null && { echo "   $(result $log)"; grep -a "✘" $log | head -3; return 0; }
    echo "   no result; retrying"
  done
}
for m in ${MODES:-custom m3}; do
  for leg in switch swing; do
    EVI=$RUN/evidence-$leg-$m; rm -rf $EVI; mkdir -p $EVI; cp $RUN/evidence/*-official.png $EVI/
    if [ $leg = switch ]; then OPT=-Dswt.viewport.themeSwitch=40:swing; else OPT=-Dswt.widget.theme=swing; fi
    one $RUN/$leg-$m.log -Xmx1g -Xms96m -XX:ReservedCodeCacheSize=96m -XX:CICompilerCount=2 $BASE $OPT -Dswt.widget.mode=$m \
      -Dswt.viewport.variants=${VARIANTS:-0,5} -Dswt.viewport.evidence=$EVI -Dswt.viewport.recording=$REC \
      -Dswt.viewport.similarity=$RUN/similarity-$leg-$m.tsv -jar $CS execute -cp "$TESTS;$JAR;$EXTRA" --select-class $SEL --disable-banner --details=tree
  done
  python -I ${WT:-K:/m3-work/agents/render-modes/wt}/docs/m3/render-modes/tools/compare_switch.py $RUN/evidence-switch-$m $RUN/evidence-swing-$m $m 40
done
echo "== all done $(date +%H:%M:%S)"
