"""Generate META-INF/rewrite/m3-swt-list-viewport.yml: the win32 List viewport hooks (one table, JSON-quoted).

Same recipe types and shape as the Tree generator (crate m3-viewport-catalogue-recipes, tools/gen_tree_viewport_yml.py).
Usage: python -I gen_list_viewport_yml.py <out.yml>
"""
import json
import sys

LIST = 'org.eclipse.swt.widgets.List'


def q(s):
    return json.dumps(s)


def call(statement):
    return '\tif (viewport != null) {\n\t\t%s;\n\t\treturn;\n\t}' % statement


def answer(expression):
    return '\tif (viewport != null) return %s;' % expression


COUNT = 'int count = (int)OS.SendMessage (handle, OS.LB_GETCOUNT, 0, 0);'

LIST_HOOKS = [
    # (method, parameterCount, position, anchor, statements[, parameterTypes])
    ('List', 2, 'METHOD_END', '', '\tif (ListViewport.mediates (this.style)) {\n\t\tviewport = new ListViewport (this);\n\t\tviewport.install ();\n\t}'),
    ('widgetStyle', 0, 'BEFORE', 'if ((style & SWT.SINGLE) != 0)', '\tif (ListViewport.mediates (style)) bits &= ~OS.WS_VSCROLL;'),
    ('add', 1, 'AFTER', 'if (string == null)', call('viewport.add (string, viewport.getItemCount ())')),
    ('add', 2, 'AFTER', 'if (index == -1)', call('viewport.add (string, index)')),
    ('remove', 1, 'AFTER', 'if (indices.length == 0)', call('viewport.remove (indices)'), 'int[]'),
    ('remove', 1, 'AFTER', 'checkWidget ();', call('viewport.remove (index)'), 'int'),
    ('remove', 2, 'AFTER', 'if (start > end)', call('viewport.remove (start, end)')),
    ('removeAll', 0, 'AFTER', 'checkWidget ();', call('viewport.removeAll ()')),
    ('setItems', 1, 'AFTER', 'for (String item : items)', call('viewport.setItems (items)')),
    ('setItemCount', 1, 'BEFORE', 'if (count < itemCount)', call('viewport.setItemCount (count)')),
    ('getItem', 1, 'AFTER', 'checkWidget ();', answer('viewport.getItem (index)')),
    ('getItemCount', 0, 'AFTER', 'checkWidget ();', answer('viewport.getItemCount ()')),
    ('getItems', 0, 'AFTER', 'checkWidget ();', answer('viewport.getItems ()')),
    ('indexOf', 2, 'BEFORE', COUNT, answer('viewport.indexOf (string, start)')),
    ('isSelected', 1, 'AFTER', 'checkWidget ();', answer('viewport.isSelected (index)')),
    ('getSelectionCount', 0, 'AFTER', 'checkWidget ();', answer('viewport.getSelectionCount ()')),
    ('getSelectionIndex', 0, 'AFTER', 'checkWidget ();', answer('viewport.getSelectionIndex ()')),
    ('getSelectionIndices', 0, 'AFTER', 'checkWidget ();', answer('viewport.getSelectionIndices ()')),
    ('getFocusIndex', 0, 'AFTER', 'checkWidget ();', answer('viewport.getFocusIndex ()')),
    ('setFocusIndex', 1, 'METHOD_START', '', call('viewport.setFocusIndex (index)')),
    ('getTopIndex', 0, 'AFTER', 'checkWidget ();', answer('viewport.getTopIndex ()')),
    ('setTopIndex', 1, 'AFTER', 'checkWidget ();', call('viewport.setTopIndex (index)')),
    ('showSelection', 0, 'AFTER', 'checkWidget ();', call('viewport.showSelection ()')),
    ('select', 2, 'METHOD_START', '', call('viewport.select (index, scroll)'), 'int,boolean'),
    ('select', 3, 'METHOD_START', '', call('viewport.select (start, end, scroll)')),
    ('select', 2, 'BEFORE', COUNT, call('viewport.selectRange (start, end)'), 'int,int'),
    ('selectAll', 0, 'AFTER', 'checkWidget ();', call('viewport.selectAll ()')),
    ('deselect', 1, 'AFTER', 'if (indices.length == 0)', call('viewport.deselect (indices)'), 'int[]'),
    ('deselect', 1, 'AFTER', 'if (index == -1)', call('viewport.deselect (index)'), 'int'),
    ('deselect', 2, 'AFTER', 'if (start > end)', call('viewport.deselect (start, end)')),
    ('deselectAll', 0, 'AFTER', 'checkWidget ();', call('viewport.deselectAll ()')),
    ('setSelection', 2, 'BEFORE', COUNT, call('viewport.setSelectionRange (start, end)')),
    ('setScrollWidth', 0, 'METHOD_START', '', call('viewport.recomputeExtent ()')),
    ('computeSizeInPixels', 3, 'AFTER', 'checkWidget ();', answer('viewport.computeSize (hintInPoints, zoom)')),
    ('updateTextDirection', 1, 'BEFORE', COUNT,
     '\tif (viewport != null) {\n\t\taddedUCC = false;\n\t\tviewport.reloadResidentRows ();\n'
     '\t\treturn textDirection == AUTO_TEXT_DIRECTION || super.updateTextDirection (textDirection);\n\t}'),
    ('updateMenuLocation', 1, 'METHOD_START', '', call('viewport.updateMenuLocation (event)')),
    ('callWindowProc', 4, 'AFTER', 'if (handle == 0)', '\tif (viewport != null && msg == OS.WM_VSCROLL) return viewport.onVScroll (wParam);'),
    ('callWindowProc', 4, 'AFTER', 'if (viewport != null && msg == OS.WM_VSCROLL)',
     '\tif (viewport != null && msg == OS.WM_MOUSEWHEEL) return viewport.onWheel (hwnd, wParam, lParam);'),
    ('callWindowProc', 4, 'BEFORE', 'long code;', '\tif (viewport != null && viewport.beforeNative (msg, wParam, lParam)) return 0;'),
    ('callWindowProc', 4, 'BEFORE', 'return code;', '\tif (viewport != null) viewport.afterNative (msg, wParam);'),
    ('WM_CHAR', 2, 'AFTER', 'if (result != null)', '\tif (viewport != null) {\n\t\tresult = viewport.onChar (wParam);\n\t\tif (result != null) return result;\n\t}'),
    ('WM_KEYDOWN', 2, 'AFTER', 'if (result != null)', '\tif (viewport != null) {\n\t\tresult = viewport.onKeyDown (wParam);\n\t\tif (result != null) return result;\n\t}'),
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
    'name: com.synexia.openrewrite.viewport.SwtListViewportWin32',
    'displayName: win32 List mediates its public API through the viewport',
    'description: >-',
    '  Catalogue VIEWPORT_WINDOWING. A List without SWT.VIRTUAL on win32 keeps only the resident window natively',
    '  (ListViewport, VirtualRowWindowPlan) and answers its public API from VirtualListModel. Each hook is one',
    '  delegation at the start of a method or after its argument checks; the logic lives in ListViewport.',
    '  The viewport is the default List mode; -Dswt.widget.mode.List=original (or the global swt.widget.mode) selects the native List.',
    'recipeList:',
    '  - com.synexia.openrewrite.viewport.M3AddMemberIfMissing:',
    '      declaringClasses: [%s]' % LIST,
    '      memberName: viewport',
    '      beforeMethod: List',
    '      memberSource: %s' % q('\t/* Viewport mediation of a list without SWT.VIRTUAL (see ListViewport); null in the original mode. */\n\tListViewport viewport;'),
] + entries(LIST, LIST_HOOKS)

with open(sys.argv[1], 'w', encoding='utf-8', newline='\n') as f:
    f.write('\n'.join(lines) + '\n')
print('hooks: List %d' % len(LIST_HOOKS))
