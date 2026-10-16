# List (and Combo) as a mediated viewport (win32 first)

Status: L1 (API) and L2 (input) implemented on win32, viewport by default; the recorded differential against the official SWT 3.135.0 List and the acceptance tests pass. See "Status".

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
| Ctrl and Shift (extended, `ListReplayProbe` on SWT 3.135.0) | Shift+key and Ctrl+Shift+key: the rows from the anchor to the new caret replace the selection. Ctrl+key: SWT's own handler moves only the caret (`LB_SETCARETINDEX`). Ctrl+Space: SWT toggles the caret row; selecting moves the anchor, deselecting does not (SWT's `LB_SETANCHORINDEX` there is `0xf19c`, which the list box ignores). Shift+click: anchor .. clicked replaces the selection. Ctrl+Shift+click: the last range (anchor .. the last clicked or `LB_SETSEL` row) is deselected, then anchor .. clicked takes the anchor row's former state, other rows keep theirs; without an anchor the selection is cleared and the clicked row becomes the anchor. A single-selection list ignores Shift and Ctrl for navigation. A SIMPLE list (`LBS_MULTIPLESEL`) moves only the caret with keys and type-ahead and still sends `LBN_SELCHANGE`. |
| Wheel remainder (`ListWheelOracle`) | one remainder per thread, shared by every list box: -60 on one list and -60 on another scroll the second by 3; reset when the rotation turns and when the focus moves. |
| Click on the empty area below the last row | the list box selects a row from its internal state (the last `LB_SETSEL` row), not the clicked point: not reproduced when that state comes from rows the window did not hold (gap). |

## Input (L2): native first

User input reaches the list box itself whenever the rows it looks at fit in a window: before a key, Space or click,
`prepareNative` grows the resident window (up to 2000 rows) to hold the visible rows, the caret, the anchor, the
selected row of a single-selection list and the target, and puts the native caret, anchor (`clearNativeAnchor` deletes
and re-inserts the first row, which is the only way to give the list box no anchor) and selection on the logical ones.
The list box then applies its own rules; `afterNative` copies the result back and the window shrinks again. When those
rows are farther apart, the measured rules above run over logical rows. A press grows the window by 500 rows each way
and no window move renumbers native rows while the mouse is captured, so drag selection and its auto-scroll stay native.
Selection API calls send the original List's message (`LB_SETSEL`, `LB_SETCURSEL`, `LB_SELITEMRANGEEX`,
`LB_SETCARETINDEX`) to resident rows, so the list box's internal state evolves as in the original.

## Status (win32, Windows 11, JDK 21, build `lv15`, hidden desktop)

| Gate | Result |
|---|---|
| Hook recipe (`list-viewport/recipe`, 43 hooks) | pass 1 CHANGED, pass 2 UNCHANGED |
| win32 compile (`compile_platform.sh`) | 0 errors |
| `Test_org_eclipse_swt_widgets_List_ViewportDifferential` on the shared harness: reference recorded with the **official SWT 3.135.0 jar**; 3 styles (SINGLE, MULTI, MULTI \| SIMPLE) x 10 seeds x 150 steps of API calls and input as window messages (keys with Shift/Ctrl, characters, clicks, double clicks, drags with auto-scroll, scroll-bar codes, wheel); answer, both bars, selection, caret, events every step; `WM_PRINT` pixels per seed | **3/3 pass** (30 seeds, 4,500 steps) |
| `Test_org_eclipse_swt_widgets_List_ViewportAcceptance` (5: 1,000,000 rows SINGLE and MULTI; scroll round trip; scroll codes, wheel, keys, type-ahead; MSAA over 100,000 rows; empty lists) | **5/5 pass** |
| `Test_VirtualRowWindowPlan` | **3/3 pass** |
| Same run with the upstream List suites (`List`, `List_Viewport`, `List_Virtual`, `VirtualListModel`) | **278 pass, 0 fail**, 2 skipped by assumption (foreground alpha, win32) |

History: the first differential (build `lv1`, fork original mode, visible desktop) failed at the freed bar after
`removeAll` (`ListBarOracle2`); later failures against SWT 3.135.0 were Ctrl+Shift range rules, the native anchor after
deletes, SIMPLE notifications and a wheel remainder shared between tests (each style now runs in its own JVM).
`List_Viewport.scrollRoundTripReproducesStateAndPixels` (`SWT.VIRTUAL`, not mediated here) is flaky with its
print-based hash on the visible desktop: three reruns each, original mode 2 failures, viewport mode 0.

Aspect matrix: [`list-viewport/ASPECT_MATRIX.tsv`](list-viewport/ASPECT_MATRIX.tsv) (generated by
`list-viewport/aspect_matrix.py`): 46 covered, 7 upstream suites only (incl. the fork-only `setItemCount` and `clear`), 6 gaps.

## Leaves

| Leaf | Scope | Acceptance |
|---|---|---|
| L1 | win32 plain List: model, window plan, bar, programmatic API mediated | recorded differential (stock JVM vs viewport JVM, answers, bar, selection, focus, events, pixels); 1,000,000 items with at most one window of native rows |
| L2 | user input: keys with Shift/Ctrl, Space, type-ahead, clicks with Shift/Ctrl, double click, drag auto-scroll, wheel, scroll bar | done: recorded differential against SWT 3.135.0 with input as window messages |
| L3 | Combo drop-down list (see "Combo") | audit + design first |
| L4 | GTK and Cocoa List | compile + hosted evidence |

## Combo (audit and design; not implemented)

### Audit of win32 `Combo`

- The native control is a `COMBOBOX` (`CBS_DROPDOWN`, `CBS_DROPDOWNLIST` for `READ_ONLY`, `CBS_SIMPLE`) with
  `CBS_AUTOHSCROLL | CBS_NOINTEGRALHEIGHT | WS_HSCROLL | WS_VSCROLL`. Its rows live in the combo's own list box
  (`ComboLBox`, reached with `GetComboBoxInfo`; a child `CBID_LIST` for `SIMPLE`). The edit field (or the static area
  of `READ_ONLY`) shows the selected row's text.
- Every item API is a `CB_*` message on all rows: `add`, `remove (index, notify)`, `remove (start, end)`, `removeAll`,
  `setItems`, `getItem`, `getItemCount`, `getItems`, `indexOf` (`CB_FINDSTRINGEXACT`), `select`, `deselect`,
  `deselectAll`, `getSelectionIndex` (`CB_GETCURSEL` / `CB_SETCURSEL`), `computeSize` and `setScrollWidth` (measure every
  row; `CB_SETDROPPEDWIDTH`, `CB_SETHORIZONTALEXTENT`), `applyListSegments` (bidi segments re-insert every row),
  `windowProc` (segment interception of `CB_ADDSTRING`, `CB_INSERTSTRING`, `CB_FINDSTRINGEXACT`), `wmCommandChild`
  (`CBN_SELCHANGE` → Selection and text sync), `getListVisible` / `setListVisible` (`CB_GETDROPPEDSTATE`,
  `CB_SHOWDROPDOWN`), `setVisibleItemCount` (`CB_SETMINVISIBLE`, `updateDropDownHeight`). About 80 call sites.
- Native behaviour over all rows that a resident window would cut short: closed-combo keys (Up, Down, Page, Home,
  End change the selection without dropping), first-letter type-ahead of `READ_ONLY`, the wheel on a closed combo,
  the drop-down's initial top row (the selected row), and for `CBS_DROPDOWN` the edit-text match when the list drops.
- The fork has no Combo viewport today: PR #129's owner-data path covers `List` and the custom `CCombo` only.

### Design

A Combo is a List in a drop-down: `ComboViewport` reuses `ListViewport`'s native core on the `ComboLBox`, which
is an ordinary list box (`LB_*` messages):

1. **Model and window** are the List's: `VirtualListModel` (text, widest row, the selection as one row),
   `VirtualRowWindowPlan`. The resident window follows the selection while the combo is closed (so the edit text and
   `CB_GETCURSEL` are native) and the list's top row while it is dropped.
2. **Bar** of the dropped list. Measured (`ComboPlaneProbe`, two runs, `evidence/ComboPlaneProbe.out.txt`): the
   combo creates its `ComboLBox` with `WS_VSCROLL` (style `0x44a08141`), and the list box keeps writing its own bar
   even after the style is removed before the first drop (`LB_SETTOPINDEX` moved it from 8 to 9 of the resident
   0..35). So the List's creation-time mechanism does not transfer. Instead the viewport subclasses the
   `ComboLBox` and writes the logical bar after each of its messages (DWM composes the frame after both writes).
   With that, the dropped list of 36 resident rows matches the stock 300-row drop-down (top row 150, bar
   0..299 page 20 position 150) except **92 pixels inside the selected row's text** (bbox 3,4..43,12): the
   highlighted text is drawn differently; the cause is not isolated yet (C1). The closed combos are identical (0 pixels).
3. **Index space**: every `CB_*` index crossing the native boundary is `logical - first`; `CBN_SELCHANGE` is mapped
   back before SWT's Selection is sent.
4. **Mediated input**: Home, End, Page keys and the wheel on a closed combo, first-letter type-ahead and the
   drop-down top row run over logical rows (the List rules above); Up and Down stay native inside the window.
5. **Drop height**: `CB_SETMINVISIBLE` sees at least `min (count, visibleItemCount)` resident rows, so the dropped height
   is the stock one.

Leaves: C1 oracle (bar ownership and pixels of the dropped list); C2 `READ_ONLY` combo; C3 `CBS_DROPDOWN` edit
matching; C4 `SIMPLE`; each with the List's recorded differential (original JVM vs viewport JVM).

## Donors

See [`list-viewport/FOSS_REUSE_DECISION.tsv`](list-viewport/FOSS_REUSE_DECISION.tsv).
