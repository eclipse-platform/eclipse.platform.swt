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

import java.util.*;

/**
 * Sparse columnar topology for materialized coordinates of a virtual Tree.
 *
 * <p>Slots are keyed by the platform item's stable SWT id. Cold logical children
 * do not require slots; only their parent's logical child count is retained.
 * This is intentionally independent of any native Tree model so native rows can
 * later become a reconstructable viewport projection.</p>
 */
final class VirtualTreeTopology {
	static final int ROOT = -1;
	private static final int ABSENT = Integer.MIN_VALUE;
	private static final int UNKNOWN_CHILD_COUNT = -1;
	/* Up to this sibling-index height (at most 2^h - 1 materialized siblings) the
	 * queries walk the sibling chain, which measured faster than a descent. */
	private static final int LINEAR_HEIGHT = 4;

	private int [] parentIds = new int [4];
	private int [] childIndices = new int [4];
	private int [] childCounts = new int [4];
	private int [] firstChildIds = new int [4];
	private int [] nextSiblingIds = new int [4];
	private long [] stateMasks = new long [4];
	private long [] visibleExtraRows = new long [4];
	private long [] childVisibleExtraSums = new long [4];
	/* Per-parent order-statistic index over the materialized children, keyed by
	 * the childIndices lane and weighted by the visibleExtraRows lane. */
	private final VirtualTreeSiblingIndex siblings = new VirtualTreeSiblingIndex ();
	private long seekBefore;
	private int rootChildCount = UNKNOWN_CHILD_COUNT;
	private int rootFirstChildId = -1;
	private long rootVisibleExtraRows;
	private int materializedCount;
	private long generation;
	/* Parent-owned presentation flags have a separate epoch: checkbox/cache
	 * changes must not force a native tree-model rebuild. */
	private long presentationGeneration;

	VirtualTreeTopology () {
		Arrays.fill (parentIds, ABSENT);
		Arrays.fill (childIndices, -1);
		Arrays.fill (childCounts, UNKNOWN_CHILD_COUNT);
		Arrays.fill (firstChildIds, -1);
		Arrays.fill (nextSiblingIds, -1);
		siblings.ensureCapacity (parentIds.length);
		siblings.lanes (childIndices, visibleExtraRows);
	}

	void bind (int id, int parentId, int childIndex) {
        if (id < 0) {
            throw new IllegalArgumentException("negative tree id");
        }
        if (parentId < ROOT) {
            throw new IllegalArgumentException("invalid parent id");
        }
        if (childIndex < 0) {
            throw new IllegalArgumentException("negative child index");
        }
        if (parentId != ROOT) {
            requirePresent(parentId);
        }
		int existing = materializedChildId (parentId, childIndex);
		if (existing >= 0 && existing != id) {
			throw new IllegalStateException ("duplicate materialized tree coordinate");
		}
		if (contains (id)) {
			for (int ancestor = parentId; ancestor != ROOT; ancestor = parentIds [ancestor]) {
                if (ancestor == id) {
                    throw new IllegalArgumentException("cyclic tree parent");
                }
			}
		}
		ensureCapacity (id + 1);
		boolean absent = parentIds [id] == ABSENT;
		long visibleContribution = 0;
		if (absent) {
			materializedCount++;
		} else {
			int oldParent = parentIds [id];
			int oldIndex = childIndices [id];
            if (oldParent == parentId && oldIndex == childIndex) {
                return;
            }
			visibleContribution = visibleExtraRows [id];
            if (visibleContribution != 0) {
                propagateVisibleContribution(oldParent, -visibleContribution);
            }
			unlink (id);
		}
		parentIds [id] = parentId;
		childIndices [id] = childIndex;
		nextSiblingIds [id] = -1;
		linkSorted (id);
		generation++;
        if (visibleContribution != 0) {
            propagateVisibleContribution(parentId, visibleContribution);
        }
	}

	void insertCoordinate (int parentId, int childIndex, int id) {
        if (childIndex < 0) {
            throw new IllegalArgumentException("negative child index");
        }
		shiftSiblingIndices (parentId, childIndex, 1);
		bind (id, parentId, childIndex);
		adjustKnownChildCount (parentId, 1);
	}

	void removeCoordinate (int parentId, int childIndex) {
		if (childIndex < 0) throw new IllegalArgumentException ("negative child index");
		if (parentId != ROOT) requirePresent (parentId);
		int count = childCount (parentId);
		if (childIndex >= count) throw new IndexOutOfBoundsException (childIndex);
		int id = materializedChildId (parentId, childIndex);
		if (id >= 0) discardSubtree (id);
		shiftSiblingIndices (parentId, childIndex + 1, -1);
		adjustKnownChildCount (parentId, -1);
	}

	void releaseSubtree (int id) {
        if (!contains(id)) {
            return;
        }
		int parentId = parentIds [id];
		int removedIndex = childIndices [id];
		discardSubtree (id);
		shiftSiblingIndices (parentId, removedIndex + 1, -1);
		adjustKnownChildCount (parentId, -1);
	}

	void clear () {
		Arrays.fill (parentIds, ABSENT);
		Arrays.fill (childIndices, -1);
		Arrays.fill (childCounts, UNKNOWN_CHILD_COUNT);
		Arrays.fill (firstChildIds, -1);
		Arrays.fill (nextSiblingIds, -1);
		Arrays.fill (stateMasks, 0);
		Arrays.fill (visibleExtraRows, 0);
		Arrays.fill (childVisibleExtraSums, 0);
		rootChildCount = UNKNOWN_CHILD_COUNT;
		rootFirstChildId = -1;
		rootVisibleExtraRows = 0;
		siblings.clear ();
		materializedCount = 0;
		presentationGeneration = Math.incrementExact (presentationGeneration);
		generation++;
	}

	/** Revision of coordinates, child counts and expansion; presentation flags are independent. */
	long generation () {
		return generation;
	}

	/** Epoch of parent-owned flags, independent of native hierarchy geometry. */
	long presentationGeneration () {
		return presentationGeneration;
	}

	boolean contains (int id) {
		return id >= 0 && id < parentIds.length && parentIds [id] != ABSENT;
	}

	int idCapacity () {
		return parentIds.length;
	}

	int materializedCount () {
		return materializedCount;
	}

	int parentId (int id) {
		requirePresent (id);
		return parentIds [id];
	}

	int childIndex (int id) {
		requirePresent (id);
		return childIndices [id];
	}

	int firstMaterializedChildId (int parentId) {
        if (parentId == ROOT) {
            return rootFirstChildId;
        }
		requirePresent (parentId);
		return firstChildIds [parentId];
	}

	int nextMaterializedSiblingId (int id) {
		requirePresent (id);
		return nextSiblingIds [id];
	}

	int materializedChildId (int parentId, int childIndex) {
		if (childIndex < 0 || parentId != ROOT && !contains (parentId)) {
			return -1;
		}
		if (siblings.treeHeight (parentId) > LINEAR_HEIGHT) {
			return siblings.find (parentId, childIndex);
		}
		for (int id = firstChild (parentId); id >= 0 && childIndices [id] <= childIndex; id = nextSiblingIds [id]) {
			if (childIndices [id] == childIndex) return id;
		}
		return -1;
	}

	/**
	 * Visible extra rows of the materialized children of {@code parentId} whose
	 * child index is below {@code childIndex}: the rows their expanded subtrees
	 * add in front of that coordinate. O(log k) for k materialized siblings.
	 */
	long visibleExtraRowsBefore (int parentId, int childIndex) {
		if (parentId != ROOT) requirePresent (parentId);
		if (siblings.treeHeight (parentId) > LINEAR_HEIGHT) {
			return siblings.prefix (parentId, childIndex);
		}
		long result = 0;
		for (int id = firstChild (parentId); id >= 0 && childIndices [id] < childIndex; id = nextSiblingIds [id]) {
			result += visibleExtraRows [id];
		}
		return result;
	}

	/** Rows that the expanded subtree of {@code id} adds below it; 0 when collapsed. */
	long visibleExtraRows (int id) {
		requirePresent (id);
		return visibleExtraRows [id];
	}

	/**
	 * Returns the last materialized child c of {@code parentId} with child index
	 * below {@code limit} whose row offset among its siblings, its index plus
	 * {@link #visibleExtraRowsBefore(int, int)}, is at most {@code row}; or -1.
	 * {@link #seekExtraRowsBefore()} then answers the extra rows in front of c.
	 */
	int seekMaterializedChild (int parentId, long row, int limit) {
		if (parentId != ROOT) requirePresent (parentId);
		if (siblings.treeHeight (parentId) > LINEAR_HEIGHT) {
			int found = siblings.seek (parentId, row, limit);
			seekBefore = siblings.seekBefore ();
			return found;
		}
		int found = -1;
		long before = 0;
		seekBefore = 0;
		for (int id = firstChild (parentId); id >= 0; id = nextSiblingIds [id]) {
			int index = childIndices [id];
			if (index >= limit || index + before > row) break;
			found = id;
			seekBefore = before;
			before += visibleExtraRows [id];
		}
		return found;
	}

	/** Extra rows in front of the child returned by the last seek. */
	long seekExtraRowsBefore () {
		return seekBefore;
	}

	private int firstChild (int parentId) {
		return parentId == ROOT ? rootFirstChildId : firstChildIds [parentId];
	}

	void setChildCount (int parentId, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("negative child count");
        }
		if (parentId == ROOT) {
			pruneCoordinatesPast (ROOT, count);
			if (rootChildCount != count) generation++;
			rootChildCount = count;
			return;
		}
		requirePresent (parentId);
		pruneCoordinatesPast (parentId, count);
		if (childCounts [parentId] != count) generation++;
		childCounts [parentId] = count;
		long state = stateMasks [parentId] | VirtualItemState.CHILDREN_KNOWN
				| VirtualItemState.CHILDREN_COMPLETE;
		state &= ~(VirtualItemState.CHILDREN_LOADING | VirtualItemState.CHILDREN_PARTIAL);
        if (count == 0) {
            state &= ~VirtualItemState.HAS_CHILDREN;
        } else {
            state |= VirtualItemState.HAS_CHILDREN;
        }
		stateMasks [parentId] = state;
		refreshVisibleExtra (parentId);
	}

	boolean childCountKnown (int parentId) {
        if (parentId == ROOT) {
            return rootChildCount != UNKNOWN_CHILD_COUNT;
        }
		return contains (parentId) && childCounts [parentId] != UNKNOWN_CHILD_COUNT;
	}

	int childCount (int parentId) {
		if (parentId == ROOT) {
            if (rootChildCount == UNKNOWN_CHILD_COUNT) {
                throw new IllegalStateException("root child count unknown");
            }
			return rootChildCount;
		}
		requirePresent (parentId);
		int count = childCounts [parentId];
        if (count == UNKNOWN_CHILD_COUNT) {
            throw new IllegalStateException("child count unknown");
        }
		return count;
	}

	/** Primitive, rebuildable snapshot for a native logical GtkTreeModel. */
	int [] nativeModelSnapshot () {
		int capacity = parentIds.length;
		int [] snapshot = new int [Math.addExact (2, Math.multiplyExact (capacity, 3))];
		snapshot [0] = capacity;
		snapshot [1] = rootChildCount == UNKNOWN_CHILD_COUNT ? 0 : rootChildCount;
		System.arraycopy (parentIds, 0, snapshot, 2, capacity);
		System.arraycopy (childIndices, 0, snapshot, 2 + capacity, capacity);
		System.arraycopy (childCounts, 0, snapshot, 2 + capacity * 2, capacity);
		return snapshot;
	}

	void state (int id, long state) {
		requirePresent (id);
		long before = stateMasks [id];
		if (before == state) return;
		boolean expansionChanged = ((before ^ state) & VirtualItemState.EXPANDED) != 0;
		long nextPresentation = Math.incrementExact (presentationGeneration);
		stateMasks [id] = state;
		presentationGeneration = nextPresentation;
        if (expansionChanged) {
            generation++;
            refreshVisibleExtra(id);
        }
	}

	long state (int id) {
		requirePresent (id);
		return stateMasks [id];
	}

	void flag (int id, long flag, boolean value) {
		requirePresent (id);
		boolean old = (stateMasks [id] & flag) != 0;
        if (old == value) {
            return;
        }
        long nextPresentation = Math.incrementExact (presentationGeneration);
        if (value) {
            stateMasks [id] |= flag;
        } else {
            stateMasks [id] &= ~flag;
        }
        presentationGeneration = nextPresentation;
        if ((flag & VirtualItemState.EXPANDED) != 0) {
            generation++;
            refreshVisibleExtra(id);
        }
	}

	boolean flag (int id, long flag) {
		requirePresent (id);
		return (stateMasks [id] & flag) != 0;
	}

	long visibleRowCount () {
		return Math.addExact ((long)childCount (ROOT), rootVisibleExtraRows);
	}

	long visibleChildrenRowCount (int parentId) {
		long extra = parentId == ROOT ? rootVisibleExtraRows : childVisibleExtraSums [parentId];
		return Math.addExact ((long)childCount (parentId), extra);
	}

	int highestChildIndexWithSubtreeFlag (int parentId, long flag) {
		int highest = -1;
		for (int id = firstMaterializedChildId (parentId); id >= 0; id = nextSiblingIds [id]) {
            if (subtreeHasFlag(id, flag)) {
                highest = childIndices [id];
            }
		}
		return highest;
	}

	void forgetSubtree (int id) {
		discardSubtree (id);
	}

	private boolean subtreeHasFlag (int id, long flag) {
		if (!contains (id)) {
			return false;
		}
		int current = id;
		while (true) {
			if ((stateMasks [current] & flag) != 0) {
				return true;
			}
			int child = firstChildIds [current];
			if (child >= 0) {
				current = child;
				continue;
			}
			/* Reuse parent links, but never escape the queried subtree. */
			while (current != id && nextSiblingIds [current] < 0) {
				current = parentIds [current];
			}
			if (current == id) {
				return false;
			}
			current = nextSiblingIds [current];
		}
	}

	private void pruneCoordinatesPast (int parentId, int count) {
		int id = firstMaterializedChildId (parentId);
		while (id >= 0) {
			int next = nextSiblingIds [id];
            if (childIndices [id] >= count) {
                discardSubtree(id);
            }
			id = next;
		}
	}

	private void discardSubtree (int id) {
        if (!contains(id)) {
            return;
        }
		int parentId = parentIds [id];
		long visibleContribution = visibleExtraRows [id];
        if (visibleContribution != 0) {
            propagateVisibleContribution(parentId, -visibleContribution);
        }
		discardSubtreeDetached (id);
	}

	private void discardSubtreeDetached (int id) {
		if (!contains (id)) {
			return;
		}
		int current = id;
		while (true) {
			int child = firstChildIds [current];
			if (child >= 0) {
				current = child;
				continue;
			}
			/* Delete leaves before their parents. Unlink advances the parent's
			 * first-child lane, which supplies the next postorder step without
			 * recursion, an auxiliary stack, or per-node temporary objects. */
			int parentId = parentIds [current];
			unlink (current);
			parentIds [current] = ABSENT;
			childIndices [current] = -1;
			childCounts [current] = UNKNOWN_CHILD_COUNT;
			firstChildIds [current] = -1;
			nextSiblingIds [current] = -1;
			stateMasks [current] = 0;
			visibleExtraRows [current] = 0;
			childVisibleExtraSums [current] = 0;
			materializedCount--;
			generation++;
			if (current == id) {
				return;
			}
			current = parentId;
		}
	}

	private void adjustKnownChildCount (int parentId, int delta) {
		if (parentId == ROOT) {
            if (rootChildCount != UNKNOWN_CHILD_COUNT) {
                rootChildCount = Math.max(0, rootChildCount + delta);
                generation++;
            }
			return;
		}
		if (contains (parentId) && childCounts [parentId] != UNKNOWN_CHILD_COUNT) {
			childCounts [parentId] = Math.max (0, childCounts [parentId] + delta);
			generation++;
            if (childCounts [parentId] == 0) {
                stateMasks [parentId] &= ~VirtualItemState.HAS_CHILDREN;
            } else {
                stateMasks [parentId] |= VirtualItemState.HAS_CHILDREN;
            }
			refreshVisibleExtra (parentId);
		}
	}

	private void refreshVisibleExtra (int id) {
		requirePresent (id);
		long next = 0;
		if ((stateMasks [id] & VirtualItemState.EXPANDED) != 0
				&& childCounts [id] != UNKNOWN_CHILD_COUNT) {
			next = Math.addExact ((long)childCounts [id], childVisibleExtraSums [id]);
		}
		long delta = Math.subtractExact (next, visibleExtraRows [id]);
        if (delta == 0) {
            return;
        }
		visibleExtraRows [id] = next;
		siblings.adjust (parentIds [id], id, delta);
		propagateVisibleContribution (parentIds [id], delta);
	}

	private void propagateVisibleContribution (int parentId, long delta) {
        if (delta == 0) {
            return;
        }
		while (parentId != ROOT) {
			requirePresent (parentId);
			childVisibleExtraSums [parentId] = Math.addExact (childVisibleExtraSums [parentId], delta);
            if ((stateMasks [parentId] & VirtualItemState.EXPANDED) == 0
                    || childCounts [parentId] == UNKNOWN_CHILD_COUNT) {
                return;
            }
			visibleExtraRows [parentId] = Math.addExact (visibleExtraRows [parentId], delta);
			siblings.adjust (parentIds [parentId], parentId, delta);
			parentId = parentIds [parentId];
		}
		rootVisibleExtraRows = Math.addExact (rootVisibleExtraRows, delta);
	}

	private void shiftSiblingIndices (int parentId, int fromInclusive, int delta) {
        if (delta == 0) {
            return;
        }
		if ((long)materializedCount * 4 < parentIds.length
				&& (parentId == ROOT || contains (parentId))
				&& tryShiftSparseSiblings (parentId, fromInclusive, delta)) return;
		boolean changed = false;
		for (int id = 0; id < parentIds.length; id++) {
			if (parentIds [id] == parentId && childIndices [id] >= fromInclusive) {
				changed = shiftSiblingIndex (id, delta, changed);
			}
		}
	}

	private boolean tryShiftSparseSiblings (int parentId, int fromInclusive, int delta) {
		int first = firstMaterializedChildId (parentId);
		// Overflow must retain the original id-ordered partial updates.
		for (int id = first; id >= 0; id = nextSiblingIds [id]) {
			if (childIndices [id] < fromInclusive) continue;
			long shifted = (long)childIndices [id] + delta;
			if (shifted < Integer.MIN_VALUE || shifted > Integer.MAX_VALUE) return false;
		}
		boolean changed = false;
		for (int id = first; id >= 0; id = nextSiblingIds [id]) {
			if (childIndices [id] >= fromInclusive) {
				changed = shiftSiblingIndex (id, delta, changed);
			}
		}
		return true;
	}

	private boolean shiftSiblingIndex (int id, int delta, boolean changed) {
		childIndices [id] = Math.addExact (childIndices [id], delta);
		if (!changed) generation++;
		return true;
	}

	/* The sibling index supplies the predecessor in O(log k); the sibling chain
	 * stays for in-order iteration. */
	private void linkSorted (int id) {
		int parentId = parentIds [id];
		int previous = siblings.lower (parentId, childIndices [id]);
		siblings.insert (parentId, id);
		if (previous < 0) {
			nextSiblingIds [id] = firstChild (parentId);
			if (parentId == ROOT) {
				rootFirstChildId = id;
			} else {
				firstChildIds [parentId] = id;
			}
			return;
		}
		nextSiblingIds [id] = nextSiblingIds [previous];
		nextSiblingIds [previous] = id;
	}

	private void unlink (int id) {
        if (!contains(id)) {
            return;
        }
		int parentId = parentIds [id];
		int previous = siblings.lower (parentId, childIndices [id]);
		if (previous < 0 ? firstChild (parentId) != id : nextSiblingIds [previous] != id) {
			throw new IllegalStateException ("materialized tree sibling chain is inconsistent");
		}
		siblings.remove (parentId, id);
		if (previous >= 0) {
			nextSiblingIds [previous] = nextSiblingIds [id];
		} else if (parentId == ROOT) {
			rootFirstChildId = nextSiblingIds [id];
		} else {
			firstChildIds [parentId] = nextSiblingIds [id];
		}
		nextSiblingIds [id] = -1;
	}

	private void ensureCapacity (int required) {
        if (required <= parentIds.length) {
            return;
        }
		int next = Math.max (required, Math.max (4, parentIds.length * 3 / 2));
		int old = parentIds.length;
		parentIds = Arrays.copyOf (parentIds, next);
		childIndices = Arrays.copyOf (childIndices, next);
		childCounts = Arrays.copyOf (childCounts, next);
		firstChildIds = Arrays.copyOf (firstChildIds, next);
		nextSiblingIds = Arrays.copyOf (nextSiblingIds, next);
		stateMasks = Arrays.copyOf (stateMasks, next);
		visibleExtraRows = Arrays.copyOf (visibleExtraRows, next);
		childVisibleExtraSums = Arrays.copyOf (childVisibleExtraSums, next);
		Arrays.fill (parentIds, old, next, ABSENT);
		Arrays.fill (childIndices, old, next, -1);
		Arrays.fill (childCounts, old, next, UNKNOWN_CHILD_COUNT);
		Arrays.fill (firstChildIds, old, next, -1);
		Arrays.fill (nextSiblingIds, old, next, -1);
		siblings.ensureCapacity (next);
		siblings.lanes (childIndices, visibleExtraRows);
	}

	private void requirePresent (int id) {
        if (!contains(id)) {
            throw new IllegalArgumentException("unknown tree id " + id);
        }
	}
}
