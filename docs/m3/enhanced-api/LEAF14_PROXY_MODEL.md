# Leaf 14: proxy model (M3ProxyModel)

Map row M3-PROXY-MODEL. Prior art: Qt `QSortFilterProxyModel`, Glazed Lists `FilteredList(SortedList(source))`, and
SwingX `RowFilter`/`RowSorter`.

A virtual widget shows a sorted or filtered view of the application's rows when every SetData request is answered
through a mapping from view rows to source rows. Applications now rebuild arrays of objects for this. The proxy keeps
the mapping as int lanes.

## Surface (package `org.eclipse.swt.m3`, public, `@since 3.138`)

| member | meaning |
|---|---|
| `M3ProxyModel(int sourceSize)` | a model over source rows `0 .. sourceSize-1` with no stage (identity) |
| `addFilter(IntPredicate keep)` | appends a filter stage; `addSort(RowComparator)` and `addSort(M3SortKeys)` append a stable sort stage |
| `clearStages()`, `setSourceSize(int)` | edit the model; nothing is recomputed until `refresh()` |
| `refresh()` | recomputes the stages in order |
| `size()` | the view rows |
| `map(int row) -> sourceRow` | view row to source row |
| `unmap(int sourceRow) -> row` | source row to view row, -1 when filtered out |
| `bind(Table, RowFill<TableItem>)` / `bind(List, IntFunction<String>)` | drives a virtual Table (SetData) or a List (items) from the model; call again after `refresh()` |

## Contract

- `map(unmap(s)) == s` for every kept source row, and `unmap(map(r)) == r` for every view row.
- `size()` equals the number of source rows that pass every filter.
- Sort stages are stable. A chain of stages equals their composition: filter then sort equals the stable sort of the
  filtered rows.
- After `bind`, SetData on view row r fills the item from source row `map(r)`. Items are never asked for rows the
  filters removed.
- The core is headless.

## DOOP

- One `int[]` permutation lane (view to source), one `int[]` inverse lane (source to view or -1), and the stage list.
- Sorting is a merge sort of int lanes with no boxing. Filters write the permutation in place.

## Algorithm choices

See `ALGORITHM_CATALOGUE.tsv`: a permutation lane plus inverse lane against object lists.
