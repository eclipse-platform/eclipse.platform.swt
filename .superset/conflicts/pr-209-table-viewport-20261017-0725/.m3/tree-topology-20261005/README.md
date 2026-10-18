Canonical recipe: [com.synexia/tree-topology-20261005](https://github.com/hsoliwal/com.synexia/tree/codex/swt-tree-topology-20261005/synexia-openrewrite-recipes/recipe-crates/tree-topology-20261005). Reproduction commands run from that canonical crate.

# Stack-safe SWT topology recipe

This canonical Maven/OpenRewrite task crate repairs two remaining recursive private walks in `VirtualTreeTopology`: flag lookup and subtree deletion. Four tests reproduce `StackOverflowError` at depth 20,000 on the sealed baseline with a 256 KB Java stack. The bounded eager-model oracle already passes before the repair. All five tests pass afterward.

The candidate reuses the existing primitive parent/first-child/sibling lanes. Flag lookup walks preorder but stops at the queried subtree boundary. Deletion removes leaves before parents; unlinking advances the first-child lane. Both use constant scalar scratch, no recursion, auxiliary stack or temporary node wrappers. Existing visible-weight propagation, rebind validation, sparse cold coordinates, stable ids and native snapshot layout remain intact.

## Recipe and proofs

Owner: `M3HashPinnedJavaSnapshotRecipe`; crate: `swt-tree-topology`. Exact source baseline: SWT `9b27ddd5de4a01ab40a9cf96cf81ff212927d50e` (PR #78). Three sealed targets are the existing topology, an absent-before regression class, and registration in AllTests. Historical hashes, frozen depth/seeds/operation families and contracts are in WORK_ORDER.json and the resource manifest.

Generation produces three exact Java candidates. A subsequent run produces zero changes. Each deliberately drifted target is refused. The baseline's four failing deep-tree cases are retained as red evidence; fixed candidate tests exercise forget, release, child-count pruning, subtree-boundary flag lookup, stable-id reuse, surviving sibling identity/flags, visible aggregates and the JNI-facing snapshot columns. Bounded eager-model comparisons use 32 fixed seeds over three removal operations; an empty generated forest has no removable node and is a non-applicable operation case.

The final clean 22-module reactor build compiles the Java backend sources and passes 14 selected headless tests: five new topology tests, five prior window tests and four existing viewport contracts. Package signatures compare exactly. All 2,473 current Java packets / 372,229 behavioral atoms restore byte-for-byte. All 124 native files and 416 PNGs are unchanged from the qualified parent. These are correctness and coverage results; no runtime speedup is claimed for this repair.

## Reproduce

Use Java 21 / Maven 3.9.11 and the same pinned, source-owned OpenRewrite provider documented by `../tree-window-20261005/README.md` (Nebula c9195dd46dc49242418e2fc1f7fad7ca7e9c1c31, provider coordinate 8.90.4-m3-f3e4bfe19654).

```sh
mvn verify -Dm3.source=/absolute/swt-parent -Dm3.output=/new/external/candidates
mvn -f contract-pom.xml clean test -Dm3.swt=/absolute/swt -Dm3.tests=/absolute/swt
mvn verify -Dm3.source=/absolute/swt
mvn -f /absolute/swt/pom.xml -Pbuild-individual-bundles clean verify \
 '-Dtest=Test_VirtualTreeTopology,Test_VirtualTreeWindow,Test_ViewportRewriteContracts#sparseProjectionKeepsTenMillionColdCoordinates+deepProjectionUsesIterationAndRetainsHiddenState+rejectedTopologyRebindPreservesCoordinatesAndAggregates+projectionMatchesPreorderAfterCountAndExpansionChanges' \
 -Dsurefire.failIfNoSpecifiedTests=false
```

Review/materialize only recipe-produced candidates. Run the existing `M3JavaStatementCompositionCli` for the three exact manifest paths. `coverage.py <swt> <new-packets> <new-external-index-directory>` composes the initial complete coverage, prior window delta and this leaf. Run the existing `RestoreAtoms` against that composed index and manifest. Prior receipts remain immutable historical evidence; no full index is copied into each leaf.

Full GTK native/UI tests, Windows/macOS native execution, bounded GTK Tree native residency, strict canonical repository promotion and offline dependency closure remain open. This leaf does not activate a replacement native model. Native source identity and a stable snapshot shape do not establish native runtime execution.
