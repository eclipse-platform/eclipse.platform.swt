#!/bin/sh
# Runs StyledText JUnit classes headless on this host against a classes dir that
# shadows the SWT jar's org.eclipse.swt.custom package.
# usage: run-tests.sh <classesDir> <testClassesDir> <reportDir> [extra JVM args...] -- <TestClass>...
# env:   SWT_JAR (unsigned SWT win32 jar), JAVA_HOME (JDK 21), JUNIT_STANDALONE (console launcher jar)
set -eu
classes=$1; tests=$2; reports=$3; shift 3
jvm=""
while [ $# -gt 0 ] && [ "$1" != "--" ]; do jvm="$jvm $1"; shift; done
[ $# -gt 0 ] && shift
w() { cygpath -w "$1" 2>/dev/null || echo "$1"; }
selects=""
for c in "$@"; do selects="$selects --select-class=org.eclipse.swt.tests.junit.$c"; done
mkdir -p "$reports"
# shellcheck disable=SC2086
"$JAVA_HOME/bin/java" -ea $jvm -jar "$(w "$JUNIT_STANDALONE")" execute \
	--class-path="$(w "$classes");$(w "$tests");$(w "$SWT_JAR")" \
	--reports-dir="$(w "$reports")" --details=summary --disable-banner $selects
