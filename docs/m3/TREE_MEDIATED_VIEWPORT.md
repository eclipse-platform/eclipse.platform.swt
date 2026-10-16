# Tree as a mediated viewport (win32 first)

Status: design and baseline. No widget source changes in this commit.

## Goal

Every `Tree` behaves like a viewport over a logical model while keeping its public contract and visible behaviour.
The internal native control is an ordinary, non-virtual TreeView that holds only the rows on screen, plus overscan and their ancestors.

Tree mediates all public API calls (`getItem`, `getItemCount`, `getTopItem`, `setTopItem`, `showItem`, selection, expansion, check state, bounds) through the logical model.

The scope is SWT's own `Tree` and `TreeItem`, not a layer on top. Public signatures, events, styles and pixels do not change.

## Baseline (master `ef565e23ce`, win32, JDK 21, probe `M3TreeViewportBaselineProbe`)

| Case | Result | Upstream contract |
|---|---|---|
| Non-virtual, 100,000 roots created with `new TreeItem` | 157.6 s to build; 100,000 native items; 27 MB heap | same API, should not need a native item per row |
| `SWT.VIRTUAL`, `setItemCount (1,000,000)` | 256 native items; vertical bar maximum 255 | the scroll bar must show 1,000,000 rows |
| `SWT.VIRTUAL`, `getItem (99,999)` | does not return in usable time (the busy cursor was closed by hand): the native prefix grows one native insert per row | O(1) facade, no native work |
| `SWT.VIRTUAL`, 1,000 roots, expand one root with 100,000 children | 522 native items; bar maximum 500 | bar must show 101,000 rows |
| Tree regression suites (Tree, TreeItem, TreeColumn, VirtualTreeWindow, VirtualTreeTopology) | 257 of 260 pass. Failures: `test_Virtual`, and `nativeTreeGeometryAutomaticallyAdmitsBoundedOverscan` (Integer/Long `assertEquals` in the test). One win32 assumption skip. | baseline for every later leaf |

These rows are the open items of SWT #163 ("bounded native peer retirement, distant scrolling, both reported scrollbar defects"). The virtual Tree keeps a growing native prefix, adding chunks of 256 as the user scrolls toward its end, and trims it only on collapse. The vertical bar mirrors that prefix, not the logical extent.

## Design

The design is reused from this repository's own GTK Tree: `usesBoundedVirtualView`, the bounded native model and two adjustments.

1. **Logical model.**
   - `VirtualTreeTopology` holds the facade ids, child counts and the flag lanes CHECKED, GRAYED, EXPANDED, PINNED and CACHED.
   - `VirtualTreeVisibleProjection` maps visible rows. Cold children are arithmetic and allocate nothing.
2. **Resident window.**
   - `VirtualTreeVisibleProjection.Residency` (common, already used by GTK) produces a dense native preorder: the ancestor path of the first row, then the paint rows `[top - overscan, top + visible + overscan)`.
   - The TreeView holds exactly these native items, whose `lParam` is the facade id, or -1 for a cold row.
   - A window move inserts the missing native items and deletes the retired ones, under one redraw suspension. Native selection, focus and check state are re-applied from the lanes.
3. **Logical scroll bar.**
   - win32 Tree already wraps the TreeView in `hwndParent` when it has columns. The TreeView is made one scroll-bar width wider, which clips its own bar, and `updateScrollBar` mirrors `SB_VERT` onto `hwndParent`.
   - A mediated Tree always uses this parent. `hwndParent`'s `SB_VERT` gets nMax = logical visible rows - 1, nPage = visible rows and nPos = the logical top row.
   - `WM_VSCROLL` and `WM_MOUSEWHEEL` set the logical top. Inside the window the TreeView scrolls natively (`TVGN_FIRSTVISIBLE`). Leaving the window's overscan rebuilds the residency around the new top.
4. **Native-initiated scrolling.**
   - Arrow keys, type-ahead, auto-scroll during drag and `TVM_ENSUREVISIBLE` scroll the TreeView inside the window.
   - After each such message, Tree reads the native first visible row and maps it to the logical top: logical top = window start + native row - ancestor rows. It then updates the bar, and shifts the window when the top is within the overscan of an edge.
   - Page Up, Page Down, Home and End are answered logically.
5. **Facades and identity.**
   - A facade returned through the API is pinned, as today, and keeps its identity.
   - A facade whose row leaves the window keeps its object and loses its native handle (`handle == 0`).
   - TreeItem methods answer detached facades from the lanes: text, images, fonts and colours are already Java-side; check, gray, expanded and selected come from topology flags; bounds are computed from the logical row × item height relative to the top. Setters write the lanes and, when resident, the native item.
6. **Selection.**
   - Single selection is the focus facade id.
   - MULTI selection is a selection lane. `getSelection`, `getSelectionCount`, `selectAll`, `deselectAll` and shift-click ranges are answered from the lane, so a range larger than the window is exact.
7. **DPI.**
   - The DPI path of #159 and #161 already visits resident items only. The window rebuild re-applies item height and images on the next window move.
   - Detached facades hold no native resource that needs rescaling.
8. **Accessibility.**
   - The TreeView's MSAA children are the resident rows. GTK has the same bound (`notifyVirtualAccessibleItemStates`).
   - A logical accessible (row count and focus row) is a separate leaf and is listed as NOT_CLAIMED until it lands.

## Leaves (each is its own PR with its tests; recipe-first)

| Leaf | Scope | Acceptance |
|---|---|---|
| T1 | win32 `SWT.VIRTUAL`: logical bar plus resident window replace the growing prefix; mediated `getItem (index)` and `getItemCount`, `getTopItem`, `setTopItem`, `showItem`, `indexOf` | native items ≤ 2 × (visible + 2 × overscan) + depth at 1,000,000 rows; bar maximum = logical rows; `getItem (999,999)` < 50 ms; scroll round trip reproduces state and the `Control.print` hash; Tree suites ≥ baseline |
| T2 | detached-facade mediation of every TreeItem getter and setter | differential: virtual vs plain Tree answer the same for the same items (List_Viewport pattern) |
| T3 | MULTI selection lane, keyboard (Page, Home, End, type-ahead) and wheel | selection count exact over 1,000,000 rows; keyboard parity scripts vs a plain Tree |
| T4 | non-virtual Trees through the same topology (`new TreeItem` registers a logical row and inserts natively only when resident) | 100,000-row build < 1 s; plain-Tree differential |
| T5 | GTK and Cocoa non-virtual mediation (GTK virtual is already bounded) | compile plus hosted runtime evidence |
| T6 | logical accessibility | NOT_CLAIMED until done |

After Tree comes Table (win32 already has `LVS_OWNERDATA`; a mediated Table always uses it internally), then the other widget families in `docs/M3-SCALE/runs/20261013-all-widget-viewport/WIDGET_IMPLEMENTATION_LEDGER.tsv` (Synexia).

## Donors

See [`tree-viewport/FOSS_REUSE_DECISION.tsv`](tree-viewport/FOSS_REUSE_DECISION.tsv).

- In-repository sources (GTK bounded view, common projection and residency, win32 `hwndParent` mirror) are adapted directly. They are EPL-2.0 in this same repository.
- Initiative31's custom-drawn Tree (`TreeItemsHandler`, page navigation) is a pattern donor. Its renderer is not used, because win32 must keep the native TreeView's pixels.
- Skija is not used by this path.
- JavaFX `VirtualFlow`, Qt `QTreeView`, wxWidgets `wxDataViewCtrl` and Swing `FixedHeightLayoutCache` are pattern-only. Their licences keep their source out of EPL files.

## Non-claims

- No leaf is implemented by this commit.
- GTK and Cocoa runtime behaviour is not measured on this host.
- Accessibility with logical row counts is not claimed.
