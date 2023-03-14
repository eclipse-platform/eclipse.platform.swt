/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 509648
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

/**
 * Instances of this class implement a selectable user interface
 * object that displays a list of images and strings and issues
 * notification when selected.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TableItem</code>.
 * </p><p>
 * Style <code>VIRTUAL</code> is used to create a <code>Table</code> whose
 * <code>TableItem</code>s are to be populated by the client on an on-demand basis
 * instead of up-front.  This can provide significant performance improvements for
 * tables that are very large or for which <code>TableItem</code> population is
 * expensive (for example, retrieving values from an external source).
 * </p><p>
 * Here is an example of using a <code>Table</code> with style <code>VIRTUAL</code>:</p>
 * <pre><code>
 *  final Table table = new Table (parent, SWT.VIRTUAL | SWT.BORDER);
 *  table.setItemCount (1000000);
 *  table.addListener (SWT.SetData, new Listener () {
 *      public void handleEvent (Event event) {
 *          TableItem item = (TableItem) event.item;
 *          int index = table.indexOf (item);
 *          item.setText ("Item " + index);
 *          System.out.println (item.getText ());
 *      }
 *  });
 * </code></pre>
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not normally make sense to add <code>Control</code> children to
 * it, or set a layout on it, unless implementing something like a cell
 * editor.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, HIDE_SELECTION, VIRTUAL, NO_SCROLL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, SetData, MeasureItem, EraseItem, PaintItem</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles SINGLE, and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#table">Table, TableItem, TableColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Table extends Composite {
	long modelHandle, checkRenderer;
	int itemCount, columnCount, lastIndexOf, sortDirection;
	int selectionCountOnPress,selectionCountOnRelease;
	long ignoreCell;
	TableItem [] items;
	TableColumn [] columns;
	TableItem currentItem;
	TableColumn sortColumn;
	ImageList imageList, headerImageList;
	boolean firstCustomDraw;
	/** True iff computeSize has never been called on this Table */
	boolean firstCompute = true;
	int drawState, drawFlags;
	GdkRGBA background, foreground, drawForegroundRGBA;
	Color headerBackground, headerForeground;
	boolean ownerDraw, ignoreSize, pixbufSizeSet, hasChildren;
	int maxWidth = 0;
	int topIndex;
	double cachedAdjustment, currentAdjustment;
	int pixbufHeight, pixbufWidth;
	int headerHeight;
	boolean boundsChangedSinceLastDraw, headerVisible, wasScrolled;
	boolean rowActivated;

	private long headerCSSProvider;

	static final int CHECKED_COLUMN = 0;
	static final int GRAYED_COLUMN = 1;
	static final int FOREGROUND_COLUMN = 2;
	static final int BACKGROUND_COLUMN = 3;
	static final int FONT_COLUMN = 4;
	static final int FIRST_COLUMN = FONT_COLUMN + 1;
	static final int CELL_PIXBUF = 0;
	static final int CELL_TEXT = 1;
	static final int CELL_FOREGROUND = 2;
	static final int CELL_BACKGROUND = 3;
	static final int CELL_FONT = 4;
	static final int CELL_SURFACE = 5;
	static final int CELL_TYPES = CELL_SURFACE + 1;

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#SINGLE
 * @see SWT#MULTI
 * @see SWT#CHECK
 * @see SWT#FULL_SELECTION
 * @see SWT#HIDE_SELECTION
 * @see SWT#VIRTUAL
 * @see SWT#NO_SCROLL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Table (Composite parent, int style) {
	super (parent, checkStyle (style));
}

@Override
void _addListener (int eventType, Listener listener) {
	super._addListener (eventType, listener);
	if (!ownerDraw) {
		switch (eventType) {
			case SWT.MeasureItem:
			case SWT.EraseItem:
			case SWT.PaintItem:
				ownerDraw = true;
				recreateRenderers ();
				break;
		}
	}
}

TableItem _getItem (int index) {
	if ((style & SWT.VIRTUAL) == 0) return items [index];
	if (items [index] != null) return items [index];
	return items [index] = new TableItem (this, SWT.NONE, index, false);
}

static int checkStyle (int style) {
	/*
	* Feature in Windows.  Even when WS_HSCROLL or
	* WS_VSCROLL is not specified, Windows creates
	* trees and tables with scroll bars.  The fix
	* is to set H_SCROLL and V_SCROLL.
	*
	* NOTE: This code appears on all platforms so that
	* applications have consistent scroll bar behavior.
	*/
	if ((style & SWT.NO_SCROLL) == 0) {
		style |= SWT.H_SCROLL | SWT.V_SCROLL;
	}
	/* GTK is always FULL_SELECTION */
	style |= SWT.FULL_SELECTION;
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}

@Override
long cellDataProc (long tree_column, long cell, long tree_model, long iter, long data) {
	if (cell == ignoreCell) return 0;
	long path = GTK.gtk_tree_model_get_path (tree_model, iter);
	int [] index = new int [1];
	C.memmove (index, GTK.gtk_tree_path_get_indices (path), 4);
	TableItem item = _getItem (index[0]);
	GTK.gtk_tree_path_free (path);
	if (item != null) OS.g_object_set_qdata (cell, Display.SWT_OBJECT_INDEX2, item.handle);
	boolean isPixbuf = GTK.GTK_IS_CELL_RENDERER_PIXBUF (cell);
	boolean isText = GTK.GTK_IS_CELL_RENDERER_TEXT (cell);
	if (isText) {
		GTK.gtk_cell_renderer_set_fixed_size (cell, -1, -1);
	}
	if (!(isPixbuf || isText)) return 0;
	int modelIndex = -1;
	boolean customDraw = false;
	if (columnCount == 0) {
		modelIndex = Table.FIRST_COLUMN;
		customDraw = firstCustomDraw;
	} else {
		TableColumn column = (TableColumn) display.getWidget (tree_column);
		if (column != null) {
			modelIndex = column.modelIndex;
			customDraw = column.customDraw;
		}
	}
	if (modelIndex == -1) return 0;
	boolean setData = false;
	if ((style & SWT.VIRTUAL) != 0) {
		if (!item.cached) {
			lastIndexOf = index[0];
			setData = checkData (item);
		}
	}
	long [] ptr = new long [1];
	if (setData) {
		ptr [0] = 0;
		if (isPixbuf) {
			GTK.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_PIXBUF, ptr, -1);
			OS.g_object_set (cell, OS.gicon, ptr [0], 0);
			if (ptr [0] != 0) OS.g_object_unref (ptr [0]);
		} else {
			GTK.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_TEXT, ptr, -1);
			if (ptr [0] != 0) {
				OS.g_object_set (cell, OS.text, ptr [0], 0);
				OS.g_free (ptr [0]);
			}
		}
	}
	if (customDraw) {
		if (!ownerDraw) {
			ptr [0] = 0;
			GTK.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_BACKGROUND, ptr, -1);
			if (ptr [0] != 0) {
				OS.g_object_set (cell, OS.cell_background_rgba, ptr[0], 0);
				GDK.gdk_rgba_free (ptr [0]);
			}
		}
		if (!isPixbuf) {
			ptr [0] = 0;
			GTK.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_FOREGROUND, ptr, -1);
			if (ptr [0] != 0) {
				OS.g_object_set (cell, OS.foreground_rgba, ptr [0], 0);
				GDK.gdk_rgba_free (ptr [0]);
			}
			ptr [0] = 0;
			GTK.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_FONT, ptr, -1);
			if (ptr [0] != 0) {
				OS.g_object_set (cell, OS.font_desc, ptr [0], 0);
				OS.pango_font_description_free (ptr [0]);
			}
		}
	}
	if (setData) {
		ignoreCell = cell;
		setScrollWidth (tree_column, item);
		ignoreCell = 0;
	}
	return 0;
}

boolean checkData (TableItem item) {
	if (item.cached) return true;
	if ((style & SWT.VIRTUAL) != 0) {
		item.cached = true;
		Event event = new Event ();
		event.item = item;
		event.index = indexOf (item);
		int mask = OS.G_SIGNAL_MATCH_DATA | OS.G_SIGNAL_MATCH_ID;
		int signal_id = OS.g_signal_lookup (OS.row_changed, GTK.gtk_tree_model_get_type ());
		OS.g_signal_handlers_block_matched (modelHandle, mask, signal_id, 0, 0, 0, handle);
		currentItem = item;
		item.settingData = true;
		sendEvent (SWT.SetData, event);
		item.settingData = false;
		//widget could be disposed at this point
		currentItem = null;
		if (isDisposed ()) return false;
		OS.g_signal_handlers_unblock_matched (modelHandle, mask, signal_id, 0, 0, 0, handle);
		if (item.isDisposed ()) return false;
	}
	return true;
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the user changes the receiver's selection, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * If the receiver has the <code>SWT.CHECK</code> style and the check selection changes,
 * the event object detail field contains the value <code>SWT.CHECK</code>.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
 * The item field of the event object is valid for default selection, but the detail field is not used.
 * </p>
 *
 * @param listener the listener which should be notified when the user changes the receiver's selection
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}

int calculateWidth (long column, long iter) {
	GTK.gtk_tree_view_column_cell_set_cell_data(column, modelHandle, iter, false, false);

	/*
	* Bug in GTK.  The width calculated by gtk_tree_view_column_cell_get_size()
	* always grows in size regardless of the text or images in the table.
	* The fix is to determine the column width from the cell renderers.
	*/

	//This workaround is causing the problem Bug 459834 in GTK3. So reverting the workaround for GTK3
	int[] width = new int[1];
	if (GTK.GTK4) {
		GTK4.gtk_tree_view_column_cell_get_size(column, null, null, width, null);
	} else {
		GTK3.gtk_tree_view_column_cell_get_size(column, null, null, null, width, null);
	}

	long textRenderer = getTextRenderer(column);
	int[] xpad = new int[1];
	if (textRenderer != 0) GTK.gtk_cell_renderer_get_padding(textRenderer, xpad, null);

	return width[0] + xpad[0] * 2;
}

/**
 * Clears the item at the given zero-relative index in the receiver.
 * The text, icon and other attributes of the item are set to the default
 * value.  If the table was created with the <code>SWT.VIRTUAL</code> style,
 * these attributes are requested again as needed.
 *
 * @param index the index of the item to clear
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 *
 * @since 3.0
 */
public void clear (int index) {
	checkWidget ();
	if (!(0 <= index && index < itemCount)) {
		error(SWT.ERROR_INVALID_RANGE);
	}
	TableItem item = items [index];
	if (item != null) item.clear ();
}

/**
 * Removes the items from the receiver which are between the given
 * zero-relative start and end indices (inclusive).  The text, icon
 * and other attributes of the items are set to their default values.
 * If the table was created with the <code>SWT.VIRTUAL</code> style,
 * these attributes are requested again as needed.
 *
 * @param start the start index of the item to clear
 * @param end the end index of the item to clear
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if either the start or end are not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 *
 * @since 3.0
 */
public void clear (int start, int end) {
	checkWidget ();
	if (start > end) return;
	if (!(0 <= start && start <= end && end < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (start == 0 && end == itemCount - 1) {
		clearAll();
	} else {
		for (int i=start; i<=end; i++) {
			TableItem item = items [i];
			if (item != null) item.clear();
		}
	}
}

/**
 * Clears the items at the given zero-relative indices in the receiver.
 * The text, icon and other attributes of the items are set to their default
 * values.  If the table was created with the <code>SWT.VIRTUAL</code> style,
 * these attributes are requested again as needed.
 *
 * @param indices the array of indices of the items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 *    <li>ERROR_NULL_ARGUMENT - if the indices array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 *
 * @since 3.0
 */
public void clear (int [] indices) {
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (indices.length == 0) return;
	for (int i=0; i<indices.length; i++) {
		if (!(0 <= indices [i] && indices [i] < itemCount)) {
			error (SWT.ERROR_INVALID_RANGE);
		}
	}
	for (int i=0; i<indices.length; i++) {
		TableItem item = items [indices [i]];
		if (item != null) item.clear();
	}
}

/**
 * Clears all the items in the receiver. The text, icon and other
 * attributes of the items are set to their default values. If the
 * table was created with the <code>SWT.VIRTUAL</code> style, these
 * attributes are requested again as needed.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 *
 * @since 3.0
 */
public void clearAll () {
	checkWidget ();
	for (int i=0; i<itemCount; i++) {
		TableItem item = items [i];
		if (item != null) item.clear();
	}
}

@Override
Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	/*
	 * Set all the TableColumn buttons visible otherwise
	 * gtk_widget_get_preferred_size() will not take their size
	 * into account.
	 */
	if (firstCompute) {
		for (int x = 0; x < columns.length; x++) {
			TableColumn column = columns[x];
			if (column != null) GTK.gtk_widget_set_visible(column.buttonHandle, true);
		}
		firstCompute = false;
	}
	Point size = computeNativeSize (handle, wHint, hHint, changed);
	/*
	 * In GTK 3, computeNativeSize(..) sometimes just returns the header
	 * height as height. In that case, calculate the table height based on
	 * the number of items in the table.
	 */
	if (hHint == SWT.DEFAULT && (size.y == getHeaderHeight()) || size.y == 0) {
		size.y = getItemCount() * getItemHeight() + getHeaderHeight();
	}
	/*
	 * Bug 465056: single column Tables have a very small initial width.
	 * Fix: set the width of the Table to the width of the widest
	 * TableItem.
	 */
	if (wHint == SWT.DEFAULT && size.x == 0 && columnCount == 0) {
		size.x = maxWidth;
	}
	Rectangle trim = computeTrimInPixels (0, 0, size.x, size.y);
	size.x = trim.width;
	/*
	 * Feature in GTK: sometimes GtkScrolledWindow's with no scrollbars
	 * won't automatically adjust their size. This happens when a Table
	 * has a header, and the initial computed height was the height of
	 * the of the header.
	 *
	 *  The fix is to increment the height by 1 in order to force a size
	 *  update for the parent GtkScrollWindow, otherwise the headers
	 *  will not be shown. This only happens once, see bug 546490.
	 */
	if (size.y == this.headerHeight && this.headerVisible && (style & SWT.NO_SCROLL) != 0) {
		trim.height = trim.height + 1;
	}
	size.y = trim.height;
	return size;
}

void copyModel(long oldModel, int oldStart, long newModel, int newStart, int modelLength) {
	long value = OS.g_malloc (OS.GValue_sizeof ());
	// GValue needs to be initialized with G_VALUE_INIT, which is zeroes
	C.memset (value, 0, OS.GValue_sizeof ());

	for (int i=0; i<itemCount; i++) {
		long newIterator = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
		if (newIterator == 0) error (SWT.ERROR_NO_HANDLES);
		GTK.gtk_list_store_append (newModel, newIterator);

		TableItem item = items [i];
		if (item == null) {
			/*
			 * In `SWT.VIRTUAL` mode, `items[]` is not populated, and
			 * iterators are not remembered. Instead, SWT will use
			 * `gtk_tree_model_iter_nth_child()`.
			 */
			OS.g_free (newIterator);
			continue;
		}

		long oldIterator = item.handle;

		// Copy header fields
		for (int iColumn = 0; iColumn < FIRST_COLUMN; iColumn++) {
			GTK.gtk_tree_model_get_value (oldModel, oldIterator, iColumn, value);
			GTK.gtk_list_store_set_value (newModel, newIterator, iColumn, value);
			OS.g_value_unset (value);
		}

		// Copy requested columns
		for (int iOffset = 0; iOffset < modelLength - FIRST_COLUMN; iOffset++) {
			GTK.gtk_tree_model_get_value (oldModel, oldIterator, oldStart + iOffset, value);
			GTK.gtk_list_store_set_value (newModel, newIterator, newStart + iOffset, value);
			OS.g_value_unset (value);
		}

		GTK.gtk_list_store_remove (oldModel, oldIterator);
		OS.g_free (oldIterator);
		item.handle = newIterator;
	}

	OS.g_free (value);
}

void createColumn (TableColumn column, int index) {
	int modelIndex = FIRST_COLUMN;
	if (columnCount != 0) {
		int modelLength = GTK.gtk_tree_model_get_n_columns (modelHandle);
		boolean [] usedColumns = new boolean [modelLength];
		for (int i=0; i<columnCount; i++) {
			int columnIndex = columns [i].modelIndex;
			for (int j = 0; j < CELL_TYPES; j++) {
				usedColumns [columnIndex + j] = true;
			}
		}
		while (modelIndex < modelLength) {
			if (!usedColumns [modelIndex]) break;
			modelIndex++;
		}
		if (modelIndex == modelLength) {
			long oldModel = modelHandle;
			long [] types = getColumnTypes (columnCount + 4); // grow by 4 rows at a time
			long newModel = GTK.gtk_list_store_newv (types.length, types);
			if (newModel == 0) error (SWT.ERROR_NO_HANDLES);
			/*
			 * In VIRTUAL Table, GTK may react to `gtk_list_store_remove()` by
			 * calling `cellDataProc()`, and SWT will be confused because
			 * `items[]` no longer match the GTK model, which has some rows
			 * deleted by `gtk_list_store_remove()`. The fix is to disconnect
			 * model during rebuilding. The new model will replace the old one
			 * anyway, so it doesn't matter if old is removed a bit earlier.
			 */
			GTK.gtk_tree_view_set_model (handle, 0);
			copyModel (oldModel, FIRST_COLUMN, newModel, FIRST_COLUMN, modelLength);
			GTK.gtk_tree_view_set_model (handle, newModel);
			setModel (newModel);
		}
	}
	long columnHandle = GTK.gtk_tree_view_column_new ();
	if (columnHandle == 0) error (SWT.ERROR_NO_HANDLES);
	if (index == 0 && columnCount > 0) {
		TableColumn checkColumn = columns [0];
		createRenderers (checkColumn.handle, checkColumn.modelIndex, false, checkColumn.style);
	}
	createRenderers (columnHandle, modelIndex, index == 0, column == null ? 0 : column.style);
	if ((style & SWT.VIRTUAL) == 0 && columnCount == 0) {
		GTK.gtk_tree_view_column_set_sizing (columnHandle, GTK.GTK_TREE_VIEW_COLUMN_GROW_ONLY);
	} else {
		GTK.gtk_tree_view_column_set_sizing (columnHandle, GTK.GTK_TREE_VIEW_COLUMN_FIXED);
	}
	GTK.gtk_tree_view_column_set_resizable (columnHandle, true);
	GTK.gtk_tree_view_column_set_clickable (columnHandle, true);
	GTK.gtk_tree_view_column_set_min_width (columnHandle, 0);
	GTK.gtk_tree_view_insert_column (handle, columnHandle, index);
	/*
	* Bug in GTK3.  The column header has the wrong CSS styling if it is hidden
	* when inserting to the tree widget.  The fix is to hide the column only
	* after it is inserted.
	*/
	if (columnCount != 0) GTK.gtk_tree_view_column_set_visible (columnHandle, false);
	if (column != null) {
		column.handle = columnHandle;
		column.modelIndex = modelIndex;
	}
	if (!searchEnabled ()) {
		GTK.gtk_tree_view_set_search_column (handle, -1);
	} else {
		/* Set the search column whenever the model changes */
		int firstColumn = columnCount == 0 ? FIRST_COLUMN : columns [0].modelIndex;
		GTK.gtk_tree_view_set_search_column (handle, firstColumn + CELL_TEXT);
	}
}

@Override
void createHandle (int index) {
	state |= HANDLE;
	fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
	if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
	if (GTK.GTK4) {
		scrolledHandle = GTK4.gtk_scrolled_window_new();
	} else {
		GTK3.gtk_widget_set_has_window(fixedHandle, true);
		scrolledHandle = GTK3.gtk_scrolled_window_new (0, 0);
	}
	if (scrolledHandle == 0) error (SWT.ERROR_NO_HANDLES);
	long [] types = getColumnTypes (1);
	modelHandle = GTK.gtk_list_store_newv (types.length, types);
	if (modelHandle == 0) error (SWT.ERROR_NO_HANDLES);
	handle = GTK.gtk_tree_view_new_with_model (modelHandle);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & SWT.CHECK) != 0) {
		checkRenderer = GTK.gtk_cell_renderer_toggle_new ();
		if (checkRenderer == 0) error (SWT.ERROR_NO_HANDLES);
		OS.g_object_ref (checkRenderer);
	}
	createColumn (null, 0);
	if (GTK.GTK4) {
		OS.swt_fixed_add(fixedHandle, scrolledHandle);
		GTK4.gtk_scrolled_window_set_child(scrolledHandle, handle);
	} else {
		GTK3.gtk_container_add(fixedHandle, scrolledHandle);
		GTK3.gtk_container_add(scrolledHandle, handle);
	}

	int mode = (style & SWT.MULTI) != 0 ? GTK.GTK_SELECTION_MULTIPLE : GTK.GTK_SELECTION_BROWSE;
	long selectionHandle = GTK.gtk_tree_view_get_selection (handle);
	GTK.gtk_tree_selection_set_mode (selectionHandle, mode);
	GTK.gtk_tree_view_set_headers_visible (handle, false);
	int hsp = (style & SWT.H_SCROLL) != 0 ? GTK.GTK_POLICY_AUTOMATIC : GTK.GTK_POLICY_NEVER;
	int vsp = (style & SWT.V_SCROLL) != 0 ? GTK.GTK_POLICY_AUTOMATIC : GTK.GTK_POLICY_NEVER;
	GTK.gtk_scrolled_window_set_policy (scrolledHandle, hsp, vsp);
	if ((style & SWT.BORDER) != 0) {
		if (GTK.GTK4) {
			GTK4.gtk_scrolled_window_set_has_frame(scrolledHandle, true);
		} else {
			GTK3.gtk_scrolled_window_set_shadow_type (scrolledHandle, GTK.GTK_SHADOW_ETCHED_IN);
		}
	}
	/*
	 * We enable fixed-height-mode for performance reasons (see bug 490203).
	 */
	if ((style & SWT.VIRTUAL) != 0) {
		OS.g_object_set (handle, OS.fixed_height_mode, true, 0);
	}
	if (!searchEnabled ()) {
		GTK.gtk_tree_view_set_search_column (handle, -1);
	}
}

void createItem (TableColumn column, int index) {
	if (!(0 <= index && index <= columnCount)) error (SWT.ERROR_INVALID_RANGE);
	if (columnCount == 0) {
		column.handle = GTK.gtk_tree_view_get_column (handle, 0);
		GTK.gtk_tree_view_column_set_sizing (column.handle, GTK.GTK_TREE_VIEW_COLUMN_FIXED);
		GTK.gtk_tree_view_column_set_visible (column.handle, false);
		column.modelIndex = FIRST_COLUMN;
		createRenderers (column.handle, column.modelIndex, true, column.style);
		column.customDraw = firstCustomDraw;
		firstCustomDraw = false;
	} else {
		createColumn (column, index);
	}

	long boxHandle = gtk_box_new(GTK.GTK_ORIENTATION_HORIZONTAL, false, 3);
	if (boxHandle == 0) error(SWT.ERROR_NO_HANDLES);
	GTK.gtk_tree_view_column_set_widget (column.handle, boxHandle);

	long labelHandle = GTK.gtk_label_new_with_mnemonic(null);
	if (labelHandle == 0) error(SWT.ERROR_NO_HANDLES);
	long imageHandle = GTK.gtk_image_new();
	if (imageHandle == 0) error(SWT.ERROR_NO_HANDLES);

	if (GTK.GTK4) {
		GTK4.gtk_box_append(boxHandle, imageHandle);
		GTK4.gtk_box_append(boxHandle, labelHandle);

		GTK.gtk_widget_hide(imageHandle);
	} else {
		GTK3.gtk_container_add (boxHandle, imageHandle);
		GTK3.gtk_container_add (boxHandle, labelHandle);

		GTK.gtk_widget_show (boxHandle);
		GTK.gtk_widget_show (labelHandle);
	}

	column.labelHandle = labelHandle;
	column.imageHandle = imageHandle;
	column.buttonHandle = GTK.gtk_tree_view_column_get_button(column.handle);
	GTK.gtk_widget_set_focus_on_click(column.buttonHandle, false);

	if (columnCount == columns.length) {
		TableColumn [] newColumns = new TableColumn [columns.length + 4];
		System.arraycopy (columns, 0, newColumns, 0, columns.length);
		columns = newColumns;
	}
	System.arraycopy (columns, index, columns, index + 1, columnCount++ - index);
	columns [index] = column;
	if ((state & FONT) != 0) {
		long fontDesc = getFontDescription ();
		column.setFontDescription (fontDesc);
		OS.pango_font_description_free (fontDesc);
	}
	if (columnCount >= 1) {
		for (int i=0; i<itemCount; i++) {
			TableItem item = items [i];
			if (item != null) {
				// Bug 545139: For consistency, do not wipe out content of first TableColumn created after TableItem
				boolean doNotModify;
				Font [] cellFont = item.cellFont;
				doNotModify = columnCount == 1 && cellFont != null && cellFont.length == columnCount;
				if (cellFont != null && !doNotModify) {
					Font [] temp = new Font [columnCount];
					System.arraycopy (cellFont, 0, temp, 0, index);
					System.arraycopy (cellFont, index, temp, index+1, columnCount-index-1);
					item.cellFont = temp;
				}
				String [] strings = item.strings;
				doNotModify = columnCount == 1 && strings != null && strings.length == columnCount;
				if (strings != null && !doNotModify) {
					String [] temp = new String [columnCount];
					System.arraycopy (strings, 0, temp, 0, index);
					System.arraycopy (strings, index, temp, index+1, columnCount-index-1);
					temp [index] = "";
					item.strings = temp;
				}
			}
		}
	}

	updateHeaderCSS();

	/*
	 * Feature in GTK. The tree view does not resize immediately if a table
	 * column is created when the table is not visible. If the width of the
	 * new column is queried, GTK returns an incorrect value. The fix is to
	 * ensure that the columns are resized before any queries.
	 */
	if(!isVisible ()) {
		forceResize();
	}
}

void createItem (TableItem item, int index) {
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	if (itemCount == items.length) {
		int length = drawCount <= 0 ? items.length + 4 : Math.max (4, items.length * 3 / 2);
		TableItem [] newItems = new TableItem [length];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	item.handle = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	if (item.handle == 0) error (SWT.ERROR_NO_HANDLES);
	/*
	* Feature in GTK.  It is much faster to append to a list store
	* than to insert at the end using gtk_list_store_insert().
	*/
	if (index == itemCount) {
		GTK.gtk_list_store_append (modelHandle, item.handle);
	} else {
		GTK.gtk_list_store_insert (modelHandle, item.handle, index);
	}
	System.arraycopy (items, index, items, index + 1, itemCount++ - index);
	items [index] = item;
}

void createRenderers (long columnHandle, int modelIndex, boolean check, int columnStyle) {
	GTK.gtk_tree_view_column_clear (columnHandle);
	if ((style & SWT.CHECK) != 0 && check) {
		GTK.gtk_tree_view_column_pack_start (columnHandle, checkRenderer, false);
		GTK.gtk_tree_view_column_add_attribute (columnHandle, checkRenderer, OS.active, CHECKED_COLUMN);
		GTK.gtk_tree_view_column_add_attribute (columnHandle, checkRenderer, OS.inconsistent, GRAYED_COLUMN);
		if (ownerDraw) {
			GTK.gtk_tree_view_column_set_cell_data_func (columnHandle, checkRenderer, display.cellDataProc, handle, 0);
			OS.g_object_set_qdata (checkRenderer, Display.SWT_OBJECT_INDEX1, columnHandle);
		} else {
			GTK.gtk_tree_view_column_add_attribute (columnHandle, checkRenderer, OS.cell_background_rgba, BACKGROUND_COLUMN);
		}
	}
	long pixbufRenderer = ownerDraw ? OS.g_object_new (display.gtk_cell_renderer_pixbuf_get_type (), 0) : GTK.gtk_cell_renderer_pixbuf_new ();
	if (pixbufRenderer == 0) {
		error (SWT.ERROR_NO_HANDLES);
	} else {
		// set default size this size is used for calculating the icon and text positions in a table
		if (!ownerDraw) {
			// Set render size to 0x0 until we actually add images, fix for
			// Bug 457196 (this applies to Tables as well).
			GTK.gtk_cell_renderer_set_fixed_size(pixbufRenderer, 0, 0);
		}
	}
	long textRenderer = ownerDraw ? OS.g_object_new (display.gtk_cell_renderer_text_get_type (), 0) : GTK.gtk_cell_renderer_text_new ();
	if (textRenderer == 0) error (SWT.ERROR_NO_HANDLES);

	if (ownerDraw) {
		OS.g_object_set_qdata (pixbufRenderer, Display.SWT_OBJECT_INDEX1, columnHandle);
		OS.g_object_set_qdata (textRenderer, Display.SWT_OBJECT_INDEX1, columnHandle);
	}

	/*
	* Feature in GTK.  When a tree view column contains only one activatable
	* cell renderer such as a toggle renderer, mouse clicks anywhere in a cell
	* activate that renderer. The workaround is to set a second cell renderer
	* to be activatable.
	*/
	if ((style & SWT.CHECK) != 0 && check) {
		OS.g_object_set (pixbufRenderer, OS.mode, GTK.GTK_CELL_RENDERER_MODE_ACTIVATABLE, 0);
	}

	/* Set alignment */
	if ((columnStyle & SWT.RIGHT) != 0) {
		OS.g_object_set(textRenderer, OS.xalign, 1f, 0);
		GTK.gtk_tree_view_column_pack_end (columnHandle, textRenderer, true);
		GTK.gtk_tree_view_column_pack_end (columnHandle, pixbufRenderer, false);
		GTK.gtk_tree_view_column_set_alignment (columnHandle, 1f);
	} else if ((columnStyle & SWT.CENTER) != 0) {
		OS.g_object_set(textRenderer, OS.xalign, 0.5f, 0);
		GTK.gtk_tree_view_column_pack_start (columnHandle, pixbufRenderer, false);
		GTK.gtk_tree_view_column_pack_end (columnHandle, textRenderer, true);
		GTK.gtk_tree_view_column_set_alignment (columnHandle, 0.5f);
	} else {
		GTK.gtk_tree_view_column_pack_start (columnHandle, pixbufRenderer, false);
		GTK.gtk_tree_view_column_pack_start (columnHandle, textRenderer, true);
		GTK.gtk_tree_view_column_set_alignment (columnHandle, 0f);
	}

	/* Add attributes */
	/*
	 * Formerly OS.gicon was set if on GTK3, but this is being removed do to spacing issues in Tables/Trees with
	 * no images. Fix for Bug 457196. NOTE: this change has been ported to Tables since Tables/Trees both
	 * use the same underlying GTK structure.
	 */
	GTK.gtk_tree_view_column_add_attribute (columnHandle, pixbufRenderer, OS.pixbuf, modelIndex + CELL_PIXBUF);
	if (!ownerDraw) {
		GTK.gtk_tree_view_column_add_attribute (columnHandle, pixbufRenderer, OS.cell_background_rgba, BACKGROUND_COLUMN);
		GTK.gtk_tree_view_column_add_attribute (columnHandle, textRenderer, OS.cell_background_rgba, BACKGROUND_COLUMN);
	}
	GTK.gtk_tree_view_column_add_attribute (columnHandle, textRenderer, OS.text, modelIndex + CELL_TEXT);
	GTK.gtk_tree_view_column_add_attribute (columnHandle, textRenderer, OS.foreground_rgba, FOREGROUND_COLUMN);
	GTK.gtk_tree_view_column_add_attribute (columnHandle, textRenderer, OS.font_desc, FONT_COLUMN);

	boolean customDraw = firstCustomDraw;
	if (columnCount != 0) {
		for (int i=0; i<columnCount; i++) {
			if (columns [i].handle == columnHandle) {
				customDraw = columns [i].customDraw;
				break;
			}
		}
	}
	if ((style & SWT.VIRTUAL) != 0 || customDraw || ownerDraw) {
		GTK.gtk_tree_view_column_set_cell_data_func (columnHandle, textRenderer, display.cellDataProc, handle, 0);
		GTK.gtk_tree_view_column_set_cell_data_func (columnHandle, pixbufRenderer, display.cellDataProc, handle, 0);
	}
}

@Override
void createWidget (int index) {
	super.createWidget (index);
	items = new TableItem [4];
	columns = new TableColumn [4];
	itemCount = columnCount = 0;
	// In GTK 3 font description is inherited from parent widget which is not how SWT has always worked,
	// reset to default font to get the usual behavior
	setFontDescription(defaultFont().handle);
}

@Override
int applyThemeBackground () {
	return -1; /* No Change */
}

@Override
GdkRGBA defaultBackground () {
	return display.getSystemColor(SWT.COLOR_LIST_BACKGROUND).handle;
}

@Override
void deregister () {
	super.deregister ();
	display.removeWidget (GTK.gtk_tree_view_get_selection (handle));
	if (checkRenderer != 0) display.removeWidget (checkRenderer);
	display.removeWidget (modelHandle);
}

/**
 * Deselects the item at the given zero-relative index in the receiver.
 * If the item at the index was already deselected, it remains
 * deselected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to deselect
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int index) {
	checkWidget();
	if (index < 0 || index >= itemCount) return;
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_tree_selection_unselect_iter (selection, _getItem (index).handle);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if (fixColumn) hideFirstColumn ();
}

/**
 * Deselects the items at the given zero-relative indices in the receiver.
 * If the item at the given zero-relative index in the receiver
 * is selected, it is deselected.  If the item at the index
 * was not selected, it remains deselected.  The range of the
 * indices is inclusive. Indices that are out of range are ignored.
 *
 * @param start the start index of the items to deselect
 * @param end the end index of the items to deselect
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int start, int end) {
	checkWidget();
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	for (int index=start; index<=end; index++) {
		if (index < 0 || index >= itemCount) continue;
		GTK.gtk_tree_selection_unselect_iter (selection, _getItem (index).handle);
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if (fixColumn) hideFirstColumn ();
}

/**
 * Deselects the items at the given zero-relative indices in the receiver.
 * If the item at the given zero-relative index in the receiver
 * is selected, it is deselected.  If the item at the index
 * was not selected, it remains deselected. Indices that are out
 * of range and duplicate indices are ignored.
 *
 * @param indices the array of indices for the items to deselect
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the set of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int [] indices) {
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	for (int i=0; i<indices.length; i++) {
		int index = indices[i];
		if (index < 0 || index >= itemCount) continue;
		GTK.gtk_tree_selection_unselect_iter (selection, _getItem (index).handle);
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if (fixColumn) hideFirstColumn ();
}

/**
 * Deselects all selected items in the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselectAll () {
	checkWidget();
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_tree_selection_unselect_all (selection);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if (fixColumn) hideFirstColumn ();
}

void destroyItem (TableColumn column) {
	int index = 0;
	while (index < columnCount) {
		if (columns [index] == column) break;
		index++;
	}
	if (index == columnCount) return;
	long columnHandle = column.handle;
	if (columnCount == 1) {
		firstCustomDraw = column.customDraw;
	}
	System.arraycopy (columns, index + 1, columns, index, --columnCount - index);
	columns [columnCount] = null;
	GTK.gtk_tree_view_remove_column (handle, columnHandle);
	if (columnCount == 0) {
		long oldModel = modelHandle;
		long [] types = getColumnTypes (1);
		long newModel = GTK.gtk_list_store_newv (types.length, types);
		if (newModel == 0) error (SWT.ERROR_NO_HANDLES);
		/*
		 * In VIRTUAL Table, GTK may react to `gtk_list_store_remove()` by
		 * calling `cellDataProc()`, and SWT will be confused because
		 * `items[]` no longer match the GTK model, which has some rows
		 * deleted by `gtk_list_store_remove()`. The fix is to disconnect
		 * model during rebuilding. The new model will replace the old one
		 * anyway, so it doesn't matter if old is removed a bit earlier.
		 */
		GTK.gtk_tree_view_set_model (handle, 0);
		copyModel (oldModel, column.modelIndex, newModel, FIRST_COLUMN, FIRST_COLUMN + CELL_TYPES);
		GTK.gtk_tree_view_set_model (handle, newModel);
		setModel (newModel);
		createColumn (null, 0);
	} else {
		for (int i=0; i<itemCount; i++) {
			TableItem item = items [i];
			if (item != null) {
				long iter = item.handle;
				int modelIndex = column.modelIndex;
				GTK.gtk_list_store_set (modelHandle, iter, modelIndex + CELL_PIXBUF, (long )0, -1);
				GTK.gtk_list_store_set (modelHandle, iter, modelIndex + CELL_TEXT, (long )0, -1);
				GTK.gtk_list_store_set (modelHandle, iter, modelIndex + CELL_FOREGROUND, (long )0, -1);
				GTK.gtk_list_store_set (modelHandle, iter, modelIndex + CELL_BACKGROUND, (long )0, -1);
				GTK.gtk_list_store_set (modelHandle, iter, modelIndex + CELL_FONT, (long )0, -1);

				Font [] cellFont = item.cellFont;
				if (cellFont != null) {
					if (columnCount == 0) {
						item.cellFont = null;
					} else {
						Font [] temp = new Font [columnCount];
						System.arraycopy (cellFont, 0, temp, 0, index);
						System.arraycopy (cellFont, index + 1, temp, index, columnCount - index);
						item.cellFont = temp;
					}
				}
			}
		}
		if (index == 0) {
			TableColumn checkColumn = columns [0];
			createRenderers (checkColumn.handle, checkColumn.modelIndex, true, checkColumn.style);
		}
	}
	if (!searchEnabled ()) {
		GTK.gtk_tree_view_set_search_column (handle, -1);
	} else {
		/* Set the search column whenever the model changes */
		int firstColumn = columnCount == 0 ? FIRST_COLUMN : columns [0].modelIndex;
		GTK.gtk_tree_view_set_search_column (handle, firstColumn + CELL_TEXT);
	}
}

void destroyItem (TableItem item) {
	int index = 0;
	while (index < itemCount) {
		if (items [index] == item) break;
		index++;
	}
	if (index == itemCount) return;
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_list_store_remove (modelHandle, item.handle);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	System.arraycopy (items, index + 1, items, index, --itemCount - index);
	items [itemCount] = null;
	if (itemCount == 0) resetCustomDraw ();
}

@Override
boolean dragDetect (int x, int y, boolean filter, boolean dragOnTimeout, boolean [] consume) {
	boolean selected = false;
	if (OS.isX11()) {
		if (filter) {
			long [] path = new long [1];
			if (GTK.gtk_tree_view_get_path_at_pos (handle, x, y, path, null, null, null)) {
				if (path [0] != 0) {
					long selection = GTK.gtk_tree_view_get_selection (handle);
					if (GTK.gtk_tree_selection_path_is_selected (selection, path [0])) selected = true;
					GTK.gtk_tree_path_free (path [0]);
				}
			} else {
				return false;
			}
		}
		boolean dragDetect = super.dragDetect (x, y, filter, false, consume);
		if (dragDetect && selected && consume != null) consume [0] = true;
		return dragDetect;
	} else {
		double [] startX = new double[1];
		double [] startY = new double [1];
		long [] path = new long [1];
		if (GTK.gtk_gesture_drag_get_start_point(dragGesture, startX, startY)) {
			if (getHeaderVisible()) {
				startY[0]-= getHeaderHeightInPixels();
			}
			if (GTK.gtk_tree_view_get_path_at_pos (handle, (int) startX[0], (int) startY[0], path, null, null, null)) {
				if (path [0] != 0) {
					boolean dragDetect = super.dragDetect (x, y, filter, false, consume);
					if (dragDetect && selected && consume != null) consume [0] = true;
					return dragDetect;
				}
			} else {
				return false;
			}
		}
	}
	return false;
}


@Override
long eventWindow () {
	return paintWindow ();
}

@Override
long eventSurface () {
	return paintSurface ();
}

@Override
Rectangle getClientAreaInPixels() {
	checkWidget();
	if(RESIZE_ON_GETCLIENTAREA) {
		forceResize();
	}

	long clientHandle = clientHandle();
	GtkAllocation allocation = new GtkAllocation();
	GTK.gtk_widget_get_allocation(clientHandle, allocation);
	int width = (state & ZERO_WIDTH) != 0 ? 0 : allocation.width;
	int height = (state & ZERO_HEIGHT) != 0 ? 0 : allocation.height;

	Rectangle rect;
	if (GTK.GTK4) {
		int[] headerHeight = new int[1], headerWidth = new int[1];
		GTK.gtk_tree_view_convert_bin_window_to_widget_coords(handle, 0, 0, headerWidth, headerHeight);
		rect = new Rectangle(headerWidth[0], headerHeight[0], width, height);
	} else {
		GTK.gtk_widget_realize(handle);
		long fixedWindow = gtk_widget_get_window(fixedHandle);
		long binWindow = GTK3.gtk_tree_view_get_bin_window(handle);
		int[] binX = new int[1], binY = new int[1];
		GDK.gdk_window_get_origin(binWindow, binX, binY);
		int[] fixedX = new int[1], fixedY = new int[1];
		GDK.gdk_window_get_origin(fixedWindow, fixedX, fixedY);
		rect = new Rectangle(fixedX[0] - binX[0], fixedY[0] - binY[0], width, height);
	}

	return rect;
}

@Override
int getClientWidth () {
	int [] w = new int [1], h = new int [1];
	if (GTK.GTK4) {
		long surface = gtk_widget_get_surface(handle);
		gdk_surface_get_size(surface, w, h);
	} else {
		GTK.gtk_widget_realize (handle);
		gdk_window_get_size(GTK3.gtk_tree_view_get_bin_window(handle), w, h);
	}
	return w[0];
}

/**
 * Returns the column at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
 * Columns are returned in the order that they were created.
 * If no <code>TableColumn</code>s were created by the programmer,
 * this method will throw <code>ERROR_INVALID_RANGE</code> despite
 * the fact that a single column of data may be visible in the table.
 * This occurs when the programmer uses the table like a list, adding
 * items but never creating a column.
 *
 * @param index the index of the column to return
 * @return the column at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#getColumnOrder()
 * @see Table#setColumnOrder(int[])
 * @see TableColumn#getMoveable()
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 */
public TableColumn getColumn (int index) {
	checkWidget();
	if (!(0 <= index && index < columnCount)) error (SWT.ERROR_INVALID_RANGE);
	return columns [index];
}

/**
 * Returns the number of columns contained in the receiver.
 * If no <code>TableColumn</code>s were created by the programmer,
 * this value is zero, despite the fact that visually, one column
 * of items may be visible. This occurs when the programmer uses
 * the table like a list, adding items but never creating a column.
 *
 * @return the number of columns
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getColumnCount () {
	checkWidget();
	return columnCount;
}

long [] getColumnTypes (int columnCount) {
	long [] types = new long [FIRST_COLUMN + (columnCount * CELL_TYPES)];
	// per row data
	types [CHECKED_COLUMN] = OS.G_TYPE_BOOLEAN ();
	types [GRAYED_COLUMN] = OS.G_TYPE_BOOLEAN ();
	types [FOREGROUND_COLUMN] = GDK.GDK_TYPE_RGBA();
	types [BACKGROUND_COLUMN] = GDK.GDK_TYPE_RGBA();
	types [FONT_COLUMN] = OS.PANGO_TYPE_FONT_DESCRIPTION ();
	// per cell data
	for (int i=FIRST_COLUMN; i<types.length; i+=CELL_TYPES) {
		types [i + CELL_PIXBUF] = GDK.GDK_TYPE_PIXBUF ();
		types [i + CELL_SURFACE] = OS.G_TYPE_LONG();
		types [i + CELL_TEXT] = OS.G_TYPE_STRING ();
		types [i + CELL_FOREGROUND] = GDK.GDK_TYPE_RGBA();
		types [i + CELL_BACKGROUND] = GDK.GDK_TYPE_RGBA();
		types [i + CELL_FONT] = OS.PANGO_TYPE_FONT_DESCRIPTION ();
	}
	return types;
}

/**
 * Returns an array of zero-relative integers that map
 * the creation order of the receiver's items to the
 * order in which they are currently being displayed.
 * <p>
 * Specifically, the indices of the returned array represent
 * the current visual order of the items, and the contents
 * of the array represent the creation order of the items.
 * </p><p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver.
 * </p>
 *
 * @return the current visual order of the receiver's items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#setColumnOrder(int[])
 * @see TableColumn#getMoveable()
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 *
 * @since 3.1
 */
public int [] getColumnOrder () {
	checkWidget ();
	if (columnCount == 0) return new int [0];
	long list = GTK.gtk_tree_view_get_columns (handle);
	if (list == 0) return new int [0];
	int  i = 0, count = OS.g_list_length (list);
	int [] order = new int [count];
	long temp = list;
	while (temp != 0) {
		long column = OS.g_list_data (temp);
		if (column != 0) {
			for (int j=0; j<columnCount; j++) {
				if (columns [j].handle == column) {
					order [i++] = j;
					break;
				}
			}
		}
		temp = OS.g_list_next (temp);
	}
	OS.g_list_free (list);
	return order;
}

/**
 * Returns an array of <code>TableColumn</code>s which are the
 * columns in the receiver.  Columns are returned in the order
 * that they were created.  If no <code>TableColumn</code>s were
 * created by the programmer, the array is empty, despite the fact
 * that visually, one column of items may be visible. This occurs
 * when the programmer uses the table like a list, adding items but
 * never creating a column.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver.
 * </p>
 *
 * @return the items in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#getColumnOrder()
 * @see Table#setColumnOrder(int[])
 * @see TableColumn#getMoveable()
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 */
public TableColumn [] getColumns () {
	checkWidget();
	TableColumn [] result = new TableColumn [columnCount];
	System.arraycopy (columns, 0, result, 0, columnCount);
	return result;
}

@Override
GdkRGBA getContextBackgroundGdkRGBA () {
	if (background != null) {
			return background;
	} else {
		// For Tables and Trees, the default background is
		// COLOR_LIST_BACKGROUND instead of COLOR_WIDGET_BACKGROUND.
		return defaultBackground();
	}
}

@Override
GdkRGBA getContextColorGdkRGBA () {
	if (foreground != null) {
		return foreground;
	} else {
		return display.COLOR_LIST_FOREGROUND_RGBA;
	}
}

TableItem getFocusItem () {
	long [] path = new long [1];
	GTK.gtk_tree_view_get_cursor (handle, path, null);
	if (path [0] == 0) return null;
	TableItem item = null;
	long indices = GTK.gtk_tree_path_get_indices (path [0]);
	if (indices != 0) {
		int [] index = new int []{-1};
		C.memmove (index, indices, 4);
		item = _getItem (index [0]);
	}
	GTK.gtk_tree_path_free (path [0]);
	return item;
}

/**
 * Returns the width in points of a grid line.
 *
 * @return the width of a grid line in points
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getGridLineWidth () {
	checkWidget ();
	return DPIUtil.autoScaleDown (getGridLineWidthInPixels ());
}

int getGridLineWidthInPixels () {
	checkWidget();
	return 0;
}

/**
 * Returns the header background color.
 *
 * @return the receiver's header background color.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.106
 */
public Color getHeaderBackground () {
	checkWidget ();
	return headerBackground != null ? headerBackground : display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
}

/**
 * Returns the header foreground color.
 *
 * @return the receiver's header foreground color.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.106
 */
public Color getHeaderForeground () {
	checkWidget ();
	return headerForeground != null ? headerForeground : display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);
}

/**
 * Returns the height of the receiver's header
 *
 * @return the height of the header or zero if the header is not visible
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 2.0
 */
public int getHeaderHeight () {
	checkWidget ();
	return DPIUtil.autoScaleDown (getHeaderHeightInPixels ());
}

int getHeaderHeightInPixels () {
	checkWidget();
	if (!GTK.gtk_tree_view_get_headers_visible(handle)) return 0;

	int height = 0;
	if (columnCount > 0) {
		GtkRequisition requisition = new GtkRequisition ();
		for (int i = 0; i < columnCount; i++) {
			long buttonHandle = columns[i].buttonHandle;
			if (buttonHandle != 0) {
				if (!GTK.gtk_widget_get_visible(buttonHandle))  {
					GTK.gtk_widget_show(buttonHandle);
					gtk_widget_get_preferred_size(buttonHandle, requisition);
					GTK.gtk_widget_hide(buttonHandle);
				} else {
					gtk_widget_get_preferred_size(buttonHandle, requisition);
				}
				height = Math.max(height, requisition.height);
			}
		}
	} else {
		if (GTK.GTK4) {
			int[] headerHeight = new int[1];
			GTK.gtk_tree_view_convert_bin_window_to_widget_coords(handle, 0, 0, null, headerHeight);
			height = headerHeight[0];
		} else {
			GTK.gtk_widget_realize (handle);
			long fixedWindow = gtk_widget_get_window (fixedHandle);
			long binWindow = GTK3.gtk_tree_view_get_bin_window (handle);
			int [] binY = new int [1];
			GDK.gdk_window_get_origin (binWindow, null, binY);
			int [] fixedY = new int [1];
			GDK.gdk_window_get_origin (fixedWindow, null, fixedY);
			height = binY [0] - fixedY [0];
		}
	}

	return height;
}

/**
 * Returns <code>true</code> if the receiver's header is visible,
 * and <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's header's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getHeaderVisible () {
	checkWidget();
	return GTK.gtk_tree_view_get_headers_visible (handle);
}

/**
 * Returns the item at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
 *
 * @param index the index of the item to return
 * @return the item at the given index
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TableItem getItem (int index) {
	checkWidget();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	return _getItem (index);
}

/**
 * Returns the item at the given point in the receiver
 * or null if no such item exists. The point is in the
 * coordinate system of the receiver.
 * <p>
 * The item that is returned represents an item that could be selected by the user.
 * For example, if selection only occurs in items in the first column, then null is
 * returned if the point is outside of the item.
 * Note that the SWT.FULL_SELECTION style hint, which specifies the selection policy,
 * determines the extent of the selection.
 * </p>
 *
 * @param point the point used to locate the item
 * @return the item at the given point, or null if the point is not in a selectable item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TableItem getItem (Point point) {
	checkWidget();
	return getItemInPixels(DPIUtil.autoScaleUp(point));
}

TableItem getItemInPixels (Point point) {
	checkWidget();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	long [] path = new long [1];
	GTK.gtk_widget_realize (handle);
	int y = point.y;
	/*
	 * On GTK4 the header is included in the entire widget's surface, so we must subtract
	 * its size from the y-coordinate. This does not apply on GTK3 as the header and
	 * "main-widget" have separate GdkWindows.
	 */
	if (getHeaderVisible() && GTK.GTK4) {
		y -= getHeaderHeight();
	}
	if (!GTK.gtk_tree_view_get_path_at_pos (handle, point.x, y, path, null, null, null)) return null;
	if (path [0] == 0) return null;
	long indices = GTK.gtk_tree_path_get_indices (path [0]);
	TableItem item = null;
	if (indices != 0) {
		int [] index = new int [1];
		C.memmove (index, indices, 4);
		item = _getItem (index [0]);
	}
	GTK.gtk_tree_path_free (path [0]);
	return item;
}

/**
 * Returns the number of items contained in the receiver.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget ();
	return itemCount;
}

/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the receiver.
 *
 * @return the height of one item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemHeight () {
	checkWidget ();
	return DPIUtil.autoScaleDown (getItemHeightInPixels ());
}

int getItemHeightInPixels () {
	checkWidget();
	int height = 0;

	if (itemCount == 0) {
		long column = GTK.gtk_tree_view_get_column(handle, 0);
		int[] h = new int[1];
		ignoreSize = true;
		if (GTK.GTK4) {
			GTK4.gtk_tree_view_column_cell_get_size(column, null, null, null, h);
		} else {
			GTK3.gtk_tree_view_column_cell_get_size(column, null, null, null, null, h);
		}

		height = h[0];
		long textRenderer = getTextRenderer(column);
		if (textRenderer != 0) GTK.gtk_cell_renderer_get_preferred_height_for_width(textRenderer, handle, 0, h, null);
		height += h[0];
		ignoreSize = false;
	} else {
		long iter = OS.g_malloc(GTK.GtkTreeIter_sizeof());
		GTK.gtk_tree_model_get_iter_first(modelHandle, iter);

		int columnCount = Math.max(1, this.columnCount);
		for (int i = 0; i < columnCount; i++) {
			long column = GTK.gtk_tree_view_get_column(handle, i);
			GTK.gtk_tree_view_column_cell_set_cell_data(column, modelHandle, iter, false, false);
			int[] h = new int[1];
			if (GTK.GTK4) {
				GTK4.gtk_tree_view_column_cell_get_size(column, null, null, null, h);
			} else {
				GTK3.gtk_tree_view_column_cell_get_size (column, null, null, null, null, h);
			}

			long textRenderer = getTextRenderer(column);
			int[] ypad = new int[1];
			if (textRenderer != 0) GTK.gtk_cell_renderer_get_padding(textRenderer, null, ypad);
			height = Math.max(height, h[0] + ypad[0]);
		}

		OS.g_free (iter);
	}

	return height;
}

/**
 * Returns a (possibly empty) array of <code>TableItem</code>s which
 * are the items in the receiver.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver.
 * </p>
 *
 * @return the items in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TableItem [] getItems () {
	checkWidget();
	TableItem [] result = new TableItem [itemCount];
	if ((style & SWT.VIRTUAL) != 0) {
		for (int i=0; i<itemCount; i++) {
			result [i] = _getItem (i);
		}
	} else {
		System.arraycopy (items, 0, result, 0, itemCount);
	}
	return result;
}

/**
 * Returns <code>true</code> if the receiver's lines are visible,
 * and <code>false</code> otherwise. Note that some platforms draw
 * grid lines while others may draw alternating row colors.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the visibility state of the lines
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getLinesVisible() {
	checkWidget();
	return GTK.gtk_tree_view_get_grid_lines(handle) > GTK.GTK_TREE_VIEW_GRID_LINES_NONE;
}

long getPixbufRenderer (long column) {
	long list = GTK.gtk_cell_layout_get_cells(column);
	if (list == 0) return 0;
	long originalList = list;
	long pixbufRenderer = 0;
	while (list != 0) {
		long renderer = OS.g_list_data (list);
		if (GTK.GTK_IS_CELL_RENDERER_PIXBUF (renderer)) {
			pixbufRenderer = renderer;
			break;
		}
		list = OS.g_list_next (list);
	}
	OS.g_list_free (originalList);
	return pixbufRenderer;
}

/**
 * Returns an array of <code>TableItem</code>s that are currently
 * selected in the receiver. The order of the items is unspecified.
 * An empty array indicates that no items are selected.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its selection, so modifying the array will
 * not affect the receiver.
 * </p>
 * @return an array representing the selection
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TableItem [] getSelection () {
	checkWidget();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	long list = GTK.gtk_tree_selection_get_selected_rows (selection, null);
	long originalList = list;
	if (list != 0) {
		int count = OS.g_list_length (list);
		int [] treeSelection = new int [count];
		int length = 0;
		for (int i=0; i<count; i++) {
			long data = OS.g_list_data (list);
			long indices = GTK.gtk_tree_path_get_indices (data);
			if (indices != 0) {
				int [] index = new int [1];
				C.memmove (index, indices, 4);
				treeSelection [length] = index [0];
				length++;
			}
			GTK.gtk_tree_path_free (data);
			list = OS.g_list_next (list);
		}
		OS.g_list_free (originalList);
		TableItem [] result = new TableItem [length];
		for (int i=0; i<result.length; i++) result [i] = _getItem (treeSelection [i]);
		return result;
	}
	return new TableItem [0];
}

/**
 * Returns the number of selected items contained in the receiver.
 *
 * @return the number of selected items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionCount () {
	checkWidget();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	return GTK.gtk_tree_selection_count_selected_rows (selection);
}

/**
 * Returns the zero-relative index of the item which is currently
 * selected in the receiver, or -1 if no item is selected.
 *
 * @return the index of the selected item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionIndex () {
	checkWidget();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	long list = GTK.gtk_tree_selection_get_selected_rows (selection, null);
	long originalList = list;
	if (list != 0) {
		int [] index = new int [1];
		boolean foundIndex = false;
		while (list != 0) {
			long data = OS.g_list_data (list);
			if (!foundIndex) {
				long indices = GTK.gtk_tree_path_get_indices (data);
				if (indices != 0) {
					C.memmove (index, indices, 4);
					foundIndex = true;
				}
			}
			list = OS.g_list_next (list);
			GTK.gtk_tree_path_free (data);
		}
		OS.g_list_free (originalList);
		return index [0];
	}
	return -1;
}

/**
 * Returns the zero-relative indices of the items which are currently
 * selected in the receiver. The order of the indices is unspecified.
 * The array is empty if no items are selected.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its selection, so modifying the array will
 * not affect the receiver.
 * </p>
 * @return the array of indices of the selected items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int [] getSelectionIndices () {
	checkWidget();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	long list = GTK.gtk_tree_selection_get_selected_rows (selection, null);
	long originalList = list;
	if (list != 0) {
		int count = OS.g_list_length (list);
		int [] treeSelection = new int [count];
		int length = 0;
		for (int i=0; i<count; i++) {
			long data = OS.g_list_data (list);
			long indices = GTK.gtk_tree_path_get_indices (data);
			if (indices != 0) {
				int [] index = new int [1];
				C.memmove (index, indices, 4);
				treeSelection [length] = index [0];
				length++;
			}
			list = OS.g_list_next (list);
			GTK.gtk_tree_path_free (data);
		}
		OS.g_list_free (originalList);
		int [] result = new int [length];
		System.arraycopy (treeSelection, 0, result, 0, length);
		return result;
	}
	return new int [0];
}

/**
 * Returns the column which shows the sort indicator for
 * the receiver. The value may be null if no column shows
 * the sort indicator.
 *
 * @return the sort indicator
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setSortColumn(TableColumn)
 *
 * @since 3.2
 */
public TableColumn getSortColumn () {
	checkWidget ();
	return sortColumn;
}

/**
 * Returns the direction of the sort indicator for the receiver.
 * The value will be one of <code>UP</code>, <code>DOWN</code>
 * or <code>NONE</code>.
 *
 * @return the sort direction
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setSortDirection(int)
 *
 * @since 3.2
 */
public int getSortDirection () {
	checkWidget ();
	return sortDirection;
}

long getTextRenderer (long column) {
	long list = GTK.gtk_cell_layout_get_cells(column);
	if (list == 0) return 0;
	long originalList = list;
	long textRenderer = 0;
	while (list != 0) {
		long renderer = OS.g_list_data (list);
		if (GTK.GTK_IS_CELL_RENDERER_TEXT (renderer)) {
			textRenderer = renderer;
			break;
		}
		list = OS.g_list_next (list);
	}
	OS.g_list_free (originalList);
	return textRenderer;
}

/**
 * Returns the zero-relative index of the item which is currently
 * at the top of the receiver. This index can change when items are
 * scrolled or new items are added or removed.
 *
 * @return the index of the top item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopIndex () {
	checkWidget();
	/*
	 * Feature in GTK: fetch the topIndex using the topItem global variable
	 * if setTopIndex() has been called and the widget has not been scrolled
	 * using the UI. Otherwise, fetch topIndex using GtkTreeView API.
	 */
	long vAdjustment;
	vAdjustment = GTK.gtk_scrollable_get_vadjustment(handle);
	currentAdjustment = GTK.gtk_adjustment_get_value(vAdjustment);
	if (cachedAdjustment == currentAdjustment){
		return topIndex;
	} else {
		long [] path = new long [1];
		GTK.gtk_widget_realize (handle);
		if (!GTK.gtk_tree_view_get_path_at_pos (handle, 1, 1, path, null, null, null)) return 0;
		if (path [0] == 0) return 0;
		long indices = GTK.gtk_tree_path_get_indices (path[0]);
		int[] index = new int [1];
		if (indices != 0) C.memmove (index, indices, 4);
		GTK.gtk_tree_path_free (path [0]);
		return index [0];
	}
}

@Override
long gtk_button_press_event (long widget, long event) {
	double [] eventX = new double [1];
	double [] eventY = new double [1];
	GDK.gdk_event_get_coords(event, eventX, eventY);

	int eventType = GDK.gdk_event_get_event_type(event);

	int [] eventButton = new int [1];
	int [] eventState = new int [1];
	GDK.gdk_event_get_button(event, eventButton);
	GDK.gdk_event_get_state(event, eventState);

	double [] eventRX = new double [1];
	double [] eventRY = new double [1];
	GDK.gdk_event_get_root_coords(event, eventRX, eventRY);


	long eventGdkResource = gdk_event_get_surface_or_window(event);
	if (eventGdkResource != GTK3.gtk_tree_view_get_bin_window (handle)) return 0;

	long result = super.gtk_button_press_event (widget, event);
	if (result != 0) return result;
	/*
	 * Feature in GTK. In multi-select tree view there is a problem with using DnD operations while also selecting multiple items.
	 * When doing a DnD, GTK de-selects all other items except for the widget being dragged from. By disabling the selection function
	 * in GTK in the case that additional items aren't being added (CTRL_MASK or SHIFT_MASK) and the item being dragged is already
	 * selected, we can give the DnD handling to MOTION-NOTIFY. Seee Bug 503431
	 */
	if ((state & DRAG_DETECT) != 0 && hooks (SWT.DragDetect) &&
			OS.isWayland() && eventType == GDK.GDK_BUTTON_PRESS) {
		// check to see if there is another event coming in that is not a double/triple click, this is to prevent Bug 514531
		long nextEvent = GDK.gdk_event_peek();
		if (nextEvent == 0) {
			long [] path = new long [1];
			long selection = GTK.gtk_tree_view_get_selection (handle);
			if (GTK.gtk_tree_view_get_path_at_pos (handle, (int)eventX[0], (int)eventY[0], path, null, null, null) &&
					path[0] != 0) {
				//  selection count is used in the case of clicking an already selected item while holding Control
				selectionCountOnPress = getSelectionCount();
				if (GTK.gtk_tree_selection_path_is_selected (selection, path[0])) {
					if (((eventState[0] & (GDK.GDK_CONTROL_MASK|GDK.GDK_SHIFT_MASK)) == 0) ||
							((eventState[0] & GDK.GDK_CONTROL_MASK) != 0)) {
						/**
						 * Disable selection on a mouse click if there are multiple items already selected. Also,
						 * if control is currently being held down, we will designate the selection logic over to release
						 * instead by first disabling the selection.
						 * E.g to reproduce: Open DNDExample, select "Tree", select multiple items, try dragging.
						 *   without line below, only one item is selected for drag.
						 */
						long gtk_false_funcPtr = GTK.GET_FUNCTION_POINTER_gtk_false();
						GTK.gtk_tree_selection_set_select_function(selection, gtk_false_funcPtr, 0, 0);
					}
				}
			}
		} else {
			gdk_event_free (nextEvent);
		}
	}
	/*
	* Feature in GTK.  In a multi-select table view, when multiple items are already
	* selected, the selection state of the item is toggled and the previous selection
	* is cleared. This is not the desired behaviour when bringing up a popup menu.
	* Also, when an item is reselected with the right button, the tree view issues
	* an unwanted selection event. The workaround is to detect that case and not
	* run the default handler when the item is already part of the current selection.
	*/
	int button = eventButton[0];
	if (button == 3 && eventType == GDK.GDK_BUTTON_PRESS) {
		long [] path = new long [1];
		if (GTK.gtk_tree_view_get_path_at_pos (handle, (int)eventX[0], (int)eventY[0], path, null, null, null)) {
			if (path [0] != 0) {
				long selection = GTK.gtk_tree_view_get_selection (handle);
				if (GTK.gtk_tree_selection_path_is_selected (selection, path [0])) result = 1;
				GTK.gtk_tree_path_free (path [0]);
			}
		}
	}

	/*
	* Feature in GTK.  When the user clicks in a single selection GtkTreeView
	* and there are no selected items, the first item is selected automatically
	* before the click is processed, causing two selection events.  The is fix
	* is the set the cursor item to be same as the clicked item to stop the
	* widget from automatically selecting the first item.
	*/
	if ((style & SWT.SINGLE) != 0 && getSelectionCount () == 0) {
		long [] path = new long [1];
		if (GTK.gtk_tree_view_get_path_at_pos (handle, (int)eventX[0], (int)eventY[0], path, null, null, null)) {
			if (path [0] != 0) {
				long selection = GTK.gtk_tree_view_get_selection (handle);
				OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				GTK.gtk_tree_view_set_cursor (handle, path [0], 0, false);
				OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				GTK.gtk_tree_path_free (path [0]);
			}
		}
	}

	/*
	 * Bug 312568: If mouse double-click pressed, manually send a DefaultSelection.
	 * Bug 518414: Added rowActivated guard flag to only send a DefaultSelection when the
	 * double-click triggers a 'row-activated' signal. Note that this relies on the fact
	 * that 'row-activated' signal comes before double-click event. This prevents
	 * opening of the current highlighted item when double clicking on any expander arrow.
	 */
	if (eventType == GDK.GDK_2BUTTON_PRESS && rowActivated) {
		sendTreeDefaultSelection ();
		rowActivated = false;
	}

	return result;
}

@Override
void gtk_gesture_press_event (long gesture, int n_press, double x, double y, long event) {
	if (n_press == 1) return;
	super.gtk_gesture_press_event(gesture, n_press, x, y, event);

	// TODO: GTK4 replicate gtk_button_press_event functions

	if (n_press == 2 && rowActivated) {
		sendTreeDefaultSelection ();
		rowActivated = false;
	}
}

@Override
long gtk_row_activated (long tree, long path, long column) {
	rowActivated = true;
	return 0;
}


@Override
long gtk_key_press_event (long widget, long event) {
	int [] key = new int[1];
	if (GTK.GTK4) {
		key[0] = GDK.gdk_key_event_get_keyval(event);
	} else {
		GDK.gdk_event_get_keyval(event, key);
	}

	switch (key[0]) {
		case GDK.GDK_Return:
			// Send DefaultSelectionEvent when:
			// when    : Enter, Shift+Enter, Ctrl+Enter are pressed.
			// Not when: Alt+Enter, (Meta|Super|Hyper)+Enter, reason is stateMask is not provided on Gtk.
			// Note: alt+Enter creates a selection on GTK, but we filter it out to be a bit more consitent Win32 (521387)
			int keymask = gdk_event_get_state (event);
			if ((keymask & (GDK.GDK_SUPER_MASK | GDK.GDK_META_MASK | GDK.GDK_HYPER_MASK | GDK.GDK_MOD1_MASK)) == 0) {
				sendTreeDefaultSelection ();
			}
			break;
		case GDK.GDK_space:
			if ((style & SWT.CHECK) != 0) {
				TableItem[] selected = getSelection();
				for (int i = 0; i < selected.length; i++) {
					toggleItemAndSendEvent(selected[i]);
				}

				// Maintain current selection by stopping additional handling to GDK_space event
				return 1;
			}
			break;
	}

	return super.gtk_key_press_event (widget, event);
}

private void toggleItemAndSendEvent(TableItem item) {
	item.setChecked (!item.getChecked ());

	Event event = new Event ();
	event.detail = SWT.CHECK;
	event.item = item;
	sendSelectionEvent (SWT.Selection, event, false);
}

/**
 * Used to emulate DefaultSelection event. See Bug 312568.
 * Feature in GTK. 'row-activation' event comes before DoubleClick event.
 * This is causing the editor not to get focus after double-click.
 * The solution is to manually send the DefaultSelection event after a double-click,
 * and to emulate it for Space/Return.
 */
void sendTreeDefaultSelection() {

	//Note, similar DefaultSelectionHandling in SWT List/Table/Tree
	TableItem tableItem = getFocusItem ();
	if (tableItem == null)
		return;

	Event event = new Event ();
	event.item = tableItem;

	sendSelectionEvent (SWT.DefaultSelection, event, false);
}


@Override
long gtk_button_release_event (long widget, long event) {
	double [] eventX = new double [1];
	double [] eventY = new double [1];
	if (GTK.GTK4) {
		GDK.gdk_event_get_position(event, eventX, eventY);
	} else {
		GDK.gdk_event_get_coords(event, eventX, eventY);
	}

	int [] eventButton = new int [1];
	int [] eventState = new int [1];
	if (GTK.GTK4) {
		eventButton[0] = GDK.gdk_button_event_get_button(event);
		eventState[0] = GDK.gdk_event_get_modifier_state(event);
	} else {
		GDK.gdk_event_get_button(event, eventButton);
		GDK.gdk_event_get_state(event, eventState);
	}

	double [] eventRX = new double [1];
	double [] eventRY = new double [1];
	GDK.gdk_event_get_root_coords(event, eventRX, eventRY);

	long eventGdkResource = gdk_event_get_surface_or_window(event);
	if (GTK.GTK4) {
		if (eventGdkResource != gtk_widget_get_surface (handle)) return 0;
	} else {
		if (eventGdkResource != GTK3.gtk_tree_view_get_bin_window (handle)) return 0;
	}
	// Check region since super.gtk_button_release_event() isn't called
	lastInput.x = (int) eventX[0];
	lastInput.y = (int) eventY[0];
	if (containedInRegion(lastInput.x, lastInput.y)) return 0;
	/*
	 * Feature in GTK. In multi-select tree view there is a problem with using DnD operations while also selecting multiple items.
	 * When doing a DnD, GTK de-selects all other items except for the widget being dragged from. By disabling the selection function
	 * in GTK in the case that additional items aren't being added (CTRL_MASK or SHIFT_MASK) and the item being dragged is already
	 * selected, we can give the DnD handling to MOTION-NOTIFY. On release, we can then re-enable the selection method
	 * and also select the item in the tree by moving the selection logic to release instead. See Bug 503431.
	 */
	if ((state & DRAG_DETECT) != 0 && hooks (SWT.DragDetect) && OS.isWayland()) {
		long [] path = new long [1];
		long selection = GTK.gtk_tree_view_get_selection (handle);
		// free up the selection function on release.
		GTK.gtk_tree_selection_set_select_function(selection,0,0,0);
		if (GTK.gtk_tree_view_get_path_at_pos (handle, (int)eventX[0], (int)eventY[0], path, null, null, null) &&
				path[0] != 0 && GTK.gtk_tree_selection_path_is_selected (selection, path[0])) {
			selectionCountOnRelease = getSelectionCount();
			if ((eventState[0] & (GDK.GDK_CONTROL_MASK|GDK.GDK_SHIFT_MASK)) == 0) {
				GTK.gtk_tree_view_set_cursor(handle, path[0], 0,  false);
			}
			// Check to see if there has been a new tree item selected when holding Control in Path.
			// If not, deselect the item.
			if ((eventState[0] & GDK.GDK_CONTROL_MASK) != 0 && selectionCountOnRelease == selectionCountOnPress) {
				GTK.gtk_tree_selection_unselect_path (selection,path[0]);
			}
		}
	}
	return super.gtk_button_release_event (widget, event);
}

@Override
long gtk_changed (long widget) {
	TableItem item = getFocusItem ();
	if (item != null) {
		Event event = new Event ();
		event.item = item;
		sendSelectionEvent (SWT.Selection, event, false);
	}
	return 0;
}

void drawInheritedBackground (long cairo) {
	if ((state & PARENT_BACKGROUND) != 0 || backgroundImage != null) {
		Control control = findBackgroundControl ();
		if (control != null) {
			int [] width = new int [1], height = new int [1];
			long gdkResource;
			if (GTK.GTK4) {
				gdkResource = gtk_widget_get_surface(handle);
				gdk_surface_get_size (gdkResource, width, height);
			} else {
				gdkResource = GTK3.gtk_tree_view_get_bin_window (handle);
				gdk_window_get_size (gdkResource, width, height);
			}
			int bottom = 0;
			if (itemCount != 0) {
				long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
				GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, itemCount - 1);
				long path = GTK.gtk_tree_model_get_path (modelHandle, iter);
				GdkRectangle rect = new GdkRectangle ();
				GTK.gtk_tree_view_get_cell_area (handle, path, 0, rect);
				bottom = rect.y + rect.height;
				GTK.gtk_tree_path_free (path);
				OS.g_free (iter);
			}
			if (height [0] > bottom) {
				drawBackground (control, gdkResource, cairo, 0, bottom, width [0], height [0] - bottom);
			}
		}
	}
}

@Override
long gtk_draw (long widget, long cairo) {
	boolean haveBoundsChanged = boundsChangedSinceLastDraw;
	boundsChangedSinceLastDraw = false;
	if ((state & OBSCURED) != 0) return 0;
	/*
	 * Bug 537960: JFace table viewers miss a repaint when resized by a SashForm
	 *
	 * If a listener of type SWT.MeasureItem, SWT.PaintItem and or SWT.EraseItem is registered,
	 * GTK will sometimes not invalidate the tree widget pixel cache when the tree is resized.
	 * As a result, a few of the bottom rows of JFace table viewers that use styled text are often not drawn on resize.
	 * If the table was resized since the last paint, we ignore this draw request
	 * and queue another draw request so that the pixel cache is properly invalidated.
	 */
	if (ownerDraw && haveBoundsChanged) {
		GTK.gtk_widget_queue_draw(handle);
		return 0;
	}
	drawInheritedBackground (cairo);
	return super.gtk_draw (widget, cairo);
}

@Override
long gtk_motion_notify_event (long widget, long event) {
	if (GTK.GTK4) {
		long surface = GDK.gdk_event_get_surface(event);
		if (surface != gtk_widget_get_surface(handle)) return 0;
	} else {
		long window = GDK.GDK_EVENT_WINDOW (event);
		if (window != GTK3.gtk_tree_view_get_bin_window (handle)) return 0;
	}
	return super.gtk_motion_notify_event (widget, event);
}

@Override
long gtk_scroll_event (long widget, long eventPtr) {
	long result = super.gtk_scroll_event(widget, eventPtr);
	if (!wasScrolled) wasScrolled = true;
	return result;
}

@Override
long gtk_start_interactive_search(long widget) {
	if (!searchEnabled()) {
		OS.g_signal_stop_emission_by_name(widget, OS.start_interactive_search);
		return 1;
	}
	return 0;
}
@Override
long gtk_toggled (long renderer, long pathStr) {
	long path = GTK.gtk_tree_path_new_from_string (pathStr);
	if (path == 0) return 0;
	long indices = GTK.gtk_tree_path_get_indices (path);
	if (indices != 0) {
		int [] index = new int [1];
		C.memmove (index, indices, 4);
		TableItem item = _getItem (index [0]);
		toggleItemAndSendEvent(item);
	}
	GTK.gtk_tree_path_free (path);
	return 0;
}

@Override
void gtk_widget_get_preferred_size (long widget, GtkRequisition requisition) {
	/*
	 * Bug in GTK.  For some reason, gtk_widget_size_request() fails
	 * to include the height of the tree view items when there are
	 * no columns visible.  The fix is to temporarily make one column
	 * visible.
	 */
	if (columnCount == 0) {
		super.gtk_widget_get_preferred_size (widget, requisition);
		return;
	}
	long columns = GTK.gtk_tree_view_get_columns (handle), list = columns;
	boolean fixVisible = columns != 0;
	while (list != 0) {
		long column = OS.g_list_data (list);
		if (GTK.gtk_tree_view_column_get_visible (column)) {
			fixVisible = false;
			break;
		}
		list = OS.g_list_next (list);
	}
	long columnHandle = 0;
	if (fixVisible) {
		columnHandle = OS.g_list_data (columns);
		GTK.gtk_tree_view_column_set_visible (columnHandle, true);
	}
	super.gtk_widget_get_preferred_size (widget, requisition);
	if (fixVisible) {
		GTK.gtk_tree_view_column_set_visible (columnHandle, false);
	}
	if (columns != 0) OS.g_list_free (columns);
}

void hideFirstColumn () {
	long firstColumn = GTK.gtk_tree_view_get_column (handle, 0);
	GTK.gtk_tree_view_column_set_visible (firstColumn, false);
}

@Override
void hookEvents () {
	super.hookEvents ();
	long selection = GTK.gtk_tree_view_get_selection(handle);
	OS.g_signal_connect_closure (selection, OS.changed, display.getClosure (CHANGED), false);
	OS.g_signal_connect_closure (handle, OS.row_activated, display.getClosure (ROW_ACTIVATED), false);
	if (checkRenderer != 0) {
		OS.g_signal_connect_closure (checkRenderer, OS.toggled, display.getClosure (TOGGLED), false);
	}
	OS.g_signal_connect_closure (handle, OS.start_interactive_search, display.getClosure (START_INTERACTIVE_SEARCH), false);
}

/**
 * Searches the receiver's list starting at the first column
 * (index 0) until a column is found that is equal to the
 * argument, and returns the index of that column. If no column
 * is found, returns -1.
 *
 * @param column the search column
 * @return the index of the column
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the column is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (TableColumn column) {
	checkWidget();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<columnCount; i++) {
		if (columns [i] == column) return i;
	}
	return -1;
}

/**
 * Searches the receiver's list starting at the first item
 * (index 0) until an item is found that is equal to the
 * argument, and returns the index of that item. If no item
 * is found, returns -1.
 *
 * @param item the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (TableItem item) {
	checkWidget();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (1 <= lastIndexOf && lastIndexOf < itemCount - 1) {
		if (items [lastIndexOf] == item) return lastIndexOf;
		if (items [lastIndexOf + 1] == item) return ++lastIndexOf;
		if (items [lastIndexOf - 1] == item) return --lastIndexOf;
	}
	if (lastIndexOf < itemCount / 2) {
		for (int i=0; i<itemCount; i++) {
			if (items [i] == item) return lastIndexOf = i;
		}
	} else {
		for (int i=itemCount - 1; i>=0; --i) {
			if (items [i] == item) return lastIndexOf = i;
		}
	}
	return -1;
}

/**
 * Returns <code>true</code> if the item is selected,
 * and <code>false</code> otherwise.  Indices out of
 * range are ignored.
 *
 * @param index the index of the item
 * @return the selection state of the item at the index
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isSelected (int index) {
	checkWidget();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	byte [] buffer = Converter.wcsToMbcs (Integer.toString (index), true);
	long path = GTK.gtk_tree_path_new_from_string (buffer);
	boolean answer = GTK.gtk_tree_selection_path_is_selected (selection, path);
	GTK.gtk_tree_path_free (path);
	return answer;
}

@Override
boolean mnemonicHit (char key) {
	for (int i=0; i<columnCount; i++) {
		long labelHandle = columns [i].labelHandle;
		if (labelHandle != 0 && mnemonicHit (labelHandle, key)) return true;
	}
	return false;
}

@Override
boolean mnemonicMatch (char key) {
	for (int i=0; i<columnCount; i++) {
		long labelHandle = columns [i].labelHandle;
		if (labelHandle != 0 && mnemonicMatch (labelHandle, key)) return true;
	}
	return false;
}

@Override
long paintWindow () {
	GTK.gtk_widget_realize (handle);
	// TODO: this function has been removed on GTK4
	return GTK3.gtk_tree_view_get_bin_window (handle);
}

@Override
void propagateDraw (long container, long cairo) {
	/*
	 * Sometimes Tree/Table headers need to be re-drawn, as some of the
	 * "noChildDrawing" widgets might still be partially drawn.
	 */
	super.propagateDraw(container, cairo);
	if (headerVisible && noChildDrawing && wasScrolled) {
		for (TableColumn column : columns) {
			if (column != null) {
				GTK.gtk_widget_queue_draw(column.buttonHandle);
			}
		}
		wasScrolled = false;
	}
}

void recreateRenderers () {
	if (checkRenderer != 0) {
		display.removeWidget (checkRenderer);
		OS.g_object_unref (checkRenderer);
		checkRenderer = ownerDraw ? OS.g_object_new (display.gtk_cell_renderer_toggle_get_type(), 0) : GTK.gtk_cell_renderer_toggle_new ();
		if (checkRenderer == 0) error (SWT.ERROR_NO_HANDLES);
		OS.g_object_ref (checkRenderer);
		display.addWidget (checkRenderer, this);
		OS.g_signal_connect_closure (checkRenderer, OS.toggled, display.getClosure (TOGGLED), false);
	}
	if (columnCount == 0) {
		createRenderers (GTK.gtk_tree_view_get_column (handle, 0), Table.FIRST_COLUMN, true, 0);
	} else {
		for (int i = 0; i < columnCount; i++) {
			TableColumn column = columns [i];
			createRenderers (column.handle, column.modelIndex, i == 0, column.style);
		}
	}
}

@Override
void redrawBackgroundImage () {
	Control control = findBackgroundControl ();
	if (control != null && control.backgroundImage != null) {
		redrawWidget (0, 0, 0, 0, true, false, false);
	}
}

@Override
void register () {
	super.register ();
	display.addWidget (GTK.gtk_tree_view_get_selection (handle), this);
	if (checkRenderer != 0) display.addWidget (checkRenderer, this);
	display.addWidget (modelHandle, this);
}

@Override
void releaseChildren (boolean destroy) {
	if (items != null) {
		for (int i=0; i<itemCount; i++) {
			TableItem item = items [i];
			if (item != null && !item.isDisposed ()) {
				item.release (false);
			}
		}
		items = null;
	}
	if (columns != null) {
		for (int i=0; i<columnCount; i++) {
			TableColumn column = columns [i];
			if (column != null && !column.isDisposed ()) {
				column.release (false);
			}
		}
		columns = null;
	}
	super.releaseChildren (destroy);
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	if (modelHandle != 0) OS.g_object_unref (modelHandle);
	modelHandle = 0;
	if (checkRenderer != 0) OS.g_object_unref (checkRenderer);
	checkRenderer = 0;
	if (imageList != null) imageList.dispose ();
	if (headerImageList != null) headerImageList.dispose ();
	imageList = headerImageList = null;
	currentItem = null;
}

/**
 * Removes the item from the receiver at the given
 * zero-relative index.
 *
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int index) {
	checkWidget();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_ITEM_NOT_REMOVED);
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	TableItem item = items [index];
	boolean disposed = false;
	if (item != null) {
		disposed = item.isDisposed ();
		if (!disposed) {
			C.memmove (iter, item.handle, GTK.GtkTreeIter_sizeof ());
			item.release (false);
		}
	} else {
		GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
	}
	if (!disposed) {
		long selection = GTK.gtk_tree_view_get_selection (handle);
		OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		GTK.gtk_list_store_remove (modelHandle, iter);
		OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		System.arraycopy (items, index + 1, items, index, --itemCount - index);
		items [itemCount] = null;
	}
	OS.g_free (iter);
}

/**
 * Removes the items from the receiver which are
 * between the given zero-relative start and end
 * indices (inclusive).
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if either the start or end are not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int start, int end) {
	checkWidget();
	if (start > end) return;
	if (!(0 <= start && start <= end && end < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (start == 0 && end == itemCount - 1) {
		removeAll();
		return;
	}
	checkSetDataInProcessBeforeRemoval(start, end + 1);
	long selection = GTK.gtk_tree_view_get_selection (handle);
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	if (iter == 0) error (SWT.ERROR_NO_HANDLES);
	int index = -1;
	for (index = start; index <= end; index++) {
		if (index == start) GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
		TableItem item = items [index];
		if (item != null && !item.isDisposed ()) item.release (false);
		OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
		GTK.gtk_list_store_remove (modelHandle, iter);
		OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	}
	OS.g_free (iter);
	index = end + 1;
	System.arraycopy (items, index, items, start, itemCount - index);
	for (int i=itemCount-(index-start); i<itemCount; i++) items [i] = null;
	itemCount = itemCount - (index - start);
}

/**
 * Removes the items from the receiver's list at the given
 * zero-relative indices.
 *
 * @param indices the array of indices of the items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 *    <li>ERROR_NULL_ARGUMENT - if the indices array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void remove (int [] indices) {
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (indices.length == 0) return;
	int [] newIndices = new int [indices.length];
	System.arraycopy (indices, 0, newIndices, 0, indices.length);
	sort (newIndices);
	int start = newIndices [newIndices.length - 1], end = newIndices [0];
	if (!(0 <= start && start <= end && end < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	long selection = GTK.gtk_tree_view_get_selection (handle);
	int last = -1;
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	if (iter == 0) error (SWT.ERROR_NO_HANDLES);
	for (int i=0; i<newIndices.length; i++) {
		int index = newIndices [i];
		if (index != last) {
			TableItem item = items [index];
			boolean disposed = false;
			if (item != null) {
				disposed = item.isDisposed ();
				if (!disposed) {
					C.memmove (iter, item.handle, GTK.GtkTreeIter_sizeof ());
					item.release (false);
				}
			} else {
				GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, 0, index);
			}
			if (!disposed) {
				OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				GTK.gtk_list_store_remove (modelHandle, iter);
				OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				System.arraycopy (items, index + 1, items, index, --itemCount - index);
				items [itemCount] = null;
			}
			last = index;
		}
	}
	OS.g_free (iter);
}

/**
 * Removes all of the items from the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget();
	checkSetDataInProcessBeforeRemoval(0, items.length);
	int index = itemCount - 1;
	while (index >= 0) {
		TableItem item = items [index];
		if (item != null && !item.isDisposed ()) item.release (false);
		--index;
	}
	items = new TableItem [4];
	itemCount = 0;
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	/*
	 * Bug 499850: On GTK3, calling gtk_list_store_clear with GtkSelectionMode GTK_SELECTION_MULTIPLE
	 * takes exponential time. Temporarily change the mode GTK_SELECTION_BROWSE before
	 * making the call to avoid performance hang.
	 */
	long selectionHandle = GTK.gtk_tree_view_get_selection(handle);
	boolean changeMode = (style & SWT.MULTI) != 0;
	if (changeMode) GTK.gtk_tree_selection_set_mode(selectionHandle, GTK.GTK_SELECTION_BROWSE);
	GTK.gtk_list_store_clear (modelHandle);
	if (changeMode) GTK.gtk_tree_selection_set_mode(selectionHandle, GTK.GTK_SELECTION_MULTIPLE);

	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);

	resetCustomDraw ();
	if (!searchEnabled ()) {
		GTK.gtk_tree_view_set_search_column (handle, -1);
	} else {
		/* Set the search column whenever the model changes */
		int firstColumn = columnCount == 0 ? FIRST_COLUMN : columns [0].modelIndex;
		GTK.gtk_tree_view_set_search_column (handle, firstColumn + CELL_TEXT);
	}
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the user changes the receiver's selection.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener(SelectionListener)
 */
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);
}

void sendMeasureEvent (long cell, long width, long height) {
	if (!ignoreSize && GTK.GTK_IS_CELL_RENDERER_TEXT (cell) && hooks (SWT.MeasureItem)) {
		long iter = OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX2);
		TableItem item = null;
		boolean isSelected = false;
		if (iter != 0) {
			long path = GTK.gtk_tree_model_get_path (modelHandle, iter);
			int [] buffer = new int [1];
			C.memmove (buffer, GTK.gtk_tree_path_get_indices (path), 4);
			int index = buffer [0];
			item = _getItem (index);
			long selection = GTK.gtk_tree_view_get_selection (handle);
			isSelected = GTK.gtk_tree_selection_path_is_selected (selection, path);
			GTK.gtk_tree_path_free (path);
		}
		if (item != null) {
			int columnIndex = 0;
			if (columnCount > 0) {
				long columnHandle = OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX1);
				for (int i = 0; i < columnCount; i++) {
					if (columns [i].handle == columnHandle) {
						columnIndex = i;
						break;
					}
				}
			}
			int [] contentWidth = new int [1], contentHeight = new int  [1];
			if (width != 0) C.memmove (contentWidth, width, 4);
			if (height != 0) C.memmove (contentHeight, height, 4);
			GTK.gtk_cell_renderer_get_preferred_height_for_width (cell, handle, contentWidth[0], contentHeight, null);
			Image image = item.getImage (columnIndex);
			int imageWidth = 0;
			if (image != null) {
				Rectangle bounds;
				if (DPIUtil.useCairoAutoScale()) {
					bounds = image.getBounds ();
				} else {
					bounds = image.getBoundsInPixels();
				}
				imageWidth = bounds.width;
			}
			contentWidth [0] += imageWidth;
			GC gc = new GC (this);
			gc.setFont (item.getFont (columnIndex));
			Event event = new Event ();
			event.item = item;
			event.index = columnIndex;
			event.gc = gc;
			Rectangle eventRect = new Rectangle (0, 0, contentWidth [0], contentHeight [0]);
			event.setBounds (DPIUtil.autoScaleDown (eventRect));
			if (isSelected) event.detail = SWT.SELECTED;
			sendEvent (SWT.MeasureItem, event);
			gc.dispose ();
			Rectangle rect = DPIUtil.autoScaleUp (event.getBounds ());
			contentWidth [0] = rect.width - imageWidth;
			if (contentHeight [0] < rect.height) contentHeight [0] = rect.height;
			if (width != 0) C.memmove (width, contentWidth, 4);
			if (height != 0) C.memmove (height, contentHeight, 4);
			GTK.gtk_cell_renderer_set_fixed_size (cell, -1, contentHeight [0]);
		}
	}
}

@Override
long rendererGetPreferredWidthProc (long cell, long handle, long minimun_size, long natural_size) {
	long g_class = OS.g_type_class_peek_parent (OS.G_OBJECT_GET_CLASS (cell));
	GtkCellRendererClass klass = new GtkCellRendererClass ();
	OS.memmove (klass, g_class);
	OS.call (klass.get_preferred_width, cell, handle, minimun_size, natural_size);
	sendMeasureEvent (cell, minimun_size, 0);
	return 0;
}

@Override
long rendererSnapshotProc (long cell, long snapshot, long widget, long background_area, long cell_area, long flags) {
	long rect = Graphene.graphene_rect_alloc();
	GdkRectangle gdkRectangle = new GdkRectangle ();
	OS.memmove(gdkRectangle, background_area, GdkRectangle.sizeof);
	Graphene.graphene_rect_init(rect, gdkRectangle.x, gdkRectangle.y, gdkRectangle.width, gdkRectangle.height);
	long cairo = GTK4.gtk_snapshot_append_cairo(snapshot, rect);
	rendererRender (cell, cairo, snapshot, widget, background_area, cell_area, 0, flags);
	return 0;
}

@Override
long rendererRenderProc (long cell, long cr, long widget, long background_area, long cell_area, long flags) {
	rendererRender (cell, cr, 0, widget, background_area, cell_area, 0, flags);
	return 0;
}

void rendererRender (long cell, long cr, long snapshot, long widget, long background_area, long cell_area, long expose_area, long flags) {
	TableItem item = null;
	boolean wasSelected = false;
	long iter = OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX2);
	if (iter != 0) {
		long path = GTK.gtk_tree_model_get_path (modelHandle, iter);
		int [] buffer = new int [1];
		C.memmove (buffer, GTK.gtk_tree_path_get_indices (path), 4);
		int index = buffer [0];
		item = _getItem (index);
		GTK.gtk_tree_path_free (path);
	}
	long columnHandle = OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX1);
	int columnIndex = 0;
	if (columnCount > 0) {
		for (int i = 0; i < columnCount; i++) {
			if (columns [i].handle == columnHandle) {
				columnIndex = i;
				break;
			}
		}
	}

	GdkRectangle rendererRect = new GdkRectangle ();
	GdkRectangle columnRect = new GdkRectangle ();
	int y_offset;
	{
		/*
		 * SWT creates multiple renderers (kind of sub-columns) per column.
		 * For example: one for checkbox, one for image, one for text.
		 * 'background_area' argument in this function is area of currently
		 * painted renderer. However, for SWT.EraseItem and SWT.PaintItem,
		 * SWT wants entire column's area along with the event. There's api
		 * 'gtk_tree_view_get_background_area()' but it calculates item's
		 * rect in control, which will have wrong Y if item is rendered
		 * separately (for example, for drag image).
		 * The workaround is to take X range from api and Y range from argument.
		 */
		OS.memmove (rendererRect, background_area, GdkRectangle.sizeof);

		long path = GTK.gtk_tree_model_get_path (modelHandle, iter);
		GTK.gtk_tree_view_get_background_area (handle, path, columnHandle, columnRect);
		GTK.gtk_tree_path_free (path);

		y_offset = columnRect.y - rendererRect.y;
		columnRect.y -= y_offset;
	}

	if (item != null) {
		if (GTK.GTK_IS_CELL_RENDERER_TOGGLE (cell) || (columnIndex != 0 || (style & SWT.CHECK) == 0)) {
			drawFlags = (int)flags;
			drawState = SWT.FOREGROUND;
			long [] ptr = new long [1];
			GTK.gtk_tree_model_get (modelHandle, item.handle, Table.BACKGROUND_COLUMN, ptr, -1);
			if (ptr [0] == 0) {
				int modelIndex = columnCount == 0 ? Table.FIRST_COLUMN : columns [columnIndex].modelIndex;
				GTK.gtk_tree_model_get (modelHandle, item.handle, modelIndex + Table.CELL_BACKGROUND, ptr, -1);
			}
			if (ptr [0] != 0) {
				drawState |= SWT.BACKGROUND;
				GDK.gdk_rgba_free (ptr [0]);
			}
			if ((flags & GTK.GTK_CELL_RENDERER_SELECTED) != 0) drawState |= SWT.SELECTED;
			if ((flags & GTK.GTK_CELL_RENDERER_SELECTED) == 0) {
				if ((flags & GTK.GTK_CELL_RENDERER_FOCUSED) != 0) drawState |= SWT.FOCUSED;
			}

			Rectangle rect = columnRect.toRectangle ();
			if ((drawState & SWT.SELECTED) == 0) {
				if ((state & PARENT_BACKGROUND) != 0 || backgroundImage != null) {
					Control control = findBackgroundControl ();
					if (control != null) {
						if (cr != 0) {
							Cairo.cairo_save (cr);
						}
						drawBackground (control, 0, cr, rect.x, rect.y, rect.width, rect.height);
						if (cr != 0) {
							Cairo.cairo_restore (cr);
						}
					}
				}
			}

			//send out measure before erase
			long textRenderer =  getTextRenderer (columnHandle);
			if (textRenderer != 0) gtk_cell_renderer_get_preferred_size (textRenderer, handle, null, null);


			if (hooks (SWT.EraseItem)) {
				Cairo.cairo_save(cr);
				/*
				 * Cache the selection state so that it is not lost if a
				 * PaintListener wants to draw custom selection foregrounds.
				 * See bug 528155.
				 */
				wasSelected = (drawState & SWT.SELECTED) != 0;
				if (wasSelected) {
					Control control = findBackgroundControl ();
					if (control == null) control = this;
				}
				GC gc = getGC(cr);
				if ((drawState & SWT.SELECTED) != 0) {
					gc.setBackground (display.getSystemColor (SWT.COLOR_LIST_SELECTION));
					gc.setForeground (display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT));
				} else {
					gc.setBackground (item.getBackground (columnIndex));
					gc.setForeground (item.getForeground (columnIndex));
				}
				gc.setFont (item.getFont (columnIndex));
				if ((style & SWT.MIRRORED) != 0) rect.x = getClientWidth () - rect.width - rect.x;
				if (cr != 0) {
					GdkRectangle r = new GdkRectangle();
					GDK.gdk_cairo_get_clip_rectangle(cr, r);
					Rectangle rect2 = DPIUtil.autoScaleDown(rect);
					// Caveat: rect2 is necessary because GC#setClipping(Rectangle) got broken by bug 446075
					gc.setClipping(rect2.x, rect2.y, rect2.width, rect2.height);
				} else {
					Rectangle rect2 = DPIUtil.autoScaleDown(rect);
					// Caveat: rect2 is necessary because GC#setClipping(Rectangle) got broken by bug 446075
					gc.setClipping(rect2.x, rect2.y, rect2.width, rect2.height);

				}

				// SWT.PaintItem/SWT.EraseItem often expect that event.y matches
				// what 'event.item.getBounds()' returns. The workaround is to
				// adjust coordinate system temporarily.
				Event event = new Event ();
				try {
					Rectangle eventRect = new Rectangle (rect.x, rect.y, rect.width, rect.height);

					eventRect.y += y_offset;
					Cairo.cairo_translate (cr, 0, -y_offset);

					event.item = item;
					event.index = columnIndex;
					event.gc = gc;
					event.detail = drawState;
					event.setBounds (DPIUtil.autoScaleDown (eventRect));
					sendEvent (SWT.EraseItem, event);
				} finally {
					Cairo.cairo_translate (cr, 0, y_offset);
				}

				drawForegroundRGBA = null;
				drawState = event.doit ? event.detail : 0;
				drawFlags &= ~(GTK.GTK_CELL_RENDERER_FOCUSED | GTK.GTK_CELL_RENDERER_SELECTED);
				if ((drawState & SWT.SELECTED) != 0) drawFlags |= GTK.GTK_CELL_RENDERER_SELECTED;
				if ((drawState & SWT.FOCUSED) != 0) drawFlags |= GTK.GTK_CELL_RENDERER_FOCUSED;
				if ((drawState & SWT.SELECTED) != 0) {
					Cairo.cairo_save (cr);
					Cairo.cairo_reset_clip (cr);
					long context = GTK.gtk_widget_get_style_context (widget);
					GTK.gtk_style_context_save (context);
					GTK.gtk_style_context_add_class (context, GTK.GTK_STYLE_CLASS_CELL);
					GTK.gtk_style_context_set_state (context, GTK.GTK_STATE_FLAG_SELECTED);
					GTK.gtk_render_background(context, cr, rect.x, rect.y, rect.width, rect.height);
					GTK.gtk_style_context_restore (context);
					Cairo.cairo_restore (cr);
				} else {
					if (wasSelected) {
						drawForegroundRGBA = gc.getForeground ().handle;
					}
				}
				gc.dispose();
				Cairo.cairo_restore(cr);
			}
		}
	}
	if ((drawState & SWT.BACKGROUND) != 0 && (drawState & SWT.SELECTED) == 0) {
		GC gc = getGC(cr);
		gc.setBackground (item.getBackground (columnIndex));
		gc.fillRectangle (DPIUtil.autoScaleDown (rendererRect.toRectangle ()));
		gc.dispose ();
	}
	if ((drawState & SWT.FOREGROUND) != 0 || GTK.GTK_IS_CELL_RENDERER_TOGGLE (cell)) {
		long g_class = OS.g_type_class_peek_parent (OS.G_OBJECT_GET_CLASS (cell));
		GtkCellRendererClass klass = new GtkCellRendererClass ();
		OS.memmove (klass, g_class);
		if (GTK.GTK_IS_CELL_RENDERER_TEXT (cell)) {
			/*
			 * SWT.FOREGROUND means the Table is responsible for painting the default foreground
			 * color. This can be either the system default (COLOR_LIST_FOREGROUND), or the
			 * color set by setForeground(). See bug 294300.
			 */
			GdkRGBA rgba = foreground != null ? foreground : display.getSystemColor(SWT.COLOR_LIST_FOREGROUND).handle;
			OS.g_object_set (cell, OS.foreground_rgba, rgba, 0);
		}
		if (GTK.GTK4) {
			OS.call (klass.snapshot, cell, snapshot, widget, background_area, cell_area, drawFlags);
		} else {
			OS.call (klass.render, cell, cr, widget, background_area, cell_area, drawFlags);
		}
	}
	if (item != null) {
		if (GTK.GTK_IS_CELL_RENDERER_TEXT (cell)) {
			if (hooks (SWT.PaintItem)) {
				if (wasSelected) drawState |= SWT.SELECTED;
				Rectangle rect = columnRect.toRectangle ();
				ignoreSize = true;
				int [] contentX = new int [1], contentWidth = new int [1];
				gtk_cell_renderer_get_preferred_size (cell, handle, contentWidth, null);
				gtk_tree_view_column_cell_get_position (columnHandle, cell, contentX, null);
				ignoreSize = false;
				Image image = item.getImage (columnIndex);
				int imageWidth = 0;
				if (image != null) {
					Rectangle bounds;
					if (DPIUtil.useCairoAutoScale()) {
						bounds = image.getBounds ();
					} else {
						bounds = image.getBoundsInPixels ();
					}
					imageWidth = bounds.width;
				}
				contentX [0] -= imageWidth;
				contentWidth [0] += imageWidth;
				GC gc = getGC(cr);
				if ((drawState & SWT.SELECTED) != 0) {
					Color background, foreground;
					if (GTK.gtk_widget_has_focus (handle)) {
						background = display.getSystemColor (SWT.COLOR_LIST_SELECTION);
						foreground = display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT);
					} else {
						/*
						 * Feature in GTK. When the widget doesn't have focus, then
						 * gtk_paint_flat_box () changes the background color state_type
						 * to GTK_STATE_ACTIVE. The fix is to use the same values in the GC.
						 */
						background = Color.gtk_new (display, display.COLOR_LIST_SELECTION_INACTIVE_RGBA);
						foreground = Color.gtk_new (display, display.COLOR_LIST_SELECTION_TEXT_INACTIVE_RGBA);
					}
					gc.setBackground (background);
					gc.setForeground (foreground);
				} else {
					gc.setBackground (item.getBackground (columnIndex));
					Color foreground = drawForegroundRGBA != null ? Color.gtk_new (display, drawForegroundRGBA) : item.getForeground (columnIndex);
					gc.setForeground (foreground);
				}
				gc.setFont (item.getFont (columnIndex));
				if ((style & SWT.MIRRORED) != 0) rect.x = getClientWidth () - rect.width - rect.x;

				Rectangle rect2 = DPIUtil.autoScaleDown(rect);
				// Caveat: rect2 is necessary because GC#setClipping(Rectangle) got broken by bug 446075
				gc.setClipping(rect2.x, rect2.y, rect2.width, rect2.height);

				// SWT.PaintItem/SWT.EraseItem often expect that event.y matches
				// what 'event.item.getBounds()' returns. The workaround is to
				// adjust coordinate system temporarily.
				Event event = new Event ();
				try {
					Rectangle eventRect = new Rectangle (rect.x + contentX [0], rect.y, contentWidth [0], rect.height);

					eventRect.y += y_offset;
					Cairo.cairo_translate (cr, 0, -y_offset);

					event.item = item;
					event.index = columnIndex;
					event.gc = gc;
					event.detail = drawState;
					event.setBounds (DPIUtil.autoScaleDown (eventRect));
					sendEvent (SWT.PaintItem, event);
				} finally {
					Cairo.cairo_translate (cr, 0, y_offset);
				}

				gc.dispose();
			}
		}
	}
}

private GC getGC(long cr) {
	GC gc;
	GCData gcData = new GCData();
	gcData.cairo = cr;
	gc = GC.gtk_new(this, gcData );
	return gc;
}

void resetCustomDraw () {
	if ((style & SWT.VIRTUAL) != 0 || ownerDraw) return;
	int end = Math.max (1, columnCount);
	for (int i=0; i<end; i++) {
		boolean customDraw = columnCount != 0 ? columns [i].customDraw : firstCustomDraw;
		if (customDraw) {
			long column = GTK.gtk_tree_view_get_column (handle, i);
			long textRenderer = getTextRenderer (column);
			GTK.gtk_tree_view_column_set_cell_data_func (column, textRenderer, 0, 0, 0);
			if (columnCount != 0) columns [i].customDraw = false;
		}
	}
	firstCustomDraw = false;
}

@Override
void reskinChildren (int flags) {
	if (items != null) {
		for (int i=0; i<itemCount; i++) {
			TableItem item = items [i];
			if (item != null) item.reskin (flags);
		}
	}
	if (columns != null) {
		for (int i=0; i<columnCount; i++) {
			TableColumn column = columns [i];
			if (!column.isDisposed ()) column.reskin (flags);
		}
	}
	super.reskinChildren (flags);
}

boolean searchEnabled () {
	/* Disable searching when using VIRTUAL */
	if ((style & SWT.VIRTUAL) != 0) return false;
	return true;
}

/**
 * Selects the item at the given zero-relative index in the receiver.
 * If the item at the index was already selected, it remains
 * selected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int index) {
	checkWidget();
	if (!(0 <= index && index < itemCount))  return;
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	TableItem item = _getItem (index);
	GTK.gtk_tree_selection_select_iter (selection, item.handle);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if (fixColumn) hideFirstColumn ();
}

/**
 * Selects the items in the range specified by the given zero-relative
 * indices in the receiver. The range of indices is inclusive.
 * The current selection is not cleared before the new items are selected.
 * <p>
 * If an item in the given range is not selected, it is selected.
 * If an item in the given range was already selected, it remains selected.
 * Indices that are out of range are ignored and no items will be selected
 * if start is greater than end.
 * If the receiver is single-select and there is more than one item in the
 * given range, then all indices are ignored.
 * </p>
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#setSelection(int,int)
 */
public void select (int start, int end) {
	checkWidget ();
	if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) return;
	if (itemCount == 0 || start >= itemCount) return;
	start = Math.max (0, start);
	end = Math.min (end, itemCount - 1);
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	for (int index=start; index<=end; index++) {
		TableItem item = _getItem (index);
		GTK.gtk_tree_selection_select_iter (selection, item.handle);
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if (fixColumn) hideFirstColumn ();
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * The current selection is not cleared before the new items are selected.
 * <p>
 * If the item at a given index is not selected, it is selected.
 * If the item at a given index was already selected, it remains selected.
 * Indices that are out of range and duplicate indices are ignored.
 * If the receiver is single-select and multiple indices are specified,
 * then all indices are ignored.
 * </p>
 *
 * @param indices the array of indices for the items to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#setSelection(int[])
 */
public void select (int [] indices) {
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = indices.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	for (int i=0; i<length; i++) {
		int index = indices [i];
		if (!(0 <= index && index < itemCount)) continue;
		TableItem item = _getItem (index);
		GTK.gtk_tree_selection_select_iter (selection, item.handle);
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if (fixColumn) hideFirstColumn ();
}

/**
 * Selects all of the items in the receiver.
 * <p>
 * If the receiver is single-select, do nothing.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void selectAll () {
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return;
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_tree_selection_select_all (selection);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if (fixColumn) hideFirstColumn ();
}

void selectFocusIndex (int index) {
	/*
	* Note that this method both selects and sets the focus to the
	* specified index, so any previous selection in the list will be lost.
	* gtk does not provide a way to just set focus to a specified list item.
	*/
	if (!(0 <= index && index < itemCount))  return;
	TableItem item = _getItem (index);
	long path = GTK.gtk_tree_model_get_path (modelHandle, item.handle);
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_tree_view_set_cursor (handle, path, 0, false);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_tree_path_free (path);
}

@Override
void setBackgroundGdkRGBA (long context, long handle, GdkRGBA rgba) {
	/* Setting the background color overrides the selected background color.
	 * To prevent this, we need to re-set the default. This can be done with CSS
	 * on GTK3.14+, or by using GtkStateFlags as an argument to
	 * gtk_widget_override_background_color() on versions of GTK3 less than 3.16.
	 */
	if (rgba == null) {
		background = defaultBackground();
	} else {
		background = rgba;
	}
	GdkRGBA selectedBackground = display.getSystemColor(SWT.COLOR_LIST_SELECTION).handle;
	String css = "treeview {background-color: " + display.gtk_rgba_to_css_string(background) + ";}\n"+
			 "treeview:selected {background-color: " + display.gtk_rgba_to_css_string(selectedBackground) + ";}";

	// Cache background color
	cssBackground = css;

	// Apply background color and any foreground color
	String finalCss = display.gtk_css_create_css_color_string (cssBackground, cssForeground, SWT.BACKGROUND);
	gtk_css_provider_load_from_css(context, finalCss);
}

@Override
void setBackgroundSurface (Image image) {
	ownerDraw = true;
	recreateRenderers ();
}

@Override
int setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	int result = super.setBounds (x, y, width, height, move, resize);
	if (result != 0) {
		boundsChangedSinceLastDraw = true;
	}
	/*
	* Bug on GTK.  The tree view sometimes does not get a paint
	* event or resizes to a one pixel square when resized in a new
	* shell that is not visible after any event loop has been run.  The
	* problem is intermittent. It doesn't seem to happen the first time
	* a new shell is created. The fix is to ensure the tree view is realized
	* after it has been resized.
	*/
	GTK.gtk_widget_realize (handle);
	return result;
}

/**
 * Sets the order that the items in the receiver should
 * be displayed in to the given argument which is described
 * in terms of the zero-relative ordering of when the items
 * were added.
 *
 * @param order the new order to display the items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item order is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item order is not the same length as the number of items</li>
 * </ul>
 *
 * @see Table#getColumnOrder()
 * @see TableColumn#getMoveable()
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 *
 * @since 3.1
 */
public void setColumnOrder (int [] order) {
	checkWidget ();
	if (order == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (columnCount == 0) {
		if (order.length > 0) error (SWT.ERROR_INVALID_ARGUMENT);
		return;
	}
	if (order.length != columnCount) error (SWT.ERROR_INVALID_ARGUMENT);
	boolean [] seen = new boolean [columnCount];
	for (int i = 0; i<order.length; i++) {
		int index = order [i];
		if (index < 0 || index >= columnCount) error (SWT.ERROR_INVALID_RANGE);
		if (seen [index]) error (SWT.ERROR_INVALID_ARGUMENT);
		seen [index] = true;
	}
	for (int i=0; i<order.length; i++) {
		long column = columns [order [i]].handle;
		long baseColumn = i == 0 ? 0 : columns [order [i-1]].handle;
		GTK.gtk_tree_view_move_column_after (handle, column, baseColumn);
	}
}

@Override
void setFontDescription (long font) {
	super.setFontDescription (font);
	TableColumn[] columns = getColumns ();
	for (int i = 0; i < columns.length; i++) {
		if (columns[i] != null) {
			columns[i].setFontDescription (font);
		}
	}
}

@Override
void setForegroundGdkRGBA (GdkRGBA rgba) {
	foreground = rgba;
	GdkRGBA toSet = rgba == null ? display.COLOR_LIST_FOREGROUND_RGBA : rgba;
	setForegroundGdkRGBA (handle, toSet);
}

/**
 * Sets the header background color to the color specified
 * by the argument, or to the default system color if the argument is null.
 * <p>
 * Note: This operation is a <em>HINT</em> and is not supported on all platforms. If
 * the native header has a 3D look and feel (e.g. Windows 7), this method
 * will cause the header to look FLAT irrespective of the state of the table style.
 * </p>
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.106
 */
public void setHeaderBackground(Color color) {
	checkWidget();
	if (color != null) {
		if (color.isDisposed())	error(SWT.ERROR_INVALID_ARGUMENT);
		if (color.equals(headerBackground)) return;
	}
	headerBackground = color;

	updateHeaderCSS();
}

void updateHeaderCSS() {
	StringBuilder css = new StringBuilder("button {");
	if (headerBackground != null) {
		/*
		 * Bug 571466: On some platforms & themes, the 'background-image'
		 * css tag also needs to be set in order to change the
		 * background color. Using 'background' tag as it overrides both
		 * 'background-image' and 'background-color'.
		 */
		css.append("background: " + display.gtk_rgba_to_css_string(headerBackground.handle) + "; ");
	}
	if (headerForeground != null) {
		css.append("color: " + display.gtk_rgba_to_css_string(headerForeground.handle) + "; ");
	}
	css.append("}\n");

	if (columnCount == 0) {
		long buttonHandle = GTK.gtk_tree_view_column_get_button(GTK.gtk_tree_view_get_column(handle, 0));
		if (headerCSSProvider == 0) {
			headerCSSProvider = GTK.gtk_css_provider_new();
			GTK.gtk_style_context_add_provider(GTK.gtk_widget_get_style_context(buttonHandle), headerCSSProvider, GTK.GTK_STYLE_PROVIDER_PRIORITY_APPLICATION);
		}

		if (GTK.GTK4) {
			GTK4.gtk_css_provider_load_from_data(headerCSSProvider, Converter.javaStringToCString(css.toString()), -1);
		} else {
			GTK3.gtk_css_provider_load_from_data(headerCSSProvider, Converter.javaStringToCString(css.toString()), -1, null);
		}
	} else {
		for (TableColumn column : columns) {
			if (column != null) {
				column.setHeaderCSS(css.toString());
			}
		}
	}
}

/**
 * Sets the header foreground color to the color specified
 * by the argument, or to the default system color if the argument is null.
 * <p>
 * Note: This operation is a <em>HINT</em> and is not supported on all platforms. If
 * the native header has a 3D look and feel (e.g. Windows 7), this method
 * will cause the header to look FLAT irrespective of the state of the table style.
 * </p>
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @since 3.106
 */
public void setHeaderForeground(Color color) {
	checkWidget();
	if (color != null) {
		if (color.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (color.equals(headerForeground)) return;
	}
	headerForeground = color;

	updateHeaderCSS();
}

/**
 * Marks the receiver's header as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param show the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHeaderVisible (boolean show) {
	checkWidget ();
	GTK.gtk_tree_view_set_headers_visible (handle, show);
	this.headerHeight = this.getHeaderHeight();
	this.headerVisible = show;
}

/**
 * Sets the number of items contained in the receiver.
 *
 * @param count the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public void setItemCount (int count) {
	checkWidget ();
	count = Math.max (0, count);
	if (count == itemCount) return;
	boolean isVirtual = (style & SWT.VIRTUAL) != 0;
	if (!isVirtual) setRedraw (false);
	remove (count, itemCount - 1);
	int length = Math.max (4, (count + 3) / 4 * 4);
	TableItem [] newItems = new TableItem [length];
	System.arraycopy (items, 0, newItems, 0, itemCount);
	items = newItems;
	if (isVirtual) {
		long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
		if (iter == 0) error (SWT.ERROR_NO_HANDLES);
		for (int i=itemCount; i<count; i++) {
			GTK.gtk_list_store_append (modelHandle, iter);
		}
		OS.g_free (iter);
		itemCount = count;
	} else {
		for (int i=itemCount; i<count; i++) {
			new TableItem (this, SWT.NONE, i, true);
		}
	}
	if (!isVirtual) setRedraw (true);
}

/**
 * Marks the receiver's lines as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. Note that some platforms draw grid lines
 * while others may draw alternating row colors.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param show the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLinesVisible (boolean show) {
	checkWidget();
	//Note: this is overriden by the active theme in GTK3.
	GTK.gtk_tree_view_set_grid_lines (handle, show ? GTK.GTK_TREE_VIEW_GRID_LINES_VERTICAL : GTK.GTK_TREE_VIEW_GRID_LINES_NONE);
}

void setModel (long newModel) {
	display.removeWidget (modelHandle);
	OS.g_object_unref (modelHandle);
	modelHandle = newModel;
	display.addWidget (modelHandle, this);
}

@Override
void setOrientation (boolean create) {
	super.setOrientation (create);
	for (int i=0; i<itemCount; i++) {
		if (items[i] != null) items[i].setOrientation (create);
	}
	for (int i=0; i<columnCount; i++) {
		if (columns[i] != null) columns[i].setOrientation (create);
	}
}

@Override
void setParentBackground () {
	ownerDraw = true;
	recreateRenderers ();
}

@Override
void setParentGdkResource (Control child) {
	/*
	 * Feature in GTK3: non-native GdkWindows are not drawn implicitly
	 * as of GTK3.10+. It is the client's responsibility to propagate draw
	 * events to these windows in the "draw" signal handler.
	 *
	 * This change breaks table editing on GTK3.10+, as the table editor
	 * widgets no longer receive draw signals. The fix is to connect the
	 * Table's fixedHandle to the draw signal, and propagate the draw
	 * signal using gtk_container_propagate_draw(). See bug 531928.
	 */
	if (GTK.GTK4) {
		// long parentGdkSurface = eventSurface ();
		// TODO: GTK4 no gtk_widget_set_parent_surface
		// GTK.gtk_widget_set_parent_surface (child.topHandle(), parentGdkSurface);
		// TODO: implement connectFixedHandleDraw with the "snapshot" signal
	} else {
		long parentGdkWindow = eventWindow ();
		GTK3.gtk_widget_set_parent_window (child.topHandle(), parentGdkWindow);
		hasChildren = true;
		connectFixedHandleDraw();
	}
}

@Override
public void setRedraw (boolean redraw) {
	checkWidget();
	super.setRedraw (redraw);
	if (redraw && drawCount == 0) {
		/* Resize the item array to match the item count */
		if (items.length > 4 && items.length - itemCount > 3) {
			int length = Math.max (4, (itemCount + 3) / 4 * 4);
			TableItem [] newItems = new TableItem [length];
			System.arraycopy (items, 0, newItems, 0, itemCount);
			items = newItems;
		}
	}
}

void setScrollWidth (long column, TableItem item) {
	if (columnCount != 0 || currentItem == item) return;
	int width = GTK.gtk_tree_view_column_get_fixed_width (column);
	int itemWidth = calculateWidth (column, item.handle);
	if (width < itemWidth) {
		GTK.gtk_tree_view_column_set_fixed_width (column, itemWidth);
	}
}

/**
 * Sets the column used by the sort indicator for the receiver. A null
 * value will clear the sort indicator.  The current sort column is cleared
 * before the new column is set.
 *
 * @param column the column used by the sort indicator or <code>null</code>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the column is disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setSortColumn (TableColumn column) {
	checkWidget ();
	if (column != null && column.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (sortColumn != null && !sortColumn.isDisposed()) {
		GTK.gtk_tree_view_column_set_sort_indicator (sortColumn.handle, false);
	}
	sortColumn = column;
	if (sortColumn != null && sortDirection != SWT.NONE) {
		GTK.gtk_tree_view_column_set_sort_indicator (sortColumn.handle, true);
		GTK.gtk_tree_view_column_set_sort_order (sortColumn.handle, sortDirection == SWT.DOWN ? 0 : 1);
	}
}

/**
 * Sets the direction of the sort indicator for the receiver. The value
 * can be one of <code>UP</code>, <code>DOWN</code> or <code>NONE</code>.
 *
 * @param direction the direction of the sort indicator
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setSortDirection  (int direction) {
	checkWidget ();
	if (direction != SWT.UP && direction != SWT.DOWN && direction != SWT.NONE) return;
	sortDirection = direction;
	if (sortColumn == null || sortColumn.isDisposed ()) return;
	if (sortDirection == SWT.NONE) {
		GTK.gtk_tree_view_column_set_sort_indicator (sortColumn.handle, false);
	} else {
		GTK.gtk_tree_view_column_set_sort_indicator (sortColumn.handle, true);
		GTK.gtk_tree_view_column_set_sort_order (sortColumn.handle, sortDirection == SWT.DOWN ? 0 : 1);
	}
}

/**
 * Selects the item at the given zero-relative index in the receiver.
 * The current selection is first cleared, then the new item is selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int)
 */
public void setSelection (int index) {
	checkWidget ();
	boolean fixColumn = showFirstColumn ();
	deselectAll ();
	selectFocusIndex (index);
	showSelection ();
	if (fixColumn) hideFirstColumn ();
}

/**
 * Selects the items in the range specified by the given zero-relative
 * indices in the receiver. The range of indices is inclusive.
 * The current selection is cleared before the new items are selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 * <p>
 * Indices that are out of range are ignored and no items will be selected
 * if start is greater than end.
 * If the receiver is single-select and there is more than one item in the
 * given range, then all indices are ignored.
 * </p>
 *
 * @param start the start index of the items to select
 * @param end the end index of the items to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int,int)
 */
public void setSelection (int start, int end) {
	checkWidget ();
	deselectAll();
	if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) return;
	if (itemCount == 0 || start >= itemCount) return;
	boolean fixColumn = showFirstColumn ();
	start = Math.max (0, start);
	end = Math.min (end, itemCount - 1);
	selectFocusIndex (start);
	if ((style & SWT.MULTI) != 0) {
		select (start, end);
	}
	showSelection ();
	if (fixColumn) hideFirstColumn ();
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * The current selection is cleared before the new items are selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 * <p>
 * Indices that are out of range and duplicate indices are ignored.
 * If the receiver is single-select and multiple indices are specified,
 * then all indices are ignored.
 * </p>
 *
 * @param indices the indices of the items to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int[])
 */
public void setSelection (int [] indices) {
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	deselectAll ();
	int length = indices.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	boolean fixColumn = showFirstColumn ();
	selectFocusIndex (indices [0]);
	if ((style & SWT.MULTI) != 0) {
		select (indices);
	}
	showSelection ();
	if (fixColumn) hideFirstColumn ();
}

/**
 * Sets the receiver's selection to the given item.
 * The current selection is cleared before the new item is selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 * <p>
 * If the item is not in the receiver, then it is ignored.
 * </p>
 *
 * @param item the item to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setSelection (TableItem item) {
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (new TableItem [] {item});
}


/**
 * Sets the receiver's selection to be the given array of items.
 * The current selection is cleared before the new items are selected,
 * and if necessary the receiver is scrolled to make the new selection visible.
 * <p>
 * Items that are not in the receiver are ignored.
 * If the receiver is single-select and multiple items are specified,
 * then all items are ignored.
 * </p>
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of items is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if one of the items has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int[])
 * @see Table#setSelection(int[])
 */
public void setSelection (TableItem [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	boolean fixColumn = showFirstColumn ();
	deselectAll ();
	int length = items.length;
	if (!(length == 0 || ((style & SWT.SINGLE) != 0 && length > 1))) {
		boolean first = true;
		for (int i = 0; i < length; i++) {
			int index = indexOf (items [i]);
			if (index != -1) {
				if (first) {
					first = false;
					selectFocusIndex (index);
				} else {
					select (index);
				}
			}
		}
		showSelection ();
	}
	if (fixColumn) hideFirstColumn ();
}

/**
 * Sets the zero-relative index of the item which is currently
 * at the top of the receiver. This index can change when items
 * are scrolled or new items are added and removed.
 *
 * @param index the index of the top item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTopIndex (int index) {
	checkWidget();
	if (!(0 <= index && index < itemCount)) return;
	/*
	 * Feature in GTK: cache the GtkAdjustment value for future use in
	 * getTopIndex(). Set topIndex to index.
	 */
	long vAdjustment;
	vAdjustment = GTK.gtk_scrollable_get_vadjustment(handle);
	cachedAdjustment = GTK.gtk_adjustment_get_value(vAdjustment);
	topIndex = index;
	long path = GTK.gtk_tree_model_get_path (modelHandle, _getItem (index).handle);
	GTK.gtk_tree_view_scroll_to_cell (handle, path, 0, true, 0f, 0f);
	GTK.gtk_tree_path_free (path);
}

/**
 * Shows the column.  If the column is already showing in the receiver,
 * this method simply returns.  Otherwise, the columns are scrolled until
 * the column is visible.
 *
 * @param column the column to be shown
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the column is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the column has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public void showColumn (TableColumn column) {
	checkWidget ();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (column.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (column.parent != this) return;

	GTK.gtk_tree_view_scroll_to_cell (handle, 0, column.handle, false, 0, 0);
}

boolean showFirstColumn () {
	/*
	* Bug in GTK.  If no columns are visible, changing the selection
	* will fail.  The fix is to temporarily make a column visible.
	*/
	int columnCount = Math.max (1, this.columnCount);
	for (int i=0; i<columnCount; i++) {
		long column = GTK.gtk_tree_view_get_column (handle, i);
		if (GTK.gtk_tree_view_column_get_visible (column)) return false;
	}
	long firstColumn = GTK.gtk_tree_view_get_column (handle, 0);
	GTK.gtk_tree_view_column_set_visible (firstColumn, true);
	return true;
}

/**
 * Shows the item.  If the item is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled until
 * the item is visible.
 *
 * @param item the item to be shown
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#showSelection()
 */
public void showItem (TableItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (item.parent != this) return;
	showItem (item.handle);
}

void showItem (long iter) {
	long path = GTK.gtk_tree_model_get_path (modelHandle, iter);

	GTK.gtk_tree_view_scroll_to_cell (handle, path, 0, false, 0, 0);
	GTK.gtk_tree_path_free (path);
}

/**
 * Shows the selection.  If the selection is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled until
 * the selection is visible.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#showItem(TableItem)
 */
public void showSelection () {
	checkWidget();
	TableItem [] selection = getSelection ();
	if (selection.length == 0) return;
	TableItem item = selection [0];
	showItem (item.handle);
}

@Override
void updateScrollBarValue (ScrollBar bar) {
	super.updateScrollBarValue (bar);

	if (!GTK.GTK4) {
		/*
		*  Bug in GTK. Scrolling changes the XWindow position
		* and makes the child widgets appear to scroll even
		* though when queried their position is unchanged.
		* The fix is to queue a resize event for each child to
		* force the position to be corrected.
		*/
		long parentHandle = parentingHandle ();
		long list = GTK3.gtk_container_get_children (parentHandle);
		if (list == 0) return;
		long temp = list;
		while (temp != 0) {
			long widget = OS.g_list_data (temp);
			if (widget != 0) GTK.gtk_widget_queue_resize  (widget);
			temp = OS.g_list_next (temp);
		}
		OS.g_list_free (list);
	}
}

@Override
long windowProc (long handle, long arg0, long user_data) {
	switch ((int)user_data) {
		case EXPOSE_EVENT: {
			/*
			 * If this Table has any child widgets, propagate the draw signal
			 * to them using gtk_container_propagate_draw(). See bug 531928.
			 */
			if (hasChildren) {
				/*
				 * If headers are visible, set noChildDrawing to true
				 * this will prevent any child widgets from drawing
				 * over the header buttons. See bug 535978.
				 */
				if (headerVisible) {
					noChildDrawing = true;
				}
				propagateDraw(handle, arg0);
			}
			break;
		}
		case EXPOSE_EVENT_INVERSE: {
			/*
			 * Feature in GTK. When the GtkTreeView has no items it does not propagate
			 * expose events. The fix is to fill the background in the inverse expose
			 * event.
			 */
			if (itemCount == 0 && (state & OBSCURED) == 0 && !GTK.GTK4) {
				if ((state & PARENT_BACKGROUND) != 0 || backgroundImage != null) {
					Control control = findBackgroundControl ();
					if (control != null) {
						long window = GTK3.gtk_tree_view_get_bin_window (handle);
						if (window == GTK3.gtk_widget_get_window(handle)) {
							GdkRectangle rect = new GdkRectangle ();
							GDK.gdk_cairo_get_clip_rectangle (arg0, rect);
							drawBackground (control, window, arg0, rect.x, rect.y, rect.width, rect.height);
						}
					}
				}
			}
			break;
		}
	}
	return super.windowProc (handle, arg0, user_data);
}

@Override
Point resizeCalculationsGTK3 (long widget, int width, int height) {
	Point sizes = super.resizeCalculationsGTK3(widget, width, height);
	/*
	 * Bug - Resizing Problems View can cause invalid rectangle errors on standard eror
	 *
	 * When resizing an SWT tree or table, its possible that the horizontal scrollbar overlaps with the column headers.
	 * This is possible due to SWT native resizing on the scrolled window of the tree or table.
	 * To avoid the error, we set a minimal size for the scrolled window.
	 * This height is equal to the header and scrollbar heights if both are visible,
	 * plus the total border height (bottom and top border height combined).
	 * In the error case, the SWT fixed which contains the table still resizes as expected,
	 * and the horizontal scrollbar is only partially visible so that it doesn't overlap with table headers.
	 */
	if (widget == scrolledHandle && getHeaderVisible()) {
		int hScrollBarHeight = hScrollBarWidth(); // this actually returns height
		if (hScrollBarHeight > 0) {
			sizes.y = Math.max(sizes.y, getHeaderHeight() + hScrollBarHeight + (getBorderWidth() * 2));
		}
	}
	return sizes;
}

/**
 * Check the table item range [start, end) for items that are in process of
 * sending {@code SWT#SetData} event. If such items exist, throw an exception.
 *
 * Does nothing if the given range contains no indices.
 *
 * @param start index of first item to check
 * @param end index after the last item to check
 */
void checkSetDataInProcessBeforeRemoval(int start, int end) {
	/*
	 * Bug 182598 - assertion failed in gtktreestore.c
	 *
	 * To prevent a crash in GTK, we ensure we are not setting data on the tree items we are about to remove.
	 * Removing an item while its data is being set will invalidate it, which will cause a crash.
	 *
	 * We therefore throw an exception to prevent the crash.
	 */
	for (int i = start; i < end; i++) {
		TableItem item = items[i];
		if (item != null && item.settingData) {
			String message = "Cannot remove a table item while its data is being set. "
					+ "At item " + i + " in range [" + start + ", " + end + ").";
			throw new SWTException(message);
		}
	}
}

@Override
public void dispose() {
	super.dispose();

	if (headerCSSProvider != 0) {
		OS.g_object_unref(headerCSSProvider);
		headerCSSProvider = 0;
	}
}
}
