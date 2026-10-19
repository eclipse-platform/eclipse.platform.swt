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
 * Reports the change of an M3ColumnFilter from its old and new kept masks, word by word: the
 * structural change of the source first (RESET, INSERTED with the inserted rows that pass,
 * REMOVED), then the rows that left (old & ~new), the rows that entered (new & ~old, outside an
 * inserted range), and the changed rows that stayed (UPDATED). Damage is in view rows (source
 * order), so a row's view position is its rank in the new mask.
 */
final class M3FilterDelta {

	private final M3ColumnFilter filter;
	private final M3RowChange change;

	M3FilterDelta (M3ColumnFilter filter) {
		this.filter = filter;
		this.change = filter.change;
	}

	void report (M3RowChange in, long [] previous, long [] kept, int n) {
		if (!filter.hasListeners ()) return;
		int kind = in == null ? 0 : in.kind;
		int words = M3RowMask.words (n), last = filter.size () - 1;
		if (kind == M3RowChange.RESET) {
			change.set (M3RowChange.RESET, 0, 0);
			change.damage (0, last);
			filter.fire ();
			return;
		}
		int skipStart = 0, skipEnd = 0;
		if (kind == M3RowChange.INSERTED || kind == M3RowChange.REMOVED) {
			change.set (kind, in.start, in.count);
			if (kind == M3RowChange.INSERTED) {
				skipStart = in.start;
				skipEnd = in.start + in.count;
				for (int row = skipStart; row < skipEnd; row++) {
					if (M3RowMask.get (kept, row)) change.add (row);
				}
			}
			change.damage (filter.rankBefore (in.start), last);
			filter.fire ();
		}
		emit (M3RowChange.LEFT, previous, kept, words, 0, 0);
		emit (M3RowChange.ENTERED, kept, previous, words, skipStart, skipEnd);
		if (kind == M3RowChange.UPDATED) updated (in, previous, kept);
	}

	/* fires the rows of a & ~b outside [skipStart, skipEnd), if any */
	private void emit (int kind, long [] a, long [] b, int words, int skipStart, int skipEnd) {
		change.set (kind, 0, 0);
		int first = -1;
		for (int w = 0; w < words; w++) {
			for (long x = a [w] & ~b [w]; x != 0; x &= x - 1) {
				int row = w << 6 | Long.numberOfTrailingZeros (x);
				if (row >= skipStart && row < skipEnd) continue;
				if (first < 0) first = row;
				change.add (row);
			}
		}
		if (change.rowCount == 0) return;
		change.damage (filter.rankBefore (first), filter.size () - 1);
		filter.fire ();
	}

	private void updated (M3RowChange in, long [] previous, long [] kept) {
		change.set (M3RowChange.UPDATED, 0, 0);
		for (int i = 0; i < in.rowCount; i++) {
			int row = in.rows [i];
			if (M3RowMask.get (kept, row) && M3RowMask.get (previous, row)) {
				change.add (row);
				int position = filter.rankBefore (row);
				change.damage (position, position);
			}
		}
		if (change.rowCount > 0) filter.fire ();
	}
}
