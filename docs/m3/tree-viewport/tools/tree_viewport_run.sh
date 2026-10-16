#!/usr/bin/env bash
# Run the Tree viewport differential + acceptance tests against an already built jar (viewport on).
# Waits (bounded) for UI memory headroom. Usage: tree_viewport_run.sh <stamp> [extra junit selectors...]
STAMP=$1; shift
export JAVA_HOME="C:/Program Files/Java/jdk-21"
JAR=C:/m3-superset/tv/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar
CS=$(cygpath -m ~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
T=org.eclipse.swt.tests.junit
SEL=${*:-"--select-class $T.Test_org_eclipse_swt_widgets_Tree_ViewportDifferential --select-class $T.Test_org_eclipse_swt_widgets_Tree_Viewport"}
for i in $(seq 1 90); do
  kb=$(powershell -NoProfile -Command '(Get-CimInstance Win32_OperatingSystem).FreeVirtualMemory' | tr -d '\r ')
  [ "$kb" -ge 6291456 ] && break; sleep 20
done
bash C:/m3-superset/tools/ui_lock.sh claude-tree bash C:/m3-superset/tools/run_guarded.sh 900 "$JAVA_HOME/bin/java" -Xmx1g -Dswt.viewport.evidence=K:/m3-work/runs/tree-evidence -Dswt.viewport.recording=K:/m3-work/runs/tree-stock-recording -jar $CS execute -cp "C:/m3-superset/tv/tests;$JAR" $SEL --disable-banner --details=tree > C:/m3-superset/tv/run-$STAMP.log 2>&1
sed 's/\x1b\[[0-9;]*m//g' C:/m3-superset/tv/run-$STAMP.log | grep -E "memory gate|✘|tests (successful|failed)|^  plain:|^  viewport:|differs|^seed|^log:" | head -16
