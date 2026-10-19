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
 * Native window of a flat viewport (a list, or the rows of a table): which logical rows the
 * native control holds and how a window move edits it.
 *
 * <p>This is the depth-1 case of {@link VirtualTreeResidentPlan}. A flat native control holds
 * one interval of logical rows, <code>[first, first + size)</code>, at native indices
 * <code>0 .. size - 1</code>; a row is addressed by its native index, so no handle maps are
 * needed. Moving to the next interval deletes at most the rows that left at the head and at the
 * tail and inserts at most the rows that entered at the head and at the tail; disjoint
 * intervals are replaced. Planning is O(1) arithmetic and allocates nothing.</p>
 *
 * <p>Logical inserts and removes made while a window is committed are reported with
 * {@link #inserted(int, int)} and {@link #removed(int, int)}, which keep the committed
 * interval aligned with the logical rows and answer the native rows to edit.</p>
 */
final class VirtualRowWindowPlan {
	private int first, size;
	private int plannedFirst, plannedSize;
	private int retiredHead, retiredTail, insertedHead, insertedTail;
	private boolean replaced;

	/** First logical row of the committed window. */
	int first () {
		return first;
	}

	/** Number of native rows of the committed window. */
	int size () {
		return size;
	}

	/** Logical row after the committed window. */
	int end () {
		return first + size;
	}

	/** Native index of a logical row, or -1 when the row is not resident. */
	int entryOf (int row) {
		return row >= first && row < first + size ? row - first : -1;
	}

	/** Logical row of a native index of the committed window. */
	int rowOf (int entry) {
		if (entry < 0 || entry >= size) throw new IllegalArgumentException ("resident entry");
		return first + entry;
	}

	/** Whether the committed window holds every row of <code>[from, to)</code>. */
	boolean covers (int from, int to) {
		return from >= first && to <= first + size;
	}

	/**
	 * Plans a move to the window <code>[nextFirst, nextFirst + nextSize)</code>. Afterwards
	 * {@link #retiredHead()} and {@link #retiredTail()} are the native rows to delete at the
	 * start and at the end (in that order), and {@link #insertedHead()} and
	 * {@link #insertedTail()} are the logical rows to insert at native index 0 (rows
	 * <code>nextFirst ..</code>) and to append (rows <code>.. nextFirst + nextSize</code>).
	 */
	void plan (int nextFirst, int nextSize) {
		if (nextFirst < 0 || nextSize < 0 || nextFirst > Integer.MAX_VALUE - nextSize) throw new IllegalArgumentException ("window");
		plannedFirst = nextFirst;
		plannedSize = nextSize;
		int end = first + size, nextEnd = nextFirst + nextSize;
		replaced = size == 0 || nextSize == 0 || nextEnd <= first || nextFirst >= end;
		if (replaced) {
			retiredHead = size;
			retiredTail = 0;
			insertedHead = nextSize;
			insertedTail = 0;
			return;
		}
		retiredHead = Math.max (0, nextFirst - first);
		retiredTail = Math.max (0, end - nextEnd);
		insertedHead = Math.max (0, first - nextFirst);
		insertedTail = Math.max (0, nextEnd - end);
	}

	/** Whether the planned window shares no row with the committed one (every native row is replaced). */
	boolean replaced () {
		return replaced;
	}

	int retiredHead () {
		return retiredHead;
	}

	int retiredTail () {
		return retiredTail;
	}

	int insertedHead () {
		return insertedHead;
	}

	int insertedTail () {
		return insertedTail;
	}

	/** Native rows touched by the planned move. */
	int touched () {
		return retiredHead + retiredTail + insertedHead + insertedTail;
	}

	int plannedFirst () {
		return plannedFirst;
	}

	int plannedSize () {
		return plannedSize;
	}

	/** Records the planned window as the committed one. */
	void commit () {
		first = plannedFirst;
		size = plannedSize;
	}

	/**
	 * Logical rows <code>[row, row + count)</code> were inserted.
	 * @return the native index at which the caller inserts them (they become resident), or -1
	 * when they are outside the window (a window behind them moves down)
	 */
	int inserted (int row, int count) {
		if (row < 0 || count < 0) throw new IllegalArgumentException ("insert");
		if (count == 0) return -1;
		if (row < first || (row == first && size == 0)) {
			first += count;
			return -1;
		}
		if (row < first + size) {
			size += count;
			return row - first;
		}
		return -1;
	}

	/**
	 * Logical rows <code>[row, row + count)</code> were removed.
	 * @return the number of resident rows among them; the caller deletes that many native rows
	 * at {@link #removedEntry(int)} of the same row
	 */
	int removed (int row, int count) {
		if (row < 0 || count < 0) throw new IllegalArgumentException ("remove");
		int end = row + count;
		int before = Math.max (0, Math.min (end, first) - row);
		int resident = Math.max (0, Math.min (end, first + size) - Math.max (row, first));
		first -= before;
		size -= resident;
		return resident;
	}

	/** Native index of the first resident row of a removal that starts at <code>row</code> (after {@link #removed(int, int)}). */
	int removedEntry (int row) {
		return Math.max (0, row - first);
	}

	/** Forgets the committed window (the caller deleted every native row). */
	void clear () {
		first = size = plannedFirst = plannedSize = 0;
		retiredHead = retiredTail = insertedHead = insertedTail = 0;
		replaced = false;
	}
}
