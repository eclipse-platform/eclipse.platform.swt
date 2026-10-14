#!/bin/sh
# JUnit before/after, 10M scale test, examples and snippets; sequential, memory-gated
cd /c/m3-work-c/lc-bench
sh scripts/gated.sh 2400 true > /dev/null || { echo GATE-TIMEOUT; exit 1; }
sh scripts/compile-tests.sh
C="Test_org_eclipse_swt_widgets_List Test_org_eclipse_swt_widgets_Combo Test_org_eclipse_swt_custom_CCombo Test_org_eclipse_swt_widgets_List_Virtual Test_org_eclipse_swt_custom_CCombo_Virtual Test_VirtualListModel"
sh scripts/run-tests.sh base base -Xmx768m -- $C
sh scripts/run-tests.sh after after -Xmx768m -- $C Test_org_eclipse_swt_widgets_List_Viewport
sh scripts/run-tests.sh after after-scale -Xmx2g -Dswt.list.scaleTest=true -- Test_org_eclipse_swt_widgets_List_Viewport
sh scripts/run-snippets.sh base > logs/snippets-base.tsv 2>&1; echo "snippets base: $(grep -c PASS logs/snippets-base.tsv) pass / $(grep -c -E 'PASS|FAIL' logs/snippets-base.tsv)"
sh scripts/run-snippets.sh after > logs/snippets-after.tsv 2>&1; echo "snippets after: $(grep -c PASS logs/snippets-after.tsv) pass / $(grep -c -E 'PASS|FAIL' logs/snippets-after.tsv)"
echo QUEUE2-DONE
