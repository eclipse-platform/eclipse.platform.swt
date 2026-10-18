#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
#
# JaCoCo line and branch coverage (brief rule 15) of the List and Combo viewport classes and of the upstream
# List and Combo classes they hook into, over: the recorded differentials (List 3 styles, resize and font,
# DPI; Combo READ_ONLY and editable) against the official SWT 3.135.0 recordings, the List and Combo suites,
# the acceptance and plan tests in the viewport mode, and the List and Combo suites in the original mode.
# Every UI JVM runs on a hidden desktop (list_viewport_run.sh -> ui_lock.sh + hidden_java.sh).
# Pattern: K:/m3-work/logs/tree_coverage.sh.
# Usage: list_coverage.sh <out-dir> <stamp>   (jar <out-dir>/dist/...-m3-<stamp>.jar, tests in <out-dir>/tests)
# M3_RUNS (recordings, see list_viewport_run.sh) and M3_COV (JaCoCo data and reports) move the output to a scratch disk.
OUT=$1; STAMP=$2
HERE=$(cd "$(dirname "$0")" && pwd)
export JAVA_HOME="C:/Program Files/Java/jdk-21"
M=~/.m2/repository/org/jacoco
AGENT=$(cygpath -w $M/org.jacoco.agent/0.8.15/org.jacoco.agent-0.8.15-runtime.jar)
CLI=$(cygpath -w $M/org.jacoco.cli/0.8.15/org.jacoco.cli-0.8.15-nodeps.jar)
COV=${M3_COV:-K:/m3-work/runs/coverage/list}; mkdir -p $COV
EXEC=$(cygpath -w $COV/list.exec); rm -f $COV/list.exec
INC='org.eclipse.swt.widgets.List:org.eclipse.swt.widgets.List$*:org.eclipse.swt.widgets.ListViewport*:org.eclipse.swt.widgets.Combo:org.eclipse.swt.widgets.Combo$*:org.eclipse.swt.widgets.ComboViewport*:org.eclipse.swt.widgets.VirtualRowWindowPlan*'
JAC="-javaagent:$AGENT=destfile=$EXEC,append=true,includes=$INC"
T=org.eclipse.swt.tests.junit
RUN="bash $HERE/list_viewport_run.sh $OUT $STAMP"
JOPTS="$JAC" TAG=-cov $RUN viewport
JOPTS="$JAC" TAG=-cov RECNAME=list-resize $RUN viewport "--select-method $T.Test_org_eclipse_swt_widgets_List_ViewportDifferential#resizeAndFontWhileScrolledAnswerLikeTheReference"
JOPTS="$JAC -Dswt.autoScale.updateOnRuntime=true" TAG=-cov RECNAME=list-zoom $RUN viewport "--select-method $T.Test_org_eclipse_swt_widgets_List_ViewportDifferential#zoomWhileScrolledAnswersLikeTheReference"
JOPTS="$JAC" TAG=-cov WIDGET=Combo $RUN viewport
SUITES="--select-class $T.Test_org_eclipse_swt_widgets_List --select-class $T.Test_org_eclipse_swt_widgets_List_Viewport --select-class $T.Test_org_eclipse_swt_widgets_List_Virtual --select-class $T.Test_org_eclipse_swt_widgets_Combo"
JOPTS="$JAC" TAG=-cov-suites RECNAME=list-suites $RUN viewport "$SUITES --select-class $T.Test_org_eclipse_swt_widgets_List_ViewportAcceptance --select-class $T.Test_org_eclipse_swt_widgets_Combo_ViewportAcceptance --select-class $T.Test_VirtualRowWindowPlan"
JOPTS="$JAC -Dswt.widget.mode=original" TAG=-cov-original RECNAME=list-suites $RUN viewport "$SUITES"
"$JAVA_HOME/bin/java" -jar "$CLI" report "$EXEC" --classfiles "$(cygpath -w $OUT/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar)" \
	--csv "$(cygpath -w $COV/coverage.csv)" --xml "$(cygpath -w $COV/coverage.xml)" --html "$(cygpath -w $COV/html)" \
	--sourcefiles "$(cygpath -w "$HERE/../../../bundles/org.eclipse.swt/Eclipse SWT/win32")" \
	--sourcefiles "$(cygpath -w "$HERE/../../../bundles/org.eclipse.swt/Eclipse SWT/common")" > $COV/report.log 2>&1
# per class (inner classes folded into their file), then every line with a missed instruction or branch
awk -F, 'NR>1 && $2=="org.eclipse.swt.widgets" && $3 ~ /^(List|ListViewport|Combo|ComboViewport|VirtualRowWindowPlan)(\.|$)/ {
	c=$3; sub(/\..*/, "", c); ml[c]+=$8; cl[c]+=$9; mb[c]+=$6; cb[c]+=$7 }
	END { for (c in ml) printf "%-22s line %6.2f%% (%d/%d)  branch %6.2f%% (%d/%d)\n", c, 100*cl[c]/(ml[c]+cl[c]), cl[c], ml[c]+cl[c],
		(mb[c]+cb[c] ? 100*cb[c]/(mb[c]+cb[c]) : 100), cb[c], mb[c]+cb[c] }' $COV/coverage.csv | sort > $COV/summary.txt
sed 's/<sourcefile /\n<sourcefile /g; s/<line /\n<line /g' $COV/coverage.xml | awk '
	/^<sourcefile / { match($0, /name="[^"]*"/); f = substr($0, RSTART + 6, RLENGTH - 7) }
	/^<line / && f ~ /^(List|ListViewport|Combo|ComboViewport|VirtualRowWindowPlan)\.java$/ {
		match($0, /nr="[0-9]+"/); nr = substr($0, RSTART + 4, RLENGTH - 5)
		match($0, /mi="[0-9]+"/); mi = substr($0, RSTART + 4, RLENGTH - 5)
		match($0, /mb="[0-9]+"/); mb = substr($0, RSTART + 4, RLENGTH - 5)
		match($0, /cb="[0-9]+"/); cb = substr($0, RSTART + 4, RLENGTH - 5)
		if (mi > 0 || mb > 0) printf "%s:%s %s%s\n", f, nr, (mi > 0 ? "missed" : "partial"), (mb > 0 ? " branches " cb "/" (mb + cb) : "") }' > $COV/missed.txt
cat $COV/summary.txt
grep -h -E "tests (successful|failed)" $OUT/run-$STAMP-*-cov*.log 2>/dev/null | sed 's/\x1b\[[0-9;]*m//g' | sort | uniq -c
