/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.math.BigInteger;

/**
 * Scroll geometry derived from logical model cardinality, one representative
 * row extent and the current body viewport.
 *
 * <p>Logical row coordinates use {@code long}. SWT/native scrollbar controls
 * still expose integer ranges, so very large logical models are projected onto
 * that integer range while preserving exact row coordinates in the model.</p>
 */
final class VirtualScrollMetrics {
	private long logicalRows;
	private int sampleRowExtent = 1;
	private int viewportExtent;

	void configure (int logicalRows, int sampleRowExtent, int viewportExtent) {
		configure ((long)logicalRows, sampleRowExtent, viewportExtent);
	}

	void configure (long logicalRows, int sampleRowExtent, int viewportExtent) {
        if (logicalRows < 0) {
            throw new IllegalArgumentException("negative logical rows");
        }
        if (sampleRowExtent <= 0) {
            throw new IllegalArgumentException("non-positive sample row extent");
        }
        if (viewportExtent < 0) {
            throw new IllegalArgumentException("negative viewport extent");
        }
		this.logicalRows = logicalRows;
		this.sampleRowExtent = sampleRowExtent;
		this.viewportExtent = viewportExtent;
	}

	int logicalRows () {
		return (int)Math.min (Integer.MAX_VALUE, logicalRows);
	}

	long logicalRowCount () {
		return logicalRows;
	}

	int sampleRowExtent () {
		return sampleRowExtent;
	}

	int viewportExtent () {
		return viewportExtent;
	}

	long estimatedContentExtent () {
		return Math.multiplyExact (logicalRows, sampleRowExtent);
	}

	int visibleRows () {
        if (logicalRows == 0 || viewportExtent == 0) {
            return 0;
        }
		long rows = ((long)viewportExtent + sampleRowExtent - 1L) / sampleRowExtent;
		return (int)Math.min (logicalRows, Math.max (1L, rows));
	}

	boolean scrollbarNeeded () {
		return logicalRows > visibleRows ();
	}

	int minimum () {
		return 0;
	}

	int maximum () {
		return (int)Math.min (Integer.MAX_VALUE, logicalRows);
	}

	int thumb () {
        if (logicalRows == 0) {
            return 0;
        }
		int visible = Math.max (1, visibleRows ());
		int maximum = maximum ();
        if (logicalRows <= Integer.MAX_VALUE) {
            return Math.min(maximum, visible);
        }
		long product = (long)visible * maximum; // two positive int values: cannot overflow
		long scaled = 1L + (product - 1L) / logicalRows;
		return (int)Math.max (1L, Math.min (maximum, scaled));
	}

	int increment () {
		return logicalRows == 0 ? 0 : 1;
	}

	int pageIncrement () {
		return Math.max (1, thumb ());
	}

	long maxTopRow () {
		return Math.max (0L, logicalRows - visibleRows ());
	}

	int clampTopRow (int requested) {
		return (int)Math.min (Integer.MAX_VALUE, clampTopRow ((long)requested));
	}

	long clampTopRow (long requested) {
		return Math.max (0L, Math.min (requested, maxTopRow ()));
	}

	int selectionForTopRow (long topRow) {
		long clamped = clampTopRow (topRow);
		long maxTop = maxTopRow ();
		int maxSelection = Math.max (0, maximum () - thumb ());
        if (clamped == 0 || maxTop == 0 || maxSelection == 0) {
            return 0;
        }
        if (logicalRows <= Integer.MAX_VALUE) {
            return (int) clamped;
        }
		return (int)roundedScale (clamped, maxTop, maxSelection);
	}

	long topRowForSelection (int selection) {
		int maxSelection = Math.max (0, maximum () - thumb ());
		int clamped = Math.max (0, Math.min (selection, maxSelection));
		long maxTop = maxTopRow ();
        if (clamped == 0 || maxSelection == 0 || maxTop == 0) {
            return 0;
        }
        if (logicalRows <= Integer.MAX_VALUE) {
            return clamped;
        }
		return roundedScale (clamped, maxSelection, maxTop);
	}

	/**
	 * Exact row/selection scale even for coordinates past 2^53. Use primitive
	 * arithmetic for ordinary ranges; a single BigInteger product is needed
	 * only for giant logical models, never for drawing or pointer events.
	 */
	private static long roundedScale (long value, long from, long to) {
		if (value <= 0 || from <= 0 || to <= 0) return 0;
		if (value >= from) return to;
		if (to <= (Long.MAX_VALUE - from / 2) / value) {
			return (value * to + from / 2) / from;
		}
		return BigInteger.valueOf (value).multiply (BigInteger.valueOf (to))
				.add (BigInteger.valueOf (from / 2))
				.divide (BigInteger.valueOf (from)).longValueExact ();
	}
}
