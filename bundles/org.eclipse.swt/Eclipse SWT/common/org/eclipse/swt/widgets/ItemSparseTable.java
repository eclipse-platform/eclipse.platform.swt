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

import java.util.Arrays;

import org.eclipse.swt.internal.PackedHeapGrowth;

/**
 * Sorted sparse {@code int item -> Object} table of {@link ItemResidency} (pinned handles,
 * item data, keyed data). Parent-owned and always resident; keys follow their items across
 * insert and remove, so a value keeps its identity for the life of its item.
 */
final class ItemSparseTable {
	int [] keys = new int [8];
	Object [] values = new Object [8];
	int size;

	int find (int item) {
		int lo = 0, hi = size - 1;
		while (lo <= hi) {
			int mid = (lo + hi) >>> 1;
			int k = keys [mid];
			if (k < item) lo = mid + 1;
			else if (k > item) hi = mid - 1;
			else return mid;
		}
		return -(lo + 1);
	}

	Object get (int item) {
		int i = find (item);
		return i < 0 ? null : values [i];
	}

	/** Stores {@code value}; null removes. Returns the previous value. */
	Object put (int item, Object value) {
		int i = find (item);
		if (i >= 0) {
			Object old = values [i];
			if (value == null) removeAt (i);
			else values [i] = value;
			return old;
		}
		if (value == null) return null;
		i = -i - 1;
		if (size == keys.length) {
			int next = PackedHeapGrowth.capacity (size, size + 1, 4);
			keys = Arrays.copyOf (keys, next);
			values = Arrays.copyOf (values, next);
		}
		System.arraycopy (keys, i, keys, i + 1, size - i);
		System.arraycopy (values, i, values, i + 1, size - i);
		keys [i] = item;
		values [i] = value;
		size++;
		return null;
	}

	private void removeAt (int i) {
		System.arraycopy (keys, i + 1, keys, i, size - i - 1);
		System.arraycopy (values, i + 1, values, i, size - i - 1);
		size--;
		values [size] = null;
	}

	/** Adds {@code delta} to every key at or above {@code from}; returns the first moved position. */
	int shift (int from, int delta) {
		int i = find (from);
		if (i < 0) i = -i - 1;
		for (int j = i; j < size; j++) {
			keys [j] += delta;
		}
		return i;
	}

	/** Drops every key at or above {@code from}; returns how many were dropped. */
	int truncate (int from) {
		int i = find (from);
		if (i < 0) i = -i - 1;
		int dropped = size - i;
		Arrays.fill (values, i, size, null);
		size = i;
		return dropped;
	}
}
