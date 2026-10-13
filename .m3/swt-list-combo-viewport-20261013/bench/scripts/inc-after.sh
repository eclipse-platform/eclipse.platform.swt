#!/bin/sh
# incremental compile of the leaf's files into after-classes
cd /c/m3-work-c/lc-bench
B="/c/m3-work-c/wt/swt-list-combo/bundles/org.eclipse.swt"
J="/c/Program Files/Java/jdk-21/bin"
: > logs/inc-after.args
for f in "Eclipse SWT/win32/org/eclipse/swt/widgets/List.java" \
  "Eclipse SWT/win32/org/eclipse/swt/widgets/Combo.java" \
  "Eclipse SWT/common/org/eclipse/swt/widgets/VirtualListModel.java" \
  "Eclipse SWT/common/org/eclipse/swt/widgets/VirtualListWidth.java" \
  "Eclipse SWT/common/org/eclipse/swt/widgets/VirtualSelectionModel.java" \
  "Eclipse SWT/common/org/eclipse/swt/widgets/VirtualViewportPlanner.java" \
  "Eclipse SWT Custom Widgets/common/org/eclipse/swt/custom/CCombo.java" \
  "Eclipse SWT/common/org/eclipse/swt/custom/StylePolicy.java" \
  "Eclipse SWT PI/win32/org/eclipse/swt/internal/win32/OS.java"; do
  printf '"%s"\n' "$(cygpath -m "$B/$f")" >> logs/inc-after.args
done
"$J/javac" -J-Xmx512m -nowarn -encoding UTF-8 --release 21 -proc:none -d "$(cygpath -w after-classes)" -cp "$(cygpath -w after-classes)" @"$(cygpath -w logs/inc-after.args)" 2>&1 | head -40
echo "inc-compile done"
