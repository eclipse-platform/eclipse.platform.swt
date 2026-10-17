# GTK virtual Tree frontier growth: qualified recipe candidate

The 500,000-root manual stress scenario stalled while constructing the native sibling prefix for `getItem(250000)`. Native thread samples at 15 and 30 seconds stopped in `gtk_tree_store_set` / `gtk_tree_store_append` from `ensureVirtualNativeChildren`. The original 90-second run ended before the middle-root capture.

This leaf recipe inserts identical cold placeholders after the fixed original resident tail, through the existing GTK JNI binding. Existing native iterators, exposed item coordinates and item identities retain their positions. New sentinel rows have no payload or public facade. A scoped native iterator is released in `finally`; no persistent cache, alternate topology or public API is introduced. This removes repeated walks through the newly growing suffix. It does not remove the original resident-prefix walk or make distant indexed access use bounded native memory.

The regression preserves root/child identities, count/index/text, selection, expansion, checked/grayed state and exactly the demanded SetData indices across growth. The manual harness now requires its cold branches to load children and actually expand, reports native root residency, and uses the current viewport diagnostic methods. Existing strict native collapse/restore tests are retained.

The branch includes master `05161c82b6ab8584d70ad7e8dff042c91438ecb3` additively, including sparse bulk traversal, the dormant logical GTK Tree model and shared viewport owners. The integrated compiler found that upstream RowWindow called a missing `saturatedAdd`; this recipe adds the private nonnegative saturating-add helper. Existing Long.MAX_VALUE viewport tests remain authoritative. The dormant native model was compiled into the GTK JNI library, but this change does not activate it.

## Reproduce

Use Java 21 and the repository-owned `openrewrite-line-terminators:8.90.4-m3-f3e4bfe19654` parser dependency already used by the statement composition crate. From the canonical checkout:

```sh
mvn -f synexia-openrewrite-recipes/recipe-crates/gtk-tree-frontier-growth-20261005/pom.xml verify -Dm3.source=/absolute/pinned-swt-checkout -Dm3.output=/absolute/new-candidate-directory
```

This compiles the existing M3HashPinnedJavaSnapshotRecipe and ApplySnapshot owners and runs them at integration-test. The source must match every pre/post seal in the manifest; unrelated target edits are refused. Candidate output is outside the input repository. A second pass must be fixed, and deliberate source drift must be refused. Tests and native execution in the SWT fork qualify behavior separately.

The default `swt-gtk-frontier-growth` manifest targets the integrated master pin (four sources). The retained `swt-gtk-frontier-growth-base70` manifest records the original three-source candidate at `7f328b38ed98fe825b7bd2ebb07978095c79c567`; select it with `-Dm3.crate=swt-gtk-frontier-growth-base70`. No earlier receipts or ancestry are discarded.

SWT verification uses `mvn -Pbuild-individual-bundles verify -Dswt.viewport.screenshotRegression=true -Dswt.viewport.screenshots=<absolute-output>` with actual GTK3/JNI libraries and a display. Compile and run the existing `ViewportRewriteStress` main with `-Dswt.viewport.screenshots=<output> -Dswt.viewport.screenshots.exit=true` for all 20 scenes. The focused test selection is `Test_org_eclipse_swt_widgets_Tree,ViewportScreenshotRegressionTest`.

## Proof boundary

`evidence.json` records executed counts, pins and log hashes. The atom archive in the SWT PR contains exact supplemental packets for the four changed sources (including compilation-unit roots); older whole-repository receipts remain tied to their original source pins. Public/protected Tree declarations are unchanged. No compiled native binary is committed.

This is an insertion-work optimization, not a claim of perfect SWT efficiency. The middle-root scenario still retains roughly 250,000 native roots. Bounded native residency requires completing the existing dormant logical-model integration with selection, accessibility, expansion, event-order and platform proofs. Native C semantic distillation, GTK4, Windows/macOS runtime execution and strict canonical whole-repository promotion are not established by this crate.
