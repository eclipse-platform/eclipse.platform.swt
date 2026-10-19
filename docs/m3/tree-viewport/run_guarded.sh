#!/usr/bin/env bash
# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
# Run a command with a hard wall-clock budget; on expiry the whole Windows process tree is killed.
# Usage: run_guarded.sh <seconds> <command...>   (use inside ui_lock.sh so a hung UI never outlives its budget)
BUDGET=$1; shift
"$@" &
pid=$!
winpid=$(cat /proc/$pid/winpid 2>/dev/null)
for ((i = 0; i < BUDGET; i++)); do
  if ! kill -0 "$pid" 2>/dev/null; then
    wait "$pid"
    exit $?
  fi
  sleep 1
done
echo "GUARD: budget of ${BUDGET}s exceeded; killing process tree (pid $pid, winpid ${winpid:-?})" >&2
[ -n "$winpid" ] && taskkill //PID "$winpid" //T //F >/dev/null 2>&1
kill -9 "$pid" 2>/dev/null
wait "$pid" 2>/dev/null
exit 124
