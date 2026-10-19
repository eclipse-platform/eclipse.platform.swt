#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
#
# JaCoCo coverage of the Tree viewport classes (copy of tree_coverage.sh for the coverage branch).
# Usage: tree_coverage_cov.sh <stamp> <part> [<part> ...]
#   parts: diff (recorded differential, plain + columns), ui (acceptance + coverage UI tests),
#          headless (pure JUnit tests, no Display), report (merge every *.exec of the out dir into csv + html)
# Each part writes its own exec file in $OUT; report merges what is there. UI parts run on a hidden desktop.
STAMP=$1; shift
export JAVA_HOME="C:/Program Files/Java/jdk-21"
M=~/.m2/repository/org/jacoco
AGENT=$(cygpath -w $M/org.jacoco.agent/0.8.15/org.jacoco.agent-0.8.15-runtime.jar)
CLI=$(cygpath -w $M/org.jacoco.cli/0.8.15/org.jacoco.cli-0.8.15-nodeps.jar)
CS=$(cygpath -w ~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
TT=$(cygpath -w C:/m3-superset/tvc/tests); JAR=$(cygpath -w C:/m3-superset/tvc/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar)
WT=C:/m3-superset/swt-tree-cov
OUT=${OUT:-K:/m3-work/runs/coverage/tree-cov}; mkdir -p $OUT
INC='org.eclipse.swt.widgets.TreeViewport*:org.eclipse.swt.widgets.VirtualTree*:org.eclipse.swt.widgets.VirtualItemState*:org.eclipse.swt.internal.WidgetMode*:org.eclipse.swt.internal.SwtLog*'
T=org.eclipse.swt.tests.junit
REC=$(cygpath -w K:/m3-work/runs/tree-stock-recording-i)
# selectors of the test classes that exist in the compiled tests
sel () { for c in "$@"; do [ -f C:/m3-superset/tvc/tests/org/eclipse/swt/tests/junit/$c.class ] && printf -- "--select-class $T.%s " "$c"; done; }
HEADLESS=$(sel Test_VirtualTreeResidentPlan Test_VirtualTreeTopology Test_VirtualTreeWindow Test_VirtualTreeViewport Test_VirtualTreeResidency Test_VirtualTreeCoverage Test_org_eclipse_swt_internal_WidgetMode Test_org_eclipse_swt_internal_SwtLog)
UI=$(sel Test_org_eclipse_swt_widgets_Tree_Viewport Test_org_eclipse_swt_widgets_Tree_ViewportCoverage Test_org_eclipse_swt_widgets_Tree_ViewportEdges)
DIFF="--select-method $T.Test_org_eclipse_swt_widgets_Tree_ViewportDifferential#seededScriptsAnswerLikeStockSwt --select-method $T.Test_org_eclipse_swt_widgets_Tree_ViewportDifferential#seededScriptsWithCheckAndColumnsAnswerLikeStockSwt"
ui_run () { # <name> <timeout> <selectors...>
  local name=$1 timeout=$2; shift 2
  rm -f $OUT/$name.exec
  bash C:/m3-superset/tools/ui_lock.sh claude-tree-cov bash C:/m3-superset/tools/hidden_java.sh $timeout $OUT/$name.log -Xmx1g $EXTRA \
    "-javaagent:$AGENT=destfile=$(cygpath -w $OUT/$name.exec),includes=$INC" -Dswt.viewport.recording=$REC \
    -Dswt.viewport.evidence=$(cygpath -w $OUT/ev) -jar "$CS" execute -cp "$TT;$JAR" "$@" --disable-banner --details=tree
  echo "$name: $(sed 's/\x1b\[[0-9;]*m//g' $OUT/$name.log | grep -E 'tests (successful|failed)' | tr -s ' ' | tr '\n' ' ')"
  gzip -f -k $OUT/$name.log
}
for part in "$@"; do
  case $part in
    diff) ui_run diff 2400 $DIFF ;;
    ui) ui_run ui 1200 $UI ;;
    orig) EXTRA=-Dswt.widget.mode=original ui_run orig 300 --select-method "$T.Test_org_eclipse_swt_widgets_Tree_ViewportCoverage#anyModeAnswersTheVirtualBasics" ;;
    headless)
      for try in $(seq 1 30); do kb=$(bash C:/m3-superset/tools/commit_free_kb.sh); [ "$kb" -ge 2097152 ] && break; sleep 20; done
      rm -f $OUT/headless.exec
      "$JAVA_HOME/bin/java" -Xmx1g "-javaagent:$AGENT=destfile=$(cygpath -w $OUT/headless.exec),includes=$INC" \
        -jar "$CS" execute -cp "$TT;$JAR" $HEADLESS --disable-banner --details=tree > $OUT/headless.log 2>&1
      echo "headless: $(sed 's/\x1b\[[0-9;]*m//g' $OUT/headless.log | grep -E 'tests (successful|failed)' | tr -s ' ' | tr '\n' ' ')" ;;
    report)
      CL=$OUT/classes; rm -rf $CL; mkdir -p $CL
      (cd $CL && "$JAVA_HOME/bin/jar" xf "$JAR" org/eclipse/swt/widgets/TreeViewport.class 'org/eclipse/swt/widgets/TreeViewport$NativeWindow.class' \
        org/eclipse/swt/widgets/VirtualTreeViewport.class org/eclipse/swt/widgets/VirtualTreeResidentPlan.class org/eclipse/swt/widgets/VirtualTreeTopology.class \
        org/eclipse/swt/widgets/VirtualTreeVisibleProjection.class 'org/eclipse/swt/widgets/VirtualTreeVisibleProjection$Residency.class' \
        'org/eclipse/swt/widgets/VirtualTreeVisibleProjection$Row.class' org/eclipse/swt/internal/WidgetMode.class org/eclipse/swt/internal/SwtLog.class)
      EXECS=$(ls $OUT/*.exec | while read f; do cygpath -w "$f"; done)
      "$JAVA_HOME/bin/java" -jar "$CLI" report $EXECS --classfiles "$(cygpath -w $CL)" \
        --sourcefiles "$(cygpath -w "$WT/bundles/org.eclipse.swt/Eclipse SWT/common")" --sourcefiles "$(cygpath -w "$WT/bundles/org.eclipse.swt/Eclipse SWT/win32")" \
        --csv "$(cygpath -w $OUT/coverage.csv)" --xml "$(cygpath -w $OUT/coverage.xml)" > $OUT/report.log 2>&1
      awk -F, 'NR>1 {ml+=$8; cl+=$9; mb+=$6; cb+=$7; printf "%-40s line %5.1f%% (%d/%d)  branch %5.1f%% (%d/%d)\n", $3, ($8+$9? 100*$9/($8+$9):100), $9, $8+$9, ($6+$7? 100*$7/($6+$7):100), $7, $6+$7} END {printf "TOTAL line %.1f%% (%d/%d)  branch %.1f%% (%d/%d)\n", 100*cl/(ml+cl), cl, ml+cl, 100*cb/(mb+cb), cb, mb+cb}' $OUT/coverage.csv ;;
  esac
done
