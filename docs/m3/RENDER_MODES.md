# Render modes: drawn planes for Tree, Table and List

Status: design (deliverable 1). Implementation status per leaf is at the end.

## The framing: drawn modes are viewport modes with a different plane

The viewport approach is king. `custom`, `skija` and `gpu` are **not new widgets**; they are the viewport
mode with another plane.

- The mediated model is the single source of truth in every mode: topology, visible projection, lanes, scroll
  metrics, resident window and facades (`VirtualTreeTopology`, `VirtualTreeVisibleProjection`,
  `VirtualTreeViewport`, `VirtualScrollMetrics`, `VirtualTreeResidentPlan`, `TreeViewport` and its Table and
  List siblings). No mode keeps a copy of the model.
- The only difference from the native viewport is the plane. In `viewport` mode a native TreeView, ListView or
  ListBox holds the resident window and paints it. In a drawn mode a drawn surface (GC, Skija raster, or the
  graphics hardware) paints exactly the resident window.
- One planner drives every plane, with the phases and names of `TreeViewport`: capture, plan, retire/insert,
  commit, bind, restore focus. A plane only adds a paint of the committed window.
- No per-mode model copies and no O(n) work per frame: a frame paints the rows that intersect the damage, found
  by arithmetic over the resident window.

## Modes and how one is selected

| `-Dswt.widget.mode` | Plane | Pixels |
|---|---|---|
| `viewport` (default) | native control holding the resident window | the platform's |
| `original` | stock SWT, no mediation | the platform's |
| `custom` | drawn plane, backend `GC` | SWT `GC` |
| `skija` | drawn plane, backend Skija raster | Skia (CPU), into a DIB section |
| `gpu` | drawn plane, backend graphics hardware | Direct2D on win32; OpenGL (Skija over `GLCanvas`) elsewhere |

`-Dswt.widget.mode.Tree=custom` selects a mode for one widget class, as for every widget.

Selection, in code:

- The mediator keeps its own query, `WidgetMode.of ("Tree", VIEWPORT) == VIEWPORT`. A drawn mode requested for
  Tree is not in that list, so `WidgetMode` falls back to `VIEWPORT` there and the mediator stays on. Nothing in
  the mediator changes.
- The plane asks `WidgetMode.of ("Tree", VIEWPORT, CUSTOM, SKIJA, GPU)` once, when its class initializes, and
  installs a drawn plane for `CUSTOM`, `SKIJA` or `GPU`. A backend that cannot start (no Skija jars on the class
  path, no Direct2D) logs once at `info` and falls back: `gpu` to `skija` to `custom`.
- `original` keeps stock SWT everywhere (it is the compatibility reference).

## The plane and its two stages

A plane owns the resident window's realization. The phases are the mediator's:

| Phase | Native plane (`viewport`) | Drawn plane |
|---|---|---|
| capture | read native caret, selection, check state of resident rows | same (stage 1), lanes only (stage 2) |
| plan | `VirtualTreeResidentPlan` diff of old and new window | same planner |
| retire/insert | `TVM_DELETEITEM`/`TVM_INSERTITEM` of changed rows only | same on the behaviour plane (stage 1); no native rows (stage 2) |
| commit | plan committed, keys and handles | same; the drawn plane's row cache is keyed by plan keys |
| bind | facades bound to resident rows | same |
| restore focus | caret restored selection-neutrally | same |
| paint | comctl32 paints the window | the drawing core paints the window |

**Stage 1 (this programme's first leaves).** The native control stays as the **behaviour plane**: it still holds
the resident window, takes keyboard and mouse input, owns the caret and answers hit tests and item rectangles,
exactly as in `viewport` mode, so every public answer, event and SetData is the viewport's by construction. It
never paints to the screen: its paint runs into an off-screen scratch bitmap, only for its side effects (SetData
of rows coming into view, label measurement, custom-draw notifications), and the drawing core paints the window.
The drawn rows use the behaviour plane's geometry (row rectangles, label rectangles, column rectangles), so what
the user sees is where the user clicks.

**Stage 2 (later leaves).** The behaviour plane's duties move to the model leaf by leaf, each proved by the same
recorded differential: label measurement and SetData timing from the projection, keyboard (Initiative 31
`ListLikeModel` anchor/current pattern, the measured page rules of the Tree viewport), mouse and hit testing from
row arithmetic, focus. When the last duty has moved, the native control is no longer created in drawn modes.

## Drawing core (one for the three drawn modes)

```
RowSource  --rows on the plane-->  RowRenderer (theme)  --ops-->  RowCanvas (backend)
                                        |                              GcCanvas        custom
                                   Theme defaults (packed ints)        SkijaCanvas     skija
                                                                       Direct2DCanvas  gpu (win32)
                                                                       SkijaGlCanvas   gpu (GTK, Cocoa)
```

- **`RowSource`**: the rows of the resident window that intersect the damage, top to bottom. Per row: plan key,
  facade, row rectangle, label rectangle, depth, state bits (selected, focused, hot, expanded, has children,
  checked, grayed). Stage 1 reads them from the behaviour plane (O(rows in the damage) native queries); stage 2
  from the projection and lanes. The first row is found by arithmetic (`(damageTop - planeTop) / itemHeight`), the
  loop stops at the first row below the damage (Initiative 31 T1/T2).
- **`RowRenderer`**: one per theme, shared by Tree, Table and List (a List is a Table with one column and no
  header; a Table is a Tree of depth 1). It paints a row's background (selection, hot, stripes), expander,
  check box, image, text per column, grid lines and focus rectangle, in this order, through a `RowCanvas`. It
  emits SWT's owner-draw events (`MeasureItem`, `EraseItem`, `PaintItem`, Initiative 31 T9 order) when the
  application listens (stage 2; in stage 1 the behaviour plane emits them).
- **`RowCanvas`**: the backend. `fillRect`, `line`, `text`, `image`, `part` (a theme part such as an expander
  glyph or a check box), clip, begin and end of a frame. Colours are ARGB ints, fonts are SWT `Font`s.
- **Theme defaults**: a table of packed ints (colours, sizes, glyph style) resolved once per theme, keyed with
  Swing/FlatLaf names (`Tree.selectionBackground`, `Tree.hash`, `Table.gridColor`, ...).

Backends:

| Backend | Frame | Text | Images and parts |
|---|---|---|---|
| `GcCanvas` (custom) | paints into the paint DC (double-buffered by the widget) | `GC.drawString` (GDI, the native font) | `GC.drawImage`; theme parts through uxtheme `DrawThemeBackground` |
| `SkijaCanvas` (skija) | `Surface.makeRasterDirect` over a DIB section kept per widget, resized only on size change; damage `BitBlt`-ed | Skija `Font` from the SWT font's face name and pixel height; shaped `TextLine` LRU keyed by (string, font) | SWT image pixels converted once into Skija `Image`s, LRU; theme parts rendered once by uxtheme into an image cache |
| `Direct2DCanvas` (gpu, win32) | records the frame into an `OpStream` (packed ints) and replays it in **one native call**: `ID2D1DCRenderTarget::BindDC` to the paint DC, `BeginDraw`, ops, `EndDraw` | DirectWrite text formats per font, cached natively by font id | bitmaps cached natively by id; theme parts as for Skija |
| `SkijaGlCanvas` (gpu, GTK and Cocoa) | Skija `DirectContext.makeGL` over SWT's `GLCanvas` context, persistent surface, damage union, last-frame snapshot (skija-canvas S1-S3) | as Skija | as Skija |

Work split (brief rule 5): the UI thread does O(rows in the damage) per frame. Font, text and image caches are
bounded LRUs. Device creation (Direct2D's first hardware target takes about 1.3 s) is started once per display,
off the paint path.

## Look and feel (theme)

`-Dswt.widget.theme=native|swing` (global) and `-Dswt.widget.theme.<Widget>=...` select the theme of the drawn
planes at startup, like Swing's `UIManager.setLookAndFeel`. The planned name in
[ALL_WIDGETS_VIEWPORT_PLAN.md](ALL_WIDGETS_VIEWPORT_PLAN.md) section 9, `swt.lookAndFeel`, is read as an alias.
A theme changes pixels only, never the model, the geometry, the API or the events: row height, indent, label
positions and column rectangles are the behaviour plane's in every theme.

- **`native`** (default): Windows 11 Explorer TreeView/ListView look. Selection, hot and focus backgrounds,
  expander glyphs (chevrons) and check boxes are drawn with the platform's own theme parts (uxtheme
  `Explorer::TreeView` `TVP_TREEITEM`/`TVP_GLYPH`/`TVP_HOTGLYPH`, `Explorer::ListView` `LVP_LISTITEM`,
  `BUTTON` `BP_CHECKBOX`). Text uses the widget's font and colours. Values that are not theme parts (text
  colours, grid colour) are measured from stock pixels.
- **`swing`**: Swing Metal (Ocean) look: selection `Tree.selectionBackground`, Metal tree handles (boxed plus and
  minus) with dotted `Tree.hash` lines, Metal check box, grid lines `Table.gridColor`. Values are measured once
  from JDK Swing renders (numbers only; the JDK's source stays out of EPL files).

## GPU route (user decision 2026-10-17: Direct2D on win32, OpenGL elsewhere)

Probes (sources in [render-modes/evidence](render-modes/evidence)) on this host (Windows 11, JDK 21), run on the
invisible test desktop, 300 x 380 pixels, a frame = clear + one filled row + 20 text rows:

| Route | Result | First setup | Per frame |
|---|---|---|---|
| Direct2D `ID2D1DCRenderTarget`, `D2D1_RENDER_TARGET_TYPE_HARDWARE`, bound to a GDI memory DC | correct pixels (fill and DirectWrite text) | 1,318 ms | 3.0 ms |
| the same, `SOFTWARE` | correct | 44 ms | 1.1 ms |
| the same, `DEFAULT` | correct | 802 ms | 1.5 ms |
| Skija `DirectContext.makeGL` on SWT `GLCanvas` (WGL through SWT's own natives) + `swapBuffers` | correct pixels, readback works | 3,466 ms (includes loading Skija's native library) | 21.8 ms |

Decision:

- **win32: Direct2D through a minimal JNI shim** (new native code, the only part of this programme that needs
  it). No donor path creates a Direct2D factory from Java: SWT's `COM.VtblCall` passes the object as the first
  argument and `OS.CallWindowProc` truncates the second argument to 32 bits, so neither can call
  `D2D1CreateFactory (enum, REFIID, options, out)`; Direct2D methods with float arguments in XMM registers cannot
  be called through them either. JNA would add a runtime dependency; Java's FFM API is a preview in JDK 21. Skija's
  `makeDirect3D` needs a D3D12 device created natively. The shim exposes a handful of entry points (create, font,
  bitmap, frame replay of the `OpStream`, dispose), built with mingw-w64.
- **GTK and Cocoa: OpenGL through Skija** on SWT's `GLCanvas` (skija-canvas pattern S1-S3). The probe shows the
  route works on win32 too (it is the fallback there if Direct2D is unavailable). GTK and Cocoa runs: NOT_RUN on
  this host.
- The hardware Direct2D target is a DC render target, so `WM_PRINT` (used by the differential's pixel capture)
  and `WM_PAINT` share one path.

## Proof

- **Contract (gate):** the recorded differentials of Tree (plain and check+columns variants), Table and List run
  with `-Dswt.widget.mode=custom|skija|gpu` against the stock recording of the official SWT 3.135.0 jar. Every
  non-pixel probe (answer, scroll bar, selection, caret, events, geometry, hit tests) must be equal.
- **Pixels (metric, not a gate):** drawn modes differ by design. The harness reports, per seed, the share of
  pixels equal to the stock capture and the mean absolute channel difference, instead of failing on the hash.
- **Benchmark:** [render-modes/bench](render-modes/bench) (deliverable 2): per widget x mode x operation, wall
  time and allocated bytes, on the invisible desktop, one JVM per mode.

## Leaves

| Leaf | Scope | Gate |
|---|---|---|
| R1 | this design and the donor inventory | review |
| R2 | mode benchmark, viewport and original first | TSV under `render-modes/bench` |
| R3 | drawing core + `GcCanvas` + Tree drawn plane (stage 1) | Tree differentials (both variants) in `custom`: all non-pixel probes equal to stock |
| R4 | `SkijaCanvas` | the same in `skija` |
| R5 | Direct2D shim + `Direct2DCanvas` | the same in `gpu` |
| R6 | Table, then List, on the same core | Table and List differentials in the three drawn modes |
| R7.. | optimization passes driven by the benchmark (before/after numbers each) | benchmark + differentials |
| S2.x | stage 2: behaviour duties move from the native control to the model | the same differentials |

## Donors

[render-modes/FOSS_REUSE_DECISION.tsv](render-modes/FOSS_REUSE_DECISION.tsv). In short: the viewport model and
planner, `WidgetMode` and the retained-planes `OpStream` are reused from this repository; Windows theme parts
through SWT's existing natives; Skija (Apache-2.0) as a library for the skija plane and the OpenGL route;
skija-canvas's text LRU and GL surface are adapted; Initiative 31's custom widget classes are not used (a second
model per widget), its owner-draw order and paint cull are patterns; the SWT-on-Swing projects (SWTSwing,
Initiative 31 prototype-swing) are patterns for the row renderer, the Swing theme keys and later the input mapping;
SwingWT, DJ Native Swing, SWT_AWT and Albireo embed rather than draw (no fit).

## Status

| Leaf | State |
|---|---|
| R1 | this document |
| R2-R7 | not started in this commit |

Non-claims: no drawn plane exists yet; GTK and Cocoa are not run on this host.
