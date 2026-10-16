# Notices: Tree viewport

## Adapted with the Apache-2.0 notice kept

- `org.eclipse.swt.widgets.VirtualTreeSiblingIndex` adapts the AVL order-statistic tree of
  hsoliwal/com.synexia `synexia-primitives/src/main/java/com/synexia/primitives/LongOrderStatisticMultiset.java`
  at `af4494b143270d03cf4d3f33d1e7b4f84325f5dd` (blob `6c7dee1415bfc0ec39e337f35a3ca0068898397d`).
  Copyright 2026 Hitesh Soliwal and contributors; licensed under the Apache License, Version 2.0.
  - Kept: parallel primitive lanes (left, right, height, subtree weight), AVL rebalance by single and double
    rotation, weighted rank by one root-to-leaf descent.
  - Changed: the nodes are the topology's item ids (no allocation or free list); keys are read from the
    caller's live child-index lane; one root per parent; the weight is the child's visible extra rows; delete
    splices the successor node instead of copying its key; a weighted descent maps a row to a child.

## Measured and not used

- hsoliwal/M3jdk21 `M3LongFenwickTree` at `5bbc54bc8e30f3f508a95a4052474664f6e93f47` (Apache-2.0). Only a
  measurement prototype in the test tree follows its shape. No source is copied into the product.
