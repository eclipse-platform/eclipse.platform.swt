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
package org.eclipse.swt.widgets;

/**
 * Gate-only deterministic workload over the shared viewport atoms. It uses its own xorshift
 * generator and int arithmetic only, so the same seed must give the same digest in the JVM and
 * in GWT-compiled JavaScript.
 */
final class GateWorkload {
	private int state;
	private long digest = 17;

	private GateWorkload (int seed) {
		state = seed == 0 ? 1 : seed;
	}

	static String run (int seed, int steps) {
		GateWorkload w = new GateWorkload (seed);
		w.lists (steps);
		w.extents (steps);
		return "seed=" + seed + " steps=" + steps + " digest=" + w.digest;
	}

	private int next (int bound) {
		state ^= state << 13;
		state ^= state >>> 17;
		state ^= state << 5;
		return (state & 0x7fffffff) % bound;
	}

	private void fold (int value) {
		// GWT does not wrap int +, - or *; long arithmetic is exact in both engines, so mask to 31 bits.
		digest = (digest * 31L + value) & 0x7fffffffL;
	}

	private void fold (long value) {
		fold ((int) (value ^ (value >>> 32)));
	}

	private String text () {
		StringBuilder b = new StringBuilder ();
		int n = next (9);
		for (int i = 0; i < n; i++) b.append ('x');
		return b.toString ();
	}

	private void lists (int steps) {
		VirtualListModel m = new VirtualListModel (s -> s.length ());
		m.setGeometry (2, 10);
		for (int step = 0; step < steps; step++) {
			int n = m.getItemCount ();
			try {
				listStep (m, n);
			} catch (IllegalArgumentException e) {
				fold (-7);
			}
			fold (m.getItemCount ());
			for (int index : m.getSelectionIndices ()) fold (index);
			fold (m.getTopIndex ());
			fold (m.getVisibleCount ());
			fold (m.getWidestWidth ());
			for (String item : m.getItems ()) fold (item.length ());
		}
	}

	private void listStep (VirtualListModel m, int n) {
		int pick = next (100);
		if (pick < 18) m.add (text ());
		else if (pick < 34) m.add (text (), next (n + 1));
		else if (pick < 42) m.setItem (next (n + 3) - 1, text ());
		else if (pick < 52) m.remove (next (n + 3) - 1);
		else if (pick < 60) m.remove (next (n + 3) - 1, next (n + 3) - 1);
		else if (pick < 62) m.removeAll ();
		else if (pick < 65) setAll (m);
		else if (pick < 73) m.select (next (n + 4) - 2);
		else if (pick < 79) m.deselect (next (n + 4) - 2);
		else if (pick < 85) m.select (next (n + 4) - 2, next (n + 4) - 2);
		else if (pick < 90) m.deselect (next (n + 4) - 2, next (n + 4) - 2);
		else if (pick < 92) m.selectAll ();
		else if (pick < 94) m.deselectAll ();
		else if (pick < 98) m.setTopIndex (next (n + 40) - 20);
		else m.setGeometry (1 + next (5), next (41));
	}

	private void setAll (VirtualListModel m) {
		String [] all = new String [next (30)];
		for (int i = 0; i < all.length; i++) all [i] = text ();
		m.setItems (all);
	}

	private void extents (int steps) {
		VirtualRowExtents x = new VirtualRowExtents (1 + next (20));
		x.reset (next (40));
		int [] out = new int [2];
		for (int step = 0; step < steps; step++) {
			int n = x.rowCount ();
			try {
				extentStep (x, n);
			} catch (IllegalArgumentException e) {
				fold (-9);
			}
			n = x.rowCount ();
			fold (n);
			fold (x.measuredCount ());
			fold (x.totalExtent ());
			queries (x, n, out);
		}
	}

	private void extentStep (VirtualRowExtents x, int n) {
		int pick = next (100);
		if (pick < 35 && n > 0) x.setExtent (next (n), next (40));
		else if (pick < 45 && n > 0) x.clearExtent (next (n));
		else if (pick < 50) x.clearFrom (next (n + 1));
		else if (pick < 66) x.insert (next (n + 1), next (6));
		else if (pick < 80 && n > 0) x.remove (next (n), next (4));
		else if (pick < 84) x.setEstimate (1 + next (30));
		else if (pick < 86) x.reset (next (50));
		else if (n > 0) fold (x.setExtentAnchored (next (n), next (40), next ((int) Math.max (1L, x.totalExtent () + 5)), next (2) == 0));
	}

	private void queries (VirtualRowExtents x, int n, int [] out) {
		long total = x.totalExtent ();
		long off = next ((int) Math.max (1L, total + 5)) - 3;
		fold (x.rowAt (off));
		x.visibleRange (off, next (60), out);
		fold (out [0]);
		fold (out [1]);
		if (n > 0) {
			int row = next (n);
			fold (x.offsetOf (row));
			for (int align = 0; align < 5; align++) fold (x.scrollOffsetFor (row, align, off < 0 ? 0 : off, next (60)));
		}
	}
}
