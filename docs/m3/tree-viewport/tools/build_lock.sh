#!/usr/bin/env bash
# Run a command while holding the C:/m3-superset build lock (FIFO ticket queue): one javac/jar build at a time across agents.
# Usage: build_lock.sh <agent-name> <command...>
AGENT=$1; shift
Q=C:/m3-superset/build.queue; L=C:/m3-superset/build.lock
mkdir -p "$Q"; T="$Q/$(date +%s%N)-$AGENT"; : > "$T"
release() { rm -rf "$L"; rm -f "$T"; }
trap 'rm -f "$T"' EXIT
while :; do
  first=$(ls "$Q" | sort | head -1)
  if [ "$Q/$first" = "$T" ] && mkdir "$L" 2>/dev/null; then break; fi
  sleep 5
done
trap release EXIT INT TERM
echo "$AGENT $(date -Iseconds)" > "$L/owner.txt"
for try in $(seq 1 45); do
  kb=$(bash C:/m3-superset/tools/commit_free_kb.sh)
  [ "$kb" -ge 4194304 ] && break
  [ "$try" = 1 ] && echo "memory gate: headroom $((kb/1048576)) GB, waiting (up to 15 min)"
  sleep 20
done
[ "$kb" -ge 4194304 ] || { echo "memory gate: headroom $((kb/1048576)) GB < 4 GB"; exit 4; }
"$@"
