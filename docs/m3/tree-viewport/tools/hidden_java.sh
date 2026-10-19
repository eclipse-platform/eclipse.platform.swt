#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# Runs java on the invisible test desktop (run_hidden_desktop.ps1): no mouse moves, no focus theft.
# Usage: hidden_java.sh <timeout-seconds> <log-file> <java arguments...>
TIMEOUT=$1; LOG=$(cygpath -w "$2"); shift 2
CMD="\"C:\Program Files\Java\jdk-21\bin\java.exe\""
for a in "$@"; do case "$a" in *" "*|*";"*) CMD="$CMD \"$a\"" ;; *) CMD="$CMD $a" ;; esac; done
powershell -NoProfile -ExecutionPolicy Bypass -File "$(cygpath -w C:/m3-superset/tools/run_hidden_desktop.ps1)" -TimeoutSec "$TIMEOUT" -Log "$LOG" -CommandLine "$CMD" -Desktop "${M3_UI_DESKTOP:-m3-ui-tests}"
