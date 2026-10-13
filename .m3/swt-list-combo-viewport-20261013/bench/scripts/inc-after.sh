#!/bin/sh
# incremental compile of the leaf's files into after-classes
cd /c/m3-work-c/lc-bench
B="/c/m3-work-c/wt/swt-list-combo/bundles/org.eclipse.swt"
J="/c/Program Files/Java/jdk-21/bin"
"$J/javac" -J-Xmx512m -nowarn -encoding UTF-8 --release 21 -proc:none -d "$(cygpath -w after-classes)" -cp "$(cygpath -w after-classes)" \
  "$(cygpath -w "$B/Eclipse SWT/win32/org/eclipse/swt/widgets/List.java")" \
  "$(cygpath -w "$B/Eclipse SWT/common/org/eclipse/swt/widgets/VirtualListModel.java")" \
  "$(cygpath -w "$B/Eclipse SWT/common/org/eclipse/swt/widgets/VirtualListWidth.java")" \
  "$(cygpath -w "$B/Eclipse SWT Custom Widgets/common/org/eclipse/swt/custom/CCombo.java")" \
  "$(cygpath -w "$B/Eclipse SWT/common/org/eclipse/swt/custom/StylePolicy.java")" \
  "$(cygpath -w "$B/Eclipse SWT PI/win32/org/eclipse/swt/internal/win32/OS.java")" 2>&1 | head -40
echo "inc-compile done"
