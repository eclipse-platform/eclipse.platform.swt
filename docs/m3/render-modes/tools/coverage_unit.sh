#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# Headless JUnit tests of the render-modes drawing core (no display) under the JaCoCo agent, and the coverage report
# of the given execution files. UI tests (the differentials) add their own .exec files through COVERAGE_AGENT.
# Usage: coverage_unit.sh <swt-jar> <tests-dir> <out-dir> [more .exec files...]
JAR=$1; TESTS=$2; OUT=$3; shift 3
JDK="C:/Program Files/Java/jdk-21"
M2=$(cygpath -m ~/.m2/repository)
AGENT=$M2/org/jacoco/org.jacoco.agent/0.8.14/org.jacoco.agent-0.8.14-runtime.jar
CS=$M2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar
JC="$M2/org/jacoco/org.jacoco.core/0.8.14/org.jacoco.core-0.8.14.jar;$M2/org/jacoco/org.jacoco.report/0.8.14/org.jacoco.report-0.8.14.jar;$M2/org/ow2/asm/asm/9.9/asm-9.9.jar;$M2/org/ow2/asm/asm-commons/9.9/asm-commons-9.9.jar;$M2/org/ow2/asm/asm-tree/9.9/asm-tree-9.9.jar"
INCLUDES="org.eclipse.swt.internal.render.*:org.eclipse.swt.widgets.*DrawnPlane"
mkdir -p "$OUT"
rm -f "$OUT/unit.exec"
"$JDK/bin/java" -Xmx512m "-javaagent:$AGENT=destfile=$(cygpath -m "$OUT")/unit.exec,includes=$INCLUDES" -jar "$CS" execute \
  -cp "$(cygpath -m "$TESTS");$(cygpath -m "$JAR")${M3_CP:+;$M3_CP}" --select-class org.eclipse.swt.tests.junit.Test_org_eclipse_swt_internal_render_RowRenderer \
  --select-class org.eclipse.swt.tests.junit.Test_org_eclipse_swt_internal_render_Themes --select-class org.eclipse.swt.tests.junit.Test_org_eclipse_swt_internal_render_Win32Backends --select-class org.eclipse.swt.tests.junit.Test_org_eclipse_swt_internal_render_M3Canvas ${EXTRA_SELECT:-} --disable-banner --details=summary > "$OUT/unit.log" 2>&1
sed 's/\x1b\[[0-9;]*m//g' "$OUT/unit.log" | grep -E "tests (successful|failed)|✘|=>" | head -12
"$JDK/bin/javac" -cp "$JC" -d "$OUT/tool" "$(cygpath -m "$(dirname "$0")")/CoverageReport.java" || exit 1
"$JDK/bin/java" -cp "$OUT/tool;$JC" CoverageReport "$(cygpath -m "$OUT")/coverage.tsv" \
  "org/eclipse/swt/internal/render/,org/eclipse/swt/widgets/DrawnPlane,org/eclipse/swt/widgets/TreeDrawnPlane,org/eclipse/swt/widgets/TableDrawnPlane,org/eclipse/swt/widgets/ListDrawnPlane" \
  "$(cygpath -m "$JAR")${M3_JAR:+;$M3_JAR}" "$(cygpath -m "$OUT")/unit.exec" "$@"
