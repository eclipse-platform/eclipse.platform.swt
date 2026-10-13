<!-- SPDX-License-Identifier: EPL-2.0 -->
# List, Combo, CCombo: native owner-data viewport (win32)

Leaf of the 2026-10-13 directive "all widgets in SWT, Nebula, React and Qt, implemented with native controls and a viewport": contract atom L4 (win32 native window) of `LIST_VIEWPORT_CONTRACT.md` (Synexia #10811 / #10824), built on L1/L2 (`VirtualListModel`, `VirtualListWidth`, SWT #116).

## 1. Switch and contract

- `SWT.VIRTUAL` (bit 28, unused by List, Combo and CCombo) turns the viewport on. A List or CCombo without it runs the unchanged code path; no new style bit.
- The public API results of a `VIRTUAL` list are the plain list's results for the same calls. Proof: a differential trace (section 5) runs the same seeded call sequences on the unmodified fork build and on this build, one JVM each, and compares them with `diff`.
- Additions are additive: `List.setItemCount(int)`, `List.clear(int)`, `List.clearAll()` (`@since 3.137`) and `SWT.SetData` (with `event.index`) for `VIRTUAL` lists, mirroring `Table`. Implemented on win32; gtk and cocoa are design only (section 8).
- `CCombo` keeps `SWT.VIRTUAL` (its style mask `StylePolicy.COMBO` now admits it) and passes it to its drop-down `List`.

## 2. Ownership (one control, no wrapper)

| state | owner | representation |
|---|---|---|
| item count | native list box (`LBS_NODATA`, `LB_SETCOUNT`) and `VirtualListModel` | one `int` each |
| item text | `VirtualListModel.items` | one `String` reference per item in one dynamic array; `null` = not materialized |
| widest item | `VirtualListWidth` lazy lane | no array while every row is unmeasured; then one `int` per row, `UNMEASURED` until measured |
| selection, caret, anchor, top row | native list box | SINGLE: 0 bytes per row; MULTI: the list box's own per-row selection state (native mouse and keyboard multi-selection need it) |
| flags | `List.storeBits` | `STORE_EMPTY_EXTENT_INSET`, `STORE_IN_SET_DATA`, `STORE_ADDED_UCC` |
| paint scratch | `List` fields | one reusable `DRAWITEMSTRUCT`, two `RECT`, one `char[]` (growth doubles, step capped at 65536 chars) |

Array growth (items and widths) goes through one grow method, `List.growCapacity`, installed as `VirtualListModel.growth`: the heap is sampled (`Runtime.maxMemory`, used heap) once per capacity miss; exact length when the headroom is under four times the doubled growth, doubling otherwise. It is the adoption point for the shared Xmx-aware helper (SWT #122, Synexia `PackedHeapGrowth`/`M3PreGc`); the shared classes stay GWT-safe because the policy is a function installed by win32 code.

Adoption point for `widgets/ItemResidency` (another session): `VirtualListModel.items` is the packed parent storage of the item strings; its swap/recreate tiers can replace the array behind `getItem`/`setItem`/`clear` without touching `List`.

Selection: on win32 the native list box keeps the interactive selection, so no second owner is added; `VirtualSelectionModel` stays the selection algebra of `VirtualListModel` (used by the gtk/cocoa design). The run/array/bitmap/pattern containers asked for on 2026-10-13 belong in `ViewportRuntime.Selection` (shared with Table and Tree) and are not part of this leaf.

## 3. Native facts that shaped the code

All measured on this host before or while the code was written (`NATIVE_LIMITS.tsv` N1-N19, probes in `bench/Native*Probe.java`, raw output in `evidence/`).

- `LB_SETCOUNT` takes up to 2^31-1 rows (N16); 10M rows cost 2.5-214 ms. Find-string and item text do not exist (N2): text, `indexOf`, keyboard search and accessibility names come from the Java store.
- Owner-draw fixed lists keep their first item height (16 vs plain 15 = `tmHeight`, N3): `LB_SETITEMHEIGHT(tmHeight)` at creation and on every `WM_SETFONT`, keeping the top row.
- Single-selection `LBS_NODATA` list boxes corrupt their count when `LB_INSERTSTRING` follows `LB_SETCOUNT` (N7) unless one row was inserted before the `LB_SETCOUNT` (N19): SINGLE primes the list box and then uses row messages, which keep the plain list's semantics exactly.
- Multi-selection list boxes keep per-row selection state; one row message at row 0 of 10M rows costs 69-145 ms (N5). MULTI uses row messages while rows x count stays under 2^26 and otherwise one `LB_SETCOUNT` plus a restore that reproduces the plain list's caret (N13, simulated per removed row), anchor (N14, dropped by deleting row 0 first because `LB_SETANCHORINDEX(-1)` is refused), selection runs (N15, `LB_SELITEMRANGEEX`, which leaves caret and anchor alone) and top row (N17, row deletes reset it to 0).
- Keyboard search of the plain list box (N8): next item after the caret, wrapping, whose first character matches ignoring case; the `VIRTUAL` list does the same over materialized rows (a key never triggers `SetData`, like `Table` answering `LVN_ODFINDITEM`), notifying only while it has focus (verified with focus by `bench/NotifyProbe.java`).
- Scroll bar: the list box publishes a 32-bit row range (N16) but `WM_VSCROLL` thumb messages carry a 16-bit position; above 65535 rows a thumb message whose HIWORD is the low 16 bits of `GetScrollInfo(SIF_TRACKPOS)` scrolls to the 32-bit track position. No scaled range is needed. A real mouse thumb drag is NOT_RUN (it needs the user's mouse).
- `COMBOBOX` has no count-only mode (N10, N11).

New OS constants (Java constants only, no new natives; the fork's DLL is unchanged): `LBS_NODATA`, `LBS_OWNERDRAWFIXED`, `LB_SETCOUNT`, `LB_SETITEMHEIGHT`, `LB_GETANCHORINDEX`, `ODS_DISABLED`, `ODS_FOCUS`, `ODS_NOFOCUSRECT`, `ETO_OPAQUE`, `ETO_RTLREADING`. `OS.LB_SETANCHORINDEX` is 0xf19c while the documented message is 0x19c (N12); the plain path keeps the existing constant, the owner-data path uses 0x19c.

## 4. Laziness, SetData and the visibility law

- Rows are materialized (`SWT.SetData`, `event.index`, `event.item == null`) only when drawn (`WM_DRAWITEM`, sent by the OS only for visible rows of a visible window) or when an API call names them (`getItem`, `getItems`, `indexOf`, `getSelection`, accessibility `getName`). `SetData` fires once per row per residency (until `clear`, `clearAll` or a shrinking `setItemCount`), never re-entrantly; a listener that does not answer leaves "". Overscan and idle prefetch are not requested (next leaf, section 9).
- Widths are measured for painted rows of an `H_SCROLL` list and for exact size queries. Lists of up to 4096 rows keep the horizontal extent exact on every change (bounded work, and it keeps the scroll bar, client height and top-row clamping identical to the plain list); above that the extent covers measured rows, as `Table` does for `VIRTUAL`, and `computeSize` measures every materialized row once and is exact.
- Batching: `setItems` turns redraw off (`WM_SETREDRAW`) around the native count change and lets the list box invalidate once; per-row paint repaints the whole row (`ETO_OPAQUE`), so no row needs a background erase. Wheel coalescing and erase suppression during scroll belong to the scroll-bar work of the visibility session (`ScrollBar`, `Scrollable`, `VirtualScrollMetrics`); List will call into it once it lands.
- CCombo: with `SWT.VIRTUAL` its drop-down `List` is the owner-data viewport (no native item storage; rows drawn only while the drop-down is open), and `computeSize` caches the widest item until an item or the font changes. The popup shell and list handles are still created in the constructor: `visibleItemCount` is derived there from `list.getItemHeight()` and every layout calls `list.computeSize`, so creating them at first open needs a measurement path outside `List` (next leaf).
- Combo: native limitation (N10, N11); items stay native strings, the drop-down is not windowed and not lazy (next leaf).

## 5. Verification

- Differential trace (`bench/ListComboDifferential.java`): 5 List styles x (12 seeded random sequences of 120 calls + a scripted removal/insert matrix + a keyboard/mouse lane), 6 CCombo and 6 Combo sequences; every call prints its result or exception and a state snapshot (count, items, selection, caret, anchor, top, item height, computeSize).
- Lanes outside the equality gate, each with its own proof: `SWT.Selection` from native input and mouse clicks depend on keyboard focus and mouse capture, which a background JVM cannot hold (`state.eventsNonGate`, `nongate.*`; proven with focus by `bench/NotifyProbe.java`); the `defect.*` lane records two plain-list defects with `SWT.AUTO_TEXT_DIRECTION` that the `VIRTUAL` list does not reproduce (`BASELINE_DEFECTS.tsv`).
- JUnit before/after, the `VIRTUAL` variants of the List and CCombo classes, the viewport/scale class (200,000 rows; 10,000,000 with `-Dswt.list.scaleTest=true`), snippets and examples: see `evidence/tests-summary.md`.
- Visual capture mode: `Control.print` (WM_PRINT) of the list; the scroll round-trip test requires the printed image to contain the drawn rows (at least three colours) and an identical hash after scrolling away and back. Display `GC.copyArea` was not needed.

## 6. Measurements

`evidence/results-before-after.tsv` (one JVM per row, memory-gated, `bench/ListViewportBench.java`).

## 7. Residual differences

| id | where | difference | why |
|---|---|---|---|
| D1 | MULTI count change above 2^26 row-work (e.g. more than 6 rows at 10M) | top row can differ by a few rows from the row-by-row plain list | the plain list's final top depends on the scroll history of thousands of row messages; the rebuild restores caret, anchor and selection exactly (forced-rebuild differential lane `after-t0`) |
| D2 | SINGLE `setItemCount` growth above 64 rows on a non-empty list (new API) | caret follows the selection | one `LB_SETCOUNT`; a single-selection list box has no caret message (N19) |

## 8. GTK and Cocoa (design only, NOT_RUN on this host)

- GTK: `List` keeps a `GtkListStore` today. Design: `SWT.VIRTUAL` backs the `GtkTreeView` with a custom `GtkTreeModel` whose `get_value` reads `VirtualListModel` (the pattern of the fork's virtual `Table`), `iter_n_children` returns the count and `row-inserted`/`row-deleted` are emitted per change; selection owner `VirtualSelectionModel` mirrored to `GtkTreeSelection` for the visible window (GTK4: `GtkListView` over a `GListModel` with items-changed(position, removed, added); pattern only, LGPL).
- Cocoa: `List` already holds `String[] items` behind an `NSTableView` data source. Design: replace the array with `VirtualListModel`, answer `tableView:objectValueForTableColumn:row:` from it (SetData on first request) and keep the `NSIndexSet` selection in step with `VirtualSelectionModel` ranges.
- Both: the new `setItemCount`/`clear`/`clearAll` need these implementations before the API is visible on all platforms.

## 9. Next leaves

1. Combo `VIRTUAL`: custom drop-down (CCombo-style popup over the owner-data `List`, created at first open) with the edit field still the native `COMBOBOX` edit; closed-state keyboard selection answered from a Java store.
2. CCombo: create the popup at first open (item height and widest item measured outside `List`, proven equal per platform).
3. SetData negotiation: visible, then overscan, then idle prefetch, batched and cancellable, through the shared layer (`VirtualPageSource` once it lands).
4. Selection containers (run, array, bitmap, pattern) in `ViewportRuntime.Selection`, shared with Table/Tree.
5. JNI accelerator: not built. The only bulk native work left is `DrawText` per measured row during an exact `computeSize`; a bulk entry point must equal `DrawText(DT_CALCRECT|DT_SINGLELINE|DT_NOPREFIX)` and fail closed.
