#!/usr/bin/env bash
# Regenerate and apply the Table viewport hook recipe to the win32 sources of a worktree, twice: the second
# pass must report UNCHANGED (fixed point). The recipe crate is read-only; this worktree's recipe directory
# is put first on the class path so its YAML is found.
# Usage: table_viewport_recipe.sh <worktree>
WT=$1
C=K:/m3-work/syn-recipes/synexia-openrewrite-recipes/crates/m3-viewport-catalogue-recipes
R="$WT/docs/m3/table-viewport/recipe"
W="$WT/bundles/org.eclipse.swt/Eclipse SWT/win32/org/eclipse/swt/widgets"
export JAVA_HOME="C:/Program Files/Java/jdk-21"
mkdir -p "$R/META-INF/rewrite"
python -I "$R/gen_table_viewport_yml.py" "$R/META-INF/rewrite/m3-swt-table-viewport.yml" || exit 1
CPR="$R;$C/target/classes;$(cat $C/target/cp.txt)"
for pass in 1 2; do
  echo "recipe pass $pass: $("$JAVA_HOME/bin/java" -Xmx1g -cp "$CPR" com.synexia.openrewrite.viewport.M3PerFileApply \
    com.synexia.openrewrite.viewport.SwtTableViewportWin32 "$W/Table.java" "$W/TableColumn.java" 2>&1 | grep -oE '^(CHANGED|UNCHANGED|ERROR)' | tr '\n' ' ')"
done
