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
 * Bounded block table behind {@link VirtualPageSource}: a fixed number of slots, each
 * holding one block of <code>blockSize</code> rows of one parent key.
 *
 * <p>All state is packed into <code>int</code> lanes and one reusable cell array; no
 * per-block or per-row objects are created after construction. Lookups are linear over
 * the slot lanes (the slot count is small and bounded), which keeps the class free of
 * hashing and of any <code>int</code> overflow reliance, so it compiles unchanged with
 * GWT. Pattern sources (no text copied): AG Grid Community infinite cache
 * (<code>maxBlocksInCache</code>, purge by last access), AndroidX Paging
 * (<code>maxSize</code>).</p>
 *
 * <p>Threading: the UI thread owns every lane. A worker thread may write only the cells
 * and row lanes of a slot that is in state {@link #LOADING} and was handed to it; the
 * hand-off and the hand-back go through the owner's lock.</p>
 */
//FLLDCIM: HPCPOP - per-row lookups are int compares over bounded lanes; no allocation
final class VirtualBlockCache {
	static final int EMPTY = 0;
	static final int PENDING = 1;
	static final int LOADING = 1 << 1;
	static final int LOADED = 1 << 2;
	static final int FAILED = 1 << 3;
	static final int CANCELLED = 1 << 4;
	static final int BUSY = PENDING | LOADING;

	/** Child-count hint of a tree row whose child count has not been fetched. */
	static final int UNKNOWN = -1;

	final int blockSize;
	final int columns;
	final int capacity;

	private final int [] slotParent;
	private final int [] slotBlock;
	private final int [] slotState;
	private final int [] slotStamp;
	private final int [] slotTotal;
	private final String [] cells;
	private final int [] childHints;
	private final int [] keys;
	private int clock;
	private int lastHit = -1;
	private int evictions;

	VirtualBlockCache (int blockSize, int capacity, int columns) {
		if (blockSize < 1 || capacity < 1 || columns < 1) throw new IllegalArgumentException ();
		if (capacity > (Integer.MAX_VALUE / blockSize) / columns) throw new IllegalArgumentException ("cache too large");
		this.blockSize = blockSize;
		this.capacity = capacity;
		this.columns = columns;
		slotParent = new int [capacity];
		slotBlock = new int [capacity];
		slotState = new int [capacity];
		slotStamp = new int [capacity];
		slotTotal = new int [capacity];
		cells = new String [capacity * blockSize * columns];
		childHints = new int [capacity * blockSize];
		keys = new int [capacity * blockSize];
		for (int i = 0; i < capacity; i++) {
			slotBlock [i] = -1;
			slotTotal [i] = UNKNOWN;
		}
	}

	/** Returns the slot holding <code>(parentKey, block)</code> in any non-empty state, or -1. */
	int find (int parentKey, int block) {
		int hit = lastHit;
		if (hit >= 0 && slotBlock [hit] == block && slotParent [hit] == parentKey && slotState [hit] != EMPTY) return hit;
		for (int i = 0; i < capacity; i++) {
			if (slotBlock [i] == block && slotParent [i] == parentKey && slotState [i] != EMPTY) {
				lastHit = i;
				return i;
			}
		}
		return -1;
	}

	/** Advances the frame clock; blocks touched at or after the returned stamp are protected. */
	int nextFrame () {
		if (clock == Integer.MAX_VALUE) rebaseStamps ();
		return ++clock;
	}

	int frame () {
		return clock;
	}

	void touch (int slot) {
		slotStamp [slot] = clock;
	}

	/**
	 * Reserves a slot for <code>(parentKey, block)</code> in state {@link #PENDING}. Prefers an
	 * empty slot, otherwise evicts the least recently touched settled block that was not
	 * touched in the current frame. Returns -1 when every slot is busy or protected.
	 */
	int reserve (int parentKey, int block) {
		int victim = -1;
		int victimStamp = 0;
		for (int i = 0; i < capacity; i++) {
			int state = slotState [i];
			if (state == EMPTY) {
				victim = i;
				break;
			}
			if ((state & BUSY) != 0 || slotStamp [i] >= clock) continue;
			if (victim < 0 || slotStamp [i] < victimStamp) {
				victim = i;
				victimStamp = slotStamp [i];
			}
		}
		if (victim < 0) return -1;
		if (slotState [victim] != EMPTY) evictions++;
		clearSlot (victim);
		slotParent [victim] = parentKey;
		slotBlock [victim] = block;
		slotState [victim] = PENDING;
		slotStamp [victim] = clock;
		return victim;
	}

	void free (int slot) {
		clearSlot (slot);
		slotState [slot] = EMPTY;
		slotBlock [slot] = -1;
		if (lastHit == slot) lastHit = -1;
	}

	/** Drops every settled block of <code>parentKey</code> (all keys when <code>parentKey</code> is
	 * {@link Integer#MIN_VALUE}); busy slots are left to their owner. Returns the count dropped. */
	int invalidate (int parentKey) {
		int dropped = 0;
		for (int i = 0; i < capacity; i++) {
			int state = slotState [i];
			if (state == EMPTY || (state & BUSY) != 0) continue;
			if (parentKey != Integer.MIN_VALUE && slotParent [i] != parentKey) continue;
			free (i);
			dropped++;
		}
		return dropped;
	}

	int state (int slot) {
		return slotState [slot];
	}

	void state (int slot, int state) {
		slotState [slot] = state;
	}

	int parentKey (int slot) {
		return slotParent [slot];
	}

	int block (int slot) {
		return slotBlock [slot];
	}

	int stamp (int slot) {
		return slotStamp [slot];
	}

	int firstRow (int slot) {
		return slotBlock [slot] * blockSize;
	}

	int total (int slot) {
		return slotTotal [slot];
	}

	void total (int slot, int total) {
		slotTotal [slot] = total;
	}

	String text (int slot, int row, int column) {
		return cells [cellIndex (slot, row, column)];
	}

	void text (int slot, int row, int column, String value) {
		cells [cellIndex (slot, row, column)] = value;
	}

	int childHint (int slot, int row) {
		return childHints [rowIndex (slot, row)];
	}

	void childHint (int slot, int row, int hint) {
		childHints [rowIndex (slot, row)] = hint;
	}

	int key (int slot, int row) {
		return keys [rowIndex (slot, row)];
	}

	void key (int slot, int row, int key) {
		keys [rowIndex (slot, row)] = key;
	}

	int count (int stateMask) {
		int count = 0;
		for (int i = 0; i < capacity; i++) {
			if ((slotState [i] & stateMask) != 0) count++;
		}
		return count;
	}

	int evictions () {
		return evictions;
	}

	private int rowIndex (int slot, int row) {
		if (slot < 0 || slot >= capacity || row < 0 || row >= blockSize) throw new IndexOutOfBoundsException ();
		return slot * blockSize + row;
	}

	private int cellIndex (int slot, int row, int column) {
		if (column < 0 || column >= columns) throw new IndexOutOfBoundsException ();
		return rowIndex (slot, row) * columns + column;
	}

	private void clearSlot (int slot) {
		int rowBase = slot * blockSize;
		int cellBase = rowBase * columns;
		for (int i = 0, n = blockSize * columns; i < n; i++) cells [cellBase + i] = null;
		for (int i = 0; i < blockSize; i++) {
			childHints [rowBase + i] = 0;
			keys [rowBase + i] = 0;
		}
		slotTotal [slot] = UNKNOWN;
	}

	private void rebaseStamps () {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < capacity; i++) min = Math.min (min, slotStamp [i]);
		for (int i = 0; i < capacity; i++) slotStamp [i] -= min;
		clock -= min;
	}
}
