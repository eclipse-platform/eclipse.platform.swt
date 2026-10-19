# Leaf 1: M3ItemRange, M3ItemSelection, M3Mode

Map rows M3-RANGE-SELECT, M3-VISIBLE-RANGE, M3-MODE-QUERY (`docs/m3/ENHANCED_API_MAP.tsv`); donor ledger
`FOSS_REUSE_DECISION.tsv` (capabilities `range-selection-api`, `visible-range-api`).

## Surface (package `org.eclipse.swt.widgets`, all public, `@since 3.138`)

| type | members |
|---|---|
| `M3ItemRange` | value `[start, end]`; `EMPTY`; `of`, `start`, `end`, `size`, `isEmpty`, `contains`, `intersection`, `countOf(M3ItemRange[])`, `coalesce(int[])` (normal form) |
| `M3ItemSelection` | Table and List: `getSelectionRanges`, `setSelectionRanges`, `select`, `deselect`, `getVisibleRange`; Tree: the same four selection methods per parent item (`null` = roots) over child indices, `getVisibleRange` over visible-row indices, `getVisibleItems` |
| `M3Mode` | `of(Widget)` -> `VIEWPORT|ORIGINAL|CUSTOM|M3|GPU`, `name(int)` |

Package-private helper `M3TreeWalk`: visible-order cursor over a Tree with two depth lanes (parent facade, next
child index), no allocation per row.

## Contract

- Additive and opt-in; no stock file changed. `setSelectionRanges` replaces the selection and **does not scroll**
  (stock `setSelection` scrolls); no `SWT.Selection` event, like every programmatic selection.
- Normal form of range arrays: sorted by start, disjoint, non-adjacent, non-empty, so `countOf` is the selection
  count. `coalesce` sorts a copy only when the input is unsorted and never modifies its argument.
- Visible range = rows whose rectangles intersect the item area (client area minus the header when shown), a
  partial last row included: `rows = ceil(area / itemHeight)`, `[top, min(count - 1, top + rows - 1)]`; `EMPTY`
  when the area or the widget is empty.
- Tree visible-row index = position in the row order of the items whose ancestors are all expanded (roots first,
  each item followed by its expanded subtree).
- Out-of-range indices are ignored on select/deselect (as `Table.select(int, int)` documents); negative range
  starts, null arguments, foreign parents and disposed widgets throw as SWT does.
- Mode-neutral: computed from public state (`getSelectionIndices`, `getTopIndex`, `getItemHeight`,
  `getClientArea`, `getHeaderHeight`, `getTopItem`, `getExpanded`, `getItem(int)`), hence identical in all five
  modes by construction.

## DOOP

Answers are ints and int ranges; `M3ItemRange` objects exist only in the returned arrays (one per run, never per
item). The Tree walk keeps `TreeItem[] parents` and `int[] next` lanes indexed by depth. `getSelectionRanges(Tree,
parent)` fills one `int[]` of the selection size and coalesces it.

Cost today (public-API path): Table/List O(selection) and O(1); Tree selection O(selection x indexOf); Tree visible
range O(rows above top + window). Next step (map row M3-ROW-MATH): when the viewport is active, `VirtualSelectionModel`
run lists and `VirtualTreeVisibleProjection` rank/select answer the same questions in O(runs) and O(log n); the
JUnit oracle stays the same.

## Tests

`Test_M3ItemRange` (headless, 6 tests) and `Test_M3ItemSelection` (hidden desktop, 19 tests): every answer is
compared with a public-API oracle (`getSelectionIndices`, item `getBounds` against the item area, a recursive
row-order walk, the mode properties). Mode differential = the same class green under `viewport` and `original`.
Results are recorded in `docs/m3/ENHANCED_API.md` (Leaves table) when run.
