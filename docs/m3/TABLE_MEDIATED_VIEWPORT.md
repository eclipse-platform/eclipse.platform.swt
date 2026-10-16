# Table as a mediated viewport (win32 first)

Status: T-Table-1 (oracle, design, tests) and T-Table-2 (owner-data plain Table) implemented on win32; see Status below.

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

## Tests (recorded differential and acceptance)

- `Test_org_eclipse_swt_widgets_Table_ViewportDifferential`: the recording line of the Tree differential (answer | bar | sel | focus | events), plus pixels per seed.
  - Seeded scripts of public API calls: structural edits, selection, top index, `showItem`, `showSelection`, reads, text, check and `clear`. They run on a stock Table and on a viewport Table holding the same rows.
  - The stock run is recorded by a child JVM started with `-Dswt.widget.mode.Table=original`. The viewport run replays the same seeds and compares every step.
  - A difference fails with the operation log, which is the reproduction.
  - The focus row is read through the package-private `Table.getFocusIndex`.
  - Three variants, 12 seeds each, 150 steps per seed: `MULTI | FULL_SELECTION` with two columns, `SINGLE | CHECK`, and `MULTI | VIRTUAL` (`SetData` order recorded).
  - Two processes see different desktops, so the harness runs in a state both can guarantee (each measure below fixed a difference seen between two stock runs):
    - The shell is off the virtual screen. Whether the native control paints during an operation, and therefore when `SWT.SetData` is sent, depends on how much of the window the desktop shows.
    - The pixels are drawn on request with `WM_PRINT` (client and children). `Control.print` copies the composed screen content (`PW_RENDERFULLCONTENT`), which can be stale in a process that is not in the foreground, and `PrintWindow` sometimes leaves the themed scroll bar unpainted. The scroll bar is recorded in every step instead.
    - Keyboard focus is on a one-pixel button outside the table, and focus rectangles are hidden. The hash covers the RGB of every pixel, not the raw pixel bytes.
    - Focused pixels are compared within one process by the acceptance test.
  - `-Dorg.eclipse.swt.tests.tableDifferential.selfCheck=true` with `-Dswt.widget.mode.Table=original` runs stock against stock, which proves that the harness is deterministic.
- `Test_org_eclipse_swt_widgets_Table_Viewport`: acceptance.
  - 1,000,000 rows have a bounded native cost: one count change and bar maximum 999,999.
  - Item identity survives inserts and removals.
  - Selection, focus and mark shift with inserts like native rows.
  - The shrink rule, the type-ahead and the column autosize with the widest row out of view.
  - Pixels against a stock owner-data (`SWT.VIRTUAL`) Table holding the same rows, with keyboard focus.
- Both classes skip (JUnit assumption), never hang, when the Table viewport is not active: a plain Table without `LVS_OWNERDATA` is the discriminator.

## Status (win32, build `tt20261016c`, Windows 11, 100 % zoom)

| Gate | Viewport off (`-Dswt.widget.mode.Table=original`) | Viewport on (default) |
|---|---|---|
| Table, TableItem, TableColumn suites (216) | 216 pass (1 win32 assumption skip) | 216 pass (1 win32 assumption skip) |
| `Test_org_eclipse_swt_widgets_Table_Viewport` (8) | skipped (not active) | **8/8 pass** |
| `Test_org_eclipse_swt_widgets_Table_ViewportDifferential` (3 variants x 12 seeds x 150 steps + pixels) | self-check stock against stock: pass | **stock child against viewport: pass, every seed** |
| `Test_org_eclipse_swt_widgets_Table.test_clear$I_resetsOnlyTheGivenRows` | fails before the `clear(int[])` fix (indent 36 expected, 4 found), passes after | passes |

Mediated in T-Table-2:
- every plain Table is owner data;
- `LVN_ODFINDITEM` and `LVN_ODSTATECHANGED`;
- `setItemCount` with one count change and the shrink rule;
- `TableColumn.pack` with the widest row brought into view.

The hooks in `Table` and `TableColumn` are one-line delegations inserted by the recipe in `table-viewport/recipe` (`gen_table_viewport_yml.py`, applied by `table_viewport_recipe.sh`; the second pass is UNCHANGED).

## Leaves

| Leaf | Scope | Acceptance |
|---|---|---|
| T-Table-1 | oracle, design, recorded differential, acceptance tests, suite baselines (viewport off and on) | this document; tests skip without the viewport |
| T-Table-2 | owner-data plain Table: owner-data mechanics for every Table, `LVN_ODSTATECHANGED`, `LVN_ODFINDITEM`, indent through `LVN_GETDISPINFO`, shrink rule and one count change in `setItemCount`, `pack` mediation | differential passes on every reported seed; Table suites ≥ baseline |
| T-Table-3 | columnar row store, facades as pinned proxies with O(1) `indexOf`, lazy facades for `setItemCount` | 1,000,000-row plain Table without a facade per row; differential passes |
| T-Table-4 | batched appends (one count change per burst), incremental column-less scroll width (`VirtualListWidth`), removal of the `LVM_SETITEM` writes that owner data ignores | row-by-row build faster than stock; differential passes |

## Donors

See [`table-viewport/FOSS_REUSE_DECISION.tsv`](table-viewport/FOSS_REUSE_DECISION.tsv).

- The owner-data mechanics come from this repository's own virtual Table path (EPL-2.0).
- The mediator shape and the recorded differential come from the Tree viewport (#196).
- The bit lanes come from `VirtualBitLanes`.
- Initiative 31's custom-drawn Table (`ListLikeModel`) and Draw2d's range model are pattern-only here: the viewport mode keeps the native keyboard model and the native bar.

## Non-claims

- Keyboard ranges with Shift and Ctrl, and marquee selection, are not compared: modifier state cannot be synthesized through window messages, and real input (used by an earlier revision of the probe) was unreliable on a shared desktop and could reach other windows, so the current probe sends window messages only.
- GTK and Cocoa are not covered.
- Accessibility is not measured.
