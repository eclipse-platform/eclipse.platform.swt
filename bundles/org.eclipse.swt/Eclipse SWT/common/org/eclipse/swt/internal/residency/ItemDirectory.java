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
 * Item-to-payload directory of {@link ItemResidency}: paged {@code int} entries, 4096 per page.
 * A page exists only while it holds a non-zero entry. Entry {@code > 0} is a resident slot + 1,
 * {@code < 0} is -(spill record + 1), {@code 0} is no payload.
 */
public final class ItemDirectory {
	public static final int SHIFT = 12, SIZE = 1 << SHIFT, MASK = SIZE - 1;

	int [][] pages = new int [4][];
	int [] used = new int [4];
	int pageCount;

	public int get (int item) {
		int p = item >>> SHIFT;
		if (p >= pages.length || pages [p] == null) return 0;
		return pages [p] [item & MASK];
	}

	public void put (int item, int value) {
		int p = item >>> SHIFT;
		ensurePages (p + 1);
		int [] page = pages [p];
		if (page == null) {
			if (value == 0) return;
			page = allocate (p);
		}
		int old = page [item & MASK];
		page [item & MASK] = value;
		account (p, old, value);
	}

	/** Moves entries {@code [index, count)} up by one; the entry at {@code index} becomes 0. */
	public void insert (int index, int count) {
		int ip = index >>> SHIFT, lp = count >>> SHIFT;
		ensurePages (lp + 1);
		for (int p = lp; p >= ip; p--) {
			int start = p == ip ? index & MASK : 0;
			int carry = p > ip ? valueAt (p - 1, MASK) : 0;
			int [] page = pages [p];
			if (page == null) {
				if (carry == 0) continue;
				page = allocate (p);
			}
			int out = page [MASK];
			System.arraycopy (page, start, page, start + 1, MASK - start);
			page [start] = carry;
			account (p, out, carry);
		}
	}

	/** Moves entries {@code (index, count)} down by one; the entry at {@code index} is dropped. */
	public void remove (int index, int count) {
		int ip = index >>> SHIFT, lp = (count - 1) >>> SHIFT;
		for (int p = ip; p <= lp && p < pages.length; p++) {
			int start = p == ip ? index & MASK : 0;
			int incoming = p < lp ? valueAt (p + 1, 0) : 0;
			int [] page = pages [p];
			if (page == null) {
				if (incoming == 0) continue;
				page = allocate (p);
			}
			int out = page [start];
			System.arraycopy (page, start + 1, page, start, MASK - start);
			page [MASK] = incoming;
			account (p, out, incoming);
		}
	}

	private int valueAt (int p, int offset) {
		return p < pages.length && pages [p] != null ? pages [p] [offset] : 0;
	}

	private int [] allocate (int p) {
		pageCount++;
		return pages [p] = new int [SIZE];
	}

	private void account (int p, int out, int in) {
		used [p] += (in != 0 ? 1 : 0) - (out != 0 ? 1 : 0);
		if (used [p] == 0 && pages [p] != null) {
			pages [p] = null;
			pageCount--;
		}
	}

	private void ensurePages (int n) {
		if (n <= pages.length) return;
		int next = PackedHeapGrowth.capacity (pages.length, n, 4);
		pages = Arrays.copyOf (pages, next);
		used = Arrays.copyOf (used, next);
	}

	/** Heap bytes of allocated pages (directory spine excluded). */
	public int bytes () {
		return pageCount * SIZE * 4;
	}
}
