# SWT lazy data, lazy tree children and lazy tab content (2026-10-13)

Ledger rows (Synexia #10864, `WIDGET_IMPLEMENTATION_LEDGER.tsv`): `lazy_data_source`
(wave 2, score 16), superset gap #6 (tree lazy async children with placeholders),
`tab_folder` lazy content. Recipe crate: Synexia `swt-lazy-data-20261013` (#10900).
Donor decisions: `swt-lazy-data-20261013-FOSS_REUSE_DECISION.tsv` beside this page
(AG Grid Community infinite row model, react-window-infinite-loader, TanStack Query
infinite queries, AndroidX Paging and Compose Lazy/Pager: ADAPT, pattern only; Avalonia
and WPF: ADAPT for facade release and lazy tabs, NO_FIT as fetchers; Qt
`fetchMore`/`canFetchMore`: LICENSE_REVIEW_REQUIRED, names only). No donor text is copied.

The design was committed before the code (D2C); this page is the record of what landed.
Results are in section 7.

## Laws this leaf is held to

1. Nothing is created, fetched or laid out until display or a caller's API needs it.
2. Bits, ints, bitwise masks and reusable arrays; objects only when a caller's API asks.
   `TableItem`/`TreeItem` are facades created for visible rows or API calls.
3. Per-item state is reproducible from the parent's packed state plus the data source, so a
   facade that scrolls out can be released and re-created with identical API values and pixels.
4. No work while the widget is not visible: set dirty bits, catch up once.
5. Native first: the win32 `LVS_OWNERDATA` list view and the TreeView text callback stay;
   the Java side answers their callbacks.
6. Public API results do not change; new API is additive and opt-in.
7. Shared (`common/`) logic is GWT-safe: no `int` overflow reliance, no `String[].clone()`,
   no `Objects.checkIndex`, `int` words over `long`.
8. Fetches run on worker threads; results reach the UI thread through one
   `Display.asyncExec` per frame; a block that leaves the overscan window is cancelled.
9. SetData is negotiated by our layer: the application is asked per block range, once per
   row while its block stays resident; rows scrolled out and back are restored from the
   layer without asking the application again.

## 1. `VirtualPageSource` (new public class, `common/.../widgets`)

The only new public type. It sits behind the existing `SWT.VIRTUAL` + `SWT.SetData`
contract of `Table` and `Tree`, so it also runs on GTK and Cocoa through public API only
(NOT_RUN there).

```java
VirtualPageSource source = new VirtualPageSource (256, 64, 2, VirtualPageSource.NONE, request -> {
    for (int i = request.getStart (), end = i + request.getCount (); i < end; i++) {
        request.setText (i, 0, db.name (i));      // worker thread
        request.setText (i, 1, db.city (i));
        request.setData (i, db.handle (i));       // optional data handle
    }
});
source.attach (table);   // table: SWT.VIRTUAL, setItemCount (10_000_000)
```

- **Range API (the negotiated SetData).** `Fetcher.fetch (Request)` is the additive range
  callback: the application fills a block `[getStart (), getStart () + getCount ())`. The
  classic per-item `SWT.SetData` listener stays fully supported for widgets without a source.
- **Block store** behind the package-private interface `VirtualBlockStore`; the in-memory
  implementation is `VirtualBlockCache`: a fixed number of slots, per slot `int` lanes
  (parent key, block id, state bits, LRU stamp, total) and per row lanes (cells, child-count
  hint, node key, data handle). Lookups are linear over the bounded slot lanes (no hashing).
  Eviction takes the least recently stamped settled block not touched in the current frame
  (AG Grid `maxBlocksInCache`/`lastAccessed`, Paging `maxSize`).
  **Swap adoption point:** `VirtualPageSource.newBlockStore (...)` is the one line that picks
  the store; the TieredSwapCache + SwapPressureController ported for ItemResidency (SWT #122)
  plugs in there, so blocks evicted under pressure swap out and back in without re-fetching.
- **Window and priority.** Each frame the source reads `getTopIndex ()` and the visible row
  count and requests the visible blocks first, then overscan blocks by distance. The overscan
  is widened in the scroll direction by the blocks moved per frame (capped at twice the
  overscan) and narrowed behind; a move of more than 8 blocks per frame is a jump and keeps the
  symmetric overscan (Paging `prefetchDistance`/`jumpThreshold`). The queue is re-sorted by
  that order every frame.
- **Cancellation.** Queued requests outside the window are dropped before they start;
  running ones get their cancel bit set (`Request.isCancelled ()`, TanStack `AbortSignal`)
  and their result is discarded.
- **Workers and delivery.** Two daemon workers, started on the first request, pull the head
  of the queue under one lock. A finished block goes on a completion list; the first
  completion of a frame schedules one `asyncExec` that drains every completion, stores the
  blocks, and re-asks the affected visible rows. One drain per frame.
- **Placeholders.** A row whose block is not loaded is answered blank (or with the
  placeholder text under `PLACEHOLDER_TEXT`) and stays *uncached*, so the native view asks
  again once the block is in; no TableItem is pinned for it.
- **API reads.** A caller's API read of a row that is not loaded (any `SetData` outside the
  paint callbacks) fetches its block on the calling thread, as a synchronous `SetData`
  would, so API results are identical to the classic listener. Paint-time requests never block.
- **Construction bit flags.** `TREE`, `PLACEHOLDER_TEXT`, `KEEP_FACADES`.
- **Counters** for the proofs: `REQUESTED`, `FETCHED`, `CANCELLED`, `FAILED`, `EVICTED`,
  `FRAMES`, `RESIDENT`, `IN_FLIGHT`, `RELEASED`.
- **Growth.** Every growable lane of the lazy layer (bit lanes, queues, node registry) grows
  through `VirtualGrowth.capacity`: on a capacity miss it samples `Runtime.maxMemory ()` and the
  used heap once and doubles only while the doubled array fits under 80 % of the limit,
  otherwise it grows to the exact length. Interim owner until the Xmx-aware growth ported from
  Synexia `PackedHeapGrowth`/`M3PreGc` lands in `org.eclipse.swt.internal` (SWT #122).

### Host hooks (win32 `Table`/`Tree`, SetData and expand paths only)

`VirtualPageHost` is package-private. win32 `Table` and `Tree` register a factory for it in
their class initializers (`VirtualPageSource.tableHost/treeHost`), so no public method is
added to the widgets; other platforms register nothing and the source uses public API.

- `checkData`: with a page source the facade handed to the source is not pinned.
- `Table` releases transient facades outside `[top - overscan, top + visible + overscan)` once
  per drain, never inside a paint callback. A facade is released only when it is not PINNED
  (no API or listener ever received it) and not DIRTY; its checked/grayed bits live in the
  parent lanes (section 4) and its text and data handle come back from the block store.
- `Table`/`Tree` mark paint-time SetData (`paintRequest`), so the source can tell a paint
  request (async) from a caller's API read (sync).
- `Tree`: `TVN_GETDISPINFO` answers the loading child (section 2) without a `TreeItem`.

**ItemResidency adoption point.** `_getItem (index, true)` creates the transient facade and
`Table.releasePageFacades` releases it; with ItemResidency (thin handle = parent + slot +
generation) these become `handle (index)` and `release (range)`, and the facade getters read
the parent lanes and the block store instead of item fields. Nothing else changes.

## 2. Tree: lazy async children with a mask-bit placeholder (gap #6)

- A row's block carries a child-count hint: `0` leaf, `n > 0` known (goes straight to
  `TreeItem.setItemCount (n)`: native placeholders, data fetched only for visible rows), or
  `UNKNOWN`: the node gets one native child with lParam `-1` (no `TreeItem`) so it shows
  an expander. An `UNKNOWN` node reports one child until its children are loaded.
- `TVN_ITEMEXPANDING` -> `SWT.Expand`: the source sets `CHILDREN_LOADING` (already in
  `VirtualItemState`) on the node's topology mask and requests block 0 of its children
  together with the child count (Paging initial load), ahead of everything else.
- `TVN_GETDISPINFO` for a native child whose parent has `CHILDREN_LOADING` writes the
  loading text into the native buffer and returns: the placeholder is drawn from the
  parent's mask bit and no `TreeItem` exists for it (test reads the native text and counts
  facades).
- When the count arrives: clear `CHILDREN_LOADING`, set `CHILDREN_KNOWN`,
  `setItemCount (n)`, re-ask the visible children. Collapsing a node cancels its pending
  and running blocks. The source never calls `expandAll`.
- Node keys and data handles are lanes of the block store; expanded nodes are remembered in a
  small registry (item, key, resolved child count), compacted when facades are disposed.

## 3. Lazy tab content (`TabFolder`, `CTabFolder`)

Opt-in by the existing `SWT.VIRTUAL` style bit (bit 28 is free in both; CTabFolder keeps the
bit in its style mask so `getStyle ()` reports it). Without the bit nothing changes.

- The folder sends `SWT.SetData` (`event.item` = tab item, `event.index` = its index) the first
  time a tab becomes the selection while the folder is showing; the listener creates the
  content and calls `setControl`, which lays it out once (`setBounds`).
- Filled tabs are bits: `VirtualBitLanes` in TabFolder, an `int[]` bitset in CTabFolder (other
  package); both shift on insert and remove.
- Selection while hidden sets a pending bit; the first `WM_PAINT` (TabFolder) or paint event
  (CTabFolder) catches up once. Visibility is one private method, `lazyContentShowing ()`,
  answering `isVisible ()` until the visibility leaf's effective-visibility bit lands.

## 4. Per-item state as parent lanes

| state | owner after this leaf | status |
|---|---|---|
| checked, grayed (virtual Table) | `VirtualBitLanes` in `Table` (`int` words, lazily allocated); `TableItem.set/getChecked/Grayed` read and write the lane; shift on insert/remove; check-all is a word fill, counts are `Integer.bitCount` | DONE |
| checked, grayed (dense Table, Tree) | item fields / `VirtualTreeTopology` mask | unchanged (next leaf) |
| text and data handle of a source row | block store lanes; the facade holds a copy | DONE |
| selection | `VirtualSelectionModel`: range pairs + complement (select-all, shift-range, invert) and an `int`-word bitset for scattered selections, switched by density (cut-over `max (1024, rows >>> 6)` runs, back to ranges after a bulk span leaves a quarter of that); the bitset form exposes the same range view | model DONE; win32 Table/Tree selection stays native (LVS_OWNERDATA/TreeView state) - adoption is the next leaf |
| expanded, has-children, children-known/loading | `VirtualTreeTopology` mask bits (`CHILDREN_LOADING` used for the placeholder) | DONE for loading/known |
| focused, hovered, enabled, filtered-visible, dirty | native / item fields; `DIRTY`/`PAINT_RESIDENT` in the virtual mask | next leaf |
| caller `Item.setData` on a facade | stays on the pinned facade (a facade the caller holds is never released) | parent reference lane: ItemResidency (SWT #122) |

Reproduction contract today: a facade is released only when every state it carries is
reproducible (not PINNED, not DIRTY; check bits in lanes; text and data from the store). The
round-trip test sets states, scrolls the facades out (they are disposed), scrolls back and
requires identical API values and an identical pixel hash. Coverage against the residency
agent's `REPRODUCTION_STATE.tsv` is pending that file.

## 5. Capture modes

Pixel hashes use `Control.print (GC)` into an image (works off screen, client area only).
The on-screen display capture (`GC.copyArea` over a topmost, active shell, needed for native
non-client areas) is not used by this leaf's proofs.

## 5b. Additions after the first review (2026-10-13 afternoon)

- **Stacked on SWT #122** (head c978f5d3): `VirtualGrowth` delegates to `PackedHeapGrowth`; the
  `SWAP` construction flag spills evicted blocks to #122's `FileSwapStore` (`VirtualBlockSpill`)
  and answers them back without asking the fetcher. Merge #122 first.
- **Paint never materializes page-source rows**: Table `LVN_GETDISPINFO` and custom draw, Tree
  `TVN_GETDISPINFO` answer rows without facades from the block store and the parent lanes; a
  facade is created only for caller API (and filled at creation) or for paint listeners
  (`MeasureItem`/`EraseItem`/`PaintItem`, which hand items to application code). Classic
  `SWT.SetData` tables without a page source still materialize one facade per newly painted row
  (the classic contract hands the application a `TableItem`); removing that needs the
  flyweight/identity policy of #122 (`IDENTITY_BY_METADATA`) - not done here.
- **Negotiated SetData** (jsonviewer pattern, improved): block-keyed dedup, generation tokens
  (`invalidate` bumps; stale results dropped), one self-reposting drain with a 4 ms budget,
  settle-gated column fit, jump load debounce, direction/velocity prefetch.
- **Busy indicator**: a caller's API read that waits more than 100 ms waits through
  `BusyIndicator.showWhile (Future)`; bulk child counts above one screen run inside the bulk
  batch with the busy cursor.
- **Bulk batch**: only above one screen of rows (threshold method until the visibility leaf owns
  it), ended by invalidating only the accumulated damage.
- **Cocoa lazy children (CI finding, macOS `Test_org_eclipse_swt_widgets_Tree` lines 367/1444)**:
  `Tree.outlineView_child_ofItem` creates a real `TreeItem` for every row NSOutlineView asks for.
  The fix is a data source handing NSOutlineView lightweight placeholder objects keyed by
  (parent, index) and mapped to packed state, materializing a `TreeItem` only when SWT API asks.
  Every Cocoa data-source and delegate callback resolves items through `display.getWidget (id)`
  today, so the placeholder must be understood by all of them. NOT_DONE in this branch: it cannot
  be compiled against AppKit or run on this Windows host; it is listed in TESTS_TO_RUN as a
  macOS CI item.

## 6. What is not done (named, not claimed)

- Win32 Table/Tree selection driven by the Java hybrid model (it stays native).
- Dense-mode Table and Tree check/expand lanes; per-cell font/colour/image ids from the source.
- Swap-out of evicted blocks (needs SWT #122); today an evicted block is re-fetched.
- GTK and Cocoa: NOT_RUN. RAP/GWT: `VirtualBitLanes`, `VirtualBlockCache`,
  `VirtualSelectionModel` are written GWT-safe; the GWT gate was not run.

## 7. Results

See the PR body and the Synexia crate evidence (`evidence/`), generated from the logs.
