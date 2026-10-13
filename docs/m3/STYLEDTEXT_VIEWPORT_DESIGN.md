# StyledText viewport design: very long text (2026-10-13)

Status: design plus a first leaf prototype on branch `m3/styledtext-viewport-20261013`.
Base: `hsoliwal/eclipse.platform.swt` master `5094846b2fa693b3889e81d61c1e122d1ed78421`.
Architecture donor: the frozen SWT/Nebula Grid viewport distillation (Synexia PR #10613,
receipt `m3/convergence/FINAL_UI_DISTILLATION_CLOSURE_20261012.md` in `hsoliwal/nebula`).
Donor ledger: `.m3/styledtext-viewport-20261013/FOSS_REUSE_DECISION.tsv` (32 rows).
Evidence: `.m3/styledtext-viewport-20261013/` (bench, results, recipe crate, receipts).

## 1. Problem: inventory of document-proportional work

`StyledText` already paints only the lines that intersect the damage rectangle. Opening,
re-styling, re-wrapping and scrolling a variable-height document still do work proportional to
the whole document. Paths are below
`bundles/org.eclipse.swt/Eclipse SWT Custom Widgets/common/org/eclipse/swt/custom/`, and line
numbers are at the base commit.

| Site (file:line) | Work | Cost proportional to | Leaf 1 |
|---|---|---|---|
| `DefaultContent.java:27` `char[] textStore` | gap buffer holds the whole text as UTF-16 (2 B/char) | document chars | replaced by default (`PagedContent`, zero-copy pieces) |
| `DefaultContent.java:822-833` `setText` | `text.toCharArray()` copy + full `indexLines()` (`:140-163`) | document chars | count-only scan, parallel over 64 Ki pages |
| `DefaultContent.java:34,55-78` | one `int[2]` object per line (~32 B/line), grown by copy | document lines | per-piece counts; offsets materialized on demand (bounded LRU) |
| `DefaultContent.java:292-363` `insert` / `:840-874` `delete` | gap move (copy of the distance) + shift of `lines[]` | distance to gap, lines after the edit | O(log pieces) split/join |
| `StyledTextRenderer.java:1617-1628` `setContent` | `reset(0, lineCount)` | document lines | in-place range reset |
| `StyledTextRenderer.java:1575-1585` `reset(int,int)` | builds a `TreeSet<Integer>` of every line (boxing, O(n log n)) | document lines | `Arrays.fill` of the range, no boxing |
| `StyledTextRenderer.java:1586-1616` `reset(Set)` | allocates one `LineSizeInfo` per line, rescans for max width | document lines | no allocation for unmeasured lines |
| `StyledText.java:844,7806,8151,8549,8600,9197,9226,9334,9358,9798,9826,10004,10873,10912,11143,11179` | `resetCache(0, lineCount)` from setters (font, tabs, wrap, alignment, margins, ...) | document lines per setter | cheap via the two rows above |
| `StyledTextRenderer.java:2002-2017` `textChanging` | full replace -> `reset(0, lineCount)` | document lines | cheap |
| `StyledTextRenderer.java:345-381` `calculateIdle` | variable height (word wrap, spacing, rise): `TextLayout` for **every** line in 50 ms slices | document lines x layout | large documents: visible + one viewport overscan (`ViewportRuntime.RowWindow`) |
| `StyledTextRenderer.java:712-733` `getHeight` | variable height: loops all lines, `content.getLine(i)` (String per line) for unmeasured ones; runs on every scrollbar update | document lines + chars | large documents: O(1) estimate |
| `StyledText.java:5228-5239` `getVerticalScrollOffset` | `renderer.calculate(0, topIndex)`: lays out every line above the viewport after an invalidation | lines above the viewport x layout | large documents: O(1) estimate |
| `StyledText.java:4007-4031` `getLinePixel` (variable) | walks exact heights (layout per line) from the top line | scroll distance x layout | far lines: O(1) estimate |
| `StyledText.java:1451-1488` `getAvailableHeightAbove/Bellow` | walks exact heights to clamp a scroll | scroll distance x layout | far: O(1) estimate |
| `StyledText.java:1095-1160` `calculateTopIndex` (variable) | walks cached/average heights, allocating `LineSizeInfo` per line | scroll distance | far jumps: O(1) proportional mapping; no allocation for estimates |
| `StyledText.java:6236-6243` `handleResize` (wrap) | `renderer.reset(0, lineCount)` + `calculateIdle` | document lines (now cheap reset + bounded idle) | covered |
| `StyledText.java:1278-1300` `computeSize(DEFAULT)` (variable) | `TextLayout` for every line | document lines x layout | open (leaf 3) |
| `StyledText.java:5031`, `:7001` accessible `getValue` | `getText()` copies the document | document chars, on demand | open (API contract) |
| `StyledText.java:8340-8347` `sendAccessibleTextChanged` -> Win32 `Accessible.textChanged` (SWT 3.135 `Accessible.java:1558-1582`, `isATRunning()` is always true at `:5265-5273`) | every `setText` asks the accessible text listener for the whole inserted range and the platform keeps it (`textInserted = eventData`, `:1345-1347`): a full document copy per `setText`, retained | document chars | above 1 Mi chars: `ACC.EVENT_VALUE_CHANGED` (the platform's own fallback); AT reads ranges on demand |
| `StyledText.java:10939-10964` `setText` with `ExtendedModify` | copies the old text for the event | document chars (API contract) | unchanged |
| `StyledTextRenderer.java:1833`, `:2181-2290` style ranges | document-wide `int[] ranges` / `StyleRange[]`; edits shift every later range | styled ranges | open (leaf 4) |
| `StyledText.java:3992`, `:3789` Line(Style/Background)Listener | per painted line, on demand | **viewport** | unchanged |
| `StyledText.java:6159-6183` + `StyledTextRenderer.java:508-563` paint | from `getLineIndex(event.y)` until `y >= endY` | **viewport** | unchanged |
| `StyledText.java:4007-4013`, `:4044-4050` fixed line height | `lineIndex * lineHeight` | O(1) | unchanged |
| `DefaultContent.java:560-600` `getLineAtOffset` | binary search | O(log lines) | O(log pieces) |

## 2. The trick, mapped onto StyledText

| Frozen Grid invariant | StyledText owner | Reused donor | Leaf |
|---|---|---|---|
| Sparse logical extent vs materialized/native residency | Content: char and line counts are subtree sums of a piece tree; per-line offsets of a piece are residency, computed on demand and kept in a bounded LRU (512 pieces). Renderer: `LineSizeInfo` exists only for measured lines; unmeasured geometry is `line x estimate`. | Synexia `MIndexStyledTextDocument` (ADAPT), SWT #106/#108 logical extent contract, Flowless `SizeTracker`, TV_5.0 estimated extent | 1 |
| Visible + bounded-overscan work only | Paint was already damage-bounded. Idle measurement for documents over 65,536 lines covers `ViewportRuntime.RowWindow` (visible + one viewport of overscan) and follows the top index; distant scroll geometry is O(1). | SWT `ViewportRuntime.RowWindow` (#77/#113, DIRECT_IMPORT), Synexia `MIndexVisibleWindow`/`MIndexViewportArithmetic` (same formula) | 1 |
| Stable escaped facade identity | No public/protected signature changes. `StyledText`, `StyledTextContent`, `getContent()`, events and listener order are unchanged; the default content stays a package-private implementation class. `-Dorg.eclipse.swt.custom.StyledText.content=gap` restores `DefaultContent`. | Grid facade rule | 1 |
| body/fixed/chrome/transient z-planes | Already separated in StyledText: body = lines (`renderer.drawLines`), fixed = margins (`handlePaint` margin fill), chrome = native scrollbars, transient = native caret (`Caret`), extra carets and block selection drawn last in `handlePaint`. | Nebula #128 `GridPaintDAG` order, SWT #83 `ViewportPaintGraph` layers | unchanged; see section 5 |
| Real SWT `GC` stays the renderer/native boundary | `TextLayout` + `GC` stay the only renderers. | SWT #83/#87/#100 `ViewportGcProxy`, Nebula #110/#137 | unchanged |
| Java is semantic authority before optional JNI | All indexing is Java; JNI scans or mapped pages need differential proof against `DefaultContent` and a measured crossover first. | Grid rule | deferred |

### 2.1 PagedContent (leaf 1)

Adapted from Synexia `MIndexStyledTextDocument` (Apache-2.0, first-party, develop `0894a5c1`):

* Storage: a balanced (AVL) tree of immutable pieces. A piece is a window into the `String`
  passed to `setText` (64 Ki chars) or to `replaceTextRange` (4 Ki chars). `setText` copies
  nothing; edits split/join O(log pieces) nodes and keep the unchanged backing strings.
* Logical extent: every node keeps its char count, its line-break count and first/last char,
  so line count, `getLineAtOffset` and `getOffsetAtLine` are O(log pieces) without a per-line
  table (MIndex prefix/select, CR LF-aware).
* Residency (gap filled): per-piece delimiter offsets are computed only for pieces that are read
  and kept in an access-ordered LRU of 512 pieces. MIndex computed them eagerly for every piece.
* Edit semantics (gap filled): SWT `DefaultContent` never merges a CR and an LF that become
  adjacent through an edit (`indexLines(int,int,int)` gap rule, bug 568033 exception). MIndex
  merged them and adjusted the event counts. `PagedContent` marks the LF after an edit boundary
  (`firstNoMerge`) so the line structure and the `TextChanging` counts
  (`replaceLineCount = lineAt(end) - lineAt(start)`, `newLineCount = lineCount(newText)`) are
  exactly `DefaultContent`'s. A 2,400-edit randomized differential test proves it (section 6).
* Validation, error codes and listener protocol are `DefaultContent`'s (`isInsideCRLF`,
  `SWT.ERROR_INVALID_ARGUMENT`, `StyledTextListener`).
* CPU workers: `setText` counts line breaks per 64 Ki page on the common fork-join pool once a
  text has 64 pages or more; the UI thread publishes the resulting immutable tree.
* Backing store: zero-copy over the caller's String (Latin-1 compact strings use 1 B/char). A
  memory-mapped page source (IntelliJ LargeFileEditor / klogg pattern) needs a new entry point
  and is deferred; the piece abstraction is the seam.

### 2.2 Renderer and StyledText changes (leaf 1)

* `setContent`, `reset(int,int)` and a full `textChanging` clear the range in place
  (`null` = "needs measurement", the meaning `getLineSize` already gives it); no `TreeSet`, no
  `LineSizeInfo` per line, identical average/max-width bookkeeping.
* Viewport mode (`lineCount > VIEWPORT_LINE_THRESHOLD = 65,536`, variable line height only):
  * `getHeight()` = `lineCount x estimatedLineHeight` (measured average, or the existing wrap
    estimate from the average line length while nothing is measured).
  * `calculateIdle()` measures `RowWindow` = visible lines + one viewport of overscan in 50 ms
    slices, re-targeted on every vertical scroll.
  * `getVerticalScrollOffset`, `getLinePixel` (lines more than 8 viewports away),
    `getAvailableHeightAbove/Bellow` and `calculateTopIndex` (jumps over 8 viewports) use the
    estimate: O(1) instead of a layout walk.
  * Non-exact height reads do not allocate `LineSizeInfo` for unmeasured lines.
* Documents up to 65,536 lines keep the exact whole-document behavior, so every existing test
  document behaves as before.

### 2.3 Later leaves

2. Height prefix structure for measured lines (Synexia `MIndexSwingExtentIndex` Fenwick tree,
   Eclipse `TreeLineTracker`, xi `BreaksMetric`) so near scrolls and `getLinePixel` are
   O(log n) with exact measured heights and the estimate only covers unmeasured blocks.
3. Paged `LineSizeInfo` blocks (sparse residency of the measurement cache itself, today a
   dense pointer array of 4-8 B/line) and `computeSize` bounded by the display height.
4. Sparse style-range store (interval tree) so `setStyleRanges` and edits do not shift
   document-wide arrays; Nebula #148 row-span index for styles spanning into the viewport.
5. Optional memory-mapped page source behind an internal factory; coalescing of tiny inserted
   pieces for append-heavy consoles.

## 3. Donor map (what is reused where)

| Donor | StyledText piece | Decision |
|---|---|---|
| SWT `ViewportRuntime.RowWindow` (#77, #113) | idle measurement window | DIRECT_IMPORT (used unmodified) |
| Synexia `MIndexStyledTextDocument` / `MIndexStyledTextContent` | `PagedContent` engine | ADAPT (Apache-2.0 notice kept, modifications listed) |
| SWT `DefaultContent` | edit semantics oracle, opt-out | ORACLE |
| SWT #106/#108 logical ScrolledComposite | logical extent independent of materialization | REUSE_CONTRACT |
| SWT #112 Java2DCanvas | bounded paint (dirty ∩ clip ∩ client) | PATTERN (StyledText paint already bounded) |
| SWT #83/#87/#100 ViewportGcProxy/PaintGraph, Nebula #110/#137 | GC boundary, per-plane scopes | REUSE_CONTRACT (design, section 5) |
| SWT #91 resource gates | CPU/heap/native memory metrics | ADAPT (Win32 harness mirrors the metrics; GTK lane scenarios pending) |
| SWT #97 SwtScreenshotCapture | pixel evidence | DIRECT_IMPORT in `StyledTextViewportProofTest` |
| Nebula #140 million-row proof | 10M-line proof structure | ADAPT_PATTERN (`StyledTextViewportProofTest`) |
| Nebula #144 lazy priority frontier | ordering of worker jobs by distance to the viewport | PATTERN (section 5) |
| Nebula #148 row-span index | styles/folds spanning into a distant viewport | PATTERN (leaf 4) |
| Synexia `MIndexDeferredUiWork`, `MIndexPageableTreeController`, jsonviewer `MIndexResourceView` | capture / prepare off-thread / publish-if-current | REUSE_CONTRACT (section 5) |
| Synexia `MIndexSwingGcDisplayList` + `MIndexSwingGcReplayer`, `MIndexOpenGlFrameSequence` | CPU-recorded display lists replayed on the real GC; commit-after-swap | ADAPT_PATTERN (section 5, design only) |
| TV_5.0 `IViewerUpdater`, `DeferrredTreeViewer` | priority/discard command contract; low-water-mark prefetch | ADAPT_PATTERN |
| eclipse.platform.ui #2 (lazy virtual TreeViewer delegates bulk traversal to SWT), #7 (selection-scoped expand/collapse) | JFace text layers must delegate to the content/viewport owner, never traverse the document; folding over selected ranges | ADAPT_PATTERN |
| gef-classic `eae0d113d1` draw2d `DeferredUpdateManager` (empty anchors ignored before damage) | bounded damage: only changed areas repaint; transient-plane repaint | PATTERN |
| eclipse.platform #2 recipe-first overlay, fork `.m3` crates | the leaf is a hash-pinned recipe crate with fixed point and refusals | REUSE_CONTRACT |
| IntelliJ, Flowless, RichTextFX, xi, klogg, Eclipse text | see ledger | PATTERN / NO_FIT |

## 4. Measurements (this Windows host, JDK 21.0.9, SWT 3.135 natives)

Harness: `.m3/styledtext-viewport-20261013/bench` (`StyledTextViewportBench`, run with
`run.sh <classes> <label> <MiB> <fixed|wrap> <Xmx> 30000`; one JVM at a time; 3 interleaved
trials; `summarize.py` medians). 80-byte ASCII lines, 1000x800 shell, Consolas 10.
`before` = base master's `org.eclipse.swt.custom` compiled against the SWT 3.135 jar;
`after` = this branch. Heap figures are what the widget holds in addition to the caller's
String (the String stays referenced in both runs).

Medians of 3 trials per row (`bench/evidence/results-matrix.tsv`; `summary.md` has every column).
The "after" rows are the final branch; `results-after-v1.tsv` keeps the earlier "after" run made
before the accessibility payload bound (100 MiB fixed setText 129 ms there vs 60 ms final).

**Open, scroll, memory (100 MiB = 1,310,721 lines)**

| mode | build | setText ms | first paint ms | idle after setText ms | scroll to end ms | UI-thread CPU ms (setText..caret end) | heap retained MiB | heap peak MiB (absolute, incl. caller's 100 MiB String) | process private MiB |
|---|---|---|---|---|---|---|---|---|---|
| fixed | before | 599.5 | 13.3 | 1.3 | 10.2 | 562.5 | 397.5 | 660.1 | 1704.8 |
| fixed | after | **60.1** | 11.4 | 0.04 | 11.6 | **62.5** | **5.9** | **142.8** | **507.0** |
| wrap | before | 661.0 | 76.0 | **30,031 (timed out, 3/3)** | **165,245** | **184,500** | 397.4 | 1360.3 | 1627.8 |
| wrap | after | **67.4** | 11.6 | **9.8** | **15.5** | **62.5** | **6.1** | **142.8** | **449.4** |

1 MiB (13,108 lines, below the 65,536-line viewport threshold): setText 42.9 -> 26.4 ms (fixed),
44.2 -> 38.4 ms (wrap); word wrap keeps the exact whole-document idle pass by design
(648 vs 686 ms idle), heap retained 5.6 -> 2.1 MiB.

**UI-thread / CPU split (250 scroll frames: 100 page-downs, 50 far jumps, 100 wheel steps)**

| size, mode | build | frame p50 / p95 / max ms | paint p50 / p95 / max ms | longest idle dispatch p95 / max ms | CPU per 64 Ki page: break count (worker) p50 / p95 us | per-page offset materialization (UI, on exposure) p50 / p95 us | index build wall ms (worker CPU ms) |
|---|---|---|---|---|---|---|---|
| 100 MiB fixed | before | 7.0 / 16.2 / 38.9 | 2.3 / 5.6 / 19.5 | 0.6 / 0.6 | (monolithic indexLines 167.8 per page-equivalent) | n/a | 268.5 (single thread) |
| 100 MiB fixed | after | 8.8 / 18.4 / 23.2 | 2.3 / 13.0 / 16.3 | 0.0 / 0.0 | 36.3 / 45.4 | 341.3 / 374.4 | 8.7 (56.4) |
| 100 MiB wrap | before | 25.2 / 40.9 / 48.8 | 16.8 / 28.3 / 39.1 | **54.3 / 65.6** | (569.9 per page-equivalent) | n/a | 911.8 |
| 100 MiB wrap | after | **6.6** / **19.1** / 44.1 | **2.9** / **13.2** / 29.4 | **9.7 / 9.7** | 36.3 / 46.8 | 341.7 / 370.3 | 9.8 (60.9) |

Pixels: `Control.print` SHA-256 of the top and end viewports is identical before and after in
all eight configurations (for example 100 MiB wrap top `72dcb47e2ba8ef8f`, end `f1427050e6dee549`).
SWT screen captures (`GC(Display).copyArea`) are recorded per run as diagnostics only.

10M-line proof (`StyledTextViewportProofTest`, 10,000,001 lines x 32 chars = 305 MiB, `-Xmx2g`;
the 1 GiB variant was not run: free physical memory was 0.5-6.5 GB, below the 8 GB rule):
fixed and wrap both pass. Measured lines per scene top / middle (line 5,000,000) /
bottom+horizontal / return: fixed 37 / 74 / 110 / 110, wrap 76 / 190 / 265 / 265 (bound 4,096).
The selection pinned at the middle survives the bottom jump; the wrap return capture is
pixel-identical to the top capture (`02af2fb9...`). Screen captures of fixed scenes 02-04 are
identical (`1509ec28...`), i.e. stale or occluded on this shared desktop, and are not claimed;
control-surface captures differ per scene.

1 GiB measurement: not run (same memory rule).

## 5. CPU / GPU / UI-thread split

Goal: the UI thread never does O(document) work; everything document-sized is CPU-worker work
that is incremental, cancellable on scroll and published as immutable snapshots; the GPU only
rasterizes and composites.

### 5.1 UI thread (viewport-bounded only)

Input, caret, selection state, scroll position and frame scheduling. Allowed per frame:
O(visible + overscan) line lookups, O(log pieces) content queries, O(1) extent math, and
`TextLayout` for lines entering the viewport. Leaf 1 status (measured, section 4): `setText`
publishes the piece tree after a parallel count (the only remaining O(document/cores) UI wait,
because `getLineCount()` must be exact when `setText` returns); scroll frames, distant jumps,
idle slices and paint are viewport-bounded.

### 5.2 CPU workers (incremental, cancellable, immutable snapshots)

| Work | Leaf 1 | Target |
|---|---|---|
| Sparse line index | per-page break counts on the fork-join pool during `setText`; per-piece offsets on demand (measured per page in section 4) | background prefetch of overscan pieces; a future content factory built entirely off the UI thread, published with `setContent` (needs API review) |
| Search / precompute | none | MIndex `findNext` (KMP over pieces) on a worker, results as offsets |
| `LineStyleListener` styling | synchronous, per painted line | worker builds revision-stamped spans (MIndex `Span(revision)`); listener reads the published snapshot; stale revisions dropped |
| `TextLayout` of visible + overscan | UI thread (SWT `TextLayout` is a native resource, display-thread bound) | keep layout on the UI thread; move shaping inputs (styles, bidi segments, tab stops) to workers |
| Wrap and height estimates | O(1) estimate on the UI thread | Fenwick extent index (MIndex `MIndexSwingExtentIndex`) updated from measured heights |

Publication contract (Synexia `MIndexPageableTreeController` + `MIndexDeferredUiWork`,
jsonviewer `MIndexResourceView`): capture a generation on the UI thread, prepare off-thread with
a cancellation token that a scroll or edit supersedes, publish through one coalesced
`Display.asyncExec` only if the generation is still current. Job order: Nebula #144 lazy
priority frontier keyed by distance to the viewport (visible > overscan > prefetch), TV_5.0
`IViewerUpdater` priorities (SET_DATA > MEASURE > PAINT > INDEX) with discard on scroll.

### 5.3 GPU (rasterization and compositing), design only

Rule (frozen Nebula): the real SWT `GC` stays the renderer/native boundary; anything GPU sits
underneath it and never owns semantic state (MIndex `MIndexOpenGlPresenter` rule).

* Planes: body (text lines), fixed (margins, line numbers in a ruler), chrome (scrollbars),
  transient (caret, selection, block selection). Body content goes to tile textures (or a glyph
  atlas); scrolling is a translate/blit of cached tiles and only newly exposed tiles are
  rasterized; a caret blink or selection change repaints only the transient plane.
* What SWT already does: Win32 `StyledText.scroll` -> `ScrollWindowEx` blits the body and
  invalidates only the exposed band; the native `Caret` is a separate OS plane; DWM composites
  the window on the GPU. GTK uses the same scroll/expose model under Cairo; Cocoa under
  CoreGraphics/CoreAnimation layer-backed views.
* Where GPU compositing can sit under `GC`: Windows: GDI today, Direct2D/DirectWrite render
  targets for `GC`/`TextLayout` plus DirectComposition visuals per plane; GTK: Cairo on a GL
  surface (GtkGLArea / GSK on GTK4); macOS: CoreGraphics into CALayer tiles (CATiledLayer).
  Portable candidate: M3jdk21 #797 m3vcpu WebGPU/Dawn backend as a tile compositor fed by
  CPU-recorded display lists (MIndex `MIndexSwingGcDisplayList` recorded off-thread, replayed
  onto the real GC by `MIndexSwingGcReplayer`; `MIndexOpenGlFrameSequence` commit-after-swap).
* Decision: not built. After leaf 1 the 100 MiB scroll frame is 6.6 ms p50 / 19.1 ms p95 of
  which paint is 2.9 / 13.2 ms (GDI + DWM, `ScrollWindowEx` blit + exposed band); the
  document-proportional costs that dominated before (165 s scroll-to-end, 54-66 ms idle
  dispatches, 600 ms setText) were CPU indexing/layout, not rasterization. GPU work is admitted
  only if a measurement shows paint dominating the frame budget (for example HiDPI, large
  fonts or many style runs), and then below `GC`.
* Fast search lanes (Synexia `MINDEX_FASTSEARCH_DONOR_PLAN.md`): SUBSTRING (KMP over pieces,
  MIndex `findNext`) for find/replace, RANGE_RANK (Fenwick extent) for leaf 2, WINDOW_SCAN for
  the visible window. StyledText has no find API; the consumer is JFace
  (`FindReplaceDocumentAdapter` over `IDocument`), so the lane is recorded, not implemented;
  `SerialAtomReview` was NOT_RUN for this leaf.

## 6. Proof

* Differential: `Test_org_eclipse_swt_custom_StyledText_PagedContent` (randomized 40 x 60
  edits over a CR/LF-heavy alphabet against `DefaultContent`: text, every line, every offset,
  every event; viewport-mode checks on a 200,001-line document with and without word wrap).
* Existing StyledText JUnit classes run on this Win32 host against the patched classes in three
  modes: before 291/293 pass, after (paged default) 296/298, after (`content=gap`) 291/293; the
  one failure in every mode is `test_backspaceAndDelete` (`Display.post` OS input, focus
  dependent: it failed 2/3 reruns on the base classes and 1/3 on the branch); 1 test aborted by
  its own assumption in every mode. `test_wrapThenLargeSetTextMeasuresOnlyTheWindow` fails on
  the classes before its fix (33,338 lines measured) and passes after.
* `StyledTextViewportProofTest` (opt-in `-Dswt.viewport.styledTextProof=true`): Nebula #140
  style top / middle / bottom+horizontal / return scenes over 10,000,001 lines (~1.0 GiB),
  fixed and wrapped, measured-line bound, pinned selection, `SwtScreenshotCapture` images and
  sidecars.
* Recipe: `.m3/styledtext-viewport-20261013/recipe` seals the leaf as an
  `M3HashPinnedJavaSnapshotRecipe` crate (`swt-styledtext-viewport-20261013`, 6 targets).
  Executed with OpenRewrite 8.63.4 (JavaParser + RecipeRun; the module pins 8.90.4, which is not
  in the offline repository here) and the canonical owner blobs of Synexia develop `5a1ad952`:
  exact postimages from the base preimages, second-run fixed point, drift refusal and
  missing-source refusal all PASS (`recipe/receipt.json`).
