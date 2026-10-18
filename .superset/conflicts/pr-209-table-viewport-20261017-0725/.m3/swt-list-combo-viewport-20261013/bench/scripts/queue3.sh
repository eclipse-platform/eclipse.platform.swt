#!/bin/sh
cd /c/m3-work-c/lc-bench
sh scripts/gated.sh 1800 true > /dev/null || { echo GATE-TIMEOUT; exit 1; }
sh scripts/inc-after.sh
sh scripts/compile-bench.sh ProcStat.java ListComboDifferential.java ListViewportBench.java
sh scripts/run-diff.sh
