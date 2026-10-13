/*
 * Copyright 2026 Hitesh Soliwal and contributors
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 *
 * Dual-licensed by the rights holder's decision of 2026-10-13: you may use this file under the
 * Eclipse Public License 2.0 (https://www.eclipse.org/legal/epl-2.0/) or the Apache License 2.0
 * (https://www.apache.org/licenses/LICENSE-2.0).
 */
package org.eclipse.swt.internal.residency;

/**
 * SetData negotiation of {@link ItemResidency}: the engine, not the caller, decides which items
 * request data. The viewport arrives as item runs in priority order (visible, overscan, prefetch);
 * each run is requested as contiguous ranges of items that are neither loaded nor requested.
 * Outstanding requests outside every run are cancelled in ranges. An item restored from HOT, WARM
 * or COLD is loaded and is never requested again; only a DESTROYED item is requested again.
 */
public final class ItemRequests {
	int rangesRequested, itemsRequested, rangesCancelled, itemsCancelled;
	private int [] keep = new int [16];
	private int keepCount;

	/** Cancels every outstanding request outside {@code runs[0, 2*runCount)}. */
	public void cancelOutside (ItemResidency owner, ItemLanes lanes, ItemResidency.DataSource source, int [] runs, int runCount) {
		sortKeep (runs, runCount);
		int i = lanes.nextSet (ItemResidency.LANE_DATA_REQUESTED, 0);
		while (i >= 0) {
			int k = covering (i);
			if (k >= 0) {
				int end = keep [2 * k + 1];
				i = end >= lanes.count ? -1 : lanes.nextSet (ItemResidency.LANE_DATA_REQUESTED, end);
				continue;
			}
			int end = i + 1;
			while (end < lanes.count && covering (end) < 0 && lanes.get (ItemResidency.LANE_DATA_REQUESTED, end)) end++;
			cancel (owner, lanes, source, i, end);
			i = end >= lanes.count ? -1 : lanes.nextSet (ItemResidency.LANE_DATA_REQUESTED, end);
		}
	}

	/** Cancels every outstanding request (structural change). */
	public void cancelAll (ItemResidency owner, ItemLanes lanes, ItemResidency.DataSource source) {
		cancelOutside (owner, lanes, source, keep, 0);
	}

	private void sortKeep (int [] runs, int runCount) {
		if (keep.length < 2 * runCount) keep = new int [2 * runCount];
		if (runs != keep) System.arraycopy (runs, 0, keep, 0, 2 * runCount);
		keepCount = runCount;
		for (int a = 1; a < runCount; a++) {
			int s = keep [2 * a], e = keep [2 * a + 1], b = a - 1;
			while (b >= 0 && keep [2 * b] > s) {
				keep [2 * b + 2] = keep [2 * b];
				keep [2 * b + 3] = keep [2 * b + 1];
				b--;
			}
			keep [2 * b + 2] = s;
			keep [2 * b + 3] = e;
		}
	}

	private int covering (int item) {
		int lo = 0, hi = keepCount - 1;
		while (lo <= hi) {
			int mid = (lo + hi) >>> 1;
			if (keep [2 * mid + 1] <= item) lo = mid + 1;
			else if (keep [2 * mid] > item) hi = mid - 1;
			else return mid;
		}
		return -1;
	}

	private void cancel (ItemResidency owner, ItemLanes lanes, ItemResidency.DataSource source, int from, int to) {
		lanes.setRange (ItemResidency.LANE_DATA_REQUESTED, from, to, false);
		rangesCancelled = ItemResidency.inc (rangesCancelled);
		itemsCancelled = ItemResidency.add (itemsCancelled, to - from);
		if (source != null) source.cancel (owner, from, to);
	}

	/** Requests every run of unloaded, unrequested items in {@code [from, to)}. */
	public void request (ItemResidency owner, ItemLanes lanes, ItemResidency.DataSource source, int from, int to) {
		int i = from;
		while (i < to) {
			if (needs (lanes, i)) {
				int end = i + 1;
				while (end < to && needs (lanes, end)) end++;
				lanes.setRange (ItemResidency.LANE_DATA_REQUESTED, i, end, true);
				rangesRequested = ItemResidency.inc (rangesRequested);
				itemsRequested = ItemResidency.add (itemsRequested, end - i);
				source.request (owner, i, end);
				i = end;
			} else {
				i++;
			}
		}
	}

	private static boolean needs (ItemLanes lanes, int item) {
		return !lanes.get (ItemResidency.LANE_DATA_LOADED, item) && !lanes.get (ItemResidency.LANE_DATA_REQUESTED, item);
	}

	/** Cancels what left the viewport, then requests the runs in priority order. */
	public void negotiate (ItemResidency owner, ItemLanes lanes, ItemResidency.DataSource source, int [] runs, int runCount) {
		cancelOutside (owner, lanes, source, runs, runCount);
		if (source == null) return;
		for (int r = 0; r < runCount; r++) {
			request (owner, lanes, source, runs [2 * r], runs [2 * r + 1]);
		}
	}
}
