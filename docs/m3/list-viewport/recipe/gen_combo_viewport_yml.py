"""Generate META-INF/rewrite/m3-swt-combo-viewport.yml: the win32 Combo viewport hooks (one table, JSON-quoted).

Same recipe types and shape as gen_list_viewport_yml.py (crate m3-viewport-catalogue-recipes).
Usage: python -I gen_combo_viewport_yml.py <out.yml>
"""
import json
import sys

COMBO = 'org.eclipse.swt.widgets.Combo'
ACTIVE = 'viewport != null && viewport.active ()'


def q(s):
    return json.dumps(s)


def call(statement):
    return '\tif (%s) {\n\t\t%s;\n\t\treturn;\n\t}' % (ACTIVE, statement)


def answer(expression):
    return '\tif (%s) return %s;' % (ACTIVE, expression)


OLD_SELECTION = 'int oldSelection = (int)OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);'
NEW_SELECTION = 'int newSelection = (int)OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);'

COMBO_HOOKS = [
    # (method, parameterCount, position, anchor, statements[, parameterTypes])
    ('Combo', 2, 'METHOD_END', '', '\tif (ComboViewport.mediates (this.style)) {\n\t\tviewport = new ComboViewport (this);\n\t\tviewport.install ();\n\t}'),
    ('add', 1, 'AFTER', 'if (string == null)', call('viewport.add (string, viewport.getItemCount ())')),
    ('add', 2, 'AFTER', 'if (string == null)', call('viewport.add (string, index)')),
    ('remove', 2, 'METHOD_START', '', call('viewport.remove (index, notify)'), 'int,boolean'),
    ('remove', 2, 'AFTER', 'if (start > end)', call('viewport.remove (start, end)'), 'int,int'),
    ('removeAll', 0, 'AFTER', 'checkWidget ();', '\tif (%s) viewport.removeAll ();' % ACTIVE),
    ('setItems', 1, 'BEFORE', 'RECT rect = null;', call('viewport.setItems (items)')),
    ('getItem', 1, 'AFTER', 'checkWidget ();', answer('viewport.getItem (index)')),
    ('getItemCount', 0, 'AFTER', 'checkWidget ();', answer('viewport.getItemCount ()')),
    ('indexOf', 2, 'AFTER', 'if (string == null)', answer('viewport.indexOf (string, start)')),
    ('select', 1, 'AFTER', 'checkWidget ();', call('viewport.select (index)')),
    ('deselect', 1, 'AFTER', 'checkWidget ();', call('viewport.deselect (index)')),
    ('getSelectionIndex', 0, 'AFTER', 'if (noSelection)', answer('viewport.getSelectionIndex ()')),
    ('setScrollWidth', 0, 'METHOD_START', '', call('viewport.recomputeScrollWidth ()')),
    ('setScrollWidth', 1, 'AFTER', 'int count = (int)OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);',
     '\tif (%s) count = viewport.getItemCount ();' % ACTIVE, 'int'),
    ('applyListSegments', 0, 'METHOD_START', '', '\tif (viewport != null) viewport.uninstall ();'),
    ('windowProc', 4, 'AFTER', 'long code = OS.CB_ERR;', '\tif (%s) return viewport.onSetCursel (wParam, lParam);' % ACTIVE),
    # notifications (CBN_DROPDOWN prepares the window, CBN_SELCHANGE pays an owed one); SWT's cases have no braces
    ('wmCommandChild', 2, 'METHOD_START', '', '\tif (%s) viewport.onNotification (OS.HIWORD (wParam));' % ACTIVE),
    ('WM_LBUTTONDOWN', 2, 'AFTER', OLD_SELECTION, '\tif (%s) oldSelection = viewport.getSelectionIndex ();' % ACTIVE),
    ('WM_LBUTTONDOWN', 2, 'AFTER', NEW_SELECTION, '\tif (%s) newSelection = viewport.getSelectionIndex ();' % ACTIVE),
    ('wmSysKeyDown', 3, 'AFTER', OLD_SELECTION, '\tif (%s) oldSelection = viewport.getSelectionIndex ();' % ACTIVE),
    ('wmSysKeyDown', 3, 'AFTER', NEW_SELECTION, '\tif (%s) newSelection = viewport.getSelectionIndex ();' % ACTIVE),
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
    'name: com.synexia.openrewrite.viewport.SwtComboViewportWin32',
    'displayName: win32 drop-down Combo mediates its item API through the viewport',
    'description: >-',
    '  Catalogue VIEWPORT_WINDOWING. A drop-down Combo (editable or READ_ONLY) on win32 keeps only the resident window in its',
    '  drop-down list box (ComboViewport, VirtualRowWindowPlan) and answers its item API from VirtualListModel. Each hook is one',
    '  delegation at the start of a method, after its argument checks or after a native read; the logic lives in ComboViewport.',
    '  The viewport is the default Combo mode; -Dswt.widget.mode.Combo=original (or the global swt.widget.mode) selects the native Combo.',
    'recipeList:',
    '  - com.synexia.openrewrite.viewport.M3AddMemberIfMissing:',
    '      declaringClasses: [%s]' % COMBO,
    '      memberName: viewport',
    '      beforeMethod: Combo',
    '      memberSource: %s' % q('\t/* Viewport mediation of a drop-down combo (see ComboViewport); null in the original mode and for SWT.SIMPLE. */\n\tComboViewport viewport;'),
] + entries(COMBO, COMBO_HOOKS)

with open(sys.argv[1], 'w', encoding='utf-8', newline='\n') as f:
    f.write('\n'.join(lines) + '\n')
print('hooks: Combo %d' % len(COMBO_HOOKS))
