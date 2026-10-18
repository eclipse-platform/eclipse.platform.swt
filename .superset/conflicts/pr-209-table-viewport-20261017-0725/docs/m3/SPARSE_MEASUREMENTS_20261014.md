# Recipe-owned sparse measurement receiver

Canonical recipe, full inputs/outputs, Maven/OpenRewrite tests, Java/C17 proof and category decisions: hsoliwal/com.synexia#11440, commit dd05614520ddcf57af9b201c684d03a7071e86ec, task synexia-openrewrite-recipes/recipes/swt-sparse-measurements-20261014. The canonical commit predates this receiver.

Base SWT#167 at dbc42bf8461288d98bf1725d0a9498cd73d9a9b9. Preserve all earlier intrinsic viewport state, sparse growth, resident DPI and affine changes. Exactly two existing production owners change; no public widget, API, JNI symbol, renderer, native handle, event registry or replacement model is introduced.

VirtualRowExtents postimage blob f0e0143a04c5b9494c7db57b93c32cf4f1704093, SHA256 0c94510fcf1269bcb4458d623e88a3b923f112279e5bba222201ea7057f7ae39.
VirtualListWidth postimage blob 6340a42f8375820caf1ec8325e08778218e14354, SHA256 f458cc73cbbc454d4eff06bc6b164f0bef995fdd7e421a3ffc53372110c647e4.

Implicit row counts now retain only measured primitive height/width exceptions. Extent Fenwick deltas preserve estimates, zero-height lookup, anchoring and alignment in the finite oracle. Eager width input remains dense; explicit full-size measurement remains O(n). Middle first measurements may be O(m), a deliberate sparse-residency tradeoff rather than a universal speedup. VirtualListModel text storage remains dense; native List frontend adoption is not claimed complete.

Executed: strict Java21; 4,342,636 assertions each on original/serial first pass/final candidate; identical observation roots; maximum-int logical counts at Xmx32m with <=16 measurement slots; 100-million-count low-heap original failures and candidate passes; five allocation fault/retry sites; two faulty variants rejected; package-callable descriptors unchanged. Actual C17 test-only JNI oracle ran 40,000 queries / 160,011 checks with UBSan and -Xcheck:jni. Full source, raw logs and caveats are in the canonical task.

HOLD: real Maven/OpenRewrite SDK, complete SWT/Tycho, actual VirtualListModel integration, native Display/GC/items/events/reentrancy, scrolling/overscan, GWT/RAP, Win32/GTK/Cocoa, DPI/IME/accessibility and performance are NOT_RUN. No entire-SWT completion, arbitrary OOME immunity or production JNI acceleration claim. Keep draft; do not merge from finite helper evidence alone. Existing EPL-2.0 notices remain intact.
