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
 * Integer geometry of planes: controls whose logical bounds leave the coordinate range of native
 * child windows (docs/m3/SCROLL_MEDIATED_VIEWPORT.md). Every method works on one axis.
 * <p>
 * A native child window keeps its position in [-32768, 32767] and its size in [0, 65535] pixels
 * (measured on win32; GTK3 on X11 has the same 16-bit window protocol). A control larger than
 * {@link #BAND} becomes a plane: its native window covers only the band <code>[base, base + BAND)</code>
 * of its logical extent, chosen around the part its parent can show. A child of a plane is placed
 * natively at <code>logical - base</code>; a child that then falls outside the native range is clamped,
 * which keeps it invisible because the band is shorter than the clamp distance.
 * </p>
 * Pure arithmetic: no native calls and no allocation.
 */
final class ViewportPlaneGeometry {
	/** Largest native window coordinate; the smallest is <code>-LIMIT - 1</code>. */
	static final int LIMIT = 32767;
	/** Native extent of a plane: well inside the native range on both sides. */
	static final int BAND = 24576;
	/**
	 * Extent of the parent's client area a plane always keeps inside its band, whatever the parent's
	 * current size, so that a parent resize never exposes the end of a band (half the band leaves a
	 * margin for hysteresis).
	 */
	static final int VISIBLE_CAP = BAND / 2;

	private ViewportPlaneGeometry () {
	}

	/** True when a native window at <code>position</code> with <code>size</code> represents these values exactly. */
	static boolean fits (int position, int size) {
		return position >= -LIMIT - 1 && position <= LIMIT && size <= BAND;
	}

	/** True when a position alone is representable natively. */
	static boolean fitsPosition (long position) {
		return position >= -LIMIT - 1 && position <= LIMIT;
	}

	/** Native extent of a control of logical <code>size</code>: the size itself, or the band for a plane. */
	static int nativeExtent (int size) {
		return Math.min (size, BAND);
	}

	/** Clamps a native coordinate into the native range. */
	static int clamp (long value) {
		return (int) Math.max (-LIMIT - 1, Math.min (LIMIT, value));
	}

	/**
	 * Native position of a control: its logical position plus its own base, relative to the parent's base,
	 * clamped into the native range.
	 */
	static int nativePosition (int position, int base, int parentBase) {
		return clamp ((long) position + base - parentBase);
	}

	/**
	 * The base of a plane on one axis.
	 *
	 * @param position logical position of the plane in its parent's logical client coordinates
	 * @param size logical size of the plane
	 * @param parentBase the parent's base on this axis (0 when the parent is not a plane)
	 * @param visible extent of the parent's native client area (at least {@link #VISIBLE_CAP} is kept in the band)
	 * @param base the current base (kept while the visible part stays inside its band)
	 * @return the base, in <code>[0, size - BAND]</code>, or 0 when the control is not larger than the band
	 */
	static int base (int position, int size, int parentBase, int visible, int base) {
		if (size <= BAND) return 0;
		long maxBase = (long) size - BAND;
		long low = Math.max (0L, (long) parentBase - position);
		/* at least VISIBLE_CAP: the parent may grow without moving the plane, and must still find its band */
		long high = Math.min ((long) size, (long) parentBase - position + Math.min (Math.max (visible, VISIBLE_CAP), BAND));
		if (high <= low) {
			/* nothing of the plane is visible: keep the base unless it is out of range */
			return (int) Math.max (0L, Math.min (maxBase, base));
		}
		if (base >= 0 && base <= maxBase && low >= base && high <= (long) base + BAND) return base;
		long centred = low - (BAND - (high - low)) / 2;
		return (int) Math.max (0L, Math.min (maxBase, centred));
	}
}
