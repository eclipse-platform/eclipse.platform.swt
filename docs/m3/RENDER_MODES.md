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
  ListBox holds the resident window and paints it. In a drawn mode a drawn surface (GC, the M3 renderer on the CPU, or the M3
  renderer on the graphics hardware) paints exactly the resident window.
- One planner drives every plane, with the phases and names of `TreeViewport`: capture, plan, retire/insert,
  commit, bind, restore focus. A plane only adds a paint of the committed window.
- No per-mode model copies and no O(n) work per frame: a frame paints the rows that intersect the damage, found
  by arithmetic over the resident window.

## Modes and how one is selected

| `-Dswt.widget.mode` | Plane | Pixels |
|---|---|---|
| `viewport` (default) | native control holding the resident window | the platform's |
| `original` | stock SWT, no mediation | the platform's |
| `custom` | drawn plane, backend `GC` | SWT `GC` (GDI) |
| `m3` | drawn plane, the M3 renderer on the CPU | M3 raster into the plane's DIB section (through the Skija library) |
| `gpu` | drawn plane, the M3 renderer on the graphics hardware | M3 over OpenGL (Skija's `DirectContext.makeGL` on SWT's WGL natives), read back into the DIB |

`-Dswt.widget.mode.Tree=custom` selects a mode for one widget class, as for every widget. Naming (brief rule 14):
nothing of ours is called Skija or Skia; the mode is `m3` (`WidgetMode.M3`) and our classes are `M3*`
(`M3RowCanvas`, `M3Gpu` in `org.eclipse.swt.internal.render.m3`, source folder `Eclipse SWT Render M3`). The renames
were made by recipe ([render-modes/recipe/render_names_recipe.sh](render-modes/recipe/render_names_recipe.sh):
OpenRewrite `ChangeType`, `ChangePackage`, `ChangeMethodName`; pass 1 CHANGED, pass 2 UNCHANGED). The library's own
binding classes (`io.github.humbleui.skija`, `io.github.humbleui.types`) keep their package because its prebuilt
natives bind to it; the package `org.eclipse.swt.internal.render.m3` is the one adapter that touches them, loaded by
name, so SWT runs without the library when no drawn mode needs it.

Selection, in code:

- The mediator keeps its own query, `WidgetMode.of ("Tree", VIEWPORT) == VIEWPORT`. A drawn mode requested for
  Tree is not in that list, so `WidgetMode` falls back to `VIEWPORT` there and the mediator stays on. Nothing in
  the mediator changes.
- The plane asks `WidgetMode.of ("Tree", VIEWPORT, CUSTOM, M3, GPU)` once, when its class initializes, and installs
  a drawn plane for `CUSTOM`, `M3` or `GPU`. A backend that cannot start (no library jars on the class path, no
  OpenGL context) logs at `info` and falls back: `gpu` to `m3` to `custom`.
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
                                        |                              GcRowCanvas     custom
                                   Theme defaults (packed ints)        M3RowCanvas     m3   (CPU raster)
                                                                       M3RowCanvas     gpu  (OpenGL, M3Gpu)
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
| `GcRowCanvas` (custom) | paints into the plane's back buffer DC (a DIB section kept per widget) | GDI `DrawText` with the native font | `GC.drawImage`; theme parts through uxtheme `DrawThemeBackground` |
| `M3RowCanvas` (m3) | `Surface.makeRasterDirect` over the same DIB section, resized only on size change; damage `BitBlt`-ed | a library font per native font (LOGFONT face, pixel height, weight, italic); shaped `TextLine` LRU keyed by (font, string) | SWT image pixels converted once, LRU; theme parts rendered once by uxtheme on black and white (alpha recovered) into an image cache |
| `M3RowCanvas` (gpu) | an offscreen GPU render target per widget (`Surface.makeRenderTarget` on the thread's OpenGL context, `M3Gpu`); after the frame, `flush` and `readPixels` of the damage straight into the DIB | as m3 (glyph atlas on the GPU) | as m3 (uploaded by the library) |

Work split (brief rule 5): the UI thread does O(rows in the damage) per frame. Font, text and image caches are
bounded LRUs. The OpenGL context is one per UI thread (a hidden, disabled, never-shown popup window, not a child of
any widget), created by the first gpu plane and freed with the last one.

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

## GPU route (Java only, user decisions 2026-10-17)

The user first chose Direct2D on win32 and OpenGL elsewhere, then ruled that every new line is Java (brief rule 13):
no C, C++ or JNI of our own and no native builds. A minimal JNI shim for Direct2D had been committed in R5
(`403c384e5f`, `Eclipse SWT Render Direct2D/win32/library/swt_m3_d2d.cpp`, built with mingw-w64); it is removed in the
following commit, with its Java binding (`Direct2D`, `Direct2DRowCanvas`) and its build script. The Java-only routes,
in the user's order, were probed on this host (Windows 11, JDK 21, invisible test desktop; 300 x 380 pixels, a frame =
clear + one filled row + 20 text rows; sources and output in [render-modes/evidence](render-modes/evidence),
`gpu-route-java-only.output.txt`):

| Route | Pixels | First setup | Per frame |
|---|---|---|---|
| 1. the M3 renderer on the GPU: Skija's `DirectContext.makeGL` on an OpenGL context made through SWT's own WGL natives (hidden popup), offscreen render target, `readPixels` of every frame | correct (fill, text) | 833 ms (includes loading the library's natives) | **0.72 ms** including the read back |
| 2. Direct2D through the JDK FFM API (`java.lang.foreign`, preview in JDK 21: `--enable-preview --enable-native-access=ALL-UNNAMED`), DC render target, COM methods through their vtable slots, hardware | correct (fill, DirectWrite text) | 342 ms | 1.22 ms |
| 2, software target | correct | 6 ms | 0.57 ms |

Decision:

- **gpu = route 1 on every platform**: it needs no preview flag on JDK 21, its natives come with the library, and the
  same code serves GTK and Cocoa (their runs: NOT_RUN on this host). A swapBuffers-based variant measured 21.8 ms per
  frame earlier (vsync); the offscreen target with read back avoids it.
- **Route 2 works** and is kept as evidence: Direct2D can be driven from Java once SWT may use the FFM API (final in
  JDK 22; preview with M3jdk21). SWT's `COM.VtblCall` alone cannot do it: it passes the object as the first argument
  (no `D2D1CreateFactory`) and passes no float arguments in registers.
- Dawn (WebGPU) and Skia's C++ are pattern donors only (rule 13).

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

The `m3` mode was named `skija` when these runs were made; the rows say `m3`.

Build `rm20261017c`: `TreeDrawnPlane` (stage 1) with `GcRowCanvas` (custom) and `M3RowCanvas` (m3, then `SkijaRowCanvas`; library
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
[render-modes/bench/r3-20261017-tree-viewport-custom-m3.tsv](render-modes/bench/r3-20261017-tree-viewport-custom-m3.tsv)):

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

## Tree drawn plane in the gpu mode (leaf R5) and the shared plane

`DrawnPlane` now holds the stage-1 frame for every row widget (behaviour pass, drawn pass, present);
`TreeDrawnPlane` only reads the Tree's rows. Build `rm20261017f` (before that refactor) with the Direct2D shim, since removed (rule 13):

| Mode | Plain variant | Check + columns variant | Pixels equal to stock (mean of 12 seeds) |
|---|---|---|---|
| `custom` | pass | pass | plain 99.52 %, columns 85.57 % |
| `m3` | pass | pass | plain 94.09 %, columns 83.37 % |
| `gpu` (the removed Direct2D shim, hardware) | pass | pass | plain 95.22 %, columns 84.49 % |

Per-seed similarity: [render-modes/evidence/tree-similarity-rm20261017f.tsv](render-modes/evidence/tree-similarity-rm20261017f.tsv).

Benchmark, Tree, four modes, 20 repetitions (min / median ms), build `rm20261017f`, raw rows in
[render-modes/bench/r5-20261017-tree-four-modes.tsv](render-modes/bench/r5-20261017-tree-four-modes.tsv). The host ran
out of commit memory during this run (memory gate at 0 GB, `fork` failures): the `exit` cells are JVMs that could not
start, not widget failures, and the medians are noisy; the minimums are the least disturbed numbers.

| widget | operation | viewport | custom | m3 | gpu |
|---|---|---|---|---|---|
| Tree | create | 24.672 / 32.449 (100 KB) | exit 1 | 19.460 / 19.995 (123 KB) | 201.491 / 212.976 (117 KB) |
| Tree | setItemCount | 4.315 / 5.292 (41 KB) | 3.757 / 9.663 (45 KB) | 2.957 / 4.336 (50 KB) | 4.958 / 6.925 (45 KB) |
| Tree | scrollPage | 7.060 / 10.354 (92 KB) | 8.142 / 10.480 (95 KB) | 7.654 / 9.244 (107 KB) | 9.347 / 11.066 (96 KB) |
| Tree | scroll10k | 8.677 / 12.099 (101 KB) | 12.759 / 22.004 (106 KB) | 8.256 / 10.565 (117 KB) | 9.271 / 11.553 (105 KB) |
| Tree | paint | 2.538 / 4.198 (26 KB) | 1.855 / 3.908 (30 KB) | 2.772 / 3.449 (35 KB) | 3.731 / 5.771 (30 KB) |
| Tree | selectAll | 5.404 / 17.770 (34 KB) | 3.543 / 5.800 (38 KB) | 4.441 / 5.192 (45 KB) | 5.291 / 6.309 (39 KB) |
| Tree | expand10k | 3.310 / 5.431 (44 KB) | 5.831 / 10.562 (49 KB) | 4.732 / 5.529 (54 KB) | 6.294 / 7.050 (49 KB) |
| Tree | collapse10k | exit 1 | 14.211 / 17.713 (53 KB) | 10.868 / 15.593 (57 KB) | 13.872 / 15.725 (53 KB) |
| Tree | selectFar | exit 127 | 7.616 / 9.397 (91 KB) | 9.057 / 10.324 (97 KB) | 11.225 / 12.147 (92 KB) |

- `gpu` `create` cost about 200 ms per Tree with the shim: each Tree made its own Direct2D context. The OpenGL route
  shares one context per UI thread (`M3Gpu`).
- Stage-1 `paint` minimums: viewport 2.5 ms, custom 1.9 ms, m3 2.8 ms, gpu (shim) 3.7 ms.

## Leaves

| Leaf | Scope | Gate |
|---|---|---|
| R1 | this design and the donor inventory | review |
| R2 | mode benchmark, viewport and original first | TSV under `render-modes/bench` |
| R3 | drawing core + `GcRowCanvas` + Tree drawn plane (stage 1) | Tree differentials (both variants) in `custom`: all non-pixel probes equal to stock |
| R4 | `M3RowCanvas` raster | the same in `m3` |
| R5 | gpu backend: Java only, `M3RowCanvas` over OpenGL (`M3Gpu`) | the same in `gpu` |
| R6 | Table, then List, on the same core (`DrawnPlane`, `TableDrawnPlane`, `ListDrawnPlane`) | Table and List differentials in the three drawn modes |
| R7.. | optimization passes driven by the benchmark (before/after numbers each); data-oriented paint (rule 18) | benchmark + differentials |
| T1.. | themes as first-class objects (rule 16): theme API, runtime selection, user-defined themes, theme differential | theme differential + JaCoCo |
| C1 | JUnit coverage of the render code, at least 99 % lines and branches (rule 15) | JaCoCo report under `render-modes/coverage` |
| S2.x | stage 2: behaviour duties move from the native control to the model | the same differentials |

## Donors

[render-modes/FOSS_REUSE_DECISION.tsv](render-modes/FOSS_REUSE_DECISION.tsv). In short: the viewport model and
planner, `WidgetMode` and the retained-planes `OpStream` are reused from this repository; Windows theme parts
through SWT's existing natives; the Skija library (Apache-2.0, with its own natives) under the M3 renderer for the
m3 and gpu planes; skija-canvas's text LRU and GL surface are adapted; Initiative 31's custom widget classes are not used (a second
model per widget), its owner-draw order and paint cull are patterns; the SWT-on-Swing projects (SWTSwing,
Initiative 31 prototype-swing) are patterns for the row renderer, the Swing theme keys and later the input mapping;
SwingWT, DJ Native Swing, SWT_AWT and Albireo embed rather than draw (no fit).

## Status

| Leaf | State |
|---|---|
| R1 | this document |
| R2 | benchmark program and driver; viewport, original and official measured (table above) |
| R3 | drawing core, GC backend, Tree drawn plane (stage 1): Tree differentials pass in `custom` |
| R4 | M3 raster backend: Tree differentials pass in `m3` (then named `skija`) |
| R5 | gpu: first a Direct2D JNI shim (passed the Tree differentials), removed under rule 13; now M3 over OpenGL, Java only; `DrawnPlane` shared by the row widgets |
| R6 | Table and List drawn planes written; verification runs in progress |
| R7, T1, C1 | not started |

Non-claims: the owner-draw events (MeasureItem, EraseItem, PaintItem) are still emitted by the behaviour plane's pass, so an application's owner drawing is painted over in the drawn modes (stage 2 moves them to the renderer); rows answered by a page source without a facade draw no text yet; GTK and Cocoa compile (`compile_platform.sh`: 0 errors) but are not run on this host.
