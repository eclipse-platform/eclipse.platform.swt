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
| `m3` | As `gdi`, plus Skia GL tiles, GPU raster of recorded content and overlay planes, GPU composite and readback present. Requires the Skija jars on the class path; any failure falls back to GDI. |

## Laws applied

* **Bits, ints and reusable arrays.** Retained state is packed primitives: `long` keys (`mask << 32 | ARGB` or font id), `int` slots, `int[]` rectangles, `long[]` bit sets for tile residency, dirtiness, full validity and visibility. Command streams are `int[]` reused per widget; they grow by doubling with a hard cap.
* **Bitwise masks.** Paint/stroke/font state is one `int` mask (`PaintKeys`): kind, line style, cap, join, antialias, XOR, 12.4 fixed-point width, backend. Planes are bits. The GPU path is a mask bit: `Control.retainedBits & GPU_RETAINED`, set at creation, so every hot-path check is `(retainedBits & GPU_RETAINED) != 0` or `data.planeDraw != -1`. It is not kept in `Widget.state`, whose bits 25..31 belong to the effective-visibility gate, SWT #124.
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
| `internal/gpu/PlaneBackend` | int-only GPU seam; the M3 implementation is loaded reflectively. |
| win32 `graphics/GdiRetained` | Device-wide retained HPEN/HBRUSH/GDI+ solid brushes, pinned while selected in a DC. |
| win32 `graphics/GCPlanes` | Plane gate and op recorder called by every GC draw method once a pass sets `planeDraw`. |
| win32 `widgets/RetainedFrame` | The frame driver for a CANVAS composite. |
| `Eclipse SWT GPU M3/win32/.../M3PlaneBackend`, `M3Target`, `Replay` | Skia GL context, tile pool, frame target, replay through Skija's primitive native entry points. Not part of the Tycho build. |

## Frame (one `WM_PAINT`): the remote-framebuffer (VNC/RFB) model

Each widget keeps a **content framebuffer**: a client-sized DIB, or a GPU render target on the Skija path. It holds the retained planes without overlays and is the source of truth for what is on screen.

1. **Damage (RFB framebuffer-update rectangles).** Content damage is coalesced into one reusable `int[]` rectangle list per frame. It comes from:
   * the paint rectangle, unless a composite-only invalidation requested it (overlay change, scroll band);
   * `Control.redraw`;
   * scroll-exposed bands;
   * the first frame.

   External content damage also drops off-screen tiles, because immediate-mode widgets report damage only for what is visible.
2. **Raster (RFB Raw).** Only the damage rectangles are rasterized: one normal `Paint` event per rectangle, clipped to it, with `planeDraw = RETAINED`. Nothing outside visible ∩ damaged is painted.
   * **GDI:** the pass paints into the content DIB.
   * **Skija:** the pass records into the `OpStream` instead of drawing. That includes `TextLayout` runs as Uniscribe-shaped glyph runs and `Control.drawBackground` fills. If every op is replayable, the stream is replayed once into the GPU content target. Otherwise the pass is painted with GDI and uploaded, and recording is retried after a 32-frame backoff.
3. **Tile cache.** The rasterized rectangles are copied into 256 px content-space tiles: the GDI atlas, or GPU tile targets.
4. **Overlay.** A widget that uses overlay planes gets a present buffer. It receives a copy of the content for the paint rectangle, then a second `Paint` pass with `planeDraw = OVERLAY` draws the selection, caret and feedback. Overlays are never written into the content framebuffer or the tiles.
5. **Present.** One `BitBlt` copies the paint rectangle to the widget's own paint DC. On Skija, a readback of that rectangle into the present DIB comes first; it is the frame's fence.

**Scroll (`Canvas.scroll`) is RFB CopyRect.**
* The screen pixels move exactly as in stock SWT (`ScrollWindowEx`). The exception is widgets with overlays: those present the full client from the present buffer.
* The content framebuffer moves the same block, by a `BitBlt` or a GPU integer-offset copy.
* The tile grid translates.
* The exposed band (`source \ destination`) is refilled from valid tiles (an RFB cache hit, measured as 0 painted pixels when scrolling back) or rasterized. Refill is used only at integral zoom (100%, 200%), because at fractional zoom content is rounded per position.
* Pixels outside the block (`client \ (source ∪ destination)`, for example fixed margins) are never damaged or repainted. Tiles that held them are invalidated as whole columns.

**Seams.** Tiles are device-pixel aligned (256 px), and every copy is an integer-offset blit (`SRCCOPY`, or Skia `SRC` blend with no scaling), so there is no filtering and no gap. Borders and scrollbars are non-client areas the OS draws, so content damage cannot touch them.

**Damage feed.** `RetainedPlanes.DamageListener` is the single adapter: painted rectangles, copied blocks and frame ends. The harness feeds it into `ViewportDamageRecorder` (SWT #121, `damagePx`/`paintedPx`/`protectedHitPx`). It reports overpaint as rasterized pixels and tiles in excess of the OS update area.

**GPU scope.** The GPU only rasterizes and composites the widget's visible ∩ damaged area: content glyph runs and fills, tile copies (translate and refill), and overlay planes. Everything else runs on the CPU:

* layout and shaping (Uniscribe on the UI thread or in the widget's workers);
* glyph positions and op recording;
* damage and tile bookkeeping (packed ints);
* data-structure swap-in and swap-out, sort, filter and search.

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
| `NO_BACKGROUND` | No background fill before raster, as stock. In GPU record mode there is no fill either: pixels the widget does not paint keep the framebuffer's previous content, as on GDI. |
| `DOUBLE_BUFFERED` | Implied: the retained content framebuffer replaces `BeginBufferedPaint` or the per-paint `new Image`. |
| `NO_MERGE_PAINTS` | Raster rectangles are merged only when they touch; separate damage regions get separate `Paint` events. |
| `NO_REDRAW_RESIZE` | Honoured through OS invalidation. Without it, a resize damages the whole client; with it, only the exposed area is rasterized and existing tiles stay valid. |
| `TRANSPARENT`, `RIGHT_TO_LEFT` | The retained bit is not set: stock path. These need the parent's pixels or a mirrored DC. |

`Decorations`/`Shell` and non-CANVAS native controls never get the bit.

## Batches

During a batch (`setRedraw(false)`, so `drawCount > 0` and `WM_SETREDRAW` is off) the viewport is suppressed: no `WM_PAINT` reaches the frame driver, and nothing is rasterized, composited or presented.

* `Control.redraw` still queues content damage in the frame's rectangle list and tile grid: the damage accumulates.
* Native scrollbars and borders are non-client and keep updating as the batch changes the scroll range.
* When the batch ends, the accumulated damage is rasterized once.

## Platform parity

The retained layer is win32-only in this change. The shared pieces live in `org.eclipse.swt.internal.gpu` and compile on every platform:

* the packed-int tables;
* `TileGrid`, `RectList` and `OpStream`;
* `RetainedPlanes` and its facade.

Without a platform hook, `RetainedPlanes.setPlane` is a no-op and `invalidatePlanes` is `redraw(x, y, w, h, false)`. GTK and Cocoa therefore keep stock behaviour. They are **NOT_RUN** on the win32 host this change was measured on.

Bringing either platform onto the retained layer means three pieces:

1. a `RetainedPlanes.Hook` implementation;
2. a GC plane gate in that platform's `GC`;
3. a frame driver in its `Composite` paint path.

Cairo surfaces would replace the DIBs, and GL/Metal would replace the Skija host context. No long operation is added, so `BusyIndicator` does not apply. Scrolling and painting stay live: CopyRect, refill and raster all run inside the normal paint.

## Visibility

`RetainedFrame.effectivelyVisible()` is the single visibility read. It uses `IsWindowVisible` and the shell's `IsIconic` until SWT #124 is merged. After that it becomes `!EffectiveVisibility.paintGated(control) && !EffectiveVisibility.occluded(control)`, which also covers occluded and cloaked shells. A surface that is not visible skips raster, composite and present, and keeps its dirty bits. A surface hidden longer than `swt.gpu.trimMillis` (default 30 s) releases its tiles and back buffer on the next damage.

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
