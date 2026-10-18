#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# Builds one stamp (SWT jar + tests + M3 backend jar) when commit headroom allows, retrying through memory storms
# (bounded); proves the tests complete (compile_tests.sh), then snapshots them and deletes older stamps only after
# everything exists. Run under build_lock.sh.
# Usage: build_stamp.sh <stamp> <tests-suffix>
STAMP=$1; SUFFIX=$2
O=${OUT:-K:/m3-work/agents/render-modes/out}
WT=${WT:-K:/m3-work/agents/render-modes/wt}
JAR=$O/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar
M3=$O/dist/swt-render-m3-$STAMP.jar
for try in 1 2 3 4 5 6; do
  for i in $(seq 1 45); do
    kb=$(bash C:/m3-superset/tools/commit_free_kb.sh)
    [ "$kb" -ge 4718592 ] && break
    sleep 20
  done
  bash C:/m3-superset/tools/build_and_tests.sh $WT $O $STAMP > $O/build-$STAMP.log 2>&1
  [ -f "$JAR" ] && break
  echo "build try $try: no jar"
done
[ -f "$JAR" ] || { echo "FAILED: no jar"; exit 1; }
bash $WT/docs/m3/render-modes/tools/build_m3_backend.sh $WT $JAR $M3 >> $O/build-$STAMP.log 2>&1
[ -f "$M3" ] || { echo "FAILED: no M3 backend jar"; exit 1; }
WT=$WT bash $WT/docs/m3/render-modes/tools/compile_tests.sh $JAR $O/tests >> $O/build-$STAMP.log 2>&1 || { echo "FAILED: tests incomplete"; exit 1; }
rm -rf $O/tests-$SUFFIX && cp -r $O/tests $O/tests-$SUFFIX
for old in $O/dist/*.jar; do case "$old" in *$STAMP*) ;; *) rm -f "$old" ;; esac; done
rm -rf $O/build-swt $O/dist/m3-backend-classes*
gzip -f $O/build-$STAMP.log
echo "OK $STAMP: $(ls $O/dist | tr '\n' ' ')"
