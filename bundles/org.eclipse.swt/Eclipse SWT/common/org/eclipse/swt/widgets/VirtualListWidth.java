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

import java.util.*;
import java.util.function.*;

/**
 * Incremental widest-item index for a flat list.
 *
 * <p>Each item is measured exactly once, when it enters the index; the width is
 * kept in a parallel {@code int} lane. {@link #maxWidth()} is O(1) while the
 * maximum is intact. Removing the last item that holds the maximum marks the
 * index stale, and the next query rebuilds it by scanning the {@code int} lane
 * only, never by measuring text again. The measure function is the only
 * platform-dependent part (for example a native text-extent call).</p>
 */
final class VirtualListWidth {
	private static final int STALE = -1;
	private final ToIntFunction<String> measure;
	private int [] widths = new int [0];
	private int count;
	private int max;
	private int maxCount;

	VirtualListWidth (ToIntFunction<String> measure) {
		this.measure = Objects.requireNonNull (measure, "measure");
	}

	int count () {
		return count;
	}

	int widthOf (int index) {
		checkIndex (index);
		return widths [index];
	}

	void insert (int index, String string) {
		if (index < 0 || index > count) throw new IllegalArgumentException ("invalid index");
		int width = measureOf (string);
		ensureCapacity (count + 1);
		System.arraycopy (widths, index, widths, index + 1, count - index);
		widths [index] = width;
		count++;
		track (width);
	}

	void set (int index, String string) {
		checkIndex (index);
		int width = measureOf (string);
		int old = widths [index];
		widths [index] = width;
		if (old == width) return;
		untrack (old);
		track (width);
	}

	void remove (int start, int removed) {
		if (removed < 0 || start < 0 || start > count - removed) throw new IllegalArgumentException ("invalid range");
		if (removed == 0) return;
		for (int i = start; i < start + removed; i++) untrack (widths [i]);
		System.arraycopy (widths, start + removed, widths, start, count - start - removed);
		count -= removed;
		if (count == 0) reset ();
	}

	void clear () {
		count = 0;
		reset ();
	}

	void setAll (String [] items) {
		int [] measured = new int [items.length];
		for (int i = 0; i < items.length; i++) measured [i] = measureOf (items [i]);
		widths = measured;
		count = measured.length;
		if (count == 0) reset ();
		else maxCount = STALE;
	}

	/** Width of the widest item, or 0 for an empty list. */
	int maxWidth () {
		if (count == 0) return 0;
		if (maxCount == STALE) rebuild ();
		return max;
	}

	/** Whether the next {@link #maxWidth()} must scan the width lane. */
	boolean isStale () {
		return count != 0 && maxCount == STALE;
	}

	private int measureOf (String string) {
		if (string == null) throw new IllegalArgumentException ("null item");
		int width = measure.applyAsInt (string);
		if (width < 0) throw new IllegalArgumentException ("negative width");
		return width;
	}

	private void track (int width) {
		if (maxCount == STALE) return;
		if (maxCount == 0 || width > max) {
			max = width;
			maxCount = 1;
		} else if (width == max) {
			maxCount++;
		}
	}

	private void untrack (int width) {
		if (maxCount == STALE || width != max) return;
		if (--maxCount == 0) maxCount = STALE;
	}

	private void rebuild () {
		int best = 0;
		int holders = 0;
		for (int i = 0; i < count; i++) {
			int width = widths [i];
			if (width > best) {
				best = width;
				holders = 1;
			} else if (width == best) {
				holders++;
			}
		}
		max = best;
		maxCount = holders;
	}

	private void reset () {
		max = 0;
		maxCount = 0;
	}

	private void ensureCapacity (int needed) {
		if (needed <= widths.length) return;
		int grown = Math.max (needed, widths.length + (widths.length >> 1) + 8);
		widths = Arrays.copyOf (widths, grown);
	}

	private void checkIndex (int index) {
		if (index < 0 || index >= count) throw new IllegalArgumentException ("invalid index");
	}
}
