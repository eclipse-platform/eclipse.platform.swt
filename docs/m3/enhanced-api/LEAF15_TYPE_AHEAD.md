# Leaf 15: row search (M3TypeAhead)

Map row M3-TYPE-AHEAD, including the Table agent's `indexOf` candidate. Prior art: JIDE `Searchable` (find from the
cursor with wrap-around). Applications search rows by scanning `getItem(i).getText()`, which makes one item object
per row, and in a virtual table one SetData call per row. The Table viewport already answers the native
type-ahead (`LVN_ODFINDITEM`) from its text lane without a facade.

## Surface (package `org.eclipse.swt.widgets`, public, `@since 3.138`)

| method | answer |
|---|---|
| `int indexOf(Table, String text, int from, boolean prefix, boolean wrap)` | the first row at or after `from` whose first-column text starts with (`prefix`) or equals `text`, ignoring case; with `wrap` the search continues from row 0; -1 when none |
| `int indexOf(List, String text, int from, boolean prefix, boolean wrap)` | the same over the list's items |
| `long indexOf(Tree, String text, long from, boolean prefix, boolean wrap)` | the same over the visible rows (`M3RowMath` numbering) |

## Contract

- The answer equals a linear scan over the public API (`getItem(i).getText()`, ignoring case with
  `String.regionMatches(true, ...)` / `equalsIgnoreCase`). This is the rule of the native list view search.
- `from` is clamped to `[0, count]`. An empty `text` with `prefix` matches the row at `from`.
- With the Table viewport no `TableItem` is made: the search reads the text lane, the same code that answers
  `LVN_ODFINDITEM`, which now delegates to it. A List never makes item objects.
- Without the viewport the Table is scanned through `getItem`, so a virtual table fills the rows it passes. A Tree
  reads the text of its visible rows.

## DOOP and seam

- `TableViewport.indexOf(String, int, boolean, boolean)` is extracted from `findItem` (behavior unchanged).
- `Table.m3IndexOf(...)` answers -2 without a viewport; gtk and cocoa have stubs that answer -2.

## Algorithm choices

A linear scan from the cursor with wrap-around, the only choice for an arbitrary prefix over unsorted rows. A
sorted-key index would pay O(n log n) per edit for a search the user types a few characters at a time. No catalogue
row.
