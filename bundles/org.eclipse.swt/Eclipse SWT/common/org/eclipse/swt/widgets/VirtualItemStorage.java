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
 * Sparse identity-preserving storage for materialized entries of a virtual widget.
 *
 * <p>The logical item count is owned by the widget. This store contains only entries that have
 * actually been materialized, so a large logical model does not require a same-sized Java
 * reference array. Logical insert/remove operations shift the sparse indices while retaining the
 * identity of already materialized items.</p>
 *
 * @param <T> materialized item type
 */
final class VirtualItemStorage<T> {
	@FunctionalInterface
	interface IndexedConsumer<T> {
		void accept(int index, T value);
	}

	private int [] indices = new int [4];
	private Object [] values = new Object [4];
	/*
	 * Parallel packed state lane.  A virtual widget can keep semantic/native-cache
	 * flags beside the logical coordinate without growing the public Item object
	 * graph.  The lane shifts together with indices/values, so state follows the
	 * same logical item across inserts/removes.
	 */
	private long [] stateMasks = new long [4];
	private int size;

	int size () {
		return size;
	}

	boolean isEmpty () {
		return size == 0;
	}

	@SuppressWarnings ("unchecked")
	T get (int index) {
        if (index < 0) {
            return null;
        }
		int position = Arrays.binarySearch (indices, 0, size, index);
		return position < 0 ? null : (T) values [position];
	}

	void put (int index, T value) {
        if (index < 0) {
            throw new IllegalArgumentException("negative index");
        }
		Objects.requireNonNull (value, "value");
		int position = Arrays.binarySearch (indices, 0, size, index);
		if (position >= 0) {
			values [position] = value;
			return;
		}
		position = -position - 1;
		ensureCapacity (size + 1);
		System.arraycopy (indices, position, indices, position + 1, size - position);
		System.arraycopy (values, position, values, position + 1, size - position);
		System.arraycopy (stateMasks, position, stateMasks, position + 1, size - position);
		indices [position] = index;
		values [position] = value;
		stateMasks [position] = 0;
		size++;
	}

	void insert (int index, T value) {
        if (index < 0) {
            throw new IllegalArgumentException("negative index");
        }
		Objects.requireNonNull (value, "value");
		int position = lowerBound (index);
		// Every existing coordinate at/after the insertion must remain representable.
		if (size > 0 && indices [size - 1] == Integer.MAX_VALUE) {
			throw new IllegalArgumentException("logical item index overflow");
		}
		ensureCapacity (size + 1);
		System.arraycopy (indices, position, indices, position + 1, size - position);
		System.arraycopy (values, position, values, position + 1, size - position);
		System.arraycopy (stateMasks, position, stateMasks, position + 1, size - position);
        for (int i = position + 1; i <= size; i++) {
            indices [i]++;
        }
		indices [position] = index;
		values [position] = value;
		stateMasks [position] = 0;
		size++;
	}

	@SuppressWarnings ("unchecked")
	T remove (int index) {
        if (index < 0) {
            throw new IllegalArgumentException("negative index");
        }
		int position = Arrays.binarySearch (indices, 0, size, index);
		T removed = null;
		if (position >= 0) {
			removed = (T) values [position];
			int tail = size - position - 1;
			if (tail > 0) {
				System.arraycopy (indices, position + 1, indices, position, tail);
				System.arraycopy (values, position + 1, values, position, tail);
				System.arraycopy (stateMasks, position + 1, stateMasks, position, tail);
			}
			size--;
			values [size] = null;
			stateMasks [size] = 0;
		} else {
			position = -position - 1;
		}
        for (int i = position; i < size; i++) {
            indices [i]--;
        }
		return removed;
	}

	void removeRange (int start, int endExclusive, Consumer<? super T> removed) {
        if (start < 0 || endExclusive < start) {
            throw new IllegalArgumentException("invalid range");
        }
        if (start == endExclusive) {
            return;
        }
		Objects.requireNonNull (removed, "removed");
		int left = lowerBound (start);
		int right = lowerBound (endExclusive);
        for (int i = left; i < right; i++) {
            removed.accept(valueAt(i));
        }
		int removedCount = right - left;
		int tail = size - right;
		if (tail > 0) {
			System.arraycopy (indices, right, indices, left, tail);
			System.arraycopy (values, right, values, left, tail);
			System.arraycopy (stateMasks, right, stateMasks, left, tail);
		}
		int logicalWidth = endExclusive - start;
		size -= removedCount;
        for (int i = left; i < size; i++) {
            indices [i] -= logicalWidth;
        }
		Arrays.fill (values, size, size + removedCount, null);
		Arrays.fill (stateMasks, size, size + removedCount, 0L);
	}

	void truncate (int logicalCount, Consumer<? super T> removed) {
        if (logicalCount < 0) {
            throw new IllegalArgumentException("negative logical count");
        }
		Objects.requireNonNull (removed, "removed");
		int firstRemoved = lowerBound (logicalCount);
        for (int i = firstRemoved; i < size; i++) {
            removed.accept(valueAt(i));
        }
		Arrays.fill (values, firstRemoved, size, null);
		Arrays.fill (stateMasks, firstRemoved, size, 0L);
		size = firstRemoved;
	}

	void clear (Consumer<? super T> removed) {
		Objects.requireNonNull (removed, "removed");
        for (int i = 0; i < size; i++) {
            removed.accept(valueAt(i));
        }
		Arrays.fill (values, 0, size, null);
		Arrays.fill (stateMasks, 0, size, 0L);
		size = 0;
	}

	void forEach (Consumer<? super T> consumer) {
		Objects.requireNonNull (consumer, "consumer");
        for (int i = 0; i < size; i++) {
            consumer.accept(valueAt(i));
        }
	}

	void forEachIndexed (IndexedConsumer<? super T> consumer) {
		Objects.requireNonNull (consumer, "consumer");
        for (int i = 0; i < size; i++) {
            consumer.accept(indices [i], valueAt(i));
        }
	}

	/**
	 * Drops materialized entries whose logical index is outside <code>[keepStart, keepEnd)</code>
	 * and whose state has none of the <code>keepMask</code> bits, without shifting any logical
	 * index. Returns the number of entries dropped.
	 */
	int releaseOutside (int keepStart, int keepEnd, long keepMask, Consumer<? super T> released) {
		Objects.requireNonNull (released, "released");
		int kept = 0;
		int dropped = 0;
		for (int i = 0; i < size; i++) {
			int index = indices [i];
			if ((index < keepStart || index >= keepEnd) && (stateMasks [i] & keepMask) == 0) {
				released.accept (valueAt (i));
				dropped++;
				continue;
			}
			if (kept != i) {
				indices [kept] = indices [i];
				values [kept] = values [i];
				stateMasks [kept] = stateMasks [i];
			}
			kept++;
		}
		Arrays.fill (values, kept, size, null);
		Arrays.fill (stateMasks, kept, size, 0L);
		size = kept;
		return dropped;
	}

	int indexOfIdentity (T value) {
		int position = positionOfIdentity (value);
		return position < 0 ? -1 : indices [position];
	}

	long state (int index) {
		int position = Arrays.binarySearch (indices, 0, size, index);
		return position < 0 ? 0L : stateMasks [position];
	}

	void state (int index, long state) {
		int position = Arrays.binarySearch (indices, 0, size, index);
        if (position < 0) {
            throw new IllegalStateException("logical item is not materialized");
        }
		stateMasks [position] = state;
	}

	boolean flag (int index, long mask) {
		return (state (index) & mask) != 0;
	}

	void flag (int index, long mask, boolean enabled) {
		int position = Arrays.binarySearch (indices, 0, size, index);
        if (position < 0) {
            throw new IllegalStateException("logical item is not materialized");
        }
        if (enabled) {
            stateMasks [position] |= mask;
        } else {
            stateMasks [position] &= ~mask;
        }
	}

	long stateOfIdentity (T value) {
		int position = positionOfIdentity (value);
		return position < 0 ? 0L : stateMasks [position];
	}

	boolean flagOfIdentity (T value, long mask) {
		return (stateOfIdentity (value) & mask) != 0;
	}

	void flagOfIdentity (T value, long mask, boolean enabled) {
		int position = positionOfIdentity (value);
        if (position < 0) {
            throw new IllegalStateException("materialized item is not in storage");
        }
        if (enabled) {
            stateMasks [position] |= mask;
        } else {
            stateMasks [position] &= ~mask;
        }
	}

	int indexAt (int materializedIndex) {
		return indices [Objects.checkIndex (materializedIndex, size)];
	}

	@SuppressWarnings ("unchecked")
	T valueAt (int materializedIndex) {
		return (T) values [Objects.checkIndex (materializedIndex, size)];
	}

	private int positionOfIdentity (T value) {
		for (int i = 0; i < size; i++) {
            if (values [i] == value) {
                return i;
            }
		}
		return -1;
	}

	private int lowerBound (int index) {
		int position = Arrays.binarySearch (indices, 0, size, index);
		return position >= 0 ? position : -position - 1;
	}

	private void ensureCapacity (int required) {
        if (required <= indices.length) {
            return;
        }
		int next = (int) Math.min (Integer.MAX_VALUE,
				Math.max ((long) required, Math.max (4L, (long) indices.length * 3 / 2)));
		// Stage every lane before publication. If any allocation fails, the old
		// equal-capacity directory, object identities and state masks stay intact.
		int [] nextIndices = Arrays.copyOf (indices, next);
		Object [] nextValues = Arrays.copyOf (values, next);
		long [] nextMasks = Arrays.copyOf (stateMasks, next);
		indices = nextIndices;
		values = nextValues;
		stateMasks = nextMasks;
	}
}
