#!/usr/bin/env bash
# Compile the SWT JUnit tests of a worktree against an already built SWT jar (run under build_lock.sh).
# Usage: table_tests_build.sh <worktree> <out-dir> <stamp>
WT=$1; OUT=$2; STAMP=$3
JDK="C:/Program Files/Java/jdk-21"
J=$(cygpath -w "$HOME/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar")
JAR="$OUT/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar"
[ -f "$JAR" ] || { echo "no jar $JAR"; exit 1; }
cd "$WT/tests/org.eclipse.swt.tests" || exit 1
{ find "JUnit Tests" data -name '*.java' | grep -v '/performance/'; echo C:/m3-superset/test-stubs/org/eclipse/test/Screenshots.java; } | sed 's#.*#"&"#' > "$OUT/test-src.txt"
rm -rf "$OUT/tests" && mkdir -p "$OUT/tests"
"$JDK/bin/javac" -J-Xmx1536m --release 21 -encoding UTF-8 -nowarn -proc:none -cp "$JAR;$J" -d "$OUT/tests" @"$OUT/test-src.txt" > "$OUT/tests.log" 2>&1
echo "tests javac errors=$(grep -c ': error:' "$OUT/tests.log")"
for root in "JUnit Tests" data; do
  (cd "$root" && find . -type f ! -name '*.java' | while IFS= read -r f; do mkdir -p "$OUT/tests/$(dirname "$f")"; cp "$f" "$OUT/tests/$f"; done)
done
