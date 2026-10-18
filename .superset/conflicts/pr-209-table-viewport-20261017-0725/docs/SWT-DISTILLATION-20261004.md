# Complete-tree distillation and verified repair candidate

The existing Maven/Tycho reactor and canonical OpenRewrite SWT composite were used on base `502e27cba93215df6d1b4e2dafdb2f1413ca3560`. Thirteen sealed source overlays repair Java-21/parser bootstrap issues and GTK Tree contracts while retaining the merged z-order, shared viewport runtime, bounded Tree frontier, StyledText, and ScrolledComposite work.

## Coverage

| Evidence | Result |
|---|---:|
| Tracked donor files with path/length/SHA-256 custody | 3,701 |
| Java source files | 2,455 |
| Java files with declarations mapped | 2,455 |
| Parser errors / unresolved Java declarations | 0 / 0 |
| Other UTF-8 text/native files | 590 |
| Opaque binary files, hash custody only | 656 |
| Canonical Java atom rows | 59,269 |
| JNI contract rows | 3,592 |

All 3,045 text inputs were processed in 55 serial batches. The original source bytes were checked before and after inventory. Non-Java inputs remain explicitly unsupported for semantic replacement; unresolved method-call targets remain in the graph. This completes the raw distillation pass, not all M3 absorption or promotion.

## Repairs

* Rename unused `_` lambda parameters and align SWT Tools BREE/source/target with the Java 21 baseline.
* Express the three Display Javadocs without the parser-incompatible inline return form.
* Preserve Tree logical counts during indexed insertion beyond the resident prefix and append; bind coordinates before exposing item identity.
* Preserve an explicitly requested top item; stop obsolete queued frontier growth after collapse/shrink/disposal; restore redraw when clearing a nonvirtual Tree.
* Repair test compilation and expectations using public APIs, correct numeric types, platform storage owners, and multi-selection style where the test requires it.

Four new GTK regressions cover insertion/append beyond the initial prefix, queued shrink, queued collapse, and nonvirtual redraw restoration. Existing Table and Tree tests continue to exercise selection, identity, clear/count behavior, expansion, logical scrolling, and sparse topology.

## Verification

| Gate | Result |
|---|---|
| Full Maven/Tycho compile | 22 modules pass on Java 21 |
| Canonical recipe, attribution, export, and admission tests | 18 pass |
| GTK Table/Tree and viewport contracts | 334 pass, zero skips |
| Screenshot regression captures | Nine inspected |
| Javadoc basher | Pass; zero source changes |
| JNI generator | Pass; zero generated-source changes |
| Exact sealed replay and fixed point | Pass |

Native checks include selected StyledText and ScrolledComposite runtime tests. SWT's existing matching generated native sources were compiled for the checks; no native binaries or manual generated-C changes are delivered.

The complete SWT `verify` suite is not green: an earlier full run exposed repaired contract failures plus missing WebKit/headful AWT and clipboard/RMI environment failures. The repaired relevant suite is green; the entire suite has not rerun. Windows/Cocoa native runtime, GTK4, AT-SPI, API/ABI compatibility, and broad performance checks remain unexecuted. The GTK frontier remains a demand-grown prefix: far indexed access can still materialize a long native prefix.

The paired Synexia candidate contains sealed pre/postimage manifests, actual canonical owners, exact source/recipe custody, proof logs, and reproduction commands. Its strict whole-repository admission, full reactor, and approved offline dependency mirror gates remain required. Neither mapped declarations nor shape matches grant replacement authority. Broader List/JFace/Nebula/accessibility/editors/DND/focus absorption remains unproven.
