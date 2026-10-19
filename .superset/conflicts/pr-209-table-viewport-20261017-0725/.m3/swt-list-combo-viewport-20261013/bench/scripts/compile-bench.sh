#!/bin/sh
# compiles the bench sources against base-classes and after-classes into bench-base/ and bench-after/
cd /c/m3-work-c/lc-bench
E=/c/m3-work-c/wt/swt-list-combo/.m3/swt-list-combo-viewport-20261013/bench
J="/c/Program Files/Java/jdk-21/bin"
for v in base after; do
  rm -rf bench-$v && mkdir -p bench-$v
  "$J/javac" -J-Xmx384m -nowarn -encoding UTF-8 -d bench-$v -cp "$(cygpath -w $v-classes)" $(for f in "$@"; do cygpath -w "$E/$f"; done) 2>&1 | head -30
  echo "compiled $v: $(ls bench-$v | wc -l) files"
done
