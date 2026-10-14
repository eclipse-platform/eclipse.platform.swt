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
import java.util.function.*;

/**
 * Platform-neutral model of a flat list: item text, selection, top index and
 * the widest item, with no native types.
 *
 * <p>The model is the Java-side authority a windowed native list can be fed
 * from. Item indices, insert/remove shifting and selection follow the shared
 * viewport layer ({@link VirtualViewportPlanner}); the widest item follows
 * {@link VirtualListWidth}. Argument errors are {@link IllegalArgumentException}
 * so a widget can map them to {@code SWT.error}. Out-of-range indices passed
 * to the selection methods are ignored, as the {@code List} documentation
 * states for {@code select} and {@code deselect}.</p>
 *
 * <p>Lazy mode (additive, {@link #VirtualListModel(ToIntFunction, boolean)}):
 * widths are measured only when asked ({@link #getWidestWidth()} measures every
 * pending slot, {@link #measureItem(int)} one painted row), and slots
 * may be unmaterialized ({@code null}) after {@link #setItemCount(int)},
 * {@link #clear(int)} or {@link #clearAll()}; a widget fills them on demand
 * (for example from an {@code SWT.SetData} callback) through
 * {@link #setItem(int, String)}. Unmaterialized slots measure as the empty
 * string. One {@code String} reference per item is the only per-item object.</p>
 */
final class VirtualListModel {
	/**
	 * Capacity for a lane of length {@code length} that needs {@code needed} slots.
	 * Platform code may install a heap-aware policy (exact length under memory
	 * pressure, doubling otherwise); the default keeps 1.5x growth.
	 */
	static IntBinaryOperator growth = (length, needed) -> Math.max (needed, length + (length >> 1) + 8);
	private final VirtualViewportPlanner planner = new VirtualViewportPlanner (true);
	private final VirtualListWidth width;
	private final boolean lazy;
	private String [] items = new String [0];
	private int count;
	private int rowHeight = 1;
	private int viewportHeight = 1;

	VirtualListModel (ToIntFunction<String> measure) {
		this (measure, false);
	}

	VirtualListModel (ToIntFunction<String> measure, boolean lazyWidths) {
		width = new VirtualListWidth (measure);
		lazy = lazyWidths;
	}

	int getItemCount () {
		return count;
	}

	/** Item text; {@code null} for an unmaterialized slot in lazy mode. */
	String getItem (int index) {
		checkIndex (index);
		return items [index];
	}

	boolean isMaterialized (int index) {
		checkIndex (index);
		return items [index] != null;
	}

	String [] getItems () {
		return Arrays.copyOf (items, count);
	}

	void add (String string) {
		add (string, count);
	}

	void add (String string, int index) {
		if (string == null) throw new IllegalArgumentException ("null item");
		if (index < 0 || index > count) throw new IllegalArgumentException ("invalid index");
		ensureCapacity (Math.addExact (count, 1));
		if (lazy) {
			width.insertUnmeasured (index, 1);
		} else {
			width.insert (index, string);
		}
		System.arraycopy (items, index, items, index + 1, count - index);
		items [index] = string;
		count++;
		planner.insert (index, 1);
		relayout ();
	}

	void setItem (int index, String string) {
		checkIndex (index);
		if (string == null) throw new IllegalArgumentException ("null item");
		if (lazy) {
			width.setUnmeasured (index);
		} else {
			width.set (index, string);
		}
		items [index] = string;
	}

	void setItems (String [] newItems) {
		if (newItems == null) throw new IllegalArgumentException ("null items");
		for (String string : newItems) {
			if (string == null) throw new IllegalArgumentException ("null item");
		}
		if (lazy) {
			width.setAllUnmeasured (newItems.length);
		} else {
			width.setAll (newItems);
		}
		items = Arrays.copyOf (newItems, newItems.length);
		count = items.length;
		planner.selection ().clear ();
		planner.setLogicalCount (count);
		setTopIndex (0);
	}

	/**
	 * Lazy mode: grows the list with unmaterialized slots or shrinks it from
	 * the end; selection and top index follow insert/remove semantics.
	 */
	void setItemCount (int newCount) {
		if (!lazy) throw new IllegalStateException ("not lazy");
		if (newCount < 0) throw new IllegalArgumentException ("negative count");
		if (newCount < count) {
			remove (newCount, count - 1);
			return;
		}
		int added = newCount - count;
		if (added == 0) return;
		ensureCapacity (newCount);
		Arrays.fill (items, count, newCount, null);
		width.insertUnmeasured (count, added);
		int start = count;
		count = newCount;
		planner.insert (start, added);
		relayout ();
	}

	/** Lazy mode: forgets the text of one slot. */
	void clear (int index) {
		checkIndex (index);
		if (!lazy) throw new IllegalStateException ("not lazy");
		items [index] = null;
		width.setUnmeasured (index);
	}

	/** Lazy mode: forgets the text of every slot. */
	void clearAll () {
		if (!lazy) throw new IllegalStateException ("not lazy");
		Arrays.fill (items, 0, count, null);
		width.setAllUnmeasured (count);
	}

	/** Removes {@code start..end} inclusive; {@code start > end} is a no-op. */
	void remove (int start, int end) {
		if (start > end) return;
		if (start < 0 || end >= count) throw new IllegalArgumentException ("invalid range");
		int removed = end - start + 1;
		width.remove (start, removed);
		System.arraycopy (items, end + 1, items, start, count - end - 1);
		Arrays.fill (items, count - removed, count, null);
		count -= removed;
		planner.remove (start, removed);
		relayout ();
	}

	void remove (int index) {
		remove (index, index);
	}

	void removeAll () {
		width.clear ();
		items = new String [0];
		count = 0;
		planner.selection ().clear ();
		planner.setLogicalCount (0);
		setTopIndex (0);
	}

	/** First index {@code >= start} holding an equal string, or -1; unmaterialized slots never match. */
	int indexOf (String string, int start) {
		if (string == null) throw new IllegalArgumentException ("null item");
		if (start < 0 || start >= count) return -1;
		for (int i = start; i < count; i++) {
			if (string.equals (items [i])) return i;
		}
		return -1;
	}

	int indexOf (String string) {
		return indexOf (string, 0);
	}

	void select (int index) {
		if (inRange (index)) planner.selection ().setSelected (index, true);
	}

	void deselect (int index) {
		if (inRange (index)) planner.selection ().setSelected (index, false);
	}

	/** Selects {@code start..end} inclusive, clamped to the list. */
	void select (int start, int end) {
		int low = Math.max (0, start);
		int high = Math.min (end, count - 1);
		if (low <= high) planner.selection ().selectRange (low, high + 1);
	}

	/** Deselects {@code start..end} inclusive, clamped to the list. */
	void deselect (int start, int end) {
		int low = Math.max (0, start);
		int high = Math.min (end, count - 1);
		if (low <= high) planner.selection ().deselectRange (low, high + 1);
	}

	void selectAll () {
		planner.selection ().selectAll ();
	}

	void deselectAll () {
		planner.selection ().clear ();
	}

	boolean isSelected (int index) {
		return inRange (index) && planner.selection ().isSelected (index);
	}

	int getSelectionCount () {
		return planner.selection ().selectedCount ();
	}

	int [] getSelectionIndices () {
		return planner.selection ().toArray ();
	}

	int getTopIndex () {
		return planner.firstVisible ();
	}

	void setTopIndex (int index) {
		planner.setUniformGeometry (count, rowHeight, viewportHeight, index);
	}

	/** Visible rows are {@code ceil (viewportHeight / rowHeight)}, at least one when the list is not empty. */
	void setGeometry (int rowHeight, int viewportHeight) {
		if (rowHeight <= 0) throw new IllegalArgumentException ("non-positive row height");
		if (viewportHeight < 0) throw new IllegalArgumentException ("negative viewport height");
		this.rowHeight = rowHeight;
		this.viewportHeight = viewportHeight;
		relayout ();
	}

	int getVisibleCount () {
		return planner.visibleCount ();
	}

	/** Width of the widest item, 0 for an empty list; lazy mode measures every pending slot first. */
	int getWidestWidth () {
		if (lazy) width.measurePending (this::measureText);
		return width.maxWidth ();
	}

	/** Width of the widest measured item; never measures. */
	int getMeasuredWidestWidth () {
		return width.maxMeasured ();
	}

	/** Number of slots whose width is not measured yet. */
	int getPendingWidthCount () {
		return width.pendingCount ();
	}

	/** Lazy mode: measures one slot (a painted row) if pending; returns its width. */
	int measureItem (int index) {
		checkIndex (index);
		return width.measure (index, measureText (index));
	}

	/** Lazy mode: every width is measured again on demand (for example after a font change). */
	void invalidateWidths () {
		if (!lazy) throw new IllegalStateException ("not lazy");
		width.setAllUnmeasured (count);
	}

	private String measureText (int index) {
		String string = items [index];
		return string == null ? "" : string;
	}

	private void relayout () {
		setTopIndex (planner.firstVisible ());
	}

	private boolean inRange (int index) {
		return index >= 0 && index < count;
	}

	private void checkIndex (int index) {
		if (!inRange (index)) throw new IllegalArgumentException ("invalid index");
	}

	private void ensureCapacity (int needed) {
		if (needed <= items.length) return;
		items = Arrays.copyOf (items, Math.max (needed, growth.applyAsInt (items.length, needed)));
	}
}
