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
 *
 * <p>Lazy lane (additive): slots may also enter {@link #UNMEASURED} through
 * {@link #insertUnmeasured(int, int)}, {@link #setUnmeasured(int)} and
 * {@link #setAllUnmeasured(int)}; nothing is measured until a caller asks.
 * {@link #measure(int, String)} measures one slot (a painted row),
 * {@link #measurePending(IntFunction)} measures every pending slot (an exact
 * size query), and {@link #maxMeasured()} answers from measured slots only.
 * {@link #maxWidth()} requires that no slot is pending. A lane whose every
 * slot is unmeasured is kept as a count only. Lazy measurements occupy a sorted
 * primitive row-ID/width directory, never a logical-count-sized array. Existing
 * dense eager input remains dense. Only an explicit measurePending full-size
 * query materializes all slots; it still measures every pending row in order.</p>
 */
final class VirtualListWidth {
	static final int UNMEASURED = -2;
	private static final int STALE = -1;
	private final ToIntFunction<String> measure;
	private int [] widths = new int [0];
	private int count;
	private int max;
	private int maxCount;
	private int pending;
	/* uniform denotes implicit UNMEASURED defaults, with sparse measured exceptions. */
	private boolean uniform;
	private int [] measuredRows = new int [0];
	private int measuredSize;

	VirtualListWidth (ToIntFunction<String> measure) {
		this.measure = Objects.requireNonNull (measure, "measure");
	}

	int count () {
		return count;
	}

	int widthOf (int index) {
		checkIndex (index);
		if (uniform) {
			int at = sparsePosition (index);
			return at < 0 ? UNMEASURED : widths [at];
		}
		return widths [index];
	}

	void insert (int index, String string) {
		if (index < 0 || index > count) throw new IllegalArgumentException ("invalid index");
		int width = measureOf (string);
		int next = Math.addExact (count, 1);
		if (uniform) {
			ensureSparse (Math.addExact (measuredSize, 1));
			int at = sparseLowerBound (index);
			for (int i = at; i < measuredSize; i++) measuredRows [i]++;
			insertSparse (at, index, width);
			count = next;
			track (width);
			return;
		}
		ensureCapacity (next);
		System.arraycopy (widths, index, widths, index + 1, count - index);
		widths [index] = width;
		count++;
		track (width);
	}

	void set (int index, String string) {
		checkIndex (index);
		int width = measureOf (string);
		if (uniform) {
			setSparse (index, width);
			return;
		}
		int old = widths [index];
		widths [index] = width;
		if (old == UNMEASURED) {
			pending--;
			track (width);
			return;
		}
		if (old == width) return;
		untrack (old);
		track (width);
	}

	void remove (int start, int removed) {
		if (removed < 0 || start < 0 || start > count - removed) throw new IllegalArgumentException ("invalid range");
		if (removed == 0) return;
		if (uniform) {
			int left = sparseLowerBound (start), right = sparseLowerBound (start + removed);
			for (int i = left; i < right; i++) untrack (widths [i]);
			System.arraycopy (measuredRows, right, measuredRows, left, measuredSize - right);
			System.arraycopy (widths, right, widths, left, measuredSize - right);
			measuredSize -= right - left;
			for (int i = left; i < measuredSize; i++) measuredRows [i] -= removed;
			count -= removed;
			pending -= removed - (right - left);
			if (count == 0) reset ();
			return;
		}
		for (int i = start; i < start + removed; i++) {
			if (widths [i] == UNMEASURED) pending--;
			else untrack (widths [i]);
		}
		System.arraycopy (widths, start + removed, widths, start, count - start - removed);
		count -= removed;
		if (count == 0) reset ();
	}

	void clear () {
		count = 0;
		uniform = false;
		measuredRows = new int [0];
		measuredSize = 0;
		reset ();
	}

	void setAll (String [] items) {
		int [] measured = new int [items.length];
		for (int i = 0; i < items.length; i++) measured [i] = measureOf (items [i]);
		widths = measured;
		measuredRows = new int [0];
		measuredSize = 0;
		count = measured.length;
		pending = 0;
		uniform = false;
		if (count == 0) reset ();
		else maxCount = STALE;
	}

	/** Width of the widest item, or 0 for an empty list; every slot must be measured. */
	int maxWidth () {
		if (pending != 0) throw new IllegalStateException ("unmeasured slots");
		return maxMeasured ();
	}

	/** Width of the widest measured item, 0 when no slot is measured; never measures. */
	int maxMeasured () {
		if (count == 0) return 0;
		if (maxCount == STALE) rebuild ();
		return max;
	}

	/** Whether the next {@link #maxWidth()} must scan the width lane. */
	boolean isStale () {
		return count != 0 && maxCount == STALE;
	}

	/** Number of slots still {@link #UNMEASURED}. */
	int pendingCount () {
		return pending;
	}

	/** Inserts {@code inserted} unmeasured slots at {@code index}. */
	void insertUnmeasured (int index, int inserted) {
		if (index < 0 || index > count || inserted < 0) throw new IllegalArgumentException ("invalid index");
		if (inserted == 0) return;
		int next = Math.addExact (count, inserted);
		if (uniform || count == 0) {
			if (count == 0) {
				widths = new int [0];
				measuredRows = new int [0];
				measuredSize = 0;
			}
			for (int i = sparseLowerBound (index); i < measuredSize; i++) measuredRows [i] += inserted;
			uniform = true;
			count = next;
			pending += inserted;
			return;
		}
		ensureCapacity (next);
		System.arraycopy (widths, index, widths, index + inserted, count - index);
		Arrays.fill (widths, index, index + inserted, UNMEASURED);
		count += inserted;
		pending += inserted;
	}

	/** Forgets the width of one slot; it is measured again on demand. */
	void setUnmeasured (int index) {
		checkIndex (index);
		if (uniform) {
			int at = sparsePosition (index);
			if (at < 0) return;
			int old = widths [at];
			System.arraycopy (measuredRows, at + 1, measuredRows, at, measuredSize - at - 1);
			System.arraycopy (widths, at + 1, widths, at, measuredSize - at - 1);
			measuredSize--;
			pending++;
			untrack (old);
			return;
		}
		int old = widths [index];
		if (old == UNMEASURED) return;
		widths [index] = UNMEASURED;
		pending++;
		untrack (old);
	}

	/** Replaces the lane with {@code newCount} unmeasured slots. */
	void setAllUnmeasured (int newCount) {
		if (newCount < 0) throw new IllegalArgumentException ("negative count");
		widths = new int [0];
		measuredRows = new int [0];
		measuredSize = 0;
		uniform = newCount != 0;
		count = newCount;
		pending = newCount;
		reset ();
	}

	/** Measures one slot if it is pending and returns its width. */
	int measure (int index, String string) {
		checkIndex (index);
		if (uniform) {
			int at = sparsePosition (index);
			if (at >= 0) return widths [at];
			int width = measureOf (string);
			setSparse (index, width);
			return width;
		}
		int width = widths [index];
		if (width != UNMEASURED) return width;
		width = measureOf (string);
		widths [index] = width;
		pending--;
		track (width);
		return width;
	}

	/** Measures every pending slot; {@code textAt} supplies the text of a slot. */
	void measurePending (IntFunction<String> textAt) {
		if (pending == 0) return;
		materialize ();
		for (int i = 0; i < count && pending != 0; i++) {
			if (widths [i] == UNMEASURED) measure (i, textAt.apply (i));
		}
	}

	/* Explicit whole-size query only: preserve sparse measurements when becoming dense. */
	private void materialize () {
		if (!uniform) return;
		int [] next = new int [count];
		Arrays.fill (next, UNMEASURED);
		for (int i = 0; i < measuredSize; i++) next [measuredRows [i]] = widths [i];
		widths = next;
		measuredRows = new int [0];
		measuredSize = 0;
		uniform = false;
	}

	private int sparsePosition (int index) {
		return Arrays.binarySearch (measuredRows, 0, measuredSize, index);
	}

	private int sparseLowerBound (int index) {
		int at = sparsePosition (index);
		return at < 0 ? -at - 1 : at;
	}

	private void ensureSparse (int needed) {
		if (needed <= widths.length) return;
		int capacity = Math.max (needed, VirtualListModel.growth.applyAsInt (widths.length, needed));
		int [] nextRows = Arrays.copyOf (measuredRows, capacity);
		int [] nextWidths = Arrays.copyOf (widths, capacity);
		measuredRows = nextRows;
		widths = nextWidths;
	}

	private void insertSparse (int at, int index, int width) {
		System.arraycopy (measuredRows, at, measuredRows, at + 1, measuredSize - at);
		System.arraycopy (widths, at, widths, at + 1, measuredSize - at);
		measuredRows [at] = index;
		widths [at] = width;
		measuredSize++;
	}

	private void setSparse (int index, int width) {
		int at = sparsePosition (index);
		if (at >= 0) {
			int old = widths [at];
			widths [at] = width;
			if (old == width) return;
			untrack (old);
		} else {
			ensureSparse (Math.addExact (measuredSize, 1));
			insertSparse (-at - 1, index, width);
			pending--;
		}
		track (width);
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
		for (int i = 0, limit = uniform ? measuredSize : count; i < limit; i++) {
			int width = widths [i];
			if (width == UNMEASURED) continue;
			if (width > best || holders == 0) {
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
		if (count == 0) pending = 0;
	}

	private void ensureCapacity (int needed) {
		if (needed <= widths.length) return;
		widths = Arrays.copyOf (widths, Math.max (needed, VirtualListModel.growth.applyAsInt (widths.length, needed)));
	}

	private void checkIndex (int index) {
		if (index < 0 || index >= count) throw new IllegalArgumentException ("invalid index");
	}
}
