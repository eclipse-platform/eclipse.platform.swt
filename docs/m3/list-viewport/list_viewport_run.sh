#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# Runs List (WIDGET=List, the default) or Combo (WIDGET=Combo) viewport tests on the hidden test desktop
# (never the user's), under the desktop lock.
# Usage: [WIDGET=Combo] list_viewport_run.sh <out-dir> <stamp> <mode> [junit selectors...]
#   mode official : record the reference with the official SWT 3.135.0 jar  -> $RUNS/<widget>-official
#   mode original : record the reference with this SWT, -Dswt.widget.mode=original -> $RUNS/<widget>-original
#   mode viewport : run this SWT (viewport) against $RUNS/<widget>-${AGAINST:-official}
# Without selectors the differential's styles run in separate JVMs (the list box keeps one wheel
# remainder per thread, so one style must not see another's history). JOPTS adds JVM options, for
# example -Dswt.viewport.combo.reach=30 (window moves inside every drop); TAG names such a run's logs; RECNAME
# names the recording directory (default: the widget), so a separate test records without deleting the others.
# M3_RUNS moves the recordings and evidence (default K:/m3-work/runs), for example to a scratch disk.
OUT=$1; STAMP=$2; MODE=$3; shift 3
RUNS=${M3_RUNS:-K:/m3-work/runs}
JAR=$(cygpath -w "$OUT/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar")
OFFICIAL=$(cygpath -w ~/.m2/repository/org/eclipse/platform/org.eclipse.swt.win32.win32.x86_64/3.135.0/org.eclipse.swt.win32.win32.x86_64-3.135.0.jar)
CS=$(cygpath -w ~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
TESTS=$(cygpath -w "$OUT/tests")
T=org.eclipse.swt.tests.junit
W=${WIDGET:-List}
R=${RECNAME:-$(echo "$W" | tr 'A-Z' 'a-z')}
D=$T.Test_org_eclipse_swt_widgets_${W}_ViewportDifferential
case "$MODE" in
  official) CP="$TESTS;$OFFICIAL"; PROPS="-Dswt.viewport.recording=$RUNS/$R-official"; rm -rf $RUNS/$R-official ;;
  original) CP="$TESTS;$JAR"; PROPS="-Dswt.widget.mode=original -Dswt.viewport.recording=$RUNS/$R-original"; rm -rf $RUNS/$R-original ;;
  *) CP="$TESTS;$JAR"; PROPS="-Dswt.viewport.recording=$RUNS/$R-${AGAINST:-official}" ;;
esac
if [ $# -eq 0 ] && [ "$W" = Combo ]; then
  set -- "--select-method $D#readOnlyComboAnswersLikeTheReference" "--select-method $D#editableComboAnswersLikeTheReference" \
    "--select-method $D#readOnlyComboWithVerifyAnswersLikeTheReference"
elif [ $# -eq 0 ]; then
  set -- "--select-method $D#singleSelectionListAnswersLikeTheReference" "--select-method $D#extendedSelectionListAnswersLikeTheReference" \
    "--select-method $D#simpleMultiSelectionListAnswersLikeTheReference"
else
  set -- "$*"
fi
i=0
for S in "$@"; do
  i=$((i + 1))
  LOG=$OUT/run-$STAMP-$R-$MODE${TAG:-}-$i.log
  bash C:/m3-superset/tools/ui_lock.sh swt-list bash C:/m3-superset/tools/hidden_java.sh ${BUDGET:-1500} "$LOG" -Xmx2g --enable-preview \
    -Dswt.viewport.evidence=$RUNS/$R-evidence $PROPS $JOPTS -jar "$CS" execute -cp "$CP" $S --disable-banner --details=tree
  sed 's/\x1b\[[0-9;]*m//g' "$LOG" | grep -E "memory gate|GUARD|TIMEOUT|hidden desktop|✘|✔ *$|tests (successful|failed)|recorded:|this run:" | grep -v "Platform Suite\|Vintage\|Jupiter ✔" | head -8 | cut -c1-400
done
