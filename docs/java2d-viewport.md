<!-- Copyright 2026 Hitesh Soliwal and contributors. SPDX-License-Identifier: Apache-2.0 -->
# Java2D rendering in an SWT logical viewport

`org.eclipse.swt.awt.Java2DCanvas` runs a real Java2D renderer inside an SWT
Canvas. The renderer paints into a raster bounded by the visible client area.
The existing SWT Image/GC implementation presents the pixels. SWT owns the
control, event thread, clipping and resource lifetime; there is no embedded
component or second event loop.

The public type is `Java2DCanvas`. Its package-private raster helper is
`Java2DSurface`. Both use SWT/Java2D vocabulary and depend only on the JDK and
existing SWT classes. No new JNI declaration, native ABI, external rendering
library or runtime service is introduced.

## Ownership and coordinates

| Concern | Owner and contract |
| --- | --- |
| Native control and events | SWT Canvas and its Display thread |
| Large logical extent | Existing `ScrolledComposite.setLogicalContentSize(int, int)` |
| Scroll origin | Existing logical ScrolledComposite; the canvas stays at `(0, 0)` |
| Content view transform | Defensive `AffineTransform` copy held by Java2DCanvas |
| Rendering | Synchronous `Consumer<Graphics2D>` callback |
| Backing pixels and validity | Java2DSurface, bounded by visible client width and height |
| Presentation | Existing SWT Image plus GC; existing ViewportGcProxy restores caller state |

Painting uses `translate(-origin.x, -origin.y) * viewTransform`. The origin is
in the parent's logical pixels after applying the view transform. The canvas
reads an origin only when it is the content of a ScrolledComposite with explicit
logical size. Other parent configurations use origin zero.

Changing the view transform does not infer a new logical extent. The application
sets the corresponding extent on ScrolledComposite. Extents remain nonnegative
Java `int` pixel dimensions; this API does not create an unlimited coordinate
space. Finite singular transforms are valid Java2D input. NaN and infinity are
rejected. Input and returned transforms and points are defensive copies.

## Example: ten million logical rows

The following belongs on the SWT Display thread after creating a shell. A shell
layout such as FillLayout sizes the ScrolledComposite normally.

```java
ScrolledComposite scrolled = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL);
Java2DCanvas canvas = new Java2DCanvas(scrolled, SWT.NONE);
scrolled.setContent(canvas);
scrolled.setLogicalContentSize(2_000, 220_000_000);

canvas.setRenderer(graphics -> {
    java.awt.Rectangle clip = graphics.getClipBounds();
    if (clip == null || clip.isEmpty()) return;
    int firstRow = Math.max(0, Math.floorDiv(clip.y, 22));
    int lastRow = Math.min(9_999_999,
        (int) Math.floorDiv((long) clip.y + clip.height - 1, 22));
    for (int row = firstRow; row <= lastRow; row++) {
        graphics.setColor((row & 1) == 0
            ? java.awt.Color.WHITE : new java.awt.Color(232, 240, 248));
        graphics.fillRect(0, row * 22, 2_000, 22);
        graphics.setColor(java.awt.Color.BLACK);
        graphics.drawString("Row " + row, 8, row * 22 + 16);
    }
});
```

The callback examines its logical clip so model traversal follows the requested
region. Canvas and raster dimensions follow the visible viewport, not the
220-million-pixel logical height. For 125% zoom, set the view transform to
`AffineTransform.getScaleInstance(1.25, 1.25)` and the corresponding logical size
to `(2_500, 275_000_000)`.

## Repaint and lifetime

Call `redraw()` after a model change affecting the whole view. The rectangle
overload invalidates canvas coordinates, matching SWT's existing redraw API.
Unchanged exposure events reuse valid raster pixels. Scroll, view, background
and client-size changes invalidate the affected backing state. The callback
must reconstruct the requested region from model state and must not rely on
callback counts or stale pixels.

For an integer translation, invalid pixels are regenerated through a physically
clipped subimage. Other affine transforms regenerate the entire visible raster.
This preserves exact Java2D geometry: moving the raster origin into a subimage
can change floating-point edge coverage for a rotated or sheared primitive.
The independent interior-damage fixture retains the reproducer. This fallback
still uses viewport-bounded storage, reuses fully valid exposures, and transfers
only the requested native dirty rectangle. It trades some Java2D repaint work
for consistent pixels under general transforms.

The canvas owns each Graphics2D passed to the callback and disposes it when the
callback returns, including exceptional exits. The callback must not retain it.
An application-created child Graphics2D remains the application's responsibility.
Created transfer Images are disposed after presentation; the caller's paint GC
remains caller-owned. Rendering failures propagate without presenting a partial
frame. Reentrant invalidation cannot mark an unfinished frame valid. A resize
or disposal releases raster capacity.

Each callback starts with the canvas background color in the damaged raster,
black paint, and Java2D's default font and rendering hints. The application may
set paints, hints, fonts, images, paths and composites. Alpha conversion preserves
straight ARGB, including source subimages and nontrivial scanline strides; other
BufferedImage formats use Java2D's normalized row conversion. SWT paints its
background before compositing the transferred pixels so newly transparent pixels
cannot expose an older frame.

The raster has one pixel per SWT logical client pixel. Native presentation uses
SWT's existing scaling behavior. The qualified screen fixture uses a 1:1 GTK3/X11
display and explicitly selects `setScrollbarsMode(SWT.NONE)` so native overlay
scrollbars do not obstruct its exact content oracle. Other DPI/backends need
their own pixel evidence. Callback cost still depends on the application scene;
viewport-sized storage is not a universal latency or leak-freedom guarantee.

## Donor comparison and license boundary

Holongate's [J2D4SWT 2.3.0 source release](https://sourceforge.net/projects/holongate/files/latest/download)
uses a real BufferedImage Graphics2D and then transfers raster pixels to SWT.
Its GTK and Windows native acceleration is a packed-pixel transfer, rather than
a translation of arbitrary Java2D commands into SWT GC operations. This task uses
that public behavior as a reference, with independently authored implementation.

Two donor constraints directly inform this implementation: a raster sized to the
entire transformed drawing can grow with logical content, and scrolling only the
visible native pixels can leave an incoherent backing image that reappears after
cover/uncover. The current SWT logical extent and bounded raster validity address
those concerns. The existing SWT ViewportGcProxy and native Image/GC paths remain
their respective owners.

The inspected release archive SHA-256 is
`375cc7383a64e7980e188b9ab485c94f0945f9fef5e1019e66cbf59133174a88`.
Its core source headers identify CPL-1.0; project metadata must not be treated as
permission to relicense those files. No Holongate source was copied into this
implementation. Independently authored files carry Apache-2.0 notices. Existing
SWT source retains its original EPL/EDL or other per-file notices. See
`docs/licenses/synexia-viewport-NOTICE.txt` for the contribution scope.

## Validation and reproducibility

The canonical reusable recipe, pinned pre/postimages, independent headless
fixtures and execution receipts are owned by
`hsoliwal/com.synexia:synexia-openrewrite-recipes/recipe-crates/swt-java2d-viewport-20261011`.
The recipe generates this target contribution from an exact reviewed baseline;
it does not overwrite drifted or already-present additions.

`Test_org_eclipse_swt_awt_Java2DCanvas` checks public API contracts, partial repaint,
native alpha, exceptional rendering, caller GC state and resource ownership. Its
screen sequence uses the existing `swt.viewport.screenshotRegression` opt-in
for the GTK3/X11 lane and captures before, pan, overlapping control, uncover and
fresh frames through `GC(Display).copyArea`. The existing viewport OpenCV lane checks
every decoded pixel and rejects blank, stale, shifted, cropped, missing-overlap
and clip-leak controls. The separate, independently authored headless JUnit
fixture exercises affine damage, compositing, source stride, singular transforms
and invalidation against a Java2D oracle.

The existing `ViewportResourceRegressionTest` owns the separate fresh-process
CPU/RSS/after-GC-heap gates, now including Java2D cold allocation, painted distant
scrolling and resize/zoom scenarios. See `docs/viewport-resource-budgets.md` for
the unchanged budgets and reproducible workload. Screenshot work is outside
resource measurement intervals. Passing one backend or a bounded workload does
not qualify Windows, Cocoa, every DPI, arbitrary model history or the full SWT
reactor. The canonical qualification receipt records actual completed gates and
remaining platform/build constraints.
