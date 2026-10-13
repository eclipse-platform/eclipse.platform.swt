/*
 * Copyright 2026 Hitesh Soliwal and contributors
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 *
 * Dual-licensed by the rights holder's decision of 2026-10-13: you may use this file under the
 * Eclipse Public License 2.0 (https://www.eclipse.org/legal/epl-2.0/) or the Apache License 2.0
 * (https://www.apache.org/licenses/LICENSE-2.0).
 */
package org.eclipse.swt.internal.residency;

import java.util.Arrays;

import org.eclipse.swt.internal.PackedHeapGrowth;

/**
 * Per-item state lanes of {@link ItemResidency}: masks, flags and patterns, never per-item objects.
 * Each lane is a rule stack evaluated per item with this precedence:
 * <ol>
 * <li><b>explicit exception</b> (an {@link ItemBitSet} override bit plus its value bit),</li>
 * <li><b>pattern</b>: packed {@code (start, end, stride, repeatMask, phase)}; item {@code i} in
 * {@code [start, end)} is set when bit {@code (i - start + phase) % stride} of {@code repeatMask}
 * is set ("every 3rd row" is stride 3, mask 0b001),</li>
 * <li><b>default</b>: 0, or the sparse value bits outside every pattern.</li>
 * </ol>
 * Pattern regions are disjoint; a mutation splits patterns only where items deviate. Memory is
 * O(exceptions + patterns + non-default runs), not O(items). {@code int} only (GWT).
 */
public final class ItemLanes {
	public static final int P_START = 0, P_END = 1, P_STRIDE = 2, P_MASK = 3, P_PHASE = 4, P = 5;

	final int laneCount;
	final ItemBitSet [] value, override;
	final int [][] patterns;
	final int [] patternCount;
	int count;
	private final int [][] scratch = {new int [16]};

	public ItemLanes (int laneCount) {
		this.laneCount = laneCount;
		value = new ItemBitSet [laneCount];
		override = new ItemBitSet [laneCount];
		patterns = new int [laneCount][];
		patternCount = new int [laneCount];
		for (int lane = 0; lane < laneCount; lane++) {
			value [lane] = new ItemBitSet ();
			patterns [lane] = new int [0];
		}
	}

	// ---- pattern arithmetic ---------------------------------------------------------------------

	/** Index of the pattern covering {@code item}, or -1. */
	public int pattern (int lane, int item) {
		int [] p = patterns [lane];
		int lo = 0, hi = patternCount [lane] - 1;
		while (lo <= hi) {
			int mid = (lo + hi) >>> 1;
			if (p [mid * P + P_END] <= item) lo = mid + 1;
			else if (p [mid * P + P_START] > item) hi = mid - 1;
			else return mid;
		}
		return -1;
	}

	private static boolean patternBit (int [] p, int k, int item) {
		int stride = p [k * P + P_STRIDE];
		int phase = (item - p [k * P + P_START]) % stride + p [k * P + P_PHASE];
		if (phase >= stride) phase -= stride;
		return (p [k * P + P_MASK] >>> phase & 1) != 0;
	}

	private static int patternCountIn (int [] p, int k, int from, int to) {
		int stride = p [k * P + P_STRIDE], mask = p [k * P + P_MASK];
		if (stride < 32) mask &= (1 << stride) - 1;
		int length = to - from;
		int total = length / stride * Integer.bitCount (mask);
		int phase = (from - p [k * P + P_START]) % stride + p [k * P + P_PHASE];
		for (int i = 0, rest = length % stride; i < rest; i++, phase++) {
			if (phase >= stride) phase -= stride;
			total += mask >>> phase & 1;
		}
		return total;
	}

	/** First pattern-set item of pattern {@code k} at or after {@code from}, or -1. */
	private static int patternNext (int [] p, int k, int from) {
		int start = p [k * P + P_START], end = p [k * P + P_END], stride = p [k * P + P_STRIDE];
		int item = Math.max (from, start);
		for (int i = 0; i < stride && item < end; i++, item++) {
			if (patternBit (p, k, item)) return item;
		}
		return -1;
	}

	private void ensurePatterns (int lane, int extra) {
		int need = (patternCount [lane] + extra) * P;
		if (need > patterns [lane].length) {
			patterns [lane] = Arrays.copyOf (patterns [lane], PackedHeapGrowth.capacity (patterns [lane].length, need, 4));
		}
	}

	/** Removes pattern coverage from {@code [from, to)}, splitting patterns at the edges. */
	private void cutPatterns (int lane, int from, int to) {
		if (patternCount [lane] == 0 || from >= to) return;
		ensurePatterns (lane, 1);
		int [] p = patterns [lane];
		int w = 0, n = patternCount [lane];
		int [] out = new int [(n + 1) * P];
		for (int k = 0; k < n; k++) {
			int s = p [k * P + P_START], e = p [k * P + P_END];
			if (e <= from || s >= to) {
				System.arraycopy (p, k * P, out, w * P, P);
				w++;
				continue;
			}
			if (s < from) {
				System.arraycopy (p, k * P, out, w * P, P);
				out [w * P + P_END] = from;
				w++;
			}
			if (e > to) {
				System.arraycopy (p, k * P, out, w * P, P);
				out [w * P + P_START] = to;
				out [w * P + P_PHASE] = phaseAt (p, k, to);
				w++;
			}
		}
		patterns [lane] = out;
		patternCount [lane] = w;
		if (override [lane] != null) override [lane].setRange (from, to, false);
	}

	private static int phaseAt (int [] p, int k, int item) {
		int stride = p [k * P + P_STRIDE];
		int phase = (item - p [k * P + P_START]) % stride + p [k * P + P_PHASE];
		return phase >= stride ? phase - stride : phase;
	}

	/**
	 * Applies a rule to {@code [start, end)}: state becomes the pattern; exceptions and value bits in
	 * the range are dropped. O(1) memory regardless of the range length.
	 */
	public void setPattern (int lane, int start, int end, int stride, int repeatMask) {
		if (stride < 1 || stride > 32) throw new IllegalArgumentException ("stride 1..32");
		if (start >= end) return;
		cutPatterns (lane, start, end);
		value [lane].setRange (start, end, false);
		if (override [lane] != null) override [lane].setRange (start, end, false);
		ensurePatterns (lane, 1);
		int [] p = patterns [lane];
		int n = patternCount [lane], at = 0;
		while (at < n && p [at * P + P_START] < start) at++;
		System.arraycopy (p, at * P, p, (at + 1) * P, (n - at) * P);
		p [at * P + P_START] = start;
		p [at * P + P_END] = end;
		p [at * P + P_STRIDE] = stride;
		p [at * P + P_MASK] = repeatMask;
		p [at * P + P_PHASE] = 0;
		patternCount [lane] = n + 1;
	}

	// ---- lane operations --------------------------------------------------------------------------

	public boolean get (int lane, int item) {
		if (patternCount [lane] == 0) return value [lane].get (item);
		int k = pattern (lane, item);
		if (k < 0) return value [lane].get (item);
		ItemBitSet o = override [lane];
		return o != null && o.get (item) ? value [lane].get (item) : patternBit (patterns [lane], k, item);
	}

	public void set (int lane, int item, boolean on) {
		int k = patternCount [lane] == 0 ? -1 : pattern (lane, item);
		if (k < 0) {
			value [lane].set (item, on);
			return;
		}
		if (patternBit (patterns [lane], k, item) == on) {
			if (override [lane] != null) override [lane].set (item, false);
			value [lane].set (item, false);
			return;
		}
		if (override [lane] == null) override [lane] = new ItemBitSet ();
		override [lane].set (item, true);
		value [lane].set (item, on);
	}

	/** Sets or clears {@code [from, to)} explicitly; patterns there are cut away. */
	public void setRange (int lane, int from, int to, boolean on) {
		if (from >= to) return;
		cutPatterns (lane, from, to);
		value [lane].setRange (from, to, on);
	}

	public int countRange (int lane, int from, int to) {
		if (from >= to) return 0;
		if (patternCount [lane] == 0) return value [lane].count (from, to);
		int [] p = patterns [lane];
		int total = 0, at = from;
		for (int k = 0; k < patternCount [lane] && at < to; k++) {
			int s = p [k * P + P_START], e = p [k * P + P_END];
			if (e <= at) continue;
			if (s > at) {
				total += value [lane].count (at, Math.min (s, to));
				at = Math.min (s, to);
			}
			if (at >= to) break;
			int b = Math.min (e, to);
			total += patternCountIn (p, k, at, b) + exceptionDelta (lane, k, at, b);
			at = b;
		}
		if (at < to) total += value [lane].count (at, to);
		return total;
	}

	/** Effect of explicit exceptions inside pattern {@code k} over {@code [a, b)}: O(exceptions). */
	private int exceptionDelta (int lane, int k, int a, int b) {
		ItemBitSet o = override [lane];
		if (o == null) return 0;
		int delta = 0;
		for (int item = o.next (a, b); item >= 0; item = o.next (item + 1, b)) {
			delta += (value [lane].get (item) ? 1 : 0) - (patternBit (patterns [lane], k, item) ? 1 : 0);
		}
		return delta;
	}

	/** First set item at or after {@code from}, or -1. */
	public int nextSet (int lane, int from) {
		if (from < 0 || from >= count) return -1;
		int explicit = value [lane].next (from, count);
		if (patternCount [lane] == 0) return explicit;
		int [] p = patterns [lane];
		ItemBitSet o = override [lane];
		int k = pattern (lane, from);
		if (k < 0) {
			k = 0;
			while (k < patternCount [lane] && p [k * P + P_END] <= from) k++;
		}
		int best = -1;
		for (int at = from; k < patternCount [lane]; k++) {
			for (int item = patternNext (p, k, Math.max (at, p [k * P + P_START])); item >= 0 && item < count;
					item = patternNext (p, k, item + 1)) {
				if (o == null || !o.get (item) || value [lane].get (item)) {
					best = item;
					break;
				}
				if (explicit >= 0 && item > explicit) break;
			}
			if (best >= 0 || (explicit >= 0 && p [k * P + P_START] > explicit)) break;
		}
		if (explicit < 0) return best;
		return best < 0 ? explicit : Math.min (best, explicit);
	}

	/** All lanes of one item as a mask (bit = lane). */
	public int gather (int item) {
		int mask = 0;
		for (int lane = 0; lane < laneCount; lane++) {
			if (get (lane, item)) mask |= 1 << lane;
		}
		return mask;
	}

	/** Lane masks of {@code [from, to)} into {@code out}: plain lanes walk set bits only. */
	public void gather (int from, int to, int [] out) {
		Arrays.fill (out, 0, to - from, 0);
		for (int lane = 0; lane < laneCount; lane++) {
			int bit = 1 << lane;
			if (patternCount [lane] == 0) {
				for (int i = value [lane].next (from, to); i >= 0; i = value [lane].next (i + 1, to)) out [i - from] |= bit;
			} else {
				for (int i = from; i < to; i++) if (get (lane, i)) out [i - from] |= bit;
			}
		}
	}

	public void setCount (int items) {
		if (items < count) {
			for (int lane = 0; lane < laneCount; lane++) setRange (lane, items, count, false);
		}
		count = items;
	}

	/** Opens a default (0) item at {@code index} in every lane; later items move up by one. */
	public void insert (int index) {
		for (int lane = 0; lane < laneCount; lane++) {
			splitAt (lane, index);
			shiftPatterns (lane, index, 1);
			value [lane].shift (index, 1, count, scratch);
			if (override [lane] != null) override [lane].shift (index, 1, count, scratch);
		}
		count++;
	}

	/** Closes {@code index} in every lane; later items move down by one keeping their state. */
	public void remove (int index) {
		for (int lane = 0; lane < laneCount; lane++) {
			splitAt (lane, index);
			splitAt (lane, index + 1);
			cutPatterns (lane, index, index + 1);
			value [lane].set (index, false);
			shiftPatterns (lane, index + 1, -1);
			value [lane].shift (index + 1, -1, count, scratch);
			if (override [lane] != null) override [lane].shift (index + 1, -1, count, scratch);
		}
		count--;
	}

	/** Splits the pattern containing {@code item} so that a pattern boundary falls at {@code item}. */
	private void splitAt (int lane, int item) {
		int k = patternCount [lane] == 0 ? -1 : pattern (lane, item);
		if (k < 0 || patterns [lane] [k * P + P_START] == item) return;
		ensurePatterns (lane, 1);
		int [] p = patterns [lane];
		int n = patternCount [lane];
		System.arraycopy (p, k * P, p, (k + 1) * P, (n - k) * P);
		p [k * P + P_END] = item;
		p [(k + 1) * P + P_START] = item;
		p [(k + 1) * P + P_PHASE] = phaseAt (p, k, item);
		patternCount [lane] = n + 1;
	}

	private void shiftPatterns (int lane, int from, int delta) {
		int [] p = patterns [lane];
		for (int k = 0; k < patternCount [lane]; k++) {
			if (p [k * P + P_START] >= from) {
				p [k * P + P_START] += delta;
				p [k * P + P_END] += delta;
			}
		}
	}

	/** Heap bytes of value, exception and pattern storage (headers estimated). */
	public int bytes () {
		int total = 0;
		for (int lane = 0; lane < laneCount; lane++) {
			total += value [lane].bytes () + patterns [lane].length * 4 + 16;
			if (override [lane] != null) total += override [lane].bytes ();
		}
		return total;
	}
}
