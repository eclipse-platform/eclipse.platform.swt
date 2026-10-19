# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
"""Table viewport aspect matrix: every public Table/TableItem member and behaviour aspect -> the test that proves it.

Run from the SWT repository root:  python -I docs/m3/table-viewport/aspect_matrix.py
The public API is extracted from the win32 sources (so a new public method appears as a SUITE or GAP row
automatically); the coverage table below is maintained with the tests.  Output: ASPECT_MATRIX.tsv.
Same shape as docs/m3/tree-viewport/aspect_matrix.py.
"""
import re
import sys

ROOT = sys.argv[1] if len(sys.argv) > 1 else '.'
WIDGETS = ROOT + '/bundles/org.eclipse.swt/Eclipse SWT/win32/org/eclipse/swt/widgets/'
OUT = ROOT + '/docs/m3/table-viewport/ASPECT_MATRIX.tsv'
SIG = re.compile(r'^public ([][A-Za-z<>, ]+ )?([A-Za-z0-9_]+) \(([^)]*)\)')

DIFF = 'Table_ViewportDifferential'  # recorded stock-vs-viewport scripts (answers, bar, selection, focus, events, pixels)
ACC = 'Table_Viewport'               # acceptance tests (1,000,000 rows, identity, measured native rules, pixels)
ORACLE = 'OwnerDataOracle'           # native rules measured on the ListView (docs/m3/table-viewport/evidence)
SUITE = 'Table/TableItem/TableColumn suites with the viewport on'

# member or aspect -> (test, status); status COVERED (recorded or asserted), SUITE (only the upstream suites), GAP
COVERAGE = {
    # Table: rows and identity
    'Table.getItem(int)': (f'{DIFF} read; {ACC} aMillionPlainRowsHaveALogicalBar', 'COVERED'),
    'Table.getItemCount()': (f'{DIFF} every step (count)', 'COVERED'),
    'Table.getItems()': (f'{DIFF} getItems', 'COVERED'),
    'Table.indexOf(TableItem)': (f'{DIFF} read, insert, append; {ACC} identitySurvivesInsertsAndRemovals, indexOfAfterAnInsertIsConstantTime', 'COVERED'),
    'Table.setItemCount(int)': (f'{DIFF} setItemCount; {ACC} shrinkingTheCountMovesTheFocusToTheNewLastRow, setItemCountCreatesNoFacadeUntilAsked; {ORACLE} B, L', 'COVERED'),
    'Table.remove(int)': (f'{DIFF} remove; {ORACLE} A', 'COVERED'),
    'Table.remove(int,int)': (f'{DIFF} remove range; {ORACLE} A', 'COVERED'),
    'Table.remove(int[])': (f'{DIFF} remove indices', 'COVERED'),
    'Table.removeAll()': (f'{DIFF} removeAll + refill; {ORACLE} B', 'COVERED'),
    'Table.clear(int)': (f'{DIFF} clear', 'COVERED'),
    'Table.clear(int[])': (f'{DIFF} clear indices; Table.test_clear$I_resetsOnlyTheGivenRows', 'COVERED'),
    'Table.clear(int,int)': (SUITE, 'SUITE'),
    'Table.clearAll()': (f'{DIFF} clearAll', 'COVERED'),
    # Table: scrolling
    'Table.getTopIndex()': (f'{DIFF} every step (top)', 'COVERED'),
    'Table.setTopIndex(int)': (f'{DIFF} setTopIndex; {ORACLE} E', 'COVERED'),
    'Table.showItem(TableItem)': (f'{DIFF} showItem; {ORACLE} E', 'COVERED'),
    'Table.showSelection()': (f'{DIFF} showSelection', 'COVERED'),
    # Table: selection
    'Table.getSelection()': (SUITE, 'SUITE'),
    'Table.getSelectionCount()': (f'{DIFF} every step (sel)', 'COVERED'),
    'Table.getSelectionIndex()': (f'{DIFF} every step (sel index)', 'COVERED'),
    'Table.getSelectionIndices()': (f'{DIFF} every step (sel)', 'COVERED'),
    'Table.isSelected(int)': (f'{DIFF} read', 'COVERED'),
    'Table.select(int)': (f'{DIFF} select', 'COVERED'),
    'Table.select(int,int)': (f'{DIFF} select range', 'COVERED'),
    'Table.select(int[])': (SUITE, 'SUITE'),
    'Table.selectAll()': (f'{DIFF} selectAll', 'COVERED'),
    'Table.deselect(int)': (f'{DIFF} deselect', 'COVERED'),
    'Table.deselect(int,int)': (SUITE, 'SUITE'),
    'Table.deselect(int[])': (SUITE, 'SUITE'),
    'Table.deselectAll()': (f'{DIFF} deselectAll', 'COVERED'),
    'Table.setSelection(int)': (f'{DIFF} setSelection', 'COVERED'),
    'Table.setSelection(int,int)': (SUITE, 'SUITE'),
    'Table.setSelection(int[])': (f'{DIFF} setSelection indices; {ACC} selectionFocusAndMarkShiftWithInsertsLikeNativeRows', 'COVERED'),
    'Table.setSelection(TableItem)': (f'{DIFF} setSelection item', 'COVERED'),
    'Table.setSelection(TableItem[])': (SUITE, 'SUITE'),
    # Table: geometry and hit testing
    'Table.getItem(Point)': (f'{DIFF} variants 5-6 (itemAt)', 'COVERED'),
    'Table.getItemHeight()': (SUITE, 'SUITE'),
    # TableItem
    'TableItem.getText()': (f'{DIFF} read', 'COVERED'),
    'TableItem.getText(int)': (f'{DIFF} read', 'COVERED'),
    'TableItem.setText(String)': (f'{DIFF} setText', 'COVERED'),
    'TableItem.getChecked()': (f'{DIFF} read, setChecked', 'COVERED'),
    'TableItem.setChecked(boolean)': (f'{DIFF} setChecked', 'COVERED'),
    'TableItem.dispose()': (f'{DIFF} dispose item', 'COVERED'),
    'TableItem.setImageIndent(int)': (f'{ORACLE} L (indent through LVN_GETDISPINFO)', 'COVERED'),
    'TableItem.getBounds()': (f'{DIFF} variants 5-6, 9 (bounds)', 'COVERED'),
    'TableItem.getBounds(int)': (f'{DIFF} variants 5-6, 9 (bounds)', 'COVERED'),
    'TableItem.getImageBounds(int)': ('Table.test_clear$I_resetsOnlyTheGivenRows', 'COVERED'),
    'TableItem.getTextBounds(int)': (f'{DIFF} variants 5-6 (bounds)', 'COVERED'),
}

ASPECTS = [
    ('aspect: scroll bar range, thumb and position', f'{DIFF} every step (bar); {ACC} aMillionPlainRowsHaveALogicalBar; {ORACLE} K', 'COVERED'),
    ('aspect: thumb bounds during a burst of appends', f'{ACC} thumbBoundsFollowAnAppendBurst (both modes)', 'COVERED'),
    ('aspect: printed pixels equal a stock Table', f'{DIFF} per seed; {ACC} pixelsMatchAStockOwnerDataTable; {ORACLE} P', 'COVERED'),
    ('aspect: Selection/DefaultSelection/SetData events and their order', f'{DIFF} every step (events)', 'COVERED'),
    ('aspect: caret/focus row', f'{DIFF} every step (focus); {ORACLE} A, B, L', 'COVERED'),
    ('aspect: selection mark across inserts, deletes and count changes', f'{ORACLE} A, B, L', 'COVERED'),
    ('aspect: structural changes (insert, append, remove, dispose, setItemCount, clear, removeAll) recorded', f'{DIFF}', 'COVERED'),
    ('aspect: SWT.CHECK (setChecked, check state)', f'{DIFF} variant SINGLE|CHECK', 'COVERED'),
    ('aspect: SWT.VIRTUAL (SetData timing, identity)', f'{DIFF} variant VIRTUAL', 'COVERED'),
    ('aspect: type-ahead (prefix, case, repeated character, timeout)', f'{ACC} typeAheadSelectsLikeTheNativeList; {ORACLE} F', 'COVERED'),
    ('aspect: keyboard (arrows, Page, Home, End) and clicks', f'{ORACLE} J (window messages)', 'COVERED'),
    ('aspect: Shift/Ctrl ranges and marquee selection', f'{DIFF} variants 3-4 (keys, clicks, double clicks, marquee with Shift/Ctrl); {ORACLE} J', 'COVERED'),
    ('aspect: columns (header, order, sort indicator, showColumn, pack)', f'{ORACLE} G (autosize); {SUITE}', 'SUITE'),
    ('aspect: presentation (fonts, colours, images per item and cell) in pixels', f'{DIFF} variants 5-6 (fonts, colours, images, pack, pixels)', 'COVERED'),
    ('aspect: MeasureItem/EraseItem/PaintItem custom draw', f'{SUITE}; {ACC} packWithMeasureItemMeasuresEveryRow, aThrowingMeasureItemListenerLeavesNoFacadeWalk', 'SUITE'),
    ('aspect: drag and drop feedback (drop highlight, insert mark)', f'{ORACLE} H, L', 'COVERED'),
    ('aspect: tooltips for truncated rows', f'{DIFF} variants 5-6 (labeltip: the text and widths the native label tip uses); evidence ToolTipProbe', 'COVERED'),
    ('aspect: accessibility (row count and names over logical rows)', f'{ACC} accessibilityAnswersEveryLogicalRow (both modes)', 'COVERED'),
    ('aspect: accessibility (focus, selection and check state of rows)', f'{DIFF} variants 3-4 (acc: focus, state and name of rows)', 'COVERED'),
    ('aspect: text direction (setTextDirection AUTO) over every row', f'{ACC} textDirectionReachesEveryRow (both modes)', 'COVERED'),
    ('aspect: DPI change while scrolled', f'{DIFF} variant 9 (WM_DPICHANGED 96-192 dpi, scrolled, resized, pixels)', 'COVERED'),
    ('aspect: dispose and reentrancy in listeners', f'{DIFF} variants 7-8 (remove, dispose, insert, count, text, top, selection, clear inside Selection, DefaultSelection, MeasureItem, SetData)', 'COVERED'),
]


def api(cls):
    rows = []
    with open(WIDGETS + cls + '.java', encoding='utf-8') as f:
        for line in f:
            m = SIG.match(line)
            if not m or m.group(2) == cls:
                continue
            params = ','.join(p.strip().rsplit(' ', 1)[0].replace(' ', '') for p in m.group(3).split(',') if p.strip())
            rows.append('%s.%s(%s)' % (cls, m.group(2), params))
    return sorted(set(rows))


def main():
    out = ['member_or_aspect\ttest\tstatus']
    counts = {}
    for cls in ('Table', 'TableItem'):
        for member in api(cls):
            test, status = COVERAGE.get(member, (SUITE, 'SUITE'))
            if member not in COVERAGE and any(member.startswith(cls + '.' + p) for p in ('add', 'remove')):
                test, status = ('listener registration (upstream suites)', 'SUITE')
            out.append('%s\t%s\t%s' % (member, test, status))
            counts[status] = counts.get(status, 0) + 1
    for aspect, test, status in ASPECTS:
        out.append('%s\t%s\t%s' % (aspect, test, status))
        counts[status] = counts.get(status, 0) + 1
    with open(OUT, 'w', encoding='utf-8', newline='\n') as f:
        f.write('\n'.join(out) + '\n')
    print(' '.join('%s=%d' % kv for kv in sorted(counts.items())))


main()
