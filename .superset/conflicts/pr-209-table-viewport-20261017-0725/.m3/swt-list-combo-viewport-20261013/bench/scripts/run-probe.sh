#!/bin/sh
cd /c/m3-work-c/lc-bench
sh scripts/gated.sh 1800 true || exit 1
J="/c/Program Files/Java/jdk-21/bin"
timeout 300 "$J/java" -Xmx1g -Dswt.library.path="$(cygpath -w natives)" -cp "$(cygpath -w probe-classes);$(cygpath -w base-classes)" NativeListProbe ${1:-10000000} > logs/native-probe.tsv 2> logs/native-probe.err
echo "exit=$?"
