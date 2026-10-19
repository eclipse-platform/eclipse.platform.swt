# Leaf 9: prefetch ranges (M3Prefetch)

Map row M3-PREFETCH, including the Table agent's `SWT.PaintRange` candidate. A lazy data source wants to know which
rows the widget is about to show, so it can fetch them in bulk, and which rows it no longer needs, so it can cancel
fetches. Stock SWT only asks per row (`SWT.SetData`, at paint time). The viewports already know the answer: the List
and Tree windows and the Table's native cache hint (`LVN_ODCACHEHINT`), i.e. `M3Residency.residentRange` (leaf 7).

## Surface (package `org.eclipse.swt.widgets`, public, `@since 3.138`)

| member | meaning |
|---|---|
| `M3Prefetch.EVENT` (`0x4D330002`) | event type; `detail` is `PREFETCH` (0) or `CANCEL` (1); `start` and `end` are the first and last row of one range (inclusive; a Tree uses `M3RowMath` visible rows) |
| `addListener` / `removeListener(Table / List / Tree, Listener)` | listeners in the widget's event table |
| `setWindow(Table / List / Tree, int rowsBefore, int rowsAfter)` | widens the announced range around the resident window (default 0, 0) |

## Contract

- When the resident window changes, the listeners receive:
  - `CANCEL` for the rows that left the widened window, as at most two ranges;
  - then `PREFETCH` for the rows that entered it, as at most two ranges.
- Scrolling by k rows announces k rows, not the window. Ranges, never items, are delivered: no facade is made.
- The first announcement after `addListener` is the whole widened window. `removeAll` cancels everything.
- The ranges are clamped to the rows.
- Ranges are row numbers at the time of the event. After an insert or a remove the rows shift, and a source keyed by
  row number should treat a structural change as a reset (re-fetch the announced window).
- The events follow the viewport windows. A Table's window (tables without `SWT.VIRTUAL`; a virtual Table is stock) comes from painting, so a Table that is not shown
  prefetches nothing. In the original mode (no viewport) every row is resident from the start and no event is sent.

## DOOP and seam

- One latch per listened widget, kept as keyed data: the last announced range (two ints) and the two margins.
- The viewports call package-private `M3Prefetch.windowChanged(widget)` where their window moves:
  - `TreeViewport.ensureWindow` after a rebuild;
  - `ListViewport.layout` and `rebuild`;
  - `TableViewport.cacheHint`.
- The call is gated by `hooks(EVENT)`.

## Algorithm choices

See `ALGORITHM_CATALOGUE.tsv`: interval difference of two closed ranges (at most two pieces each way).
