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
 * Per-row boolean state of a widget kept as bit lanes in the parent: one bitset of
 * <code>int</code> words per lane, indexed by logical row.
 *
 * <p>A lane's words are allocated on its first set bit, so an untouched lane costs
 * nothing. Range set/clear, counting and insert/remove shifting are word-wise. The class
 * uses <code>int</code> words only (no <code>long</code> emulation, no reliance on
 * <code>int</code> overflow), so it compiles unchanged with GWT.</p>
 */
//FLLDCIM: HPCPOP - check-all/count are word fills and Integer.bitCount; no per-row work
final class VirtualBitLanes {
	private final int [][] lanes;
	private int length;

	VirtualBitLanes (int laneCount) {
		if (laneCount < 1) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		lanes = new int [laneCount][];
	}

	int length () {
		return length;
	}

	/** Sets the logical row count; bits at or beyond it are cleared. */
	void setLength (int newLength) {
		if (newLength < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		if (newLength < length) {
			for (int lane = 0; lane < lanes.length; lane++) clearBits (lanes [lane], newLength, length);
		}
		length = newLength;
	}

	void clear () {
		for (int lane = 0; lane < lanes.length; lane++) lanes [lane] = null;
		length = 0;
	}

	boolean get (int lane, int index) {
		int [] words = lanes [lane];
		if (words == null || index < 0 || index >= length) return false;
		int word = index >>> 5;
		return word < words.length && (words [word] & (1 << (index & 31))) != 0;
	}

	void set (int lane, int index, boolean value) {
		checkIndex (index);
		if (value) {
			int [] words = ensure (lane, index + 1);
			words [index >>> 5] |= 1 << (index & 31);
		} else {
			int [] words = lanes [lane];
			if (words == null) return;
			int word = index >>> 5;
			if (word < words.length) words [word] &= ~(1 << (index & 31));
		}
	}

	/** Sets or clears <code>[start, end)</code> of a lane word-wise. */
	void setRange (int lane, int start, int end, boolean value) {
		if (start < 0 || end > length || start > end) SWT.error (SWT.ERROR_INVALID_RANGE);
		if (start == end) return;
		if (value) {
			setBits (ensure (lane, end), start, end);
		} else {
			clearBits (lanes [lane], start, end);
		}
	}

	/** Number of set bits in a lane. */
	int count (int lane) {
		int [] words = lanes [lane];
		if (words == null) return 0;
		int count = 0;
		for (int word : words) count += Integer.bitCount (word);
		return count;
	}

	/** Number of set bits of a lane in <code>[start, end)</code>. */
	int count (int lane, int start, int end) {
		if (start < 0 || end > length || start > end) SWT.error (SWT.ERROR_INVALID_RANGE);
		int [] words = lanes [lane];
		if (words == null || start == end) return 0;
		int count = 0;
		int first = start >>> 5;
		int last = (end - 1) >>> 5;
		for (int word = first; word <= last && word < words.length; word++) {
			int bits = words [word];
			if (word == first) bits &= -1 << (start & 31);
			if (word == last) bits &= -1 >>> (31 - ((end - 1) & 31));
			count += Integer.bitCount (bits);
		}
		return count;
	}

	/** Flips <code>[start, end)</code> of a lane word-wise. */
	void flipRange (int lane, int start, int end) {
		if (start < 0 || end > length || start > end) SWT.error (SWT.ERROR_INVALID_RANGE);
		if (start == end) return;
		int [] words = ensure (lane, end);
		int first = start >>> 5;
		int last = (end - 1) >>> 5;
		int firstMask = -1 << (start & 31);
		int lastMask = -1 >>> (31 - ((end - 1) & 31));
		if (first == last) {
			words [first] ^= firstMask & lastMask;
			return;
		}
		words [first] ^= firstMask;
		for (int word = first + 1; word < last; word++) words [word] = ~words [word];
		words [last] ^= lastMask;
	}

	/** Index of the first clear bit at or after <code>from</code>, or <code>length ()</code>. */
	int nextClear (int lane, int from) {
		if (from < 0) from = 0;
		if (from >= length) return length;
		int [] words = lanes [lane];
		if (words == null) return from;
		int word = from >>> 5;
		if (word >= words.length) return from;
		int bits = ~words [word] & (-1 << (from & 31));
		while (true) {
			if (bits != 0) {
				int index = (word << 5) + Integer.numberOfTrailingZeros (bits);
				return Math.min (index, length);
			}
			if (++word >= words.length) return Math.min (word << 5, length);
			bits = ~words [word];
		}
	}

	/** Index of the first set bit at or after <code>from</code>, or -1. */
	int nextSet (int lane, int from) {
		int [] words = lanes [lane];
		if (words == null || from >= length) return -1;
		if (from < 0) from = 0;
		int word = from >>> 5;
		if (word >= words.length) return -1;
		int bits = words [word] & (-1 << (from & 31));
		while (true) {
			if (bits != 0) {
				int index = (word << 5) + Integer.numberOfTrailingZeros (bits);
				return index < length ? index : -1;
			}
			if (++word >= words.length) return -1;
			bits = words [word];
		}
	}

	/** Inserts <code>count</code> clear rows at <code>index</code> in every lane. */
	void insert (int index, int count) {
		if (index < 0 || index > length || count < 0 || count > Integer.MAX_VALUE - length) SWT.error (SWT.ERROR_INVALID_RANGE);
		if (count == 0) return;
		int newLength = length + count;
		int requiredWords = wordsFor (newLength);
		// Allocate all required growth before shifting any original state.
		// If all lanes already fit, the hot path does not allocate at all.
		int [][] staged = null;
		for (int lane = 0; lane < lanes.length; lane++) {
			int [] original = lanes [lane];
			if (original == null || original.length >= requiredWords) continue;
			if (staged == null) staged = lanes.clone ();
			staged [lane] = grow (original, newLength);
		}
		// All allocations have completed; existing word shifts allocate nothing.
		for (int lane = 0; lane < lanes.length; lane++) {
			int [] words = staged == null ? lanes [lane] : staged [lane];
			if (words != null) shiftUp (words, index, count, newLength);
		}
		if (staged != null) System.arraycopy (staged, 0, lanes, 0, lanes.length);
		length = newLength;
	}

	/** Removes rows <code>[index, index + count)</code> from every lane. */
	void remove (int index, int count) {
		if (index < 0 || count < 0 || index > length - count) SWT.error (SWT.ERROR_INVALID_RANGE);
		if (count == 0) return;
		int newLength = length - count;
		for (int lane = 0; lane < lanes.length; lane++) {
			int [] words = lanes [lane];
			if (words == null) continue;
			shiftDown (words, index, count, newLength);
			clearBits (words, newLength, length);
		}
		length = newLength;
	}

	private void checkIndex (int index) {
		if (index < 0 || index >= length) SWT.error (SWT.ERROR_INVALID_RANGE);
	}

	private int [] ensure (int lane, int bits) {
		int [] original = lanes [lane];
		int [] grown = grow (original, bits);
		if (grown != original) lanes [lane] = grown;
		return grown;
	}

	/** Allocate replacement backing without publishing it; preserve VirtualGrowth. */
	private int [] grow (int [] original, int bits) {
		int needed = wordsFor (bits);
		if (original != null && original.length >= needed) return original;
		int capacity = Math.max (needed, Math.min (
				VirtualGrowth.capacity (original == null ? 0 : original.length, needed, 4),
				Math.max (needed, wordsFor (length))));
		int [] grown = new int [capacity];
		if (original != null) System.arraycopy (original, 0, grown, 0, original.length);
		return grown;
	}

	private static int wordsFor (int bits) {
		return (bits >>> 5) + ((bits & 31) != 0 ? 1 : 0);
	}

	/** 32 bits starting at <code>offset</code> (which may be negative); missing bits read as 0. */
	private static int bitsAt (int [] words, int offset) {
		if (offset < 0) {
			int shift = -offset;
			return shift >= 32 || words.length == 0 ? 0 : words [0] << shift;
		}
		int word = offset >>> 5;
		int shift = offset & 31;
		int low = word < words.length ? words [word] >>> shift : 0;
		if (shift == 0) return low;
		int high = word + 1 < words.length ? words [word + 1] << (32 - shift) : 0;
		return low | high;
	}

	/** Moves bits <code>[index, newLength - count)</code> up by <code>count</code>; clears the gap. */
	private static void shiftUp (int [] words, int index, int count, int newLength) {
		int destinationStart = index + count;
		int lastWord = (newLength - 1) >>> 5;
		int firstWord = destinationStart >>> 5;
		for (int word = lastWord; word >= firstWord; word--) {
			int base = word << 5;
			int source = bitsAt (words, base - count);
			int keep = base < destinationStart ? (1 << (destinationStart - base)) - 1 : 0;
			words [word] = (words [word] & keep) | (source & ~keep);
		}
		clearBits (words, index, destinationStart);
	}

	/** Moves bits <code>[index + count, ...)</code> down to <code>index</code>. */
	private static void shiftDown (int [] words, int index, int count, int newLength) {
		if (newLength <= index) return;
		int firstWord = index >>> 5;
		int lastWord = Math.min ((newLength - 1) >>> 5, words.length - 1);
		for (int word = firstWord; word <= lastWord; word++) {
			int base = word << 5;
			int source = bitsAt (words, base + count);
			int keep = base < index ? (1 << (index - base)) - 1 : 0;
			words [word] = (words [word] & keep) | (source & ~keep);
		}
	}

	private static void setBits (int [] words, int start, int end) {
		int first = start >>> 5;
		int last = (end - 1) >>> 5;
		int firstMask = -1 << (start & 31);
		int lastMask = -1 >>> (31 - ((end - 1) & 31));
		if (first == last) {
			words [first] |= firstMask & lastMask;
			return;
		}
		words [first] |= firstMask;
		for (int word = first + 1; word < last; word++) words [word] = -1;
		words [last] |= lastMask;
	}

	private static void clearBits (int [] words, int start, int end) {
		if (words == null || start >= end) return;
		int capacityBits = words.length > (Integer.MAX_VALUE >>> 5) ? Integer.MAX_VALUE : words.length << 5;
		if (start >= capacityBits) return;
		if (end > capacityBits) end = capacityBits;
		int first = start >>> 5;
		int last = (end - 1) >>> 5;
		int firstMask = -1 << (start & 31);
		int lastMask = -1 >>> (31 - ((end - 1) & 31));
		if (first == last) {
			words [first] &= ~(firstMask & lastMask);
			return;
		}
		words [first] &= ~firstMask;
		for (int word = first + 1; word < last; word++) words [word] = 0;
		words [last] &= ~lastMask;
	}
}
