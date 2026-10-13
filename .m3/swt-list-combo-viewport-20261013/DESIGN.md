<!-- SPDX-License-Identifier: EPL-2.0 -->
# List, Combo, CCombo: native owner-data viewport (win32)

Leaf of the 2026-10-13 directive "all widgets in SWT, Nebula, React and Qt, implemented with native controls and a viewport", contract atoms L4 (win32 native window) of `LIST_VIEWPORT_CONTRACT.md` (Synexia #10811/#10824), on top of L1/L2 (`VirtualListModel`, `VirtualListWidth`, SWT #116).

## 1. Switch and contract

- `SWT.VIRTUAL` (bit 28, unused by List, Combo and CCombo) turns the viewport on. A List or CCombo without it runs the unchanged code path. No new style bit.
- Public API results of a `VIRTUAL` list are the plain list's results for the same calls. The proof is a differential trace (section 5): the same seeded call sequences run on the unmodified fork build and on this build; both traces are compared with `diff`.
- Additions are additive: `List.setItemCount(int)`, `List.clear(int)`, `List.clearAll()` (`@since 3.137`), and `SWT.SetData` for `VIRTUAL` lists, mirroring `Table`. They are implemented on win32; gtk and cocoa are design only (section 7).

## 2. Ownership (one control, no wrapper)

| state | owner | representation |
|---|---|---|
| item count | native list box (`LBS_NODATA`, `LB_SETCOUNT`) and `VirtualListModel` | one `int` each |
| item text | `VirtualListModel.items` | one `String` reference per item; `null` = not materialized (SetData pending) |
| widest item | `VirtualListWidth` lazy lane | not allocated until a row is measured; then one `int` per item, `UNMEASURED` until asked |
| selection, caret, anchor, top index | native list box | 0 bytes per row for SINGLE; the list box's own per-row selection state for MULTI (selection must stay native for native mouse and keyboard multi-selection) |
| painting | `List.wmDrawChild` (`WM_DRAWITEM`) | visible rows only; one reusable `DRAWITEMSTRUCT`, `RECT` and `char[]` (doubling growth, step capped at 65536 chars) |

`VirtualSelectionModel` (packed int ranges) stays the selection algebra of `VirtualListModel`; on win32 the native list box keeps the interactive selection, so no second selection owner is added. The gtk/cocoa design (section 7) makes `VirtualSelectionModel` the owner because those toolkits have no count-only native list.

## 3. Native facts that shaped the code

All measured on this host before the code was written (`NATIVE_LIMITS.tsv`, `bench/NativeListProbe.java`, `bench/NativeInsertProbe.java`).

- `LB_SETCOUNT(10,000,000)` works for single, extended and multiple selection (N1). Find-string and item text are unavailable (N2), so text, `indexOf`, keyboard search and accessibility names come from the Java store.
- Owner-draw fixed lists keep their first item height (16 px vs the plain 15 px = `tmHeight`, N3): `LB_SETITEMHEIGHT(tmHeight)` at creation and on every `WM_SETFONT`.
- Single-selection `LBS_NODATA` list boxes lose their count when `LB_INSERTSTRING` follows `LB_SETCOUNT` (N7, a native defect). SINGLE therefore changes the count only with `LB_SETCOUNT` and restores the current selection and top index.
- Multi-selection list boxes keep a selection state per row; one `LB_DELETESTRING` at row 0 of 10M rows costs 69-145 ms (N5). Changes of more than `removeRebuildThreshold` (64) rows use one `LB_SETCOUNT` and restore selection runs (`LB_SELITEMRANGEEX`), caret, anchor and top index; smaller ones use row messages in the plain list's order.
- The plain list box searches by first character after the caret, wrapping, ignoring case (N8). The `VIRTUAL` list does the same over materialized rows; like `Table` answering `LVN_ODFINDITEM` with -1, a key never triggers `SetData`.
- `COMBOBOX` has no count-only mode: its drop-down list box never gets `LBS_NODATA` (0x2000 is `CBS_UPPERCASE`) and refuses `LB_SETCOUNT`, also after `SetWindowLong` (N10, N11).

New OS constants (Java constants only, no new natives, the fork's existing DLL is unchanged): `LBS_NODATA`, `LBS_OWNERDRAWFIXED`, `LB_SETCOUNT`, `LB_SETITEMHEIGHT`, `LB_GETANCHORINDEX`, `ODS_DISABLED`, `ODS_FOCUS`, `ODS_NOFOCUSRECT`, `ETO_OPAQUE`, `ETO_RTLREADING`. `OS.LB_SETANCHORINDEX` is 0xf19c (documented 0x19c, N12); the plain path keeps it, the rebuild path uses 0x19c.

## 4. Laziness and the visibility law

- Rows are materialized (`SWT.SetData`) only when drawn (`WM_DRAWITEM`, which the OS sends only for visible rows of a visible window) or when an API call names them (`getItem`, `getItems`, `indexOf`, `getSelection`, accessibility `getName`). A materialized row is not requested again; `clear`/`clearAll` forget text.
- Widths are measured only for painted rows of an `H_SCROLL` list and for exact size queries (`computeSize`); nothing is measured while the list is hidden. The native horizontal extent therefore covers measured rows (as `Table` does for `VIRTUAL`); `computeSize` measures every materialized row once and is exact.
- The effective-visibility bit being added to `Control` by another session is not needed here: every deferred cost is driven by native paint or by a caller's request.
- CCombo: with `SWT.VIRTUAL` its drop-down `List` is the owner-data viewport (no native item storage, rows drawn only while the drop-down is open) and `computeSize` caches the widest item until an item or the font changes. The popup shell and list handles are still created in the constructor, because `visibleItemCount` is derived from `list.getItemHeight()` there; creating them at first open is the next leaf (section 8).
- Combo: native limitation (N10, N11); items stay native strings. A count-only Combo needs a custom drop-down (section 8).

## 5. Verification

See `evidence/` and the coverage table `LIST_COMBO_API_COVERAGE.tsv` (one row per public/protected member of List, Combo, CCombo and per behaviour lane).

## 6. Measurements

`evidence/results-before-after.tsv`, produced by `bench/ListViewportBench.java` (one JVM per row, memory-gated).

## 7. GTK and Cocoa (design only, NOT_RUN on this host)

- GTK: `List` keeps a `GtkListStore` today. Design: `SWT.VIRTUAL` backs the `GtkTreeView` with a custom `GtkTreeModel` whose `get_value` reads `VirtualListModel` (the pattern of the fork's virtual `Table`), `iter_n_children` returns the count, and `row-inserted`/`row-deleted` are emitted per change; selection owner `VirtualSelectionModel` mirrored to `GtkTreeSelection` for the visible window only (GTK4: `GtkListView` over a `GListModel`, items-changed(position, removed, added), pattern only, LGPL).
- Cocoa: `List` already holds `String[] items` and an `NSTableView` data source; design: replace the array with `VirtualListModel`, answer `tableView:objectValueForTableColumn:row:` from it (SetData on first request), and keep `NSIndexSet` selection in step with `VirtualSelectionModel` ranges.
- Both: the new `setItemCount`/`clear`/`clearAll` need these implementations before the API is visible on all platforms.

## 8. Next leaves

1. CCombo: create the popup at first open (item height from the text font metrics, proven equal to `List.getItemHeight()` per platform), keep items in a `VirtualListModel`-shaped store until then.
2. Combo `VIRTUAL`: custom drop-down (CCombo-style popup over the owner-data `List`) while the edit field stays the native `COMBOBOX` edit; keyboard selection in the closed state answered from the Java store.
3. JNI accelerator: not built. The only bulk native work left is `DrawText` per measured row during an exact `computeSize`; a bulk measure entry point must be proven equal to `DrawText(DT_CALCRECT|DT_SINGLELINE|DT_NOPREFIX)` and fail closed.
4. Async paging: consume `VirtualPageSource` (another session) once it lands.
