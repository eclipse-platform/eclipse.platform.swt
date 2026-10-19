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

import java.util.*;
import java.util.function.*;

/*
 * The sorting algorithms of M3ColumnSort over int lanes, with their reusable scratch lanes
 * (ALGORITHM_CATALOGUE.tsv in docs/m3/sort-filter):
 * - multi-key order: every key becomes an unsigned radix key per row; LSD radix sort, least
 *   significant key first, one stable counting pass per byte, skipping bytes equal in every row;
 * - comparator keys (text, rows): dense ranks from one stable merge sort (coordinate compression);
 * - small inputs: stable merge sort with the key chain comparator.
 */
final class M3SortLanes {

	/* below this many rows the comparator merge sort beats radix keys (MEASUREMENTS.md) */
	static final int MERGE_CUTOVER = 48;
	private static final int RUN = 32;

	int [] rowTmp = new int [0], idx = new int [0], idxTmp = new int [0];
	long [] keys = new long [0], keyTmp = new long [0];
	String [] texts = new String [0];
	private final int [] counts = new int [8 << 8];

	void ensure (int n) {
		if (keys.length >= n) return;
		int capacity = Math.max (n, keys.length + (keys.length >> 1));
		rowTmp = new int [capacity];
		idx = new int [capacity];
		idxTmp = new int [capacity];
		keys = new long [capacity];
		keyTmp = new long [capacity];
	}

	String [] texts (int n) {
		if (texts.length < n) texts = new String [Math.max (n, texts.length + (texts.length >> 1))];
		return texts;
	}

	void releaseTexts (int n) {
		Arrays.fill (texts, 0, n, null);
	}

	/* orders rows [0, n) by the key chain; rows whose keys are all equal keep ascending source order */
	void sort (int [] rows, int n, M3ColumnKey [] chain, boolean [] descending, int keyCount, IntBinaryOperator compare) {
		if (n < 2) return;
		ensure (n);
		if (keyCount > 0 && n < MERGE_CUTOVER) {
			mergeSort (rows, n, rowTmp, compare);
			return;
		}
		sourceOrder (rows, n);
		for (int j = keyCount - 1; j >= 0; j--) {
			int bytes = chain [j].radixKeys (rows, n, this);
			if (descending [j]) {
				long mask = bytes == 8 ? -1L : (1L << (bytes << 3)) - 1;
				for (int i = 0; i < n; i++) keys [i] ^= mask;
			}
			radixSort (rows, n, bytes);
		}
	}

	/* puts rows [0, n) in ascending order (they are distinct source rows) */
	void sourceOrder (int [] rows, int n) {
		int i = 1;
		while (i < n && rows [i - 1] < rows [i]) i++;
		if (i >= n) return;
		ensure (n);
		for (int k = 0; k < n; k++) keys [k] = rows [k];
		radixSort (rows, n, 4);
	}

	/* stable LSD radix sort of rows [0, n) by keys [0, n) (unsigned, the low `bytes` bytes) */
	void radixSort (int [] rows, int n, int bytes) {
		Arrays.fill (counts, 0, bytes << 8, 0);
		for (int i = 0; i < n; i++) {
			long key = keys [i];
			for (int b = 0; b < bytes; b++) counts [b << 8 | (int) (key >>> (b << 3)) & 0xFF]++;
		}
		int [] r = rows, rt = rowTmp;
		long [] k = keys, kt = keyTmp;
		for (int b = 0; b < bytes; b++) {
			int base = b << 8, shift = b << 3;
			if (counts [base | (int) (k [0] >>> shift) & 0xFF] == n) continue;
			for (int d = 0, sum = 0; d < 256; d++) {
				int c = counts [base | d];
				counts [base | d] = sum;
				sum += c;
			}
			for (int i = 0; i < n; i++) {
				int p = counts [base | (int) (k [i] >>> shift) & 0xFF]++;
				rt [p] = r [i];
				kt [p] = k [i];
			}
			int [] swapRows = r; r = rt; rt = swapRows;
			long [] swapKeys = k; k = kt; kt = swapKeys;
		}
		if (r != rows) {
			System.arraycopy (r, 0, rows, 0, n);
			System.arraycopy (k, 0, keys, 0, n);
		}
	}

	/* dense ranks (0, 1, ...) of the items 0 .. n-1 under the comparator of item indices, into keys; returns the largest rank */
	long ranks (int n, IntBinaryOperator compareItems) {
		for (int i = 0; i < n; i++) idx [i] = i;
		mergeSort (idx, n, idxTmp, compareItems);
		long rank = 0;
		if (n > 0) keys [idx [0]] = 0;
		for (int k = 1; k < n; k++) {
			if (compareItems.applyAsInt (idx [k - 1], idx [k]) != 0) rank++;
			keys [idx [k]] = rank;
		}
		return rank;
	}

	static int bytesFor (long maxKey) {
		return Math.max (1, (64 - Long.numberOfLeadingZeros (maxKey) + 7) >> 3);
	}

	/* stable merge sort of a [0, n): insertion-sorted runs, then bottom-up merges (an ordered pair of runs is copied) */
	static void mergeSort (int [] a, int n, int [] tmp, IntBinaryOperator compare) {
		for (int low = 0; low < n; low += RUN) insertionSort (a, low, Math.min (low + RUN, n), compare);
		int [] from = a, to = tmp;
		for (int width = RUN; width < n; width <<= 1) {
			for (int low = 0; low < n; low += width << 1) {
				merge (from, to, low, Math.min (low + width, n), Math.min (low + (width << 1), n), compare);
			}
			int [] swap = from; from = to; to = swap;
		}
		if (from != a) System.arraycopy (from, 0, a, 0, n);
	}

	private static void insertionSort (int [] a, int low, int high, IntBinaryOperator compare) {
		for (int i = low + 1; i < high; i++) {
			int item = a [i], j = i - 1;
			while (j >= low && compare.applyAsInt (a [j], item) > 0) {
				a [j + 1] = a [j];
				j--;
			}
			a [j + 1] = item;
		}
	}

	private static void merge (int [] from, int [] to, int low, int middle, int high, IntBinaryOperator compare) {
		if (middle >= high || compare.applyAsInt (from [middle - 1], from [middle]) <= 0) {
			System.arraycopy (from, low, to, low, high - low);
			return;
		}
		int left = low, right = middle, out = low;
		while (left < middle && right < high) {
			to [out++] = compare.applyAsInt (from [right], from [left]) < 0 ? from [right++] : from [left++];
		}
		System.arraycopy (from, left, to, out, middle - left);
		System.arraycopy (from, right, to, out + middle - left, high - right);
	}
}
