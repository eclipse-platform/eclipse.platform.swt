# Recipe-driven SWT style-policy distillation

Base: `e3d6452a6da986e684badb59104ce353755553c1`.

The canonical recipes live in `hsoliwal/com.synexia`, under
`synexia-openrewrite-recipes/src/main/java/com/synexia/rewrite/M3SwtStyleBitsRecipe.java`.
The Maven project `verification/swt-style-distillation-20261004/pom.xml` runs the
actual attributed SWT sources through two separate serial OpenRewrite phases:

1. `M3SwtStyleBitsAtomize`: extract each platform's `Widget.checkBits` policy into a
   verified private atom.
2. `M3SwtStyleBitsPatternize`: consolidate those atoms into the package-private
   common `StyleBits` owner and delegate the existing entrypoints.

The original sequential mask operations are retained exactly, including overlapping,
repeated, zero and negative masks. Every unrelated Widget member is preserved.
The recipes reject ambiguous, incomplete, unattributed or changed cohorts and reach
a fixed point. They neither merge nor grant automatic M3 promotion authority.

The full build exposed two independent baseline failures. The sealed
`swt-table-scroll-contracts-v1` recipe repairs GTK3 virtual Table hit translation
while a programmatic native scroll is pending, and updates an old viewport assertion
to include the existing editor and feedback planes. Native clipping, sparse facade
residency, pinning and repeat-hit identity remain covered by regression tests.
No repaint policy, JNI declaration or generated native C file is changed.

Validation on Java 21:

* Complete Linux Maven/Tycho reactor: 22 modules SUCCESS; Maven reports 4,430 tests,
  zero failures/errors and 20 skips. Existing platform/suite skips remain in the logs.
* All 145 Table and 157 Tree tests pass; browser tests complete successfully.
* Nine recipe tests cover typed transformations, exact authoring/repair replay,
  fixed points, drift refusal and nested lookalike preservation.
* 114,688 GTK differential style-policy cases match the retained original oracle.
* GTK, Win32 and Cocoa complete Java source sets compile. The shared policy's
  bytecode exactly equals each original method, including stack/local requirements
  and exception table. Widget public/protected descriptors and GTK Table API remain
  unchanged.

The paired Synexia verification directory contains phase receipts, pre/postimage
manifests, source/dependency identities, history review, commands and complete
compressed reactor logs. Windows/Cocoa native runtime, GTK4, AT-SPI, broader
performance and repository-wide Synexia/offline-mirror gates remain unverified.
This delivers a real source-changing cohort; it does not claim complete SWT/JNI absorption.
