#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# Applies the M3 naming recipe (brief rule 14) to the all-widgets sources that name the drawn mode, twice: the second
# pass must report UNCHANGED for every file.
# Usage: laf_names_recipe.sh <worktree>
WT=$1
C=K:/m3-work/syn-recipes/synexia-openrewrite-recipes/crates/m3-viewport-catalogue-recipes
R="$WT/docs/m3/look-and-feel/recipe"
S="$WT/bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt"
T="$WT/tests/org.eclipse.swt.tests/JUnit Tests/org/eclipse/swt/tests/junit"
export JAVA_HOME="C:/Program Files/Java/jdk-21"
CPR="$R;$C/target/classes;$(cat $C/target/cp.txt)"
FILES=("$S/internal/laf/LookAndFeel.java" "$S/internal/laf/RendererFactory.java" "$S/widgets/ViewportScrollBarGeometry.java"
  "$T/Test_org_eclipse_swt_internal_laf_LookAndFeel.java" "$T/Test_ViewportDifferentialHarness.java")
for pass in 1 2; do
  echo "names pass $pass: $("$JAVA_HOME/bin/java" -Xmx1g -cp "$CPR" com.synexia.openrewrite.viewport.M3PerFileApply \
    com.synexia.openrewrite.viewport.SwtAllWidgetsM3Names "${FILES[@]}" 2>&1 | grep -oE '^(CHANGED|UNCHANGED|ERROR)' | tr '\n' ' ')"
done
