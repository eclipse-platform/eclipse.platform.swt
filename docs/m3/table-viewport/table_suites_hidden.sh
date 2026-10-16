#!/usr/bin/env bash
# Runs the Table suites and the Table viewport acceptance tests with the viewport on (default) and off
# (-Dswt.widget.mode.Table=original) on the invisible test desktop. Run it under the desktop lock:
#   bash C:/m3-superset/tools/ui_lock.sh <agent> bash table_suites_hidden.sh <out-dir> <stamp>
O=$1; STAMP=$2
JAR=$O/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar
CS=$(cygpath -m ~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
H=C:/m3-superset/tools/hidden_java.sh
T=org.eclipse.swt.tests.junit
SEL="--select-class $T.Test_org_eclipse_swt_widgets_Table --select-class $T.Test_org_eclipse_swt_widgets_TableItem --select-class $T.Test_org_eclipse_swt_widgets_TableColumn --select-class $T.Test_org_eclipse_swt_widgets_Table_Viewport --disable-banner --details=tree"
bash $H 1500 $O/suites-viewport.log -Xmx1g -jar $CS execute -cp "$O/tests;$JAR" $SEL
bash $H 1500 $O/suites-original.log -Xmx1g -Dswt.widget.mode.Table=original -jar $CS execute -cp "$O/tests;$JAR" $SEL
for f in viewport original; do
  echo "== $f"
  sed 's/\x1b\[[0-9;]*m//g' $O/suites-$f.log | grep -E "hidden desktop|✘|tests (successful|failed|aborted)" | head -12
done
