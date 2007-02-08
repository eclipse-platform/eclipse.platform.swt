/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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
import org.eclipse.swt.events.*;

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
 * Here is an example of using a <code>Tree</code> with style <code>VIRTUAL</code>:
 * <code><pre>
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
 * </pre></code>
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, VIRTUAL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, Collapse, Expand, SetData, MeasureItem, EraseItem, PaintItem</dd>
 * </dl>
 * </p><p>
 * Note: Only one of the styles SINGLE and MULTI may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class Tree extends Composite {
	int columns, parentingHandle;
	int columnCount, itemCount;
	byte headerVisibility = OS.Visibility_Collapsed;
	boolean ignoreSelection;

	static final String HEADER_PART_NAME = "SWT_PART_HEADER";
	static final String CHECKBOX_PART_NAME = "SWT_PART_CHECKBOX";
	static final String IMAGE_PART_NAME = "SWT_PART_IMAGE";
	static final String TEXT_PART_NAME = "SWT_PART_TEXT";
	static final String SCROLLVIEWER_PART_NAME = "SWT_PART_SCROLLVIEWER";
	
	static final int BACKGROUND_NOTIFY = 3;
	static final int CHECK_NOTIFY = 4;
	static final int FONT_NOTIFY = 5;
	static final int FOREGROUND_NOTIFY = 2;
	static final int IMAGE_NOTIFY = 1;
	static final int TEXT_NOTIFY = 0;

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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Tree (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	/*
	* To be compatible with Windows, force the H_SCROLL
	* and V_SCROLL style bits.  On Windows, it is not
	* possible to create a table without scroll bars.
	*/
	style |= SWT.H_SCROLL | SWT.V_SCROLL;
	/* WPF is always FULL_SELECTION */
	style |= SWT.FULL_SELECTION;
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * If the receiver has <code>SWT.CHECK</code> style set and the check selection changes,
 * the event object detail field contains the value <code>SWT.CHECK</code>.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
 * The item field of the event object is valid for default selection, but the detail field is not used.
 * </p>
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
public void addTreeListener (TreeListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Expand, typedListener);
	addListener (SWT.Collapse, typedListener);
} 

int backgroundHandle () {
	return parentingHandle;
}

boolean checkData (TreeItem item) {
	if ((style & SWT.VIRTUAL) == 0) return true;
	if (!item.cached) {
		item.cached = true;
		int parentItem = OS.FrameworkElement_Parent (item.handle);
		int items = OS.ItemsControl_Items (parentItem);
		int index = OS.ItemCollection_IndexOf (items, item.handle);
		OS.GCHandle_Free (items);
		OS.GCHandle_Free (parentItem);
		Event event = new Event ();
		event.item = item;
		event.index = index;
		sendEvent (SWT.SetData, event);
		if (isDisposed () || item.isDisposed ()) return false;
	}
	return true;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
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
	clear (handle, index, all);
}

void clear (int parentHandle, int index, boolean all) {
	int items = OS.ItemsControl_Items (parentHandle);
	TreeItem item = getItem (items, index, false);
	if (item != null) {
		item.clear ();
		if (all) clearAll (item, true);
	}
	OS.GCHandle_Free (items);
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
	clearAll (null, all);
}

void clearAll (TreeItem parentItem, boolean all) {
	int count = parentItem != null ? parentItem.itemCount : itemCount;
	int parentHandle = parentItem != null ? parentItem.handle : handle;
	int items = OS.ItemsControl_Items (parentHandle);
	for (int i=0; i<count; i++) {
		TreeItem item = getItem (items, i, false);
		if (item != null) {
			item.clear ();
			if (all) clearAll (item, true);
		}
	}
	OS.GCHandle_Free (items);
}

int createCellTemplate (int index) {
	int template = OS.gcnew_DataTemplate ();
	int stackPanelType = OS.StackPanel_typeid ();
	int stackPanelNode = OS.gcnew_FrameworkElementFactory (stackPanelType);
	if (index == 0 && (style & SWT.CHECK) != 0) {
		int checkBoxType = OS.CheckBox_typeid ();
		int checkBoxName = createDotNetString (CHECKBOX_PART_NAME, false);
		int checkBoxNode = OS.gcnew_FrameworkElementFactory (checkBoxType, checkBoxName);
		int verticalAlignmentProperty = OS.FrameworkElement_VerticalAlignmentProperty ();
		OS.FrameworkElementFactory_SetValueVerticalAlignment (checkBoxNode, verticalAlignmentProperty, OS.VerticalAlignment_Center);
		int marginProperty = OS.FrameworkElement_MarginProperty ();
		int thickness = OS.gcnew_Thickness (0,0,4,0);
		OS.FrameworkElementFactory_SetValue (checkBoxNode, marginProperty, thickness);
		int threeStateProperty = OS.ToggleButton_IsThreeStateProperty ();
		OS.FrameworkElementFactory_SetValue (checkBoxNode, threeStateProperty, true);
		OS.FrameworkElementFactory_AppendChild (stackPanelNode, checkBoxNode);
		//binding
		int checkPath = createDotNetString ("Check", false);
		int checkBinding = OS.gcnew_Binding (checkPath);
		int isCheckedProperty = OS.ToggleButton_IsCheckedProperty ();
		OS.FrameworkElementFactory_SetBinding (checkBoxNode, isCheckedProperty, checkBinding);
		OS.GCHandle_Free (checkPath);
		OS.GCHandle_Free (checkBinding);
		OS.GCHandle_Free (isCheckedProperty);
		OS.GCHandle_Free (checkBoxName);
		OS.GCHandle_Free (threeStateProperty);
		OS.GCHandle_Free (thickness);
		OS.GCHandle_Free (marginProperty);
		OS.GCHandle_Free (verticalAlignmentProperty);
		OS.GCHandle_Free (checkBoxNode);
		OS.GCHandle_Free (checkBoxType);
	}
	int textType = OS.TextBlock_typeid ();
	int textName = createDotNetString (TEXT_PART_NAME, false);
	int textNode = OS.gcnew_FrameworkElementFactory (textType, textName);
	int imageType = OS.Image_typeid ();
	int imageName = createDotNetString (IMAGE_PART_NAME, false);
	int imageNode = OS.gcnew_FrameworkElementFactory (imageType, imageName);
	int marginProperty = OS.FrameworkElement_MarginProperty ();
	int thickness = OS.gcnew_Thickness (0, 0, 4, 0);
	OS.FrameworkElementFactory_SetValue (imageNode, marginProperty, thickness);
	int orientationProperty = OS.StackPanel_OrientationProperty ();
	OS.FrameworkElementFactory_SetValueOrientation (stackPanelNode, orientationProperty, OS.Orientation_Horizontal);
	OS.FrameworkElementFactory_AppendChild (stackPanelNode, imageNode);
	OS.FrameworkElementFactory_AppendChild (stackPanelNode, textNode);
	OS.FrameworkTemplate_VisualTree (template, stackPanelNode);
	
	//bindings
	int cellConverter = OS.gcnew_SWTCellConverter ();
	int textPath = createDotNetString ("Text", false);
	int textBinding = OS.gcnew_Binding (textPath);
	OS.Binding_Converter (textBinding, cellConverter);
	OS.Binding_ConverterParameter (textBinding, index);
	int textProperty = OS.TextBlock_TextProperty ();
	OS.FrameworkElementFactory_SetBinding (textNode, textProperty, textBinding);
	int imagePath = createDotNetString ("Image", false);
	int imageBinding = OS.gcnew_Binding (imagePath);
	OS.Binding_Converter (imageBinding, cellConverter);
	OS.Binding_ConverterParameter (imageBinding, index);
	int imageProperty = OS.Image_SourceProperty ();
	OS.FrameworkElementFactory_SetBinding (imageNode, imageProperty, imageBinding);
	OS.GCHandle_Free (textBinding);
	OS.GCHandle_Free (textPath);
	OS.GCHandle_Free (textProperty);
	OS.GCHandle_Free (imageBinding);
	OS.GCHandle_Free (imagePath);
	OS.GCHandle_Free (imageProperty);
	OS.GCHandle_Free (cellConverter);
	
	OS.GCHandle_Free (imageName);
	OS.GCHandle_Free (textName);
	OS.GCHandle_Free (marginProperty);
	OS.GCHandle_Free (thickness);
	OS.GCHandle_Free (stackPanelType);
	OS.GCHandle_Free (imageType);
	OS.GCHandle_Free (textType);
	OS.GCHandle_Free (stackPanelNode);
	OS.GCHandle_Free (textNode);
	OS.GCHandle_Free (imageNode);
	OS.GCHandle_Free (orientationProperty);
	return template;
}

void createDefaultColumn () {
	int column = OS.gcnew_GridViewColumn ();
	OS.GridViewColumnCollection_Insert (columns, 0, column);
	int cellTemplate = createCellTemplate (0);
	OS.GridViewColumn_CellTemplate (column, cellTemplate);
	OS.GCHandle_Free (column);
	OS.GCHandle_Free (cellTemplate);
}

int createControlTemplate () {
	int template = OS.gcnew_ControlTemplate ();
	int borderType = OS.Border_typeid ();
	int borderNode = OS.gcnew_FrameworkElementFactory (borderType);
	int dockPanelType = OS.DockPanel_typeid ();
	int dockPanelNode = OS.gcnew_FrameworkElementFactory (dockPanelType);
	int headerType = OS.GridViewHeaderRowPresenter_typeid ();
	int nameString = createDotNetString (HEADER_PART_NAME, false);
	int headerNode = OS.gcnew_FrameworkElementFactory (headerType, nameString);
	int scrollViewerType = OS.ScrollViewer_typeid ();
	int scrollViewerName = createDotNetString (SCROLLVIEWER_PART_NAME, false);
	int scrollViewerNode = OS.gcnew_FrameworkElementFactory (scrollViewerType, scrollViewerName);
	int columnsProperty = OS.GridViewHeaderRowPresenter_ColumnsProperty ();
	OS.FrameworkElementFactory_SetValue (headerNode, columnsProperty, columns);
	int visibilityProperty = OS.UIElement_VisibilityProperty ();
	OS.FrameworkElementFactory_SetValueVisibility (headerNode, visibilityProperty, headerVisibility);
	int dockProperty = OS.DockPanel_DockProperty ();
	OS.FrameworkElementFactory_SetValueDock (headerNode, dockProperty, OS.Dock_Top);
	int itemsPresenterType = OS.ItemsPresenter_typeid ();
	int itemsPresenterNode = OS.gcnew_FrameworkElementFactory (itemsPresenterType);
	OS.FrameworkElementFactory_AppendChild (borderNode, scrollViewerNode);
	OS.FrameworkElementFactory_AppendChild (scrollViewerNode, dockPanelNode);
	OS.FrameworkElementFactory_AppendChild (dockPanelNode, headerNode);
	OS.FrameworkElementFactory_AppendChild (dockPanelNode, itemsPresenterNode);
	OS.FrameworkTemplate_VisualTree (template, borderNode);
	OS.GCHandle_Free (scrollViewerType);
	OS.GCHandle_Free (scrollViewerName);
	OS.GCHandle_Free (scrollViewerNode);
	OS.GCHandle_Free (nameString);
	OS.GCHandle_Free (borderType);
	OS.GCHandle_Free (borderNode);
	OS.GCHandle_Free (dockPanelType);
	OS.GCHandle_Free (dockPanelNode);
	OS.GCHandle_Free (headerType);
	OS.GCHandle_Free (headerNode);
	OS.GCHandle_Free (columnsProperty);
	OS.GCHandle_Free (visibilityProperty);
	OS.GCHandle_Free (dockProperty);
	OS.GCHandle_Free (itemsPresenterType);
	OS.GCHandle_Free (itemsPresenterNode);
    return template;
}

void createHandle () {
	parentingHandle = OS.gcnew_Canvas ();
	if (parentingHandle == 0) error (SWT.ERROR_NO_HANDLES);
	handle = OS.gcnew_TreeView ();
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	columns = OS.gcnew_GridViewColumnCollection ();
	if (columns == 0) error (SWT.ERROR_NO_HANDLES);
	createDefaultColumn ();
	int template = createControlTemplate ();
	OS.Control_Template (handle, template);
	OS.GCHandle_Free (template);
	OS.Canvas_SetLeft (handle, 0);
	OS.Canvas_SetTop (handle, 0);
	int children = OS.Panel_Children (parentingHandle);
	OS.UIElementCollection_Add (children, handle);
	OS.GCHandle_Free (children);
}

int createHeaderTemplate () {
	int template = OS.gcnew_DataTemplate ();
	int stackPanelType = OS.StackPanel_typeid ();
	int stackPanelNode = OS.gcnew_FrameworkElementFactory (stackPanelType);
	int textType = OS.TextBlock_typeid ();
	int textName = createDotNetString (TEXT_PART_NAME, false);
	int textNode = OS.gcnew_FrameworkElementFactory (textType, textName);
	int imageType = OS.Image_typeid ();
	int imageName = createDotNetString (IMAGE_PART_NAME, false);
	int imageNode = OS.gcnew_FrameworkElementFactory (imageType, imageName);
	int marginProperty = OS.FrameworkElement_MarginProperty ();
	int thickness = OS.gcnew_Thickness (0,0,4,0);
	OS.FrameworkElementFactory_SetValue (imageNode, marginProperty, thickness);
	int orientationProperty = OS.StackPanel_OrientationProperty ();
	OS.FrameworkElementFactory_SetValueOrientation (stackPanelNode, orientationProperty, OS.Orientation_Horizontal);
	int stretchProperty = OS.Image_StretchProperty ();
	OS.FrameworkElementFactory_SetValueStretch (imageNode, stretchProperty, OS.Stretch_None);
	OS.FrameworkElementFactory_AppendChild (stackPanelNode, imageNode);
	OS.FrameworkElementFactory_AppendChild (stackPanelNode, textNode);
	OS.FrameworkTemplate_VisualTree (template, stackPanelNode);
	//bindings
	int textPath = createDotNetString ("Text", false);
	int textBinding = OS.gcnew_Binding (textPath);
	int textProperty = OS.TextBlock_TextProperty ();
	OS.FrameworkElementFactory_SetBinding (textNode, textProperty, textBinding);
	int imagePath = createDotNetString ("Image", false);
	int imageBinding = OS.gcnew_Binding (imagePath);
	int imageProperty = OS.Image_SourceProperty ();
	OS.FrameworkElementFactory_SetBinding (imageNode, imageProperty, imageBinding);
	OS.GCHandle_Free (textPath);
	OS.GCHandle_Free (textBinding);
	OS.GCHandle_Free (textProperty);
	OS.GCHandle_Free (imagePath);
	OS.GCHandle_Free (imageBinding);
	OS.GCHandle_Free (imageProperty);
	
	OS.GCHandle_Free (imageType);
	OS.GCHandle_Free (textName);
	OS.GCHandle_Free (textType);
	OS.GCHandle_Free (stackPanelType);
	OS.GCHandle_Free (stackPanelNode);
	OS.GCHandle_Free (textNode);
	OS.GCHandle_Free (marginProperty);
	OS.GCHandle_Free (thickness);
	OS.GCHandle_Free (imageName);
	OS.GCHandle_Free (imageNode);
	OS.GCHandle_Free (orientationProperty);
	OS.GCHandle_Free (stretchProperty);
	return template;
}

void createItem (TreeColumn column, int index) {
    if (!(0 <= index && index <= columnCount)) error (SWT.ERROR_INVALID_RANGE);
	column.createWidget ();
	int template = createHeaderTemplate ();
	OS.GridViewColumn_HeaderTemplate (column.handle, template);
	OS.GCHandle_Free (template);
	template = createCellTemplate (index);
	OS.GridViewColumn_CellTemplate (column.handle, template);
	OS.GCHandle_Free (template);
	if (columnCount == 0) { 
		OS.GridViewColumnCollection_Clear (columns);
	}
    OS.GridViewColumnCollection_Insert (columns, index, column.handle);
    int items = OS.ItemsControl_Items (handle);
    for (int i=0; i<itemCount; i++) {
    	TreeItem item = getItem (items, i, false);
    	if (item != null) {
    		item.columnAdded (index);
    	}
    }
    OS.GCHandle_Free (items);
    columnCount++;
}

void createItem (TreeItem item, TreeItem parentItem, int index) {
	int itemCount = parentItem != null ? parentItem.itemCount : this.itemCount;
	if (!(0 <= index && index <= itemCount)) error (SWT.ERROR_INVALID_RANGE);
	item.createWidget ();
	int parentHandle = parentItem != null ? parentItem.handle : handle;
	int items = OS.ItemsControl_Items (parentHandle);
	OS.ItemCollection_Insert (items, index, item.handle);		
	int count = OS.ItemCollection_Count (items);
	OS.GCHandle_Free (items);
	if (itemCount == count) error (SWT.ERROR_ITEM_NOT_ADDED);
	if (parentItem != null) {
		parentItem.itemCount++;
	} else {
		this.itemCount++;
	}
}

int defaultBackground () {
	return OS.Colors_White;
}

void deregister () {
	super.deregister ();
	display.removeWidget (parentingHandle);
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
	//TODO multiselection...
	int item = OS.TreeView_SelectedItem (handle);
	if (item != 0) {
		ignoreSelection = true;
		OS.TreeViewItem_IsSelected (item, false);
		ignoreSelection = false;
		OS.GCHandle_Free (item);
	}
}

void destroyItem (TreeColumn column) {
	int index = OS.GridViewColumnCollection_IndexOf (columns, column.handle);
    boolean removed = OS.GridViewColumnCollection_Remove (columns, column.handle);
    if (!removed) error (SWT.ERROR_ITEM_NOT_REMOVED);
	columnCount--;
	if (columnCount == 0) {
		createDefaultColumn ();
	} 
	int items = OS.ItemsControl_Items (handle);
    for (int i=0; i<itemCount; i++) {
		TreeItem item = getItem (items, i, false);
		if (item != null) {
			item.columnRemoved (index);
		}
	}
    OS.GCHandle_Free (items);
}

void destroyItem (TreeItem item) {
	TreeItem parentItem = item.getParentItem ();
	int itemCount = parentItem != null ? parentItem.itemCount : this.itemCount;
	int parentHandle = parentItem != null ? parentItem.handle : handle;
	
	
	int items = OS.ItemsControl_Items (parentHandle);
	OS.ItemCollection_Remove (items, item.handle);
	int count = OS.ItemCollection_Count (items);
	OS.GCHandle_Free (items);
	if (count == itemCount) error (SWT.ERROR_ITEM_NOT_REMOVED);
	if (parentItem != null) {
		parentItem.itemCount--;
	} else {
		this.itemCount--;
	}
}

int findScrollViewer(int current, int scrollViewerType) {
	int template = OS.Control_Template (handle);
	int scrollViewerName = createDotNetString (SCROLLVIEWER_PART_NAME, false);
	int scrollViewer = OS.FrameworkTemplate_FindName (template, scrollViewerName, handle);
	OS.GCHandle_Free (scrollViewerName);
	OS.GCHandle_Free (template);
	return scrollViewer;
}

int GetBackground (int itemHandle) {
	TreeItem item = getItem (itemHandle, true); 
	checkData (item);
	return item.backgroundList;
}

int GetCheck (int itemHandle) {
	TreeItem item = getItem (itemHandle, true); 
	checkData (item);
	return item.checkState;
}

int GetFont (int itemHandle) {
	TreeItem item = getItem (itemHandle, true); 
	checkData (item);
	return item.fontList;
}

int GetForeground (int itemHandle) {
	TreeItem item = getItem (itemHandle, true); 
	checkData (item);
	return item.foregroundList;
}

int GetImage (int itemHandle) {
	TreeItem item = getItem (itemHandle, true); 
	checkData (item);
	return item.imageList;
}

int GetText (int itemHandle) {
	TreeItem item = getItem (itemHandle, true); 
	checkData (item);
	return item.stringList;
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
 * 
 * @since 3.1
 */
public int getGridLineWidth () {
	checkWidget ();
	//TODO
	return 0;
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
	//TODO
	return 0;
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
	checkWidget ();
	//TODO
	return false;
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
	checkWidget ();
	if (!(0 <= index && index < columnCount)) error (SWT.ERROR_INVALID_RANGE);
	return _getColumn (index);
}

TreeColumn _getColumn (int index) {
	if (columnCount == 0) return null;
	int gridColumn = OS.GridViewColumnCollection_default (columns, index);
	int header = OS.GridViewColumn_Header (gridColumn);
	TreeColumn column = (TreeColumn) display.getWidget (header);
	OS.GCHandle_Free (gridColumn);
	OS.GCHandle_Free (header);
	return column;
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
 * @see Tree#setColumnOrder(int[])
 * @see TreeColumn#getMoveable()
 * @see TreeColumn#setMoveable(boolean)
 * @see SWT#Move
 * 
 * @since 3.2
 */
public int[] getColumnOrder () {
	checkWidget ();
	//TODO
	int [] order = new int [columnCount];
	for (int i=0; i<order.length; i++) order [i] = i;
	return order;
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
	checkWidget ();
	TreeColumn [] treeColumns = new TreeColumn [columnCount];
	for (int i = 0; i < treeColumns.length; i++) {
		treeColumns [i] = _getColumn (i); 
	}
	return treeColumns;
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
	int items = OS.ItemsControl_Items (handle);
	TreeItem treeItem = getItem (items, index, true);
	OS.GCHandle_Free (items);
	return treeItem;
}

TreeItem getItem (int items, int index, boolean create) {
	int item = OS.ItemCollection_GetItemAt (items, index);
	TreeItem result = getItem (item, create);
	OS.GCHandle_Free (item);
	return result;
}

TreeItem getItem (int item, boolean create) {
	int tag = OS.FrameworkElement_Tag (item);
	if (tag != 0) {
		int contentValue = OS.IntPtr_ToInt32 (tag);
		OS.GCHandle_Free (tag);
		return (TreeItem) OS.JNIGetObject (contentValue);
	}
	if (create) {
		int itemHandle = OS.GCHandle_Alloc (item);
		int parentHandle = OS.FrameworkElement_Parent (item);
		TreeItem parentItem = null;
		if (!OS.Object_Equals (parentHandle, handle)) parentItem = (TreeItem) display.getWidget (parentHandle);
		OS.GCHandle_Free (parentHandle);
		return new TreeItem (this, parentItem, SWT.NONE, 0, itemHandle);
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
public TreeItem getItem (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	int pt = OS.gcnew_Point (point.x, point.y);
	int input = OS.UIElement_InputHitTest (handle, pt);
	OS.GCHandle_Free (pt);
	if (input == 0) return null;
	Widget widget = display.getWidget (input);
	OS.GCHandle_Free (input);
	if (widget instanceof TreeItem) {
		return (TreeItem) widget;
	}
	return null;
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
	return itemCount;
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
	//FIXME
	if (itemCount == 0) return 16;
	int items = OS.ItemsControl_Items (handle);
	int item = OS.ItemCollection_GetItemAt (items, 0);
	double height = OS.FrameworkElement_ActualHeight (item);
	OS.GCHandle_Free (item);
	OS.GCHandle_Free (items);
	return height != 0 ? (int) height : 16;
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
	TreeItem [] result = new TreeItem [itemCount];
	int items = OS.ItemsControl_Items (handle);
	for (int i = 0; i < itemCount; i++) {
      	result [i] = getItem (items, i, true);
	}
	OS.GCHandle_Free (items);
	return result;
}

/**
 * Returns <code>true</code> if the receiver's lines are visible,
 * and <code>false</code> otherwise.
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
public boolean getLinesVisible () {
	checkWidget ();
	//TODO
	return false;
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
public TreeItem [] getSelection () {
	checkWidget ();
	//TODO
//	if ((style & SWT.SINGLE) != 0) {
		int item = OS.TreeView_SelectedItem (handle);
		if (item == 0) return new TreeItem [0];
		TreeItem result = (TreeItem) display.getWidget (item);
		OS.GCHandle_Free (item);
		return new TreeItem [] {result};
//	}
//	result = new TreeItem [0];
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
	//TODO
//	if ((style & SWT.SINGLE) != 0) {
		int item = OS.TreeView_SelectedItem (handle);
		if (item == 0) return 0;
		OS.GCHandle_Free (item);
		return 1;
//	}
//	return 0;
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
	return SWT.NONE;
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
	//TODO
	return null;
}

boolean hasItems () {
	return true;
}

void HandleChecked (int sender, int e) {
	if (!checkEvent (e)) return;
	int source = OS.RoutedEventArgs_Source (e);
	TreeItem item = (TreeItem) display.getWidget (source);
	OS.GCHandle_Free (source);
	// ignore events that result from the initial binding
	if (!OS.FrameworkElement_IsLoaded (item.handle)) return;
	if (item.grayed) {
		int checkbox = item.findPart (0, CHECKBOX_PART_NAME);
		if (checkbox != 0) {
			OS.ToggleButton_IsCheckedNullSetter (checkbox);
			OS.GCHandle_Free (checkbox);
		}
	}
	item.checked = true;
	item.updateCheckState (false);
	Event event = new Event ();
	event.item = item;
	event.detail = SWT.CHECK;
	sendEvent (SWT.Selection, event);
}

void HandleCollapsed (int sender, int e) {
	if (!checkEvent (e)) return;
	int source = OS.RoutedEventArgs_Source (e);
	if (OS.ItemsControl_HasItems (source)) {
		Event event = new Event ();
		event.item = (TreeItem) display.getWidget (source);;
		sendEvent (SWT.Collapse, event);
	}
	OS.GCHandle_Free (source);
}

void HandleExpanded (int sender, int e) {
	if (!checkEvent (e)) return;
	int source = OS.RoutedEventArgs_Source (e);
	if (OS.ItemsControl_HasItems (source)) {
		Event event = new Event ();
		event.item = (TreeItem) display.getWidget (source);;
		sendEvent (SWT.Expand, event);
	}
	OS.GCHandle_Free (source);
}

void HandleIndeterminate (int sender, int e) {
	if (!checkEvent (e)) return;
	int source = OS.RoutedEventArgs_Source (e);
	TreeItem item = (TreeItem) display.getWidget (source);
	OS.GCHandle_Free (source);
	// ignore events that result from the initial binding
	if (!OS.FrameworkElement_IsLoaded (item.handle)) return;
	if (!item.grayed) {
		int checkbox = item.findPart (0, CHECKBOX_PART_NAME);
		if (checkbox != 0) {
			OS.ToggleButton_IsChecked (checkbox, false);
			OS.GCHandle_Free (checkbox);
		}
	}
}

void HandleKeyDown (int sender, int e) {
	if (!checkEvent (e)) return;
	super.HandleKeyDown (sender, e);
	int key = OS.KeyEventArgs_Key (e);
	if (key == OS.Key_Return) {
		int source = OS.RoutedEventArgs_OriginalSource (e);
		Widget widget = display.getWidget (source);
		OS.GCHandle_Free (source);
		if (widget instanceof TreeItem) {
			Event event = new Event ();
			event.item = (TreeItem) widget;
			postEvent (SWT.DefaultSelection, event);
		}
	}
}

void HandleLoaded (int sender, int e) {
	if (!checkEvent (e)) return;
	int template = OS.Control_Template (handle);
	int nameString = createDotNetString (HEADER_PART_NAME, false);
	int header = OS.FrameworkTemplate_FindName (template, nameString, handle);
	if (header != 0) {
		OS.UIElement_Visibility (header, headerVisibility);
		OS.GCHandle_Free (header);
	}
	OS.GCHandle_Free (nameString);
	OS.GCHandle_Free (template);
}

void HandleMouseDoubleClick (int sender, int e) {
	if (!checkEvent (e)) return;
	int source = OS.RoutedEventArgs_OriginalSource (e);
	Widget widget = display.getWidget (source);
	OS.GCHandle_Free (source);
	if (widget instanceof TreeItem) {
		Event event = new Event ();
		event.item = (TreeItem) widget;
		postEvent (SWT.DefaultSelection, event);
	}
}

void HandleSelectedItemChanged (int sender, int e) {
	if (!checkEvent (e)) return;
	if (ignoreSelection) return;
	int selectedItem = OS.TreeView_SelectedItem (handle);
	if (selectedItem == 0) return;
	TreeItem item = (TreeItem) display.getWidget (selectedItem);
	Event event = new Event ();
	event.item = item;
	postEvent (SWT.Selection, event);
}

void HandleUnchecked (int sender, int e) {
	if (!checkEvent (e)) return;
	int source = OS.RoutedEventArgs_Source (e);
	TreeItem item = (TreeItem) display.getWidget (source);
	OS.GCHandle_Free (source);
	// ignore events that result from the initial binding
	if (!OS.FrameworkElement_IsLoaded (item.handle)) return;
	item.checked = false;
	item.updateCheckState (false);
	Event event = new Event ();
	event.item = item;
	event.detail = SWT.CHECK;
	sendEvent (SWT.Selection, event);
}

void hookEvents() {
	super.hookEvents ();
	int handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleLoaded");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	OS.FrameworkElement_Loaded (handle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_RoutedPropertyChangedEventHandlerObject (jniRef, "HandleSelectedItemChanged");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	OS.TreeView_SelectedItemChanged (handle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_MouseButtonEventHandler (jniRef, "HandleMouseDoubleClick");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	OS.Control_MouseDoubleClick (handle, handler);
	OS.GCHandle_Free (handler);
	
	/* Item events */
	handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleExpanded");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	int event = OS.TreeViewItem_ExpandedEvent ();
	OS.UIElement_AddHandler (handle, event, handler);
	OS.GCHandle_Free (event);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleCollapsed");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	event = OS.TreeViewItem_CollapsedEvent ();
	OS.UIElement_AddHandler (handle, event, handler);
	OS.GCHandle_Free (event);
	OS.GCHandle_Free (handler);
	if ((style & SWT.CHECK) != 0) {
		handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleChecked");
		if (handler == 0) error (SWT.ERROR_NO_HANDLES);
		event = OS.ToggleButton_CheckedEvent ();
		OS.UIElement_AddHandler (handle, event, handler);
		OS.GCHandle_Free (event);
		OS.GCHandle_Free (handler);
		handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleUnchecked");
		if (handler == 0) error (SWT.ERROR_NO_HANDLES);
		event = OS.ToggleButton_UncheckedEvent ();
		OS.UIElement_AddHandler (handle, event, handler);
		OS.GCHandle_Free (event);
		OS.GCHandle_Free (handler);
		handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleIndeterminate");
		if (handler == 0) error (SWT.ERROR_NO_HANDLES);
		event = OS.ToggleButton_IndeterminateEvent ();
		OS.UIElement_AddHandler (handle, event, handler);
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
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.1
 */
public int indexOf (TreeColumn column) {
	checkWidget ();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	int index = OS.GridViewColumnCollection_IndexOf (columns, column.handle);
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
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	int items = OS.ItemsControl_Items (handle);
	int index = OS.ItemCollection_IndexOf (items, item.handle);
	OS.GCHandle_Free (items);
	return index;
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
		TreeItem item = getItem (items, i, false);
		if (item != null && !item.isDisposed ()) item.release (false);
	}
	OS.GCHandle_Free (items);
	for (int i=0; i<columnCount; i++) {
		TreeColumn column = _getColumn(i);
		if (column != null && !column.isDisposed ()) {
			column.release (false);
		}
	}
	super.releaseChildren (destroy);
}

void releaseHandle () {
	super.releaseHandle ();
	if (columns != 0) OS.GCHandle_Free (columns);
	columns = 0;
	OS.GCHandle_Free (parentingHandle);
	parentingHandle = 0;
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
		TreeItem item = getItem (items, i, false);
		if (item != null && !item.isDisposed ()) item.release (false);
	}
	OS.ItemCollection_Clear (items);
	itemCount = OS.ItemCollection_Count (items);
	OS.GCHandle_Free (items);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver's selection changes.
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

int setBounds (int x, int y, int width, int height, int flags) {
	int result = super.setBounds (x, y, width, height, flags);
	if ((result & RESIZED) != 0) {
		if (columnCount == 0) {
			int column = OS.GridViewColumnCollection_default (columns, 0);
			OS.GridViewColumn_Width (column, width);
			OS.GCHandle_Free (column);
		}
		OS.FrameworkElement_Width (handle, width);
		OS.FrameworkElement_Height (handle, height);
	}
	return result;
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
	if (item != null && item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	//TODO
}

/**
 * Sets the number of root-level items contained in the receiver.
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
	setItemCount (null, count);
}

void setItemCount (TreeItem parentItem, int count) {
	int itemCount = parentItem != null ? parentItem.itemCount : this.itemCount;
	count = Math.max (0, count);
	if (count == itemCount) return;
	int parentHandle = parentItem != null ? parentItem.handle : handle;
	int index = itemCount - 1;
	int items = OS.ItemsControl_Items (parentHandle);
	while (index >= count) {
		TreeItem item = getItem (items, index, false);
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
			int item = OS.gcnew_TreeViewItem ();
			if (item == 0) error (SWT.ERROR_NO_HANDLES);
			int headerHandle = OS.gcnew_SWTTreeViewRowPresenter (handle);
			if (headerHandle == 0) error (SWT.ERROR_NO_HANDLES);
			OS.GridViewRowPresenterBase_Columns (headerHandle, columns);
			OS.HeaderedItemsControl_Header (item, headerHandle);
			int row = OS.gcnew_SWTRow (jniRef, item);
			OS.GridViewRowPresenter_Content (headerHandle, row);
			OS.GCHandle_Free (row);
			OS.GCHandle_Free (headerHandle);
			OS.ItemCollection_Add (items, item);
			OS.GCHandle_Free (item);
		}
	} else {
		for (int i=itemCount; i<count; i++) {
			new TreeItem (this, parentItem, SWT.NONE, i, 0);
		}
	}
	itemCount = OS.ItemCollection_Count (items);
	OS.GCHandle_Free (items);
	if (itemCount != count) error (SWT.ERROR_ITEM_NOT_ADDED);
	if (parentItem != null) {
		parentItem.itemCount = itemCount;
	} else {
		this.itemCount = itemCount;
	}
}

/**
 * Sets the height of the area which would be used to
 * display <em>one</em> of the items in the tree.
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
	if (itemHeight < -1) error (SWT.ERROR_INVALID_ARGUMENT);
	//TODO
}

/**
 * Marks the receiver's lines as visible if the argument is <code>true</code>,
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
public void setLinesVisible (boolean show) {
	checkWidget ();
	//TODO
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
	//TODO
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
	//TODO
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
 * 
 * @since 3.1
 */
public void setHeaderVisible (boolean show) {
	checkWidget ();
	headerVisibility = show ? OS.Visibility_Visible : OS.Visibility_Collapsed;
	int template = OS.Control_Template (handle);
	int nameString = createDotNetString (HEADER_PART_NAME, false);
	int header = OS.FrameworkTemplate_FindName (template, nameString, handle);
	if (header != 0) {
		OS.UIElement_Visibility (header, headerVisibility);
		OS.GCHandle_Free (header);
	}
	OS.GCHandle_Free (nameString);
	OS.GCHandle_Free (template);
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
public void setSelection (TreeItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (new TreeItem [] {item});
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
 * @see Tree#deselectAll()
 */
public void setSelection (TreeItem [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = items.length;
	if (length == 0 || ((style & SWT.SINGLE) != 0 && length > 1)) {
		deselectAll ();
		return;
	}
	int [] handles = new int [length];
	for (int i = 0; i < items.length; i++) {
		TreeItem item = items [i]; 
		if (item.isDisposed ()) error (SWT.ERROR_WIDGET_DISPOSED);
		handles [i] = item.handle;
	}
	ignoreSelection = true;
	for (int i = 0; i < handles.length; i++) {
		OS.TreeViewItem_IsSelected (handles [i], true);	
	}
	ignoreSelection = false;
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
	//TODO
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
	//TODO
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
	checkWidget ();
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
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
	if (column.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (column.parent != this) return;
	int index = indexOf (column);
	if (index == -1) return;
	//TODO
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
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	if (item.parent != this) return;
	int parent = OS.FrameworkElement_Parent (item.handle);
	while (!OS.Object_Equals (parent, handle)) {
		OS.TreeViewItem_IsExpanded (parent, true);
		int newParent = OS.FrameworkElement_Parent (parent);
		OS.GCHandle_Free (parent);
		parent = newParent;
	}
	OS.GCHandle_Free (parent);
	OS.FrameworkElement_BringIntoView (item.handle);
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
	checkWidget ();
	int item = OS.TreeView_SelectedItem (handle);
	if (item != 0) {
		OS.FrameworkElement_BringIntoView (item);
		OS.GCHandle_Free (item);
	}
}

int topHandle () {
	return parentingHandle;
}

}