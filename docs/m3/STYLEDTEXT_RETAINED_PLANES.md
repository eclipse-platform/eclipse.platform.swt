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

Filled from the CPU inventory and the frame bench (section 6).

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

## 6. Results

Pending.

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

Part B (next branch): per-line state as an int mask per line with sparse attribute runs and a
windowed size store (bytes per line at 10M lines), resize behaviour (re-wrap of the visible window
only, top-line anchor, scroll bar hysteresis, resize sweep metrics), wheel and thumb coalescing and a
scaled scroll bar range beyond the int pixel range, a keyed layout cache and interned style resources.
