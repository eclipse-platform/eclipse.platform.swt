# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# JaCoCo coverage of what the all-widgets branch adds or changes outside the look and feel package (brief rule 15):
# WidgetMode, the ExpandBar and CoolBar viewports (ExpandBar, ExpandItem, CoolBar), the hidden-child deferral of the
# plane mediation (Composite, ViewportPlane) and ViewportScrollBarGeometry. Records the official 3.135.0 reference of
# the differentials, then runs the suites with the JaCoCo agent on the hidden desktop in three JVMs (default mode;
# -Dswt.widget.mode=original; -Dswt.widget.mode.ExpandBar=original with the Composite viewport on) into one exec file.
# Writes jacoco.csv (whole classes) and changed-lines.tsv (every executable line the branch adds or changes since
# BASE, default the merge base with origin/master) next to this script; HTML under <out-dir>/aw-coverage/report.
# Run the jar build first (build_and_tests.sh <wt> <out> <stamp>). Usage: allwidgets_coverage.sh <worktree> <out-dir> <stamp>
WT=$1; OUT=$2; STAMP=$3
BASE=${BASE:-$(git -C "$WT" merge-base HEAD origin/master)}
JDK="C:/Program Files/Java/jdk-21"
M2=$(cygpath -w "$HOME/.m2/repository")
J="$M2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar"
AGENT="$M2/org/jacoco/org.jacoco.agent/0.8.11/org.jacoco.agent-0.8.11-runtime.jar"
CLI="$M2/org/jacoco/org.jacoco.cli/0.8.15/org.jacoco.cli-0.8.15-nodeps.jar"
OFFICIAL="$M2/org/eclipse/platform/org.eclipse.swt.win32.win32.x86_64/3.135.0/org.eclipse.swt.win32.win32.x86_64-3.135.0.jar"
JAR="$OUT/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar"
COV="$OUT/aw-coverage"; DOC="$WT/docs/m3/allwidgets-viewport/coverage"; SRC="$WT/bundles/org.eclipse.swt/Eclipse SWT"
FILES=("common/org/eclipse/swt/internal/WidgetMode.java" "common/org/eclipse/swt/widgets/ViewportScrollBarGeometry.java"
  "win32/org/eclipse/swt/widgets/Composite.java" "win32/org/eclipse/swt/widgets/CoolBar.java" "win32/org/eclipse/swt/widgets/ExpandBar.java"
  "win32/org/eclipse/swt/widgets/ExpandItem.java" "win32/org/eclipse/swt/widgets/ViewportPlane.java")
W=org.eclipse.swt.widgets; T=org.eclipse.swt.tests.junit
INCLUDES="org.eclipse.swt.internal.WidgetMode*:$W.ViewportScrollBarGeometry*:$W.Composite:$W.Composite\$*:$W.CoolBar:$W.CoolBar\$*:$W.ExpandBar:$W.ExpandBar\$*:$W.ExpandItem:$W.ExpandItem\$*:$W.ViewportPlane:$W.ViewportPlane\$*"
DIFF="--select-class $T.Test_org_eclipse_swt_widgets_ExpandBar_ViewportDifferential --select-class $T.Test_org_eclipse_swt_widgets_CoolBar_ViewportDifferential"
STOCK="--select-class $T.Test_org_eclipse_swt_widgets_ExpandBar --select-class $T.Test_org_eclipse_swt_widgets_ExpandItem --select-class $T.Test_org_eclipse_swt_widgets_CoolBar --select-class $T.Test_org_eclipse_swt_widgets_CoolItem"
sel () { for c in "$@"; do [ -f "$OUT/tests/org/eclipse/swt/tests/junit/$c.class" ] && printf -- " --select-class $T.%s" "$c"; done; }
OWN=$(sel Test_org_eclipse_swt_internal_WidgetMode Test_ViewportScrollBarGeometry Test_org_eclipse_swt_widgets_ExpandBar_Viewport Test_org_eclipse_swt_widgets_ExpandBar_Coverage Test_org_eclipse_swt_widgets_CoolBar_Coverage \
  Test_org_eclipse_swt_widgets_Composite Test_org_eclipse_swt_widgets_ScrolledComposite_Viewport)
GAPS=$(sel Test_org_eclipse_swt_widgets_ExpandBar_Coverage Test_org_eclipse_swt_widgets_CoolBar_Coverage)
rm -rf "$COV" && mkdir -p "$COV/classes" "$COV/rec"
unzip -q "$(cygpath -u "$JAR")" 'org/eclipse/swt/internal/WidgetMode*.class' 'org/eclipse/swt/widgets/ViewportScrollBarGeometry*.class' \
  'org/eclipse/swt/widgets/Composite.class' 'org/eclipse/swt/widgets/Composite$*.class' 'org/eclipse/swt/widgets/CoolBar.class' 'org/eclipse/swt/widgets/CoolBar$*.class' \
  'org/eclipse/swt/widgets/ExpandBar.class' 'org/eclipse/swt/widgets/ExpandBar$*.class' 'org/eclipse/swt/widgets/ExpandItem.class' 'org/eclipse/swt/widgets/ExpandItem$*.class' \
  'org/eclipse/swt/widgets/ViewportPlane.class' 'org/eclipse/swt/widgets/ViewportPlane$*.class' -d "$COV/classes"
A="-javaagent:$AGENT=destfile=$COV/aw.exec,includes=$INCLUDES"
R="-Xmx1g -Dswt.viewport.recording=$COV/rec -Dswt.viewport.evidence=$COV/evidence"
H="bash C:/m3-superset/tools/hidden_java.sh"
bash C:/m3-superset/tools/ui_lock.sh swt-allwidgets bash -c "
$H 900 '$COV/record-official.log' $R -jar '$J' execute -cp '$OUT/tests;$OFFICIAL' $DIFF --disable-banner --details=summary
$H 1500 '$COV/viewport.log' $R '$A' -jar '$J' execute -cp '$OUT/tests;$JAR' $OWN $STOCK $DIFF --disable-banner --details=tree
$H 900 '$COV/original.log' $R -Dswt.widget.mode=original '$A' -jar '$J' execute -cp '$OUT/tests;$JAR' $STOCK $DIFF $GAPS --disable-banner --details=tree
$H 900 '$COV/expandbar-original.log' $R -Dswt.widget.mode.ExpandBar=original '$A' -jar '$J' execute -cp '$OUT/tests;$JAR' --select-class $T.Test_org_eclipse_swt_widgets_ExpandBar --select-class $T.Test_org_eclipse_swt_widgets_ExpandBar_ViewportDifferential $GAPS --disable-banner --details=tree
"
for m in record-official viewport original expandbar-original; do
  echo "== $m: $(sed 's/\x1b\[[0-9;]*m//g' "$COV/$m.log" | grep -E "tests (successful|failed|aborted)" | tr -s ' []' ' ' | tr '\n' ';')"
  sed 's/\x1b\[[0-9;]*m//g' "$COV/$m.log" | grep -E "✘" | cut -c1-300 | head -10
done
"$JDK/bin/java" -jar "$CLI" report "$COV/aw.exec" --classfiles "$COV/classes" --sourcefiles "$SRC/common" --sourcefiles "$SRC/win32" \
  --csv "$DOC/jacoco.csv" --xml "$COV/jacoco.xml" --html "$COV/report" --name "all-widgets branch ($STAMP)" > /dev/null
sed -i 's/\r$//' "$DOC/jacoco.csv"
# every line the branch adds or changes (new side of the diff), as <package path>/<file> <line>
for f in "${FILES[@]}"; do
  git -C "$WT" diff -U0 "$BASE" HEAD -- "bundles/org.eclipse.swt/Eclipse SWT/$f" | awk -v key="${f#*/}" '
    /^@@/ { split ($3, n, ","); start = substr (n[1], 2); count = (2 in n) ? n[2] : 1; for (i = 0; i < count; i++) print key, start + i }'
done > "$COV/changed.txt"
# the JaCoCo line records of the measured source files, joined with the changed lines
sed 's/></>\n</g' "$COV/jacoco.xml" | awk '
  FNR == NR { changed[$1 " " $2] = 1; next }
  /^<package / { match ($0, /name="[^"]*"/); pkg = substr ($0, RSTART + 6, RLENGTH - 7) }
  /^<sourcefile / { match ($0, /name="[^"]*"/); file = pkg "/" substr ($0, RSTART + 6, RLENGTH - 7) }
  /^<line / && file != "" {
    split ($0, a, "\""); key = file " " a[2];
    if (key in changed) printf "%s\t%d\t%d\t%d\t%d\t%d\n", file, a[2], a[4], a[6], a[8], a[10];
  }' "$COV/changed.txt" - | sort -k1,1 -k2,2n > "$COV/lines.tsv"
{ printf "file\tline\tmissed_instructions\tcovered_instructions\tmissed_branches\tcovered_branches\n"; cat "$COV/lines.tsv"; } > "$DOC/changed-lines.tsv"
echo "base $BASE"
awk -F'\t' '{
  f = $1; sub (/.*\//, "", f); lines[f]++; if ($4 > 0) cl[f]++; mb[f] += $5; cb[f] += $6; L++; if ($4 > 0) C++; MB += $5; CB += $6;
  if ($4 == 0 || $5 > 0) miss[f] = miss[f] " " $2 ($4 == 0 ? "" : "b");
} END {
  for (f in lines) printf "%-28s changed lines %3d/%-3d  branches %3d/%-3d  missed:%s\n", f, cl[f], lines[f], cb[f], mb[f] + cb[f], miss[f];
  printf "%-28s changed lines %3d/%-3d (%5.1f%%)  branches %3d/%-3d (%5.1f%%)\n", "TOTAL", C, L, 100 * C / L, CB, MB + CB, 100 * CB / (MB + CB);
}' "$COV/lines.tsv"
awk -F, 'NR > 1 && $3 !~ /\./ { printf "%-28s class lines %4d/%-4d  branches %4d/%-4d\n", $3, $9, $8 + $9, $7, $6 + $7 }' "$DOC/jacoco.csv"
