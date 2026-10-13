# M3 superset consolidation, 2026-10-13

This document records how the `m3/superset-consolidation-20261013` merge turns the fork's `master` into
one superset of SWT: every fix and feature that the 2026-10 branches carry, with SWT's public API
unchanged. The ledger is also packaged in the superset jar as `META-INF/m3/LEDGER.tsv`.

## Why a consolidation was needed

Five merges on `master` recorded a pull request as *ancestry only*: the merge tree equals its first
parent, so the pull request's head became an ancestor of `master` while none of its code arrived.

| Merge on master | Pull request | Missing head | Missing change |
|---|---|---|---|
| `90abe20bf6` | #126 GPU retained planes | `ee747afc58` | 69 files, +5490/-31 |
| `4e6503b71f` | #127 paint edges, leaf 2 | `d23d0bd046` | 24 files, +2229/-4 |
| `669d5b1e05` | #128 | `d4487d100f` | 6 files, +341 |
| `a01cd34264` | #129 List/Combo viewport | `917b9c2bfc` | 73 files, +6295/-23 |
| `c5bedd533f` | #134 integration | `44a3db1d8e` | 163 files, +14032/-59 |

Because those heads are already ancestors of `master`, a plain `git merge` is a no-op. Each head was
therefore replayed as a real three-way merge against its original fork point:

```
C=$(git commit-tree <head>^{tree} -p <fork-point>)
git cherry-pick -X ignore-cr-at-eol $C
```

`master` also failed to compile: `VirtualTabContent` (from the swt-lazy workspace snapshot) calls
`TabFolder.paintCatchUpByListener()`, which no platform defined.

## How conflicts were decided

* **Blob lineage first.** When one side's file is a revision that occurs in the history of the other
  side's file, the later revision is kept whole. Hunks of the older revision are never re-applied.
  `stale_audit` re-checked all 171 changed paths after the replays and found 0 stale reintroductions.
* **Union when the sides are independent.** For example, `Control.releaseWidget` keeps both the
  EffectiveVisibility release and the retained-frame disposal.
* **Same feature built twice.** `VirtualSelectionModel` keeps master's engine (`VirtualBitLanes`, the
  measured cut-over) and adds #129's policies: the `(boolean hybrid)` constructor with the eager
  cut-over or ranges only, and `bitmapMode()`. Errors in the bitset form are identical to the range form.
* **SWT idiom wins on error shapes.** `ItemResidency.insert` keeps `SWT.error(SWT.ERROR_INVALID_RANGE)`
  for a bad index and adds #135's capacity admission as `SWT.error(SWT.ERROR_ITEM_NOT_ADDED)`.
* **CR-only changes never land** (`-X ignore-cr-at-eol`; snapshot branches whose only change is line
  endings are recorded with master's bytes).

## Ledger

| Source | PR | Commit | Action | Effect | Notes |
|---|---|---|---|---|---|
| replay:90abe20bf6 | #126 | `ee747afc58` | REPLAYED | 69 files +5484/-31 | base 0b6a31ff61; Control.releaseWidget and Display.releaseDisplay kept both release steps |
| replay:4e6503b71f | #127 | `d23d0bd046` | REPLAYED | 22 files +2198/-9 | base 7885c3011e; StyledText/StyledTextRenderer/AllWidgetTests conflict hunks keep master's evolved code; resetCache Javadoc moved back onto resetCache |
| replay:669d5b1e05 | #128 | `d4487d100f` | REPLAYED | 6 files +341 | base d23d0bd046; clean |
| replay:a01cd34264 | #129 | `917b9c2bfc` | REPLAYED | 73 files +6065/-26 | base 0b6a31ff61; VirtualListModel lazy widths kept; VirtualSelectionModel engine from master plus #129 API |
| replay:c5bedd533f | #134 | `44a3db1d8e` | REPLAYED | 7 files +694/-33 | base d8a856b0d2; 19 paths kept whole by blob lineage (current file is the later revision); stale ViewportPlanes test not restored (renamed by 779ab14055); VirtualSelectionModel/Planner take the integration resolution of #119+#129 |
| fix/swt-1468-text-shift-del-verify | - | `4ac4b93bc2` | MERGED | 2 files +65 | upstream bug 1468: Shift+Del in Text goes through verify |
| fix/swt-3411-gtk-datetime-tab-traversal | - | `a1ee397183` | MERGED | 2 files +44/-5 | upstream bug 3411: GTK DateTime tab traversal |
| fix/swt-3416-webkit-openwindow-same-browser | - | `86ad21c82c` | MERGED | 2 files +49/-1 | upstream bug 3416: WebKit OpenWindowListener returning the receiver |
| m3/integration-20261013 | #134 | `(head)` | MERGED | 3 files +33/-7 | commit after the #134 snapshot |
| codex/m3-viewport-arithmetic-20261013 | #135 | `298dbafee1` | MERGED | 7 files +195/-5 | integer boundaries for damage, ItemBitSet and residency admission; insert keeps SWT.error(ERROR_INVALID_RANGE) and adds SWT.error(ERROR_ITEM_NOT_ADDED) at Integer.MAX_VALUE; test fixture rethrows Errors |
| m3/swt-list-virtual-model-20261013 | - | `ec1ee2b388` | MERGED | 0 files | first revision of VirtualListModel/VirtualListWidth; blob already in master history (0b6a31ff61), evolved files kept |
| m3/viewport-matrix-completeness-20261013 | - | `8bded3e260` | MERGED | 4 files +246 | fail-closed matrix plan/result completeness oracle |
| m3/styledtext-retained-planes-b-20261013 | - | `d2886c57c9` | MERGED | 5 files +2067/-808 | StyledText leaf 3 part B: line masks (StyledTextLineStore), keyed layouts, one scroll per frame; removes per-line LineDrawInfo objects by design |
| codex/m3-work-c-swt-replay-20261013 | - | `7e22364591` | MERGED_SUPERSEDED | 0 files | snapshot is an older revision of the residency engine (pre idiom pass, pre #135); tree unchanged |
| codex/m3-work-c-swt-examples-src-20261013 | - | `3f0bc1c303` | MERGED_NO_EFFECT | 0 files | 389 files differ by CRLF only; master bytes kept |
| fix:TabFolder.paintCatchUpByListener | - | `8f9b8665ec` | FIXED | 3 files | master did not compile (VirtualTabContent call without definition); win32/gtk/cocoa answer true |
| branches:same-root | - | `-` | CONTAINED | 31 branches | every 2026-10 branch on master's history is contained after this merge (merge-tree rescan) |
| branches:old-root(3275970a) | #1..#110 | `-` | SUPERSEDED_BY_MASTER | 99 merged into old masters + 33 empty | old master content present except helpers removed on purpose (LineDrawInfo family by 1cbae810c5, syncViewportRuntime by e042a3c816) |
| branches:old-root-unmerged | - | `-` | NOT_INTEGRATED | 38 branches | alternative implementations and generated distillation dumps never merged into any master; listed in docs/m3/SUPERSET_CONSOLIDATION_20261013.md |
| old:c9f4112676 | #111 | `687c8a2f8b` | FOLLOW_UP | 17 upstream commits | upstream Eclipse sync f87da9a863..687c8a2f8b never reached master; first commit a4bc054ce9 conflicts with the GTK virtual native model; next PR |
| old:be5cef162a / codex/java2d-recovery-20261012 | #109 / #112 | `486688b4bf / -` | FOLLOW_UP | about 1.1k lines | Java2DCanvas restore; 17-27% of lines present; next PR |
| old:m3/viewport-empty-overscan-20261012 | #113 | `-` | FOLLOW_UP | 451 lines | GTK empty/zero-width/hidden row windows; 0% present; next PR |
| old:m3/viewport-model-20261012 | #114 | `-` | FOLLOW_UP | 3131 lines | GTK logical Table viewport; 0% present (master root 5094846b2f is its ancestry-only snapshot); next PR |
| fix:Text.wmClipboard | - | `59009d8ff0` | FIXED | 1 file | win32 cut/clear verified twice (edit control deletes WM_CUT via WM_CLEAR); Shift+Del test of #1468 passes |
| test:StyledText.verticalIndent | - | `(this PR)` | ADAPTED | 1 file | upstream white-box test reads the cached height from StyledTextLineStore (part B) instead of LineSizeInfo |
| pre-existing:List_Viewport | #129 | `917b9c2bfc` | PRE_EXISTING | 3 tests | scrollRoundTrip SetData count, selectionAndRemovalShift text, setItems capacity bound: fail identically on #129's own head; follow-up |

## Verification

| Gate | Result |
|---|---|
| Conflict markers in java/md/xml/MF/sh/tsv/py | 0 |
| Re-scan of every 2026-10 branch on master's history (merge-tree) | 31 CONTAINED, 1 NO_EFFECT, 0 with remaining change |
| `javac --release 21`, win32 x86_64 fragment (734 sources) | 0 errors |
| `javac --release 21`, GTK source set (544 sources) | 0 errors |
| `javac --release 21`, Cocoa source set (684 sources) | 0 errors |
| `javac`, `tests/org.eclipse.swt.tests` (211 sources; `org.eclipse.test.Screenshots` stubbed on the build host) | 0 errors |
| Headless JUnit: residency, damage, bit sets, selection, row windows, list and tree models, graphics, layout (22 classes) | 147 / 147 passed |
| Display JUnit: StyledText (7 classes), List_Virtual, List_Viewport, CCombo_Virtual, Control_batch, VisibilityGate_box, RetainedPlanes, Text, ItemResidencyPrint | 1226 tests: 1209 passed, 6 aborted by assumptions (no win32 foreground alpha; desktop capture), 11 failed in the batch run |

The 11 batch failures were resolved or classified:

* `Text#test_shiftDeleteCutsWithVerifyListener`: a real defect, fixed. The edit control deletes the
  selection of `WM_CUT` through `WM_CLEAR`, so the cut was verified twice.
* `StyledText#test_verticalIndent_resetsCacheForAllLinesAssumingLowestLineHeight`: the test was adapted
  to `StyledTextLineStore`, which replaced `LineSizeInfo`. It passes.
* `StyledText#test_bug551335_lostStyles`, `StyledText#test_backspaceAndDelete` and
  `Text#test_backspaceAndDelete` pass when run alone. They post keys or read screen pixels, and fail
  only when the test shell is not the foreground window.
* `StyledText_VisibilityGate`, 4 parameterized cases: all 5 tests pass when the class runs alone. The
  failures depend on test order within one JVM. Follow-up.
* `List_Viewport`, 3 tests: they fail identically on #129's own head (`917b9c2bfc`, built and run as a
  baseline), so they pre-date this consolidation. Follow-up.

## Branches outside master's history

`master` was re-rooted twice by parentless snapshot commits: `5094846b2f` (PR #114) and `0b6a31ff61`
(PR #116). The 177 branches of the older root (`3275970ab8`, PRs #1 to #110) were audited:

* 99 are ancestors of an old `master`, and 33 carry no own change. Old `master`'s content is present
  here, except the helpers that later refactors removed on purpose: the `LineDrawInfo` family
  (`1cbae810c5`, "paint builds no per-line objects") and `syncViewportRuntime` (`e042a3c816`).
* Five old pull requests never reached any `master` and are the next consolidation step: #111 (17
  upstream Eclipse commits, f87da9a863..687c8a2f8b), #109/#112 (Java2DCanvas restore), #113 (GTK empty
  row windows) and #114 (GTK logical Table viewport).
* 38 branches were never merged anywhere. One of them (`docs/m3-design-invitation-20261007`) is fully present already; the other 37 are alternative implementations and generated
  distillation dumps. Each row gives the branch, its added lines of 25 or more characters, and the
  share of those lines present in this tree:

| Branch | Added lines | Present |
|---|---|---|
| `aix/atom-pattern-mastery-rollout-v3-20261007` | 117281 | 26.2% |
| `docs/development-guide-20261005` | 108668 | 23.7% |
| `m3/swt-api-version-maven-align-20261004` | 4564 | 15.4% |
| `m3/swt-example-screenshot-scenes-20261004` | 4265 | 16.2% |
| `m3/swt-fragment-lifecycle-proof-20261004` | 6930 | 17.6% |
| `m3/swt-full-distillation-final-receipt-20261005` | 44161 | 45.2% |
| `m3/swt-gtk-tree-frontier-recovery-20261004` | 7072 | 17.8% |
| `m3/swt-gtk-tree-logical-model-activate-20261005` | 43671 | 44.5% |
| `m3/swt-gtk-tree-logical-model-activation-20261005` | 108828 | 23.6% |
| `m3/swt-gtk-visible-header-redraw-20261004` | 4908 | 15.0% |
| `m3/swt-native-fragment-version-align-20261004` | 6277 | 16.4% |
| `m3/swt-native-source-distillation-20261005` | 44273 | 45.2% |
| `m3/swt-render-command-distillation-20261004` | 7068 | 15.9% |
| `m3/swt-tree-test-display-fix-20261006` | 109556 | 23.7% |
| `m3/swt-viewport-contract-hardening-20261003` | 4348 | 13.8% |
| `m3/swt-viewport-contract-hardening-v2-20261004` | 5052 | 14.7% |
| `m3/swt-viewport-control-editor-zplane-20261004` | 15754 | 60.0% |
| `m3/swt-viewport-damage-regions-20261004` | 7156 | 15.7% |
| `m3/swt-viewport-dnd-proof-v2-20261004` | 15909 | 59.4% |
| `m3/swt-viewport-gc-affine-v2-20261006` | 109869 | 23.7% |
| `m3/swt-viewport-gtk-tree-collapse-residency-v2-20261003` | 3072 | 12.5% |
| `m3/swt-viewport-packed-tree-state-20261003` | 2173 | 11.8% |
| `m3/swt-viewport-paint-state-consolidation-20261004` | 5897 | 13.5% |
| `m3/swt-viewport-resource-budgets-20261006` | 116652 | 25.9% |
| `m3/swt-viewport-retained-path-20261004` | 6328 | 15.9% |
| `m3/swt-viewport-retained-path-v2-20261004` | 6456 | 16.3% |
| `m3/swt-viewport-screenshot-ci-20261004` | 4880 | 15.1% |
| `m3/swt-viewport-scrollbar-visual-scenes-20261004` | 5263 | 16.1% |
| `m3/swt-viewport-scrollbar-visual-scenes-v2-20261004` | 6136 | 16.0% |
| `m3/swt-viewport-win32-table-storage-20261003` | 3846 | 10.0% |
| `m3/swt-viewport-win32-tree-topology-20261003` | 4172 | 14.2% |
| `m3/swt-win32-tree-native-frontier-20261011` | 132 | 0.0% |
| `m3/synexia-canonical-recipe-home-20261006` | 116954 | 26.0% |
| `m3/ui-distillation-final-20261007-v2` | 117281 | 26.2% |
| `m3/upstream-merge-20261006` | 116555 | 25.9% |
| `m3/upstream-sync-20261006` | 116594 | 25.9% |
| `m3/viewport-model-20261012` | 3131 | 0.0% |
| `m3/viewport-stroke-culling-regression-20261004` | 6232 | 15.8% |

## Build

`C:/m3-superset/build-swt.sh` reproduces the jar `org.eclipse.swt.win32.win32.x86_64-m3-20261013.2.jar`: the classes of this tree, plus the official 3.135.0 native libraries renamed to the fork's library version. The jar carries `META-INF/m3/MAPPING.tsv` (class, source path, source blob, origin) and `META-INF/m3/LEDGER.tsv`. The manifest adds `M3-Build`, `M3-Source-Branch`, `M3-Source-Commit`, `M3-Natives`, `M3-Mapping` and `M3-Ledger`.

## Upstream sync of PR #111 (stacked on the consolidation)

PR #111 merged upstream Eclipse SWT `f87da9a863..687c8a2f8b` as ancestry only. Its 17 upstream commits are cherry-picked with `-x`, so each keeps its upstream author and origin line.

| Upstream commit | Result | Resolution |
|---|---|---|
| a4bc054ce9 [GTK] Keep the Image of each Tree and Table cell for getImage() | applied | Native path takes upstream's `cellImage[]`. The virtual native model keeps `virtualImages`. Column insert in Table and Tree shifts `cellImage` inside M3's materialized-item loop. `setImage` keeps `setCachedState (true)`. |
| 406fbb0304 Remove unused numLines parameter from DefaultContent.delete | applied | signature taken, M3 formatting kept |
| 19cc7189be [GTK] Remove the obsolete Shell geometry abstraction | applied | clean |
| 8f4fe7a6f5 [Cocoa] Reject non byte[] data in ByteArrayTransfer.javaToNative | applied | clean |
| 5dee807c35 [GTK4] Set gtk-interface-color-scheme | applied | clean |
| ca720bdb30 Prevent CTabFolder chevron and min/max toolbars from overlapping | applied | upstream's refactored overlap test taken |
| 610dd379f2 Add tests for Shell minimum and maximum size | applied | GTK4 `trimHeight()` always measures the header bar (upstream fix) |
| c08b7dd40c Use a small default size for test shells | applied | clean |
| 3ea419e5a1 [StyledText] Remove unused styles-only mode | **skipped** | dead-code removal only (`COMPACT_STYLES`/`MERGE_STYLES` are constant `true`); its 9 hunks collide with the part B renderer rewrite; no behaviour is lost |
| f24d4eb72f [StyledText] Remove unreachable gap removal from DefaultContent | applied | clean |
| ab0e32a04d, 7daa37bbda, 33459eb055, 18fe3cdcc7 [GTK] NativeImageLoader cleanups | applied | clean |
| 539aeb0b17 [GTK] Don't detach model in Tree.removeAll() | applied | Upstream's `clearingModel` guard replaces M3's detach workaround, which caused the broken redraw. The guard is combined with M3's `virtualCallbackDepth` bookkeeping. Upstream's `structureChanged()` call is dropped because the M3 tree model has no such hook. |
| 7c30c9506b Run the Cocoa test bundle on JUnit 5 | applied | clean |
| 687c8a2f8b [GTK] Fix loading 1 pixel wide RGB images with glycin | applied | clean |

Verification of the sync:
* javac reports 0 errors for win32 (734 sources), GTK (542), Cocoa (684) and the tests.
* JUnit: 929 tests found, 914 passed, 9 assumption-aborted.
* The 5 batch failures (StyledText bug551335, setStyleRanges_render, backspaceAndDelete; Text Shift+Del, backspaceAndDelete) post keys or read screen pixels, and each passes on its first isolated run.
