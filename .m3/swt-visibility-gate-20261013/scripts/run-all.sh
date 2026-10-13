#!/bin/sh
# Sequential proof run (one JVM at a time): bench matrix x3, kill-switch row, SWT examples smoke, JUnit before/after.
G=/c/m3-work-c/visgate; cd $G
S="tab scrolled minimized covered stack"
for r in 1 2 3; do for s in $S; do for m in before after; do sh scripts/bench.sh $s $m 100; done; done; done > run-matrix3.log 2>&1
for s in $S; do sh scripts/bench.sh $s after-off 100; done >> run-matrix3.log 2>&1
echo MATRIX-DONE >> run-matrix3.log
for w in control custom; do for m in before after after-off; do sh scripts/smoke.sh $w $m 10; done; done > run-smoke.log 2>&1
echo SMOKE-DONE >> run-matrix3.log
T="Test_org_eclipse_swt_widgets_Composite Test_org_eclipse_swt_widgets_Shell Test_org_eclipse_swt_widgets_Canvas Test_org_eclipse_swt_widgets_Display Test_org_eclipse_swt_widgets_ScrolledComposite Test_org_eclipse_swt_custom_CTabFolder Test_org_eclipse_swt_layout_GridData Test_org_eclipse_swt_layout_FormAttachment Test_org_eclipse_swt_layout_BorderLayout Test_org_eclipse_swt_widgets_Group Test_org_eclipse_swt_widgets_TabFolder Test_org_eclipse_swt_widgets_Table Test_org_eclipse_swt_widgets_Tree Test_org_eclipse_swt_custom_StyledText"
sh scripts/junit.sh before $G/junit-before $T > junit-before.summary 2>&1
sh scripts/junit.sh after $G/junit-after $T Test_org_eclipse_swt_internal_VisibilityGate > junit-after.summary 2>&1
echo ALL-DONE >> run-matrix3.log
