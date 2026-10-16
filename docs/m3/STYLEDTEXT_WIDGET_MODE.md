# StyledText behind the startup widget mode (leaf L4)

Status: implemented; the viewport mode is recorded equal to the official SWT 3.135.0 jar on win32 (invisible desktop).
Plan: [`ALL_WIDGETS_VIEWPORT_PLAN.md`](ALL_WIDGETS_VIEWPORT_PLAN.md) leaf L4. Earlier StyledText viewport work:
[`STYLEDTEXT_VIEWPORT_DESIGN.md`](STYLEDTEXT_VIEWPORT_DESIGN.md), [`STYLEDTEXT_RETAINED_PLANES.md`](STYLEDTEXT_RETAINED_PLANES.md),
[`STYLEDTEXT_API_COVERAGE.md`](STYLEDTEXT_API_COVERAGE.md), [`STYLEDTEXT_VISIBILITY_GATE.md`](STYLEDTEXT_VISIBILITY_GATE.md).

## Switch

`StyledText.VIEWPORT = WidgetMode.of ("StyledText", WidgetMode.VIEWPORT) == WidgetMode.VIEWPORT` (default true).
With `-Dswt.widget.mode.StyledText=original` (or the global `swt.widget.mode=original`):

| Viewport part | Viewport mode (default) | Original mode |
|---|---|---|
| default content | `PagedContent` (piece table, line counts per page) | `DefaultContent` (gap buffer) |
| renderer viewport mode (documents above 65,536 lines or `SWT.VIRTUAL`) | on: visible window plus bounded overscan, O(1) distant positions | off: every line measured as stock |
| `computeSize` with word wrap and a width hint | lays out one display of lines and extrapolates | lays out every line (exact) |

`-Dorg.eclipse.swt.custom.StyledText.content=gap` still selects the gap buffer in the viewport mode. Hooks: recipe
`styledtext-mode/recipe` (`M3AddMemberIfMissing` + three `M3InsertStatements`; second pass UNCHANGED).

## Recorded differential (invisible desktop, jars `aw11` and `aw12`)

`Test_org_eclipse_swt_custom_StyledText_ViewportDifferential.recordedAgainstReference`: 8 seeds x 120 steps over
`setText` (0-300 lines, and 70,000 lines: above the viewport-mode threshold, fixed line height), `replaceTextRange`,
`append`, `setSelection`, `setCaretOffset`, `setTopIndex`, `setTopPixel`, `setHorizontalPixel`, `invokeAction` (line, page,
text, word, column, select, line start/end), word wrap toggles (documents up to 1,000 lines), `setStyleRange`,
`setLineBackground`, shell resize, `setMargins`, scroll bar messages, `setLineSpacing`, `getTextRange`. Every step records
the answer, the state (character and line counts, top index and pixel, horizontal pixel, caret, caret line, selection,
a line pixel, the caret location, line height, client area), both scroll bars, the events (Modify, ExtendedModify,
Selection, CaretMoved, scroll bar Selection) and the Paint regions; every seed the rendered pixels.

| Comparison | Result |
|---|---|
| viewport mode vs the official 3.135.0 recording | pass: answers, state, bars, events and pixels equal; Paint regions within the official ones |
| viewport mode vs the original-mode recording | pass |

**Paint regions.** The official StyledText repaints its whole client area in cases where the fork repaints nothing or a
strip (for example `setCaretOffset` to the current offset of an empty document: official `0,0 399x299`, fork none). This is
the damage-only repaint of the earlier StyledText leaves (#123: scroll by blit, no full repaints for caret and selection).
The differential checks it with `ViewportDifferential.REGIONS_WITHIN`: every Paint rectangle of the viewport run lies
inside one the official run painted, and the pixels are identical.

## Regression suites (invisible desktop)

`Test_org_eclipse_swt_custom_StyledText`, `..._ApiDifferential`, `..._VisibilityDifferential`, `StyledTextViewportProofTest`,
`Test_org_eclipse_swt_custom_StyledTextLineSpacingProvider`:

| Run | Passed | Failed | Aborted |
|---|---|---|---|
| jar `aw9` (before this leaf), viewport | 677 | 2 | 3 |
| jar `aw11`, viewport | 677 | 2 | 3 |
| jar `aw11`, original | 675 | 4 | 3 |

`test_backspaceAndDelete` fails in every run: it types through `Display.post`, which cannot reach the invisible desktop.
The remaining failures are `ApiDifferential` steps (paged vs gap content in one JVM: `SELECT_LINE_UP`, `SELECT_WINDOW_END`,
`WORD_PREVIOUS`, `autoscroll-left`) whose set changes from run to run, including before this leaf.

## Non-claims

- The fork's StyledText retained-plane and caret changes (#123) are not behind the mode: the original mode keeps the
  fork's damage-only repaint and integer extra carets. Making the original mode repaint like the official jar needs the
  stock code paths back for those sites (next StyledText leaf).
- Word wrap on documents above 65,536 lines uses estimates in the viewport mode by design (distant line positions are
  extrapolated, then reconciled as lines are measured); the differential toggles word wrap only up to 1,000 lines.
- GTK and Cocoa are not run.
