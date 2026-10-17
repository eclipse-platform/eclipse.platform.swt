#!/bin/sh
# usage: smoke.sh <control|custom> <before|after|after-off> [rounds]; SWT examples harness (memory gated, one JVM).
G=/c/m3-work-c/visgate; J="/c/Program Files/Java/jdk-21/bin"
NAT="-Dswt.library.path=$(cygpath -w $G/natives)"
B=/c/m3-work-c/wt/swt-visibility-gate/.m3/swt-visibility-gate-20261013/bench
E="/c/m3-work-c/wt/swt-visibility-gate/examples/org.eclipse.swt.examples/src"
w=$1; m=$2; n=${3:-10}
case $m in before) C=$G/base-classes; P="";; after) C=$G/after-classes; P="";; after-off) C=$G/after-classes; P="-Dswt.visibility.gate=false";; esac
O=$G/smoke-classes-$m; mkdir -p $O
"$J/javac" -nowarn -encoding UTF-8 -proc:none -cp "$(cygpath -w $C)" -sourcepath "$(cygpath -w "$E")" -d "$(cygpath -w $O)" "$(cygpath -w $B/ExampleSmoke.java)" "$(cygpath -w "$E/org/eclipse/swt/examples/controlexample/CustomControlExample.java")" > $O.javac.log 2>&1 || { echo COMPILE-FAIL; tail -5 $O.javac.log; exit 2; }
(cd "$E" && find . -name '*.properties' -o -name '*.gif' -o -name '*.png' -o -name '*.bmp' -o -name '*.html' -o -name '*.jpg' | while read -r f; do mkdir -p "$O/$(dirname "$f")"; cp "$f" "$O/$f"; done)
sh $G/scripts/gate.sh > /dev/null || { echo "LOWMEM smoke $w $m"; exit 3; }
timeout 300 "$J/java" -Xmx1g $NAT $P -cp "$(cygpath -w $C);$(cygpath -w $O)" ExampleSmoke $w $m $n
