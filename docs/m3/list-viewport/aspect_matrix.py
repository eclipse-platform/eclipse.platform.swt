"""List viewport aspect matrix: every public List member and behaviour aspect -> the test that proves it.

Run from the SWT repository root:  python -I docs/m3/list-viewport/aspect_matrix.py
The public API is extracted from the win32 sources (so a new public method appears as a row
automatically); the coverage table below is maintained with the tests.  Output: ASPECT_MATRIX.tsv.
Same format as docs/m3/tree-viewport/aspect_matrix.py.
"""
import re
import sys

ROOT = sys.argv[1] if len(sys.argv) > 1 else '.'
WIDGETS = ROOT + '/bundles/org.eclipse.swt/Eclipse SWT/win32/org/eclipse/swt/widgets/'
OUT = ROOT + '/docs/m3/list-viewport/ASPECT_MATRIX.tsv'
SIG = re.compile(r'^public ([][A-Za-z<>, .]+ )?([A-Za-z0-9_]+) ?\(([^)]*)\)')

DIFF = 'List_ViewportDifferential'   # recorded original-vs-viewport scripts (answers, bars, selection, focus, events, pixels)
ACC = 'List_ViewportAcceptance'      # acceptance tests (1,000,000 rows, bounded native rows)
SUITE = 'List suites with the viewport on'

# member or aspect -> (test, status); status COVERED (recorded or asserted), SUITE (only the upstream suites), GAP
COVERAGE = {
    'List.add(String)': (f'{DIFF} add (every step: count, top, bars)', 'COVERED'),
    'List.add(String,int)': (f'{DIFF} addAt, addEmpty, addNul (NUL truncation); {ACC}', 'COVERED'),
    'List.remove(int)': (f'{DIFF} remove (caret, anchor, top rules); {ACC}', 'COVERED'),
    'List.remove(int,int)': (f'{DIFF} removeRange; {ACC}', 'COVERED'),
    'List.remove(int[])': (f'{DIFF} removeIndices (duplicates, out of range)', 'COVERED'),
    'List.remove(String)': (SUITE, 'SUITE'),
    'List.removeAll()': (f'{DIFF} removeAll (bar freed); {ACC} emptyAndTinyLists', 'COVERED'),
    'List.setItems(String[])': (f'{DIFF} setItems; {ACC} millionItems', 'COVERED'),
    'List.setItem(int,String)': (f'{DIFF} setItem', 'COVERED'),
    'List.setItemCount(int)': (f'{DIFF} setItemCount', 'COVERED'),
    'List.clear(int)': (f'{DIFF} clear', 'COVERED'),
    'List.clearAll()': (SUITE, 'SUITE'),
    'List.getItem(int)': (f'{DIFF} read; {ACC}', 'COVERED'),
    'List.getItemCount()': (f'{DIFF} every step (count)', 'COVERED'),
    'List.getItems()': (SUITE, 'SUITE'),
    'List.getItemHeight()': (f'{DIFF} read', 'COVERED'),
    'List.indexOf(String)': (f'{DIFF} indexOf', 'COVERED'),
    'List.indexOf(String,int)': (f'{DIFF} indexOf', 'COVERED'),
    'List.getTopIndex()': (f'{DIFF} every step (top); {ACC}', 'COVERED'),
    'List.setTopIndex(int)': (f'{DIFF} setTopIndex (clamp); {ACC} scrollingMovesOnlyTheChangedRows', 'COVERED'),
    'List.showSelection()': (f'{DIFF} showSelection', 'COVERED'),
    'List.select(int)': (f'{DIFF} select', 'COVERED'),
    'List.select(int,int)': (f'{DIFF} selectRange', 'COVERED'),
    'List.select(int[])': (f'{DIFF} selectIndices', 'COVERED'),
    'List.selectAll()': (f'{DIFF} selectAll; {ACC} millionItems', 'COVERED'),
    'List.deselect(int)': (f'{DIFF} deselect', 'COVERED'),
    'List.deselect(int,int)': (f'{DIFF} deselectRange; {ACC}', 'COVERED'),
    'List.deselect(int[])': (f'{DIFF} deselectIndices', 'COVERED'),
    'List.deselectAll()': (f'{DIFF} deselectAll', 'COVERED'),
    'List.setSelection(int)': (f'{DIFF} setSelection (scroll into view)', 'COVERED'),
    'List.setSelection(int,int)': (f'{DIFF} setSelectionRange', 'COVERED'),
    'List.setSelection(int[])': (f'{DIFF} setSelectionIndices', 'COVERED'),
    'List.setSelection(String[])': (f'{DIFF} setSelectionStrings', 'COVERED'),
    'List.getSelection()': (f'{DIFF} every step (sel)', 'COVERED'),
    'List.getSelectionCount()': (f'{DIFF} every step (sel)', 'COVERED'),
    'List.getSelectionIndex()': (f'{DIFF} selectionIndex', 'COVERED'),
    'List.getSelectionIndices()': (f'{DIFF} every step (sel)', 'COVERED'),
    'List.isSelected(int)': (f'{DIFF} read', 'COVERED'),
    'List.getFocusIndex()': (f'{DIFF} every step (focus)', 'COVERED'),
    'List.setFont(Font)': ('', 'GAP'),
}

ASPECTS = [
    ('aspect: vertical bar range, thumb, position and visibility (incl. the freed bar after removeAll)', f'{DIFF} every step (v); ListBarOracle, ListBarOracle2', 'COVERED'),
    ('aspect: horizontal extent (widest row + inset) and horizontal bar', f'{DIFF} every step (h)', 'COVERED'),
    ('aspect: screen pixels equal the original List (focused)', f'{DIFF} per seed; ListPlaneProbe (0 differing pixels)', 'COVERED'),
    ('aspect: Selection / DefaultSelection events of programmatic calls (none)', f'{DIFF} every step (events); ListRulesOracle2', 'COVERED'),
    ('aspect: caret and anchor after insert, delete, select (native rules)', f'{DIFF} every step (focus); ListRulesOracle, ListRulesOracle2', 'COVERED'),
    ('aspect: bounded native rows at 1,000,000 rows', f'{ACC} millionItemsKeepOneWindowNative', 'COVERED'),
    ('aspect: keyboard Home, End, Next, Prior, Down at the page edge and from an off-window caret (window messages)', f'{ACC} scrollBarWheelAndKeysMoveTheLogicalTopLikeTheListBox', 'COVERED'),
    ('aspect: keyboard Space, Ctrl+keys and Shift ranges with an off-window anchor', '', 'GAP'),
    ('aspect: type-ahead over logical rows (first character, wrapping)', f'{ACC} scrollBarWheelAndKeysMoveTheLogicalTopLikeTheListBox (row 99,000)', 'COVERED'),
    ('aspect: mouse (click, Shift/Ctrl click, drag selection with auto-scroll, double click)', '', 'GAP'),
    ('aspect: scroll bar codes (line, page, top, bottom) and the wheel', f'{ACC} scrollBarWheelAndKeysMoveTheLogicalTopLikeTheListBox', 'COVERED'),
    ('aspect: scroll bar thumb drag beyond 65,535 rows (32-bit track position)', '', 'GAP'),
    ('aspect: resident window diff touches only entering and leaving rows', 'Test_VirtualRowWindowPlan (2,000 random moves)', 'COVERED'),
    ('aspect: resize, font and DPI change while scrolled', '', 'GAP'),
    ('aspect: bidi auto text direction', '', 'GAP'),
    ('aspect: accessibility (row count, names) beyond the resident rows', '', 'GAP'),
    ('aspect: drag and drop source/target over off-window rows', '', 'GAP'),
]


def api(cls):
    rows = []
    with open(WIDGETS + cls + '.java', encoding='utf-8') as f:
        for line in f:
            m = SIG.match(line)
            if not m or m.group(2) == cls:
                continue
            params = ','.join(p.strip().rsplit(' ', 1)[0].replace(' ', '').replace('...', '[]') for p in m.group(3).split(',') if p.strip())
            rows.append('%s.%s(%s)' % (cls, m.group(2), params))
    return sorted(set(rows))


def main():
    out = ['member_or_aspect\ttest\tstatus']
    counts = {}
    for member in api('List'):
        test, status = COVERAGE.get(member, (SUITE, 'SUITE'))
        if member not in COVERAGE and any(member.startswith('List.' + p) for p in ('addSelection', 'removeSelection')):
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
