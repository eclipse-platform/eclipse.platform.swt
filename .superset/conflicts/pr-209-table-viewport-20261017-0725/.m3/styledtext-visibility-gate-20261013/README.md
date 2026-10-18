# StyledText visibility gate (2026-10-13)

Design: [docs/m3/STYLEDTEXT_VISIBILITY_GATE.md](../../docs/m3/STYLEDTEXT_VISIBILITY_GATE.md).
Stacked on leaf 2 (`m3/styledtext-api-coverage-20261013`, SWT #118); recipe preimages: `8b7c3dfd4d`.

## Contents

* `FOSS_REUSE_DECISION.tsv`: in-repo reuse (win32 `Control.redrawInPixels` early-out), the pending
  effective-visibility bit, pattern donors (CodeMirror 6 in-view measure gating, CSS
  `content-visibility`, Android `View` visibility) and names-only Qt/GTK rows (`LICENSE_REVIEW_REQUIRED`).
* `evidence/`: JUnit tree logs of the gate test (4 MiB default and 100 MiB), the gate report TSV and the
  regression suite logs.
* `recipe/`: the leaf as a hash-pinned `M3HashPinnedJavaSnapshotRecipe` crate
  (`swt-styledtext-visibility-gate-20261013`), epoch 3 over the epoch 2 postimages.

## Report columns (`evidence/gate-report-*.tsv`)

case, wrap, chars, lines, layouts_while_hidden (always 0: asserted), catch_up_layouts,
reference_layouts (a widget visible throughout, same operations), hidden_ms (setText plus edits while
hidden), reference_ms (the same on the visible widget).

## Reproduce

```sh
JUNIT_DETAILS=tree sh .m3/styledtext-viewport-20261013/bench/run-tests.sh <classes> <test-classes> <reports> -Xmx3000m \
  -Dswt.styledtext.visibilityGate.mib=100 -Dswt.styledtext.visibilityGate.report=<file> -- Test_org_eclipse_swt_custom_StyledText_VisibilityGate
```
