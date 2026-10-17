# StyledText retained planes, packed state and flicker-free scrolling (leaf 3, 2026-10-13)

Branch `m3/styledtext-retained-planes-20261013`, stacked on the visibility gate
(`m3/styledtext-visibility-gate-20261013`, SWT #120). Evidence: `.m3/styledtext-retained-planes-20261013/`.

## 1. Laws (user, relayed)

| Law | Consequence in StyledText |
|---|---|
| Retained planes: selection, caret and block selection are overlays; no relayout when they change | a selection or caret change repaints the affected rows from cached line layouts; the frame bench counts layouts built per frame |
| Retained per-line `TextLayout` cache bounded to visible plus overscan, keyed by what shapes the line | cache hit rate measured per scenario |
| Interned per-document resources, no per-paint allocation | Java bytes allocated per frame measured per scenario |
| Scroll as a translate | `scroll()` copy area plus repaint of the exposed strip only |
| "There are other ways to make the widget not flicker; scrollbars: manage or customize them for viewport peak performance" | paint only the damage rectangle; batch internal updates and invalidate once; coalesce wheel and thumb events into one scroll per frame; `setValues` only when a value changes; beyond the int pixel range, a scaled scroll bar range with the thumb anchored to a logical line. Flicker count measured in resize and scroll storms |
| "Every state captured using long or integer bit mask and dynamic array" | per-line state in parent-owned dynamic arrays indexed by line (doubling growth with a cap, reused): an `int` mask per line, `long[]` bitsets for range operations, parallel `int[]` ids; default lines cost only their bits; no per-line objects |
| "Selection state" | selection, block selection and multi-selection packed in ints: a sorted `int[]` of offset pairs plus mode bits; block selection as column and line int pairs; arrays and `Point`s created only when an API asks |
| "All the states and UI states that allow reproduction when the viewport scrolls" | every line and render state reproducible from the packed arrays plus the content after its cached layout is evicted; round-trip test |
| Do not touch `org/eclipse/swt/graphics/*`; planes stay GC calls | another agent owns the GPU half |

## 2. Inventory before this leaf (gate head `b07f472fb7`)

| Site | Cost before |
|---|---|
| `scrollVertical` | `super.redraw()` of the whole client area on every scroll (horizontal scroll already copied) |
| `setCaretLocations(Point[], int)` | `super.redraw()` of the whole widget on every caret placement, so caret moves and selection drags repainted everything |
| `setScrollBar` | `setMaximum`, `setThumb`, `setPageIncrement` (three native updates) on every call, changed or not |
| `StyledTextRenderer.textChanging` | tail memmove of the size array on every edit; editing the widest line walked every line with `getLineSize`, allocating one `LineSizeInfo` per line (766 of about 1,000 JFR samples of a 10M-line typing run) |
| `Point[] selection` | one `Point` object per range; `drawLines` copied the ranges (`getSelectionRanges`) per paint and built a `Point` list per line |
| `drawLineBackground` | `getClientArea()` (a `Rectangle` and a native call) per painted line |

## 3. Design

Filled per step as each lands (section 7 lists the steps and their commits).

## 4. Measurements

`bench/StyledTextFrameBench.java`: a 900 x 700 shell, documents of 100 MiB (`mib100`, about 1.86 million
lines) and 10,000,000 short lines (`lines10m`). Scenarios: scroll (3 lines per frame), selection drag
(selection end grows), caret (column and line moves), typing (one character per frame), scroll storm
and resize storm. A frame is the action plus `update()` plus draining the event queue. Columns: frame
p50/p95/max in microseconds, layouts requested and built per frame (`layoutRequests`,
`layoutBuilds`), Java bytes allocated per frame (`ThreadMXBean.getThreadAllocatedBytes`), layout cache
hit rate, paint events and painted pixels per frame, and full-client repaints (the flicker count: a
paint that covers the whole client area while a strip or nothing changed).

## 5. Memory

Bytes per line at 10M lines and bytes for 100K multi-selection ranges: section 6.

## 6. Results (part A; Win32, JDK 21.0.9; `evidence/frame-bench.tsv`)

Structural metrics are deterministic; frame times on this shared, busy host vary run to run by up to
3x and are reported but not claimed as improvements.

| Document, scenario | Full repaints (200 frames) before -> after | Painted px per frame | Layouts requested per frame | Bytes allocated per frame | p50 / p95 ms before -> after |
|---|---|---|---|---|---|
| 100 MiB, scroll | 200 -> 0 | 558,348 -> 39,015 | 47 -> 8 | 61,085 -> 24,390 | 29.1 / 48.0 -> 25.4 / 43.3 |
| 100 MiB, selection drag | 200 -> 0 | 558,348 -> 168,637 | 46.0 -> 16.0 | 33,064 -> 17,530 | 13.2 / 22.5 -> 11.5 / 22.1 |
| 100 MiB, caret | 200 -> 0 | 558,348 -> 0 | 46.0 -> 3.0 | 30,097 -> 1,558 | 11.7 / 30.9 -> 2.8 / 5.1 |
| 100 MiB, typing | 0 -> 0 | 13,005 -> 13,005 | 2 -> 2 | 11,694 -> 11,543 | 44.3 / 70.4 -> 11.6 / 21.8 (step 1 run); 31.0 / 48.6 (final run) |
| 100 MiB, scroll storm | 200 -> 0 | 558,348 -> 19,507 | 44 -> 3.5 | 41,816 -> 18,131 | 34.2 / 54.6 -> 43.8 / 60.4 |
| 10M lines, scroll | 200 -> 0 | 558,348 -> 39,015 | 47 -> 8 | 55,876 -> 22,883 | 14.5 / 41.6 -> 35.4 / 50.8 |
| 10M lines, selection drag | 200 -> 0 | 558,348 -> 414,273 | 46.0 -> 34.9 | 35,326 -> 32,952 | 18.6 / 30.8 -> 9.1 / 19.7 |
| 10M lines, caret | 200 -> 0 | 558,348 -> 1,040 | 45.9 -> 3.0 | 28,815 -> 2,029 | 22.4 / 59.5 -> 1.9 / 28.9 |
| 10M lines, typing | 0 -> 0 | 0 -> 0 | 0 -> 0 | 5,363 -> 4,468 | 42.8 / 72.7 -> 12.8 / 24.9 (step 1 run); 21.8 / 34.4 (final run) |
| 10M lines, scroll storm | 200 -> 0 | 558,348 -> 19,507 | 44 -> 2.5 | 39,008 -> 15,416 | 51.2 / 77.5 -> 41.7 / 63.2 |

Resize storms are unchanged (6 full repaints in 60 resizes: a resize exposes new area).

### Regression

StyledText suite in one JVM (existing classes, PagedContent, SingleControl, ApiDifferential, VisibilityGate,
VisibilityDifferential, RoundTrip): 713 tests, 708 passed, 1 assumption abort, 4 failed:
`test_backspaceAndDelete` (the `Display.post` flake, also on the base) and 3 pixel-only paged-vs-gap
mismatches in `ApiDifferential`, a different operation each run (also seen on the gate and leaf 2 classes).
The dumped images of one (`removeLineBackgroundListener`) differ only in whether the line-background
listener's colour is present on the "5 " lines in one of the two widgets: a harness timing flake, the same
code renders both. `Test_org_eclipse_swt_custom_StyledText_RoundTrip` (fixed and wrapped) and the
hidden-vs-visible differential (185) passed.

### Selection memory (100K multi-selection ranges, 64-bit JVM, compressed references)

| Store | Before | After |
|---|---|---|
| ranges | `Point[]`: 16 + 100,000 x 4 = 400,016 bytes, plus 100,000 `Point` objects x 24 bytes = 2,400,000 bytes; 2,800,016 bytes (28 bytes per range) | `int[]` pairs: 16 + 200,000 x 4 = 800,016 bytes (8 bytes per range) |
| anchors, caret offsets | `int[]` each: 400,016 bytes | unchanged |
| per-range carets | one `Caret` per range (`carets[]`) | unchanged in part A; part B paints extra carets from ints |

### Inset report (the window in the user's screenshot)

The window was `StyledTextFlickerBench` (this leaf's bench): a `Shell` with `FillLayout` holding one
`StyledText(MULTI | V_SCROLL | H_SCROLL | BORDER)`; margins 9, 7, 11, 5 points in magenta on purpose,
200,000 lines. Control tree (`evidence/control-tree.txt`): `Shell` -> `StyledText` (V_SCROLL H_SCROLL
BORDER), no wrapper, no child controls; the StyledText owns its scroll bars.

`StyledTextBoxModelProbe` (`evidence/boxmodel.tsv`, `evidence/png/`) captures the same widget with
`Control.print` at 100 %, 125 %, 150 % and 200 % and scans the middle row and column in device pixels
(lead = border, ring widths, tail = scroll bar track plus border):

| Zoom | Row: border, left ring, right ring, bar + border | Column: border, top ring, bottom ring, bar + border | Capture hash (this branch = `gap` content = base SWT) |
|---|---|---|---|
| 100 % | 2, 9, 11, 19 | 2, 7, 5, 19 | `25c7d89d50d55eb5` |
| 125 % | 2, 11, 14, 19 | 2, 9, 6, 19 | `4fb1c9ff16ea828c` |
| 150 % | 2, 14, 17, 19 | 2, 11, 7, 19 | `61481d04309f69f4` |
| 200 % | 2, 18, 21, 19 | 2, 14, 9, 19 | `c7971b552e78209d` |

* The ring is flush with the border and with the scroll bar tracks at every zoom (no gap): the
  client area ends where the native bars begin, and the ring is painted inside the client area.
* The captures are byte-identical for this branch, the `gap` content and the unmodified SWT
  StyledText from the SWT 3.135 jar at every zoom: nothing in the screenshot is a regression of ours.
* Item 1 (thin white vertical track, tiny thumb): the Windows 11 native scroll bar in the non-client
  area; 200,000 lines make the thumb its minimum size.
* Item 2 (horizontal bar spanning beyond the ring): Win32 places both bars outside the client
  rectangle; the bar runs under the vertical bar's column by design of the native layout.
* Item 3 (last line under the bottom band): the bottom band is painted after the lines and covers the
  partial line exactly at the text-viewport edge; band pixels are all margin colour in every capture.
* Item 4 (left band narrower): the bench configures 9 / 7 / 11 / 5 points. At fractional zoom the band
  widths follow the GC point-to-pixel rounding of each rectangle (125 %: 11.25 -> 11, 8.75 -> 9,
  13.75 -> 14, 6.25 -> 6). At 200 % the right and bottom bands measure 21 and 9 instead of 22 and 10:
  the client area in device pixels is odd, `getClientArea()` reports it in rounded points, and the
  margin rectangles are placed from that rounded size. Owner: `Scrollable.getClientArea` and the
  point-to-pixel conversion in GC (the visibility agent's box model and the graphics owner), not
  StyledText.
* On-screen captures (`GC(display).copyArea`, shell `ON_TOP` and active) on this shared desktop are
  not reproducible run to run (other windows and composition timing): the same build gave 1 and 94
  margin frames in two runs of the same storm. `evidence/flicker-screen.tsv` is kept for a quiet
  desktop; the print-based numbers above are the evidence.

## 7. Steps (part A, this branch)

| Step | Change | Why |
|---|---|---|
| A1 | `blitVertical`: vertical scroll copies the text area (client minus margins) with `scroll()`; the copy invalidates only the exposed strip; margins are never shifted or repainted; a second scroll before the next paint invalidates once instead of copying again (`paintBits & PAINT_BLITTED`) | scroll as a translate; one copy per frame |
| A2 | `exactDeviceOffset(pixels)`: copy only when the offset is a whole number of device pixels at the device zoom and the monitor zoom (vertical and horizontal); otherwise repaint the text area | a copied band and a repainted band must round the same way and meet exactly at 125 % and 150 % |
| A3 | `setScrollBar`: one `setValues`, only when maximum, thumb or page change; `setSelection` only when the value changes | scroll bars repaint only on a real change |
| A4 | `setCaretLocations` no longer redraws the whole widget: only the cells of painted extra carets (`extraCaretCells`, packed ints); the first caret is the platform caret | caret moves and selection drags were full repaints |
| A5 | typing: an edit that keeps the line count skips the tail memmove of the size array; editing the widest line no longer allocates a size object for every line (viewport mode rebuilds the extent from lines measured next) | 44 ms per keystroke at 10M lines, 766 of 1,000 profile samples in `getLineSize` |
| A6 | selection packed: `int[] selection` = sorted `{start0, end0, start1, end1, ...}` replaces `Point[]`; helpers `hasNonEmptyRange()`, `selectionEdges(edge)`, `selectionPoints()` (local, never stored); `getSelection`, `getSelectionRange(s)`, `getSelectionText` build arrays and Points only on call | selection law |
| A7 | paint: the selection plane reads the packed pairs directly (no `getSelectionRanges()` copy per paint, no `Point` list per line); line backgrounds use the client-area ints (no `getClientArea()` per line) | no per-paint allocation in the selection plane |

Part B: branch `m3/styledtext-retained-planes-b-20261013`, stacked on this one (section 8).

## 8. Part B design (branch `m3/styledtext-retained-planes-b-20261013`)

Written before the code (D2C); section 9 lists each step and its commit.

### 8.1 Per-line state: masks, runs, patterns, ids (`StyledTextLineStore`)

One store per renderer (parent-owned), no per-line objects. It replaces `LineSizeInfo[] lineSizes`
(one reference per line plus one object per measured line) and `LineInfo[] lines` (one reference plus
one 56-byte object per line with any attribute).

| Part | Representation | Cost |
|---|---|---|
| attribute mask | `int[] mask`, one int per line, allocated on the first dense write; bits `BACKGROUND`, `ALIGNMENT`, `INDENT`, `JUSTIFY`, `SEGMENTS`, `TABSTOPS`, `WRAP_INDENT`, `SEGMENT_CHARS`, `VERTICAL_INDENT` (the old `LineInfo.flags` values) plus `JUSTIFY_ON` (the justify value) | 0 bytes per line until a dense write, then 4 |
| attribute values | parallel `int[]` per attribute kind, allocated on the first dense write of that kind: background colour id, alignment, indent, wrap indent, vertical indent, segments id, segment chars id, tab stops id | 4 bytes per line per kind in use |
| runs and patterns | packed `int[]`, 5 ints per entry: `(start, end, stride, mask, value)`; a run is a pattern with stride 1; the newest entry wins; at most 64 entries, the oldest is materialized into the dense arrays when full | a uniform `setLineBackground(0, 10M, c)` costs 20 bytes, not 10M ints |
| stride detection | the last single-line write is kept as `(line, bit, value)`; a second single-line write of the same bit and value at a later line becomes a pattern with that stride, and later writes on the stride extend it (zebra striping is one pattern) | 0 |
| objects | identity-interned tables (`Color`, `int[]`, `char[]`) addressed by int id, id 0 = null; colour entries also keep the packed RGBA | one entry per distinct object |
| sizes | `int[] width`, `int[] height` over a window `[sizeBase, sizeBase + capacity)`, -1 = unmeasured; the window is the whole document in exact mode and the visible lines plus overscan in viewport mode (re-centred when the top line leaves the middle half) | 8 bytes per line in exact mode (at most 65,536 lines); constant in viewport mode |
| transient bits | `WRAPPED`, `DIRTY_LAYOUT`, `DIRTY_PAINT`, `HAS_STYLE` in a window mask beside the sizes | constant |

Text edits keep the `DefaultContent` semantics of the old arrays: lines above the edit are unchanged,
removed lines drop their state, lines after the edit move by the line delta, inserted lines have none;
a pattern that spans the edit splits into a part above and a phase-preserving part below. Replacing
all text clears the store. `copyInto` (printing) copies the arrays.

`StyledTextLineStore.footprintBytes()` reports the store's bytes; `bench/StyledTextLineStoreBench.java`
reports bytes per line at 10M lines for: no attributes, one uniform background, zebra backgrounds and
per-line backgrounds of distinct colours (store bytes, and heap delta after GC).

### 8.2 Client area in ints

`getClientArea()` settles the parent's deferred window positions (`Control.forceResize`), so an edit
that read it forced a deferred layout. The client area is read once per `Resize` into
`clientAreaWidth` / `clientAreaHeight`; every other path (idle window, bullet redraw, viewport height)
reads the ints, so hidden edits read no client area. When the visibility agent's `defer` signature
arrives, hidden edits route through it; until then the int cache is the mechanism.

### 8.3 Keyed layout cache

Replaces the 300-slot `TextLayout[]` that was reallocated on every top-line change. Direct-mapped
slots, capacity = visible lines plus one viewport of overscan above and below, rounded up to a power of
two (minimum 16): `TextLayout[] slotLayout`, `int[] slotLine`, `long[] slotKey`. The key hashes what
shapes the line: the style runs that touch it (interned style ids and offsets, read from the packed
ranges without a copy), wrap width, font id, tab stops, alignment, indent, wrap indent, justify,
orientation and spacing. Text identity is the line index plus the line's size validity: an edit or a
style change resets the line's size, which invalidates the slot as before. An evicted slot reuses its
`TextLayout`; layouts are disposed only when the capacity shrinks, on reset and on dispose. Styles,
colours and fonts are interned as int ids by identity (`styleIds`, the line store's tables).

### 8.4 Scroll input and scroll bars

* Thumb drags (`detail == SWT.DRAG`) are coalesced: each event updates `topIndex` / `topIndexY`
  (ints); the caret placement and the client-area measurement are deferred to the next paint
  (`DEFER_CARET`, `DEFER_LAYOUT` through `deferToFrame`), and the text area is invalidated once (no
  copy). The next paint settles them, so N drag events cost one layout pass and one paint. Wheel steps
  keep the part A blit, one copy per frame.
* Scaled range: the bar works in units of `1 << barShift` pixels, the smallest shift that keeps the
  extent within `1 << 30`. Bar values are the pixel values shifted; a bar position maps back to a
  logical line (`topIndex` = (position << shift) / estimated line height, `topIndexY` = 0), so the
  thumb is anchored to a line for documents beyond the int pixel range.
* 32-bit thumb position: on Win32 `Scrollable.wmScroll` already reads `SIF_TRACKPOS` with
  `GetScrollInfo`, and `ScrollBar.getSelection()` reads `SIF_POS`; StyledText reads the bar's
  selection, never the 16-bit `HIWORD(wParam)`. Test: a drag to a position above 65,535 lands exactly.

### 8.5 Resize

* Word wrap, width change: the size store is reset (window only in viewport mode), the top line is
  the anchor (`topIndex` kept, `topIndexY` clamped), the offset above it is derived from cached
  heights (no layout of the lines above), and the visible window plus overscan is laid out. Exact mode
  keeps its idle pass for the remaining lines, so the API is exact once it completes; viewport mode
  keeps them estimated. `getTopPixel()` settles the exact offset first.
* `computeSize` with a width hint in wrap mode lays out lines only until the height exceeds the
  height hint or the display height, then extrapolates from their average.
* Hysteresis (`alwaysShowScroll == false`): a vertical bar that is visible stays visible while the
  content is taller than the client area minus one line; it is shown when the content is taller than
  the client area.

### 8.6 Box model

`textViewport(int[] box)` is the one private method that maps the shared box model onto StyledText:
outer bounds -> border (native, `SWT.BORDER`) -> scroll bar tracks (native, outside the client area on
Win32, GTK and Cocoa) -> inset (0: StyledText has no inset) -> margins (`leftMargin`, `topMargin`,
`rightMargin`, `bottomMargin`) -> text viewport. It writes `{x, y, width, height}` of the text viewport
in client coordinates from the cached client-area ints. The blit, the exposed strips and the margin
fill read it. The 1 px short right and bottom bands at 200 % (`Scrollable.getClientArea` rounds in
points) belong to the visibility agent's box model and are routed through the coordinator.

### 8.7 Carets from ints

The first caret is the platform caret (`Canvas.setCaret`). Extra carets are `int[] extraCarets`,
5 ints each `(x, y, width, height, flags)`, flags = visible; no `Caret` object per extra caret. Paint
draws them from the ints (the first caret's image when it has one); placement repaints old and new
cells.

### 8.8 Planes, batches and the busy indicator

Plane bits: `PLANE_CHROME` (margins, border, chrome), `PLANE_TEXT` (the text viewport),
`PLANE_BARS` (scroll bars). `freeze(planes)` / `thaw(planes)` nest per plane; a frozen plane
accumulates its damage as a packed bounding rectangle and paints it once on thaw. The control is never
`setRedraw(false)`-ed as a whole, and a frozen plane does not stop the others.

| Operation | Frozen | Live |
|---|---|---|
| bulk `setText` (text longer than one screen), `setStyleRanges` / `replaceStyleRanges` over more than a screen of lines | text | chrome, bars |
| margins, font, tabs | chrome | text (keeps its content until the re-layout publishes), bars |
| small edits (typing, a style range inside the screen) | none | precise damage, one paint per frame, never the stock full redraw |

Long operations (bulk `setText` of at least `BUSY_CHARS` characters into `PagedContent`, the default
content; `setStyleRanges` of at least `BUSY_STYLES` ranges): the pure CPU part (the `PagedContent`
piece tree; the packing of the style runs) runs on a worker while the UI thread waits in
`BusyIndicator.showWhile(Future)`: busy cursor, scroll bars live, text plane frozen. The widget state
is unchanged until the worker finishes, so events dispatched during the wait see a consistent widget;
if the content changed during the wait the result is discarded and the work is redone synchronously.
API results when the call returns are identical. A full re-wrap after a font change is window-only
(8.5) and needs no wait.

The shared batch API with plane masks belongs to the visibility agent; when it lands, `freeze` and
`thaw` delegate to it (one switch point).

### 8.9 Paint allocates no objects

Paint-path inventory and replacement: `LineDrawInfo` record per painted line -> renderer int fields;
`isVariableHeight` (a `StyleRange[]` and a line `String` per height query) -> an int scan of the
packed ranges; `caret.getBounds()` three times per extra caret -> the caret ints; the block selection
`Rectangle` -> ints. `StyleRange`, `Point` and `Rectangle` are created only by API calls.
