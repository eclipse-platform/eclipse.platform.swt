# Table viewport coverage (rule 15), build `tt20261019p6`

JaCoCo 0.8.15 over one gate run ([`table_coverage.sh`](../table_coverage.sh), invisible desktop): the Table suites and acceptance tests in both modes, the differential of the default mode against the official recording, the differential recording of the original mode, the headless lane tests, the branch acceptance tests (`Test_org_eclipse_swt_widgets_Table_ViewportBranches`, and the white-box `Test_org_eclipse_swt_widgets_Table_ViewportArms`: selection tracks under every notification shape and nesting, facades that are not rows, paint helpers, undrawn stages; synthesized `LVN_ODFINDITEM` shapes, selection snapshots beyond 4096 rows, released and weakly held facades, paint-facade array reuse, text direction, pack over empty, image and indent rows, trace logging on) and the R4_38 store tests of the original mode (`Test_org_eclipse_swt_widgets_Table_StockItems`). [`table_changed_coverage.sh`](../table_changed_coverage.sh) writes [`classes.tsv`](classes.tsv) (per class) and [`changed-lines.tsv`](changed-lines.tsv) (the lines this fork adds to upstream files, against tag `R4_38`).

The official recording of this gate was recorded afresh on the official jar (`org.eclipse.swt.win32.win32.x86_64-3.135.0.jar`, sha1 `872a552b`) with the tests of this build (88 seeds, header `reference official, SWT 4974 win32`).

Gate results of this run:

| Run | Result |
|---|---|
| Suites, default mode | 259 pass, 0 fail (6 skipped: the 5 store tests of the original mode, foreground alpha on win32) |
| Suites, original mode | 228 pass, 0 fail (37 skipped: tests that need the viewport or the item store of the default mode, foreground alpha on win32) |
| Differential, default mode against the official recording | fails on the pixel line after variant 7, seed 1; every step line before it matches (the run stops at the first failure, so variants 8 and 9 did not run in it) |
| Differential recording, original mode | 87 of 88 seeds equal to the official recording; variant 8, seed 3 differs in its pixel line only |

| Class | Line | Branch |
|---|---|---|
| `VirtualListWidth` | **100 %** (224/224) | **100 %** (144/144) |
| `VirtualCellLanes` | **100 %** (136/136) | **100 %** (160/160) |
| `TableViewport` | **100 %** (497/497) | **99.8 %** (478/479) |
| `TableStockItems` | **100 %** (176/176) | **100 %** (176/176) |
| `TableItem` | 85.4 % | 80.9 % |
| `TableColumn` | 62.9 % | 52.4 % |
| `Table` | 58.8 % | 50.7 % |
| `VirtualBitLanes` | 67.0 % | 54.2 % |
| `ScrollBar` | 28.2 % | 13.6 % |

Reading the numbers:

- `Table`, `TableItem`, `TableColumn`, `ScrollBar` and `VirtualBitLanes` are upstream or shared classes; their totals cover all of R4_38's code, most of which this leaf does not touch. `changed-lines.tsv` measures the fork's added lines instead, and its baseline (`R4_38`) still counts the whole fork delta (the superset's VIRTUAL item store, DPI and win32 changes), not only this leaf's hooks; the hooks themselves are one-line delegations into `TableViewport`, where they are measured.
- `VirtualBitLanes` is owned by the Tree viewport (its suites cover it; `Test_VirtualBitLanes` is headless and could be selected here too).
- `TableStockItems` (the R4_38 store of a VIRTUAL Table in the original mode) is covered by the API where an API sequence reaches it: the keyed and dense forms grown hidden, shown and with redraw off, compacted, scanned and shrunk at `WM_PAINT` (`keyedStoreLooksUpGrowsAndCompacts`, `denseStoreGrowsScansAndShrinks`, `growsByHalfWhileRedrawIsOff`). The arms no API sequence of a VIRTUAL Table reaches are driven white-box with their state forced (`guardsOfTheStockStore`, `armsNoApiSequenceReaches`): the bug 292199 count over a small array, a full array and a disposed item, lookups while the table is not VIRTUAL, a key whose item is gone, a compaction that already holds the row looked up, the keyed `getItems` loop to its end.

Open gap to 99 % (next leaf):

- `TableViewport` branches (99.8 %, 1 arm open): the `code == 0` arm after `LVM_DELETEITEM` in `destroy` (the stock check, kept): the ListView deletes any row below its count, and every pending append is brought up to date before the delete reaches it. Dead conditions were removed instead of tested (`Item.text` is never null; a count of zero after the early return always had rows; a table with rows always has a widest row), and the per-column reads go through `cell`/`cellInt`. The remaining guards are tested white-box with their state forced (`Test_org_eclipse_swt_widgets_Table_ViewportArms.defensiveGuardsAnswerSafely`).
