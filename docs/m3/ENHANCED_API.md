# Enhanced SWT API (M3): map and first leaves

Branch `m3/enhanced-api-20261017`, based on integration round 3 (`ff36ce3c51`). The map is
`docs/m3/ENHANCED_API_MAP.tsv`; the donor ledger of the shipped leaves is
`docs/m3/enhanced-api/FOSS_REUSE_DECISION.tsv`. The user's directive (2026-10-17): the donor links are
mined; expand them into an enhanced, additive API and include the first implementations; then "is there
any API you can add" from our own commit history, and from ecosystems beyond IntelliJ/SWT/Swing/SwingX.

## Binding rules

- **Additive and opt-in.** The stock SWT contract and behaviour do not change. Nothing here is reachable
  unless a client calls an `M3*` type. No stock file is modified by the first leaves.
- **M3 names.** Widget-bound API lives in `org.eclipse.swt.widgets.M3*` (public, M3-prefixed, so it has
  package access to the viewport lanes for fast paths); widget-free API (text, language) lives in
  `org.eclipse.swt.m3`. Never Skija/Skia names.
- **Mode-neutral contract.** Every M3 API answers identically in all five modes
  (`-Dswt.widget.mode=viewport|original|custom|m3|gpu`). The first implementation computes from public
  SWT state with integer arithmetic; a lane fast path may replace it only when the recorded answers stay
  equal (the leaf's JUnit test runs under `viewport` and `original`; that is the leaf's differential).
- **DOOP** (brief rule 18): results are ints, packed longs and int ranges; value objects
  (`M3ItemRange`, `M3ScrollInfo`, `M3TextSegments`) exist only because the caller asked and hold no
  state the lanes do not hold. No per-item allocation on any path that scales with the item count.
- **Java only** (rule 13); Synexia header on every new file (`SYNEXIA_HEADER.java.txt`); JUnit for every
  class with the 99% target (rule 15); no commit trailers.
- **Donor-first.** Every leaf has rows in `enhanced-api/FOSS_REUSE_DECISION.tsv` before code. GPLv2+CPE,
  LGPL and non-Java donors are patterns only; EPL/Apache/BSD rows may be adapted with the notice kept;
  first-party fork classes are reused as they are.

## Map columns (`ENHANCED_API_MAP.tsv`)

| column | meaning |
|---|---|
| `api_id` | stable id, `M3-<CAPABILITY>` |
| `family` | tree, table, list, grid, all, text, language, theme, perf |
| `source` | `first-party` (phase 1b: already in our viewport internals, never exposed by stock), `donor` (phase 1: `DONOR_CATALOGUE.tsv` keys), `cross-ecosystem` (phase 1c: non-Java patterns, rule 13) |
| `swt`, `nebula`, `rwt`, `intellij`, `swing_swingx`, `awt` | toolkit applicability (user 2026-10-18, "everything now has a viewport"): `Y` the same M3 surface over the toolkit's own widget (SWT Tree/Table/List; Nebula Grid/Gallery; RAP RWT Tree/Table; IntelliJ JBList/Tree/JBTable; Swing JList/JTree/JTable and SwingX JX*; AWT java.awt.List), `M` needs a mapping to a different widget kind, `-` no counterpart |
| `m3jdk_core` | `Y` when the lane logic is toolkit-neutral and moves into the shared M3JDK viewport core (the viewport-everywhere agent carries it); `-` for toolkit event wiring only |
| `donor_slugs` | catalogue keys, `HF:<hf_id>` rows of `HF_LANGUAGE.tsv`, or `CROSS:<ecosystem-feature>` |
| `donor_evidence` | the donor classes/methods or our classes that prove the mechanism |
| `proposed_m3_surface` | type and methods |
| `doop_data_plan` | lanes and the facade policy |
| `contract_tests` | what the JUnit test must assert |
| `priority` | P1..P4 by viewport relevance |
| `status` | `leaf-N-shipped` or `proposed` |

Ranking: first-party rows outrank donor rows where they overlap (the mechanism exists and is measured);
the task's first three leaves are first-party exposures plus the language seed; the strongest
cross-ecosystem patterns (diffable snapshots, visibility observer, proxy models, scroll restoration) are
P2, directly behind them.

## Phase 1b: first-party capabilities (mined from our commit history)

`git log` of the Tree, Table, List, all-widgets, Grid and Swing branches and the classes under
`Eclipse SWT/common/org/eclipse/swt/widgets` and `internal/residency`:

| api_id | our mechanism | commit / class evidence |
|---|---|---|
| M3-RANGE-SELECT, M3-VISIBLE-RANGE | hybrid range/bitset selection, visible window | `VirtualSelectionModel`, `ViewportRuntime.Selection`, `VirtualScrollMetrics` |
| M3-SCROLL-INFO, M3-SCROLLBAR-GEOMETRY | logical scroll model owned by the viewport | `VirtualScrollMetrics`, `ViewportScrollBarGeometry`, `ViewportScrollLayout` |
| M3-ROW-MATH | O(log n) row/item mapping, subtree sizes, row extents | `VirtualTreeSiblingIndex`, `VirtualTreeTopology`, `VirtualTreeVisibleProjection`, `VirtualRowExtents`, PR #197 |
| M3-RESIDENCY, M3-OVERSCAN | resident window, materialized facades, pressure | `VirtualTreeResidentPlan`, `org.eclipse.swt.internal.residency.*`, `M3_ITEM_RESIDENCY.md` |
| M3-BULK-UPDATES | one diff-planned native pass; `setItemCount(10^6)` 4.9 ms | `ViewportBatch`, `BatchScope`, `VirtualRowWindowPlan`, commit 6359659c9a |
| M3-EMPTINESS | `SWT.EmptinessChanged` on n->0 and 0->n | commit 0904279df1, `TreeViewport` |
| M3-PREFETCH | SetData negotiated for the window only | `TreeViewport.checkData` calls, `LIST_MEDIATED_VIEWPORT.md` item 6 |
| M3-MODE-QUERY | startup mode switch | `WidgetMode` (commit cbc458de66) |
| M3-SNAPSHOT | recorded differential: state string + pixel CRC per step | `ViewportDifferential`, commit 8823cb2213 |
| M3-LOG | asynchronous structured logging | `SwtLog` |
| M3-THEME-HOOKS | look-and-feel seam LF1/LF2 | commit 6a1fe02b18, `LOOK_AND_FEEL.md` |
| M3-WIDTH-TRACK | width-defining row | `VirtualListWidth` |

## Phase 1c: cross-ecosystem patterns (non-Java; patterns only)

iOS UIKit (diffable data sources, prefetchDataSource, performBatchUpdates, swipe actions), Android
RecyclerView (RecycledViewPool, DiffUtil, ItemAnimator, SnapHelper, nested scrolling), web virtual
scrollers and DOM (overscan, scroll restoration, sticky rows, IntersectionObserver, scroll anchoring,
scroll snap), data grids (AG Grid pinning, grouping, server-side rows, flash, fill handle; the AG Grid
block cache is already in `docs/m3/swt-lazy-data-20261013-FOSS_REUSE_DECISION.tsv`), Flutter slivers,
Monaco/CodeMirror (decorations, view zones) and Qt (`QSortFilterProxyModel`). Rows: M3-DIFF-APPLY,
M3-VISIBILITY-OBSERVER, M3-PROXY-MODEL, M3-SCROLL-RESTORE, M3-STICKY-ROWS, M3-REUSE-POOL, M3-VIEW-ZONES,
M3-COLUMN-PIN, M3-ROW-GROUPING, M3-SNAP-SCROLL, M3-OVERSCAN, M3-ITEM-ANIMATOR, M3-ROW-ACTIONS,
M3-COLLAPSING-HEADER, M3-CELL-FLASH, M3-FILL-HANDLE, M3-NESTED-SCROLL, M3-SERVER-TREE.

## Leaves

| leaf | api_ids | types | design doc | status |
|---|---|---|---|---|
| 1 | M3-RANGE-SELECT, M3-VISIBLE-RANGE, M3-MODE-QUERY | `M3ItemRange`, `M3ItemSelection`, `M3Mode` | `enhanced-api/LEAF1_ITEM_RANGES.md` | green: Test_M3ItemRange 6/6, Test_M3ItemSelection 15/15, viewport and original |
| 2 | M3-SCROLL-INFO, M3-SCROLL-TO-ALIGNED | `M3ScrollInfo`, `M3Scrolling` | `enhanced-api/LEAF2_SCROLLING.md` | green: Test_M3Scrolling 11/11, viewport and original |
| 3 | M3-TEXT-SEGMENTS | `org.eclipse.swt.m3.M3TextSegments` | `enhanced-api/LEAF3_TEXT_SEGMENTS.md` | green: Test_M3TextSegments 8/8 (headless and on the hidden desktop) |

Next leaves after these, in map order: M3-TOKEN-CURSOR (token-aware matching over interned ids, with the m3-regex-cursor track), M3-EMPTINESS (expose), M3-ROW-MATH, M3-RESIDENCY,
M3-BULK-UPDATES, M3-PREFETCH, M3-DIFF-APPLY, M3-VISIBILITY-OBSERVER, M3-PROXY-MODEL,
M3-SCROLL-RESTORE, M3-LOCALE-COLLATION.

## Test method

Each leaf has one JUnit class under `tests/org.eclipse.swt.tests/JUnit Tests/org/eclipse/swt/tests/junit/`.
Widget tests run on the hidden desktop (`ui_lock.sh` + `hidden_java.sh`) twice: once with the default
`viewport` mode and once with `-Dswt.widget.mode=original`; both must pass with the same assertions
(the mode differential of the leaf). The text leaf runs headless. Every assertion compares the M3 answer
with a public-API oracle (`getSelectionIndices`, `getBounds`, `getTopIndex`, `java.text.BreakIterator`).

## Runs

| date | jar | sources | run | result |
|---|---|---|---|---|
| 2026-10-18 | `ta20261017a` (C:/m3-superset/tva) | leaves 1-3 at c1b4e589e9 (before the round-6 merge; the merge touches none of the M3 files) | hidden desktop, `-Dswt.widget.mode=viewport`, the four classes | 40/40 |
| 2026-10-18 | same | same | hidden desktop, `-Dswt.widget.mode=original` | 40/40 |
| 2026-10-18 | same | same | headless, Test_M3TextSegments + Test_M3ItemRange | 14/14 |

Not run yet: the same classes on a jar built from the round-6 merge (two javac attempts failed with native out-of-memory
during a machine-wide memory storm, logs gzipped in C:/m3-superset/tva/logs); JaCoCo coverage of the M3 classes.
