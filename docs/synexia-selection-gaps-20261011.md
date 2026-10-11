# Selection gap enumeration receipt

Canonical Synexia recipe/evidence:
https://github.com/hsoliwal/com.synexia/pull/10215

Pinned canonical commit: `ee69c23e1b12ddef404c0296494fe4655cdf9c2d`.
Crate: `synexia-openrewrite-recipes/recipe-crates/swt-selection-gaps-20261011`.
SWT preimage: `3a61904b21017412ba18a789b74cc0615fd1c1e7`.

The two actual SDK stages extract a private range-emission atom and reuse it to emit
the complement gaps between normalized excluded ranges. This avoids scanning unselected
logical rows, preserves sorted fresh arrays and all public/protected runtime descriptors.
The third generated Java file wires the new JUnit regression into the existing `AllTests` suite.

| Generated path | Final SHA-256 |
| --- | --- |
| `bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/internal/ViewportRuntime.java` | `afea88e36a79d3212f590fe9d1bfd88fc6a30c12c45fc9af54adb231ed49a6fa` |
| `tests/org.eclipse.swt.tests/JUnit Tests/org/eclipse/swt/tests/junit/AllTests.java` | `af627b6470e76539011cb9024047a1eff7b51dee4c5320fb7dbf47adfbbfc830` |
| `tests/org.eclipse.swt.tests/JUnit Tests/org/eclipse/swt/tests/junit/Test_ViewportSelectionEnumeration.java` | `da830555b26ef71042523918654d24001a2893460f166a920c357431a9b268c1` |

Qualification: offline Maven PASS; Java 21 compilation, six frozen JUnit tests on baseline
and each of two stages; 4,094 exhaustive states and 8,192 seeded mutation steps per stage;
maximum logical count; public/protected javap equality; both fixed points; four source and
two template drift refusals. All existing suite entries are retained. No native stub is used.

Measured model-only workload: 100,000,000 logical rows and three selected rows; three isolated
JVM forks, equal nine-export batches. Median warm thread CPU 74.25 ms -> 1.14 us; 32 allocated
bytes/export in both. Separate cold preparation/export, observed heap and peak process RSS
are retained in the pinned canonical evidence. Tiny candidate timings include observer overhead.

**Pending:** full target Maven/Tycho suite, platform/native/JNI integration and SWT capture/OpenCV
plus widget CPU/heap/RSS acceptance; canonical every-module task/serial full-review/strict admission.
This receiving draft has no promotion authority. It does not establish complete SWT/Nebula
distillation or lower any existing resource/visual gate. No native code is modified.

Recipe source, fixtures, manifests and reusable proof tooling remain Synexia-owned. Existing SWT
and suite source retain EPL-2.0; the new independent test is Apache-2.0 with first-party attribution.
Generation and source reuse do not relicense upstream SWT or its assets.
