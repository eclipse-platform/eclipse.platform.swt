# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
"""Compares the captures of a theme-switch leg with a leg that started in the target theme (analysis only).

Usage: python -I compare_switch.py <switched-evidence-dir> <started-evidence-dir> <mode> <switch-step>
Captures after the switch (steps >= switch step, and every seed's final capture) must be equal pixel for pixel;
captures before it must differ wherever the two themes paint differently (they are reported, not judged).
"""
import os
import re
import sys

from PIL import Image

switched, started, mode, switch_step = sys.argv[1], sys.argv[2], sys.argv[3], int(sys.argv[4])
pattern = re.compile(r'^(?P<name>.+)-seed(?P<seed>\d+)(-step(?P<step>\d+))?-' + re.escape(mode) + r'\.png$')
after_equal = after_total = before_equal = before_total = 0
mismatches = []
for file in sorted(os.listdir(switched)):
    match = pattern.match(file)
    if not match or not os.path.exists(os.path.join(started, file)):
        continue
    a = Image.open(os.path.join(switched, file)).convert('RGB')
    b = Image.open(os.path.join(started, file)).convert('RGB')
    equal = a.size == b.size and a.tobytes() == b.tobytes()
    step = match.group('step')
    after = step is None or int(step) >= switch_step
    if after:
        after_total += 1
        after_equal += equal
        if not equal:
            mismatches.append(file)
    else:
        before_total += 1
        before_equal += equal
print('%s: after the switch %d/%d captures equal to the started-in-target leg; before it %d/%d equal (expected to differ)'
      % (mode, after_equal, after_total, before_equal, before_total))
for file in mismatches[:20]:
    print('  DIFFERS ' + file)
