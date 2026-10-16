<!-- SPDX-License-Identifier: EPL-2.0 -->
# Original SWT Tree — guarded row-selection MouseUp admission

**DRAFT; candidate-only and only partially addresses native-event RED status.**

Canonical source and Maven/OpenRewrite recipe FIRST:
[Synexia #11739](https://github.com/hsoliwal/com.synexia/pull/11739),
`com.synexia.m3.TreeNativePointerAdmission20261016`.
This branch is stacked on the exact original Initiative31 Tree
[SWT #172](https://github.com/hsoliwal/eclipse.platform.swt/pull/172)
at source commit `5536d1195386c2927562637a39a0ed97d3ce6472`.
Do not retarget/merge into native SWT master without stock-platform proof.

## Target bytes and scope

The candidate modifies exactly the original `Tree.java` MouseDown/MouseUp
private flow, adds one package-private transient `TreePointerSelection` class
(two primitive fields), adds an eight-method EPL-2.0 SWT JUnit and registers it
in the existing `AllWidgetTests` explicit suite.

- Tree.java input Git blob `ab68ac590865251816c7a47c3d6aa1dbbc37c6fb`;
  input SHA-256 `9ec368e1480f0aed7b4151327609d81a8a1bb11cf465a2324b614d70d310d8fe`.
- Tree.java reviewed candidate Git blob `4c16fb4734d425748bb030159586c03d09c3c6b2`;
  SHA-256 `591e7be014b1446c9f8a32ce50b119eb53640e571c23b8961c84929fd4de2999`.
- New helper Git blob `05419bde2ea08a3d7414d8b8209ba0ecd056e691`;
  SHA-256 `330cfa6b1407858a200377ceb40f1e1cda53bda034242f1fa23160cf039ee1c3`.
- Dedicated test Git blob `274c1c10bda480f0e3fe851924e9da87e4ecb71c`;
  SHA-256 `dfbc979db169fb626c1858ba0f554c7a86b7ed728edb7d90d0297e2d0417d2a9`.
- Existing explicit SWT suite adds only that new test class.
  The TreeItem, checked/expanded semantics, paint, SkijaGC, DPI,
  event loop, ScrollBar, public/protected/package signatures and JNI symbols
  are **not changed by this source packet**.

## Nested MouseDown counterexample corrected

The original first-pass gesture helper accepted `rowPressed(1)` from an
older callback after a nested, newer MouseDown. A strict Java21 negative
control reproduced the extra generic Selection on the exact former helper
blob `3b2f7ad20d4bf175b2096afc22631444ff259b70`.
The current reviewed recipe changes this to `rowPressed(button, capturedEpoch)`
and records the generation **before** regular/virtual hit-test callbacks.
A stale outer row hit neither authorizes a new Selection nor erases a newer
admitted row. The class continues to use the same two primitive fields, no
new Tree methods, and no product JNI hot path.

[Synexia counterexample and test receipt](https://github.com/hsoliwal/com.synexia/blob/m3/tree-native-pointer-admission-recipe-20261016/synexia-openrewrite-recipes/recipe-crates/m3-tree-native-pointer-admission-20261016/verification/LOCAL_EXECUTION_RECEIPT.md)
preserves the source hashes and actual test/negative-control evidence.

The parent's two-field state is reset on MouseDown; only valid regular or
SWT.VIRTUAL admitted row selections authorize the generic Selection on a
matching MouseUp. Disclosure/CHECK already use their distinct existing
TreeItem callbacks and must not emit an additional itemless generic
Selection. Header/empty/stale gestures are refused. A reentrant MouseUp
cannot steal a newer row selection. This suppresses a faulty custom event
and is an intentional behavioral repair toward the stock Tree contract.

## Actually executed and remaining red gates

**Executed locally:** strict Java 21 helper+eight authored SWT test method
compilation and all eight method bodies with a small, disclosed JUnit/SWT
compile-only shim; 50,000 seeded paired Java-versus-test-only-C17 JNI row-admission
and MouseUp consume comparisons, `-Xcheck:jni`, plus an independently run interpreted JVM
with GCC UBSan. JDK21 compiler AST proof itself was strictly compiled,
but **full Tree Java AST images not executed**.

**NOT_RUN / HOLD** on this receiver: real Maven/OpenRewrite SDK replay,
real Eclipse SWT/Tycho/JUnit, native Win32/GTK/Cocoa input,
Expand/Collapse/CHECK callback timing/focus, exact current eight-owner
native Tree launch, Skija pixel/screenshots, accessibility/IME/DPI,
memory/performance and JNI production ABI. The separate stock/common
four-case native comparison in [Synexia #11692](https://github.com/hsoliwal/com.synexia/pull/11692)
remains **RED**, from older `fe3b552a` runtime. This PR does not
fix or claim to fix native default-action timing merely by removing
the extra generic selection.

The 360-source contract matrix stays `HOLD_UNQUALIFIED`;
the source is EPL-2.0, proof/orchestration in Synexia Apache-2.0,
and no donor code was copied or relicensed.
