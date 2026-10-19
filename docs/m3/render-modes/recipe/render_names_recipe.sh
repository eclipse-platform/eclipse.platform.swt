#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# Applies the M3 naming recipe (brief rule 14) to the drawn-plane sources of a worktree, twice: the second pass must
# report UNCHANGED. Type renames keep the file path (M3PerFileApply writes in place); the files are then moved to the
# paths of their new names with git mv, so history follows them.
# Usage: render_names_recipe.sh <worktree>
WT=$1
C=K:/m3-work/syn-recipes/synexia-openrewrite-recipes/crates/m3-viewport-catalogue-recipes
R="$WT/docs/m3/render-modes/recipe"
B="$WT/bundles/org.eclipse.swt"
export JAVA_HOME="C:/Program Files/Java/jdk-21"
CPR="$R;$C/target/classes;$(cat $C/target/cp.txt)"
OLD="$B/Eclipse SWT Render M3/win32/org/eclipse/swt/internal/render/skija"
NEW="$B/Eclipse SWT Render M3/win32/org/eclipse/swt/internal/render/m3"
FILES=("$B/Eclipse SWT/win32/org/eclipse/swt/internal/render/RowBackends.java" "$B/Eclipse SWT/win32/org/eclipse/swt/widgets/DrawnPlane.java"
  "$B/Eclipse SWT/win32/org/eclipse/swt/widgets/TreeDrawnPlane.java" "$B/Eclipse SWT/win32/org/eclipse/swt/widgets/TableDrawnPlane.java"
  "$B/Eclipse SWT/win32/org/eclipse/swt/widgets/ListDrawnPlane.java")
if [ -d "$OLD" ]; then FILES+=("$OLD/SkijaRowCanvas.java" "$OLD/SkijaGpu.java"); else FILES+=("$NEW/M3RowCanvas.java" "$NEW/M3Gpu.java"); fi
for pass in 1 2; do
  echo "names pass $pass: $("$JAVA_HOME/bin/java" -Xmx1g -cp "$CPR" com.synexia.openrewrite.viewport.M3PerFileApply \
    com.synexia.openrewrite.viewport.SwtRenderModesM3Names "${FILES[@]}" 2>&1 | grep -oE '^(CHANGED|UNCHANGED|ERROR)' | tr '\n' ' ')"
done
if [ -d "$OLD" ]; then
  mkdir -p "$NEW"
  git -C "$WT" mv "$OLD/SkijaRowCanvas.java" "$NEW/M3RowCanvas.java"
  git -C "$WT" mv "$OLD/SkijaGpu.java" "$NEW/M3Gpu.java"
  rmdir "$OLD" 2>/dev/null
  echo "moved to $NEW"
fi
