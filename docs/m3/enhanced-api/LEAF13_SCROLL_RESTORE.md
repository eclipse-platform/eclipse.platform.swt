# Leaf 13: scroll memento (M3ScrollMemento)

Map row M3-SCROLL-RESTORE (history.scrollRestoration, VirtualFlow's (index, offset) anchor). After a reload or a
structural change the application wants the user to see the same rows again. Stock SWT offers
`getTopIndex`/`setTopIndex`, but the index goes stale as soon as rows are inserted or removed above it, and a Tree has
no row index at all.

## Surface (package `org.eclipse.swt.widgets`, public, `@since 3.138`)

| method | meaning |
|---|---|
| `long save(Table / List / Tree)` | packs the anchor (the top row; the visible row of `M3RowMath` for a Tree) and the horizontal scroll offset into one long |
| `long remap(long memento, int index, int delta)` | moves the anchor through an edit: `delta > 0` rows inserted at `index`, `delta < 0` rows removed from `index` on |
| `boolean restore(Table / List / Tree, long memento)` | scrolls back to the anchor (clamped to the rows); false, with nothing changed, for a value that is not a memento |

## Contract

- `restore(save())` shows the same top row and horizontal offset.
- With `remap` applied for each edit, the same row is on top after inserts and removals above it.
- A removed anchor yields the row that took its place.
- Only `save` creates mementos. `restore` rejects any other long (a tag byte), including 0 and -1, and changes nothing.
- The answers are the same in every startup mode.

## DOOP

- One `long`: an 8-bit tag, 31 bits of row, and 24 bits of horizontal offset.
- No object and no listener: remapping is arithmetic on the caller's edit counts.

## Algorithm choices

See `ALGORITHM_CATALOGUE.tsv`: bit packing of the anchor and an index shift under insert/remove.
