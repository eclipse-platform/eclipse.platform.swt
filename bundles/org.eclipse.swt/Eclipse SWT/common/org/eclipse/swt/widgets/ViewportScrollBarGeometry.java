/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

/**
 * Geometry of a scroll bar that SWT draws itself (the <code>custom</code> and <code>m3</code> widget
 * modes), equal to the geometry of the native win32 scroll bar for the same values
 * (docs/m3/SCROLLBAR_GEOMETRY.md, measured by <code>ScrollBarGeometryOracle</code>).
 * <p>
 * All methods work on one axis, in pixels, relative to the start of the track (the shaft between the
 * arrow buttons), with the values of {@link ScrollBar}: <code>minimum</code>, <code>maximum</code>,
 * <code>thumb</code> and <code>selection</code>. The native bar receives them as
 * <code>nMin = minimum</code>, <code>nMax = maximum</code>, <code>nPage = thumb + 1</code>.
 * Measured rules (win32, 100 %):
 * </p>
 * <ul>
 * <li>thumb length = <code>MulDiv (track, thumb + 1, maximum - minimum + 1)</code>, at least
 * <code>minThumb</code> (8 px at 100 %);</li>
 * <li>no thumb is shown when the track is shorter than <code>minThumb</code>;</li>
 * <li>thumb start = <code>MulDiv (track - length, selection - minimum, maximum - thumb - minimum)</code>;
 * when the bar cannot scroll (<code>maximum - thumb &lt;= minimum</code>) the thumb starts one pixel before
 * the track;</li>
 * <li>a disabled bar keeps the same geometry.</li>
 * </ul>
 * <code>MulDiv</code> is <code>a * b / c</code> rounded half away from zero. Pure integer arithmetic,
 * no allocation.
 */
final class ViewportScrollBarGeometry {
	/** Parts of a scroll bar, answered by {@link #hit}. */
	static final int NONE = 0, ARROW_DECREASE = 1, PAGE_DECREASE = 2, THUMB = 3, PAGE_INCREASE = 4, ARROW_INCREASE = 5;

	private ViewportScrollBarGeometry () {
	}

	/** <code>a * b / c</code> rounded half away from zero (win32 <code>MulDiv</code>); 0 when <code>c</code> is 0. */
	static int mulDiv (int a, int b, int c) {
		if (c == 0) return 0;
		long product = (long) a * b;
		long quotient = Math.abs (product) / Math.abs ((long) c), remainder = Math.abs (product) % Math.abs ((long) c);
		if (2 * remainder >= Math.abs ((long) c)) quotient++;
		return (int) ((product < 0) == (c < 0) ? quotient : -quotient);
	}

	/** True when a thumb is shown in a track of this length. */
	static boolean hasThumb (int track, int minThumb) {
		return track >= minThumb;
	}

	/** Length of the thumb, or 0 when no thumb is shown. */
	static int thumbLength (int track, int minThumb, int minimum, int maximum, int thumb) {
		if (!hasThumb (track, minThumb)) return 0;
		return Math.max (minThumb, mulDiv (track, thumb + 1, maximum - minimum + 1));
	}

	/** Start of the thumb relative to the track start (meaningless when {@link #thumbLength} is 0). */
	static int thumbStart (int track, int minThumb, int minimum, int maximum, int thumb, int selection) {
		int length = thumbLength (track, minThumb, minimum, maximum, thumb);
		int last = maximum - thumb;
		if (last <= minimum) return -1;
		return mulDiv (track - length, selection - minimum, last - minimum);
	}

	/**
	 * The selection for a thumb dragged to <code>start</code> (relative to the track start), clamped to
	 * <code>[minimum, maximum - thumb]</code>: the inverse of {@link #thumbStart} for the same rounding.
	 */
	static int selectionAt (int start, int track, int minThumb, int minimum, int maximum, int thumb) {
		int last = maximum - thumb;
		if (last <= minimum) return minimum;
		int free = track - thumbLength (track, minThumb, minimum, maximum, thumb);
		if (free <= 0) return minimum;
		int clamped = Math.max (0, Math.min (free, start));
		return minimum + mulDiv (clamped, last - minimum, free);
	}

	/**
	 * The part of the bar at <code>position</code>, measured from the start of the bar (the decrease arrow).
	 *
	 * @param arrow length of each arrow button
	 * @param track length of the track between the arrows
	 */
	static int hit (int position, int arrow, int track, int minThumb, int minimum, int maximum, int thumb, int selection) {
		if (position < 0 || position >= 2 * arrow + track) return NONE;
		if (position < arrow) return ARROW_DECREASE;
		if (position >= arrow + track) return ARROW_INCREASE;
		int length = thumbLength (track, minThumb, minimum, maximum, thumb);
		if (length == 0) return NONE;
		int start = thumbStart (track, minThumb, minimum, maximum, thumb, selection), offset = position - arrow;
		if (offset < start) return PAGE_DECREASE;
		if (offset < start + length) return THUMB;
		return PAGE_INCREASE;
	}
}
