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

import org.eclipse.swt.*;

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
final class VirtualBlockCache implements VirtualBlockStore {

	private final int blockSize;
	private final int columns;
	private final int capacity;

	private final int [] slotParent;
	private final int [] slotBlock;
	private final int [] slotState;
	private final int [] slotStamp;
	private final int [] slotTotal;
	private final String [] cells;
	private final int [] childHints;
	private final int [] keys;
	private final Object [] handles;
	private int clock;
	private Evictor evictor;
	private int lastHit = -1;
	private int evictions;

	VirtualBlockCache (int blockSize, int capacity, int columns) {
		if (blockSize < 1 || capacity < 1 || columns < 1) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		if (capacity > (Integer.MAX_VALUE / blockSize) / columns) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
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
		handles = new Object [capacity * blockSize];
		for (int i = 0; i < capacity; i++) {
			slotBlock [i] = -1;
			slotTotal [i] = UNKNOWN;
		}
	}

	/** Returns the slot holding <code>(parentKey, block)</code> in any non-empty state, or -1. */
	@Override
	public int find (int parentKey, int block) {
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
	@Override
	public int nextFrame () {
		if (clock == Integer.MAX_VALUE) rebaseStamps ();
		return ++clock;
	}

	int frame () {
		return clock;
	}

	@Override
	public int blockSize () {
		return blockSize;
	}

	@Override
	public int columns () {
		return columns;
	}

	@Override
	public int capacity () {
		return capacity;
	}

	@Override
	public void touch (int slot) {
		slotStamp [slot] = clock;
	}

	/**
	 * Reserves a slot for <code>(parentKey, block)</code> in state {@link #PENDING}. Prefers an
	 * empty slot, otherwise evicts the least recently touched settled block that was not
	 * touched in the current frame. Returns -1 when every slot is busy or protected.
	 */
	@Override
	public int reserve (int parentKey, int block) {
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
		if (slotState [victim] != EMPTY) {
			evictions++;
			if (evictor != null && slotState [victim] == LOADED) evictor.evicting (victim);
		}
		clearSlot (victim);
		slotParent [victim] = parentKey;
		slotBlock [victim] = block;
		slotState [victim] = PENDING;
		slotStamp [victim] = clock;
		return victim;
	}

	@Override
	public void free (int slot) {
		clearSlot (slot);
		slotState [slot] = EMPTY;
		slotBlock [slot] = -1;
		if (lastHit == slot) lastHit = -1;
	}

	/** Drops every settled block of <code>parentKey</code> (all keys when <code>parentKey</code> is
	 * {@link Integer#MIN_VALUE}); busy slots are left to their owner. Returns the count dropped. */
	@Override
	public int invalidate (int parentKey) {
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

	@Override
	public int state (int slot) {
		return slotState [slot];
	}

	@Override
	public void state (int slot, int state) {
		slotState [slot] = state;
	}

	@Override
	public int parentKey (int slot) {
		return slotParent [slot];
	}

	@Override
	public int block (int slot) {
		return slotBlock [slot];
	}

	int stamp (int slot) {
		return slotStamp [slot];
	}

	@Override
	public int firstRow (int slot) {
		return slotBlock [slot] * blockSize;
	}

	@Override
	public int total (int slot) {
		return slotTotal [slot];
	}

	@Override
	public void total (int slot, int total) {
		slotTotal [slot] = total;
	}

	@Override
	public String text (int slot, int row, int column) {
		return cells [cellIndex (slot, row, column)];
	}

	@Override
	public void text (int slot, int row, int column, String value) {
		cells [cellIndex (slot, row, column)] = value;
	}

	@Override
	public int childHint (int slot, int row) {
		return childHints [rowIndex (slot, row)];
	}

	@Override
	public void childHint (int slot, int row, int hint) {
		childHints [rowIndex (slot, row)] = hint;
	}

	@Override
	public int key (int slot, int row) {
		return keys [rowIndex (slot, row)];
	}

	@Override
	public void key (int slot, int row, int key) {
		keys [rowIndex (slot, row)] = key;
	}

	@Override
	public Object data (int slot, int row) {
		return handles [rowIndex (slot, row)];
	}

	@Override
	public void data (int slot, int row, Object data) {
		handles [rowIndex (slot, row)] = data;
	}

	@Override
	public int count (int stateMask) {
		int count = 0;
		for (int i = 0; i < capacity; i++) {
			if ((slotState [i] & stateMask) != 0) count++;
		}
		return count;
	}

	@Override
	public int evictions () {
		return evictions;
	}

	@Override
	public void evictor (Evictor evictor) {
		this.evictor = evictor;
	}

	private int rowIndex (int slot, int row) {
		if (slot < 0 || slot >= capacity || row < 0 || row >= blockSize) SWT.error (SWT.ERROR_INVALID_RANGE);
		return slot * blockSize + row;
	}

	private int cellIndex (int slot, int row, int column) {
		if (column < 0 || column >= columns) SWT.error (SWT.ERROR_INVALID_RANGE);
		return rowIndex (slot, row) * columns + column;
	}

	private void clearSlot (int slot) {
		int rowBase = slot * blockSize;
		int cellBase = rowBase * columns;
		for (int i = 0, n = blockSize * columns; i < n; i++) cells [cellBase + i] = null;
		for (int i = 0; i < blockSize; i++) {
			childHints [rowBase + i] = 0;
			keys [rowBase + i] = 0;
			handles [rowBase + i] = null;
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
