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
 *     Hitesh Soliwal - initial API and implementation; contract adapted from
 *         Synexia SwtSkijaDirtyRegionPlanner (Apache-2.0, hsoliwal/com.synexia@af4494b1432),
 *         see docs/licenses/swt-gpu-retained-planes-NOTICE.txt
 *******************************************************************************/
package org.eclipse.swt.internal.gpu;

import org.eclipse.swt.*;

/**
 * Reusable dirty-rectangle list in a packed {@code int[]} of
 * {@code (x0, y0, x1, y1)} quads, half-open. Adding never allocates once the
 * list has reached its bounded capacity; a full list collapses to its bounds.
 */
public final class RectList {
	private final int maxRects;
	private final int [] r;
	private int count;

	public RectList (int maxRects) {
		if (maxRects <= 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		this.maxRects = maxRects;
		r = new int [(maxRects + 1) * 4];
	}

	public int count () {
		return count;
	}

	public int x0 (int i) { return r [i * 4]; }
	public int y0 (int i) { return r [i * 4 + 1]; }
	public int x1 (int i) { return r [i * 4 + 2]; }
	public int y1 (int i) { return r [i * 4 + 3]; }

	public void clear () {
		count = 0;
	}

	/** Adds a half-open rectangle; touching or overlapping rectangles merge transitively. */
	public void add (int ax0, int ay0, int ax1, int ay1) {
		if (ax0 >= ax1 || ay0 >= ay1) return;
		for (int i = 0; i < count;) {
			int o = i * 4;
			if (r [o] <= ax1 && ax0 <= r [o + 2] && r [o + 1] <= ay1 && ay0 <= r [o + 3]) {
				ax0 = Math.min (ax0, r [o]);
				ay0 = Math.min (ay0, r [o + 1]);
				ax1 = Math.max (ax1, r [o + 2]);
				ay1 = Math.max (ay1, r [o + 3]);
				removeAt (i);
				i = 0;
			} else {
				i++;
			}
		}
		int o = count * 4;
		r [o] = ax0;
		r [o + 1] = ay0;
		r [o + 2] = ax1;
		r [o + 3] = ay1;
		count++;
		if (count > maxRects) collapse ();
	}

	/** Promotes to one bounding region when the summed area reaches {@code percent} of the bounds area. */
	public void promoteByCoverage (int percent) {
		if (count <= 1) return;
		long sum = 0;
		for (int i = 0; i < count; i++) sum += (long) (x1 (i) - x0 (i)) * (y1 (i) - y0 (i));
		bounds ();
		if (sum * 100 >= (long) (bx1 - bx0) * (by1 - by0) * percent) collapse ();
	}

	private int bx0, by0, bx1, by1;

	private void bounds () {
		bx0 = Integer.MAX_VALUE; by0 = Integer.MAX_VALUE; bx1 = Integer.MIN_VALUE; by1 = Integer.MIN_VALUE;
		for (int i = 0; i < count; i++) {
			bx0 = Math.min (bx0, x0 (i)); by0 = Math.min (by0, y0 (i));
			bx1 = Math.max (bx1, x1 (i)); by1 = Math.max (by1, y1 (i));
		}
	}

	private void collapse () {
		bounds ();
		r [0] = bx0; r [1] = by0; r [2] = bx1; r [3] = by1;
		count = 1;
	}

	private void removeAt (int i) {
		int last = (count - 1) * 4, o = i * 4;
		r [o] = r [last]; r [o + 1] = r [last + 1]; r [o + 2] = r [last + 2]; r [o + 3] = r [last + 3];
		count--;
	}
}
