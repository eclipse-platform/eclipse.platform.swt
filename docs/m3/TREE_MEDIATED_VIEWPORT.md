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
3. **Viewport layout: `hwndParent` is the viewport, the TreeView is a content plane.**
   - win32 Tree already wraps the TreeView in `hwndParent` for columns (`createParent`). A mediated Tree always has this parent.
   - The parent owns both native scroll bars, driven by `VirtualTreeViewport` / `VirtualScrollMetrics`: maximum = logical visible rows, thumb = visible rows, selection = logical top row. The scaling for more than 2^31 rows is already in `VirtualScrollMetrics`.
   - The TreeView gets `TVS_NOSCROLL` and becomes a non-scrolling content plane, as tall as its resident rows. It is positioned at `y = -(native row of the logical top) × item height` and, for horizontal scrolling, `x = -horizontal position`, which is the same mechanism column mode uses.
   - **Pixel evidence** (`ViewportPlaneProbe`, win32, Windows 11): a stock Tree scrolled to row 100 and the plane layout positioned at row 100, with the same items and bar range, differ in 2 of 114,000 pixels. Both are anti-aliasing at the scroll bar edge.
   - **Why a plane and not a native-scrolling TreeView:** a second probe (`NoScrollProbe`) showed that `TVS_NOSCROLL` keeps a caller-owned `SCROLLINFO` but disables every native scroll (`TVGN_FIRSTVISIBLE`, `TVM_ENSUREVISIBLE`, keys). With native scrolling enabled, the TreeView overwrites its own bar. Positioning the plane inside the viewport needs neither.
4. **Scroll input is mediated.**
   - `WM_VSCROLL`, `WM_MOUSEWHEEL`, Page Up, Page Down, Home, End, `setTopItem`, `showItem`, `showSelection` and drag auto-scroll set the logical top. `VirtualTreeViewport.ensureVisible` applies the same rules as the native control.
   - Arrow keys and type-ahead move the native focus inside the plane. After the key is processed, the focused row is mapped to its logical row and passed to `ensureVisible`.
   - Each top change repositions the plane, or moves the resident window (`VirtualTreeResidentPlan`) when the top leaves the overscan.
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

## Startup modes

The viewport is the default Tree mode. One SWT-global switch, read once at startup, selects the strategy for every
widget, and a per-class property overrides it (class `org.eclipse.swt.internal.WidgetMode`):

| Property value | Mode |
|---|---|
| `viewport` (default) | mediated viewport over the native control: the native control holds only the resident window |
| `original` | the native SWT implementation, unchanged |
| `custom` | drawn by SWT with a GC (Initiative 31 style); falls back to `viewport` until a widget implements it |
| `skija` | drawn by SWT through Skija; falls back to `viewport` until a widget implements it |

`-Dswt.widget.mode=original` restores stock SWT for every widget; `-Dswt.widget.mode.Tree=original` only for Tree.
Nebula widgets read the same property names.

## Status (win32, build `tv20261016`, measured while the viewport was opt-in)

| Gate | Viewport off | Viewport on |
|---|---|---|
| `Test_org_eclipse_swt_widgets_Tree_Viewport` (7) | skipped (not active) | **7/7 pass** |
| Tree, TreeItem, TreeColumn, VirtualTreeWindow, VirtualTreeTopology, VirtualTreeResidentPlan (265) | 263 pass, 1 fail (Integer/Long assertion bug, also on master), 1 platform assumption | 250 pass, 14 fail |

The 14 failures with the viewport on:

- **Seven white-box tests of the replaced growing-prefix mechanism**, such as "a native frontier of 256" and "collapse keeps one sentinel". They are skipped with the viewport on; the acceptance tests cover the viewport's own invariants.
- **Seven contract failures from one cause**: application-created items on a virtual Tree (`new TreeItem (tree, 0, 0)`, `new TreeItem (parentItem, 0)`). T1 does not mediate insertion yet. They are next.

## Order-statistic sibling index

**Problem.** `VirtualTreeVisibleProjection.rowAt` and `offsetWithinParent` walked the materialized siblings of each parent in order (`firstMaterializedChildId`, `nextMaterializedSiblingId`). A row-to-item or item-to-row mapping therefore cost O(k) per level, where k is the number of materialized children of that parent. k grows with every facade the application obtains, for example 100,000 pinned root items. `VirtualTreeTopology.linkSorted`, `unlink` and `materializedChildId` had the same linear walk, so materializing k siblings cost O(k²).

**Structure.** Each parent owns a height-balanced (AVL) binary search tree over its materialized children.

- The nodes are the topology's own item ids. There is no node allocation and no per-node object. The lanes are parallel primitive arrays indexed by id: `left`, `right` (int), `height` (byte) and `sum` (long), plus one root slot per parent (`roots [parentId + 1]`, slot 0 is the Tree itself). This costs 17 bytes per id plus 4 bytes per parent slot.
- The search key is read from the topology's live `childIndices` lane and is never copied. `insertCoordinate` and `removeCoordinate` shift every later sibling index by the same amount, which keeps the in-order sequence unchanged, so a shift needs no tree update.
- Every node carries the visible extra rows of its child: the child's descendants shown when it is expanded with a known child count, else 0. This is the existing `visibleExtraRows` lane. `sum` is the total of those weights in the node's subtree.
- Weights are maintained where `visibleExtraRows` changes: `refreshVisibleExtra` (expand, collapse, child count) and each expanded ancestor step of `propagateVisibleContribution`. One change costs O(log k) on each affected level.

**Queries.** Let `W (c) = c + extra rows of the materialized siblings before c` be the row offset of logical child c inside its parent.

- `offsetWithinParent (p, t) = t + prefix (p, t)`: one root-to-leaf descent that adds `sum (left) + weight` whenever the node's key is below t. O(log k).
- `rowAt` within a parent: the last materialized child m with key below the logical count and `W (m) <= row`, found in one descent. The row is m itself, a descendant of m (descend one level), or a cold child at `row - extra rows up to and including m`. With no such m it is the cold child `row`. O(log k) per level.
- `window` seeks its first row with the same descent and then emits rows in preorder over the sibling lanes. The seek was O(materialized siblings before the window) and is now O(depth × log k).
- `materializedChildId`, the predecessor needed by `linkSorted` and `unlink`, and therefore `bind`, `insertCoordinate` and `releaseSubtree`, use the same tree. Each is O(log k).

**Kept linear.** `shiftSiblingIndices` still rewrites the index of every later materialized sibling (O(k)), because indices are stored as absolute values. Storing them relative to the tree, with a lazy shift, would make this O(log k) too, but would also make every `childIndex (id)` read O(log k). It is not done here.

**Donors and measurement.** See [`tree-viewport/FOSS_REUSE_DECISION.tsv`](tree-viewport/FOSS_REUSE_DECISION.tsv), rows `sibling-order-statistic-index`, `sibling-prefix-fenwick` and `sibling-sparse-sorted-fenwick`.

- The AVL tree is adapted from Synexia's `LongOrderStatisticMultiset`. Its rank query is this prefix sum.
- A Fenwick tree over logical child indices was rejected. Its memory grows with the logical child count, which can be 2^31 - 1, and every `insertCoordinate` shifts all later indices, which forces an O(child count) rebuild.
- Sorted id arrays with a Fenwick tree, the shape `VirtualRowExtents` uses, were rejected because inserting in the middle is O(k).

**Small sibling sets.** For a parent whose tree is at most 4 levels high (at most 15 materialized children), `materializedChildId`, the prefix sum and the row seek walk the sibling chain instead. The tree is still maintained, so a parent can cross the threshold with no rebuild. The threshold is measured, one JVM per k, with the walk forced on and then off: the walk was faster up to k = 8 to 12, the descent from k = 16 on.

**Measured.** Source: `VirtualTreeOrderStatisticBenchmark`, a plain `main` in the test tree. It calls only API that exists before and after this change. The "before" run uses the headless classes of `8a05de9757`; the "after" run uses this branch.

- Fixture: one parent with 2k logical children, of which k are materialized (at the odd indices). Every fourth materialized child is expanded with 3 cold children.
- Each cell is 5 separate JVMs, interleaved before/after, on JDK 21 on Windows 11 with 32 logical processors. The host ran other agents' builds at the same time (about 63 % CPU load), so the table gives the minimum and the median. The minimum is the least disturbed estimate.
- `rowAt` includes allocating the returned `Row`.

| k materialized siblings | `rowAt` ns before (min / median) | `rowAt` ns after (min / median) | `visibleIndexOf` ns before | `visibleIndexOf` ns after |
|---|---|---|---|---|
| 10 | 19.7 / 27.6 | 22.3 / 52.9 | 12.8 / 20.5 | 8.8 / 9.8 |
| 1,000 | 1,265 / 1,463 | 80.9 / 91.3 | 773 / 921 | 43.9 / 55.1 |
| 100,000 | 115,530 / 179,006 | 227.9 / 250.1 | 146,118 / 808,065 | 129.3 / 150.2 |

Mutations, minimum ns per operation:

| k | bind + forget, before | after | `insertCoordinate` + `removeCoordinate`, before | after |
|---|---|---|---|---|
| 10 | 37.1 | 146.2 | 77.1 | 146.0 |
| 1,000 | 2,359 | 668 | 5,480 | 2,486 |
| 100,000 | 264,696 | 1,363 | 569,421 | 223,223 |

- At k = 10 both versions walk the chain for queries. The extra cost of a mutation at small k, about 110 ns, is AVL insertion and removal.
- `insertCoordinate` + `removeCoordinate` remain O(k) because of `shiftSiblingIndices` (see "Kept linear").
- The rejected Fenwick tree over 2k logical children, median of 4 runs: a row descent takes 18.1, 66.6 and 158.2 ns for k = 10, 1,000 and 100,000. A rebuild takes 61, 2,881 and 342,284 ns, and every coordinate insert or removal would need one. It uses 168, 16,008 and 1,600,008 bytes per parent, which grows with the logical child count. That count can be up to 2^31 - 1, about 16 GiB.

**Proof.** `Test_VirtualTreeOrderStatistic` runs two seeded scripts:

- **Random depths:** 32 seeds × 300 steps. Each step is one of setChildCount, bind, forgetSubtree, expand or collapse, insertCoordinate, removeCoordinate or releaseSubtree, or a move. The parent is ROOT or any materialized node. After every step the test compares sampled `rowAt` (every row when there are at most 64), `visibleIndexOf`, `visibleIndexOfCoordinate`, `window` and `residencyWindow` answers.
- **100,000 siblings:** 100,000 root siblings materialized in random order out of 200,000 logical children, then 12 rounds of 40 random steps with the same comparisons.

Every answer is compared with the linear walk, kept in the test as `LinearReference`. Run against the old classes, the test passes, so the reference matches the old code. Five mutants of the new code all fail it: a seek bound off by one, a missing weight update in `propagateVisibleContribution`, a prefix comparison off by one, and the same two off-by-one bounds in the small-set walk.

## Rules for every leaf

- **Transient objects (every widget):** per-callback scratch data (`TVITEM`, `RECT`, custom-draw and dispinfo structs, `Event`) is reinitialized and reused from a memory-pressure LRU store. An object the application may still hold is reused only after dispatch returns and only when nothing retains it. The JVM keeps no reference counts, so "retains" is decided by SWT-internal ownership, or by M3jdk21's compile-time escape metadata (#826). Otherwise the object is not reused.

- **Threading: CPU and GPU work are split, and the UI thread does no backing-data-structure work.** The UI thread runs only native operations bounded by the window (insert, delete, position), plus O(window) planning. O(n) model work (subtree weights, sorting, filtering, bulk selection counting, compaction) runs on a worker over a generation-stamped snapshot. The result is published to the UI thread only if the generation still matches.
- **Contract:** a client that asks for `SWT.VIRTUAL` gets exactly the virtual API and behaviour (`SetData` on demand, item identity, events). Internally everything is mediated and only the resident window is native.
- **Lean structures:** primitive and bit-lane structures from Synexia and M3jdk21 may be inlined into SWT (copied, with provenance and licence notice). They are never added as a dependency.
- **Diagnostics:** use `System.Logger` (`System.getLogger ("org.eclipse.swt.widgets.Tree")`) at `TRACE`, `DEBUG` or `INFO`. No `System.out` and no ad-hoc counters in product code.
- **Desktop safety:** every UI test runs under `tools/ui_lock.sh` and `tools/run_guarded.sh` (a hard wall-clock budget). Acceptance tests first check, without blocking, that the viewport is active, and skip otherwise. A build without the fix can therefore never hang the desktop.

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
