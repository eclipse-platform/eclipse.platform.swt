#!/bin/sh
cd /c/m3-work-c/lc-bench
sh scripts/gated.sh 2400 true > /dev/null || { echo GATE-TIMEOUT; exit 1; }
sh scripts/compile-bench.sh ProcStat.java ListComboDifferential.java ListViewportBench.java
sh scripts/run-diff.sh
sh scripts/queue2.sh
SIZES="100000 1000000 10000000" sh scripts/run-bench.sh
echo QUEUE4-DONE
