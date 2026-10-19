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

import java.util.*;

/**
 * Native window of a viewport Tree: which native rows survive a window move, which are
 * deleted and where the new rows go.
 *
 * <p>A row is identified by its logical coordinate (parent topology id, child index), never
 * by its native position: a native parent holds a contiguous sub-range of its logical
 * children. Rows present before and after a move keep their native handle, so native
 * focus, hot and selection state survive. Only top-most retired rows are deleted, because
 * deleting a native parent deletes its subtree. New rows are inserted in preorder, each
 * after its previous physical sibling, which keeps the logical order of every native
 * parent.</p>
 *
 * <p>Inputs are the dense preorder of {@link VirtualTreeVisibleProjection.Residency}:
 * logical parent id, child index and physical parent entry ({@code -1} for a root row).
 * The plan allocates nothing per logical row; its arrays are sized by the window.</p>
 */
final class VirtualTreeResidentPlan {
	private long [] keys = new long [0], handles = new long [0];
	private int [] parentSlots = new int [0];
	private int size;
	private final HashMap<Long, Integer> slotByKey = new HashMap<> ();
	private final HashMap<Long, Integer> slotByHandle = new HashMap<> ();

	private long [] planned = new long [0], retired = new long [0];
	private int [] previousSiblings = new int [0];
	private boolean [] kept = new boolean [0];
	private int plannedCount, retiredCount;

	static long key (int parentId, int childIndex) {
		return ((long) parentId << 32) | (childIndex & 0xFFFFFFFFL);
	}

	/** Number of native rows of the committed window. */
	int size () {
		return size;
	}

	/** Native handle of the committed row at this coordinate, or 0 when it is not resident. */
	long handleOf (int parentId, int childIndex) {
		Integer slot = slotByKey.get (key (parentId, childIndex));
		return slot == null ? 0 : handles [slot];
	}

	/** Entry (dense preorder index) of a committed native handle, or -1. */
	int entryOf (long handle) {
		Integer slot = slotByHandle.get (handle);
		return slot == null ? -1 : slot;
	}

	/** Native handle of a committed entry. */
	long handle (int entry) {
		requireEntry (entry);
		return handles [entry];
	}

	/** Logical coordinate key of a committed entry. */
	long keyOf (int entry) {
		requireEntry (entry);
		return keys [entry];
	}

	/**
	 * Plans a move to the next window. Afterwards {@link #plannedHandle(int)} is the reused
	 * native handle of each next entry (0 when it must be inserted) and
	 * {@link #retired(int)} lists the top-most native rows to delete.
	 */
	void plan (int count, int [] parentIds, int [] childIndices, int [] physicalParents) {
		if (count < 0 || parentIds.length < count || childIndices.length < count || physicalParents.length < count) {
			throw new IllegalArgumentException ("window arrays");
		}
		if (planned.length < count) {
			planned = new long [capacity (planned.length, count)];
			previousSiblings = new int [planned.length];
		}
		if (kept.length < size) kept = new boolean [capacity (kept.length, size)];
		Arrays.fill (kept, 0, size, false);
		int [] lastChild = new int [count + 1];
		Arrays.fill (lastChild, -1);
		for (int entry = 0; entry < count; entry++) {
			int physicalParent = physicalParents [entry];
			if (physicalParent < -1 || physicalParent >= entry) throw new IllegalArgumentException ("preorder");
			previousSiblings [entry] = lastChild [physicalParent + 1];
			lastChild [physicalParent + 1] = entry;
			/* A reused child handle is valid only while its native parent survives. A logical
			 * parent may retain its topology id while its coordinate (and native row) changes.
			 * Reusing the child's old handle after deleting that parent would be use-after-delete. */
			boolean parentSurvives = physicalParent == -1 || planned [physicalParent] != 0;
			Integer slot = slotByKey.get (key (parentIds [entry], childIndices [entry]));
			if (parentSurvives && slot != null && !kept [slot]) {
				kept [slot] = true;
				planned [entry] = handles [slot];
			} else {
				planned [entry] = 0;
			}
		}
		plannedCount = count;
		retiredCount = 0;
		for (int slot = 0; slot < size; slot++) {
			int parent = parentSlots [slot];
			if (!kept [slot] && (parent == -1 || kept [parent])) {
				if (retiredCount == retired.length) retired = Arrays.copyOf (retired, capacity (retired.length, retiredCount + 1));
				retired [retiredCount++] = handles [slot];
			}
		}
	}

	/** Convenience form of {@link #plan(int, int[], int[], int[])} over a residency buffer. */
	void plan (VirtualTreeVisibleProjection.Residency next) {
		int count = next.size ();
		int [] parentIds = new int [count], childIndices = new int [count], physicalParents = new int [count];
		for (int entry = 0; entry < count; entry++) {
			parentIds [entry] = next.parentId (entry);
			childIndices [entry] = next.childIndex (entry);
			physicalParents [entry] = next.physicalParent (entry);
		}
		plan (count, parentIds, childIndices, physicalParents);
		lastParentIds = parentIds;
		lastChildIndices = childIndices;
		lastPhysicalParents = physicalParents;
	}

	private int [] lastParentIds, lastChildIndices, lastPhysicalParents;

	int plannedCount () {
		return plannedCount;
	}

	/** Reused native handle of a planned entry, or 0 when the entry must be inserted. */
	long plannedHandle (int entry) {
		if (entry < 0 || entry >= plannedCount) throw new IllegalArgumentException ("planned entry");
		return planned [entry];
	}

	/** Previous physical sibling of a planned entry in the next window, or -1 for a first child. */
	int previousSibling (int entry) {
		if (entry < 0 || entry >= plannedCount) throw new IllegalArgumentException ("planned entry");
		return previousSiblings [entry];
	}

	int retiredCount () {
		return retiredCount;
	}

	/** Top-most native row to delete; deleting it deletes its native subtree. */
	long retired (int index) {
		if (index < 0 || index >= retiredCount) throw new IllegalArgumentException ("retired index");
		return retired [index];
	}

	/** Records the native handles of the planned window (reused and inserted) as the committed window. */
	void commit (long [] nativeHandles) {
		if (lastParentIds == null) throw new IllegalStateException ("commit without residency plan");
		commit (plannedCount, lastParentIds, lastChildIndices, lastPhysicalParents, nativeHandles);
	}

	void commit (int count, int [] parentIds, int [] childIndices, int [] physicalParents, long [] nativeHandles) {
		if (count != plannedCount || nativeHandles.length < count) throw new IllegalArgumentException ("commit arrays");
		if (keys.length < count) {
			int next = capacity (keys.length, count);
			keys = new long [next];
			handles = new long [next];
			parentSlots = new int [next];
		}
		slotByKey.clear ();
		slotByHandle.clear ();
		for (int entry = 0; entry < count; entry++) {
			long handle = nativeHandles [entry];
			if (handle == 0) throw new IllegalArgumentException ("uncommitted native row " + entry);
			keys [entry] = key (parentIds [entry], childIndices [entry]);
			handles [entry] = handle;
			parentSlots [entry] = physicalParents [entry];
			slotByKey.put (keys [entry], entry);
			slotByHandle.put (handle, entry);
		}
		size = count;
		lastParentIds = lastChildIndices = lastPhysicalParents = null;
	}

	/**
	 * Siblings at or after <code>fromIndex</code> under <code>parentId</code> moved by <code>delta</code>
	 * (an insert or a removal before them). Their committed rows keep their native handles and only
	 * change keys, so the next plan reuses them instead of rebuilding the window.
	 */
	void shiftSiblings (int parentId, int fromIndex, int delta) {
		if (delta == 0) return;
		boolean moved = false;
		for (int entry = 0; entry < size; entry++) {
			long key = keys [entry];
			int index = (int) key;
			if ((int) (key >> 32) == parentId && index >= fromIndex) {
				if (index + delta < 0) throw new IllegalArgumentException ("negative child index");
				keys [entry] = key (parentId, index + delta);
				moved = true;
			}
		}
		if (moved) reindex ();
	}

	/**
	 * The committed row of <code>entry</code> and its native subtree were deleted by the caller. In
	 * preorder the subtree is the entry and the following entries whose physical parent lies inside it.
	 */
	void removeSubtree (int entry) {
		requireEntry (entry);
		int end = entry + 1;
		while (end < size && parentSlots [end] >= entry) end++;
		int removed = end - entry, tail = size - end;
		System.arraycopy (keys, end, keys, entry, tail);
		System.arraycopy (handles, end, handles, entry, tail);
		System.arraycopy (parentSlots, end, parentSlots, entry, tail);
		size -= removed;
		for (int next = entry; next < size; next++) {
			if (parentSlots [next] >= end) parentSlots [next] -= removed;
		}
		reindex ();
	}

	private void reindex () {
		slotByKey.clear ();
		slotByHandle.clear ();
		for (int entry = 0; entry < size; entry++) {
			slotByKey.put (keys [entry], entry);
			slotByHandle.put (handles [entry], entry);
		}
		plannedCount = retiredCount = 0;
		lastParentIds = lastChildIndices = lastPhysicalParents = null;
	}

	/** Forgets the committed window (all native rows were deleted by the caller). */
	void clear () {
		size = plannedCount = retiredCount = 0;
		slotByKey.clear ();
		slotByHandle.clear ();
		lastParentIds = lastChildIndices = lastPhysicalParents = null;
	}

	private void requireEntry (int entry) {
		if (entry < 0 || entry >= size) throw new IllegalArgumentException ("resident entry");
	}

	private static int capacity (int current, int required) {
		return (int) Math.min (Integer.MAX_VALUE - 8, Math.max ((long) required, Math.max (8L, current + (long) current / 2)));
	}
}
