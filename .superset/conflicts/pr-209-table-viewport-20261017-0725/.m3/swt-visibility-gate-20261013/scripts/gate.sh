#!/bin/sh
# Waits (polling every 15 s, max 40 min) until memcheck passes; exit 1 on timeout.
i=0
while ! sh /c/m3-work-c/visgate/scripts/memcheck.sh > /dev/null; do
  i=$((i + 1)); [ $i -gt 160 ] && { echo "GATE-TIMEOUT"; exit 1; }
  powershell -NoProfile -Command "Start-Sleep -Seconds 15" > /dev/null 2>&1
done
sh /c/m3-work-c/visgate/scripts/memcheck.sh
