# Leaf 10: identity diff (M3DiffApply)

Map row M3-DIFF-APPLY (cross-ecosystem: UIKit diffable data source, RecyclerView DiffUtil, Glazed Lists Tree4Deltas).
Applications that reload a model into a List, Table or Tree today call `removeAll` and add everything again. Every
row then loses its item object, selection, check state and (Tree) expansion, and the native control redoes all of
it. With stable row identities the widget can keep every row that is still in place and touch only the rest.

## Surface (package `org.eclipse.swt.widgets`, public, `@since 3.138`)

| method | meaning |
|---|---|
| `int [] apply(List, long[] oldIds, long[] newIds, LongFunction<String> text)` | reorders the list from `oldIds` to `newIds` |
| `int [] apply(Table, long[] oldIds, long[] newIds, ObjLongConsumer<TableItem> fill)` | the same for a table; `fill` fills each new or moved row (for `SWT.VIRTUAL` it may be null: rows fill through SetData) |
| `int [] applyChildren(Tree, TreeItem parent, long[] oldIds, long[] newIds, ObjLongConsumer<TreeItem> fill)` | the same for the children of `parent` (the roots when null) |

- `oldIds` must describe the current rows (length = item count); ids are unique within each array.
- The result is `{kept, removed, inserted}`: rows left in place, rows removed (dropped or moved), and rows inserted
  (new or moved).

## Contract

- After `apply`, the rows are in `newIds` order, and every row whose identity is in both arrays and whose relative order
  is kept is untouched: the same item object, selection, checked state and expansion.
- The kept rows are a longest increasing subsequence of the old positions taken in new order, so the number of
  remove/insert operations, `n_old - kept + n_new - kept`, is minimal for a widget whose only edits are remove and
  insert.
- Removals run in one `remove(int[])` call. Inserts run in ascending new index. The whole edit runs inside
  `M3BulkUpdates` (one repaint, at most one emptiness event).
- 10^5 rows reorder in well under a second; the diff itself is O(n log n).

## DOOP

- The id arrays are the only per-row input: a `long[]` index map, an `int[]` sequence, the LIS predecessor and tail
  lanes, and a kept bitset.
- No item object is made for a kept row.

## Algorithm choices

See `ALGORITHM_CATALOGUE.tsv`: LIS by patience sorting (binary search over tails) against Myers diff and against
remove-all-and-reload.
