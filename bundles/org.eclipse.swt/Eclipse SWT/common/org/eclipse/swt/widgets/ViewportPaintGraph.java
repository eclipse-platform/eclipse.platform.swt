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

import java.util.function.*;

/**
 * Internal retained paint-dependency graph for viewport-controlled drawing.
 *
 * <p>The graph owns no native graphics resources and never replaces the public
 * SWT GC.  It only retains logical paint bounds, z-plane ownership and dirty
 * dependencies so widgets can decide what SWT-controlled work must be redrawn.
 * Application paint callbacks remain immediate-mode and are not replayed.</p>
 *
 * <p>Storage is columnar and primitive. Dependencies are forward edges:
 * invalidating one node recursively dirties its dependants.</p>
 */
final class ViewportPaintGraph {

	private static final int INITIAL_CAPACITY = 8;
	private static final int DENSE_WORD_THRESHOLD = 16;

	private long [] keys = new long [INITIAL_CAPACITY];
	private int [] layers = new int [INITIAL_CAPACITY];
	private long [] x = new long [INITIAL_CAPACITY];
	private long [] y = new long [INITIAL_CAPACITY];
	private long [] width = new long [INITIAL_CAPACITY];
	private long [] height = new long [INITIAL_CAPACITY];
	private int [] firstOutgoing = filled (INITIAL_CAPACITY, -1);

	private int [] edgeTo = new int [INITIAL_CAPACITY];
	private int [] edgeNext = filled (INITIAL_CAPACITY, -1);

	private long [] dirtyWords = new long [1];
	private long [] pendingWords = new long [1];
	private int nodeCount;
	private int edgeCount;

	int addNode (long key, int layer, long x, long y, long width, long height) {
		if (layer == 0 || (layer & ~ViewportLayerState.ALL) != 0) {
			throw new IllegalArgumentException ("invalid viewport layer");
		}
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("negative paint bounds");
        }
		ensureNodeCapacity (nodeCount + 1);
		int id = nodeCount++;
		keys [id] = key;
		layers [id] = layer;
		this.x [id] = x;
		this.y [id] = y;
		this.width [id] = width;
		this.height [id] = height;
		firstOutgoing [id] = -1;
		ensureDirtyCapacity (nodeCount);
		setDirty (id, true);
		return id;
	}

	void addDependency (int source, int dependant) {
		checkNode (source);
		checkNode (dependant);
        if (source == dependant) {
            throw new IllegalArgumentException("self dependency");
        }
        /*
         * Requiring forward edges gives us a DAG by construction and matches
         * paint layering: base geometry is created before overlays that depend on it.
         */
        if (source > dependant) {
            throw new IllegalArgumentException("dependency must point forward");
        }
		ensureEdgeCapacity (edgeCount + 1);
		edgeTo [edgeCount] = dependant;
		edgeNext [edgeCount] = firstOutgoing [source];
		firstOutgoing [source] = edgeCount++;
	}

	int nodeCount () {
		return nodeCount;
	}

	long key (int node) {
		checkNode (node);
		return keys [node];
	}

	int layer (int node) {
		checkNode (node);
		return layers [node];
	}

	boolean isDirty (int node) {
		checkNode (node);
		return (dirtyWords [node >>> 6] & (1L << (node & 63))) != 0;
	}

	int dirtyCount () {
		int result = 0;
		for (int word = 0; word < dirtyWords.length; word++) {
			long bits = dirtyWords [word];
			if (word == dirtyWords.length - 1 && (nodeCount & 63) != 0) {
				bits &= (1L << (nodeCount & 63)) - 1;
			}
			result += Long.bitCount (bits);
		}
		return result;
	}

	void markClean (int node) {
		checkNode (node);
		setDirty (node, false);
	}

	void markAllClean () {
		java.util.Arrays.fill (dirtyWords, 0);
	}

	void invalidateLayers (int layerMask) {
		if ((layerMask & ~ViewportLayerState.ALL) != 0) {
			throw new IllegalArgumentException ("invalid viewport layer mask");
		}
		for (int node = 0; node < nodeCount; node++) {
            if ((layers [node] & layerMask) != 0) {
                queueDirty(node);
            }
		}
		propagateDirty ();
	}

	void invalidateBounds (
			long dirtyX, long dirtyY, long dirtyWidth, long dirtyHeight, int layerMask) {
		if (dirtyWidth < 0 || dirtyHeight < 0) {
			throw new IllegalArgumentException ("negative dirty bounds");
		}
		if ((layerMask & ~ViewportLayerState.ALL) != 0) {
			throw new IllegalArgumentException ("invalid viewport layer mask");
		}
		for (int node = 0; node < nodeCount; node++) {
            if ((layers [node] & layerMask) == 0) {
                continue;
            }
			if (intersects (
					x [node], y [node], width [node], height [node],
					dirtyX, dirtyY, dirtyWidth, dirtyHeight)) {
				queueDirty (node);
			}
		}
		propagateDirty ();
	}

	ViewportAffineTransform.Bounds deviceBounds (
			int node, ViewportAffineTransform transform) {
		checkNode (node);
        if (transform == null) {
            throw new IllegalArgumentException("null transform");
        }
		return transform.mapBounds (x [node], y [node], width [node], height [node]);
	}

	void forEachDirty (IntConsumer action) {
        if (action == null) {
            throw new IllegalArgumentException("null action");
        }
        // For tiny viewports and globally dense paint sets, preserve the
        // original per-node loop. Dirty count is parent-owned live bit state;
        // the strategy choice adds no index or retained per-node objects.
        if (nodeCount < 256 || dirtyCount() >= (nodeCount >>> 2)) {
            for (int node = 0; node < nodeCount; node++) {
                if (isDirty(node)) action.accept(node);
            }
            return;
        }
        // Read the live dirty words after every callback. Snapshotting the
        // current word would lose newly dirtied later nodes or replay nodes
        // cleaned by reentrant listeners.
        int next = 0;
        while (next < nodeCount) {
            int word = next >>> 6;
            long bits = dirtyWords[word] & (-1L << (next & 63));
            if (bits == 0) {
                // An empty word contains no eligible callbacks; avoid a
                // per-node isDirty check. Use long for the final word edge.
                long afterWord = ((long) word + 1) << 6;
                if (afterWord >= nodeCount) break;
                next = (int) afterWord;
                continue;
            }
            if (Long.bitCount(bits) >= DENSE_WORD_THRESHOLD) {
                // Dense words keep the original live per-node membership path.
                // A callback may still clean or dirty subsequent nodes.
                do {
                    if (isDirty(next)) action.accept(next);
                    next++;
                } while (next < nodeCount && (next >>> 6) == word);
            } else {
                int node = (word << 6) + Long.numberOfTrailingZeros(bits);
                action.accept(node);
                next = node + 1;
            }
        }
	}

	private void queueDirty (int node) {
		long mask = 1L << (node & 63);
		int word = node >>> 6;
		dirtyWords [word] |= mask;
		pendingWords [word] |= mask;
	}

	private void propagateDirty () {
		/*
		 * Dirty means "needs painting", not "visited during this invalidation".
		 * An intermediate node can remain dirty after a dependant was painted.
		 * Forward-only edges let this reusable pending lane visit each affected
		 * node once, even with diamonds/duplicate edges or partially clean planes.
		 */
		for (int word = 0; word < pendingWords.length; word++) {
			while (pendingWords [word] != 0) {
				int node = (word << 6) + Long.numberOfTrailingZeros (pendingWords [word]);
				pendingWords [word] &= pendingWords [word] - 1;
				for (int edge = firstOutgoing [node]; edge >= 0; edge = edgeNext [edge]) {
					queueDirty (edgeTo [edge]);
				}
			}
		}
	}

	private void setDirty (int node, boolean value) {
		long mask = 1L << (node & 63);
		int word = node >>> 6;
        if (value) {
            dirtyWords [word] |= mask;
        } else {
            dirtyWords [word] &= ~mask;
        }
	}

	private void ensureNodeCapacity (int required) {
        if (required <= keys.length) {
            return;
        }
		int next = Math.max (required, keys.length * 2);
		keys = java.util.Arrays.copyOf (keys, next);
		layers = java.util.Arrays.copyOf (layers, next);
		x = java.util.Arrays.copyOf (x, next);
		y = java.util.Arrays.copyOf (y, next);
		width = java.util.Arrays.copyOf (width, next);
		height = java.util.Arrays.copyOf (height, next);
		int old = firstOutgoing.length;
		firstOutgoing = java.util.Arrays.copyOf (firstOutgoing, next);
		java.util.Arrays.fill (firstOutgoing, old, next, -1);
	}

	private void ensureEdgeCapacity (int required) {
        if (required <= edgeTo.length) {
            return;
        }
		int next = Math.max (required, edgeTo.length * 2);
		edgeTo = java.util.Arrays.copyOf (edgeTo, next);
		int old = edgeNext.length;
		edgeNext = java.util.Arrays.copyOf (edgeNext, next);
		java.util.Arrays.fill (edgeNext, old, next, -1);
	}

	private void ensureDirtyCapacity (int requiredNodes) {
		int requiredWords = (requiredNodes + 63) >>> 6;
		if (requiredWords > dirtyWords.length) {
			dirtyWords = java.util.Arrays.copyOf (dirtyWords, requiredWords);
			pendingWords = java.util.Arrays.copyOf (pendingWords, requiredWords);
		}
	}

	private void checkNode (int node) {
        if (node < 0 || node >= nodeCount) {
            throw new IllegalArgumentException("invalid paint node");
        }
	}

	private static boolean intersects (
			long ax, long ay, long aw, long ah,
			long bx, long by, long bw, long bh) {
        if (aw == 0 || ah == 0 || bw == 0 || bh == 0) {
            return false;
        }
		return ax < saturatedAdd (bx, bw)
				&& bx < saturatedAdd (ax, aw)
				&& ay < saturatedAdd (by, bh)
				&& by < saturatedAdd (ay, ah);
	}

	private static long saturatedAdd (long value, long delta) {
        if (delta > 0 && value > Long.MAX_VALUE - delta) {
            return Long.MAX_VALUE;
        }
		return value + delta;
	}

	private static int [] filled (int length, int value) {
		int [] result = new int [length];
		java.util.Arrays.fill (result, value);
		return result;
	}
}
