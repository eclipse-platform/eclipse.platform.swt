/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import org.eclipse.swt.internal.win32.*;
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
 */

public class TableItem extends Item {
	Table parent;
	int background, foreground;
	int [] cellBackground, cellForeground; 

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
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, parent.getItemCount ());
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
public TableItem (Table parent, int style, int index) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, index);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
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
	int pixel = (background == -1) ? parent.getBackgroundPixel() : background;
	return Color.win32_new (display, pixel);
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
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count -1) return getBackground ();
	int pixel = (cellBackground != null) ? cellBackground [index] : -1;
	if (pixel == -1) return getBackground ();
	return Color.win32_new (display, pixel);
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
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return new Rectangle (0, 0, 0, 0);
	int hwnd = parent.handle;	
	int hwndHeader =  OS.SendMessage (hwnd, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (!(0 <= index && index < count)) return new Rectangle (0, 0, 0, 0); 
	int gridWidth = 0;
	if (parent.getLinesVisible ()) gridWidth = parent.getGridLineWidth ();
	RECT rect = new RECT ();
	rect.top = index;
	rect.left = OS.LVIR_LABEL;
	OS.SendMessage (hwnd, OS. LVM_GETSUBITEMRECT, itemIndex, rect);
	if (index == 0) {
		RECT iconRect = new RECT ();
		iconRect.left = OS.LVIR_ICON;
		OS.SendMessage (hwnd, OS.LVM_GETSUBITEMRECT, itemIndex, iconRect);	
		rect.left = iconRect.left - gridWidth;
	}
	int width = rect.right - rect.left - gridWidth * 2;
	int height = rect.bottom - rect.top - gridWidth * 2;
	/*
	* Bug in Windows.  In version 5.80 of COMCTL32.DLL, the top
	* of the rectangle returned by LVM_GETSUBITEMRECT is off by
	* the grid width when the grid is visible.  The fix is to
	* move the top of the rectangle up by the grid width.
	*/
	if ((COMCTL32_MAJOR << 16 | COMCTL32_MINOR) >= (5 << 16 | 80)) {
		rect.top -= gridWidth;
	}
	return new Rectangle (rect.left + gridWidth, rect.top + gridWidth, width, height);
}

/**
 * Returns <code>true</code> if the receiver is checked,
 * and false otherwise.  When the parent does not have
 * the <code>CHECK style, return false.
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
	if ((parent.style & SWT.CHECK) == 0) return false;
	int index = parent.indexOf (this);
	if (index == -1) return false;
	int hwnd = parent.handle;
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_STATE;
	lvItem.stateMask = OS.LVIS_STATEIMAGEMASK;
	lvItem.iItem = index;
	int result = OS.SendMessage (hwnd, OS.LVM_GETITEM, 0, lvItem);
	return (result != 0) && (((lvItem.state >> 12) & 1) == 0);
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
	int pixel = (foreground == -1) ? parent.getForegroundPixel () : foreground;
	return Color.win32_new (display, pixel);
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
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count -1) return getForeground ();
	int pixel = (cellForeground != null) ? cellForeground [index] : -1;
	if (pixel == -1) return getForeground ();
	return Color.win32_new (display, pixel);
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
	checkWidget();
	if ((parent.style & SWT.CHECK) == 0) return false;
	int index = parent.indexOf (this);
	if (index == -1) return false;
	int hwnd = parent.handle;
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_STATE;
	lvItem.stateMask = OS.LVIS_STATEIMAGEMASK;
	lvItem.iItem = index;
	int result = OS.SendMessage (hwnd, OS.LVM_GETITEM, 0, lvItem);
	return (result != 0) && ((lvItem.state >> 12) > 2);
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
	if (index == 0) return super.getImage ();
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return null;
	int hwnd = parent.handle;
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_IMAGE;
	lvItem.iItem = itemIndex;
	lvItem.iSubItem = index;
	if (OS.SendMessage (hwnd, OS.LVM_GETITEM, 0, lvItem) == 0) return null;
	if (lvItem.iImage >= 0) {
		ImageList imageList = parent.imageList;
		if (imageList != null) return imageList.get (lvItem.iImage);
	}
	return null;
}

/**
 * Returns a rectangle describing the size and location
 * relative to its parent of an image at a column in the
 * table.
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
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return new Rectangle (0, 0, 0, 0);
	int hwnd = parent.handle;	
	int hwndHeader =  OS.SendMessage (hwnd, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (!(0 <= index && index < count)) return new Rectangle (0, 0, 0, 0); 
	int gridWidth = 0;
	if (parent.getLinesVisible ()) gridWidth = parent.getGridLineWidth ();
	RECT rect = new RECT ();
	rect.top = index;
	rect.left = OS.LVIR_ICON;
	OS.SendMessage (hwnd, OS. LVM_GETSUBITEMRECT, itemIndex, rect);
	if (index == 0) {
		rect.left -= gridWidth;
	}
	int width = rect.right - rect.left - gridWidth * 2;
	int height = rect.bottom - rect.top - gridWidth * 2;
	if (index != 0) {
		LVITEM lvItem = new LVITEM ();
		lvItem.mask = OS.LVIF_IMAGE;
		lvItem.iItem = itemIndex;
		lvItem.iSubItem = index;
		if (OS.SendMessage (hwnd, OS.LVM_GETITEM, 0, lvItem) == 0 || lvItem.iImage < 0) {
			width = 0;
		}
	}
	/*
	* Bug in Windows.  In version 5.80 of COMCTL32.DLL, the top
	* of the rectangle returned by LVM_GETSUBITEMRECT is off by
	* the grid width when the grid is visible.  The fix is to
	* move the top of the rectangle up by the grid width.
	*/
	if ((COMCTL32_MAJOR << 16 | COMCTL32_MINOR) >= (5 << 16 | 80)) {
		rect.top -= gridWidth;
	}
	return new Rectangle (rect.left + gridWidth, rect.top + gridWidth, width, height);
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
	int index = parent.indexOf (this);
	if (index == -1) return 0;
	int hwnd = parent.handle;
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_INDENT;
	lvItem.iItem = index;
	OS.SendMessage (hwnd, OS.LVM_GETITEM, 0, lvItem);
	return lvItem.iIndent;
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
	checkWidget();
	return parent;
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_TEXT - if the column at index does not exist</li>
 * </ul>
 */
public String getText (int index) {
	checkWidget();
	if (index == 0) return super.getText ();
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) error (SWT.ERROR_CANNOT_GET_TEXT);
	int cchTextMax = 1024;
	int hwnd = parent.handle;
	int hHeap = OS.GetProcessHeap ();
	int byteCount = cchTextMax * TCHAR.sizeof;
	int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_TEXT;
	lvItem.iItem = itemIndex;
	lvItem.iSubItem = index;
	lvItem.pszText = pszText;
	lvItem.cchTextMax = cchTextMax;
	int result = OS.SendMessage (hwnd, OS.LVM_GETITEM, 0, lvItem);
	TCHAR buffer = new TCHAR (parent.getCodePage (), cchTextMax);
	OS.MoveMemory (buffer, pszText, byteCount);
	OS.HeapFree (hHeap, 0, pszText);
	if (result == 0) error (SWT.ERROR_CANNOT_GET_TEXT);
	return buffer.toString (0, buffer.strlen ());
}

void redraw () {
	if (parent.drawCount > 0) return;
	int hwnd = parent.handle;
	if (!OS.IsWindowVisible (hwnd)) return;
	int index = parent.indexOf (this);
	RECT rect = new RECT ();
	rect.left = OS.LVIR_BOUNDS;
	if (OS.SendMessage (hwnd, OS.LVM_GETITEMRECT, index, rect) != 0) {	
		OS.InvalidateRect (hwnd, rect, true);
	}
}

void releaseChild () {
	super.releaseChild ();
	parent.destroyItem (this);
}

void releaseWidget () {
	super.releaseWidget ();
	parent = null;
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
	int pixel = -1;
	if (color != null) {
		parent.customDraw = true;
		pixel = color.handle;
	}
	background = pixel;
	redraw ();
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
 * 
 */
public void setBackground (int index, Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count -1) return;
	int pixel = -1;
	if (color != null) {
		parent.customDraw = true;
		pixel = color.handle;
	}
	if (cellBackground == null) {
		int hwndHeader = OS.SendMessage (parent.handle, OS.LVM_GETHEADER, 0, 0);
		int itemCount = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
		cellBackground = new int [itemCount];
		for (int i = 0; i < itemCount; i++) {
			cellBackground [i] = -1;
		}
	}
	cellBackground [index] = pixel;
	redraw ();
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
	int index = parent.indexOf (this);
	if (index == -1) return;
	int hwnd = parent.handle;
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_STATE;
	lvItem.stateMask = OS.LVIS_STATEIMAGEMASK;
	lvItem.iItem = index;
	OS.SendMessage (hwnd, OS.LVM_GETITEM, 0, lvItem);
	int state = lvItem.state >> 12;
	if (checked) {
		if ((state & 0x1) != 0) state++;
	} else {
		if ((state & 0x1) == 0) --state;
	}
	lvItem.state = state << 12;
	parent.ignoreSelect = true;
	OS.SendMessage (hwnd, OS.LVM_SETITEM, 0, lvItem);
	parent.ignoreSelect = false;
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
public void setForeground (Color color){
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int pixel = -1;
	if (color != null) {
		parent.customDraw = true;
		pixel = color.handle;
	}
	foreground = pixel;
	redraw ();
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
 * 
 */
public void setForeground (int index, Color color){
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count -1) return;
	int pixel = -1;
	if (color != null) {
		parent.customDraw = true;
		pixel = color.handle;
	}
	if (cellForeground == null) {
		int hwndHeader = OS.SendMessage (parent.handle, OS.LVM_GETHEADER, 0, 0);
		int itemCount = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
		cellForeground = new int [itemCount];
		for (int i = 0; i < itemCount; i++) {
			cellForeground [i] = -1;
		}
	}
	cellForeground [index] = pixel;
	redraw ();
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
	checkWidget();
	if ((parent.style & SWT.CHECK) == 0) return;
	int index = parent.indexOf (this);
	if (index == -1) return;
	int hwnd = parent.handle;
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_STATE;
	lvItem.stateMask = OS.LVIS_STATEIMAGEMASK;
	lvItem.iItem = index;
	OS.SendMessage (hwnd, OS.LVM_GETITEM, 0, lvItem);
	int state = lvItem.state >> 12;
	if (grayed) {
		if (state <= 2) state +=2;
	} else {
		if (state > 2) state -=2;
	}
	lvItem.state = state << 12;
	parent.ignoreSelect = true;
	OS.SendMessage (hwnd, OS.LVM_SETITEM, 0, lvItem);
	parent.ignoreSelect = false;
}

/**
 * Sets the image for multiple columns in the Table. 
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
	if (index == 0) {
		super.setImage (image);
	}
	int hwnd = parent.handle;
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_IMAGE;
	lvItem.iItem = itemIndex;
	lvItem.iSubItem = index;
	lvItem.iImage = parent.imageIndex (image);
	OS.SendMessage (hwnd, OS.LVM_SETITEM, 0, lvItem);
	if (index == 0) parent.setScrollWidth ();
	parent.fixCheckboxImageList ();
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
 */
public void setImageIndent (int indent) {
	checkWidget();
	if (indent < 0) return;
	int index = parent.indexOf (this);
	if (index == -1) return;
	int hwnd = parent.handle;
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_INDENT;
	lvItem.iItem = index;
	lvItem.iIndent = indent;
	OS.SendMessage (hwnd, OS.LVM_SETITEM, 0, lvItem);
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
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return;
	if (index == 0) {
		super.setText (string);
	}
	int hwnd = parent.handle;
	int hHeap = OS.GetProcessHeap ();	
	TCHAR buffer = new TCHAR (parent.getCodePage (), string, true);
	int byteCount = buffer.length () * TCHAR.sizeof;
	int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	OS.MoveMemory (pszText, buffer, byteCount); 
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_TEXT;
	lvItem.iItem = itemIndex;
	lvItem.pszText = pszText;
	lvItem.iSubItem = index;
	OS.SendMessage (hwnd, OS.LVM_SETITEM, 0, lvItem);
	if (index == 0) parent.setScrollWidth ();
	OS.HeapFree (hHeap, 0, pszText);
}

public void setText (String string) {
	checkWidget();
	setText (0, string);
}

}
