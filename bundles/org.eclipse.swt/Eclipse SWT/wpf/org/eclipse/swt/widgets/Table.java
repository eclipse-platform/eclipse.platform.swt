/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.wpf.OS;

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
 * Here is an example of using a <code>Table</code> with style <code>VIRTUAL</code>:
 * <code><pre>
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
 * </pre></code>
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not normally make sense to add <code>Control</code> children to
 * it, or set a layout on it, unless implementing something like a cell
 * editor.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, HIDE_SELECTION, VIRTUAL, NO_SCROLL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, SetData, MeasureItem, EraseItem, PaintItem</dd>
 * </dl>
 * </p><p>
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
	int gridViewHandle, parentingHandle;
	int columnCount, itemCount;
	boolean ignoreSelection;
	TableColumn [] columns;
	boolean linesVisible;
	
	static final String CHECKBOX_PART_NAME = "SWT_PART_CHECKBOX";
	static final String IMAGE_PART_NAME = "SWT_PART_IMAGE";
	static final String TEXT_PART_NAME = "SWT_PART_TEXT";
	static final String CONTENTPANEL_PART_NAME = "SWT_PART_CONTENTPANEL";
	static final String RENDER_PANEL_NAME = "SWT_PART_RENDERPANEL";

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

void _addListener (int eventType, Listener listener) {
	super._addListener (eventType, listener);
	switch (eventType) {
		case SWT.MeasureItem:
		case SWT.EraseItem:
		case SWT.PaintItem:
			//TODO
			break;
	}
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

int backgroundProperty () {
	return OS.Control_BackgroundProperty ();
}

boolean checkData (TableItem item) {
	if ((style & SWT.VIRTUAL) == 0) return true;
	if (!item.cached) {
		item.cached = true;
		Event event = new Event ();
		event.item = item;
		event.index = indexOf (item);
		sendEvent (SWT.SetData, event);
		//widget could be disposed at this point
		if (isDisposed () || item.isDisposed ()) return false;
	}
	return true;
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
	/* WPF is always FULL_SELECTION */
	style |= SWT.FULL_SELECTION;
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
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
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	int items = OS.ItemsControl_Items (handle);
	TableItem item = getItem (items, index, false);
	OS.GCHandle_Free (items);
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
	checkWidget ();
	if (start > end) return;
	if (!(0 <= start && start <= end && end < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (start == 0 && end == itemCount - 1) {
		clearAll ();
	} else {
		int items = OS.ItemsControl_Items (handle);
		for (int i=start; i<=end; i++) {
			TableItem item = getItem (items, i, false);
			if (item != null) item.clear ();
		}
		OS.GCHandle_Free (items);
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
	int items = OS.ItemsControl_Items (handle);
	for (int i=0; i<indices.length; i++) {
		int index = indices [i];
		TableItem item = getItem (items, index, false);
		if (item != null) item.clear ();
	}
	OS.GCHandle_Free (items);
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
	int items = OS.ItemsControl_Items (handle);
	for (int i=0; i<itemCount; i++) {
		TableItem item = getItem (items, i, false);
		if (item != null) item.clear ();
	}
	OS.GCHandle_Free (items);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	updateLayout (parent.handle);
	if (columnCount == 0) { // pack the default column
		double width = 0;
		int columns = OS.GridView_Columns (gridViewHandle);
		int column = OS.GridViewColumnCollection_default (columns, 0);
		OS.GCHandle_Free (columns);
		int columnHeader = OS.GridViewColumn_Header (column);
		if (columnHeader != 0) {
			int size = OS.UIElement_DesiredSize (columnHeader);
			width = OS.Size_Width (size);
			OS.GCHandle_Free (size);
			OS.GCHandle_Free (columnHeader);
		}
		int items = OS.ItemsControl_Items (handle);
		for (int i=0; i<itemCount; i++) {
			TableItem item = getItem (items, i, false);
			if (item != null) width = Math.max (width, item.computeWidth (0));
		}
		OS.GCHandle_Free (items);
		OS.GridViewColumn_Width (column, width);
		OS.GCHandle_Free (column);
	}
	Point size = computeSize (handle, wHint, hHint, changed);
	if (size.x == 0) size.x = DEFAULT_WIDTH;
	if (size.y == 0) size.y = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	Rectangle trim = computeTrim (0, 0, size.x, size.y);
	return new Point (trim.width, trim.height);
}

int createCellTemplate (int index) {
	int template = OS.gcnew_DataTemplate ();
	int renderPanelType = OS.SWTDockPanel_typeid ();
	int renderPanelName = createDotNetString(RENDER_PANEL_NAME, false);
	int onRenderNode = OS.gcnew_FrameworkElementFactory (renderPanelType, renderPanelName);
	OS.GCHandle_Free(renderPanelName);
	OS.GCHandle_Free (renderPanelType);
	int jniRefProperty = OS.SWTDockPanel_JNIRefProperty ();
	OS.FrameworkElementFactory_SetValueInt (onRenderNode, jniRefProperty, jniRef);
	OS.GCHandle_Free (jniRefProperty);
	int contentPanelName = createDotNetString (CONTENTPANEL_PART_NAME, false);
	int contentPanelType = OS.StackPanel_typeid ();
	int cellContentNode = OS.gcnew_FrameworkElementFactory (contentPanelType, contentPanelName);
	OS.GCHandle_Free (contentPanelType);
	OS.GCHandle_Free (contentPanelName);
	int clipProperty = OS.UIElement_ClipToBoundsProperty ();
	OS.FrameworkElementFactory_SetValue (cellContentNode, clipProperty, true);
	OS.GCHandle_Free (clipProperty);
	int orientationProperty = OS.StackPanel_OrientationProperty ();
	OS.FrameworkElementFactory_SetValueOrientation (cellContentNode, orientationProperty, OS.Orientation_Horizontal);
	OS.GCHandle_Free (orientationProperty);
	if (index == 0 && (style & SWT.CHECK) != 0) {
		int checkBoxType = OS.CheckBox_typeid ();
		int checkBoxName = createDotNetString (CHECKBOX_PART_NAME, false);
		int checkBoxNode = OS.gcnew_FrameworkElementFactory (checkBoxType, checkBoxName);
		int verticalAlignmentProperty = OS.FrameworkElement_VerticalAlignmentProperty ();
		OS.FrameworkElementFactory_SetValueVerticalAlignment (checkBoxNode, verticalAlignmentProperty, OS.VerticalAlignment_Center);
		int marginProperty = OS.FrameworkElement_MarginProperty ();
		int thickness = OS.gcnew_Thickness (0,0,4,0);
		OS.FrameworkElementFactory_SetValue (checkBoxNode, marginProperty, thickness);
		OS.FrameworkElementFactory_AppendChild (cellContentNode, checkBoxNode);
		OS.GCHandle_Free (thickness);
		OS.GCHandle_Free (marginProperty);
		OS.GCHandle_Free (verticalAlignmentProperty);
		OS.GCHandle_Free (checkBoxName);
		OS.GCHandle_Free (checkBoxNode);
		OS.GCHandle_Free (checkBoxType);
	}
	int textType = OS.TextBlock_typeid ();
	int textName = createDotNetString (TEXT_PART_NAME, false);
	int textNode = OS.gcnew_FrameworkElementFactory (textType, textName);
	OS.GCHandle_Free (textName);
	OS.GCHandle_Free (textType);
	int verticalAlignmentProperty = OS.FrameworkElement_VerticalAlignmentProperty ();
	OS.FrameworkElementFactory_SetValueVerticalAlignment (textNode, verticalAlignmentProperty, OS.VerticalAlignment_Center);
	OS.GCHandle_Free (verticalAlignmentProperty);
	int imageType = OS.Image_typeid ();
	int imageName = createDotNetString (IMAGE_PART_NAME, false);
	int imageNode = OS.gcnew_FrameworkElementFactory (imageType, imageName);
	OS.GCHandle_Free (imageName);
	OS.GCHandle_Free (imageType);
	int marginProperty = OS.FrameworkElement_MarginProperty ();
	int thickness = OS.gcnew_Thickness (0,0,4,0);
	OS.FrameworkElementFactory_SetValue (imageNode, marginProperty, thickness);
	OS.GCHandle_Free (marginProperty);
	OS.GCHandle_Free (thickness);
	int stretchProperty = OS.Image_StretchProperty ();
	OS.FrameworkElementFactory_SetValueStretch(imageNode, stretchProperty, OS.Stretch_None);
	OS.GCHandle_Free(stretchProperty);
	OS.FrameworkElementFactory_AppendChild (cellContentNode, imageNode);
	OS.GCHandle_Free (imageNode);
	OS.FrameworkElementFactory_AppendChild (cellContentNode, textNode);
	OS.GCHandle_Free (textNode);
	OS.FrameworkElementFactory_AppendChild (onRenderNode, cellContentNode);
	OS.GCHandle_Free (cellContentNode);
	OS.FrameworkTemplate_VisualTree (template, onRenderNode);
	OS.GCHandle_Free (onRenderNode);
	return template;
}

void createDefaultColumn () {
	int column = OS.gcnew_GridViewColumn ();
	int columnCollection = OS.GridView_Columns (gridViewHandle);
	int headerHandle = OS.gcnew_GridViewColumnHeader ();
	OS.GridViewColumn_Header (column, headerHandle);
	OS.GCHandle_Free (headerHandle);
	OS.GridViewColumnCollection_Insert (columnCollection, 0, column);
	int cellTemplate = createCellTemplate (0);
	OS.GridViewColumn_CellTemplate (column, cellTemplate);
	OS.GCHandle_Free (columnCollection);
	OS.GCHandle_Free (column);
	OS.GCHandle_Free (cellTemplate);
}

void createHandle () {
	parentingHandle = OS.gcnew_Canvas ();
	if (parentingHandle == 0) error (SWT.ERROR_NO_HANDLES);
	handle = OS.gcnew_ListView ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	gridViewHandle = OS.gcnew_GridView ();
	if (gridViewHandle == 0) error (SWT.ERROR_NO_HANDLES);
	OS.ListView_View (handle, gridViewHandle);
	if ((style & SWT.MULTI) == 0) OS.ListBox_SelectionMode (handle, OS.SelectionMode_Single);
	createDefaultColumn ();
	setHeaderVisible (false);
	OS.Selector_IsSynchronizedWithCurrentItem (handle, true);
	OS.GridView_AllowsColumnReorder (gridViewHandle, false);
	OS.Canvas_SetLeft (handle, 0);
	OS.Canvas_SetTop (handle, 0);
	int children = OS.Panel_Children (parentingHandle);
	OS.UIElementCollection_Add (children, handle);
	OS.GCHandle_Free (children);
}

int createHeaderTemplate (int columnJniRef) {
	int template = OS.gcnew_DataTemplate ();
	int stackPanelType = OS.StackPanel_typeid ();
	int stackPanelName = createDotNetString (CONTENTPANEL_PART_NAME, false);
	int stackPanelNode = OS.gcnew_FrameworkElementFactory (stackPanelType, stackPanelName);
	int textType = OS.TextBlock_typeid ();
	int textName = createDotNetString(TEXT_PART_NAME, false);
	int textNode = OS.gcnew_FrameworkElementFactory (textType, textName);
	int verticalAlignmentProperty = OS.FrameworkElement_VerticalAlignmentProperty ();
	OS.FrameworkElementFactory_SetValueVerticalAlignment (textNode, verticalAlignmentProperty, OS.VerticalAlignment_Center);
	int imageType = OS.Image_typeid ();
	int imageName = createDotNetString(IMAGE_PART_NAME, false);
	int imageNode = OS.gcnew_FrameworkElementFactory (imageType, imageName);
	int marginProperty = OS.FrameworkElement_MarginProperty ();
	int thickness = OS.gcnew_Thickness (0,0,4,0);
	OS.FrameworkElementFactory_SetValue (imageNode, marginProperty, thickness);
	int orientationProperty = OS.StackPanel_OrientationProperty ();
	OS.FrameworkElementFactory_SetValueOrientation (stackPanelNode, orientationProperty, OS.Orientation_Horizontal);
	OS.FrameworkElementFactory_AppendChild (stackPanelNode, imageNode);
	OS.FrameworkElementFactory_AppendChild (stackPanelNode, textNode);
	OS.FrameworkTemplate_VisualTree (template, stackPanelNode);
	OS.GCHandle_Free (stackPanelName);
	OS.GCHandle_Free (imageType);
	OS.GCHandle_Free (imageName);
	OS.GCHandle_Free (marginProperty);
	OS.GCHandle_Free (thickness);
	OS.GCHandle_Free (textType);
	OS.GCHandle_Free (textName);
	OS.GCHandle_Free (stackPanelType);
	OS.GCHandle_Free (stackPanelNode);
	OS.GCHandle_Free (textNode);
	OS.GCHandle_Free (imageNode);
	OS.GCHandle_Free (orientationProperty);
	OS.GCHandle_Free (verticalAlignmentProperty);
	return template;
}

void createItem (TableColumn column, int index) {
    if (index == -1) index = columnCount;
    if (!(0 <= index && index <= columnCount)) error (SWT.ERROR_INVALID_RANGE);
	column.createWidget ();
	int template = createHeaderTemplate (column.jniRef);
	OS.GridViewColumn_HeaderTemplate (column.handle, template);
	OS.GCHandle_Free (template);
	template = createCellTemplate (index);
	OS.GridViewColumn_CellTemplate (column.handle, template);
	OS.GCHandle_Free (template);
	int gvColumns = OS.GridView_Columns (gridViewHandle);
	if (columnCount == 0) OS.GridViewColumnCollection_Clear (gvColumns);
	OS.GridViewColumnCollection_Insert (gvColumns, index, column.handle);
	OS.GCHandle_Free (gvColumns);
	// When columnCount is 0, a "default column" is created in
	// the WPF control, therefore there is no need to manipulate 
	// the item's array the first time a TableColumn is created
	// because the number of columns in the OS control is still one.
	if (columnCount != 0) {
		int items = OS.ItemsControl_Items (handle);
		for (int i=0; i<itemCount; i++) {
			TableItem item = getItem (items, i, false);
			if (item != null) item.columnAdded (index);
		}
		OS.GCHandle_Free (items);
	}
	if (columns == null) columns = new TableColumn [4];
	if (columns.length == columnCount) {
		TableColumn [] newColumns = new TableColumn [columnCount + 4];
		System.arraycopy(columns, 0, newColumns, 0, columnCount);
		columns = newColumns;
	}
	columns [columnCount] = column;
	columnCount++;
}

void createItem (TableItem item, int index) {
	if (index == -1) index = itemCount;
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	item.createWidget ();
	int items = OS.ItemsControl_Items (handle);
	OS.ItemCollection_Insert (items, index, item.handle);
	int count = OS.ItemCollection_Count (items);
	OS.GCHandle_Free (items);
	if (itemCount == count) error (SWT.ERROR_ITEM_NOT_ADDED);
	itemCount++;
}

int defaultBackground () {
	return display.getSystemColor (SWT.COLOR_LIST_BACKGROUND).handle;
}

int defaultForeground () {
	return display.getSystemColor (SWT.COLOR_LIST_FOREGROUND).handle;
}

void deregister () {
	super.deregister ();
	display.removeWidget (parentingHandle);
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
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (indices.length == 0) return;
	int items = OS.ItemsControl_Items (handle);
	ignoreSelection = true;
	for (int i=0; i<indices.length; i++) {
		int index = indices [i];
		if (!(0 <= index && index < itemCount)) continue;
		int item = OS.ItemCollection_GetItemAt (items, index);
		OS.ListBoxItem_IsSelected (item, false);
		OS.GCHandle_Free (item);
	}
	ignoreSelection = false;
	OS.GCHandle_Free (items);
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
	checkWidget ();
	if (!(0 <= index && index < itemCount)) return;
	int items = OS.ItemsControl_Items (handle); 
	int item = OS.ItemCollection_GetItemAt (items, index);
	ignoreSelection = true;
	OS.ListBoxItem_IsSelected (item, false);
	ignoreSelection = false;
	OS.GCHandle_Free (item);
	OS.GCHandle_Free (items);
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
	checkWidget ();
	if (start <= 0 && end >= itemCount - 1) {
		deselectAll ();
	} else {
		start = Math.max (0, start);
		end = Math.min (end, itemCount - 1);
		int items = OS.ItemsControl_Items (handle);
		ignoreSelection = true;
		for (int i=start; i<=end; i++) {
			int item = OS.ItemCollection_GetItemAt (items, i);
			OS.ListBoxItem_IsSelected (item, false);
			OS.GCHandle_Free (item);
		}
		ignoreSelection = false;
		OS.GCHandle_Free (items);
	}
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
	checkWidget ();
	ignoreSelection = true;
	OS.ListBox_UnselectAll(handle);
	ignoreSelection = false;
}

void destroyItem (TableColumn column) {
	int gvColumns = OS.GridView_Columns (gridViewHandle);
	int index = OS.GridViewColumnCollection_IndexOf (gvColumns, column.handle);
    boolean removed = OS.GridViewColumnCollection_Remove (gvColumns, column.handle);
    OS.GCHandle_Free (gvColumns);
    if (!removed) error (SWT.ERROR_ITEM_NOT_REMOVED);
    int arrayIndex = -1;
    for (int i = 0; i < columnCount; i++) {
    	TableColumn tc = columns [i];
    	if (tc.equals(column)) {
    		arrayIndex = i;
    		break;
    	}
    }
	columnCount--;
	columns [arrayIndex] = null;
	if (arrayIndex < columnCount) System.arraycopy (columns, arrayIndex+1, columns, arrayIndex, columnCount - arrayIndex);
	if (columnCount == 0) {
		createDefaultColumn ();
	}
	int items = OS.ItemsControl_Items (handle);
    for (int i=0; i<itemCount; i++) {
		TableItem item = getItem (items, i, false);
		if (item != null) item.columnRemoved (index);
	}
    OS.GCHandle_Free (items);
}

void destroyItem (TableItem item) {
	int items = OS.ItemsControl_Items (handle);
	ignoreSelection = true;
	OS.ItemCollection_Remove (items, item.handle);
	ignoreSelection = false;
	int count = OS.ItemCollection_Count (items);
	OS.GCHandle_Free (items);
	if (itemCount == count) error (SWT.ERROR_ITEM_NOT_REMOVED);
	itemCount--;
}

int findPartByType (int source, int type) {
	if (OS.Type_IsInstanceOfType (type, source)) return source;
	int parent = OS.VisualTreeHelper_GetParent(source);
	if (parent == 0) return 0;
	int result = findPartByType(parent, type);
	if (result != parent) OS.GCHandle_Free(parent);
	return result;
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
	checkWidget ();
	if (!(0 <= index && index < columnCount)) error (SWT.ERROR_INVALID_RANGE);
	return columns [index];
}

TableColumn getColumn (int columns, int index) {
	int gridColumn = OS.GridViewColumnCollection_default (columns, index);
	int header = OS.GridViewColumn_Header (gridColumn);
	TableColumn column = (TableColumn) display.getWidget (header);
	OS.GCHandle_Free (header);
	OS.GCHandle_Free (gridColumn);
	return column;
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
	checkWidget ();
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
 * @see Table#setColumnOrder(int[])
 * @see TableColumn#getMoveable()
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 * 
 * @since 3.1
 */
public int[] getColumnOrder () {
	checkWidget ();
	int [] order = new int [columnCount];
	for (int i=0; i<order.length; i++) order [i] = i;
	int gvColumns = OS.GridView_Columns (gridViewHandle);
	for (int i = 0; i < order.length; i++) {
		TableColumn column = columns [i];
		int index = OS.IList_IndexOf (gvColumns, column.handle);
		order [index] = i;	
	}
	OS.GCHandle_Free (gvColumns);
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
	checkWidget ();
	TableColumn [] result = new TableColumn [columnCount];
	for (int i = 0; i < result.length; i++) {
		result [i] = columns [i];
	}
	return result;
}

/**
 * Returns the width in pixels of a grid line.
 *
 * @return the width of a grid line in pixels
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getGridLineWidth () {
	checkWidget ();
	return 0; //FIXME: No grid lines yet
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
	int columns = OS.GridView_Columns (gridViewHandle);
	int column = OS.GridViewColumnCollection_default (columns, 0);
	int height = 0;
	int header = OS.GridViewColumn_Header (column);
	if (header != 0) {
		height = (int) OS.FrameworkElement_ActualHeight (header);
		if (height == 0) { 
			updateLayout (header);
			height = (int) OS.FrameworkElement_ActualHeight (header);
		}
		OS.GCHandle_Free (header);
	}
	OS.GCHandle_Free (column);
	OS.GCHandle_Free (columns);
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
	checkWidget ();
	int columns = OS.GridView_Columns (gridViewHandle);
	int column = OS.GridViewColumnCollection_default (columns, 0);
	int header = OS.GridViewColumn_Header (column);
	boolean visible = OS.UIElement_Visibility (header) == OS.Visibility_Visible;
	OS.GCHandle_Free (header);
	OS.GCHandle_Free (column);
	OS.GCHandle_Free (columns);
	return visible;
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
	checkWidget ();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	int items = OS.ItemsControl_Items (handle);
	TableItem item = getItem (items, index, true); 
	OS.GCHandle_Free (items);
	return item;
}

TableItem getItem (int items, int index, boolean create) {
	int item = OS.ItemCollection_GetItemAt (items, index);
	TableItem result = getItem (item, create);
	OS.GCHandle_Free (item);
	return result;
}

TableItem getItem (int item, boolean create) {
	int tag = OS.FrameworkElement_Tag (item);
	if (tag != 0) {
		int contentValue = OS.IntPtr_ToInt32 (tag);
		OS.GCHandle_Free (tag);
		return (TableItem) OS.JNIGetObject (contentValue);
	}
	if (create) {
		int itemHandle = OS.GCHandle_Alloc (item);
		return new TableItem (this, SWT.NONE, 0, itemHandle);
	}
	return null;
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
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	int pt = OS.gcnew_Point (point.x, point.y);
	int input = OS.UIElement_InputHitTest (handle, pt);
	OS.GCHandle_Free (pt);
	if (input == 0) return null;
	Widget widget = display.getWidget (input);
	OS.GCHandle_Free (input);
	if (widget instanceof TableItem) {
		return (TableItem) widget;
	}
	return null;
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
	//FIXME what is the default size?
	if (itemCount == 0) return 16;
	int items = OS.ItemsControl_Items (handle);
	int item = OS.ItemCollection_GetItemAt (items, 0);
	double height = OS.FrameworkElement_ActualHeight (item);
	OS.GCHandle_Free (item);
	OS.GCHandle_Free (items);
	return height != 0 ? (int) height : 16;
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
	checkWidget ();
	TableItem [] result = new TableItem [itemCount];
	int items = OS.ItemsControl_Items (handle);
	for (int i=0; i<itemCount; i++) {
		result [i] = getItem (items, i, true);
	}
	OS.GCHandle_Free (items);
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
public boolean getLinesVisible () {
	checkWidget ();
	//TODO
	return linesVisible; 
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
	checkWidget ();
	int selected = OS.ListBox_SelectedItems (handle);
	int enumerator = OS.IList_GetEnumerator (selected);
	int count = OS.ICollection_Count (selected);
	TableItem [] result = new TableItem [count];
	int index = 0;
    while (OS.IEnumerator_MoveNext (enumerator)) {
    	int item = OS.IEnumerator_Current (enumerator);
    	result [index++] = getItem (item, true);
		OS.GCHandle_Free (item);
	}
    OS.GCHandle_Free (enumerator);
	OS.GCHandle_Free (selected);
	return result;
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
	checkWidget ();
	int selected = OS.ListBox_SelectedItems (handle);
	int count = OS.ICollection_Count (selected);
	OS.GCHandle_Free (selected);
	return count;
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
	checkWidget ();
	return OS.Selector_SelectedIndex (handle);
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
	checkWidget ();
	int items = OS.ItemsControl_Items (handle);
	int list = OS.ListBox_SelectedItems (handle);
	int enumerator = OS.IList_GetEnumerator (list);
	int count = OS.ICollection_Count (list);
	int [] indices = new int [count];
	int index = 0;
    while (OS.IEnumerator_MoveNext (enumerator)) {
    	int item = OS.IEnumerator_Current (enumerator);
		indices [index++] = OS.ItemCollection_IndexOf (items, item);
		OS.GCHandle_Free (item);
	}
    OS.GCHandle_Free (enumerator);
	OS.GCHandle_Free (list);
	OS.GCHandle_Free (items);
	sortAscending (indices);
	return indices;
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
	//TODO
	return null;
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
	//TODO
	return -1;
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
	checkWidget ();
	int items = OS.ItemsControl_Items (handle);
	int item = OS.ItemCollection_GetItemAt (items, 0);
	OS.GCHandle_Free (items);
	int virtualizingStackPanel = OS.VisualTreeHelper_GetParent (item);
	OS.GCHandle_Free (item);
	int topIndex = 0;
	if (virtualizingStackPanel != 0) {
		topIndex = (int) OS.VirtualizingStackPanel_VerticalOffset (virtualizingStackPanel);
		OS.GCHandle_Free (virtualizingStackPanel); 
	}
	return topIndex;
}

boolean hasItems () {
	return true;
}

void HandleChecked (int sender, int e) {
	if (!checkEvent (e)) return;
	if (ignoreSelection) return;
	int source = OS.RoutedEventArgs_Source (e);
	TableItem item = (TableItem) display.getWidget (source);
	OS.GCHandle_Free (source);
	if (item.grayed) {
		int checkbox = item.findPart (0, CHECKBOX_PART_NAME);
		if (checkbox != 0) {
			OS.ToggleButton_IsCheckedNullSetter (checkbox);
			OS.GCHandle_Free (checkbox);
		}
	}
	item.checked = true;
	Event event = new Event ();
	event.item = item;
	event.detail = SWT.CHECK;
	sendEvent (SWT.Selection, event);
}

void HandleIndeterminate (int sender, int e) {
	if (!checkEvent (e)) return;
	if (ignoreSelection) return;
	int source = OS.RoutedEventArgs_Source (e);
	TableItem item = (TableItem) display.getWidget (source);
	OS.GCHandle_Free (source);
	if (!item.grayed) {
		int checkbox = item.findPart (0, CHECKBOX_PART_NAME);
		if (checkbox != 0) {
			OS.ToggleButton_IsChecked (checkbox, false);
			OS.GCHandle_Free (checkbox);
		}
	}
}

void HandlePreviewKeyDown (int sender, int e) {
	super.HandlePreviewKeyDown (sender, e);
	if (!checkEvent (e)) return;
	int key = OS.KeyEventArgs_Key (e);
	if (key == OS.Key_Return) {
		int source = OS.RoutedEventArgs_OriginalSource (e);
		Widget widget = display.getWidget (source);
		OS.GCHandle_Free (source);
		if (widget instanceof TableItem) {
			Event event = new Event ();
			event.item = (TableItem)widget;
			postEvent (SWT.DefaultSelection, event);
		}
	}
}

void HandleMouseDoubleClick (int sender, int e) {
	if (!checkEvent (e)) return;
	int source = OS.RoutedEventArgs_OriginalSource (e);
	Widget widget = display.getWidget (source);
	OS.GCHandle_Free (source);
	if (widget instanceof TableItem) {
		Event event = new Event ();
		event.item = (TableItem)widget;
		postEvent (SWT.DefaultSelection, event);
	}
}

void HandleSelectionChanged (int sender, int e) {
	if (!checkEvent (e)) return;
	if (ignoreSelection) return;
	int item = 0;
	int list = OS.SelectionChangedEventArgs_AddedItems (e);
	if (list != 0) {
		int count = OS.ICollection_Count (list);
		if (count > 0) item	= OS.IList_default (list, count - 1);
	}
	OS.GCHandle_Free (list);
	if (item == 0) {
		list = OS.SelectionChangedEventArgs_RemovedItems (e);
		int count = OS.ICollection_Count (list);
		if (count > 0) item	= OS.IList_default (list, count - 1);
		OS.GCHandle_Free (list);
	}
	if (item != 0) {
		TableItem result = getItem (item, true);
		OS.GCHandle_Free (item);
		if (result != null) {
			Event event = new Event ();
			event.item = result;
			postEvent (SWT.Selection, event);
		}
	}
}

void HandleUnchecked (int sender, int e) {
	if (!checkEvent (e)) return;
	if (ignoreSelection) return;
	int source = OS.RoutedEventArgs_Source (e);
	TableItem item = (TableItem) display.getWidget (source);
	OS.GCHandle_Free (source);
	item.checked = false;
	Event event = new Event ();
	event.item = item;
	event.detail = SWT.CHECK;
	sendEvent (SWT.Selection, event);
}

void hookEvents () {
	super.hookEvents ();
	int handler = OS.gcnew_SelectionChangedEventHandler (jniRef, "HandleSelectionChanged");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	OS.Selector_SelectionChanged (handle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_MouseButtonEventHandler (jniRef, "HandleMouseDoubleClick");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	OS.Control_MouseDoubleClick (handle, handler);
	OS.GCHandle_Free (handler);
	if ((style & SWT.CHECK) != 0) {
		/* Item events */
		handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleChecked");
		if (handler == 0) error (SWT.ERROR_NO_HANDLES);
		int event = OS.ToggleButton_CheckedEvent ();
		OS.UIElement_AddHandler (handle, event, handler, false);
		OS.GCHandle_Free (event);
		OS.GCHandle_Free (handler);
		handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleUnchecked");
		if (handler == 0) error (SWT.ERROR_NO_HANDLES);
		event = OS.ToggleButton_UncheckedEvent ();
		OS.UIElement_AddHandler (handle, event, handler, false);
		OS.GCHandle_Free (event);
		OS.GCHandle_Free (handler);
		handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleIndeterminate");
		if (handler == 0) error (SWT.ERROR_NO_HANDLES);
		event = OS.ToggleButton_IndeterminateEvent ();
		OS.UIElement_AddHandler (handle, event, handler, false);
		OS.GCHandle_Free (event);
		OS.GCHandle_Free (handler);
	}
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
	checkWidget ();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	int columns = OS.GridView_Columns (gridViewHandle);
	int index = OS.GridViewColumnCollection_IndexOf (columns, column.handle);
	OS.GCHandle_Free (columns);
	return index;
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
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int items = OS.ItemsControl_Items(handle);
	int index = OS.ItemCollection_IndexOf(items, item.handle);
	OS.GCHandle_Free (items);
	return index;
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
	checkWidget ();
	int items = OS.ItemsControl_Items (handle);
	int item = OS.ItemCollection_GetItemAt (items, index);
	boolean result = OS.ListBoxItem_IsSelected (item);
	OS.GCHandle_Free (item);
	OS.GCHandle_Free (items);
	return result;
}

void OnRender (int source, int dc) {
	if (isDisposed ()) return;
	int type =  OS.ListViewItem_typeid();
	int itemHandle = findPartByType (source, type);
	OS.GCHandle_Free (type);
	TableItem item = getItem (itemHandle, true);
	OS.GCHandle_Free (itemHandle);
	if ((item.cached || (style & SWT.VIRTUAL) == 0) && item.rowHandle != 0) return;
	checkData (item);
	if (item.rowHandle == 0) {
		int rowPresenterType = OS.GridViewRowPresenter_typeid ();
		item.rowHandle = item.findRowPresenter (item.handle, rowPresenterType);
		OS.GCHandle_Free (rowPresenterType);
	}
	int columns = columnCount == 0 ? 1 : columnCount;
	item.updateCheck ();
	for (int i=0; i<columns; i++) {
		item.updateText (i);
		item.updateImage (i);
		item.updateBackground (i);
		item.updateForeground (i);
		item.updateFont (i);
	}
}

int parentingHandle () {
	return parentingHandle;
}

void register() {
	super.register();
	display.addWidget (parentingHandle, this);
}

void releaseChildren (boolean destroy) {
	int items = OS.ItemsControl_Items (handle);
	for (int i=0; i<itemCount; i++) {
		TableItem item = getItem (items, i, false);
		if (item != null && !item.isDisposed ()) item.release (false);
	}
	OS.GCHandle_Free (items);
	int columns = OS.GridView_Columns (gridViewHandle);
	for (int i=0; i<columnCount; i++) {
		TableColumn column = getColumn(columns, i);
		if (!column.isDisposed ()) column.release (false);
	}
	OS.GCHandle_Free (columns);
	super.releaseChildren (destroy);
}

void releaseHandle () {
	super.releaseHandle ();
	OS.GCHandle_Free (gridViewHandle);
	gridViewHandle = 0;
	OS.GCHandle_Free (parentingHandle);
	parentingHandle = 0;
}

void releaseWidget () {
	super.releaseWidget ();
	columns = null;
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
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (indices.length == 0) return;
	int [] newIndices = new int [indices.length];
	System.arraycopy (indices, 0, newIndices, 0, indices.length);
	sort (newIndices);
	int start = newIndices [newIndices.length - 1], end = newIndices [0];
	if (!(0 <= start && start <= end && end < itemCount)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int items = OS.ItemsControl_Items (handle);
	ignoreSelection = true;
	int lastIndex = -1;
	for (int i = newIndices.length-1; i >= 0; i--) {
		int index = newIndices [i];
		if (index != lastIndex) {
			TableItem item = getItem (items, index, false);
			if (item != null && !item.isDisposed ()) item.release (false);
			OS.ItemCollection_RemoveAt (items, index);
			lastIndex = index;
		}
	}
	ignoreSelection = false;
	itemCount = OS.ItemCollection_Count (items);
	OS.GCHandle_Free (items);
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
	checkWidget ();
	if (!(0 <= index && index < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	int items = OS.ItemsControl_Items (handle);
	TableItem item = getItem (items, index, false);
	if (item != null && !item.isDisposed ()) item.release (false);
	ignoreSelection = true;
	OS.ItemCollection_RemoveAt (items, index);
	ignoreSelection = false;
	itemCount = OS.ItemCollection_Count (items);
	OS.GCHandle_Free (items);
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
	checkWidget ();
	if (start > end) return;
	if (!(0 <= start && start <= end && end < itemCount)) error (SWT.ERROR_INVALID_RANGE);
	if (start == 0 && end == itemCount - 1) {
		removeAll ();
		return;
	} 
	int items = OS.ItemsControl_Items (handle);
	ignoreSelection = true;
	for (int i = end; i >= start; i--) {
		TableItem item = getItem (items, i, false);
		if (item != null && !item.isDisposed ()) item.release (false);
		OS.ItemCollection_RemoveAt (items, i);
	}
	ignoreSelection = false;
	itemCount = OS.ItemCollection_Count (items);
	OS.GCHandle_Free (items);
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
	int items = OS.ItemsControl_Items (handle);
	for (int i = 0; i < itemCount; i++) {
		TableItem item = getItem (items, i, false);
		if (item != null && !item.isDisposed ()) item.release (false);
	}
	
	ignoreSelection = true;
	OS.ItemCollection_Clear (items);
	ignoreSelection = false;
	itemCount = OS.ItemCollection_Count (items);
	OS.GCHandle_Free (items);
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
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
	ignoreSelection = true;
	int items = OS.ItemsControl_Items (handle);
	for (int i = 0; i < indices.length; i++) {
		if (!(0 <= indices[i] && indices[i] < itemCount)) continue;
		int item = OS.ItemCollection_GetItemAt (items, indices[i]);
		OS.ListBoxItem_IsSelected (item, true);
		OS.GCHandle_Free (item);
	}
	OS.GCHandle_Free (items);
	ignoreSelection = false;
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
	checkWidget ();
	if (!(0 <= index && index < itemCount)) return;
	int items = OS.ItemsControl_Items (handle); 
	int item = OS.ItemCollection_GetItemAt (items, index);
	ignoreSelection = true;
	OS.ListBoxItem_IsSelected (item, true);
	ignoreSelection = false;
	OS.GCHandle_Free (item);
	OS.GCHandle_Free (items);
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
	if ((style & SWT.SINGLE) != 0 && start != end) return;
	if (start <= 0 && end >= itemCount - 1) {
		selectAll ();
	} else {
		start = Math.max (0, start);
		end = Math.min (end, itemCount - 1);
		int items = OS.ItemsControl_Items (handle); 
		ignoreSelection = true;
		for (int i=start; i<=end; i++) {
			int item = OS.ItemCollection_GetItemAt (items, i);
			OS.ListBoxItem_IsSelected (item, true);
			OS.GCHandle_Free (item);
		}
		ignoreSelection = false;
		OS.GCHandle_Free (items);
	}
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
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return;
	ignoreSelection = true;
	OS.ListBox_SelectAll (handle);
	ignoreSelection = false;
}

void setBackgroundBrush(int brush) {
	if (brush != 0) {
		OS.Control_Background (handle, brush);
	} else {
		int property = OS.Control_BackgroundProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
	}
}

int setBounds (int x, int y, int width, int height, int flags) {
	int result = super.setBounds (x, y, width, height, flags);
	if ((result & RESIZED) != 0) {
		if (columnCount == 0) {
			int columns = OS.GridView_Columns (gridViewHandle);
			int column = OS.GridViewColumnCollection_default (columns, 0);
			OS.GridViewColumn_Width (column, width);
			OS.GCHandle_Free (column);
			OS.GCHandle_Free (columns);
		}
		OS.FrameworkElement_Width (handle, width);
		OS.FrameworkElement_Height (handle, height);
	}
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
	if (order.length != columnCount) error (SWT.ERROR_INVALID_ARGUMENT);
	int [] oldOrder = getColumnOrder ();
	boolean reorder = false;
	boolean [] seen = new boolean [columnCount];
	for (int i=0; i<order.length; i++) {
		int index = order [i];
		if (index < 0 || index >= columnCount) error (SWT.ERROR_INVALID_ARGUMENT);
		if (seen [index]) error (SWT.ERROR_INVALID_ARGUMENT);
		seen [index] = true;
		if (order [i] != oldOrder [i]) reorder = true;
	}
	if (!reorder) return;
	int gvColumns = OS.GridView_Columns (gridViewHandle);
	for (int i = 0; i < order.length; i++) {
		TableColumn column = columns [order [i]];
		int index = OS.IList_IndexOf (gvColumns, column.handle);
		if (index != i) OS.ObservableCollectionGridViewColumn_Move (gvColumns, index, i);	
	}
	OS.GCHandle_Free (gvColumns);
}

void setFont (int font, double size) {
	if (font != 0) {
		int fontFamily = OS.Typeface_FontFamily( font);
		int style = OS.Typeface_Style (font);
		int weight = OS.Typeface_Weight (font);
		int stretch = OS.Typeface_Stretch (font);
		OS.Control_FontFamily (handle, fontFamily);
		OS.Control_FontStyle (handle, style);
		OS.Control_FontWeight (handle, weight);
		OS.Control_FontStretch (handle, stretch);
		OS.Control_FontSize (handle, size);
		OS.GCHandle_Free (fontFamily);
		OS.GCHandle_Free (style);
		OS.GCHandle_Free (weight);
		OS.GCHandle_Free (stretch);
	} else {
		int property = OS.Control_FontFamilyProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
		property = OS.Control_FontStyleProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
		property = OS.Control_FontWeightProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
		property = OS.Control_FontStretchProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
		property = OS.Control_FontSizeProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
	}
}

void setForegroundBrush (int brush) {
	if (brush != 0) {
		OS.Control_Foreground (handle, brush);
	} else {
		int property = OS.Control_ForegroundProperty ();
		OS.DependencyObject_ClearValue (handle, property);
		OS.GCHandle_Free (property);
	}
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
	int style = 0;
	if (!show) {
		style = OS.gcnew_Style ();
		int dp = OS.UIElement_VisibilityProperty ();
		int setter = OS.gcnew_SetterVisibility (dp, OS.Visibility_Collapsed);
		int collection = OS.Style_Setters (style);
		OS.SetterBaseCollection_Add (collection, setter);
		OS.GCHandle_Free (collection);
		OS.GCHandle_Free (setter);
		OS.GCHandle_Free (dp);
	}
	OS.GridView_ColumnHeaderContainerStyle (gridViewHandle, style);
	if (style != 0) OS.GCHandle_Free (style);
	for (int i=0; i<columnCount; i++) {
		TableColumn column = getColumn (i);
		column.updateImage ();
		column.updateText ();
	}
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
	int index = itemCount - 1;
	int items = OS.ItemsControl_Items (handle);
	while (index >= count) {
		TableItem item = getItem (items, index, false);
		if (item != null) {
			if (!item.isDisposed()) item.release (true);
		} else {
			OS.ItemCollection_RemoveAt (items, index);
		}
		index--;
	}
	if (OS.ItemCollection_Count (items) > count) error (SWT.ERROR_ITEM_NOT_REMOVED);
	if ((style & SWT.VIRTUAL) != 0) {
		for (int i=itemCount; i<count; i++) {
			int item = OS.gcnew_ListViewItem ();
			if (item == 0) error (SWT.ERROR_NO_HANDLES);
			OS.ItemCollection_Add (items, item);
			OS.GCHandle_Free (item);
		}
	} else {
		for (int i=itemCount; i<count; i++) {
			new TableItem (this, SWT.NONE, i);
		}
	}
	itemCount = OS.ItemCollection_Count (items);
	OS.GCHandle_Free (items);
	if (itemCount != count) error (SWT.ERROR_ITEM_NOT_ADDED);
}

/**
 * Sets the height of the area which would be used to
 * display <em>one</em> of the items in the table.
 *
 * @return the height of one item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
/*public*/ void setItemHeight (int itemHeight) {
	checkWidget ();
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
	checkWidget ();
	linesVisible = show;
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * The current selection is cleared before the new items are selected.
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
	select (indices);
	//TODO
//	int focusIndex = indices [0];
//	if (focusIndex != -1) setFocusIndex (focusIndex);
	showSelection ();
}

/**
 * Sets the receiver's selection to the given item.
 * The current selection is cleared before the new item is selected.
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
public void setSelection (TableItem  item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (new TableItem [] {item});
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selection is cleared before the new items are selected.
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
	deselectAll ();
	int length = items.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) return;
	for (int i=0; i<length; i++) {
		int index = indexOf (items [i]);
		if (index != -1) {
			select (index);
		}
	}
	//TODO
//	if (focusIndex != -1) setFocusIndex (focusIndex);
	showSelection ();
}

/**
 * Selects the item at the given zero-relative index in the receiver. 
 * The current selection is first cleared, then the new item is selected.
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
	deselectAll ();
	select (index);
	//TODO
//	if (index != -1) setFocusIndex (index);
	showSelection ();
}

/**
 * Selects the items in the range specified by the given zero-relative
 * indices in the receiver. The range of indices is inclusive.
 * The current selection is cleared before the new items are selected.
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
	deselectAll ();
	if (end < 0 || start > end || ((style & SWT.SINGLE) != 0 && start != end)) return;
	if (itemCount == 0 || start >= itemCount) return;
	start = Math.max (0, start);
	end = Math.min (end, itemCount - 1);
	select (start, end);
	//TODO
//	setFocusIndex (start);
	showSelection ();
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
	//TODO
//	if (sortColumn != null && !sortColumn.isDisposed ()) {
//		sortColumn.setSortDirection (SWT.NONE);
//	}
//	sortColumn = column;
//	if (sortColumn != null && sortDirection != SWT.NONE) {
//		sortColumn.setSortDirection (sortDirection);
//	}
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
public void setSortDirection (int direction) {
	checkWidget ();
	if ((direction & (SWT.UP | SWT.DOWN)) == 0 && direction != SWT.NONE) return;
	//TODO
//	sortDirection = direction;
//	if (sortColumn != null && !sortColumn.isDisposed ()) {
//		sortColumn.setSortDirection (direction);
//	}
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
	checkWidget (); 
	//TODO
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
	if (column.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (column.parent != this) return;
	int index = indexOf (column);
	if (!(0 <= index && index < columnCount)) return;
	//TODO
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
	OS.ListBox_ScrollIntoView (handle, item.handle);
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
	checkWidget (); 
	int itemCollection = OS.ItemsControl_Items (handle);
	int list = OS.ListBox_SelectedItems (handle);
	int enumerator = OS.IList_GetEnumerator (list);
	if (OS.IEnumerator_MoveNext (enumerator)) {
		int item = OS.IEnumerator_Current (enumerator);
	    OS.ListBox_ScrollIntoView (handle, item);
		OS.GCHandle_Free (item);
	}
    OS.GCHandle_Free (enumerator);
	OS.GCHandle_Free (list);
	OS.GCHandle_Free (itemCollection);
}

int topHandle () {
	return parentingHandle;
}

void updateMoveable () {
	int columns = OS.GridView_Columns (gridViewHandle);
	boolean moveable = true;
	for (int i = 0; moveable && i < columnCount; i++) {
		TableColumn column = getColumn (columns, i);
		if (!column.moveable) moveable = false;
	}
	OS.GCHandle_Free (columns);
	OS.GridView_AllowsColumnReorder (gridViewHandle, moveable);
} 

}