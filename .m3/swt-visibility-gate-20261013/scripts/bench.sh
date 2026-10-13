#!/bin/sh
# usage: bench.sh <scenario> <mode: before|after|after-off> [updates]; memory-gated, one JVM, appends a TSV row.
G=/c/m3-work-c/visgate; J="/c/Program Files/Java/jdk-21/bin"
NAT="-Dswt.library.path=$(cygpath -w /c/m3-work-c/visgate/natives)"
B=/c/m3-work-c/wt/swt-visibility-gate/.m3/swt-visibility-gate-20261013/bench
s=$1; m=$2; n=${3:-100}
case $m in before) C=$G/base-classes; P="";; after) C=$G/after-classes; P="";; after-off) C=$G/after-classes; P="-Dswt.visibility.gate=false";; esac
mkdir -p $G/bench-classes-$m
"$J/javac" -nowarn -cp "$(cygpath -w $C)" -d "$(cygpath -w $G/bench-classes-$m)" "$(cygpath -w $B/VisibilityGateBench.java)" || exit 2
sh $G/scripts/gate.sh > /dev/null || { echo "LOWMEM $s $m"; exit 3; }
timeout 300 "$J/java" -Xmx1g $NAT $P $BENCH_OPTS -cp "$(cygpath -w $C);$(cygpath -w $G/bench-classes-$m)" VisibilityGateBench $s $m $n
