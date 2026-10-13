# SWT lazy data, lazy tree children and lazy tab content (2026-10-13)

Status: DESIGN, written before code (D2C). This page is updated in the same
branch as each part lands; the status column of the table below is the record.

Ledger rows (Synexia #10864, `WIDGET_IMPLEMENTATION_LEDGER.tsv`): `lazy_data_source`
(wave 2, score 16), superset gap #6 (tree lazy async children with placeholders),
`tab_folder` lazy content. Donor decisions: `FOSS_REUSE_DECISION.tsv` of the
Synexia crate `swt-lazy-data-20261013` (AG Grid Community infinite row model,
react-window-infinite-loader, TanStack Query infinite queries, AndroidX Paging and
Compose Lazy/Pager: ADAPT, pattern only; Avalonia and WPF: ADAPT for facade release
and lazy tabs, NO_FIT as fetchers; Qt `fetchMore`/`canFetchMore`:
LICENSE_REVIEW_REQUIRED, names only). No donor text is copied.

## Laws this design is held to

1. Nothing is created, fetched or laid out until display or a caller's API needs it.
2. Bits, ints, bitwise masks and reusable arrays; objects only when a caller's API asks.
   `TableItem`/`TreeItem` are thin facades created for visible rows or API calls.
3. Every per-item state is reproducible from the parent's packed state plus the data
   source, so a facade that scrolls out can be released and re-created identically.
4. No work while the widget is not visible: set dirty bits, catch up once.
5. Native first: the win32 `LVS_OWNERDATA` list view and the TreeView text callback
   stay; the Java side answers their callbacks.
6. Public API results do not change; new API is additive and opt-in. With no page
   source and no `SWT.VIRTUAL` on a tab folder, every code path is the old one.
7. Shared (`common/`) logic is GWT-safe: no reliance on `int` overflow, no
   `String[].clone()`, no `Objects.checkIndex`, `int` words over `long` where it
   transpiles.
8. Fetches run on worker threads; results reach the UI thread through one
   `Display.asyncExec` per frame (coalesced); a block that leaves the overscan window
   is cancelled.

## 1. `VirtualPageSource` (new, `common/org/eclipse/swt/widgets`)

Additive public class; the only new public type. It sits behind the existing
`SWT.VIRTUAL` + `SWT.SetData` contract of `Table` and `Tree`, so it also runs on GTK
and Cocoa through public API only (NOT_RUN there).

```java
VirtualPageSource source = new VirtualPageSource (
    /*blockSize*/ 256, /*maxBlocks*/ 64, /*columns*/ 3, VirtualPageSource.NONE,
    request -> { /* worker thread: fill request.setText (row, col, s) */ });
source.attach (table);          // table created with SWT.VIRTUAL, setItemCount (10_000_000)
```

- **Block table** (`VirtualBlockCache`, package-private, pure Java, GWT-safe): a fixed
  number of slots (`maxBlocks`); per slot `int blockId`, `int parentKey`, `int lruStamp`,
  `int state` (EMPTY, PENDING, LOADING, LOADED, FAILED as bits) and one reusable
  `String[]` of `blockSize * columns` cells, plus `int[]` lanes for tree rows (child
  count hint, node key). A lookup is an open-addressed `int` hash of
  `(parentKey, blockId)`; eviction takes the least recently stamped LOADED slot that is
  outside the paint window (AG Grid `maxBlocksInCache`/`lastAccessed`, Paging `maxSize`).
- **Window and priority.** Each frame the source reads the host's visible range
  (`getTopIndex` + client height / item height) and builds the wanted block list:
  the visible blocks first, then overscan blocks ordered by distance (react-window
  `threshold`, Paging `prefetchDistance`). Pending requests outside the window are
  dropped before they start; in-flight ones get their cancel flag set and count as
  cancelled (Paging `jumpThreshold`, TanStack `AbortSignal`).
- **Workers.** `threads` daemon workers (default 2) pull the highest-priority pending
  request under one lock (AG Grid `maxConcurrentDatasourceRequests`). The fetcher fills
  a pooled `Request` (reused array, no per-row objects) and may poll `isCancelled()`.
- **Delivery.** A finished request is put on a completion list; the first completion of
  a frame schedules one `asyncExec`; that runnable drains every completion, stores the
  blocks, and issues one `clear (start, end)` per contiguous visible run so the native
  view asks again. One drain per frame, however many blocks arrived.
- **Placeholder rows.** A row whose block is not loaded is answered blank (or with
  the `PLACEHOLDER` text when the construction flag `PLACEHOLDER_TEXT` is set) and its
  LOADING bit is set; it is re-asked once the block arrives. Once loaded, results are
  identical to a synchronous `SetData` that writes the same strings.
- **Construction bit flags.** `TREE` (rows are tree nodes; children fetched on expand),
  `PLACEHOLDER_TEXT`, `KEEP_FACADES` (never release transient facades), `SYNC_API_READ`
  (an API read of an unloaded row fetches its block on the calling thread).
- **Counters** for proofs: blocks requested, fetched, cancelled, evicted, drains,
  resident blocks, in-flight.

### Host hooks (win32 `Table`/`Tree`, SetData and expand paths only)

`VirtualPageHost` is a package-private interface in `common/`; win32 `Table` and
`Tree` implement it, other platforms do not, and the source falls back to public API.

- `Table.checkData`: with a page source attached the facade handed to the source is not
  pinned (the source keeps no reference), so it stays a *transient* facade.
- `Table` releases transient facades outside `[paintStart, paintEnd)` once per drain
  (never inside an `LVN_GETDISPINFO` or custom-draw callback). A facade is released only
  if it is not PINNED (no API or listener ever saw it) and not DIRTY; its checked and
  grayed bits live in the parent lanes (section 4), so nothing is lost.
- `Tree`: the `TVN_ITEMEXPANDING` path asks the source for children of a node whose
  count is unknown; see section 2.

**ItemResidency adoption point.** A peer agent is building `widgets/ItemResidency`
(thin handle = parent + slot + generation, spill store, compile-time pin metadata).
The adoption point is exactly the pair *`_getItem (index, create)` + the release pass*:
today a transient `TableItem` is created there and released by the pass above; with
ItemResidency the same two calls become `residency.handle (index)` and
`residency.release (range)`, and the facade's getters read the parent lanes and the
block cache instead of item fields. No other call site changes.

## 2. Tree: lazy async children with a mask-bit placeholder (gap #6)

- A row's block carries, per row, a child-count hint: `0` leaf, `n > 0` known,
  `UNKNOWN` (has children, count not fetched). Known counts go straight to
  `TreeItem.setItemCount (n)` (native placeholders, data fetched only for visible rows).
- `UNKNOWN` gives the node one native placeholder child (lParam `-1`, no `TreeItem`).
- On `TVN_ITEMEXPANDING` of such a node the source sets `CHILDREN_LOADING`
  (`VirtualItemState`, already defined) on the node's topology mask and requests block 0
  of its children together with the child count (Paging initial load).
- `TVN_GETDISPINFO` for a native child whose parent has `CHILDREN_LOADING` answers the
  placeholder text into the native buffer and returns: no `TreeItem` is created. The
  placeholder is the parent's mask bit, drawn by the native callback.
- When the count arrives: clear `CHILDREN_LOADING`, set `CHILDREN_KNOWN`,
  `setItemCount (n)`, re-ask row 0. Collapsing a node cancels its pending blocks.
- `expandAll` is never called by the source; nothing forces a subtree open.

## 3. Lazy tab content (`TabFolder`, `CTabFolder`)

Opt-in by the existing `SWT.VIRTUAL` style bit (bit 28 is free in both classes; the
README of #10864 records that). Default behaviour is unchanged.

- With `SWT.VIRTUAL`, the folder sends `SWT.SetData` (`event.item` = the tab item,
  `event.index` = its index) the first time a tab becomes the selection while the
  folder is visible. The listener creates the tab's control and calls
  `item.setControl (c)`; layout happens once, at that `setBounds`.
- A packed `int[]` bitset in the folder records which tabs were filled; it shifts on
  insert and remove.
- Selection while the folder is not visible sets a pending bit; the first `WM_PAINT`
  (TabFolder) or paint event (CTabFolder) catches up once. Until the visibility agent's
  effective-visibility bit lands, one private method `lazyContentShowing ()` answers
  `isVisible ()`; switching it over is a one-line change.
- So 50 tabs with heavy content create exactly one content control at startup.

## 4. Per-item state as parent bit lanes (user laws of 2026-10-13)

Every per-item boolean is a bit lane in the parent; per-item values (text, font,
colour and image ids per cell, measured height, level, data handle) are parent arrays
or come from the data source. Plan and status:

| lane / state | owner today | this branch |
|---|---|---|
| checked, grayed (Table) | `TableItem` fields (dense) / `VirtualItemStorage` mask (virtual) | `VirtualBitLanes` in `Table`, `int[]` words; `TableItem.setChecked/getChecked` read and write the lane; check-all = word fill, count = `Integer.bitCount` |
| checked, grayed (Tree) | `TreeItem` fields / `VirtualTreeTopology` mask | topology mask (unchanged this pass) |
| expanded, has-children, children-known/loading/complete | native `TVIS_EXPANDED` + `VirtualTreeTopology` mask | topology mask bits; `CHILDREN_LOADING` used for the placeholder |
| selected | native + `VirtualSelectionModel` | extend `VirtualSelectionModel` to range pairs + bitset by density (planned leaf) |
| focused, hovered, enabled, filtered-visible, dirty (paint/measure/data) | native / item fields | planned leaf; `DIRTY`/`PAINT_RESIDENT` already in the virtual mask |
| text per cell | `TableItem`/`TreeItem` fields | page-source block cache for source rows; a facade the caller wrote to is PINNED and kept |

A transient facade is released only when all of its state is reproducible (not
PINNED, not DIRTY). The round-trip proof: set states, scroll so the facades are
released, scroll back, require identical API values and an identical pixel hash.
Coverage is checked against the residency agent's `REPRODUCTION_STATE.tsv` once it
is published.

## 5. Proof plan

| proof | harness | status |
|---|---|---|
| API coverage `LAZY_API_COVERAGE.tsv`, differential vs unmodified widgets | JUnit, `SetData` oracle vs page source, after drain | planned |
| 10M-row Table, 5 ms/block source, frame p50/p95/max, fetched/cancelled, materialized, heap | JUnit scale class, before = synchronous `SetData` fetching the same blocks | planned |
| 1M-node Tree with lazy children, expand never forced | JUnit scale class | planned |
| 50 CTabFolder / TabFolder tabs with heavy content: startup creates only the selected tab | JUnit + CustomControlExample/ControlExample | planned |
| 10M-row check-all timing, identical results | JUnit | planned |
| GTK, Cocoa | n/a | NOT_RUN |
