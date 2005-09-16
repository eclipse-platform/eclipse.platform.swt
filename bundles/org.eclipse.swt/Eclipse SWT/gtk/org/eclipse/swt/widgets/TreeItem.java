/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a hierarchy of tree items in a tree widget.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class TreeItem extends Item {
	Tree parent;
	Font font;
	Font[] cellFont;
	boolean cached, grayed;
	static final int EXPANDER_EXTRA_PADDING = 4;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>)
 * and a style value describing its behavior and appearance.
 * The item is added to the end of the items maintained by its parent.
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
 * @param parent a tree control which will be the parent of the new instance (cannot be null)
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TreeItem (Tree parent, int style) {
	this (checkNull (parent), 0, style, -1, true);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>),
 * a style value describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
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
 * @param parent a tree control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TreeItem (Tree parent, int style, int index) {
	this (checkNull (parent), 0, style, checkIndex (index), true);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>)
 * and a style value describing its behavior and appearance.
 * The item is added to the end of the items maintained by its parent.
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
 * @param parentItem a tree control which will be the parent of the new instance (cannot be null)
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TreeItem (TreeItem parentItem, int style) {
	this (checkNull (parentItem).parent, parentItem.handle, style, -1, true);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code> or a <code>TreeItem</code>),
 * a style value describing its behavior and appearance, and the index
 * at which to place it in the items maintained by its parent.
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
 * @param parentItem a tree control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 * @param index the index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TreeItem (TreeItem parentItem, int style, int index) {
	this (checkNull (parentItem).parent, parentItem.handle, style, checkIndex (index), true);
}

TreeItem (Tree parent, int /*long*/ parentIter, int style, int index, boolean create) {
	super (parent, style);
	this.parent = parent;
	if (create) {
		parent.createItem (this, parentIter, index);
	} else {
		handle = OS.g_malloc (OS.GtkTreeIter_sizeof ());
		OS.gtk_tree_model_iter_nth_child (parent.modelHandle, handle, parentIter, index);
	}
}

static TreeItem checkNull (TreeItem item) {
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return item;
}
static Tree checkNull (Tree control) {
	if (control == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return control;
}
static int checkIndex (int index) {
	if (index < 0) SWT.error (SWT.ERROR_ITEM_NOT_ADDED);
	return index;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void clear () {
	if (parent.currentItem == this) return;
	if (cached || (parent.style & SWT.VIRTUAL) == 0) {
		int columnCount = OS.gtk_tree_model_get_n_columns (parent.modelHandle);
		for (int i=0; i<columnCount; i++) {
			OS.gtk_tree_store_set (parent.modelHandle, handle, i, 0, -1);
		}
		OS.gtk_tree_store_set (parent.modelHandle, handle, parent.ID_COLUMN, -1, -1);
		/*
		* Bug in GTK.  When using fixed-height-mode on versions before 2.6.3,
		* row changes do not cause the row to be repainted.  The fix is to
		* invalidate the row when it is cleared.
		*/
		if ((parent.style & SWT.VIRTUAL) != 0) {
			if (OS.GTK_VERSION >= OS.VERSION (2, 3, 2) && OS.GTK_VERSION < OS.VERSION (2, 6, 3)) {
				if ((OS.GTK_WIDGET_FLAGS (parent.handle) & OS.GTK_REALIZED) != 0) {
					int /*long*/ parentHandle = parent.handle;
					int /*long*/ path = OS.gtk_tree_model_get_path (parent.modelHandle, handle);
					GdkRectangle rect = new GdkRectangle ();
					OS.gtk_tree_view_get_cell_area (parentHandle, path, 0, rect);
					OS.gtk_tree_path_free (path);
					int /*long*/ window = OS.gtk_tree_view_get_bin_window (parentHandle);
					rect.x = 0;
					rect.width = OS.GTK_WIDGET_WIDTH (parentHandle);
					OS.gdk_window_invalidate_rect (window, rect, false);
				}
			}
		}
		
	}
	cached = false;
	font = null;
	cellFont = null;
}

/*public*/ void clear (int index, boolean all) {
	checkWidget ();
	parent.clear (handle, index, all);
}

/*public*/ void clearAll (boolean all) {
	checkWidget ();
	parent.clearAll (all, handle);
}

void destroyWidget () {
	parent.releaseItem (this, false);
	parent.destroyItem (this);
	releaseHandle ();
}

/**
 * Returns the receiver's background color.
 *
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public Color getBackground () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	int /*long*/ [] ptr = new int /*long*/ [1];
	OS.gtk_tree_model_get (parent.modelHandle, handle, Tree.BACKGROUND_COLUMN, ptr, -1);
	if (ptr [0] == 0) return parent.getBackground ();
	GdkColor gdkColor = new GdkColor ();
	OS.memmove (gdkColor, ptr [0], GdkColor.sizeof);
	return Color.gtk_new (display, gdkColor);
}

/**
 * Returns the background color at the given column index in the receiver.
 *
 * @param index the column index
 * @return the background color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public Color getBackground (int index) {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	int count = Math.max (1, parent.columnCount);
	if (0 > index || index > count - 1) return getBackground ();
	int /*long*/ [] ptr = new int /*long*/ [1];
	int modelIndex = parent.columnCount == 0 ? Tree.FIRST_COLUMN : parent.columns [index].modelIndex;
	OS.gtk_tree_model_get (parent.modelHandle, handle, modelIndex + Tree.CELL_BACKGROUND, ptr, -1);
	if (ptr [0] == 0) return getBackground ();
	GdkColor gdkColor = new GdkColor ();
	OS.memmove (gdkColor, ptr [0], GdkColor.sizeof);
	return Color.gtk_new (display, gdkColor);
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent at a column in the tree.
 *
 * @param index the index that specifies the column
 * @return the receiver's bounding column rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public Rectangle getBounds (int index) {
	// TODO fully test on early and later versions of GTK
	checkWidget();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	int /*long*/ parentHandle = parent.handle;
	int /*long*/ column = 0;
	if (index >= 0 && index < parent.columnCount) {
		column = parent.columns [index].handle;
	} else {
		column = OS.gtk_tree_view_get_column (parentHandle, index);
	}
	if (column == 0) return new Rectangle (0, 0, 0, 0);
	int /*long*/ path = OS.gtk_tree_model_get_path (parent.modelHandle, handle);
	OS.gtk_widget_realize (parentHandle);
	GdkRectangle rect = new GdkRectangle ();
	OS.gtk_tree_view_get_cell_area (parentHandle, path, column, rect);
	OS.gtk_tree_path_free (path);
	
	if (OS.gtk_tree_view_get_expander_column (parentHandle) == column) {
		int [] buffer = new int [1];
		OS.gtk_widget_style_get (parentHandle, OS.expander_size, buffer, 0);
		rect.x += buffer [0] + TreeItem.EXPANDER_EXTRA_PADDING;
		rect.width -= buffer [0] + TreeItem.EXPANDER_EXTRA_PADDING;
		OS.gtk_widget_style_get (parentHandle, OS.horizontal_separator, buffer, 0);
		rect.x += buffer [0];
		//rect.width -= buffer [0]; // TODO Is this required for some versions?
	}
	
	if (index == 0 && (parent.style & SWT.CHECK) != 0) {
		if (OS.GTK_VERSION >= OS.VERSION (2, 1, 3)) {
			int [] x = new int [1], w = new int [1];
			OS.gtk_tree_view_column_cell_get_position (column, parent.checkRenderer, x, w);
			rect.x += x [0] + w [0];
			rect.width -= x [0] + w [0];
		} else {
			int [] w = new int [1];
			OS.gtk_cell_renderer_get_size (parent.checkRenderer, parentHandle, null, null, null, w, null);
			int [] buffer = new int [1];
			OS.gtk_widget_style_get (parentHandle, OS.horizontal_separator, buffer, 0);
			rect.x += w [0]  + buffer [0];
			rect.width -= w [0]  + buffer [0];
		}
	}
	
	/* 
	 * In the horizontal direction, the origin of the bin window is 
	 * not the same as the origin of the scrolled handle.
	 * The method gtk_tree_view_get_cell_area returns the 
	 * x coordinate relative to the bin window.  In order to
	 * get the coordinates relative to the top left corner
	 * of the client area,  we need to account for the
	 * horizontal scroll adjustment.
	 */
	int[] wx = new int[1];
	OS.gtk_tree_view_tree_to_widget_coords(parentHandle, rect.x, 0, wx, null);
	rect.x = wx[0];
	
	int border = parent.getBorderWidth ();
	int headerHeight = parent.getHeaderHeight ();
	return new Rectangle (rect.x + (index == 0 ? 0 : border), rect.y + (headerHeight == 0 ?  border : headerHeight), rect.width + 1, rect.height + 1);
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	// TODO fully test on early and later versions of GTK
	// shifted a bit too far right on later versions of GTK - however, old Tree also had this problem
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	int /*long*/ parentHandle = parent.handle;
	int /*long*/ column = OS.gtk_tree_view_get_column (parentHandle, 0);
	if (column == 0) return new Rectangle (0, 0, 0, 0);
	int /*long*/ textRenderer = parent.getTextRenderer (column);
	int /*long*/ pixbufRenderer = parent.getPixbufRenderer (column);
	if (textRenderer == 0 || pixbufRenderer == 0)  return new Rectangle (0, 0, 0, 0);

	int /*long*/ path = OS.gtk_tree_model_get_path (parent.modelHandle, handle);
	OS.gtk_widget_realize (parentHandle);
	
	boolean isExpander = OS.gtk_tree_model_iter_n_children (parent.modelHandle, handle) > 0;
	boolean isExpanded = OS.gtk_tree_view_row_expanded (parentHandle, path);
	OS.gtk_tree_view_column_cell_set_cell_data (column, parent.modelHandle, handle, isExpander, isExpanded);
	
	GdkRectangle rect = new GdkRectangle ();
	OS.gtk_tree_view_get_cell_area (parentHandle, path, column, rect);
	OS.gtk_tree_path_free (path);
	int right = rect.x + rect.width;

	int [] x = new int [1], w = new int [1];
	OS.gtk_cell_renderer_get_size (textRenderer, parentHandle, null, null, null, w, null);
	rect.width = w [0];
	int [] buffer = new int [1];
	if (OS.gtk_tree_view_get_expander_column (parentHandle) == column) {
		OS.gtk_widget_style_get (parentHandle, OS.expander_size, buffer, 0);
		rect.x += buffer [0] + TreeItem.EXPANDER_EXTRA_PADDING;
	}
	OS.gtk_widget_style_get (parentHandle, OS.horizontal_separator, buffer, 0);
	int horizontalSeparator = buffer[0];
	rect.x += horizontalSeparator;
	
	if (OS.GTK_VERSION >= OS.VERSION (2, 1, 3)) {
		OS.gtk_tree_view_column_cell_get_position (column, textRenderer, x, null);
		rect.x += x [0];
	} else {
		if ((parent.style & SWT.CHECK) != 0) {
			OS.gtk_cell_renderer_get_size (parent.checkRenderer, parentHandle, null, null, null, w, null);
			rect.x += w [0] + horizontalSeparator;
		}
		OS.gtk_cell_renderer_get_size (pixbufRenderer, parentHandle, null, null, null, w, null);
		rect.x += w [0] + horizontalSeparator;
	}
	
	/* 
	 * In the horizontal direction, the origin of the bin window is 
	 * not the same as the origin of the scrolled handle.
	 * The method gtk_tree_view_get_cell_area returns the 
	 * x coordinate relative to the bin window.  In order to
	 * get the coordinates relative to the top left corner
	 * of the client area,  we need to account for the
	 * horizontal scroll adjustment.
	 */
	int[] wx = new int[1];
	OS.gtk_tree_view_tree_to_widget_coords(parentHandle, rect.x, 0, wx, null);
	rect.x = wx[0];

	int border = parent.getBorderWidth ();
	int headerHeight = parent.getHeaderHeight ();
	if (parent.columnCount > 0) {
		if (rect.x + border + rect.width > right) {
			rect.width = Math.max (0, right - rect.x - border);
		}
	}
	return new Rectangle (rect.x + border, rect.y + (headerHeight == 0 ?  border : headerHeight), rect.width + 1, rect.height + 1);
}

/**
 * Returns <code>true</code> if the receiver is checked,
 * and false otherwise.  When the parent does not have
 * the <code>CHECK</code> style, return false.
 * <p>
 *
 * @return the checked state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getChecked () {
	checkWidget();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((parent.style & SWT.CHECK) == 0) return false;
	int /*long*/ [] ptr = new int /*long*/ [1];
	OS.gtk_tree_model_get (parent.modelHandle, handle, Tree.CHECKED_COLUMN, ptr, -1);
	return ptr [0] != 0;
}

/**
 * Returns <code>true</code> if the receiver is expanded,
 * and false otherwise.
 * <p>
 *
 * @return the expanded state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getExpanded () {
	checkWidget();
	int /*long*/ path = OS.gtk_tree_model_get_path (parent.modelHandle, handle);
	boolean answer = OS.gtk_tree_view_row_expanded (parent.handle, path);
	OS.gtk_tree_path_free (path);
	return answer;
}

/**
 * Returns the font that the receiver will use to paint textual information for this item.
 *
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.0
 */
public Font getFont () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	return font != null ? font : parent.getFont ();
}

/**
 * Returns the font that the receiver will use to paint textual information
 * for the specified cell in this item.
 *
 * @param index the column index
 * @return the receiver's font
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public Font getFont (int index) {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	int count = Math.max (1, parent.columnCount);
	if (0 > index || index > count - 1) return getFont ();
	if (cellFont == null || cellFont [index] == null) return getFont ();
	return cellFont [index];
}


/**
 * Returns the foreground color that the receiver will use to draw.
 *
 * @return the receiver's foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 *
 */
public Color getForeground () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	int /*long*/ [] ptr = new int /*long*/ [1];
	OS.gtk_tree_model_get (parent.modelHandle, handle, Tree.FOREGROUND_COLUMN, ptr, -1);
	if (ptr [0] == 0) return parent.getForeground ();
	GdkColor gdkColor = new GdkColor ();
	OS.memmove (gdkColor, ptr [0], GdkColor.sizeof);
	return Color.gtk_new (display, gdkColor);
}

/**
 * 
 * Returns the foreground color at the given column index in the receiver.
 *
 * @param index the column index
 * @return the foreground color
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public Color getForeground (int index) {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	int count = Math.max (1, parent.columnCount);
	if (0 > index || index > count - 1) return getForeground ();
	int /*long*/ [] ptr = new int /*long*/ [1];
	int modelIndex =  parent.columnCount == 0 ? Tree.FIRST_COLUMN : parent.columns [index].modelIndex;
	OS.gtk_tree_model_get (parent.modelHandle, handle, modelIndex + Tree.CELL_FOREGROUND, ptr, -1);
	if (ptr [0] == 0) return getForeground ();
	GdkColor gdkColor = new GdkColor ();
	OS.memmove (gdkColor, ptr [0], GdkColor.sizeof);
	return Color.gtk_new (display, gdkColor);
}

/**
 * Returns <code>true</code> if the receiver is grayed,
 * and false otherwise. When the parent does not have
 * the <code>CHECK</code> style, return false.
 *
 * @return the grayed state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getGrayed () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((parent.style & SWT.CHECK) == 0) return false;
	return grayed;
}

public Image getImage () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	return getImage (0);
}

/**
 * Returns the image stored at the given column index in the receiver,
 * or null if the image has not been set or if the column does not exist.
 *
 * @param index the column index
 * @return the image stored at the given column index in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public Image getImage (int index) {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return null;
	int /*long*/ [] ptr = new int /*long*/ [1];
	int modelIndex = parent.columnCount == 0 ? Tree.FIRST_COLUMN : parent.columns [index].modelIndex;
	OS.gtk_tree_model_get (parent.modelHandle, handle, modelIndex + Tree.CELL_PIXBUF, ptr, -1);
	if (ptr [0] == 0) return null;
	ImageList imageList = parent.imageList;
	int imageIndex = imageList.indexOf (ptr [0]);
	if (imageIndex == -1) return null;
	return imageList.get (imageIndex);
}

/**
 * Returns a rectangle describing the size and location
 * relative to its parent of an image at a column in the
 * tree.
 *
 * @param index the index that specifies the column
 * @return the receiver's bounding image rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public Rectangle getImageBounds (int index) {
	// TODO fully test on early and later versions of GTK
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	int /*long*/ parentHandle = parent.handle;
	int /*long*/ column = 0;
	if (index >= 0 && index < parent.getColumnCount ()) {
		column = parent.columns [index].handle;
	} else {
		column = OS.gtk_tree_view_get_column (parentHandle, index);
	}
	if (column == 0) return new Rectangle (0, 0, 0, 0);
	int /*long*/ pixbufRenderer = parent.getPixbufRenderer (column);
	if (pixbufRenderer == 0)  return new Rectangle (0, 0, 0, 0);
	GdkRectangle rect = new GdkRectangle ();
	int /*long*/ path = OS.gtk_tree_model_get_path (parent.modelHandle, handle);
	OS.gtk_widget_realize (parentHandle);
	OS.gtk_tree_view_get_cell_area (parentHandle, path, column, rect);
	OS.gtk_tree_path_free (path);
	
	if (OS.gtk_tree_view_get_expander_column (parentHandle) == column) {
		int [] buffer = new int [1];
		OS.gtk_widget_style_get (parentHandle, OS.expander_size, buffer, 0);
		rect.x += buffer [0] + TreeItem.EXPANDER_EXTRA_PADDING;
		rect.width -= buffer [0] + TreeItem.EXPANDER_EXTRA_PADDING;
		//OS.gtk_widget_style_get (parentHandle, OS.horizontal_separator, buffer, 0);
		//int horizontalSeparator = buffer[0];
		//rect.x += horizontalSeparator;
	}
	
	/*
	* The OS call gtk_cell_renderer_get_size() provides the width of image to be drawn
	* by the cell renderer.  If there is no image in the cell, the width is zero.  If the table contains
	* images of varying widths, gtk_cell_renderer_get_size() will return the width of the image, 
	* not the width of the area in which the image is drawn.
	* New API was added in GTK 2.1.3 for determining the full width of the renderer area.
	* For earlier versions of GTK, the result is only correct if all rows have images of the same
	* width.
	*/
	if (OS.GTK_VERSION >= OS.VERSION (2, 1, 3)) {
		int [] x = new int [1], w = new int[1];
		OS.gtk_tree_view_column_cell_get_position (column, pixbufRenderer, x, w);
		rect.x += x [0];
		rect.width = w [0];
	} else {
		int [] w = new int [1];
		OS.gtk_tree_view_column_cell_set_cell_data (column, parent.modelHandle, handle, false, false);
		OS.gtk_cell_renderer_get_size (pixbufRenderer, parentHandle, null, null, null, w, null);
		rect.width = w [0];
	}
	
	/* 
	 * In the horizontal direction, the origin of the bin window is 
	 * not the same as the origin of the scrolled handle.
	 * The method gtk_tree_view_get_cell_area returns the 
	 * x coordinate relative to the bin window.  In order to
	 * get the coordinates relative to the top left corner
	 * of the client area,  we need to account for the
	 * horizontal scroll adjustment.
	 */
	int[] wx = new int[1];
	OS.gtk_tree_view_tree_to_widget_coords(parentHandle, rect.x, 0, wx, null);
	rect.x = wx[0];

	int border = parent.getBorderWidth ();
	int headerHeight = parent.getHeaderHeight ();
	return new Rectangle (rect.x + (index == 0 ? 0 : border), rect.y + (headerHeight == 0 ?  border : headerHeight), rect.width, rect.height + 1);
}

/**
 * Returns the number of items contained in the receiver
 * that are direct item children of the receiver.
 *
 * @return the number of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget();
	return OS.gtk_tree_model_iter_n_children (parent.modelHandle, handle);
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
	checkWidget();
	return  parent._getItem (handle, index);
}

/**
 * Returns a (possibly empty) array of <code>TreeItem</code>s which
 * are the direct item children of the receiver.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the receiver's items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TreeItem [] getItems () {
	checkWidget();
	return parent.getItems (handle);
}

String getNameText () {
	if ((parent.style & SWT.VIRTUAL) != 0) {
		if (!cached) return "*virtual*"; //$NON-NLS-1$
	}
	return super.getNameText ();
}

/**
 * Returns the receiver's parent, which must be a <code>Tree</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Tree getParent () {
	checkWidget ();
	return parent;
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
	checkWidget();
	int /*long*/ path = OS.gtk_tree_model_get_path (parent.modelHandle, handle);
	TreeItem item = null;
	int depth = OS.gtk_tree_path_get_depth (path); 
	if (depth > 1) {
		OS.gtk_tree_path_up (path);
		int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
		if (OS.gtk_tree_model_get_iter (parent.modelHandle, iter, path)) {
			item = parent._getItem (iter);
		}
		OS.g_free (iter);
	}
	OS.gtk_tree_path_free (path);
	return item;
}

public String getText () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	return getText (0);
}

/**
 * Returns the text stored at the given column index in the receiver,
 * or empty string if the text has not been set.
 *
 * @param index the column index
 * @return the text stored at the given column index in the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public String getText (int index) {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return "";
	int /*long*/ [] ptr = new int /*long*/ [1];
	int modelIndex = parent.columnCount == 0 ? Tree.FIRST_COLUMN : parent.columns [index].modelIndex;
	OS.gtk_tree_model_get (parent.modelHandle, handle, modelIndex + Tree.CELL_TEXT, ptr, -1);
	if (ptr [0] == 0) return ""; //$NON-NLS-1$
	int length = OS.strlen (ptr [0]);
	byte[] buffer = new byte [length];
	OS.memmove (buffer, ptr [0], length);
	OS.g_free (ptr [0]);
	return new String (Converter.mbcsToWcs (null, buffer));
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
 *    <li>ERROR_NULL_ARGUMENT - if the tool item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the tool item has been disposed</li>
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
	boolean isParent = false;
	int /*long*/ currentPath = OS.gtk_tree_model_get_path (parent.modelHandle, handle);
	int /*long*/ parentPath = OS.gtk_tree_model_get_path (parent.modelHandle, item.handle);
	int depth = OS.gtk_tree_path_get_depth (parentPath);
	if (depth > 1 && OS.gtk_tree_path_up(parentPath)) {
		if (OS.gtk_tree_path_compare(currentPath, parentPath) == 0) isParent = true;
	}
	OS.gtk_tree_path_free (currentPath);
	OS.gtk_tree_path_free (parentPath);
	if (!isParent) return index;
	int /*long*/ path = OS.gtk_tree_model_get_path (parent.modelHandle, item.handle);
	if (depth > 1) {
		int /*long*/ indices = OS.gtk_tree_path_get_indices (path);
		if (indices != 0) {	
			int[] temp = new int[depth];
			OS.memmove (temp, indices, 4 * temp.length);
			index = temp[temp.length - 1];
		}
	}
	OS.gtk_tree_path_free (path);
	return index;
}

void releaseChildren (boolean destroy) {
	if (destroy) {
		parent.releaseItems (handle);
	}
	super.releaseChildren (destroy);
}

void releaseHandle () {
	if (handle != 0) OS.g_free (handle);
	handle = 0;
	super.releaseHandle ();
	parent = null;
}

void releaseWidget () {
	super.releaseWidget ();
	font = null;
	cellFont = null;
}

/**
 * Removes all of the items from the receiver.
 * <p>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.1
 */
public void removeAll () {
	checkWidget ();
	int length = OS.gtk_tree_model_iter_n_children (parent.modelHandle, handle);
	if (length == 0) return;
	int /*long*/ iter = OS.g_malloc (OS.GtkTreeIter_sizeof ());
	int [] index = new int [1];
	while (OS.gtk_tree_model_iter_children (parent.modelHandle, iter, handle)) {
		OS.gtk_tree_model_get (parent.modelHandle, iter, Tree.ID_COLUMN, index, -1);
		if (index [0] != -1) {
			TreeItem item = parent.items [index [0]];
			if (item != null && !item.isDisposed ()) {
				item.dispose ();
			}
		}
	}
	OS.g_free (iter);
}

/**
 * Sets the receiver's background color to the color specified
 * by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param color the new color (or null)
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 * 
 */
public void setBackground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	GdkColor gdkColor = color != null ? color.handle : null;
	OS.gtk_tree_store_set (parent.modelHandle, handle, Tree.BACKGROUND_COLUMN, gdkColor, -1);
	cached = true;
}

/**
 * Sets the background color at the given column index in the receiver 
 * to the color specified by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param index the column index
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 * 
 */
public void setBackground (int index, Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	int modelIndex = parent.columnCount == 0 ? Tree.FIRST_COLUMN : parent.columns [index].modelIndex;
	GdkColor gdkColor = color != null ? color.handle : null;
	OS.gtk_tree_store_set (parent.modelHandle, handle, modelIndex + Tree.CELL_BACKGROUND, gdkColor, -1);
	cached = true;
	
	if (color != null) {
		boolean customDraw = (parent.columnCount == 0)  ? parent.firstCustomDraw : parent.columns [index].customDraw;
		if (!customDraw) {
			if ((parent.style & SWT.VIRTUAL) == 0) {
				int /*long*/ parentHandle = parent.handle;
				int /*long*/ column = 0;
				if (parent.columnCount > 0) {
					column = parent.columns [index].handle;
				} else {
					column = OS.gtk_tree_view_get_column (parentHandle, index);
				}
				if (column == 0) return;
				int /*long*/ textRenderer = parent.getTextRenderer (column);
				int /*long*/ imageRenderer = parent.getPixbufRenderer (column);
				OS.gtk_tree_view_column_set_cell_data_func (column, textRenderer, display.textCellDataProc, parentHandle, 0);
				OS.gtk_tree_view_column_set_cell_data_func (column, imageRenderer, display.pixbufCellDataProc, parentHandle, 0);
			}
			if (parent.columnCount == 0) {
				parent.firstCustomDraw = true;
			} else {
				parent.columns [index].customDraw = true;
			}
		}
	}
}

/**
 * Sets the checked state of the receiver.
 * <p>
 *
 * @param checked the new checked state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setChecked (boolean checked) {
	checkWidget();
	if ((parent.style & SWT.CHECK) == 0) return;
	OS.gtk_tree_store_set (parent.modelHandle, handle, Tree.CHECKED_COLUMN, checked, -1);
	/*
	* GTK+'s "inconsistent" state does not match SWT's concept of grayed.  To
	* show checked+grayed differently from unchecked+grayed, we must toggle the
	* grayed state on check and uncheck. 
	*/
	OS.gtk_tree_store_set (parent.modelHandle, handle, Tree.GRAYED_COLUMN, !checked ? false : grayed, -1);
	cached = true;
}

/**
 * Sets the expanded state of the receiver.
 * <p>
 *
 * @param expanded the new expanded state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setExpanded (boolean expanded) {
	checkWidget();
	int /*long*/ path = OS.gtk_tree_model_get_path (parent.modelHandle, handle);
	if (expanded) {
		OS.g_signal_handlers_block_matched (parent.handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_EXPAND_ROW);
		OS.gtk_tree_view_expand_row (parent.handle, path, false);
		OS.g_signal_handlers_unblock_matched (parent.handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_EXPAND_ROW);
	} else {
		OS.g_signal_handlers_block_matched (parent.handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_COLLAPSE_ROW);
		OS.gtk_widget_realize (parent.handle);
		OS.gtk_tree_view_collapse_row (parent.handle, path);
		OS.g_signal_handlers_unblock_matched (parent.handle, OS.G_SIGNAL_MATCH_DATA, 0, 0, 0, 0, TEST_COLLAPSE_ROW);
	}
	OS.gtk_tree_path_free (path);
	cached = true;
}


/**
 * Sets the font that the receiver will use to paint textual information
 * for this item to the font specified by the argument, or to the default font
 * for that kind of control if the argument is null.
 *
 * @param font the new font (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setFont (Font font){
	checkWidget ();
	if (font != null && font.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (this.font == font) return;
	if (this.font != null && this.font.equals (font)) return;
	this.font = font;
	int /*long*/ fontHandle = font != null ? font.handle : 0;
	OS.gtk_tree_store_set (parent.modelHandle, handle, Tree.FONT_COLUMN, fontHandle, -1);
	cached = true;
}

/**
 * Sets the font that the receiver will use to paint textual information
 * for the specified cell in this item to the font specified by the 
 * argument, or to the default font for that kind of control if the 
 * argument is null.
 *
 * @param index the column index
 * @param font the new font (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setFont (int index, Font font) {
	checkWidget ();
	if (font != null && font.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	if (cellFont == null) {
		cellFont = new Font [count];
	}
	if (cellFont [index] == font) return;
	if (cellFont [index] != null && cellFont [index].equals (font)) return;
	cellFont [index] = font;
	
	int modelIndex = parent.columnCount == 0 ? Tree.FIRST_COLUMN : parent.columns [index].modelIndex;
	int /*long*/ fontHandle  = font != null ? font.handle : 0;
	OS.gtk_tree_store_set (parent.modelHandle, handle, modelIndex + Tree.CELL_FONT, fontHandle, -1);
	cached = true;
	
	if (font != null) {
		boolean customDraw = (parent.columnCount == 0)  ? parent.firstCustomDraw : parent.columns [index].customDraw;
		if (!customDraw) {
			if ((parent.style & SWT.VIRTUAL) == 0) {
				int /*long*/ parentHandle = parent.handle;
				int /*long*/ column = 0;
				if (parent.columnCount > 0) {
					column = parent.columns [index].handle;
				} else {
					column = OS.gtk_tree_view_get_column (parentHandle, index);
				}
				if (column == 0) return;
				int /*long*/ textRenderer = parent.getTextRenderer (column);
				int /*long*/ imageRenderer = parent.getPixbufRenderer (column);
				OS.gtk_tree_view_column_set_cell_data_func (column, textRenderer, display.textCellDataProc, parentHandle, 0);
				OS.gtk_tree_view_column_set_cell_data_func (column, imageRenderer, display.pixbufCellDataProc, parentHandle, 0);
			}
			if (parent.columnCount == 0) {
				parent.firstCustomDraw = true;
			} else {
				parent.columns [index].customDraw = true;
			}
		}
	}
}

/**
 * Sets the receiver's foreground color to the color specified
 * by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param color the new color (or null)
 *
 * @since 2.0
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.0
 */
public void setForeground (Color color){
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	GdkColor gdkColor = color != null ? color.handle : null;
	OS.gtk_tree_store_set (parent.modelHandle, handle, Tree.FOREGROUND_COLUMN, gdkColor, -1);
	cached = true;
}

/**
 * Sets the foreground color at the given column index in the receiver 
 * to the color specified by the argument, or to the default system color for the item
 * if the argument is null.
 *
 * @param index the column index
 * @param color the new color (or null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument has been disposed</li> 
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 * 
 */
public void setForeground (int index, Color color){
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	int modelIndex = parent.columnCount == 0 ? Tree.FIRST_COLUMN : parent.columns [index].modelIndex;
	GdkColor gdkColor = color != null ? color.handle : null;
	OS.gtk_tree_store_set (parent.modelHandle, handle, modelIndex + Tree.CELL_FOREGROUND, gdkColor, -1);
	cached = true;
	
	if (color != null) {
		boolean customDraw = (parent.columnCount == 0)  ? parent.firstCustomDraw : parent.columns [index].customDraw;
		if (!customDraw) {
			if ((parent.style & SWT.VIRTUAL) == 0) {
				int /*long*/ parentHandle = parent.handle;
				int /*long*/ column = 0;
				if (parent.columnCount > 0) {
					column = parent.columns [index].handle;
				} else {
					column = OS.gtk_tree_view_get_column (parentHandle, index);
				}
				if (column == 0) return;
				int /*long*/ textRenderer = parent.getTextRenderer (column);
				int /*long*/ imageRenderer = parent.getPixbufRenderer (column);
				OS.gtk_tree_view_column_set_cell_data_func (column, textRenderer, display.textCellDataProc, parentHandle, 0);
				OS.gtk_tree_view_column_set_cell_data_func (column, imageRenderer, display.pixbufCellDataProc, parentHandle, 0);
			}
			if (parent.columnCount == 0) {
				parent.firstCustomDraw = true;
			} else {
				parent.columns [index].customDraw = true;
			}
		}
	}
}

/**
 * Sets the grayed state of the receiver.
 * <p>
 *
 * @param grayed the new grayed state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setGrayed (boolean grayed) {
	checkWidget();
	if ((parent.style & SWT.CHECK) == 0) return;
	this.grayed = grayed;
	/*
	* GTK+'s "inconsistent" state does not match SWT's concept of grayed.
	* Render checked+grayed as "inconsistent", unchecked+grayed as blank.
	*/
	int /*long*/ [] ptr = new int /*long*/ [1];
	OS.gtk_tree_model_get (parent.modelHandle, handle, Tree.CHECKED_COLUMN, ptr, -1);
	OS.gtk_tree_store_set (parent.modelHandle, handle, Tree.GRAYED_COLUMN, ptr [0] == 0 ? false : grayed, -1);
	cached = true;
}

/**
 * Sets the receiver's image at a column.
 *
 * @param index the column index
 * @param image the new image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setImage (int index, Image image) {
	checkWidget ();
	if (image != null && image.isDisposed()) {
		error(SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	int /*long*/ pixbuf = 0;
	if (image != null) {
		ImageList imageList = parent.imageList;
		if (imageList == null) imageList = parent.imageList = new ImageList ();
		int imageIndex = imageList.indexOf (image);
		if (imageIndex == -1) imageIndex = imageList.add (image);
		pixbuf = imageList.getPixbuf (imageIndex);
	}
	int modelIndex = parent.columnCount == 0 ? Tree.FIRST_COLUMN : parent.columns [index].modelIndex;
	OS.gtk_tree_store_set (parent.modelHandle, handle, modelIndex + Tree.CELL_PIXBUF, pixbuf, -1);
	cached = true;
}

public void setImage (Image image) {
	checkWidget ();
	setImage (0, image);
}

/**
 * Sets the image for multiple columns in the tree. 
 * 
 * @param images the array of new images
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of images is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if one of the images has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setImage (Image [] images) {
	checkWidget ();
	if (images == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<images.length; i++) {
		setImage (i, images [i]);
	}
}

/*public*/ void setItemCount (int count) {
	checkWidget ();
	count = Math.max (0, count);
	parent.setItemCount (handle, count);
}

/**
 * Sets the receiver's text at a column
 *
 * @param index the column index
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setText (int index, String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	byte[] buffer = Converter.wcsToMbcs (null, string, true);
	int modelIndex = parent.columnCount == 0 ? Tree.FIRST_COLUMN : parent.columns [index].modelIndex;
	OS.gtk_tree_store_set (parent.modelHandle, handle, modelIndex + Tree.CELL_TEXT, buffer, -1);
	cached = true;
}

public void setText (String string) {
	checkWidget ();
	setText (0, string);
}

/**
 * Sets the text for multiple columns in the tree. 
 * 
 * @param strings the array of new strings
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public void setText (String [] strings) {
	checkWidget ();
	if (strings == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<strings.length; i++) {
		String string = strings [i];
		if (string != null) setText (i, string);
	}
}
}
