# Superset Viewport Showcase and Viewport Test Matrix

Source: `examples/org.eclipse.swt.examples/src/org/eclipse/swt/examples/superset/`.
Scripts: this directory.

## What it is

* **`SupersetShowcase`** is an SWT application. It shows every concept of the SWT ∪ Nebula ∪ Qt ∪ React superset catalog (Synexia `docs/M3-SCALE/runs/20261013-all-widget-viewport/SUPERSET_WIDGET_CATALOG.tsv`).
  * There is one category tab per catalog category and one tab per concept inside it.
  * Each concept tab has one sub-tab per widget: an SWT core or custom class, or a Nebula family.
  * Large-content widgets are created in their large configuration (SWT.VIRTUAL, generated content, a huge model). They are filled at the selected scale: 1K, 100K, 1M or 10M rows, lines, points or nodes.
  * Concepts with no implementation get a placeholder tab that names the ledger row, its owner and its status.
  * Modal dialogs, the Tracker and desktop side effects (tray, taskbar, toast) open only when you click their button.
  * Every page is created on first selection. The metrics strip ticks only while it is visible.
* **`MatrixRunner`** drives one widget at one scale per JVM, so a crash or a hang costs one cell. `run-matrix.sh` loops over all cells and writes `VIEWPORT_TEST_MATRIX.tsv`.

The structure follows ControlExample and CustomControlExample: a tab per widget, with one class (`SupersetDemo`) playing the role of `Tab`. The logic is not copied.
* `superset-catalog.tsv` is a projection of the Synexia catalog and ledger, made with `awk`.
* `superset-demos.tsv` is the widget table. It covers all 105 concepts and the 5 not-applicable rows, every class in SWT_WIDGET_INVENTORY.tsv (as a widget or a hosted class) and every Nebula family.

Nebula is not on the examples bundle's compile classpath. Nebula widgets are bound by name through `Reflect`, so the bundle still builds with Tycho and needs no new dependency. Run with the Nebula jars on the classpath to see them.

## Data law

Every generated value is a pure function `RowText.value(row, col)`. No storage is kept per row; a 10M-row table costs nothing until a widget asks for a row.

Strings are created only where a widget API demands them:
* SetData callbacks, `setItems(String[])`, `StyledTextContent.getLine`;
* `GC.drawString`;
* one item object per row for APIs that have no virtual mode (List, Combo, CTree, GanttEvent, PTProperty, TextPrint).

Other data is packed: XYGraph receives `double[]` and IntensityGraph and Oscilloscope receive `int[]`. Reused buffers are used throughout.

## Re-run: one command

```bash
MATRIX_OUT=/c/m3-work-c/showcase-build/matrix \
JAVA_HOME="/c/Program Files/Java/jdk-21" NEBULA_COMMIT=<sha> \
  examples/org.eclipse.swt.examples/superset-matrix/run-matrix.sh <swt-jar> <nebula-jars-dir> <label> [regex]
```

* `<swt-jar>`: the SWT jar under test.
  * For the baseline, this is Maven Central `org.eclipse.platform:org.eclipse.swt.win32.win32.x86_64:3.135.0`.
  * For a widget PR, it is the jar built from that PR.
* `<nebula-jars-dir>`: the jars made by `build-nebula.sh`. Platform dependencies (JFace, draw2d and others) go in its `deps/` folder.
* `<label>`: the output folder name and the screenshot folder name, for example `baseline-swt3.135.0`.
* `[regex]`: optional. Only concepts or demo keys (`concept__Class`) that match it are run, for example `table|tree|list` or `text_editor_large`.

The run resumes: a cell already in the TSV is skipped. To re-measure a cell, delete its row.

Compare a PR with the baseline using the same machine, the same label scheme and `diff`/`join` on columns 1 to 3:

```bash
join -t$'\t' <(tail -n+2 base/VIEWPORT_TEST_MATRIX.tsv | awk -F'\t' '{print $1"|"$2"|"$3"\t"$4"\t"$6"\t"$8"\t"$18}' | sort) \
             <(tail -n+2 pr/VIEWPORT_TEST_MATRIX.tsv   | awk -F'\t' '{print $1"|"$2"|"$3"\t"$4"\t"$6"\t"$8"\t"$18}' | sort)
```

### Safety built into the script

* **Memory gate.** Before every JVM, including javac, the script waits until free physical memory is at least `MEM_FREE_MB` (4096) and free commit is at least `COMMIT_FREE_MB` (8192).
* **One JVM at a time, one run per label.** `$OUT/.run.lock` holds the owner pid. A second invocation for the same label exits with code 3 while the owner is alive.
* **Heap size.** `-Xmx` comes from the kind's bytes-per-row estimate times 1.5, plus 256 MB. It is capped at 2 GB; a cell that needs more is NOT_RUN.
* **Scales.** 1K, 100K, 1M and 10M. 10M runs only for demos whose design is virtual (`max_scale` = 10M).
  * Table, Tree, List and StyledText also run 100M and 1B memory-flatness cells, under a fixed `-Xmx256m`. A widget that is not in viewport mode is expected to fail there (OOM), and that failure is its baseline result.
  * StackLayout runs once with 50 panes.
* **Native memory projection.** Every cell above 1M is projected before it runs: native private-memory slope from the 1K and 1M rows (private minus heap), times scale, plus `-Xmx`. When the projection does not fit in free memory minus 1 GB, the cell is NOT_RUN and the projection is in its notes.
* **Timeouts.** Every JVM runs under `timeout`: 120 s, 240 s at 1M and 10M, and 360 s at 100M and 1B. An in-process watchdog also prints a FAIL row that names the phase. The resize sweep has its own 20 s budget.
* **Handle counts.** `HandleProbe.cs` is compiled with .NET `csc.exe` and gives kernel, GDI and USER handle counts.
* **Display captures** never leave the process: only their SHA-256 prefix and pixel counts are written. On a shared desktop another window may cover the shell, and its pixels must not be stored.

## CaptureUtil (shared with other agents)

`org.eclipse.swt.examples.superset.CaptureUtil` provides SWT-level capture at every level. Each method returns a `Capture(mode, ImageData data, byte[] png, String sha256)`; pass `encode=false` to skip PNG and SHA-256.

* `printControl(Control, encode)` and `printShell(Shell, encode)` use `Control.print(GC)` into an Image (WM_PRINT on win32).
  * This works while the window is covered.
  * Use it for pixel hashes, reproduction and edge pixels.
* `copyDisplay(Shell, encode)` and `copyDisplay(Control, encode)` copy the screen.
  * They activate the shell (`setActive` plus `forceActive`), process events until idle, then `new GC(display).copyArea(image, x, y)` over the control's outer bounds.
  * The result is what is really on screen, including non-client scrollbars and composition.
  * They return null when the shell could not be made active.
* `diffPixels(a, b)` counts differing RGB pixels. `png(ImageData)` and `sha256(byte[])` are the encoders.

Compare a print capture with a display capture before trusting the display. The runner's `underpaint_rest_diff_px` note is that comparison at rest. Underpaint is measured only when it is within 1% of the widget's pixels.

## Building Nebula jars

```bash
git -C <nebula-clone> archive <sha> widgets | tar -x -C nebula-src
JAVAC=.../javac JAR=.../jar build-nebula.sh nebula-src <swt-jar> <platform-deps-dir> <out-dir>
```

The script builds one jar per bundle with plain javac, in dependency rounds, and writes `build-nebula.tsv` with a status per bundle.

## Phases per cell

The widget is the TabItem's control itself: there is no wrapper composite and no ScrolledComposite around it.

1. **create**: `create_ms` and the monitor `zoom` go in notes.
2. **first_paint**: `setData(scale)`, then `update()`, then drain the event queue.
   * For a large demo, `first_paint_ms` is time from data to painted. For a small demo, it is create plus first paint.
   * `materialized` is the items, rows or lines created by that first paint: SetData calls, LineStyle callbacks, rows drawn, panes painted, or item objects for APIs without a virtual mode.
   * `bound` is the visible rows plus 2.
   * `screenshot_top` is taken here.
3. **wrappers**: `wrapper_controls` counts the controls between the tab and the widget, plus every descendant the widget owns without `SWT.NO_FOCUS`. The target is 0.
   * Harness content (StackLayout panes, ScrolledComposite children, CompositeTable rows) is created with `NO_FOCUS`, so only widget-made children count.
   * Notes give `tree=` (the control tree) and `widget_creates_focusable_children=`.
4. **repro** (round-trip reproduction):
   * Set a state mix on rows near the top: selection, check, expansion, font, colour, image, an open cell editor, line background, alignment or indent, and focus, as the widget supports. `repro_states_tested` lists them.
   * Capture the public-API values and the settled `Control.print` CRC32.
   * Scroll to the end, scroll back, and compare: `repro_api_equal` and `repro_pixel_equal`.
   * Focus is left out of the API compare when window activation changed meanwhile, which happens on a shared desktop.
   * The pixel compare is `-` when the print kept changing without input (an animated widget).
5. **damage**: what a VNC server would ship.
   * Paint-event clip rectangles feed `org.eclipse.swt.internal.ViewportDamageRecorder` (SWT PR #121, peer session com-synexia-d2) when the SWT jar has it. Otherwise they feed the interim sums in `DamageSink`. `damage_source` in notes says which.
   * The protected band is a 2 px margin inside the client area. Border and native scrollbars are non-client and never receive Paint.
   * Selection change and caret/hover each run 20 frames at the top. Hover is a synthetic `SWT.MouseMove`, so it applies only to Canvas-based widgets; the caret applies to StyledText.
   * The scroll scenario is the sweep below.
   * Each damage column holds `scroll/selection/caret_or_hover`, with `-` where the interaction does not apply:
     * `damage_px_per_frame` and `damage_rects_per_frame` are means per frame;
     * `full_repaints` counts frames whose visible painted area is at least 95% of the viewport;
     * `protected_hit_px` is the total painted over the protected band;
     * `overpaint_px` is the total of painted − visible∩damaged;
     * `underpaint_px` is the mean of stale on-screen pixels per sampled frame: display capture versus fresh print, minus the rest difference. It is `-` when the display capture is not comparable.
   * The first 3 selection frames are pixel-diffed between consecutive prints. Changes outside the damage union go in notes as `damage_outside_px`.
6. **scroll_sweep**: 250 frames, 125 to the end and 125 back, using `setTopIndex`, `setTopItem`, `setTopRow`, `setOrigin`, `setSelection` or the ScrollBar, as the widget offers.
   * Each frame is scroll, `update()`, drain.
   * Columns: `frame_p50`, `frame_p95`, `frame_max`.
   * `screenshot_end` is taken at frame 125.
   * `items_materialized_per_paint` is `items/setdata/handles` per paint over the storm:
     * items: item objects created, read from the widget's private `items` field, so `-` when the widget has none;
     * setdata: SetData, LineStyle or row-draw callbacks;
     * handles: GDI+USER handles created.
   * The target is 0 items, and SetData only for newly visible, unloaded rows.
   * `FLAG paint_materializes_items` in notes names a widget whose owner should change it.
7. **burst** (coalescing): 50 scroll calls with no update in between, then one update. `burst_paints` in notes.
8. **redraw_storm**: 20 `redraw()` + `update()` at rest. Any items or SetData created here are `FLAG redraw_materializes`.
9. **resize**: the shell grows by 1 px in both directions for 150 steps, then shrinks back, within a 20 s budget (`resize_steps` in notes when cut short).
   * `layout_calls_per_resize` and `computesize_calls` come from `CountingLayout` wrappers around every `org.eclipse.swt.layout` layout inside the widget. `Control.computeSize` overrides are not observable.
   * Other columns: `resize_frame_p50`, `resize_frame_p95`, and `scrollbar_toggles` (visibility flips).
   * `edge_pixel_changes` counts pixels that changed in the 3 px outer band and the scrollbar strips between print captures before and after the sweep, at the same size.
10. **capture**: print and display capture of the widget. `capture_mode` is `print+display`, or `print` when the shell could not be made active.
11. **memory**:
    * `heap_mb` is used heap after two GCs.
    * `private_mb` is JVM committed virtual memory, which is private bytes on Windows.
    * `handles` is GDI plus USER objects; kernel handles are in notes.
    * `memory_flatness` is filled in after the run for Table, Tree, List and StyledText: heap/private MB at 1M, 100M and 1B, plus max/min ratios (target about 1).
12. **hidden_update**: the other tab is selected, the data is changed, and events are pumped for 400 ms.
    * `hidden_paints` counts SWT.Paint on the widget tree.
    * `hidden_layouts` counts layout calls inside the widget.
    * Notes give `hidden_materialize`, `hidden_measure` and `hidden_painted_px` (target 0).

Screenshots are `Control.print` into an Image, so a covered window on a shared desktop still captures correctly. They are downscaled to at most 480 px wide PNG and re-saved smaller while over 150 KB.

## Cross-scale columns (`MatrixRunner --postprocess <tsv>`, last step of the script)

* `size_independence`: the visible UI is small, so per-frame work must depend on the viewport only. The largest measured scale (1M, 10M, or 1B where it ran) is compared with the 1K run on these counters:
  * first-paint materialized;
  * items and SetData per paint;
  * painted px per frame for scroll, selection and caret/hover;
  * layout and measure calls per frame;
  * allocated bytes per frame (`com.sun.management.ThreadMXBean`, from the `per_frame_*` notes).

  The verdict is `PASS` when every ratio is at most 1.05; otherwise `FAIL`, which is a bug for the widget's owner. The column gives the max ratio and each counter's ratio.
* `memory_flatness`: see the memory phase.

## Showcase tour (`TOUR=1`)

With `TOUR=1`, the script finally runs `SupersetShowcase --tour <out>`. It selects every category, concept and widget tab the way a user would, prints the shell into `tour/*.png` (800 px wide), and writes `SHOWCASE_TOUR.tsv`, recording for each tab whether it shows a WIDGET, PLACEHOLDER, LAUNCHER or ERROR.

## Scenario families

* **StackLayout** (after Snippet249): 50 panes of 24 labels. The sweep is pane switching. Notes give `hidden_pane_paints` and `hidden_pane_layouts` (target 0).
* **Slider** (after Snippet17/20): the maximum is the scale (up to 10M). Notes give the thumb, the maximum and `selection_readback_errors`.
* **ScrollBar**: the Canvas demo scrolls by a 10M-row ScrollBar.
* **ScrolledComposite** (after Snippet5/166):
  * Physical content: with the fork's `setLogicalContentSize` (#106/#108) the logical-content mode is used; notes say which.
  * `ScrolledComposite+children`: 1K labels. Notes give `children_painted_outside_viewport` (target 0).

## Result

* **PASS**: all of the following hold:
  * `within_bound` is `y` or `-`;
  * there are no hidden paints or layouts;
  * `repro_api_equal` is not `n`;
  * `repro_pixel_equal` is not `n`;
  * there is no paint or redraw materialization flag.
* **FAIL**: any of those is broken. Notes say which: `over_bound`, `hidden_work`, `repro_mismatch` or `paint_materializes`. A crash, an OOM or a timeout is also FAIL, and notes give the phase and the exception.
* **NOT_IMPLEMENTED**: the concept has no SWT or Nebula widget (a placeholder row), or the SWT class is absent from the jar under test. An absent Nebula class is FAIL, because its bundle did not build.
* **NOT_RUN**: one of these:
  * a modal dialog, a modal loop or a desktop side effect (opened in the showcase only);
  * an abstract base class;
  * a scale above the design maximum of a non-virtual widget;
  * the memory projection or heap cap;
  * a harness gap, named in notes;
  * a non-Windows platform: GTK and Cocoa have not been run.

For charts and streams (XYGraph, IntensityGraph, Oscilloscope), the widget keeps every point by API and points drawn per frame cannot be observed. Their `materialized`, `bound` and `within_bound` are `-`.

## Columns of VIEWPORT_TEST_MATRIX.tsv

The single source is `MatrixRow.COLUMNS`; print it with `MatrixRunner --header`.

`concept, widget_class, scale, first_paint_ms, frame_p50, frame_p95, frame_max, materialized, bound, within_bound, heap_mb, private_mb, handles, hidden_paints, hidden_layouts, screenshot_top, screenshot_end, result, repro_api_equal, repro_pixel_equal, repro_states_tested, damage_px_per_frame, damage_rects_per_frame, full_repaints, protected_hit_px, layout_calls_per_resize, computesize_calls, resize_frame_p50, resize_frame_p95, scrollbar_toggles, edge_pixel_changes, wrapper_controls, capture_mode, overpaint_px, underpaint_px, items_materialized_per_paint, size_independence, memory_flatness, notes`
