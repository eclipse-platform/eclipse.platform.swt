/*
 * Copyright 2026 Hitesh Soliwal and contributors
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 *
 * Dual-licensed by the rights holder's decision of 2026-10-13: you may use this file under the
 * Eclipse Public License 2.0 (https://www.eclipse.org/legal/epl-2.0/) or the Apache License 2.0
 * (https://www.apache.org/licenses/LICENSE-2.0).
 */
package org.eclipse.swt.internal.residency;

import java.io.*;
import java.util.*;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.swap.*;

/**
 * Desktop COLD tier of {@link ItemResidency} backed by the ported Synexia swap owners: payload
 * records go through {@link TieredSwapCache} (its own HOT/WARM byte residency, LRU) to a
 * {@link SwapStore} ({@link FileSwapStore}, the Java fail-closed backend). The engine sees int
 * handles; each maps to one immutable store id. Excluded from the web (GWT/RAP) build, where the
 * engine runs DESTROY-only.
 */
public final class ItemSwapSpill implements ItemResidency.Spill {
	private static final TieredSwapCache.Codec<byte []> BYTES = new TieredSwapCache.Codec<> () {
		@Override
		public byte [] encode (byte [] value) {
			return value;
		}

		@Override
		public byte [] decode (byte [] bytes) {
			return bytes;
		}

		@Override
		public long weight (byte [] value) {
			return value.length + 16L;
		}
	};

	final FileSwapStore store;
	final TieredSwapCache<byte []> cache;
	final TieredSwapCache.Budget budget;
	private long [] ids = new long [16];
	private int [] freeHandles = new int [16];
	private int handles, freeCount;
	int liveHandles;

	public ItemSwapSpill (FileSwapStore store, TieredSwapCache.Budget budget) {
		this.store = store;
		this.budget = budget;
		cache = new TieredSwapCache<> (store, BYTES, budget, TieredSwapCache.Policy.LRU, true);
	}

	/** Temporary store with a byte-residency budget of {@code cacheBytes} for swapped payload. */
	public static ItemSwapSpill temporary (long cacheBytes) throws IOException {
		long bytes = Math.max (4096L, cacheBytes);
		return new ItemSwapSpill (FileSwapStore.temporary (), new TieredSwapCache.Budget (bytes, 1 << 16, bytes / 4));
	}

	/** {@link #temporary(long)} when the flags ask for SWAP, else null (web or DESTROY-only). */
	public static ItemSwapSpill temporaryOrNull (int flags, long cacheBytes) {
		if ((flags & ItemResidency.RESIDENCY_SWAP) == 0) return null;
		try {
			return temporary (cacheBytes);
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public int write (byte [] data, int length) {
		byte [] record = Arrays.copyOf (data, length);
		long id;
		try {
			id = store.reserve (length);
			cache.put (id, record);
		} catch (IOException e) {
			throw new UncheckedIOException (e);
		}
		int handle;
		if (freeCount > 0) {
			handle = freeHandles [--freeCount];
		} else {
			if (handles == Integer.MAX_VALUE) throw new IllegalStateException ("swap handle space exhausted");
			if (handles == ids.length) ids = Arrays.copyOf (ids, PackedHeapGrowth.capacity (ids.length, handles + 1, 8));
			handle = handles++;
		}
		ids [handle] = id;
		liveHandles++;
		return handle;
	}

	@Override
	public byte [] read (int record) {
		try (TieredSwapCache<byte []>.Lease lease = cache.acquire (ids [record])) {
			return lease.value ();
		} catch (IOException e) {
			throw new UncheckedIOException (e);
		}
	}

	@Override
	public void free (int record) {
		try {
			store.release (ids [record]);
		} catch (IOException e) {
			throw new UncheckedIOException (e);
		}
		ids [record] = -1;
		if (freeCount == freeHandles.length) freeHandles = Arrays.copyOf (freeHandles, PackedHeapGrowth.capacity (freeHandles.length, freeCount + 1, 4));
		freeHandles [freeCount++] = record;
		liveHandles--;
	}

	/** Highest swap-file offset written, in bytes. */
	public long fileBytes () {
		return store.fileBytes ();
	}

	@Override
	public void close () {
		try {
			cache.close ();
		} catch (IOException e) {
			throw new UncheckedIOException (e);
		}
	}
}
