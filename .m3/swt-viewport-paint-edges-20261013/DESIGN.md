# Leaf 2: planes, box model, damage, debounce (win32), 2026-10-13

Stacked on leaf 1 (#124) and on the peer damage region (#121). Consumers (StyledText, Table/Tree, List/Combo, Nebula Grid) use `org.eclipse.swt.internal.VisibilityGate`; GTK and Cocoa get no-op ports (NOT_RUN).

## 1. One box model per widget

```
+--------------------------- outer bounds (GetWindowRect) -------+
| BORDER (WS_EX_CLIENTEDGE, WS_BORDER, theme)                     |
|  +--------------------------------------------+-------------+  |
|  | INSET (widget padding)                     | TRACK       |  |
|  |  +--------------------------------------+  | (native     |  |
|  |  | MARGIN (content margins)             |  |  vertical   |  |
|  |  |  +--------------------------------+  |  |  scrollbar, |  |
|  |  |  | content viewport (0, 0, w, h)  |  |  |  non-client)|  |
|  |  |  +--------------------------------+  |  |             |  |
|  |  +--------------------------------------+  |             |  |
|  +--------------------------------------------+-------------+  |
|  | TRACK (native horizontal scrollbar, non-client)            |  |
+----------------------------------------------------------------+
```

| Ring (outside in) | Source | Stored as | Read by |
|---|---|---|---|
| BORDER | window rect minus client rect, minus TRACK | computed on demand (one OS reading) | `getBorderWidth`, `computeTrim`, protected set |
| TRACK | `WS_VSCROLL`/`WS_HSCROLL` x system scrollbar metric at the control's DPI; left side when `WS_EX_LEFTSCROLLBAR` xor `WS_EX_LAYOUTRTL` | computed on demand | scrollbar rectangles, protected set |
| INSET | widget padding (owner) | `Scrollable.boxInset`, packed 8 bits per side | content viewport, protected set |
| MARGIN | content margins (StyledText owner) | `Scrollable.boxMargin`, packed 8 bits per side | content viewport, protected set |
| content viewport | client rect minus INSET and MARGIN, exact device pixels | `VisibilityGate.contentPixels` | painting, blit clip, damage |

Rules:
- One reading feeds every consumer, so `computeTrim`, `getClientArea`, `getBorderWidth`, the scrollbar rects and the protected set agree. `Test_org_eclipse_swt_internal_ViewportPlanes` checks this for 6 style sets on Canvas and StyledText.
- An overlay scrollbar has no edge and changes no ring.
- The protected set is the BORDER, TRACK, INSET and MARGIN rings, built outside in with #121 `addFrame` (`VisibilityGate.protectBox`). Content damage never touches it.
- **200% rounding (item B).** Public `getClientArea()` is in points and rounds, so an odd pixel width loses 1 px at 200%. `contentPixels` and `boxEdges` are exact device pixels, so margin rings drawn from them meet the edges exactly. Stock behaviour is unchanged (pre-existing). GC's point-to-pixel rounding (9 pt to 11 px at 125%) belongs to the graphics owner.

## 2. Planes: freeze only what the operation changes

| Plane bit | Frozen by | While frozen | At the last `endBatch` |
|---|---|---|---|
| `PLANE_MAIN` (structure, chrome, layout, children) | relayout, column/header change, child add/remove, reconfiguration | Composite layout deferred (`setLayoutDeferred`); the viewport keeps scrolling and painting | one layout |
| `PLANE_VIEWPORT` (content) | setItems, sort, filter, setText, setStyleRanges | WM_PAINT records its update region and paints nothing (BeginPaint/EndPaint with the erase answered as done, so WM_NCPAINT and the native scrollbars stay live); `redraw` records its rectangle | the accumulated damage is invalidated once, no erase, no frame |
| `PLANE_SCROLLBARS` | rare: scrollbar reconfiguration storms | `SetScrollInfo` updates values without redraw | one frame repaint (RDW_FRAME, no erase) |

- Depths are packed, 8 bits per plane per control, so nesting is safe.
- No WM_SETREDRAW: it hides the whole window, scrollbars included, and the stock `setRedraw (true)` ends with RDW_ERASE|RDW_FRAME|RDW_ALLCHILDREN.
- **Long operations:** `runBatch (control, planes, cpuWork, uiApply, expectedMs)` runs `cpuWork` on a worker behind `BusyIndicator.showWhile (Future)` when `expectedMs >= BUSY_THRESHOLD_MS` (100, `-Dswt.batch.busyMs`). The busy cursor shows, the event loop and scrollbars stay live, and the frozen planes paint once.
- **Threshold:** small operations add damage directly; a batch is for large ones. `PlanesBench` (changes 1/5/20/100/1000; direct, stock setRedraw, viewport) measures the crossover.

## 3. Debounce (item A)

- Input-driven work (scroll, wheel, drag, typing) is coalesced per frame by its owner and never time-debounced.
- `VisibilityGate.debounce (control, kind, delayMs)` covers bursty non-input work. Each call extends the deadline by `delayMs` (default 16 ms), capped at `DEBOUNCE_MAX_MS` (100) after the burst's first call. When it settles, the control gets one `DEBOUNCED` event with the ORed kinds.
- State is one deadline int, one start int and one kind mask per pending control in reusable Display arrays, with one preallocated timer Runnable. No allocation per call.

## 4. Damage test hook

`VisibilityGate.attachDamageRecorder (control, recorder)` feeds the WM_PAINT update region (GetUpdateRgn before BeginPaint) of the control and its descendants into a #121 `ViewportDamageRecorder`, in content-viewport coordinates. It is off unless attached; the WM_PAINT fast path is one int test. Absorbed (frozen-viewport) paints are not recorded as painted. Blit copies come from `Canvas.scroll`, which this leaf does not own; that owner records them.

## 5. Consumer API (internal)

```java
boolean VisibilityGate.isOffscreen (Control control, boolean paint)
boolean VisibilityGate.defer (Control control, int kinds)        // PAINT | LAYOUT | MEASURE | DATA
int     VisibilityGate.CATCH_UP                                   // event, detail = deferred kinds
boolean VisibilityGate.beginBatch (Control control, int planes)  // PLANE_MAIN | PLANE_VIEWPORT | PLANE_SCROLLBARS
boolean VisibilityGate.endBatch (Control control, int planes)
void    VisibilityGate.runBatch (Control control, int planes, Runnable cpuWork, Runnable uiApply, int expectedMs)
boolean VisibilityGate.debounce (Control control, int kind, int delayMs)
int     VisibilityGate.DEBOUNCED                                  // event, detail = ORed kinds
boolean VisibilityGate.boxEdges (Control control, int [] out16)  // [ring * 4 + side], device pixels
boolean VisibilityGate.contentPixels (Control control, int [] xywh)
void    VisibilityGate.setBoxEdges (Control control, int packedInset, int packedMargin)
boolean VisibilityGate.protectBox (Control control, ViewportDamageRegion region, int viewportWidth, int viewportHeight)
void    VisibilityGate.attachDamageRecorder (Control control, ViewportDamageRecorder recorder)
```
