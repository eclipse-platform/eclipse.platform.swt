#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# Verification of a build, one JVM at a time. Before each JVM: a bounded wait for 6 GB of commit headroom; after it:
# the log must be new and carry a result, else (the JVM did not start in a memory storm) it is retried, up to 5 times.
# Run under ui_lock.sh. The runner is copied first, so edits to the worktree cannot disturb a running shell.
# Environment: STAMP (default rm20261017h), TESTSET (tests-<set>, default h), ITEMS ("Widget mode [theme]" list), SKIP_TABLE.
O=${OUT:-K:/m3-work/agents/render-modes/out}
cp ${WT:-K:/m3-work/agents/render-modes/wt}/docs/m3/render-modes/tools/tree_differential_modes.sh $O/differential_modes_v.sh
T=$O/differential_modes_v.sh
JAR=$O/dist/org.eclipse.swt.win32.win32.x86_64-m3-${STAMP:-rm20261017h}.jar
TESTS=$O/tests-${TESTSET:-h}
SK=C:/m3-superset/i31/binaries/org.eclipse.swt.win32.win32.x86_64/lib
export EXTRA_CP="$(cygpath -w $O/dist/swt-render-m3-${STAMP:-rm20261017h}.jar);$(cygpath -w $SK/skija-shared-0.116.3.jar);$(cygpath -w $SK/skija-windows-x64-0.116.3.jar);$(cygpath -w $SK/types-0.1.1.jar)"
SMALL="-Xms96m -XX:ReservedCodeCacheSize=96m -XX:CICompilerCount=2"
wait_memory () {
  for i in $(seq 1 90); do
    kb=$(bash C:/m3-superset/tools/commit_free_kb.sh)
    [ "$kb" -ge 6291456 ] && return 0
    [ "$i" = 1 ] && echo "memory: $((kb / 1048576)) GB headroom, waiting"
    sleep 20
  done
  echo "memory: still below 6 GB after 30 minutes"
}
# one <widget> <run-dir> <mode|official> [theme]
one () {
  local w=$1 run=$2 mode=$3 theme=$4 log
  mkdir -p $run
  if [ "$mode" = official ]; then log=$run/official.log; else log=$run/$mode${theme:+-$theme}.log; fi
  for try in 1 2 3 4 5; do
    rm -f $log $run/$mode.log
    wait_memory
    echo "== $w $mode ${theme:+theme $theme }try $try $(date +%H:%M:%S)"
    if [ "$mode" = official ]; then
      rm -rf $run/recording
      bash $T $JAR $TESTS $run Test_org_eclipse_swt_widgets_${w}_ViewportDifferential
    else
      JVM_OPTS="$BASE_OPTS $SMALL ${theme:+-Dswt.widget.theme=$theme}" bash $T $JAR $TESTS $run Test_org_eclipse_swt_widgets_${w}_ViewportDifferential $mode
      [ -n "$theme" ] && [ -f $run/$mode.log ] && mv $run/$mode.log $log
    fi
    [ -s $log ] && grep -aq "tests successful" $log && return 0
    echo "   no result (JVM did not run); retrying"
  done
}
export BASE_OPTS=""
export JVM_OPTS="$SMALL"
for f in $O/list-diff/similarity.tsv $O/tree-diff/similarity.tsv; do [ -f $f ] && mv $f ${f%.tsv}-old-$(date +%H%M%S).tsv; done
IFS=';' read -ra LIST <<< "${ITEMS:-List viewport;Tree custom;Tree m3;Tree gpu;Tree custom swing;Tree custom initiative31;List custom;List m3;List gpu}"
for item in "${LIST[@]}"; do
  read -r w mode theme <<< "$item"
  one $w $O/$(echo $w | tr A-Z a-z)-diff $mode $theme
done
if [ -z "$SKIP_TABLE" ]; then
  export BASE_OPTS="--enable-preview --enable-native-access=ALL-UNNAMED -Dswt.viewport.upstreamDefects=$(cygpath -m K:/m3-work/agents/render-modes/wt/docs/m3/table-viewport/UPSTREAM_DEFECTS.tsv)"
  export JVM_OPTS="$BASE_OPTS"
  export XMX_OFFICIAL=-Xmx1536m
  [ -n "$(ls $O/table-diff/recording 2>/dev/null)" ] || one Table $O/table-diff official
  for m in viewport custom m3 gpu; do one Table $O/table-diff $m; done
fi
echo "== done $(date +%H:%M:%S)"
