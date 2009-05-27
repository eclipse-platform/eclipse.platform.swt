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

/**
 * Instances of this class represent a selectable user interface object
 * that represents an item in a table.
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
 * @see <a href="http://www.eclipse.org/swt/snippets/#table">Table, TableItem, TableColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class TableItem extends Item {
	Table parent;
	Image [] images;
	String [] strings;
	Color [] cellBackground, cellForeground;
	Font [] cellFont;
	int rowHandle;
	boolean checked, grayed, cached; 
	Color background, foreground;
	Font font;
	
/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TableItem (Table parent, int style) {
	this (parent, style, -1);
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Table</code>), a style value
 * describing its behavior and appearance, and the index
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
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
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
public TableItem (Table parent, int style, int index) {
	this (parent, style, index, 0);
}

TableItem (Table parent, int style, int index, int handle) {
	super (parent, style);
	this.parent = parent;
	this.handle = handle;
	if (handle == 0) {
		parent.createItem (this, index);
	} else {
		createWidget ();
	}
}

double computeWidth (int columnIndex) {
	int rowPresenterType = OS.GridViewRowPresenter_typeid ();
	double width = 0;
	if (rowHandle != 0) {
		int contentPresenter = OS.VisualTreeHelper_GetChild (rowHandle, columnIndex);
		int availSize = OS.gcnew_Size (0x7FFFFFFF,0x7FFFFFFF);
		OS.UIElement_Measure (contentPresenter, availSize);
		OS.GCHandle_Free (availSize);
		int size = OS.UIElement_DesiredSize (contentPresenter);
		width = OS.Size_Width (size);
		OS.GCHandle_Free (size);
		OS.GCHandle_Free (contentPresenter);
	}
	OS.GCHandle_Free (rowPresenterType);
	return width;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void columnAdded (int index) {
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

void columnRemoved (int index) {
	if (strings != null) {
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

void createHandle () {
	if (handle == 0) {
		handle = OS.gcnew_ListViewItem ();
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	}
	OS.Control_HorizontalContentAlignment (handle, OS.HorizontalAlignment_Stretch);
	OS.Control_VerticalContentAlignment (handle, OS.VerticalAlignment_Stretch);
}

void clear () {
	strings = null;
	images = null;
	checked = grayed = false;
	setFont (null);
	setForeground (null);
	setBackground (null);
	if ((parent.style & SWT.VIRTUAL) != 0) cached = false;
	updateCheck ();
	int columns = parent.columnCount == 0 ? 1 : parent.columnCount;
	for (int i = 0; i < columns; i++) {
		updateText (i);
		updateImage (i);
		updateBackground (i);
		updateForeground (i);
		updateFont (i);
	}
	if ((parent.style & SWT.VIRTUAL) != 0) cached = false;
	int part = findPart (0, Table.RENDER_PANEL_NAME);
	if (part != 0) OS.UIElement_InvalidateVisual (part);
	OS.GCHandle_Free (part);
}

void deregister () {
	display.removeWidget (handle);
}

void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

int findRowPresenter (int current, int rowPresenterType) {
	int type = OS.Object_GetType (current);
	boolean found = OS.Object_Equals (rowPresenterType, type);
	OS.GCHandle_Free (type);
	if (found) return current;
	int childCount = OS.VisualTreeHelper_GetChildrenCount (current);
	for (int i = 0; i < childCount; i++) {
		int child = OS.VisualTreeHelper_GetChild (current, i);
		int result = findRowPresenter (child, rowPresenterType);
		if (child != result) OS.GCHandle_Free (child);
		if (result != 0) return result;
	}
	return 0;
}

int findPart (int column, String partName) {
	if (rowHandle == 0) return 0; //not Loaded yet.
	updateLayout (rowHandle);
	int contentPresenter = OS.VisualTreeHelper_GetChild (rowHandle, column);
	if (contentPresenter == 0) return 0;
	int columnHandle;
	if (parent.columnCount == 0) {
		int columns = OS.GridView_Columns (parent.gridViewHandle);
		columnHandle = OS.GridViewColumnCollection_default (columns, column);
		OS.GCHandle_Free (columns);
	} else {
		columnHandle = parent.columns [column].handle;
	}
	int cellTemplate = OS.GridViewColumn_CellTemplate (columnHandle);
	int name = createDotNetString (partName, false);
	int result = OS.FrameworkTemplate_FindName (cellTemplate, name, contentPresenter);
	OS.GCHandle_Free (contentPresenter);
	if (parent.columnCount == 0) OS.GCHandle_Free (columnHandle);
	OS.GCHandle_Free (cellTemplate);
	OS.GCHandle_Free (name);
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
 * @since 3.0
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
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent.
 *
 * @return the receiver's bounding rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public Rectangle getBounds () {
	checkWidget();
	return getTextBounds (0);
}

/**
 * Returns a rectangle describing the receiver's size and location
 * relative to its parent at a column in the table.
 *
 * @param index the index that specifies the column
 * @return the receiver's bounding column rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getBounds (int index) {
	checkWidget();
	if (index != 0 && !parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (!(0 <= index && index < parent.columnCount)) return new Rectangle (0, 0, 0, 0);
	int rowPresenterType = OS.GridViewRowPresenter_typeid ();
	int contentPresenter = OS.VisualTreeHelper_GetChild (rowHandle, index);
	int point = OS.gcnew_Point (0, 0);
	if (point == 0) error (SWT.ERROR_NO_HANDLES);
	int parentHandle = parent.topHandle ();
	int location = OS.UIElement_TranslatePoint (contentPresenter, point, parentHandle);
	int x = (int) OS.Point_X (location);
	int y = (int) OS.Point_Y (location);
	int width = (int) OS.FrameworkElement_ActualWidth (contentPresenter);
	int height = (int) OS.FrameworkElement_ActualHeight (handle);
	OS.GCHandle_Free (rowPresenterType);
	OS.GCHandle_Free (point);
	OS.GCHandle_Free (location);
	OS.GCHandle_Free (contentPresenter);
	return new Rectangle (x, y, width, height);
}

/**
 * Returns <code>true</code> if the receiver is checked,
 * and false otherwise.  When the parent does not have
 * the <code>CHECK</code> style, return false.
 *
 * @return the checked state of the checkbox
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
	return checked;
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
 * @since 3.0
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
 * @since 3.0
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
 * the <code>CHECK</code> style, return false.
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
 * table.  An empty rectangle is returned if index exceeds
 * the index of the table's last column.
 *
 * @param index the index that specifies the column
 * @return the receiver's bounding image rectangle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Rectangle getImageBounds (int index) {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index != 0 && !(0 <= index && index < parent.columnCount)) return new Rectangle (0, 0, 0, 0);
	int parentHandle = parent.topHandle ();
	int image = findPart (index, Table.IMAGE_PART_NAME);
	int point = OS.gcnew_Point (0, 0);
	if (point == 0) error (SWT.ERROR_NO_HANDLES);
	int location = OS.UIElement_TranslatePoint (image, point, parentHandle);
	int x = (int) OS.Point_X (location);
	int y = (int) OS.Point_Y (location);
	OS.GCHandle_Free (point);
	OS.GCHandle_Free (location);
	int width = (int) OS.FrameworkElement_ActualWidth (image);
	int height = (int) OS.FrameworkElement_ActualHeight (image);
	OS.GCHandle_Free (image);
	return new Rectangle (x, y, width, height);
}

/**
 * Gets the image indent.
 *
 * @return the indent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getImageIndent () {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	//TODO
	return 0;
//	return imageIndent;
}

String getNameText () {
	if ((parent.style & SWT.VIRTUAL) != 0) {
		if (!cached) return "*virtual*"; //$NON-NLS-1$
	}
	return super.getNameText ();
}

/**
 * Returns the receiver's parent, which must be a <code>Table</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Table getParent () {
	checkWidget ();
	return parent;
}

/**
 * Returns a rectangle describing the size and location
 * relative to its parent of the text at a column in the
 * table.  An empty rectangle is returned if index exceeds
 * the index of the table's last column.
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
	int textBlock = findPart (index, Table.TEXT_PART_NAME);
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
 */
public String getText (int index) {
	checkWidget ();
	if (!parent.checkData (this)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (strings != null && 0 <= index && index < strings.length) {
		return strings [index]!= null ? strings[index] : "";
	}
	return "";
}

Control getWidgetControl () {
	return parent;
}

void register () {
	display.addWidget (handle, this);
}

void releaseHandle () {
	super.releaseHandle ();
	if (handle != 0) OS.GCHandle_Free (handle);
	handle = 0;
	if (rowHandle != 0) OS.GCHandle_Free (rowHandle);
	rowHandle = 0;
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
 */
public void setBackground (Color color) {
	checkWidget ();
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
 * @since 3.0
 */
public void setBackground (int index, Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	if (cellBackground == null) cellBackground = new Color [count];
	cellBackground [index] = color;
	updateBackground (index);
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
}

/**
 * Sets the checked state of the checkbox for this item.  This state change 
 * only applies if the Table was created with the SWT.CHECK style.
 *
 * @param checked the new checked state of the checkbox
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setChecked (boolean checked) {
	checkWidget();
	if ((parent.style & SWT.CHECK) == 0) return;
	if (this.checked == checked) return;
	this.checked = checked;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	updateCheck ();
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
 * @since 3.0
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
 */
public void setForeground (Color color){
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
 * @since 3.0
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
 * only applies if the Table was created with the SWT.CHECK style.
 *
 * @param grayed the new grayed state of the checkbox; 
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
	updateCheck ();
}

/**
 * Sets the image for multiple columns in the table. 
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
 */
public void setImage (int index, Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
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
 * Sets the indent of the first column's image, expressed in terms of the image's width.
 *
 * @param indent the new indent
 *
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @deprecated this functionality is not supported on most platforms
 */
public void setImageIndent (int indent) {
	checkWidget ();
}

/**
 * Sets the text for multiple columns in the table. 
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
	int panel = findPart (index, Table.CONTENTPANEL_PART_NAME);
	if (panel != 0) {
		if (cellBackground != null && cellBackground [index] != null) {
			int brush = OS.gcnew_SolidColorBrush (cellBackground [index].handle);
			int current = OS.Panel_Background (panel);
			if (!OS.Object_Equals (brush, current))
				OS.Panel_Background (panel, brush);
			OS.GCHandle_Free (current);
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
	int checkBox = findPart (0, Table.CHECKBOX_PART_NAME);
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
	int textBlock = findPart (index, Table.TEXT_PART_NAME);
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
	int textBlock = findPart (index, Table.TEXT_PART_NAME);
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
	int img = findPart (index, Table.IMAGE_PART_NAME);
	if (img != 0) {
		int src = 0;
		if (images != null) src = images [index] != null ? images [index].handle : 0;
		int current = OS.Image_Source (img);
		OS.Image_Source (img, src);
		OS.GCHandle_Free (current);
		OS.GCHandle_Free (img);
	}
}

void updateText (int index) {
	int textBlock = findPart (index, Table.TEXT_PART_NAME);
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
