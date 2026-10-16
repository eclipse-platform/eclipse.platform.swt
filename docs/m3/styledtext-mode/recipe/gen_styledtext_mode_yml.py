"""Generate META-INF/rewrite/m3-swt-styledtext-mode.yml: StyledText's viewport (paged content, viewport mode for large
documents, layout estimates) behind WidgetMode (leaf L4, docs/m3/STYLEDTEXT_WIDGET_MODE.md).
Run: python -I gen_styledtext_mode_yml.py <out.yml>"""
import json, sys

def q(s):
    return json.dumps(s)

ST = 'org.eclipse.swt.custom.StyledText'
RENDERER = 'org.eclipse.swt.custom.StyledTextRenderer'
FIELD = ('\t/** The StyledText viewport (paged content, viewport mode for large documents) unless the startup widget mode of\n'
         '\t * StyledText is original (-Dswt.widget.mode.StyledText=original). */\n'
         '\tstatic final boolean VIEWPORT = org.eclipse.swt.internal.WidgetMode.of ("StyledText", org.eclipse.swt.internal.WidgetMode.VIEWPORT) == org.eclipse.swt.internal.WidgetMode.VIEWPORT;')

HOOKS = [
    (ST, 'createDefaultContent', 0, 'METHOD_START', '', '\tif (!VIEWPORT) return new DefaultContent ();'),
    (ST, 'computeSize', 3, 'AFTER', 'boolean estimate = !isFixedLineHeight() && (renderer.isViewportMode() || wordWrap && wHint != SWT.DEFAULT);',
     '\t\tif (!VIEWPORT) estimate = false;'),
    (RENDERER, 'isViewportMode', 0, 'METHOD_START', '', '\tif (!StyledText.VIEWPORT) return false;'),
]

lines = [
    '---',
    'type: specs.openrewrite.org/v1beta/recipe',
    'name: com.synexia.openrewrite.viewport.SwtStyledTextWidgetMode',
    'displayName: StyledText viewport behind the startup widget mode',
    'description: >-',
    '  Catalogue VIEWPORT_MODES. The StyledText viewport (PagedContent, the renderer viewport mode for large or',
    '  SWT.VIRTUAL documents, layout estimates) is the default; -Dswt.widget.mode.StyledText=original (or the global',
    '  swt.widget.mode) restores the gap-buffer content and exact layout. Each hook is one statement.',
    'recipeList:',
    '  - com.synexia.openrewrite.viewport.M3AddMemberIfMissing:',
    '      declaringClasses: [%s]' % ST,
    '      memberName: VIEWPORT',
    '      beforeMethod: StyledText',
    '      memberSource: %s' % q(FIELD),
]
for cls, method, params, position, anchor, statement in HOOKS:
    lines.append('  - com.synexia.openrewrite.viewport.M3InsertStatements:')
    lines.append('      declaringClasses: [%s]' % cls)
    lines.append('      methodName: %s' % method)
    lines.append('      parameterCount: %d' % params)
    if anchor:
        lines.append('      anchor: %s' % q(anchor))
    lines.append('      position: %s' % position)
    lines.append('      statements: %s' % q(statement))

with open(sys.argv[1], 'w', encoding='utf-8', newline='\n') as f:
    f.write('\n'.join(lines) + '\n')
print('hooks %d + 1 member' % len(HOOKS))
