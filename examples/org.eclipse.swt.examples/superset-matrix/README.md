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
* **One JVM at a time.**
* **Heap size.** `-Xmx` comes from the kind's bytes-per-row estimate times 1.5, plus 256 MB. It is capped at 2 GB; a cell that needs more is NOT_RUN.
* **10M scale.** 10M runs only for demos whose design is virtual (`max_scale` = 10M). It is skipped (NOT_RUN) when 10× the 1M run's private memory would not fit in free memory minus 1 GB.
* **Timeouts.** Every JVM runs under `timeout`. An in-process watchdog also prints a FAIL row that names the phase.
* **Handle counts.** `HandleProbe.cs` is compiled with .NET `csc.exe` and gives kernel, GDI and USER handle counts.

## Building Nebula jars

```bash
git -C <nebula-clone> archive <sha> widgets | tar -x -C nebula-src
JAVAC=.../javac JAR=.../jar build-nebula.sh nebula-src <swt-jar> <platform-deps-dir> <out-dir>
```

The script builds one jar per bundle with plain javac, in dependency rounds, and writes `build-nebula.tsv` with a status per bundle.

## Phases per cell

1. **create**: the widget, inside a TabFolder page (`create_ms` in notes). The host page counts layout calls through `CountingLayout`.
2. **first_paint**: `setData(scale)`, then `update()`, then drain the event queue.
   * For a large demo, `first_paint_ms` is time from data to painted. For a small demo, it is create plus first paint.
   * `materialized` is the items, rows or lines created by that first paint: SetData calls, LineStyle callbacks, rows drawn, or item objects for APIs without a virtual mode.
   * `bound` is the visible rows plus 2.
3. **repro** (round-trip reproduction):
   * Set a state mix on rows near the top: selection, check, expansion, font, colour, image, an open cell editor and focus, as the widget supports. `repro_states_tested` lists them.
   * Capture the public-API values and the `Control.print` pixel CRC32.
   * Scroll to the end, scroll back, and capture again.
   * `repro_api_equal` and `repro_pixel_equal` are `y` or `n`. The pixel compare is `-` when two captures taken without scrolling already differ (an animated widget) or the control cannot print.
4. **damage**: the damage area per interaction, as a VNC server would ship it.
   * The source is each SWT.Paint event's clip rectangle, which is the bounds of the OS update region. This is the interim source. The `DamageRecorder` in `common/org/eclipse/swt/internal` is being built by peer session com-synexia-d2; this harness switches to it when it lands and builds no recorder of its own.
   * Selection change and caret/hover each run 20 frames at the top. Hover is a synthetic `SWT.MouseMove`, so it applies only to custom-drawn (Canvas) widgets. The caret applies to StyledText.
   * Scroll damage is taken during the sweep.
   * The columns hold `scroll/selection/caret_or_hover`:
     * `damage_px_per_frame` is mean damaged pixels per frame;
     * `damage_rects_per_frame` is mean paint rectangles per frame;
     * `full_repaints` is the count of paints that covered the whole control.
   * `-` means the interaction does not apply.
   * The first 3 selection frames are also pixel-diffed between consecutive `Control.print` captures. Pixels that changed outside the union of the damage rectangles go in notes as `damage_outside_px`.
5. **scroll_sweep**: 250 frames, 125 to the end and 125 back, using `setTopIndex`, `setTopItem`, `setTopRow` or the ScrollBar selection, as the widget offers.
   * Each frame is scroll, `update()`, drain.
   * Columns: `frame_p50`, `frame_p95`, `frame_max`. Notes: `sweep_materialized`.
   * `screenshot_end` is taken at frame 125.
6. **memory**: `heap_mb` is used heap after two GCs. `private_mb` is JVM committed virtual memory, which is private bytes on Windows. `handles` is GDI plus USER objects; kernel handles are in notes.
7. **hidden_update**: the other tab is selected, the data is changed, and events are pumped for 400 ms.
   * `hidden_paints` counts SWT.Paint on the widget tree.
   * `hidden_layouts` counts `layout()` calls on the host and on every `org.eclipse.swt.layout` layout inside the widget.
   * `hidden_materialize` and `hidden_measure` go in notes.

Screenshots are `Control.print` into an Image, so a covered window on a shared desktop still captures correctly. They are downscaled to at most 480 px wide PNG and re-saved smaller while over 150 KB.

## Result

* **PASS**: all of the following hold:
  * `within_bound` is `y` or `-`;
  * there are no hidden paints or layouts;
  * `repro_api_equal` is not `n`;
  * `repro_pixel_equal` is not `n`.
* **FAIL**: any of those is broken. Notes say which: `over_bound`, `hidden_work` or `repro_mismatch`. A cell that crashes or hits a timeout is also FAIL, and notes give the phase and the exception.
* **NOT_IMPLEMENTED**: the concept has no SWT or Nebula widget (a placeholder row), or the class is absent from the jars under test.
* **NOT_RUN**: one of these:
  * a modal dialog, a modal loop or a desktop side effect (opened in the showcase only);
  * an abstract base class;
  * a scale above the design maximum of a non-virtual widget;
  * the memory projection or heap cap;
  * a harness gap, named in notes;
  * a non-Windows platform: GTK and Cocoa have not been run.

For charts and streams (XYGraph, IntensityGraph, Oscilloscope), the widget keeps every point by API and points drawn per frame cannot be observed. Their `materialized`, `bound` and `within_bound` are `-`.

## Columns of VIEWPORT_TEST_MATRIX.tsv

`concept, widget_class, scale, first_paint_ms, frame_p50, frame_p95, frame_max, materialized, bound, within_bound, heap_mb, private_mb, handles, hidden_paints, hidden_layouts, screenshot_top, screenshot_end, result, repro_api_equal, repro_pixel_equal, repro_states_tested, damage_px_per_frame, damage_rects_per_frame, full_repaints, notes`
