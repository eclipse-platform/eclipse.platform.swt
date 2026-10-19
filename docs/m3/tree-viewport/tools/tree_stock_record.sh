#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# Record the stock SWT reference of the Tree differential: the official SWT jar runs the scripts with
# -Dswt.widget.mode=original and writes one file per seed. Usage: tree_stock_record.sh <stamp-of-test-build>
STAMP=$1
export JAVA_HOME="C:/Program Files/Java/jdk-21"
OFFICIAL=$(cygpath -w ~/.m2/repository/org/eclipse/platform/org.eclipse.swt.win32.win32.x86_64/3.135.0/org.eclipse.swt.win32.win32.x86_64-3.135.0.jar)
CS=$(cygpath -w ~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
TT=$(cygpath -w C:/m3-superset/tv/tests)
rm -rf K:/m3-work/runs/tree-stock-recording
bash C:/m3-superset/tools/ui_lock.sh claude-tree bash C:/m3-superset/tools/hidden_java.sh 900 C:/m3-superset/tv/stock-record-$STAMP.log -Xmx1g -Dswt.widget.mode=original -Dswt.viewport.evidence=K:/m3-work/runs/tree-evidence -Dswt.viewport.recording=K:/m3-work/runs/tree-stock-recording -jar "$CS" execute -cp "$TT;$OFFICIAL" --select-class org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_Tree_ViewportDifferential --disable-banner --details=tree
sed 's/\x1b\[[0-9;]*m//g' C:/m3-superset/tv/stock-record-$STAMP.log | grep -E "✘|✔|tests (successful|failed|aborted)|memory gate" | head -8
ls K:/m3-work/runs/tree-stock-recording | wc -l
