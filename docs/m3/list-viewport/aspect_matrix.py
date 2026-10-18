"""List and Combo viewport aspect matrix: every public List and Combo member and behaviour aspect -> the test that proves it.

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

DIFF = 'List_ViewportDifferential'   # recorded official-SWT-vs-viewport scripts (answers, bars, selection, focus, events, pixels)
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
    'List.setItemCount(int)': ('fork API, not in SWT 3.135.0: List_Viewport and List suites with the viewport on', 'SUITE'),
    'List.clear(int)': ('fork API, not in SWT 3.135.0: List suites with the viewport on; the differential covers setItem (index, "")', 'SUITE'),
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
    ('aspect: vertical bar range, thumb, position and visibility (incl. the freed bar after removeAll)', f'{DIFF} every step (bars); ListBarOracle, ListBarOracle2', 'COVERED'),
    ('aspect: horizontal extent (widest row + inset) and horizontal bar', f'{DIFF} every step (bars)', 'COVERED'),
    ('aspect: pixels equal the official SWT 3.135.0 List (WM_PRINT, hidden desktop)', f'{DIFF} per seed; ListPlaneProbe (screen copy, 0 differing pixels)', 'COVERED'),
    ('aspect: Selection / DefaultSelection events (programmatic: none; input: as the list box)', f'{DIFF} every step (events); ListRulesOracle2', 'COVERED'),
    ('aspect: caret and anchor after insert, delete, select (native rules)', f'{DIFF} every step (focus); ListRulesOracle, ListRulesOracle2, ListAnchorOracle', 'COVERED'),
    ('aspect: bounded native rows at 1,000,000 rows', f'{ACC} millionItemsKeepOneWindowNative', 'COVERED'),
    ('aspect: keyboard Up, Down, Prior, Next, Home, End, Space with none, Shift, Ctrl, Ctrl+Shift (SINGLE, MULTI, SIMPLE)', f'{DIFF} key ops (window messages, keyboard state); {ACC} scrollBarWheelAndKeys...', 'COVERED'),
    ('aspect: type-ahead over logical rows (first character, wrapping)', f'{DIFF} type ops; {ACC} scrollBarWheelAndKeys... (row 99,000)', 'COVERED'),
    ('aspect: mouse click with none, Shift, Ctrl, Ctrl+Shift; double click; drag selection with auto-scroll', f'{DIFF} click, dblclick, drag ops (window messages)', 'COVERED'),
    ('aspect: scroll bar codes and the wheel (whole notches; one remainder per thread, ListWheelOracle)', f'{DIFF} vscroll and wheel ops; {ACC} scrollBarWheelAndKeys...', 'COVERED'),
    ('aspect: resident window diff touches only entering and leaving rows', 'Test_VirtualRowWindowPlan (2,000 random moves)', 'COVERED'),
    ('aspect: build loops (add, remove): bar values at once, bar painted once per burst', f'ListAddBench (before/after, evidence/ListAddBench.out.txt); {DIFF} pixels per seed', 'COVERED'),
    ('aspect: VIRTUAL round trip pixels (List_Viewport, synchronous WM_PRINT capture)', 'List_Viewport.scrollRoundTripReproducesStateAndPixels (6 of 6 runs, both modes)', 'COVERED'),
    ('aspect: accessibility (MSAA child count, names, roles, states, focus) over logical rows', f'{ACC} accessibilityAnswersForLogicalRows', 'COVERED'),
    ('aspect: scroll bar thumb drag beyond 65,535 rows (32-bit track position)', '', 'GAP'),
    ('aspect: resize, font and DPI change while scrolled', f'{DIFF} resizeAndFontWhileScrolled (10 x 150 steps), zoomWhileScrolled (10 x 24 steps, 120/144 dpi and back); ListZoomProbe', 'COVERED'),
    ('aspect: bidi auto text direction', '', 'GAP'),
    ('aspect: drag and drop source/target over off-window rows', '', 'GAP'),
    ('aspect: a click on the empty area below the last row of a short list (list box state from earlier LB_SETSEL calls)', '', 'GAP'),
]


CDIFF = 'Combo_ViewportDifferential'   # recorded official-SWT-vs-viewport scripts (answers, text, events, face and dropped-list pixels, dropped bar)
CACC = 'Combo_ViewportAcceptance'      # acceptance tests (1,000,000 rows, far keys, scrolling and cancelling a drop, edit-text match)
CSUITE = 'Combo suite with the viewport on'
NATIVE_EDIT = 'native edit field or selected-item area (not mediated); ' + CSUITE

COMBO_COVERAGE = {
    'Combo.add(String)': (f'{CDIFF} add (every step: count, text, face)', 'COVERED'),
    'Combo.add(String,int)': (f'{CDIFF} addAt', 'COVERED'),
    'Combo.remove(int)': (f'{CDIFF} remove (Modify when the text changes)', 'COVERED'),
    'Combo.remove(int,int)': (f'{CDIFF} removeRange; {CACC} millionItems', 'COVERED'),
    'Combo.remove(String)': (f'{CDIFF} removeString (absent text fails)', 'COVERED'),
    'Combo.removeAll()': (f'{CDIFF} removeAll', 'COVERED'),
    'Combo.setItems(String[])': (f'{CDIFF} setItems; {CACC} every test', 'COVERED'),
    'Combo.setItem(int,String)': (f'{CDIFF} setItem', 'COVERED'),
    'Combo.getItem(int)': (f'{CDIFF} read; {CACC}', 'COVERED'),
    'Combo.getItemCount()': (f'{CDIFF} every step (count)', 'COVERED'),
    'Combo.getItems()': (f'{CDIFF} read (items)', 'COVERED'),
    'Combo.getItemHeight()': (f'{CDIFF} read (height)', 'COVERED'),
    'Combo.indexOf(String)': (f'{CDIFF} read; {CACC} millionItems', 'COVERED'),
    'Combo.indexOf(String,int)': (f'{CDIFF} read', 'COVERED'),
    'Combo.select(int)': (f'{CDIFF} select; {CACC}', 'COVERED'),
    'Combo.deselect(int)': (f'{CDIFF} deselect', 'COVERED'),
    'Combo.deselectAll()': (f'{CDIFF} deselectAll', 'COVERED'),
    'Combo.getSelectionIndex()': (f'{CDIFF} every step (sel)', 'COVERED'),
    'Combo.getText()': (f'{CDIFF} every step (text)', 'COVERED'),
    'Combo.setText(String)': (f'{CDIFF} setText (item, prefix, other case, no match); {CACC} editTextMatch', 'COVERED'),
    'Combo.getListVisible()': (f'{CDIFF} every step (dropped)', 'COVERED'),
    'Combo.setListVisible(boolean)': (f'{CDIFF} drop', 'COVERED'),
    'Combo.setVisibleItemCount(int)': (f'{CDIFF} visible (3 .. 27 rows: the dropped height and page)', 'COVERED'),
    'Combo.getVisibleItemCount()': (CSUITE, 'SUITE'),
    'Combo.setFont(Font)': ('widths measured again with the new font (ComboViewport.beginMeasure); ' + CSUITE, 'SUITE'),
}

COMBO_ASPECTS = [
    ('aspect: Combo dropped list pixels and vertical bar every step (WM_PRINT of the ComboLBox, logical SCROLLINFO)', f'{CDIFF} every step (list); ComboPrintProbe (0 differing pixels)', 'COVERED'),
    ('aspect: Combo face pixels every step (edit field or selected-item area, button)', f'{CDIFF} every step (face)', 'COVERED'),
    ('aspect: Combo Modify / Selection / DefaultSelection events', f'{CDIFF} every step (events); ComboMessageOracle', 'COVERED'),
    ('aspect: Combo keys closed and dropped (Up, Down, Left, Right, Prior, Next, Home, End, F4, Alt+Down, Escape, Return)', f'{CDIFF} key, altdown ops; {CACC} keysAndTypeAhead; ComboRulesOracle', 'COVERED'),
    ('aspect: Combo type-ahead (read-only) and typing with the edit-text match (editable)', f'{CDIFF} type ops; {CACC} keysAndTypeAhead, editTextMatch', 'COVERED'),
    ('aspect: Combo wheel closed and dropped, dropped scroll-bar codes and thumb', f'{CDIFF} wheel, listwheel, vscroll, thumb ops; {CACC} droppedListScrollsAndCancels', 'COVERED'),
    ("aspect: Combo hover, click, cancel and commit of a drop after window moves (the list box's recorded entry, ComboTrackProbe)", f'{CDIFF} hover, click, key ops (also with -Dswt.viewport.combo.reach=30); {CACC} droppedListScrollsAndCancels', 'COVERED'),
    ('aspect: Combo caret of the list box after inserts, removes and focus (ComboFocus)', f'{CDIFF} key ops after API changes', 'COVERED'),
    ('aspect: Combo bounded native rows at 1,000,000 rows', f'{CACC} millionItemsKeepOneWindowNative', 'COVERED'),
    ('aspect: Combo segments and automatic text direction (every row is put back into the list box, the combo is native from then on)', CSUITE, 'SUITE'),
    ('aspect: Combo SWT.SIMPLE (not mediated: the list is a child list box)', '', 'GAP'),
    ('aspect: Combo accessibility of dropped rows over logical rows', '', 'GAP'),
    ('aspect: Combo selected row scrolled more than the reach away while dropped (the read-only area repaints it empty until the drop ends)', '', 'GAP'),
    ('aspect: Combo dropped scroll-bar thumb drag beyond 65,535 rows', '', 'GAP'),
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
    combo = {}
    for member in api('Combo'):
        test, status = COMBO_COVERAGE.get(member, (NATIVE_EDIT, 'SUITE'))
        if member not in COMBO_COVERAGE and 'Listener' in member:
            test, status = ('listener registration (upstream suites)', 'SUITE')
        out.append('%s\t%s\t%s' % (member, test, status))
        combo[status] = combo.get(status, 0) + 1
    for aspect, test, status in COMBO_ASPECTS:
        out.append('%s\t%s\t%s' % (aspect, test, status))
        combo[status] = combo.get(status, 0) + 1
    with open(OUT, 'w', encoding='utf-8', newline='\n') as f:
        f.write('\n'.join(out) + '\n')
    print('List ' + ' '.join('%s=%d' % kv for kv in sorted(counts.items())))
    print('Combo ' + ' '.join('%s=%d' % kv for kv in sorted(combo.items())))


main()
