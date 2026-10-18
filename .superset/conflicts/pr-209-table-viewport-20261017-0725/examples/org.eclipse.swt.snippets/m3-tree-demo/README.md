# M3 SWT Tree — non-virtual, explicitly mediated SetData

This isolated Java 21 SWT snippet constructs the original platform Tree **without SWT.VIRTUAL**. It owns its native scrollbar, header, selection, expansion and disposal. A controller decides when real TreeItems are admitted and sends an explicit synchronous SWT.SetData event with Tree.notifyListeners carrying the immutable logical address and sibling index. SWT itself will not send SetData automatically on a non-virtual Tree.

Synthetic geometry: 12 modules × 128 packages × 256 files = **394,764 logical nodes** including groups and packages. Requested children are instantiated only in bounded 24-row pages; previous page rows are disposed before the next page is created. The controller retains logical keys, not a 394k TreeItem array. Nested SetData callbacks restore their stack in finally; same-key cycles refuse and callback failure can be retried.

### Java21 model proof

In examples/org.eclipse.swt.snippets/m3-tree-demo run:

    bash run-model-test.sh

Observed local OpenJDK21.0.12.1: **1,182,752 checks PASS**, including full logical index uniqueness, bounds, callbacks, reentrancy, exception cleanup and retry. The SWT Java demonstration was separately statically compiled against compile-only API stubs. These checks do **not** establish a native GTK launch.

### Launch real SWT

Provide approved Java21-compatible SWT core and matching GTK/Win32/Cocoa library JARs:

    export SWT_CLASSPATH="/path/to/org.eclipse.swt.jar:/path/to/platform-fragment.jar"
    bash run-swt.sh

GTK smoke: xvfb-run -a bash run-swt.sh --close-after-ms=1500.

The dedicated target CI runs the Java21 model proof and attempts a **real GTK platform SWT launch**, obtaining pinned public Maven Central SWT 3.126.0 binary artifacts for testing only. Actual smoke PASS must come from the exact-head job; no native success is inferred from this README. No new JNI symbols are introduced.

### Existing donors and implementation choice

Reuse SWT Snippet8's non-virtual lazy Tree expansion, SWT Snippet61's Selection/Expand/Collapse ownership, Snippet170's columns, and Synexia's deterministic FILE recipe contracts. This demo is intentionally **not** the unqualified Initiative31 Tree #172: that PR has known pre-paint selection, cold setTopItem, bounds and long-prefix defects. It does not rewrite the platform Tree/TreeItem or public SWT.VIRTUAL behavior.

Accepted category: UI/Tree/nonvirtual-demand-mediated, immutable-key/paged-real-item composition, explicit SetData mediation. LeetCode/HackerRank/GeeksforGeeks contribute taxonomy only, no source bodies; they have no implementation relevance to SWT's UI lifetime. EPL-2.0 on example/test, Apache-2.0 on first-party Synexia recipe/proof; original donors retain rights.

**HOLD for promotion into SWT core**: general SWT.VIRTUAL substitution, huge native scroll extents, accessibility and identity after paging, GTK/Cocoa/Win32 behavior, native crashes/GC and all widget family acceptance. This is a runnable example with a real platform control, not a claimed general virtual Tree replacement.
