# StyledText viewport leaf 1 (2026-10-13)

Design: [docs/m3/STYLEDTEXT_VIEWPORT_DESIGN.md](../../docs/m3/STYLEDTEXT_VIEWPORT_DESIGN.md).
Donor ledger: [FOSS_REUSE_DECISION.tsv](FOSS_REUSE_DECISION.tsv).
Base: master `5094846b2fa693b3889e81d61c1e122d1ed78421`.
Canonical recipe: hsoliwal/com.synexia PR #10808, crate
`synexia-openrewrite-recipes/recipe-crates/swt-styledtext-viewport-20261013`
(`M3SwtStyledTextViewport20261013Recipe`, resources `swt-styledtext-viewport-20261013-v1`,
`manifest.tsv` SHA-256 `7f22cbd3909c070afe05ac0ad2502e1efe3d326c7accee83a4c48153a40e7a9f`).
`recipe/` here is the pinned authoring copy (same targets and hashes).

## Change

* `PagedContent` (new, package-private): default `StyledTextContent`, a balanced piece tree
  adapted from Synexia `MIndexStyledTextDocument` (Apache-2.0) with `DefaultContent` edit
  semantics, lazy bounded per-piece line offsets and a parallel page count on `setText`.
  `-Dorg.eclipse.swt.custom.StyledText.content=gap` restores `DefaultContent`.
* `StyledTextRenderer`: in-place range resets (no `TreeSet<Integer>`, no per-line objects);
  viewport mode above 65,536 lines with variable line height: O(1) estimated extent and
  `ViewportRuntime.RowWindow` (visible + one viewport overscan) idle measurement.
* `StyledText`: default content factory; viewport-mode estimates for distant geometry
  (`getVerticalScrollOffset`, `getLinePixel`, `getAvailableHeightAbove/Bellow`,
  `calculateTopIndex`), idle window re-targeted on scroll.
* Tests: `Test_org_eclipse_swt_custom_StyledText_PagedContent` (registered in
  `AllWidgetTests`), opt-in `StyledTextViewportProofTest`.

No public or protected API changes; no JNI or native changes.

## Evidence in this directory

* `bench/` measurement harness (`StyledTextViewportBench`, bench-only
  `org/eclipse/swt/custom/StyledTextViewportProbe`, `run.sh`, `run-tests.sh`,
  `summarize.py`), raw rows `bench/results-matrix.tsv`, `bench/summary.md`, test summaries.
* `recipe/` the leaf as an `M3HashPinnedJavaSnapshotRecipe` crate
  (`swt-styledtext-viewport-20261013`): `generate_crate.py` seals preimage/postimage hashes,
  `verify.sh` compiles the canonical Synexia owner and runs `ApplyStyledTextViewportRecipe`
  (exact postimages, second-run fixed point, drift and missing-source refusal); receipt in
  `recipe/receipt.json`.

## Reproduce (Windows, JDK 21, SWT 3.135 win32 jar with signatures stripped)

```sh
export JAVA_HOME=".../jdk-21" SWT_JAR=.../swt-3.135.0-unsigned.jar
javac -d after-classes -cp $SWT_JAR "bundles/org.eclipse.swt/Eclipse SWT Custom Widgets/common/org/eclipse/swt/custom/"*.java \
  "bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/custom/StylePolicy.java" \
  "bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/internal/ViewportRuntime.java"
BENCH_OUT=results.tsv sh .m3/styledtext-viewport-20261013/bench/run.sh after-classes after 100 wrap 3g 30000
```

The class directory shadows `org.eclipse.swt.custom` in the jar (the fork's `ViewportRuntime`
and `StylePolicy` are compiled alongside; the jar's natives are 4974r10, the fork's Java
platform layer is not used).
