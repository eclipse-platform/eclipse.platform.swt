#!/usr/bin/env bash
# Run the List viewport differential + acceptance tests (or the given JUnit selectors) against a built jar,
# under the desktop lock and a hard time budget. Usage: list_viewport_run.sh <out-dir> <stamp> [selectors...]
OUT=$1; STAMP=$2; shift 2
export JAVA_HOME="C:/Program Files/Java/jdk-21"
JAR=$OUT/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar
CS=$(cygpath -m ~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
T=org.eclipse.swt.tests.junit
SEL=${*:-"--select-class $T.Test_org_eclipse_swt_widgets_List_ViewportDifferential --select-class $T.Test_org_eclipse_swt_widgets_List_ViewportAcceptance"}
bash C:/m3-superset/tools/ui_lock.sh swt-list bash C:/m3-superset/tools/run_guarded.sh 1200 "$JAVA_HOME/bin/java" -Xmx2g -jar $CS execute -cp "$OUT/tests;$JAR" $SEL --disable-banner --details=tree > "$OUT/run-$STAMP.log" 2>&1
sed 's/\x1b\[[0-9;]*m//g' "$OUT/run-$STAMP.log" | grep -E "memory gate|GUARD|✘|✔|tests (successful|failed|skipped)|^\s+original:|^\s+viewport:|differs|=> " | head -40
