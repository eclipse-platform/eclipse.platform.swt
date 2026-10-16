#!/usr/bin/env bash
# Runs List viewport tests on the hidden test desktop (never the user's), under the desktop lock.
# Usage: list_viewport_run.sh <out-dir> <stamp> <mode> [junit selectors...]
#   mode official : record the reference with the official SWT 3.135.0 jar  -> $RUNS/list-official
#   mode original : record the reference with this SWT, -Dswt.widget.mode=original -> $RUNS/list-original
#   mode viewport : run this SWT (viewport) against $RUNS/list-${AGAINST:-official}
# Without selectors the differential's three styles run in separate JVMs (the list box keeps one
# wheel remainder per thread, so one style must not see another's history).
OUT=$1; STAMP=$2; MODE=$3; shift 3
RUNS=K:/m3-work/runs
JAR=$(cygpath -w "$OUT/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar")
OFFICIAL=$(cygpath -w ~/.m2/repository/org/eclipse/platform/org.eclipse.swt.win32.win32.x86_64/3.135.0/org.eclipse.swt.win32.win32.x86_64-3.135.0.jar)
CS=$(cygpath -w ~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
TESTS=$(cygpath -w "$OUT/tests")
T=org.eclipse.swt.tests.junit
D=$T.Test_org_eclipse_swt_widgets_List_ViewportDifferential
case "$MODE" in
  official) CP="$TESTS;$OFFICIAL"; PROPS="-Dswt.viewport.recording=$RUNS/list-official"; rm -rf $RUNS/list-official ;;
  original) CP="$TESTS;$JAR"; PROPS="-Dswt.widget.mode=original -Dswt.viewport.recording=$RUNS/list-original"; rm -rf $RUNS/list-original ;;
  *) CP="$TESTS;$JAR"; PROPS="-Dswt.viewport.recording=$RUNS/list-${AGAINST:-official}" ;;
esac
if [ $# -eq 0 ]; then
  set -- "--select-method $D#singleSelectionListAnswersLikeTheReference" "--select-method $D#extendedSelectionListAnswersLikeTheReference" \
    "--select-method $D#simpleMultiSelectionListAnswersLikeTheReference"
else
  set -- "$*"
fi
i=0
for S in "$@"; do
  i=$((i + 1))
  LOG=$OUT/run-$STAMP-$MODE${TAG:-}-$i.log
  bash C:/m3-superset/tools/ui_lock.sh swt-list bash C:/m3-superset/tools/hidden_java.sh ${BUDGET:-1500} "$LOG" -Xmx2g --enable-preview \
    -Dswt.viewport.evidence=$RUNS/list-evidence $PROPS $JOPTS -jar "$CS" execute -cp "$CP" $S --disable-banner --details=tree
  sed 's/\x1b\[[0-9;]*m//g' "$LOG" | grep -E "memory gate|GUARD|TIMEOUT|hidden desktop|✘|✔ *$|tests (successful|failed)|recorded:|this run:" | grep -v "Platform Suite\|Vintage\|Jupiter ✔" | head -8 | cut -c1-400
done
