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
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#table">Table, TableItem, TableColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class TableItem extends Item {
	Table parent;
	String [] strings;
	Image [] images;
	Font font;
	Font [] cellFont;
	boolean checked, grayed, cached;
	int imageIndent, background = -1, foreground = -1; 
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

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void clear () {
	text = "";
	image = null;
	strings = null;
	images = null;
	imageIndent = 0;
	checked = grayed = false;
	font = null;
	background = foreground = -1;
	cellFont = null; 
	cellBackground = cellForeground = null;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = false;
}

void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

int /*long*/ fontHandle (int index) {
	if (cellFont != null && cellFont [index] != null) return cellFont [index].handle;
	if (font != null) return font.handle;
	return -1;
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
	if (background == -1) return parent.getBackground ();
	return Color.win32_new (display, background);
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
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return getBackground ();
	int pixel = cellBackground != null ? cellBackground [index] : -1;
	return pixel == -1 ? getBackground () : Color.win32_new (display, pixel);
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return new Rectangle (0, 0, 0, 0);
	RECT rect = getBounds (itemIndex, 0, true, false, false);
	int width = rect.right - rect.left, height = rect.bottom - rect.top;
	return new Rectangle (rect.left, rect.top, width, height);
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
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return new Rectangle (0, 0, 0, 0);
	RECT rect = getBounds (itemIndex, index, true, true, true);
	int width = rect.right - rect.left, height = rect.bottom - rect.top;
	return new Rectangle (rect.left, rect.top, width, height);
}

RECT getBounds (int row, int column, boolean getText, boolean getImage, boolean fullText) {
	return getBounds (row, column, getText, getImage, fullText, false, 0);
}

RECT getBounds (int row, int column, boolean getText, boolean getImage, boolean fullText, boolean fullImage, int /*long*/ hDC) {
	if (!getText && !getImage) return new RECT ();
	int columnCount = parent.getColumnCount ();
	if (!(0 <= column && column < Math.max (1, columnCount))) {
		return new RECT ();
	}
	if (parent.fixScrollWidth) parent.setScrollWidth (null, true);
	RECT rect = new RECT ();
	int /*long*/ hwnd = parent.handle;
	int bits = (int)/*64*/OS.SendMessage (hwnd, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
	if (column == 0 && (bits & OS.LVS_EX_FULLROWSELECT) == 0) {
		if (parent.explorerTheme) {
			rect.left = OS.LVIR_ICON;
			parent.ignoreCustomDraw = true;
			int /*long*/ code = OS.SendMessage (hwnd, OS. LVM_GETITEMRECT, row, rect);
			parent.ignoreCustomDraw = false;
			if (code == 0) return new RECT ();
			if (getText) {
				int width = 0;
				int /*long*/ hFont = fontHandle (column);
				if (hFont == -1 && hDC == 0) {
					TCHAR buffer = new TCHAR (parent.getCodePage (), text, true);
					width = (int)/*64*/OS.SendMessage (hwnd, OS.LVM_GETSTRINGWIDTH, 0, buffer);
				} else {
					TCHAR buffer = new TCHAR (parent.getCodePage (), text, false);
					int /*long*/ textDC = hDC != 0 ? hDC : OS.GetDC (hwnd), oldFont = -1;
					if (hDC == 0) {
						if (hFont == -1) hFont = OS.SendMessage (hwnd, OS.WM_GETFONT, 0, 0);
						oldFont = OS.SelectObject (textDC, hFont);
					}
					RECT textRect = new RECT ();
					int flags = OS.DT_NOPREFIX | OS.DT_SINGLELINE | OS.DT_CALCRECT;
					OS.DrawText (textDC, buffer, buffer.length (), textRect, flags);
					width = textRect.right - textRect.left;
					if (hDC == 0) {
						if (oldFont != -1) OS.SelectObject (textDC, oldFont);
						OS.ReleaseDC (hwnd, textDC);
					}
				}
				if (!getImage) rect.left = rect.right;
				rect.right += width + Table.INSET * 2;
			}
		} else {
			if (getText) {
				rect.left = OS.LVIR_SELECTBOUNDS;
				parent.ignoreCustomDraw = true;
				int /*long*/ code = OS.SendMessage (hwnd, OS.LVM_GETITEMRECT, row, rect);
				parent.ignoreCustomDraw = false;
				if (code == 0) return new RECT ();
				if (!getImage) {
					RECT iconRect = new RECT ();
					iconRect.left = OS.LVIR_ICON;
					parent.ignoreCustomDraw = true;
					code = OS.SendMessage (hwnd, OS. LVM_GETITEMRECT, row, iconRect);
					parent.ignoreCustomDraw = false;
					if (code != 0) rect.left = iconRect.right;
				}
			} else {
				rect.left = OS.LVIR_ICON;
				parent.ignoreCustomDraw = true;
				int /*long*/ code = OS.SendMessage (hwnd, OS.LVM_GETITEMRECT, row, rect);
				parent.ignoreCustomDraw = false;
				if (code == 0) return new RECT ();
			}
		}
		if (fullText || fullImage) {
			RECT headerRect = new RECT ();
			int /*long*/ hwndHeader = OS.SendMessage (hwnd, OS.LVM_GETHEADER, 0, 0);
			OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, 0, headerRect);
			OS.MapWindowPoints (hwndHeader, hwnd, headerRect, 2);
			if (getText && fullText) rect.right = headerRect.right;
			if (getImage && fullImage) rect.left = headerRect.left;
		}
	} else {
		/*
		* Feature in Windows.  LVM_GETSUBITEMRECT returns an image width
		* even when the subitem does not contain an image.  The fix is to
		* test for this case and adjust the rectangle to represent the area
		* the table is actually drawing.
		*/
		boolean hasImage = (column == 0 && image != null) || (images != null && images [column] != null);
		rect.top = column;
		if (fullText || fullImage || hDC == 0) {
			/*
			* Bug in Windows.  Despite the fact that the documentation states
			* that LVIR_BOUNDS and LVIR_LABEL are identical when used with
			* LVM_GETSUBITEMRECT, LVIR_BOUNDS can return a zero height.  The
			* fix is to use LVIR_LABEL.
			*/
			rect.left = getText ? OS.LVIR_LABEL : OS.LVIR_ICON;
			parent.ignoreCustomDraw = true;
			int /*long*/ code = OS.SendMessage (hwnd, OS. LVM_GETSUBITEMRECT, row, rect);
			parent.ignoreCustomDraw = false;
			if (code == 0) return new RECT ();
			/*
			* Feature in Windows.  Calling LVM_GETSUBITEMRECT with LVIR_LABEL
			* and zero for the column number gives the bounds of the first item
			* without including the bounds of the icon.  This is undocumented.
			* When called with values greater than zero, the icon bounds are
			* included and this behavior is documented.  If the icon is needed
			* in the bounds of the first item, the fix is to adjust the item
			* bounds using the icon bounds.
			*/
			if (column == 0 && getText && getImage) {
				RECT iconRect = new RECT ();
				iconRect.left = OS.LVIR_ICON;
				parent.ignoreCustomDraw = true;
				code = OS.SendMessage (hwnd, OS. LVM_GETSUBITEMRECT, row, iconRect);
				parent.ignoreCustomDraw = false;
				if (code != 0) rect.left = iconRect.left;
			}
			if (hasImage) {
				if (column != 0 && getText && !getImage) {
					RECT iconRect = new RECT ();
					iconRect.top = column;		
					iconRect.left = OS.LVIR_ICON;
					if (OS.SendMessage (hwnd, OS. LVM_GETSUBITEMRECT, row, iconRect) != 0) {
						rect.left = iconRect.right + Table.INSET / 2;
					}
				}
			} else {
				if (getImage && !getText) rect.right = rect.left;
			}
			if (column == 0 && fullImage) {
				RECT headerRect = new RECT ();
				int /*long*/ hwndHeader = OS.SendMessage (hwnd, OS.LVM_GETHEADER, 0, 0);
				OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, 0, headerRect);
				OS.MapWindowPoints (hwndHeader, hwnd, headerRect, 2);
				rect.left = headerRect.left;
			}
		} else {
			rect.left = OS.LVIR_ICON;
			parent.ignoreCustomDraw = true;
			int /*long*/ code = OS.SendMessage (hwnd, OS. LVM_GETSUBITEMRECT, row, rect);
			parent.ignoreCustomDraw = false;
			if (code == 0) return new RECT ();
			if (!hasImage) rect.right = rect.left;
			if (getText) {
				String string = column == 0 ? text : strings != null ? strings [column] : null;
				if (string != null) {
					RECT textRect = new RECT ();
					TCHAR buffer = new TCHAR (parent.getCodePage (), string, false);
					int flags = OS.DT_NOPREFIX | OS.DT_SINGLELINE | OS.DT_CALCRECT;
					OS.DrawText (hDC, buffer, buffer.length (), textRect, flags);
					rect.right += textRect.right - textRect.left + Table.INSET * 3 + 1;
				}
			}
		}
	}
	/*
	* Bug in Windows.  In version 5.80 of COMCTL32.DLL, the top
	* of the rectangle returned by LVM_GETSUBITEMRECT is off by
	* the grid width when the grid is visible.  The fix is to
	* move the top of the rectangle up by the grid width.
	*/
	int gridWidth = parent.getLinesVisible () ? Table.GRID_WIDTH : 0;
	if (OS.COMCTL32_VERSION >= OS.VERSION (5, 80)) rect.top -= gridWidth;
	if (column != 0) rect.left += gridWidth;
	rect.right = Math.max (rect.right, rect.left);
	rect.top += gridWidth;
	rect.bottom = Math.max (rect.bottom - gridWidth, rect.top);
	return rect;
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
	int count = Math.max (1, parent.getColumnCount ());
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
	if (foreground == -1) return parent.getForeground ();
	return Color.win32_new (display, foreground);
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
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count -1) return getForeground ();
	int pixel = cellForeground != null ? cellForeground [index] : -1;
	return pixel == -1 ? getForeground () : Color.win32_new (display, pixel);
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
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return new Rectangle (0, 0, 0, 0);
	RECT rect = getBounds (itemIndex, index, false, true, false);
	int width = rect.right - rect.left, height = rect.bottom - rect.top;
	return new Rectangle (rect.left, rect.top, width, height);
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
	return imageIndent;
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
	checkWidget();
	return parent;
}

public String getText () {
	checkWidget();
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
	checkWidget();
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
	checkWidget();
	if (!parent.checkData (this, true)) error (SWT.ERROR_WIDGET_DISPOSED);
	int itemIndex = parent.indexOf (this);
	if (itemIndex == -1) return new Rectangle (0, 0, 0, 0);
	RECT rect = getBounds (itemIndex, index, true, false, true);
	rect.left += 2;
	if (index != 0) rect.left += Table.INSET;
	rect.left = Math.min (rect.left, rect.right);
	rect.right = rect.right - Table.INSET;
	int width = Math.max (0, rect.right - rect.left);
	int height = Math.max (0, rect.bottom - rect.top);
	return new Rectangle (rect.left, rect.top, width, height);
}

void redraw () {
	if (parent.currentItem == this || !parent.getDrawing ()) return;
	int /*long*/ hwnd = parent.handle;
	if (!OS.IsWindowVisible (hwnd)) return;
	int index = parent.indexOf (this);
	if (index == -1) return;
	OS.SendMessage (hwnd, OS.LVM_REDRAWITEMS, index, index);
}

void redraw (int column, boolean drawText, boolean drawImage) {
	if (parent.currentItem == this || !parent.getDrawing ()) return;
	int /*long*/ hwnd = parent.handle;
	if (!OS.IsWindowVisible (hwnd)) return;
	int index = parent.indexOf (this);
	if (index == -1) return;
	RECT rect = getBounds (index, column, drawText, drawImage, true);
	OS.InvalidateRect (hwnd, rect, true);
}

void releaseHandle () {
	super.releaseHandle ();
	parent = null;
}

void releaseWidget () {
	super.releaseWidget ();
	strings = null;
	images = null;
	cellFont = null; 
	cellBackground = cellForeground = null;
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
	int pixel = -1;
	if (color != null) {
		parent.setCustomDraw (true);
		pixel = color.handle;
	}
	if (background == pixel) return;
	background = pixel;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
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
 */
public void setBackground (int index, Color color) {
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	int pixel = -1;
	if (color != null) {
		parent.setCustomDraw (true);
		pixel = color.handle;
	}
	if (cellBackground == null) {
		cellBackground = new int [count];
		for (int i = 0; i < count; i++) {
			cellBackground [i] = -1;
		}
	}
	if (cellBackground [index] == pixel) return;
	cellBackground [index] = pixel;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	redraw (index, true, true);
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
	setChecked (checked, false);
}

void setChecked (boolean checked, boolean notify) {
	this.checked = checked;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	if (notify) {
		Event event = new Event();
		event.item = this;
		event.detail = SWT.CHECK;
		parent.postEvent (SWT.Selection, event);
	}
	redraw ();
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
	Font oldFont = this.font;
	if (oldFont == font) return;
	this.font = font;
	if (oldFont != null && oldFont.equals (font)) return;
	if (font != null) parent.setCustomDraw (true);
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	/*
	* Bug in Windows.  Despite the fact that every item in the
	* table always has LPSTR_TEXTCALLBACK, Windows caches the
	* bounds for the selected items.  This means that 
	* when you change the string to be something else, Windows
	* correctly asks you for the new string but when the item
	* is selected, the selection draws using the bounds of the
	* previous item.  The fix is to reset LPSTR_TEXTCALLBACK
	* even though it has not changed, causing Windows to flush
	* cached bounds.
	*/
	if ((parent.style & SWT.VIRTUAL) == 0 && cached) {
		int itemIndex = parent.indexOf (this);
		if (itemIndex != -1) {
			int /*long*/ hwnd = parent.handle;
			LVITEM lvItem = new LVITEM ();
			lvItem.mask = OS.LVIF_TEXT;
			lvItem.iItem = itemIndex;
			lvItem.pszText = OS.LPSTR_TEXTCALLBACK;
			OS.SendMessage (hwnd, OS.LVM_SETITEM, 0, lvItem);
			cached = false;
		}
	}
	parent.setScrollWidth (this, false);
	redraw ();
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
	if (font != null) parent.setCustomDraw (true);
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	if (index == 0) {
		/*
		* Bug in Windows.  Despite the fact that every item in the
		* table always has LPSTR_TEXTCALLBACK, Windows caches the
		* bounds for the selected items.  This means that 
		* when you change the string to be something else, Windows
		* correctly asks you for the new string but when the item
		* is selected, the selection draws using the bounds of the
		* previous item.  The fix is to reset LPSTR_TEXTCALLBACK
		* even though it has not changed, causing Windows to flush
		* cached bounds.
		*/
		if ((parent.style & SWT.VIRTUAL) == 0 && cached) {
			int itemIndex = parent.indexOf (this);
			if (itemIndex != -1) {
				int /*long*/ hwnd = parent.handle;
				LVITEM lvItem = new LVITEM ();
				lvItem.mask = OS.LVIF_TEXT;
				lvItem.iItem = itemIndex;
				lvItem.pszText = OS.LPSTR_TEXTCALLBACK;
				OS.SendMessage (hwnd, OS.LVM_SETITEM, 0, lvItem);
				cached = false;
			}
		}
		parent.setScrollWidth (this, false);
	}	
	redraw (index, true, false);
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
	int pixel = -1;
	if (color != null) {
		parent.setCustomDraw (true);
		pixel = color.handle;
	}
	if (foreground == pixel) return;
	foreground = pixel;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
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
 */
public void setForeground (int index, Color color){
	checkWidget ();
	if (color != null && color.isDisposed ()) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	int pixel = -1;
	if (color != null) {
		parent.setCustomDraw (true);
		pixel = color.handle;
	}
	if (cellForeground == null) {
		cellForeground = new int [count];
		for (int i = 0; i < count; i++) {
			cellForeground [i] = -1;
		}
	}
	if (cellForeground [index] == pixel) return;
	cellForeground [index] = pixel;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	redraw (index, true, false);
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
	if (this.grayed == grayed) return;
	this.grayed = grayed;
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	redraw ();
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
	Image oldImage = null;
	if (index == 0) {
		if (image != null && image.type == SWT.ICON) {
			if (image.equals (this.image)) return;
		}
		oldImage = this.image;
		super.setImage (image);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	if (images == null && index != 0) {
		images = new Image [count];
		images [0] = image;
	}
	if (images != null) {
		if (image != null && image.type == SWT.ICON) {
			if (image.equals (images [index])) return;
		}
		oldImage = images [index];
		images [index] = image;
	}
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	
	/* Ensure that the image list is created */
	parent.imageIndex (image, index);
	
	if (index == 0) parent.setScrollWidth (this, false);
	boolean drawText = (image == null && oldImage != null) || (image != null && oldImage == null);
	redraw (index, drawText, true);
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
	if (imageIndent == indent) return;
	imageIndent = indent;
	if ((parent.style & SWT.VIRTUAL) != 0) {
		cached = true;
	} else {
		int index = parent.indexOf (this);
		if (index != -1) {
			int /*long*/ hwnd = parent.handle;
			LVITEM lvItem = new LVITEM ();
			lvItem.mask = OS.LVIF_INDENT;
			lvItem.iItem = index;
			lvItem.iIndent = indent;
			OS.SendMessage (hwnd, OS.LVM_SETITEM, 0, lvItem);
		}
	}
	parent.setScrollWidth (this, false);
	redraw ();
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
		super.setText (string);
	}
	int count = Math.max (1, parent.getColumnCount ());
	if (0 > index || index > count - 1) return;
	if (strings == null && index != 0)  {
		strings = new String [count];
		strings [0] = text;
	}
	if (strings != null) {
		if (string.equals (strings [index])) return;
		strings [index] = string;
	}
	if ((parent.style & SWT.VIRTUAL) != 0) cached = true;
	if (index == 0) {
		/*
		* Bug in Windows.  Despite the fact that every item in the
		* table always has LPSTR_TEXTCALLBACK, Windows caches the
		* bounds for the selected items.  This means that 
		* when you change the string to be something else, Windows
		* correctly asks you for the new string but when the item
		* is selected, the selection draws using the bounds of the
		* previous item.  The fix is to reset LPSTR_TEXTCALLBACK
		* even though it has not changed, causing Windows to flush
		* cached bounds.
		*/
		if ((parent.style & SWT.VIRTUAL) == 0 && cached) {
			int itemIndex = parent.indexOf (this);
			if (itemIndex != -1) {
				int /*long*/ hwnd = parent.handle;
				LVITEM lvItem = new LVITEM ();
				lvItem.mask = OS.LVIF_TEXT;
				lvItem.iItem = itemIndex;
				lvItem.pszText = OS.LPSTR_TEXTCALLBACK;
				OS.SendMessage (hwnd, OS.LVM_SETITEM, 0, lvItem);
				cached = false;
			}
		}
		parent.setScrollWidth (this, false);
	}
	redraw (index, true, false);
}

public void setText (String string) {
	checkWidget();
	setText (0, string);
}

}
