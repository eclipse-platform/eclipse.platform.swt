#!/usr/bin/env bash
# One fix cycle of the List and Combo viewports: regenerate + apply the hook recipes twice (the second pass must be
# UNCHANGED), compile win32, build the jar and tests. Usage: list_viewport_cycle.sh <worktree> <out-dir> <stamp>
WT=$1; OUT=$2; STAMP=$3
C=K:/m3-work/syn-recipes/synexia-openrewrite-recipes/crates/m3-viewport-catalogue-recipes
R="$WT/docs/m3/list-viewport/recipe"
W="$WT/bundles/org.eclipse.swt/Eclipse SWT/win32/org/eclipse/swt/widgets"
export JAVA_HOME="C:/Program Files/Java/jdk-21"
mkdir -p "$R/META-INF/rewrite"
python -I "$R/gen_list_viewport_yml.py" "$R/META-INF/rewrite/m3-swt-list-viewport.yml" || exit 1
python -I "$R/gen_combo_viewport_yml.py" "$R/META-INF/rewrite/m3-swt-combo-viewport.yml" || exit 1
CPR="$R;$C/target/classes;$(cat $C/target/cp.txt)"
for pass in 1 2; do
  for target in SwtListViewportWin32:List SwtComboViewportWin32:Combo; do
    echo "recipe ${target%%:*} pass $pass: $("$JAVA_HOME/bin/java" -Xmx1g -cp "$CPR" com.synexia.openrewrite.viewport.M3PerFileApply com.synexia.openrewrite.viewport.${target%%:*} "$W/${target##*:}.java" 2>&1 | grep -oE '^(CHANGED|UNCHANGED|ERROR).*' | cut -c1-80 | tr '\n' ' ')"
  done
done
bash C:/m3-superset/tools/build_lock.sh swt-list bash C:/m3-superset/tools/compile_platform.sh "$WT" win32 || exit 1
[ "$STAMP" = "-" ] && exit 0
bash C:/m3-superset/tools/build_lock.sh swt-list bash C:/m3-superset/tools/build_and_tests.sh "$WT" "$OUT" "$STAMP" > "$OUT/build-$STAMP.out" 2>&1
tail -3 "$OUT/build-$STAMP.out"
