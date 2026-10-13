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

import org.eclipse.swt.internal.*;

/**
 * Packed logical selection of a virtual widget, held by the parent as a hybrid:
 * sorted range pairs (with a complement flag) for contiguous runs, and an <code>int</code>
 * word bitset for scattered selections.
 *
 * <p>The range form is the canonical {@link ViewportRuntime.Selection} algebra (select-all,
 * shift-range and invert are O(1) or O(ranges)). When scattered single selections make the
 * range list longer than {@link #bitsCutover(int)} runs, the model switches to a bitset
 * (single toggles O(1), counts by <code>Integer.bitCount</code>); a bulk operation that leaves
 * few runs, and select-all/clear, switch back. Both forms answer every query identically; the
 * range accessors of the bitset form are a run list built on demand and cached until the next
 * mutation. Only <code>int</code> arithmetic without overflow reliance is used (GWT-safe).</p>
 */
final class VirtualSelectionModel {
	/** Minimum run count before the bitset form is considered. */
	static final int MIN_BITS_RUNS = 1024;
	/* Representation policies: adaptive (measured cut-over), eager bitset, ranges only. */
	static final int ADAPTIVE = 0, EAGER = 1, RANGES = 2;

	private final ViewportRuntime.Selection selection = new ViewportRuntime.Selection ();
	/* Bitset form: non-null while active; lane 0 = selected. */
	private VirtualBitLanes bits;
	private int bitsSelected;
	/* Run list of the bitset form, built on demand. */
	private int [] runStarts = new int [0];
	private int [] runEnds = new int [0];
	private int runCount = -1;
	private int switches;
	private final int policy;

	/**
	 * An adaptive hybrid model: the bitset form is taken above {@link #bitsCutover(int)} runs.
	 */
	VirtualSelectionModel () {
		policy = ADAPTIVE;
	}

	/**
	 * A model with an explicit representation policy.
	 *
	 * @param hybrid <code>true</code> to move scattered selections to the bitset early, above
	 *        {@link #eagerCutover(int)} runs; <code>false</code> to keep the range form only (the
	 *        reference model of the differential tests)
	 */
	VirtualSelectionModel (boolean hybrid) {
		policy = hybrid ? EAGER : RANGES;
	}

	/**
	 * Run count above which scattered selections are kept as a bitset: the bitset may cost at most
	 * four times the range form (8 bytes per run against one bit per row), and never fewer than
	 * {@link #MIN_BITS_RUNS} runs. Measured cut-over: Test_VirtualBitLanes#test_selection10MillionRowsTimingsAndCutover.
	 */
	static int bitsCutover (int logicalCount) {
		return Math.max (MIN_BITS_RUNS, logicalCount >>> 8);
	}

	/**
	 * Run count above which the eager hybrid model keeps scattered selections as a bitset: more
	 * ranges than the bitset needs words, and never fewer than 16 runs.
	 */
	static int eagerCutover (int logicalCount) {
		return Math.max (16, logicalCount >>> 6);
	}

	private int cutover (int logicalCount) {
		return policy == EAGER ? eagerCutover (logicalCount) : bitsCutover (logicalCount);
	}

	boolean bitsMode () {
		return bits != null;
	}

	/** Same as {@link #bitsMode()}. */
	boolean bitmapMode () {
		return bits != null;
	}

	int representationSwitches () {
		return switches;
	}

	int logicalCount () {
		return bits != null ? bits.length () : selection.logicalCount ();
	}

	void setLogicalCount (int count) {
		if (count < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		if (bits == null) {
			selection.setLogicalCount (count);
			return;
		}
		if (count < bits.length ()) bitsSelected -= bits.count (0, count, bits.length ());
		bits.setLength (count);
		runCount = -1;
	}

	boolean isSelected (int index) {
		if (bits == null) return selection.isSelected (index);
		checkIndex (index);
		return bits.get (0, index);
	}

	void setSelected (int index, boolean selected) {
		if (bits == null) {
			selection.setSelected (index, selected);
			maybeToBits ();
			return;
		}
		checkIndex (index);
		if (bits.get (0, index) == selected) return;
		bits.set (0, index, selected);
		bitsSelected += selected ? 1 : -1;
		runCount = -1;
	}

	void selectRange (int start, int endExclusive) {
		if (bits == null) {
			selection.selectRange (start, endExclusive);
			maybeToBits ();
			return;
		}
		checkRange (start, endExclusive);
		bitsSelected += (endExclusive - start) - bits.count (0, start, endExclusive);
		bits.setRange (0, start, endExclusive, true);
		runCount = -1;
		maybeToRanges (endExclusive - start);
	}

	void deselectRange (int start, int endExclusive) {
		if (bits == null) {
			selection.deselectRange (start, endExclusive);
			maybeToBits ();
			return;
		}
		checkRange (start, endExclusive);
		bitsSelected -= bits.count (0, start, endExclusive);
		bits.setRange (0, start, endExclusive, false);
		runCount = -1;
		maybeToRanges (endExclusive - start);
	}

	/** Inverts every row: O(1) in the range form (complement flag), word-wise in the bitset form. */
	void invert () {
		int count = logicalCount ();
		if (bits != null) {
			bits.flipRange (0, 0, count);
			bitsSelected = count - bitsSelected;
			runCount = -1;
			return;
		}
		selection.invert ();
	}

	void clear () {
		toRanges (false);
		selection.clear ();
	}

	void selectAll () {
		toRanges (false);
		selection.selectAll ();
	}

	int selectedCount () {
		return bits != null ? bitsSelected : selection.selectedCount ();
	}

	int [] toArray () {
		if (bits == null) return selection.toArray ();
		int [] result = new int [bitsSelected];
		int n = 0;
		for (int i = bits.nextSet (0, 0); i >= 0; i = bits.nextSet (0, i + 1)) result [n++] = i;
		return result;
	}

	void insert (int index, int count) {
		if (bits == null) {
			selection.insert (index, count);
			return;
		}
		bits.insert (index, count);
		runCount = -1;
	}

	void remove (int index, int count) {
		if (bits == null) {
			selection.remove (index, count);
			return;
		}
		bitsSelected -= bits.count (0, index, index + count);
		bits.remove (index, count);
		runCount = -1;
	}

	int rangeCount () {
		if (bits == null) return selection.rangeCount ();
		buildRuns ();
		return runCount;
	}

	int rangeStart (int range) {
		if (bits == null) return selection.rangeStart (range);
		buildRuns ();
		if (range < 0 || range >= runCount) SWT.error (SWT.ERROR_INVALID_RANGE);
		return runStarts [range];
	}

	int rangeEndExclusive (int range) {
		if (bits == null) return selection.rangeEndExclusive (range);
		buildRuns ();
		if (range < 0 || range >= runCount) SWT.error (SWT.ERROR_INVALID_RANGE);
		return runEnds [range];
	}

	boolean complementMode () {
		return bits == null && selection.complementMode ();
	}

	/* ------------------------------------------------------------ representation */

	private void maybeToBits () {
		if (policy == RANGES || selection.rangeCount () <= cutover (selection.logicalCount ())) return;
		int count = selection.logicalCount ();
		VirtualBitLanes lanes = new VirtualBitLanes (1);
		lanes.setLength (count);
		boolean complement = selection.complementMode ();
		int ranges = selection.rangeCount ();
		if (complement) {
			lanes.setRange (0, 0, count, true);
			for (int r = 0; r < ranges; r++) lanes.setRange (0, selection.rangeStart (r), selection.rangeEndExclusive (r), false);
		} else {
			for (int r = 0; r < ranges; r++) lanes.setRange (0, selection.rangeStart (r), selection.rangeEndExclusive (r), true);
		}
		bitsSelected = selection.selectedCount ();
		bits = lanes;
		runCount = -1;
		selection.clear ();
		switches++;
	}

	/** After a bulk span of at least a quarter of the rows, return to ranges when few runs remain. */
	private void maybeToRanges (int span) {
		int count = bits.length ();
		if (span < (count >>> 2)) return;
		buildRuns ();
		if (runCount <= (cutover (count) >>> 2)) toRanges (true);
	}

	private void toRanges (boolean keepSelection) {
		if (bits == null) return;
		int count = bits.length ();
		if (keepSelection) buildRuns ();
		selection.clear ();
		selection.setLogicalCount (0);
		selection.setLogicalCount (count);
		if (keepSelection) {
			for (int r = 0; r < runCount; r++) selection.selectRange (runStarts [r], runEnds [r]);
		}
		bits = null;
		bitsSelected = 0;
		runCount = -1;
		switches++;
	}

	private void buildRuns () {
		if (runCount >= 0) return;
		int n = 0;
		int i = bits.nextSet (0, 0);
		while (i >= 0) {
			int end = bits.nextClear (0, i);
			if (n == runStarts.length) {
				int capacity = Math.max (8, n * 2);
				int [] starts = new int [capacity], ends = new int [capacity];
				System.arraycopy (runStarts, 0, starts, 0, n);
				System.arraycopy (runEnds, 0, ends, 0, n);
				runStarts = starts;
				runEnds = ends;
			}
			runStarts [n] = i;
			runEnds [n] = end;
			n++;
			i = bits.nextSet (0, end);
		}
		runCount = n;
	}

	private void checkIndex (int index) {
		if (index < 0 || index >= bits.length ()) SWT.error (SWT.ERROR_INVALID_RANGE);
	}

	private void checkRange (int start, int endExclusive) {
		if (start < 0 || endExclusive > bits.length () || start > endExclusive) SWT.error (SWT.ERROR_INVALID_RANGE);
	}
}
