# StyledText API coverage under the viewport (leaf 2, 2026-10-13)

Branch `m3/styledtext-api-coverage-20261013`, stacked on `m3/styledtext-viewport-20261013`
(SWT #115, unmerged). Coverage table: `.m3/styledtext-api-coverage-20261013/STYLEDTEXT_API_COVERAGE.tsv`.
Donor ledger: `.m3/styledtext-api-coverage-20261013/FOSS_REUSE_DECISION.tsv` (41 rows).

## 1. Laws this leaf follows

| Law (user) | How StyledText follows it in this leaf |
|---|---|
| Every public/protected API works at huge-document scale; superset only | 215 API methods, 44 `ST` constants, 76 `case ST.` paths, listener/event types, StyleRange/TextStyle/Bullet features and widget features are each a coverage row with a paged-vs-gap differential test id and a 1M-line scale test id. No public or protected signature changed. |
| Bits and ints; objects only when a caller asks | New state added in this leaf is packed ints: line-size cache generation (`sizeGeneration`), idle window `[idleStart, idleEnd)`, modified-line intervals (`int[]` pairs instead of `TreeSet<Integer>`), layout request counter. Existing per-measured-line `LineSizeInfo` and per-attribute `LineInfo` objects remain and are the leaf 3 target (packed int tables, Scintilla `Partitioning` shape). |
| Bitwise masks | The viewport switch is the `SWT.VIRTUAL` style bit (`(style & SWT.VIRTUAL) != 0`) or a document above 65,536 lines; style/line flags as int masks are leaf 3. |
| The viewport and the widget are one | `StyledText`'s `ViewportRuntime` mirror object is removed: the origin is the widget's own `horizontalScrollOffset`/`verticalScrollOffset`. The renderer's `ViewportRuntime.RowWindow` object is replaced by two ints with the same arithmetic. No child control or helper surface exists; `Test_org_eclipse_swt_custom_StyledText_SingleControl` proves focus, TAB traversal and the accessible role stay on the StyledText (with and without `SWT.VIRTUAL`). Any future helper surface (for example a GPU surface) must be created with `SWT.NO_FOCUS | SWT.NO_BACKGROUND` and forward its events. |

### Mask bits the viewport honours

| Bit | Source | Effect on the viewport |
|---|---|---|
| `SWT.VIRTUAL` | caller style (new meaning for StyledText, additive) | forces viewport mode for any document size |
| `SWT.NO_BACKGROUND` | always added by `StylePolicy.STYLED_TEXT` | the widget paints every pixel itself (`handlePaint` fills below the last line), so exposed strips need no OS erase |
| `SWT.DOUBLE_BUFFERED` | always added | paint goes through the platform back buffer; a scroll blit plus exposed-strip paint is flicker free |
| `SWT.NO_REDRAW_RESIZE` | always added | a resize exposes only the new strip; viewport mode re-targets the idle window instead of re-measuring |
| `SWT.NO_FOCUS` | not set on StyledText | StyledText keeps focus; reserved for helper surfaces |
| `SWT.NO_MERGE_PAINTS` | not set | paint events may merge; the idle window is independent of paint merging |
| `SWT.SINGLE` / `SWT.MULTI` / `SWT.WRAP` / `SWT.READ_ONLY` / `SWT.FULL_SELECTION` / `SWT.BORDER` | caller | unchanged semantics; exercised by CustomControlExample's StyledText tab toggles on 1M lines |

## 2. Structural changes in this leaf (and why)

| Change | Reason |
|---|---|
| `StyledTextRenderer` full reset is O(1) via `sizeGeneration`; `peekLineSize` reads without allocating | setters (font, tabs, wrap, margins, alignment...) no longer walk every line |
| modified-line intervals (`int[]`) in `setStyleRanges`, `resetCache(int[])` | a style spanning a million lines boxed a million `Integer`s into a `TreeSet` |
| `hasVariableHeightStyle()` scans the style store without copying | `setStyleRanges` copied every style on each call |
| `computeSize` in viewport mode lays out one display of lines and estimates the rest | it laid out every line with word wrap |
| `getLineIndex(y)` far path (O(1) estimate) | distant y walked line heights with layouts |
| accessible `getValue` returns the viewport projection above 1 Mi chars | it copied the whole document on every AT query |
| `setLineSpacingProvider` in viewport mode resets once instead of calling the provider for every line | O(lines) provider calls |
| renderer `layoutRequests` counter | observability for the scale bound (test and bench only) |
| block-mode `selectAll` in viewport mode covers the logical extent | it laid out every line to find the widest (999,796 layouts, 112 s on 1M lines) |
| `ViewportRuntime` mirror removed from `StyledText`; fork test `test_viewportRuntimeMirrorsStyledTextScrollState` rewritten as `test_viewportOriginLivesInStyledText` | one-widget law; the mirror was write-only |
| renderer `RowWindow` object replaced by `idleStart/idleEnd` ints | one-widget law, bits/ints law |
| viewport mode honours `SWT.VIRTUAL` | behaviour switches are mask bits |
| `getVerticalScrollOffset` in fixed-height viewport mode is `topIndex * lineHeight - topIndexY` | after a wrap-to-nowrap switch at a deep top index it laid out every line above the viewport (found by the Snippet163 exerciser run) |
| pre-measurement height capped by the display height in viewport mode (`viewportHeight()`) | a control sized to its whole preferred height (Snippet343 in an ExpandBar) pre-measured thousands of off-screen lines; paint still lays out whatever it paints |
| `PagedContent`: explicit range and overflow checks instead of `Objects.checkFromIndexSize` / `Math.addExact` | shared logic must stay GWT-transpilable (GWT does not wrap int arithmetic; avoid `Objects.check*`) |

## 3. Tests

Coverage table `STYLEDTEXT_API_COVERAGE.tsv` (438 rows):

| Kind | Rows | Status |
|---|---|---|
| public/protected methods (incl. anonymous timer/print `run()`, `getData`) | 184 | 183 PASSED, 1 NOT_RUN (`print()` starts a real job on the default printer) |
| accessibility adapter methods (MSAA/IA2) | 28 | 28 PASSED |
| content listener methods (`textChanging/textChanged/textSet`) | 3 | 3 PASSED |
| `ST` constants | 44 | 44 PASSED |
| `case ST.` action paths (main, block, block insert) | 76 | 76 PASSED |
| StyleRange/TextStyle/Bullet and widget features | 24 | 24 PASSED |
| one-widget checks | 5 | 5 PASSED |
| example runs on 1,000,000 lines | 54 | 49 PASSED, 3 CLIENT_AREA_BOUND, 2 SNIPPET_ASSUMPTION_SAME_ON_GAP |
| platforms | 3 | NOT_RUN (GTK, Cocoa, default-printer job) |
| Qt gaps | 17 | QT_GAP_NOT_IMPLEMENTED (listed only) |

Count reconciliation: the widget catalog (Synexia #10864) counts 178 public methods on the top-level
`StyledText` class. The 184 method rows are those 178, the constructor, and the 5 public `run()` of
nested classes; the 28 accessibility adapter and 3 content listener rows are also nested-class members
(215 inventory rows in all).

Test runs (Win32 host, JDK 21.0.9, SWT 3.135 natives):

* `Test_org_eclipse_swt_custom_StyledText_ApiDifferential`: 221/221 PASSED. Every op runs on the paged
  default content and on `content=gap`; after each op the public state snapshot, the listener event
  transcript and the client-area pixels (Control.print) must be identical. The print-to-file job ran for both
  (Microsoft Print to PDF, page 1, both PDFs 86,276 bytes).
* `Test_org_eclipse_swt_custom_StyledText_ApiScale`: 442/442 PASSED on 1,000,001 lines, fixed line height and
  word wrap, layout bound 4,096 per op; the largest was 630 layouts (wrap autoscroll-left), the slowest op
  `getData("StyledText.htmlText")` (whole-document HTML, inherent O(chars)) at 5.3 s.
* Existing StyledText classes + PagedContent + SingleControl: 298/300 (paged default), 299/300 (`content=gap`);
  the paged failure is `test_backspaceAndDelete` (`Display.post` OS input; fails on the base classes too);
  one assumption-skipped test in both modes.
* Examples: 29 StyledText snippets, CustomControlExample (StyledText tab and every style checkbox toggle) and
  TextEditor each ran unmodified with 1,000,000 lines appended (13 ops each). CLIENT_AREA_BOUND rows are
  configurations where the example sizes the widget to its preferred height (65,514-65,535 px client area):
  layouts stay bounded by that client area (4,286-8,932 per op). Snippet332's BidiSegmentListener returns
  invalid segments for lines without "Eclipse" and fails identically with `content=gap`.

Found and fixed by the exercisers: block-mode `selectAll` measured every line (999,796 layouts, 112 s);
fixed-height `getVerticalScrollOffset` after a wrap toggle laid out every line above the top index; a control
sized to its full preferred height pre-measured off-screen lines.

## 4. Web transpilation notes (RWT + GWT)

Desktop Java/JNI stays primary. Logic that should also transpile (content index, packed keys) keeps
int-packed state, never relies on int overflow wrapping (GWT does not wrap `+ - *`), and avoids
`Objects.check*` and `String[].clone()`. `long` appears only in desktop-only arithmetic (estimated
pixel offsets, counters). Follow-up: the word-wrap scroll extent (an O(1) uniform estimate today)
can consume `VirtualRowExtents` (SWT #117, variable row extents, measurement cache, scroll anchoring)
once #117 lands.

## 5. Qt superset

`qt_equivalent` maps every row to QPlainTextEdit / QTextEdit / QTextDocument / QTextCursor /
QSyntaxHighlighter API names (pattern only; Qt is LGPL-3/GPL, `LICENSE_REVIEW_REQUIRED` in the ledger,
no code read or copied). Rows of kind `qt-gap` list Qt capabilities StyledText lacks (undo/redo, find,
maximum line count, extra selections, per-line user state, incremental rehighlight, zoom, wrap at
column, rich import, visible whitespace, update-request with scroll delta, rich objects, custom document
layout, interaction flags, modified flag, placeholder, center-on-scroll). They are listed, not
implemented in this leaf. StyledText supersets Qt on block (column) selection, multiple carets/selections,
`BidiSegmentListener`, `MovementListener` hooks and `PaintObjectListener` inline objects.
