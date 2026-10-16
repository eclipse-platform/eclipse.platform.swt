# Every SWT and Nebula widget as a viewport: plan (2026-10-16)

Status: plan and audit. The first leaf (scrollable-Composite geometry) is designed in
[`SCROLL_MEDIATED_VIEWPORT.md`](SCROLL_MEDIATED_VIEWPORT.md) and implemented on this branch.

## 1. Goal

Every SWT widget (win32 first, then GTK and Cocoa) and every Nebula widget behaves as a viewport over a logical model:
it keeps only what is on screen (plus overscan) as native or painted state, it scrolls through logical metrics that do not
overflow, and it repaints only damaged pixels. The public API contract and the visible behaviour do not change.

"Viewport" means four things for a widget. Each widget below says which of them apply.

| Aspect | Meaning | Shared code (common unless noted) |
|---|---|---|
| **R** resident window | only the items, rows, children, lines or tiles on screen (plus overscan) exist natively; the rest are lanes (bits, packed ints) and facades | `VirtualViewportPlanner`, `VirtualTreeVisibleProjection.Residency`, `VirtualListModel`, `VirtualRowExtents`, `org.eclipse.swt.internal.residency` (`ItemResidency`, `ItemLanes`, `ItemBitSet`) |
| **G** logical geometry | positions and extents are logical (`long` where needed); native coordinates stay inside the native range; scroll bars are projections | `VirtualScrollMetrics`, `ViewportRuntime` (`solvePixels`, `solveRows`), `ViewportScrollLayout`, `ViewportAffineTransform`, `ViewportPlaneGeometry` (new, leaf L1) |
| **D** damage-only repaint | a scroll is a blit plus the exposed strip; an item change repaints its rectangle; offscreen work is deferred | `ViewportPaintGraph`, `ViewportLayerState`, `ViewportDamageRegion`, `ViewportDamageRecorder`; win32 `ViewportBatch`, `ViewportBox`, `ViewportDamageHook`, `EffectiveVisibility`, `RetainedFrame` |
| **P** retained planes | content, header, editor and feedback layers that move independently (a scroll moves only the body plane) | `ViewportRuntime.BODY/HEADER/EDITOR/FEEDBACK/SCROLLBAR`, `ViewportLayerState`, win32 `ViewportBatch` |

## 2. One core, four modes: the widget generator

This SWT is a generator of widgets. A widget is the product of three choices, and only the first is widget-specific:

```
widget = shape (what the logical model is)  x  viewport core (shared phases)  x  mode (how it is realized)
```

**Shapes.** Every widget model is one of six shapes. A flat list is a tree of depth 1; a table is a list with columns;
a scrolled composite is a list of children positioned by geometry instead of by index.

| Shape | Logical model | Widgets (SWT, Nebula) | Core classes reused |
|---|---|---|---|
| ROWS | ordered rows, uniform or variable height, optional depth and columns | Tree, Table, List, Combo list, CCombo; Grid, XViewer, CTree, CTreeCombo, TableCombo, CompositeTable rows, PShelf items, PropertyTable | `VirtualTreeTopology`, `VirtualTreeVisibleProjection`, `VirtualListModel`, `VirtualRowExtents`, `VirtualSelectionModel`, `VirtualScrollMetrics` |
| CHILDREN | child controls placed by geometry | Composite, ScrolledComposite, Canvas with children, Group, ExpandBar items, CTabFolder/TabFolder pages, SashForm, PGroup, CompositeTable | `ViewportPlaneGeometry` (L1), `EffectiveVisibility`, `ViewportBox` |
| LINES | text lines with variable extents | StyledText, Text (multi), Link (wrapped), RichText (browser) | StyledText `PagedContent`, `ViewportRuntime.RowWindow`, `VirtualRowExtents` |
| TILES | 2-D grid of items or tiles | Gallery, Tiles, GeoMap, PaperClips preview pages, TreeMapper | `VirtualRowExtents` per axis, `VirtualScrollMetrics` per axis |
| AXIS | continuous axis (time, value) with decimation | GanttChart, Timeline, Oscilloscope, Visualization XYGraph, Calendar/DateChooser months | `ViewportAffineTransform`, packed decimation (Nebula #152) |
| FIXED | small control, no scrolling | Button, Label, CLabel, Sash, ProgressBar, Scale, Slider, Spinner, DateTime, Link (short), Menu, ToolTip; Nebula LED, badges, switches | damage only (`ViewportBox`, `EffectiveVisibility`) |

**Shared phases (rule 9).** Every mediator is shaped like `TreeViewport` (PR #196) and uses the same method names and the
same six phases per rebuild:

1. **capture**: read the logical top, focus and selection anchors (lanes, never native objects);
2. **plan**: diff the old resident window against the new one (`VirtualViewportPlanner`); touch only changed rows/children; no per-row allocation;
3. **retire / insert**: native deletes and inserts for the diff, under one redraw suspension (`ViewportBatch`);
4. **commit**: native positions (plane offset), logical scroll bar (`VirtualScrollMetrics`), damage rectangles;
5. **bind**: facades get their native handles; `residentHandle` validates a handle before every use (native handles are reused);
6. **restore focus**: selection-neutral caret restore.

**Shared tests.** Every widget gets the same three gates, generated from one table of operations:

- acceptance test `Test_org_eclipse_swt_widgets_<W>_Viewport`: the viewport invariants; skips (JUnit assumption) when the viewport is inactive;
- **recorded differential** `Test_org_eclipse_swt_widgets_<W>_ViewportDifferential`: seeded scripts of public API operations against a stock
  widget and a viewport widget; each step records `answer | bars | origin/top | selection | focus | events` and the pixels per seed;
  any difference fails with the operation log (the log is the reproduction). For geometry whose stock behaviour is broken by native
  limits (L1), the differential is **shifted**: the same script runs at an in-range origin and at a far origin, and answers must be
  equal after subtracting the shift, pixels identical;
- aspect matrix (`docs/m3/<widget>-viewport/aspect_matrix.py`): every public member and behaviour aspect mapped to its test.

**Recipe-generated hooks (rule 3).** A widget's hooks into existing SWT files are one table (`gen_<widget>_viewport_yml.py`) of
`(method, parameterCount, position, anchor, statement)` rows turned into an OpenRewrite `M3InsertStatements` YAML; mediators and
tests are new files. Applying the recipe twice is a fixed point. The same table drives the aspect matrix.

**Modes (rule 4).** `org.eclipse.swt.internal.WidgetMode`, one SWT-global startup switch: `-Dswt.widget.mode=viewport|original|custom|skija`
and `-Dswt.widget.mode.<Widget>=...`. Nebula reads the same property names through its own copy.

| Mode | Realization | Source of the widget |
|---|---|---|
| `viewport` (default) | native control holds the resident window; native pixels | this plan's leaves |
| `original` | stock SWT | upstream code, untouched |
| `custom` | SWT draws with `GC` | Initiative 31 `prototype-skija` / `custom-drawn-controls` classes (section 6), on the same viewport core |
| `skija` | SWT draws through Skija | the same I31 classes with a Skija GC on the `skija-canvas` pattern (S1-S6) |

A widget without a `custom`/`skija` realization falls back to `viewport`, and without a viewport to `original`.

## 3. Measured native rules (oracle first)

Oracle: [`scroll-viewport/evidence/ScrollGeometryOracle.java.txt`](scroll-viewport/evidence/ScrollGeometryOracle.java.txt), win32, Windows 11,
JDK 21, master `ef565e23ce` jar with `-Dswt.widget.mode=original`. Results in `scroll-viewport/evidence/stock-100.tsv` and `stock-200.tsv`.

| Rule (stock win32) | 100 % | 200 % (`-Dswt.autoScale=200`) |
|---|---|---|
| A child window's native position is clamped to the signed 16-bit range [-32768, 32767] pixels | content location stops at y = -32768; a child set at y = 32970 reports y = 32767 | the same pixel limits; at 200 % that is 16384 points |
| A child window's native size is clamped to 65535 pixels | `content.setSize (300, 70000)` then `getSize ()` answers 65535 | `setSize (300, 33000)` answers 32768 points |
| ScrolledComposite past the limit: the scroll bar and the origin disagree, and the end of the content is unreachable | h = 40000: bar selection 39703, `getOrigin ()` 32768, bottom child not painted (`WHITE` where `BLUE` is expected) | h = 20000 points (40000 px): bar 19681, origin 16384, bottom child not painted |
| Inside the limit, a scroll is a blit: the content gets a Paint event only for the exposed strip | step 10: one paint `300x10`; step 100: `300x100`; a jump beyond the client height: one full paint | the same (in points) |
| Native scroll messages on a ScrolledComposite | `SB_LINEDOWN` moves 15 px (bar increment), `SB_PAGEDOWN` one page; events: content `Move`, then bar `Selection` with detail `ARROW_DOWN`, `PAGE_DOWN`, `END`, `ARROW_UP`, `HOME`; wheel: 3 lines, detail `0` | wheel: 24 px; past the clamp, `SB_LINEUP` from the end sends `Selection` but no `Move` |
| `Canvas` with `V_SCROLL`: the bar accepts `Integer.MAX_VALUE`; `Canvas.scroll` blits and paints the exposed strip | `scroll 10`: one paint `387x10` | `404x10` |

These rules make **L1** the top generic leaf: every scrollable composite on win32 (and GTK3/X11, whose window protocol also uses 16-bit
coordinates; not measured on this host) silently breaks past 32767 pixels, which is only 16384 points at 200 % scale.

## 4. Audit: SWT widgets (win32 first; GTK and Cocoa notes)

Columns: what viewport means (aspects R/G/D/P), what native path must be mediated, what to measure first, expected win, priority (1 = first).

| Widget | Viewport meaning | Native path to mediate (win32) | Oracle first | Expected win | GTK / Cocoa | Prio |
|---|---|---|---|---|---|---|
| Composite (H/V_SCROLL), Canvas | G+D: logical child geometry beyond 16 bits; exposed-strip repaint | child `SetWindowPos` (16-bit clamp), `WM_PAINT` rect and DC origin, mouse `lParam`, `MapWindowPoints` | done (section 3) | content of any size reachable; contract of `getBounds` kept past 32767 px | GTK3/X11 `GdkWindow` 16-bit; GTK4/Wayland and Cocoa use client-side/float coordinates (identity) | 1 |
| ScrolledComposite | G+D+P: the content is a plane whose native window is a band around the origin | none in the custom class; the content's native bounds (L1) | done | stock is broken past 32767 px (bar and origin disagree) | same as Composite | 1 |
| Tree | R+G+D+P | TreeView resident window (PR #196) | done (#196) | 1 M rows, O(window) | GTK bounded view exists | other agent |
| Table, List, Combo | R+G+D | `LVS_OWNERDATA`, `LBS_NODATA` (#129) | per agent | as Tree | GTK logical Table (#114) | other agents |
| StyledText | R(lines)+G+D+P | none (drawn); `PagedContent`, line window, blit scroll (#115, #118, #120, #123) | `docs/m3/STYLEDTEXT_*.md` | 100 M+ characters | drawn on all platforms | 2 (wire to `WidgetMode`) |
| Text (multi) | none in `viewport` mode (EDIT virtualizes its own lines); D only | EDIT control | measure `setText` and line count at 10^6 lines | limited; `custom`/`skija` use I31 `Text`/`TextModel` | native | 6 |
| ExpandBar | G+D+P: items are headers plus child planes; logical scroll | SWT-drawn on win32 (`ScrollWindowEx` already at `ExpandBar.java:697,951`) | item count vs paint and layout time | many items; child planes via L1 | GTK `GtkExpander` native; Cocoa drawn | 4 |
| CTabFolder | R(tabs)+D: visible tab window via prefix sums of tab widths, chevron arithmetic; per-tab damage; lazy page content | none (drawn) | 10^4 tabs: layout and paint time | O(visible tabs) layout and paint | drawn everywhere | 3 |
| TabFolder | lazy page content (`SWT.VIRTUAL`, merged #195); R for tab items not needed | `TCM_INSERTITEM` | not needed | done | done (#195) | done |
| ToolBar, CoolBar | D only; item counts are small | `TB_*`, `REBAR` | none | small | native | 7 |
| Group, Label, CLabel, Link, Button | D only (`viewport` = native); `custom`/`skija` from I31 | none | none | none in `viewport` mode | native | 8 |
| Sash, ProgressBar, Scale, Slider, Spinner | D only; Slider/Scale/Spinner keep int ranges (the contract is int) | none | none | none in `viewport` mode; I31 renderers for `custom`/`skija` | native | 8 |
| DateTime | FIXED | DTP / MonthCal | none | none | native | 9 |
| Menu | FIXED; items stay native (the menu scrolls itself); lazy items already via `SWT.Show` | HMENU | none | none | native | 9 |
| Browser | its own viewport (WebView2/Edge, WebKit) | none | none | none | none | n/a |
| Shell, Decorations, Caret, IME, Tracker, ToolTip, Tray | n/a (no logical content) | none | none | none | none | n/a |

## 5. Audit: Nebula widgets (read-only source `K:/m3-work/superset/nebula/widgets`)

Inventory basis: `NEBULA_VIEWPORT_COVERAGE.tsv` and `WIDGET_IMPLEMENTATION_LEDGER.tsv` of the Synexia run
`docs/M3-SCALE/runs/20261013-all-widget-viewport` and the hsoliwal/nebula PR history. Nebula widgets are drawn (Canvas or Composite),
so their viewport is always SWT-side code: no native mediation, only resident model, logical geometry and damage.

| Widget | Shape | Viewport meaning | Expected win | Prior PRs (hsoliwal/nebula) | Prio |
|---|---|---|---|---|---|
| Grid | ROWS | resident rows and columns, logical bars, row-rect damage | 10^6 rows | #10-#232 (53 Grid PRs, 50 merged; #151 packed model, #148 row spans, #140 million-row snapshot) | other agent |
| XViewer, CTree, CTreeCombo, TableCombo, CalendarCombo, CompositeTable | ROWS (+CHILDREN for CompositeTable) | delegate to the SWT Tree/Table viewport; CompositeTable row controls as a resident window of child planes (L1 + R) | large row counts without one control per row | #150 (tree/table family onto the viewport layer), #154, #155 | 3 |
| Gallery | TILES | resident item window over group rows, two-axis metrics, tile damage | 10^5+ images | #175, #176 (#177, #184 closed) | 3 |
| PShelf | ROWS | items and the open item's body plane | small | #190, #193 | 7 |
| GanttChart, Timeline | AXIS | time-axis plane, blit scroll, event window by interval search (sorted int arrays) | long schedules | (none) | 4 |
| Visualization (XYGraph, IntensityGraph), Oscilloscope | AXIS | packed decimation window | done | #152 | done |
| GeoMap, Tiles | TILES | tile window and tile cache, blit pan | smooth pan | (none) | 5 |
| PaperClips print preview | TILES (pages) | page window | large documents | (none) | 6 |
| CDateTime, DateChooser | FIXED / small AXIS | damage only | small | #30-#52 (CSS, owner recipes) | 8 |
| RichText | LINES (browser) | the browser is its own viewport | none | (none) | n/a |
| CWT (VPanel), PGroup, Opal widgets, Chips, Carousel, SegmentedBar, Stepbar, SplitButton, LED, badges, switches, FontAwesome, Pagination, FormattedText, PasswordRevealer, ProgressCircle, NebulaSlider, NebulaToolbar, RadioGroup, RoundedCheckbox, TreeMapper, Effects | FIXED (TreeMapper: TILES) | damage only; inside a ScrolledComposite they inherit L1 | small | #50, #202, #210, #215, #218, #225, #228 | 9 |

## 6. Donors

All decisions are recorded in [`scroll-viewport/FOSS_REUSE_DECISION.tsv`](scroll-viewport/FOSS_REUSE_DECISION.tsv) (L1) and, per widget, in
`docs/m3/<widget>-viewport/FOSS_REUSE_DECISION.tsv`.

**This repository first.** `ViewportRuntime`, `VirtualScrollMetrics`, `ViewportScrollLayout`, the residency engine, `ViewportBatch`,
`ViewportBox`, `ViewportDamageHook`, `EffectiveVisibility` and `RetainedFrame` are reused directly (EPL-2.0, same repository).
ScrolledComposite already has a logical-content mode for paint-driven content (`setLogicalContentSize`, #106, #108); L1 is the
complement for ordinary child controls and needs no new API.

**Eclipse GEF Draw2d** (eclipse-gef/gef-classic `4052c50a1ed0`, EPL-2.0, `org.eclipse.draw2d/src/org/eclipse/draw2d/`):

| Draw2d class (blob) | Mechanism | Used for |
|---|---|---|
| `Viewport` (d5369b236c49) | the view location is an offset applied by `translateToParent`/`translateFromParent`; `readjustScrollBars` sets each `RangeModel` to `(0, clientExtent, contentsExtent)` | the plane offset of L1 (`ViewportPlaneGeometry`): logical coordinates on the API, an offset at the native boundary |
| `FreeformViewport` (0a08b23cf4fc) | contents may extend to negative coordinates; the range model's minimum follows the extent | plane base for content left of or above the origin |
| `RangeModel`, `DefaultRangeModel` (71c7f84dc677, c4611c7f266a) | `setValue` clamps to `[min, max - extent]`; `setAll (min, extent, max)` is one atomic update with one notification | custom-scrollbar model (leaf L3) for `custom`/`skija` modes; same clamp as `VirtualScrollMetrics.clampTopRow` |
| `ScrollPaneSolver` (677612fc8e6f) | scroll bar visibility as a fixed point (showing one bar may require the other) | already in `ViewportRuntime.solvePixels` (#31) |
| `DeferredUpdateManager` (b8f6be30762b) | dirty regions per figure are unioned and repainted once, asynchronously | damage coalescing (`ViewportBatch`, `ViewportDamageRegion`) |
| `ViewportLayout` (255b89f35cd5) | contents sized to the larger of preferred size and client area | ScrolledComposite `expandHorizontal`/`expandVertical` (already equivalent) |

Draw2d is a pattern donor: its figures are not SWT controls and it has no native coordinate limit, so no source is copied.

**Initiative 31 and Skija** (`C:/m3-superset/tools/I31_SKIJA_TRICKS.md`). Per widget, for the `custom` and `skija` modes (the `viewport`
mode keeps native pixels):

| Widget | I31 `custom` realization (prototype-skija `146eee2a`, EPL-2.0) | Tricks reused by the viewport core |
|---|---|---|
| Button, Label, Link, Scale, Sash, ProgressBar, Slider | `CustomControl` + `Default<W>Renderer` | T9 owner-draw order; S1-S6 for `skija` |
| CoolBar, Group, TabFolder, ToolBar, ExpandBar | `CustomComposite` + renderer + items handler | T2 paint cull; T5 item-rect damage |
| Table, Tree, List | `TableItemsHandler`, `TreeItemsHandler`, `DefaultListRenderer`; CDC `ListLikeModel` | T1 visible range, T3 SetData once, T7 scroll bar model, T8 anchor/current keyboard, T10 stripe parity |
| Text, Combo, Caret | `NativeBasedCustomScrollable`, `TextModel`, `CCombo` | T7 |
| Canvas (any client-drawn) | skija-canvas `ExternalCanvasHandler` + `SkiaGlCanvasExtension` | S1 persistent surface, S2 damage union, S3 last-frame reuse, S4 text-image LRU |
| scrollable composites (L1) | none (I31 scrolls by `setTopIndex` + full `redraw ()`, rejected) | T13 `Canvas.scroll` = `ScrollWindowEx` blit (upstream SWT) |

Rejected: I31 scroll = full redraw; `SkijaGC` per-paint PNG commit; GPL-3.0 `prototype-skiavcl-libreoffice`.

## 7. Ranked leaf table

Size: S (< 400 lines), M (400-1200), L (> 1200). "Depends" lists leaves that must merge first.

| Rank | Widget(s) | Leaf | Mechanism | Oracle needed | Tests | Size | Depends on | Prior PRs |
|---|---|---|---|---|---|---|---|---|
| 0 | all | V0 `WidgetMode` switch | `swt.widget.mode[.<W>]`, byte-identical class on every branch | none | `WidgetMode` parsing | S | - | #196 (8a05de9757) |
| 1 | Composite, ScrolledComposite, Canvas (and every CHILDREN widget, every Nebula widget inside a ScrolledComposite) | **L1 plane geometry** | a control whose logical bounds leave the native 16-bit range becomes a plane: native position = logical - band base; children mapped by the base; getters, mapping, mouse and paint translated; Move/Resize events kept | done: `ScrollGeometryOracle` | `ViewportPlaneGeometry` unit; `Test_..._ScrolledComposite_Viewport` acceptance; shifted differential (answer, bars, origin, events, pixels) | M | V0 | #52, #106, #108, #124, #127, #128 |
| 2 | all scrollable widgets | L2 shared differential harness | the Tree differential's recorder and script runner as one test base class (`ViewportDifferentialHarness`) used by Tree, Table, List, ScrolledComposite, ExpandBar, CTabFolder | none | the harness's own self-test | S | #196 | #196 |
| 3 | all scrollable widgets in `custom`/`skija` modes | L3 custom scroll bar geometry | `ViewportScrollBarGeometry`: arrows, track, thumb rectangles and hit codes from `VirtualScrollMetrics` (Draw2d `RangeModel` clamp, CDC T7) | measure native thumb sizes (`SM_CYVTHUMB`, `SBM_GETSCROLLBARINFO`) | unit + pixel comparison against native bar in `custom` mode | S | L1 | #31, #35 |
| 4 | StyledText | L4 wire the StyledText viewport (#115, #118, #120, #123) to `WidgetMode` and the shared harness | existing paged content and line window | done (StyledText docs) | StyledText suites in both modes; differential | M | V0, L2 | #54, #115, #118, #120, #123, #193 |
| 5 | CTabFolder | L5 tab window | Fenwick prefix sums of tab widths, visible tab range, chevron count; per-tab damage; lazy page content like TabFolder `SWT.VIRTUAL` | 10^4 tabs: layout/paint time and tab bounds | CTabFolder suites; differential | M | V0, L2 | #195 |
| 6 | ExpandBar | L6 item headers + child planes | header window, logical bar, child planes (L1) | many items: native scroll (`ScrollWindowEx`) evidence | ExpandBar suites; differential | M | L1, L2 | - |
| 7 | CompositeTable, Gallery, XViewer family (Nebula) | L7 Nebula rows/tiles on the core | CompositeTable rows = resident child planes; Gallery two-axis tile window | per widget | Nebula suites; differential | M each | L1, L2, Nebula `WidgetMode` copy | nebula #150, #175, #176 |
| 8 | GanttChart, Timeline (Nebula) | L8 axis plane | time-axis plane, blit pan, interval index over events | event counts vs paint time | differential | M | L1 | - |
| 9 | GeoMap, Tiles, PaperClips (Nebula) | L9 tile window | tile/page window, tile cache, blit pan | none | differential | M | L1 | - |
| 10 | Text (multi) | L10 measure only | in `viewport` mode the EDIT control stays native unless the oracle shows a document-proportional cost | `setText`, `getLineCount`, scroll at 10^6 lines | oracle evidence | S | - | - |
| 11 | Button, Label, Link, Group, Sash, ProgressBar, Scale, Slider, Spinner, ToolBar, CoolBar | L11 `custom`/`skija` realizations | I31 classes behind `WidgetMode` (section 6) | visual comparison per widget | widget suites under each mode | L (many small) | V0 | #187, #188, #194 |
| - | DateTime, Menu, Browser, Shell, Tray, Caret, IME, Tracker | no viewport | native or no logical content | none | none | - | - | - |

## 8. GTK and Cocoa

- GTK3 on X11: child `GdkWindow` positions and sizes are 16-bit in the X protocol, so L1's rule applies; GTK4 and Wayland use client-side
  coordinates. Cocoa `NSView` frames are `CGFloat` (double): no 16-bit clamp. L1's arithmetic is common code; only the native boundary
  (bounds, mapping, paint origin, mouse) is per platform. GTK and Cocoa behaviour is **not measured on this host**.
- The GTK Tree and Table already have bounded viewports (#114, #142); TabFolder `SWT.VIRTUAL` is wired on GTK and Cocoa (#195).

## 9. Non-claims

- Only L1 is implemented on this branch; every other leaf is planned.
- GTK and Cocoa are not measured here.
- The Nebula audit is based on the source inventory and PR history; per-widget oracles are listed as needed, not run.
