#!/bin/sh
# before/after measurement matrix; one JVM per row, memory-gated; appends to results tsv
cd /c/m3-work-c/lc-bench
J="/c/Program Files/Java/jdk-21/bin"
OUT=$(cygpath -w results/results-before-after.tsv)
mkdir -p results
[ -s results/results-before-after.tsv ] || printf 'label\titems\tfeed\tsetItems_ms\tfirst_paint_ms\tscroll_end_ms\tdrag_p50_ms\tdrag_p95_ms\tdrag_max_ms\theap_delta_mb\tprivate_delta_mb\thandles_delta\tgdi_delta\tuser_delta\tset_data_calls\tcount_check\tselectAll_ms\tgetSelectionCount_ms\tgetSelectionIndices_ms\n' > results/results-before-after.tsv
for n in ${SIZES:-100000 1000000 10000000}; do
  for job in "before base setItems" "after after setItems" "after-setdata after setData"; do
    set -- $job
    sh scripts/gated.sh 2400 true > /dev/null || { echo "$1 $n GATE-TIMEOUT"; continue; }
    t0=$(date +%s)
    timeout ${BENCH_TIMEOUT:-1500} "$J/java" -Xmx${XMX:-2g} -Dswt.library.path="$(cygpath -w natives)" -cp "$(cygpath -w bench-$2);$(cygpath -w $2-classes)" ListViewportBench $1 $n $3 "$OUT" > results/$1-$n.log 2>&1
    echo "$1 $n exit=$? secs=$(( $(date +%s) - t0 ))"
  done
done
echo BENCH-DONE
