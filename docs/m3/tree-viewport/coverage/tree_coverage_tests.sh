#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
#
# Recompiles only the JUnit tests of the coverage worktree against an existing jar (run under build_lock.sh).
# Usage: tree_cov_tests.sh <stamp>
STAMP=$1
JDK="C:/Program Files/Java/jdk-21"; OUT=C:/m3-superset/tvc; WT=C:/m3-superset/swt-tree-cov
J=$(cygpath -w "$HOME/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar")
JAR="$OUT/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar"
cd "$WT/tests/org.eclipse.swt.tests" || exit 1
{ find "JUnit Tests" data -name '*.java' | grep -v '/performance/'; echo C:/m3-superset/test-stubs/org/eclipse/test/Screenshots.java; } | sed 's#.*#"&"#' > "$OUT/test-src.txt"
rm -rf "$OUT/tests" && mkdir -p "$OUT/tests"
"$JDK/bin/javac" -J-Xmx1536m --release 21 -encoding UTF-8 -nowarn -proc:none -cp "$JAR;$J" -d "$OUT/tests" @"$OUT/test-src.txt" > "$OUT/tests.log" 2>&1
echo "tests javac errors=$(grep -c ': error:' "$OUT/tests.log")"
grep -A2 ': error:' "$OUT/tests.log" | head -40
for root in "JUnit Tests" data; do
  (cd "$root" && find . -type f ! -name '*.java' | while IFS= read -r f; do mkdir -p "$OUT/tests/$(dirname "$f")"; cp "$f" "$OUT/tests/$f"; done)
done
