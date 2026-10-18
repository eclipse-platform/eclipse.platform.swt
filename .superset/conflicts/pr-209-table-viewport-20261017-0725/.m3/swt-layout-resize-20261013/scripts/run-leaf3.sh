#!/bin/sh
# Leaf 3 proof: ResizeBench (control sweep, wrap oscillation, stack switching) on stock, leaf-1 and leaf-3 builds.
# Desktop lock per run. Build of leaf-3 classes first (memory gated).
G=/c/m3-work-c/visgate; W=/c/m3-work-c/wt/swt-layout-resize; M=$W/.m3/swt-layout-resize-20261013
J="/c/Program Files/Java/jdk-21/bin"; NAT="-Dswt.library.path=$(cygpath -w $G/natives)"
E="$W/examples/org.eclipse.swt.examples/src"
cd $G; . scripts/lock.sh
sh scripts/gate.sh > /dev/null && rm -rf l3-classes && sh scripts/build-swt.sh $W $G/l3-classes $G/l3-sources.list > l3-build.log 2>&1; tail -1 l3-build.log
for b in base-classes after-classes l3-classes; do
  O=$G/l3-bench-$b; rm -rf $O; mkdir -p $O
  "$J/javac" -nowarn -encoding UTF-8 -proc:none -cp "$(cygpath -w $G/$b)" -sourcepath "$(cygpath -w "$E")" -d "$(cygpath -w $O)" "$(cygpath -w $M/bench/ResizeBench.java)" > $O.log 2>&1 || { echo "COMPILE-FAIL $b"; tail -5 $O.log; }
  (cd "$E" && find . -name '*.properties' -o -name '*.gif' -o -name '*.png' -o -name '*.bmp' -o -name '*.html' -o -name '*.jpg' | while read -r f; do mkdir -p "$O/$(dirname "$f")"; cp "$f" "$O/$f"; done)
done
for r in 1 2 3; do for s in control wrap stack; do for b in base-classes after-classes l3-classes; do
  sh scripts/gate.sh > /dev/null || { echo "LOWMEM $s $b"; continue; }
  with_lock timeout 600 "$J/java" -Xmx1g $NAT -cp "$(cygpath -w $G/$b);$(cygpath -w $G/l3-bench-$b)" ResizeBench $s $b 200
done; done; done | tee l3-resize.tsv
