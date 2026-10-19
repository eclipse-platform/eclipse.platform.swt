# Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
# SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
"""Generate META-INF/rewrite/m3-swt-scroll-viewport.yml: the win32 plane-geometry hooks of leaf L1
(docs/m3/SCROLL_MEDIATED_VIEWPORT.md). One table per class; every row is (method, parameterCount,
position, anchor, statements[, parameterTypes]). Run: python -I gen_scroll_viewport_yml.py <out.yml>"""
import json, sys

P = 'org.eclipse.swt.widgets.'

def q(s):
    return json.dumps(s)

SET_BOUNDS = ('\tif (ViewportPlane.ENABLED && display.planeDepth == 0 && ViewportPlane.mediates (this, x, y, width, height, flags)) {\n'
              '\t\tViewportPlane.setBounds (this, x, y, width, height, flags, defer);\n\t\treturn;\n\t}')
COMBO = ('\t\tif (ViewportPlane.ENABLED && display.planeDepth == 0 && ViewportPlane.mediates (this, x, y, width, height, flags)) {\n'
         '\t\t\tViewportPlane.setBounds (this, x, y, width, height, flags & ~OS.SWP_NOSIZE, false);\n\t\t\treturn;\n\t\t}')
QUIET = '\tif (display.planeQuiet > 0) return null;'
DRAG = ('\tif (ViewportPlane.ENABLED && this instanceof Control control && control.plane != null) {\n'
        '\t\tx += control.plane.baseX;\n\t\ty += control.plane.baseY;\n\t}')

MEMBERS = [
    # (class, memberName, beforeMethod, source)
    ('Control', 'plane', 'Control', '\t/* Logical bounds while this control leaves the native coordinate range (ViewportPlane); null otherwise. */\n\tViewportPlane plane;'),
    ('Display', 'planeDepth', 'Display', '\t/* ViewportPlane: depth of the mediated setBounds that re-enters the stock path */\n\tint planeDepth;'),
    ('Display', 'planeQuiet', 'Display', '\t/* ViewportPlane: WM_MOVE and WM_SIZE caused by the mediation send no events while positive */\n\tint planeQuiet;'),
    ('Display', 'planeScratch', 'Display', '\t/* ViewportPlane: reused region, rectangle and paint GC */\n\tViewportPlane.Scratch planeScratch;'),
]

HOOKS = {
    'Control': [
        ('setBoundsInPixels', 6, 'METHOD_START', '', SET_BOUNDS, 'int,int,int,int,int,boolean'),
        ('getBoundsInPixels', 0, 'METHOD_START', '', '\tif (ViewportPlane.ENABLED && ViewportPlane.answers (this)) return ViewportPlane.getBounds (this);'),
        ('getLocationInPixels', 0, 'METHOD_START', '', '\tif (ViewportPlane.ENABLED && ViewportPlane.answers (this)) return ViewportPlane.getLocation (this);'),
        ('getSizeInPixels', 0, 'METHOD_START', '', '\tif (ViewportPlane.ENABLED && plane != null) return ViewportPlane.getSize (this);'),
        ('toDisplayInPixels', 2, 'BEFORE', 'OS.ClientToScreen (handle, pt);', '\tif (display.planeScratch != null) ViewportPlane.toNative (this, pt);'),
        ('toControlInPixels', 2, 'AFTER', 'OS.ScreenToClient (handle, pt);', '\tif (display.planeScratch != null) ViewportPlane.toLogical (this, pt);'),
        ('WM_MOVE', 2, 'METHOD_START', '', QUIET),
        ('WM_SIZE', 2, 'METHOD_START', '', QUIET),
        ('redrawInPixels', 2, 'METHOD_START', '', '\tif (ViewportPlane.ENABLED && plane != null && rect != null) rect = ViewportPlane.redrawRect (this, rect);'),
        ('internal_new_GC', 1, 'BEFORE', 'return hDC;', '\tif (plane != null && (data == null || data.ps == null) && hwnd == handle) ViewportPlane.shiftDC (this, hDC);'),
        ('drawImageBackground', 6, 'AFTER', 'OS.MapWindowPoints (hwnd, handle, rect2, 2);', '\tif (plane != null) OS.OffsetRect (rect2, plane.baseX, plane.baseY);'),
    ],
    'Composite': [
        ('setBoundsInPixels', 6, 'METHOD_START', '', SET_BOUNDS, 'int,int,int,int,int,boolean'),
        ('WM_SIZE', 2, 'BEFORE', 'if (OS.IsWindowVisible (handle))', '\tif (display.planeScratch != null) ViewportPlane.parentResized (this);'),
        ('WM_PAINT', 2, 'AFTER', 'event.setBounds(Win32DPIUtils.pixelToPoint(new Rectangle.OfFloat(ps.left, ps.top, width, height), getAutoscalingZoom()));',
         '\t\t\t\t\tif (plane != null) ViewportPlane.paintBuffered (this, event, ps.left, ps.top, width, height);'),
        ('WM_PAINT', 2, 'BEFORE', 'OS.SetRect (rect, lpRgnData [offset], lpRgnData [offset + 1], lpRgnData [offset + 2], lpRgnData [offset + 3]);',
         '\t\t\t\t\t\tif (plane != null) ViewportPlane.unpaint (this, event);'),
        ('WM_PAINT', 2, 'AFTER', 'event.setBounds(Win32DPIUtils.pixelToPoint(new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top), zoom));',
         '\t\t\t\t\t\tif (plane != null) ViewportPlane.paint (this, event, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);'),
        ('WM_PAINT', 2, 'AFTER', 'event.setBounds(Win32DPIUtils.pixelToPoint(new Rectangle(ps.left, ps.top, width, height), zoom));',
         '\t\t\t\t\tif (plane != null) ViewportPlane.paint (this, event, ps.left, ps.top, width, height);'),
        ('WM_PRINTCLIENT', 2, 'AFTER', 'event.setBounds(Win32DPIUtils.pixelToPoint(new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top), getAutoscalingZoom()));',
         '\t\t\tif (plane != null) ViewportPlane.paint (this, event, rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);'),
    ],
    'Canvas': [
        ('scrollInPixels', 7, 'AFTER', 'OS.SetRect (sourceRect, x, y, x + width, y + height);', '\tif (plane != null) OS.OffsetRect (sourceRect, -plane.baseX, -plane.baseY);'),
    ],
    'Combo': [
        ('setBoundsInPixels', 5, 'BEFORE', 'OS.SetWindowPos (handle, 0, x, y, width, height, flags);', COMBO),
    ],
    'Caret': [
        ('move', 0, 'AFTER', 'if (!OS.SetCaretPos(getXInPixels(), getYInPixels()))', '\tif (parent.plane != null) OS.SetCaretPos (getXInPixels () - parent.plane.baseX, getYInPixels () - parent.plane.baseY);'),
        ('resize', 0, 'AFTER', 'OS.SetCaretPos (getXInPixels(), getYInPixels());', '\tif (parent.plane != null) OS.SetCaretPos (getXInPixels () - parent.plane.baseX, getYInPixels () - parent.plane.baseY);'),
    ],
    'Scrollable': [
        ('getClientAreaInPixels', 0, 'BEFORE', 'return new Rectangle (x, y, width, height);',
         '\tif (plane != null) return ViewportPlane.getClientArea (this, new Rectangle (x, y, width, height));'),
    ],
    'Display': [
        ('mapInPixels', 4, 'BEFORE', 'OS.MapWindowPoints (hwndFrom, hwndTo, point, 1);', '\tif (from != null && planeScratch != null) ViewportPlane.toNative (from, point);'),
        ('mapInPixels', 4, 'AFTER', 'OS.MapWindowPoints (hwndFrom, hwndTo, point, 1);', '\tif (to != null && planeScratch != null) ViewportPlane.toLogical (to, point);'),
        ('mapInPixels', 6, 'BEFORE', 'OS.MapWindowPoints (hwndFrom, hwndTo, rect, 2);', '\tif (from != null && planeScratch != null) ViewportPlane.toNative (from, rect);'),
        ('mapInPixels', 6, 'AFTER', 'OS.MapWindowPoints (hwndFrom, hwndTo, rect, 2);', '\tif (to != null && planeScratch != null) ViewportPlane.toLogical (to, rect);'),
        ('releaseDisplay', 0, 'AFTER', 'ViewportBatch.release (this);', '\tViewportPlane.release (this);'),
    ],
    'Widget': [
        ('sendMouseEvent', 7, 'AFTER', 'event.setLocation(DPIUtil.pixelToPoint(OS.GET_X_LPARAM (lParam), zoom)',
         '\tif (ViewportPlane.ENABLED && this instanceof Control control && control.plane != null) ViewportPlane.mouse (control, event, hwnd, lParam, zoom);'),
        ('sendDragEvent', 3, 'METHOD_START', '', DRAG),
        ('sendDragEvent', 4, 'METHOD_START', '', DRAG),
    ],
}

def hook_entries(cls, hooks):
    out = []
    for hook in hooks:
        method, params, position, anchor, statements = hook[:5]
        types = hook[5] if len(hook) > 5 else None
        out.append('  - com.synexia.openrewrite.viewport.M3InsertStatements:')
        out.append('      declaringClasses: [%s%s]' % (P, cls))
        out.append('      methodName: %s' % method)
        out.append('      parameterCount: %d' % params)
        if types:
            out.append('      parameterTypes: %s' % q(types))
        if anchor:
            out.append('      anchor: %s' % q(anchor))
        out.append('      position: %s' % position)
        out.append('      statements: %s' % q(statements))
    return out

def member_entries(cls):
    out = []
    for c, name, before, source in MEMBERS:
        if c != cls:
            continue
        out.append('  - com.synexia.openrewrite.viewport.M3AddMemberIfMissing:')
        out.append('      declaringClasses: [%s%s]' % (P, c))
        out.append('      memberName: %s' % name)
        out.append('      beforeMethod: %s' % before)
        out.append('      memberSource: %s' % q(source))
    return out

ROOT = 'com.synexia.openrewrite.viewport.SwtScrollViewportWin32'
lines = [
    '---',
    'type: specs.openrewrite.org/v1beta/recipe',
    'name: %s' % ROOT,
    'displayName: win32 controls keep logical bounds beyond the native coordinate range (planes)',
    'description: >-',
    '  Catalogue VIEWPORT_GEOMETRY. A control whose bounds leave the 16-bit range of native child windows keeps',
    '  logical bounds in ViewportPlane; a control larger than the band becomes a plane whose native window covers',
    '  the band around its visible part. Each hook is one delegation; the logic lives in ViewportPlane and',
    '  ViewportPlaneGeometry. -Dswt.widget.mode.Composite=original selects stock geometry.',
    'recipeList:',
] + ['  - %s%s' % (ROOT, c) for c in HOOKS]
for cls, hooks in HOOKS.items():
    lines += [
        '---',
        'type: specs.openrewrite.org/v1beta/recipe',
        'name: %s%s' % (ROOT, cls),
        'displayName: win32 %s plane hooks' % cls,
        'description: Plane-geometry delegations of %s.' % cls,
        'recipeList:',
    ] + member_entries(cls) + hook_entries(cls, hooks)

with open(sys.argv[1], 'w', encoding='utf-8', newline='\n') as f:
    f.write('\n'.join(lines) + '\n')
print('hooks: ' + ', '.join('%s %d' % (c, len(h)) for c, h in HOOKS.items()) + '; members %d' % len(MEMBERS))
