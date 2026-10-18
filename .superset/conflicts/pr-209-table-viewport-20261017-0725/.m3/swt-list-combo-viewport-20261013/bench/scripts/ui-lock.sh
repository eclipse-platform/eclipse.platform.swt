#!/bin/sh
# usage: ui-lock.sh <label> <command...>
# Runs one chunk while holding the desktop lock C:/m3-work-c/ui-test.lock (mkdir is the atomic acquire;
# retry every 30 s; owner.txt names the holder; the trap releases the lock when the chunk ends).
L=/c/m3-work-c/ui-test.lock
label=$1; shift
while ! mkdir "$L" 2>/dev/null; do
  echo "ui-lock busy: $(head -1 "$L/owner.txt" 2>/dev/null); retry in 30 s ($label)"
  sleep 30
done
printf 'owner=list-combo-viewport pid=%s label=%s since=%s\n' "$$" "$label" "$(date '+%F %T')" > "$L/owner.txt"
trap 'rm -f "$L/owner.txt"; rmdir "$L" 2>/dev/null' EXIT INT TERM
"$@"
