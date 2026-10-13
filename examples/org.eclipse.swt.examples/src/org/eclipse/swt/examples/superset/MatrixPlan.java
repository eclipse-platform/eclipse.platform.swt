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

import java.util.Map;

/**
 * Expands the demo table into matrix cells. Runnable cells print
 * {@code PLAN\tconcept\twidget\tscale\txmx_mb}; cells the runner does not
 * drive print their final {@code ROW} directly (NOT_IMPLEMENTED / NOT_RUN).
 * Heap is sized per kind from a bytes-per-row estimate and capped at 2 GB;
 * scales above a demo's design maximum (non-virtual designs) are NOT_RUN. A
 * demo whose design maximum is below 1K (StackLayout's 50 panes) runs once
 * at that fixed scale. Virtual-capable kinds (Table, Tree, List, StyledText)
 * add 100M and 1B memory-flatness cells under a fixed -Xmx256m; a widget that
 * is not in viewport mode is expected to fail there, which is its result.
 */
public final class MatrixPlan {

	public static final int BASE_MB = 256, CAP_MB = 2048, FLAT_XMX_MB = 256;

	/** Virtual-capable kinds that also run the 100M and 1B memory-flatness cells. */
	public static final java.util.Set<String> FLAT_KINDS = java.util.Set.of("VTABLE", "VTREE", "LIST", "STYLEDTEXT");

	/**
	 * Flat-scale kinds whose rows also cost native memory (win32 Tree inserts one
	 * HTREEITEM per row). Their 100M/1B cells keep the native-memory projection
	 * gate. The other flat cells are bounded by -Xmx256m and always run, so a
	 * baseline OOM is recorded as the result instead of being projected away.
	 */
	public static final java.util.Set<String> NATIVE_PER_ROW_KINDS = java.util.Set.of("VTREE");

	/** Retained heap bytes per row/item for each kind (estimate, x1.5 margin applied). */
	private static final Map<String, Integer> BYTES_PER_ROW = Map.ofEntries(Map.entry("VTABLE", 48),
			Map.entry("VTABLE_CURSOR", 48), Map.entry("VTREE", 64), Map.entry("VTREE_CURSOR", 64),
			Map.entry("COLUMNS", 600), Map.entry("LIST", 160), Map.entry("COMBO", 160), Map.entry("STYLEDTEXT", 32),
			Map.entry("CANVAS", 0), Map.entry("SCROLLED", 0), Map.entry("TABS", 600), Map.entry("NGRID", 96),
			Map.entry("NGRIDCOLS", 800), Map.entry("NGALLERY", 160), Map.entry("NCOMPTABLE", 16),
			Map.entry("NCTREE", 600), Map.entry("NTABLECOMBO", 200), Map.entry("NCTREECOMBO", 600),
			Map.entry("NDUALLIST", 300), Map.entry("NPROPTABLE", 800), Map.entry("NXYGRAPH", 96),
			Map.entry("NINTENSITY", 16), Map.entry("NOSC", 16), Map.entry("NGANTT", 1200), Map.entry("NPRINT", 400),
			Map.entry("SLIDER", 0), Map.entry("SCROLLED_CHILDREN", 2000), Map.entry("STACK", 0));

	private MatrixPlan() {
	}

	/** @return -Xmx in MB for one cell, or -1 when it exceeds the 2 GB cap */
	public static int xmxMb(DemoSpec spec, int scale) {
		long bytes = (long) BYTES_PER_ROW.getOrDefault(spec.kind(), 64) * scale * 3 / 2;
		long mb = BASE_MB + (bytes >> 20);
		mb = (mb + 63) / 64 * 64;
		return mb > CAP_MB ? -1 : (int) mb;
	}

	/** Prints the plan for every demo (optionally only those whose key matches {@code filter}). */
	public static void print(SupersetCatalog catalog, String filter) {
		for (DemoSpec spec : catalog.demos()) {
			if (filter != null && !spec.key().matches(filter) && !spec.concept().matches(filter)) continue;
			if (!spec.runnable()) {
				System.out.println(new MatrixRow(spec, 0).set(MatrixRow.RESULT, Demos.staticResult(spec))
						.note(Demos.staticReason(spec)).line());
				continue;
			}
			if (!spec.largeContent()) {
				System.out.println("PLAN\t" + spec.concept() + "\t" + spec.widgetClass() + "\t0\t" + BASE_MB);
				continue;
			}
			if (spec.maxScale() < DemoSpec.SCALES[0]) {
				System.out.println("PLAN\t" + spec.concept() + "\t" + spec.widgetClass() + "\t" + spec.maxScale() + "\t" + BASE_MB);
				continue;
			}
			for (int scale : DemoSpec.SCALES) planScale(spec, scale);
			if (!FLAT_KINDS.contains(spec.kind())) continue;
			for (int scale : DemoSpec.FLAT_SCALES) {
				System.out.println("PLAN\t" + spec.concept() + "\t" + spec.widgetClass() + "\t" + scale + "\t" + FLAT_XMX_MB + "\t"
						+ (NATIVE_PER_ROW_KINDS.contains(spec.kind()) ? "native" : "heap"));
			}
		}
	}

	private static void planScale(DemoSpec spec, int scale) {
		int xmx = xmxMb(spec, scale);
		if (scale > spec.maxScale() || xmx < 0) {
			String why = scale > spec.maxScale()
					? "scale above design maximum " + DemoSpec.scaleLabel(spec.maxScale()) + " (non-virtual design)"
					: "projected heap exceeds " + CAP_MB + " MB cap";
			System.out.println(new MatrixRow(spec, scale).set(MatrixRow.RESULT, "NOT_RUN").note(why).line());
			return;
		}
		System.out.println("PLAN\t" + spec.concept() + "\t" + spec.widgetClass() + "\t" + scale + "\t" + xmx);
	}
}
