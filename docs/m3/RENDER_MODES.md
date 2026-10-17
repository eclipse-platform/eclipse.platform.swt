# Render modes: drawn planes for Tree, Table and List

Status: design (deliverable 1). Implementation status per leaf is at the end.

## The framing: drawn modes are viewport modes with a different plane

The viewport approach is king. `custom`, `m3` and `gpu` are **not new widgets**; they are the viewport
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
| `m3` | drawn plane, backend Skija raster | Skia (CPU), into a DIB section |
| `gpu` | drawn plane, backend graphics hardware | Direct2D on win32; OpenGL (Skija over `GLCanvas`) elsewhere |

`-Dswt.widget.mode.Tree=custom` selects a mode for one widget class, as for every widget.

Selection, in code:

- The mediator keeps its own query, `WidgetMode.of ("Tree", VIEWPORT) == VIEWPORT`. A drawn mode requested for
  Tree is not in that list, so `WidgetMode` falls back to `VIEWPORT` there and the mediator stays on. Nothing in
  the mediator changes.
- The plane asks `WidgetMode.of ("Tree", VIEWPORT, CUSTOM, M3, GPU)` once, when its class initializes, and
  installs a drawn plane for `CUSTOM`, `M3` or `GPU`. A backend that cannot start (no Skija jars on the class
  path, no Direct2D) logs once at `info` and falls back: `gpu` to `m3` to `custom`.
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
                                   Theme defaults (packed ints)        M3Canvas     m3
                                                                       Direct2DCanvas  gpu (win32)
                                                                       M3GlCanvas   gpu (GTK, Cocoa)
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
| `M3Canvas` (m3) | `Surface.makeRasterDirect` over a DIB section kept per widget, resized only on size change; damage `BitBlt`-ed | Skija `Font` from the SWT font's face name and pixel height; shaped `TextLine` LRU keyed by (string, font) | SWT image pixels converted once into Skija `Image`s, LRU; theme parts rendered once by uxtheme into an image cache |
| `Direct2DCanvas` (gpu, win32) | records the frame into an `OpStream` (packed ints) and replays it in **one native call**: `ID2D1DCRenderTarget::BindDC` to the paint DC, `BeginDraw`, ops, `EndDraw` | DirectWrite text formats per font, cached natively by font id | bitmaps cached natively by id; theme parts as for Skija |
| `M3GlCanvas` (gpu, GTK and Cocoa) | Skija `DirectContext.makeGL` over SWT's `GLCanvas` context, persistent surface, damage union, last-frame snapshot (skija-canvas S1-S3) | as Skija | as Skija |

Work split (brief rule 5): the UI thread does O(rows in the damage) per frame. Font, text and image caches are
bounded LRUs. Device creation (Direct2D's first hardware target takes about 1.3 s) is started once per display,
off the paint path.

## Look and feel (theme)

`-Dswt.widget.theme=native|swing|initiative31` (global) and `-Dswt.widget.theme.<Widget>=...` select the theme of
the drawn planes at startup, like Swing's `UIManager.setLookAndFeel` (class `org.eclipse.swt.internal.render.RowTheme`).
The planned name in [ALL_WIDGETS_VIEWPORT_PLAN.md](ALL_WIDGETS_VIEWPORT_PLAN.md) section 9, `swt.lookAndFeel`, is
read as an alias. A theme changes pixels only, never the model, the geometry, the API or the events: row height,
indent, label positions and column rectangles are the behaviour plane's in every theme. Priority (lead 2026-10-17):
contract first, performance second, themes third; `native` stays the default.

| Theme | Donor | Selection | Expander | Check box | Other values |
|---|---|---|---|---|---|
| `native` (default) | the platform's uxtheme parts through SWT's own natives (`ThemeParts`) | `Explorer::TreeView` `TVP_TREEITEM` (`ListView` `LVP_LISTITEM` for Table and List), states selected, selected-not-focus, hot | `TVP_GLYPH`/`TVP_HOTGLYPH` chevrons at their part size | `BUTTON` `BP_CHECKBOX` at its part size, as `Tree.setCheckboxImageList` | widget colours and font; grid `COLOR_3DLIGHT`-like E3E3E3; focus rectangle with keyboard cues |
| `swing` | Swing Metal (Ocean); values measured from the JDK by [SwingThemeProbe](render-modes/evidence/swing-theme.output.txt); SWTSwing/prototype-swing key names | `Tree.selectionBackground` B8CFE5 | 9 x 9 handle, border 6382BF, white, minus/plus 333333, `Tree.hash` B8CFE5 stub | 13 x 13 box, border 6382BF, check mark | foreground 333333, `Table.gridColor` 7A8A99, focus A3B8CC |
| `initiative31` | swt-initiative31/prototype-skija `TreeItemRenderer`, `DefaultCheckboxRenderer`, `DefaultColorProvider` (EPL-2.0, adapted in `Initiative31Look` with its notice) | `Table.SELECTION_COLOR` E0EEFE; hover `COLOR_YELLOW` | grey (`COLOR_DARK_GRAY`) triangle, 10 px, right or down | 12 px rounded box, border gray128, filled `hsb (208, 1, 0.72)` = 0062B8 when checked, gray192 when grayed | focus on every selected row of the focus control |

Measured fidelity of `native` against stock pixels is in the R3 results below. `swing` and `initiative31` are
implemented on the same renderer; their visual review follows the contract and performance leaves.

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
  with `-Dswt.widget.mode=custom|m3|gpu` against the stock recording of the official SWT 3.135.0 jar. Every
  non-pixel probe (answer, scroll bar, selection, caret, events, geometry, hit tests) must be equal.
- **Pixels (metric, not a gate):** drawn modes differ by design. The harness reports, per seed, the share of
  pixels equal to the stock capture and the mean absolute channel difference, instead of failing on the hash.
- **Benchmark:** [render-modes/bench](render-modes/bench) (deliverable 2): per widget x mode x operation, wall
  time and allocated bytes, on the invisible desktop, one JVM per mode.

## Benchmark (leaf R2)

Program `RenderModeBenchmark` (test tree, public API only, so it also runs on the official jar), driver
[render-modes/bench/run_bench.sh](render-modes/bench/run_bench.sh), table generator
[render-modes/bench/bench_table.py](render-modes/bench/bench_table.py). One JVM per widget x mode x operation on the
invisible desktop (90 s budget per JVM; a JVM over budget is a `timeout` row). Every widget has 1,000,000 rows: Tree
and Table `SWT.VIRTUAL` with `setItemCount`, List `setItems` (List has no virtual style); the Tree's first root has
10,000 children. Each measured repetition ends with `update ()`, so the paint it causes is included. Cells: median
ms over 10 repetitions after 2 warm-up runs (create: 3 after 1; selectAll: 5), and the median bytes the UI thread
allocated per repetition. `official` is the SWT 3.135.0 jar.

Operations: `create` (new widget + 1,000,000 rows + first paint), `setItemCount` (alternately 999,000 and 1,000,000),
`scrollPage` and `scroll10k` (`setTopItem`/`setTopIndex` by one page and by 10,000 rows), `paint` (`redraw`),
`selectAll`, `expand10k`/`collapse10k` (the 10,000-child root), `selectFar` (`setSelection` of row 900,000 and
`showSelection`).

R2 run, 2026-10-17, build `rm20261017a` (this branch at the integration head), raw rows in
[render-modes/bench/r2-20261017-viewport-original-official.tsv](render-modes/bench/r2-20261017-viewport-original-official.tsv):

| widget | operation | official | original | viewport |
|---|---|---|---|---|
| Tree | create | timeout | 165.261 (528 KB) | 21.358 (101 KB) |
| Tree | setItemCount | timeout | 0.236 (13 KB) | 4.918 (41 KB) |
| Tree | scrollPage | timeout | 26.404 (515 KB) | 10.683 (91 KB) |
| Tree | scroll10k | timeout | timeout | 10.545 (101 KB) |
| Tree | paint | timeout | 8.483 (263 KB) | 2.957 (26 KB) |
| Tree | selectAll | timeout | 18.673 (303 KB) | 2.524 (34 KB) |
| Tree | expand10k | timeout | 10.606 (343 KB) | 6.631 (44 KB) |
| Tree | collapse10k | timeout | 17.444 (272 KB) | 11.837 (49 KB) |
| Tree | selectFar | timeout | timeout | 12.532 (86 KB) |
| Table | create | 34.672 (78 KB) | 46.369 (79 KB) | 32.862 (79 KB) |
| Table | setItemCount | 0.726 (1 KB) | 0.848 (1 KB) | 0.421 (1 KB) |
| Table | scrollPage | 11.574 (51 KB) | 8.958 (49 KB) | 13.797 (43 KB) |
| Table | scroll10k | 7.004 (43 KB) | 4.034 (43 KB) | 5.517 (36 KB) |
| Table | paint | 3.236 (25 KB) | 1.680 (25 KB) | 2.240 (25 KB) |
| Table | selectAll | 2.855 (25 KB) | 2.205 (25 KB) | 6.056 (25 KB) |
| Table | selectFar | 7.278 (27 KB) | 3.673 (27 KB) | 2.554 (27 KB) |
| List | create | timeout | timeout | 52.172 (7820 KB) |
| List | setItemCount | 2554.691 (125 KB) | 2407.163 (125 KB) | 443.855 (136 KB) |
| List | scrollPage | 2.621 (1 KB) | 2.329 (1 KB) | 4.226 (7 KB) |
| List | scroll10k | 2.764 (1 KB) | 2.264 (1 KB) | 9.964 (5 KB) |
| List | paint | 1.222 (1 KB) | 1.103 (1 KB) | 1.136 (1 KB) |
| List | selectAll | 51.523 (1 KB) | 56.404 (1 KB) | 1.766 (2 KB) |
| List | selectFar | 10.433 (1 KB) | 9.148 (1 KB) | 4.801 (6 KB) |

Reading it:

- Stock SWT cannot hold 1,000,000 virtual Tree rows: `setItemCount (1,000,000)` on the official jar does not return
  within 90 s, so every official Tree operation times out in its setup. The fork's `original` Tree keeps a growing
  native prefix: `scroll10k` and `selectFar` time out there (`getItem` far away grows the prefix row by row).
- Stock List cannot create 1,000,000 items four times in 90 s (`create` times out); one creation fits in the setup
  of the other operations.
- Candidates for the viewport owners (reported, not changed here): Tree `setItemCount` costs 4.9 ms in `viewport`
  against 0.24 ms in `original`; Table `scrollPage` 13.8 ms against 9.0 ms and `selectAll` 6.1 ms against 2.2 ms;
  List `scroll10k` 10.0 ms against 2.3 ms and `scrollPage` 4.2 ms against 2.3 ms.

## Tree drawn plane: results (leaves R3, R4)

Build `rm20261017c`: `TreeDrawnPlane` (stage 1) with `GcRowCanvas` (custom) and `M3RowCanvas` (m3, Skija
0.116.3 jars and the backend jar on the class path). Hooks in `Tree.java` come from the recipe
[render-modes/recipe](render-modes/recipe) (`render_modes_recipe.sh`: pass 1 CHANGED, pass 2 UNCHANGED): the
`drawnPlane` field, `TreeDrawnPlane.install` at the end of `createWidget`, one delegation in `callWindowProc`
for `WM_PAINT`/`WM_PRINTCLIENT` of the TreeView, and the release in `releaseWidget`.

Recorded differential `Test_org_eclipse_swt_widgets_Tree_ViewportDifferential` against the official SWT 3.135.0
recording made with this build's tests (both variants, 12 seeds x 150 steps each; probes answer, bar, selection,
caret, events, geometry and hit tests, plus the column probes in the check+columns variant), runner
[render-modes/tools/tree_differential_modes.sh](render-modes/tools/tree_differential_modes.sh), hidden desktop:

| Mode | Plain variant | Check + columns variant | Pixels equal to stock (mean of 12 seeds) |
|---|---|---|---|
| `custom` | pass | pass | plain 99.52 %, columns 85.57 % |
| `m3` | pass | pass | plain 94.08 %, columns 83.33 % |
| `viewport` (same recording, for reference) | pixels of seed 0 differ from stock (owner's leaf; reported) | pass | strict gate |

Per-seed similarity: [render-modes/evidence/tree-similarity-rm20261017c.tsv](render-modes/evidence/tree-similarity-rm20261017c.tsv).
The columns variant differs mostly in the full-row selection over the sort column (the sort colour shows through
the selection part) and in the text inset of columns after the first; both are theme work, after the contract and
performance leaves.

Benchmark of the Tree in the drawn modes (same program as R2, build `rm20261017c`, raw rows in
[render-modes/bench/r3-20261017-tree-viewport-custom-skija.tsv](render-modes/bench/r3-20261017-tree-viewport-custom-skija.tsv)):

| widget | operation | viewport | custom | m3 |
|---|---|---|---|---|
| Tree | create | 23.055 (100 KB) | 28.297 (107 KB) | 28.792 (124 KB) |
| Tree | setItemCount | 4.452 (41 KB) | 5.931 (45 KB) | 5.168 (50 KB) |
| Tree | scrollPage | 5.750 (90 KB) | 13.386 (95 KB) | 10.793 (104 KB) |
| Tree | scroll10k | 6.990 (100 KB) | 14.565 (105 KB) | 11.681 (116 KB) |
| Tree | paint | 3.940 (26 KB) | 5.957 (30 KB) | 3.882 (35 KB) |
| Tree | selectAll | 4.010 (34 KB) | 5.168 (38 KB) | 5.175 (45 KB) |
| Tree | expand10k | 6.473 (45 KB) | 6.995 (49 KB) | 7.648 (54 KB) |
| Tree | collapse10k | 17.353 (48 KB) | 20.105 (53 KB) | 15.243 (58 KB) |
| Tree | selectFar | 8.984 (86 KB) | 10.615 (90 KB) | 10.215 (97 KB) |

Stage 1 pays for two passes per frame (the behaviour plane's paint into the back buffer, then the drawn rows):
`paint` costs 6.0 ms in `custom` against 3.9 ms in `viewport`; `m3` is at 3.9 ms because its text lines are
shaped once and cached. The optimization leaves (R7) start with the behaviour pass.

## Leaves

| Leaf | Scope | Gate |
|---|---|---|
| R1 | this design and the donor inventory | review |
| R2 | mode benchmark, viewport and original first | TSV under `render-modes/bench` |
| R3 | drawing core + `GcCanvas` + Tree drawn plane (stage 1) | Tree differentials (both variants) in `custom`: all non-pixel probes equal to stock |
| R4 | `M3Canvas` | the same in `m3` |
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
| R2 | benchmark program and driver; viewport, original and official measured (table above) |
| R3 | drawing core, GC backend, Tree drawn plane (stage 1): Tree differentials pass in `custom` |
| R4 | M3 backend: Tree differentials pass in `m3` |
| R5-R7 | not started in this commit (gpu falls back to m3, then custom, with an info log) |

Non-claims: Table and List have no drawn plane yet; the gpu mode draws with the m3 backend until R5; the owner-draw events (MeasureItem, EraseItem, PaintItem) are still emitted by the behaviour plane's pass, so an application's owner drawing is painted over in the drawn modes (stage 2 moves them to the renderer); rows answered by a page source without a facade draw no text yet; GTK and Cocoa compile (`compile_platform.sh`: 0 errors) but are not run on this host.
