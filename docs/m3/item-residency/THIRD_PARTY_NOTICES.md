# Notices: M3 item residency

## Licences of the files in this leaf

- **Original engine code, EPL-2.0:** `org.eclipse.swt.internal.residency` (`ItemResidency`, `ItemBitSet`, `ItemLanes`,
  `ItemTreeShape`, `ItemSlots`, `ItemDirectory`, `ItemSparseTable`, `ItemRequests`, `ItemSwapSpill`,
  `ItemSwapPressure`), `org.eclipse.swt.internal.swap.FileSwapStore` and `SwapPageBinaryOpsSelector`.
- **Tests and tooling, Apache-2.0:** this matches `Test_VirtualListModel`.

## Ported with the Apache-2.0 notice kept

The source is hsoliwal/com.synexia at `46e296ad537c6ecbd62b9783874e8f1714e278a9`. Copyright 2026 Synexia
(Hitesh Soliwal); licensed under the Apache License, Version 2.0. Each file's header names its origin path.

- `org.eclipse.swt.internal.swap`: `SwapStore`, `TieredSwapCache`, `SwapBudgetPlane`, `SwapPressureController`,
  `SwapPageBinaryOps`, `JavaSwapPageBinaryOps` and `ParityCheckedSwapPageBinaryOps`.
  - Origin: `synexia-mindex/runtime/src/main/java/com/synexia/mindex/swap/`.
  - Change: the package is renamed. `SwapPressureController` also loses its two readjust overloads for
    unported owners.
- `org.eclipse.swt.internal.PackedHeapGrowth`: copied and specialized.
  - Origin: `synexia-common/.../collections/PackedHeapGrowth.java` (#10871), `PackedSupport.growArrayCapacity`,
    and the once-per-miss sampling of `synexia-mindex/collections/.../M3PreGc.java` (#10879).

## Adapted as pattern or vocabulary only (no code copied)

- **SWT fork** `VirtualItemState`, `VirtualItemStorage` and `VirtualTreeViewport` at
  `a02236ca87810c697091db63029196a1a7cd77fa` (EPL-2.0).
- **RoaringBitmap** `c1cc24ab52933d65db40bda33ed687e02d826a4f` (Apache-2.0): the ARRAY/BITMAP/RUN container
  idea and its smallest-representation rule.
- **Synexia** PRs #10889, #10891 and #10892, plus the Tree residency crates.

## Pattern only

Licence and commit for each are in `FOSS_REUSE_DECISION.tsv`:

| Project | Licence |
|---|---|
| Caffeine W-TinyLFU | Apache-2.0 |
| Agrona buffers | Apache-2.0 |
| MapDB `StoreDirect` free lists | Apache-2.0 |
| AndroidX RecyclerView pool | Apache-2.0 |
| Flutter element reuse | BSD-3-Clause |
| Error Prone `ReferenceEquality` | Apache-2.0 |
| Checker Framework aliasing | GPL-2.0 with classpath exception; checker-qual MIT |
| OpenJDK C2 escape analysis | GPL-2.0 with classpath exception |

No source from these projects is included.
