#!/usr/bin/env bash
# Records the Table differential with both references and compares the viewport run with each, on the
# invisible test desktop (hidden_java.sh). Run it under the desktop lock:
#   bash C:/m3-superset/tools/ui_lock.sh <agent> bash table_differential_references.sh <out-dir> <stamp>
# <out-dir>/tests holds the compiled tests and <out-dir>/dist the jar of this SWT; recordings go to
# <out-dir>/recording/{official,original}, logs to <out-dir>/differential-*.log. The comparison with the official
# recording accepts exactly the steps listed in UPSTREAM_DEFECTS.tsv (upstream_defects.py).
O=$1; STAMP=$2
JAR=$O/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar
OFFICIAL=$(cygpath -m ~/.m2/repository/org/eclipse/platform/org.eclipse.swt.win32.win32.x86_64/3.135.0/org.eclipse.swt.win32.win32.x86_64-3.135.0.jar)
CS=$(cygpath -m ~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
H=C:/m3-superset/tools/hidden_java.sh
# the official answers of upstream defects that the comparison with the official recording accepts
DEFECTS=$(cygpath -m "$(dirname "$0")")/UPSTREAM_DEFECTS.tsv
SEL="--select-class org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Table_ViewportDifferential --disable-banner --details=tree"
R=$O/recording
mkdir -p $R/official $R/original
bash $H 900 $O/differential-record-official.log -Xmx1g -Dswt.viewport.recording=$R/official -Dswt.viewport.evidence=$O/evidence -jar $CS execute -cp "$O/tests;$OFFICIAL" $SEL
bash $H 900 $O/differential-record-original.log -Xmx1g -Dswt.widget.mode.Table=original -Dswt.viewport.recording=$R/original -Dswt.viewport.evidence=$O/evidence -jar $CS execute -cp "$O/tests;$JAR" $SEL
bash $H 900 $O/differential-viewport-vs-official.log -Xmx1g -Dswt.viewport.recording=$R/official -Dswt.viewport.upstreamDefects=$DEFECTS -Dswt.viewport.evidence=$O/evidence -jar $CS execute -cp "$O/tests;$JAR" $SEL
bash $H 900 $O/differential-viewport-vs-original.log -Xmx1g -Dswt.viewport.recording=$R/original -Dswt.viewport.evidence=$O/evidence -jar $CS execute -cp "$O/tests;$JAR" $SEL
for f in record-official record-original viewport-vs-official viewport-vs-original; do
  echo "== $f"
  sed 's/\x1b\[[0-9;]*m//g' $O/differential-$f.log | grep -E "hidden desktop|✘|■|tests (successful|failed|aborted)|recorded:|this run:|differs" | head -8
done
head -1 $R/official/table-variant0-seed0.txt $R/original/table-variant0-seed0.txt 2>/dev/null
