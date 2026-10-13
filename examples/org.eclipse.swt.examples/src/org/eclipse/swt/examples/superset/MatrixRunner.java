/*******************************************************************************
 * Copyright (c) 2026 Hitesh Soliwal and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.examples.superset;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * Viewport Test Matrix runner: drives ONE widget at ONE scale per JVM, so a
 * crash or hang costs one cell. The widget is the tab's control itself (no
 * wrapper composite). Phases: create, first paint, wrapper count, round-trip
 * reproduction, selection and caret/hover damage, 250-frame scroll sweep with
 * damage, burst coalescing, resize sweep, print-vs-display capture, memory,
 * hidden update (visibility law). Prints one {@code ROW} line.
 *
 * <pre>
 * MatrixRunner --plan [regex]
 * MatrixRunner --header
 * MatrixRunner --run concept widgetClass scale outDir label timeoutSeconds
 * </pre>
 */
public final class MatrixRunner {

	public static final int FRAMES = 250, HIDDEN_WINDOW_MS = 400, BURST = 50;

	private final DemoSpec spec;
	private final int scale;
	private final File shots;
	private final String shotPrefix;
	private final MatrixRow row;
	private final Counters counters = new Counters();
	private final Metrics metrics = new Metrics(FRAMES);
	private volatile String phase = "start";
	private Display display;
	private MatrixProbes probes;

	private MatrixRunner(DemoSpec spec, int scale, File out, String label) {
		this.spec = spec;
		this.scale = scale;
		this.shots = new File(out, "screenshots/" + label);
		this.shotPrefix = "screenshots/" + label + "/";
		this.row = new MatrixRow(spec, scale);
	}

	public static void main(String[] args) throws java.io.IOException {
		if (args.length > 0 && "--header".equals(args[0])) {
			System.out.println(MatrixRow.header());
			return;
		}
		if (args.length > 1 && "--postprocess".equals(args[0])) {
			MatrixPost.run(java.nio.file.Path.of(args[1]));
			return;
		}
		SupersetCatalog catalog = new SupersetCatalog();
		if (args.length > 0 && "--plan".equals(args[0])) {
			MatrixPlan.print(catalog, args.length > 1 ? args[1] : null);
			return;
		}
		if (args.length < 7 || !"--run".equals(args[0])) {
			System.err.println("usage: MatrixRunner --plan [regex] | --header | --run concept widgetClass scale outDir label timeoutSeconds");
			System.exit(2);
		}
		DemoSpec spec = catalog.find(args[1], args[2]);
		if (spec == null) throw new IllegalArgumentException("unknown demo " + args[1] + " " + args[2]);
		MatrixRunner runner = new MatrixRunner(spec, Integer.parseInt(args[3]), new File(args[4]), args[5]);
		runner.watchdog(Integer.parseInt(args[6]));
		runner.runSafely();
		System.exit(0);
	}

	private void watchdog(int seconds) {
		Thread t = new Thread(() -> {
			try {
				Thread.sleep(seconds * 1000L);
			} catch (InterruptedException e) {
				return;
			}
			System.out.println(row.set(MatrixRow.RESULT, "FAIL").note("timeout in phase " + phase + " after " + seconds + "s").line());
			System.out.flush();
			Runtime.getRuntime().halt(124);
		}, "matrix-watchdog");
		t.setDaemon(true);
		t.start();
	}

	private void runSafely() {
		try {
			run();
		} catch (Throwable t) {
			String msg = t.getClass().getSimpleName() + ": " + t.getMessage();
			boolean absent = msg.contains("class absent: " + spec.widgetClass());
			boolean nebula = spec.widgetClass().startsWith("org.eclipse.nebula.");
			String result = absent && !nebula ? "NOT_IMPLEMENTED"
					: t instanceof UnsupportedOperationException && String.valueOf(t.getMessage()).startsWith("harness:")
							? "NOT_RUN" : "FAIL";
			row.set(MatrixRow.RESULT, result).note("phase " + phase + ": " + msg).note(frame(t));
			if (absent && nebula) row.note("Nebula class absent from the jars under test: its bundle did not build against this SWT (see build-nebula.tsv)");
		}
		System.out.println(row.line());
		System.out.flush();
	}

	private static String frame(Throwable t) {
		Throwable root = t;
		while (root.getCause() != null && root.getCause() != root) root = root.getCause();
		StackTraceElement[] st = root.getStackTrace();
		return st.length == 0 ? "" : "at " + st[0];
	}

	private void run() {
		display = new Display();
		probes = new MatrixProbes(display, row, counters);
		Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setText("superset matrix: " + spec.key() + " " + DemoSpec.scaleLabel(scale));
		shell.setLayout(new FillLayout());
		shell.setBounds(24, 24, 960, 680);
		TabFolder folder = new TabFolder(shell, SWT.NONE);
		SupersetDemo demo = Demos.create(spec, counters);
		firstPaint(shell, folder, demo);
		phase = "wrappers";
		probes.wrappers(folder, demo);
		repro(shell, demo);
		phase = "damage";
		probes.calibrate(demo.control());
		probes.interactions(demo);
		sweep(demo);
		burst(demo);
		redrawStorm(demo);
		probes.damageColumns();
		phase = "resize";
		probes.resize(shell, demo);
		phase = "capture";
		probes.captures(shell, demo.control(), shots,
				spec.key() + "__" + DemoSpec.scaleLabel(scale) + "_final");
		memory();
		hidden(folder, demo);
		judge(demo);
		shell.dispose();
		display.dispose();
	}

	private static void tab(TabFolder folder, String text, Control page) {
		TabItem item = new TabItem(folder, SWT.NONE);
		item.setText(text);
		item.setControl(page);
	}

	private void firstPaint(Shell shell, TabFolder folder, SupersetDemo demo) {
		phase = "create";
		long t0 = System.nanoTime();
		tab(folder, spec.simpleName(), demo.create(folder));
		tab(folder, "hidden", new Composite(folder, SWT.NONE));
		shell.open();
		drain();
		long created = System.nanoTime();
		row.note("create_ms=" + Math.round(Metrics.ms(created - t0)) + " zoom=" + shell.getMonitor().getZoom());
		phase = "first_paint";
		counters.reset();
		long t1 = System.nanoTime();
		demo.setData(scale);
		counters.hookPaint(demo.control());
		CountingLayout.wrapTree(demo.control(), counters);
		demo.control().update();
		drain();
		long painted = System.nanoTime();
		row.ms(MatrixRow.FIRST_PAINT, Metrics.ms(spec.largeContent() ? painted - t1 : painted - t0));
		int made = demo.materialized();
		row.set(MatrixRow.MATERIALIZED, made < 0 ? "-" : Integer.toString(made));
		row.set(MatrixRow.BOUND, made < 0 ? "-" : Integer.toString(spec.largeContent() ? demo.visibleRows() + 2 : 1));
		row.set(MatrixRow.SHOT_TOP, shot(demo, "top"));
		probes.protect(demo.control());
		probes.discard();
	}

	/**
	 * Round-trip reproduction (user law: every state and UI state must be
	 * reproduced when the viewport scrolls): set a state mix on rows near the
	 * top, scroll to the end so they leave the viewport, scroll back, then
	 * compare public-API values and the settled Control.print pixel hash.
	 */
	private void repro(Shell shell, SupersetDemo demo) {
		phase = "repro";
		String states = demo.applyState();
		demo.control().update();
		drain();
		boolean active = display.getActiveShell() == shell;
		String api0 = demo.captureState();
		long pix0 = settledHash(demo);
		demo.scrollTo(Math.max(0, demo.rows() - 1));
		demo.control().update();
		drain();
		demo.scrollTo(0);
		demo.control().update();
		drain();
		String api1 = demo.captureState();
		long pix1 = settledHash(demo);
		boolean stable = pix0 != Long.MIN_VALUE && pix1 != Long.MIN_VALUE;
		row.set(MatrixRow.REPRO_STATES, states);
		boolean activationChanged = active != (display.getActiveShell() == shell);
		if (activationChanged) {
			// focus follows window activation on a shared desktop: compare the widget state without it
			api0 = api0.replaceAll("focus_within=(true|false)", "");
			api1 = api1.replaceAll("focus_within=(true|false)", "");
		}
		row.set(MatrixRow.REPRO_API, api0.equals(api1) ? "y" : "n");
		row.set(MatrixRow.REPRO_PIXEL, pix0 < 0 || pix1 < 0 || !stable ? "-" : pix0 == pix1 ? "y" : "n");
		if (!stable) row.note("repro_pixels_unsettled: print hash kept changing without input over 6x60 ms (animated or live widget)");
		if (!api0.equals(api1)) row.note("repro_api_before=" + api0 + " after=" + api1);
		if (activationChanged) row.note("repro_activation_changed (focus excluded from the API compare)");
		probes.discard();
	}

	/** @return the print hash once two captures 60 ms apart agree, or Long.MIN_VALUE */
	private long settledHash(SupersetDemo demo) {
		long prev = UiState.pixelHash(demo.control());
		for (int i = 0; i < 6; i++) {
			MatrixProbes.sleep(60);
			drain();
			long next = UiState.pixelHash(demo.control());
			if (next == prev) return next;
			prev = next;
		}
		return Long.MIN_VALUE;
	}

	private void sweep(SupersetDemo demo) {
		phase = "scroll_sweep";
		int last = Math.max(0, demo.rows() - 1), half = FRAMES / 2, before = demo.materialized();
		ItemCensus census = new ItemCensus(demo.control());
		long items0 = census.count(demo.control());
		int setData0 = counters.get(Counters.MATERIALIZE), paints0 = counters.get(Counters.PAINT);
		int[] handles0 = Metrics.handles();
		int layouts0 = counters.get(Counters.LAYOUT), measures0 = counters.get(Counters.MEASURE);
		long allocated = 0;
		metrics.reset();
		probes.discard();
		for (int f = 1; f <= FRAMES; f++) {
			int target = f <= half ? (int) ((long) last * f / half) : (int) ((long) last * (FRAMES - f) / half);
			long a0 = Metrics.allocatedBytes();
			long t = System.nanoTime();
			demo.scrollTo(target);
			demo.control().update();
			drain();
			metrics.frame(System.nanoTime() - t);
			allocated += Metrics.allocatedBytes() - a0;
			probes.frame(0);
			if (f % 75 == 25) probes.underpaint(0, demo.control());
			if (f == half) {
				row.set(MatrixRow.SHOT_END, shot(demo, "end"));
				probes.discard();
			}
		}
		double[] p = metrics.percentiles();
		row.ms(MatrixRow.P50, p[0]).ms(MatrixRow.P95, p[1]).ms(MatrixRow.MAX, p[2]);
		if (before >= 0) row.note("sweep_materialized=" + (demo.materialized() - before));
		row.note(String.format(java.util.Locale.ROOT, "per_frame_alloc_bytes=%.0f per_frame_layouts=%.2f per_frame_measures=%.2f",
				allocated / (double) FRAMES, (counters.get(Counters.LAYOUT) - layouts0) / (double) FRAMES,
				(counters.get(Counters.MEASURE) - measures0) / (double) FRAMES));
		perPaint(census, demo, items0, setData0, paints0, handles0);
	}

	/**
	 * items_materialized_per_paint (user law: if redraw triggers materialization,
	 * change the widget): item objects created, SetData calls and GDI+USER
	 * handles created per paint during the scroll storm, as items/setdata/handles.
	 */
	private void perPaint(ItemCensus census, SupersetDemo demo, long items0, int setData0, int paints0, int[] handles0) {
		int paints = Math.max(1, counters.get(Counters.PAINT) - paints0);
		long items1 = census.count(demo.control());
		int[] handles1 = Metrics.handles();
		String items = items0 < 0 || items1 < 0 ? "-" : String.format(java.util.Locale.ROOT, "%.2f", (items1 - items0) / (double) paints);
		String setData = spec.largeContent() && demo.materialized() >= 0 && demo.paintCountsAsMaterialization()
				? String.format(java.util.Locale.ROOT, "%.2f", (counters.get(Counters.MATERIALIZE) - setData0) / (double) paints) : "-";
		String handles = handles0[1] < 0 || handles1[1] < 0 ? "-"
				: String.format(java.util.Locale.ROOT, "%.2f", (handles1[1] + handles1[2] - handles0[1] - handles0[2]) / (double) paints);
		row.set(MatrixRow.ITEMS_PER_PAINT, items + "/" + setData + "/" + handles);
		if (!"-".equals(items) && items1 > items0) row.note("FLAG paint_materializes_items: " + (items1 - items0) + " item objects created by " + paints + " paints (owner: change the widget)");
	}

	/** Redraw storm at rest: 20 redraw+update with no scrolling must create nothing. */
	private void redrawStorm(SupersetDemo demo) {
		phase = "redraw_storm";
		ItemCensus census = new ItemCensus(demo.control());
		long items0 = census.count(demo.control());
		int setData0 = counters.get(Counters.MATERIALIZE);
		for (int i = 0; i < 20; i++) {
			demo.control().redraw();
			demo.control().update();
			drain();
		}
		long items1 = census.count(demo.control());
		int setData = counters.get(Counters.MATERIALIZE) - setData0;
		boolean counts = demo.materialized() >= 0 && demo.paintCountsAsMaterialization();
		row.note("redraw_storm: items=" + (items0 < 0 ? "-" : Long.toString(items1 - items0)) + " setdata_or_lines=" + (counts ? Integer.toString(setData) : "-"));
		if ((items0 >= 0 && items1 > items0) || (spec.largeContent() && counts && setData > 0)) {
			row.note("FLAG redraw_materializes (owner: change the widget)");
		}
		probes.discard();
	}

	/** Coalescing: BURST scroll calls with no update in between, then one update. */
	private void burst(SupersetDemo demo) {
		phase = "burst";
		int last = Math.max(0, demo.rows() - 1), paints = counters.get(Counters.PAINT);
		for (int i = 1; i <= BURST; i++) demo.scrollTo((int) ((long) last * i / BURST));
		demo.control().update();
		drain();
		row.note("burst_paints=" + (counters.get(Counters.PAINT) - paints) + " for " + BURST + " scroll calls");
		demo.scrollTo(0);
		demo.control().update();
		drain();
		probes.discard();
	}

	private void memory() {
		phase = "memory";
		row.ms(MatrixRow.HEAP, Metrics.heapMb()).ms(MatrixRow.PRIVATE, Metrics.privateMb());
		int[] h = Metrics.handles();
		row.set(MatrixRow.HANDLES, h[1] < 0 ? "-" : Integer.toString(h[1] + h[2]));
		if (h[0] >= 0) row.note("kernel_handles=" + h[0] + " gdi=" + h[1] + " user=" + h[2]);
	}

	private void hidden(TabFolder folder, SupersetDemo demo) {
		phase = "hidden_update";
		folder.setSelection(1);
		drain();
		probes.discard();
		counters.reset();
		demo.updateData();
		long end = System.currentTimeMillis() + HIDDEN_WINDOW_MS;
		while (System.currentTimeMillis() < end) {
			if (!display.readAndDispatch()) MatrixProbes.sleep(5);
		}
		row.set(MatrixRow.HIDDEN_PAINTS, counters.get(Counters.PAINT));
		row.set(MatrixRow.HIDDEN_LAYOUTS, counters.get(Counters.LAYOUT));
		row.note("hidden_materialize=" + counters.get(Counters.MATERIALIZE) + " hidden_measure=" + counters.get(Counters.MEASURE)
				+ " hidden_painted_px=" + counters.sink().endFrame()[4]);
	}

	private void judge(SupersetDemo demo) {
		phase = "judge";
		boolean observable = !"-".equals(row.get(MatrixRow.MATERIALIZED));
		boolean within = !observable || Integer.parseInt(row.get(MatrixRow.MATERIALIZED)) <= Integer.parseInt(row.get(MatrixRow.BOUND));
		boolean quiet = "0".equals(row.get(MatrixRow.HIDDEN_PAINTS)) && "0".equals(row.get(MatrixRow.HIDDEN_LAYOUTS));
		String perPaint = row.get(MatrixRow.ITEMS_PER_PAINT);
		boolean noPaintItems = !row.line().contains("FLAG paint_materializes_items") && !row.line().contains("FLAG redraw_materializes");
		boolean repro = !"n".equals(row.get(MatrixRow.REPRO_API))
				&& (!"n".equals(row.get(MatrixRow.REPRO_PIXEL)) || row.line().contains("repro_activation_changed"));
		row.set(MatrixRow.WITHIN, observable ? (within ? "y" : "n") : "-");
		row.set(MatrixRow.RESULT, within && quiet && repro && noPaintItems ? "PASS" : "FAIL");
		if (!within) row.note("over_bound");
		if (!quiet) row.note("hidden_work");
		if (!repro) row.note("repro_mismatch");
		if (!noPaintItems) row.note("paint_materializes (items_materialized_per_paint=" + perPaint + ")");
		row.note(demo.notes());
	}

	private String shot(SupersetDemo demo, String which) {
		String name = spec.key() + "__" + DemoSpec.scaleLabel(scale) + "_" + which + ".png";
		CaptureUtil.Capture capture = CaptureUtil.printControl(demo.control(), true);
		if (capture == null) return "-";
		String saved = Snapshot.save(capture.data(), shots, name);
		if ("-".equals(saved)) return "-";
		try {
			File file = new File(shots, saved);
			String hash = CaptureUtil.sha256(java.nio.file.Files.readAllBytes(file.toPath()));
			String sidecar = String.join("\n",
					"mode=print",
					"png=" + saved,
					"sha256=" + hash,
					"source.width=" + capture.data().width,
					"source.height=" + capture.data().height,
					"");
			java.nio.file.Files.writeString(
					new File(shots, saved.substring(0, saved.length() - 4) + ".properties").toPath(),
					sidecar);
		} catch (java.io.IOException e) {
			row.note("shot_sidecar_failed=" + which + ":" + e.getMessage());
		}
		return shotPrefix + saved;
	}

	private void drain() {
		probes.drain();
	}
}
