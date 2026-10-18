# SWT viewport rewrite: compatibility and stress catalogue

This branch treats SWT `Table`, `Tree`, and large scrolled surfaces as a logical coordinate model plus a bounded paint viewport.

## Compatibility evidence

The implementation is checked against SWT's own snippets/manual tests and against behavioral categories represented by the long-running Java2s SWT catalogue. Java2s is reference material only; source is not copied.

Useful Java2s categories:

- SWT Table, TableItem, Table renderer and Table event examples.
- SWT Tree examples, including lazy population.
- SWT TreeViewer examples.
- SWT ScrolledComposite examples.
- SWT Canvas / paint examples.

Reference catalogue:

- https://www.java2s.com/Tutorial/Java/CatalogJava.html
- https://java2s.com/Tutorial/Java/0280__SWT/1180__Tree.html

The catalogue exposes dedicated SWT sections for ScrolledComposite, Table, Tree, TreeViewer and Canvas/paint behavior. The rewrite preserves those API shapes while changing the internal storage and residency model.

## Rewrite invariant

The public widget API is a facade. The authority underneath it is:

- logical coordinate/count;
- columnar primitive/reference arrays;
- packed state masks;
- range-compressed selection;
- viewport origin and extent;
- visible + overscan paint window;
- identity-pinned SWT `Item` facades once exposed.

A materialized `TableItem` / `TreeItem` is never rebound to a different logical coordinate. Future native-residency compaction may discard only private render/native cache that can be reconstructed without violating the public item identity contract.

## Redraw-locked mutation

Large projection changes use the same safe shape:

```java
widget.setRedraw(false);
try {
	// mutate logical coordinates / expansion / selection / native residency
} finally {
	if (!widget.isDisposed()) widget.setRedraw(true);
}
```

The lock is scoped to the mutation. It is not a substitute for the viewport model and must not become a global lock.

## Collapse/expand direction

Collapse is a projection boundary:

1. record logical child count and child state lanes;
2. lock repaint;
3. make the collapsed subtree non-resident for paint/native purposes where the platform allows it;
4. keep public SWT item facades identity-pinned if they have escaped through API/SetData;
5. restore native/render residency from logical coordinates and masks when expansion or direct API demand requires it.

This separates semantic lifetime from render lifetime.

## Manual stress harness

Run `org.eclipse.swt.tests.manual.ViewportRewriteStress`.

It contains three deliberately large scenarios:

- **2,000,000-row virtual Table** with `SWT.SetData`, checkbox state, selection and owner-paint counting.
- **500,000-root lazy virtual Tree**, with 10,000 logical children per demanded branch.
- **1,000,000-row ScrolledComposite + Canvas**, where paint derives its row window from the GC clipping rectangle and expands it by only eight overscan rows. Selection is held in one packed `long[]` bit lane.

The status line reports logical extent versus SetData/paint activity so viewport behavior is visible while scrolling.

Existing SWT manual tests such as `Bug548982_TreeAddRemoveMany` remain important regression inputs, particularly its redraw-lock, reverse insertion, expand/collapse and large tree cases.


## Synexia concepts distilled into SWT

Recent `hsoliwal/com.synexia` viewer work was reviewed as design evidence. SWT should absorb the low-level data structures that belong below JFace, while rejecting higher-level model/session dependencies.

### Inline into SWT core

1. **Visible-tree preorder intervals**

   The useful idea from `MIndexVisibleTreeIndex` is the representation, not the class: assign the known logical projection a DFS/preorder coordinate lane, retain depth and subtree-end lanes, and represent expansion independently from native items. A subtree collapse then becomes a range/state operation rather than recursive widget destruction. Visible-row rank/select can be computed from the logical projection and used to manufacture only the visible + overscan facade set.

2. **Sparse row extent geometry**

   The useful idea from `MIndexSwingExtentIndex` is a uniform row-height baseline with sparse changed blocks. A million- or billion-row logical surface should not allocate one height value per row merely because a few owner-drawn rows measure differently. Pixel offset -> logical row and logical row -> pixel offset belong in the viewport model.

3. **Caller-owned viewport buffers**

   `MIndexVisibleTreeIndex.windowInto(...)` demonstrates the right allocation shape: paint code supplies reusable coordinate/depth buffers and the model fills them. SWT paint passes should not allocate a new row object graph for every scroll frame.

4. **Frame-atomic projection changes**

   The XViewer reconciler's useful invariant is one long-lived control plus a bounded transaction:

   ```java
   control.setRedraw(false);
   try {
       // update logical state / projection / native residency
   } finally {
       if (!control.isDisposed()) control.setRedraw(true);
   }
   ```

   State restoration and top-row publication belong inside the same transaction.

5. **Coordinate remapping after structural edits**

   Synexia's coordinate-remap work is useful as a rule: insertion/removal must move selection, checks, expansion, focus and viewport coordinates together. SWT's mutable implementation can do this directly in its parallel lanes instead of allocating an immutable remap object for every edit.

### Keep above SWT

Do **not** inline these Synexia owners into SWT:

- `MIndexViewerSession`, contexts, generation ownership or source snapshots;
- XViewer/Nebula/JFace content-provider abstractions;
- workbench Jobs, model filtering/sorting, source refresh or persistence;
- domain scene models such as `MIndexNativeSceneWindow`;
- MIndex string/token dictionaries.

Those are clients of SWT's viewport machinery, not toolkit primitives.

### Deferred / measurement-gated

Two Synexia ideas are deliberately not copied into the current heap implementation yet:

- the fixed 128-byte direct-address row image used by `MappedIndexWordRows`;
- the general JNI addressed-array execution engine.

SWT's mutable widget state is currently better represented by flat Java primitive/reference lanes. A direct/mapped fixed-stride row image becomes attractive only after profiling shows a JNI/FFM/native batch boundary is worthwhile.

The resulting ownership stack is:

```text
Workbench/JFace/Nebula model semantics
        |
        v
SWT public widget/item facade API
        |
        v
logical coordinate + topology arrays
state masks + range selection + sparse extents
visible/overscan viewport planner
        |
        v
bounded native residency / paint
```

## Screenshot + SWT Spy regression lane

The viewport stress harness can capture deterministic visual checkpoints together with structural diagnostics.

Launch `org.eclipse.swt.tests.manual.ViewportRewriteStress` with:

```text
-Dswt.viewport.screenshots=<output-directory>
-Dswt.viewport.screenshots.exit=true
```

The second property is optional; when true, the shell closes after the scripted capture sequence.

Each checkpoint writes:

- `<scenario>.png` — the rendered widget surface;
- `<scenario>.txt` — Spy-style structural diagnostics.

The diagnostic sidecar records:

- platform;
- control class and SWT style bits;
- parent class;
- bounds and client area;
- visibility/enabled state;
- layout and layout-data classes;
- horizontal/vertical scrollbar selection, minimum, maximum, thumb, increment, page increment and visibility;
- Table/Tree logical counts, top coordinate, selection count, row height, columns and header state;
- ScrolledComposite origin/minimum/expansion state;
- live `TableItem` and `TreeItem` counts captured with SWT's existing `WidgetSpy.NonDisposedWidgetTracker`, both globally and scoped to the captured widget subtree.

The scripted sequence includes top/middle/end virtual Table states, expanded/collapsed large Tree states, vertical and horizontal ScrolledComposite states, fixed-surface logical viewport states, and narrow/wide resize cases that exercise interdependent scrollbar visibility. Each PNG is hashed so a same-platform/theme/DPI lane can cheaply detect an unexpected visual change before a human inspects the image.

The purpose is not pixel-identical output across operating systems or themes. Compare screenshots within the same platform/theme/DPI lane and combine visual evidence with the structural sidecar and SetData/paint counters.

### Example-driven visual contracts

The supplied historical examples are mapped to visual checkpoints instead of copied into SWT:

- `ViewPort.java`: narrow/wide resize states verify the interdependent horizontal/vertical scrollbar visibility calculation. Its implementation deliberately recomputes horizontal visibility after vertical visibility changes because one scrollbar reduces the other axis' client extent.
- `TreeViewerWithViewPort.java`: scroll-position checkpoints verify redraw-locked projection changes and top-item stability while an external scrollbar drives the underlying Tree.
- `TreeViewerLazyTool.java`: its bounded `PROGRESSIVE_REVEAL_STEP = 100` pattern motivates frontier/residency screenshots while the full logical child count remains visible in the sidecar.
- `DeferrredTreeViewer.java`: its 2,000-child step, dummy loading element, separate expansion list and retained selection motivate the pinned-child collapse/restore screenshot sequence.
- Virtual TreeView 8.4.1: logical node visibility/state, visible-column calculations, dedicated header painting/backbuffer and scroll-range management motivate separate header/body screenshots plus logical-vs-resident diagnostics.
- SWT/Java2s examples remain API-behavior donors for SetData, owner draw, selection, check state, ScrolledComposite and resize combinations; their source is not copied.

Additional scripted images cover:

- `table-checked-selection.png`: checkbox state and selection painting together;
- `tree-pinned-expanded.png`: a pinned checked/grayed child while expanded;
- `tree-pinned-collapsed.png`: the same logical branch after collapsed native-residency compaction;
- `tree-pinned-restored.png`: restored expansion proving the pinned facade/state remains stable.

For Tree scenes the sidecar also attempts to record virtual visible-row count, topology materialized count, viewport first/visible/paint range, and native resident child count for the scripted root. These diagnostics are reflective and best-effort so the manual harness remains usable on every SWT platform.

### Donor evidence for the screenshot lane

The supplied legacy viewer sources reinforce the test shape:

- the old `ViewPort` recomputes horizontal/vertical scrollbar visibility together because showing one scrollbar changes the other axis' available extent;
- `TreeViewerLazyTool` uses bounded progressive reveal while keeping the full logical child count separately;
- `DeferrredTreeViewer` retains expansion/selection while progressively increasing realized child counts;
- Virtual TreeView separates node/visibility state from current painting and maintains a dedicated header implementation.

SWT's own current `WidgetSpy` creation/disposal hook is reused for residency evidence rather than adding another tracker.


## Automated GTK screenshot regression

The compiled JUnit visual lane is `ViewportScreenshotRegressionTest`. It is property-gated and therefore skipped during ordinary SWT test runs.

The reusable build workflow exposes:

`viewport_screenshots: true`

When enabled for a GTK build it runs the visual test under the same Xvfb/Wayland compositor environment used by SWT CI with:

```text
-Dtest=ViewportScreenshotRegressionTest
-Dswt.viewport.screenshotRegression=true
-Dswt.viewport.screenshots=target/screenshots/viewport
```

`.github/workflows/viewport-screenshots.yml` currently runs the deterministic GTK3/X11 lane on relevant pull requests and on manual dispatch. The existing SWT build artifact uploader already includes `**/target/screenshots/*.png`, so the generated images and their text sidecars are retained with the CI run.

The CI scenes deliberately focus on state that is difficult to prove from unit assertions alone:

- two-million-row virtual Table with checked + selected rows;
- expanded virtual Tree with a pinned checked/grayed child;
- the same branch after collapsed native-residency compaction;
- restored expansion with the same public child facade and semantic state.

This complements, rather than replaces, the larger manual screenshot matrix.

## Compatibility requests and SWT classic

The optimized viewport implementation is the forward development path. Public SWT API and observable behavior remain the compatibility boundary.

When a real application reports a regression:

1. reproduce it against the existing SWT snippets/manual/JUnit coverage;
2. prefer a narrow compatibility repair that preserves the optimized storage/viewport model;
3. add the reproducer as a regression test;
4. use the `swt-classic` branch only when the client genuinely depends on legacy implementation behavior that cannot yet be preserved safely.

`hsoliwal/eclipse.platform.swt:swt-classic` is intentionally retained as the classic escape hatch. It is not the architecture target and should not absorb new viewport work. Compatibility requests are accepted; one exceptional client must not force the optimized core back to dense/eager widget construction.


## Java2s SWT/Swing distillation matrix

The following catalogues are treated as behavioral donor corpora. Their source is not copied:

- https://www.java2s.com/Tutorial/Java/0280__SWT/Catalog0280__SWT.html
- https://www.java2s.com/Tutorial/Java/0300__SWT-2D-Graphics/Catalog0300__SWT-2D-Graphics.html
- https://www.java2s.com/Tutorial/Java/0240__Swing/Catalog0240__Swing.html
- https://www.java2s.com/Tutorial/Java/0260__Swing-Event/Catalog0260__Swing-Event.html

The SWT catalogue contributes API-behavior shapes for Table/TableItem/TableColumn,
Tree/TreeItem/TreeColumn, TreeViewer, editors, renderers, ScrolledComposite,
ScrollBar, Canvas, focus, keyboard/mouse events, drag/drop, timers and screen
capture. The rewrite must continue to satisfy those ordinary application shapes
even when the retained implementation is sparse.

The SWT 2D Graphics catalogue contributes independent paint dimensions: GC state,
paint clipping, transforms, paths, line/stroke state, text, animation and image
rendering. These are not flattened into widget state. They are composed at the
paint boundary.

The Swing catalogue is useful as an independent design cross-check:

- `JViewport` demonstrates that logical view coordinates and viewport coordinates
  are separate concerns.
- `JLayeredPane` reinforces independent z-planes for body, frozen content,
  header, editor and transient feedback.
- `JTableHeader` being independently managed from table rows reinforces the SWT
  header/body split.
- JTable/JTree model-vs-view indexing reinforces explicit coordinate mapping after
  sort/filter/structural edits.
- renderer/editor examples reinforce reusable transient presentation objects rather
  than one retained component per row.

The Swing Event catalogue reinforces event-space separation:

- scrollbar adjustment is a viewport-origin event, not a data-model mutation;
- mouse/mouse-wheel coordinates must be mapped through the inverse of the same
  transform used for paint;
- tree expansion/selection/model events are distinct semantic channels;
- model-change events should invalidate logical ranges rather than force eager
  widget reconstruction.

### Retained paint graph + viewport state

`org.eclipse.swt.internal.ViewportPaintGraph` is the canonical retained rendering
owner. The Java2s/Swing distillation extends that existing graph rather than
introducing a parallel paint DAG.

The graph already stores reusable geometry templates and DAG instance edges in
structure-of-arrays form and replays through the real SWT `GC`. It now also
carries the viewport state needed around those retained atoms:

- inherited viewport z-plane metadata;
- group-local 2D affine transforms;
- local clip bounds that can be conservatively intersected in root/device space;
- inherited stroke width/style/cap/join metadata;
- local -> root/device coordinate mapping;
- root/device -> local inverse mapping for hit testing and input events.

Detached reusable templates deliberately have no unique root coordinate until
instanced. Coordinate queries therefore reject detached nodes rather than
inventing a device location.

This makes the ownership pipeline:

```text
logical row/cell coordinates
        |
        v
ViewportPaintGraph
  retained templates + instance DAG
  body / frozen / header / editor / feedback state
  affine mapping + clip bounds + inherited stroke
        |
        v
real SWT GC
        |
        v
platform renderer
```

The public `PaintEvent.gc` remains the real SWT `GC`; identity and existing GC
semantics are unchanged. The graph's clip/stroke lanes are viewport planning
state: callers can derive the effective root clip/stroke without retaining a GC
or a native graphics resource.

Win32 already contains a lower-level replay mechanism for reapplicable GC
operations such as transform, clipping, alpha, line state and drawing
operations. That backend mechanism remains complementary: the viewport graph
plans reusable viewport paint atoms and coordinate state, while the platform GC
continues to own native graphics lifetime.

### Retained vector paths and graphics-resource lifetime

The SWT 2D Graphics catalogue's `Path` examples are distilled into the existing
`ViewportPaintGraph` rather than a second retained graphics API.

A retained path stores only copied, device-independent primitive geometry:

- SWT path opcode bytes;
- float point/control-point coordinates;
- per-node offsets/counts and conservative bounds.

The graph never retains an SWT `Path`, `GC`, `Transform`, `Color`, `Font`
or `Image`. During one replay, path nodes are lazily materialized as temporary
native SWT `Path` objects, cached only for that replay so repeated DAG instances
share one native path, and disposed before replay returns. Caller-owned `PathData`
arrays are copied when the node is created, so later caller mutation cannot
change retained geometry.

This keeps the resource boundary explicit:

```text
stable primitive geometry                 native/resource state
-------------------------                 ---------------------
PathData opcode/point copy  retained      Path             replay-local
affine transforms          retained      Transform         replay-local
clip/cull/stroke metadata  retained      GC               PaintEvent/caller
z-layer/channel ids        retained      Color/Font/Image  widget/application
```

Arbitrary vector geometry therefore participates in the same affine, layer and
viewport culling pipeline as lines/rectangles without changing public GC or
native graphics lifetimes. Draw-path culling includes the current conservative
stroke envelope; fill-path culling includes an antialiasing safety pixel. The
real platform GC remains the exact rasterization and clipping authority.

### Distilled rendering invariants

1. **Vertical scrolling does not repaint the header merely because the body moved.**
2. **Horizontal scrolling translates the normal header and body together while
   frozen content stays on its own x-plane.**
3. **Paint and input use the same coordinate transform in opposite directions.**
4. **Nested clips intersect before rendering reaches the platform GC.**
5. **Stroke/path/transform state is presentation state, not row-model state.**
6. **Owner-draw callbacks still receive the ordinary SWT GC and item facade.**
7. **Renderer/editor shells may be transient/reusable; exposed SWT Item identity may
   not be rebound.**
8. **Model/view coordinate conversion is explicit after sorting, filtering,
   insertion, removal or expansion changes.**

The deterministic screenshot lane includes
`viewport-affine-clip-stroke.png`, which exercises a real SWT GC with transform,
clip, cubic Path, line width/style/cap/join and text while preserving/restoring
incoming GC state.


### Additional viewport/GPU donor distillation

The viewport rewrite also reviews modern native/immediate/GPU GUI projects as
architecture donors. Source is not copied.

- **viewport-lib (Rust/wgpu)** separates the host application's window, event
  loop and tool state from viewport rendering. It builds frame data, prepares
  renderer resources once per frame, batches instances, and treats camera input,
  picking and overlays as viewport-space concerns. Because viewport-lib is
  GPL-3.0, SWT uses these ideas only as design evidence.
- **Dear ImGui** carries clip rectangles with draw commands, keeps background and
  foreground viewport draw lists, and can split/merge draw channels so layers
  can be emitted out of order before being flattened for the backend. It also
  keeps coarse culling above the primitive draw-list level.
- **LVGL** separates draw tasks from draw units/backends, dispatches tasks into
  layers, and allocates layer buffers lazily. Simple layers may be rendered in
  bounded chunks, while transformed layers require a larger complete transform
  extent. This reinforces SWT's distinction between cheap translated viewport
  planes and transform-heavy presentation.
- **MyGUI** keeps one GUI core while selecting among OpenGL, Direct3D, Vulkan,
  Ogre and other rendering backends. Its cross-backend screenshot comparison
  reinforces keeping SWT's retained paint plan backend-neutral.
- **NanoGUI/NanoVG** reinforces the ordinary retained-widget + immediate vector
  drawing split and the need to keep event/layout ownership above the graphics
  backend rather than embedding semantic widget state in draw commands.

The common distilled rule is:

```text
semantic widgets / events / selection
              |
              v
logical viewport coordinates
              |
              v
ViewportPaintGraph
  retained templates
  affine transforms
  clips + stroke metadata
  z-plane channels
              |
              v
prepare / cull / choose layers
              |
       +------+------+
       |             |
       v             v
    SWT GC       future GPU
  platform API    draw unit
```

`ViewportPaintGraph.replayLayer(...)` and `replayLayers(...)` provide the
first backend-neutral draw-channel contract. Ordinary `replay(...)` keeps its
existing insertion order, so this is additive. Viewport owners may replay only a
dirty plane or specify an explicit back-to-front order such as body, frozen
content, header, editor and feedback.

This deliberately stops short of introducing a GPU dependency into SWT core.
A future GPU backend should consume prepared graph state through a narrow backend
boundary while the existing Cocoa/GTK/Win32 GC paths remain authoritative for
public SWT behavior.

## GPU viewport donor: viewport-lib

`grimandgreedy/viewport-lib` is used as a conceptual donor only. Its GPL-3.0 source is not copied into SWT.

Useful distilled shapes:

- retained draw groups may carry an explicit stable z-order across render families;
- equal-z work keeps insertion order;
- repeated geometry should be instanced/reused rather than rebuilt per visible row;
- host-owned event loops and render targets remain authoritative;
- preparation and paint can be separated so expensive state updates are bounded outside the draw pass;
- picking/event coordinates belong to the viewport coordinate model rather than to transient render shells.

SWT maps those ideas onto its existing owners:

- `org.eclipse.swt.internal.ViewportPaintGraph` retains primitive paint atoms, transforms, paths, clips, layers and stable sibling z-order;
- real SWT `GC` remains short-lived and public API behavior remains immediate-mode;
- `ViewportLayerState` owns coarse BODY/FROZEN/HEADER/EDITOR/SCROLLBAR/FEEDBACK planes;
- `VirtualViewportPlanner` / `VirtualTreeViewport` own logical visible coordinates and overscan;
- public `Item` identities and SWT events remain outside retained rendering.

This donor does not justify adding a wgpu dependency, a second scene graph, or another render-command owner.


## Pending virtual Table scroll hit coordinates

The pending programmatic-scroll translation in GTK Table applies to both GTK3 and
GTK4 virtual tables. Native hit testing still owns column geometry and clipping;
only a valid native row is translated while the requested top row is pending.
An already-settled native top row clears the pending state through the existing path.

The DND cold-row regression uses `getClientArea()`'s actual origin. Cocoa retains a
scrolled document origin, so `(4, rowHeight / 2)` alone can address an off-viewport
row. Assert viewport containment and the display/client round trip before checking
the first and adjacent logical rows, facade identity, pinning and sparse residency.
Those existing correctness and resource bounds remain unchanged.

Canonical task recipe and evidence: Synexia crate
`swt-virtual-table-hit-20261010`. Qualification is per executed backend; the Cocoa
Tree density failures and broader resource/platform gates remain separate.
