#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# Compiles the SWT JUnit tests (with resources) against a built jar, and proves the result complete: no javac error,
# no JVM out-of-memory line, one top-level class per source and every resource copied. "errors=0" alone is not proof:
# a javac that dies in a memory storm (paging file too small) prints no ": error:" line and leaves an empty tree.
# Already complete: exits at once. Else: bounded wait for commit headroom, then up to 5 tries.
# Usage: compile_tests.sh <jar> <tests-dir>      Environment: WT (worktree), XMX (javac heap, default 1536m)
JAR=$1; DIR=$2
WT=${WT:-K:/m3-work/agents/render-modes/wt}
JDK="C:/Program Files/Java/jdk-21"
J=$(cygpath -w "$HOME/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar")
SRC="$WT/tests/org.eclipse.swt.tests"
LIST="$DIR.src.txt"; LOG="$DIR.javac.log"
roots () { printf '%s\n' "JUnit Tests" data; }
sources () { (cd "$SRC" && roots | while IFS= read -r r; do find "$r" -name '*.java'; done | grep -v '/performance/'); }
resources () { (cd "$SRC" && roots | while IFS= read -r r; do (cd "$r" && find . -type f ! -name '*.java'); done); }
complete () {
  [ -s "$LIST" ] && [ -f "$LOG" ] || return 1
  grep -qE ': error:|insufficient memory|paging file' "$LOG" && return 1
  [ "$(find "$DIR" -name '*.class' ! -name '*$*' | wc -l)" -ge "$(wc -l < "$LIST")" ] || return 1
  [ -z "$(resources | while IFS= read -r f; do [ -f "$DIR/$f" ] || echo "$f"; done)" ]
}
complete && { echo "tests complete: $DIR"; exit 0; }
for try in 1 2 3 4 5; do
  for i in $(seq 1 45); do [ "$(bash C:/m3-superset/tools/commit_free_kb.sh)" -ge 4718592 ] && break; sleep 20; done
  { sources | sed "s#^#$SRC/#"; echo C:/m3-superset/test-stubs/org/eclipse/test/Screenshots.java; } | sed 's#.*#"&"#' > "$LIST"
  rm -rf "$DIR" && mkdir -p "$DIR"
  "$JDK/bin/javac" -J-Xmx${XMX:-1536m} --release 21 -encoding UTF-8 -nowarn -proc:none -cp "$JAR;$J" -d "$DIR" @"$LIST" > "$LOG" 2>&1
  (cd "$SRC" && roots | while IFS= read -r r; do (cd "$r" && find . -type f ! -name '*.java' -exec cp --parents {} "$DIR/" \;); done)
  complete && { echo "tests complete: $DIR (try $try)"; exit 0; }
  echo "tests try $try incomplete: $(grep -cE ': error:' "$LOG") errors, $(grep -cE 'insufficient memory|paging file' "$LOG") memory lines"
done
echo "FAILED: tests incomplete after 5 tries ($LOG)"; exit 1
