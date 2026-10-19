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
 * Flat visible-row projection over {@link VirtualTreeTopology}.
 *
 * <p>Cold logical children are arithmetic coordinates and require no topology
 * slot. Only materialized expanded nodes contribute descendant rows. The
 * projection therefore scales with the materialized/expanded frontier rather
 * than total logical tree cardinality.</p>
 */
final class VirtualTreeVisibleProjection {

	record Row (int parentId, int childIndex, int materializedId, int depth) {
		boolean materialized () {
			return materializedId >= 0;
		}
	}

	/**
	 * Caller-owned native demand buffer. Entries are a dense physical preorder of
	 * paint rows and their ancestor closure. Logical sibling indices remain
	 * separate from dense physical indices; no Item is materialized by planning.
	 */
	static final class Residency {
		private int [] parentIds = new int [0], childIndices = new int [0];
		private int [] materializedIds = new int [0], depths = new int [0];
		private int [] physicalParents = new int [0], physicalIndices = new int [0];
		private int [] childCounts = new int [0], firstChildren = new int [0];
		private int [] lastChildren = new int [0], nextSiblings = new int [0];
		private long [] visibleRows = new long [0];
		private int [] paintEntries = new int [0];
		private int [] pathIds = new int [0], pathEntries = new int [0];
		private long [] pathVisibleRows = new long [0];
		private int size, paintCount, rootCount, firstRoot = -1, lastRoot = -1;
		private long firstPaintRow, sourceGeneration = -1;

		boolean isHint (int entry) { requireEntry (entry); return materializedIds [entry] == -2; }

		void bindFacade (int entry, int id) {
			requireEntry (entry);
			if (id < 0 || isHint (entry)) throw new IllegalArgumentException ("facade binding");
			materializedIds [entry] = id;
		}

		/** One non-paint child gives GTK a real expander without advertising N. */
		void addExpanderHints (VirtualTreeTopology topology) {
			int rows = size;
			for (int entry = 0; entry < rows; entry++) {
				int id = materializedIds [entry];
				if (id >= 0 && childCounts [entry] == 0 && topology.childCountKnown (id)
						&& topology.childCount (id) > 0) {
					appendEntry (id, 0, -2, depths [entry] + 1, entry, -1);
				}
			}
		}

		int size () { return size; }
		int paintCount () { return paintCount; }
		int rootCount () { return rootCount; }
		long firstPaintRow () { return firstPaintRow; }
		long sourceGeneration () { return sourceGeneration; }

		int parentId (int entry) { requireEntry (entry); return parentIds [entry]; }
		int childIndex (int entry) { requireEntry (entry); return childIndices [entry]; }
		int materializedId (int entry) { requireEntry (entry); return materializedIds [entry]; }
		int depth (int entry) { requireEntry (entry); return depths [entry]; }
		int physicalParent (int entry) { requireEntry (entry); return physicalParents [entry]; }
		int physicalIndex (int entry) { requireEntry (entry); return physicalIndices [entry]; }
		int childCount (int entry) { requireEntry (entry); return childCounts [entry]; }
		long visibleRow (int entry) { requireEntry (entry); return visibleRows [entry]; }

		int paintEntry (int offset) {
			if (offset < 0 || offset >= paintCount) throw new IllegalArgumentException ("paint offset");
			return paintEntries [offset];
		}

		int entryForMaterializedId (int id) {
			if (id < 0) return -1;
			for (int entry = 0; entry < size; entry++) {
				if (materializedIds [entry] == id) return entry;
			}
			return -1;
		}

		int entryForCoordinate (int parentId, int childIndex) {
			for (int entry = 0; entry < size; entry++) {
				if (parentIds [entry] == parentId && childIndices [entry] == childIndex) return entry;
			}
			return -1;
		}

		int physicalChild (int parentEntry, int index) {
			if (parentEntry != -1) requireEntry (parentEntry);
			int count = parentEntry == -1 ? rootCount : childCounts [parentEntry];
			if (index < 0 || index >= count) return -1;
			int entry = parentEntry == -1 ? firstRoot : firstChildren [parentEntry];
			for (int i = 0; i < index; i++) entry = nextSiblings [entry];
			return entry;
		}

		int entryAtPhysicalPath (int [] path, int length) {
			if (path == null || length <= 0 || length > path.length) {
				throw new IllegalArgumentException ("physical path");
			}
			int entry = -1;
			for (int level = 0; level < length; level++) {
				entry = physicalChild (entry, path [level]);
				if (entry < 0) return -1;
			}
			return entry;
		}

		/** Writes a physical path into caller-owned storage; returns its length. */
		int physicalPath (int entry, int [] path) {
			requireEntry (entry);
			int length = Math.incrementExact (depths [entry]);
			if (path == null || path.length < length) throw new IllegalArgumentException ("path capacity");
			for (int level = length - 1; level >= 0; level--) {
				path [level] = physicalIndices [entry];
				entry = physicalParents [entry];
			}
			return length;
		}

		/**
		 * Rebuildable native packet: entry/root counts, then physical parent,
		 * physical sibling index, physical child count and SWT facade-id lanes.
		 * Native child counts describe this bounded projection, never logical N.
		 */
		int [] nativeModelSnapshot () {
			int [] snapshot = new int [Math.addExact (2, Math.multiplyExact (size, 4))];
			snapshot [0] = size;
			snapshot [1] = rootCount;
			System.arraycopy (physicalParents, 0, snapshot, 2, size);
			System.arraycopy (physicalIndices, 0, snapshot, 2 + size, size);
			System.arraycopy (childCounts, 0, snapshot, 2 + size * 2, size);
			System.arraycopy (materializedIds, 0, snapshot, 2 + size * 3, size);
			return snapshot;
		}

		private void reset (long firstRow) {
			size = paintCount = rootCount = 0;
			firstRoot = lastRoot = -1;
			firstPaintRow = firstRow;
			sourceGeneration = -1;
		}

		private void appendPaintRow (VirtualTreeVisibleProjection projection,
				int parentId, int childIndex, int id, int depth, long visibleRow) {
			ensurePathCapacity (Math.incrementExact (depth));
			if (paintCount == 0) {
				VirtualTreeTopology topology = projection.topology;
				int ancestor = parentId, index = childIndex;
				long ancestorRow = visibleRow;
				for (int level = depth - 1; level >= 0; level--) {
					pathIds [level] = ancestor;
					ancestorRow = Math.subtractExact (ancestorRow,
							Math.addExact (1, projection.offsetWithinParent (ancestor, index)));
					pathVisibleRows [level] = ancestorRow;
					index = topology.childIndex (ancestor);
					ancestor = topology.parentId (ancestor);
				}
				if (ancestor != VirtualTreeTopology.ROOT) {
					throw new IllegalStateException ("projection ancestry mismatch");
				}
				for (int level = 0; level < depth; level++) {
					ancestor = pathIds [level];
					pathEntries [level] = appendEntry (topology.parentId (ancestor),
							topology.childIndex (ancestor), ancestor, level,
							level == 0 ? -1 : pathEntries [level - 1], pathVisibleRows [level]);
				}
			}
			int parentEntry = depth == 0 ? -1 : pathEntries [depth - 1];
			if (depth > 0 && materializedIds [parentEntry] != parentId) {
				throw new IllegalStateException ("non-contiguous projection window");
			}
			int entry = appendEntry (parentId, childIndex, id, depth, parentEntry, visibleRow);
			pathEntries [depth] = entry;
			if (paintCount == paintEntries.length) {
				paintEntries = Arrays.copyOf (paintEntries, capacity (paintEntries.length, paintCount + 1));
			}
			paintEntries [paintCount++] = entry;
		}

		private int appendEntry (int parentId, int childIndex, int id,
				int depth, int parentEntry, long visibleRow) {
			ensureCapacity (Math.incrementExact (size));
			int entry = size++;
			parentIds [entry] = parentId;
			childIndices [entry] = childIndex;
			materializedIds [entry] = id;
			depths [entry] = depth;
			visibleRows [entry] = visibleRow;
			physicalParents [entry] = parentEntry;
			childCounts [entry] = 0;
			firstChildren [entry] = lastChildren [entry] = nextSiblings [entry] = -1;
			if (parentEntry == -1) {
				physicalIndices [entry] = rootCount++;
				if (lastRoot == -1) firstRoot = entry;
				else nextSiblings [lastRoot] = entry;
				lastRoot = entry;
			} else {
				physicalIndices [entry] = childCounts [parentEntry]++;
				int previous = lastChildren [parentEntry];
				if (previous == -1) firstChildren [parentEntry] = entry;
				else nextSiblings [previous] = entry;
				lastChildren [parentEntry] = entry;
			}
			return entry;
		}

		private void ensureCapacity (int required) {
			if (required <= parentIds.length) return;
			int next = capacity (parentIds.length, required);
			parentIds = Arrays.copyOf (parentIds, next);
			childIndices = Arrays.copyOf (childIndices, next);
			materializedIds = Arrays.copyOf (materializedIds, next);
			depths = Arrays.copyOf (depths, next);
			physicalParents = Arrays.copyOf (physicalParents, next);
			physicalIndices = Arrays.copyOf (physicalIndices, next);
			childCounts = Arrays.copyOf (childCounts, next);
			firstChildren = Arrays.copyOf (firstChildren, next);
			lastChildren = Arrays.copyOf (lastChildren, next);
			nextSiblings = Arrays.copyOf (nextSiblings, next);
			visibleRows = Arrays.copyOf (visibleRows, next);
		}

		private void ensurePathCapacity (int required) {
			if (required <= pathIds.length) return;
			int next = capacity (pathIds.length, required);
			pathIds = Arrays.copyOf (pathIds, next);
			pathEntries = Arrays.copyOf (pathEntries, next);
			pathVisibleRows = Arrays.copyOf (pathVisibleRows, next);
		}

		private static int capacity (int current, int required) {
			return (int)Math.min (Integer.MAX_VALUE,
					Math.max ((long)required, Math.max (4L, current + (long)current / 2)));
		}

		private void requireEntry (int entry) {
			if (entry < 0 || entry >= size) throw new IllegalArgumentException ("resident entry");
		}
	}

	private final VirtualTreeTopology topology;
	/* Result registers of seek (int, long); confined to the owner's thread like the topology. */
	private int seekParent, seekIndex, seekId, seekPrevious;
	private long seekRow;

	VirtualTreeVisibleProjection (VirtualTreeTopology topology) {
        if (topology == null) {
            throw new IllegalArgumentException("topology");
        }
		this.topology = topology;
	}

	long visibleRowCount () {
		return visibleChildren (VirtualTreeTopology.ROOT);
	}

	long generation () {
		return topology.generation ();
	}

	/** Flags stay on the owning Tree topology; native physical rows do not. */
	long presentationGeneration () {
		return topology.presentationGeneration ();
	}

	long visibleChildren (int parentId) {
        if (!topology.childCountKnown(parentId)) {
            return 0;
        }
		return topology.visibleChildrenRowCount (parentId);
	}

	Row rowAt (long visibleIndex) {
		long total = visibleRowCount ();
		if (visibleIndex < 0 || visibleIndex >= total) {
			throw new IllegalArgumentException ("visible row outside projection");
		}
		return rowAt (VirtualTreeTopology.ROOT, visibleIndex, 0);
	}

	Row [] window (long firstVisible, int rowCount) {
        if (firstVisible < 0 || rowCount < 0) {
            throw new IllegalArgumentException("negative window");
        }
		long total = visibleRowCount ();
        if (firstVisible >= total || rowCount == 0) {
            return new Row [0];
        }
		int length = (int)Math.min ((long)rowCount, total - firstVisible);
		Row [] rows = new Row [length];
		fillWindow (firstVisible, length, rows, null);
		return rows;
	}


	/**
	 * Seek by the sibling index, then emit one continuous preorder range.
	 * Existing parent/sibling lanes supply the return path, so no traversal
	 * stack or retained cursor is needed. Cold siblings are skipped arithmetically.
	 */
	void residencyWindow (long firstVisible, int rowCount, Residency target) {
		if (firstVisible < 0 || rowCount < 0 || target == null) {
			throw new IllegalArgumentException ("residency window");
		}
		long total = visibleRowCount ();
		target.reset (firstVisible);
		int length = firstVisible >= total ? 0 : (int)Math.min ((long)rowCount, total - firstVisible);
		fillWindow (firstVisible, length, null, target);
		target.sourceGeneration = generation ();
	}

	private void emitWindowRow (Row [] rows, Residency target, long firstVisible,
			int offset, int parentId, int childIndex, int id, int depth) {
		if (target == null) rows [offset] = new Row (parentId, childIndex, id, depth);
		else target.appendPaintRow (this, parentId, childIndex, id, depth, firstVisible + offset);
	}

	private void fillWindow (long firstVisible, int length, Row [] rows, Residency target) {
		if (length == 0) {
			return;
		}
		int parentId = VirtualTreeTopology.ROOT, depth = 0, written = 0;
		for (long row = firstVisible; seek (parentId, row); row = seekRow) {
			parentId = seekParent;
			depth++;
		}
		int coordinate = seekIndex, nextId = seekId;
		if (nextId < 0) {
			nextId = seekPrevious < 0 ? topology.firstMaterializedChildId (parentId)
					: topology.nextMaterializedSiblingId (seekPrevious);
		}
		while (written < length) {
			int logicalCount = topology.childCount (parentId);
			int coldEnd = nextId >= 0 ? Math.min (logicalCount, topology.childIndex (nextId)) : logicalCount;
			if (coordinate < coldEnd) {
				int emit = Math.min (coldEnd - coordinate, length - written);
				for (int i = 0; i < emit; i++) {
					emitWindowRow (rows, target, firstVisible, written++, parentId, coordinate++, -1, depth);
				}
				if (written == length) {
					return;
				}
			}
			if (nextId >= 0 && coordinate < logicalCount) {
				int id = nextId;
				nextId = topology.nextMaterializedSiblingId (id);
				int index = coordinate++;
				emitWindowRow (rows, target, firstVisible, written++, parentId, index, id, depth);
				if (written == length) {
					return;
				}
				if (topology.visibleExtraRows (id) > 0) {
					depth++;
					parentId = id;
					coordinate = 0;
					nextId = topology.firstMaterializedChildId (id);
				}
				continue;
			}
			if (depth == 0) {
				throw new IllegalStateException ("projection overflow");
			}
			depth--;
			coordinate = topology.childIndex (parentId) + 1;
			nextId = topology.nextMaterializedSiblingId (parentId);
			parentId = topology.parentId (parentId);
		}
	}

	long visibleIndexOfCoordinate (int parentId, int childIndex) {
		if (!topology.childCountKnown (parentId) || childIndex < 0 || childIndex >= topology.childCount (parentId)) {
			throw new IllegalArgumentException ("logical coordinate");
		}
		long base = 0;
		if (parentId != VirtualTreeTopology.ROOT) {
			if (!topology.flag (parentId, VirtualItemState.EXPANDED)) return -1;
			long parentRow = visibleIndexOf (parentId);
			if (parentRow < 0) return -1;
			base = Math.addExact (parentRow, 1);
		}
		return Math.addExact (base, offsetWithinParent (parentId, childIndex));
	}

	long visibleIndexOf (int materializedId) {
        if (!topology.contains(materializedId)) {
            return -1;
        }
		long offset = 0;
		int id = materializedId;
		while (true) {
			int parentId = topology.parentId (id);
			offset = Math.addExact (offset, offsetWithinParent (parentId, topology.childIndex (id)));
            if (parentId == VirtualTreeTopology.ROOT) {
                return offset;
            }
            if (!topology.flag(parentId, VirtualItemState.EXPANDED)) {
                return -1;
            }
			offset = Math.addExact (offset, 1);
			id = parentId;
		}
	}

	private Row rowAt (int parentId, long row, int depth) {
		while (true) {
			if (seek (parentId, row)) {
				parentId = seekParent;
				row = seekRow;
				depth = Math.incrementExact (depth);
				continue;
			}
			return new Row (parentId, seekIndex, seekId, depth);
		}
	}

	/**
	 * Locates visible row {@code row} among the children of {@code parentId} and
	 * their visible descendants with one descent of the sibling index, O(log k)
	 * for k materialized siblings. Returns true when the row lies below the
	 * materialized child {@code seekParent}, at offset {@code seekRow} within its
	 * children. Otherwise the row is the child {@code seekIndex} of
	 * {@code parentId}, materialized as {@code seekId} or cold (-1); a cold
	 * row follows the materialized sibling {@code seekPrevious} (-1 for none).
	 */
	private boolean seek (int parentId, long row) {
		int logicalCount = topology.childCount (parentId);
		int id = topology.seekMaterializedChild (parentId, row, logicalCount);
		long before = id < 0 ? 0 : topology.seekExtraRowsBefore ();
		if (id >= 0) {
			int index = topology.childIndex (id);
			long inside = row - index - before;
			if (inside == 0) {
				return seekResult (index, id, -1);
			}
			long extra = topology.visibleExtraRows (id);
			if (inside <= extra) {
				seekParent = id;
				seekRow = inside - 1;
				return true;
			}
			before += extra;
		}
		long childIndex = row - before;
		if (childIndex >= logicalCount) {
			throw new IllegalStateException ("projection overflow");
		}
		return seekResult ((int)childIndex, -1, id);
	}

	private boolean seekResult (int index, int id, int previous) {
		seekIndex = index;
		seekId = id;
		seekPrevious = previous;
		return false;
	}

	private long offsetWithinParent (int parentId, int targetChildIndex) {
		return Math.addExact ((long)targetChildIndex, topology.visibleExtraRowsBefore (parentId, targetChildIndex));
	}
}
