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
(two primitive fields), adds a six-method EPL-2.0 SWT JUnit and registers it
in the existing `AllWidgetTests` explicit suite.

- Tree.java input Git blob `ab68ac590865251816c7a47c3d6aa1dbbc37c6fb`;
  input SHA-256 `9ec368e1480f0aed7b4151327609d81a8a1bb11cf465a2324b614d70d310d8fe`.
- Tree.java reviewed candidate Git blob `8b5f696b9ac6d569ce30ceb2ef813c76d80f015a`;
  SHA-256 `fe75cd0116fc41451899a04b698d76e0dc00cb23ec05c3be78c7ec7ab27bacf3`.
- New helper Git blob `3b2f7ad20d4bf175b2096afc22631444ff259b70`;
  SHA-256 `a2149ab939b0647caa5a9a407a1f9eaccf8e48b721797cb687655a2b4c6cef0c`.
- Dedicated test Git blob `a6a423577472c0ff47879235fc4f447f5db3e0ed`;
  SHA-256 `a511f29b414ee75ec08160ff4c8422d9417b6f0597b01896df178e9634713a6e`.
- Existing explicit SWT suite adds only that new test class.
  The TreeItem, checked/expanded semantics, paint, SkijaGC, DPI,
  event loop, ScrollBar, public/protected/package signatures and JNI symbols
  are **not changed by this source packet**.

The parent's two-field state is reset on MouseDown; only valid regular or
SWT.VIRTUAL admitted row selections authorize the generic Selection on a
matching MouseUp. Disclosure/CHECK already use their distinct existing
TreeItem callbacks and must not emit an additional itemless generic
Selection. Header/empty/stale gestures are refused. A reentrant MouseUp
cannot steal a newer row selection. This suppresses a faulty custom event
and is an intentional behavioral repair toward the stock Tree contract.

## Actually executed and remaining red gates

**Executed locally:** strict Java 21 helper+six authored SWT test method
compilation and all six method bodies with a small, disclosed JUnit/SWT
compile-only shim; 50,000 Java-versus-test-only-C17 JNI scalar admission
comparisons, `-Xcheck:jni`, plus an independently run interpreted JVM
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
