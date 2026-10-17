#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
#
# Summarises a JaCoCo report of table_coverage.sh: line and branch coverage per class of the Table viewport, and
# the coverage of the lines this fork adds to the upstream files (Table, TableItem, TableColumn, ScrollBar) against
# the stock tag R4_38. Usage: table_changed_coverage.sh <worktree> <coverage-dir>
# Writes <coverage-dir>/classes.tsv, changed-lines.tsv and summary.txt.
WT=$1; C=$2
W="bundles/org.eclipse.swt/Eclipse SWT/win32/org/eclipse/swt/widgets"
# per class: CSV columns GROUP,PACKAGE,CLASS,INSTRUCTION_MISSED,INSTRUCTION_COVERED,BRANCH_MISSED,BRANCH_COVERED,LINE_MISSED,LINE_COVERED,...
awk -F, 'BEGIN {OFS="\t"; print "class", "lines_covered", "lines", "line_pct", "branches_covered", "branches", "branch_pct"}
  NR > 1 && $2 == "org.eclipse.swt.widgets" && $3 ~ /^(Table|VirtualListWidth|VirtualCellLanes|VirtualBitLanes|ScrollBar)/ {l = $8 + $9; b = $6 + $7; print $3, $9, l, (l ? sprintf ("%.1f", 100 * $9 / l) : "-"), $7, b, (b ? sprintf ("%.1f", 100 * $7 / b) : "-")}' "$C/coverage.csv" | sort > "$C/classes.tsv"
# per source line of the report: file, line, missed and covered instructions, missed and covered branches
awk 'BEGIN {RS = "<"} /^sourcefile name=/ {match ($0, /name="[^"]*"/); file = substr ($0, RSTART + 6, RLENGTH - 7)}
  /^line nr=/ {split ($0, a, "\""); print file "\t" a[2] "\t" a[4] "\t" a[6] "\t" a[8] "\t" a[10]}' "$C/coverage.xml" > "$C/lines.tsv"
printf 'file\tchanged_lines\tcode_lines\tlines_covered\tbranches\tbranches_covered\tmissed_lines\n' > "$C/changed-lines.tsv"
for f in Table TableItem TableColumn ScrollBar; do
  git -C "$WT" diff -U0 R4_38 HEAD -- "$W/$f.java" | awk '/^\+\+\+/ {next} /^@@/ {split ($3, a, ","); n = substr (a[1], 2) + 0; next} /^\+/ {print n; n++}' > "$C/changed-$f.txt"
  awk -F'\t' -v file="$f.java" 'NR == FNR {changed[$1] = 1; total++; next}
    $1 == file && ($2 in changed) {code++; if ($4 > 0) covered++; else missed = missed (missed ? "," : "") $2; branches += $5 + $6; bcovered += $6}
    END {printf "%s\t%d\t%d\t%d\t%d\t%d\t%s\n", file, total, code, covered, branches, bcovered, (missed ? missed : "-")}' "$C/changed-$f.txt" "$C/lines.tsv" >> "$C/changed-lines.tsv"
done
{
  echo "== classes"
  column -t -s $'\t' "$C/classes.tsv"
  echo "== lines added to upstream files (against R4_38)"
  column -t -s $'\t' "$C/changed-lines.tsv"
} > "$C/summary.txt"
cat "$C/summary.txt"
