# SWT viewport runtime hardening — 2026-10-04

Baseline: `0f34224bc30c3091aa3475df15cd3b263e2d48e1`. This records the four Java-file
runtime repair on top of PR #47; it supplements the earlier viewport verification receipt.

## Changes and preserved contracts

The internal retained renderer now updates each group's exclusively owned transform slot.
Repeated scrolling, including identity resets, no longer appends one retained transform per frame.
The shared identity slot and unrelated groups remain unchanged.

The widget dependency scheduler now uses a reusable packed pending lane distinct from its
persistent dirty-paint lane. Invalidation passes through an already-dirty intermediate to a
previously painted dependant. Forward-only DAG edges allow one ascending traversal per affected
node, including diamonds and duplicate edges, without a node-count stack allocation per seed.

The existing graph owners, retained paths, stroke/clip behavior, layer order, public SWT interfaces,
JNI signatures and exposed Item identity contracts are preserved. The recipe uses the canonical
Synexia `M3HashPinnedJavaSnapshotRecipe` engine and a four-target pre/post SHA-256 manifest.
Both original graph recipe crates are updated to emit the same corrected postimages.
Canonical recipe review: [Synexia PR #8690](https://github.com/hsoliwal/com.synexia/pull/8690).

## Executed checks

OpenJDK 21.0.12; Maven 3.9.9; OpenRewrite 8.17.1; Linux GTK3/X11 under Xvfb.

| Check | Result |
| --- | --- |
| Exact OpenRewrite replay, refusal and fixed-point suite | 7 pass, 0 skipped |
| GTK x86_64 Java source roots, `--release 21` | 486 source files compile |
| Win32 x86_64 Java source roots, `--release 21` | 665 source files compile |
| Cocoa x86_64 Java source roots, `--release 21` | 626 source files compile |
| Native retained rendering / GC pixel oracles | 21 pass |
| Dependency scheduling / affine / partial-paint oracle | 5 pass |
| Existing sparse model, Item identity and scrollbar contracts | 7 pass |
| All three focused classes together with `-ea` | 33 pass, 0 skipped |
| SWT Maven/Tycho verification | Blocked: unknown OSGi execution environment `JavaSE-25` |

The new bounded-transform test runs 10,000 updates. The scheduler oracle uses a deterministic
193-node DAG, duplicate edges, 200 invalidation/partial-paint rounds and 38,600 dirty-state
comparisons. All three new regressions fail on the old owners. These results establish the
covered correctness and transform-retention properties, not a general performance speedup.

## Native build and execution

The checked-in GTK binary is older than the current virtual-Table Java/native source contract.
Its `content_providers_create_gtype` path cannot instantiate the current GObject model, causing
two Table contract failures. Rebuilding the matching native sources removes both failures.
No generated C was edited and no built native library is committed.

For this run, the current files from `Eclipse SWT/common/library`, `Eclipse SWT PI/common/library`,
`Eclipse SWT PI/gtk/library` and `Eclipse SWT PI/cairo/library` were copied into a scratch native
build directory. With GTK3 development headers and the JDK JNI headers available:

```sh
SWT_JAVA_HOME="$JAVA_HOME" SWT_PTR_CFLAGS=-DJNI64 CFLAGS='-O -Wall -fPIC' \
  make -B -j4 -f make_linux.mak make_swt make_cairo
```

The resulting `libswt-gtk-4975r7.so`, `libswt-pi3-gtk-4975r7.so` and
`libswt-cairo-gtk-4975r7.so` took precedence in `java.library.path` over the checked-in fragment.
JUnit Platform Console 1.13.4 ran these classes against the compiled GTK source roots:

```sh
java -ea -Djava.library.path="$SWT_NATIVE_BUILD:$SWT_GTK_FRAGMENT" \
  -jar "$JUNIT_CONSOLE" execute --class-path "$SWT_CLASSES" \
  --select-class org.eclipse.swt.tests.junit.Test_org_eclipse_swt_internal_ViewportPaintGraph \
  --select-class org.eclipse.swt.tests.junit.Test_org_eclipse_swt_widgets_ViewportPaintGraph \
  --select-class org.eclipse.swt.tests.junit.Test_ViewportRewriteContracts
```

The three test sources were compiled with Java 21 and the console standalone jar on the
classpath. GTK/Xvfb ran with `SWT_GTK3=1`, `GDK_BACKEND=x11` and `NO_AT_BRIDGE=1`; accessibility
bridge integration is not covered by this focused run.

The full gate attempted was:

```sh
mvn -Pbuild-individual-bundles verify -DskipNativeTests=false \
  -Dsurefire.failIfNoSpecifiedTests=false \
  -Dtest=Test_org_eclipse_swt_internal_ViewportPaintGraph,Test_org_eclipse_swt_widgets_ViewportPaintGraph,Test_ViewportRewriteContracts
```

It stopped before test execution with `Unknown OSGi execution environment: 'JavaSE-25'`.
Native Cocoa/Win32/GTK4, complete API/JNI/reactor admission, native Tree residency, the full
manual screenshot workload and whole-toolkit adoption are still outstanding. This receipt
does not supersede or mark those earlier open gates complete.
