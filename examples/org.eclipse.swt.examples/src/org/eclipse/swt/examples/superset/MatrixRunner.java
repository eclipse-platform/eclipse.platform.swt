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
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * Viewport Test Matrix runner: drives ONE widget at ONE scale per JVM, so a
 * crash or hang costs one cell. Phases: create, first paint with data, 250
 * scroll frames to the end and back, round-trip state reproduction, memory
 * and handles, hidden update (visibility law), screenshots via Control.print.
 * Prints one {@code ROW} line.
 *
 * <pre>
 * MatrixRunner --plan [regex]
 * MatrixRunner --run concept widgetClass scale outDir label timeoutSeconds
 * </pre>
 */
public final class MatrixRunner {

	public static final int FRAMES = 250, HIDDEN_WINDOW_MS = 400, DAMAGE_FRAMES = 20;

	private final DemoSpec spec;
	private final int scale;
	private final File shots;
	private final String shotPrefix;
	private final MatrixRow row;
	private final Counters counters = new Counters();
	private final Metrics metrics = new Metrics(FRAMES);
	private volatile String phase = "start";
	private Display display;

	private MatrixRunner(DemoSpec spec, int scale, File out, String label) {
		this.spec = spec;
		this.scale = scale;
		this.shots = new File(out, "screenshots/" + label);
		this.shotPrefix = "screenshots/" + label + "/";
		this.row = new MatrixRow(spec, scale);
	}

	public static void main(String[] args) {
		SupersetCatalog catalog = new SupersetCatalog();
		if (args.length > 0 && "--plan".equals(args[0])) {
			MatrixPlan.print(catalog, args.length > 1 ? args[1] : null);
			return;
		}
		if (args.length < 7 || !"--run".equals(args[0])) {
			System.err.println("usage: MatrixRunner --plan [regex] | --run concept widgetClass scale outDir label timeoutSeconds");
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
		Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setText("superset matrix: " + spec.key() + " " + DemoSpec.scaleLabel(scale));
		shell.setLayout(new FillLayout());
		shell.setBounds(24, 24, 960, 680);
		TabFolder folder = new TabFolder(shell, SWT.NONE);
		Composite host = new Composite(folder, SWT.NONE);
		host.setLayout(new CountingLayout(null, counters));
		tab(folder, spec.simpleName(), host);
		tab(folder, "hidden", new Composite(folder, SWT.NONE));
		SupersetDemo demo = Demos.create(spec, counters);
		firstPaint(shell, host, demo);
		repro(shell, demo);
		damage(demo);
		sweep(demo);
		damageColumns();
		memory();
		hidden(folder, demo);
		judge(demo);
		shell.dispose();
		display.dispose();
	}

	private static void tab(TabFolder folder, String text, Composite page) {
		TabItem item = new TabItem(folder, SWT.NONE);
		item.setText(text);
		item.setControl(page);
	}

	private void firstPaint(Shell shell, Composite host, SupersetDemo demo) {
		phase = "create";
		long t0 = System.nanoTime();
		demo.create(host);
		shell.open();
		host.layout(true, true);
		drain();
		long created = System.nanoTime();
		row.note("create_ms=" + Math.round(Metrics.ms(created - t0)));
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
	}

	/**
	 * Round-trip reproduction (user law: every state and UI state must be
	 * reproduced when the viewport scrolls): set a state mix on rows near the
	 * top, scroll to the end so they leave the viewport, scroll back, then
	 * compare public-API values and the Control.print pixel hash.
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
		row.set(MatrixRow.REPRO_API, api0.equals(api1) ? "y" : "n");
		row.set(MatrixRow.REPRO_PIXEL, pix0 < 0 || pix1 < 0 || !stable ? "-" : pix0 == pix1 ? "y" : "n");
		if (!stable) row.note("repro_pixels_unsettled: print hash kept changing without input over 6x60 ms (animated or live widget)");
		if (!api0.equals(api1)) row.note("repro_api_before=" + api0 + " after=" + api1);
		if (active != (display.getActiveShell() == shell)) row.note("repro_activation_changed");
	}

	/**
	 * Damage area per interaction (user law: isolate the GC damage area, as a
	 * VNC server would). Selection and caret/hover run {@link #DAMAGE_FRAMES}
	 * frames each at the top; scroll damage is taken during the sweep. The
	 * first 3 selection frames are also checked by pixel diff: pixels changed
	 * outside the union of the damage rectangles are counted in notes.
	 */
	private void damage(SupersetDemo demo) {
		phase = "damage";
		long outside = 0;
		int checked = 0;
		for (int f = 0; f < DAMAGE_FRAMES; f++) {
			ImageData before = f < 3 ? UiState.print(demo.control()) : null;
			counters.resetDamage();
			boolean did = demo.select(f + 1);
			demo.control().update();
			drain();
			accumulate(0, did);
			if (before != null && did) {
				org.eclipse.swt.graphics.Rectangle union = counters.damageUnion();
				outside += UiState.changedOutside(before, UiState.print(demo.control()), union);
				checked++;
			}
		}
		if (checked > 0) row.note("damage_outside_px=" + outside + " (pixel diff, " + checked + " selection frames)");
		for (int f = 0; f < DAMAGE_FRAMES; f++) {
			counters.resetDamage();
			boolean did = demo.caretOrHover(f + 1);
			demo.control().update();
			drain();
			accumulate(1, did);
		}
	}

	/** damage[scenario] = {px, unused, rects, full, frames}; scenario 0 selection, 1 caret/hover, 2 scroll */
	private final long[][] damage = new long[3][5];

	private void accumulate(int scenario, boolean applies) {
		if (!applies) return;
		long[] d = damage[scenario];
		d[0] += counters.damagePx();
		d[2] += counters.get(Counters.DAMAGE_RECTS);
		d[3] += counters.get(Counters.FULL_REPAINTS);
		d[4]++;
	}

	private void damageColumns() {
		long[] scroll = damage[2], sel = damage[0], hover = damage[1];
		row.set(MatrixRow.DAMAGE_PX, per(scroll, 0) + "/" + per(sel, 0) + "/" + per(hover, 0));
		row.set(MatrixRow.DAMAGE_RECTS, perRects(scroll) + "/" + perRects(sel) + "/" + perRects(hover));
		row.set(MatrixRow.FULL_REPAINTS, total(scroll) + "/" + total(sel) + "/" + total(hover));
	}

	private static String per(long[] d, int slot) {
		return d[4] == 0 ? "-" : Long.toString(d[slot] / d[4]);
	}

	private static String perRects(long[] d) {
		return d[4] == 0 ? "-" : String.format(java.util.Locale.ROOT, "%.2f", d[2] / (double) d[4]);
	}

	private static String total(long[] d) {
		return d[4] == 0 ? "-" : Long.toString(d[3]);
	}

	/** @return the print hash once two captures 60 ms apart agree, or Long.MIN_VALUE */
	private long settledHash(SupersetDemo demo) {
		long prev = UiState.pixelHash(demo.control());
		for (int i = 0; i < 6; i++) {
			sleep(60);
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
		metrics.reset();
		counters.resetDamage();
		long[] d = damage[2];
		for (int f = 1; f <= FRAMES; f++) {
			int target = f <= half ? (int) ((long) last * f / half) : (int) ((long) last * (FRAMES - f) / half);
			long t = System.nanoTime();
			demo.scrollTo(target);
			demo.control().update();
			drain();
			metrics.frame(System.nanoTime() - t);
			d[0] += counters.damagePx();
			d[2] += counters.get(Counters.DAMAGE_RECTS);
			d[3] += counters.get(Counters.FULL_REPAINTS);
			d[4]++;
			if (f == half) row.set(MatrixRow.SHOT_END, shot(demo, "end"));
			counters.resetDamage();
		}
		double[] p = metrics.percentiles();
		row.ms(MatrixRow.P50, p[0]).ms(MatrixRow.P95, p[1]).ms(MatrixRow.MAX, p[2]);
		if (before >= 0) row.note("sweep_materialized=" + (demo.materialized() - before));
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
		counters.reset();
		demo.updateData();
		long end = System.currentTimeMillis() + HIDDEN_WINDOW_MS;
		while (System.currentTimeMillis() < end) {
			if (!display.readAndDispatch()) sleep(5);
		}
		row.set(MatrixRow.HIDDEN_PAINTS, counters.get(Counters.PAINT));
		row.set(MatrixRow.HIDDEN_LAYOUTS, counters.get(Counters.LAYOUT));
		row.note("hidden_materialize=" + counters.get(Counters.MATERIALIZE) + " hidden_measure=" + counters.get(Counters.MEASURE));
	}

	private void judge(SupersetDemo demo) {
		phase = "judge";
		boolean observable = !"-".equals(row.get(MatrixRow.MATERIALIZED));
		boolean within = !observable || Integer.parseInt(row.get(MatrixRow.MATERIALIZED)) <= Integer.parseInt(row.get(MatrixRow.BOUND));
		boolean quiet = "0".equals(row.get(MatrixRow.HIDDEN_PAINTS)) && "0".equals(row.get(MatrixRow.HIDDEN_LAYOUTS));
		row.set(MatrixRow.WITHIN, observable ? (within ? "y" : "n") : "-");
		boolean repro = !"n".equals(row.get(MatrixRow.REPRO_API))
				&& (!"n".equals(row.get(MatrixRow.REPRO_PIXEL)) || row.line().contains("repro_activation_changed"));
		row.set(MatrixRow.RESULT, within && quiet && repro ? "PASS" : "FAIL");
		if (!within) row.note("over_bound");
		if (!quiet) row.note("hidden_work");
		if (!repro) row.note("repro_mismatch");
		row.note(demo.notes());
	}

	private String shot(SupersetDemo demo, String which) {
		String name = spec.key() + "__" + DemoSpec.scaleLabel(scale) + "_" + which + ".png";
		String saved = Snapshot.save(demo.control(), shots, name);
		return "-".equals(saved) ? "-" : shotPrefix + saved;
	}

	private void drain() {
		while (display.readAndDispatch()) {
			// dispatch everything queued, including WM_PAINT
		}
	}

	private static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
