# Leaf 2 (queued, not started): no-flicker paint and viewport scrollbars

Stacked on `m3/swt-visibility-gate-20261013`. Status: QUEUED. No code, no
measurements yet. This file records the scope handed over on 2026-10-13 so the
next session starts from it.

## Ownership

- This leaf: win32 `widgets/{Control, Composite, Shell, Scrollable, ScrollBar,
  VirtualScrollMetrics}.java`.
- Not this leaf: `VirtualRowExtents` (peer, SWT #117); `DamageRegion` and
  `DamageRecorder` in `common/org/eclipse/swt/internal` (peer com-synexia-d2).
  Wire them through one private method per call site so the swap is one line.
- Not this leaf: Table, Tree, TabFolder, CTabFolder, VirtualPageSource
  (lazy-data agent); List, Combo, CCombo (List/Combo agent); StyledText*
  (StyledText agent); graphics (GPU agent).

## A. No flicker (measure each, adopt what wins)

1. Skip WM_ERASEBKGND when the control paints opaquely (SWT.NO_BACKGROUND rule).
2. `BeginBufferedPaint`/`EndBufferedPaint` vs SWT.DOUBLE_BUFFERED offscreen image.
3. WS_EX_COMPOSITED for composite trees where it measures better.
4. WM_SETREDRAW (`setRedraw(false)`) around batch updates, one invalidation at the end.
5. Minimal damage rectangle, never the whole widget.
6. SWT.NO_MERGE_PAINTS where it helps.
7. Blit scrolling: ScrollWindowEx plus the exposed strip (RFB CopyRect model).

Proof: frame capture during resize, scroll and update storms; count frames
where background shows; before/after.

## B. Viewport scrollbars

1. Logical extent to native int range with scaling (1G lines, 10G px, no
   overflow); thumb maps to a logical row with scroll anchoring; read the 32-bit
   thumb with GetScrollInfo (WM_VSCROLL SB_THUMBTRACK carries 16 bits).
2. Coalesce wheel/thumb events to one scroll per frame; `setValues` only on
   change.
3. Scrollbar is part of the widget: native by default; optional custom-drawn
   overlay plane (thin, auto-hide, minimap-ready) by style/state bit, NO_FOCUS
   semantics; optional kinetic/smooth scrolling.
4. GWT-safe shared math: no int-overflow reliance, prefer int; long scaling
   isolated in desktop code with a GWT-safe twin.

## C. Damage model (donor: RFB / Eclipse TML VNC viewer, EPL-1.0, WRAP)

- Rectangle lists per frame; CopyRect for scroll; merge two rectangles only
  when area(union) <= k * (area(a) + area(b)); measure k.
- DamageRecorder test hook (peer class): WM_PAINT update region via
  GetUpdateRgn in Control, GC damage rectangles.
- Proof: damage px/frame and rects/frame for scroll, selection change, caret
  blink, typing. Target: selection change repaints old+new bands only; caret
  blink repaints only the caret rectangle.

Recipe-first crate, Synexia PR and SWT fork PR as in leaf 1; the shared
`run-matrix.sh` once it lands.

## D. Pixel-exact edges, trim outside damage (user, 2026-10-13)

1. Every damage, scroll and tile rectangle in device pixels with one rounding
   rule (floor start, ceil end) at every DPIUtil zoom; adjacent rectangles share
   edges exactly. Test 100/125/150/175/200 %.
2. Borders, margins and non-client area are never invalidated by content
   changes; blit scrolling clips to the content rectangle (ScrollWindowEx
   prcClip = client minus margins/borders).
3. Native scrollbar non-client region never repainted during content scroll;
   custom overlay scrollbar composited in the same frame after content, aligned
   to the viewport edge; a value change repaints only the thumb track.
4. Proof: frame capture in scroll/resize storms counting frames where any
   border, margin or scrollbar-edge pixel changes; target 0 at every zoom.

Leaf-1 evidence relevant here: SWT's themed CANVAS border is drawn after
DefWindowProc's classic edge in `Composite.wmNCPaint`; any path that skips that
WM_NCPAINT (ValidateRect, RDW_FRAME races, PrintWindow capture) leaves the
classic 3D edge visible. Observed in stock SWT captures as well.

## E. Scrollable box model (user, 2026-10-13; leaf 2 or 3)

Edges outside in: outer bounds, border (style/theme), scrollbar tracks (native
non-client or custom), inset (padding), margin (content margins, e.g.
StyledText), content viewport. Computed once per change, stored as packed ints
(one per edge per side); computeTrim, getClientArea, getBorderWidth, scrollbar
rects, blit clip and the damage protected set read the same numbers (floor
start, ceil end at every zoom). Scrollbar visibility changes only its edge
(one content-width change with hysteresis, repaint only the exposed strip);
overlay scrollbars change no edge. Border/track/inset/margin rings are the
protected set. Proof: computeTrim/getClientArea/getBorderWidth/ScrollBar bounds
identical to unmodified widgets for BORDER, H_SCROLL, V_SCROLL, NO_SCROLL, flat
at 100/125/150/200 %; zero edge-pixel changes in scroll/resize storms. Publish a
diagram plus table in the design doc.

## F. Damage classes available (peer PR hsoliwal/eclipse.platform.swt#121)

`org.eclipse.swt.internal.ViewportDamageRegion` / `ViewportDamageRecorder`
(branch `m3/viewport-damage-region-20261013`, draft). Region: `add`,
`addPoints(zoom)` (pixelFloor/pixelCeil), `addCopy` (destination only),
`covers`, `isSubsetOf`, `areaWithin`, `intersectionArea`. Recorder: `paint`,
`copy`, `endFrame[(expected)]`, `protectedArea()`, `protectedHitPx`,
`protectedHitFrames`, `damagePx`, `paintedPx`, `damageRects`, `copyRects`,
`isFullRepaint`, `fullRepaints`. Leaf 2 stacks on #121 or calls it through one
private adapter; feed from GetUpdateRgn in WM_PAINT, invalidation, blit scroll
and the box-model rings.

## G. User bug "inset problem" (screenshot images/1.png) and "viewport and widget are one"

- Box order border -> scrollbar tracks -> inset -> margin; the native scrollbar
  non-client area is painted with its track in every frame where exposed; no
  white strip between track and inset/margin ring at any zoom.
- Test: capture the scrollbar column and assert track pixels are painted
  (parity against a reference widget that never went through the gate).
- Scrollbars always belong to the Scrollable that paints the content; a
  test-only debug assertion behind a bit flags a Scrollable whose scrollbars
  belong to an ancestor (wrapper/helper child for scrolling = bug, or NO_FOCUS
  with edges from the same box ints).

## H. Protected set from the box model (peer #121 `addFrame`)

`ViewportDamageRegion.addFrame(x, y, w, h, top, right, bottom, left)` adds the
ring between the outer rect and the rect inset by four edges (four disjoint
strips; 0 edge adds nothing, fits overlay scrollbars). Build
`recorder.protectedArea()` outside-in, each call on the rect inside the previous
ring: (1) outer bounds with border edges, (2) scrollbar track with only its side
edge, (3) inset, (4) margin; feed the packed edge ints directly. Peer check:
100x80 widget, border 2, track 10, inset 3, margin 4 -> ring area 3536 px,
72x62 viewport undamaged.

## I. Paint invariant (user: "the viewport that is visible and damaged is what gets repainted")

painted = (visible region from the leaf-1 bit and ancestor/occlusion clip)
intersect (damaged region from #121) minus protected rings. Measure with the
#121 recorder (`paintedPx`, `visiblePaintedPx`, `damagePx`): overpaint =
painted - (visible & damaged), underpaint = (visible & damaged) - painted
(pixel-diff oracle); both 0 for scroll, selection change, caret blink, typing,
resize, hidden.

## J. Carried after the session restart (coordinator list)

- `M3_BATCH_UPDATES` construction-bit batch scope:
  `try { setRedraw(false); ... } finally { setRedraw(true); }` (leaf 1's gate
  already turns the batch into one layout and one invalidate at
  `setRedraw(true)`; the bit and its scope are leaf-2 work).
- Debounce rules and the 200% `getClientArea` rounding bug: named by the
  coordinator; the detailed messages were not received by this session before
  the restart. Re-request the text before starting.
- Capture modes: `new GC(display).copyArea` over the shell (topmost, active)
  for real-screen frames, `control.print(gc)` for content-only parity. Leaf-1
  finding: `Control.print` (PrintWindow PW_RENDERFULLCONTENT) intermittently
  returned black client areas and classic borders on stock SWT too; screen
  capture with foreign-window obstruction detection was used instead.
