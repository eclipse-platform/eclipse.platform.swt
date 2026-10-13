<!-- SPDX-License-Identifier: EPL-2.0 -->
# SWT integration build 2026-10-13

Branch `m3/integration-20261013` puts every open SWT pull request from 2026-10-13 into one tree, so that one
win32 x86_64 jar carries all of the day's viewport, flicker, lazy-data and residency work. The individual pull
requests remain the units of review. Do not merge this branch before its parts are merged.

- **Base:** `origin/master` at `a02236ca87`. It already contains #115 to #118 and the CI workflow fix.
- **Method:** forward merges only. Each pull request head is merged with `git merge --no-ff`, in the order of
  `CONSOLIDATION_20261013.md` (Synexia #10921). Nothing is rebased.
- **Re-run:** `build-integration.sh merge swt`. It fetches the current heads and merges only what is new. Recorded
  conflict resolutions are replayed with `git rerere`.
- **Record:** `MERGES.tsv` lists the head SHA of every pull request and the merge commit that brought it in. The
  script rewrites it on each run.

## Merge order

| # | PR | Branch | What it brings |
|---|---|---|---|
| 0 | master | `master` | Base: #115–#118 and the CI workflow fix |
| 1 | #120 | `m3/styledtext-visibility-gate-20261013` | StyledText does no work while not visible |
| 2 | #123 | `m3/styledtext-retained-planes-20261013` | StyledText scrolls by blit and packs its selection (stacked on #120) |
| 3 | #121 | `m3/viewport-damage-region-20261013` | `ViewportDamageRegion` and `ViewportDamageRecorder` |
| 4 | #124 | `m3/swt-visibility-gate-20261013` | win32 effective-visibility gate for paint and layout |
| 5 | #122 | `m3/item-residency-20261013` | Shared `ItemResidency` engine |
| 6 | #119 | `m3/swt-lazy-data-20261013` | Lazy data source, lazy tree children, lazy tab content |
| 7 | #126 | `m3/swt-gpu-retained-planes-20261013` | Retained GPU planes beneath the win32 GC (off by default) |
| 8 | #125 | `m3/superset-viewport-showcase-20261013` | Superset Viewport Showcase and the test matrix runner |
| 9 | #127 | `m3/swt-viewport-paint-edges-20261013` | Leaf 2 plan (no flicker, exact edges, scrollbars). Docs only, stacked on #124 |
| 10 | #128 | `m3/swt-layout-resize-20261013` | Leaf 3 plan (layout, resize, box model). Docs only, stacked on #127 |
| 11 | #129 | `m3/swt-list-combo-viewport-20261013` | List, Combo and CCombo: native owner-data viewport for `SWT.VIRTUAL` (win32 `LBS_NODATA`) |
| 12 | #130 | `m3/swt-ci-workflow-load-20261013` | CI: cut the fork's Actions load, guard secret-less publishers |
| 13 | #131 | `m3/swt-ole-example-version-bump-20261013` | Version bump for `org.eclipse.swt.examples.ole.win32` (3.110.200) |

#129, #130 and #131 were opened while this integration ran, and are merged after the list from the ledger.

Several heads moved while the merges ran. #119 was merged four times, #126 three times, and #125, #127 and #128
twice each. Each later merge brought in only the new commits.

## Not merged

| Item | Reason |
|---|---|
| GTK and Cocoa | Not built or run here. The merges are source-level for every platform, but only win32 x86_64 was compiled. |

## Conflicts and resolutions

| Merge | File, member | Integration side | Incoming side | Resolution |
|---|---|---|---|---|
| #126 onto #124 | `Eclipse SWT/win32/.../widgets/Control.java`, `releaseWidget()` | `EffectiveVisibility.release (this);` | `retainedFrame.dispose (); retainedFrame = null;` | Union. The #124 line comes first, then the #126 block. The two release independent state. |
| #126 onto #124 | `Eclipse SWT/win32/.../widgets/Display.java`, `releaseDisplay()` | stop `occlusionTracker`, clear `visDirty[]`, reset the visibility counters | `gpuBackend.dispose ()` in try/catch, then `gpuBackend = null` | Union. The #124 block comes first, then the #126 block. The two release independent state. |
| #129 onto #119 | `Eclipse SWT/common/.../widgets/VirtualListModel.java`, `setItems` | `items = Arrays.copyOf (...)` after `width.setAll` | the same, plus `width.setAllUnmeasured` for lazy models | #129 side. It is a superset of the integration side. |
| #129 onto #119 | `Eclipse SWT/common/.../widgets/VirtualSelectionModel.java`, the whole class | #119: a hybrid of ranges and bitsets over `VirtualBitLanes`, always on. Its cut-over is `max(1024, n/256)` runs. It adds `invert()`, `bitsMode()`, `representationSwitches()`, `bitsCutover()` and `MIN_BITS_RUNS`. | #129: a hybrid over its own `int[]` bitmap, opt-in through `VirtualSelectionModel(boolean)`. Its cut-over is `max(16, n/64)`. It adds `bitmapMode()`, and returns to ranges when the bitmap empties. Indices outside the model throw `IllegalArgumentException`. | Superset on the #119 engine. `VirtualSelectionModel()` keeps the #119 policy, which `Test_VirtualBitLanes` expects. `VirtualSelectionModel(true)` uses the #129 cut-over and `(false)` keeps ranges only, which `Test_VirtualSelectionModel_Hybrid` expects. The smoke results below say whether both tests pass. `bitmapMode()` is an alias of `bitsMode()`. Where the two sides disagreed, the newer #129 behaviour wins: out-of-model indices and ranges throw `IllegalArgumentException`, matching the range form; inserts and removes are validated the same way; and an empty bitset returns to ranges. |
| #129 onto #119, auto-merged file | `Eclipse SWT/common/.../widgets/VirtualViewportPlanner.java`, `VirtualViewportPlanner()` | #119: `new VirtualSelectionModel ()`, a hybrid | #129: `this (false)`, ranges only | The no-argument constructor keeps the #119 hybrid. Table and Tree planners call it. `VirtualViewportPlanner(boolean)` is #129's and is used by `VirtualListModel (true)`. Each side keeps the behaviour it chose for its own callers. This change is part of merge commit `a047b0098e`. |

Every other merge was clean.
