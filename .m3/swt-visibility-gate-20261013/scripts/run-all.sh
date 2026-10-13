#!/bin/sh
# Sequential proof run (one JVM at a time): bench matrix x3, kill-switch row, then JUnit before/after.
G=/c/m3-work-c/visgate; cd $G
for r in 1 2 3; do for s in tab scrolled minimized covered; do for m in before after; do sh scripts/bench.sh $s $m 100; done; done; done > run-matrix2.log 2>&1
for s in tab scrolled minimized covered; do sh scripts/bench.sh $s after-off 100; done >> run-matrix2.log 2>&1
echo MATRIX-DONE >> run-matrix2.log
T="Test_org_eclipse_swt_widgets_Composite Test_org_eclipse_swt_widgets_Shell Test_org_eclipse_swt_widgets_Canvas Test_org_eclipse_swt_widgets_Display Test_org_eclipse_swt_widgets_ScrolledComposite Test_org_eclipse_swt_custom_CTabFolder Test_org_eclipse_swt_layout_GridData Test_org_eclipse_swt_layout_FormAttachment Test_org_eclipse_swt_layout_BorderLayout Test_org_eclipse_swt_widgets_Group Test_org_eclipse_swt_widgets_TabFolder Test_org_eclipse_swt_widgets_Table Test_org_eclipse_swt_widgets_Tree Test_org_eclipse_swt_custom_StyledText"
sh scripts/junit.sh before $G/junit-before $T > junit-before.summary 2>&1
sh scripts/junit.sh after $G/junit-after $T > junit-after.summary 2>&1
echo ALL-DONE >> run-matrix2.log
