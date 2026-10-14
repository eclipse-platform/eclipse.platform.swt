# Recipe-owned Initiative31 Drawing receiver

Canonical recipe, full before/after source, Maven/OpenRewrite tests, Java/C17 proof and category records: hsoliwal/com.synexia#11440, commit dd05614520ddcf57af9b201c684d03a7071e86ec. Task: synexia-openrewrite-recipes/recipes/swt-sparse-measurements-20261014. Named recipe: com.synexia.m3.SwtDrawingOwnership20261014. The canonical commit predates this receiver.

Base: SWT#157 at a9a8f1475d5c87c989b9cc0c6cd9059ec45acbee. Preserve the complete Initiative31 List selection and incremental scrollbar-metric branch. This is not a native-master Drawing/GC transplant.

Only one existing production file changes: bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/graphics/Drawing.java. Original blob 6711d30ed3c9747f8439a9bd5341f09d77b4177a; postimage blob ddea4ee87948534f2602fe9b6a9d98adaec8c589; postimage SHA256 f8ded252de305521e4f5d0cbd7bff9a5030518a0fb7f1204a0b9245b72e9999d. Received bytes exactly match the already committed recipe template.

The existing shared drawWithGC, executeOnGC and measure paths now enclose adapter creation/setup in their ownership scope. Both owned contexts are attempted during cleanup even when adapter disposal fails. Borrowed paint GCs are never disposed; an aliased original is disposed only once. Preserve primary RuntimeException/Error identity and attach cleanup failures. Fixing broken exceptional outcomes intentionally differs from the original. Partial native resources not returned by a failing factory remain that factory's responsibility.

No replacement renderer, new native window, event owner, public method, JNI ABI, selection/scrollbar state or widget Item identity is introduced.

Executed: complete original and generated Drawing source compiled under Java21 --release 21 -proc:none -Xlint:all -Werror with explicitly instrumented GC/Control/Skija test doubles. 640 scenario combinations: original456 assertion violations, candidate0 (1586 checks). Borrowed/owned/native/adapter branches, setup/operation/commit failures, cleanup failure suppression, Error/RuntimeException, foreign-GC rejection and nested calls are covered. Public javap descriptors match. These are not actual native handle, Display, GPU or painting results. The companion JNI oracle in Synexia qualifies sparse-measurement helpers only; it does not test this Drawing implementation natively.

HOLD: actual Maven/OpenRewrite SDK, full Initiative31/SWT/Tycho build, real GC/Skija/Display exceptions, native cleanup, events/reentrancy, Win32/GTK/Cocoa, pixel output, DPI/IME/accessibility and application latency/memory remain NOT_RUN. Keep draft. The all-widget SWT/Nebula requirement remains open; this shared-path repair does not certify every widget. Preserve existing EPL-2.0 notice and upstream attribution. No merge, rebase, force-push or default-branch edit.
