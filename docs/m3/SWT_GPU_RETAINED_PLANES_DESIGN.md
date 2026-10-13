# SWT retained GPU planes beneath `GC` (win32)

Status: candidate, draft PR. GDI/GDI+ stay the default and the oracle. GTK and Cocoa: NOT_RUN on this host.

## What it is

A retained layer under the SWT `GC`. Every widget that paints through `GC` benefits without API changes:
`Canvas`, `StyledText`, Nebula `Grid`, and the owner-drawn parts of `Table`/`Tree` (their `EraseItem`/`PaintItem` GCs).
Native `Table`/`Tree` item painting is done by Windows common controls and is outside `GC`; for those widgets only the retained GDI paint objects apply.

Enable it with a system property. It is mapped once, at control creation, to a widget state bit:

| `-Dswt.gpu=` | Effect |
| --- | --- |
| unset | Stock SWT. No retained objects, no frame driver. |
| `gdi` (or `retained`) | Retained GDI pens, brushes and GDI+ solid brushes for every display-thread GC. Retained tiles and planes for CANVAS widgets, composited with GDI. |
| `skija` | As `gdi`, plus Skia GL tiles, GPU raster of recorded content and overlay planes, GPU composite and readback present. Requires the Skija jars on the class path; any failure falls back to GDI. |

## Laws applied

* **Bits, ints and reusable arrays.** Retained state is packed primitives: `long` keys (`mask << 32 | ARGB` or font id), `int` slots, `int[]` rectangles, `long[]` bit sets for tile residency, dirtiness, full validity and visibility. Command streams are `int[]` reused per widget; they grow by doubling with a hard cap.
* **Bitwise masks.** Paint/stroke/font state is one `int` mask (`PaintKeys`): kind, line style, cap, join, antialias, XOR, 12.4 fixed-point width, backend. Planes are bits. The GPU path is a state bit: every hot-path check is `(state & GPU_RETAINED) != 0` or `data.planeDraw != -1`.
* **Objects only when a draw needs them.** Skija `Paint`, `Font`, `TextBlob` and `Surface` objects are created on the first replayed op that needs them, cached by int key, and closed by their owner (`RetainedSlots` evictor or `dispose`). Nothing is created speculatively.
* **The widget is the viewport.** No wrapper control and no child window. The frame is presented with one `BitBlt` into the widget's own `WM_PAINT` DC. The one native helper, the GL host window, is a hidden, disabled, never-shown popup (`WS_POPUP|WS_DISABLED`, `WS_EX_NOACTIVATE|WS_EX_TOOLWINDOW`), not a child of any widget. It never receives input, focus or accessibility traffic.
* **Fail closed.** A GPU error drops the GPU target and the widget repaints on stock GDI.

## Components

| File | Role |
| --- | --- |
| `internal/gpu/RetainedSlots` | `long` key → dense `int` slot; open addressing with backward-shift delete; intrusive `int[]` LRU; pin counts; hard ceiling; `readjust` pressure. M3 bounded-cache contract. |
| `internal/gpu/PaintKeys` | Bit packing for paint/stroke/font keys; COLORREF↔ARGB. |
| `internal/gpu/TileGrid` | 256 px tiles in content space (client + scroll origin). Per slot: one valid rectangle and four `long[]` bit sets. Scroll is `translate`. Dirty-rect → tile range by shifts; set bits iterated with `Long.numberOfTrailingZeros`. |
| `internal/gpu/RectList`, `Rects` | Reusable `int[]` rectangle lists and set operations. |
| `internal/gpu/OpStream` | Packed `int[]` command stream (fill/stroke rect, line, point, oval, gradient, text, shaped glyph run, polygon, clip). |
| `internal/gpu/FontTable` | Font ids keyed by LOGFONT value (never by a recyclable handle), GDI metrics and GDI advance pages. |
| `internal/gpu/RetainedPlanes` | Cross-platform facade: `setPlane(GC, bit)`, `drawnPlanes(GC)`, `invalidatePlanes(Control, bits, x, y, w, h)`. Without a platform hook these are today's behaviour. |
| `internal/gpu/PlaneBackend` | int-only GPU seam; the Skija implementation is loaded reflectively. |
| win32 `graphics/GdiRetained` | Device-wide retained HPEN/HBRUSH/GDI+ solid brushes, pinned while selected in a DC. |
| win32 `graphics/GCPlanes` | Plane gate and op recorder called by every GC draw method once a pass sets `planeDraw`. |
| win32 `widgets/RetainedFrame` | The frame driver for a CANVAS composite. |
| `Eclipse SWT GPU Skija/win32/.../SkijaPlaneBackend`, `SkijaTarget`, `Replay` | Skia GL context, tile pool, frame target, replay through Skija's primitive native entry points. Not part of the Tycho build. |

## Frame (one `WM_PAINT`)

1. **Damage.** The paint rectangle is content damage unless it was requested by a composite-only invalidation (scroll or overlay). `Control.redraw` also damages tiles directly. Content damage drops off-screen tiles: immediate-mode widgets only report damage for what is visible.
2. **Raster.** For each visible tile, compute the visible-but-invalid rectangle. Merge them and send the widget one normal `Paint` event per merged rectangle, into the retained back-buffer DIB, clipped to that rectangle, with `planeDraw = RETAINED`.
   * GDI backend: copy each tile's part into the CPU atlas.
   * Skija backend: the pass records into the `OpStream` instead of drawing. If every op is replayable, each tile is rasterized on the GPU from the stream. Otherwise the same pass runs again with GDI and the result is uploaded to the GPU tile.
3. **Composite.** Visible tiles go into the back buffer, or into the GPU frame target.
4. **Overlay.** If the widget uses overlay planes, a second `Paint` pass with `planeDraw = OVERLAY` draws the selection, caret and feedback planes on top. It runs on GDI, or as recorded ops on the GPU. Tiles are never invalidated by overlays.
5. **Present.** The Skija backend reads the damaged rectangle back into the DIB (the fence). Then one `BitBlt` copies the DIB to the widget's paint DC.

Scrolling through `Canvas.scroll` translates the grid. Only the exposed band is rasterized: unchanged pixels are `client \ (source ∪ destination)`, exposed pixels are `source \ destination`.

## Planes API (for StyledText, Grid and other widget owners)

```java
RetainedPlanes.setPlane(gc, RetainedPlanes.SELECTION); // draw selection here
...
RetainedPlanes.setPlane(gc, RetainedPlanes.CARET);
...
RetainedPlanes.setPlane(gc, RetainedPlanes.BODY);
// on selection or caret change:
RetainedPlanes.invalidatePlanes(control, RetainedPlanes.SELECTION, x, y, w, h);
// to skip layout work for planes not drawn in this pass:
if ((RetainedPlanes.drawnPlanes(gc) & RetainedPlanes.BODY) == 0) { /* overlay-only pass */ }
```

The plane bits reuse the fork's `ViewportRuntime` z-plane values: `BODY`, `FROZEN` and `HEADER` are retained (tile-cached); `EDITOR` and `FEEDBACK`, plus the new `SELECTION` (bit 6) and `CARET` (bit 7), are overlays. When retained planes are off, or on GTK and Cocoa, `setPlane` is a no-op and `invalidatePlanes` is `redraw(x, y, w, h, false)`.

Two changes would let `StyledText` benefit from the retained layer. They are requested of its owner; this leaf does not edit `StyledText*.java`:

1. Scroll with `scroll(...)` instead of `super.redraw()`, so the grid translates instead of rerasterizing every visible line.
2. Draw the selection and caret in overlay planes, and invalidate them with `invalidatePlanes`. Today `TextLayout` draws the selection, and a selected line falls back to GDI raster on the Skija path.

## CPU/GPU split, threading and fences

* **UI thread:** event handling, `Paint` dispatch (recording or GDI raster), the composite, GPU submission and present. GDI DCs and the WGL context are thread-affine: the context is current only on the display thread, and `makeCurrent` re-binds it if application GL code changed the current context.
* **CPU workers:** layout and shaping belong to the widget (the StyledText leaf publishes immutable line snapshots). The retained layer consumes what the paint pass records and never shares mutable state with workers. `OpStream`, `TileGrid` and the slot tables are UI-thread confined.
* **GPU:** rasterizes recorded ops into tile render targets, composites tiles and overlays into the frame target, and is read back.
* **Fence rules:**
  1. `present` calls `flush` and then a synchronous `readPixels` into the DIB. This completes all GPU work of the frame before GDI reads the DIB.
  2. Before the CPU uploads a GDI raster, a GDI call that returns a value (`GetPixel`) flushes the thread's GDI batch.
  3. Tiles are rewritten only on the UI thread, after the previous frame's readback. No tile is sampled by an in-flight frame.

## SWT mask bits honoured

| Bit | Behaviour on the retained path |
| --- | --- |
| `NO_BACKGROUND` | No background fill before raster (as stock). Recorded content is not GPU-replayed without a known background; it uses GDI raster plus upload. |
| `DOUBLE_BUFFERED` | Implied: the retained back buffer replaces `BeginBufferedPaint` or the per-paint `new Image`. |
| `NO_MERGE_PAINTS` | Raster rectangles are merged only when they touch; separate damage regions get separate `Paint` events. |
| `NO_REDRAW_RESIZE` | Honoured through OS invalidation. Without it, a resize damages the whole client; with it, only the exposed area is rasterized and existing tiles stay valid. |
| `TRANSPARENT`, `RIGHT_TO_LEFT` | The retained bit is not set: stock path. These need the parent's pixels or a mirrored DC. |

`Decorations`/`Shell` and non-CANVAS native controls never get the bit.

## Visibility

`RetainedFrame.effectivelyVisible()` is the single visibility read. It uses `IsWindowVisible` and the shell's `IsIconic` until the shared effective-visibility state bit lands. A surface that is not visible skips raster, composite and present, and keeps its dirty bits. A surface hidden longer than `swt.gpu.trimMillis` (default 30 s) releases its tiles and back buffer on the next damage.

## Bounds

| Property | Default | Bounds |
| --- | --- | --- |
| `swt.gpu.tiles` | 256 | Tile slots per widget. Capacity is `2 × tiles(client)`, at least 16. |
| `swt.gpu.gdiObjects` | 256 | Retained objects per GDI table. |
| `swt.gpu.blobs` | 4096 | TextBlobs per backend, for each of strings and shaped runs. |
| `FontTable.CAPACITY` | 1024 | Font ids, pinned. When full, new fonts fall back to GDI. |
| `OpStream` | 4096 ints initial | Hard cap 4 Mi ints; overflow falls back to GDI. |

## Measurements

See `.m3/swt-gpu-retained-planes-20261013/README.md` for the host, the commands and the measured tables.
