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
 * it does not normally make sense to add <code>Control</code> children to
 * it, or set a layout on it, unless implementing something like a cell
 * editor.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, VIRTUAL, NO_SCROLL</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection, Collapse, Expand, SetData, MeasureItem, EraseItem, PaintItem</dd>
 * </dl>
 * </p><p>
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
	int gvColumns, parentingHandle, headerTemplate;
	int columnCount, itemCount;
	TreeItem anchor, lastSelection, unselect, reselect;
	TreeColumn [] columns;
	byte headerVisibility = OS.Visibility_Collapsed;
	boolean ignoreSelection, shiftDown, ctrlDown;

	static final String HEADER_PART_NAME = "SWT_PART_HEADER";
	static final String SCROLLVIEWER_PART_NAME = "SWT_PART_SCROLLVIEWER";
	static final String CHECKBOX_PART_NAME = "SWT_PART_CHECKBOX";
	static final String IMAGE_PART_NAME = "SWT_PART_IMAGE";
	static final String TEXT_PART_NAME = "SWT_PART_TEXT";
	static final String CONTENTPANEL_PART_NAME = "SWT_PART_CONTENTPANEL";
	static final String RENDER_PANEL_NAME = "SWT_PART_RENDERPANEL";
	
	static String scrollViewerStyle = "<Style TargetType=\"ScrollViewer\" " +
			"xmlns=\"http://schemas.microsoft.com/winfx/2006/xaml/presentation\" " +
			"xmlns:s=\"clr-namespace:System;assembly=mscorlib\" " +
			"xmlns:x=\"http://schemas.microsoft.com/winfx/2006/xaml\">" +
		"<Setter Property=\"UIElement.Focusable\" Value=\"False\"/>" +
		"<Setter Property=\"Control.Template\">" +
			"<Setter.Value>" +
				"<ControlTemplate TargetType=\"ScrollViewer\">" +
					"<Grid Background=\"{TemplateBinding Control.Background}\" SnapsToDevicePixels=\"True\">" +
						"<Grid.ColumnDefinitions>" +
							"<ColumnDefinition Width=\"*\" />" +
							"<ColumnDefinition Width=\"Auto\" />" +
						"</Grid.ColumnDefinitions>" +
						"<Grid.RowDefinitions>" +
							"<RowDefinition Height=\"*\" />" +
							"<RowDefinition Height=\"Auto\" />" +
						"</Grid.RowDefinitions>" +
						"<DockPanel Margin=\"{TemplateBinding Control.Padding}\">" +
							"<ScrollViewer HorizontalScrollBarVisibility=\"Hidden\" VerticalScrollBarVisibility=\"Hidden\" Focusable=\"False\" DockPanel.Dock=\"Top\">" +
								"<GridViewHeaderRowPresenter Name=\"SWT_PART_HEADER\" Margin=\"2,0,2,0\" Visibility=\"Collapsed\" ColumnHeaderToolTip=\"{x:Null}\" " +
										"ColumnHeaderContainerStyle=\"{x:Null}\" SnapsToDevicePixels=\"{TemplateBinding UIElement.SnapsToDevicePixels}\" " +
										"AllowsColumnReorder=\"True\" ColumnHeaderTemplate=\"{x:Null}\" ColumnHeaderContextMenu=\"{x:Null}\">" +
									"<GridViewHeaderRowPresenter.Columns>" +
										"<TemplateBinding Property=\"GridViewHeaderRowPresenter.Columns\"/>" +
									"</GridViewHeaderRowPresenter.Columns>" +
								"</GridViewHeaderRowPresenter>" +
							"</ScrollViewer>" +
							"<ScrollContentPresenter VirtualizingStackPanel.IsVirtualizing=\"True\" CanVerticallyScroll=\"False\" " +
									"CanHorizontallyScroll=\"False\" Name=\"PART_ScrollContentPresenter\" " +
									"SnapsToDevicePixels=\"{TemplateBinding UIElement.SnapsToDevicePixels}\" " +
									"ContentTemplate=\"{TemplateBinding ContentControl.ContentTemplate}\" " +
									"CanContentScroll=\"{TemplateBinding ScrollViewer.CanContentScroll}\" " +
									"Content=\"{TemplateBinding ContentControl.Content}\" KeyboardNavigation.DirectionalNavigation=\"Local\" />" +
						"</DockPanel>" +
						"<ScrollBar Value=\"{TemplateBinding HorizontalOffset}\" Maximum=\"{TemplateBinding ScrollViewer.ScrollableWidth}\" " +
								"Visibility=\"{TemplateBinding ScrollViewer.ComputedHorizontalScrollBarVisibility}\" Name=\"PART_HorizontalScrollBar\" " +
								"Cursor=\"Arrow\"  Minimum=\"0\"  Orientation=\"Horizontal\"  Grid.Row=\"1\" " +
								"ViewportSize=\"{TemplateBinding ViewportWidth}\"/>" +
						"<ScrollBar Value=\"{TemplateBinding VerticalOffset}\" Maximum=\"{TemplateBinding ScrollViewer.ScrollableHeight}\" " +
								"Visibility=\"{TemplateBinding ScrollViewer.ComputedVerticalScrollBarVisibility}\"  Name=\"PART_VerticalScrollBar\" " +
								"Cursor=\"Arrow\" Minimum=\"0\" Orientation=\"Vertical\" Grid.Column=\"1\" " +
								"ViewportSize=\"{TemplateBinding ViewportHeight}\" />" +
						"<DockPanel Background=\"{x:Null}\" LastChildFill=\"False\" Grid.Column=\"1\" Grid.Row=\"1\">" +
							"<Rectangle Width=\"1\" Visibility=\"{TemplateBinding ScrollViewer.ComputedVerticalScrollBarVisibility}\" " +
									"Fill=\"#FFFFFFFF\" DockPanel.Dock=\"Left\" />" +
							"<Rectangle Height=\"1\" Visibility=\"{TemplateBinding ScrollViewer.ComputedHorizontalScrollBarVisibility}\" " +
									"Fill=\"#FFFFFFFF\" DockPanel.Dock=\"Top\" />" +
						"</DockPanel>" +
					"</Grid>" +
				"</ControlTemplate>" +
			"</Setter.Value>" +
		"</Setter>" +
   	"</Style>";

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
	
	
//	int vert = OS.FrameworkElement_VerticalAlignmentProperty();
//	OS.FrameworkElementFactory_SetValueVerticalAlignment(onRenderNode, vert, OS.VerticalAlignment_Stretch);
//	OS.GCHandle_Free (vert);
//	
//	int dp = OS.Panel_BackgroundProperty();
//	int red = OS.Brushes_Red();
//	int navy = OS.Brushes_Navy();
//	OS.FrameworkElementFactory_SetValue(cellContentNode, dp, red);
//	OS.FrameworkElementFactory_SetValue(onRenderNode, dp, navy);
//	OS.GCHandle_Free (dp);
//	OS.GCHandle_Free (red);
//	OS.GCHandle_Free (navy);
	
	
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

int createControlTemplate () {
	int template = OS.gcnew_ControlTemplate ();
	int borderType = OS.Border_typeid ();
	int borderNode = OS.gcnew_FrameworkElementFactory (borderType);
	int brushProperty = OS.Control_BorderBrushProperty ();
	int brushBinding = OS.gcnew_TemplateBindingExtension (brushProperty);
	OS.FrameworkElementFactory_SetValue (borderNode, brushProperty, brushBinding);
	int thicknessProperty = OS.Control_BorderThicknessProperty ();
	int thicknessBinding = OS.gcnew_TemplateBindingExtension (thicknessProperty);
	OS.FrameworkElementFactory_SetValue (borderNode, thicknessProperty, thicknessBinding);
	int scrollViewerType = OS.ScrollViewer_typeid ();
	int scrollViewerName = createDotNetString (SCROLLVIEWER_PART_NAME, false);
	int scrollViewerNode = OS.gcnew_FrameworkElementFactory (scrollViewerType, scrollViewerName);
	int itemsPresenterType = OS.ItemsPresenter_typeid ();
	int itemsPresenterNode = OS.gcnew_FrameworkElementFactory (itemsPresenterType);
	OS.FrameworkElementFactory_AppendChild (borderNode, scrollViewerNode);
	OS.FrameworkElementFactory_AppendChild (scrollViewerNode, itemsPresenterNode);
	int scrollStyle = createDotNetString(scrollViewerStyle, false);
	int stringReader = OS.gcnew_StringReader (scrollStyle);
	int xmlReader = OS.XmlReader_Create (stringReader);
	int xamlStyle = OS.XamlReader_Load (xmlReader);
	int styleProperty = OS.FrameworkElement_StyleProperty();
	OS.FrameworkElementFactory_SetValue (scrollViewerNode, styleProperty, xamlStyle);
	int columnsProperty = OS.GridViewRowPresenterBase_ColumnsProperty ();
	OS.FrameworkElementFactory_SetValue (scrollViewerNode, columnsProperty, gvColumns);
	OS.FrameworkTemplate_VisualTree (template, borderNode);
	OS.GCHandle_Free (brushProperty);
	OS.GCHandle_Free (thicknessProperty);
	OS.GCHandle_Free (brushBinding);
	OS.GCHandle_Free (thicknessBinding);	
	OS.GCHandle_Free (scrollStyle);
	OS.GCHandle_Free (stringReader);
	OS.GCHandle_Free (xmlReader);
	OS.GCHandle_Free (styleProperty);
	OS.GCHandle_Free (columnsProperty);
	OS.GCHandle_Free (xamlStyle);
	OS.GCHandle_Free (scrollViewerType);
	OS.GCHandle_Free (scrollViewerName);
	OS.GCHandle_Free (scrollViewerNode);
	OS.GCHandle_Free (borderType);
	OS.GCHandle_Free (borderNode);
	OS.GCHandle_Free (itemsPresenterType);
	OS.GCHandle_Free (itemsPresenterNode);
    return template;
}

void createHandle () {
	parentingHandle = OS.gcnew_Canvas ();
	if (parentingHandle == 0) error (SWT.ERROR_NO_HANDLES);
	if ((style & SWT.SINGLE) != 0) {
		handle = OS.gcnew_TreeView ();
	} else {
		handle = OS.gcnew_SWTTreeView (jniRef);
	}
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
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
	OS.GCHandle_Free (stackPanelName);
	OS.GCHandle_Free (stackPanelType);
	int textType = OS.TextBlock_typeid ();
	int textName = createDotNetString (TEXT_PART_NAME, false);
	int textNode = OS.gcnew_FrameworkElementFactory (textType, textName);
	OS.GCHandle_Free (textName);
	OS.GCHandle_Free (textType);
	int imageType = OS.Image_typeid ();
	int imageName = createDotNetString (IMAGE_PART_NAME, false);
	int imageNode = OS.gcnew_FrameworkElementFactory (imageType, imageName);
	OS.GCHandle_Free (imageName);
	OS.GCHandle_Free (imageType);
	int marginProperty = OS.FrameworkElement_MarginProperty ();
	int thickness = OS.gcnew_Thickness (0,0,4,0);
	OS.FrameworkElementFactory_SetValue (imageNode, marginProperty, thickness);
	OS.GCHandle_Free (thickness);
	OS.GCHandle_Free (marginProperty);
	int orientationProperty = OS.StackPanel_OrientationProperty ();
	OS.FrameworkElementFactory_SetValueOrientation (stackPanelNode, orientationProperty, OS.Orientation_Horizontal);
	OS.GCHandle_Free (orientationProperty);
	int stretchProperty = OS.Image_StretchProperty ();
	OS.FrameworkElementFactory_SetValueStretch (imageNode, stretchProperty, OS.Stretch_None);
	OS.GCHandle_Free (stretchProperty);
	OS.FrameworkElementFactory_AppendChild (stackPanelNode, imageNode);
	OS.GCHandle_Free (imageNode);
	OS.FrameworkElementFactory_AppendChild (stackPanelNode, textNode);
	OS.GCHandle_Free (textNode);
	OS.FrameworkTemplate_VisualTree (template, stackPanelNode);
	OS.GCHandle_Free (stackPanelNode);
	return template;
}

void createItem (TreeColumn column, int index) {
    if (!(0 <= index && index <= columnCount)) error (SWT.ERROR_INVALID_RANGE);
    if (columnCount == 0) {
    	gvColumns = OS.gcnew_GridViewColumnCollection ();
    	if (gvColumns == 0) error (SWT.ERROR_NO_HANDLES);
    	int template = createControlTemplate ();
    	OS.Control_Template (handle, template);   	
    	OS.GCHandle_Free (template);
    	updateHeaderVisibility ();
    }
	column.createWidget ();
	int template = createHeaderTemplate (column.jniRef);
	OS.GridViewColumn_HeaderTemplate (column.handle, template);
	OS.GCHandle_Free (template);
	template = createCellTemplate (index);
	OS.GridViewColumn_CellTemplate (column.handle, template);
	OS.GCHandle_Free (template);
	if (columnCount == 0) { 
		OS.GridViewColumnCollection_Clear (gvColumns);
	}
    OS.GridViewColumnCollection_Insert (gvColumns, index, column.handle);
    int items = OS.ItemsControl_Items (handle);
    for (int i=0; i<itemCount; i++) {
    	TreeItem item = getItem (items, i, false);
    	if (item != null) {
    		item.columnAdded (index);
    	}
    }
    OS.GCHandle_Free (items);
	if (columns == null) columns = new TreeColumn [4];
	if (columns.length == columnCount) {
		TreeColumn [] newColumns = new TreeColumn [columnCount + 4];
		System.arraycopy(columns, 0, newColumns, 0, columnCount);
		columns = newColumns;
	}
	columns [columnCount] = column;
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

void createWidget() {
	super.createWidget ();
	headerTemplate = createCellTemplate (0);
	int brush = OS.Brushes_Transparent ();
	OS.Control_Background (handle, brush);
	OS.GCHandle_Free (brush);
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
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SINGLE) != 0) {
		ignoreSelection = true;
		OS.TreeViewItem_IsSelected (item.handle, false);
		ignoreSelection = false;
		return;
	}
	ignoreSelection = true;
	setIsSelectionActiveProperty(true);
	OS.TreeViewItem_IsSelected (item.handle, false);	
	setIsSelectionActiveProperty(false);
	ignoreSelection = false;
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
	if ((style & SWT.SINGLE) != 0) {
	int tvItem = OS.TreeView_SelectedItem (handle);
	if (tvItem != 0) {
		ignoreSelection = true;
		OS.TreeViewItem_IsSelected (tvItem, false);
		ignoreSelection = false;
		OS.GCHandle_Free (tvItem);
	}
	} else {
	    int items = OS.ItemsControl_Items (handle);
	    int itemCount = OS.ItemCollection_Count (items);
	    boolean[] selecting = new boolean[] {false};
	    for (int i = 0; i < itemCount; i++) {
			int item = OS.ItemCollection_GetItemAt (items, i);
			fixSelection (item, null, null, selecting);
			OS.GCHandle_Free (item);
		}
	    OS.GCHandle_Free (items);
	}
}

void destroyItem (TreeColumn column) {
	int index = OS.GridViewColumnCollection_IndexOf (gvColumns, column.handle);
    boolean removed = OS.GridViewColumnCollection_Remove (gvColumns, column.handle);
    if (!removed) error (SWT.ERROR_ITEM_NOT_REMOVED);
    int arrayIndex = -1;
    for (int i = 0; i < columnCount; i++) {
    	TreeColumn tc = columns [i];
    	if (tc.equals(column)) {
    		arrayIndex = i;
    		break;
    	}
    }
	columnCount--;
	columns [arrayIndex] = null;
	if (arrayIndex < columnCount) System.arraycopy (columns, arrayIndex+1, columns, arrayIndex, columnCount - arrayIndex);
	if (columnCount == 0) {
		OS.GCHandle_Free (gvColumns);
		gvColumns = 0;
		int templateProperty = OS.Control_TemplateProperty ();
		OS.DependencyObject_ClearValue(handle, templateProperty);
		OS.GCHandle_Free(templateProperty);
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
	if (columnCount != 0) {
		int template = OS.Control_Template (handle);
		int scrollViewerName = createDotNetString (SCROLLVIEWER_PART_NAME, false);
		int scrollViewer = OS.FrameworkTemplate_FindName (template, scrollViewerName, handle);
		OS.GCHandle_Free (scrollViewerName);
		OS.GCHandle_Free (template);
		return scrollViewer;
	}
	return super.findScrollViewer (current, scrollViewerType);
}

int findPartOfType (int source, int type) {
	if (OS.Type_IsInstanceOfType (type, source)) return source;
	int parent = OS.VisualTreeHelper_GetParent(source);
	if (parent == 0) return 0;
	int result = findPartOfType(parent, type);
	if (result != parent) OS.GCHandle_Free(parent);
	return result;
}

void fixScrollbarVisibility () {
	int typeid = OS.ScrollViewer_typeid();
	int scrolledHandle = findScrollViewer(handle, typeid);
	OS.ScrollViewer_SetHorizontalScrollBarVisibility (scrolledHandle, OS.ScrollBarVisibility_Visible);
	OS.ScrollViewer_SetVerticalScrollBarVisibility (scrolledHandle, OS.ScrollBarVisibility_Visible);
	OS.GCHandle_Free(scrolledHandle);
	OS.GCHandle_Free(typeid);
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
	if (gvColumns == 0) return 0;
	int column = OS.GridViewColumnCollection_default (gvColumns, 0);
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
	OS.GCHandle_Free (gvColumns);
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
	checkWidget ();
	if (gvColumns == 0) return false;
	int column = OS.GridViewColumnCollection_default (gvColumns, 0);
	int header = OS.GridViewColumn_Header (column);
	boolean visible = OS.UIElement_Visibility (header) == OS.Visibility_Visible;
	OS.GCHandle_Free (header);
	OS.GCHandle_Free (column);
	return visible;
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
	return columns [index];
}

//TreeColumn _getColumn (int index) {
//	if (columnCount == 0) return null;
//	int gridColumn = OS.GridViewColumnCollection_default (gvColumns, index);
//	int header = OS.GridViewColumn_Header (gridColumn);
//	TreeColumn column = (TreeColumn) display.getWidget (header);
//	OS.GCHandle_Free (gridColumn);
//	OS.GCHandle_Free (header);
//	return column;
//}

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
	int [] order = new int [columnCount];
	for (int i=0; i<order.length; i++) order [i] = i;
	for (int i = 0; i < order.length; i++) {
		TreeColumn column = columns [i];
		int index = OS.IList_IndexOf (gvColumns, column.handle);
		order [index] = i;	
	}
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
	TreeColumn [] result = new TreeColumn [columnCount];
	for (int i = 0; i < result.length; i++) {
		result [i] = columns [i];
	}
	return result;
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
	TreeItem [] result;
	if ((style & SWT.SINGLE) != 0) {
		int item = OS.TreeView_SelectedItem (handle);
		if (item == 0) return new TreeItem [0];
		result = new TreeItem [] { (TreeItem) display.getWidget (item) };
		OS.GCHandle_Free (item);
	} else {
		result = getSelectedItems (handle, new TreeItem [4], new int [1]);
	}
	return result;
}

TreeItem[] getSelectedItems(int itemsControl, TreeItem [] selectedItems, int [] nextIndex) {
	int items = OS.ItemsControl_Items (itemsControl);
	int count = OS.ItemCollection_Count (items);
	for (int i = 0; i < count; i++) {
		int item = OS.ItemCollection_GetItemAt (items, i);
		boolean selected = OS.TreeViewItem_IsSelected (item);
		if (selected) {
			if (nextIndex [0] == selectedItems.length) {
				TreeItem [] newArray = new TreeItem [selectedItems.length + 4];
				System.arraycopy (selectedItems, 0, newArray, 0, selectedItems.length);
				selectedItems = newArray;
			}
			selectedItems [nextIndex[0]++] = getItem (item, true);
		}
		if (OS.TreeViewItem_IsExpanded (item)) {
			selectedItems = getSelectedItems (item, selectedItems, nextIndex);
		}
		OS.GCHandle_Free (item);
	}
	OS.GCHandle_Free (items);
	if (selectedItems.length != nextIndex [0]) {
		TreeItem[] newArray = new TreeItem [nextIndex[0]];
		System.arraycopy (selectedItems, 0, newArray, 0, nextIndex [0]);
		selectedItems = newArray;
	}
	return selectedItems;
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
	int result;
	if ((style & SWT.SINGLE) != 0) {
		int item = OS.TreeView_SelectedItem (handle);
		result = item == 0 ? 0 : 1;
		OS.GCHandle_Free (item);
	} else {
		TreeItem[] selectedItems = getSelectedItems(handle, new TreeItem[4], new int[] {0});
		result = selectedItems.length;
	}
	return result;
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
	if (ignoreSelection) return;
	int origsource = OS.RoutedEventArgs_OriginalSource (e);
	int typeid = OS.CheckBox_typeid ();
	boolean isCheckBox = OS.Type_IsInstanceOfType (typeid, origsource);
	OS.GCHandle_Free (typeid);
	OS.GCHandle_Free (origsource);
	if (!isCheckBox) return;
	int source = OS.RoutedEventArgs_Source (e);
	TreeItem item = (TreeItem) display.getWidget (source);
	OS.GCHandle_Free (source);
	if (item.grayed) {
		int checkbox = item.findPart (0, CHECKBOX_PART_NAME);
		if (checkbox != 0) {
			OS.ToggleButton_IsCheckedNullSetter (checkbox);
			OS.GCHandle_Free (checkbox);
		}
	}
	item.checked = true;
	item.updateCheck ();
	Event event = new Event ();
	event.item = item;
	event.detail = SWT.CHECK;
	sendEvent (SWT.Selection, event);

}

void HandleCollapsed (int sender, int e) {
	if (!checkEvent (e)) return;
	int source = OS.RoutedEventArgs_Source (e);
	if (OS.ItemsControl_HasItems (source)) {
		TreeItem item = (TreeItem) display.getWidget (source);
		int items = OS.ItemsControl_Items (item.handle);
		int count = OS.ItemCollection_Count (items);
		boolean[] selecting = new boolean [] {false};
		for (int i = 0; i < count; i++) {
			int child = OS.ItemCollection_GetItemAt (items, i);
			fixSelection (child, null, null, selecting);
			OS.GCHandle_Free (child);
		}
		OS.GCHandle_Free (items);
		Event event = new Event ();
		event.item = item;
		sendEvent (SWT.Collapse, event);
	}
	OS.GCHandle_Free (source);
}

void HandleExpanded (int sender, int e) {
	if (!checkEvent (e)) return;
	int source = OS.RoutedEventArgs_Source (e);
	if (OS.ItemsControl_HasItems (source)) {
		Event event = new Event ();
		event.item = (TreeItem) display.getWidget (source);
		sendEvent (SWT.Expand, event);
	}
	OS.GCHandle_Free (source);
}

void HandlePreviewKeyDown (int sender, int e) {
	super.HandlePreviewKeyDown (sender, e);
	if (!checkEvent (e)) return;
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
	if (key == OS.Key_RightShift || key == OS.Key_LeftShift) shiftDown = true;
	if (key == OS.Key_RightCtrl || key == OS.Key_LeftCtrl) ctrlDown = true;
}

void HandlePreviewKeyUp (int sender, int e) {
	super.HandlePreviewKeyUp (sender, e);
	if (!checkEvent (e)) return;
	int key = OS.KeyEventArgs_Key (e);
	if (key == OS.Key_RightShift || key == OS.Key_LeftShift) shiftDown = false;
	if (key == OS.Key_RightCtrl || key == OS.Key_LeftCtrl) ctrlDown = false;
}

void HandleLoaded (int sender, int e) {
	if (!checkEvent (e)) return;
	updateHeaderVisibility();
}

void HandlePreviewMouseDoubleClick (int sender, int e) {
	if (!checkEvent (e)) return;
	int source = OS.RoutedEventArgs_OriginalSource (e);
	Widget widget = display.getWidget (source);
	OS.GCHandle_Free (source);
	if (widget instanceof TreeItem) {
		Event event = new Event ();
		event.item = (TreeItem) widget;
		postEvent (SWT.DefaultSelection, event);
	}
	if (hooks (SWT.DefaultSelection)) OS.RoutedEventArgs_Handled(e, true);
}

void HandlePreviewMouseDown (int sender, int e) {
	super.HandlePreviewMouseDown (sender, e);
	if (!checkEvent (e)) return;
	if ((style & SWT.SINGLE) != 0) return;
	int source = OS.RoutedEventArgs_Source (e);
	Widget widget = display.getWidget (source);
	OS.GCHandle_Free (source);
	if (widget instanceof TreeItem) {
		TreeItem item = (TreeItem) widget;
		/* Check that content of item was clicked, not the expander */
		int point = OS.MouseEventArgs_GetPosition (e, item.contentHandle);
		int input = OS.UIElement_InputHitTest (item.contentHandle, point);
		OS.GCHandle_Free (point);
		if (input != 0) {
			OS.GCHandle_Free (input);
			boolean rightClick = OS.MouseEventArgs_RightButton (e) == OS.MouseButtonState_Pressed;
			if (rightClick && (ctrlDown || shiftDown)) return;
			if (ctrlDown) {
				boolean selected = OS.TreeViewItem_IsSelected (item.handle);
				if (widget.equals (lastSelection)) {
					OS.TreeViewItem_IsSelected (item.handle, !selected);
				} else {
					if (selected) unselect = item;
					if (lastSelection != null && OS.TreeViewItem_IsSelected (lastSelection.handle)) reselect = lastSelection;
				}
			}
			if (!shiftDown && !ctrlDown) {
				boolean selected = OS.TreeViewItem_IsSelected (item.handle);
	    		if (selected && rightClick) return;
	   			deselectAll ();
	   			OS.TreeViewItem_IsSelected (item.handle, true);
			}
		}
		lastSelection = item;
	}
}

void HandleSelectedItemChanged (int sender, int e) {
	if (!checkEvent (e)) return;
	if (ignoreSelection) return;
	int selectedItem = OS.TreeView_SelectedItem (handle);
	if (selectedItem == 0) return;
	TreeItem item = (TreeItem) display.getWidget (selectedItem);
	OS.GCHandle_Free (selectedItem);
	Event event = new Event ();
	event.item = item;
	postEvent (SWT.Selection, event);
}

void HandleUnchecked (int sender, int e) {
	if (!checkEvent (e)) return;
	if (ignoreSelection) return;
	int origsource = OS.RoutedEventArgs_OriginalSource (e);
	int typeid = OS.CheckBox_typeid ();
	boolean isCheckBox = OS.Type_IsInstanceOfType (typeid, origsource);
	OS.GCHandle_Free (typeid);
	OS.GCHandle_Free (origsource);
	if (!isCheckBox) return;
	int source = OS.RoutedEventArgs_Source (e);
	TreeItem item = (TreeItem) display.getWidget (source);
	OS.GCHandle_Free (source);
	item.checked = false;
	item.updateCheck ();
	Event event = new Event ();
	event.item = item;
	event.detail = SWT.CHECK;
	sendEvent (SWT.Selection, event);
}

void hookEvents () {
	super.hookEvents ();
	int handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleLoaded");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	OS.FrameworkElement_Loaded (handle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_RoutedPropertyChangedEventHandlerObject (jniRef, "HandleSelectedItemChanged");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	OS.TreeView_SelectedItemChanged (handle, handler);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_MouseButtonEventHandler (jniRef, "HandlePreviewMouseDoubleClick");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	OS.Control_PreviewMouseDoubleClick (handle, handler);
	OS.GCHandle_Free (handler);
	
	/* Item events */
	handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleExpanded");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	int event = OS.TreeViewItem_ExpandedEvent ();
	OS.UIElement_AddHandler (handle, event, handler, false);
	OS.GCHandle_Free (event);
	OS.GCHandle_Free (handler);
	handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleCollapsed");
	if (handler == 0) error (SWT.ERROR_NO_HANDLES);
	event = OS.TreeViewItem_CollapsedEvent ();
	OS.UIElement_AddHandler (handle, event, handler, false);
	OS.GCHandle_Free (event);
	OS.GCHandle_Free (handler);
	if ((style & SWT.CHECK) != 0) {
		handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleChecked");
		if (handler == 0) error (SWT.ERROR_NO_HANDLES);
		event = OS.ToggleButton_CheckedEvent ();
		OS.UIElement_AddHandler (handle, event, handler, false);
		OS.GCHandle_Free (event);
		OS.GCHandle_Free (handler);
		handler = OS.gcnew_RoutedEventHandler (jniRef, "HandleUnchecked");
		if (handler == 0) error (SWT.ERROR_NO_HANDLES);
		event = OS.ToggleButton_UncheckedEvent ();
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
 * 
 * @since 3.1
 */
public int indexOf (TreeColumn column) {
	checkWidget ();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (gvColumns == 0) return -1; 
	int index = OS.GridViewColumnCollection_IndexOf (gvColumns, column.handle);
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
	if (item.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	int items = OS.ItemsControl_Items (handle);
	int index = OS.ItemCollection_IndexOf (items, item.handle);
	OS.GCHandle_Free (items);
	return index;
}

void OnRender (int source, int dc) {
	int type =  OS.TreeViewItem_typeid ();
	int itemHandle = findPartOfType (source, type);
	OS.GCHandle_Free (type);
	TreeItem item = getItem (itemHandle, true);
	OS.GCHandle_Free (itemHandle);
	if ((item.cached || (style & SWT.VIRTUAL) == 0) && item.contentHandle != 0) return;
	checkData (item);
	if (item.contentHandle == 0) {
		item.contentHandle = item.findContentPresenter();
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

void OnSelectedItemChanged (int args) {
	int newItemRef = OS.RoutedPropertyChangedEventArgs_NewValue (args);
	TreeItem newItem = null;
	if (newItemRef != 0) {
		int unsetValue = OS.DependencyProperty_UnsetValue ();
		if (!OS.Object_Equals (newItemRef, unsetValue)) newItem = getItem (newItemRef, false);
		OS.GCHandle_Free (newItemRef);
		OS.GCHandle_Free (unsetValue);
		newItemRef = 0;
	}
    setIsSelectionActiveProperty (true);
    if (!shiftDown && !ctrlDown) {
    	deselectAll ();
    	if (newItem != null) OS.TreeViewItem_IsSelected (newItem.handle, true);
    	anchor = newItem;
    } else {
    	if (shiftDown) {
    		deselectAll ();
    		if (anchor == null || anchor == newItem) {
    	    	if (newItem != null) OS.TreeViewItem_IsSelected (newItem.handle, true);
    	    	anchor = newItem;
    		} else {
    			int zero = OS.gcnew_Point (0, 0);
    			int point = OS.UIElement_TranslatePoint (anchor.handle, zero, newItem.handle);
    			OS.GCHandle_Free (zero);
    		    boolean down = OS.Point_Y (point) < 0;
    		    OS.GCHandle_Free (point);
    		    TreeItem from = down ? anchor : newItem;
    		    TreeItem to = down ? newItem : anchor;
    		    int items = OS.ItemsControl_Items (handle);
    		    int itemCount = OS.ItemCollection_Count (items);
    		    boolean[] selecting = new boolean[] {false};
    		    for (int i = 0; i < itemCount; i++) {
					int item = OS.ItemCollection_GetItemAt(items, i);
					fixSelection (item, from, to, selecting);
					OS.GCHandle_Free (item);
				}
    		    OS.GCHandle_Free (items);
    		}
    	} else {
			if (unselect != null) {
    			OS.TreeViewItem_IsSelected (unselect.handle, false);
    			unselect = null;
    		}
    		if (reselect != null) {
    			OS.TreeViewItem_IsSelected (reselect.handle, true);
    			reselect = null;
    		}
    		anchor = newItem;
    	}
    } 
    setIsSelectionActiveProperty (false);
}

private void fixSelection (int tvItem, TreeItem from, TreeItem to, boolean [] selecting) {
    if (selecting [0]) {
    	OS.TreeViewItem_IsSelected (tvItem, true);
    	if (to != null && OS.Object_Equals (tvItem, to.handle)) selecting [0] = false;
    } else {
    	if (from != null && OS.Object_Equals (tvItem, from.handle)) selecting [0] = true;
        OS.TreeViewItem_IsSelected(tvItem, selecting [0]);
    }
    if (OS.TreeViewItem_IsExpanded (tvItem)) {
    	int items = OS.ItemsControl_Items (tvItem);
    	int itemCount = OS.ItemCollection_Count (items);
    	for (int i = 0; i < itemCount; i++) {
    		int tvChild = OS.ItemCollection_GetItemAt (items, i);
			fixSelection (tvChild, from, to, selecting);
			OS.GCHandle_Free (tvChild);
		}
    	OS.GCHandle_Free (items);
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
		TreeItem item = getItem (items, i, false);
		if (item != null && !item.isDisposed ()) item.release (false);
	}
	OS.GCHandle_Free (items);
	for (int i=0; i<columnCount; i++) {
		TreeColumn column = columns [i];
		if (column != null && !column.isDisposed ()) {
			column.release (false);
		}
	}
	super.releaseChildren (destroy);
}

void releaseHandle () {
	super.releaseHandle ();
	if (gvColumns != 0) OS.GCHandle_Free (gvColumns);
	gvColumns = 0;
	if (headerTemplate != 0) OS.GCHandle_Free (headerTemplate);
	headerTemplate = 0;
	OS.GCHandle_Free (parentingHandle);
	parentingHandle = 0;
}

void releaseWidget () {
	super.releaseWidget ();
	columns = null;
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
			if (columnCount != 0) {
				int headerHandle = OS.gcnew_SWTTreeViewRowPresenter (handle);
				if (headerHandle == 0) error (SWT.ERROR_NO_HANDLES);
				OS.GridViewRowPresenterBase_Columns (headerHandle, gvColumns);
				OS.HeaderedItemsControl_Header (item, headerHandle);
				OS.GCHandle_Free (headerHandle);
			} else {
				OS.TreeViewItem_HeaderTemplate (item, headerTemplate);
			}
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
	checkWidget ();
	//TODO
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
	if (item == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed ()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	if ((style & SWT.SINGLE) != 0) {
		ignoreSelection = true;
		OS.TreeViewItem_IsSelected (item.handle, true);
		ignoreSelection = false;
		return;
	}
	
	ignoreSelection = true;
	setIsSelectionActiveProperty(true);
	OS.TreeViewItem_IsSelected (item.handle, true);	
	setIsSelectionActiveProperty(false);
	ignoreSelection = false;
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
	int items = OS.ItemsControl_Items (handle);
    int itemCount = OS.ItemCollection_Count (items);
    boolean[] selecting = new boolean[] {true};
    setIsSelectionActiveProperty(true);
    for (int i = 0; i < itemCount; i++) {
		int item = OS.ItemCollection_GetItemAt(items, i);
		fixSelection(item, null, null, selecting);
		OS.GCHandle_Free(item);
	}
    setIsSelectionActiveProperty(false);
    OS.GCHandle_Free(items);
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
	for (int i = 0; i < order.length; i++) {
		TreeColumn column = columns [order [i]];
		int index = OS.IList_IndexOf (gvColumns, column.handle);
		if (index != i) OS.ObservableCollectionGridViewColumn_Move (gvColumns, index, i);	
	}
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
 * 
 * @since 3.1
 */
public void setHeaderVisible (boolean show) {
	checkWidget ();
	headerVisibility = show ? OS.Visibility_Visible : OS.Visibility_Collapsed;
	updateHeaderVisibility ();
	for (int i=0; i<columnCount; i++) {
		TreeColumn column = getColumn(i);
		column.updateImage ();
		column.updateText ();
	}
}

void setIsSelectionActiveProperty(boolean active) {
	int treeType = OS.Object_GetType (handle);
    int propertyName = createDotNetString ("IsSelectionChangeActive", false);
    int property = OS.Type_GetProperty (treeType, propertyName, OS.BindingFlags_Instance | OS.BindingFlags_NonPublic);
    OS.GCHandle_Free (treeType);
    OS.GCHandle_Free (propertyName);
    OS.PropertyInfo_SetValueBoolean (property, handle, active, 0);
	OS.GCHandle_Free (property);
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
	for (int i = 0; i < items.length; i++) {
		TreeItem item = items [i];
		if (item != null && item.isDisposed ()) error (SWT.ERROR_WIDGET_DISPOSED);
	}
	deselectAll ();
	ignoreSelection = true;
	if ((style & SWT.SINGLE) != 0) {
		TreeItem select = items [0];
		if (select != null) {
			OS.TreeViewItem_IsSelected (items [0].handle, true);
		}
	} else {
		setIsSelectionActiveProperty (true);
		for (int i = 0; i < length; i++) {
			TreeItem item = items [i];
			if (item != null) OS.TreeViewItem_IsSelected (item.handle, true);	
		}
		setIsSelectionActiveProperty (false);
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

void updateHeaderVisibility() {
	int template = OS.Control_Template (handle);
	int scrollViewerName = createDotNetString (SCROLLVIEWER_PART_NAME, false);
	int scrollViewer = OS.FrameworkTemplate_FindName (template, scrollViewerName, handle);
	if (scrollViewer != 0) {
		int scrollViewerTemplate = OS.Control_Template(scrollViewer);
		int headerName = createDotNetString(HEADER_PART_NAME, false);
		int header = OS.FrameworkTemplate_FindName (scrollViewerTemplate, headerName, scrollViewer);
		if (header != 0) {
			OS.UIElement_Visibility (header, headerVisibility);
			OS.GCHandle_Free (header);
		}
		OS.GCHandle_Free (scrollViewerTemplate);
		OS.GCHandle_Free (headerName);
		OS.GCHandle_Free (scrollViewer);
	}
	OS.GCHandle_Free (scrollViewerName);
	OS.GCHandle_Free (template);
}

}