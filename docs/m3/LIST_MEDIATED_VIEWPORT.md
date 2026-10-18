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

### Lanes and facades (DOOP, brief rule 18)

| Lane | Representation | Owner |
|---|---|---|
| Item text | one `String []` dynamic array (insert/remove shift), the only per-row object, as the API hands strings in and out | `VirtualListModel` |
| Widest item | `int` widths with a max lane (`VirtualListWidth`) | `VirtualListModel` |
| Selection | sorted `int` ranges plus `int` bit words (`VirtualSelectionModel`) | `VirtualListModel` |
| Top, caret, anchor, range end, parked caret, native anchor | `int` scalars | `ListViewport`, `ComboViewport` |
| Dirty state (native input, selection, focus), bar state, bar paint | `boolean` / `int` flags | `ListViewport`, `ComboViewport` |
| Resident window | `first`, `size` and the planned retire/insert counts, all `int` | `VirtualRowWindowPlan` |
| Scratch for layout and measuring | one `RECT`, one `SCROLLINFO` (and the combo's measuring `char []`) per viewport, reused, so scrolling and building allocate nothing per row | `ListViewport`, `ComboViewport` |

Facades: a List has no item objects; a native list-box row is addressed by its index and holds a copy of one window
row only. `getItems`, `getSelection` and `getSelectionIndices` build their arrays when the caller asks, as stock does.

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
### Resize, font and DPI change while scrolled (item 4)

- `resizeAndFontWhileScrolledAnswerLikeTheReference` (official SWT 3.135.0 reference, MULTI, 10 seeds x 150 steps):
  random `setSize`, `setFont` (7, 9, 12, 16 pt), `setTopIndex`, scroll codes, wheel, `select`, `showSelection`, keys,
  `add`, `remove`; top row, client area, item height, both bars, selection, caret, events every step, pixels per seed:
  **pass** (build `cv15`).
- `zoomWhileScrolledAnswersLikeTheReference` (`-Dswt.autoScale.updateOnRuntime=true` in both runs, 10 seeds x 24
  steps): scroll and select, `WM_DPICHANGED` to 120 or 144 dpi on the shell, scroll, resize, keys, add, back to 96 dpi;
  the test dispatches until the display is idle after each change, because SWT rescales the children in runnables it
  posts and a newer change cancels an unfinished one (random back-to-back DPI changes made the recorded run depend on
  timing, in the official jar as in the fork's original mode). **pass** (build `cv15`).
- Build `lv17` (2026-10-18, coverage run): every step line matched until the run stopped on one seed's pixels
  (resize seed 7, zoom seed 2), so the later seeds did not run. Every differing pixel is the selection highlight, (0, 120, 215) in the recordings of 2026-10-17 07:56
  and (0, 120, 212) now; the List recordings made later that day already have (0, 120, 212). The system highlight
  colour changed between the two recordings, so both must be recorded again with the official jar before they are
  compared.
- `ListZoomProbe` (`evidence/ListZoomProbe.java.txt`): one DPI change while scrolled answers alike in the official
  jar, the original mode and the viewport mode (item height 15 -> 20 px, page 25 -> 18, extent 94 -> 126, top kept).

### Build loops (item 5): deferred bar painting

`ListAddBench` (`evidence/ListAddBench.java.txt`, `.out.txt`): 30,000 `add (String)` and 3,000 `add (String, 0)` on a
visible 300 x 380 list, hidden desktop, best of three. Before, the viewport wrote and painted the bar on every add,
read the horizontal extent back on every add and restored the caret even for rows added after the window.

| Build | SINGLE add | SINGLE add at 0 | MULTI \| H_SCROLL add | MULTI \| H_SCROLL add at 0 |
|---|---|---|---|---|
| official SWT 3.135.0 (two runs) | 8830 / 7897 ms | 913 / 691 ms | 8072 / 10559 ms | 754 / 1155 ms |
| viewport before (`cv8`) | 6626 ms | 773 ms | 9975 ms | 1066 ms |
| viewport after (`cv9`) | **1780 ms** | **148 ms** | **5161 ms** | **606 ms** |

After: inside `add` and `remove` the bar's values are written at once (`SetScrollInfo` without redraw: the API and
the list box read them, and a bar that appears or disappears still repaints at once), and the frame is repainted once
when the event loop runs after the burst (`RedrawWindow (RDW_FRAME)`); the extent written to the list box is cached;
a row added after the window leaves the native caret and anchor alone. The recorded differential (3 styles) passes
unchanged on `cv9`, pixels included. The remaining cost of the H_SCROLL list is the per-row `DrawText` the stock list
pays too.

### VIRTUAL round trip (item 6)

`List_Viewport.scrollRoundTripReproducesStateAndPixels` (`SWT.VIRTUAL`, not mediated here) failed 2 of 3 runs in the
original mode on the visible desktop. Both failures (`out/run-lv5-rt-original-1.log`, `-3.log`) have identical API
state before and after the round trip and differ only in the pixel hash, and the "after" hash is the same
(`1dc4dac1`) for the SINGLE list (row 1003 selected) and the MULTI list (rows 1002, 1005, 1006): the capture did not
depend on the list's rows at all. The test captured with `Control.print`, which on win32 is `PrintWindow` with
`PW_RENDERFULLCONTENT`, a copy of the window's composed DWM surface; right after `setTopIndex` + `update` that
surface can still hold an earlier frame. On the hidden desktop (no DWM redirection) the unchanged test passed 6 of 6
runs in the original mode. Fix (test only, the code was right): the hash is taken from a synchronous `WM_PRINT`
(`ViewportDifferential.render`, the list box paints its rows into the image while the message runs), as every other
viewport capture does.

Aspect matrix: [`list-viewport/ASPECT_MATRIX.tsv`](list-viewport/ASPECT_MATRIX.tsv) (generated by
`list-viewport/aspect_matrix.py`): 46 covered, 7 upstream suites only (incl. the fork-only `setItemCount` and `clear`), 6 gaps.

## Leaves

| Leaf | Scope | Acceptance |
|---|---|---|
| L1 | win32 plain List: model, window plan, bar, programmatic API mediated | recorded differential (stock JVM vs viewport JVM, answers, bar, selection, focus, events, pixels); 1,000,000 items with at most one window of native rows |
| L2 | user input: keys with Shift/Ctrl, Space, type-ahead, clicks with Shift/Ctrl, double click, drag auto-scroll, wheel, scroll bar | done: recorded differential against SWT 3.135.0 with input as window messages |
| L3 | Combo drop-down list (see "Combo"): C1 pixels, C2 `READ_ONLY` and editable combos | C2 implemented: recorded differential against SWT 3.135.0 (see "Combo") |
| L4 | GTK and Cocoa List | compile + hosted evidence |

## Combo (win32: drop-down combos mediated, `SWT.SIMPLE` native)

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
  `CB_SHOWDROPDOWN`), `setVisibleItemCount` (`updateDropDownHeight`). About 80 call sites.
- The fork had no Combo viewport: PR #129's owner-data path covers `List` and the custom `CCombo` only.

### C1: pixels of a resident dropped list (`ComboPrintProbe`, hidden desktop, `WM_PRINT`, official SWT 3.135.0)

A `READ_ONLY` combo with 300 rows (row 150 selected, 20 visible) against one holding only rows `[142, 178)` with native
row 8 selected, both dropped, captured with `WM_PRINT` (`evidence/ComboPrintProbe.out.txt`, `combo-print-*.png`):

| Comparison | Differing pixels |
|---|---|
| closed combos | 0 |
| stock dropped list printed twice | 0 |
| dropped lists, after the resident list got the stock top row (`LB_SETTOPINDEX`) and the stock `SCROLLINFO` | **0** |
| dropped lists, native bar of the 36 resident rows | 264 (the thumb, x 290..291) |

The 92 pixels of the earlier screen copies on the visible desktop (`ComboPlaneProbe`, bbox 3,4..43,12 in the selected
row's text) are not a rendering difference: all 92 differ by exactly one level in one channel (60 in blue, 27 in green,
5 in both), on ClearType glyph edges that are coloured in both images; the highlight colour (0,120,215, 4021 pixels) and
the background are identical. They come from the screen composition of the drop-down popup, not from the list box:
`WM_PRINT` of the same two lists is identical. No item state, focus rectangle or selection colour source differs.
The fix is the measurement: every Combo comparison uses `WM_PRINT` on the hidden desktop.

### Measured native rules (`ComboRulesOracle`, `ComboMessageOracle`, `ComboTrackProbe`, `ComboFocus`; official SWT 3.135.0)

| Rule | Measured |
|---|---|
| Closed keys | The combo passes Up, Down (and Left, Right when read-only), Prior, Next, Home, End and the wheel (as Up/Down) to its list box: ±1, ±(list box page - 1), first, last row; from no selection a key starts at row 0 (read-only Next → 3, editable Next → 4: the read-only list box has no caret, `LB_CARETON` took it away when the combo got the focus). An editable combo's Home, End, Left and Right move in the edit field. Each change: `CBN_SELENDOK`, `CBN_SELCHANGE` → Modify, Selection. |
| Type-ahead (read-only) | first character only, case-insensitive, the next row after the selection, wrapping; no prefix accumulation (`aa` steps twice); no match changes nothing. |
| Drop | `CBN_DROPDOWN` first; then a read-only list puts the selected row on top (`LB_SETTOPINDEX`), clamped to `count - page` (row 0 without a selection); an editable list searches its edit text (`LB_FINDSTRINGEXACT`, then `LB_FINDSTRING`: first row starting with the text, ignoring case), selects it, puts it on top and copies its text into the edit field (no match: no selection, row 0 on top). |
| Dropped keys | ±1, ±(page - 1), first, last; the new row goes **on top** (clamped); `CBN_SELCHANGE` each time. |
| Dropped wheel, scroll bar | 3 rows per notch; line ±1, page ±(page - 1), top, bottom, thumb to the row (clamped to `count - page`); the selection stays. |
| Hover, click | hovering selects the row under the pointer (`CB_GETCURSEL` answers it) without `CBN_SELCHANGE`; a click ends the drop: `CBN_SELENDOK`, `LBCB_ENDTRACK (1)`, `CBN_CLOSEUP`, `CBN_SELCHANGE`. |
| Escape, Enter | Escape: `CBN_SELENDCANCEL`, `LBCB_ENDTRACK (0)`, which restores the list box's recorded entry. Enter keeps the selection. Keyboard moves inside the drop are kept by Escape (they were committed). |
| Recorded entry (`ComboTrackProbe`) | `LBCB_STARTTRACK` records the selected entry, every `LB_SETCURSEL` of a row records it (`LB_SETCURSEL (-1)`, as typing sends it, does not; `ComboReplayProbe`), `LB_RESETCONTENT` forgets it; inserts and deletes do **not** move it (a delete before it makes Escape restore the next row). A commit sends `CBN_SELCHANGE` when the selection is not the recorded entry. |
| Delete (`ComboReplayProbe`) | `LB_DELETESTRING` keeps the top row index and then scrolls the caret into view (caret 161, top 34, a row below the page deleted: top 142); an insert does not scroll. Stock `Combo.remove` does not set the top row back (stock `List.remove` does). |
| Caret (`ComboFocus`) | `LB_SETCARETINDEX` fails while a row is selected; `LB_CARETON` (sent on focus) takes the caret away when nothing is selected; deletes and inserts do not move it. |
| Typing (editable) | each character: `CBN_EDITUPDATE`, `LB_FINDSTRING` of the whole text, no selection, caret and top on the match, `CBN_EDITCHANGE` → Modify; closing a list dropped this way copies the caret row's text into the edit field. |

### Design (C2, implemented: `ComboViewport`)

A Combo is a List in a drop-down. `ComboViewport` has `ListViewport`'s structure and method names (`install`,
`layout`, `rebuild`, `restoreFocus`, `positionPlane`, `updateBar`, `onVScroll`, `onWheel`, `onKeyDown`, `afterNative`)
and reuses `VirtualListModel` (text, widths measured with the combo's `DrawText` rule) and `VirtualRowWindowPlan`.

1. **Window**: the combo's list box holds one interval of logical rows at native entries `0 .. size - 1`, at least a
   dropped page plus an overscan of `max (16, 2 × page)` on each side: around the selection while the combo is
   closed, around the visible rows while it is dropped. A row outside the window reads as native -1, where the native
   rules start from row 0, so a window that must answer "from row 0" starts at row 0.
2. **Native first**: before the list box applies a rule, the window is made to hold every row the rule can reach
   (`cover`): the selection and the caret (when they lie within `NATIVE_REACH` = 2000 rows of the target), the key's
   target and, dropped, the page the target brings on top; the first logical match of a search (`LB_FINDSTRING`,
   `LB_FINDSTRINGEXACT`, intercepted in the list box) and the rows after it; the type-ahead target. Hover, clicks,
   Escape, Enter, the wheel and keys then run natively and send the stock notifications. A selection farther than the
   reach is parked: the list box does not hold it, the API answers it, a commit makes it resident again.
3. **The list box is subclassed** (`listProc`, one static `Callback`): after each message that changes it, the vertical
   bar is written with the stock values (`0 .. count - 1`, the list box's page, the logical top row) whenever the window
   is not the whole list; the scroll-bar codes move the logical top row (a 32-bit thumb), the wheel runs natively in a
   window that holds its lines and the window follows.
4. **Caret lane**: the list box's caret is a logical lane with the stock rules (not moved by inserts, steps back when its
   row is deleted, -1 after `LB_CARETON`), put back natively with the List's delete/re-insert trick
   (`setSingleState`) because a single-selection list box moves its caret only with its selection.
5. **Recorded entry**: in stock the index the list box recorded for its drop is a logical index that inserts and
   deletes do not move. The viewport keeps it as a raw logical row with the window origin it was recorded under.
   While the window keeps that origin the list box's own cancel and commit are the stock ones; after a window move a
   cancel puts the recorded row back, and a commit makes the list box notify exactly when stock would (setting its
   entry to the selection when stock would not notify) or owes the `CBN_SELCHANGE` until the event loop runs, paid by
   a real one (a click commits after `CBN_CLOSEUP`).
6. **API**: 22 hooks (recipe `list-viewport/recipe/gen_combo_viewport_yml.py`, second pass UNCHANGED) delegate the item
   and selection API to the model; `CB_SETCURSEL` keeps SWT's Verify of read-only combos over logical rows; the scroll
   width is the widest logical row; the selection comparisons of `WM_LBUTTONDOWN` and Alt+Down are logical. Segments and
   automatic text direction rewrite every native row, so they put every row back into the list box and hand the combo
   to its native implementation. `SWT.SIMPLE` combos stay native.

### Combo status (build `cv18`, hidden desktops)

- Hook recipe (22 hooks) pass 1 CHANGED, pass 2 UNCHANGED; win32 compile 0 errors.
- `Test_org_eclipse_swt_widgets_Combo_ViewportDifferential` against recordings of the **official SWT 3.135.0 jar**
  (`READ_ONLY` and editable, 10 seeds x 150 steps each: item and selection API, `setText` with items, prefixes,
  other case and no match, keys, Alt+Down, characters, the wheel, the dropped list's scroll codes, thumb, wheel,
  hovering and clicks, `setVisibleItemCount`, drops and closes; selection, text, count, dropped state, events, the
  combo's pixels and the dropped list's bar and pixels every step): **2/2 pass** (3,000 steps).
- Keyboard cues (build `lv17`): the differential captures the combo's face and pixels with the cues hidden
  (`WM_CHANGEUISTATE UIS_SET`, `UISF_HIDEFOCUS | UISF_HIDEACCEL`, to the shell, as the Tree differential does).
  Before that, the `READ_ONLY` run failed now and then on the face alone (seed 8 step 0, cues shown against
  hidden). `Shell.open` in each seed's reset sends `UIS_INITIALIZE`, which Windows resolves from the last input
  event. `ComboCueTraceProbe` hooked every cue message (`WM_CHANGEUISTATE`, `WM_UPDATEUISTATE`,
  `WM_QUERYUISTATE`) over seeds 0..8 on both jars ([`list-viewport/evidence/cues/`](list-viewport/evidence/cues/)):
  the same traffic, and none of it sent by the viewport. The only extra message is a `WM_QUERYUISTATE` that Windows
  sends inside `CreateWindowEx` for the editable combo of the viewport run's mode check. Re-recorded with the
  official jar, the differential passes **2/2** on `lv17`.
- `ComboRulesOracle` replayed on the viewport build: every public answer equals the official output except the timing
  of posted Selection events.
- Failures found on the way and fixed: the list box's bar state (freed, default, written; scroll codes write it) and
  page (fully visible rows); the caret lane (stale after head deletes, -1 after `LB_CARETON`); the drop's recorded
  index (a raw logical index; `LB_SETCURSEL (-1)` does not record; restored only after a started tracking); a
  closed combo's key re-parking the selection it changed; deletes in a dropped list (the list box keeps its top index
  beyond `count - page` and scrolls the caret into view: done natively within the reach); a search without a match
  puts the caret and top on row 0.
- Upstream defects fixed in the viewport mode only (rule 12): [`list-viewport/UPSTREAM_DEFECTS.tsv`](list-viewport/UPSTREAM_DEFECTS.tsv)
  (COMBO-1 `indexOf ("", negative start)` throws, COMBO-2 `deselect (-1)` without a selection sends Modify and clears
  an editable combo's text); the differential accepts exactly these.

## Donors

See [`list-viewport/FOSS_REUSE_DECISION.tsv`](list-viewport/FOSS_REUSE_DECISION.tsv).
