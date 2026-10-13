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

import org.eclipse.swt.internal.*;

/**
 * Compatibility adapter over the shared logical selection algebra.
 *
 * <p>The canonical range/complement representation lives in
 * {@link ViewportRuntime.Selection}; this type preserves existing widget call
 * sites while all viewport-driven controls converge on the same selection
 * semantics.</p>
 *
 * <p>Hybrid mode (additive, {@link #VirtualSelectionModel(boolean)}): sorted
 * int range pairs hold contiguous runs (and select-all as a complement); when a
 * scattered selection needs more ranges than a bitmap needs words (more than
 * {@code max(16, logicalCount / 64)} ranges) the selection moves to an
 * {@code int[]} bitmap with a maintained {@code Integer.bitCount} total, and it
 * returns to ranges on {@link #clear()} and {@link #selectAll()}. Results are
 * the same in both containers; ranges of a bitmap are computed on demand.
 * Only ints are used (GWT-safe).</p>
 */
final class VirtualSelectionModel {
	private final ViewportRuntime.Selection selection = new ViewportRuntime.Selection ();
	private final boolean hybrid;
	/* bitmap container, non-null while active: row i is bit (i & 31) of word i >>> 5 */
	private int [] bits;
	private int bitLogical, bitSelected;
	/* runs of the bitmap, computed on demand; runCount == -1 when stale */
	private int [] runStarts, runEnds;
	private int runCount = -1;

	VirtualSelectionModel () {
		this (false);
	}

	VirtualSelectionModel (boolean hybrid) {
		this.hybrid = hybrid;
	}

	int logicalCount () {
		return bits != null ? bitLogical : selection.logicalCount ();
	}

	void setLogicalCount (int count) {
		if (bits == null) {
			selection.setLogicalCount (count);
			return;
		}
		if (count < 0) throw new IllegalArgumentException ("negative logical count");
		if (count < bitLogical) {
			remove (count, bitLogical - count);
		} else if (count > bitLogical) {
			insertCleared (bitLogical, count - bitLogical);
		}
	}

	boolean isSelected (int index) {
		if (bits == null) return selection.isSelected (index);
		checkIndex (index);
		return bit (index);
	}

	void setSelected (int index, boolean selected) {
		if (bits == null) {
			selection.setSelected (index, selected);
			adapt ();
			return;
		}
		checkIndex (index);
		setBit (index, selected);
	}

	void selectRange (int start, int endExclusive) {
		if (bits == null) {
			selection.selectRange (start, endExclusive);
			adapt ();
			return;
		}
		checkRange (start, endExclusive);
		for (int i = start; i < endExclusive; i++) setBit (i, true);
	}

	void deselectRange (int start, int endExclusive) {
		if (bits == null) {
			selection.deselectRange (start, endExclusive);
			adapt ();
			return;
		}
		checkRange (start, endExclusive);
		for (int i = start; i < endExclusive; i++) setBit (i, false);
	}

	void clear () {
		dropBitmap ();
		selection.clear ();
	}

	void selectAll () {
		dropBitmap ();
		selection.selectAll ();
	}

	/* Leaves the bitmap without carrying its rows (the caller replaces the selection). */
	private void dropBitmap () {
		if (bits == null) return;
		int logical = bitLogical;
		bits = null;
		runCount = -1;
		selection.clear ();
		selection.setLogicalCount (0);
		selection.setLogicalCount (logical);
	}

	int selectedCount () {
		return bits != null ? bitSelected : selection.selectedCount ();
	}

	int [] toArray () {
		if (bits == null) return selection.toArray ();
		int [] result = new int [bitSelected];
		int offset = 0;
		for (int word = 0; word < bits.length && offset < result.length; word++) {
			int value = bits [word];
			while (value != 0) {
				int bit = Integer.numberOfTrailingZeros (value);
				result [offset++] = (word << 5) + bit;
				value &= value - 1;
			}
		}
		return result;
	}

	void insert (int index, int count) {
		if (bits == null) {
			selection.insert (index, count);
			return;
		}
		if (count < 0 || index < 0 || index > bitLogical) throw new IllegalArgumentException ("invalid insert");
		insertCleared (index, count);
	}

	void remove (int index, int count) {
		if (bits == null) {
			selection.remove (index, count);
			return;
		}
		if (count < 0 || index < 0 || index > bitLogical - count) throw new IllegalArgumentException ("invalid remove");
		if (count == 0) return;
		for (int i = index; i < index + count; i++) {
			if (bit (i)) bitSelected--;
		}
		int tail = bitLogical - index - count;
		for (int i = 0; i < tail; i++) {
			writeBit (index + i, bit (index + count + i));
		}
		for (int i = bitLogical - count; i < bitLogical; i++) writeBit (i, false);
		bitLogical -= count;
		runCount = -1;
		if (bitSelected == 0) {
			toRanges ();
		}
	}

	int rangeCount () {
		if (bits == null) return selection.rangeCount ();
		computeRuns ();
		return runCount;
	}

	int rangeStart (int range) {
		if (bits == null) return selection.rangeStart (range);
		computeRuns ();
		if (range < 0 || range >= runCount) throw new IndexOutOfBoundsException ("range " + range);
		return runStarts [range];
	}

	int rangeEndExclusive (int range) {
		if (bits == null) return selection.rangeEndExclusive (range);
		computeRuns ();
		if (range < 0 || range >= runCount) throw new IndexOutOfBoundsException ("range " + range);
		return runEnds [range];
	}

	boolean complementMode () {
		return bits == null && selection.complementMode ();
	}

	/** Whether the bitmap container is active (hybrid mode only). */
	boolean bitmapMode () {
		return bits != null;
	}

	/* Moves a scattered range selection into the bitmap. */
	private void adapt () {
		if (!hybrid || bits != null || selection.complementMode ()) return;
		int logical = selection.logicalCount ();
		if (selection.rangeCount () <= Math.max (16, logical >> 6)) return;
		int [] words = new int [(logical + 31) >>> 5];
		int selected = 0;
		for (int range = 0; range < selection.rangeCount (); range++) {
			int start = selection.rangeStart (range), end = selection.rangeEndExclusive (range);
			for (int i = start; i < end; i++) words [i >>> 5] |= 1 << (i & 31);
			selected += end - start;
		}
		bits = words;
		bitLogical = logical;
		bitSelected = selected;
		runCount = -1;
	}

	/* Returns to the range container, carrying the bitmap's rows. */
	private void toRanges () {
		if (bits == null) return;
		int [] words = bits;
		int logical = bitLogical;
		bits = null;
		runCount = -1;
		selection.clear ();
		selection.setLogicalCount (0);
		selection.setLogicalCount (logical);
		int start = -1;
		for (int i = 0; i <= logical; i++) {
			boolean on = i < logical && (words [i >>> 5] & (1 << (i & 31))) != 0;
			if (on && start < 0) start = i;
			if (!on && start >= 0) {
				selection.selectRange (start, i);
				start = -1;
			}
		}
	}

	private void insertCleared (int index, int count) {
		if (count == 0) return;
		int logical = bitLogical + count;
		int needed = (logical + 31) >>> 5;
		if (needed > bits.length) {
			int [] grown = new int [Math.max (needed, VirtualListModel.growth.applyAsInt (bits.length, needed))];
			System.arraycopy (bits, 0, grown, 0, bits.length);
			bits = grown;
		}
		int tail = bitLogical - index;
		bitLogical = logical;
		for (int i = tail - 1; i >= 0; i--) {
			writeBit (index + count + i, bit (index + i));
		}
		for (int i = index; i < index + count; i++) writeBit (i, false);
		runCount = -1;
	}

	private boolean bit (int index) {
		return (bits [index >>> 5] & (1 << (index & 31))) != 0;
	}

	private void writeBit (int index, boolean on) {
		if (on) bits [index >>> 5] |= 1 << (index & 31);
		else bits [index >>> 5] &= ~(1 << (index & 31));
	}

	private void setBit (int index, boolean on) {
		boolean was = bit (index);
		if (was == on) return;
		writeBit (index, on);
		bitSelected += on ? 1 : -1;
		runCount = -1;
	}

	private void computeRuns () {
		if (runCount >= 0) return;
		int count = 0;
		int [] starts = runStarts == null ? new int [16] : runStarts;
		int [] ends = runEnds == null ? new int [16] : runEnds;
		int start = -1;
		for (int i = 0; i <= bitLogical; i++) {
			boolean on = i < bitLogical && bit (i);
			if (on && start < 0) start = i;
			if (!on && start >= 0) {
				if (count == starts.length) {
					int [] s = new int [count * 2], e = new int [count * 2];
					System.arraycopy (starts, 0, s, 0, count);
					System.arraycopy (ends, 0, e, 0, count);
					starts = s;
					ends = e;
				}
				starts [count] = start;
				ends [count++] = i;
				start = -1;
			}
		}
		runStarts = starts;
		runEnds = ends;
		runCount = count;
	}

	private void checkIndex (int index) {
		if (index < 0 || index >= bitLogical) throw new IllegalArgumentException ("index outside logical model");
	}

	private void checkRange (int start, int endExclusive) {
		if (start < 0 || endExclusive < start || endExclusive > bitLogical) {
			throw new IllegalArgumentException ("range outside logical model");
		}
	}
}
