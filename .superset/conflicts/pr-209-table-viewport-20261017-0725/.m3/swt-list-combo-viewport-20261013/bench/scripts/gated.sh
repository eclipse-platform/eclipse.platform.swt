#!/bin/sh
# usage: gated.sh <max_wait_s> <command...>; waits for the memory gate (>=4 GB free, >=8 GB commit headroom), then runs the command.
max=$1; shift
waited=0
while ! sh /c/m3-work-c/lc-bench/scripts/gate.sh; do
  [ $waited -ge $max ] && { echo "GATE-TIMEOUT after ${waited}s"; exit 3; }
  sleep 20; waited=$((waited+20))
done
echo "gate-ok after ${waited}s $(date +%T)"
"$@"
