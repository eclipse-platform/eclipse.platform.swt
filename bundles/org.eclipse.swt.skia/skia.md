# Architecture: Skia Canvas Feature (`org.eclipse.swt.skia`)

## Overview

The `org.eclipse.swt.skia` plugin extends SWT with hardware-accelerated 2D rendering using the
[Skija](https://github.com/HumbleUI/Skija) Java bindings for the [Skia](https://skia.org/) graphics
library. It integrates transparently into the existing SWT drawing lifecycle and provides with SWT.SKIA a switch between the native rendering backend for canvases and the Skia drawing framework. 

This feature is currently only supported for windows and linux and it requires OpenGL support.

The plugin org.eclipse.swt.skia is structured into four Java packages:

| Package | Purpose |
|---|---|
| `org.eclipse.swt.internal.skia` | Core extension classes: rendering pipeline, resource management, DPI scaling utility (`DpiScalerUtil`) |
| `org.eclipse.swt.internal.skia.cache` | Cache key records for image, text-image, and text-split caches (`ImageKey`, `ImageTextKey`, `SplitsTextCache`) |
| `org.eclipse.swt.internal.graphics` | GC implementation (`SkiaGC`), paint management (`SkiaPaintManager`), text drawing (`SkiaTextDrawing`), image conversion (`SwtToSkiaImageConverter`, `SkijaToSwtImageConverter`, `RGBAEncoder`), color conversion (`SkiaColorConverter`), path conversion (`SkiaPathConverter`), transform conversion (`SkiaTransformConverter`), region calculation (`SkiaRegionCalculator`), rectangle/coordinate conversion (`RectangleConverter`), font metrics (`SkiaFontMetrics`) |
| `org.eclipse.swt.internal.canvasext` | Factory class (`SkiaCanvasFactory`) and logging utility (`Logger`) that plug the Skia backend into the SWT canvas extension framework |

---

## Modification in the existing binary plugins of SWT

To support Skia drawing, only minimal changes were made to the SWT binary fragments. Importantly, these fragments do not depend directly on any Skia resources. Instead, they introduce a lightweight, independent extension mechanism: painting commands are delegated to external handlers (such as the Skia fragment) using the Java ServiceLoader (see `ExternalCanvasHandler`).

If the Skia fragment is not present, SWT automatically falls back to the classic rendering path. When a `Canvas` is instantiated with the `SWT.SKIA` style, the `externalCanvasHandler` field is initialized (if available), and all paint method calls are routed through this handler.

To enable a GC API for Skia, the `final` modifier on `GC` was replaced with `sealed`, allowing only the new `GCExtension` subclass (added in the SWT fragments). `GCExtension` simply delegates all `GC` method calls to an internal delegate. The same approach is used for `FontMetrics` with `FontMetricsExtension`.

In order to use Skia, a connection with OpenGL must be established. For this the `GLCanvasExtension` was created, which is a duplication of the `GLCanvas`, just without extending the class hierarchy of `Canvas`. In order to send a Paint event with OpenGL, the `GLPaintEventInvoker` provides the necessary delegation mechanism. 

There are also other small modifications to enable Skia drawing. But these are no modifications to the SWT API.

The following packages were added or modified in the SWT binary fragments:

| Package | Purpose |
|---|---|
| `org.eclipse.swt.internal.canvasext` | Extension framework: `ExternalCanvasHandler`, `IExternalCanvasFactory`, `IExternalCanvasHandler`, `IExternalGC`, `IExternalFontMetrics`, `DpiScaler`, `FontProperties` |
| `org.eclipse.swt.opengl` | OpenGL integration: `GLCanvasExtension`, `GLPaintEventInvoker` |
| `org.eclipse.swt.graphics` | Extension subclasses: `GCExtension`, `FontMetricsExtension` |

---

### Skia Handler Implementation

The Skia backend provides its handler via the `SkiaCanvasFactory` class, which implements `IExternalCanvasFactory` and is registered as a service in the OSGi bundle via:

```
resources/META-INF/services/org.eclipse.swt.internal.canvasext.IExternalCanvasFactory
```

with the content:

```
org.eclipse.swt.internal.canvasext.SkiaCanvasFactory
```

This enables the `ServiceLoader` to discover the Skia factory at runtime.

### Handler Lifecycle and Resource Management

The handler instance (e.g., `SkiaGlCanvasExtension`) is responsible for creating and managing the base Skia resources (contexts, surfaces) and connecting these to the GL render target.
The handler is attached to the canvas for its lifetime. On canvas disposal, all resources are released via the handler's cleanup logic.

### Error Handling and Fallback

If handler creation fails (e.g., due to missing OpenGL support or Skia initialization errors), the error is logged and the external handler mechanism is permanently disabled for the process. All subsequent canvases fall back to the default SWT rendering path.

---

## Class Overview

### `SkiaGlCanvasExtension`
**Package:** `org.eclipse.swt.internal.skia`

The central class of the plugin. It connects an SWT `Canvas` widget to a Skia rendering surface
backed by an OpenGL framebuffer.

**Inheritance:**
```
GLCanvasExtension
  └── GLPaintEventInvoker
        └── SkiaGlCanvasExtension  (implements ISkiaCanvasExtension, IExternalCanvasHandler)
```

**Responsibilities:**
- Creates and manages the Skia `DirectContext` (OpenGL-backed Skia GPU context).
- Creates and re-creates the `BackendRenderTarget` and `Surface` on canvas resize.
- Maintains a `lastImage` snapshot after each frame so that unchanged areas can be restored
  cheaply without re-executing paint listeners.
- Drives the paint event loop by constructing a `SkiaGC`, wrapping it in a `GCExtension`, and
  sending a paint `Event` to all registered SWT paint listeners.

**Key fields:**
- `skijaContext` — Skia `DirectContext` for OpenGL, created once at construction time.
- `renderTarget` / `surface` — recreated on each canvas resize; `surface` is the drawable Skia canvas.
- `lastImage` — snapshot of the last fully rendered frame, used to restore unchanged areas on partial redraws.
- `redrawCommands` — list of pending redraw areas accumulated between paint cycles; `null` area means full repaint.
- `resources` — shared `SkiaResources` instance for fonts, images, and colors.
- `scaler` — `DpiScalerUtil` for automatic HiDPI coordinate scaling.

---

### `GLCanvasExtension`
**Package:** `org.eclipse.swt.opengl`

Duplicates the functionality of `GLCanvas` but without extending the `Canvas` widget hierarchy.
It manages the OpenGL context, pixel format setup, and provides methods like `setCurrent()`,
`swapBuffers()`, `isCurrent()`, and `getGLData()`. Disposal of the OpenGL context is handled
via an `SWT.Dispose` listener on the canvas.

---

### `GLPaintEventInvoker`
**Package:** `org.eclipse.swt.opengl`

Extends `GLCanvasExtension` and provides the mechanism to invoke paint events within an OpenGL
context. It receives a `Consumer<Event>` (the paint event sender), calls the abstract `doPaint()`
method (implemented by subclasses like `SkiaGlCanvasExtension`), and then calls `swapBuffers()`.

It also manages a `redrawTriggered` flag to schedule additional redraws when needed.

---

### `ISkiaCanvasExtension`
**Package:** `org.eclipse.swt.internal.skia`

Interface implemented by `SkiaGlCanvasExtension` and used by `SkiaGC`. It provides the GC with
access to Skia-specific services without exposing the full extension class.

**Methods:**
- `getSurface()` — returns the current Skia `Surface` for drawing.
- `getResources()` — returns the shared `SkiaResources` instance.
- `createSupportSurface(int w, int h)` — creates a temporary off-screen GPU surface (used for
  cached text rendering).
- `getScaler()` — returns the `DpiScalerUtil` for logical-to-physical coordinate conversion.

---

### `SkiaGC`
**Package:** `org.eclipse.swt.internal.graphics`

Implements `IExternalGC` — the SWT-internal interface that maps all GC drawing calls to a
non-native backend. `SkiaGC` translates every SWT drawing operation into Skija canvas calls.
Drawing operations (stroke and fill) are delegated to `SkiaPaintManager`, which configures
the Skija `Paint` object with color, alpha, line style, pattern shaders, and XOR blend mode.

**Canvas state management (save/restore stack):**

Skija's `Canvas` uses a state stack. Each `save()` pushes the current clip and transform state;
`restore()` pops it. `SkiaGC` manages this stack as follows:
- On construction: `canvas.save()` captures the baseline state (`initialSaveCount`).
- On `dispose()`: `canvas.restoreToCount(initialSaveCount)` undoes all state changes made during
  the lifetime of this GC, including all clipping and transform layers.
- Each `setClipping(...)` call checks the `isClipSet` flag; if a previous clip was set, it calls
  `canvas.restore()` to remove that clip layer. Then it calls `canvas.save()` followed by the
  new clip call to push a fresh clip layer and sets `isClipSet = true`.
- `setTransform(...)` calls `canvas.save()` after applying the matrix so that subsequent clip
  operations are stacked on top of the transform independently.

**SWT-to-Skija coordinate mapping:**
- All SWT coordinates are in logical (device-independent) pixels.
- `DpiScalerUtil.autoScaleUp()` converts logical pixel values to physical pixels before every Skija
  draw call.
- `RectangleConverter.createScaledRectangle(scaler, x, y, w, h)` scales a bounding rectangle and
  converts it to `io.github.humbleui.types.Rect`.
- `RectangleConverter.getScaledOffsetValue(scaler, lineWidth)` computes a 0.5-pixel sub-pixel
  offset applied to stroke operations to prevent anti-aliasing blur when drawing on integer pixel
  boundaries with odd-width strokes.

**Image handling:**
- SWT `Image` objects are converted to Skija `Image` objects on demand via
  `SwtToSkiaImageConverter.convertSWTImageToSkijaImage()`.
- Converted images are cached in `SkiaResources` by `(SWT Image identity, version, zoom)` key.
  The version field invalidates the cache entry when a GC is created on the SWT image, then we expect an image modification.
- The pixel format of the SWT image is detected and mapped to the corresponding Skija `ColorType`.
  Unsupported or ambiguous formats fall back to a full RGBA conversion via `RGBAEncoder`.

---

### `SkiaPaintManager`
**Package:** `org.eclipse.swt.internal.graphics`

Manages the creation and configuration of Skija `Paint` objects for all drawing operations in
`SkiaGC`. Provides two main entry points:

- `performDraw(Consumer<Paint>)` — configures a `Paint` for stroke operations (line width, line
  cap, line style / dash pattern, foreground color, foreground pattern shader, XOR blend mode).
- `performDrawFilled(Consumer<Paint>)` — configures a `Paint` for fill operations (background
  color, background pattern shader, XOR blend mode).

**Pattern handling:**
- `convertSWTPatternToSkijaShader(Pattern)` converts an SWT `Pattern` to a Skija `Shader`:
  - Gradient patterns become `Shader.makeLinearGradient(...)`.
  - Image patterns are converted via `SwtToSkiaImageConverter` and become `image.makeShader(FilterTileMode.REPEAT)`.

---

### `SkiaTextDrawing`
**Package:** `org.eclipse.swt.internal.graphics`

Static utility class responsible for drawing text strings onto the Skia surface. Supports two
modes controlled by the `USE_TEXT_CACHE` flag:
- `drawTextBlobWithCache` — renders text into a cached off-screen image and reuses it for
  identical text/font/color combinations (default, enabled).
- `drawTextBlobNoCache` — renders text directly onto the surface without caching (debug mode).

Handles multi-line text (split by delimiters), transparent backgrounds, and anti-aliasing settings.

---

### `SwtToSkiaImageConverter`
**Package:** `org.eclipse.swt.internal.graphics`

Converts SWT `Image` objects to Skija `Image` objects. Uses `SkiaResources` for caching: if a
cached Skija image already exists for the given SWT image identity, version, and zoom level, it
is returned directly. Otherwise, the SWT `ImageData` pixel buffer is extracted and mapped to the
appropriate Skija `ColorType`.

---

### `SkijaToSwtImageConverter`
**Package:** `org.eclipse.swt.internal.graphics`

Converts Skija `Image` objects back to SWT `Image` objects (used when SWT code needs an SWT
image from Skia-rendered content).

---

### `SkiaColorConverter`
**Package:** `org.eclipse.swt.internal.graphics`

Utility class for converting between SWT `Color` values and Skija integer color representations.
Provides methods for standard conversion, conversion with alpha, and color inversion.

---

### `SkiaPathConverter`
**Package:** `org.eclipse.swt.internal.graphics`

Converts SWT `Path` objects to Skija `Path` objects, applying DPI scaling to all coordinates.

---

### `SkiaTransformConverter`
**Package:** `org.eclipse.swt.internal.graphics`

Converts SWT `Transform` objects to Skija `Matrix33` representations for canvas transformations.

---

### `SkiaRegionCalculator`
**Package:** `org.eclipse.swt.internal.graphics`

Implements `AutoCloseable`. Calculates and manages Skia clip regions from SWT `Region` objects.

---

### `RectangleConverter`
**Package:** `org.eclipse.swt.internal.graphics`

Static utility class for converting and scaling SWT `Rectangle` coordinates to Skija `Rect`
objects. Provides methods such as `createScaledRectangle()`, `createScaledRectangleWithOffset()`,
`scaleUpRectangle()`, and `getScaledOffsetValue()` — all of which use `DpiScalerUtil` for
physical-to-logical pixel conversion.

---

### `RGBAEncoder`
**Package:** `org.eclipse.swt.internal.graphics`

Fallback pixel format converter. When the SWT image pixel format cannot be directly mapped to a
Skija `ColorType`, this class performs a full RGBA conversion of the pixel data.

---

### `DpiScalerUtil`
**Package:** `org.eclipse.swt.internal.skia`

Implements `IDpiScaler`. Wraps a platform-specific `DpiScaler` (or any `IDpiScaler`) and provides
all DPI-aware scaling methods used throughout the Skia plugin: `autoScaleUp(int)`,
`autoScaleUp(float)`, `autoScaleDown(float)`, `autoScaleDown(float[])`, `getZoomedFontSize(int)`,
`scaleSize(int, int)`, and `scaleSurfaceSize(int, int)`. The underlying zoom factor is obtained
from the wrapped `IDpiScaler.getNativeZoom()`.

---

### `SkiaResources`
**Package:** `org.eclipse.swt.internal.skia`

Centralizes all shared Skia resources for a single canvas widget. One instance is created per
`SkiaGlCanvasExtension` and is shared with the `SkiaGC` created for each paint event.

**Responsibilities:**
- **Font management:** Converts SWT `Font` objects to Skija `Font` objects and caches them by
  `FontProperties` (family name, weight, height, italic flag). Font family resolution uses a
  best-fit scoring algorithm against all font families registered in the system `FontMgr`.
  If no match is found, the system default font is used as a fallback. Unresolvable names are
  added to the `unknownFonts` set to avoid repeated lookup overhead.
- **Image cache:** LRU cache (`LruImageCache`) of Skija `Image` objects keyed by
  `ImageKey(SWT Image identity, version, zoom level)`. Maximum capacity: 256 entries.
  Evicted images are automatically closed.
- **Text image cache:** LRU cache of pre-rendered text images keyed by
  `ImageTextKey(text, font properties, transparency flag, background color, foreground color, antialias)`.
  Maximum capacity: 512 entries. Used by `SkiaTextDrawing.drawTextBlobWithCache`.
- **Text split cache:** Caches pre-processed text split arrays (`SplitsTextCache` key) covering
  tab expansion, delimiter splitting, and mnemonic stripping to avoid repeated string processing.
- **Color management:** Stores the current foreground and background `Color` for the GC.
  `resetBaseColors()` is called on `SkiaGC.dispose()` to clear the references and avoid
  retaining disposed colors.
- **Cleanup:** All cached Skija resources are explicitly closed in `resetResources()`, which
  is registered as both an `SWT.Dispose` and `SWT.ZoomChanged` listener on the canvas widget.

---

### Caret Support (not yet implemented)

Caret rendering on the Skia surface is **not yet implemented**. The `SkiaGlCanvasExtension.doPaint()`
method contains comments indicating where caret drawing would be integrated — after the image
snapshot is taken, so that a blinking caret could be redrawn by restoring the last image and
painting the caret on top without re-executing paint listeners. Full caret support requires
additional modifications to `Canvas` and `Caret`.

---

### `SkiaCanvasFactory`
**Package:** `org.eclipse.swt.internal.canvasext`

Implements `IExternalCanvasFactory`. This is the OSGi service registered via the service loader
to plug the Skia backend into the SWT canvas extension framework.

When an SWT `Canvas` with the `SWT.SKIA` style is created, the framework calls
`createCanvasExtension(Canvas)` on this factory. If Skia initialization fails (for example,
because the platform does not support OpenGL), the error is logged via `Logger.logException()`
and Skia is permanently disabled for the current process (`skiaFailedWithErrors = true`), so
that subsequent canvas creations do not repeatedly attempt and fail to initialize Skia.

---

### `SkiaFontMetrics`
**Package:** `org.eclipse.swt.internal.graphics`

Wraps a Skija `FontMetrics` object and implements `IExternalFontMetrics`. Provides ascent, descent,
height, leading, and average character width values to the SWT `FontMetrics` API via
`FontMetricsExtension`. All values are scaled down from physical to logical pixels via `DpiScalerUtil`.

---

## Rendering Pipeline

```
SWT widget calls canvas.redraw()
         │
         ▼
SkiaGlCanvasExtension.redrawTriggered()
  └── adds RedrawCommand to queue, delegates to GLPaintEventInvoker
         │
         ▼
GLPaintEventInvoker.paint(consumer, wParam, lParam)
  ├── calls doPaint(consumer) → dispatched to SkiaGlCanvasExtension
  ├── calls swapBuffers()
  └── if redrawTriggered flag set → canvas.redraw() (schedule next frame)
         │
         ▼
SkiaGlCanvasExtension.doPaint(paintEventSender)
  ├── Determine dirty area (union of all pending RedrawCommands)
  │     └── null area → full repaint
  ├── If partial repaint: draw lastImage onto surface (restore unchanged content)
  ├── surface.getCanvas().save() + clipRect(dirtyArea)
  ├── surface.getCanvas().clear(backgroundColor)
  ├── executePaintEvents(paintEventSender, dirtyArea)
  │     ├── create SkiaGC (saves initial canvas state)
  │     ├── wrap in GCExtension, attach to Event
  │     └── invoke paintEventSender → SWT paint listeners fire
  │           └── listeners call gc.drawXxx() / gc.fillXxx()
  │                 └── SkiaPaintManager.performDraw() / performDrawFilled()
  │                       ├── configure Paint (color, style, shader)
  │                       └── Skija Canvas.drawXxx() calls
  ├── SkiaGC.dispose() → canvas.restoreToCount(initialSaveCount)
  ├── createLastImageSnapshot() — cache frame for next partial repaint
  └── skijaContext.flush() — submit all buffered GPU commands to OpenGL
```

---

## DPI and Coordinate Scaling

Since Skia requires physical pixels, the conversion from SWT logical pixels is handled
transparently by `DpiScalerUtil` (which wraps a platform-specific `DpiScaler` / `IDpiScaler`)
and helper methods in `RectangleConverter`:

- `DpiScalerUtil.autoScaleUp(int)` / `DpiScalerUtil.autoScaleUp(float)` — converts a logical pixel value to physical pixels.
- `DpiScalerUtil.autoScaleDown(float)` / `DpiScalerUtil.autoScaleDown(float[])` — converts physical pixel values back to logical pixels.
- `DpiScalerUtil.getZoomedFontSize(int)` — converts a font size from points to zoomed physical pixels.
- `RectangleConverter.scaleUpRectangle(DpiScalerUtil, Rectangle)` — scales a full rectangle to physical coordinates.

---

## Architecture Decision: `Canvas`, `GCExtension` and `FontMetricsExtension`

In order to minimize API modifications, the only new API is the field in SWT, SKIA. So the classical canvas can be used, just the style SWT.SKIA must be set. This minimizes the modifications, which are necessary for application developers to use the Skia feature.

Only small modifications to the canvas class were necessary.

SWT's `GC` and `FontMetrics` classes are effectively sealed types — they are tightly coupled to
the native platform handles and are not designed for subclassing by external rendering backends.

Rather than refactoring `GC` into a wrapper class with a delegate (which would require
changes across the entire SWT repository), the Skia integration introduces two minimal subclasses:

- **`GCExtension extends GC`**: Overrides all drawing and state methods of `GC` to delegate them
  to an `IExternalGC` implementation (i.e. `SkiaGC`). Native handle access methods throw
  `IllegalStateException` to prevent misuse. `GCExtension` is annotated `@noreference` so that
  it remains an internal implementation detail.

- **`FontMetricsExtension extends FontMetrics`**: Overrides all measurement methods to delegate
  to an `IExternalFontMetrics` implementation (i.e. `SkiaFontMetrics`).

**Rationale:** This approach minimizes the impact on the SWT codebase. No modifications to the
existing SWT widget hierarchy, paint dispatch logic, or public API are required. The Skia backend
plugs in entirely through the `SkiaCanvasFactory` service registration and the two extension
classes. This makes the feature self-contained and easy to enable or disable at the OSGi bundle
level. Further architectural refinements (e.g. a proper wrapper `GC` delegate model) can follow
in a later iteration.

---

## Widgets with Limited or No Support

The Skia backend operates at the `Canvas` level. Higher-level SWT widgets that manage their own
native painting (such as `StyledText`, `Table`, and `Tree`) do not currently use the Skia
rendering path. These widgets paint through native OS controls or through their own internal GC
usage, which bypasses the canvas extension mechanism. `ExternalCanvasHandler` explicitly excludes
`StyledText` and `Decorations` subclasses.

---

## Code Review

The following findings were identified by reviewing the source code referenced in this
architecture document. They are grouped by severity.

### Critical — Resource Leaks and Native Memory

| # | File / Location | Finding |
|---|---|---|
| C1 | `SkiaGlCanvasExtension.onDispose()` (line 94) | **`skijaContext` is intentionally never closed** ("freezes the app"). This indicates a deeper lifecycle or threading issue with the OpenGL context. The `DirectContext` holds GPU resources; leaking it on every `Canvas` dispose accumulates native memory. The root cause of the freeze should be investigated and the context should be properly closed. |
| C2 | `SkiaGlCanvasExtension.onResize()` (line 107) | **Redundant `DpiScalerUtil` allocation on every resize.** A new `DpiScalerUtil` is constructed from `resources.getScaler()` on every resize event, although the field `this.scaler` already exists and wraps the same `DpiScaler`. The local `util` variable should be replaced with `this.scaler`. The same issue occurs in `doPaint()` (line 198). |
| C3 | `SkiaGC.drawPath()` (line 332–334) | **Hardcoded miter limit of 100 000 and anti-alias off.** The caller's `lineJoin`, `miterLimit`, and `antialias` settings are completely ignored; instead, fixed values are applied (`setStrokeMiter(100000)`, `setAntiAlias(false)`, `setStrokeCap(BUTT)`). This breaks the GC contract for any consumer who sets these attributes before calling `drawPath()`. |
| C4 | `SkiaPaintManager.performDraw()` (line 51) | **`lineJoin` is never applied.** The `Paint` object is configured with stroke cap and width, but `paint.setStrokeJoin(...)` is never called. Consequently the Skia default join (MITER) is always used, regardless of what `gc.setLineJoin(SWT.JOIN_ROUND)` requests. Similarly, `dashOffset` and `miterLimit` stored in `SkiaGC` are never forwarded to `createPathEffect` or `paint.setStrokeMiter(...)`. |
| C5 | `SkiaPaintManager.convertSWTPatternToSkijaShader()` (line 166–168) | **Image-pattern Shader created from a cached image that is immediately closed.** The `try-with-resources` block closes the Skija `Image` obtained from `SwtToSkiaImageConverter` right after `makeShader()`. If Skia's `Shader` retains only a reference to the underlying pixel data (and does not deep-copy it), this produces a use-after-free on the GPU. Whether this is safe depends on Skija internals and should be verified or the image should be kept alive as long as the Shader is in use. |
| C6 | `SkiaTextDrawing.drawTextBlobWithCache()` (line 227) | **`makeImageSnapshot()` result is cached but never explicitly closed outside the cache.** The snapshot is stored in the `textImageCache` LRU cache and will be closed when evicted. However, if `cacheTextImage()` replaces an existing entry (same key), the old image is closed via `old.close()` — but `textImageCache.get(key)` is called first, which promotes the entry in the LRU and may interfere with the eviction check. Verify that the double-close guard (`if (!old.isClosed())`) is always reached. |

### High — Correctness and Behavioral Bugs

| # | File / Location | Finding |
|---|---|---|
| H1 | `SkiaGC.drawPolygon()` (line 370–373) | **Mutates the caller's `pointArray` in place** by decrementing x-coordinates for the `SWT.MIRRORED` style and then incrementing them back afterwards. If the paint listener is concurrent or the array is reused, this is a race condition. A defensive copy should be used instead of modifying the input array. |
| H2 | `SkiaGC.setTransform()` (line 595–603) | **Cumulative canvas saves without matching restores.** Each call to `setTransform()` calls `canvas.save()` but never calls `canvas.restore()` for the previous transform layer. If `setTransform()` is called multiple times, the save stack grows unboundedly until `dispose()` restores to `initialSaveCount`. While not a memory leak (Skia's stack is lightweight), it is semantically incorrect — subsequent `setClipping()` operations interact with each stacked layer in unexpected ways. |
| H3 | `SkiaGC.setClipping(Path)` (line 896–918) | **`currentClipBounds` and `currentClipRegion` are not cleared** when clipping is set via a `Path`. After calling `setClipping(Path)`, a subsequent call to `getClipping()` would return stale bounds from a previous `setClipping(Rectangle)` call. |
| H4 | `SkiaGC.executeTextDraw()` (line 284) | **Early-return check uses physical surface size against logical coordinates.** `surface.getWidth()` returns physical pixels, but `x` and `y` are logical (unscaled) SWT coordinates. On a 150 % DPI display, text at logical x = 800 would be rejected even though the physical surface is 1200 px wide. The comparison should either scale the surface bounds down or the coordinates up. |
| H5 | `SkiaResources.findBestFit()` (line 233–264) | **Font matching returns false positives.** The algorithm scores each system font family by counting how many whitespace-separated tokens of the requested font name appear in the family name. A request for `"Segoe"` would match `"Segoe UI"` (score 1) but also `"Segoe MDL2 Assets"` (score 1) with the same score. The first family that reaches the top score wins — order depends on `FontMgr` enumeration. A stricter scoring (e.g. exact-match bonus, prefix matching, length penalty) would be more robust. |
| H6 | `SkiaResources.textExtent()` (line 463–467) | **`flags` parameter is ignored.** The `flags` argument (e.g. `SWT.DRAW_MNEMONIC`, `SWT.DRAW_DELIMITER`, `SWT.DRAW_TAB`) is accepted but never evaluated. The measurement always returns the raw single-line text extent, even when the caller expects multi-line measurement. |
| H7 | `SkiaResources.expandTabs()` (line 455) | **Per-character measurement via `String.valueOf(ch)` creates a new `String` object for every character.** The comment above it even says "measureTextWidth avoids creating an intermediate String object per character" — but it does exactly that. For long strings this causes excessive allocation pressure. Consider measuring runs of non-tab characters as a single string. |
| H8 | `ExternalCanvasHandler.createHandler()` (line 128) | **Catches `Throwable`** (including `Error`, `OutOfMemoryError`, `StackOverflowError`). This is extremely broad. A fatal JVM error should not be silently swallowed with `FAILED_WITH_ERRORS = true`. Consider catching `Exception` only, and rethrowing `Error` subclasses. The same issue exists in `SkiaCanvasFactory.createCanvasExtension()`. |

### Medium — Design, Maintainability, and Performance

| # | File / Location | Finding |
|---|---|---|
| M1 | `SkiaGC` (class level) | **1 258-line God class.** `SkiaGC` implements the entire `IExternalGC` interface in a single class with no delegation other than `SkiaPaintManager` for paint configuration. Methods like `copyArea`, `textLayoutDraw`, `fillGradientRectangle`, and `drawImage` with their complex logic should be extracted into focused helper classes (similar to how `SkiaTextDrawing` was extracted). |
| M2 | `SkiaGC.writeFile()` (line 226–239) | **Debug/test method left in production code.** The static `writeFile()` method writes a PNG snapshot to an arbitrary file path. It has no access protection and is documented as "Test method for drawing an image." This should be removed or moved to a test utility class. |
| M3 | `SkiaGC.getAdvanceWidth()` (line 787–803) | **Abuses `AtomicInteger` and `performDraw()` to measure text width.** `performDraw()` creates a full `Paint` object with stroke configuration just to measure a single character. The actual measurement only needs a `Paint` in FILL mode. A simpler approach would be to call `font.measureTextWidth()` directly. |
| M4 | `SkiaGC.getGCData()` (line 827–829) | **Returns `null`.** Any caller that depends on non-null `GCData` (which is common in SWT internals) will get a `NullPointerException`. If `GCData` is not applicable for Skia, consider returning a dummy instance or throwing `UnsupportedOperationException`. |
| M5 | `DpiScalerUtil` (constructors) | **Three constructors with overlapping concerns.** `DpiScalerUtil(IDpiScaler)`, `DpiScalerUtil(int)`, and `DpiScalerUtil(SkiaResources)` all set `this.scaler`. The `SkiaResources` constructor just delegates to `resources.getScaler()`, so the caller could pass the `IDpiScaler` directly. Multiple constructors increase the API surface without clear benefit. |
| M6 | `DpiScalerUtil.getZoomedFontSize()` (line 43–46) | **Calls `Display.getDefault()` from a utility class.** This couples the utility to the SWT display singleton and makes it untestable. The DPI value should be injected or obtained from the canvas that is already available in the call chain. |
| M7 | `SkiaResources` (field level) | **Caches (`fontCache`, `fontNameMapping`, `imageCache`, `textImageCache`, `cachedTextSplits`) grow without bounds (except the LRU caches).** `fontCache`, `fontNameMapping`, and `cachedTextSplits` are plain `HashMap` instances that never evict entries during the canvas lifetime. For long-running applications with many font or text variations this can become a memory issue. |
| M8 | `SkiaResources.getSkijaFont()` (line 160–162) | **Redundant cache lookup.** After `fontCache.put(props, f)` on line 158, line 162 does `return fontCache.get(props)` instead of simply `return cachedFont` (which at this point holds the same reference as `f`). In fact, `cachedFont` is not `null` on line 162 — this code is unreachable because the `if (cachedFont == null)` branch already returned on line 159. The dead code should be simplified. |
| M9 | `ExternalCanvasHandler` (line 25–26) | **Non-final static mutable fields (`FAILED_WITH_ERRORS`, `ExternalCanvasWasLogged`) with no synchronization.** These flags are written and read from the SWT UI thread, which is single-threaded in practice, but the class provides no documentation of this threading assumption. Also, `FAILED_WITH_ERRORS` does not follow Java naming conventions for non-constant fields. |
| M10 | `RectangleConverter` (line 52–61) | **Direct access to internal Skija field `_radii`.** `rect._radii` accesses a package-private or internal field of `RRect`. This is fragile and may break with Skija library updates. Use a public accessor if available. |
| M11 | `SkiaGC.copyArea(Image, int, int)` (line 689–724) | **Inefficient round-trip conversion.** The method creates a Skija snapshot, converts it to SWT `ImageData`, creates a new SWT `Image`, creates a native `GC`, draws the image, and disposes everything. This GC-on-GC approach is slow and allocates significant temporary resources. A direct Skija-to-Skija blit would be more efficient. |
| M12 | `SkiaGC.textLayoutDraw()` (line 1214–1238) | **Falls back to native GC rendering.** The method creates a temporary SWT `Image`, draws the `TextLayout` with a native `GC`, then converts and blits the result into the Skia surface. This means text layout rendering is always rasterized at 100 % zoom and then scaled, losing quality on HiDPI displays. A comment explaining why this fallback is needed and a TODO for a native Skia `TextLayout` implementation would be helpful. |

### Low — Style and Minor Issues

| # | File / Location | Finding |
|---|---|---|
| L1 | `SkiaGC` (line 82–85) | **TODO comment for `dashOffset` and `miterLimit`** has been present since initial commit. These fields are stored but never applied to the Skia `Paint`. Either implement them or remove the fields and throw `UnsupportedOperationException` in the setters. |
| L2 | `SkiaGlCanvasExtension` (line 181) | **Typo in comment:** `"if if we don't even have"` — double "if". |
| L3 | `ExternalCanvasHandler` (lines 44–65) | **Verbose boolean-returning methods.** `isDisabled()`, `isForcedEnabled()`, and `isLogActive()` can be simplified from `if (x != null) { return true; } return false;` to `return x != null;`. |
| L4 | `SkiaGC` (line 68) | **`logImageNullError` static flag** suppresses image-null errors after the first occurrence globally across all GC instances. This makes debugging difficult if the issue occurs in different contexts. Consider per-instance logging or a counter. |
| L5 | `SkiaPaintManager` (line 107) | **Unused parameter.** `getScaledPathFloats()` accepts a `DpiScalerUtil scaler` parameter but never uses it. The parameter should be removed. |
| L6 | `SkiaResources.replaceMnemonics()` (line 478–485) | **`lastIndexOf('&')` is computed but its result is only used to check `!= -1`.** The actual index is never used for underlining. This is currently just a check before calling `replaceAll("&", "")` — the `lastIndexOf` check is redundant since `replaceAll` on a string without `&` is a no-op. |
| L7 | Multiple files | **Inconsistent field-access style.** Some methods use `this.surface`, others use `surface` directly. Some use `getScaler()`, others use `this.dpiScalerUtil` or create a new `DpiScalerUtil`. A consistent convention should be established. |

---

## Known Limitations and Open TODOs

- **Mnemonic underlining:** `replaceMnemonics()` strips `&` prefix characters from text but does
  not yet draw an underline beneath the mnemonic character.
- **No Caret support currently** (caret rendering is not yet implemented; see comments in `SkiaGlCanvasExtension.doPaint()`)
- **No Skia TextLayout**

---