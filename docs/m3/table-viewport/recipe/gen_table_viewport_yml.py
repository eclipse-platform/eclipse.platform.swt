"""Generate META-INF/rewrite/m3-swt-table-viewport.yml: the win32 Table/TableColumn viewport hooks (one table, JSON-quoted).

Usage: python -I gen_table_viewport_yml.py <output.yml>
Every hook is one delegation statement inserted by com.synexia.openrewrite.viewport.M3InsertStatements; the logic
lives in TableViewport. Same shape as tools/gen_tree_viewport_yml.py of the m3-viewport-catalogue-recipes crate.
"""
import json, sys

TABLE = 'org.eclipse.swt.widgets.Table'
COLUMN = 'org.eclipse.swt.widgets.TableColumn'

def q(s):
    return json.dumps(s)

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
]

COLUMN_HOOKS = [
    ('calcAutoWidth', 2, 'BEFORE', 'RECT rect = null;',
     '\t\tint viewportTop = parent.viewport != null ? parent.viewport.revealWidest (index) : -1;'),
    ('calcAutoWidth', 2, 'BEFORE', 'int newWidth = (int)OS.SendMessage (hwnd, OS.LVM_GETCOLUMNWIDTH, index, 0);',
     '\t\tif (viewportTop >= 0) parent.viewport.restoreTop (viewportTop);'),
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
    '---',
    'type: specs.openrewrite.org/v1beta/recipe',
    'name: com.synexia.openrewrite.viewport.SwtTableViewportWin32Table',
    'displayName: win32 Table viewport hooks',
    'description: The viewport field and the Table delegations.',
    'recipeList:',
    '  - com.synexia.openrewrite.viewport.M3AddMemberIfMissing:',
    '      declaringClasses: [%s]' % TABLE,
    '      memberName: viewport',
    '      beforeMethod: Table',
    '      memberSource: %s' % q('\t/* Viewport mediation of a plain Table (see TableViewport); null for SWT.VIRTUAL and in the original mode. */\n\tTableViewport viewport;'),
] + entries(TABLE, TABLE_HOOKS) + [
    '---',
    'type: specs.openrewrite.org/v1beta/recipe',
    'name: com.synexia.openrewrite.viewport.SwtTableViewportWin32TableColumn',
    'displayName: win32 TableColumn viewport hooks',
    'description: The column autosize of an owner-data ListView measures only the rows in view; the widest row is shown first.',
    'recipeList:',
] + entries(COLUMN, COLUMN_HOOKS)

with open(sys.argv[1], 'w', encoding='utf-8', newline='\n') as f:
    f.write('\n'.join(lines) + '\n')
print('hooks: Table %d, TableColumn %d' % (len(TABLE_HOOKS), len(COLUMN_HOOKS)))
