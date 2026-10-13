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
 */
final class VirtualListModel {
	private final VirtualViewportPlanner planner = new VirtualViewportPlanner ();
	private final VirtualListWidth width;
	private String [] items = new String [0];
	private int count;
	private int rowHeight = 1;
	private int viewportHeight = 1;

	VirtualListModel (ToIntFunction<String> measure) {
		width = new VirtualListWidth (measure);
	}

	int getItemCount () {
		return count;
	}

	String getItem (int index) {
		checkIndex (index);
		return items [index];
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
		width.insert (index, string);
		System.arraycopy (items, index, items, index + 1, count - index);
		items [index] = string;
		count++;
		planner.insert (index, 1);
		relayout ();
	}

	void setItem (int index, String string) {
		checkIndex (index);
		if (string == null) throw new IllegalArgumentException ("null item");
		width.set (index, string);
		items [index] = string;
	}

	void setItems (String [] newItems) {
		if (newItems == null) throw new IllegalArgumentException ("null items");
		for (String string : newItems) {
			if (string == null) throw new IllegalArgumentException ("null item");
		}
		width.setAll (newItems);
		items = Arrays.copyOf (newItems, newItems.length);
		count = items.length;
		planner.selection ().clear ();
		planner.setLogicalCount (count);
		setTopIndex (0);
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

	/** First index {@code >= start} holding an equal string, or -1. */
	int indexOf (String string, int start) {
		if (string == null) throw new IllegalArgumentException ("null item");
		if (start < 0 || start >= count) return -1;
		for (int i = start; i < count; i++) {
			if (items [i].equals (string)) return i;
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

	/** Width of the widest item, 0 for an empty list. */
	int getWidestWidth () {
		return width.maxWidth ();
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
		items = Arrays.copyOf (items, Math.max (needed, items.length + (items.length >> 1) + 8));
	}
}
