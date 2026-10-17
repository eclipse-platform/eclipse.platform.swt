# Tests to run: leaf 2 (branch `m3/swt-viewport-paint-edges-20261013`)

One command (Git Bash, win32, memory gated, desktop lock per class/run):

```sh
sh .m3/swt-viewport-paint-edges-20261013/scripts/run-leaf2.sh
```

1. Build leaf-2 classes (`l2-classes`).
2. JUnit at 100% and `-Dswt.autoScale=200` (200% rounding item B):
   - `Test_org_eclipse_swt_widgets_Control_batch` (public `beginBatch`/`endBatch`/`runBatch`: viewport batch paints once with no overpaint/underpaint via the #121 recorder and keeps the scroll bar live; main-plane batch defers layout while the child viewport paints; nested batches; runBatch keeps the event loop live, shows the busy cursor after the threshold, applies once, maps failures to ERROR_FAILED_EXEC);
   - `Test_org_eclipse_swt_internal_VisibilityGate_box` (box edges vs window/client/trim/border for 6 style sets on Canvas and StyledText; protected rings + viewport = outer bounds; content pixels exact; burst debounce coalescing and cap; scroll-ownership assertion);
   - `Test_org_eclipse_swt_internal_VisibilityGate` (leaf 1 on the leaf-2 build);
   - `Test_ViewportDamage` (peer #121).
3. `PlanesBench` threshold sweep, changes 1/5/20/100/1000: `direct`, `setredraw` (stock and leaf-2 build) and `viewport` (leaf-2 build): paint events, painted px, distinct on-screen scrollbar thumb frames during the operation (1 = frozen), operation ms, UI CPU ms. The crossover where `viewport` beats `direct` sets the batch threshold.
