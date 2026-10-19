# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
"""Classify every failed, aborted (assumption) and skipped test of a JUnit console log mechanically.

Usage: python -I regression_triage.py <junit-console-log> [<log-of-other-mode>] > REGRESSION_TRIAGE.tsv
Rules match the failure message (never prose judgement); the first matching rule wins.
Categories: a = environment/desktop/platform (focus, activation, timing, DPI, test order, platform
assumptions), b = genuine upstream SWT bug, c = test bug.
"""
import re
import sys

ANSI = re.compile(r'\x1b\[[0-9;]*m')
RULES = [
    # (message regex, failure type, category, root cause, action)
    (r'Another Display is already associated by this thread', 'SWTException', 'a',
     'test order: Test_org_eclipse_swt_widgets_Display constructs its own Display and must run before any test creates the '
     'default Display on the thread; upstream AllNonBrowserTests runs it first, the regression run selected it after other classes',
     'run Test_org_eclipse_swt_widgets_Display first in its own JVM (scroll_viewport_cycle.sh); test unchanged'),
    (r'Alpha support for foreground colors does not exist', 'assumption', 'a',
     'platform assumption: win32 has no alpha foreground colours (upstream intent)', 'none (by design)'),
    (r'only covered on GTK3|only works on GTK3', 'assumption', 'a',
     'platform assumption: behaviour covered on GTK only (upstream intent)', 'none (by design)'),
    (r'^test_degenerateSizeDoesNotExpandShell.*assumption is not true', 'assumption', 'a',
     'platform assumption: assumeTrue(isGTK && !isGTK4) at Test_org_eclipse_swt_widgets_Shell.java:1098', 'none (by design)'),
    (r'Test works fine locally but fails to get correct focus on automated builds', 'disabled', 'a',
     'upstream @Disabled: focus on automated builds', 'none (upstream decision)'),
    (r'Display#post failed', 'AssertionFailedError', 'a',
     'invisible test desktop: Display.post (SendInput) reaches only the input desktop; UI runs use WinSta0\m3-ui-tests by rule',
     'none on the hidden desktop; covered by an opt-in run on the input desktop'),
    (r'Shell did not activate', 'AssertionFailedError', 'a',
     'desktop activation: Windows foreground rules can refuse SetForegroundWindow to a test shell; intermittent (one of five runs)',
     'documented; no change (the upstream test already waits for activation)'),
    (r'^test_(viewportOriginMirrorsLegacyContentMovementAndLayoutStabilizes|logicalContentSizeKeepsPhysicalContentBounded).*expected: <true> but was: <false>',
     'AssertionFailedError', 'c',
     'fork test asserts ScrollBar.getVisible() on a shell that was never opened; win32 ScrollBar.getVisible reports the native '
     'STATE_SYSTEM_INVISIBLE of the bar (GetScrollBarInfo), which is set while the window is hidden (the Javadoc allows either answer)',
     'open the shell before asserting bar visibility'),
]


def parse(path):
    rows = []
    cls = ''
    with open(path, encoding='utf-8', errors='replace') as f:
        for raw in f:
            line = ANSI.sub('', raw.rstrip('\n'))
            m = re.match(r'^[│ ]*[├└]─ (Test_\S+) [✔✘]', line)
            if m:
                cls = m.group(1)
                continue
            m = re.match(r'^[│ ]*[├└]─ (\S+?(?:\(.*?\)|\.\.\.)) ([✘■↷]) ?(.*)$', line)
            if m:
                outcome = {'✘': 'failed', '■': 'aborted', '↷': 'skipped'}[m.group(2)]
                rows.append((cls, m.group(1), outcome, m.group(3).strip()))
    return rows


def classify(test, message):
    text = test + ' ' + message
    for pattern, kind, category, cause, action in RULES:
        if re.search(pattern, text):
            return kind, category, cause, action
    return 'unclassified', '?', 'no rule matched', 'investigate'


def main():
    rows = parse(sys.argv[1])
    other = {(c, t) for c, t, _, _ in parse(sys.argv[2])} if len(sys.argv) > 2 else None
    print('class\ttest\toutcome\tfailure_type\tmessage\tcategory\troot_cause\taction\tsame_in_other_mode')
    for cls, test, outcome, message in rows:
        kind, category, cause, action = classify(test, message)
        same = '' if other is None else ('yes' if (cls, test) in other else 'no')
        print('\t'.join([cls, test, outcome, kind, message.replace('\t', ' '), category, cause, action, same]))


main()
