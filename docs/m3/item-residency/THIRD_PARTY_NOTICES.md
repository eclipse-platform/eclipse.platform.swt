# Notices: M3 item residency

`ItemResidency`, `ItemSpillStore`, `ItemResidencyPressure` and their tests are original code in this fork
under EPL-2.0 (SWT bundle owners) and Apache-2.0 (tests and tooling, matching `Test_VirtualListModel`).

Adapted (pattern and vocabulary, no code copied):

- SWT fork `VirtualItemState`, `VirtualItemStorage`, `VirtualTreeViewport` at `a02236ca87810c697091db63029196a1a7cd77fa` (EPL-2.0): state names and sparse sorted identity storage.
- Synexia `SwapPressureController`, `SwapBudgetPlane`, `TieredSwapCache` at `46e296ad537c6ecbd62b9783874e8f1714e278a9` and PRs #10889, #10891, #10892 (Apache-2.0, user-owned): tier names, pressure arithmetic and the M3-LAZY-OBJECT-PRESSURE-1 invariant.
- Synexia Tree residency crates `swt-tree-bounded-residency-20261005`, `swt-tree-residency-live-20261005`, `swt-row-window-capacity-20261005`: viewport-plus-overscan window and pinned identity.

Pattern only (licence and commit in `FOSS_REUSE_DECISION.tsv`): Caffeine W-TinyLFU (Apache-2.0), Agrona buffers (Apache-2.0), MapDB `StoreDirect` free lists (Apache-2.0), AndroidX RecyclerView pool (Apache-2.0), Flutter element reuse (BSD-3-Clause), Error Prone `ReferenceEquality` (Apache-2.0), Checker Framework aliasing (GPL-2.0 with classpath exception; checker-qual MIT), OpenJDK C2 escape analysis (GPL-2.0 with classpath exception). No source from these projects is included.
