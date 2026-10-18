#!/bin/sh
# Prints free physical and commit headroom (MB); exit 1 unless free >= 4 GB and commit headroom >= 8 GB.
out=$(powershell -NoProfile -Command "\$o=Get-CimInstance Win32_OperatingSystem; '{0} {1}' -f [int](\$o.FreePhysicalMemory/1024), [int](\$o.FreeVirtualMemory/1024)")
set -- $out
echo "freeMB=$1 commitHeadroomMB=$2"
[ "$1" -ge 4096 ] && [ "$2" -ge 8192 ]
