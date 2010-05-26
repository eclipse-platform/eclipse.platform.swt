/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tree">Tree, TreeItem, TreeColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class TreeItem extends Item {
	Tree parent;
	int contentHandle;
	int itemCount;
	Image [] images;
	String [] strings;
	Color [] cellBackground, cellForeground;
	Font [] cellFont;
	boolean checked, grayed, cached, ignoreNotify;
	Color background, foreground;
	Font font;
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
	strings = null;
	images = null;
	checked = grayed = false;
	updateCheck ();
	setForeground (null);
	setBackground (null);
	setFont (null);
	int columns = parent.columnCount == 0 ? 1 : parent.columnCount;
	for (int i = 0; i < columns; i++) {
		updateText (i);
		updateImage (i);
		updateBackground (i);
		updateForeground (i);
		updateFont (i);
	}
	if ((parent.style & SWT.VIRTUAL) != 0) cached = false;
	int part = findPart (0, Tree.RENDER_PANEL_NAME);
	if (part != 0) OS.UIElement_InvalidateVisual (part);
	OS.GCHandle_Free (part);
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
	if (index < 0 || index >= itemCount) SWT.error (SWT.ERROR_INVALID_RANGE);
	parent.clear (handle, index, all);
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
	parent.clearAll (this, all);
}

void columnAdded (int index) {
	if (parent.columnCount == 0) {
		int headerTemplate = OS.HeaderedItemsControl_HeaderTemplateProperty ();
		OS.DependencyObject_ClearValue (handle, headerTemplate);
		OS.GCHandle_Free (headerTemplate);
		int header = OS.gcnew_SWTTreeViewRowPresenter (parent.handle);
		if (header == 0) error (SWT.ERROR_NO_HANDLES);
		OS.GridViewRowPresenterBase_Columns (header, parent.gvColumns);
		OS.HeaderedItemsControl_Header (handle, header);
		OS.GCHandle_Free (header);
	} else {
		int newLength = parent.columnCount + 1;
		if (strings != null) {
			String [] temp = new String [newLength];
			System.arraycopy (strings, 0, temp, 0, index);
			System.arraycopy (strings, index, temp, index + 1, parent.columnCount - index);
			strings = temp;
		}
		if (images != null) {
			Image [] temp = new Image [newLength];
			System.arraycopy (images, 0, temp, 0, index);
			System.arraycopy (images, index, temp, index + 1, parent.columnCount - index);
			images = temp;
		}
		if (cellBackground != null) {
			Color [] temp = new Color [newLength];
			System.arraycopy (cellBackground, 0, temp, 0, index);
			System.arraycopy (cellBackground, index, temp, index + 1, parent.columnCount - index);
			cellBackground = temp;
		}
		if (cellForeground != null) {
			Color [] temp = new Color [newLength];
			System.arraycopy (cellForeground, 0, temp, 0, index);
			System.arraycopy (cellForeground, index, temp, index + 1, parent.columnCount - index);
			cellForeground = temp;
		}
		if (cellFont != null) {
			Font [] temp = new Font [newLength];
			System.arraycopy (cellFont, 0, temp, 0, index);
			System.arraycopy (cellFont, index, temp, index + 1, parent.columnCount - index);
			cellFont = temp;
		}
	}
	int items = OS.ItemsControl_Items (handle);
    for (int i=0; i<itemCount; i++) {
		TreeItem item = parent.getItem (items, i, false);
		if (item != null) {
			item.columnAdded (index);
		}
    }
    OS.GCHandle_Free (items);
}

void columnRemoved(int index) {
	if (parent.columnCount == 0) {
		OS.TreeViewItem_HeaderTemplate (handle, parent.headerTemplate);
	} else {
		if (strings == null) {
			String [] temp = new String [parent.columnCount];
			System.arraycopy (strings, 0, temp, 0, index);
			System.arraycopy (strings, index + 1, temp, index, parent.columnCount - index);
			strings = temp;
		}
		if (images != null) {
			Image [] temp = new Image [parent.columnCount];
			System.arraycopy (images, 0, temp, 0, index);
			System.arraycopy (images, index + 1, temp, index, parent.columnCount - index);
			images = temp;
		}
		if (cellBackground != null) {
			Color [] temp = new Color [parent.columnCount];
			System.arraycopy (cellBackground, 0, temp, 0, index);
			System.arraycopy (cellBackground, index + 1, temp, index, parent.columnCount - index);
			cellBackground = temp;
		}
		if (cellForeground != null) {
			Color [] temp = new Color [parent.columnCount];
			System.arraycopy (cellForeground, 0, temp, 0, index);
			System.arraycopy (cellForeground, index + 1, temp, index, parent.columnCount - index);
			cellForeground = temp;
		}
		if (cellFont != null) {
			Font [] temp = new Font [parent.columnCount];
			System.arraycopy (cellFont, 0, temp, 0, index);
			System.arraycopy (cellFont, index + 1, temp, index, parent.columnCount - index);
			cellFont = temp;
		}
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
	if (handle == 0) {
		handle = OS.gcnew_TreeViewItem ();
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		if (parent.columnCount != 0) {
			int headerHandle = OS.gcnew_SWTTreeViewRowPresenter (parent.handle);
			if (headerHandle == 0) error (SWT.ERROR_NO_HANDLES);
			OS.GridViewRowPresenterBase_Columns (headerHandle, parent.gvColumns);
			OS.HeaderedItemsControl_Header (handle, headerHandle);
			OS.GCHandle_Free (headerHandle);
		} else {
			OS.TreeViewItem_HeaderTemplate (handle, parent.headerTemplate);
		}
	}
	OS.Control_HorizontalContentAlignment (handle, OS.HorizontalAlignment_Stretch);
	OS.Control_VerticalContentAlignment (handle, OS.VerticalAlignment_Stretch);
	updateCheck ();
	fixStyle ();
}

void deregister () {
	display.removeWidget (handle);
}

void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

int findContentPresenter () {
	updateLayout (handle);
	int controlTemplate = OS.Control_Template (handle);
	int headerName = createDotNetString ("PART_Header", false);
	int contentPresenter = OS.FrameworkTemplate_FindName (controlTemplate, headerName, handle);
	OS.GCHandle_Free (headerName);
	OS.GCHandle_Free (controlTemplate);
	return contentPresenter;
}

int findPart (int column, String partName) {
	if (contentHandle == 0) updateLayout (handle);
	if (contentHandle == 0) return 0;
	int name = createDotNetString (partName, false);
	int result = 0;
	if (parent.columnCount == 0) {
		int template = OS.TreeViewItem_HeaderTemplate (handle);		
		result = OS.FrameworkTemplate_FindName (template, name, contentHandle);
		OS.GCHandle_Free (template);
	} else {
		int rowPresenter = OS.HeaderedItemsControl_Header (handle);
		int contentPresenter = OS.VisualTreeHelper_GetChild (rowPresenter, column);
		OS.GCHandle_Free (rowPresenter);
		int columnHandle = OS.GridViewColumnCollection_default (parent.gvColumns, column);
		int template = OS.GridViewColumn_CellTemplate (columnHandle);
		OS.GCHandle_Free (columnHandle);
		result = OS.FrameworkTemplate_FindName (template, name, contentPresenter);
		OS.GCHandle_Free (contentPresenter);
		OS.GCHandle_Free (template);
	}
	OS.GCHandle_Free (name);
	return result;
}

void fixStyle () {
	int itemStyle = OS.gcnew_Style ();
	if (itemStyle == 0) error (SWT.ERROR_NO_HANDLES);
	int setters = OS.Style_Setters (itemStyle);
	int source = OS.gcnew_RelativeSource (OS.RelativeSourceMode_FindAncestor);
	int treeViewType = OS.TreeView_typeid ();

	/* clear the templated foreground color */
	int property = OS.Control_ForegroundProperty ();
	int propertyPath = createDotNetString ("Foreground", false);
	int binding = OS.gcnew_Binding (propertyPath);
	if (binding == 0) error (SWT.ERROR_NO_HANDLES);
	OS.GCHandle_Free (propertyPath);
	OS.RelativeSource_AncestorType (source, treeViewType);
	OS.Binding_RelativeSource (binding, source);
	int setter = OS.gcnew_Setter (property, binding);
	OS.GCHandle_Free (property);
	OS.GCHandle_Free (binding);
	OS.SetterBaseCollection_Add (setters, setter);
	OS.GCHandle_Free (setter);

	/* bind font properties to tree instead of parent item */
	property = OS.Control_FontSizeProperty ();
	propertyPath = createDotNetString ("FontSize", false);
	binding = OS.gcnew_Binding (propertyPath);
	if (binding == 0) error (SWT.ERROR_NO_HANDLES);
	OS.GCHandle_Free (propertyPath);
	OS.Binding_RelativeSource (binding, source);
	setter = OS.gcnew_Setter (property, binding);
	OS.GCHandle_Free (property);
	OS.GCHandle_Free (binding);
	OS.SetterBaseCollection_Add (setters, setter);
	OS.GCHandle_Free (setter);
	
	property = OS.TextBlock_FontFamilyProperty ();
	propertyPath = createDotNetString ("FontFamily", false);
	binding = OS.gcnew_Binding (propertyPath);
	if (binding == 0) error (SWT.ERROR_NO_HANDLES);
	OS.GCHandle_Free (propertyPath);
	OS.Binding_RelativeSource (binding, source);
	setter = OS.gcnew_Setter (property, binding);
	OS.GCHandle_Free (property);
	OS.GCHandle_Free (binding);
	OS.SetterBaseCollection_Add (setters, setter);
	OS.GCHandle_Free (setter);
	
	property = OS.TextBlock_FontStretchProperty ();
	propertyPath = createDotNetString ("FontStretch", false);
	binding = OS.gcnew_Binding (propertyPath);
	if (binding == 0) error (SWT.ERROR_NO_HANDLES);
	OS.GCHandle_Free (propertyPath);
	OS.Binding_RelativeSource (binding, source);
	setter = OS.gcnew_Setter (property, binding);
	OS.GCHandle_Free (property);
	OS.GCHandle_Free (binding);
	OS.SetterBaseCollection_Add (setters, setter);
	OS.GCHandle_Free (setter);
	
	property = OS.TextBlock_FontWeightProperty ();
	propertyPath = createDotNetString ("FontWeight", false);
	binding = OS.gcnew_Binding (propertyPath);
	if (binding == 0) error (SWT.ERROR_NO_HANDLES);
	OS.GCHandle_Free (propertyPath);
	OS.Binding_RelativeSource (binding, source);
	setter = OS.gcnew_Setter (property, binding);
	OS.GCHandle_Free (property);
	OS.GCHandle_Free (binding);
	OS.SetterBaseCollection_Add (setters, setter);
	OS.GCHandle_Free (setter);
	
	property = OS.TextBlock_FontStyleProperty ();
	propertyPath = createDotNetString ("FontStyle", false);
	binding = OS.gcnew_Binding (propertyPath);
	if (binding == 0) error (SWT.ERROR_NO_HANDLES);
	OS.GCHandle_Free (propertyPath);
	OS.Binding_RelativeSource (binding, source);
	setter = OS.gcnew_Setter (property, binding);
	OS.GCHandle_Free (property);
	OS.GCHandle_Free (binding);
	OS.SetterBaseCollection_Add (setters, setter);
	OS.GCHandle_Free (setter);
	
	OS.FrameworkElement_Style (handle, itemStyle);
	OS.GCHandle_Free (treeViewType);
	OS.GCHandle_Free (source);
	OS.GCHandle_Free (setters);
	OS.GCHandle_Free (itemStyle);
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
	return background != null ? background : parent.getBackground ();
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
	if (0 > index || index > count -1) return getBackground ();
	if (cellBackground == null || cellBackground [index] == null) return getBackground ();
	return cellBackground [index];
}

/**
 * Returns a rectangle describing the size and location of the receiver's
 * text relative to its parent.
 *
 * @return the bounding rectangle of the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds () {
	checkWidget ();
	return getTextBounds (0);
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
	if (index != 0 && !(0 <= index && index < parent.columnCount)) return new Rectangle (0, 0, 0, 0);
	if (!OS.UIElement_IsVisible (handle)) return new Rectangle (0, 0, 0, 0);
	updateLayout (handle);
	int point = OS.gcnew_Point (0, 0);
	int stackPanel = findPart (index, Tree.CONTENTPANEL_PART_NAME);
	int location = OS.UIElement_TranslatePoint (stackPanel, point, parent.handle);
	int x = (int) OS.Point_X (location);
	int y = (int) OS.Point_Y (location);
	int width = (int) OS.FrameworkElement_ActualWidth (stackPanel);
	int height = (int) OS.FrameworkElement_ActualHeight (stackPanel);
	OS.GCHandle_Free (point);
	OS.GCHandle_Free (location);
	OS.GCHandle_Free (stackPanel);
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
	if (0 > index || index > count -1) return getFont ();
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
	return foreground != null ? foreground : parent.getForeground ();
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
	if (0 > index || index > count -1) return getForeground ();
	if (cellForeground == null || cellForeground [index] == null) return getForeground ();
	return cellForeground [index];
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
	if (index != 0 && !(0 <= index && index < parent.columnCount)) return new Rectangle (0, 0, 0, 0);
	int parentHandle = parent.topHandle ();
	int part = findPart (index, Tree.IMAGE_PART_NAME);
	int point = OS.gcnew_Point (0, 0);
	if (point == 0) error (SWT.ERROR_NO_HANDLES);
	int location = OS.UIElement_TranslatePoint (part, point, parentHandle);
	int x = (int) OS.Point_X (location);
	int y = (int) OS.Point_Y (location);
	OS.GCHandle_Free (point);
	OS.GCHandle_Free (location);
	int width = (int) OS.FrameworkElement_ActualWidth (part);
	int height = (int) OS.FrameworkElement_ActualHeight (part);
	OS.GCHandle_Free (part);
	return new Rectangle (x, y, width, height);
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

/**
 * Returns a rectangle describing the size and location
 * relative to its parent of the text at a column in the
 * tree.
 *
 * @param index the index that specifies the column
 * @return the receiver's bounding text rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.3
 */
public Rectangle getTextBounds (int index) {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index != 0 && !(0 <= index && index < parent.columnCount)) return new Rectangle (0, 0, 0, 0);
	int parentHandle = parent.topHandle ();
	int point = OS.gcnew_Point (0, 0);
	if (point == 0) error (SWT.ERROR_NO_HANDLES);
	int textBlock = findPart (index, Tree.TEXT_PART_NAME);
	int renderPanel = findPart (index, Table.RENDER_PANEL_NAME);
	Rectangle result = new Rectangle (0, 0, 0, 0);
	if (textBlock != 0 && renderPanel != 0) {
		int location = OS.UIElement_TranslatePoint (textBlock, point, parentHandle);
		int x = (int) OS.Point_X (location);
		int y = (int) OS.Point_Y (location);
		OS.GCHandle_Free (location);
		double textWidth = OS.FrameworkElement_ActualWidth (textBlock);
		int panelLocation = OS.UIElement_TranslatePoint (textBlock, point, renderPanel);
		double visibleWidth = Math.max (0, OS.FrameworkElement_ActualWidth (renderPanel) - OS.Point_X (panelLocation));
		OS.GCHandle_Free (panelLocation);
		int width = (int) Math.min (textWidth, visibleWidth);
		int height = (int) OS.FrameworkElement_ActualHeight (textBlock);
		result = new Rectangle (x, y, width, height);
	}
	OS.GCHandle_Free (point);
	if (textBlock != 0) OS.GCHandle_Free (textBlock);
	if (renderPanel != 0) OS.GCHandle_Free (renderPanel);
	return result;
}

public String getText () {
	checkWidget ();
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
	if (strings != null && 0 <= index && index < strings.length) {
		return strings [index]!= null ? strings [index] : "";
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
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	int items = OS.ItemsControl_Items (handle);
	int index = OS.ItemCollection_IndexOf (items, item.handle);
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
	if (contentHandle != 0) OS.GCHandle_Free (contentHandle);
	contentHandle = 0;
	parent = null;
}

void releaseWidget () {
	super.releaseWidget ();
	strings = null;
	images = null;
	cellBackground = cellForeground = null;
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
	int items = OS.ItemsControl_Items (handle);
	for (int i = 0; i < itemCount; i++) {
		TreeItem item = parent.getItem (items, i, false);
		if (item != null && !item.isDisposed ()) item.release (false);
	}
	parent.ignoreSelection = true;
	OS.ItemCollection_Clear (items);
	parent.ignoreSelection = false;
	OS.GCHandle_Free (items);
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
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (color != null) {
		int brush = OS.gcnew_SolidColorBrush (color.handle);
		OS.Control_Background (handle, brush);
		OS.GCHandle_Free (brush);
	} else {
		int property = OS.Control_BackgroundProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
	}
	background = color;
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
	if (color != null && color.isDisposed ())  SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	if (cellBackground == null) cellBackground = new Color [count];
	cellBackground [index] = color;
	updateBackground (index);
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
	if (this.checked == checked) return;
	this.checked = checked;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	parent.ignoreSelection = true;
	updateCheck ();
	parent.ignoreSelection = false;
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
	if (font != null) {
		int family = OS.Typeface_FontFamily (font.handle);
		OS.Control_FontFamily (handle, family);
		OS.GCHandle_Free (family);
		int stretch = OS.Typeface_Stretch (font.handle);
		OS.Control_FontStretch (handle, stretch);
		OS.GCHandle_Free (stretch);
		int style = OS.Typeface_Style (font.handle);
		OS.Control_FontStyle (handle, style);
		OS.GCHandle_Free (style);
		int weight = OS.Typeface_Weight (font.handle);
		OS.Control_FontWeight (handle, weight);
		OS.GCHandle_Free (weight);
		OS.Control_FontSize (handle, font.size);
	} else {
		int property = OS.Control_FontFamilyProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
		property = OS. Control_FontStyleProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
		property = OS.Control_FontStretchProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
		property = OS.Control_FontWeightProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
		property = OS.Control_FontSizeProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
	}
	this.font = font;
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
	if (cellFont == null) {
		if (font == null) return;
		cellFont = new Font [count];
	}
	Font oldFont = cellFont [index];
	if (oldFont == font) return;
	cellFont [index] = font;
	if (oldFont != null && oldFont.equals (font)) return;
	updateFont (index);
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
}

/**
 * Sets the receiver's foreground color to the color specified
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
public void setForeground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if (color != null) {
		int brush = OS.gcnew_SolidColorBrush (color.handle);
		OS.Control_Foreground (handle, brush);
		OS.GCHandle_Free (brush);
	} else {
		int property = OS.Control_ForegroundProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
	}
	foreground = color;
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
	if (cellForeground == null) cellForeground = new Color [count];
	cellForeground [index] = color;
	updateForeground (index);
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
	if (this.grayed == grayed) return;
	this.grayed = grayed;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	parent.ignoreSelection = true;
	updateCheck ();
	parent.ignoreSelection = false;
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
	checkWidget ();
	if (image != null && image.isDisposed ()) {
		error(SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	if (images == null) images = new Image [count];
	images [index] = image;
	updateImage (index);
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
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
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	if (strings == null) strings = new String [count];
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	strings [index] = string;
	updateText (index);
}

public void setText (String string) {
	checkWidget ();
	setText (0, string);
}

void updateBackground (int index) {
	int panel = findPart (index, Tree.CONTENTPANEL_PART_NAME);
	if (panel != 0) {
		if (cellBackground != null && cellBackground [index] != null) {
			int brush = OS.gcnew_SolidColorBrush (cellBackground [index].handle);
			OS.Panel_Background (panel, brush);
			OS.GCHandle_Free (brush);
		} else {
			int property = OS.Panel_BackgroundProperty ();
			OS.DependencyObject_ClearValue (panel, property);
			OS.GCHandle_Free (property);
		}
		OS.GCHandle_Free (panel);
	}
}

void updateCheck () {
	if ((parent.style & SWT.CHECK) == 0) return;
	int checkBox = findPart (0, Tree.CHECKBOX_PART_NAME);
	if (checkBox != 0) {
		parent.ignoreSelection = true;
		if (!grayed) {
			OS.ToggleButton_IsChecked (checkBox, checked);
		} else {
			if (checked) 
				OS.ToggleButton_IsCheckedNullSetter (checkBox);
		}
		parent.ignoreSelection = false;
		OS.GCHandle_Free (checkBox);
	}
}

void updateFont (int index) {
	if (cellFont == null) return;
	int textBlock = findPart (index, Tree.TEXT_PART_NAME);
	if (textBlock != 0) {
		Font font = cellFont != null ? cellFont [index] : null;
		if (font != null) {
			int family = OS.Typeface_FontFamily (font.handle);
			OS.TextBlock_FontFamily (textBlock, family);
			OS.GCHandle_Free (family);
			int stretch = OS.Typeface_Stretch (font.handle);
			OS.TextBlock_FontStretch (textBlock, stretch);
			OS.GCHandle_Free (stretch);
			int style = OS.Typeface_Style (font.handle);
			OS.TextBlock_FontStyle (textBlock, style);
			OS.GCHandle_Free (style);
			int weight = OS.Typeface_Weight (font.handle);
			OS.TextBlock_FontWeight (textBlock, weight);
			OS.GCHandle_Free (weight);
			OS.TextBlock_FontSize (textBlock, font.size);
		} else {
			int property = OS.TextBlock_FontFamilyProperty ();
			OS.DependencyObject_ClearValue (textBlock, property);
			OS.GCHandle_Free (property);
			property = OS.TextBlock_FontSizeProperty ();
			OS.DependencyObject_ClearValue (textBlock, property);
			OS.GCHandle_Free (property);
			property = OS.TextBlock_FontStretchProperty ();
			OS.DependencyObject_ClearValue (textBlock, property);
			OS.GCHandle_Free (property);
			property = OS.TextBlock_FontWeightProperty ();
			OS.DependencyObject_ClearValue (textBlock, property);
			OS.GCHandle_Free (property);
			property = OS.TextBlock_FontStyleProperty ();
			OS.DependencyObject_ClearValue (textBlock, property);
			OS.GCHandle_Free (property);
		}
		OS.GCHandle_Free (textBlock);
	}
}

void updateForeground (int index) {
	int textBlock = findPart (index, Tree.TEXT_PART_NAME);
	if (textBlock != 0) {
		if (cellForeground != null && cellForeground [index] != null) {
			int brush = OS.gcnew_SolidColorBrush (cellForeground [index].handle);
			OS.TextBlock_Foreground (textBlock, brush);
			OS.GCHandle_Free (brush);
		} else {
			int property = OS.Control_ForegroundProperty ();
			OS.DependencyObject_ClearValue (textBlock, property);
			OS.GCHandle_Free (property);
		}
		OS.GCHandle_Free (textBlock);
	}
}

void updateImage (int index) {
	if (images == null) return;
	int img = findPart (index, Tree.IMAGE_PART_NAME);
	if (img != 0) {
		int src = 0;
		if (images != null) src = images [index] != null ? images [index].handle : 0;
		OS.Image_Source (img, src);
		OS.GCHandle_Free (img);
	}
}

void updateText (int index) {
	int textBlock = findPart (index, Tree.TEXT_PART_NAME);
	if (textBlock != 0) {
		if (strings != null && strings [index] != null) {
			int strPtr = createDotNetString (strings [index], false);
			OS.TextBlock_Text (textBlock, strPtr);
			OS.GCHandle_Free (strPtr);
		} else {
			int property = OS.TextBlock_TextProperty ();
			OS.DependencyObject_ClearValue (textBlock, property);
			OS.GCHandle_Free (property);
		}
		OS.GCHandle_Free (textBlock);
	}
}
}
