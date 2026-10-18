# Leaf 2: M3ScrollInfo, M3Scrolling

Map rows M3-SCROLL-INFO, M3-SCROLL-TO-ALIGNED (`docs/m3/ENHANCED_API_MAP.tsv`); donor ledger
`FOSS_REUSE_DECISION.tsv` (capability `scroll-info-api`: first-party `VirtualScrollMetrics`; Flowless
`SizeTracker`/`TargetPosition` and the Draw2D reveal clamp as patterns; OpenJFX `VirtualFlow` pattern only).

## Surface (package `org.eclipse.swt.widgets`, public, `@since 3.138`)

| type | members |
|---|---|
| `M3ScrollInfo` | `topIndex`, `pixelOffset`, `rowExtent`, `viewportExtent`, `rowCount`; derived `visibleRows`, `fullRows`, `contentExtent` (long), `pixelPosition` (long), `maxTopIndex` |
| `M3Scrolling` | `getScrollInfo(Table|List|Tree)`; `scrollTo(Table|List, index, alignment) -> int top`; `scrollTo(Tree, TreeItem, alignment) -> TreeItem top`; alignments `TOP`, `CENTER`, `BOTTOM`, `MIN_DISTANCE` |

## Contract

- Additive and opt-in; no stock file changed; identical in all modes (computed from `getTopIndex`,
  `getItemHeight`, `getClientArea`, `getHeaderHeight`, `getItemCount`, `getTopItem`, `getExpanded`).
- `pixelPosition = topIndex * rowExtent + pixelOffset`; native modes align rows, so `pixelOffset` is 0 (drawn
  modes may report pixels). `contentExtent = rowCount * rowExtent` (exact, not estimated: SWT rows are uniform).
- `fullRows = viewportExtent / rowExtent`; `maxTopIndex = max(0, rowCount - max(1, fullRows))`: the oracle is the
  clamping of `setTopIndex(rowCount - 1)`.
- `scrollTo` target top: `TOP: row`; `BOTTOM: row - full + 1`; `CENTER: row - (full - 1) / 2`;
  `MIN_DISTANCE: row < top ? row : row > top + full - 1 ? row - full + 1 : top` (Draw2D `reveal` per axis,
  Flowless `MinDistanceTo`); then clamp to `[0, maxTopIndex]`; the row index is clamped to the rows; an empty
  widget is a no-op returning 0. The widget's own `setTopIndex`/`setTopItem` performs the scroll, so events and
  repaint timing are the stock ones.
- Tree: rows are visible rows; `scrollTo(Tree, item)` expands collapsed ancestors first with `setExpanded(true)`
  (no Expand event, as for `showItem`), then sets the top item found by walking to the target row.

## DOOP

One `M3ScrollInfo` value per query (five ints), no widget reference. Tree queries use the `M3TreeWalk` depth lanes:
`getScrollInfo(Tree)` walks all visible rows once (O(rows)); `scrollTo(Tree)` walks to the item and to the target
row. Fast path next (M3-ROW-MATH): `VirtualScrollMetrics` and the topology lanes answer in O(1)/O(log n).

## Tests

`Test_M3Scrolling` (hidden desktop, 11 tests): fields against the widget getters; `maxTopIndex` against the
stock clamping; BOTTOM/MIN_DISTANCE against item bounds (the row ends inside the item area, the next one does
not); CENTER within one row of the middle; Tree against the recursive row-order oracle; ancestors expanded with
no events; argument checks. Mode differential = green under `viewport` and `original`.
