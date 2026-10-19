#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# Records the stock reference of a widget's recorded differential with the official SWT 3.135.0 jar (once), then
# runs the differential of this build in each requested mode against it. Run under ui_lock.sh (hidden desktop).
# Usage: tree_differential_modes.sh <swt-jar> <tests-dir> <run-dir> <test-class> <modes...>
# Environment: EXTRA_CP (Skija jars for skija and gpu), BUDGET seconds per JVM (default 900).
JAR=$1; TESTS=$2; RUN=$3; CLASS=$4; shift 4
BUDGET=${BUDGET:-900}
OFFICIAL=$(cygpath -w ~/.m2/repository/org/eclipse/platform/org.eclipse.swt.win32.win32.x86_64/3.135.0/org.eclipse.swt.win32.win32.x86_64-3.135.0.jar)
CS=$(cygpath -w ~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
REC="$RUN/recording"; EVI="$RUN/evidence"; mkdir -p "$EVI"
W() { cygpath -w "$1"; }
if [ ! -d "$REC" ] || [ -z "$(ls "$REC" 2>/dev/null)" ]; then
  bash C:/m3-superset/tools/hidden_java.sh "$BUDGET" "$RUN/official.log" -Xmx1g -Dswt.widget.mode=original \
    -Dswt.viewport.evidence="$(W "$EVI")" -Dswt.viewport.recording="$(W "$REC")" -jar "$CS" execute \
    -cp "$(W "$TESTS");$OFFICIAL" --select-class "org.eclipse.swt.tests.junit.$CLASS" --disable-banner --details=tree
  echo "official: $(sed 's/\x1b\[[0-9;]*m//g' "$RUN/official.log" | grep -E 'tests (successful|failed|aborted)' | tr '\n' ' ')"
fi
for mode in "$@"; do
  bash C:/m3-superset/tools/hidden_java.sh "$BUDGET" "$RUN/$mode.log" -Xmx1g -Dswt.widget.mode=$mode \
    -Dswt.viewport.evidence="$(W "$EVI")" -Dswt.viewport.recording="$(W "$REC")" -Dswt.viewport.similarity="$(W "$RUN/similarity.tsv")" \
    -jar "$CS" execute -cp "$(W "$TESTS");$(W "$JAR")${EXTRA_CP:+;$EXTRA_CP}" --select-class "org.eclipse.swt.tests.junit.$CLASS" --disable-banner --details=tree
  echo "$mode: $(sed 's/\x1b\[[0-9;]*m//g' "$RUN/$mode.log" | grep -E 'tests (successful|failed|aborted|skipped)' | tr '\n' ' ')"
done
