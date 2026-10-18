# Table as a mediated viewport (win32 first)

Status: T-Table-1 to T-Table-4 implemented on win32, and the follow-up T-Table-5 (logging, upstream-defect allow-list, text direction, MeasureItem, accessibility over logical rows). See Status below.

## Goal

Every `Table` behaves like a viewport over a logical row model while keeping its public contract and visible behaviour.
A client still gets the plain API, or the `SWT.VIRTUAL` API, with the same `SetData` timing, item identity, selection, focus, events, scroll bar and pixels.
Internally the win32 ListView is always `LVS_OWNERDATA`: it holds no native item per row.

The design follows the win32 Tree viewport (`docs/m3/TREE_MEDIATED_VIEWPORT.md`, branch `m3/tree-viewport-20261016`).
"A Table is a Tree of depth 1 with columns", so `TableViewport` uses the same phases, names and tests as `TreeViewport`.
The difference is the native realization, which the oracle below decided.

## Native realization: the owner-data ListView is the viewport

The Tree needs a content plane inside `hwndParent` because a TreeView has no owner-data mode: every native row is a native item, so only the resident window may be native, and `hwndParent` must own a logical scroll bar.

A ListView has an owner-data mode. In it, a row is only an index; text, images, indent and check state come from `LVN_GETDISPINFO`.
The oracle (`table-viewport/evidence/OwnerDataOracle.java.txt`, run log `OwnerDataOracle-win11-run.txt`) measured an owner-data ListView (O) against an ordinary ListView (N) with SWT's styles and the same rows.

| Case | Measured on Windows 11 (100 % zoom) | Consequence |
|---|---|---|
| A | `LVM_INSERTITEM` and `LVM_DELETEITEM` on O shift selection, focus and selection mark exactly like on N, including deleting the focused row and the focused last row. The notifications are identical too. | Structural edits stay native, one message each. |
| A | On O, `LVM_SETITEMCOUNT` changes only the count. Appending by count equals an append by `LVM_INSERTITEM`; inserting by count does not shift. | Appends may be batched into one count change; inserts may not. |
| B | Shrinking O by `LVM_SETITEMCOUNT` drops the focus (-1). Deleting N's rows from the end moves the focus to the new last row, and a mark beyond the end too. | Rule, verified by case L: after a count shrink, a focus or mark at or beyond the new end moves to the new last row. |
| C | `LVM_SETITEMSTATE` at index = count, count + 5 or -2: returns 0 on both and changes nothing. `LVM_GETITEM` out of range returns 0 on both. | No guard needed. |
| D | `LVS_SINGLESEL`: same states. O reports the implicit deselection as one `LVN_ITEMCHANGED` with item -1; N reports the row. | SWT already treats item -1 as a selection change. |
| E | `LVM_ENSUREVISIBLE`, including a body with `LVM_GETCOUNTPERPAGE` = 0 (the double call of `setTopIndex` and `showItem`): identical top rows. | Scrolling stays native. |
| F | Type-ahead: O sends `LVN_ODFINDITEM` with flags 0x26 (`LVFI_STRING | LVFI_SUBSTRING | LVFI_WRAP`; `LVFI_SUBSTRING` is the newer name of `LVFI_PARTIAL`) and the start row. Repeating a character searches that character from focus + 1; continuing a prefix searches from the focus; after the timeout a new search starts at focus + 1. Answering "first row from the start row, wrapping, whose text starts with the string ignoring case" gives the same focus and selection as N for 26 keystrokes (prefixes, case, repeated characters, no match, timeouts of 400 to 1500 ms). | `TableViewport` answers `LVN_ODFINDITEM` for a plain Table. A virtual Table keeps answering -1, as today. |
| G | `LVSCW_AUTOSIZE` on O measures only the rows in view (0..19): width 75 against N's 355 for a long row 250. With row 250 scrolled into view (top 232) O measures rows 232..251 and gives 355, as N; `LVSCW_AUTOSIZE_USEHEADER` too. | `TableColumn.pack` is mediated: the row with the widest estimate is scrolled into view (redraw off) before the native autosize, and the top row is put back. |
| H | `LVIS_DROPHILITED` is stored by O (`LVM_GETITEMSTATE` returns it) and painted identically. | Drop feedback unchanged. |
| I | `LVN_ODCACHEHINT` names the rows about to be painted: the visible page on repaint, the new rows after a scroll. | The resident window of the Tree design is this range; nothing is inserted or retired. |
| J | Up, Down, Page Down, End, Page Up, Home and clicks (window messages): identical focus, selection and top row. O reports range changes with `LVN_ODSTATECHANGED` and deselection as item -1 (an earlier revision of the probe drove real Shift+Down input: `OwnerDataOracle-win11-run-realinput.txt`, case J notes). | `LVN_ODSTATECHANGED` must reach SWT's selection logic for a plain Table too (today it is gated on `SWT.VIRTUAL`). |
| L | Indent: O ignores `LVM_SETITEM` (returns 0) and asks for `LVIF_INDENT` in `LVN_GETDISPINFO` (masks seen: text, image, state, indent); answering it paints identical pixels. Insert mark (`LVM_SETINSERTMARK`) works on O and paints identically. | Indent comes from the row model; every `LVM_SETITEM` write is skipped. |
| P | Pixels (PrintWindow) of N and O: identical initially, with a selection without and with keyboard focus, scrolled to row 150, with an empty first-column text, with drop highlight, indent and insert mark. | Native pixels are kept. |
| K | 1,000,000 rows on O by one `LVM_SETITEMCOUNT`: 0.24 ms, vertical bar maximum 999,999, page 19. Inserting 5,000 rows one by one with redraw on: N 2.5 s, O 1.9 s (0.42 s and 0.46 s with `WM_SETREDRAW` off). | The native bar is already the logical bar. Building row by row costs about the same as stock; batching is a later leaf. |

So for Table the native control already is the viewport.
The vertical bar covers every logical row, the visible range is native, and keyboard, caret, anchor, type-ahead and scrolling stay native.
There is no content plane and no `hwndParent`, so `layout`, `positionPlane`, `updateBar`, `onVScroll` and `onWheel` of `TreeViewport` have no Table counterpart.
The window-sized alternative (an owner-data count equal to the visible rows, mapping row i to first + i) was considered and rejected.
It would move every keyboard, caret, anchor and type-ahead rule out of the native control into Java, which is the risk this design avoids.

## Design

1. **Modes.** `TableViewport.ENABLED = WidgetMode.of ("Table", WidgetMode.VIEWPORT) == WidgetMode.VIEWPORT`.
   `-Dswt.widget.mode.Table=original` (or the global `swt.widget.mode`) selects stock SWT.
   With the viewport on, every Table is created with `LVS_OWNERDATA`.
   A `SWT.VIRTUAL` Table already was, and keeps its path: sparse facades, `SetData` on paint, and -1 for `LVN_ODFINDITEM`.
2. **Owner-data mechanics are separated from virtual semantics.**
   Native mechanics test "owner data"; `SetData`, sparse facades and cached flags keep testing `SWT.VIRTUAL`.
   - `LVM_SETITEM` writes of a plain Table (the cached-bounds flush in `clear`, `setText` and `setFont`, the column insert and delete rewrites, the indent) are ignored by owner data (oracle L: returns 0). In T-Table-2 they stay in place, at stock cost; T-Table-4 removes them.
   - `LVN_ODSTATECHANGED` is handled for every owner-data Table.
   - `LVN_ODFINDITEM` is answered for a plain Table (rule F).
   - The indent is answered through `LVN_GETDISPINFO` (rule L).
3. **`TableViewport` (win32), shaped like `TreeViewport`.** Its phases and names are those of the Tree mediator wherever the Table has the same work.
   - `install`: owner-data callback masks.
   - Structural edits (`insert`, `destroy`, `setItemCount`, `clear`, `clearAll`) run as plan, then commit, then bind, then `restoreFocus`:
     - plan: old count against new count, so a count change touches only the changed rows;
     - commit: one `LVM_INSERTITEM` or `LVM_DELETEITEM` per changed row, or one `LVM_SETITEMCOUNT` for appends and truncation;
     - bind: facades;
     - `restoreFocus`: rule B for a count shrink.
   - `residentRow (item)` validates a facade's row the way `residentHandle` validates a Tree facade's handle.
   - The resident window is the range of `LVN_ODCACHEHINT`.
4. **Row store (T-Table-3).**
   - A columnar store in the Table: text, image, font, background and foreground lanes per column, a row lane for font and colours, an indent lane, and check, gray and cached bits in `VirtualBitLanes`.
   - A facade (`TableItem`) is a pinned proxy that holds its row index as a hint. `indexOf` checks the hint in O(1) and renumbers lazily, only from the lowest edited row. This replaces today's linear scan, which costs O(n²) over a dense loop.
   - Rows that no API call has exposed have no facade: `setItemCount` on a plain Table creates none, and `getItem` creates the facade once and pins it.
   - Rows have one height in a ListView, so no prefix sums over row heights are needed. The Tree's Fenwick-style row arithmetic does not apply here.
5. **Column width (`TableColumn.pack`).** Before the native autosize, the row with the widest text estimate (string width + image + indent) is brought into view with redraw off, and the old top is restored afterwards (rule G). Stock virtual Tables keep their native behaviour.
6. **Threading.** The UI thread does at most one native message per changed row, or one count change. Type-ahead scans rows as the native ListView does. Width estimates for `pack` are O(rows), as the native autosize of a plain ListView is.

### Lanes and facades (DOOP, rule 18)

The state of a plain Table's rows lives in lanes of `TableViewport`; the row count lives in the native ListView (`LVM_SETITEMCOUNT`), with no Java array of rows behind it.

| Lane | Structure | Content |
|---|---|---|
| `cells` | `VirtualCellLanes`, one slot per column | object lanes `TEXT`, `IMAGE`, `CELL_FONT`; int lanes `CELL_BACKGROUND`, `CELL_FOREGROUND` (-1: none) |
| `rowLanes` | `VirtualCellLanes`, one slot | object lane `ROW_FONT`; int lanes `ROW_BACKGROUND`, `ROW_FOREGROUND`, `INDENT` |
| `flags` | `VirtualBitLanes` | `CHECKED`, `GRAYED`, `DIRECTION`, `DIRECTION_AUTO`, `DIRECTION_FLIP` |
| `widths` | `VirtualListWidth` | text width per row (an int lane, or a sorted row/width directory while few rows are measured) and its maximum |

Facades:
- A `TableItem` is made only when an API asks for one (`facade`: `getItem`, `getItems`, `new TableItem`, a walk that must see a row with state), loaded from the lanes (`hydrate`) and written back by every setter (`store`). It holds its row as a hint (`viewportRow`); `indexOf` renumbers lazily from the lowest moved row (`exactBelow`).
- It is held strongly in `Table.items` while it is one of the 1024 most recent or is pinned (listeners other than ZoomChanged, data, style bits, the current item); otherwise weakly (`weak`), so the application decides how long it lives, and its row keeps the state.
- Paint makes no facade: up to four reusable paint facades, one per nesting depth of `LVN_GETDISPINFO` and custom draw, are loaded from the lanes, unless a MeasureItem, EraseItem or PaintItem listener needs a real one.

## Tests (recorded differential and acceptance)

- `Test_org_eclipse_swt_widgets_Table_ViewportDifferential`, on the shared harness `ViewportDifferential` (a byte-identical copy from #198, see `docs/m3/VIEWPORT_DIFFERENTIAL_HARNESS.md`).
  - Every step records the answer, then the probes: rows and top row, scroll bar, selection, focus row and events (Selection, DefaultSelection and SetData, in order). Every seed ends with pixels.
  - Seeded scripts of public API calls: structural edits, selection, top index, `showItem`, `showSelection`, reads, text, check and `clear`. All randomness is drawn before the operation runs; row positions are fractions of the current row count.
  - Three variants, 12 seeds each, 150 steps per seed: `MULTI | FULL_SELECTION` with two columns, `SINGLE | CHECK`, and `MULTI | VIRTUAL`.
  - Two references are recorded into separate directories (`-Dswt.viewport.recording`). One is the **official** SWT 3.135.0 jar on the class path; the other is this SWT with `-Dswt.widget.mode.Table=original`. The viewport run is compared with each, and a difference fails with the seed's operation log and names the reference.
  - The focus row is read through the package-private `Table.getFocusIndex`.
  - An operation that throws a RuntimeException answers `error <class> <message>`, so a reference that throws still records every seed. The comparison with the official recording accepts exactly the steps listed in `table-viewport/UPSTREAM_DEFECTS.tsv`, and only while the recorded answer is the listed one (section Upstream defects); the shared harness is unchanged.
  - All runs use the invisible test desktop (`C:/m3-superset/tools/hidden_java.sh`); `table-viewport/table_differential_references.sh` runs the four of them.
  - Two processes see different desktops, so each seed runs in a state both can guarantee (each measure fixed a difference seen between two stock runs):
    - The shell is off the virtual screen. Whether the native control paints during an operation, and therefore when `SWT.SetData` is sent, depends on how much of the window the desktop shows.
    - The pixels are drawn on request with `WM_PRINT` (client and children) and converted to 24-bit RGB data. `Control.print` copies the composed screen content (`PW_RENDERFULLCONTENT`), which can be stale in a process that is not in the foreground. `PrintWindow` sometimes leaves the themed scroll bar unpainted; the scroll bar is recorded in every step instead. And the unused byte of 32-bit pixel data differs between processes.
    - Keyboard focus is on a one-pixel button outside the table, and focus rectangles are hidden.
    - Focused pixels are compared within one process by the acceptance test.
- `Test_org_eclipse_swt_widgets_Table_Viewport`: acceptance.
  - 1,000,000 rows have a bounded native cost: one count change and bar maximum 999,999.
  - Item identity survives inserts and removals.
  - Selection, focus and mark shift with inserts like native rows.
  - The shrink rule, the type-ahead and the column autosize with the widest row out of view.
  - Pixels against a stock owner-data (`SWT.VIRTUAL`) Table holding the same rows, with keyboard focus.
  - Released rows keep their state, columns move it, released rows paint it; appends are batched; `pack` with MeasureItem measures every row, and a MeasureItem listener that throws out of `pack` leaves no state behind.
  - In both modes (stock rows are the reference): `setTextDirection (SWT.AUTO_TEXT_DIRECTION)` reaches every row, and the ListView's accessible object counts and names every logical row.
- Both classes skip (JUnit assumption), never hang, when the Table viewport is not active: a plain Table without `LVS_OWNERDATA` is the discriminator. A reference run (official jar or original mode) records instead. The two acceptance tests that run in both modes do not skip.

## Status (win32, build `tt20261017n6`, invisible desktop, Windows 11, 100 % zoom)

| Gate | Viewport off (`-Dswt.widget.mode.Table=original`) | Viewport on (default) |
|---|---|---|
| Table, TableItem, TableColumn suites and `Test_org_eclipse_swt_widgets_Table_Viewport` | 216 pass, 0 fail (the acceptance tests that need the viewport are skipped as not active) | **235 pass, 0 fail** |
| `Test_org_eclipse_swt_widgets_Table_ViewportDifferential` variants 0-9 (88 seeds: variants 0-2 12 seeds x 150 steps, 3-8 8 seeds x 100 steps, 9 4 seeds x 60 steps; pixels per seed) | records the **original** reference; **equal to the official recording line for line on all 88 seeds** (rule 12) | **matches the official and the original recording on all 88 seeds, accepting exactly the 155 steps of `UPSTREAM_DEFECTS.tsv`** |

Variants 3-9 drive scripts of `TableViewportScripts`: input (keys and clicks with Shift and Ctrl in the thread keyboard state, marquee; variants 3, 4), geometry and presentation (`getItem (Point)`, item, text and image bounds, fonts, colours and images per row and cell, label-tip text, resizes while scrolled; 5, 6), listener reentrancy (7, 8) and `WM_DPICHANGED` (9).
`ASPECT_MATRIX.tsv` maps every public member and behaviour aspect to its test: 59 COVERED by the differential, 57 SUITE, no GAP rows.

Rules found by these variants (T-Table-7, T-Table-8):
- **Selection of input.** An owner-data ListView notifies a range (`LVN_ODSTATECHANGED`) or every row (`LVN_ITEMCHANGED`, item -1) whether or not a row's state changes; stock rows notify only changed rows. While a key, a button or a button-down move runs, `TableViewport` follows the selection: a snapshot (at most 4096 selected rows) before the message, and each notification counts as a change only where a row's state differs. A Shift range key (Shift+Space without Ctrl, or a Shift navigation key of a MULTI Table) counts only a different selection after the key. Stock rows re-notify a reselected row (End on the selected last row sends Selection).
- **Pack.** The row brought into view is the one whose text is widest with its own font (cell font, then item font), as the native autosize measures through custom draw; the first column reserves the icon for every row once there is an image list.
- **Determinism of the recording.** SWT posts input Selection events and runs them after the next native message, and the ListView arms timers after keys and clicks; `TableViewportScripts.quiesce` re-arms each timer with at least its native delay, awaits it, paints and drains.

## Performance (T-Table-11, build `tt20261017n6`)

The five-mode benchmark of the render-modes agent (`RenderModeBenchmark`, one JVM of 10 repetitions per widget, mode and operation) reported the Table viewport slower than the original mode for `scrollPage` (13.8 against 9.0 ms) and `selectAll` (6.1 against 2.2 ms).
Its Table operations were run again with 40 repetitions after 3 warm-up runs per JVM, the three runs (official jar, original, viewport) interleaved, three rounds, on the invisible desktop ([`TableBench`](table-viewport/evidence/TableBench.java.txt), [`table_bench.sh`](table-viewport/evidence/table_bench.sh), every run in [`TableBench-win11-hidden-run.tsv`](table-viewport/evidence/TableBench-win11-hidden-run.tsv)). Median ms, 1,000,000-row VIRTUAL Table (a VIRTUAL Table is not mediated; the default mode keeps it in `VirtualItemStorage`, the original mode in the R4_38 store):

| Operation | Official 3.135.0 | Original | Viewport |
|---|---|---|---|
| scrollPage | 7.0 / 12.1 | 5.8 / 9.7 | **3.7 / 4.1** |
| selectAll | 2.4 / 3.4 | 1.4 / 2.6 | **1.0 / 1.2** |
| scroll10k | 4.9 / 4.7 | 2.6 / 3.7 | **1.7 / 1.8** |
| paint | 1.3 / 2.2 | 1.3 / 2.0 | **1.0 / 1.0** |
| selectFar | 2.1 / 2.8 | 1.6 / 3.0 | **1.3 / 1.6** |

(rounds 1 / 2; round 3 ran under load from other sessions and is slower for every mode alike.) The default mode is not slower here; the benchmark's numbers came from single runs of 10 repetitions under load.
Java Flight Recordings of 300 `scrollPage` and 120 `selectAll` repetitions of this Table sample no Java frame of `TableViewport` or `Table` more than once: the time is spent in native code.

Median ms, 100,000-row plain Table (mediated in the default mode; rounds 1 / 2 / 3; the official run of round 1 did not start for lack of memory):

| Operation | Official 3.135.0 | Original | Viewport |
|---|---|---|---|
| scrollPage | 7.2 / 6.5 | 4.8 / 4.7 / 4.9 | 5.9 / 6.0 / 6.6 |
| selectAll | 438 / 524 | 826 / 274 / 506 | **2.1 / 2.5 / 1.2** |
| scroll10k | 1.2 / 2.7 | 2.7 / 2.9 / 2.5 | 3.2 / 2.6 / 2.6 |
| paint | 0.9 / 2.4 | 2.3 / 2.2 / 0.8 | 2.3 / 2.4 / 1.8 |
| selectFar | 2.3 / 5.6 | 5.9 / 5.0 / 5.0 | **3.0 / 2.9 / 2.7** |

`selectAll` sets one state on the owner-data ListView instead of one per native item. A page scroll of the plain Table costs about 1.2 ms more than in the original mode and no more than with the official jar; the cause is not profiled yet (open: T-Table-12).

`thumbBoundsFollowAnAppendBurst` fails on `tt20261016i` (before the ScrollBar hooks) and passes in both modes since `tt20261016j`.

The official 3.135.0 jar has an upstream defect in its VIRTUAL Table (section Upstream defects). On variant 2 it throws from seed 3 step 48 on (`ArrayIndexOutOfBoundsException: Index 297 out of bounds for length 289` in `Table._checkGrow`, from `getItems`), and in seeds 4, 8 and 11 rows answer another row's item or SetData. Earlier reports of this document named step 98; the recording with error answers shows step 48. The official recording completes all 36 seeds of variants 0-2; 109 of the 155 listed steps are this defect's (the other 46 are the insert defect of variant 8). Since `tt20261017k` the original mode answers these steps as the official jar does (the R4_38 store, rule 12).

Mediated in T-Table-2:
- every plain Table is owner data;
- `LVN_ODFINDITEM` and `LVN_ODSTATECHANGED`;
- `setItemCount` with one count change and the shrink rule;
- `TableColumn.pack` with the widest row brought into view.

Mediated in T-Table-3:
- the Table's item array is the row store, and a row has no facade until an API call asks for it. `setItemCount` creates none, and paint answers such rows from the default facade (unless a MeasureItem, EraseItem or PaintItem listener needs a real one);
- a facade keeps its row as a hint. `indexOf` checks it in O(1) and renumbers lazily from the first row an insert or removal moved; dispose uses it too.

Measured with `table-viewport/evidence/TablePerf.java.txt` (100,000 rows):

| Operation | Stock (`original`), visible desktop, `tt20261016d` | Viewport, visible desktop, `tt20261016d` | Stock, invisible desktop, `tt20261016g2` | Viewport, invisible desktop, `tt20261016g2` (batched appends) |
|---|---|---|---|---|
| `setItemCount (100000)` on a plain Table | 2,439 ms | 11 ms | 371 ms | 9 ms |
| 100,000 × `new TableItem` + `setText` | 46,983 ms | 36,039 ms | 33,928 ms | 1,904 ms |
| 20,000 × `indexOf` after an insert at row 0 | 12 ms | 8 ms | 14 ms | 1 ms |

Mediated in T-Table-3b (columnar lanes, facades released):
- The state of every row lives in columnar lanes, `VirtualCellLanes` (common). The lanes are text, image, font, background and foreground per cell; font, background, foreground and image indent per row; and check and gray bits in `VirtualBitLanes`.
  - A lane is one array per field and column, allocated on its first value that is not the default. Rows move in all lanes together.
  - Columns move by moving lanes, right after the Table's own loop over the items.
  - Growth follows `VirtualGrowth`, that is `PackedHeapGrowth`, the Synexia `PackedHeapGrowth`/`M3PreGc` structure already inlined by #122.
- A facade is a proxy of its row. It is made on demand and loaded from the lanes (hydrate). Every TableItem setter writes its state back at the end (store, a recipe-inserted one-line hook in twelve setters).
- The 1,024 most recently made or used facades are held strongly, and so are facades that carry state the lanes do not keep (listeners, data, a style other than the text direction). All others are held weakly.
  - While the application holds a facade, its row answers the same TableItem.
  - Once nothing holds it, it can be collected, and the row answers a new facade loaded from the lanes. No caller can tell the two apart.
- Paint answers rows without a live facade through paint facades loaded from the lanes, one per nesting depth; these are never handed to application code.
- `LVN_ODCACHEHINT`: live facades of the rows about to be painted are held strongly again.
- Callers that walk existing items (`clear`, `reskin`, column changes) get a facade for every row that holds state, as stock SWT has an item for every row. During dispose only live facades are released.

Mediated in T-Table-4:
- **Batched appends.** An `LVM_INSERTITEM` at the end of a plain Table is deferred; the native count follows with one `LVM_SETITEMCOUNT` per burst. This is equivalent because an append changes no selection, focus or mark (oracle A).
  - Every message to the ListView passes through `Table.windowProc` first. There, `LVM_GETITEMCOUNT` answers the logical count, and queries that do not depend on the rows pass (string and column widths, image lists, styles, font, header, an explicit column width).
  - Any other message brings the native count up to date before it runs. So do the scroll bar (`Table.scrolledHandle`, through which every `ScrollBar` reads) and the event loop.
  - An appended row that the native control does not count yet is not repainted.
- **Column-less scroll width.** `setScrollWidth (null, ...)` takes the widest first-column text from `VirtualListWidth`. Each row is measured once per change of its text, its font or the Table's font, instead of measuring every row on every recomputation; the image indent is the largest value of its lane.
- **Ignored native writes skipped.** The `LVM_SETITEM` writes that owner data ignores are skipped: the cached-bounds flush in `clear`, `setText` and `setFont`, the indent, and the per-row rewrite on column insert and delete.
- **`pack` with a MeasureItem listener** sends MeasureItem for every row, including rows without a facade, as for stock rows.

Mediated in T-Table-5 (follow-up):
- **Logging.** `TableViewport` logs through `SwtLog` (`org.eclipse.swt.internal.SwtLog`: java.util.logging, asynchronous), a byte-identical copy of the shared file, as `WidgetMode` is (which now logs through `SwtLog` too). The logger is `org.eclipse.swt.widgets.Table`; type-ahead answers, count changes and flushed append bursts are logged at trace level, with suppliers, behind `isTraceEnabled`.
- **Text direction over every row.** `Table.updateTextDirection` gives the items the Table's direction, but its loop sees only the strongly held facades. A hook right after the direction is computed (`textDirectionChanged`) sets three bit lanes (direction set, auto, flip) over the existing rows, word-wise, and updates the weakly held facades; facades loaded later take the direction from the bits. Rows inserted later have none, as stock items made later have none. The direction bits are not row state that needs a facade, and the flip bit does not pin a facade.
- **MeasureItem without a counter.** For a row without a facade, `TableColumn.pack` with a MeasureItem listener asks for one itself (a hook right after its `_getItem (i, false)`). The former depth counter is gone: a listener that threw out of `pack` left it raised, so every later walk over the rows made a facade per row. With no counter there is nothing to reset in a try/finally.
- **Scroll bar reads.** Every `ScrollBar` getter that reads the native bar through `scrolledHandle` (maximum, minimum, selection, thumb, size, visibility, and the setters) first brings the native count up to date. `getThumbBounds` and `getThumbTrackBounds` call `GetScrollBarInfo (parent.handle)` directly; T-Table-6 adds a one-line recipe hook at their start (`ScrollBar`, the only hooks outside the Table classes), so they flush too. `thumbBoundsFollowAnAppendBurst` (both modes) fails on build `tt20261016i` without the hook (thumb height 127 during a burst of 40 appends to 40 rows, 81 after it) and passes on `tt20261016j`.
- **Accessibility over logical rows.** The ListView's accessible object (`CreateStdAccessibleObject`, `OBJID_CLIENT`) counts and names every logical row: rows of a pending append burst, rows without a facade, rows made by `setItemCount` and rows after a removal. Measured in both modes; the object counts one child that is not a row, in both modes. The proxy reads the ListView through window messages, so the logical count and the flush before row queries apply to it as to SWT's own calls.

The hooks in `Table`, `TableItem` and `TableColumn` are one-line delegations inserted by the recipe in `table-viewport/recipe` (`gen_table_viewport_yml.py`, applied by `table_viewport_recipe.sh`; the second pass is UNCHANGED).

## Upstream defects

[`table-viewport/UPSTREAM_DEFECTS.tsv`](table-viewport/UPSTREAM_DEFECTS.tsv) lists every differential step where the official 3.135.0 jar (tag `R4_38`) answers differently from this SWT because of an upstream defect: variant, seed, step, the stock answer, this SWT's answer and the upstream cause. [`upstream_defects.py`](table-viewport/upstream_defects.py) generates it from the official and original recordings, and stops on any divergence that is not the defect below (outside the VIRTUAL variant, or not after a range removal). `Test_org_eclipse_swt_widgets_Table_ViewportDifferential` compares with a copy of the official recording in which exactly the listed answers are this SWT's; a listed step whose recorded answer is not the listed stock answer fails.

**Defect: R4_38 `Table._removeItems` shifts the later keys of a VIRTUAL Table by the wrong amount.** A VIRTUAL Table keeps the items it has made in a sparse store: `items [k]` is the item of row `keys [k]`. `remove (int start, int end)` calls `_removeItems (start, end + 1, count)` (line 3181), which drops the keys of the removed rows and shifts the later keys by `right - left`, the number of removed rows that had an item (line 392):

```java
for (int i=left; i<keyCount; i++) keys[i] -= (right - left);
```

The number of removed rows is `end - start` of `_removeItems`. When a removed row had no item yet, every later key stays too large. `_removeItem` (one row) shifts by one whether or not the row had an item (line 371) and is correct. Consequences:

- A kept item still claims its old row, which can lie beyond the row count, and the row it moved to answers a new item: `getItem`, `getItems`, `indexOf` and SetData differ.
- Once more than half of the rows have items, `_checkGrow` compacts the store into an array of `count + 4` (visible) or `count * 3 / 2` entries and writes `newItems [keys [i]] = items [i]` (line 193). A stale key at or beyond the length throws `ArrayIndexOutOfBoundsException`, from `_getItem` (line 295, called by `getItems` at line 2611) and from `createItem` (line 1767).
- A fix is `keys[i] -= (end - start);` on line 392.

[`evidence/KeyedStoreProbe.java.txt`](table-viewport/evidence/KeyedStoreProbe.java.txt) shows the defect in isolation, run on the invisible test desktop ([`KeyedStoreProbe-win11-hidden-run.txt`](table-viewport/evidence/KeyedStoreProbe-win11-hidden-run.txt)). In a VIRTUAL Table of 10 rows, rows 0 to 3 have no item when `remove (0, 3)` runs:

| Probe | Official 3.135.0 | This SWT |
|---|---|---|
| A: item of row 9 kept, `remove (0, 3)` | `indexOf` answers 9 in a Table of 6 rows; row 5 answers a new item ("new 5") | `indexOf` 5; row 5 answers the kept item ("old 9") |
| B: rows 6 to 9 have items, `remove (0, 3)`, `getItems ()` | `ArrayIndexOutOfBoundsException: Index 9 out of bounds for length 9` | 6 items, the last is "old 9" |

In the differential, the listed steps are variant 2 (`MULTI | VIRTUAL`) seeds 3 (102 steps from step 48: the store stays broken, so every later step differs), 4 (2 steps), 8 (2 steps) and 11 (3 steps), each after a `remove a..b`. **Rule 12 (viewport agent brief): the original mode is stock, the default mode fixes the defect.** In the default mode a VIRTUAL Table keeps its items in `VirtualItemStorage` (superset PR #116), which does not have the defect (the Table viewport does not mediate VIRTUAL Tables). In the original mode (`-Dswt.widget.mode.Table=original`) it keeps the R4_38 store: [`TableStockItems`](../../bundles/org.eclipse.swt/Eclipse%20SWT/win32/org/eclipse/swt/widgets/TableStockItems.java) is a transliteration of R4_38 `_checkGrow`, `_checkShrink`, `_getItem`, `_getItems`, `_initItems`, `_insertItem`, `_removeItem`, `_removeItems` (line 392 kept), `_setItemCount`, `indexOf`, the item loop of `releaseChildren` and of `updateTextDirection`; 23 recipe hooks (`STOCK_HOOKS`, `STOCK_ITEM_HOOKS` in the generator) delegate to it, keep a TableItem's cached, checked and grayed state in its own fields as R4_38 does, and let a DPI change reach `getItems ()` as R4_38 does. `virtualStoreIsStockOnlyInTheOriginalMode` (both modes) asserts the KeyedStoreProbe answers of the official jar in the original mode and the fixed answers in the default mode. The differential gates it: the original recording must equal the official recording line for line (`table_differential_references.sh`, "original equals official"), and the comparisons of the default mode with either recording accept exactly the listed steps.

**Defect: R4_38 `createItem` of a VIRTUAL Table loses the inserted item when the insert repaints inside a listener.** `createItem` grows the keyed store (`_checkGrow`, line 1767) before `LVM_INSERTITEM` (line 1787). Inside a listener the insert repaints, and `LVN_GETDISPINFO` makes items in the store (`_getItem` from line 6552), so the store can be full again when `_insertItem` (line 1790) copies the keys: `System.arraycopy` past the end of `keys` at line 347 throws `ArrayIndexOutOfBoundsException` out of `new TableItem (table, style, index)`, whose Javadoc lists no such exception, and the inserted item is missing from the store. [`evidence/InsertReplayProbe.java.txt`](table-viewport/evidence/InsertReplayProbe.java.txt) replays variant 8 seeds 0, 3 and 5 on the official jar ([run](table-viewport/evidence/InsertReplayProbe-win11-hidden-run.txt): keys 2/4 before the listener, 20/20 at the throw); [`InsertProbe`](table-viewport/evidence/InsertProbe.java.txt) shows that the same inserts outside a listener make no item during the insert. These are the 46 listed steps of variant 8 (seeds 0, 3, 5). The default mode keeps `VirtualItemStorage`, which inserts; the original mode keeps the R4_38 store with the defect.

## Leaves

| Leaf | Scope | Acceptance |
|---|---|---|
| T-Table-1 | oracle, design, recorded differential, acceptance tests, suite baselines (viewport off and on) | this document; tests skip without the viewport |
| T-Table-2 | owner-data plain Table: owner-data mechanics for every Table, `LVN_ODSTATECHANGED`, `LVN_ODFINDITEM`, indent through `LVN_GETDISPINFO`, shrink rule and one count change in `setItemCount`, `pack` mediation | differential passes on every reported seed; Table suites ≥ baseline |
| T-Table-3 | facades as pinned proxies with O(1) `indexOf`, lazy facades for `setItemCount`, default-facade paint; T-Table-3b columnar lanes, weakly held facades, cache-hint hold | 1,000,000-row plain Table without a facade per row; state survives released facades; differential passes |
| T-Table-4 | batched appends (one count change per burst), incremental column-less scroll width (`VirtualListWidth`), removal of the `LVM_SETITEM` writes that owner data ignores, `pack` with MeasureItem over every row | row-by-row build faster than stock; differential passes |
| T-Table-5 | `SwtLog` logging; error answers and the upstream-defect allow-list; text direction over every row; MeasureItem without a counter; scroll-bar note; accessibility over logical rows | differential matches both references (official with exactly the listed steps); acceptance in both modes where it applies |
| T-Table-6 | thumb bounds of a Table bar flushed (ScrollBar hooks); shared harness updated to the all-widgets head (survey mode, relations) | `thumbBoundsFollowAnAppendBurst` fails before, passes after; differential unchanged |
| T-Table-R12 | rule 12: the original mode is stock. A VIRTUAL Table of the original mode keeps the R4_38 item store (`TableStockItems`, 23 recipe hooks), with the `_removeItems` defect; the default mode keeps `VirtualItemStorage`; `UPSTREAM_DEFECTS.tsv` names the stock Javadoc of every listed step | original recording equal to the official one (36/36 seeds); default mode matches both with the listed steps; `virtualStoreIsStockOnlyInTheOriginalMode` in both modes |
| T-Table-7 | differential variants 3-9 (input with Shift and Ctrl, marquee, geometry and presentation, reentrancy, DPI); Selection of input follows per-row changes | all 10 variants match both references with the listed steps (gate `tt20261017n6`) |
| T-Table-8 | `pack` measures the widest row with its own font; second upstream defect (insert inside a listener of a VIRTUAL Table) listed | variants 5, 6 and 8 match both references |
| T-Table-9 | `ASPECT_MATRIX.tsv` without GAP rows | 59 COVERED, 57 SUITE |
| T-Table-10 | `upstream_defects.py` classifies both defects and stops on any other divergence | 155 listed steps, regenerated from the recordings |
| T-Table-11 | benchmark candidates (`scrollPage`, `selectAll`) measured again | viewport not slower (section Performance) |

## Donors

See [`table-viewport/FOSS_REUSE_DECISION.tsv`](table-viewport/FOSS_REUSE_DECISION.tsv).

- The owner-data mechanics come from this repository's own virtual Table path (EPL-2.0).
- The mediator shape and the recorded differential come from the Tree viewport (#196).
- The bit lanes come from `VirtualBitLanes`.
- Initiative 31's custom-drawn Table (`ListLikeModel`) and Draw2d's range model are pattern-only here: the viewport mode keeps the native keyboard model and the native bar.

## Non-claims

- Keyboard ranges with Shift and Ctrl, and marquee selection, are not compared: modifier state cannot be synthesized through window messages, and real input (used by an earlier revision of the probe) was unreliable on a shared desktop and could reach other windows, so the current probe sends window messages only.
- GTK and Cocoa are not covered.
- Accessibility: the row count and the row names of the accessible object are measured; focus, selection and check state of rows through it are not.
- The official jar was not patched to confirm the fix of the upstream defect; the defect itself is shown by `KeyedStoreProbe`.
