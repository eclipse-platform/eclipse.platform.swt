#!/bin/sh
# usage: run-tests.sh <base|after> <tag> [jvm args...] -- <TestClass>...
cd /c/m3-work-c/lc-bench
v=$1; tag=$2; shift 2
jvm=""; while [ $# -gt 0 ] && [ "$1" != "--" ]; do jvm="$jvm $1"; shift; done; [ $# -gt 0 ] && shift
sel=""; for c in "$@"; do sel="$sel --select-class=org.eclipse.swt.tests.junit.$c"; done
J="/c/Program Files/Java/jdk-21/bin"
JU=$(cygpath -w /c/Users/hsoliwal/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
mkdir -p reports/$tag
sh scripts/gated.sh 1200 true || exit 1
t0=$(date +%s)
timeout ${TEST_TIMEOUT:-900} "$J/java" -ea $jvm -Dswt.library.path="$(cygpath -w natives)" -jar "$JU" execute \
  --class-path="$(cygpath -w $v-classes);$(cygpath -w tests-$v)" --reports-dir="$(cygpath -w reports/$tag)" \
  --details=tree --disable-banner $sel > logs/test-$tag.log 2>&1
echo "$tag exit=$? secs=$(( $(date +%s) - t0 )) $(grep -E 'tests (successful|failed|found)' logs/test-$tag.log | tr -s ' ' | tr '\n' ' ')"
