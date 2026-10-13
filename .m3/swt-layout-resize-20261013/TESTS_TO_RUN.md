# Tests to run: leaf 3 (branch `m3/swt-layout-resize-20261013`)

```sh
sh .m3/swt-layout-resize-20261013/scripts/run-leaf3.sh
```

`ResizeBench` on stock (`base-classes`), leaf 1 (`after-classes`) and leaf 3 (`l3-classes`), 3 rounds:
- `control`: ControlExample live-resize sweep, 200 frames: Resize, Move and Paint events per frame (x100), frame time p50/p95 (us), final bounds/client-area tree hash (must match stock).
- `wrap`: ScrolledComposite + wrapping Label, 1 px width sweep across the scrollbar threshold: total vertical-scrollbar toggles, max toggles in one step, oscillating steps (target 0), step time p50/p95.
- `stack`: StackLayout with 8 panes of 120 buttons, 200 switches: Resize/Move/Paint events per switch (x100), ms per switch p50/p95.
