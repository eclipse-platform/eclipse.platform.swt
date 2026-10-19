#!/bin/sh
# Compiles the complete win32 SWT source set of a worktree (win32 fragment layout) into <out>.
# usage: build-swt.sh <swt-worktree> <out-classes-dir> <sources-list-file>
set -eu
src=$1; out=$2; list=$3
J="/c/Program Files/Java/jdk-21/bin"
B="$src/bundles/org.eclipse.swt"
: > "$list"
for d in "Eclipse SWT/win32" "Eclipse SWT/common" "Eclipse SWT PI/common" "Eclipse SWT PI/win32" "Eclipse SWT OLE Win32/win32" \
  "Eclipse SWT Accessibility/win32" "Eclipse SWT Accessibility/common" "Eclipse SWT AWT/win32" "Eclipse SWT AWT/common" \
  "Eclipse SWT Drag and Drop/win32" "Eclipse SWT Drag and Drop/common" "Eclipse SWT Printing/win32" "Eclipse SWT Printing/common" \
  "Eclipse SWT Program/win32" "Eclipse SWT Program/common" "Eclipse SWT Custom Widgets/common" "Eclipse SWT Browser/common" \
  "Eclipse SWT Browser/win32" "Eclipse SWT OpenGL/win32" "Eclipse SWT OpenGL/common"; do
  find "$B/$d" -name '*.java' | while read -r f; do printf '"%s"\n' "$(cygpath -m "$f")"; done >> "$list"
done
mkdir -p "$out"
"$J/javac" -J-Xmx1536m -nowarn -encoding UTF-8 --release 21 -proc:none -d "$(cygpath -w "$out")" @"$(cygpath -w "$list")"
for d in "Eclipse SWT/common" "Eclipse SWT/win32" "Eclipse SWT PI/common" "Eclipse SWT PI/win32"; do
  (cd "$B/$d" && find . -name '*.properties' -exec cp --parents {} "$out" \;)
done
echo "classes=$(find "$out" -name '*.class' | wc -l) sources=$(wc -l < "$list")"
