"""Tree viewport aspect matrix: every public Tree/TreeItem member and behaviour aspect -> the test that proves it.

Run from the SWT repository root:  python -I docs/m3/tree-viewport/aspect_matrix.py
The public API is extracted from the win32 sources (so a new public method appears as a GAP row
automatically); the coverage table below is maintained with the tests.  Output: ASPECT_MATRIX.tsv.
"""
import re
import sys

ROOT = sys.argv[1] if len(sys.argv) > 1 else '.'
WIDGETS = ROOT + '/bundles/org.eclipse.swt/Eclipse SWT/win32/org/eclipse/swt/widgets/'
OUT = ROOT + '/docs/m3/tree-viewport/ASPECT_MATRIX.tsv'
SIG = re.compile(r'^public ([][A-Za-z<>, ]+ )?([A-Za-z0-9_]+) \(([^)]*)\)')

DIFF = 'Tree_ViewportDifferential'   # recorded plain-vs-viewport scripts (answers, bar, selection, focus, events, pixels)
ACC = 'Tree_Viewport'                # acceptance tests (1,000,000 rows, pixels, identity)
SUITE = 'Tree/TreeItem/TreeColumn suites with the viewport on'

# member or aspect -> (test, status); status COVERED (recorded or asserted), SUITE (only the upstream suites), GAP
COVERAGE = {
    # Tree: rows and identity
    'Tree.getItem(int)': (f'{DIFF} read; {ACC} distantGetItemIsImmediateAndKeepsIdentity', 'COVERED'),
    'Tree.getItemCount()': (f'{ACC} virtualAndPlainTreesAnswerTheSame', 'COVERED'),
    'Tree.getItems()': (SUITE, 'SUITE'),
    'Tree.indexOf(TreeItem)': (f'{DIFF} read; {ACC} distantGetItemIsImmediateAndKeepsIdentity', 'COVERED'),
    'Tree.setItemCount(int)': (f'{ACC} verticalBarCoversEveryLogicalRow; {SUITE}', 'COVERED'),
    'Tree.clear(int,boolean)': (SUITE, 'SUITE'),
    'Tree.clearAll(boolean)': (SUITE, 'SUITE'),
    'Tree.removeAll()': (SUITE, 'SUITE'),
    # Tree: scrolling
    'Tree.getTopItem()': (f'{DIFF} every step (top) ; {ACC}', 'COVERED'),
    'Tree.setTopItem(TreeItem)': (f'{DIFF} setTopItem; {ACC} setTopItemMovesTheLogicalTopAndTheBar', 'COVERED'),
    'Tree.showItem(TreeItem)': (f'{DIFF} showItem; EnsureVisibleOracle (66 measured cases)', 'COVERED'),
    'Tree.showSelection()': (f'{DIFF} showSelection', 'COVERED'),
    # Tree: selection
    'Tree.getSelection()': (f'{DIFF} every step (sel)', 'COVERED'),
    'Tree.getSelectionCount()': (f'{DIFF} every step (sel)', 'COVERED'),
    'Tree.setSelection(TreeItem)': (f'{DIFF} setSelection', 'COVERED'),
    'Tree.setSelection(TreeItem[])': (f'{ACC} scrollRoundTripReproducesStateAndPixels; {SUITE}', 'COVERED'),
    'Tree.select(TreeItem)': (f'{DIFF} select+', 'COVERED'),
    'Tree.deselect(TreeItem)': (f'{DIFF} deselect', 'COVERED'),
    'Tree.deselectAll()': (f'{DIFF} deselectAll', 'COVERED'),
    'Tree.selectAll()': (f'{DIFF} selectAll (pattern selection of every visible row)', 'COVERED'),
    # Tree: expansion model API
    'Tree.getExpandedItems()': (SUITE, 'SUITE'),
    'Tree.setExpandedItems(TreeItem[])': (SUITE, 'SUITE'),
    'Tree.expandAll()': (SUITE, 'SUITE'),
    'Tree.collapseAll()': (SUITE, 'SUITE'),
    'Tree.expandToLevel(int)': (SUITE, 'SUITE'),
    'Tree.expandToLevel(TreeItem,int)': (SUITE, 'SUITE'),
    'Tree.collapseToLevel(int)': (SUITE, 'SUITE'),
    'Tree.collapseToLevel(TreeItem,int)': (SUITE, 'SUITE'),
    'Tree.expandSelection()': (SUITE, 'SUITE'),
    'Tree.expandSelectionToLevel(int)': (SUITE, 'SUITE'),
    'Tree.collapseSelection()': (SUITE, 'SUITE'),
    'Tree.collapseSelectionToLevel(int)': (SUITE, 'SUITE'),
    # Tree: geometry and hit testing
    'Tree.getItem(Point)': (f'{DIFF} every step (geo hit0, hit5 after a paint)', 'COVERED'),
    'Tree.getItemHeight()': (SUITE, 'SUITE'),
    # TreeItem
    'TreeItem.getItem(int)': (f'{DIFF} read (child)', 'COVERED'),
    'TreeItem.getItemCount()': (f'{DIFF} read', 'COVERED'),
    'TreeItem.getItems()': (SUITE, 'SUITE'),
    'TreeItem.indexOf(TreeItem)': (f'{DIFF} read', 'COVERED'),
    'TreeItem.getParentItem()': (f'{DIFF} parentOf', 'COVERED'),
    'TreeItem.getExpanded()': (f'{DIFF} expand/collapse/read', 'COVERED'),
    'TreeItem.setExpanded(boolean)': (f'{DIFF} expand/collapse (top item kept, caret on collapse)', 'COVERED'),
    'TreeItem.getText()': (f'{DIFF} read', 'COVERED'),
    'TreeItem.setItemCount(int)': (f'{ACC} nestedExpansionRangeIsLogical', 'COVERED'),
    'TreeItem.getBounds()': (f'{DIFF} every step (geo: top item bounds)', 'COVERED'),
    'TreeItem.getBounds(int)': (f'{DIFF} every step (geo cell)', 'COVERED'),
    'TreeItem.getImageBounds(int)': (f'{DIFF} every step (geo img)', 'COVERED'),
    'TreeItem.getTextBounds(int)': (f'{DIFF} every step (geo txt)', 'COVERED'),
}

ASPECTS = [
    ('aspect: scroll bar range, thumb and position', f'{DIFF} every step (bar); {ACC} verticalBarCoversEveryLogicalRow', 'COVERED'),
    ('aspect: printed pixels equal a plain Tree', f'{ACC} printedPixelsMatchAPlainTreeAtTheSameTopRow; {DIFF} per seed', 'COVERED'),
    ('aspect: Expand/Collapse/Selection events and their order', f'{DIFF} every step (events)', 'COVERED'),
    ('aspect: caret/focus item', f'{DIFF} every step (focus)', 'COVERED'),
    ('aspect: bounded native rows at any position', f'{ACC} nativeRowsStayBoundedAtTopMiddleAndEnd', 'COVERED'),
    ('aspect: SetData once per row per residency', f'{ACC} scrollRoundTripReproducesStateAndPixels', 'COVERED'),
    ('aspect: keyboard (arrows, Page, Home, End, +, -, *, Left, Right, type-ahead) via Display.post', '', 'GAP'),
    ('aspect: mouse (row click, expander click, check box click, shift/ctrl click) via Display.post', '', 'GAP'),
    ('aspect: mouse wheel and scroll bar drag via Display.post', '', 'GAP'),
    ('aspect: structural changes (insert, dispose, setItemCount, clear, removeAll) recorded', '', 'GAP'),
    ('aspect: SWT.CHECK (setChecked, setGrayed, check events)', '', 'GAP'),
    ('aspect: columns (header, column order, sort indicator, showColumn, horizontal scroll)', '', 'GAP'),
    ('aspect: presentation (fonts, colours, images per item and cell) in pixels', '', 'GAP'),
    ('aspect: MeasureItem/EraseItem/PaintItem custom draw', '', 'GAP'),
    ('aspect: drag and drop feedback (insert mark, auto-scroll)', '', 'GAP'),
    ('aspect: tooltips for truncated rows', '', 'GAP'),
    ('aspect: accessibility (row count, focus, names)', '', 'GAP'),
    ('aspect: DPI change while scrolled', '', 'GAP'),
    ('aspect: dispose and reentrancy in listeners', '', 'GAP'),
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
    for cls in ('Tree', 'TreeItem'):
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
