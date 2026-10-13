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

/**
 * One row of {@code superset-demos.tsv}: a widget shown under a catalog concept.
 *
 * @param concept   catalog concept (SUPERSET_WIDGET_CATALOG.tsv)
 * @param owner     {@code swt}, {@code nebula/<family>} or {@code -}
 * @param widgetClass fully qualified class name, or {@code -} for a placeholder
 * @param kind      demo kind, see {@link Demos#create(DemoSpec)}
 * @param maxScale  largest scale the demo is designed for; 0 = small content
 * @param hosts     classes exercised through this demo (items, columns, ...)
 */
public record DemoSpec(String concept, String owner, String widgetClass, String kind, int maxScale, String hosts) {

	/** Scales used for large-content demos: rows, lines, points or nodes. */
	public static final int[] SCALES = {1_000, 100_000, 1_000_000, 10_000_000};

	/** Memory-flatness scales, run under a fixed -Xmx256m for virtual-capable kinds. */
	public static final int[] FLAT_SCALES = {100_000_000, 1_000_000_000};

	static DemoSpec parse(String line) {
		String[] f = line.split("\t", -1);
		return new DemoSpec(f[0], f[1], f[2], f[3], Integer.parseInt(f[4]), f[5]);
	}

	/** @return true when the demo fills generated data at a selectable scale */
	public boolean largeContent() {
		return maxScale > 0;
	}

	/** @return true when the demo has a widget the harness can drive */
	public boolean runnable() {
		return switch (kind) {
			case "PLACEHOLDER", "NOT_APPLICABLE", "DIALOG", "MODAL", "SIDE_EFFECT", "ABSTRACT" -> false;
			default -> true;
		};
	}

	/** @return the simple class name, used for tab text and file names */
	public String simpleName() {
		int dot = widgetClass.lastIndexOf('.');
		return dot < 0 ? widgetClass : widgetClass.substring(dot + 1);
	}

	/** @return a file-name-safe key: concept__Class */
	public String key() {
		return concept + "__" + simpleName().replace('-', '_');
	}

	/** @return a display label for scale {@code n}: 1K, 100K, 1M, 10M */
	public static String scaleLabel(int n) {
		if (n <= 0) return "-";
		if (n < 1_000) return Integer.toString(n);
		if (n >= 1_000_000_000) return (n / 1_000_000_000) + "B";
		if (n >= 1_000_000) return (n / 1_000_000) + "M";
		return (n / 1_000) + "K";
	}
}
