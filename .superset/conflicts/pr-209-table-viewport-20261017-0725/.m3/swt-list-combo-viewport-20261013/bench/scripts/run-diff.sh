#!/bin/sh
# differential traces: base, after, after with rebuild threshold 0; one JVM at a time, memory-gated
cd /c/m3-work-c/lc-bench
J="/c/Program Files/Java/jdk-21/bin"
NAT="-Dswt.library.path=$(cygpath -w natives)"
mkdir -p traces
run() { tag=$1; cls=$2; shift 2
  sh scripts/gated.sh 1800 true > /dev/null || { echo "$tag GATE-TIMEOUT"; return; }
  t0=$(date +%s)
  timeout 900 "$J/java" -Xmx768m $NAT -cp "$(cygpath -w bench-$cls);$(cygpath -w $cls-classes)" ListComboDifferential "$(cygpath -w traces/$tag.trace)" "$@" > traces/$tag.out 2>&1
  echo "$tag exit=$? secs=$(( $(date +%s) - t0 )) lines=$(wc -l < traces/$tag.trace)"
}
[ "${SKIP_BASE:-0}" = 1 ] || run base base
run after after
run after-t0 after 0
grep -v -E '^defect\.|^nongate\.|state\.eventsNonGate' traces/base.trace > traces/base.gate
for t in after after-t0; do
  grep -v -E '^defect\.|^nongate\.|state\.eventsNonGate' traces/$t.trace > traces/$t.gate
  diff traces/base.gate traces/$t.gate > traces/$t.diff
  echo "$t gate_difflines=$(grep -c '^[<>]' traces/$t.diff)"
done
echo DIFF-DONE
