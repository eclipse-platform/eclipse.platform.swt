# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
"""Generate META-INF/rewrite/m3-swt-render-modes.yml: the hooks of the drawn planes (docs/m3/RENDER_MODES.md).

Usage: python -I gen_render_modes_yml.py <output.yml>
Every hook is one delegation inserted by com.synexia.openrewrite.viewport.M3InsertStatements (fields by
M3AddMemberIfMissing); the logic lives in <Widget>DrawnPlane. Same shape as the Table viewport generator.
"""
import json, sys

TREE = 'org.eclipse.swt.widgets.Tree'

def q(s):
    return json.dumps(s)

MEMBERS = [
    # (class, memberName, beforeMethod, source)
    (TREE, 'drawnPlane', 'Tree',
     '\t/* The drawn plane of the custom, m3 and gpu modes (see TreeDrawnPlane); null in the viewport and original modes. */\n\tTreeDrawnPlane drawnPlane;'),
]

HOOKS = [
    # (class, method, parameterCount, position, anchor, statements)
    (TREE, 'createWidget', 0, 'METHOD_END', '',
     '\tdrawnPlane = TreeDrawnPlane.install (this);'),
    (TREE, 'callWindowProc', 4, 'BEFORE', 'if (viewport != null && msg == OS.WM_MOUSEWHEEL) return viewport.onWheel (wParam);',
     '\tif (drawnPlane != null && drawnPlane.paints (hwnd, msg)) return drawnPlane.paint (hwnd, msg, wParam, lParam);'),
    (TREE, 'releaseWidget', 0, 'METHOD_END', '',
     '\tif (drawnPlane != null) {\n\t\tdrawnPlane.release ();\n\t\tdrawnPlane = null;\n\t}'),
]

def recipe():
    out = ['---', 'type: specs.openrewrite.org/v1beta/recipe',
           'name: com.synexia.openrewrite.viewport.SwtRenderModesWin32',
           'displayName: win32 drawn planes of the custom, m3 and gpu modes',
           'description: >-',
           '  The drawn modes are viewport modes with another plane: the native control stays the behaviour plane and',
           '  never paints to the screen; the drawing core paints the resident window. Each hook is one delegation.',
           'recipeList:']
    for cls, name, before, source in MEMBERS:
        out += ['  - com.synexia.openrewrite.viewport.M3AddMemberIfMissing:',
                '      declaringClasses: [%s]' % cls,
                '      memberName: %s' % name,
                '      beforeMethod: %s' % before,
                '      memberSource: %s' % q(source)]
    for cls, method, count, position, anchor, statements in HOOKS:
        out += ['  - com.synexia.openrewrite.viewport.M3InsertStatements:',
                '      declaringClasses: [%s]' % cls,
                '      methodName: %s' % method,
                '      parameterCount: %d' % count]
        if anchor:
            out.append('      anchor: %s' % q(anchor))
        out += ['      position: %s' % position,
                '      statements: %s' % q(statements)]
    return '\n'.join(out) + '\n'

if __name__ == '__main__':
    with open(sys.argv[1], 'w', encoding='utf-8', newline='') as f:
        f.write(recipe())
