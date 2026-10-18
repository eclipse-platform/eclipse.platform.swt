# Leaf 3 (queued, not started): wider layout behaviour

Stacked after leaf 2. Status: QUEUED. Owner files: win32 Composite, Control,
Shell, Scrollable; `org/eclipse/swt/layout/*` (Grid/Form/Row/Fill/Stack layouts
and data classes).

1. Lazy, visible-only: layout and computeSize for hidden or not-yet-shown
   children deferred behind the LAYOUT dirty bit, done once on first show
   (leaf 1 already defers `updateLayout` of hidden composites; extend to
   computeSize caches and collapsed sections).
2. Incremental: `changed()`/`requestLayout()` mark bits up the parent chain;
   one coalesced pass per frame over dirty subtrees only; packed per-composite
   layout caches invalidated by bits; internal setLayoutDeferred/setRedraw
   batching for bulk changes.
3. Resize storms: layout and paint in the same frame (no half-laid-out paint);
   child moves batched with BeginDeferWindowPos; pixel snap with the single
   floor/ceil rule at every zoom.
4. No oscillation: hysteresis or two-pass solve when a scrollbar shrinks the
   client area; prove with 1 px resize sweeps across the threshold.
5. DPI change: one relayout plus one repaint.
6. Proof before/after on ControlExample, the layout example and the showcase:
   layout calls per resize event, computeSize calls, ms per resize frame
   (p50/p95), scrollbar toggles, frames with border/margin pixel changes.
   getBounds/computeSize/getClientArea identical after layout completes.

Leaf-1 measurement relevant here: a hidden widget that queries its own
geometry during updates (StyledText.getClientArea) pulls its deferred layout
through the on-demand path; one layout per update remains in the hidden-tab
scenario (stock: two).

## StackLayout, ScrolledComposite, ScrollBar (user, 2026-10-13)

- StackLayout: only topControl laid out, measured, painted; other panes hidden
  and gated (leaf 1 already defers their layout and paint; leaf 1 bench has a
  `stack` scenario); lazy pane creation on first topControl; switching costs one
  layout of the new pane plus one paint, no full-shell relayout.
- ScrolledComposite (#106 logical-content viewport, #108 10M rows): own
  scrollbars, flush content edges, blit scroll with exposed strips only,
  children outside the viewport not laid out or painted; never wraps a widget
  that can scroll itself.
- ScrollBar snippets (Slider/ScrollBar listeners, setValues, increments, page
  increments): identical results with scaling and coalescing.
- Regression and scale scenarios from examples/org.eclipse.swt.snippets
  (StackLayout, ScrolledComposite, ScrollBar); before/after numbers.

## Resize, relayout (user, 2026-10-13)

1. Resize coalescing in live resize: setBounds/setSize/WM_SIZE coalesced; one
   SWT.Resize per control per frame with the final size; Resize/Move order and
   final values identical to today.
2. No-op skipping: packed-int bounds; unchanged setBounds fires and lays out
   nothing; layout only for composites whose client size or children's
   preferred sizes changed (dirty bits).
3. No recursion: per-frame re-entrancy bit; no second full pass per frame;
   convergence loops capped by the hysteresis rule.
4. Hidden controls: setBounds records packed bounds + LAYOUT bit; native move
   and layout once on show; getBounds() returns the new values immediately.
5. Atomic moves: one DeferWindowPos batch per composite per frame.
6. Proof in the resize sweep: Resize events per control per frame (<= 1),
   layout passes per frame (<= 1 per dirty composite), SetWindowPos calls per
   frame, frame p50/p95, identical final bounds/client areas/event sequences
   versus unmodified SWT.
