# SWT current-master UI distillation closure — 2026-10-11

## Status

- `CURRENT_SWT_SOURCE_CLOSURE=GREEN`
- `CURRENT_SWT_MASTER=695efd3c5bf984f5ff38c941c341d60ab309cfd7`
- `FRESH_HOSTED_RUN_AFTER_SELECTION_TREE_SUCCESSOR=NOT_EMITTED`
- `PUBLIC_API_CONTRACT=UNCHANGED_BY_FINAL_SUCCESSOR`
- `RUNTIME_LLM_DEPENDENCY=NONE`

This receipt closes the viewport/UI distillation at the current `hsoliwal/eclipse.platform.swt`
master without inventing a hosted/native result that did not occur.

## Final successor since the previous qualified receiver

The exact comparison from
`3a61904b21017412ba18a789b74cc0615fd1c1e7`
to the current master changes only:

- `ViewportRuntime.Selection` gap enumeration;
- `VirtualTreeTopology` sparse sibling-coordinate shifts;
- their retained JUnit suites and source-custody receipts.

The successor was admitted through merged SWT PR #104 from the canonical Synexia recipe/evidence
owners. It preserves public/protected signatures and introduces no JNI declaration or platform
widget API.

## Final viewport/runtime owner set

The distilled SWT runtime retains:

- shared sparse `VirtualItemStorage` for virtual row residency;
- `VirtualTreeTopology`, `VirtualTreeVisibleProjection` and `VirtualTreeViewport` for logical
  tree topology, visible-row projection and viewport coordinates;
- packed `VirtualItemState` masks for semantic state independent from native/paint residency;
- `ViewportRuntime` for shared selection/window mechanics;
- `ViewportLayerState` for body/fixed/header/editor/feedback plane separation;
- `TreeExpansionModel` below JFace for redraw-bounded expand/collapse operations;
- stable escaped `TableItem`/`TreeItem` facade identity;
- visible + bounded overscan painting and logical scroll coordinates on Cocoa, GTK and Win32.

`swt-classic` remains the compatibility escape hatch; the optimized path remains the forward path.

## Graphics distillation

SWT's public/native `GC` remains the execution surface.

- `ViewportGcProxy` is a scoped state guard around the real `GC`;
- `ViewportPaintGraph` is the retained primitive-array paint DAG;
- affine transforms use six primitive lanes and exact quadrant handling;
- repeated row/header geometry may be retained once and replayed as instances;
- stroke/path state, clipping, culling, layers and z-order are represented without retaining GC;
- translation-only replay avoids native `Transform` allocation;
- full mutable GC state and clipping are restored at scope boundaries.

This is the accepted answer to the proxy-GC / affine / stroke-DAG design: no replacement GC class,
no retained native GC ownership, and no JNI graphics path without a measured crossover plus
differential proof.

## Java2s / Swing donor closure

The following public catalogues are retained as behavior taxonomies only; no tutorial source body
is copied:

- https://www.java2s.com/Tutorial/Java/0280__SWT/Catalog0280__SWT.html
- https://www.java2s.com/Tutorial/Java/0300__SWT-2D-Graphics/Catalog0300__SWT-2D-Graphics.html
- https://www.java2s.com/Tutorial/Java/0240__Swing/Catalog0240__Swing.html
- https://www.java2s.com/Tutorial/Java/0260__Swing-Event/Catalog0260__Swing-Event.html

Admitted behavior families include Canvas/Paint/GC/Transform/Path, Table/Tree/TreeViewer,
ScrolledComposite/ScrollBar, owner draw/editors, JTable/JTree model-render separation,
JViewport/JScrollPane origin separation, JTableHeader/JLayeredPane z-plane separation, and
selection/focus/mouse/key/tree event ordering.

The million-row/table/tree/scrolled-canvas/manual screenshot lanes are original SWT proofs derived
from those behavior shapes, not copied examples.

## Competitive donor policy

LeetCode, HackerRank and GeeksForGeeks remain problem-category donors only. The retained queue is
lower-bound/galloping search, interval/range operations, prefix/Fenwick/segment structures only
where measured, iterative DFS/BFS, bitsets, bounded caches and state-machine convergence.
Challenge/editorial solution bodies are not copied.

## Verification boundary

The final successor has source-sealed Synexia evidence, frozen model oracles, fixed-point replay
and drift refusal. The current head did not emit a fresh attached hosted run after PR #104, and a
local repository clone was unavailable in this execution environment. Therefore this receipt does
not claim a new full Tycho/platform/native/JNI run for `695efd3c...`.

Existing screenshot/OpenCV, JNI/native, CPU/heap/RSS and platform evidence remains retained from
the earlier qualified owner chain and is not rewritten.

## Final invariant

Future UI/runtime changes must be additive successor recipes from this head. Do not hand-edit the
same transformation across platform files. Create or reuse the canonical Maven/OpenRewrite recipe,
qualify it, apply the exact postimage, and preserve merge ancestry.

No LLM is required in the SWT runtime/core.
