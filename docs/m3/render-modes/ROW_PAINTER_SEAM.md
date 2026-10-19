# The M3 row-painter seam (for the viewport-everywhere track)

Status: stable seam of the render-modes leaf (PR #202). Read-only for other tracks: copy or port, do not edit here.

The drawn planes of SWT's Tree, Table and List paint every row through three small Java pieces that know nothing
about SWT widgets, only about rows. That makes them the shared row painter for the other toolkits of the
viewport-everywhere track (Swing, AWT, IntelliJ, RWT): each toolkit fills a `RowPaint` from its own model and hands it
to a `RowRenderer` with a `RowCanvas` of its own surface. The M3 renderer (Skija library underneath) is the canvas that
is the same on every toolkit, which is how "Skija maps across the libs".

## The three pieces (package `org.eclipse.swt.internal.render`, `Eclipse SWT/common`)

| Piece | What it is | Toolkit knowledge |
|---|---|---|
| `RowPaint` | one row as data: row rectangle, selection rectangle, expander and check box slots, state bits, depth/indent/index, per-cell lanes (box, text box, image box, alignment, text, image, font handle, colours), frame defaults | none (ints, Strings, one `Image` lane) |
| `RowRenderer` + `Theme` | paints a `RowPaint` in a fixed order (background, cell backgrounds, selection, expander, check box, cell content, grid lines, focus) with the colours, look, glyph images and painters of a `Theme` | none |
| `RowCanvas` | eleven primitives: `begin`, `end`, `fill`, `text`, `image`, `imageData`, `polygon`, `part`, `focus`, `dispose`, plus the part/state constants | the backend's only |

Contract of a frame: `canvas.begin (damage)`, then `renderer.paint (canvas, row)` for every row that intersects the
damage (top to bottom; the first one found by arithmetic, the loop stops below the damage), `renderer.paintEmpty (...)`
for the area below the last row, `canvas.end ()`. Colours are `0xRRGGBB` ints (-1 = default); geometry is device pixels
of the surface; nothing outside the damage changes.

## Backends that exist

| Backend | Surface | Where |
|---|---|---|
| `GcRowCanvas` | GDI DC of a DIB back buffer, uxtheme parts | win32, `Eclipse SWT/win32/.../internal/render` |
| `M3RowCanvas` (CPU) | the M3 renderer over the same DIB (`Surface.makeRasterDirect`) | `Eclipse SWT Render M3/win32/.../internal/render/m3` |
| `M3RowCanvas` (GPU) | the M3 renderer on an OpenGL render target, read back into the DIB; one context per UI thread (`M3Gpu`) | same |

`M3RowCanvas` is the one adapter that touches the library's binding classes (`io.github.humbleui.*`); it is loaded by
name, so code without the library jars still runs. Its paint path is data oriented: fills and text go through the
library's primitive native entry points on cached pointers, fonts and shaped lines are bounded LRUs.

## How another toolkit plugs in

1. **Fill `RowPaint` from your model**, not from objects per row: reuse one instance per frame (`cells (count)` grows
   the lanes and clears them). A Swing `JTree`/`JTable`/`JList` viewport fills it from its row model and the
   `TreePath`/row index of the visible range; IntelliJ from the tree model of `Tree`/`JBTable`; RWT from its own lanes.
2. **Pick a canvas**: the M3 canvas works on any surface the library can wrap: a Java2D `BufferedImage`'s pixels
   (`Surface.makeRasterDirect` over a direct buffer), an AWT/Swing component painted from a pixel buffer, or an OpenGL
   target. The win32 `GcRowCanvas` is SWT-specific; a Java2D canvas (`Graphics2D` primitives) is a small new class.
3. **Theme parts** (`part (PART_SELECTION | PART_EXPANDER | PART_CHECKBOX, state, ...)`): the native look asks the
   canvas for the platform's parts. On win32 `ThemeParts` renders uxtheme parts into images (premultiplied BGRA) that
   any canvas can draw; other toolkits map parts to their own look (Swing `UIManager` icons, IntelliJ `UIUtil`), or use
   the `swing`/`initiative31` themes, which draw with primitives only.
4. **Themes are shared**: `Themes.register` / `Themes.select` work the same for every toolkit, so one theme gives
   SWT, Swing and IntelliJ rows the same look.

## What a port must keep

- No per-row objects on the paint path (rule 18): lanes in, primitives out.
- Geometry stays the toolkit's (row height, indent, label position): a theme changes pixels, never answers.
- Every visual comes from the theme (rule 16).
- Proof the same way: a recorded differential of the toolkit's public API against its stock behaviour, pixels as a
  similarity metric.

## Tests that pin the seam

- `Test_org_eclipse_swt_internal_render_RowRenderer`: the paint order and every look through a recording canvas.
- `Test_org_eclipse_swt_internal_render_Themes`: the theme API.
- `Test_org_eclipse_swt_internal_render_M3Canvas` and `..._Win32Backends`: the backends without a display.
