/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contract adapted from Synexia PackedLongLongLruCache (Apache-2.0,
 * hsoliwal/com.synexia@af4494b1432): hard ceiling, admission before eviction,
 * pressure readjust, no per-entry allocation, eviction counter.
 * See docs/licenses/swt-gpu-retained-planes-NOTICE.txt.
 *******************************************************************************/
package org.eclipse.swt.internal.gpu;

/**
 * Bounded retained-resource slot table: a packed {@code long} key maps to a
 * dense {@code int} slot. Callers keep their resource (a native handle, a
 * lazily created Skija object, a tile rectangle) in parallel arrays indexed by
 * slot, so a hit costs one probe and allocates nothing.
 * <p>
 * Recency is an intrusive doubly linked list over {@code int[]} links (head is
 * least recent). A slot with a positive pin count is never evicted: a GDI pen
 * selected into a live DC, or a tile drawn in the current frame, stays resident.
 * When every slot is pinned, {@link #acquire(long, Evictor)} refuses with
 * {@code -1} instead of growing; callers then use their unretained path.
 * </p>
 * <p>UI-thread confined. No synchronization.</p>
 */
public final class RetainedSlots {
	/** Receives the key of a slot about to be reused so the owner can release its resource. */
	public interface Evictor {
		void evict (int slot, long key);
	}

	private final int capacity;
	private final long [] keys;
	private final int [] pins, prev, next, index, free;
	private final int mask;
	private int resident, size, head = -1, tail = -1, freeTop;
	private int lastNew;
	/** Lookup counters: hit, miss, eviction, refusal (all pinned). */
	public long hits, misses, evictions, refusals;

	public RetainedSlots (int capacity) {
		if (capacity <= 0 || capacity > (1 << 24)) throw new IllegalArgumentException ("capacity");
		this.capacity = resident = capacity;
		keys = new long [capacity];
		pins = new int [capacity];
		prev = new int [capacity];
		next = new int [capacity];
		free = new int [capacity];
		int tableSize = Integer.highestOneBit (capacity * 2 - 1) << 1;
		index = new int [tableSize];
		mask = tableSize - 1;
		for (int i = 0; i < capacity; i++) free [i] = capacity - 1 - i;
		freeTop = capacity;
	}

	static int hash (long key) {
		key ^= key >>> 33;
		key *= 0xff51afd7ed558ccdL;
		key ^= key >>> 33;
		key *= 0xc4ceb9fe1a85ec53L;
		key ^= key >>> 33;
		return (int) key;
	}

	/** Returns the slot holding {@code key} without touching recency or counters, else -1. */
	public int peek (long key) {
		for (int i = hash (key) & mask;; i = (i + 1) & mask) {
			int entry = index [i];
			if (entry == 0) return -1;
			if (keys [entry - 1] == key) return entry - 1;
		}
	}

	/** Hit: touch and return the slot. Miss: returns -1 and counts the miss. */
	public int find (long key) {
		int slot = peek (key);
		if (slot < 0) {
			misses++;
			return -1;
		}
		hits++;
		touch (slot);
		return slot;
	}

	/**
	 * Returns the slot of {@code key}, admitting it when absent. Admission takes
	 * a free slot or evicts the least recent unpinned slot; {@link #isNew}
	 * reports whether the caller must create the resource. Returns -1 when every
	 * slot is pinned.
	 */
	public int acquire (long key, Evictor evictor) {
		int slot = find (key);
		if (slot >= 0) {
			lastNew = 0;
			return slot;
		}
		slot = admit (evictor);
		if (slot < 0) return -1;
		keys [slot] = key;
		insertIndex (slot);
		linkTail (slot);
		size++;
		lastNew = 1;
		return slot;
	}

	/** True when the last {@link #acquire} admitted a new key (the caller must create the resource). */
	public boolean isNew () {
		return lastNew != 0;
	}

	private int admit (Evictor evictor) {
		if (size < resident && freeTop > 0) return free [--freeTop];
		for (int s = head; s >= 0; s = next [s]) {
			if (pins [s] == 0) {
				long key = keys [s];
				removeSlot (s);
				evictions++;
				if (evictor != null) evictor.evict (s, key);
				return free [--freeTop];
			}
		}
		refusals++;
		return -1;
	}

	public void pin (int slot) {
		pins [slot]++;
	}

	public void unpin (int slot) {
		if (pins [slot] > 0) pins [slot]--;
	}

	public int pins (int slot) {
		return pins [slot];
	}

	public long key (int slot) {
		return keys [slot];
	}

	public int size () {
		return size;
	}

	public int capacity () {
		return capacity;
	}

	public int residentCapacity () {
		return resident;
	}

	/** Moves {@code slot} to the most recent end. */
	public void touch (int slot) {
		if (slot == tail) return;
		unlink (slot);
		linkTail (slot);
	}

	/** Removes a resident slot; the caller releases its resource first. */
	public void remove (int slot) {
		removeSlot (slot);
	}

	/**
	 * Pressure readjust in the M3 bounded-cache style: shrinking evicts least
	 * recent unpinned slots immediately; growing only raises the ceiling up to
	 * the hard capacity. Returns the number of evicted slots.
	 */
	public int readjust (int newResident, Evictor evictor) {
		resident = Math.max (0, Math.min (capacity, newResident));
		int removed = 0;
		for (int s = head; s >= 0 && size > resident;) {
			int n = next [s];
			if (pins [s] == 0) {
				long key = keys [s];
				removeSlot (s);
				evictions++;
				removed++;
				if (evictor != null) evictor.evict (s, key);
			}
			s = n;
		}
		return removed;
	}

	/** Visits resident slots least recent first; used for disposal. */
	public int first () {
		return head;
	}

	public int next (int slot) {
		return next [slot];
	}

	private void removeSlot (int slot) {
		unlink (slot);
		deleteIndex (slot);
		pins [slot] = 0;
		free [freeTop++] = slot;
		size--;
	}

	private void insertIndex (int slot) {
		int i = hash (keys [slot]) & mask;
		while (index [i] != 0) i = (i + 1) & mask;
		index [i] = slot + 1;
	}

	/** Backward-shift deletion keeps linear probing tombstone free. */
	private void deleteIndex (int slot) {
		int i = hash (keys [slot]) & mask;
		while (index [i] != slot + 1) i = (i + 1) & mask;
		for (int j = (i + 1) & mask;; j = (j + 1) & mask) {
			int entry = index [j];
			if (entry == 0) break;
			int home = hash (keys [entry - 1]) & mask;
			if (((j - home) & mask) >= ((j - i) & mask)) {
				index [i] = entry;
				i = j;
			}
		}
		index [i] = 0;
	}

	private void linkTail (int slot) {
		prev [slot] = tail;
		next [slot] = -1;
		if (tail >= 0) next [tail] = slot; else head = slot;
		tail = slot;
	}

	private void unlink (int slot) {
		int p = prev [slot], n = next [slot];
		if (p >= 0) next [p] = n; else head = n;
		if (n >= 0) prev [n] = p; else tail = p;
	}
}
