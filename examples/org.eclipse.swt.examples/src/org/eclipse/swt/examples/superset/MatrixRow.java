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

import java.util.Arrays;
import java.util.Locale;

/**
 * One row of VIEWPORT_TEST_MATRIX.tsv. Columns are fixed and documented in
 * superset-matrix/README.md. The three repro_* columns (round-trip state
 * reproduction), damage, resize-sweep, wrapper and capture columns follow
 * {@code result} so earlier column positions stay stable; {@code notes} is
 * the last column. Damage columns (damage_*, full_repaints, protected_hit_px,
 * overpaint_px, underpaint_px)
 * hold three values, {@code scroll/selection/caret_or_hover}, with {@code -}
 * where the interaction does not apply.
 */
public final class MatrixRow {

	public static final String[] COLUMNS = {"concept", "widget_class", "scale", "first_paint_ms", "frame_p50",
			"frame_p95", "frame_max", "materialized", "bound", "within_bound", "heap_mb", "private_mb", "handles",
			"hidden_paints", "hidden_layouts", "screenshot_top", "screenshot_end", "result", "repro_api_equal",
			"repro_pixel_equal", "repro_states_tested", "damage_px_per_frame", "damage_rects_per_frame", "full_repaints",
			"protected_hit_px", "layout_calls_per_resize", "computesize_calls", "resize_frame_p50", "resize_frame_p95",
			"scrollbar_toggles", "edge_pixel_changes", "wrapper_controls", "capture_mode", "overpaint_px", "underpaint_px", "items_materialized_per_paint",
			"memory_flatness", "notes"};
	public static final int CONCEPT = 0, WIDGET = 1, SCALE = 2, FIRST_PAINT = 3, P50 = 4, P95 = 5, MAX = 6,
			MATERIALIZED = 7, BOUND = 8, WITHIN = 9, HEAP = 10, PRIVATE = 11, HANDLES = 12, HIDDEN_PAINTS = 13,
			HIDDEN_LAYOUTS = 14, SHOT_TOP = 15, SHOT_END = 16, RESULT = 17, REPRO_API = 18, REPRO_PIXEL = 19,
			REPRO_STATES = 20, DAMAGE_PX = 21, DAMAGE_RECTS = 22, FULL_REPAINTS = 23, PROTECTED_HIT = 24,
			LAYOUT_PER_RESIZE = 25, COMPUTESIZE = 26, RESIZE_P50 = 27, RESIZE_P95 = 28, SCROLLBAR_TOGGLES = 29,
			EDGE_PIXELS = 30, WRAPPERS = 31, CAPTURE_MODE = 32, OVERPAINT = 33, UNDERPAINT = 34, ITEMS_PER_PAINT = 35,
			MEMORY_FLATNESS = 36, NOTES = 37;

	private final String[] cols = new String[COLUMNS.length];
	private final StringBuilder notes = new StringBuilder();

	public MatrixRow(DemoSpec spec, int scale) {
		Arrays.fill(cols, "-");
		cols[CONCEPT] = spec.concept();
		cols[WIDGET] = spec.widgetClass();
		cols[SCALE] = scale > 0 ? Integer.toString(scale) : "-";
	}

	/** @return the TSV line for a cell the runner did not measure: all metrics "-" */
	public static String blank(String concept, String widget, String scale, String result, String notes) {
		String[] c = new String[COLUMNS.length];
		Arrays.fill(c, "-");
		c[CONCEPT] = concept;
		c[WIDGET] = widget;
		c[SCALE] = scale;
		c[RESULT] = result;
		c[NOTES] = clean(notes);
		return String.join("\t", c);
	}

	public static String header() {
		return String.join("\t", COLUMNS);
	}

	public synchronized MatrixRow set(int col, Object value) {
		cols[col] = clean(String.valueOf(value));
		return this;
	}

	public synchronized MatrixRow ms(int col, double value) {
		cols[col] = String.format(Locale.ROOT, "%.1f", value);
		return this;
	}

	public synchronized MatrixRow note(String fact) {
		if (fact != null && !fact.isEmpty()) notes.append(notes.length() > 0 ? "; " : "").append(clean(fact));
		return this;
	}

	public synchronized String get(int col) {
		return cols[col];
	}

	/** @return the TSV line, prefixed {@code ROW\t} for the driving script */
	public synchronized String line() {
		cols[NOTES] = notes.length() == 0 ? "-" : notes.toString();
		return "ROW\t" + String.join("\t", cols);
	}

	private static String clean(String s) {
		String t = s.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ');
		return t.length() > 400 ? t.substring(0, 397) + "..." : t;
	}
}
