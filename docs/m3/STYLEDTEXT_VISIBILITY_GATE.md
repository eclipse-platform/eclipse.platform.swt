# StyledText visibility gate (2026-10-13)

User law: "IF A WIDGET IS NOT VISIBLE WHY SHOULD IT EVEN BE PAINTED".

Branch `m3/styledtext-visibility-gate-20261013`, stacked on leaf 2 (`m3/styledtext-api-coverage-20261013`,
SWT #118). Evidence: `.m3/styledtext-visibility-gate-20261013/`.

## 1. Contract

While a StyledText is not effectively visible it does no idle measurement pass, no autoscroll timer,
no client-area pre-measurement, no caret layout and no redraw work. It records what it skipped as
dirty bits and settles them once, when it first paints or is resized while visible. Every public and
protected API keeps its exact results: a caller's query is answered from the current state, measuring
on demand as before.

## 2. Effective visibility (interim)

`StyledText.isEffectivelyVisible()` is the single switch point:

```java
return clientAreaWidth > 0 && clientAreaHeight > 0 && isVisible() && !getShell().getMinimized();
```

`isVisible()` covers the control and every ancestor (a hidden `CTabFolder` tab, an unopened shell);
the client-area fields cover a zero-size control; `getMinimized()` covers a minimized shell. Another
agent owns the win32 `Control`/`Composite`/`Shell`/`Display` effective-visibility bit (clipped,
scrolled out, occluded, cloaked), in `Widget.state` bits 25 to 31. When it lands, only the body of this
method changes. StyledText never uses those `state` bits: its dirty bits are its own int field.

## 3. Dirty bits (`int deferredWork`, StyledText's own field)

| Bit | Mask | Skipped while hidden | Settled by |
|---|---|---|---|
| `DEFER_PAINT` | `1` | `internalRedrawRange`, `redrawLines`, `redrawLinesBullet`, `scrollText` (copy area), the redraw geometry in `handleTextChanged`, `handleTextChanging` (changes at or below the top line) and `setStyleRanges` | `super.redraw()` of the whole client area |
| `DEFER_LAYOUT` | `1 << 1` | client-area pre-measurement (`measureClientArea` in the `resetCache` family, `calculateTopIndex`, `handleResize`, `redraw()`), wrap-mode `claimBottomFreeSpace` | `measureClientArea`, `setScrollBars(true)`, `claimBottomFreeSpace()` |
| `DEFER_MEASURE` | `1 << 2` | the idle measurement pass (`calculateIdle`, `calculateIdleWindow`; a queued pass stops at its next slice) | `renderer.calculateIdle()` (the visible window in viewport mode) |
| `DEFER_CARET` | `1 << 3` | layout for the default caret's placement (`setCaretLocations`, including the constructor's); the remembered column `columnX` becomes `COLUMN_X_UNSET` | `setCaretLocations()`; a column restored after the deferral (line/page navigation) is kept |
| `DEFER_SETTLING` | `1 << 4` | marks a settle in progress, so the settled work runs | cleared after the settle |

Settle points: `handlePaint` (before drawing; the first paint after the widget becomes visible),
`handleResize` while visible, `getCaret()` (caret only), and `doLineUp`/`doLineDown`/`doPageUp`/`doPageDown`
(caret only, because they read `columnX`). The link-cursor hit test (`doMouseLinkCursor`) answers "no
offset" while hidden: the pointer cannot be over a widget that is not visible.

## 4. What stays eager, and why

| Work | Reason |
|---|---|
| Caller queries (`getLocationAtOffset`, `getOffsetAtPoint`, `getLinePixel`, `getTextBounds`, ...) | asked by the caller; they measure the lines they need, as before |
| Scroll requests (`showSelection`, `setSelection(int,int)`, `setSelection(Point)`, `showCaret`) | the scroll position is caller-visible state; they lay out the one or two lines they reveal |
| Scroll bar values (`setScrollBars`) | O(1) from the cached extents; caller-visible through `getVerticalBar()`/`getHorizontalBar()` |
| Caret placement of a caller-installed caret | the caller holds the `Caret` reference and may read its location directly |
| `SWT.SINGLE` alignment margin | geometry state of the single line |

The default caret is reachable only through `getCaret()`. `StyledText` now overrides
`Canvas.getCaret()` (same signature) to settle a deferred placement before returning, so
`getCaret().getLocation()` is exact while hidden. Internal code uses `super.getCaret()`.

## 5. Paint honours the clip rectangle

`handlePaint` already draws only the lines from `event.y` to `event.y + event.height`. The block
selection overlay is now drawn only when it intersects the event rectangle, and each margin strip
only where it meets it.

## 6. Proof (`Test_org_eclipse_swt_custom_StyledText_VisibilityGate`)

* Hidden `CTabFolder` tab: `setText` followed by edits at the start, middle and end, `append`,
  `setStyleRange`, `setSelectionRange`, `insert` and `setCaretOffset`, then a drain of the event loop.
  The renderer's `layoutRequests` counter does not move and no idle pass is queued. Selecting the tab
  settles once; the catch-up measures the visible window only (viewport mode) or the small document
  once. State (caret, selection, top index and pixel, horizontal pixel, caret location, scroll bars)
  and client-area pixels (`Control.print`, SHA-256) equal a widget that was visible throughout.
  Cases: fixed and wrapped, the large document (`-Dswt.styledtext.visibilityGate.mib`, default 4 MiB,
  100 MiB for the proof) and a 2,000-line document (non-viewport path).
* Minimized shell: the same sequence while the shell is minimized, then restore.

Results: section 7, `.m3/styledtext-visibility-gate-20261013/evidence/`.

## 7. Results

Filled from the evidence files (see the README in the evidence folder).

## 8. Known limits

* `columnX`, the pixel column that line and page navigation keep, is resolved when a deferred caret
  placement settles. A visible widget keeps the column from the caret's last placement across a style
  change on the caret's line (`setStyleRanges` saves and restores it); a widget that was hidden through
  that style change resolves the column with the new style. The two can differ by the width change of
  the restyled text before the caret, and only for the next vertical move.

* The horizontal extent in viewport mode is the widest line measured so far (leaf 2). A widget that
  was hidden measures only the lines visible when it settles; a widget that was visible throughout
  may also have measured lines that are no longer in view. The extents can differ when an
  intermediate state showed a line wider than any line in the final view.
* GTK and Cocoa: NOT_RUN (Windows host). The gate is in the common custom-widget code.
