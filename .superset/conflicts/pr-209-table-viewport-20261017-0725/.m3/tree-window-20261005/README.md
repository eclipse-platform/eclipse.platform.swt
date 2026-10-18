Canonical Maven crate and replay resources: [com.synexia/tree-window-20261005](https://github.com/hsoliwal/com.synexia/tree/codex/swt-tree-window-20261005/synexia-openrewrite-recipes/recipe-crates/tree-window-20261005). Commands below run from that canonical crate. This directory holds the SWT delivery receipts.

# SWT window traversal recipe

`VirtualTreeVisibleProjection.window` used to seek from the root for every output row. This crate qualifies an original EPL modification of the existing SWT owner: skip cold ranges arithmetically, skip expanded subtrees using existing visible weights, and traverse the requested preorder window once. Existing parent/sibling lanes provide the return path. Traversal uses O(1) scalar scratch, with no retained cache, per-node wrapper, ancestor stack or new public API. The requested Row[] remains caller-owned.

Canonical recipe: `M3HashPinnedJavaSnapshotRecipe`. Runtime destination: `hsoliwal/eclipse.platform.swt`. This does not activate or replace a native backend. The GTK prefix-residency limitation remains open.

## Frozen experiments and serial delivery

`WORK_ORDER.json` freezes the bounded oracle and baseline. `TRIALS.json` retains the first passing, stack-based candidate and its refinement. Resources preserve each sealed definition instead of overwriting evidence. `swt-tree-window-final` is the default directly replayable three-target recipe: projection, new headless regression class, and default AllTests suite registration. `swt-tree-window-refine` records the actual v1-to-v2 refinement. Earlier definitions are historical trial recipes; they are not applied blindly to the final tree.

The final recipe generated all three files from sealed preimages. A replay generated zero changes. Each of the three deliberately drifted targets was refused. The final source is byte-identical whether reached directly or through the recorded serial refinements. No public/package projection signature or JNI declaration changes.

The independent eager oracle covers 64 frozen sparse forests, bounded window lengths including Integer.MAX_VALUE, expansion/count changes, rebind/prune, cold gaps, unknown/empty children, 4,294,967,294 logical rows and a 12,000-level chain. The tests run with a 256 KB Java stack in the focused crate. They passed before optimization and after both candidates. Four existing shared viewport regressions also passed with the final delivered source.

## Reproduce

Use Java 21 and Maven 3.9.11. Build/install the existing source-owned parser at `hsoliwal/nebula` commit `c9195dd46dc49242418e2fc1f7fad7ca7e9c1c31`, `m3/openrewrite-line-terminators`; its existing source and artifact hash preflight remains enabled. Parser coordinate: `com.synexia.recipe:openrewrite-line-terminators:8.90.4-m3-f3e4bfe19654`. No alternate parser or convergence engine is introduced.

From this crate, with absolute paths substituted:

```sh
python3 prepare_before.py /tmp/window-before
mvn verify -Dm3.source=/tmp/window-before -Dm3.output=/tmp/window-candidate
mvn -f contract-pom.xml clean test -Dm3.swt=/path/to/swt -Dm3.tests=/path/to/swt
mvn verify -Dm3.source=/path/to/swt
```

Candidate output stays outside the SWT checkout. Review and copy only the manifest-sealed outputs to their exact paths. The final SWT reactor command is:

```sh
mvn -f /path/to/swt/pom.xml -Pbuild-individual-bundles verify \
 '-Dtest=Test_VirtualTreeWindow,Test_ViewportRewriteContracts#sparseProjectionKeepsTenMillionColdCoordinates+deepProjectionUsesIterationAndRetainsHiddenState+rejectedTopologyRebindPreservesCoordinatesAndAggregates+projectionMatchesPreorderAfterCountAndExpansionChanges' \
 -Dsurefire.failIfNoSpecifiedTests=false
```

Run the existing `M3JavaStatementCompositionCli` over the three exact manifest paths to a new external packet directory. Then run `coverage.py <swt> <packets> <new-external-composed-index-directory>` and the existing `RestoreAtoms` with the composed index/manifest. The coverage proof reuses all earlier exact packets and publishes only this leaf's three payloads; it does not duplicate a full repository index after each leaf change.

## Evidence and limits

The final 22-module GTK reactor build/verify passed with nine selected tests, zero failures/errors/skips. This is not a rerun of all 4,494 native/UI tests from the prior base. All 2,472 current Java files / 372,035 behavioral atoms restore exactly; all 124 native files and 416 PNG files are unchanged from that previously qualified base. Three changed/new files received fresh canonical packets and pattern receipts.

`WindowCost.java` compares the unchanged rowAt loop against the new window in one JVM, checks equal rows, separates preparation, warms both paths, alternates order, and reports seven samples of 200 calls. The final local median ratios were 30.56x for a 64-row window after 2,000 materialized sparse siblings and 12.31x for a 32-row window at the end of a 12,000-level chain. These are two bounded workload observations, not a universal widget speedup; the cold preparation and full samples are retained.

Windows/macOS native execution, full GTK UI rerun, bounded native Tree residency, canonical full-reactor/strict repository promotion and repository-owned offline dependency closure are not established. Package signature equality, finite corpus convergence and complete atom coverage do not substitute for those gates. No native binaries or generated JNI sources are committed.
