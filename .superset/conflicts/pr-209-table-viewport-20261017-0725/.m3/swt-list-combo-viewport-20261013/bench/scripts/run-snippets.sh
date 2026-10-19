#!/bin/sh
# compiles and launches the List/Combo/CCombo snippets and the two example applications on one build; each run is cut by timeout
# usage: run-snippets.sh <base|after>
cd /c/m3-work-c/lc-bench
v=$1
J="/c/Program Files/Java/jdk-21/bin"
X=/c/m3-work-c/wt/swt-list-combo/examples
S="$X/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets"
E="$X/org.eclipse.swt.examples/src"
out=snippets-$v; rm -rf $out && mkdir -p $out/classes
files=""
for n in 126 147 217 237 24 26 269 289 332 359 361 365 384 386 39 58 59 65 75; do files="$files $(cygpath -w $S/Snippet$n.java)"; done
"$J/javac" -J-Xmx512m -nowarn -encoding UTF-8 -proc:none -d $out/classes -cp "$(cygpath -w $v-classes)" -sourcepath "$(cygpath -w $E);$(cygpath -w $X/org.eclipse.swt.snippets/src)" $files \
  "$(cygpath -w $E/org/eclipse/swt/examples/controlexample/ControlExample.java)" "$(cygpath -w $E/org/eclipse/swt/examples/controlexample/CustomControlExample.java)" > $out/compile.log 2>&1
echo "compile exit=$? classes=$(find $out/classes -name '*.class' | wc -l)"
(cd "$E" && find . -name '*.properties' -o -name '*.png' -o -name '*.gif' -o -name '*.bmp' | while read -r f; do mkdir -p "/c/m3-work-c/lc-bench/$out/classes/$(dirname "$f")"; cp "$f" "/c/m3-work-c/lc-bench/$out/classes/$f"; done)
for main in $(for n in 126 147 217 237 24 26 269 289 332 359 361 365 384 386 39 58 59 65 75; do echo org.eclipse.swt.snippets.Snippet$n; done) org.eclipse.swt.examples.controlexample.ControlExample org.eclipse.swt.examples.controlexample.CustomControlExample; do
  sh scripts/gated.sh 600 true > /dev/null || { echo "$main GATE-TIMEOUT"; continue; }
  timeout 10 "$J/java" -Xmx256m -Dswt.library.path="$(cygpath -w natives)" -cp "$(cygpath -w $out/classes);$(cygpath -w $v-classes)" $main > $out/${main##*.}.log 2>&1
  rc=$?
  ex=$(grep -c -E "Exception|Error:" $out/${main##*.}.log)
  verdict=FAIL; { [ $rc -eq 124 ] || [ $rc -eq 0 ]; } && [ $ex -eq 0 ] && verdict=PASS
  printf '%s\t%s\trc=%s\texceptions=%s\n' "${main##*.}" "$verdict" "$rc" "$ex"
done
