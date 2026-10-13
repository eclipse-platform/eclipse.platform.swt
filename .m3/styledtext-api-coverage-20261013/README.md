# StyledText API coverage under the viewport (leaf 2, 2026-10-13)

Design and laws: [docs/m3/STYLEDTEXT_API_COVERAGE.md](../../docs/m3/STYLEDTEXT_API_COVERAGE.md).
Stacked on leaf 1 (`m3/styledtext-viewport-20261013`, SWT #115, unmerged); base for the recipe
preimages: `985109317d`.

## Contents

* `STYLEDTEXT_API_COVERAGE.tsv`: one row per API item (215 public/protected methods incl. anonymous
  accessibility/listener/timer methods, 44 `ST` constants, 76 `case ST.` paths, widget features,
  example runs, one-widget checks, platforms, Qt gap rows). Columns: kind, api, source_line,
  cost_class, paged_vs_gap_test_id, scale_test_id, status, qt_equivalent, example_source, note.
* `coverage/generate_coverage.py` + `coverage/inputs/`: the CPU-only generator; inputs are the
  mechanical inventory (`STYLEDTEXT_PUBLIC_API.tsv`, `ST_CONSTANTS.tsv`, `CASE_ST.tsv`), the JUnit tree
  logs of the differential and scale tests, the example exerciser report and the snippet sources.
* `examples/StyledTextExampleExerciser.java`: runs SWT examples/snippets unmodified and drives each
  multi-line StyledText on a 1,000,000-line document; CustomControlExample's StyledText tab toggles each
  style checkbox.
* `evidence/`: test tree logs, scale report, examples report, summaries.
* `recipe/`: the leaf as a hash-pinned `M3HashPinnedJavaSnapshotRecipe` crate
  (`swt-styledtext-api-coverage-20261013`), epoch 2 over the leaf 1 postimages.
* `FOSS_REUSE_DECISION.tsv`: leaf 1 ledger plus editor donors (VS Code piece tree, CodeMirror 6
  heightmap, AvalonEdit HeightTree, Scintilla Partitioning), pattern-only GPL/LGPL kits (JavaFX
  VirtualFlow, Swing text, GtkTextView) and the Qt API map (`LICENSE_REVIEW_REQUIRED`).

## Reproduce

```sh
# differential (paged vs gap) and scale (1M lines), JUnit console, Win32 host
JUNIT_DETAILS=tree sh .m3/styledtext-viewport-20261013/bench/run-tests.sh <classes> <test-classes> <reports> -Xmx768m -- Test_org_eclipse_swt_custom_StyledText_ApiDifferential
JUNIT_DETAILS=tree sh .m3/styledtext-viewport-20261013/bench/run-tests.sh <classes> <test-classes> <reports> -Xmx1200m -Dswt.styledtext.scaleTest=true -- Test_org_eclipse_swt_custom_StyledText_ApiScale
python -B .m3/styledtext-api-coverage-20261013/coverage/generate_coverage.py coverage/inputs <diff-tree.log> <scale-tree.log> STYLEDTEXT_API_COVERAGE.tsv <examples-report.tsv> <single-control-status> <example source roots...>
```
