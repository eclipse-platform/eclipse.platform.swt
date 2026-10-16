#!/usr/bin/env bash
# Run JUnit classes of the Table viewport against a built jar and tests, under the desktop lock and a wall-clock guard.
# Usage: table_viewport_run.sh <out-dir> <stamp> <log-name> <budget-seconds> <jvm-option|-> <junit selectors...>
# The jvm options (space separated, one argument) are, for example, -Dswt.widget.mode.Table=original (stock Table),
# or - for the default (viewport).
OUT=$1; STAMP=$2; NAME=$3; BUDGET=$4; MODE=$5; shift 5
export JAVA_HOME="C:/Program Files/Java/jdk-21"
JAR="$OUT/dist/org.eclipse.swt.win32.win32.x86_64-m3-$STAMP.jar"
CS=$(cygpath -m ~/.m2/repository/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar)
OPTS=(-Xmx1g)
[ "$MODE" = "-" ] || { read -ra EXTRA <<< "$MODE"; OPTS+=("${EXTRA[@]}"); }
bash C:/m3-superset/tools/ui_lock.sh swt-table bash C:/m3-superset/tools/run_guarded.sh "$BUDGET" "$JAVA_HOME/bin/java" "${OPTS[@]}" \
  -jar "$CS" execute -cp "$OUT/tests;$JAR" "$@" --disable-banner --details=tree > "$OUT/$NAME.log" 2>&1
echo "exit=$?"
sed 's/\x1b\[[0-9;]*m//g' "$OUT/$NAME.log" | grep -E "memory gate|GUARD|✘|tests (successful|failed|skipped|aborted)" | head -40
