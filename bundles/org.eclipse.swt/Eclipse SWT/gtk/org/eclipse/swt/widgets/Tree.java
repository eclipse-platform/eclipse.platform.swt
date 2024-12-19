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
 *******************************************************************************/
package org.eclipse.swt.widgets;


import java.util.*;
import java.util.List;
import java.util.stream.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.cairo.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk3.*;
import org.eclipse.swt.internal.gtk4.*;

/**
 * Instances of this class provide a selectable user interface object
 * that displays a hierarchy of items and issues notification when an
 * item in the hierarchy is selected.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TreeItem</code>.
 * </p><p>
 * Style <code>VIRTUAL</code> is used to create a <code>Tree</code> whose
 * <code>TreeItem</code>s are to be populated by the client on an on-demand basis
 * instead of up-front.  This can provide significant performance improvements for
 * trees that are very large or for which <code>TreeItem</code> population is
 * expensive (for example, retrieving values from an external source).
 * </p><p>
 * Here is an example of using a <code>Tree</code> with style <code>VIRTUAL</code>:</p>
 * <pre><code>
 *  final Tree tree = new Tree(parent, SWT.VIRTUAL | SWT.BORDER);
 *  tree.setItemCount(20);
 *  tree.addListener(SWT.SetData, new Listener() {
 *      public void handleEvent(Event event) {
 *          TreeItem item = (TreeItem)event.item;
 *          TreeItem parentItem = item.getParentItem();
 *          String text = null;
 *          if (parentItem == null) {
 *              text = "node " + tree.indexOf(item);
 *          } else {
 *              text = parentItem.getText() + " - " + parentItem.indexOf(item);
 *          }
 *          item.setText(text);
 *          System.out.println(text);
 *          item.setItemCount(10);
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
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, VIRTUAL, NO_SCROLL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, Collapse, Expand, SetData, MeasureItem, EraseItem, PaintItem, EmptinessChanged</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles SINGLE and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tree">Tree, TreeItem, TreeColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Tree extends Composite {
	long modelHandle, checkRenderer;
	int columnCount, sortDirection;
	int selectionCountOnPress,selectionCountOnRelease;
	long ignoreCell;
	final List<TreeItem> roots = new ArrayList<> ();
	int nextId;
	TreeColumn [] columns;
	TreeColumn sortColumn;
	TreeItem currentItem;
	ImageList imageList, headerImageList;
	boolean firstCustomDraw;
	/** True iff computeSize has never been called on this Tree */
	boolean firstCompute = true;
	boolean modelChanged;
	boolean expandAll;
	int drawState, drawFlags;
	GdkRGBA background, foreground, drawForegroundRGBA;
	/** The owner of the widget is responsible for drawing */
	boolean isOwnerDrawn;
	boolean ignoreSize, pixbufSizeSet, hasChildren;
	int pixbufHeight, pixbufWidth, headerHeight;
	boolean headerVisible;
	TreeItem topItem;
	double cachedAdjustment, currentAdjustment;
	Color headerBackground, headerForeground;
	boolean boundsChangedSinceLastDraw, wasScrolled;
	boolean rowActivated;

	private long headerCSSProvider;

	static final int ID_COLUMN = 0;
	static final int CHECKED_COLUMN = 1;
	static final int GRAYED_COLUMN = 2;
	static final int FOREGROUND_COLUMN = 3;
	static final int BACKGROUND_COLUMN = 4;
	static final int FONT_COLUMN = 5;
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
 * @see SWT#VIRTUAL
 * @see SWT#NO_SCROLL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Tree (Composite parent, int style) {
	super (parent, checkStyle (style));
}

@Override
void _addListener (int eventType, Listener listener) {
	super._addListener (eventType, listener);
	if (!isOwnerDrawn) {
		switch (eventType) {
			case SWT.MeasureItem:
			case SWT.EraseItem:
			case SWT.PaintItem:
				isOwnerDrawn = true;
				recreateRenderers ();
				break;
		}
	}
}

TreeItem _getItemByIter (long iter) {
	if (iter == 0) return null;
	long path = GTK.gtk_tree_model_get_path (modelHandle, iter);
	if (path == 0) error (SWT.ERROR_NO_HANDLES);
	try {
		return _getItemByPath (path);
	} finally {
		GTK.gtk_tree_path_free (path);
	}
}

TreeItem _getItemByPath (long path) {
	if (path == 0) return null;
	int depth;
	int [] indices;
	depth = GTK.gtk_tree_path_get_depth (path);
	indices = new int [depth];
	C.memmove (indices, GTK.gtk_tree_path_get_indices (path), 4*depth);
	TreeItem item = depth > 0 ? getItem (indices [0]) : null;
	for (int i = 1; i < depth; i++) {
		item = item.getItem (indices [i]);
	}
	return item;
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
	TreeItem item = _getItemByIter (iter);
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
		modelIndex = Tree.FIRST_COLUMN;
		customDraw = firstCustomDraw;
	} else {
		TreeColumn column = (TreeColumn) display.getWidget (tree_column);
		if (column != null) {
			modelIndex = column.modelIndex;
			customDraw = column.customDraw;
		}
	}
	if (modelIndex == -1) return 0;
	boolean setData = false;
	boolean updated = false;
	if ((style & SWT.VIRTUAL) != 0) {
		if (!item.cached) {
			//lastIndexOf = index [0];
			setData = checkData (item);
		}
		if (item.updated) {
			updated = true;
			item.updated = false;
		}
	}
	long [] ptr = new long [1];
	if (setData) {
		if (isPixbuf) {
			ptr [0] = 0;
			GTK.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_PIXBUF, ptr, -1);
			OS.g_object_set (cell, OS.gicon, ptr [0], 0);
			if (ptr [0] != 0) OS.g_object_unref (ptr [0]);
		} else {
			ptr [0] = 0;
			GTK.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_TEXT, ptr, -1);
			if (ptr [0] != 0) {
				OS.g_object_set (cell, OS.text, ptr[0], 0);
				OS.g_free (ptr[0]);
			}
		}
	}
	if (customDraw) {
		if (!isOwnerDrawn) {
			ptr [0] = 0;
			GTK.gtk_tree_model_get (tree_model, iter, modelIndex + CELL_BACKGROUND, ptr, -1);
			if (ptr [0] != 0) {
				OS.g_object_set (cell, OS.cell_background_rgba, ptr[0], 0);
				GDK.gdk_rgba_free(ptr [0]);
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
				OS.g_object_set (cell, OS.font_desc, ptr[0], 0);
				OS.pango_font_description_free (ptr [0]);
			}
		}
	}
	if (setData || updated) {
		ignoreCell = cell;
		setScrollWidth (tree_column, item);
		ignoreCell = 0;
	}
	return 0;
}

boolean checkData (TreeItem item) {
	if (item.cached) return true;
	if ((style & SWT.VIRTUAL) != 0) {
		item.cached = true;
		TreeItem parentItem = item.getParentItem ();
		Event event = new Event ();
		event.item = item;
		event.index = parentItem == null ? indexOf (item) : parentItem.indexOf (item);
		int mask = OS.G_SIGNAL_MATCH_DATA | OS.G_SIGNAL_MATCH_ID;
		int signal_id = OS.g_signal_lookup (OS.row_changed, GTK.gtk_tree_model_get_type ());
		OS.g_signal_handlers_block_matched (modelHandle, mask, signal_id, 0, 0, 0, handle);
		currentItem = item;
		item.settingData = true;
		sendEvent (SWT.SetData, event);
		item.settingData = false;
		currentItem = null;
		//widget could be disposed at this point
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
	addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when an item in the receiver is expanded or collapsed
 * by sending it one of the messages defined in the <code>TreeListener</code>
 * interface.
 *
 * @param listener the listener which should be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see TreeListener
 * @see #removeTreeListener
 */
public void addTreeListener(TreeListener listener) {
	addTypedListener(listener, SWT.Expand, SWT.Collapse);
}

int calculateWidth (long column, long iter, boolean recurse) {
	GTK.gtk_tree_view_column_cell_set_cell_data (column, modelHandle, iter, false, false);
	/*
	* Bug in GTK.  The width calculated by gtk_tree_view_column_cell_get_size()
	* always grows in size regardless of the text or images in the table.
	* The fix is to determine the column width from the cell renderers.
	*/
	// Code intentionally commented
	//int [] width = new int [1];
	//OS.gtk_tree_view_column_cell_get_size (column, null, null, null, width, null);
	//return width [0];

	int width = 0;
	int [] w = new int [1];
	long path = 0;

	/*
	 * gtk_tree_view_get_expander_column() returns 0 if the expander column is not visible.
	 * When pack is called for the first time, the expander arrow indent is not added to
	 * the width for the expander column. The fix is to always get the expander column as if
	 * it is visible.
	 */
	long expander_column = GTK.gtk_tree_view_get_expander_column(handle);
	if (expander_column == 0 && !GTK.gtk_tree_view_column_get_visible(column)) {
		GTK.gtk_tree_view_column_set_visible(column, true);
		expander_column = GTK.gtk_tree_view_get_expander_column(handle);
		GTK.gtk_tree_view_column_set_visible(column, false);
	}
	if (expander_column == column) {
		/* indent */
		GdkRectangle rect = new GdkRectangle ();
		GTK.gtk_widget_realize (handle);
		path = GTK.gtk_tree_model_get_path (modelHandle, iter);
		GTK.gtk_tree_view_get_cell_area (handle, path, column, rect);
		width += rect.x;
		/* expander */
		if (!GTK.gtk_tree_view_column_get_visible(column)) {
			if (GTK.GTK4) {
				long image = GTK4.gtk_image_new_from_icon_name(GTK.GTK_NAMED_ICON_PAN_DOWN);
				GtkRequisition requisition = new GtkRequisition ();
				GTK.gtk_widget_get_preferred_size(image, requisition, null);
				width += requisition.width + TreeItem.EXPANDER_EXTRA_PADDING;
			} else {
				GTK3.gtk_widget_style_get (handle, OS.expander_size, w, 0);
				width += w [0] + TreeItem.EXPANDER_EXTRA_PADDING;
			}
		}
	}
	/*
	 * Focus line width is done via CSS in GTK4, and does not contribute
	 * to the size of the widget.
	 */
	if (!GTK.GTK4) {
		GTK3.gtk_widget_style_get(handle, OS.focus_line_width, w, 0);
		width += 2 * w [0];
	}
	long list = GTK.gtk_cell_layout_get_cells(column);
	if (list == 0) return 0;
	long temp = list;
	while (temp != 0) {
		long renderer = OS.g_list_data (temp);
		if (renderer != 0) {
			gtk_cell_renderer_get_preferred_size (renderer, handle, w, null);
			width += w [0];
		}
		temp = OS.g_list_next (temp);
	}
	OS.g_list_free (list);

	if (recurse) {
		if (path == 0) path = GTK.gtk_tree_model_get_path (modelHandle, iter);
		boolean expanded = GTK.gtk_tree_view_row_expanded (handle, path);
		if (expanded) {
			long childIter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
			boolean valid = GTK.gtk_tree_model_iter_children (modelHandle, childIter, iter);
			while (valid) {
				width = Math.max (width, calculateWidth (column, childIter, true));
				valid = GTK.gtk_tree_model_iter_next (modelHandle, childIter);
			}
			OS.g_free (childIter);
		}
	}

	if (path != 0) GTK.gtk_tree_path_free (path);
	if (GTK.gtk_tree_view_get_grid_lines(handle) > GTK.GTK_TREE_VIEW_GRID_LINES_NONE) {
		/*
		 * Grid line width is handled via CSS in GTK4.
		 */
		if (!GTK.GTK4) {
			GTK3.gtk_widget_style_get (handle, OS.grid_line_width, w, 0) ;
			width += 2 * w [0];
		}
	}
	return width;
}

/**
 * Clears the item at the given zero-relative index in the receiver.
 * The text, icon and other attributes of the item are set to the default
 * value.  If the tree was created with the <code>SWT.VIRTUAL</code> style,
 * these attributes are requested again as needed.
 *
 * @param index the index of the item to clear
 * @param all <code>true</code> if all child items of the indexed item should be
 * cleared recursively, and <code>false</code> otherwise
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
 * @since 3.2
 */
public void clear(int index, boolean all) {
	checkWidget ();
	TreeItem item = roots.get (index);
	if (item == null) return;
	item.clear();
	if (all) item.clearAll (all);
}


/**
 * Clears all the items in the receiver. The text, icon and other
 * attributes of the items are set to their default values. If the
 * tree was created with the <code>SWT.VIRTUAL</code> style, these
 * attributes are requested again as needed.
 *
 * @param all <code>true</code> if all child items should be cleared
 * recursively, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SWT#VIRTUAL
 * @see SWT#SetData
 *
 * @since 3.2
 */
public void clearAll (boolean all) {
	checkWidget ();
	for (TreeItem item: roots) {
		if (item != null) {
			item.clear ();
			if (all) item.clearAll (all);
		}
	}
}

@Override
Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();
	if (wHint != SWT.DEFAULT && wHint < 0) wHint = 0;
	if (hHint != SWT.DEFAULT && hHint < 0) hHint = 0;
	/*
	 * Bug 546490: Set all the TreeColumn buttons visible otherwise
	 * gtk_widget_get_preferred_size() will not take their size
	 * into account.
	 */
	if (!GTK.GTK4) {
		if (firstCompute) {
			for (TreeColumn column : columns) {
				if (column != null) GTK.gtk_widget_set_visible(column.buttonHandle, true);
			}
			firstCompute = false;
		}
	}

	GTK.gtk_widget_realize(handle);
	Point size = computeNativeSize (handle, wHint, hHint, changed);

	/*
	 * In GTK 3, computeNativeSize(..) sometimes just returns the header
	 * height as height. In that case, calculate the tree height based on
	 * the number of items at the root of the tree.
	 */
	if (hHint == SWT.DEFAULT && size.y == getHeaderHeight()) {
		int itemHeight = getItemHeightInPixels();

		// Initialize to height of root items & header
		size.y = getItemCount() * itemHeight + getHeaderHeight();

		getKnownItemsRecursively ().forEach (item -> {
			if (item != null && item.isExpanded) {
				size.y += item.getItemCount () * itemHeight;
			}
		});
	}

	Rectangle trim = computeTrimInPixels (0, 0, size.x, size.y);
	size.x = trim.width;
	/*
	 * Feature in GTK: sometimes GtkScrolledWindow's with no scrollbars
	 * won't automatically adjust their size. This happens when a Tree
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

void copyModel (long oldModel, int oldStart, long newModel, int newStart, TreeItem parentItem, long newParent, int modelLength) {
	long iter = OS.g_malloc(GTK.GtkTreeIter_sizeof ());
	long value = OS.g_malloc (OS.GValue_sizeof ());
	// GValue needs to be initialized with G_VALUE_INIT, which is zeroes
	C.memset (value, 0, OS.GValue_sizeof ());

	long oldParent = parentItem != null ? parentItem.handle : 0;
	if (GTK.gtk_tree_model_iter_children (oldModel, iter, oldParent))  {
		long [] oldItems = new long [GTK.gtk_tree_model_iter_n_children (oldModel, oldParent)];
		int oldIndex = 0;
		do {
			long newIterator = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
			if (newIterator == 0) error (SWT.ERROR_NO_HANDLES);
			GTK.gtk_tree_store_append (newModel, newIterator, newParent);
			TreeItem item = parentItem != null ? parentItem._peekItem (oldIndex) : roots.get (oldIndex);
			if (item != null) {
				long oldIterator = item.handle;
				oldItems[oldIndex++] = oldIterator;

				// Copy header fields
				for (int iColumn = 0; iColumn < FIRST_COLUMN; iColumn++) {
					GTK.gtk_tree_model_get_value (oldModel, oldIterator, iColumn, value);
					GTK.gtk_tree_store_set_value (newModel, newIterator, iColumn, value);
					OS.g_value_unset (value);
				}

				// Copy requested columns
				for (int iOffset = 0; iOffset < modelLength - FIRST_COLUMN; iOffset++) {
					GTK.gtk_tree_model_get_value (oldModel, oldIterator, oldStart + iOffset, value);
					GTK.gtk_tree_store_set_value (newModel, newIterator, newStart + iOffset, value);
					OS.g_value_unset (value);
				}
			}
			// recurse through children
			copyModel (oldModel, oldStart, newModel, newStart, item, newIterator, modelLength);

			if (item!= null) {
				item.handle = newIterator;
			} else {
				OS.g_free (newIterator);
			}
		} while (GTK.gtk_tree_model_iter_next(oldModel, iter));
		for (int i = 0; i < oldItems.length; i++) {
			long oldItem = oldItems [i];
			if (oldItem != 0) {
				GTK.gtk_tree_store_remove (oldModel, oldItem);
				OS.g_free (oldItem);
			}
		}
	}

	OS.g_free (value);
	OS.g_free (iter);
}

void createColumn (TreeColumn column, int index) {
/*
* Bug in ATK. For some reason, ATK segments fault if
* the GtkTreeView has a column and does not have items.
* The fix is to insert the column only when an item is
* created.
*/

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
			long newModel = GTK.gtk_tree_store_newv (types.length, types);
			if (newModel == 0) error (SWT.ERROR_NO_HANDLES);
			copyModel (oldModel, FIRST_COLUMN, newModel, FIRST_COLUMN, null, (long )0, modelLength);
			GTK.gtk_tree_view_set_model (handle, newModel);
			setModel (newModel);
		}
	}
	long columnHandle = GTK.gtk_tree_view_column_new ();
	if (columnHandle == 0) error (SWT.ERROR_NO_HANDLES);
	if (index == 0 && columnCount > 0) {
		TreeColumn checkColumn = columns [0];
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
	modelHandle = GTK.gtk_tree_store_newv (types.length, types);
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
		GTK3.gtk_container_add (fixedHandle, scrolledHandle);
		GTK3.gtk_container_add (scrolledHandle, handle);
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

	if (GTK.GTK4) bindArrowKeyBindings();
}

/**
 * Binds the left and right arrow keys to
 * allow for expanding and collapsing of the
 * tree nodes.
 *
 * Note: This function is to only be called in GTK4.
 * Binding of the arrow keys are also done in GTK3,
 * however it is done through GtkBindingSets in CSS.
 * See Device.init() for more information, specifically,
 * swt_functional_gtk_3_20.css
 */
void bindArrowKeyBindings() {
	if (!GTK.GTK4) return;

	int[] keyval = new int[1];
	GTK.gtk_accelerator_parse(Converter.javaStringToCString("Left"), keyval, null);
	GTK4.gtk_widget_class_add_binding_signal(GTK.GTK_WIDGET_GET_CLASS(handle), keyval[0], 0,
			Converter.javaStringToCString("expand-collapse-cursor-row"),
			Converter.javaStringToCString("(bbb)"),
			false, false, false);

	GTK.gtk_accelerator_parse(Converter.javaStringToCString("Right"), keyval, null);
	GTK4.gtk_widget_class_add_binding_signal(GTK.GTK_WIDGET_GET_CLASS(handle), keyval[0], 0,
			Converter.javaStringToCString("expand-collapse-cursor-row"),
			Converter.javaStringToCString("(bbb)"),
			false, true, false);
}

@Override
int applyThemeBackground () {
	return -1; /* No Change */
}

void createItem (TreeColumn column, int index) {
	if (!(0 <= index && index <= columnCount)) error (SWT.ERROR_INVALID_RANGE);
	if (index == 0) {
		// first column must be left aligned
		column.style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
		column.style |= SWT.LEFT;
	}
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
	long boxHandle = gtk_box_new (GTK.GTK_ORIENTATION_HORIZONTAL, false, 3);
	if (boxHandle == 0) error (SWT.ERROR_NO_HANDLES);
	long labelHandle = GTK.gtk_label_new_with_mnemonic (null);
	if (labelHandle == 0) error (SWT.ERROR_NO_HANDLES);
	long imageHandle = GTK.gtk_image_new ();
	if (imageHandle == 0) error (SWT.ERROR_NO_HANDLES);

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
	GTK.gtk_tree_view_column_set_widget (column.handle, boxHandle);
	column.buttonHandle = GTK.gtk_tree_view_column_get_button(column.handle);
	GTK.gtk_widget_set_focus_on_click(column.buttonHandle, false);
	if (columnCount == columns.length) {
		TreeColumn [] newColumns = new TreeColumn [columns.length + 4];
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
		getKnownItemsRecursively ().forEach (item -> {
			if (item != null) {
				Font [] cellFont = item.cellFont;
				if (cellFont != null) {
					Font [] temp = new Font [columnCount];
					System.arraycopy (cellFont, 0, temp, 0, index);
					System.arraycopy (cellFont, index, temp, index+1, columnCount-index-1);
					item.cellFont = temp;
				}
				String [] strings = item.strings;
				if (strings != null) {
					String [] temp = new String [columnCount];
					System.arraycopy (strings, 0, temp, 0, index);
					System.arraycopy (strings, index, temp, index+1, columnCount-index-1);
					temp [index] = "";
					item.strings = temp;
				}

			}
		});
	}

	updateHeaderCSS();
}

void itemCreated (TreeItem item) {
	modelChanged = true;

	if (item.getParentItem () == null) {
		/*
		 If this was the first root item fire an EmptinessChanged event.
		 */

		if (roots.size () == 1) {
			Event event = new Event ();
			event.detail = 0;
			sendEvent (SWT.EmptinessChanged, event);
		}
	}
}

void createRenderers (long columnHandle, int modelIndex, boolean check, int columnStyle) {
	GTK.gtk_tree_view_column_clear (columnHandle);
	if ((style & SWT.CHECK) != 0 && check) {
		GTK.gtk_tree_view_column_pack_start (columnHandle, checkRenderer, false);
		GTK.gtk_tree_view_column_add_attribute (columnHandle, checkRenderer, OS.active, CHECKED_COLUMN);
		GTK.gtk_tree_view_column_add_attribute (columnHandle, checkRenderer, OS.inconsistent, GRAYED_COLUMN);
		if (!isOwnerDrawn) GTK.gtk_tree_view_column_add_attribute (columnHandle, checkRenderer, OS.cell_background_rgba, BACKGROUND_COLUMN);
		if (isOwnerDrawn) {
			GTK.gtk_tree_view_column_set_cell_data_func (columnHandle, checkRenderer, display.cellDataProc, handle, 0);
			OS.g_object_set_qdata (checkRenderer, Display.SWT_OBJECT_INDEX1, columnHandle);
		}
	}

	long pixbufRenderer;
	if (GTK.GTK4) {
		pixbufRenderer = GTK.gtk_cell_renderer_pixbuf_new();
	} else {
		pixbufRenderer = isOwnerDrawn ? OS.g_object_new (display.gtk_cell_renderer_pixbuf_get_type (), 0) : GTK.gtk_cell_renderer_pixbuf_new ();
	}

	if (pixbufRenderer == 0) {
		error (SWT.ERROR_NO_HANDLES);
	} else {
		// set default size this size is used for calculating the icon and text positions in a tree
		if ((!isOwnerDrawn)) {
			/*
			 * When SWT.VIRTUAL is specified, size the pixbuf renderer
			 * according to the size of the first image set. If no image
			 * is set, specify a size of 0x0 like for all other Tree
			 * styles. Fix for bug 480261.
			 */
			if ((style & SWT.VIRTUAL) != 0 && pixbufSizeSet)  {
				GTK.gtk_cell_renderer_set_fixed_size(pixbufRenderer, pixbufHeight, pixbufWidth);
			} else {
				/*
				 * For all other styles, set render size to 0x0 until we
				 * actually add images, fix for bugs 469277 & 476419.
				 */
				GTK.gtk_cell_renderer_set_fixed_size(pixbufRenderer, 0, 0);
			}
		}
	}
	long textRenderer = isOwnerDrawn ? OS.g_object_new (display.gtk_cell_renderer_text_get_type (), 0) : GTK.gtk_cell_renderer_text_new ();
	if (textRenderer == 0) error (SWT.ERROR_NO_HANDLES);

	if (isOwnerDrawn) {
		OS.g_object_set_qdata (pixbufRenderer, Display.SWT_OBJECT_INDEX1, columnHandle);
		OS.g_object_set_qdata (textRenderer, Display.SWT_OBJECT_INDEX1, columnHandle);
	}

	/*
	* Feature in GTK.  When a tree view column contains only one activatable
	* cell renderer such as a toggle renderer, mouse clicks anywhere in a cell
	* activate that renderer. The workaround is to set a second  cell renderer
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

	/*
	 * Add attributes
	 *
	 * Formerly OS.gicon was set if on GTK3, but this is being removed do to spacing issues in Tables/Trees with
	 * no images. Fix for Bug 469277 & 476419. NOTE: this change has been ported to Tables since Tables/Trees both
	 * use the same underlying GTK structure.
	 */
	GTK.gtk_tree_view_column_add_attribute (columnHandle, pixbufRenderer, OS.pixbuf, modelIndex + CELL_PIXBUF);
	if (!isOwnerDrawn) {
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
	if ((style & SWT.VIRTUAL) != 0 || customDraw || isOwnerDrawn) {
		GTK.gtk_tree_view_column_set_cell_data_func (columnHandle, textRenderer, display.cellDataProc, handle, 0);
		GTK.gtk_tree_view_column_set_cell_data_func (columnHandle, pixbufRenderer, display.cellDataProc, handle, 0);
	}
}

@Override
void createWidget (int index) {
	super.createWidget (index);
	columns = new TreeColumn [4];
	columnCount = 0;
	// In GTK 3 font description is inherited from parent widget which is not how SWT has always worked,
	// reset to default font to get the usual behavior
	setFontDescription(defaultFont().handle);
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
 * Deselects an item in the receiver.  If the item was already
 * deselected, it remains deselected.
 *
 * @param item the item to be deselected
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
 * @since 3.4
 */
public void deselect (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_tree_selection_unselect_iter (selection, item.handle);
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
public void deselectAll() {
	checkWidget();
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_tree_selection_unselect_all (selection);
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if (fixColumn) hideFirstColumn ();
}

void destroyItem (TreeColumn column) {
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
		long newModel = GTK.gtk_tree_store_newv (types.length, types);
		if (newModel == 0) error (SWT.ERROR_NO_HANDLES);
		copyModel (oldModel, column.modelIndex, newModel, FIRST_COLUMN, null, (long )0, FIRST_COLUMN + CELL_TYPES);
		GTK.gtk_tree_view_set_model (handle, newModel);
		setModel (newModel);
		createColumn (null, 0);
	} else {
		int indexCopy = index;
		getKnownItemsRecursively ().forEach (item -> {
			long iter = item.handle;
			int modelIndex = column.modelIndex;
			GTK.gtk_tree_store_set (modelHandle, iter, modelIndex + CELL_PIXBUF, (long )0, -1);
			GTK.gtk_tree_store_set (modelHandle, iter, modelIndex + CELL_TEXT, (long )0, -1);
			GTK.gtk_tree_store_set (modelHandle, iter, modelIndex + CELL_FOREGROUND, (long )0, -1);
			GTK.gtk_tree_store_set (modelHandle, iter, modelIndex + CELL_BACKGROUND, (long )0, -1);
			GTK.gtk_tree_store_set (modelHandle, iter, modelIndex + CELL_FONT, (long )0, -1);

			Font [] cellFont = item.cellFont;
			if (cellFont != null) {
				if (columnCount == 0) {
					item.cellFont = null;
				} else {
					Font [] temp = new Font [columnCount];
					System.arraycopy (cellFont, 0, temp, 0, indexCopy);
					System.arraycopy (cellFont, indexCopy + 1, temp, indexCopy, columnCount - indexCopy);
					item.cellFont = temp;
				}
			}
		});
		if (index == 0) {
			// first column must be left aligned and must show check box
			TreeColumn firstColumn = columns [0];
			firstColumn.style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
			firstColumn.style |= SWT.LEFT;
			createRenderers (firstColumn.handle, firstColumn.modelIndex, true, firstColumn.style);
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


void itemDestroyed (TreeItem item) {
	modelChanged = true;
	/*
	 If this was the last root item fire an EmptinessChanged event.
	 */
	if (roots.isEmpty ()) {
		Event event = new Event ();
		event.detail = 1;
		sendEvent (SWT.EmptinessChanged, event);
	}
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
Rectangle getClientAreaInPixels () {
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
 * If no <code>TreeColumn</code>s were created by the programmer,
 * this method will throw <code>ERROR_INVALID_RANGE</code> despite
 * the fact that a single column of data may be visible in the tree.
 * This occurs when the programmer uses the tree like a list, adding
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
 * @see Tree#getColumnOrder()
 * @see Tree#setColumnOrder(int[])
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 *
 * @since 3.1
 */
public TreeColumn getColumn (int index) {
	checkWidget();
	if (!(0 <= index && index < columnCount)) error (SWT.ERROR_INVALID_RANGE);
	return columns [index];
}

/**
 * Returns the number of columns contained in the receiver.
 * If no <code>TreeColumn</code>s were created by the programmer,
 * this value is zero, despite the fact that visually, one column
 * of items may be visible. This occurs when the programmer uses
 * the tree like a list, adding items but never creating a column.
 *
 * @return the number of columns
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public int getColumnCount () {
	checkWidget();
	return columnCount;
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
 * @see Tree#setColumnOrder(int[])
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 *
 * @since 3.2
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

long [] getColumnTypes (int columnCount) {
	long [] types = new long [FIRST_COLUMN + (columnCount * CELL_TYPES)];
	// per row data
	types [ID_COLUMN] = OS.G_TYPE_INT ();
	types [CHECKED_COLUMN] = OS.G_TYPE_BOOLEAN ();
	types [GRAYED_COLUMN] = OS.G_TYPE_BOOLEAN ();
	types [FOREGROUND_COLUMN] = GDK.GDK_TYPE_RGBA();
	types [BACKGROUND_COLUMN] = GDK.GDK_TYPE_RGBA();
	types [FONT_COLUMN] = OS.PANGO_TYPE_FONT_DESCRIPTION ();
	// per cell data
	for (int i=FIRST_COLUMN; i<types.length; i+=CELL_TYPES) {
		types [i + CELL_PIXBUF] = GDK.GDK_TYPE_PIXBUF ();
		types [i + CELL_TEXT] = OS.G_TYPE_STRING ();
		types [i + CELL_FOREGROUND] = GDK.GDK_TYPE_RGBA();
		types [i + CELL_BACKGROUND] = GDK.GDK_TYPE_RGBA();
		types [i + CELL_FONT] = OS.PANGO_TYPE_FONT_DESCRIPTION ();
		types [i + CELL_SURFACE] = OS.G_TYPE_LONG();
	}
	return types;
}

/**
 * Returns an array of <code>TreeColumn</code>s which are the
 * columns in the receiver. Columns are returned in the order
 * that they were created.  If no <code>TreeColumn</code>s were
 * created by the programmer, the array is empty, despite the fact
 * that visually, one column of items may be visible. This occurs
 * when the programmer uses the tree like a list, adding items but
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
 * @see Tree#getColumnOrder()
 * @see Tree#setColumnOrder(int[])
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 *
 * @since 3.1
 */
public TreeColumn [] getColumns () {
	checkWidget();
	TreeColumn [] result = new TreeColumn [columnCount];
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

TreeItem getFocusItem () {
	long [] path = new long [1];
	GTK.gtk_tree_view_get_cursor (handle, path, null);
	if (path [0] == 0) return null;
	try {
		return _getItemByPath (path [0]);
	} finally {
		GTK.gtk_tree_path_free (path [0]);
	}
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
 *
 * @since 3.1
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
 * @since 3.1
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
		for (int i=0; i<columnCount; i++) {
			long buttonHandle = columns [i].buttonHandle;
			if (buttonHandle != 0) {
				gtk_widget_get_preferred_size (buttonHandle, requisition);
				height = Math.max (height, requisition.height);
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
 *
 * @since 3.1
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
 *
 * @since 3.1
 */
public TreeItem getItem (int index) {
	checkWidget ();
	if (index < 0) error (SWT.ERROR_INVALID_RANGE);
	if (index >= roots.size ()) error (SWT.ERROR_INVALID_RANGE);

	return _getItem (null, roots, index);
}

TreeItem _getItem (TreeItem parentItem, List<TreeItem> siblings, int index) {
	long parentIter = parentItem == null ? 0 : parentItem.handle;
	TreeItem result = siblings.get (index);
	if (result != null) return result;

	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	try {
		// Optimization: do not restart iteration if previous element is known
		// Often happens when items are accessed sequentially
		TreeItem previous = index > 1 ? siblings.get (index - 1) : null;
		if (previous != null) {
			C.memmove (iter, previous.handle, GTK.GtkTreeIter_sizeof ());
			if (!GTK.gtk_tree_model_iter_next (modelHandle, iter)) error (SWT.ERROR_INVALID_RANGE);
		} else {
			if (!GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, parentIter, index)) error (SWT.ERROR_INVALID_RANGE);
		}
		result = new TreeItem (this, parentItem, SWT.NONE, index, iter);
		assert siblings.get (index) == result: "TreeItem shoud self-insert into sibling list";
		return result;
	} finally {
		OS.g_free (iter);
	}
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
public TreeItem getItem (Point point) {
	checkWidget();
	return getItemInPixels(DPIUtil.autoScaleUp(point));
}

TreeItem getItemInPixels (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	long [] path = new long [1];
	GTK.gtk_widget_realize (handle);
	int x = point.x;
	int y = point.y;
	/*
	 * On GTK4 the header is included in the entire widget's surface, so we must subtract
	 * its size from the y-coordinate. This does not apply on GTK3 as the header and
	 * "main-widget" have separate GdkWindows.
	 */
	if (getHeaderVisible() && GTK.GTK4) {
		y -= getHeaderHeight();
	}
	if ((style & SWT.MIRRORED) != 0) x = getClientWidth () - x;
	long [] columnHandle = new long [1];
	if (!GTK.gtk_tree_view_get_path_at_pos (handle, x, y, path, columnHandle, null, null)) return null;
	if (path [0] == 0) return null;
	TreeItem item = null;
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	if (GTK.gtk_tree_model_get_iter (modelHandle, iter, path [0])) {
		boolean overExpander = false;
		if (GTK.gtk_tree_view_get_expander_column (handle) == columnHandle [0]) {
			GdkRectangle rect = new GdkRectangle ();
			GTK.gtk_tree_view_get_cell_area (handle, path [0], columnHandle [0], rect);
			if ((style & SWT.MIRRORED) != 0) {
				overExpander = x > rect.x + rect.width;
			} else {
				overExpander = x < rect.x;
			}
		}
		if (!overExpander) {
			item = _getItemByIter (iter);
		}
	}
	OS.g_free (iter);
	GTK.gtk_tree_path_free (path [0]);
	return item;
}

/**
 * Returns the number of items contained in the receiver
 * that are direct item children of the receiver.  The
 * number that is returned is the number of roots in the
 * tree.
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
	assert roots.size () == GTK.gtk_tree_model_iter_n_children (modelHandle, 0);
	return roots.size ();
}

/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the tree.
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
	int itemCount = GTK.gtk_tree_model_iter_n_children(modelHandle, 0);

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
 * Returns a (possibly empty) array of items contained in the
 * receiver that are direct item children of the receiver.  These
 * are the roots of the tree.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver.
 * </p>
 *
 * @return the items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem [] getItems () {
	checkWidget ();
	return _getItems (null, roots);
}

TreeItem [] _getItems (TreeItem parentItem, List<TreeItem> items) {
	long parentIter = parentItem == null ? 0 : parentItem.handle;
	int i = 0;
	if (items.indexOf (null) < 0) return items.toArray (new TreeItem [items.size ()]);
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
	if (iter == 0) error (SWT.ERROR_NO_HANDLES);
	try {
		boolean valid = GTK.gtk_tree_model_iter_children (modelHandle, iter, parentIter);
		while (valid) {
			if (items.size () <= i || items.get (i) == null) {
				TreeItem inserted = new TreeItem (this, parentItem, SWT.NONE, i, iter);
				assert items.get (i) == inserted : "TreeItem constructor should update sibling list";
			}
			i++;
			valid = GTK.gtk_tree_model_iter_next (modelHandle, iter);
		}
	} finally {
		OS.g_free (iter);
	}
	assert items.indexOf (null) < 0 : "All items should be hydrated";
	return items.toArray (new TreeItem [items.size ()]);
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
 *
 * @since 3.1
 */
public boolean getLinesVisible() {
	checkWidget();
	return GTK.gtk_tree_view_get_grid_lines(handle) > GTK.GTK_TREE_VIEW_GRID_LINES_NONE;
}

/**
 * Returns the receiver's parent item, which must be a
 * <code>TreeItem</code> or null when the receiver is a
 * root.
 *
 * @return the receiver's parent item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem getParentItem () {
	checkWidget ();
	return null;
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
 * Returns an array of <code>TreeItem</code>s that are currently
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
public TreeItem[] getSelection () {
	checkWidget();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	long list = GTK.gtk_tree_selection_get_selected_rows (selection, null);
	if (list != 0) {
		long originalList = list;
		int count = OS.g_list_length (list);
		TreeItem [] treeSelection = new TreeItem [count];
		int length = 0;
		for (int i=0; i<count; i++) {
			long data = OS.g_list_data (list);
			long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
			if (GTK.gtk_tree_model_get_iter (modelHandle, iter, data)) {
				treeSelection [length] = _getItemByIter (iter);
				length++;
			}
			list = OS.g_list_next (list);
			OS.g_free (iter);
			GTK.gtk_tree_path_free (data);
		}
		OS.g_list_free (originalList);
		if (length < count) {
			TreeItem [] temp = new TreeItem [length];
			System.arraycopy(treeSelection, 0, temp, 0, length);
			treeSelection = temp;
		}
		return treeSelection;
	}
	return new TreeItem [0];
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
 * @see #setSortColumn(TreeColumn)
 *
 * @since 3.2
 */
public TreeColumn getSortColumn () {
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
 * Returns the item which is currently at the top of the receiver.
 * This item can change when items are expanded, collapsed, scrolled
 * or new items are added or removed.
 *
 * @return the item at the top of the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 2.1
 */
public TreeItem getTopItem () {
	checkWidget ();
	/*
	 * Feature in GTK: fetch the topItem using the topItem global variable
	 * if setTopItem() has been called and the widget has not been scrolled
	 * using the UI. Otherwise, fetch topItem using GtkTreeView API.
	 */
	long vAdjustment;
	vAdjustment = GTK.gtk_scrollable_get_vadjustment(handle);
	currentAdjustment = GTK.gtk_adjustment_get_value(vAdjustment);
	TreeItem item = null;
	if (cachedAdjustment == currentAdjustment) {
		item = _getCachedTopItem();
	}
	/*
	 * Bug 501420: check to make sure the item is not disposed before returning
	 * it. If it is, find the topItem using GtkTreeView API.
	 */
	if(item != null && !item.isDisposed()){
		return item;
	}
	// Use GTK method to get topItem if there has been changes to the vAdjustment
	long [] path = new long [1];
	GTK.gtk_widget_realize (handle);
	if (!GTK.gtk_tree_view_get_path_at_pos (handle, 1, 1, path, null, null, null)) return null;
	if (path [0] == 0) return null;
	item = null;
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof());
	if (GTK.gtk_tree_model_get_iter (modelHandle, iter, path [0])) {
		item = _getItemByIter (iter);
	}
	OS.g_free (iter);
	GTK.gtk_tree_path_free (path [0]);
	topItem = item;
	return item;
}

TreeItem _getCachedTopItem() {
	/*
	 *  Check to see if the selected item is also the topItem. If it is, that means topItem is
	 *  in sync with the GTK view. If not, the real top item should be the last selected item, which is caused
	 *  by setSelection().
	 */
	long treeSelect = GTK.gtk_tree_view_get_selection(handle);
	long list = GTK.gtk_tree_selection_get_selected_rows(treeSelect, null);
	TreeItem treeSelection = null;
	if (list != 0) {
		long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
		long data = OS.g_list_data (list);
		if (GTK.gtk_tree_model_get_iter (modelHandle, iter, data)) {
			treeSelection = _getItemByIter (iter);
		}
		OS.g_free (iter);
		GTK.gtk_tree_path_free (data);
		if (topItem == treeSelection) {
			return topItem;
		}
		else {
			return treeSelection;
		}
	} else {
		if (topItem == null) {
			// if topItem isn't set and there is nothing selected, topItem is the first item on the Tree
			TreeItem item = null;
			long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof());
			if (GTK.gtk_tree_model_get_iter_first (modelHandle, iter)) {
				item = _getItemByIter (iter);
			}
			OS.g_free (iter);
			return item;
		} else {
			return topItem;
		}
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
	* Feature in GTK.  In a multi-select tree view, when multiple items are already
	* selected, the selection state of the item is toggled and the previous selection
	* is cleared. This is not the desired behaviour when bringing up a popup menu
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
	super.gtk_gesture_press_event(gesture, n_press, x, y, event);

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
			// When    : Enter, Shift+Enter, Ctrl+Enter are pressed.
			// Not when: Alt+Enter, (Meta|Super|Hyper)+Enter, reason is stateMask is not provided on Gtk.
			// Note: alt+Enter creates a selection on GTK, but we filter it out to be a bit more consistent Win32 (521387)
			int keymask = gdk_event_get_state (event);
			if ((keymask & (GDK.GDK_SUPER_MASK | GDK.GDK_META_MASK | GDK.GDK_HYPER_MASK | GDK.GDK_MOD1_MASK)) == 0) {
				sendTreeDefaultSelection ();
			}
			break;
	}

	return super.gtk_key_press_event (widget, event);
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
	TreeItem treeItem = getFocusItem ();
	if (treeItem == null)
		return;

	Event event = new Event ();
	event.item = treeItem;

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
	TreeItem item = getFocusItem ();
	if (item != null) {
		Event event = new Event ();
		event.item = item;
		sendSelectionEvent (SWT.Selection, event, false);
	}
	return 0;
}

@Override
long gtk_expand_collapse_cursor_row (long widget, long logical, long expand, long open_all) {
	// FIXME - this flag is never cleared.  It should be cleared when the expand all operation completes.
	if (expand != 0 && open_all != 0) expandAll = true;
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
			long parent = 0;
			int itemCount = GTK.gtk_tree_model_iter_n_children (modelHandle, parent);
			GdkRectangle rect = new GdkRectangle ();
			boolean expanded = true;
			while (itemCount != 0 && expanded && height [0] > (rect.y + rect.height)) {
				long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
				GTK.gtk_tree_model_iter_nth_child (modelHandle, iter, parent, itemCount - 1);
				itemCount = GTK.gtk_tree_model_iter_n_children (modelHandle, iter);
				long path = GTK.gtk_tree_model_get_path (modelHandle, iter);
				GTK.gtk_tree_view_get_cell_area (handle, path, 0, rect);
				expanded = GTK.gtk_tree_view_row_expanded (handle, path);
				GTK.gtk_tree_path_free (path);
				if (parent != 0) OS.g_free (parent);
				parent = iter;
			}
			if (parent != 0) OS.g_free (parent);
			if (height [0] > (rect.y + rect.height)) {
				drawBackground (control, gdkResource, cairo, 0, rect.y + rect.height, width [0], height [0] - (rect.y + rect.height));
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
	 * Bug 537960: JFace tree viewers miss a repaint when resized by a SashForm
	 *
	 * If a listener of type SWT.MeasureItem, SWT.PaintItem and or SWT.EraseItem is registered,
	 * GTK will sometimes not invalidate the tree widget pixel cache when the tree is resized.
	 * As a result, a few of the bottom rows of JFace tree viewers that use styled text are often not drawn on resize.
	 * If the tree was resized since the last paint, we ignore this draw request
	 * and queue another draw request so that the pixel cache is properly invalidated.
	 */
	if (isOwnerDrawn && haveBoundsChanged) {
		GTK.gtk_widget_queue_draw(handle);
		return 0;
	}
	drawInheritedBackground	(cairo);
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
long gtk_row_has_child_toggled (long model, long path, long iter) {
	/*
	* Feature in GTK. The expanded state of a row that lost
	* its children is not persisted by GTK. So, the row
	* doesn't exhibit the expanded state after obtaining the
	* children. The fix is to preserve the expanded state
	* and use this callback, as it is invoked when a row has
	* gotten the first child row or lost its last child row.
	*/
	TreeItem item = _getItemByPath (path);
	int childCount = GTK.gtk_tree_model_iter_n_children (modelHandle, item.handle);
	if (childCount != 0 && item.isExpanded) {
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_EXPAND_ROW);
		GTK.gtk_tree_view_expand_row (handle, path, false);
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_EXPAND_ROW);
	}
	return 0;
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
long gtk_test_collapse_row (long tree, long iter, long path) {
	TreeItem item = _getItemByIter (iter);
	Event event = new Event ();
	event.item = item;
	boolean oldModelChanged = modelChanged;
	modelChanged = false;
	sendEvent (SWT.Collapse, event);
	/*
	* Bug in GTK.  Collapsing the target row during the test_collapse_row
	* handler will cause a segmentation fault if the animation code is allowed
	* to run.  The fix is to block the animation if the row is already
	* collapsed.
	*/
	boolean changed = modelChanged || !GTK.gtk_tree_view_row_expanded (handle, path);
	modelChanged = oldModelChanged;
	if (isDisposed () || item.isDisposed ()) return 1;
	item.isExpanded = false;
	/*
	* Bug in GTK.  Expanding or collapsing a row which has no more
	* children causes the model state to become invalid, causing
	* GTK to give warnings and behave strangely.  Other changes to
	* the model can cause expansion to fail when using the multiple
	* expansion keys (such as *).  The fix is to stop the expansion
	* if there are model changes.
	*
	* Note: This callback must return 0 for the collapsing
	* animation to occur.
	*/
	if (changed) {
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_COLLAPSE_ROW);
		GTK.gtk_tree_view_collapse_row (handle, path);
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_COLLAPSE_ROW);
		return 1;
	}
	return 0;
}

@Override
long gtk_test_expand_row (long tree, long iter, long path) {
	TreeItem item = _getItemByPath (path);
	Event event = new Event ();
	event.item = item;
	boolean oldModelChanged = modelChanged;
	modelChanged = false;
	sendEvent (SWT.Expand, event);
	/*
	* Bug in GTK.  Expanding the target row during the test_expand_row
	* handler will cause a segmentation fault if the animation code is allowed
	* to run.  The fix is to block the animation if the row is already
	* expanded.
	*/
	boolean changed = modelChanged || GTK.gtk_tree_view_row_expanded (handle, path);
	modelChanged = oldModelChanged;
	if (isDisposed () || item.isDisposed ()) return 1;
	item.isExpanded = true;
	/*
	* Bug in GTK.  Expanding or collapsing a row which has no more
	* children causes the model state to become invalid, causing
	* GTK to give warnings and behave strangely.  Other changes to
	* the model can cause expansion to fail when using the multiple
	* expansion keys (such as *).  The fix is to stop the expansion
	* if there are model changes.
	*
	* Bug in GTK.  test-expand-row does not get called for each row
	* in an expand all operation.  The fix is to block the initial
	* expansion and only expand a single level.
	*
	* Note: This callback must return 0 for the collapsing
	* animation to occur.
	*/
	if (changed || expandAll) {
		OS.g_signal_handlers_block_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_EXPAND_ROW);
		GTK.gtk_tree_view_expand_row (handle, path, false);
		OS.g_signal_handlers_unblock_matched (handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_EXPAND_ROW);
		return 1;
	}
	return 0;
}

@Override
long gtk_toggled (long renderer, long pathStr) {
	long path = GTK.gtk_tree_path_new_from_string (pathStr);
	if (path == 0) return 0;
	TreeItem item = null;
	long iter = OS.g_malloc (GTK.GtkTreeIter_sizeof());
	if (GTK.gtk_tree_model_get_iter (modelHandle, iter, path)) {
		item = _getItemByIter (iter);
	}
	OS.g_free (iter);
	GTK.gtk_tree_path_free (path);
	if (item != null) {
		item.setChecked (!item.getChecked ());
		Event event = new Event ();
		event.detail = SWT.CHECK;
		event.item = item;
		sendSelectionEvent (SWT.Selection, event, false);
	}
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
	OS.g_signal_connect_closure (handle, OS.test_expand_row, display.getClosure (TEST_EXPAND_ROW), false);
	OS.g_signal_connect_closure (handle, OS.test_collapse_row, display.getClosure (TEST_COLLAPSE_ROW), false);
	OS.g_signal_connect_closure (handle, OS.expand_collapse_cursor_row, display.getClosure (EXPAND_COLLAPSE_CURSOR_ROW), false);
	OS.g_signal_connect_closure (modelHandle, OS.row_has_child_toggled, display.getClosure (ROW_HAS_CHILD_TOGGLED), false);
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
 *
 * @since 3.1
 */
public int indexOf (TreeColumn column) {
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
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public int indexOf (TreeItem item) {
	checkWidget();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	int index = -1;
	long path = GTK.gtk_tree_model_get_path (modelHandle, item.handle);
	int depth = GTK.gtk_tree_path_get_depth (path);
	if (depth == 1) {
		long indices = GTK.gtk_tree_path_get_indices (path);
		if (indices != 0) {
			int[] temp = new int[1];
			C.memmove (temp, indices, 4);
			index = temp[0];
		}
	}
	GTK.gtk_tree_path_free (path);
	return index;
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
		for (TreeColumn column : columns) {
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
		checkRenderer = isOwnerDrawn ? OS.g_object_new (display.gtk_cell_renderer_toggle_get_type(), 0) : GTK.gtk_cell_renderer_toggle_new ();
		if (checkRenderer == 0) error (SWT.ERROR_NO_HANDLES);
		OS.g_object_ref (checkRenderer);
		display.addWidget (checkRenderer, this);
		OS.g_signal_connect_closure (checkRenderer, OS.toggled, display.getClosure (TOGGLED), false);
	}
	if (columnCount == 0) {
		createRenderers (GTK.gtk_tree_view_get_column (handle, 0), Tree.FIRST_COLUMN, true, 0);
	} else {
		for (int i = 0; i < columnCount; i++) {
			TreeColumn column = columns [i];
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

void _releaseItems (long parentIter, List<TreeItem> items) {
	for (TreeItem item: items) {
		if (item != null && !isDisposed ()) {
			item.release (false);
		}
	}
}

@Override
void releaseChildren (boolean destroy) {
	getKnownItemsRecursively ().forEach (item -> {
		if (item != null && !item.isDisposed ()) {
			item.release (false);
		}
	});
	if (columns != null) {
		for (int i=0; i<columnCount; i++) {
			TreeColumn column = columns [i];
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
 * Removes all of the items from the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget ();
	checkSetDataInProcessBeforeRemoval();

	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);

	GTK.gtk_tree_store_clear (modelHandle);

	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);

	for (TreeItem item: new ArrayList<> (roots)) {
		if (item != null && !item.isDisposed () ) {
			item.release (false);
		}
	}

	roots.clear ();

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
 * @see #addSelectionListener
 */
public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when items in the receiver are expanded or collapsed.
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
 * @see TreeListener
 * @see #addTreeListener
 */
public void removeTreeListener(TreeListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Expand, listener);
	eventTable.unhook (SWT.Collapse, listener);
}

void sendMeasureEvent (long cell, long width, long height) {
	if (!ignoreSize && GTK.GTK_IS_CELL_RENDERER_TEXT (cell) && hooks (SWT.MeasureItem)) {
		long iter = OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX2);
		TreeItem item = null;
		if (iter != 0) item = _getItemByIter (iter);
		if (item != null && !item.isDisposed()) {
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
			if (image != null && !image.isDisposed()) {
				Rectangle bounds;
				if (DPIUtil.useCairoAutoScale()) {
					bounds = image.getBounds ();
				} else {
					bounds = image.getBoundsInPixels ();
				}
				bounds = image.getBounds ();
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
			long path = GTK.gtk_tree_model_get_path (modelHandle, iter);
			long selection = GTK.gtk_tree_view_get_selection (handle);
			if (GTK.gtk_tree_selection_path_is_selected (selection, path)) {
				event.detail = SWT.SELECTED;
			}
			GTK.gtk_tree_path_free (path);
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
	TreeItem item = null;
	boolean wasSelected = false;
	long iter = OS.g_object_get_qdata (cell, Display.SWT_OBJECT_INDEX2);
	if (iter != 0) item = _getItemByIter (iter);
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
		if (GTK.GTK_IS_CELL_RENDERER_TOGGLE (cell) || ( columnIndex != 0 || (style & SWT.CHECK) == 0)) {
			drawFlags = (int)flags;
			drawState = SWT.FOREGROUND;
			long [] ptr = new long [1];
			GTK.gtk_tree_model_get (modelHandle, item.handle, Tree.BACKGROUND_COLUMN, ptr, -1);
			if (ptr [0] == 0) {
				int modelIndex = columnCount == 0 ? Tree.FIRST_COLUMN : columns [columnIndex].modelIndex;
				GTK.gtk_tree_model_get (modelHandle, item.handle, modelIndex + Tree.CELL_BACKGROUND, ptr, -1);
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
			// Use the x and width information from the Cairo context. See bug 535124.
			if (cr != 0) {
				GdkRectangle r2 = new GdkRectangle ();
				GDK.gdk_cairo_get_clip_rectangle (cr, r2);
				rect.x = r2.x;
				rect.width = r2.width;
			}
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
					// Use the original rectangle, not the Cairo clipping for the y, width, and height values.
					// See bug 535124.
					Rectangle rect2 = DPIUtil.autoScaleDown(rect);
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
				} else {
					if (wasSelected) {
						drawForegroundRGBA = gc.getForeground ().handle;
					}
				}
				gc.dispose();
				Cairo.cairo_restore (cr);
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
			 * SWT.FOREGROUND means the Tree is responsible for painting the default foreground
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
					if(DPIUtil.useCairoAutoScale()) {
						bounds = image.getBounds ();
					} else {
						bounds = image.getBoundsInPixels ();
					}
					imageWidth = bounds.width;
				}
				// Account for the image width on GTK3, see bug 535124.
				if (cr != 0) {
					rect.x -= imageWidth;
					rect.width +=imageWidth;
				}
				contentX [0] -= imageWidth;
				contentWidth [0] += imageWidth;

				// Account for the expander arrow offset in x position
				if (GTK.gtk_tree_view_get_expander_column (handle) == columnHandle) {
					/* indent */
					GdkRectangle rect3 = new GdkRectangle ();
					GTK.gtk_widget_realize (handle);
					long path = GTK.gtk_tree_model_get_path (modelHandle, iter);
					GTK.gtk_tree_view_get_cell_area (handle, path, columnHandle, rect3);
					GTK.gtk_tree_path_free (path);
					contentX[0] += rect3.x;
				}
				GC gc = getGC(cr);
				if ((drawState & SWT.SELECTED) != 0) {
					Color background, foreground;
					background = display.getSystemColor (SWT.COLOR_LIST_SELECTION);
					foreground = display.getSystemColor (SWT.COLOR_LIST_SELECTION_TEXT);
					gc.setBackground (background);
					gc.setForeground (foreground);
				} else {
					gc.setBackground (item.getBackground (columnIndex));
					Color foreground;
					foreground = drawForegroundRGBA != null ? Color.gtk_new (display, drawForegroundRGBA) : item.getForeground (columnIndex);
					gc.setForeground (foreground);
				}
				gc.setFont (item.getFont (columnIndex));
				if ((style & SWT.MIRRORED) != 0) {
					rect.x = getClientWidth () - rect.width - rect.x;
				}

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
	if ((style & SWT.VIRTUAL) != 0 || isOwnerDrawn) return;
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
	getKnownItemsRecursively().forEach(item -> item.reskinChildren (flags));
	if (columns != null) {
		for (int i=0; i<columns.length; i++) {
			TreeColumn column = columns [i];
			if (column != null) column.reskinChildren (flags);
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
 * Display a mark indicating the point at which an item will be inserted.
 * The drop insert item has a visual hint to show where a dragged item
 * will be inserted when dropped on the tree.
 *
 * @param item the insert item.  Null will clear the insertion mark.
 * @param before true places the insert mark above 'item'. false places
 *	the insert mark below 'item'.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setInsertMark (TreeItem item, boolean before) {
	checkWidget ();
	if (item == null) {
		GTK.gtk_tree_view_set_drag_dest_row(handle, 0, GTK.GTK_TREE_VIEW_DROP_BEFORE);
		return;
	}
	if (item.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (item.parent != this) return;
	Rectangle rect = item.getBoundsInPixels();
	long [] path = new long [1];
	GTK.gtk_widget_realize (handle);
	if (!GTK.gtk_tree_view_get_path_at_pos(handle, rect.x, rect.y, path, null, null, null)) return;
	if (path [0] == 0) return;
	int position = before ? GTK.GTK_TREE_VIEW_DROP_BEFORE : GTK.GTK_TREE_VIEW_DROP_AFTER;
	GTK.gtk_tree_view_set_drag_dest_row(handle, path[0], position);
	GTK.gtk_tree_path_free (path [0]);
}

void _setItemCount (TreeItem parentItem, long parentIter, List<TreeItem> items, int count) {
	boolean isVirtual = (style & SWT.VIRTUAL) != 0;
	if (!isVirtual) setRedraw (false);
	try {
		int oldSize = items.size ();
		assert GTK.gtk_tree_model_iter_n_children (modelHandle, parentIter) == oldSize;
		if (oldSize > count) {
			long removeIter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
			if (removeIter == 0) error (SWT.ERROR_NO_HANDLES);
			try {
				long previousIter = count > 0 ? _getItem (parentItem, items, count - 1).handle : 0;
				long selection = GTK.gtk_tree_view_get_selection (handle);
				while (items.size () > count) {
					OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
					TreeItem item = items.get (count);
					if (item != null) {
						item.dispose ();
					} else {
						if (previousIter != 0) {
							C.memmove (removeIter, previousIter, GTK.GtkTreeIter_sizeof ());
							if (!GTK.gtk_tree_model_iter_next (modelHandle, removeIter)) error (SWT.ERROR_INVALID_RANGE);
							GTK.gtk_tree_store_remove (modelHandle, removeIter);
						} else {
							GTK.gtk_tree_model_iter_nth_child (modelHandle, removeIter, parentIter, 0);
							GTK.gtk_tree_store_remove (modelHandle, removeIter);
						}
						TreeItem removed = items.remove (count);
						if (removed != null) throw new AssertionError ("Unexpected hydrated TreeItem");
					}
					OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
				}
			} finally {
				OS.g_free (removeIter);
			}
		} else if (oldSize < count) {
			long createdIter = OS.g_malloc (GTK.GtkTreeIter_sizeof ());
			if (createdIter == 0) error (SWT.ERROR_NO_HANDLES);
			try {
				long insertAfterIter = oldSize > 0 ? _getItem (parentItem, items, oldSize - 1).handle : 0;
				items.addAll (Collections.nCopies (count - oldSize, null));
				for (int i = oldSize; i < count; i++) {
					GTK.gtk_tree_store_insert_after (modelHandle, createdIter, parentIter, insertAfterIter);
				}
			} finally {
				OS.g_free (createdIter);
			}
		}
	} finally {
		if (!isVirtual) setRedraw (true);
	}
	assert GTK.gtk_tree_model_iter_n_children (modelHandle, parentIter) == count;
	assert items.size () == count;
	modelChanged = true;
}

/**
 * Sets the number of root-level items contained in the receiver.
 * <p>
 * The fastest way to insert many items is documented in {@link TreeItem#TreeItem(Tree,int,int)}
 * and {@link TreeItem#setItemCount}
 *
 * @param count the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void setItemCount (int count) {
	if (count < 0) count = 0;
	checkWidget ();
	_setItemCount (null, 0, roots, count);
}

/**
 * Selects an item in the receiver.  If the item was already
 * selected, it remains selected.
 *
 * @param item the item to be selected
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
 * @since 3.4
 */
public void select (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	GTK.gtk_tree_selection_select_iter (selection, item.handle);
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
	String css = "treeview {background-color: " + display.gtk_rgba_to_css_string(background) + ";}\n"
			+ "treeview:selected {background-color: " + display.gtk_rgba_to_css_string(selectedBackground) + ";}";

	// Cache background color
	cssBackground = css;

	// Apply background color and any foreground color
	String finalCss = display.gtk_css_create_css_color_string (cssBackground, cssForeground, SWT.BACKGROUND);
	gtk_css_provider_load_from_css(context, finalCss);
}

@Override
void setBackgroundSurface (Image image) {
	isOwnerDrawn = true;
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
 * @see Tree#getColumnOrder()
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 *
 * @since 3.2
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
	long baseColumn = 0;
	for (int i=0; i<order.length; i++) {
		long column = columns [order [i]].handle;
		GTK.gtk_tree_view_move_column_after (handle, column, baseColumn);
		baseColumn = column;
	}
}

@Override
void setFontDescription (long font) {
	super.setFontDescription (font);
	TreeColumn[] columns = getColumns ();
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
 * will cause the header to look FLAT irrespective of the state of the tree style.
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
		if (color.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
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
		for (TreeColumn column : columns) {
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
 * will cause the header to look FLAT irrespective of the state of the tree style.
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
 *
 * @since 3.1
 */
public void setHeaderVisible (boolean show) {
	checkWidget ();
	GTK.gtk_tree_view_set_headers_visible (handle, show);
	this.headerHeight = this.getHeaderHeight();
	this.headerVisible = show;
}

/**
 * Marks the receiver's lines as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. Note that some platforms draw
 * grid lines while others may draw alternating row colors.
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
 *
 * @since 3.1
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
	getKnownItemsRecursively ().forEach (item -> item.setOrientation (create));
	if (columns != null) {
		for (int i=0; i<columns.length; i++) {
			if (columns [i] != null) columns [i].setOrientation (create);
		}
	}
}

@Override
void setParentBackground () {
	isOwnerDrawn = true;
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

void setScrollWidth (long column, TreeItem item) {
	if (columnCount != 0 || currentItem == item) return;
	int width = GTK.gtk_tree_view_column_get_fixed_width (column);
	int itemWidth = calculateWidth (column, item.handle, true);
	if (width < itemWidth) {
		GTK.gtk_tree_view_column_set_fixed_width (column, itemWidth);
	}
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
public void setSelection (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (new TreeItem [] {item});
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
 * @see Tree#deselectAll()
 */
public void setSelection (TreeItem [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	deselectAll ();
	int length = items.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	boolean fixColumn = showFirstColumn ();
	long selection = GTK.gtk_tree_view_get_selection (handle);
	OS.g_signal_handlers_block_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	boolean first = true;
	for (int i = 0; i < length; i++) {
		TreeItem item = items [i];
		if (item == null) continue;
		if (item.isDisposed ()) break;
		if (item.parent != this) continue;
		long path = GTK.gtk_tree_model_get_path (modelHandle, item.handle);
		showItem (path, false);
		if (first) {
			GTK.gtk_tree_view_set_cursor (handle, path, 0, false);
		}
		GTK.gtk_tree_selection_select_iter (selection, item.handle);
		GTK.gtk_tree_path_free (path);
		first = false;
	}
	OS.g_signal_handlers_unblock_matched (selection, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, CHANGED);
	if (fixColumn) hideFirstColumn ();
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
public void setSortColumn (TreeColumn column) {
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
 * Sets the item which is currently at the top of the receiver.
 * This item can change when items are expanded, collapsed, scrolled
 * or new items are added or removed.
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
 * @see Tree#getTopItem()
 *
 * @since 2.1
 */
public void setTopItem (TreeItem item) {

	/*
	 * Feature in GTK: cache the GtkAdjustment value for future use in
	 * getTopItem(). Set topItem to item.
	 */
	long vAdjustment;
	vAdjustment = GTK.gtk_scrollable_get_vadjustment(handle);
	cachedAdjustment = GTK.gtk_adjustment_get_value(vAdjustment);
	topItem = item;

	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (item.parent != this) return;
	long path = GTK.gtk_tree_model_get_path (modelHandle, item.handle);
	showItem (path, false);
	GTK.gtk_tree_view_scroll_to_cell (handle, path, 0, true, 0, 0);
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
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void showColumn (TreeColumn column) {
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
 * Shows the selection.  If the selection is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled until
 * the selection is visible.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Tree#showItem(TreeItem)
 */
public void showSelection () {
	checkWidget();
	TreeItem [] items = getSelection ();
	if (items.length != 0 && items [0] != null) showItem (items [0]);
}

void showItem (long path, boolean scroll) {
	int depth = GTK.gtk_tree_path_get_depth (path);
	if (depth > 1) {
		int [] indices = new int [depth - 1];
		long indicesPtr = GTK.gtk_tree_path_get_indices (path);
		C.memmove (indices, indicesPtr, indices.length * 4);
		long tempPath = GTK.gtk_tree_path_new ();
		for (int i=0; i<indices.length; i++) {
			GTK.gtk_tree_path_append_index (tempPath, indices [i]);
			GTK.gtk_tree_view_expand_row (handle, tempPath, false);
		}
		GTK.gtk_tree_path_free (tempPath);
	}
	if (scroll) {
		GTK.gtk_tree_view_scroll_to_cell (handle, path, 0, false, 0.5f, 0.0f);
	}
}

/**
 * Shows the item.  If the item is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled
 * and expanded until the item is visible.
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
 * @see Tree#showSelection()
 */
public void showItem (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) error(SWT.ERROR_INVALID_ARGUMENT);
	if (item.parent != this) return;
	long path = GTK.gtk_tree_model_get_path (modelHandle, item.handle);
	showItem (path, true);
	GTK.gtk_tree_path_free (path);
}

@Override
void updateScrollBarValue (ScrollBar bar) {
	super.updateScrollBarValue (bar);

	if (!GTK.GTK4) {
		/*
		* Bug in GTK. Scrolling changes the XWindow position
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
			 * If this Tree has any child widgets, propagate the draw signal
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
			int itemCount = GTK.gtk_tree_model_iter_n_children (modelHandle, 0);
			if (itemCount == 0 && (state & OBSCURED) == 0) {
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
	 * In the error case, the SWT fixed which contains the tree still resizes as expected,
	 * and the horizontal scrollbar is only partially visible so that it doesn't overlap with tree headers.
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
 * Check the tree for items that are in process of
 * sending {@code SWT#SetData} event. If such items exist, throw an exception.
 *
 * Does nothing if the given range contains no indices,
 * or if we are below GTK 3.22.0 or are using GTK 4.
 */
void checkSetDataInProcessBeforeRemoval() {
	/*
	 * Bug 182598 - assertion failed in gtktreestore.c
	 *
	 * To prevent a crash in GTK, we ensure we are not setting data on the tree items we are about to remove.
	 * Removing an item while its data is being set will invalidate it, which will cause a crash.
	 *
	 * We therefore throw an exception to prevent the crash.
	 */
	getKnownItemsRecursively().forEach(item -> {
		if (item != null && item.settingData) {
			throwCannotRemoveItem ();
		}
	});
}

private void throwCannotRemoveItem() {
	String message = "Cannot remove item with index.";
	throw new SWTException(message);
}

@Override
public void dispose() {
	super.dispose ();

	if (headerCSSProvider != 0) {
		OS.g_object_unref(headerCSSProvider);
		headerCSSProvider = 0;
	}
}

Stream<TreeItem> getKnownItemsRecursively () {
	if (roots == null) return Stream.empty ();  // called from constructor
	return roots.stream ()
			.filter (Objects::nonNull).
			flatMap ( item -> Stream.concat (Stream.of (item), item.getKnownChildrenRecursively ()));
}
}
