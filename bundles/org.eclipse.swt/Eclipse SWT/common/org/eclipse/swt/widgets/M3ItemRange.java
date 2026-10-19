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

import java.util.*;

import org.eclipse.swt.*;

/**
 * An inclusive range <code>[start, end]</code> of item indices (enhanced M3 API).
 * <p>
 * Ranges describe rows of a <code>Table</code> or <code>List</code>, children of one
 * <code>TreeItem</code> (or the roots of a <code>Tree</code>), or visible rows of a
 * <code>Tree</code>, as each method of {@link M3ItemSelection} documents. A range is a value:
 * two <code>int</code>s, immutable, never shared with the widget. A range whose end is
 * smaller than its start is empty; {@link #EMPTY} is the canonical empty range.
 * </p>
 * <p>
 * Arrays of ranges returned by the M3 API are in <em>normal form</em>: sorted by start,
 * non-overlapping, non-adjacent and non-empty, so that {@link #countOf(M3ItemRange[])} is
 * the number of items they cover. {@link #coalesce(int[])} builds the normal form from
 * plain indices.
 * </p>
 *
 * @since 3.138
 */
public final class M3ItemRange {

	/** The empty range: start 0, end -1. */
	public static final M3ItemRange EMPTY = new M3ItemRange (0, -1);

	private static final M3ItemRange [] NO_RANGES = new M3ItemRange [0];

	private final int start;
	private final int end;

	/**
	 * Constructs a range of the indices from <code>start</code> to <code>end</code>, both
	 * included. The range is empty when <code>end &lt; start</code>.
	 *
	 * @param start the first index, not negative
	 * @param end the last index
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if start is negative</li>
	 * </ul>
	 */
	public M3ItemRange (int start, int end) {
		if (start < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		this.start = start;
		this.end = end;
	}

	/**
	 * Returns the range of the indices from <code>start</code> to <code>end</code>, both
	 * included, or {@link #EMPTY} when <code>end &lt; start</code>.
	 *
	 * @param start the first index, not negative
	 * @param end the last index
	 * @return the range
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if start is negative</li>
	 * </ul>
	 */
	public static M3ItemRange of (int start, int end) {
		if (start < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		return end < start ? EMPTY : new M3ItemRange (start, end);
	}

	/**
	 * Returns the first index of the range.
	 *
	 * @return the first index
	 */
	public int start () {
		return start;
	}

	/**
	 * Returns the last index of the range; smaller than the start when the range is empty.
	 *
	 * @return the last index
	 */
	public int end () {
		return end;
	}

	/**
	 * Returns the number of indices in the range, 0 when it is empty.
	 *
	 * @return the size
	 */
	public int size () {
		return end < start ? 0 : end - start + 1;
	}

	/**
	 * Returns whether the range holds no index.
	 *
	 * @return <code>true</code> when empty
	 */
	public boolean isEmpty () {
		return end < start;
	}

	/**
	 * Returns whether the index lies in the range.
	 *
	 * @param index the index
	 * @return <code>true</code> when <code>start &lt;= index &lt;= end</code>
	 */
	public boolean contains (int index) {
		return start <= index && index <= end;
	}

	/**
	 * Returns the indices this range and the argument have in common, {@link #EMPTY} when none.
	 *
	 * @param other the other range
	 * @return the intersection
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
	 * </ul>
	 */
	public M3ItemRange intersection (M3ItemRange other) {
		if (other == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		return of (Math.max (start, other.start), Math.min (end, other.end));
	}

	/**
	 * Returns the number of indices covered by ranges in normal form.
	 *
	 * @param ranges ranges in normal form
	 * @return the sum of their sizes
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the array or one of its elements is null</li>
	 * </ul>
	 */
	public static int countOf (M3ItemRange [] ranges) {
		if (ranges == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		int count = 0;
		for (M3ItemRange range : ranges) {
			if (range == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
			count += range.size ();
		}
		return count;
	}

	/**
	 * Builds the normal form of a set of indices: sorted, duplicates dropped, runs of
	 * consecutive indices joined into one range. The argument is not modified.
	 *
	 * @param indices the indices, in any order
	 * @return the ranges in normal form, empty when there are no indices
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if the array is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if an index is negative</li>
	 * </ul>
	 */
	public static M3ItemRange [] coalesce (int [] indices) {
		if (indices == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		return coalesce (indices, indices.length);
	}

	/* Normal form of the first <code>length</code> indices. */
	static M3ItemRange [] coalesce (int [] indices, int length) {
		if (length == 0) return NO_RANGES;
		int [] sorted = sorted (indices, length);
		if (sorted [0] < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		M3ItemRange [] ranges = new M3ItemRange [runs (sorted)];
		int run = 0, first = sorted [0];
		for (int i = 1; i <= sorted.length; i++) {
			if (i < sorted.length && sorted [i] <= sorted [i - 1] + 1) continue;
			ranges [run++] = new M3ItemRange (first, sorted [i - 1]);
			if (i < sorted.length) first = sorted [i];
		}
		return ranges;
	}

	/* The indices in ascending order: the argument itself when already sorted, else a sorted copy. */
	private static int [] sorted (int [] indices, int length) {
		boolean ascending = true;
		for (int i = 1; i < length && ascending; i++) ascending = indices [i - 1] <= indices [i];
		if (ascending && length == indices.length) return indices;
		int [] copy = Arrays.copyOf (indices, length);
		if (!ascending) Arrays.sort (copy);
		return copy;
	}

	/* Number of maximal runs of consecutive (or repeated) indices in a sorted array. */
	private static int runs (int [] sorted) {
		int runs = 1;
		for (int i = 1; i < sorted.length; i++) {
			if (sorted [i] > sorted [i - 1] + 1) runs++;
		}
		return runs;
	}

	@Override
	public boolean equals (Object object) {
		if (this == object) return true;
		if (!(object instanceof M3ItemRange)) return false;
		M3ItemRange other = (M3ItemRange) object;
		if (isEmpty () && other.isEmpty ()) return true;
		return start == other.start && end == other.end;
	}

	@Override
	public int hashCode () {
		return isEmpty () ? -1 : start * 31 + end;
	}

	@Override
	public String toString () {
		return isEmpty () ? "M3ItemRange []" : "M3ItemRange [" + start + ".." + end + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
}
