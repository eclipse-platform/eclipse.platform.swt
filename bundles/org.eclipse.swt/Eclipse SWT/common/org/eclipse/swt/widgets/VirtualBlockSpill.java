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

import java.io.*;
import java.util.*;

import org.eclipse.swt.internal.swap.*;

/**
 * Cold tier of the {@link VirtualPageSource} block store (construction flag
 * {@link VirtualPageSource#SWAP}): a loaded block evicted from the slot table is encoded and
 * written to the swap store of SWT #122 ({@link FileSwapStore} with the selected page ops, JNI
 * with a Java fallback), and a later request for the same block is answered from it without
 * asking the application again.
 *
 * <p>It fails closed: any I/O error disables the tier and the block is simply fetched again.
 * Blocks that carry data handles (arbitrary caller objects) are never written; they are dropped
 * and fetched again, so caller references stay pinned in memory.</p>
 *
 * <p>Thread-safety: {@link #spill} runs on the UI thread, {@link #take} on worker threads; all
 * state is guarded by this object's monitor.</p>
 */
final class VirtualBlockSpill {
	private long [] keys = new long [16];
	private long [] ids = new long [16];
	private int size;
	private FileSwapStore store;
	private boolean failed;
	private int spilled, restored;

	static long key (int parentKey, int block) {
		return ((long) parentKey << 32) | (block & 0xFFFFFFFFL);
	}

	synchronized boolean contains (long key) {
		return !failed && Arrays.binarySearch (keys, 0, size, key) >= 0;
	}

	synchronized int spilled () {
		return spilled;
	}

	synchronized int restored () {
		return restored;
	}

	/** Writes one encoded block; replaces an older copy of the same block. */
	synchronized void spill (long key, byte [] encoded) {
		if (failed || encoded == null) return;
		try {
			if (store == null) store = FileSwapStore.temporary ();
			long id = store.reserve (encoded.length);
			store.write (Map.of (id, encoded));
			int position = Arrays.binarySearch (keys, 0, size, key);
			if (position >= 0) {
				store.release (ids [position]);
				ids [position] = id;
			} else {
				position = -position - 1;
				if (size == keys.length) {
					int capacity = VirtualGrowth.capacity (keys.length, size + 1, 16);
					keys = Arrays.copyOf (keys, capacity);
					ids = Arrays.copyOf (ids, capacity);
				}
				System.arraycopy (keys, position, keys, position + 1, size - position);
				System.arraycopy (ids, position, ids, position + 1, size - position);
				keys [position] = key;
				ids [position] = id;
				size++;
			}
			spilled++;
		} catch (IOException | RuntimeException e) {
			fail ();
		}
	}

	/** Removes and returns a spilled block, or null when it is not in the cold tier. */
	synchronized byte [] take (long key) {
		if (failed) return null;
		int position = Arrays.binarySearch (keys, 0, size, key);
		if (position < 0) return null;
		long id = ids [position];
		System.arraycopy (keys, position + 1, keys, position, size - position - 1);
		System.arraycopy (ids, position + 1, ids, position, size - position - 1);
		size--;
		try {
			byte [] bytes = store.read (id);
			store.release (id);
			if (bytes != null) restored++;
			return bytes;
		} catch (IOException | RuntimeException e) {
			fail ();
			return null;
		}
	}

	/** Forgets every spilled block of a parent (or all with {@link Integer#MIN_VALUE}). */
	synchronized void invalidate (int parentKey) {
		int kept = 0;
		for (int i = 0; i < size; i++) {
			if (parentKey == Integer.MIN_VALUE || (int) (keys [i] >> 32) == parentKey) {
				try {
					if (store != null) store.release (ids [i]);
				} catch (IOException | RuntimeException e) {
					fail ();
					return;
				}
				continue;
			}
			keys [kept] = keys [i];
			ids [kept] = ids [i];
			kept++;
		}
		size = kept;
	}

	synchronized void close () {
		size = 0;
		if (store != null) {
			try {
				store.close ();
			} catch (IOException | RuntimeException e) {
				// the temporary store deletes itself on close; nothing else to undo
			}
			store = null;
		}
	}

	private void fail () {
		failed = true;
		size = 0;
		if (store != null) {
			try {
				store.close ();
			} catch (IOException | RuntimeException e) {
				// fail closed
			}
			store = null;
		}
	}

	/* ------------------------------------------------------------ block codec */

	/**
	 * Encodes a loaded block: rows, columns, total, then per row its child hint, key and cells
	 * (length -1 for null). Returns null for a block that holds data handles.
	 */
	static byte [] encode (VirtualBlockStore cache, int slot) {
		int rows = cache.blockSize ();
		int columns = cache.columns ();
		for (int row = 0; row < rows; row++) {
			if (cache.data (slot, row) != null) return null;
		}
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream (rows * (8 + columns * 24));
			DataOutputStream out = new DataOutputStream (bytes);
			out.writeInt (rows);
			out.writeInt (columns);
			out.writeInt (cache.total (slot));
			for (int row = 0; row < rows; row++) {
				out.writeInt (cache.childHint (slot, row));
				out.writeInt (cache.key (slot, row));
				for (int column = 0; column < columns; column++) {
					String text = cache.text (slot, row, column);
					if (text == null) {
						out.writeInt (-1);
					} else {
						out.writeInt (text.length ());
						out.writeChars (text);
					}
				}
			}
			out.flush ();
			return bytes.toByteArray ();
		} catch (IOException e) {
			return null;
		}
	}

	/** Decodes a block into a reserved slot; returns the block's total, or throws on a mismatch. */
	static int decode (byte [] encoded, VirtualBlockStore cache, int slot) throws IOException {
		DataInputStream in = new DataInputStream (new ByteArrayInputStream (encoded));
		int rows = in.readInt ();
		int columns = in.readInt ();
		if (rows != cache.blockSize () || columns != cache.columns ()) throw new IOException ("block shape changed");
		int total = in.readInt ();
		char [] chars = new char [64];
		for (int row = 0; row < rows; row++) {
			cache.childHint (slot, row, in.readInt ());
			cache.key (slot, row, in.readInt ());
			for (int column = 0; column < columns; column++) {
				int length = in.readInt ();
				if (length < 0) {
					cache.text (slot, row, column, null);
					continue;
				}
				if (chars.length < length) chars = new char [length];
				for (int i = 0; i < length; i++) chars [i] = in.readChar ();
				cache.text (slot, row, column, new String (chars, 0, length));
			}
		}
		return total;
	}
}
