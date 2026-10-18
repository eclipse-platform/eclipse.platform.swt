# Source-sealed SWT parent-owned mask insertion

**Canonical recipe committed first:** [Synexia #11397](https://github.com/hsoliwal/com.synexia/pull/11397), head commit `7dff117c8b81c61b0978dfc406eddcfc22cf1bdd`.
**Receiver input:** `hsoliwal/eclipse.platform.swt@8d607fc88f6ba5b3b3bf07a6d34967c104da63e8`.
**Named Maven/OpenRewrite recipe:** `com.synexia.M3SwtBitLaneStagedInsert20261014`, reusing `M3GpuToolJoinRecipe`.

One original EPL-2.0 source changed, exact before SHA-256:
`515f9a0509303a3d266f813c5ce7509b3cd3bf64c55a745faf631a672d97ed35`; exact after SHA-256: `a502d5e66faa8ae413fcebf60be9cffdc8fc3c42e1095cf2ba0351b3d67ca09a`.
Both were independently checked against the source-sealed canonical manifest.
All non-private/package-facing member declarations remain unchanged.
No new widget, listener, scrollbar, cache, JNI, native handle or public API.

The existing `VirtualBitLanes.insert` previously grew and shifted one lane
before attempting the next growth. If the next lane allocation failed, the
original state could be half-mutated. The candidate allocates every needed
replacement lane before shifting the original CPU mask words; only growing
lanes are copied, and no-growth calls allocate nothing. Original
`VirtualGrowth`/`PackedHeapGrowth`, GWT Java fallback and all widget
semantics remain the original owners. Allocation-failure safety is under
SWT thread confinement, not concurrent atomic publication.

**Proof:** independent Java21 model 100,487,067 bit comparisons/checks passed,
including second-allocation injected failure. This does NOT prove the
original full SWT Java/Tycho/native helper. Maven/OpenRewrite SDK
execution, Win32/GTK/Cocoa Display, GWT, memory benchmarking and JNI:
**NOT_RUN**; **PROMOTION HOLD**. The receiver contains exact reviewed template
bytes and is not presented as completed SDK execution. No rebase, no
default-branch changes or auto-merge; original EPL notices retained.
