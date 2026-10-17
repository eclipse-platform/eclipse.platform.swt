#!/bin/sh
cd /c/m3-work-c/lc-bench
J="/c/Program Files/Java/jdk-21/bin"
D=/c/m3-work-c/wt/swt-list-combo/.m3/swt-list-combo-viewport-20261013/evidence
sh scripts/gated.sh 1800 true > /dev/null || { echo GATE-TIMEOUT; exit 1; }
sh scripts/inc-after.sh
sh scripts/compile-bench.sh ProcStat.java ListComboDifferential.java ListViewportBench.java
sh scripts/gated.sh 1800 true > /dev/null && timeout 300 "$J/java" -Xmx256m -Dswt.library.path="$(cygpath -w natives)" -cp "$(cygpath -w probe3);$(cygpath -w base-classes)" NativeShiftProbe > $D/native-shift-probe.tsv 2> logs/shift-probe.err; echo "shift-probe exit=$? rows=$(wc -l < $D/native-shift-probe.tsv)"
sh scripts/gated.sh 1800 true > /dev/null && timeout 120 "$J/java" -Xmx256m -Dswt.library.path="$(cygpath -w natives)" -cp "$(cygpath -w probe3);$(cygpath -w base-classes)" NativeInsertProbe > $D/native-insert-probe.txt 2> logs/insert-probe.err; echo "insert-probe exit=$?"
sh scripts/run-diff.sh
