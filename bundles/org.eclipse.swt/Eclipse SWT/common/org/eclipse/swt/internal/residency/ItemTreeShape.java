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
 * Lazy tree topology for {@link ItemResidency} at billion scale. No parent/child index is
 * materialized: node keys are arithmetic over per-depth fan-outs supplied by the data source
 * ({@code fanout[0]} roots, {@code fanout[d]} children per node at depth {@code d - 1}), so the
 * key of child {@code c} of node {@code k} is {@code levelStart[d] + (k - levelStart[d-1]) *
 * fanout[d] + c}. Expansion is a depth rule ("expand to depth N", O(1) memory) plus explicit
 * exceptions; only the exception path structure is kept, with per-node sorted children deltas and
 * prefix sums for row-to-node mapping. Row and key arithmetic is {@code int} (GWT); the total key
 * space and the visible row count must fit in an int.
 *
 * <p>Scope of this leaf: uniform fan-out per depth. Sources with per-node child counts keep the
 * existing {@code VirtualTreeTopology} until the Tree owner adopts a per-node variant.</p>
 */
public final class ItemTreeShape {
	public static final int RULE = 0, EXPANDED = 1, COLLAPSED = 2, ROOT = -1;

	/** Exception-path node: explicit state plus sorted (child index, subtree delta) entries. */
	public static final class Path {
		int state;
		int [] child = new int [2], delta = new int [2], prefix = new int [3];
		int size;

		int find (int c) {
			int lo = 0, hi = size - 1;
			while (lo <= hi) {
				int mid = (lo + hi) >>> 1;
				if (child [mid] < c) lo = mid + 1;
				else if (child [mid] > c) hi = mid - 1;
				else return mid;
			}
			return -(lo + 1);
		}

		/** Sets the delta of child {@code c}; zero removes the entry. Recomputes prefix sums. */
		void put (int c, int d) {
			int j = find (c);
			if (j >= 0 && d == 0) {
				System.arraycopy (child, j + 1, child, j, size - j - 1);
				System.arraycopy (delta, j + 1, delta, j, size - j - 1);
				size--;
			} else if (j >= 0) {
				delta [j] = d;
			} else if (d != 0) {
				j = -j - 1;
				if (size == child.length) {
					int next = PackedHeapGrowth.capacity (child.length, size + 1, 8);
					child = Arrays.copyOf (child, next);
					delta = Arrays.copyOf (delta, next);
				}
				System.arraycopy (child, j, child, j + 1, size - j);
				System.arraycopy (delta, j, delta, j + 1, size - j);
				child [j] = c;
				delta [j] = d;
				size++;
			}
			if (prefix.length < size + 1) prefix = new int [child.length + 1];
			for (int i = 0; i < size; i++) prefix [i + 1] = prefix [i] + delta [i];
		}

		int total () {
			return prefix [size];
		}
	}

	final int [] fanout, levelStart, levelCount;
	final int depths, keyCount;
	int ruleDepth;
	int [] ruleSub;
	final Path root = new Path ();
	final ItemSparseTable paths = new ItemSparseTable ();

	public ItemTreeShape (int [] fanout) {
		depths = fanout.length;
		if (depths < 1) throw new IllegalArgumentException ("no roots");
		this.fanout = Arrays.copyOf (fanout, depths);
		levelStart = new int [depths];
		levelCount = new int [depths];
		long start = 0, nodes = 1;
		for (int d = 0; d < depths; d++) {
			if (fanout [d] < 0) throw new IllegalArgumentException ("negative fan-out");
			nodes *= fanout [d];
			if (start + nodes > Integer.MAX_VALUE) throw new IllegalArgumentException ("key space exceeds int");
			levelStart [d] = (int) start;
			levelCount [d] = (int) nodes;
			start += nodes;
		}
		keyCount = (int) start;
		expandToDepth (0);
	}

	/** Rule: nodes at depth below {@code depth} are expanded; clears every exception (O(1) memory). */
	public void expandToDepth (int depth) {
		int [] sub = new int [depths];
		long below = 0;
		for (int d = depths - 1; d >= 0; d--) {
			long s = d < depth && d + 1 < depths ? (long) fanout [d + 1] * (1 + below) : 0;
			if (s > Integer.MAX_VALUE) throw new IllegalArgumentException ("visible rows exceed int");
			sub [d] = (int) s;
			below = s;
		}
		if ((long) levelCount [0] * (1 + sub [0]) > Integer.MAX_VALUE) throw new IllegalArgumentException ("visible rows exceed int");
		ruleDepth = depth;
		ruleSub = sub;
		root.size = 0;
		root.prefix [0] = 0;
		paths.truncate (0);
	}

	public int depth (int key) {
		int d = depths - 1;
		while (d > 0 && levelStart [d] > key) d--;
		return d;
	}

	public int parent (int key) {
		int d = depth (key);
		return d == 0 ? ROOT : levelStart [d - 1] + (key - levelStart [d]) / fanout [d];
	}

	public int childIndex (int key) {
		int d = depth (key);
		return d == 0 ? key : (key - levelStart [d]) % fanout [d];
	}

	public int childKey (int parent, int c) {
		if (parent == ROOT) return c;
		int d = depth (parent) + 1;
		return levelStart [d] + (parent - levelStart [d - 1]) * fanout [d] + c;
	}

	public int childCount (int key) {
		int d = key == ROOT ? -1 : depth (key);
		return d + 1 < depths ? fanout [d + 1] : 0;
	}

	public boolean isExpanded (int key) {
		Path p = (Path) paths.get (key);
		if (p != null && p.state != RULE) return p.state == EXPANDED;
		return depth (key) < ruleDepth && childCount (key) > 0;
	}

	/** Rows under a rule-shaped child at depth {@code d} (0 when the rule leaves it collapsed). */
	private int childSub (int d) {
		return d < depths ? ruleSub [d] : 0;
	}

	public void setExpanded (int key, boolean expanded) {
		if (key < 0 || key >= keyCount) throw new IndexOutOfBoundsException ("node " + key);
		boolean rule = depth (key) < ruleDepth && childCount (key) > 0;
		Path p = (Path) paths.get (key);
		int state = expanded == rule ? RULE : expanded ? EXPANDED : COLLAPSED;
		if (p == null) {
			if (state == RULE) return;
			p = new Path ();
			paths.put (key, p);
		}
		p.state = state;
		propagate (key);
	}

	/** Visible rows under {@code key} minus what the rule alone would give. */
	private int subDelta (int key, Path p) {
		int d = depth (key);
		int actual = 0;
		if (isExpanded (key)) {
			actual = childCount (key) * (1 + childSub (d + 1)) + (p == null ? 0 : p.total ());
		}
		return actual - ruleSub [d];
	}

	private void propagate (int key) {
		while (true) {
			Path p = (Path) paths.get (key);
			int delta = subDelta (key, p);
			if (p != null && p.state == RULE && p.size == 0) paths.put (key, null);
			int parent = parent (key);
			Path up = parent == ROOT ? root : (Path) paths.get (parent);
			if (up == null) {
				if (delta == 0) return;
				up = new Path ();
				paths.put (parent, up);
			}
			up.put (childIndex (key), delta);
			if (parent == ROOT) return;
			key = parent;
		}
	}

	public int rowCount () {
		return levelCount [0] * (1 + ruleSub [0]) + root.total ();
	}

	/** Node key at visible {@code row}. */
	public int key (int row) {
		if (row < 0 || row >= rowCount ()) throw new IndexOutOfBoundsException ("row " + row);
		int parent = ROOT, d = 0, r = row;
		while (true) {
			int unit = 1 + childSub (d);
			Path p = parent == ROOT ? root : (Path) paths.get (parent);
			int c, off;
			int j = p == null ? -1 : lastStartAtOrBefore (p, unit, r);
			if (j < 0) {
				c = r / unit;
				off = r % unit;
			} else {
				int base = p.child [j] * unit + p.prefix [j], size = unit + p.delta [j];
				if (r < base + size) {
					c = p.child [j];
					off = r - base;
				} else {
					int r2 = r - base - size;
					c = p.child [j] + 1 + r2 / unit;
					off = r2 % unit;
				}
			}
			int key = childKey (parent, c);
			if (off == 0) return key;
			parent = key;
			r = off - 1;
			d++;
		}
	}

	private static int lastStartAtOrBefore (Path p, int unit, int r) {
		int lo = 0, hi = p.size - 1, best = -1;
		while (lo <= hi) {
			int mid = (lo + hi) >>> 1;
			if (p.child [mid] * unit + p.prefix [mid] <= r) {
				best = mid;
				lo = mid + 1;
			} else {
				hi = mid - 1;
			}
		}
		return best;
	}

	/** Visible row of {@code key}, or -1 when an ancestor is collapsed. */
	public int row (int key) {
		int d = depth (key);
		int [] chain = new int [d + 1];
		for (int k = key, i = d; i >= 0; i--, k = parent (k)) chain [i] = k;
		int row = -1, parent = ROOT;
		for (int i = 0; i <= d; i++) {
			if (parent != ROOT && !isExpanded (parent)) return -1;
			int unit = 1 + childSub (i), c = childIndex (chain [i]);
			Path p = parent == ROOT ? root : (Path) paths.get (parent);
			int before = 0;
			if (p != null) {
				int j = p.find (c);
				before = p.prefix [j < 0 ? -j - 1 : j];
			}
			row += 1 + c * unit + before;
			parent = chain [i];
		}
		return row;
	}

	/** Merges the keys of rows {@code [first, first + rows)} into runs; returns the run count. */
	public int keyRuns (int first, int rows, int [] out) {
		int n = 0, end = Math.min (rowCount (), first + rows);
		for (int row = first; row < end; row++) {
			int k = key (row);
			if (n > 0 && out [2 * n - 1] == k) {
				out [2 * n - 1] = k + 1;
			} else {
				out [2 * n] = k;
				out [2 * n + 1] = k + 1;
				n++;
			}
		}
		return n;
	}

	public int keyCount () {
		return keyCount;
	}

	/** Exception-path nodes kept (memory is O(this)). */
	public int pathNodes () {
		return paths.size;
	}
}
