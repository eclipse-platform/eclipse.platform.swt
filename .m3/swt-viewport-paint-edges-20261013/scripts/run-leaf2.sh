#!/bin/sh
# Leaf 2 proof: build leaf-2 classes, JUnit (planes, box model, damage hook, debounce; 100% and -Dswt.autoScale=200),
# leaf-1 gate test on the leaf-2 build, PlanesBench threshold sweep. Desktop lock per test class / per bench run.
G=/c/m3-work-c/visgate; W=/c/m3-work-c/wt/swt-paint-edges; M=$W/.m3/swt-viewport-paint-edges-20261013
J="/c/Program Files/Java/jdk-21/bin"; NAT="-Dswt.library.path=$(cygpath -w $G/natives)"
cd $G; . scripts/lock.sh
sh scripts/gate.sh > /dev/null && rm -rf l2-classes && sh scripts/build-swt.sh $W $G/l2-classes $G/l2-sources.list > l2-build.log 2>&1; tail -1 l2-build.log
JU=$(cygpath -w ~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
T="$W/tests/org.eclipse.swt.tests"; TC=$G/l2-test-classes; rm -rf $TC; mkdir -p $TC
"$J/javac" -J-Xmx1g -nowarn -encoding UTF-8 -proc:none -implicit:class -cp "$(cygpath -w $G/l2-classes);$JU" -sourcepath "$(cygpath -w "$T/JUnit Tests");$(cygpath -w $G/test-support)" -d "$(cygpath -w $TC)" "$(cygpath -w "$T/JUnit Tests/org/eclipse/swt/tests/junit/Test_org_eclipse_swt_internal_ViewportPlanes.java")" "$(cygpath -w "$T/JUnit Tests/org/eclipse/swt/tests/junit/Test_org_eclipse_swt_internal_VisibilityGate.java")" "$(cygpath -w "$T/JUnit Tests/org/eclipse/swt/tests/junit/Test_ViewportDamage.java")" > l2-test-javac.log 2>&1 || { echo TEST-COMPILE-FAIL; tail -20 l2-test-javac.log; exit 2; }
for c in Test_org_eclipse_swt_internal_ViewportPlanes Test_org_eclipse_swt_internal_VisibilityGate Test_ViewportDamage; do
  for z in 100 200; do
    with_lock "$J/java" -Xmx1g $NAT -Dswt.autoScale=$z -jar "$JU" execute -cp "$(cygpath -w $G/l2-classes);$(cygpath -w $TC)" --select-class=org.eclipse.swt.tests.junit.$c --details=summary --disable-banner > l2-$c-$z.log 2>&1
    printf '%s\tzoom %s\t%s\n' $c $z "$(grep -E 'tests (successful|failed)' l2-$c-$z.log | tr -s ' ' | tr '\n' ' ')"
  done
done
BC=$G/l2-bench; mkdir -p $BC; "$J/javac" -nowarn -cp "$(cygpath -w $G/l2-classes)" -d "$(cygpath -w $BC)" "$(cygpath -w $M/bench/PlanesBench.java)"
BB=$G/l2-bench-base; mkdir -p $BB; "$J/javac" -nowarn -cp "$(cygpath -w $G/base-classes)" -d "$(cygpath -w $BB)" "$(cygpath -w $M/bench/PlanesBench.java)"
for n in 1 5 20 100 1000; do
  for m in direct setredraw; do with_lock "$J/java" -Xmx1g $NAT -cp "$(cygpath -w $G/base-classes);$(cygpath -w $BB)" PlanesBench $n $m before; done
  for m in direct setredraw viewport; do with_lock "$J/java" -Xmx1g $NAT -cp "$(cygpath -w $G/l2-classes);$(cygpath -w $BC)" PlanesBench $n $m after; done
done | tee l2-planes.tsv
