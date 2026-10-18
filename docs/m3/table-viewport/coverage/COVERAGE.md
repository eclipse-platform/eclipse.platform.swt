# Table viewport coverage (rule 15), build `tt20261017p2`

JaCoCo 0.8.15 over one gate run ([`table_coverage.sh`](../table_coverage.sh), invisible desktop): the Table suites and acceptance tests in both modes, the differential of the default mode against the official recording (88 seeds, exactly the 155 listed steps accepted), the differential recording of the original mode (equal to the official recording line for line, 88 seeds), , the headless lane tests and the branch acceptance tests (`Test_org_eclipse_swt_widgets_Table_ViewportBranches`, and the white-box `Test_org_eclipse_swt_widgets_Table_ViewportArms`: selection tracks under every notification shape and nesting, facades that are not rows, paint helpers, undrawn stages; synthesized `LVN_ODFINDITEM` shapes, selection snapshots beyond 4096 rows, released and weakly held facades, paint-facade array reuse, text direction, pack over empty, image and indent rows, trace logging on). [`table_changed_coverage.sh`](../table_changed_coverage.sh) writes [`classes.tsv`](classes.tsv) (per class) and [`changed-lines.tsv`](changed-lines.tsv) (the lines this fork adds to upstream files, against tag `R4_38`).

| Class | Line | Branch |
|---|---|---|
| `VirtualListWidth` | **100 %** (224/224) | **100 %** (144/144) |
| `VirtualCellLanes` | **100 %** (136/136) | **100 %** (160/160) |
| `TableViewport` | **100 %** (495/495) | 91.3 % (454/497) |
| `TableStockItems` | 92.6 % (163/176) | 77.3 % (136/176) |
| `TableItem` | 85.1 % | 79.6 % |
| `TableColumn` | 62.9 % | 51.8 % |
| `Table` | 54.0 % | 46.0 % |
| `VirtualBitLanes` | 67.0 % | 54.2 % |
| `ScrollBar` | 28.2 % | 13.6 % |

Reading the numbers:

- `Table`, `TableItem`, `TableColumn`, `ScrollBar` and `VirtualBitLanes` are upstream or shared classes; their totals cover all of R4_38's code, most of which this leaf does not touch. `changed-lines.tsv` measures the fork's added lines instead, and its baseline (`R4_38`) still counts the whole fork delta (the superset's VIRTUAL item store, DPI and win32 changes), not only this leaf's hooks; the hooks themselves are one-line delegations into `TableViewport`, where they are measured.
- `VirtualBitLanes` is owned by the Tree viewport (its suites cover it; `Test_VirtualBitLanes` is headless and could be selected here too).

Open gaps to 99 % (next leaf):

- `TableViewport` branches (91.3 %, 43 arms open): closed since 76.5 % by the branch acceptance and white-box tests. Still open: (a) combined conditions whose other operand order no input reaches (`focusOnly` with both bits, the weak-slot bounds in `rowsRemoved` and `textDirectionChanged`, `store` with arrays shorter than the columns); (b) defensive guards no stock path reaches (`code == 0` after `LVM_DELETEITEM`, `DISPOSE_SENT` during a facade request, an `LVM_SETCOLUMNWIDTH` with a negative width outside `pack`); (c) `AUTO_TEXT_DIRECTION` rows loaded into a facade without a live one (531). Each is listed by line in `tv-missed.tsv` of the run.
- `TableStockItems` (92.6 % line): `_checkShrink` and the DPI loop run only on paths the suites of the original mode do not drive yet.
