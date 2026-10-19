/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * The AVL order-statistic lanes are adapted from hsoliwal/com.synexia
 * synexia-primitives/src/main/java/com/synexia/primitives/LongOrderStatisticMultiset.java
 * (af4494b143270d03cf4d3f33d1e7b4f84325f5dd), Copyright 2026 Hitesh Soliwal and
 * contributors, Apache-2.0. See docs/m3/tree-viewport/THIRD_PARTY_NOTICES.md.
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.*;

/**
 * Order-statistic index over the materialized children of every parent of a
 * {@link VirtualTreeTopology}.
 *
 * <p>Each parent owns a height-balanced binary search tree whose nodes are the
 * topology's item ids, so the index allocates no node of its own. The search
 * key of a node is read from the owner's child-index lane and the weight from
 * its visible-extra-rows lane; both lanes are borrowed, never copied. Shifting
 * the child indices of every sibling after a position by the same amount keeps
 * the in-order sequence, so such a shift needs no update here. The owner reports
 * every weight change of a linked node through {@link #adjust(int, int, long)}
 * before any other operation on the same parent.</p>
 *
 * <p>Every lookup, prefix sum and row descent is one root-to-leaf walk, and
 * insertion and removal rebalance along one path: all are O(log k) for k
 * materialized children of the parent. The lanes are parallel primitive arrays
 * indexed by id: two ints, one byte and one long per id, plus one root int per
 * parent slot (slot 0 is the root of the tree itself).</p>
 */
final class VirtualTreeSiblingIndex {
	private static final int NONE = -1;

	private int [] left = new int [0];
	private int [] right = new int [0];
	private byte [] heights = new byte [0];
	private long [] sums = new long [0];
	private int [] roots = {NONE};
	private int [] keys = new int [0];
	private long [] weights = new long [0];
	private long seekBefore;

	/** Rebinds the borrowed key and weight lanes after the owner grows them. */
	void lanes (int [] keys, long [] weights) {
		this.keys = keys;
		this.weights = weights;
	}

	/** Grows the lanes to hold ids below {@code capacity}. */
	void ensureCapacity (int capacity) {
		if (capacity <= left.length) return;
		int old = roots.length;
		left = Arrays.copyOf (left, capacity);
		right = Arrays.copyOf (right, capacity);
		heights = Arrays.copyOf (heights, capacity);
		sums = Arrays.copyOf (sums, capacity);
		roots = Arrays.copyOf (roots, Math.addExact (capacity, 1));
		Arrays.fill (roots, old, roots.length, NONE);
	}

	/** Forgets every parent's tree; node lanes are reinitialized on insertion. */
	void clear () {
		Arrays.fill (roots, NONE);
	}

	/** Returns the child of {@code parentId} at {@code key}, or -1. */
	int find (int parentId, int key) {
		int node = roots [parentId + 1];
		while (node != NONE) {
			int nodeKey = keys [node];
			if (key == nodeKey) return node;
			node = key < nodeKey ? left [node] : right [node];
		}
		return NONE;
	}

	/** Returns the child of {@code parentId} with the greatest key below {@code key}, or -1. */
	int lower (int parentId, int key) {
		int found = NONE;
		int node = roots [parentId + 1];
		while (node != NONE) {
			if (keys [node] < key) {
				found = node;
				node = right [node];
			} else {
				node = left [node];
			}
		}
		return found;
	}

	/** Links {@code id}, whose key and weight are already in the owner's lanes. */
	void insert (int parentId, int id) {
		roots [parentId + 1] = insertNode (roots [parentId + 1], id);
	}

	/** Unlinks {@code id}; its key must not have changed since it was linked. */
	void remove (int parentId, int id) {
		roots [parentId + 1] = removeNode (roots [parentId + 1], id);
	}

	/** Records that the weight of the linked {@code id} changed by {@code delta}. */
	void adjust (int parentId, int id, long delta) {
		if (delta == 0) return;
		int key = keys [id];
		int node = roots [parentId + 1];
		while (node != NONE) {
			sums [node] += delta;
			if (node == id) return;
			node = key < keys [node] ? left [node] : right [node];
		}
		throw new IllegalStateException ("sibling index is inconsistent");
	}

	/** Sum of the weights of the children of {@code parentId} whose key is below {@code key}. */
	long prefix (int parentId, int key) {
		long result = 0;
		int node = roots [parentId + 1];
		while (node != NONE) {
			if (keys [node] < key) {
				result += sums [node] - sum (right [node]);
				node = right [node];
			} else {
				node = left [node];
			}
		}
		return result;
	}

	/**
	 * Height of the tree of {@code parentId}, 0 when it has no child. A tree of
	 * height h holds at most 2^h - 1 children.
	 */
	int treeHeight (int parentId) {
		return height (roots [parentId + 1]);
	}

	/** Sum of the weights of every child of {@code parentId}. */
	long total (int parentId) {
		return sum (roots [parentId + 1]);
	}

	/**
	 * Returns the last child c of {@code parentId} with {@code key (c) < limit} and
	 * {@code key (c) + prefix (key (c)) <= row}, or -1. That sum is the row offset
	 * of c among its siblings when every logical child counts one row plus its
	 * weight. {@link #seekBefore()} then answers {@code prefix (key (c))}.
	 */
	int seek (int parentId, long row, int limit) {
		int found = NONE;
		long before = 0, foundBefore = 0;
		int node = roots [parentId + 1];
		while (node != NONE) {
			int key = keys [node];
			long leftSum = sum (left [node]);
			if (key < limit && key + before + leftSum <= row) {
				found = node;
				foundBefore = before + leftSum;
				before = foundBefore + weights [node];
				node = right [node];
			} else {
				node = left [node];
			}
		}
		seekBefore = foundBefore;
		return found;
	}

	/** Weight before the child returned by the last {@link #seek(int, long, int)}. */
	long seekBefore () {
		return seekBefore;
	}

	private int insertNode (int node, int id) {
		if (node == NONE) {
			left [id] = right [id] = NONE;
			heights [id] = 1;
			sums [id] = weights [id];
			return id;
		}
		int key = keys [id], nodeKey = keys [node];
		if (key == nodeKey) throw new IllegalStateException ("duplicate sibling index");
		if (key < nodeKey) {
			left [node] = insertNode (left [node], id);
		} else {
			right [node] = insertNode (right [node], id);
		}
		return rebalance (node);
	}

	private int removeNode (int node, int id) {
		if (node == NONE) throw new IllegalStateException ("sibling index is inconsistent");
		if (node == id) return detach (id);
		if (keys [id] < keys [node]) {
			left [node] = removeNode (left [node], id);
		} else {
			right [node] = removeNode (right [node], id);
		}
		return rebalance (node);
	}

	/* The donor copies the successor's key into the removed slot. Here a slot is
	 * an item id, so the successor node itself takes the removed node's place. */
	private int detach (int id) {
		int l = left [id], r = right [id];
		left [id] = right [id] = NONE;
		heights [id] = 0;
		sums [id] = 0;
		if (l == NONE) return r;
		if (r == NONE) return l;
		int successor = r;
		while (left [successor] != NONE) successor = left [successor];
		right [successor] = removeFirst (r);
		left [successor] = l;
		return rebalance (successor);
	}

	private int removeFirst (int node) {
		if (left [node] == NONE) return right [node];
		left [node] = removeFirst (left [node]);
		return rebalance (node);
	}

	private int rebalance (int node) {
		refresh (node);
		int balance = height (left [node]) - height (right [node]);
		if (balance > 1) {
			if (height (left [left [node]]) < height (right [left [node]])) {
				left [node] = rotateLeft (left [node]);
			}
			return rotateRight (node);
		}
		if (balance < -1) {
			if (height (right [right [node]]) < height (left [right [node]])) {
				right [node] = rotateRight (right [node]);
			}
			return rotateLeft (node);
		}
		return node;
	}

	private int rotateLeft (int node) {
		int pivot = right [node];
		right [node] = left [pivot];
		left [pivot] = node;
		refresh (node);
		refresh (pivot);
		return pivot;
	}

	private int rotateRight (int node) {
		int pivot = left [node];
		left [node] = right [pivot];
		right [pivot] = node;
		refresh (node);
		refresh (pivot);
		return pivot;
	}

	private void refresh (int node) {
		int l = left [node], r = right [node];
		sums [node] = weights [node] + sum (l) + sum (r);
		heights [node] = (byte)(1 + Math.max (height (l), height (r)));
	}

	private int height (int node) {
		return node == NONE ? 0 : heights [node];
	}

	private long sum (int node) {
		return node == NONE ? 0 : sums [node];
	}
}
