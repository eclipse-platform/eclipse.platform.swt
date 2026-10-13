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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cross-scale columns, filled after every cell has run:
 * <ul>
 * <li>{@code size_independence}: per-frame work at the largest measured scale
 * against the 1K run (rows/lines materialized, SetData per paint, items per
 * paint, painted px per frame for scroll/selection/caret, layout and measure
 * calls per frame, allocated bytes per frame). PASS when every ratio is within
 * 5%; the visible UI is small, so per-frame work must not grow with data.</li>
 * <li>{@code memory_flatness}: heap and private MB at 1M, 100M and 1B and the
 * max/min ratios (target about 1).</li>
 * </ul>
 */
final class MatrixPost {

	private static final double TOLERANCE = 1.05;
	private static final Pattern PER_FRAME = Pattern.compile("per_frame_(alloc_bytes|layouts|measures)=([0-9.]+)");

	private MatrixPost() {
	}

	static void run(Path tsv) throws IOException {
		List<String[]> rows = new ArrayList<>();
		for (String line : Files.readAllLines(tsv, StandardCharsets.UTF_8)) rows.add(line.split("\t", -1));
		Map<String, List<String[]>> groups = new LinkedHashMap<>();
		for (String[] r : rows.subList(1, rows.size())) groups.computeIfAbsent(r[0] + "\t" + r[1], k -> new ArrayList<>()).add(r);
		for (List<String[]> g : groups.values()) {
			String size = sizeIndependence(g), flat = flatness(g);
			for (String[] r : g) {
				r[MatrixRow.SIZE_INDEPENDENCE] = size;
				r[MatrixRow.MEMORY_FLATNESS] = flat;
			}
		}
		List<String> out = new ArrayList<>();
		for (String[] r : rows) out.add(String.join("\t", r));
		Files.write(tsv, out, StandardCharsets.UTF_8);
	}

	private static boolean measured(String[] r) {
		return r[MatrixRow.SCALE].matches("\\d+") && !"-".equals(r[MatrixRow.P50]);
	}

	private static String sizeIndependence(List<String[]> g) {
		String[] base = null, top = null;
		for (String[] r : g) {
			if (!measured(r)) continue;
			if ("1000".equals(r[MatrixRow.SCALE])) base = r;
			if (top == null || Long.parseLong(r[MatrixRow.SCALE]) > Long.parseLong(top[MatrixRow.SCALE])) top = r;
		}
		if (base == null || top == null || top == base) return "-";
		Map<String, double[]> m = new LinkedHashMap<>();
		pair(m, "materialized", num(base[MatrixRow.MATERIALIZED]), num(top[MatrixRow.MATERIALIZED]));
		pair(m, "items", part(base[MatrixRow.ITEMS_PER_PAINT], 0), part(top[MatrixRow.ITEMS_PER_PAINT], 0));
		pair(m, "setdata", part(base[MatrixRow.ITEMS_PER_PAINT], 1), part(top[MatrixRow.ITEMS_PER_PAINT], 1));
		pair(m, "scroll_px", part(base[MatrixRow.DAMAGE_PX], 0), part(top[MatrixRow.DAMAGE_PX], 0));
		pair(m, "selection_px", part(base[MatrixRow.DAMAGE_PX], 1), part(top[MatrixRow.DAMAGE_PX], 1));
		pair(m, "caret_px", part(base[MatrixRow.DAMAGE_PX], 2), part(top[MatrixRow.DAMAGE_PX], 2));
		for (String k : new String[] {"layouts", "measures", "alloc_bytes"}) pair(m, k, perFrame(base, k), perFrame(top, k));
		double worst = 1;
		StringBuilder b = new StringBuilder();
		for (Map.Entry<String, double[]> e : m.entrySet()) {
			double ratio = ratio(e.getValue()[0], e.getValue()[1]);
			worst = Math.max(worst, ratio);
			b.append(b.length() == 0 ? "" : " ").append(e.getKey()).append('=').append(fmt(ratio));
		}
		return (worst <= TOLERANCE ? "PASS" : "FAIL") + " max_ratio=" + fmt(worst) + " 1K->" + DemoSpec.scaleLabel(Integer.parseInt(top[MatrixRow.SCALE])) + " (" + b + ")";
	}

	private static void pair(Map<String, double[]> m, String key, double a, double b) {
		if (!Double.isNaN(a) && !Double.isNaN(b)) m.put(key, new double[] {a, b});
	}

	private static double ratio(double a, double b) {
		if (a == 0) return b == 0 ? 1 : Double.POSITIVE_INFINITY;
		return Math.max(b / a, 0);
	}

	private static String fmt(double v) {
		return Double.isInfinite(v) ? "inf" : String.format(Locale.ROOT, "%.2f", v);
	}

	private static double num(String s) {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return Double.NaN;
		}
	}

	private static double part(String s, int i) {
		String[] p = s.split("/");
		return i < p.length ? num(p[i]) : Double.NaN;
	}

	private static double perFrame(String[] r, String key) {
		Matcher m = PER_FRAME.matcher(r[MatrixRow.NOTES]);
		while (m.find()) {
			if (m.group(1).equals(key)) return num(m.group(2));
		}
		return Double.NaN;
	}

	private static String flatness(List<String[]> g) {
		String[] scales = {"1000000", "100000000", "1000000000"};
		boolean flatCells = false;
		for (String[] r : g) flatCells |= "100000000".equals(r[MatrixRow.SCALE]) || "1000000000".equals(r[MatrixRow.SCALE]);
		if (!flatCells) return "-";
		StringBuilder b = new StringBuilder("heap/private MB");
		double hMin = Double.MAX_VALUE, hMax = 0, pMin = Double.MAX_VALUE, pMax = 0;
		boolean complete = true;
		for (String s : scales) {
			String[] r = g.stream().filter(x -> s.equals(x[MatrixRow.SCALE])).findFirst().orElse(null);
			String result = r == null ? "absent" : r[MatrixRow.RESULT];
			double h = r == null ? Double.NaN : num(r[MatrixRow.HEAP]), p = r == null ? Double.NaN : num(r[MatrixRow.PRIVATE]);
			b.append(' ').append(DemoSpec.scaleLabel(Integer.parseInt(s))).append('=')
					.append(Double.isNaN(h) ? "-" : fmt(h)).append('/').append(Double.isNaN(p) ? "-" : fmt(p)).append('(').append(result).append(')');
			if (Double.isNaN(h) || Double.isNaN(p) || h <= 0 || p <= 0) {
				complete = false;
				continue;
			}
			hMin = Math.min(hMin, h);
			hMax = Math.max(hMax, h);
			pMin = Math.min(pMin, p);
			pMax = Math.max(pMax, p);
		}
		return b + (complete ? "; heap_ratio=" + fmt(hMax / hMin) + " private_ratio=" + fmt(pMax / pMin) : "; ratio=n/a (a scale failed or did not run)");
	}
}
