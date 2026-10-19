#!/usr/bin/env bash
# Print the commit headroom (free virtual memory) in kB; 0 when no probe answers.
# Every probe runs under a hard timeout: a hung WMI query (Get-CimInstance) must never block the lock queues.
kb=$(timeout -s KILL 45 powershell -NoProfile -Command '(Get-CimInstance Win32_OperatingSystem).FreeVirtualMemory' 2>/dev/null | tr -d '\r ')
case "$kb" in ''|*[!0-9]*) kb= ;; esac
if [ -z "$kb" ]; then
  # PDH fallback (no WMI): commit limit minus committed bytes
  kb=$(timeout -s KILL 30 typeperf -sc 1 "\Memory\Commit Limit" "\Memory\Committed Bytes" 2>/dev/null \
    | awk -F'","' 'NR==3 { gsub(/"/, "", $3); gsub(/"/, "", $2); printf "%d", ($2 - $3) / 1024 }')
fi
case "$kb" in ''|*[!0-9]*) kb=0 ;; esac
echo "$kb"
