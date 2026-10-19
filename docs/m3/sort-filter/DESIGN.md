# M3 column sort and column filter (enhanced SWT API)

Status: leaf 1 (headless core, Table/Tree bindings, header UI, filter popup and filter row). Branch
`m3/sort-filter-20261019`, based on integration round 8 (`e22b93a6b1`). Owner: sort-filter agent.
User directives (2026-10-19): "multi column sorting" and "column filters: D:\TV_5.0".

The API is additive and opt-in, in package `org.eclipse.swt.m3` next to the enhanced-API classes
(`M3SortKeys`, `M3ProxyModel`, `M3Collation`). Nothing changes for an application that does not use it, and no
existing SWT file is modified. Every class here uses public SWT API only.

## 1. Shape: views over views

```
source rows (application lanes)                       0 .. sourceSize-1
   |  M3ColumnFilter   bitmask per criterion, AND/OR fold, tree ancestor closure  -> kept rows
   |  M3ColumnSort     ordered sort keys -> permutation lane (+ inverse lane, + sibling CSR for trees)
   |  M3RowWindow      [first, first + count) of the sorted view (the resident window)
   v
widget: SWT.VIRTUAL Table/Tree; SetData asks only for resident rows (viewport mode materializes the window only)
```

Every stage implements `M3RowView`:

| member | meaning |
|---|---|
| `size()` | rows of this view |
| `map(row)` | view row -> source row |
| `unmap(source)` | source row -> view row, or -1 when the row is not in the view |
| `sourceSize()` | rows of the source |
| `addListener/removeListener(M3RowView.Listener)` | change notifications (`M3RowChange`, one reused instance per view) |

The same names are used by `M3ProxyModel` (enhanced-API leaf 14), so the proxy can implement `M3RowView` when the
branches meet; `M3ColumnSort.compare(int, int)` has the shape of `M3ProxyModel.RowComparator`.

**Shared design with M3JDK collection views** (PROGRAMME_PLAN 00a): `M3ColumnFilter` is the SWT twin of
`M3FlatteningView` used as a filtered view (a mask lane over an indexed sequence, plus the ancestor closure that
keeps a flattened tree consistent), `M3ColumnSort` is a permutation view (an `int[]` order lane and its inverse),
and `M3RowWindow` is `M3WindowView` (a sliding `[first, first + size)` window). All three are arrays + offsets +
masks, no entry objects, and they compose like the M3JDK filtered/sorted wrapper lane. When `m3/collections`
ships those views, these classes become thin SWT adapters over them (rename by recipe, rule 3/14).

### Changes flow down the chain

A stage receives a change from its input and emits a change to its listeners. Kinds (`M3RowChange`):

| kind | payload | filter does | sort does | window does |
|---|---|---|---|---|
| `RESET` | - | rebuild masks | rebuild order | clamp, damage all |
| `INSERTED` | source `start, count`; rows that entered | shift masks, evaluate new rows | shift indices, merge the entered rows | damage from the first moved row |
| `REMOVED` | source `start, count`; rows that left | shift masks | compact, shift indices | damage from the first moved row |
| `ENTERED` / `LEFT` | source rows | (emitted after a criterion change: old XOR new mask) | sorted-batch merge / one-pass compaction | damage range |
| `UPDATED` | source rows whose values changed | re-test those rows (may turn into ENTERED/LEFT) | move each row by binary search | damage range |
| `ORDER` | view rows `first .. last` changed position | - | emitted after a sort-key change | damage range |

Stability is defined against **source order**: rows whose keys are all equal keep ascending source order, in both
directions (the `DefaultRowSorter` rule). Because of that, the sort result is a function of the *set* of rows it
gets, so an `ORDER` change from upstream never forces a re-sort.

## 2. M3ColumnSort (multi-column sort)

- Sort keys: an ordered list of `(column, direction)`; direction `SWT.UP` (ascending) or `SWT.DOWN` (descending),
  the same constants as `Table.setSortDirection`. `toggle(column, accumulate)` is the header click: without
  accumulate the column becomes the only key (cycle UP -> DOWN -> none, NatTable `SortDirectionEnum` pattern);
  with accumulate (Shift+click) the column is appended as the next key, or its direction cycles in place.
- Column keys (`M3ColumnKey`): `ofInts(IntUnaryOperator)`, `ofLongs(IntToLongFunction)`, `ofDoubles(IntToDoubleFunction)`,
  `ofText(IntFunction<String>, Comparator<? super String>)`, `ofRows(comparator of source rows)`. They read the
  application's lanes by source row. No TableItem/TreeItem is ever read or created by the sort.
- **Algorithm** (catalogue rows in `ALGORITHM_CATALOGUE.tsv`, measured in `MEASUREMENTS.md`): every key becomes a
  64-bit unsigned-order radix key per row (ints/longs: sign flip; doubles: IEEE total-order bits; text and
  comparator keys: dense ranks from one stable merge sort of the rows = coordinate compression). Descending is the
  bitwise complement of the radix key. The permutation is then built by **LSD radix sort**, least significant key
  first, one stable counting pass per byte, skipping bytes that are equal for every row (an int key costs 4 passes
  at most, ranks below 2^16 cost 2). Small views (below the measured cut-over) use a stable merge sort of the
  permutation with the key chain comparator.
- Incremental: `UPDATED` moves a row by binary search plus one `System.arraycopy`; `ENTERED` sorts the batch and
  merges it with the order lane in one pass; `LEFT` and `REMOVED` compact in one pass. The inverse lane is
  rewritten only over the moved span.
- Trees: with `setParents(IntUnaryOperator)` the sorted order is bucketed by parent with one stable counting pass,
  giving a CSR sibling index (`childCount(parent)`, `child(parent, i)`, `indexOfChild(source)`), so siblings are
  sorted inside each parent and the global sort is reused for every parent.

### Header UI

`M3ColumnSort.install(Table|Tree)` listens to the column `Selection` events. Shift+click adds a secondary key.
The primary key keeps the native sort indicator (`setSortColumn`/`setSortDirection`); when two or more keys are
active every sorted column also shows a header image with the direction arrow and its priority number (1, 2, 3 ...).
The image is computed as `ImageData` from a 3x5 digit bitmap and a triangle (no font, deterministic pixels, scaled
per zoom through `ImageDataProvider`), coloured from the control foreground (theme seam, rule 16). The column's own
image is saved and restored (TV_5.0 `ColumnFilterUI.updateColumnImage` pattern); sort and filter marks share one
decoration (`M3HeaderDecoration`) so they never overwrite each other.

## 3. M3ColumnFilter (per-column filters)

- Criteria (one per column, plus one multi-column criterion):
  - text: `M3TextMatch` modes `CONTAINS`, `STARTS_WITH`, `EXACT`, `WILDCARD` (`*`, `?`, `\` escape; whole text),
    `REGEX` (whole text, `Matcher.matches`, TV_5.0 `AbstractRegexFilter`), flags `NEGATE` ("doesn't match") and
    `IGNORE_CASE`. The pattern text is split at unescaped `,` into tokens; a positive filter keeps a row when any
    token matches, a negated one when no token matches (TV_5.0 `AbstractRegexFilter.select` OR/NOR semantics).
  - multi-property text (TV_5.0 `MultiPropertyTextFilter`): one `M3TextMatch` over a set of columns, a row matches
    when any of those columns matches.
  - value lists: `setValues(column, IntUnaryOperator valueId, int... allowedIds)` (allowed ids as a bitset, O(1)
    per row) or `setValues(column, String...)` over the cell text.
- Combination: criteria fold left in column order (multi-column criterion first) with `AND` (default) or `OR`
  per criterion (TV_5.0 `ElementFilter` logic flags, without the object chain).
- **DOOP**: one `long[]` mask per active criterion (bit = source row), folded into the result mask with word-wide
  `&=`/`|=`; the kept-row lane is the compaction of the set bits (`numberOfTrailingZeros` walk) and `unmap` is a rank
  query: per-word prefix popcounts + `bitCount` of the masked word (rank/select bitvector). A criterion change
  recomputes one mask, refolds, and reports `ENTERED`/`LEFT` as `old ^ new` word by word.
- **Tree ancestor rule** (TV_5.0 `FilteredTreeElementProvider` / `TreeFilterStateProvider`): with
  `setParents(IntUnaryOperator)`, every ancestor of a matching row is kept so the match stays reachable. Closure:
  for each matching row walk up until an already-kept ancestor (each row is marked once: O(n) amortized).
- Cell text comes from `M3ColumnFilter.CellText` (`text(sourceRow, column)`), the same function the application
  uses to fill items; the filter never reads widget items.

### Filter UI (adapted from TV_5.0, user-authored)

- `M3FilterPopup`: right-click (MenuDetect) on a column header opens a tool shell under the column with the
  edit panel; it closes on Escape/Return, focus loss, deactivation or a click outside (TV_5.0 `PopUpFilterUI`,
  `ColumnFilterUI`, `ViewerFilterUI`).
- `M3FilterEditPanel`: column combo (first entry: no filter), match-type combo, pattern text with validation
  feedback, and a tool bar (default text, paste from clipboard, close) (TV_5.0 `FilterEditPanel`). Validation
  errors leave the active filter unchanged.
- `M3FilterRow`: a row of `Text` fields above a Table/Tree aligned to the columns (order, widths and horizontal
  scroll), each a `CONTAINS` criterion (NatTable filter row pattern); edits apply after a short debounce
  (TV_5.0 `ViewerFilterUI.DeferredRun`).
- Filtered columns carry a filter mark in their header image (shared `M3HeaderDecoration`).
- Strings come from `SWT.getMessage` keys `SWT_M3_Filter_*` (TV_5.0 `messages.properties` values).

## 4. Binding to virtual widgets

`M3RowBinding.bind(Table, M3RowView, RowFill<TableItem>)` and `bind(Tree, M3ColumnSort, RowFill<TreeItem>)`
(tables must be `SWT.VIRTUAL`): the item count follows the view; on a change only the damaged view range is
cleared (`Table.clear(start, end)`, `TreeItem.clear`) so, in viewport mode, only resident rows are asked again
through `SWT.SetData`. SetData fills an item from `map(index)`; tree items remember their source row in item data.

## 5. Tests and proof

- JUnit: sort stability, multi-key order, directions, toggle/accumulate, incremental updates; filter modes,
  regex/wildcard semantics, value lists, AND/OR folds, tree ancestor rule; view composition and change flow.
- Differential: seeded random data up to 10^6 rows against a straightforward reference (a `List<Integer>` filtered
  with predicates and sorted with `List.sort` and a comparator chain), plus random incremental scripts; timings in
  `MEASUREMENTS.md`.
- UI tests on the hidden desktop: header clicks (Shift), indicator images, popup open/close/apply, filter row,
  virtual Table/Tree binding.
- JaCoCo report in `coverage/`.

## 6. Lanes and facades (rule 18)

| class | lanes | facades |
|---|---|---|
| M3ColumnFilter | `long[]` mask per criterion, result mask, `int[]` kept lane, `int[]` per-word rank | none |
| M3ColumnSort | `int[]` order, `int[]` inverse, `long[]` radix keys (scratch), `int[]` CSR child start/rows | none |
| M3RowWindow | `first`, `count` | none |
| bindings | none | TableItem/TreeItem created by SWT for resident rows only |
