# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
"""Generate META-INF/rewrite/m3-swt-table-viewport.yml: the win32 Table/TableColumn viewport hooks (one table, JSON-quoted).

Usage: python -I gen_table_viewport_yml.py <output.yml>
Every hook is one delegation statement inserted by com.synexia.openrewrite.viewport.M3InsertStatements; the logic
lives in TableViewport. Same shape as tools/gen_tree_viewport_yml.py of the m3-viewport-catalogue-recipes crate.
"""
import json, sys

TABLE = 'org.eclipse.swt.widgets.Table'
COLUMN = 'org.eclipse.swt.widgets.TableColumn'
ITEM = 'org.eclipse.swt.widgets.TableItem'
BAR = 'org.eclipse.swt.widgets.ScrollBar'

def q(s):
    return json.dumps(s)

def block(cond, call):
    """One delegation that ends the method: if (cond) { call; return; }"""
    return '\tif (%s) {\n\t\t%s;\n\t\treturn;\n\t}' % (cond, call)

TABLE_HOOKS = [
    # (method, parameterCount, position, anchor, statements)
    ('widgetStyle', 0, 'BEFORE', 'return bits;',
     '\tif (TableViewport.ENABLED) bits |= OS.LVS_OWNERDATA;'),
    ('createWidget', 0, 'METHOD_END', '',
     '\tif (TableViewport.ENABLED && (style & SWT.VIRTUAL) == 0) viewport = new TableViewport (this);'),
    ('wmNotifyChild', 3, 'BEFORE', 'if ((style & SWT.VIRTUAL) != 0) { return new LRESULT(-1);',
     '\t\t\tif (viewport != null) return viewport.findItem (lParam);'),
    ('wmNotifyChild', 3, 'BEFORE', 'if ((style & SWT.VIRTUAL) != 0) { if (!ignoreSelect)',
     '\t\t\tif (viewport != null) viewport.stateChanged (lParam);'),
    ('setItemCount', 1, 'AFTER', 'count = Math.max (0, count);',
     '\tif (viewport != null) {\n\t\tviewport.setItemCount (count);\n\t\treturn;\n\t}'),
    # T-Table-3: facades as pinned proxies with row hints; rows nobody asked for have no facade
    ('_getItem', 3, 'BEFORE', 'if (index >= items.length)',
     '\tif (viewport != null) return viewport.facade (index, create);'),
    ('_getItems', 2, 'METHOD_START', '',
     '\tif (viewport != null) {\n\t\tviewport.items (result, count);\n\t\treturn;\n\t}'),
    ('_insertItem', 3, 'METHOD_START', '',
     '\tif (viewport != null) viewport.rowsInserted (index, 1);'),
    ('_removeItem', 2, 'METHOD_START', '',
     '\tif (viewport != null) viewport.rowsRemoved (index, 1);'),
    ('_removeItems', 3, 'METHOD_START', '',
     '\tif (viewport != null) viewport.rowsRemoved (start, index - start);'),
    ('_setItemCount', 2, 'METHOD_START', '',
     '\tif (viewport != null) viewport.rowsResized (count);'),
    ('_initItems', 0, 'METHOD_END', '',
     '\tif (viewport != null) viewport.rowsResized (0);'),
    # T-Table-3b: columnar lanes; facades held weakly beyond the most recent ones
    ('createItem', 2, 'METHOD_END', '',
     '\tif (viewport != null) viewport.created (item);', 'TableItem,int'),
    # the lanes move with the facades' arrays, right after the Table's loop over the items
    ('createItem', 2, 'AFTER', 'for (int i=0; i<itemCount; i++) { TableItem item = _getItem (i, false); if (item != null) { String [] strings = item.strings;',
     '\tif (viewport != null) viewport.columnInserted (index);', 'TableColumn,int'),
    ('destroyItem', 1, 'AFTER', 'for (int i=0; i<itemCount; i++) { TableItem item = _getItem (i, false); if (item != null) { if (columnCount == 0)',
     '\tif (viewport != null) viewport.columnRemoved (index);', 'TableColumn'),
    ('wmNotifyChild', 3, 'METHOD_START', '',
     '\tif (viewport != null && hdr.code == TableViewport.LVN_ODCACHEHINT) viewport.cacheHint (lParam);'),
    # T-Table-4: appends batched into one native count change; incremental column-less width
    ('windowProc', 4, 'METHOD_START', '',
     '\tif (viewport != null && hwnd == handle && handle != 0) {\n\t\tlong answer = viewport.beforeMessage (msg, wParam, lParam);\n\t\tif (answer != TableViewport.PASS) return answer;\n\t}'),
    ('setScrollWidth', 2, 'AFTER', 'fixScrollWidth = false;',
     '\tif (viewport != null && item == null && columnCount == 0) return viewport.widenToWidestRow ();', 'TableItem,boolean'),
    ('setFont', 1, 'METHOD_START', '',
     '\tif (viewport != null) viewport.fontChanged ();'),
    # the Table's loop reaches only the strongly held facades
    ('updateTextDirection', 1, 'AFTER', 'int direction = textDirection == AUTO_TEXT_DIRECTION ? AUTO_TEXT_DIRECTION : style & SWT.FLIP_TEXT_DIRECTION;',
     '\t\t\tif (viewport != null) viewport.textDirectionChanged (direction);'),
    # T-Table-4: LVM_SETITEM writes that owner data ignores are skipped
    ('clear', 1, 'BEFORE', 'if ((style & SWT.VIRTUAL) == 0 && item.cached)',
     '\t\tif (viewport != null) item.cached = false;', 'int'),
    ('clear', 2, 'BEFORE', 'if ((style & SWT.VIRTUAL) == 0 && item.cached)',
     '\t\t\t\tif (viewport != null) item.cached = false;'),
    ('clear', 1, 'BEFORE', 'if ((style & SWT.VIRTUAL) == 0 && item.cached)',
     '\t\t\tif (viewport != null) item.cached = false;', 'int[]'),
    ('clearAll', 0, 'BEFORE', 'if ((style & SWT.VIRTUAL) == 0 && item.cached)',
     '\t\t\tif (viewport != null) item.cached = false;'),
    ('createItem', 2, 'BLOCK_START', 'if ((style & SWT.VIRTUAL) == 0) { LVITEM lvItem = new LVITEM ();',
     '\t\t\tif (viewport != null) itemCount = 0;', 'TableColumn,int'),
    ('destroyItem', 1, 'AFTER', 'int itemCount = (int)OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);',
     '\t\t\tif (viewport != null) itemCount = 0;', 'TableColumn'),
    ('indexOf', 1, 'BEFORE', 'int count = (int)OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);',
     '\tif (viewport != null) return viewport.indexOf (item);', 'TableItem'),
    ('destroyItem', 1, 'METHOD_START', '',
     '\tif (viewport != null) {\n\t\tviewport.destroyItem (item);\n\t\treturn;\n\t}', 'TableItem'),
    ('wmNotifyChild', 3, 'BEFORE', 'TableItem item = _getItem (plvfi.iItem);',
     '\t\t\tif (viewport != null) viewport.painting++;'),
    ('wmNotifyChild', 3, 'AFTER', 'TableItem item = _getItem (plvfi.iItem);',
     '\t\t\tif (viewport != null) viewport.painting--;'),
    # T-Table-7: a Selection event of input only when the selection changed (owner data notifies unchanged ranges)
    ('callWindowProc', 5, 'BEFORE', 'long code = 0;',
     '\tObject viewportSelection = viewport != null && checkSelection ? viewport.selectionBegin (msg, wParam) : null;'),
    ('callWindowProc', 5, 'BEFORE', 'if (checkSelection) { if (wasSelected || forceSelect)',
     '\tif (viewport != null && checkSelection) wasSelected = viewport.selectionEnd (viewportSelection, wasSelected);'),
    ('wmNotifyChild', 3, 'BEFORE', 'if (!ignoreSelect) { NMLISTVIEW pnmlv = new NMLISTVIEW ();',
     '\t\t\tif (viewport != null) viewport.itemChanged (lParam);'),
    ('wmNotifyChild', 3, 'BEFORE', 'switch (nmcd.dwDrawStage)',
     '\t\t\tif (viewport != null && TableViewport.drawsStage (nmcd)) return viewport.customDraw (nmcd, wParam, lParam);'),
]

# Rule 12 (viewport agent brief): the original mode is stock. A VIRTUAL Table of the original mode keeps the R4_38
# item store (TableStockItems), with the R4_38 _removeItems defect; the default mode keeps VirtualItemStorage.
S = 'stockItems != null'
STOCK_HOOKS = [
    ('_initItems', 0, 'METHOD_END', '', '\tstockItems = TableStockItems.of (this);'),
    ('_clearItems', 0, 'METHOD_END', '', '\tstockItems = null;'),
    ('_checkGrow', 1, 'METHOD_START', '', '\tif (%s) return stockItems.checkGrow (count);' % S),
    ('_checkShrink', 0, 'METHOD_START', '', block(S, 'stockItems.checkShrink ()')),
    ('_getItem', 3, 'METHOD_START', '', '\tif (%s) return stockItems.get (index, create, count);' % S),
    ('_getItems', 2, 'METHOD_START', '', block(S, 'stockItems.getItems (result, count)')),
    ('_insertItem', 3, 'METHOD_START', '', block(S, 'stockItems.insert (index, item, count)')),
    ('_removeItem', 2, 'METHOD_START', '', block(S, 'stockItems.remove (index, count)')),
    ('_removeItems', 3, 'METHOD_START', '', block(S, 'stockItems.removeRange (start, index, count)')),
    ('_setItemCount', 2, 'METHOD_START', '', block(S, 'stockItems.setItemCount (count, itemCount)')),
    ('indexOf', 1, 'BEFORE', 'if ((style & SWT.VIRTUAL) != 0) { return virtualItems.indexOfIdentity(item);',
     '\tif (%s) return stockItems.indexOf (item);' % S, 'TableItem'),
    ('releaseChildren', 1, 'METHOD_START', '', '\tif (%s) stockItems.release ();' % S),
    ('updateTextDirection', 1, 'BEFORE', 'OS.InvalidateRect (handle, null, true);',
     '\t\tif (%s && (textDirection == AUTO_TEXT_DIRECTION || (state & HAS_AUTO_DIRECTION) != 0)) stockItems.forEach (item -> item.updateTextDirection (textDirection == AUTO_TEXT_DIRECTION ? AUTO_TEXT_DIRECTION : style & SWT.FLIP_TEXT_DIRECTION));' % S),
    ('dpiResidentItems', 0, 'METHOD_START', '', '\tif (%s) return getItems ();' % S),
]
P = 'parent.stockItems != null'
STOCK_ITEM_HOOKS = [
    ('isCachedState', 0, 'METHOD_START', '', '\tif (%s) return cached;' % P),
    ('setCachedState', 1, 'METHOD_START', '', block(P, 'cached = value')),
    ('isCheckedState', 0, 'METHOD_START', '', '\tif (%s) return checked;' % P),
    ('setCheckedState', 1, 'METHOD_START', '', block(P, 'checked = value')),
    ('isGrayedState', 0, 'METHOD_START', '', '\tif (%s) return grayed;' % P),
    ('setGrayedState', 1, 'METHOD_START', '', block(P, 'grayed = value')),
    ('pinVirtualFacade', 0, 'METHOD_START', '', '\tif (%s) return;' % P),
    ('markVirtualDirty', 0, 'METHOD_START', '', '\tif (%s) return;' % P),
    ('markVirtualPainted', 0, 'METHOD_START', '', '\tif (%s) return;' % P),
]

STORE = '\tif (parent.viewport != null) parent.viewport.store (this);'
ITEM_HOOKS = [
    # every TableItem setter writes the facade's state back to its row (TableViewport.store)
    ('setBackground', 1, 'METHOD_END', '', STORE),
    ('setBackground', 2, 'METHOD_END', '', STORE),
    ('setForeground', 1, 'METHOD_END', '', STORE),
    ('setForeground', 2, 'METHOD_END', '', STORE),
    ('setFont', 1, 'METHOD_END', '', STORE),
    ('setFont', 2, 'METHOD_END', '', STORE),
    ('setChecked', 2, 'METHOD_END', '', STORE),
    ('setGrayed', 1, 'METHOD_END', '', STORE),
    ('setImage', 2, 'METHOD_END', '', STORE),
    ('setImageIndent', 1, 'METHOD_END', '', STORE),
    ('setText', 2, 'METHOD_END', '', STORE),
    ('clear', 0, 'METHOD_END', '', STORE),
    # T-Table-4: owner data ignores LVM_SETITEM; an appended row the native control does not count yet needs no repaint
    ('setFont', 1, 'BEFORE', 'if ((parent.style & SWT.VIRTUAL) == 0 && cached)',
     '\tif (parent.viewport != null) cached = false;'),
    ('setFont', 2, 'BEFORE', 'if ((parent.style & SWT.VIRTUAL) == 0 && cached)',
     '\t\tif (parent.viewport != null) cached = false;'),
    ('setText', 2, 'BEFORE', 'if ((parent.style & SWT.VIRTUAL) == 0 && cached)',
     '\t\tif (parent.viewport != null) cached = false;'),
    ('setImageIndent', 1, 'AFTER', 'int index = parent.indexOf (this);',
     '\t\tif (parent.viewport != null) index = -1;'),
    ('redraw', 0, 'METHOD_START', '',
     '\tif (parent.viewport != null && parent.viewport.pending (this)) return;'),
    ('redraw', 3, 'METHOD_START', '',
     '\tif (parent.viewport != null && parent.viewport.pending (this)) return;'),
]

COLUMN_HOOKS = [
    # T-Table-4: with a MeasureItem listener pack sends MeasureItem for every row, as for stock rows
    ('pack', 0, 'AFTER', 'TableItem item = parent._getItem (i, false);',
     '\t\t\tif (item == null && parent.viewport != null) item = parent._getItem (i);'),
    ('calcAutoWidth', 2, 'BEFORE', 'RECT rect = null;',
     '\t\tint viewportTop = parent.viewport != null ? parent.viewport.revealWidest (index) : -1;'),
    ('calcAutoWidth', 2, 'BEFORE', 'int newWidth = (int)OS.SendMessage (hwnd, OS.LVM_GETCOLUMNWIDTH, index, 0);',
     '\t\tif (viewportTop >= 0) parent.viewport.restoreTop (viewportTop);'),
]

BAR_HOOKS = [
    # T-Table-6: the thumb geometry is read with GetScrollBarInfo on the handle, not through scrolledHandle
    ('getThumbBoundsInPixels', 0, 'METHOD_START', '',
     '	if (parent instanceof Table table && table.viewport != null) table.viewport.flush ();'),
    ('getThumbTrackBoundsInPixels', 0, 'METHOD_START', '',
     '	if (parent instanceof Table table && table.viewport != null) table.viewport.flush ();'),
]

def entries(cls, hooks):
    out = []
    for hook in hooks:
        method, params, position, anchor, statements = hook[:5]
        types = hook[5] if len(hook) > 5 else None
        out.append('  - com.synexia.openrewrite.viewport.M3InsertStatements:')
        out.append('      declaringClasses: [%s]' % cls)
        out.append('      methodName: %s' % method)
        out.append('      parameterCount: %d' % params)
        if types:
            out.append('      parameterTypes: %s' % q(types))
        if anchor:
            out.append('      anchor: %s' % q(anchor))
        out.append('      position: %s' % position)
        out.append('      statements: %s' % q(statements))
    return out

lines = [
    '---',
    'type: specs.openrewrite.org/v1beta/recipe',
    'name: com.synexia.openrewrite.viewport.SwtTableViewportWin32',
    'displayName: win32 Table realizes every row as owner data and keeps the plain API',
    'description: >-',
    '  Catalogue VIEWPORT_WINDOWING. A plain Table on win32 is an owner-data ListView (TableViewport): the native',
    '  control holds no item per row, its bar covers every logical row, and the type-ahead, the count shrink and the',
    '  column autosize are mediated by rules measured on the native control. Each hook is one delegation.',
    '  The viewport is the default Table mode; -Dswt.widget.mode.Table=original (or the global swt.widget.mode) selects the native Table.',
    'recipeList:',
    '  - com.synexia.openrewrite.viewport.SwtTableViewportWin32Table',
    '  - com.synexia.openrewrite.viewport.SwtTableViewportWin32TableColumn',
    '  - com.synexia.openrewrite.viewport.SwtTableViewportWin32TableItem',
    '  - com.synexia.openrewrite.viewport.SwtTableViewportWin32ScrollBar',
    '---',
    'type: specs.openrewrite.org/v1beta/recipe',
    'name: com.synexia.openrewrite.viewport.SwtTableViewportWin32Table',
    'displayName: win32 Table viewport hooks',
    'description: The viewport field and the Table delegations.',
    'recipeList:',
    '  - com.synexia.openrewrite.viewport.M3AddMemberIfMissing:',
    '      declaringClasses: [%s]' % TABLE,
    '      memberName: scrolledHandle',
    '      beforeMethod: select',
    '      memberSource: %s' % q('@Override\nlong scrolledHandle () {\n\t/* the native bar follows the rows appended since the last native count change (TableViewport) */\n\tif (viewport != null) viewport.flush ();\n\treturn super.scrolledHandle ();\n}'),
    '  - com.synexia.openrewrite.viewport.M3AddMemberIfMissing:',
    '      declaringClasses: [%s]' % TABLE,
    '      memberName: viewport',
    '      beforeMethod: Table',
    '      memberSource: %s' % q('\t/* Viewport mediation of a plain Table (see TableViewport); null for SWT.VIRTUAL and in the original mode. */\n\tTableViewport viewport;'),
    '  - com.synexia.openrewrite.viewport.M3AddMemberIfMissing:',
    '      declaringClasses: [%s]' % TABLE,
    '      memberName: stockItems',
    '      beforeMethod: Table',
    '      memberSource: %s' % q('\t/* The R4_38 item store of a VIRTUAL Table in the original mode (see TableStockItems); null otherwise. */\n\tTableStockItems stockItems;'),
] + entries(TABLE, TABLE_HOOKS + STOCK_HOOKS) + [
    '---',
    'type: specs.openrewrite.org/v1beta/recipe',
    'name: com.synexia.openrewrite.viewport.SwtTableViewportWin32TableColumn',
    'displayName: win32 TableColumn viewport hooks',
    'description: The column autosize of an owner-data ListView measures only the rows in view; the widest row is shown first.',
    'recipeList:',
] + entries(COLUMN, COLUMN_HOOKS) + [
    '---',
    'type: specs.openrewrite.org/v1beta/recipe',
    'name: com.synexia.openrewrite.viewport.SwtTableViewportWin32TableItem',
    'displayName: win32 TableItem row hint',
    'description: A facade of a mediated Table holds its row as a hint; TableViewport.indexOf checks it before use.',
    'recipeList:',
    '  - com.synexia.openrewrite.viewport.M3AddMemberIfMissing:',
    '      declaringClasses: [%s]' % ITEM,
    '      memberName: viewportRow',
    '      beforeMethod: TableItem',
    '      memberSource: %s' % q('\t/* Row of this item in a mediated Table (see TableViewport): a hint, checked before use. */\n\tint viewportRow = -1;'),
] + entries(ITEM, ITEM_HOOKS + STOCK_ITEM_HOOKS) + [
    '---',
    'type: specs.openrewrite.org/v1beta/recipe',
    'name: com.synexia.openrewrite.viewport.SwtTableViewportWin32ScrollBar',
    'displayName: win32 ScrollBar thumb geometry of a mediated Table',
    'description: The thumb bounds of a Table bar follow the rows appended since the last native count change.',
    'recipeList:',
] + entries(BAR, BAR_HOOKS)

with open(sys.argv[1], 'w', encoding='utf-8', newline='\n') as f:
    f.write('\n'.join(lines) + '\n')
print('hooks: Table %d + %d stock + 2 members, TableColumn %d, TableItem %d + %d stock + 1 member, ScrollBar %d' % (len(TABLE_HOOKS), len(STOCK_HOOKS), len(COLUMN_HOOKS), len(ITEM_HOOKS), len(STOCK_ITEM_HOOKS), len(BAR_HOOKS)))
