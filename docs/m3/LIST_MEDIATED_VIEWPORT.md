# List (and Combo) as a mediated viewport (win32 first)

Status: design, pixel oracle and bar-ownership oracle. Native rules: see "Measured native rules".

## Goal

Every `List` behaves like a viewport over a logical model while keeping its public contract and visible behaviour.
The native LISTBOX is an ordinary string list box that holds only the rows on screen plus overscan; the logical
rows, their text, the widest item, the selection, the caret and the top row live in Java lanes.
The viewport is the default List mode (`org.eclipse.swt.internal.WidgetMode`):
`-Dswt.widget.mode.List=original` (or the global `swt.widget.mode`) selects the native List at startup.
`SWT.VIRTUAL` lists keep their existing owner-data path (LBS_NODATA, PR #129) unchanged.

A List is a Tree of depth 1 and a Table with one column and no header. `ListViewport` therefore has
`TreeViewport`'s structure, phases and method names (`install`, `layout`, `rebuild` = `captureResidentState`
→ plan → retire/insert → commit → bind → `restoreFocus`, `updateBar`, `positionPlane`, `onVScroll`, `onWheel`,
`onKeyDown`, `afterNative`, `beforeSetFocus`, `residentHandle`-style validation, selection-neutral caret restore,
pending caret).

## Which native mechanism keeps stock pixels (oracle `ListPlaneProbe`)

Three realizations of the same 300 rows, scrolled to row 100, rows 101, 103 and 104 selected, caret on 103,
win32 (Windows 11), JDK 21, 300 x 380 px each. Screen copies, compared pixel by pixel with the stock List:

| Realization | Unfocused | Focused |
|---|---|---|
| **resident window**: LISTBOX created without `WS_VSCROLL` holding rows `[92, 133)`, native top index 8, the logical bar `0..299, page 25, pos 100` set on the same window by the caller | **0 differing pixels** | **0 differing pixels** |
| owner data: the existing `SWT.VIRTUAL` List (`LBS_NODATA \| LBS_OWNERDRAWFIXED`, rows drawn by SWT) | 2209 | 2097 |

(Evidence: `list-viewport/evidence/ListPlaneProbe.java.txt`, `ListPlaneProbe.out.txt`, and the two PNGs.
`Control.print` is not a usable oracle for a list box: printing the *stock* List returned an empty client area
in one of three runs, so the comparisons use screen copies.)

**Bar ownership.** A list box created without `WS_VSCROLL` never writes the vertical bar, even after the caller's
`SetScrollInfo` has shown it (which sets `WS_VSCROLL`). Measured after `LB_ADDSTRING`, `LB_INSERTSTRING`,
`LB_DELETESTRING`, `LB_SETTOPINDEX`, `LB_SETCARETINDEX`, resize, `WM_SETFONT`, `WM_KEYDOWN` Down and Next,
`WM_VSCROLL`, `WM_MOUSEWHEEL`, `LB_RESETCONTENT` and `SWP_FRAMECHANGED`: the bar stayed exactly as set (13/13).

**Decision.** The list box itself is the viewport. Unlike Tree (whose TreeView cannot keep a caller-owned bar
while scrolling natively, so it needs `hwndParent` and a content plane), the List needs no extra window:
- the LISTBOX is created without `WS_VSCROLL` (the horizontal bar stays native, driven by `LB_SETHORIZONTALEXTENT`
  from the logical widest item);
- `updateBar` sets the logical `SCROLLINFO` on the list box: range = logical rows, page = fully visible rows,
  position = logical top row;
- `positionPlane` is `LB_SETTOPINDEX (top - first)`: the list box scrolls its resident window natively, with its
  own blit and paint, which is why pixels are identical;
- `SWT.VIRTUAL` owner data is rejected for this mode (pixels differ).

## Design

1. **Logical model** (common, arithmetic, no per-row objects besides the item `String`):
   - `VirtualListModel` (PR #116): text lane, insert/remove shifting, widest-item lane (`VirtualListWidth`) and
     the packed range/bitset selection (`VirtualSelectionModel`, sorted ranges + `int` words).
   - `VirtualListViewport` (new, common): the flat core of `VirtualTreeViewport` — `VirtualScrollMetrics` +
     `ViewportRuntime.RowWindow` with the same method names (`configureGeometry`, `refreshLogicalRange`,
     `setTopRow`, `topRow`, `visibleRowCount`, `visibleRows`, `scrollbar*`, `firstPaintRow`, `paintRowCount`).
     A Table viewport can use it unchanged.
   - Caret and anchor are two `int`s.
2. **Resident window** (`VirtualRowWindowPlan`, new, common): the depth-1 case of `VirtualTreeResidentPlan`.
   A LISTBOX row has no handle; it is addressed by its native index, and the window is one interval of logical
   rows at native indices `0..count-1`. Moving from `[a0, a1)` to `[b0, b1)` deletes at most the head and tail
   that left and inserts at most the head and tail that entered (interval arithmetic, O(1) planning, no
   allocation); disjoint windows are replaced. Native operations per move are bounded by the window.
3. **Viewport layout**: the list box is the viewport (see the oracle). The native top index of the resident
   window is `top - first`.
4. **Scroll input is mediated**: `WM_VSCROLL`, `WM_MOUSEWHEEL`, Page Up, Page Down, Home, End, `setTopIndex`,
   `showSelection` and `setSelection` set the logical top with the native rules (below). Up, Down, clicks and
   drag selection stay native inside the window; afterwards the native top and caret are mapped back to logical
   rows (the window is contiguous, so `logical = first + native`), and the window follows.
5. **Selection and caret**: resident rows keep their selection natively (programmatic `LB_SETSEL` /
   `LB_SETCURSEL` send no `LBN_SELCHANGE`); `captureResidentState` copies them into the lanes only after user
   input reached the native control. Off-window rows keep their selection in the lanes. A caret outside the
   window is pending; the native caret is parked on a non-painted overscan row and restored, selection-neutral,
   when the caret row becomes resident.
6. **Type-ahead** (`WM_CHAR`) searches the logical rows with the native rule, not only the resident ones.
7. **Threading**: the UI thread does only window-bounded native work; whole-list work (setItems copy, widest
   width of new items) is the work the stock List does natively too and is reported as such.

## Measured native rules (oracle `ListRulesOracle`)

Pending: top-index clamp, insert/remove before the top, caret and anchor on insert/remove, `setSelection` and
`showSelection` scrolling, scroll codes, wheel lines, type-ahead, `LBN_SELCHANGE` versus programmatic changes.

## Leaves

| Leaf | Scope | Acceptance |
|---|---|---|
| L1 | win32 plain List: model, window plan, bar, programmatic API mediated | recorded differential (stock JVM vs viewport JVM, answers, bar, selection, focus, events, pixels); 1,000,000 items with at most one window of native rows |
| L2 | user input: keyboard (Page, Home, End, off-window Up/Down), wheel, scroll bar, type-ahead, MULTI click/shift ranges with an off-window anchor | scripted input parity vs stock |
| L3 | Combo drop-down list (see "Combo") | audit + design first |
| L4 | GTK and Cocoa List | compile + hosted evidence |

## Combo

Pending audit.

## Donors

See [`list-viewport/FOSS_REUSE_DECISION.tsv`](list-viewport/FOSS_REUSE_DECISION.tsv).
