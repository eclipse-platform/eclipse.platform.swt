# Catalogue-driven viewport stroke culling evidence

Current integration baseline: `b678db3bae164f5334f098348e79c57c1877d06d`.

This is a focused retained-rendering correctness repair. It does not claim completion of
the SWT platform/widget matrix.

## Donor map

The implementation was distilled from behavior, not copied source:

- SWT/Java2s graphics examples: thick lines, rectangles, transforms and clipping remain
  observable through the ordinary SWT `GC`.
- Dear ImGui: reject coarse list/item ranges before detailed command submission.
- LVGL: keep invalidation region-local rather than redrawing unrelated chrome/content.
- GPU viewport renderers such as viewport-lib: conservative instance bounds/scissor before
  draw dispatch.

Those patterns are folded into the existing `ViewportPaintGraph`. SWT keeps its public
`GC`, widget and event contracts; no Rust/wgpu, ImGui, LVGL or other runtime dependency
is introduced.

## Failure mode

The original command-level culler compared line/rectangle geometry against the replay clip.
A native stroke can extend beyond that geometry because of line width, fractional width,
caps, joins and antialiasing.

After coarse retained-subtree bounds were added, the same issue could also occur one level
earlier: a group's logical geometry bounds might miss the clip even though a child stroke
crosses the group edge.

## Repair

One conservative stroke envelope is computed per replay.

- command-level culling uses `long` geometry/clip intermediates and expands LINE and
  DRAW_RECT bounds by the stroke envelope;
- group/template cull bounds use a conservative affine-scaled envelope before rejecting a
  retained subtree;
- fill and text behavior is unchanged;
- layer replay, reusable templates, affine coordinate mapping, inverse event mapping and
  caller-GC state restoration remain in the canonical graph.

The group envelope intentionally overestimates. Exact clipping remains the responsibility
of the real SWT `GC`.

## Proof encoded in SWT tests

`Test_org_eclipse_swt_internal_ViewportPaintGraph` now includes native-GC differential
oracles for:

1. thick horizontal stroke entering the clip;
2. square-cap corner entering the clip;
3. rectangle join entering the clip;
4. fractional-width stroke;
5. a thick child stroke crossing an otherwise off-screen group cull bound.

The retained and immediate paths receive identical `LineAttributes` and clipping, then
all pixels of their 64x64 images are compared.

Existing tests continue proving retained geometry reuse, affine/layer behavior, clip/stroke
inheritance, coarse subtree skipping and caller-transform restoration.

## Verification boundary

The predecessor isolated stroke repair (SWT #36) recorded GTK3/X11 native pixel execution
against its older paint-graph baseline. It must not be merged because that branch predates
the current affine/layer/coarse-culling graph.

This successor applies the repair on current master and preserves the full retained graph.
Its Java source has been mechanically checked for balanced delimiters, merge markers and
presence of the expected retained-graph/state features.

At the time of this update:

- the repository screenshot workflow is configured, but the available GitHub runs are
  creating zero jobs;
- the local execution container has Java/Xvfb but does not provide the Maven/GTK packages
  required to reproduce SWT's GTK test lane;
- therefore no current-head native GTK, GTK4, Cocoa, Win32 or full Tycho PASS is claimed.

The executable tests remain the acceptance gate when an SWT-capable runner is available.
