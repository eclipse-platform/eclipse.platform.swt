#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
#
# Applies the name convergence recipe (recipe/META-INF/rewrite/m3-swt-table-names.yml, NAME_CONVERGENCE.tsv) to
# TableViewport, its hook call sites and the white-box test, twice: the second pass must be UNCHANGED. The sources
# are parsed together with the SWT jar of a build on the class path, so the rename reaches the call sites.
# Run it under the build lock:
#   bash C:/m3-superset/tools/build_lock.sh <agent> bash table_names_recipe.sh <worktree> <swt-jar> <work-dir>
WT=$1; JAR=$2; W=$3
export JAVA_HOME="C:/Program Files/Java/jdk-21"
C=K:/m3-work/syn-recipes/synexia-openrewrite-recipes/crates/m3-viewport-catalogue-recipes
R="$WT/docs/m3/table-viewport/recipe"
CP="$C/target/classes;$(cat $C/target/cp.txt)"
mkdir -p "$W/classes"
"$JAVA_HOME/bin/javac" --release 21 -nowarn -cp "$CP" -d "$W/classes" "$R/M3ClasspathApply.java" || exit 1
S="bundles/org.eclipse.swt/Eclipse SWT/win32/org/eclipse/swt/widgets"
T="tests/org.eclipse.swt.tests/JUnit Tests/org/eclipse/swt/tests/junit"
for pass in 1 2; do
  echo "== names recipe pass $pass"
  "$JAVA_HOME/bin/java" -Xmx1500m -cp "$R;$W/classes;$CP" com.synexia.openrewrite.viewport.table.M3ClasspathApply \
    com.synexia.openrewrite.viewport.SwtTableViewportNames "$JAR" "$WT" \
    "$S/TableViewport.java" "$S/Table.java" "$S/TableItem.java" "$S/TableColumn.java" "$S/ScrollBar.java" \
    "$T/Test_org_eclipse_swt_widgets_Table_ViewportArms.java"
done
