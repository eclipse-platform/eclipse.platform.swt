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
package org.eclipse.swt.internal.residency;

import java.util.Arrays;

import org.eclipse.swt.internal.PackedHeapGrowth;

/**
 * Sparse bit set over non-negative int positions in RoaringBitmap-style containers (container
 * idea adapted from RoaringBitmap, Apache-2.0): sorted 65536-position chunks, each an ARRAY of
 * sparse positions, a RUN list (long runs such as select-all) or a BITMAP (dense pages). Memory
 * is O(set state), not O(positions). {@code int} only (GWT).
 */
public final class ItemBitSet {
	public static final int ARRAY = 0, BITMAP = 1, RUN = 2;
	public static final int CHUNK = 1 << 16, ARRAY_MAX = 4096, RUN_MAX = 2048, BITMAP_WORDS = CHUNK / 32;

	int [] keys = new int [0], kinds = new int [0], sizes = new int [0];
	int [][] data = new int [0][];
	int n;

	private int find (int key) {
		int lo = 0, hi = n - 1;
		while (lo <= hi) {
			int mid = (lo + hi) >>> 1;
			if (keys [mid] < key) lo = mid + 1;
			else if (keys [mid] > key) hi = mid - 1;
			else return mid;
		}
		return -(lo + 1);
	}

	private int insertAt (int at, int key, int kind, int [] payload) {
		if (n == keys.length) {
			int next = PackedHeapGrowth.capacity (keys.length, n + 1, 16);
			keys = Arrays.copyOf (keys, next);
			kinds = Arrays.copyOf (kinds, next);
			sizes = Arrays.copyOf (sizes, next);
			data = Arrays.copyOf (data, next);
		}
		System.arraycopy (keys, at, keys, at + 1, n - at);
		System.arraycopy (kinds, at, kinds, at + 1, n - at);
		System.arraycopy (sizes, at, sizes, at + 1, n - at);
		System.arraycopy (data, at, data, at + 1, n - at);
		keys [at] = key;
		kinds [at] = kind;
		sizes [at] = 0;
		data [at] = payload;
		n++;
		return at;
	}

	private void removeAt (int at) {
		System.arraycopy (keys, at + 1, keys, at, n - at - 1);
		System.arraycopy (kinds, at + 1, kinds, at, n - at - 1);
		System.arraycopy (sizes, at + 1, sizes, at, n - at - 1);
		System.arraycopy (data, at + 1, data, at, n - at - 1);
		n--;
		data [n] = null;
	}

	// ---- container primitives (values 0..65535) --------------------------------------------------

	private boolean contains (int c, int v) {
		int [] d = data [c];
		switch (kinds [c]) {
			case BITMAP: return (d [v >>> 5] & (1 << v)) != 0;
			case ARRAY: return search (d, sizes [c], v) >= 0;
			default:
				for (int i = 0, r = sizes [c]; i < r; i++) {
					if (v < d [2 * i]) return false;
					if (v < d [2 * i + 1]) return true;
				}
				return false;
		}
	}

	private static int search (int [] d, int size, int v) {
		int lo = 0, hi = size - 1;
		while (lo <= hi) {
			int mid = (lo + hi) >>> 1;
			if (d [mid] < v) lo = mid + 1;
			else if (d [mid] > v) hi = mid - 1;
			else return mid;
		}
		return -(lo + 1);
	}

	private void toBitmap (int c) {
		if (kinds [c] == BITMAP) return;
		int [] bits = new int [BITMAP_WORDS], d = data [c];
		int card = 0;
		if (kinds [c] == ARRAY) {
			for (int i = 0; i < sizes [c]; i++) bits [d [i] >>> 5] |= 1 << d [i];
			card = sizes [c];
		} else {
			for (int i = 0; i < sizes [c]; i++) {
				setBits (bits, d [2 * i], d [2 * i + 1], true);
				card += d [2 * i + 1] - d [2 * i];
			}
		}
		kinds [c] = BITMAP;
		data [c] = bits;
		sizes [c] = card;
	}

	private static void setBits (int [] w, int from, int to, boolean on) {
		if (from >= to) return;
		int fw = from >>> 5, lw = (to - 1) >>> 5;
		int fm = -1 << from, lm = -1 >>> (31 - ((to - 1) & 31));
		if (fw == lw) {
			w [fw] = on ? w [fw] | (fm & lm) : w [fw] & ~(fm & lm);
			return;
		}
		w [fw] = on ? w [fw] | fm : w [fw] & ~fm;
		Arrays.fill (w, fw + 1, lw, on ? -1 : 0);
		w [lw] = on ? w [lw] | lm : w [lw] & ~lm;
	}

	private static int bitCount (int [] w, int from, int to) {
		if (from >= to) return 0;
		int fw = from >>> 5, lw = (to - 1) >>> 5;
		int fm = -1 << from, lm = -1 >>> (31 - ((to - 1) & 31));
		if (fw == lw) return Integer.bitCount (w [fw] & fm & lm);
		int total = Integer.bitCount (w [fw] & fm) + Integer.bitCount (w [lw] & lm);
		for (int i = fw + 1; i < lw; i++) total += Integer.bitCount (w [i]);
		return total;
	}

	/** Re-chooses the smallest representation of a bitmap container (Roaring size rule). */
	private void optimize (int c) {
		if (sizes [c] == 0) {
			removeAt (c);
			return;
		}
		if (kinds [c] != BITMAP) return;
		int [] w = data [c];
		int runCount = 0;
		boolean prev = false;
		for (int i = 0; i < BITMAP_WORDS; i++) {
			int word = w [i];
			runCount += Integer.bitCount (word & ~((word << 1) | (prev ? 1 : 0)));
			prev = word < 0;
		}
		int card = sizes [c];
		int runBytes = 8 * runCount, arrayBytes = card <= ARRAY_MAX ? 4 * card : Integer.MAX_VALUE;
		if (runBytes < 4 * BITMAP_WORDS && runBytes <= arrayBytes) {
			int [] d = new int [2 * runCount];
			int r = 0, v = nextInBitmap (w, 0);
			while (v >= 0) {
				int end = nextClearInBitmap (w, v);
				d [2 * r] = v;
				d [2 * r + 1] = end;
				r++;
				v = end >= CHUNK ? -1 : nextInBitmap (w, end);
			}
			kinds [c] = RUN;
			data [c] = d;
			sizes [c] = runCount;
		} else if (arrayBytes < 4 * BITMAP_WORDS) {
			int [] d = new int [card];
			int i = 0, v = nextInBitmap (w, 0);
			while (v >= 0) {
				d [i++] = v;
				v = v + 1 >= CHUNK ? -1 : nextInBitmap (w, v + 1);
			}
			kinds [c] = ARRAY;
			data [c] = d;
		}
	}

	private static int nextInBitmap (int [] w, int from) {
		int i = from >>> 5;
		int word = w [i] & (-1 << from);
		while (word == 0) {
			if (++i >= BITMAP_WORDS) return -1;
			word = w [i];
		}
		return (i << 5) + Integer.numberOfTrailingZeros (word);
	}

	private static int nextClearInBitmap (int [] w, int from) {
		int i = from >>> 5;
		int word = ~w [i] & (-1 << from);
		while (word == 0) {
			if (++i >= BITMAP_WORDS) return CHUNK;
			word = ~w [i];
		}
		return (i << 5) + Integer.numberOfTrailingZeros (word);
	}

	/** Rewrites container {@code c}'s runs with {@code [a, b)} set or cleared. */
	private void runRange (int c, int a, int b, boolean on) {
		int [] d = data [c];
		int r = sizes [c];
		int [] out = new int [2 * r + 4];
		int o = 0;
		boolean placed = !on;
		for (int i = 0; i < r; i++) {
			int s = d [2 * i], e = d [2 * i + 1];
			if (on) {
				if (e < a || s > b) {
					if (!placed && s > b) {
						out [o++] = a;
						out [o++] = b;
						placed = true;
					}
					out [o++] = s;
					out [o++] = e;
				} else {
					a = Math.min (a, s);
					b = Math.max (b, e);
				}
			} else if (e <= a || s >= b) {
				out [o++] = s;
				out [o++] = e;
			} else {
				if (s < a) {
					out [o++] = s;
					out [o++] = a;
				}
				if (e > b) {
					out [o++] = b;
					out [o++] = e;
				}
			}
		}
		if (!placed) {
			out [o++] = a;
			out [o++] = b;
		}
		data [c] = out;
		sizes [c] = o / 2;
	}

	private static int runCardinality (int [] d, int runs, int a, int b) {
		int total = 0;
		for (int i = 0; i < runs; i++) {
			int s = Math.max (a, d [2 * i]), e = Math.min (b, d [2 * i + 1]);
			if (e > s) total += e - s;
		}
		return total;
	}

	// ---- set operations ---------------------------------------------------------------------------

	public boolean get (int position) {
		int c = find (position >>> 16);
		return c >= 0 && contains (c, position & 0xFFFF);
	}

	public void set (int position, boolean on) {
		int key = position >>> 16, v = position & 0xFFFF;
		int c = find (key);
		if (c < 0) {
			if (!on) return;
			c = insertAt (-c - 1, key, ARRAY, new int [4]);
		}
		if (contains (c, v) == on) return;
		switch (kinds [c]) {
			case BITMAP:
				data [c] [v >>> 5] ^= 1 << v;
				sizes [c] += on ? 1 : -1;
				if (sizes [c] == 0) removeAt (c);
				return;
			case ARRAY:
				arraySet (c, v, on);
				return;
			default:
				runRange (c, v, v + 1, on);
				if (sizes [c] == 0) removeAt (c);
				else if (sizes [c] > RUN_MAX) toBitmap (c);
		}
	}

	private void arraySet (int c, int v, boolean on) {
		int [] d = data [c];
		int size = sizes [c], at = search (d, size, v);
		if (on) {
			if (size == ARRAY_MAX) {
				toBitmap (c);
				data [c] [v >>> 5] |= 1 << v;
				sizes [c]++;
				return;
			}
			at = -at - 1;
			if (size == d.length) d = data [c] = Arrays.copyOf (d, PackedHeapGrowth.capacity (d.length, size + 1, 4));
			System.arraycopy (d, at, d, at + 1, size - at);
			d [at] = v;
			sizes [c] = size + 1;
		} else {
			System.arraycopy (d, at + 1, d, at, size - at - 1);
			sizes [c] = size - 1;
			if (size == 1) removeAt (c);
		}
	}

	/** Sets or clears {@code [from, to)}; clearing visits only existing containers. */
	public void setRange (int from, int to, boolean on) {
		if (from >= to) return;
		int first = from >>> 16, last = (to - 1) >>> 16;
		if (!on) {
			int c = find (first);
			if (c < 0) c = -c - 1;
			while (c < n && keys [c] <= last) {
				int before = n;
				apply (c, keys [c], first, last, from, to, false);
				if (n == before) c++;
			}
			return;
		}
		for (int key = first; key <= last; key++) {
			int c = find (key);
			if (c < 0) c = insertAt (-c - 1, key, RUN, new int [0]);
			apply (c, key, first, last, from, to, true);
		}
	}

	private void apply (int c, int key, int first, int last, int from, int to, boolean on) {
		int a = key == first ? from & 0xFFFF : 0, b = key == last ? ((to - 1) & 0xFFFF) + 1 : CHUNK;
		if (a == 0 && b == CHUNK) {
			if (!on) {
				removeAt (c);
				return;
			}
			kinds [c] = RUN;
			data [c] = new int [] {0, CHUNK};
			sizes [c] = 1;
			return;
		}
		if (kinds [c] == RUN) {
			runRange (c, a, b, on);
			if (sizes [c] == 0) removeAt (c);
			else if (sizes [c] > RUN_MAX) toBitmap (c);
			return;
		}
		if (kinds [c] == ARRAY && b - a <= 16) {
			int base = keys [c] << 16;
			for (int v = a; v < b && c < n && keys [c] == key; v++) set (base | v, on);
			return;
		}
		toBitmap (c);
		int [] w = data [c];
		int before = bitCount (w, a, b);
		setBits (w, a, b, on);
		sizes [c] += on ? (b - a) - before : -before;
		optimize (c);
	}

	public int count (int from, int to) {
		if (from >= to) return 0;
		int first = from >>> 16, last = (to - 1) >>> 16, total = 0;
		int c = find (first);
		if (c < 0) c = -c - 1;
		for (; c < n && keys [c] <= last; c++) {
			int key = keys [c];
			int a = key == first ? from & 0xFFFF : 0, b = key == last ? ((to - 1) & 0xFFFF) + 1 : CHUNK;
			int [] d = data [c];
			if (a == 0 && b == CHUNK && kinds [c] != RUN) {
				total += sizes [c];
			} else if (kinds [c] == BITMAP) {
				total += bitCount (d, a, b);
			} else if (kinds [c] == ARRAY) {
				int lo = search (d, sizes [c], a), hi = search (d, sizes [c], b);
				total += (hi < 0 ? -hi - 1 : hi) - (lo < 0 ? -lo - 1 : lo);
			} else {
				total += runCardinality (d, sizes [c], a, b);
			}
		}
		return total;
	}

	/** First set position at or after {@code from} and below {@code limit}, or -1. */
	public int next (int from, int limit) {
		if (from < 0 || from >= limit) return -1;
		int key = from >>> 16;
		int c = find (key);
		if (c < 0) c = -c - 1;
		for (; c < n; c++) {
			int found = nextIn (c, keys [c] == key ? from & 0xFFFF : 0);
			if (found >= 0) {
				int position = (keys [c] << 16) | found;
				return position < limit ? position : -1;
			}
		}
		return -1;
	}

	private int nextIn (int c, int v) {
		int [] d = data [c];
		switch (kinds [c]) {
			case BITMAP: return nextInBitmap (d, v);
			case ARRAY: {
				int at = search (d, sizes [c], v);
				if (at < 0) at = -at - 1;
				return at < sizes [c] ? d [at] : -1;
			}
			default:
				for (int i = 0; i < sizes [c]; i++) {
					if (v < d [2 * i + 1]) return Math.max (v, d [2 * i]);
				}
				return -1;
		}
	}

	/** End (exclusive) of the set run that contains {@code position}; {@code position} when clear. */
	public int runEnd (int position, int limit) {
		int end = position;
		while (end < limit) {
			int c = find (end >>> 16);
			if (c < 0) return end;
			int v = end & 0xFFFF, base = keys [c] << 16;
			int [] d = data [c];
			int next;
			switch (kinds [c]) {
				case BITMAP:
					next = base + nextClearInBitmap (d, v);
					break;
				case RUN: {
					next = end;
					for (int i = 0; i < sizes [c]; i++) {
						if (v >= d [2 * i] && v < d [2 * i + 1]) next = base + d [2 * i + 1];
					}
					break;
				}
				default:
					next = contains (c, v) ? end + 1 : end;
			}
			if (next == end) return end;
			end = next;
		}
		return limit;
	}

	/** Moves every set position at or after {@code from} by {@code delta}; cost O(runs after from). */
	public void shift (int from, int delta, int limit, int [][] scratch) {
		int r = 0, position = next (from, limit);
		int [] runs = scratch [0];
		while (position >= 0) {
			int end = runEnd (position, limit);
			if (2 * r + 2 > runs.length) runs = scratch [0] = Arrays.copyOf (runs, PackedHeapGrowth.capacity (runs.length, 2 * r + 2, 4));
			runs [2 * r] = position;
			runs [2 * r + 1] = end;
			r++;
			position = next (end, limit);
		}
		setRange (from, limit, false);
		for (int i = 0; i < r; i++) {
			if (runs [2 * i + 1] - runs [2 * i] == 1) set (runs [2 * i] + delta, true);
			else setRange (runs [2 * i] + delta, runs [2 * i + 1] + delta, true);
		}
	}

	public boolean isEmpty () {
		return n == 0;
	}

	/** Heap bytes of containers and chunk tables (object headers estimated at 16 bytes). */
	public int bytes () {
		int total = 16 + keys.length * 16;
		for (int c = 0; c < n; c++) total += 16 + data [c].length * 4;
		return total;
	}
}
