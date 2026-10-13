#!/bin/sh
# Leaf 1 (visibility gate) proof, one command. Sequential, memory gated, one JVM at a time.
# Workspace C:/m3-work-c/visgate must hold natives/ (swt-*-win32-4975r10.dll, WebView2Loader.dll) and base-src/ (base snapshot).
set -u
G=/c/m3-work-c/visgate
W=/c/m3-work-c/wt/swt-visibility-gate
S=$W/.m3/swt-visibility-gate-20261013/scripts
mkdir -p $G/scripts $G/tools $G/test-support/org/eclipse/test
cp $S/*.sh $G/scripts/ && cp $S/pngdiff.py $S/summarize.py $G/tools/ && cp $S/test-support/org/eclipse/test/Screenshots.java $G/test-support/org/eclipse/test/
cd $G
[ -d base-classes ] || sh scripts/build-swt.sh $G/base-src $G/base-classes $G/base-sources.list
sh scripts/gate.sh > /dev/null && rm -rf after-classes && sh scripts/build-swt.sh $W $G/after-classes $G/after-sources.list
sh scripts/junit.sh after junit-gate Test_org_eclipse_swt_internal_VisibilityGate
T="Test_org_eclipse_swt_widgets_Composite Test_org_eclipse_swt_widgets_Shell Test_org_eclipse_swt_widgets_Canvas Test_org_eclipse_swt_widgets_ScrolledComposite Test_org_eclipse_swt_custom_CTabFolder Test_org_eclipse_swt_layout_GridData Test_org_eclipse_swt_layout_FormAttachment Test_org_eclipse_swt_layout_BorderLayout Test_org_eclipse_swt_widgets_Group Test_org_eclipse_swt_widgets_TabFolder Test_org_eclipse_swt_widgets_Table Test_org_eclipse_swt_widgets_Tree Test_org_eclipse_swt_custom_StyledText"
for m in before after; do
  sh scripts/junit.sh $m junit-$m $T > junit-$m.summary 2>&1
  sh scripts/junit.sh $m junit-display-$m Test_org_eclipse_swt_widgets_Display > junit-display-$m.summary 2>&1
  grep -hE "^  JUnit Jupiter:" junit-$m.log junit-display-$m.log | sort > junit-$m.failed
done
echo "== failing only after:"; comm -13 junit-before.failed junit-after.failed
for r in 1 2 3; do for s in tab scrolled minimized covered stack; do for m in before after; do sh scripts/bench.sh $s $m 100; done; done; done > run-matrix4.log 2>&1
for s in tab scrolled minimized covered stack; do sh scripts/bench.sh $s after-off 100; done >> run-matrix4.log 2>&1
python -I tools/summarize.py run-matrix4.log
for w in control custom; do for m in before after after-off; do sh scripts/smoke.sh $w $m 10; done; done | tee run-smoke2.log
