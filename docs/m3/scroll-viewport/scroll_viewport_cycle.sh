#!/usr/bin/env bash
# One cycle of leaf L1 (docs/m3/SCROLL_MEDIATED_VIEWPORT.md): regenerate and apply the hook recipe twice
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
    "$W/Control.java" "$W/Composite.java" "$W/Scrollable.java" "$W/Display.java" "$W/Widget.java" 2>&1 | grep -oE '^(CHANGED|UNCHANGED|ERROR)' | tr '\n' ' ')"
done
bash $TOOLS/build_lock.sh $AGENT bash $TOOLS/compile_platform.sh "$WT" win32 || exit 1
bash $TOOLS/build_lock.sh $AGENT bash $TOOLS/build_and_tests.sh "$WT" "$OUT" "$STAMP" | tail -2
JAR=$OUT/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar
CS=$(cygpath -m ~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
T=org.eclipse.swt.tests.junit
REC=$OUT/rec-$STAMP; EV=$OUT/evidence-$STAMP
rm -rf "$REC" "$EV"; mkdir -p "$REC"
L1="--select-class $T.Test_ViewportPlaneGeometry --select-class $T.Test_org_eclipse_swt_widgets_ScrolledComposite_Viewport --select-class $T.Test_org_eclipse_swt_widgets_ScrolledComposite_ViewportDifferential"
REG=""; for c in ScrolledComposite Composite Canvas Display Shell Group Label Button Sash ExpandBar Control_batch; do REG="$REG --select-class $T.Test_org_eclipse_swt_widgets_$c"; done
REG="$REG --select-class $T.Test_org_eclipse_swt_events_ControlEvent"
G="bash $TOOLS/run_guarded.sh"
bash $TOOLS/ui_lock.sh $AGENT bash -c "
$G 600 '$JAVA' -Xmx1g -Dswt.widget.mode=original -Dswt.viewport.recording=$REC -Dswt.viewport.evidence=$EV -jar $CS execute -cp '$OUT/tests;$JAR' --select-method $T.Test_org_eclipse_swt_widgets_ScrolledComposite_ViewportDifferential#recordedAgainstStock --disable-banner --details=tree > $OUT/run-$STAMP-stock.log 2>&1
$G 900 '$JAVA' -Xmx1g -Dswt.viewport.recording=$REC -Dswt.viewport.evidence=$EV -jar $CS execute -cp '$OUT/tests;$JAR' $L1 --disable-banner --details=tree > $OUT/run-$STAMP-viewport.log 2>&1
$G 900 '$JAVA' -Xmx1g -Dswt.widget.mode=original -jar $CS execute -cp '$OUT/tests;$JAR' $REG --disable-banner --details=tree > $OUT/regress-$STAMP-original.log 2>&1
$G 900 '$JAVA' -Xmx1g -jar $CS execute -cp '$OUT/tests;$JAR' $REG --disable-banner --details=tree > $OUT/regress-$STAMP-viewport.log 2>&1
"
for m in run-$STAMP-stock run-$STAMP-viewport regress-$STAMP-original regress-$STAMP-viewport; do
  echo "== $m"; sed 's/\x1b\[[0-9;]*m//g' $OUT/$m.log | grep -E "GUARD|memory gate|tests (successful|failed|skipped|aborted)"
done
for m in original viewport; do sed 's/\x1b\[[0-9;]*m//g' $OUT/regress-$STAMP-$m.log | grep "✘" | sed 's/^[│ ├└─]*//' | sort > $OUT/regress-$STAMP-$m.fail; done
echo "== plane test failures"; sed 's/\x1b\[[0-9;]*m//g' $OUT/run-$STAMP-viewport.log | grep -E "✘|^  (stock|viewport|near|far):" | cut -c1-700
echo "== regression failing only in viewport"; comm -13 $OUT/regress-$STAMP-original.fail $OUT/regress-$STAMP-viewport.fail | cut -c1-250
