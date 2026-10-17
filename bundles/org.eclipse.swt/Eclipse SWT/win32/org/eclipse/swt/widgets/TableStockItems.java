/*******************************************************************************
 * Copyright (c) 2000, 2026 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation (Table.java, R4_38)
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.function.Consumer;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.win32.*;

/**
 * The item store of a VIRTUAL Table in the original mode: the R4_38 store, transliterated from Table.java of tag
 * R4_38 (<code>_checkGrow</code> line 168, <code>_checkShrink</code> 217, <code>_getItem</code> 274,
 * <code>_getItems</code> 313, <code>_initItems</code> 329, <code>_insertItem</code> 340, <code>_removeItem</code> 357,
 * <code>_removeItems</code> 377, <code>_setItemCount</code> 397, <code>indexOf</code> 2948, <code>releaseChildren</code>
 * 3020, <code>updateTextDirection</code> 5632). Sparse <code>keys</code>/<code>items</code> while at most half of the
 * rows have items; <code>_checkGrow</code> compacts them into a dense array.
 * <p>
 * The original mode is stock behaviour (docs/m3/TABLE_MEDIATED_VIEWPORT.md, rule 12 of the viewport agent brief),
 * including the R4_38 <code>_removeItems</code> defect (line 392, docs/m3/table-viewport/UPSTREAM_DEFECTS.tsv): the
 * default mode keeps the fork's <code>VirtualItemStorage</code>, which does not have it. In the original mode the
 * Table's <code>virtualItems</code> stays empty and <code>virtualViewport</code> is null; the Table delegates its
 * item-store methods here, and a TableItem keeps its cached, checked and grayed state in its own fields, as in R4_38.
 * </p>
 */
final class TableStockItems {
	/* the store of the original mode; the viewport (default) mode keeps VirtualItemStorage */
	static final boolean ENABLED = !TableViewport.ENABLED;

	final Table table;
	TableItem [] items = new TableItem [4];
	int [] keys = new int [4];
	int keyCount;

	private TableStockItems (Table table) {
		this.table = table;
	}

	/** The store of a VIRTUAL Table in the original mode (R4_38 <code>_initItems</code>), otherwise null. */
	static TableStockItems of (Table table) {
		if (!ENABLED || (table.style & SWT.VIRTUAL) == 0) return null;
		table.virtualViewport = null;
		return new TableStockItems (table);
	}

	/* R4_38 _checkGrow */
	boolean checkGrow (int count) {
		if (keys == null) {
			if (count == items.length) {
				/*
				* Grow the array faster when redraw is off or the
				* table is not visible.  When the table is painted,
				* the items array is resized to be smaller to reduce
				* memory usage.
				*/
				boolean small = table.getDrawing () && OS.IsWindowVisible (table.handle);
				int length = small ? items.length + 4 : Math.max (4, items.length * 3 / 2);
				TableItem [] newItems = new TableItem [length];
				System.arraycopy (items, 0, newItems, 0, items.length);
				items = newItems;
			}
		} else {
			if (!table.ignoreShrink && keyCount > count / 2) {
				boolean small = table.getDrawing () && OS.IsWindowVisible (table.handle);
				int length = small ? count + 4 : Math.max (4, count * 3 / 2);
				TableItem [] newItems = new TableItem [length];
				for (int i=0; i<keyCount; i++) {
					newItems [keys [i]] = items [i];
				}
				items = newItems;
				keys = null;
				keyCount = 0;
				return true;
			} else {
				if (keyCount == keys.length) {
					boolean small = table.getDrawing () && OS.IsWindowVisible (table.handle);
					int length = small ? keys.length + 4 : Math.max (4, keys.length * 3 / 2);
					int [] newKeys = new int [length];
					System.arraycopy (keys, 0, newKeys, 0, keys.length);
					keys = newKeys;
					TableItem [] newItems = new TableItem [length];
					System.arraycopy (items, 0, newItems, 0, items.length);
					items = newItems;
				}
			}
		}
		return false;
	}

	/* R4_38 _checkShrink */
	void checkShrink () {
		if (keys == null) {
			if (!table.ignoreShrink) {
				/* Resize the item array to match the item count */
				int count = (int)OS.SendMessage (table.handle, OS.LVM_GETITEMCOUNT, 0, 0);

				/*
				* Bug in Windows. Call to OS.LVM_GETITEMCOUNT unexpectedly returns zero,
				* leading to a possible "ArrayIndexOutOfBoundsException: 4" in SWT table.
				* So, double check for any existing living items in the table and fixing
				* the count value. Refer bug 292199.
				*/
				if (count == 0 && items.length > 4) {
					while (count<items.length && items[count] != null && !items[count].isDisposed()) {
						count++;
					}
				}

				if (items.length > 4 && items.length - count > 3) {
					int length = Math.max (4, (count + 3) / 4 * 4);
					TableItem [] newItems = new TableItem [length];
					System.arraycopy (items, 0, newItems, 0, count);
					items = newItems;
				}
			}
		} else {
			if (!table.ignoreShrink) {
				if (keys.length > 4 && keys.length - keyCount > 3) {
					int length = Math.max (4, (keyCount + 3) / 4 * 4);
					int [] newKeys = new int [length];
					System.arraycopy (keys, 0, newKeys, 0, keyCount);
					keys = newKeys;
					TableItem [] newItems = new TableItem [length];
					System.arraycopy (items, 0, newItems, 0, keyCount);
					items = newItems;
				}
			}
		}
	}

	/* R4_38 _getItem (int, boolean, int) */
	TableItem get (int index, boolean create, int count) {
		if (keys == null) {
			if (index >= items.length) return null;
			if ((table.style & SWT.VIRTUAL) == 0 || !create) return items [index];
			if (items [index] != null) return items [index];
			return items [index] = new TableItem (table, SWT.NONE, -1, false);
		} else {
			if ((table.style & SWT.VIRTUAL) == 0 || !create) {
				if (keyCount == 0) return null;
				if (index > keys [keyCount - 1]) return null;
			}
			int keyIndex = table.binarySearch (keys, 0, keyCount, index);
			if ((table.style & SWT.VIRTUAL) == 0 || !create) {
				return keyIndex < 0 ? null : items [keyIndex];
			}
			if (keyIndex < 0) {
				if (count == -1) {
					count = (int)OS.SendMessage (table.handle, OS.LVM_GETITEMCOUNT, 0, 0);
				}
				if (checkGrow (count)) {
					if (items [index] != null) return items [index];
					return items [index] = new TableItem (table, SWT.NONE, -1, false);
				}
				keyIndex = -keyIndex - 1;
				if (keyIndex < keyCount) {
					System.arraycopy(keys, keyIndex, keys, keyIndex + 1, keyCount - keyIndex);
					System.arraycopy(items, keyIndex, items, keyIndex + 1, keyCount - keyIndex);
				}
				keyCount++;
				keys [keyIndex] = index;
			} else {
				if (items [keyIndex] != null) return items [keyIndex];
			}
			return items [keyIndex] = new TableItem (table, SWT.NONE, -1, false);
		}
	}

	/* R4_38 _getItems */
	void getItems (TableItem [] result, int count) {
		if (keys == null) {
			System.arraycopy (items, 0, result, 0, count);
		} else {
			/* NOTE: Null items will be in the array when keyCount != count */
			for (int i=0; i<keyCount; i++) {
				if (keys [i] >= count) break;
				result [keys [i]] = items [keys [i]];
			}
		}
	}

	/* R4_38 _insertItem; NOTE: The array has already been grown to have space for the new item */
	void insert (int index, TableItem item, int count) {
		if (keys == null) {
			System.arraycopy (items, index, items, index + 1, count - index);
			items [index] = item;
		} else {
			int keyIndex = table.binarySearch (keys, 0, keyCount, index);
			if (keyIndex < 0) keyIndex = -keyIndex - 1;
			System.arraycopy(keys, keyIndex, keys, keyIndex + 1, keyCount - keyIndex);
			keys [keyIndex] = index;
			System.arraycopy(items, keyIndex, items, keyIndex + 1, keyCount - keyIndex);
			items [keyIndex] = item;
			keyCount++;
			for (int i=keyIndex + 1; i<keyCount; i++) keys[i]++;
		}
	}

	/* R4_38 _removeItem */
	void remove (int index, int count) {
		if (keys == null) {
			System.arraycopy (items, index + 1, items, index, --count - index);
			items [count] = null;
		} else {
			int keyIndex = table.binarySearch (keys, 0, keyCount, index);
			if (keyIndex < 0) {
				keyIndex = -keyIndex - 1;
			} else {
				--keyCount;
				System.arraycopy (keys, keyIndex + 1, keys, keyIndex, keyCount - keyIndex);
				keys [keyCount] = 0;
				System.arraycopy (items, keyIndex + 1, items, keyIndex, keyCount - keyIndex);
				items [keyCount] = null;
			}
			for (int i=keyIndex; i<keyCount; i++) --keys[i];
		}
	}

	/* R4_38 _removeItems; NOTE: Removes from start to index - 1 */
	void removeRange (int start, int index, int count) {
		if (keys == null) {
			System.arraycopy (items, index, items, start, count - index);
			for (int i=count-(index-start); i<count; i++) items [i] = null;
		} else {
			int end = index;
			int left = table.binarySearch (keys, 0, keyCount, start);
			if (left < 0) left = -left - 1;
			int right = table.binarySearch (keys, left, keyCount, end);
			if (right < 0) right = -right - 1;
			System.arraycopy (keys, right, keys, left, keyCount - right);
			for (int i=keyCount-(right-left); i<keyCount; i++) keys [i] = 0;
			System.arraycopy (items, right, items, left, keyCount - right);
			for (int i=keyCount-(right-left); i<keyCount; i++) items [i] = null;
			keyCount -= (right - left);
			/* R4_38 line 392, kept: the later keys shift by the removed items, not by the removed rows (UPSTREAM_DEFECTS.tsv) */
			for (int i=left; i<keyCount; i++) keys[i] -= (right - left);
		}
	}

	/* R4_38 _setItemCount */
	void setItemCount (int count, int itemCount) {
		if (keys == null) {
			int length = Math.max (4, (count + 3) / 4 * 4);
			TableItem [] newItems = new TableItem [length];
			System.arraycopy (items, 0, newItems, 0, Math.min (count, itemCount));
			items = newItems;
		} else {
			int index = Math.min (count, itemCount);
			keyCount = table.binarySearch (keys, 0, keyCount, index);
			if (keyCount < 0) keyCount = -keyCount - 1;
			int length = Math.max (4, (keyCount + 3) / 4 * 4);
			int [] newKeys = new int [length];
			System.arraycopy (keys, 0, newKeys, 0, keyCount);
			keys = newKeys;
			TableItem [] newItems = new TableItem [length];
			System.arraycopy (items, 0, newItems, 0, keyCount);
			items = newItems;
		}
	}

	/* R4_38 indexOf (TableItem), after the argument check */
	int indexOf (TableItem item) {
		if (keys == null) {
			int count = (int)OS.SendMessage (table.handle, OS.LVM_GETITEMCOUNT, 0, 0);
			int lastIndexOf = table.lastIndexOf;
			if (1 <= lastIndexOf && lastIndexOf < count - 1) {
				if (get (lastIndexOf, false, -1) == item) return lastIndexOf;
				if (get (lastIndexOf + 1, false, -1) == item) return table.lastIndexOf = lastIndexOf + 1;
				if (get (lastIndexOf - 1, false, -1) == item) return table.lastIndexOf = lastIndexOf - 1;
			}
			if (lastIndexOf < count / 2) {
				for (int i=0; i<count; i++) {
					if (get (i, false, -1) == item) return table.lastIndexOf = i;
				}
			} else {
				for (int i=count - 1; i>=0; --i) {
					if (get (i, false, -1) == item) return table.lastIndexOf = i;
				}
			}
		} else {
			for (int i=0; i<keyCount; i++) {
				if (items [i] == item) return keys [i];
			}
		}
		return -1;
	}

	/* R4_38 releaseChildren, the items: released before the store is cleared */
	void release () {
		if (keys == null) {
			int itemCount = (int)OS.SendMessage (table.handle, OS.LVM_GETITEMCOUNT, 0, 0);
			for (int i=0; i<itemCount; i++) {
				TableItem item = get (i, false, -1);
				if (item != null && !item.isDisposed ()) item.release (false);
			}
		} else {
			for (int i=0; i<keyCount; i++) {
				TableItem item = items [i];
				if (item != null && !item.isDisposed ()) item.release (false);
			}
		}
	}

	/* R4_38 updateTextDirection: every item of the array */
	void forEach (Consumer<TableItem> consumer) {
		for (TableItem item : items) {
			if (item != null) consumer.accept (item);
		}
	}
}
