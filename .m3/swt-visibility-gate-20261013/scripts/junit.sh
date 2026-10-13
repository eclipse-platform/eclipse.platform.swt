#!/bin/sh
# usage: junit.sh <before|after> <out-prefix> <TestClass...>; compiles selected SWT JUnit classes against the mode's classes and runs them (memory gated, one JVM).
G=/c/m3-work-c/visgate; J="/c/Program Files/Java/jdk-21/bin"
T="/c/m3-work-c/wt/swt-visibility-gate/tests/org.eclipse.swt.tests"
JU=$(cygpath -w ~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
m=$1; out=$2; shift 2
case $m in before) C=$G/base-classes;; after) C=$G/after-classes;; esac
TC=$G/test-classes-$m; rm -rf "$TC"; mkdir -p "$TC"
args=$out.javac.args; : > "$args"; sel=""
for c in "$@"; do printf '"%s"\n' "$(cygpath -m "$T/JUnit Tests/org/eclipse/swt/tests/junit/$c.java")" >> "$args"; sel="$sel --select-class=org.eclipse.swt.tests.junit.$c"; done
"$J/javac" -J-Xmx1g -nowarn -encoding UTF-8 -proc:none -implicit:class -cp "$(cygpath -w $C);$JU" -sourcepath "$(cygpath -w "$T/JUnit Tests");$(cygpath -w $G/test-support)" -d "$(cygpath -w $TC)" @"$(cygpath -w $args)" > $out.javac.log 2>&1 || { echo COMPILE-FAIL; tail -20 $out.javac.log; exit 2; }
cp -r "$T/data/." "$TC/" 2>/dev/null
sh $G/scripts/gate.sh > /dev/null || { echo LOWMEM; exit 3; }
timeout 1500 "$J/java" -Xmx1g -Dswt.library.path="$(cygpath -w $G/natives)" -jar "$JU" execute -cp "$(cygpath -w $C);$(cygpath -w $TC)" $sel --details=summary --disable-banner --reports-dir="$(cygpath -w $out-reports)" > $out.log 2>&1
echo "EXIT=$?" >> $out.log
tail -25 $out.log
