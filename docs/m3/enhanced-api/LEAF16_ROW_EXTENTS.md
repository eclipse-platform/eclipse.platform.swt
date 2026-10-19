# Leaf 16: estimated and measured row extents (M3RowExtents)

Map row M3-ESTIMATED-HEIGHTS. Native Table, List and Tree rows share one height, but owner-drawn and custom-drawn row
widgets need variable rows: Canvas lists, Nebula Grid, the drawn tree modes, wrapped text. React-window,
TanStack Virtual and VirtualFlow solve this with an estimate for unmeasured rows plus a cache of measured ones.

The fork already has that engine, package-private and unused by any widget: `VirtualRowExtents`. It keeps the
measured deltas from the estimate in a Fenwick tree over the measured rows only, with no per-row object. This leaf
publishes it.

## Surface (package `org.eclipse.swt.widgets`, public, `@since 3.138`)

`M3RowExtents(int rows, int estimate)`:

| group | members |
|---|---|
| counts | `rowCount`, `estimate`, `measuredCount` |
| per row | `isMeasured(row)`, `extentOf(row)` |
| edits | `setEstimate`, `setExtent(row, extent)`, `clearExtent(row)`, `clearFrom(row)`, `insert(index, rows)`, `remove(index, rows)`, `reset(rows)` |
| queries | `offsetOf(row)` (row may equal the count: the total), `totalExtent`, `rowAt(offset)`, `visibleRange(offset, viewport) -> M3ItemRange` |
| scrolling | `maxScrollOffset(viewport)`, `scrollOffsetFor(row, align, current, viewport)` with `ALIGN_AUTO` / `START` / `CENTER` / `END` / `SMART`, and `setExtentAnchored(row, extent, scrollOffset, scrollingBackward) -> long` (how far to move the offset so the content in view does not jump) |

## Contract

- `offsetOf(r)` equals the naive prefix sum of the extents, with unmeasured rows at the estimate.
- `rowAt(offsetOf(r)) == r` for rows of positive extent.
- `visibleRange` covers exactly the rows intersecting `[offset, offset + viewport)`.
- A re-measured row above the scroll offset returns its delta, so the anchor row stays at the same pixel. A row below
  returns 0.
- Insert and remove shift the measurements like the rows.
- Errors are SWT errors: ERROR_INVALID_RANGE for rows and ERROR_INVALID_ARGUMENT for negative extents, viewports and
  estimates below 1.

## DOOP

The same lanes as `VirtualRowExtents`, which this class wraps without a copy:
- sorted `int[]` row ids and `int[]` extents of the measured rows;
- a `long[]` Fenwick tree of the deltas.

With m measured rows, offsets and re-measurements cost O(log m).

## Algorithm choices

Already catalogued for VirtualRowExtents (Fenwick over measured deltas). The catalogue row below records the public
exposure.
