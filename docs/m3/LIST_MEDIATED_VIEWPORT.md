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

## Measured native rules (oracles `ListRulesOracle`, `ListRulesOracle2`, `ListBarOracle`)

Stock List, 300 x 380 px (client 376 px, item height 15, so **page = floor (client / item height) = 25**),
SINGLE and MULTI (`LBS_EXTENDEDSEL`) unless stated. Raw outputs: `list-viewport/evidence/*.out.txt`.

| Rule | Measured |
|---|---|
| Top-row clamp | `top ∈ [0, count - max (1, page)]`; count ≤ page gives 0. Page 0 (client smaller than a row, or 0 px) keeps one row: max top = count - 1. A font of item height 30 gives page 12 and max top 288 of 300. |
| `setTopIndex` out of range | clamped to `[0, count - 1]` by SWT, then by the native clamp; empty list stays 0. |
| Resize | growing a list scrolled to the end pulls the top back to the new maximum (275 → 265 at page 35); shrinking keeps it. |
| Vertical bar | `nMin 0, nMax count - 1, nPage min (count, page), nPos top`; shown iff `count > page` (page 0: shown for any row). After `LB_RESETCONTENT` (removeAll, empty) `GetScrollInfo` fails, so `ScrollBar` answers 0. |
| Insert (`LB_INSERTSTRING`) | selection shifts with the rows; **caret, anchor and top-row index do not** (insert at 50 with top 100, caret 110: top 100, caret 110, selection 111). |
| Delete (`LB_DELETESTRING` at `i`) | selection shifts; caret: `i == caret && caret > 0` → caret - 1, then clamped to `count - 1`; rows before the caret do not move it; anchor: `i <= anchor` → -1, otherwise kept. SWT's `remove (start, end)` deletes at `start` repeatedly, `remove (int[])` in descending order, each with this rule. |
| `removeAll`, `setItems` | top 0, caret 0, selection empty, **anchor kept**. |
| SWT remove top row | `remove (i)`: `i < top` → top - 1; `remove (s, e)`: `e < top` → top - (e - s + 1), else unchanged; `remove (int[])`: top - (removed rows below top); then the native clamp. |
| `LB_SETCURSEL` (SINGLE) | caret = selection = index, scrolled just into view (`index < top` → top = index; `index ≥ top + page` → top = index - page + 1); anchor unchanged. |
| `LB_SETSEL (1, i)` (MULTI) | selects `i` and sets the anchor to `i`; SWT puts the caret back. `LB_SETSEL (0, i)`, `LB_SELITEMRANGEEX` and select-all keep caret and anchor. |
| `LB_SETCARETINDEX` (MULTI) | caret = index, scrolled just into view; ignored by a SINGLE list box that has a selection. |
| Degenerate page 0 | SINGLE scrolls to `index + 1`; MULTI to `index` when the client is 1..14 px and to `index + 1` when it is 0 px. |
| `showSelection` | SWT rule: nothing when the first selected row is visible, else top = `min (max (index - page / 2, 0), count - 1)`, then clamped (125 → 113, 299 → 275). |
| `WM_VSCROLL` | line ±1, page ±(page - 1), top / bottom, thumb = track position; each sends one ScrollBar Selection; end-scroll none. |
| Wheel (3 lines) | ±120 → ∓3 rows; -60 → 0, -60 -60 → 3; -40 × 3 → 3; -360 → 9; -240 then +120 → 6 (the reversing delta is ignored). The viewport lets the list box compute it inside the window. |
| Keyboard (from caret 110, top 100) | Down / Up ±1 just into view; Next: caret = top + page - 1 if the caret is above it, else caret + page - 1, top follows (110 → caret 134, top 110); Prior symmetric; Home 0; End count - 1 with top at the maximum; anchor = caret; Space keeps the selection. |
| Type-ahead | **first character only**, no prefix accumulation: each `WM_CHAR` moves to the next row after the caret (wrapping) whose text starts with that character, case-insensitively, and selects it like a keyboard move; no match keeps everything (`b`,`b` → banana, blueberry; `b`,`l`,`a` → banana, banana, avocado; `c` × 3 cycles cherry, cantaloupe, cherry). |
| Mouse (MULTI) | a click without Ctrl replaces the whole selection, rows outside the screen included; modifiers come from `GetKeyState`, not from `wParam`. |
| Events | programmatic API changes send no Selection (16 calls, 0 events); keyboard and mouse send `LBN_SELCHANGE` → Selection (posted, delivered after the next dispatched message). |

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
