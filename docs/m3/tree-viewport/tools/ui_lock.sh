#!/usr/bin/env bash
# Run a command while holding one of the C:/m3-superset desktop slots (FIFO ticket queue, see UI_LOCK_PROTOCOL.md).
# Each slot is its own invisible desktop (M3_UI_DESKTOP, read by hidden_java.sh), so up to M3_UI_SLOTS runs
# (default 3) proceed at once without sharing windows, focus or input state.
# Usage: ui_lock.sh <agent-name> <command...>
AGENT=$1; shift
Q=C:/m3-superset/ui-test.queue; L=C:/m3-superset/ui-test.lock
mkdir -p "$Q"; T="$Q/$(date +%s%N)-$AGENT"; : > "$T"
SLOTS=${M3_UI_SLOTS:-3}
trap 'rm -f "$T"' EXIT
while :; do
  first=$(ls "$Q" | sort | head -1)
  if [ "$Q/$first" = "$T" ] && [ ! -d "$L" ]; then
    for s in $(seq 0 $((SLOTS - 1))); do
      if mkdir "$L.$s" 2>/dev/null; then SLOT=$s; break 2; fi
    done
  fi
  sleep 5
done
release() { rm -rf "$L.$SLOT"; rm -f "$T"; }
trap release EXIT INT TERM
# the slot is held: leave the queue so the next ticket can take another slot
rm -f "$T"
echo "$AGENT $(date -Iseconds)" > "$L.$SLOT/owner.txt"
export M3_UI_DESKTOP=m3-ui-tests-$SLOT
for try in $(seq 1 45); do
  kb=$(bash C:/m3-superset/tools/commit_free_kb.sh)
  [ "$kb" -ge 6291456 ] && break
  [ "$try" = 1 ] && echo "memory gate: headroom $((kb/1048576)) GB, waiting (up to 15 min)"
  sleep 20
done
[ "$kb" -ge 6291456 ] || { echo "memory gate: headroom $((kb/1048576)) GB < 6 GB"; exit 4; }
"$@"
