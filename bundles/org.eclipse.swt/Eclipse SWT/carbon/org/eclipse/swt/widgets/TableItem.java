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


import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;

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
	String [] strings;
	Image [] images;
	boolean checked, grayed, cached;
	Color foreground, background;
	Color[] cellForeground, cellBackground;
	Font font;
	Font[] cellFont;
	int width = -1;
	
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
	this (parent, style, checkNull (parent).getItemCount (), true);
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
	this (parent, style, index, true);
}

TableItem (Table parent, int style, int index, boolean create) {
	super (parent, style);
	this.parent = parent;
	if (create) parent.createItem (this, index);
}

static Table checkNull (Table control) {
	if (control == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	return control;
}

int calculateWidth (int index, GC gc) {
	if (index == 0 && width != -1) return width;
	int width = 0;
	Image image = index == 0 ? this.image : (images == null ? null : images [index]);
	String text = index == 0 ? this.text : (strings == null ? "" : strings [index]);
	Font font = null;
	if (cellFont != null) font = cellFont[index];
	if (font == null) font = this.font;
	if (font == null) font = parent.getFont();
	gc.setFont (font);
	if (image != null) width += image.getBounds ().width + parent.getGap ();
	if (text != null && text.length () > 0) width += gc.stringExtent (text).x;
	boolean sendMeasure = true;
	if ((parent.style & SWT.VIRTUAL) != 0) {
		sendMeasure = cached;
	}
	if (sendMeasure && parent.hooks (SWT.MeasureItem)) {
		Event event = new Event ();
		event.item = this;
		event.index = index;
		event.gc = gc;
		short [] height = new short [1];
		OS.GetDataBrowserTableViewRowHeight (parent.handle, height);
		event.width = width;
		event.height = height[0];
		parent.sendEvent (SWT.MeasureItem, event);
		if (height[0] < event.height) {
			OS.SetDataBrowserTableViewRowHeight (parent.handle, (short) event.height);
			redrawWidget (parent.handle, false);
		}
		width = event.width;
	}
	if (index == 0) this.width = width;
	return width;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void clear () {
	text = "";
	image = null;
	strings = null;
	images = null;
	checked = grayed = cached = false;
	foreground = background = null;
	cellForeground = cellBackground = null;
	font = null;
	cellFont = null;
	width = -1;
}

void destroyWidget () {
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
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
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	Rect rect = new Rect();
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return new Rectangle (0, 0, 0, 0);
	int id = parent.getId (itemIndex);
	int columnId = parent.columnCount == 0 ? parent.column_id : parent.columns [0].id;
	if (OS.GetDataBrowserItemPartBounds (parent.handle, id, columnId, OS.kDataBrowserPropertyContentPart, rect) != OS.noErr) {
		return new Rectangle (0, 0, 0, 0);
	}
	int x = rect.left, y = rect.top;
	int width = 0;
	if (image != null) {
		Rectangle bounds = image.getBounds ();
		x += bounds.width + parent.getGap ();
	}
	GC gc = new GC (parent);
	Point extent = gc.stringExtent (text);
	gc.dispose ();
	width += extent.x;
	if (parent.columnCount > 0) {
		width = Math.min (width, rect.right - x);
	}
	int height = rect.bottom - rect.top;
	return new Rectangle (x, y, width, height);
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
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	parent.checkItems (true);
	if (index != 0 && !(0 <= index && index < parent.columnCount)) return new Rectangle (0, 0, 0, 0);
	Rect rect = new Rect();
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return new Rectangle (0, 0, 0, 0);
	int id = parent.getId (itemIndex);
	int columnId = parent.columnCount == 0 ? parent.column_id : parent.columns [index].id;
	if (OS.GetDataBrowserItemPartBounds (parent.handle, id, columnId, OS.kDataBrowserPropertyEnclosingPart, rect) != OS.noErr) {
		return new Rectangle (0, 0, 0, 0);
	}
	int x, y, width, height;
	if (OS.VERSION >= 0x1040) {
		if (parent.getLinesVisible ()) {
			rect.left += Table.GRID_WIDTH;
			rect.top += Table.GRID_WIDTH;
		}
		x = rect.left;
		y = rect.top;
		width = rect.right - rect.left;
		height = rect.bottom - rect.top;
	} else {
		Rect rect2 = new Rect();
		if (OS.GetDataBrowserItemPartBounds (parent.handle, id, columnId, OS.kDataBrowserPropertyContentPart, rect2) != OS.noErr) {
			return new Rectangle (0, 0, 0, 0);
		}
		x = rect2.left;
		y = rect2.top;
		width = rect.right - rect2.left + 1;
		height = rect2.bottom - rect2.top + 1;
	}
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
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if ((parent.style & SWT.CHECK) == 0) return false;
	return grayed;
}

public Image getImage () {
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
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
 */
public Image getImage (int index) {
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index == 0) return getImage ();
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
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	parent.checkItems (true);
	if (index != 0 && !(0 <= index && index < parent.columnCount)) return new Rectangle (0, 0, 0, 0);
	Rect rect = new Rect();
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return new Rectangle (0, 0, 0, 0);
	int id = parent.getId (itemIndex);
	int columnId = parent.columnCount == 0 ? parent.column_id : parent.columns [index].id;
	if (OS.GetDataBrowserItemPartBounds (parent.handle, id, columnId, OS.kDataBrowserPropertyContentPart, rect) != OS.noErr) {
		return new Rectangle (0, 0, 0, 0);
	}
	int x = rect.left, y = rect.top;
	int width = 0;
	if (index == 0 && image != null) {
		Rectangle bounds = image.getBounds ();
		width += bounds.width;
	}
	if (index != 0 && images != null && images[index] != null) {
		Rectangle bounds = images [index].getBounds ();
		width += bounds.width;
	}
	int height = rect.bottom - rect.top + 1;
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
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	return 0;
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

public String getText () {
	checkWidget ();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	return super.getText ();
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	if (index == 0) return getText ();
	if (strings != null) {
		if (0 <= index && index < strings.length) {
			String string = strings [index];
			return string != null ? string : "";
		}
	}
	return "";
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	parent.checkItems (true);
	if (index != 0 && !(0 <= index && index < parent.columnCount)) return new Rectangle (0, 0, 0, 0);
	Rect rect = new Rect();
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return new Rectangle (0, 0, 0, 0);
	int id = parent.getId (itemIndex);
	int columnId = parent.columnCount == 0 ? parent.column_id : parent.columns [index].id;
	if (OS.GetDataBrowserItemPartBounds (parent.handle, id, columnId, OS.kDataBrowserPropertyEnclosingPart, rect) != OS.noErr) {
		return new Rectangle (0, 0, 0, 0);
	}
	int imageWidth = 0;
	int margin = parent.getInsetWidth () / 2;
	Image image = getImage (index);
	if (image != null) {
		Rectangle bounds = image.getBounds ();
		imageWidth = bounds.width + parent.getGap ();
	}
	int x, y, width, height;
	if (OS.VERSION >= 0x1040) {
		if (parent.getLinesVisible ()) {
			rect.left += Table.GRID_WIDTH;
			rect.top += Table.GRID_WIDTH;
		}
		x = rect.left + imageWidth + margin;
		y = rect.top;
		width = Math.max (0, rect.right - rect.left - imageWidth - margin * 2);
		height = rect.bottom - rect.top;
	} else {
		Rect rect2 = new Rect();
		if (OS.GetDataBrowserItemPartBounds (parent.handle, id, columnId, OS.kDataBrowserPropertyContentPart, rect2) != OS.noErr) {
			return new Rectangle (0, 0, 0, 0);
		}
		x = rect2.left + imageWidth + margin;
		y = rect2.top;
		width = Math.max (0, rect.right - rect2.left + 1 - imageWidth - margin * 2);
		height = rect2.bottom - rect2.top + 1;
	}
	return new Rectangle (x, y, width, height);
}

void redraw (int propertyID) {
	if (parent.currentItem == this) return;
	if (!getDrawing () && propertyID != Table.CHECK_COLUMN_ID) return;
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return;
	int [] id = new int [] {parent.getId (itemIndex)};
	OS.UpdateDataBrowserItems (parent.handle, OS.kDataBrowserNoItem, id.length, id, OS.kDataBrowserItemNoProperty, propertyID);
	/*
	* Bug in the Macintosh. When the height of the row is smaller than the
	* check box, the tail of the check mark draws outside of the item part
	* bounds. This means it will not be redrawn when the item is unckeched.
	* The fix is to redraw the area.
	*/
	if (propertyID == Table.CHECK_COLUMN_ID) {
		Rect rect = new Rect();
		if (OS.GetDataBrowserItemPartBounds (parent.handle, parent.getId (itemIndex), propertyID, OS.kDataBrowserPropertyEnclosingPart, rect) == OS.noErr) {
			int x = rect.left;
			int y = rect.top - 1;
			int width = rect.right - rect.left;
			int height = 1;
			redrawWidget (parent.handle, x, y, width, height, false);
		}
	}
}

void releaseHandle () {
	super.releaseHandle ();
	parent = null;
}

void releaseParent () {
	super.releaseParent ();
	parent.checkItems (true);
}

void releaseWidget () {
	super.releaseWidget ();
	strings = null;
	images = null;
	background = foreground = null;
	font = null;
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
	Color oldColor = background;
	if (oldColor == color) return;
	background = color;
	if (oldColor != null && oldColor.equals (color)) return;
	cached = true;
	redraw (OS.kDataBrowserNoItem);
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
	int count = Math.max (1, parent.columnCount);
	if (0 > index || index > count - 1) return;
	if (cellBackground == null) {
		if (color == null) return;
		cellBackground = new Color [count];
	}
	Color oldColor = cellBackground [index];
	if (oldColor == color) return;
	cellBackground [index] = color;
	if (oldColor != null && oldColor.equals (color)) return;
	cached = true;
	redraw (OS.kDataBrowserNoItem);
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
	checkWidget ();
	if ((parent.style & SWT.CHECK) == 0) return;
	if (this.checked == checked) return;
	setChecked (checked, false);
}

void setChecked (boolean checked, boolean notify) {
	this.checked = checked;
	cached = true;
	redraw (Table.CHECK_COLUMN_ID);
	if (notify) {
		Event event = new Event ();
		event.item = this;
		event.detail = SWT.CHECK;
		parent.postEvent (SWT.Selection, event);
	}
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
public void setFont (Font font) {
	checkWidget ();
	if (font != null && font.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	Font oldFont = this.font;
	if (oldFont == font) return;
	this.font = font;
	if (oldFont != null && oldFont.equals (font)) return;
	cached = true;
	redraw (OS.kDataBrowserNoItem);
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
	int count = Math.max (1, parent.columnCount);
	if (0 > index || index > count - 1) return;
	if (cellFont == null) {
		if (font == null) return;
		cellFont = new Font [count];
	}
	Font oldFont = cellFont [index];
	if (oldFont == font) return;
	cellFont [index] = font;
	if (oldFont != null && oldFont.equals (font)) return;
	cached = true;
	redraw (OS.kDataBrowserNoItem);
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
public void setForeground (Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	Color oldColor = foreground;
	if (oldColor == color) return;
	foreground = color;
	if (oldColor != null && oldColor.equals (color)) return;
	cached = true;
	redraw (OS.kDataBrowserNoItem);
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
	int count = Math.max (1, parent.columnCount);
	if (0 > index || index > count - 1) return;
	if (cellForeground == null) {
		if (color == null) return;
		cellForeground = new Color [count];
	}
	Color oldColor = cellForeground [index];
	if (oldColor == color) return;
	cellForeground [index] = color;
	if (oldColor != null && oldColor.equals (color)) return;
	cached = true;
	redraw (OS.kDataBrowserNoItem);
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
	cached = true;
	redraw (Table.CHECK_COLUMN_ID);
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
	checkWidget();
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
	checkWidget();
	if (image != null && image.isDisposed ()) {
		error(SWT.ERROR_INVALID_ARGUMENT);
	}
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return;
	if (parent.imageBounds == null && image != null) {
		parent.setItemHeight (image);
	}
	if (index == 0)  {
		if (image != null && image.type == SWT.ICON) {
			if (image.equals (this.image)) return;
		}
		width = -1;
		super.setImage (image);
	}
	int count = Math.max (1, parent.columnCount);
	if (0 <= index && index < count) {
		if (images == null) images = new Image [count];
		if (image != null && image.type == SWT.ICON) {
			if (image.equals (images [index])) return;
		}
		images [index] = image;	
	}
	cached = true;
	if (index == 0) parent.setScrollWidth (this);
	redraw (OS.kDataBrowserNoItem);
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
	checkWidget();
	if (indent < 0) return;
	cached = true;
	/* Image indent is not supported on the Macintosh */
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
	checkWidget();
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == 0) {
		if (string.equals (text)) return;
		width = -1;
		super.setText (string);
	}
	int count = Math.max (1, parent.columnCount);
	if (0 <= index && index < count) {
		if (strings == null) strings = new String [count];
		if (string.equals (strings [index])) return;
		strings [index] = string;
	}
	cached = true;
	if (index == 0) parent.setScrollWidth (this);
	redraw (OS.kDataBrowserNoItem);
}

public void setText (String string) {
	checkWidget();
	setText (0, string);
}

}
