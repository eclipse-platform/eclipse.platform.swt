# SWT lazy data (#119): tests to run after the pause

Tests were paused on the user's request ("stop running the tests ... we'll test after the
interview"). Everything below is written and committed but **not run** on the latest head unless
a result is recorded in the last column. Run one JVM at a time, after the memory gate (free
physical >= 4 GB, commit headroom >= 8 GB), `-Xmx2g` or less. Scripts live in
`C:\m3-work-c\lazy\scripts` (outside the repository).

| # | lane | command | expectation | last result |
|---|---|---|---|---|
| 0 | compile-check of the changed sources + the #122 owners | `bash scripts/check-compile.sh` | exit 0, no errors | PASS on d5db98c7 and on the bulk-batch commit (javac -J-Xmx512m, 27 files, 3-9 s) |
| 1 | incremental build into `after2-classes` | `sh scripts/gated.sh sh scripts/inc-compile2.sh` (add `internal/swap/*.java`, `internal/PackedHeapGrowth.java`, `VirtualBlockSpill.java` to the list) | exit 0 | NOT_RUN on latest |
| 2 | functional + headless lanes | `sh scripts/junit2.sh after runs/func -- Test_VirtualBitLanes Test_org_eclipse_swt_widgets_VirtualPageSource` | all pass (26 tests incl. the new swap, debounce and paint-materialization tests) | 19/20 on 717242c4 before the debounce isIdle fix (the debounce test failed: `isIdle` ignored the armed timer; fixed, not re-run) |
| 3 | viewport headless suites before/after | `Test_ViewportRewriteContracts Test_ViewportSelectionEnumeration Test_VirtualListModel Test_ViewportRowWindow Test_VirtualTreeTopology Test_VirtualTreeWindow Test_VirtualRowExtents` on base and after | identical | 54/54 before, 54/54 after (717242c4 classes) |
| 4 | existing widget suites before/after | `Test_org_eclipse_swt_widgets_{Table,TableItem,TableColumn,Tree,TreeItem,TreeColumn,TabFolder,TabItem}` + `Test_org_eclipse_swt_custom_{CTabFolder,CTabItem}` | after has no test that passed before and fails after | before: 684 found, 539 passed, 140 failed, 4 aborted, 1 skipped (pre-existing failures, mostly "Argument cannot be null" in the test fixture); after: NOT_RUN on latest (the run on ed11ec59 classes was stopped) |
| 5 | scale, classic API on unmodified classes | `junit2.sh before runs/scale-before -Xmx2g -Dswt.lazy.scale=true -Dswt.lazy.mode=before -Dswt.lazy.report=runs/scale.tsv -- Test_org_eclipse_swt_widgets_VirtualPageSource_ScaleClassic` | metrics rows | stopped at start (pause) |
| 6 | scale, classic API on the lazy branch | same with `after` and `-Dswt.lazy.mode=after-classic` | same as 5 (default paths unchanged) | NOT_RUN |
| 7 | scale, page source | `junit2.sh after runs/scale-after-paged -Xmx2g -Dswt.lazy.scale=true -- Test_org_eclipse_swt_widgets_VirtualPageSource_ScalePaged` | 10M table: frames under 16.7 ms, facades during the storm = 0, resident blocks <= 64; 1M tree; tabs: 1 tab at startup | NOT_RUN |
| 8 | billion-node tree at flat memory | `junit2.sh after runs/billion -Xmx256m -Dswt.lazy.scale=true -Dswt.lazy.billion=true -- 'Test_org_eclipse_swt_widgets_VirtualPageSource_ScalePaged#treeBillionNodesFlatHeap'` | heap and RSS flat across 2M / 100M / 1B logical nodes; expand-all never forced | NOT_RUN |
| 9 | examples lane | `sh runs/examples.sh` (ControlExample Table/Tree/TabFolder, CustomControlExample CTabFolder, Snippets 144 151 192 201 202 76 165 266, before vs after) | `EXAMPLES IDENTICAL` | NOT_RUN |
| 10 | showcase matrix | `run-matrix.sh` of the showcase PR once it lands on master | table/tree/tab_folder rows | NOT_RUN (not on master yet) |
| 11 | recipe crate | `mvn -B -o -ntp -f synexia-openrewrite-recipes/recipe-crates/swt-lazy-data-20261013/pom.xml verify` after `make_crate.py` regenerates the resources from the final head | 3/3: exact postimages, fixed point, drift and missing-source refusal | NOT_RUN |
| 12 | flicker | `test_treeBulkChildCountIsBatchedWithPreciseDamageOnly` + | count full-client invalidations and erase frames for small (one block) and large (setItemCount jump, select-all over a screen) operations | lazy layer: 0 full-client invalidations (precise row damage only) | NOT_RUN |

Results already measured (earlier heads, same code paths unless noted):

- `Test_VirtualBitLanes`: 6/6 on 95b4e750; 10M check-all 5.1-6.6 ms, uncheck-all 1.5-2.3 ms,
  insert-shift 1.3-2.2 ms; selection 10M select-all 0.4-0.8 ms, invert 0.10-0.22 ms, shift-click
  3.9-6.2 ms; scattered-selection cut-over table in the PR body.
- `Test_org_eclipse_swt_widgets_VirtualPageSource`: 13/13 on 95b4e750 (before the debounce,
  swap and paint-materialization tests were added).

Added after RESUME TESTS:

| # | lane | command | expectation | last result |
|---|---|---|---|---|
| 13 | busy-indicator API wait | a 300 ms/block source, `getItem (far).getText ()` | `BUSY_WAITS` = 1, result identical | NOT_RUN |
| 14 | generation tokens | `invalidate ()` while blocks run | `STALE_DROPPED` > 0, no stale text applied | NOT_RUN |
| 15 | drain-turn budget | 10M slow-source storm | p95 of `recentTurnMicros ()` <= 4000 us, `REPOSTS` counted | NOT_RUN |
| 16 | showcase matrix with this branch | `run-matrix.sh <jar of after2-classes> ... swt-pr119` | Table/Tree `items_materialized_per_paint` 0 with a page source; classic SetData rows unchanged (see design 5b) | NOT_RUN |
| 17 | Cocoa placeholder data source | macOS CI legs | `Test_org_eclipse_swt_widgets_Tree` sparse-residency asserts (lines 367, 1444) | NOT_DONE (code not written; macOS only) |

