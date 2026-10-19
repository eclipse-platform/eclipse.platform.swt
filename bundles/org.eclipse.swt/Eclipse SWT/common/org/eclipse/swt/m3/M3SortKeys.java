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
package org.eclipse.swt.m3;

import java.util.*;

import org.eclipse.swt.*;

/**
 * Collation sort keys of a set of items, built by {@link M3Collation} (enhanced M3 language API).
 * <p>
 * The keys of all items live in one byte lane with an offset lane; comparing two items is an
 * unsigned byte-range compare with no allocation. Instances are immutable.
 * </p>
 *
 * @since 3.138
 */
public final class M3SortKeys {

	private final byte [] lane;
	private final int [] offsets;

	M3SortKeys (byte [] lane, int [] offsets) {
		this.lane = lane;
		this.offsets = offsets;
	}

	/**
	 * Returns the number of items.
	 *
	 * @return the size
	 */
	public int size () {
		return offsets.length - 1;
	}

	/**
	 * Compares the keys of two items: negative, zero or positive as item <code>i</code> sorts
	 * before, with or after item <code>j</code> (the sign of <code>Collator.compare</code>).
	 *
	 * @param i the first item
	 * @param j the second item
	 * @return the comparison
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if an index is not an item</li>
	 * </ul>
	 */
	public int compare (int i, int j) {
		check (i);
		check (j);
		return Arrays.compareUnsigned (lane, offsets [i], offsets [i + 1], lane, offsets [j], offsets [j + 1]);
	}

	/**
	 * Returns the item indices in ascending key order; items with equal keys keep their input
	 * order (stable).
	 *
	 * @return a new permutation of <code>0 .. size() - 1</code>
	 */
	public int [] sortedOrder () {
		int n = size ();
		int [] order = new int [n], scratch = new int [n];
		for (int i = 0; i < n; i++) order [i] = i;
		/* bottom-up merge sort of the permutation: stable, no boxing */
		for (int width = 1; width < n; width *= 2) {
			for (int low = 0; low < n; low += 2 * width) {
				int middle = Math.min (low + width, n), high = Math.min (low + 2 * width, n);
				int left = low, right = middle, out = low;
				while (left < middle && right < high) {
					scratch [out++] = compareUnchecked (order [right], order [left]) < 0 ? order [right++] : order [left++];
				}
				while (left < middle) scratch [out++] = order [left++];
				while (right < high) scratch [out++] = order [right++];
			}
			int [] swap = order;
			order = scratch;
			scratch = swap;
		}
		return order;
	}

	/**
	 * Returns the length in bytes of the key of an item.
	 *
	 * @param i the item
	 * @return the key length
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_RANGE - if the index is not an item</li>
	 * </ul>
	 */
	public int keyLength (int i) {
		check (i);
		return offsets [i + 1] - offsets [i];
	}

	private int compareUnchecked (int i, int j) {
		return Arrays.compareUnsigned (lane, offsets [i], offsets [i + 1], lane, offsets [j], offsets [j + 1]);
	}

	private void check (int i) {
		if (i < 0 || i >= size ()) SWT.error (SWT.ERROR_INVALID_RANGE);
	}

	@Override
	public String toString () {
		return "M3SortKeys [" + size () + " items, " + lane.length + " key bytes]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
