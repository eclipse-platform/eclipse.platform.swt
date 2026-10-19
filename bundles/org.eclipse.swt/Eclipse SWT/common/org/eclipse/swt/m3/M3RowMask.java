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

/*
 * Row masks: one bit per source row in a long[] lane (bit i of word i >>> 6). Bit runs are moved
 * 64 bits at a time for insertions and removals of source rows; the kept lane of a mask is its
 * compaction (trailing-zero walk) and a view row is found back by a rank query (per-word prefix
 * popcounts + bitCount of the masked word).
 */
final class M3RowMask {

	private M3RowMask () {
	}

	static int words (int bits) {
		return (bits + 63) >>> 6;
	}

	static long [] ensure (long [] mask, int bits) {
		int words = words (bits);
		if (mask.length >= words) return mask;
		long [] grown = new long [Math.max (words, mask.length + (mask.length >> 1))];
		System.arraycopy (mask, 0, grown, 0, mask.length);
		return grown;
	}

	static boolean get (long [] mask, int bit) {
		return (mask [bit >>> 6] & 1L << bit) != 0;
	}

	static void set (long [] mask, int bit, boolean value) {
		if (value) mask [bit >>> 6] |= 1L << bit; else mask [bit >>> 6] &= ~(1L << bit);
	}

	/* sets bits [0, size) and clears the rest of the lane */
	static void fill (long [] mask, int size) {
		int full = size >>> 6;
		java.util.Arrays.fill (mask, 0, full, -1L);
		java.util.Arrays.fill (mask, full, mask.length, 0L);
		if ((size & 63) != 0) mask [full] = (1L << size) - 1;
	}

	/* bits [start, size) move up by count; the count bits from start are cleared (capacity size + count) */
	static void insert (long [] mask, int size, int start, int count) {
		for (int remaining = size - start; remaining > 0;) {
			int length = Math.min (64, remaining);
			remaining -= length;
			write (mask, start + remaining + count, read (mask, start + remaining), length);
		}
		clear (mask, start, count);
	}

	/* bits [start + count, size) move down by count; the count bits at the end are cleared */
	static void remove (long [] mask, int size, int start, int count) {
		for (int done = 0, total = size - start - count; done < total;) {
			int length = Math.min (64, total - done);
			write (mask, start + done, read (mask, start + count + done), length);
			done += length;
		}
		clear (mask, size - count, count);
	}

	static void clear (long [] mask, int start, int count) {
		for (int done = 0; done < count;) {
			int length = Math.min (64, count - done);
			write (mask, start + done, 0, length);
			done += length;
		}
	}

	/* the 64 bits from position (bits past the lane read as 0) */
	static long read (long [] mask, int position) {
		int word = position >>> 6, bit = position & 63;
		long low = word < mask.length ? mask [word] >>> bit : 0;
		if (bit == 0) return low;
		long high = word + 1 < mask.length ? mask [word + 1] << (64 - bit) : 0;
		return low | high;
	}

	/* writes the low length (1 .. 64) bits of value at position */
	static void write (long [] mask, int position, long value, int length) {
		int word = position >>> 6, bit = position & 63;
		long ones = length == 64 ? -1L : (1L << length) - 1;
		value &= ones;
		mask [word] = mask [word] & ~(ones << bit) | value << bit;
		if (bit + length > 64) {
			int shift = 64 - bit;
			mask [word + 1] = mask [word + 1] & ~(ones >>> shift) | value >>> shift;
		}
	}

	/* writes the positions of the set bits of mask [0, words) into kept (capacity: their count); returns the count */
	static int compact (long [] mask, int words, int [] kept, int [] rank) {
		int count = 0;
		for (int w = 0; w < words; w++) {
			rank [w] = count;
			for (long x = mask [w]; x != 0; x &= x - 1) kept [count++] = w << 6 | Long.numberOfTrailingZeros (x);
		}
		return count;
	}

	/* the number of set bits of mask [0, words) */
	static int count (long [] mask, int words) {
		int count = 0;
		for (int w = 0; w < words; w++) count += Long.bitCount (mask [w]);
		return count;
	}

	/* the view row of a set bit, -1 for a clear bit */
	static int rank (long [] mask, int [] rank, int bit) {
		long word = mask [bit >>> 6], b = 1L << bit;
		return (word & b) == 0 ? -1 : rank [bit >>> 6] + Long.bitCount (word & b - 1);
	}
}
