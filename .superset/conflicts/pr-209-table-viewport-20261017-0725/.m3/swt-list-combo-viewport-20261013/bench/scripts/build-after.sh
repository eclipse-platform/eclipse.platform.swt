#!/bin/sh
cd /c/m3-work-c/lc-bench
sh scripts/gated.sh 1200 true || exit 1
timeout 900 sh scripts/build-swt.sh /c/m3-work-c/wt/swt-list-combo after-classes > logs/after-build.log 2>&1
echo "exit=$?" >> logs/after-build.log
grep -E "error:" -A2 logs/after-build.log | head -60 > logs/after-build-errors.log
