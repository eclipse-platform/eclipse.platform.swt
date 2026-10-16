#!/usr/bin/env bash
# One fix cycle of the Tree viewport: regenerate + apply the hook recipe (fixed point), compile win32,
# build the jar and tests, then run the recorded differential and the acceptance tests (viewport on).
# Usage: tree_viewport_cycle.sh <stamp>
STAMP=$1
C=K:/m3-work/syn-recipes/synexia-openrewrite-recipes/crates/m3-viewport-catalogue-recipes
S=C:/m3-superset/swt-tree
W="$S/bundles/org.eclipse.swt/Eclipse SWT/win32/org/eclipse/swt/widgets"
export JAVA_HOME="C:/Program Files/Java/jdk-21"
python -I $C/tools/gen_tree_viewport_yml.py $C/src/main/resources/META-INF/rewrite/m3-swt-tree-viewport.yml
(cd $C && mvn -B -o -q compile > /dev/null 2>&1)
CPR="$C/target/classes;$(cat $C/target/cp.txt)"
for pass in 1 2; do
  echo "recipe pass $pass: $("$JAVA_HOME/bin/java" -Xmx1g -cp "$CPR" com.synexia.openrewrite.viewport.M3PerFileApply com.synexia.openrewrite.viewport.SwtTreeViewportWin32 "$W/Tree.java" "$W/TreeItem.java" 2>&1 | grep -oE '^(CHANGED|UNCHANGED|ERROR)' | tr '\n' ' ')"
done
bash C:/m3-superset/tools/compile_platform.sh $S win32 || exit 1
(cd C:/m3-superset && bash tools/build_and_tests.sh $S C:/m3-superset/tv $STAMP > C:/m3-superset/tv/build-$STAMP.out 2>&1)
grep "tests javac" C:/m3-superset/tv/build-$STAMP.out
JAR=C:/m3-superset/tv/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar
CS=~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar
T=org.eclipse.swt.tests.junit
bash C:/m3-superset/tools/ui_lock.sh claude-tree bash C:/m3-superset/tools/run_guarded.sh 900 "$JAVA_HOME/bin/java" -Xmx1g -Dorg.eclipse.swt.internal.win32.Tree.viewport=true -jar $CS execute -cp "C:/m3-superset/tv/tests;$JAR" --select-class $T.Test_org_eclipse_swt_widgets_Tree_ViewportDifferential --select-class $T.Test_org_eclipse_swt_widgets_Tree_Viewport --disable-banner --details=tree > C:/m3-superset/tv/cycle-$STAMP.log 2>&1
sed 's/\x1b\[[0-9;]*m//g' C:/m3-superset/tv/cycle-$STAMP.log | grep -E "memory gate|✘|tests (successful|failed)|^  plain:|^  viewport:" | head -12
