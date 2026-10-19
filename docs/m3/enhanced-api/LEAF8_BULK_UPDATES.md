# Leaf 8: bulk updates (M3BulkUpdates)

Map row M3-BULK-UPDATES (with the Table agent's append-burst candidate).

## What already exists

The viewports already plan their native work instead of mirroring every edit:

- The List rebuilds its native window only when the edited rows leave the window (`ListViewport.ensureWindow`).
- The Tree answers `setItemCount(10^6)` in O(window).
- The Table defers an append burst into one `LVM_SETITEMCOUNT` (`TableViewport.defer` / `flush`).
- Every control can freeze its paint and scrollbar planes with `Control.beginBatch` / `endBatch` (public since 3.137).

What an application lacks is one call that says "these edits are one update": no paint, no scrollbar churn and no
emptiness flicker in between, and exception-safe.

## Surface (package `org.eclipse.swt.widgets`, public, `@since 3.138`)

| member | meaning |
|---|---|
| `M3BulkUpdates.run(Tree / Table / List, Runnable edits)` | runs the edits as one update: the viewport and scrollbar planes are frozen, M3Emptiness is held, and at the end the widget repaints only what changed, once, and announces at most one emptiness crossing |

## Contract

- The resulting items, texts, selection and top index equal those of the same edits without `run`.
- No paint reaches the widget while the edits run, even if the edits pump events. After `run` the damaged area is
  painted once.
- At most one `M3Emptiness` event, carrying the final count.
- Nesting is counted. An exception from the edits leaves no plane frozen and no hold open, and it propagates
  unchanged.
- In viewport mode the native work of a Table append burst is one count change (the `TableViewport` flush), and the
  List and Tree native work stays bounded by the window. In original mode the stock per-item native calls remain; the
  batch only coalesces their painting.

## DOOP

No new state: the plane depths live in the display's batch entry (`ViewportBatch`) and the hold lives in the M3Emptiness
latch. The class is common code over public and package API, so it works on every platform where `beginBatch` exists.

## Algorithm choices

See `ALGORITHM_CATALOGUE.tsv`: scoped deferral (begin/end with a depth counter, try/finally) over per-edit flushing.

## Known issue (outside this leaf)

If the edits dispose the widget, `M3BulkUpdates` cannot end its batch: `endBatch` checks the widget. Win32
`ViewportBatch` has no dispose path, so the display keeps the disposed control's entry and its frozen-plane counters
(`batchViewports`, `batchScrollbars`) until the display itself is released. Behaviour stays correct: other controls
miss the entry lookup and paint normally. The cost is one leaked control reference and an entry lookup per paint.

The fix belongs to the viewport track, which owns `ViewportBatch`: `Control.releaseWidget` should drop the control's
batch entry and decrement the counters of its frozen planes without flushing. `Test_M3BulkUpdates.test_theEditsMayDisposeTheWidget`
exercises this path.
