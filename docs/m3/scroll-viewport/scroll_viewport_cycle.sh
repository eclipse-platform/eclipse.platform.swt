#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# One cycle of leaf L1 (UI runs on the invisible desktop WinSta0m3-ui-tests) (docs/m3/SCROLL_MEDIATED_VIEWPORT.md): regenerate and apply the hook recipe twice
# (the second pass must be UNCHANGED), compile win32, build the jar and the tests, then run on the desktop:
# the stock recording (-Dswt.widget.mode=original), the plane tests, and the regression suites in both modes.
# Usage: scroll_viewport_cycle.sh <stamp>
# Environment: WT (worktree), OUT (output dir), C (m3-viewport-catalogue-recipes crate), AGENT (lock name).
STAMP=$1
WT=${WT:-K:/m3-work/agents/swt-allwidgets/wt}
OUT=${OUT:-K:/m3-work/agents/swt-allwidgets/out}
C=${C:-K:/m3-work/syn-recipes/synexia-openrewrite-recipes/crates/m3-viewport-catalogue-recipes}
AGENT=${AGENT:-swt-allwidgets}
TOOLS=C:/m3-superset/tools
JAVA="C:/Program Files/Java/jdk-21/bin/java"
R=$WT/docs/m3/scroll-viewport/recipe
W="$WT/bundles/org.eclipse.swt/Eclipse SWT/win32/org/eclipse/swt/widgets"
python -I "$R/gen_scroll_viewport_yml.py" "$R/META-INF/rewrite/m3-swt-scroll-viewport.yml"
CPR="$R;$C/target/classes;$(cat "$C/target/cp.txt")"
for pass in 1 2; do
  echo "recipe pass $pass: $("$JAVA" -Xmx1g -cp "$CPR" com.synexia.openrewrite.viewport.M3PerFileApply com.synexia.openrewrite.viewport.SwtScrollViewportWin32 \
    "$W/Control.java" "$W/Composite.java" "$W/Scrollable.java" "$W/Display.java" "$W/Widget.java" "$W/Canvas.java" "$W/Caret.java" "$W/Combo.java" 2>&1 | grep -oE '^(CHANGED|UNCHANGED|ERROR)' | tr '\n' ' ')"
done
bash $TOOLS/build_lock.sh $AGENT bash $TOOLS/compile_platform.sh "$WT" win32 || exit 1
bash $TOOLS/build_lock.sh $AGENT bash $TOOLS/build_and_tests.sh "$WT" "$OUT" "$STAMP" | tail -2
JAR=$OUT/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar
OFFICIAL=$(cygpath -m ~/.m2/repository/org/eclipse/platform/org.eclipse.swt.win32.win32.x86_64/3.135.0/org.eclipse.swt.win32.win32.x86_64-3.135.0.jar)
CS=$(cygpath -m ~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
T=org.eclipse.swt.tests.junit
H="bash C:/m3-superset/tools/hidden_java.sh"
L=$OUT/hidden-$STAMP; rm -rf $L; mkdir -p $L/rec-official $L/rec-original $L/evidence
REC="--select-method $T.Test_org_eclipse_swt_widgets_ScrolledComposite_ViewportDifferential#recordedAgainstStock"
SUITE="--select-class $T.Test_ViewportDifferentialHarness --select-class $T.Test_ViewportPlaneGeometry --select-class $T.Test_org_eclipse_swt_widgets_ScrolledComposite_Viewport --select-class $T.Test_org_eclipse_swt_widgets_ScrolledComposite_ViewportDifferential"
REG=""; for c in ScrolledComposite Composite Canvas Shell Group Label Button Sash ExpandBar Control_batch; do REG="$REG --select-class $T.Test_org_eclipse_swt_widgets_$c"; done
REG="$REG --select-class $T.Test_org_eclipse_swt_events_ControlEvent"
J="-Xmx1g -Dswt.viewport.evidence=$L/evidence"
bash C:/m3-superset/tools/ui_lock.sh $AGENT bash -c "
$H 600 $L/record-official.log $J -Dswt.viewport.recording=$L/rec-official -jar $CS execute -cp '$OUT/tests;$OFFICIAL' $REC --disable-banner --details=tree
$H 600 $L/record-original.log $J -Dswt.widget.mode=original -Dswt.viewport.recording=$L/rec-original -jar $CS execute -cp '$OUT/tests;$JAR' $REC --disable-banner --details=tree
$H 900 $L/suite-vs-official.log $J -Dswt.viewport.recording=$L/rec-official -jar $CS execute -cp '$OUT/tests;$JAR' $SUITE --disable-banner --details=tree
$H 600 $L/vs-original.log $J -Dswt.viewport.recording=$L/rec-original -jar $CS execute -cp '$OUT/tests;$JAR' $REC --disable-banner --details=tree
$H 600 $L/regress-original-display.log -Xmx1g -Dswt.widget.mode=original -jar $CS execute -cp '$OUT/tests;$JAR' --select-class $T.Test_org_eclipse_swt_widgets_Display --disable-banner --details=tree
$H 900 $L/regress-original.log -Xmx1g -Dswt.widget.mode=original -jar $CS execute -cp '$OUT/tests;$JAR' $REG --disable-banner --details=tree
$H 600 $L/regress-viewport-display.log -Xmx1g -jar $CS execute -cp '$OUT/tests;$JAR' --select-class $T.Test_org_eclipse_swt_widgets_Display --disable-banner --details=tree
$H 900 $L/regress-viewport.log -Xmx1g -jar $CS execute -cp '$OUT/tests;$JAR' $REG --disable-banner --details=tree
"
for m in record-official record-original suite-vs-official vs-original regress-original-display regress-original regress-viewport-display regress-viewport; do
  echo "== $m: $(sed 's/\x1b\[[0-9;]*m//g' $L/$m.log 2>/dev/null | grep -E "tests (successful|failed|aborted|skipped)" | tr -s ' []' ' ' | tr '\n' ';')"
  sed 's/\x1b\[[0-9;]*m//g' $L/$m.log 2>/dev/null | grep -E "✘|■|^  (recorded|this run|expected|actual):|hidden desktop" | cut -c1-400 | head -8
done
python -I $WT/docs/m3/scroll-viewport/regression_triage.py $L/regress-viewport.log $L/regress-original.log > $L/triage.tsv
echo "== triage (outcome, category)"; cut -f3,6 $L/triage.tsv | sort | uniq -c
head -1 $L/rec-official/ScrolledComposite-plane-seed0.txt $L/rec-original/ScrolledComposite-plane-seed0.txt 2>/dev/null
