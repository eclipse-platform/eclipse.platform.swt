# Synexia sparse tree / viewport handoff — 2026-10-11

Canonical recipe PR: https://github.com/hsoliwal/com.synexia/pull/10242
Exact recipe/evidence commit: `8b016d69d4e35f5d12a4fd055ef0bce2d94c1f77`
SWT source baseline: `3a61904b21017412ba18a789b74cc0615fd1c1e7`
Existing receiving parent: `ea8f8d333e7ded676d2e50ce319d663c036ab07e`

This additive receiver preserves the prior selection change and adds the actual SDK-generated
`VirtualTreeTopology` output and its four new regressions in the existing topology test class.
The test class was already included by SWT's AllTests; the selection suite entry remains present.
Reusable recipe source, fixtures, manifests, rejected candidates and mastery evidence stay in Synexia.

## Exact execution binding

- Existing owner: `com.synexia.rewrite.M3HashPinnedJavaSnapshotRecipe`.
- Ordered qualified stages: tree shift atomization -> sparse sibling patternization ->
  selection atomization -> selection gap enumeration.
- Terminal task: `com.synexia.rewrite.ApplySwtSparseViewportTask20261011`, crate
  `swt-sparse-viewport-task-20261011`. Its five generated files equal the ordered composition.
  Replay is unchanged and all five source-drift probes are refused.
- Task packet: `synexia-openrewrite-recipes/recipe-crates/swt-tree-sibling-shifts-20261011/`.
- Input manifest SHA-256: `c5850510cc68dd93437480ffc03e77fa7240459da6f1e05151f18298f80bac86`.
- Final receipt SHA-256: `925045f3c4b85cea4ba25b6dff45479f8c86e68e406c5d3ae18066260427ce49`.
- API descriptor SHA-256: `449ea2004aad288efaf80f5bb3b9f6230c25a19997df89e6eadb1af23d83d850`.
- Tree source SHA-256: `f5f0c14f1648cee88543709af404f176189f81181b4a8ef297cccd569a56ba9b`.
- Topology test SHA-256: `7bbe3cb2c3597501e3ee99d94bd49e48274e1da3e5b61a816652870c9040e619`.

Offline Maven 3.9.11 / pinned Java 21 qualification passed. There are 20 distinct JUnit test
methods in the combined suite and 102 positive test executions across six accepted images.
Every image compiles and preserves public/protected/package-private descriptors. The topology
suite checks 4,096 seeded insertion/removal operations per stage, existing deep-tree/window
behavior, native snapshot columns, generation and exact exception partial state. A naive
sibling-traversal candidate compiles but fails the independently frozen overflow test; that
candidate and diagnostic remain in Synexia, never in receiving production source.

## Resource observations and open gates

Sparse topology uses existing sibling links rather than scanning empty ID capacity. Overflow
preflight retains the original ID-ordered mutation/exception state. Dense and invalid-parent
paths retain the original scan. There is no added retained index/cache or JNI interface.

Three-fork median model CPU per insert/release pair: sparse capacity 500001, 971173 -> 682 ns;
dense capacity 6, 97.6 -> 139.7 ns. The final dense median is slower; short-loop/JIT variation
is material and earlier refined runs differed. No dense or full-widget speedup is claimed.
CPU, cold preparation, warm allocation, after-GC heap and process peak RSS are recorded separately
in the pinned packet. These Java-model observations do not establish native widget residency.

Target Tycho/platform/JNI runtime tests, automated SWT capture/OpenCV checks, widget CPU/retained-heap/
native-memory acceptance, and canonical strict every-module/serial full-review admission remain open.
No manual QA substitute, lowered threshold, or full SWT/Nebula completion claim is introduced.
Java native-payload column checks do not prove GTK/JNI execution.

Derived SWT code/test postimages retain EPL-2.0 and upstream notices. First-party Synexia recipes
and adapters retain Apache-2.0. No challenge/editorial or Apache Xalan source body is imported.
