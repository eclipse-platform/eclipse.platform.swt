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
import java.io.IOException;
import java.nio.file.Files;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;

/**
 * Matrix probes added on top of the viewport sweep: round-trip state
 * reproduction, damage area per interaction, resize sweep, wrapper-control
 * count and print-versus-display capture. Each writes its columns into the
 * row; the runner owns the phase order.
 */
final class MatrixProbes {

	static final int DAMAGE_FRAMES = 20, RESIZE_STEPS = 150, EDGE_BAND = 3, MARGIN_BAND = 2;
	static final long RESIZE_BUDGET_MS = 20_000;

	private final Display display;
	private final MatrixRow row;
	private final Counters counters;
	/**
	 * damage[scenario] = {px, rects, fullRepaints, protectedHitPx, frames, overpaintPx, underpaintPx,
	 * underpaintSamples}; scenario 0 scroll, 1 selection, 2 caret/hover
	 */
	private final long[][] damage = new long[3][8];
	private long restDiff = -1;
	private boolean comparable;
	private int invalidSamples;

	/**
	 * A display capture is valid only when the widget's outer 3 px frame (its
	 * border, which every interaction leaves alone) matches the print capture;
	 * otherwise another window covered the widget on the shared desktop.
	 */
	private static boolean sameFrame(Control c, CaptureUtil.Capture shown, CaptureUtil.Capture printed) {
		int w = c.getSize().x, h = c.getSize().y, e = EDGE_BAND;
		int[] frame = {0, 0, w, e, 0, h - e, w, e, 0, 0, e, h, w - e, 0, e, h};
		return UiState.changedInBands(shown.data(), printed.data(), frame) == 0;
	}

	MatrixProbes(Display display, MatrixRow row, Counters counters) {
		this.display = display;
		this.row = row;
		this.counters = counters;
	}

	void drain() {
		while (display.readAndDispatch()) {
			// dispatch everything queued, including WM_PAINT
		}
	}

	static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/** wrapper_controls: controls between the tab and the widget plus focusable children the widget owns. */
	void wrappers(Composite tab, SupersetDemo demo) {
		int between = 0;
		for (Composite p = demo.control().getParent(); p != null && p != tab; p = p.getParent()) between++;
		StringBuilder tree = new StringBuilder();
		int focusable = focusableChildren(demo.control(), tree, 0);
		row.set(MatrixRow.WRAPPERS, between + focusable);
		row.note("tree=" + demo.control().getClass().getSimpleName() + (tree.length() == 0 ? "" : ">" + tree));
		if (focusable > 0) row.note("widget_creates_focusable_children=" + focusable);
	}

	private static int focusableChildren(Control c, StringBuilder tree, int depth) {
		if (!(c instanceof Composite composite)) return 0;
		int n = 0;
		for (Control child : composite.getChildren()) {
			if ((child.getStyle() & SWT.NO_FOCUS) == 0) n++;
			if (tree.length() < 160) tree.append(tree.length() == 0 ? "" : ",").append(child.getClass().getSimpleName());
			n += focusableChildren(child, tree, depth + 1);
		}
		return n;
	}

	/** Viewport and protected band (client-area margins) for the damage sink. */
	void protect(Control c) {
		Rectangle area = c instanceof Scrollable s ? s.getClientArea() : new Rectangle(0, 0, c.getSize().x, c.getSize().y);
		counters.sink().viewport(area.width, area.height, MARGIN_BAND);
		row.note("damage_source=" + counters.sink().source());
	}

	/** Ends one damage frame into scenario {@code s}. */
	void frame(int s) {
		long[] f = counters.sink().endFrame();
		long[] d = damage[s];
		for (int i = 0; i < 4; i++) d[i] += f[i];
		d[4]++;
		d[5] += Math.max(0, f[4] - f[5]);
	}

	/** Discards damage recorded since the last frame (paints caused by Control.print). */
	void discard() {
		counters.sink().endFrame();
		counters.resetUnion();
	}

	/**
	 * Calibrates the on-screen check: the pixel difference between the display
	 * capture and the print capture of the widget at rest. Underpaint is only
	 * measured when that difference is at most 1% of the widget's pixels (on a
	 * shared desktop another window may cover the shell).
	 */
	void calibrate(Control c) {
		CaptureUtil.Capture shown = CaptureUtil.copyDisplay(c, false);
		CaptureUtil.Capture printed = CaptureUtil.printControl(c, false);
		discard();
		debug("rest", shown, printed);
		restDiff = shown == null || printed == null || !sameFrame(c, shown, printed) ? -1 : CaptureUtil.diffPixels(shown.data(), printed.data());
		long pixels = (long) c.getSize().x * c.getSize().y;
		comparable = restDiff >= 0 && restDiff * 100 <= pixels;
		row.note("underpaint_rest_diff_px=" + restDiff + (comparable ? "" : " (display capture not comparable: underpaint not measured)"));
	}

	/** With -Dsuperset.debugCapture=dir, writes both captures as PNG for inspection. */
	private void debug(String tag, CaptureUtil.Capture shown, CaptureUtil.Capture printed) {
		String dir = System.getProperty("superset.debugCapture");
		if (dir == null || shown == null || printed == null) return;
		try {
			java.nio.file.Files.write(java.nio.file.Path.of(dir, tag + "_display.png"), CaptureUtil.png(shown.data()));
			java.nio.file.Files.write(java.nio.file.Path.of(dir, tag + "_print.png"), CaptureUtil.png(printed.data()));
		} catch (java.io.IOException e) {
			row.note("debug capture write failed: " + e);
		}
	}

	/**
	 * Underpaint sample for scenario s: stale on-screen pixels, i.e. the display
	 * capture against a fresh print of the same widget, minus the rest difference.
	 */
	void underpaint(int s, Control c) {
		if (!comparable) return;
		CaptureUtil.Capture shown = CaptureUtil.copyDisplay(c, false);
		CaptureUtil.Capture printed = CaptureUtil.printControl(c, false);
		discard();
		if (shown == null || printed == null) return;
		if (!sameFrame(c, shown, printed)) {
			invalidSamples++;
			return;
		}
		long d = CaptureUtil.diffPixels(shown.data(), printed.data());
		if (d < 0) return;
		damage[s][6] += Math.max(0, d - restDiff);
		damage[s][7]++;
	}

	/** Selection and caret/hover damage, DAMAGE_FRAMES frames each, plus the pixel-diff check. */
	void interactions(SupersetDemo demo) {
		long outside = 0;
		int checked = 0;
		for (int f = 0; f < DAMAGE_FRAMES; f++) {
			ImageData before = f < 3 ? UiState.print(demo.control()) : null;
			discard();
			boolean did = demo.select(f + 1);
			demo.control().update();
			drain();
			Rectangle union = counters.damageUnion();
			if (did) frame(1);
			else discard();
			if (before != null && did) {
				outside += UiState.changedOutside(before, UiState.print(demo.control()), union);
				checked++;
				discard();
				underpaint(1, demo.control());
			}
		}
		if (checked > 0) row.note("damage_outside_px=" + outside + " (pixel diff, " + checked + " selection frames)");
		for (int f = 0; f < DAMAGE_FRAMES; f++) {
			discard();
			boolean did = demo.caretOrHover(f + 1);
			demo.control().update();
			drain();
			if (did) frame(2);
			else discard();
			if (did && f < 3) underpaint(2, demo.control());
		}
	}

	void damageColumns() {
		row.set(MatrixRow.DAMAGE_PX, triple(0, true)).set(MatrixRow.DAMAGE_RECTS, rects());
		row.set(MatrixRow.FULL_REPAINTS, triple(2, false)).set(MatrixRow.PROTECTED_HIT, triple(3, false));
		row.set(MatrixRow.OVERPAINT, triple(5, false)).set(MatrixRow.UNDERPAINT, underTriple());
		if (invalidSamples > 0) row.note("underpaint_samples_skipped=" + invalidSamples + " (widget covered on screen)");
	}

	private String triple(int slot, boolean perFrame) {
		StringBuilder b = new StringBuilder();
		for (int s = 0; s < 3; s++) {
			long[] d = damage[s];
			b.append(s == 0 ? "" : "/").append(d[4] == 0 ? "-" : Long.toString(perFrame ? d[slot] / d[4] : d[slot]));
		}
		return b.toString();
	}

	private String underTriple() {
		StringBuilder b = new StringBuilder();
		for (int s = 0; s < 3; s++) {
			long[] d = damage[s];
			b.append(s == 0 ? "" : "/").append(d[4] == 0 || d[7] == 0 ? "-" : Long.toString(d[6] / d[7]));
		}
		return b.toString();
	}

	private String rects() {
		StringBuilder b = new StringBuilder();
		for (int s = 0; s < 3; s++) {
			long[] d = damage[s];
			b.append(s == 0 ? "" : "/").append(d[4] == 0 ? "-" : String.format(Locale.ROOT, "%.2f", d[1] / (double) d[4]));
		}
		return b.toString();
	}

	/**
	 * Resize sweep: grow the shell 1 px per step in both directions for
	 * RESIZE_STEPS steps, then shrink back. Layout/computeSize calls come from
	 * the CountingLayout wrappers inside the widget; edge pixels are compared
	 * before and after at the same size over the outer band and scrollbar strips.
	 */
	void resize(Shell shell, SupersetDemo demo) {
		Control c = demo.control();
		ImageData before = UiState.print(c);
		int[] bands = edgeBands(c);
		Rectangle b = shell.getBounds();
		int layouts = counters.get(Counters.LAYOUT), measures = counters.get(Counters.MEASURE), toggles = 0;
		int bars = barState(c);
		Metrics m = new Metrics(2 * RESIZE_STEPS);
		long deadline = System.currentTimeMillis() + RESIZE_BUDGET_MS;
		int steps = 0, grown = 0;
		for (int i = 1; i <= 2 * RESIZE_STEPS; i++) {
			if (i <= RESIZE_STEPS && System.currentTimeMillis() > deadline) i = 2 * RESIZE_STEPS - grown;
			int d = i <= RESIZE_STEPS ? i : 2 * RESIZE_STEPS - i;
			if (i <= RESIZE_STEPS) grown = i;
			steps++;
			long t = System.nanoTime();
			shell.setSize(b.width + d, b.height + d);
			c.update();
			drain();
			m.frame(System.nanoTime() - t);
			int now = barState(c);
			if (now != bars) toggles++;
			bars = now;
		}
		discard();
		double[] p = m.percentiles();
		row.set(MatrixRow.LAYOUT_PER_RESIZE, String.format(Locale.ROOT, "%.2f", (counters.get(Counters.LAYOUT) - layouts) / (double) steps));
		if (steps < 2 * RESIZE_STEPS) row.note("resize_steps=" + steps + "/" + 2 * RESIZE_STEPS + " (grew " + grown + " px; " + RESIZE_BUDGET_MS / 1000 + " s budget)");
		row.set(MatrixRow.COMPUTESIZE, counters.get(Counters.MEASURE) - measures);
		row.ms(MatrixRow.RESIZE_P50, p[0]).ms(MatrixRow.RESIZE_P95, p[1]);
		row.set(MatrixRow.SCROLLBAR_TOGGLES, toggles);
		long edge = UiState.changedInBands(before, UiState.print(c), bands);
		row.set(MatrixRow.EDGE_PIXELS, edge < 0 ? "-" : Long.toString(edge));
		discard();
	}

	private static int barState(Control c) {
		if (!(c instanceof Scrollable s)) return 0;
		ScrollBar v = s.getVerticalBar(), h = s.getHorizontalBar();
		return (v != null && v.getVisible() ? 1 : 0) | (h != null && h.getVisible() ? 2 : 0);
	}

	/** Outer EDGE_BAND frame of the control plus its vertical/horizontal scrollbar strips. */
	private static int[] edgeBands(Control c) {
		Point size = c.getSize();
		int w = size.x, h = size.y, e = EDGE_BAND;
		int vw = 0, hh = 0;
		if (c instanceof Scrollable s) {
			if (s.getVerticalBar() != null && s.getVerticalBar().getVisible()) vw = s.getVerticalBar().getSize().x + c.getBorderWidth();
			if (s.getHorizontalBar() != null && s.getHorizontalBar().getVisible()) hh = s.getHorizontalBar().getSize().y + c.getBorderWidth();
		}
		return new int[] {0, 0, w, e, 0, h - e, w, e, 0, 0, e, h, w - e, 0, e, h, w - vw, 0, vw, h, 0, h - hh, w, hh};
	}

	/**
	 * Captures the widget by print and from the screen (shell activated);
	 * records capture_mode and the pixel difference over the widget's bounds,
	 * which flags paint paths print does not exercise (non-client scrollbars).
	 */
	void captures(Shell shell, Control widget, File shots, String stem) {
		CaptureUtil.Capture printed = CaptureUtil.printControl(widget, true);
		CaptureUtil.Capture shown = CaptureUtil.copyDisplay(widget, true);
		if (shown != null && printed != null && !sameFrame(widget, shown, printed)) {
			// Another window covers the widget on this shared desktop: the screen pixels are
			// someone else's content and must never be persisted or published.
			row.note("display_capture=rejected (widget border differs from print: covered on screen; not persisted)");
			shown = null;
		}
		debug("final", shown, printed);
		discard();
		if (printed == null) {
			row.set(MatrixRow.CAPTURE_MODE, "none");
			row.note("print_capture=failed");
			return;
		}
		String printName = Snapshot.save(printed.data(), shots, stem + "-print.png");
		String displayName = shown == null ? "-" : Snapshot.save(shown.data(), shots, stem + "-display.png");
		try {
			File printFile = new File(shots, printName);
			String printHash = CaptureUtil.sha256(Files.readAllBytes(printFile.toPath()));
			String displayHash = "-";
			if (!"-".equals(displayName)) {
				displayHash = CaptureUtil.sha256(Files.readAllBytes(new File(shots, displayName).toPath()));
			}
			String sidecar = String.join("\n",
					"print=" + printName,
					"print.sha256=" + printHash,
					"display=" + displayName,
					"display.sha256=" + displayHash,
					"print.width=" + printed.data().width,
					"print.height=" + printed.data().height,
					"display.width=" + (shown == null ? -1 : shown.data().width),
					"display.height=" + (shown == null ? -1 : shown.data().height),
					"mode=" + (shown == null ? "print" : "print+display"),
					"");
			Files.writeString(new File(shots, stem + "-capture.properties").toPath(), sidecar);
			row.note("capture_sidecar=" + stem + "-capture.properties");
		} catch (IOException e) {
			row.note("capture_persist_failed=" + e.getMessage());
		}
		if (shown == null) {
			row.set(MatrixRow.CAPTURE_MODE, "print");
			if (!row.line().contains("display_capture=rejected")) row.note("display_capture=skipped (shell could not be made active)");
			return;
		}
		row.set(MatrixRow.CAPTURE_MODE, "print+display");
		long diff = CaptureUtil.diffPixels(printed.data(), shown.data());
		row.note("print_vs_display_diff_px=" + diff + " print_sha256=" + printed.sha256().substring(0, 16)
				+ " display_sha256=" + shown.sha256().substring(0, 16));
	}
}
