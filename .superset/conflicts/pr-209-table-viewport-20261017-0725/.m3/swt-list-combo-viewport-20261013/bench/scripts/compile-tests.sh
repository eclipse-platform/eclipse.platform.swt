#!/bin/sh
# compiles the JUnit classes of this leaf against base-classes and after-classes
cd /c/m3-work-c/lc-bench
J="/c/Program Files/Java/jdk-21/bin"
JU=$(cygpath -w /c/Users/hsoliwal/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
T="/c/m3-work-c/wt/swt-list-combo/tests/org.eclipse.swt.tests/JUnit Tests/org/eclipse/swt/tests/junit"
SP="$(cygpath -w "/c/m3-work-c/wt/swt-list-combo/tests/org.eclipse.swt.tests/JUnit Tests");$(cygpath -w teststubs)"
COMMON="Test_org_eclipse_swt_widgets_List Test_org_eclipse_swt_widgets_Combo Test_org_eclipse_swt_custom_CCombo Test_org_eclipse_swt_widgets_List_Virtual Test_org_eclipse_swt_custom_CCombo_Virtual Test_VirtualListModel"
for v in base after; do
  extra=""; [ $v = after ] && extra="Test_org_eclipse_swt_widgets_List_Viewport"
  : > logs/tests-$v.args
  for c in $COMMON $extra; do printf '"%s"\n' "$(cygpath -m "$T/$c.java")" >> logs/tests-$v.args; done
  rm -rf tests-$v && mkdir -p tests-$v
  "$J/javac" -J-Xmx512m -nowarn -encoding UTF-8 -proc:none -d tests-$v -cp "$(cygpath -w $v-classes);$JU" -sourcepath "$SP" @"$(cygpath -w logs/tests-$v.args)" > logs/tests-$v-compile.log 2>&1
  echo "tests-$v javac=$? classes=$(find tests-$v -name '*.class' | wc -l)"
  grep -E "error:" -A2 logs/tests-$v-compile.log | head -20
done
