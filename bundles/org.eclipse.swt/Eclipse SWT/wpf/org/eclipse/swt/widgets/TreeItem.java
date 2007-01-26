/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import org.eclipse.swt.internal.wpf.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class represent a selectable user interface object
 * that represents a hierarchy of tree items in a tree widget.
 * 
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
	int [] stringHandle;
	Tree parent;
	String [] strings;
	Image [] images;
	int itemCount;
	boolean checked, grayed, cached;
	
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
	this (checkNull (parent), null, style, parent.itemCount, 0);
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
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
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
	this (checkNull (parent), null, style, index, 0);
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
	this (checkNull (parentItem).parent, parentItem, style, parentItem.itemCount, 0);
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
 * @param index the zero-relative index to store the receiver in its parent
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the parent (inclusive)</li>
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
	this (checkNull (parentItem).parent, parentItem, style, index, 0);
}

TreeItem (Tree parent, TreeItem parentItem, int style, int index, int handle) {
	super (parent, style);
	this.parent = parent;
	this.handle = handle;
	if (handle == 0) {
		parent.createItem (this, parentItem, index);
	} else {
		createWidget ();
	}
}

static Tree checkNull (Tree tree) {
	if (tree == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return tree;
}

static TreeItem checkNull (TreeItem item) {
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return item;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void clear () {
	text = "";
	image = null;
	strings = null;
	images = null;
//	background = foreground = font = -1;
//	cellBackground = cellForeground = cellFont = null;
//	if ((parent.style & SWT.VIRTUAL) != 0) cached = false;
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
public void clear (int index, boolean all) {
	checkWidget ();
//	int hwnd = parent.handle;
//	int hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, handle);
//	if (hItem == 0) error (SWT.ERROR_INVALID_RANGE);
//	hItem = parent.findItem (hItem, index);
//	if (hItem == 0) error (SWT.ERROR_INVALID_RANGE);
//	TVITEM tvItem = new TVITEM ();
//	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
//	parent.clear (hItem, tvItem);
//	if (all) {
//		hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, hItem);
//		parent.clearAll (hItem, tvItem, all);
//	}
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
//	int hwnd = parent.handle;
//	int hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_CHILD, handle);
//	if (hItem == 0) return;
//	TVITEM tvItem = new TVITEM ();
//	tvItem.mask = OS.TVIF_HANDLE | OS.TVIF_PARAM;
//	parent.clearAll (hItem, tvItem, all);
}

void columnInserted(int index) {
	int newLength = parent.columnCount + 1;
	if (strings != null && strings.length == newLength) return;
	if (strings != null) {
		String [] temp = new String [newLength];
		System.arraycopy (strings, 0, temp, 0, index);
		System.arraycopy (strings, index, temp, index + 1, parent.columnCount -  index);
		strings = temp;
	}
	if (stringHandle != null) {
		int [] temp = new int [newLength];
		System.arraycopy (stringHandle, 0, temp, 0, index);
		System.arraycopy (stringHandle, index, temp, index + 1, parent.columnCount -  index);
		stringHandle = temp;
	}
	if (images != null) {
		Image [] temp = new Image [newLength];
		System.arraycopy (images, 0, temp, 0, index);
		System.arraycopy (images, index, temp, index + 1, parent.columnCount - index);
		images = temp;
	}
	int items = OS.ItemsControl_Items (handle);
    for (int i=0; i<itemCount; i++) {
		TreeItem item = parent.getItem (items, i, false);
		if (item != null) {
			item.columnInserted (index);
		}
    }
    OS.GCHandle_Free (items);
}

public void columnRemoved(int index) {
	if (strings != null) {
		String [] temp = new String [parent.columnCount];
		System.arraycopy (strings, 0, temp, 0, index);
		System.arraycopy (strings, index + 1, temp, index, parent.columnCount - index);
		strings = temp;
	}
	if (stringHandle != null) {
		int [] temp = new int [parent.columnCount];
		System.arraycopy (stringHandle, 0, temp, 0, index);
		System.arraycopy (stringHandle, index + 1, temp, index, parent.columnCount - index);
		if (stringHandle [index] != 0) OS.GCHandle_Free (stringHandle [index]);
		stringHandle = temp;
	}
	if (images != null) {
		Image [] temp = new Image [parent.columnCount];
		System.arraycopy (images, 0, temp, 0, index);
		System.arraycopy (images, index + 1, temp, index, parent.columnCount - index);
		images = temp;
	}
	int items = OS.ItemsControl_Items (handle);
    for (int i=0; i<itemCount; i++) {
		TreeItem item = parent.getItem (items, i, false);
		if (item != null) {
			item.columnRemoved (index);
		}
    }
    OS.GCHandle_Free (items);
}

void createHandle () {
	int headerHandle;
	if (handle == 0) {
		handle = OS.gcnew_TreeViewItem ();
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		headerHandle = OS.gcnew_SWTTreeViewRowPresenter (parent.handle);
		if (headerHandle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.GridViewRowPresenterBase_Columns (headerHandle, parent.columns);
		OS.HeaderedItemsControl_Header (handle, headerHandle);
	} else {
		headerHandle = OS.HeaderedItemsControl_Header (handle);
	}
	int content = OS.gcnew_Point (jniRef, 0);
	OS.GridViewRowPresenter_Content (headerHandle, content);
	OS.GCHandle_Free (headerHandle);
	OS.GCHandle_Free (content);
}

void deregister () {
	display.removeWidget (handle);
}

void destroyWidget () {
	parent.destroyItem (this);
	super.destroyWidget ();
}

int findContentPresenter (int element, int column) {
	int controlTemplate = OS.Control_Template (handle);
	int headerName = createDotNetString ("PART_Header", false);
	int partHeader = OS.FrameworkTemplate_FindName (controlTemplate, headerName, handle);
	//TODO - test this on classic look
	int rowPresenter = OS.VisualTreeHelper_GetChild (partHeader, 0);
	int contentPresenter = OS.VisualTreeHelper_GetChild (rowPresenter, column);
	OS.GCHandle_Free (partHeader);
	OS.GCHandle_Free (headerName);
	OS.GCHandle_Free (controlTemplate);
	OS.GCHandle_Free(rowPresenter);
	return contentPresenter;
}

int findPart (int column, String part) {
	if (!OS.FrameworkElement_IsLoaded (handle)) return 0;
	if (OS.UIElement_Visibility (handle) != OS.Visibility_Visible) return 0;
	int contentPresenter = findContentPresenter (handle, column);
	int columnHandle = OS.GridViewColumnCollection_default (parent.columns, column);
	int cellTemplate = OS.GridViewColumn_CellTemplate (columnHandle);
	int name = createDotNetString (part, false);
	int result = OS.FrameworkTemplate_FindName (cellTemplate, name, contentPresenter);
	OS.GCHandle_Free (columnHandle);
	if (name != 0) OS.GCHandle_Free (name);
	OS.GCHandle_Free (cellTemplate);
	OS.GCHandle_Free (contentPresenter);
	return result;
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
 * 
 */
public Color getBackground () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	return parent.getBackground ();
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
	return parent.getBackground ();
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
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	//BUG?
	if (!OS.UIElement_IsVisible(handle)) return new Rectangle (0, 0, 0, 0);
	int point = OS.gcnew_Point (0, 0);
	int location = OS.UIElement_TranslatePoint (handle, point, parent.handle);
	int x = (int) OS.Point_X (location);
	int y = (int) OS.Point_Y (location);
	OS.GCHandle_Free (point);
	OS.GCHandle_Free (location);
	int width = (int) OS.FrameworkElement_ActualWidth (handle);
	int height = (int) OS.FrameworkElement_ActualHeight (handle);
	return new Rectangle (x, y, width, height);
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
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (!(0 <= index && index < parent.columnCount)) return new Rectangle (0, 0, 0, 0);
	if (!OS.UIElement_IsVisible (handle)) return new Rectangle (0, 0, 0, 0);
	int point = OS.gcnew_Point (0, 0);
	OS.UIElement_UpdateLayout (handle);
	int contentPresenter = findContentPresenter (handle, index);
	int location = OS.UIElement_TranslatePoint (contentPresenter, point, parent.handle);
	int x = (int) OS.Point_X (location);
	int y = (int) OS.Point_Y (location);
	OS.GCHandle_Free (point);
	OS.GCHandle_Free (location);
	int width = (int) OS.FrameworkElement_ActualWidth (contentPresenter);
	int height = (int) OS.FrameworkElement_ActualHeight (contentPresenter);
	return new Rectangle (x, y, width, height);
}

/**
 * Returns <code>true</code> if the receiver is checked,
 * and false otherwise.  When the parent does not have
 * the <code>CHECK style, return false.
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
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((parent.style & SWT.CHECK) == 0) return false;
	return checked;
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
	checkWidget ();
	return OS.TreeViewItem_IsExpanded (handle);
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
	return parent.getFont ();
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
	return parent.getFont ();
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
	return parent.getForeground ();
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
	return parent.getForeground ();
}

/**
 * Returns <code>true</code> if the receiver is grayed,
 * and false otherwise. When the parent does not have
 * the <code>CHECK style, return false.
 * <p>
 *
 * @return the grayed state of the checkbox
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
	return false;
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
	if (index < 0 || index >= itemCount) error (SWT.ERROR_INVALID_RANGE);	
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	int items = OS.ItemsControl_Items (handle);
	TreeItem item = parent.getItem (items, index, true);
	OS.GCHandle_Free (items);
	return item;
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
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	return itemCount;
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
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	int items = OS.ItemsControl_Items (handle);
	TreeItem [] result = new TreeItem [itemCount];
	for (int i = 0; i < result.length; i++) {
      	result [i] = parent.getItem (items, i, true);
	}
	OS.GCHandle_Free (items);
	return result;
}

public Image getImage () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	return super.getImage ();
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
	if (index == 0) return getImage ();
	if (images != null) {
		if (0 <= index && index < images.length) return images [index];
	}
	return null;
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
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	//TODO
	return new Rectangle (0, 0, 0, 0);
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
	checkWidget ();
	int parentItem = OS.FrameworkElement_Parent (handle);
	TreeItem result = null;
	if (!OS.Object_Equals (parentItem, parent.handle)) {
		result = (TreeItem) display.getWidget (parentItem);
	}
	OS.GCHandle_Free (parentItem);
	return result;
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
	if (strings != null) {
		if (0 <= index && index < strings.length) {
			String string = strings [index];
			return string != null ? string : "";
		}
	}
	return "";
}

Control getWidgetControl () {
	return parent;
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
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	int items = OS.ItemsControl_Items (handle);
	int index = OS.ItemCollection_IndexOf(items, item.handle);
	OS.GCHandle_Free (items);
	return index;
}

void register () {
	display.addWidget (handle, this);
}

void releaseChildren (boolean destroy) {
	int items = OS.ItemsControl_Items (handle);
	for (int i=0; i<itemCount; i++) {
		TreeItem item = parent.getItem (items, i, false);
		if (item != null && !item.isDisposed ()) item.release (false);
	}
	OS.GCHandle_Free (items);
	super.releaseChildren (destroy);	
}

void releaseHandle () {
	super.releaseHandle ();
	if (handle != 0) OS.GCHandle_Free (handle);
	handle = 0;
	parent = null;
}

void releaseWidget () {
	super.releaseWidget ();
	if (stringHandle != null) {
		for (int i = 0; i < stringHandle.length; i++) {
			if (stringHandle [i] != 0) OS.GCHandle_Free (stringHandle [i]);
		}
	}
	stringHandle = null;
	strings = null;
	images = null;
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
	int items = OS.ItemsControl_Items (handle);
	for (int i = 0; i < itemCount; i++) {
		TreeItem item = parent.getItem (items, i, false);
		if (item != null && !item.isDisposed ()) item.release (false);
	}
	OS.ItemCollection_Clear (items);
	OS.GCHandle_Free (items);
}

void resetImage (int index) {
	if (OS.FrameworkElement_IsLoaded (handle)) {
		int part = findPart (index, Table.IMAGE_PART_NAME);
		int property = OS.Image_SourceProperty ();
		int bindingExpression = OS.FrameworkElement_GetBindingExpression (part, property);
		OS.BindingExpression_UpdateTarget (bindingExpression);
		OS.GCHandle_Free (part);
		OS.GCHandle_Free (property);
		OS.GCHandle_Free (bindingExpression);
	}
}

void resetText (int index) {
	if (OS.FrameworkElement_IsLoaded (handle)) {
		int part = findPart (index, Table.TEXT_PART_NAME);
		int property = OS.TextBlock_TextProperty ();
		int bindingExpression = OS.FrameworkElement_GetBindingExpression (part, property);
		OS.BindingExpression_UpdateTarget (bindingExpression);
		OS.GCHandle_Free (part);
		OS.GCHandle_Free (property);
		OS.GCHandle_Free (bindingExpression);
	}
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
	//TODO
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
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
	//TODO
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
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
	checkWidget ();
	if ((parent.style & SWT.CHECK) == 0) return;
	//TODO
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
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
	checkWidget ();
	OS.TreeViewItem_IsExpanded (handle, expanded);
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
	//TODO
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
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
	//TODO
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
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
 * 
 */
public void setForeground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	//TODO
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
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
	//TODO
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
}

/**
 * Sets the grayed state of the checkbox for this item.  This state change 
 * only applies if the Tree was created with the SWT.CHECK style.
 *
 * @param grayed the new grayed state of the checkbox
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setGrayed (boolean grayed) {
	checkWidget ();
	if ((parent.style & SWT.CHECK) == 0) return;
	this.grayed = grayed;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
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
	checkWidget();
	if (image != null && image.isDisposed ()) {
		error(SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	if (images == null) {
		images = new Image [count];
	}
	images [index] = image;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	resetImage (index);
}

public void setImage (Image image) {
	checkWidget ();
	setImage (0, image);
}

/**
 * Sets the number of child items contained in the receiver.
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
	checkWidget ();
	parent.setItemCount (this, count);
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = Math.max(1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	
	if (strings == null) {
		strings = new String [count];
	}
	if (string.equals (strings [index])) return;
	strings [index] = string;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	if (stringHandle != null && stringHandle [index] != 0) {
		OS.GCHandle_Free (stringHandle [index]);
		stringHandle [index] = 0;
	}
	resetText (index);
}

public void setText (String string) {
	checkWidget ();
	setText (0, string);
}

}
