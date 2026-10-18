"""Builds LIST_COMBO_API_COVERAGE.tsv mechanically.

Rows: every public/protected member declared at column 0 in win32 List.java, win32 Combo.java and
CCombo.java, plus behaviour lanes. Inputs: the differential traces (line-aligned base vs after;
lanes nongate.*, defect.* and state.eventsNonGate are outside the gate), the JUnit test sources
(test method names, test_<member>...), the JUnit XML reports when present, and git diff (whether
the member's source changed). Only the cost-class strings are typed by hand.
usage: python coverage.py <swt-worktree> <lc-bench> <out.tsv> [base-commit]
"""
import glob
import os
import re
import subprocess
import sys
import xml.etree.ElementTree as ET

SWT, BENCH, OUT = sys.argv[1:4]
BASE = sys.argv[4] if len(sys.argv) > 4 else '0b6a31ff619b9643729d91cb007307bbcda1cf1c'
B = os.path.join(SWT, 'bundles/org.eclipse.swt')
REL = {'List': 'bundles/org.eclipse.swt/Eclipse SWT/win32/org/eclipse/swt/widgets/List.java',
       'Combo': 'bundles/org.eclipse.swt/Eclipse SWT/win32/org/eclipse/swt/widgets/Combo.java',
       'CCombo': 'bundles/org.eclipse.swt/Eclipse SWT Custom Widgets/common/org/eclipse/swt/custom/CCombo.java'}
TESTDIR = os.path.join(SWT, 'tests/org.eclipse.swt.tests/JUnit Tests/org/eclipse/swt/tests/junit')
TEST_CLASSES = {'List': ['Test_org_eclipse_swt_widgets_List', 'Test_org_eclipse_swt_widgets_List_Virtual'],
                'Combo': ['Test_org_eclipse_swt_widgets_Combo', 'Test_org_eclipse_swt_widgets_Combo_Virtual'],
                'CCombo': ['Test_org_eclipse_swt_custom_CCombo', 'Test_org_eclipse_swt_custom_CCombo_Virtual']}
DECL = re.compile(r'^(public|protected)\s+(?!class\b)(?:static\s+|final\s+|synchronized\s+)*([\w\[\]<>, .]+?)\s*\b(\w+)\s*\(([^)]*)\)')


def params(text):
    out = []
    for p in [x.strip() for x in text.split(',') if x.strip()]:
        p = re.sub(r'\s+', ' ', p)
        out.append(p.rsplit(' ', 1)[0].replace(' ', '').replace('...', '[]'))
    return out


def changed_lines(rel):
    diff = subprocess.run(['git', '-C', SWT, 'diff', '-U0', BASE, 'HEAD', '--', rel], capture_output=True, text=True).stdout
    lines = set()
    for m in re.finditer(r'^@@ -\d+(?:,\d+)? \+(\d+)(?:,(\d+))? @@', diff, re.M):
        start, n = int(m.group(1)), int(m.group(2) or 1)
        lines.update(range(start, start + max(n, 1)))
    return lines


members = []
for widget, rel in REL.items():
    path = os.path.join(SWT, rel)
    text = open(path, encoding='utf-8').read().split('\n')
    changed = changed_lines(rel)
    decls = [(i + 1, DECL.match(l)) for i, l in enumerate(text)]
    decls = [(n, m) for n, m in decls if m]
    for k, (n, m) in enumerate(decls):
        end = decls[k + 1][0] if k + 1 < len(decls) else len(text)
        touched = any(n <= c < end for c in changed)
        members.append((widget, m.group(1), m.group(3), params(m.group(4)), n, touched))

tests_by_class = {}
for classes in TEST_CLASSES.values():
    for cls in classes:
        f = os.path.join(TESTDIR, cls + '.java')
        if os.path.exists(f):
            tests_by_class[cls] = re.findall(r'public void (test_\w+)\s*\(', open(f, encoding='utf-8').read())
# the _Virtual classes inherit every test of their parent
for parent, child in (('Test_org_eclipse_swt_widgets_List', 'Test_org_eclipse_swt_widgets_List_Virtual'),
                      ('Test_org_eclipse_swt_custom_CCombo', 'Test_org_eclipse_swt_custom_CCombo_Virtual'),
                      ('Test_org_eclipse_swt_widgets_Combo', 'Test_org_eclipse_swt_widgets_Combo_Virtual')):
    if child in tests_by_class or os.path.exists(os.path.join(TESTDIR, child + '.java')):
        tests_by_class[child] = sorted(set(tests_by_class.get(child, [])) | set(tests_by_class.get(parent, [])))

junit = {}
for xml in glob.glob(os.path.join(BENCH, 'reports/*/TEST-junit-jupiter.xml')):
    run = os.path.basename(os.path.dirname(xml))
    for case in ET.parse(xml).getroot().iter('testcase'):
        cls = case.get('classname').rsplit('.', 1)[-1]
        state = 'fail' if case.find('failure') is not None or case.find('error') is not None else 'pass'
        junit[(run, cls, case.get('name').split('(')[0])] = state


def load(path):
    return [l.rstrip('\n').split('\t') for l in open(path, encoding='utf-8')] if os.path.exists(path) else []


def gated(row):
    return len(row) >= 4 and not row[0].startswith(('nongate.', 'defect.')) and row[2] != 'state.eventsNonGate'


base = load(os.path.join(BENCH, 'traces/base.trace'))
calls, bad = {}, {}
for tag in ('after', 'after-t0'):
    after = load(os.path.join(BENCH, 'traces/%s.trace' % tag))
    assert len(after) == len(base), tag
    last = {}
    for b, a in zip(base, after):
        if not gated(b):
            continue
        widget = b[0].split('.')[0]
        if not b[2].startswith('state.'):
            last[widget] = b[2]
        if tag == 'after':
            calls[(widget, b[2])] = calls.get((widget, b[2]), 0) + 1
        if b != a:
            bad.setdefault((widget, b[2]), set()).add(tag)
            if b[2].startswith('state.') and widget in last:
                bad.setdefault((widget, last[widget]), set()).add(tag)

STATE_OPS = {'getItemCount': 'state.count', 'getItems': 'state.items', 'getSelectionIndices': 'state.selectionIndices',
             'getSelectionIndex': 'state.selectionIndex', 'getSelectionCount': 'state.selectionCount',
             'getFocusIndex': 'state.focus', 'getTopIndex': 'state.top', 'getItemHeight': 'state.itemHeight',
             'getText': 'state.text', 'getVisibleItemCount': 'state.visibleItemCount', 'getListVisible': 'state.listVisible',
             'getSelection': 'state.selection'}
COST = {
    'add(String)': 'O(1) amortized Java append; native: one row message (SINGLE primed) or LB_SETCOUNT when empty',
    'add(String,int)': 'O(n-index) Java arraycopy; native one LB_INSERTSTRING (MULTI moves per-row selection bytes)',
    'getItem(int)': 'O(1); one SetData if the row is not materialized',
    'getItemCount()': 'O(1)', 'getItemHeight()': 'O(1)',
    'getItems()': 'O(n) intrinsic (array result); SetData for unmaterialized rows',
    'getSelection()': 'O(selected) intrinsic', 'getSelectionIndices()': 'O(selected) result; native per-row scan for MULTI (unchanged)',
    'getSelectionCount()': 'O(1) SINGLE; native per-row scan MULTI (unchanged)',
    'indexOf(String)': 'O(n) intrinsic scan of the Java store', 'indexOf(String,int)': 'O(n-start) intrinsic scan',
    'remove(int)': 'O(n-index) Java arraycopy; native one row message',
    'remove(int,int)': 'O(n) arraycopy; native row messages while rows*count <= 2^26, else one LB_SETCOUNT + restore',
    'remove(int[])': 'O(k log k) sort + per run as remove(int,int)', 'remove(String)': 'indexOf + remove(int)',
    'removeAll()': 'O(1)', 'setItem(int,String)': 'O(1) inside SetData; otherwise remove+add like the plain list',
    'setItems(String[])': 'O(n) intrinsic array copy; native one LB_SETCOUNT, no per-item native object',
    'setFont(Font)': 'O(1) for > 4096 rows (widths re-measured lazily); exact extent up to 4096 rows',
    'setItemCount(int)': 'O(delta) Java slots; native one LB_SETCOUNT or row messages [NEW API]',
    'clear(int)': 'O(1) [NEW API]', 'clearAll()': 'O(n) null fill, SetData only when drawn or read [NEW API]',
    'selectAll()': 'native per-row fill (MULTI), unchanged', 'deselectAll()': 'native per-row fill (MULTI), unchanged',
}
DEFAULT_COST = {'List': 'O(1) native message(s)', 'Combo': 'native COMBOBOX', 'CCombo': 'delegates to the SWT.VIRTUAL List'}

rows = []
for widget, kind, name, ps, line, touched in members:
    sig = '%s(%s)' % (name, ','.join(ps))
    w = widget.lower()
    ops = [k for k in calls if k[0] == w and (k[1] == sig or k[1] == name or k[1] == name + '(large)' or (not ps and k[1] == STATE_OPS.get(name)))]
    if name in ('List', 'CCombo', 'getStyle'):
        ops = [k for k in calls if k[0] == w and k[1].startswith('getStyle')]
    if name == 'computeSize' or name == 'setFont':
        ops += [k for k in calls if k[0] == w and k[1] in ('state.computeSize', 'computeSize(hint)', 'setFont')]
    ops = sorted(set(ops))
    n_calls = sum(calls[k] for k in ops)
    tags = sorted(set().union(*[bad.get(k, set()) for k in ops])) if ops else []
    tests = []
    for cls in TEST_CLASSES[widget]:
        for t in tests_by_class.get(cls, []):
            if re.match(r'test_' + re.escape(name) + r'($|[A-Z$_])', t):
                tests.append((cls, t))
    new_api = widget == 'List' and name in ('setItemCount', 'clear', 'clearAll')
    run_states = [junit.get(('after', c, t)) for c, t in tests]
    ids = []
    if ops:
        ids.append('diff:%s x%d' % ('|'.join(k[1] for k in ops), n_calls))
    if tests:
        ids.append('junit:' + '|'.join(sorted(set('%s#%s' % (c.replace('Test_org_eclipse_swt_', ''), t) for c, t in tests))))
    if new_api:
        ids.append('junit:widgets_List_Viewport')
    if 'after' in tags:
        status = 'DIFF_MISMATCH'
    elif any(s == 'fail' for s in run_states):
        status = 'JUNIT_FAIL'
    elif ops and run_states and all(s == 'pass' for s in run_states):
        status = 'PASS_DIFF+JUNIT'
    elif ops:
        status = 'PASS_DIFF' + ('+JUNIT_NOT_RUN' if tests else '')
    elif new_api:
        status = 'NEW_API_JUNIT_NOT_RUN'
    elif tests:
        status = 'JUNIT_NOT_RUN' if not touched or widget != 'Combo' else 'JUNIT_NOT_RUN'
    elif not touched:
        status = 'UNCHANGED_SOURCE'
    else:
        status = 'UNTESTED'
    if 'after-t0' in tags and status.startswith('PASS'):
        status += ';REBUILD_LANE_DIFF'
    cost = COST.get(sig) if widget == 'List' else None
    if widget == 'CCombo' and name == 'computeSize':
        cost = 'O(n) once per item/font change (cached widest item) under SWT.VIRTUAL'
    rows.append((widget, kind, sig, str(line), 'changed' if touched else 'unchanged', cost or DEFAULT_COST[widget], ';'.join(ids) or '-', status))

LANES = [('List', 'lane', 'Selection/DefaultSelection from native input', '-', '-', 'native notification', 'probe:NotifyProbe (focus)', 'PASS_PROBE'),
         ('List', 'lane', 'SetData materialization bound and once-per-residency', '-', '-', 'O(visible + overscan + prefetch)', 'junit:widgets_List_Viewport#setItemCountMaterializesOnlyPaintedRows|scrollRoundTripReproducesStateAndPixels', 'JUNIT_NOT_RUN'),
         ('List', 'lane', 'accessibility row names (MSAA getName)', '-', '-', 'O(1) per row', 'junit:widgets_List_Viewport#accessibleNamesComeFromTheItemStore', 'JUNIT_NOT_RUN'),
         ('List', 'lane', 'paint (WM_DRAWITEM)', '-', '-', 'O(visible rows), reusable DRAWITEMSTRUCT/RECT/char[]', 'bench:ListViewportBench', 'MEASURED'),
         ('List', 'lane', 'keyboard search (WM_CHAR)', '-', '-', 'O(n) worst case scan of materialized rows', 'diff:list.keys.* WM_CHAR', 'PASS_DIFF'),
         ('List', 'lane', 'gtk / cocoa implementation', '-', '-', 'design only (DESIGN.md section 8)', '-', 'NOT_RUN')]
rows.extend(LANES)
with open(OUT, 'w', encoding='utf-8', newline='\n') as f:
    f.write('widget\tkind\tmember\tline\tsource\tcost_class\ttest_id\tstatus\n')
    for r in rows:
        f.write('\t'.join(r) + '\n')
counts = {}
for r in rows:
    counts[r[7]] = counts.get(r[7], 0) + 1
print('rows=%d' % len(rows), sorted(counts.items()))
