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

 
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class represent a column in a table widget.
 * <p><dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd> Move, Resize, Selection</dd>
 * </dl>
 * </p><p>
 * Note: Only one of the styles LEFT, RIGHT and CENTER may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#table">Table, TableItem, TableColumn snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class TableColumn extends Item {
	Table parent;
	boolean resizable, moveable;
	String toolTipText;
	int id;

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
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TableColumn (Table parent, int style) {
	super (parent, checkStyle (style));
	resizable = true;
	this.parent = parent;
	parent.createItem (this, parent.getColumnCount ());
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
 * <p>
 * Note that due to a restriction on some platforms, the first column
 * is always left aligned.
 * </p>
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
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TableColumn (Table parent, int style, int index) {
	super (parent, checkStyle (style));
	resizable = true;
	this.parent = parent;
	parent.createItem (this, index);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is moved or resized, by sending
 * it one of the messages defined in the <code>ControlListener</code>
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
 * @see ControlListener
 * @see #removeControlListener
 */
public void addControlListener(ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Resize,typedListener);
	addListener (SWT.Move,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the column header is selected.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified when the control is selected by the user
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
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>.
 *
 * @return the alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAlignment () {
	checkWidget ();
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

String getNameText () {
	return getText ();
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
 * Gets the moveable attribute. A column that is
 * not moveable cannot be reordered by the user 
 * by dragging the header but may be reordered 
 * by the programmer.
 *
 * @return the moveable attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Table#getColumnOrder()
 * @see Table#setColumnOrder(int[])
 * @see TableColumn#setMoveable(boolean)
 * @see SWT#Move
 * 
 * @since 3.1
 */
public boolean getMoveable () {
	checkWidget ();
	return moveable;
}

/**
 * Gets the resizable attribute. A column that is
 * not resizable cannot be dragged by the user but
 * may be resized by the programmer.
 *
 * @return the resizable attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getResizable () {
	checkWidget ();
	return resizable;
}

/**
 * Returns the receiver's tool tip text, or null if it has
 * not been set.
 *
 * @return the receiver's tool tip text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public String getToolTipText () {
	checkWidget();
	return toolTipText;
}

/**
 * Gets the width of the receiver.
 *
 * @return the width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getWidth () {
	checkWidget ();
	int index = parent.indexOf (this);
	if (index == -1) return 0;
	int /*long*/ hwnd = parent.handle;
	return (int)/*64*/OS.SendMessage (hwnd, OS.LVM_GETCOLUMNWIDTH, index, 0);
}

/**
 * Causes the receiver to be resized to its preferred size.
 * For a composite, this involves computing the preferred size
 * from its layout, if there is one.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 */
public void pack () {
	checkWidget ();
	int index = parent.indexOf (this);
	if (index == -1) return;
	int /*long*/ hwnd = parent.handle;
	int oldWidth = (int)/*64*/OS.SendMessage (hwnd, OS.LVM_GETCOLUMNWIDTH, index, 0);
	TCHAR buffer = new TCHAR (parent.getCodePage (), text, true);
	int headerWidth = (int)/*64*/OS.SendMessage (hwnd, OS.LVM_GETSTRINGWIDTH, 0, buffer) + Table.HEADER_MARGIN;
	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) headerWidth += Table.HEADER_EXTRA;
	boolean hasHeaderImage = false;
	if (image != null || parent.sortColumn == this) {
		hasHeaderImage = true;
		Image headerImage = null;
		if (parent.sortColumn == this && parent.sortDirection != SWT.NONE) {
			if (OS.COMCTL32_MAJOR < 6) {
				headerImage = display.getSortImage (parent.sortDirection);
			} else {
				headerWidth += Table.SORT_WIDTH;
			}
		} else {
			headerImage = image;
		}
		if (headerImage != null) {
			Rectangle bounds = headerImage.getBounds ();
			headerWidth += bounds.width;
		}
		int margin = 0;
		if (OS.COMCTL32_VERSION >= OS.VERSION (5, 80)) {
			int /*long*/ hwndHeader = OS.SendMessage (hwnd, OS.LVM_GETHEADER, 0, 0);
			margin = (int)/*64*/OS.SendMessage (hwndHeader, OS.HDM_GETBITMAPMARGIN, 0, 0);
		} else {
			margin = OS.GetSystemMetrics (OS.SM_CXEDGE) * 3;
		}
		headerWidth += margin * 4;
	}
	parent.ignoreColumnResize = true;
	int columnWidth = 0;
	if (parent.hooks (SWT.MeasureItem)) {
		RECT headerRect = new RECT ();
		int /*long*/ hwndHeader = OS.SendMessage (hwnd, OS.LVM_GETHEADER, 0, 0);
		OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, index, headerRect);
		OS.MapWindowPoints (hwndHeader, hwnd, headerRect, 2);
		int /*long*/ hDC = OS.GetDC (hwnd);
		int /*long*/ oldFont = 0, newFont = OS.SendMessage (hwnd, OS.WM_GETFONT, 0, 0);
		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
		int count = (int)/*64*/OS.SendMessage (hwnd, OS.LVM_GETITEMCOUNT, 0, 0);
		for (int i=0; i<count; i++) {
			TableItem item = parent.items [i];
			if (item != null) {
				int /*long*/ hFont = item.fontHandle (index);
				if (hFont != -1) hFont = OS.SelectObject (hDC, hFont);
				Event event = parent.sendMeasureItemEvent (item, i, index, hDC);
				if (hFont != -1) hFont = OS.SelectObject (hDC, hFont);
				if (isDisposed () || parent.isDisposed ()) break;
				columnWidth = Math.max (columnWidth, event.x + event.width - headerRect.left);
			}
		}
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (hwnd, hDC);
		OS.SendMessage (hwnd, OS.LVM_SETCOLUMNWIDTH, index, columnWidth);
	} else {
		OS.SendMessage (hwnd, OS.LVM_SETCOLUMNWIDTH, index, OS.LVSCW_AUTOSIZE);
		columnWidth = (int)/*64*/OS.SendMessage (hwnd, OS.LVM_GETCOLUMNWIDTH, index, 0);
		if (index == 0) {
			/*
			* Bug in Windows.  When LVM_SETCOLUMNWIDTH is used with LVSCW_AUTOSIZE
			* where each item has I_IMAGECALLBACK but there are no images in the
			* table, the size computed by LVM_SETCOLUMNWIDTH is too small for the
			* first column, causing long items to be clipped with '...'.  The fix
			* is to increase the column width by a small amount.
			*/
			if (parent.imageList == null) columnWidth += 2;
			/*
			* Bug in Windows.  When the first column of a table does not
			* have an image and the user double clicks on the divider,
			* Windows packs the column but does not take into account
			* the empty space left for the image.  The fix is to increase
			* the column width by the width of the image list.
			* 
			* NOTE:  This bug does not happen on Vista.
			*/
			if (!OS.IsWinCE && OS.WIN32_VERSION < OS.VERSION (6, 0)) {
				if (!parent.firstColumnImage) {
					int /*long*/ hImageList = OS.SendMessage (hwnd, OS.LVM_GETIMAGELIST, OS.LVSIL_SMALL, 0);
					if (hImageList != 0) {
						int [] cx = new int [1], cy = new int [1];
						OS.ImageList_GetIconSize (hImageList, cx, cy);
						columnWidth += cx [0];
					}
				}
			}
			/*
			* Bug in Windows.  When LVM_SETCOLUMNWIDTH is used with LVSCW_AUTOSIZE
			* for a table with a state image list, the column is width does not
			* include space for the state icon.  The fix is to increase the column
			* width by the width of the image list.
			*/
			if ((parent.style & SWT.CHECK) != 0) {
				int /*long*/ hStateList = OS.SendMessage (hwnd, OS.LVM_GETIMAGELIST, OS.LVSIL_STATE, 0);
				if (hStateList != 0) {
					int [] cx = new int [1], cy = new int [1];
					OS.ImageList_GetIconSize (hStateList, cx, cy);
					columnWidth += cx [0];
				}
			}
		}
	}
	if (headerWidth > columnWidth) {
		if (!hasHeaderImage) {
			/*
			* Feature in Windows.  When LVSCW_AUTOSIZE_USEHEADER is used
			* with LVM_SETCOLUMNWIDTH to resize the last column, the last
			* column is expanded to fill the client area.  The fix is to
			* resize the table to be small, set the column width and then
			* restore the table to its original size.
			*/
			RECT rect = null;
			boolean fixWidth = index == parent.getColumnCount () - 1;
			if (fixWidth) {
				rect = new RECT ();
				OS.GetWindowRect (hwnd, rect);
				OS.UpdateWindow (hwnd);
				int flags = OS.SWP_NOACTIVATE | OS.SWP_NOMOVE | OS.SWP_NOREDRAW | OS.SWP_NOZORDER;
				SetWindowPos (hwnd, 0, 0, 0, 0, rect.bottom - rect.top, flags);
			}
			OS.SendMessage (hwnd, OS.LVM_SETCOLUMNWIDTH, index, OS.LVSCW_AUTOSIZE_USEHEADER);
			if (fixWidth) {
				int flags = OS.SWP_NOACTIVATE | OS.SWP_NOMOVE | OS.SWP_NOZORDER;
				SetWindowPos (hwnd, 0, 0, 0, rect.right - rect.left, rect.bottom - rect.top, flags);
			}
		} else {
			OS.SendMessage (hwnd, OS.LVM_SETCOLUMNWIDTH, index, headerWidth);
		}
	} else {
		if (index == 0) {
			OS.SendMessage (hwnd, OS.LVM_SETCOLUMNWIDTH, index, columnWidth);
		}
	}
	parent.ignoreColumnResize = false;
	int newWidth = (int)/*64*/OS.SendMessage (hwnd, OS.LVM_GETCOLUMNWIDTH, index, 0);
	if (oldWidth != newWidth) {
		updateToolTip (index);
		sendEvent (SWT.Resize);
		if (isDisposed ()) return;
		boolean moved = false;
		int [] order = parent.getColumnOrder ();
		TableColumn [] columns = parent.getColumns ();
		for (int i=0; i<order.length; i++) {
			TableColumn column = columns [order [i]];
			if (moved && !column.isDisposed ()) {
				column.updateToolTip (order [i]);
				column.sendEvent (SWT.Move);
			}
			if (column == this) moved = true;
		}
	}
}

void releaseHandle () {
	super.releaseHandle ();
	parent = null;
}

void releaseParent () {
	super.releaseParent ();
	if (parent.sortColumn == this) {
		parent.sortColumn = null;
	}
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is moved or resized.
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
 * @see ControlListener
 * @see #addControlListener
 */
public void removeControlListener (ControlListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Move, listener);
	eventTable.unhook (SWT.Resize, listener);
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected by the user.
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
public void removeSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Controls how text and images will be displayed in the receiver.
 * The argument should be one of <code>LEFT</code>, <code>RIGHT</code>
 * or <code>CENTER</code>.
 * <p>
 * Note that due to a restriction on some platforms, the first column
 * is always left aligned.
 * </p>
 * @param alignment the new alignment 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlignment (int alignment) {
	checkWidget ();
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	int index = parent.indexOf (this);
	if (index == -1 || index == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	int /*long*/ hwnd = parent.handle;
	LVCOLUMN lvColumn = new LVCOLUMN ();
	lvColumn.mask = OS.LVCF_FMT;
	OS.SendMessage (hwnd, OS.LVM_GETCOLUMN, index, lvColumn);
	lvColumn.fmt &= ~OS.LVCFMT_JUSTIFYMASK;
	int fmt = 0;
	if ((style & SWT.LEFT) == SWT.LEFT) fmt = OS.LVCFMT_LEFT;
	if ((style & SWT.CENTER) == SWT.CENTER) fmt = OS.LVCFMT_CENTER;
	if ((style & SWT.RIGHT) == SWT.RIGHT) fmt = OS.LVCFMT_RIGHT;
	lvColumn.fmt |= fmt;
	OS.SendMessage (hwnd, OS.LVM_SETCOLUMN, index, lvColumn);
	/*
	* Bug in Windows.  When LVM_SETCOLUMN is used to change
	* the alignment of a column, the column is not redrawn
	* to show the new alignment.  The fix is to compute the
	* visible rectangle for the column and redraw it.
	*/
	if (index != 0) {
		parent.forceResize ();
		RECT rect = new RECT (), headerRect = new RECT ();
		OS.GetClientRect (hwnd, rect);
		int /*long*/ hwndHeader = OS.SendMessage (hwnd, OS.LVM_GETHEADER, 0, 0);
		OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, index, headerRect);
		OS.MapWindowPoints (hwndHeader, hwnd, headerRect, 2);
		rect.left = headerRect.left;
		rect.right = headerRect.right;
		OS.InvalidateRect (hwnd, rect, true);
	}
}

public void setImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	super.setImage (image);
	if (parent.sortColumn != this || parent.sortDirection != SWT.NONE) {
		setImage (image, false, false);
	}
}

void setImage (Image image, boolean sort, boolean right) {
	int index = parent.indexOf (this);
	if (index == -1) return;
	int /*long*/ hwnd = parent.handle;
	if (OS.COMCTL32_MAJOR < 6) {
		int /*long*/ hwndHeader = OS.SendMessage (hwnd, OS.LVM_GETHEADER, 0, 0);
		HDITEM hdItem = new HDITEM ();
		hdItem.mask = OS.HDI_FORMAT | OS.HDI_IMAGE | OS.HDI_BITMAP;
		OS.SendMessage (hwndHeader, OS.HDM_GETITEM, index, hdItem);
		hdItem.fmt &= ~OS.HDF_BITMAP_ON_RIGHT;
		if (image != null) {
			if (sort) {
				hdItem.mask &= ~OS.HDI_IMAGE;
				hdItem.fmt &= ~OS.HDF_IMAGE;
				hdItem.fmt |= OS.HDF_BITMAP;
				hdItem.hbm = image.handle;
			} else {
				hdItem.mask &= ~OS.HDI_BITMAP;
				hdItem.fmt &= ~OS.HDF_BITMAP;
				hdItem.fmt |= OS.HDF_IMAGE;
				hdItem.iImage = parent.imageIndexHeader (image);
			}
			if (right) hdItem.fmt |= OS.HDF_BITMAP_ON_RIGHT;
		} else {
			hdItem.fmt &= ~(OS.HDF_IMAGE | OS.HDF_BITMAP);
		}
		OS.SendMessage (hwndHeader, OS.HDM_SETITEM, index, hdItem);
	} else {
		LVCOLUMN lvColumn = new LVCOLUMN ();
		lvColumn.mask = OS.LVCF_FMT | OS.LVCF_IMAGE;
		OS.SendMessage (hwnd, OS.LVM_GETCOLUMN, index, lvColumn);
		if (image != null) {
			lvColumn.fmt |= OS.LVCFMT_IMAGE;
			lvColumn.iImage = parent.imageIndexHeader (image);
			if (right) lvColumn.fmt |= OS.LVCFMT_BITMAP_ON_RIGHT;
		} else {
			lvColumn.mask &= ~OS.LVCF_IMAGE;
			lvColumn.fmt &= ~(OS.LVCFMT_IMAGE | OS.LVCFMT_BITMAP_ON_RIGHT);
		}
		OS.SendMessage (hwnd, OS.LVM_SETCOLUMN, index, lvColumn);
	}
}

/**
 * Sets the moveable attribute.  A column that is
 * moveable can be reordered by the user by dragging
 * the header. A column that is not moveable cannot be 
 * dragged by the user but may be reordered 
 * by the programmer.
 *
 * @param moveable the moveable attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see Table#setColumnOrder(int[])
 * @see Table#getColumnOrder()
 * @see TableColumn#getMoveable()
 * @see SWT#Move
 * 
 * @since 3.1
 */
public void setMoveable (boolean moveable) {
	checkWidget ();
	this.moveable = moveable;
	parent.updateMoveable ();
}

/**
 * Sets the resizable attribute.  A column that is
 * resizable can be resized by the user dragging the
 * edge of the header.  A column that is not resizable 
 * cannot be dragged by the user but may be resized 
 * by the programmer.
 *
 * @param resizable the resize attribute
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setResizable (boolean resizable) {
	checkWidget ();
	this.resizable = resizable;
}

void setSortDirection (int direction) {
	if (OS.COMCTL32_MAJOR >= 6) {
		int index = parent.indexOf (this);
		if (index == -1) return;
		int /*long*/ hwnd = parent.handle;
		int /*long*/ hwndHeader = OS.SendMessage (hwnd, OS.LVM_GETHEADER, 0, 0);
		HDITEM hdItem = new HDITEM ();
		hdItem.mask = OS.HDI_FORMAT | OS.HDI_IMAGE;
		OS.SendMessage (hwndHeader, OS.HDM_GETITEM, index, hdItem);
		switch (direction) {
			case SWT.UP:
				hdItem.fmt &= ~(OS.HDF_IMAGE | OS.HDF_SORTDOWN);
				hdItem.fmt |= OS.HDF_SORTUP;
				if (image == null) hdItem.mask &= ~OS.HDI_IMAGE;
				break;
			case SWT.DOWN:
				hdItem.fmt &= ~(OS.HDF_IMAGE | OS.HDF_SORTUP);
				hdItem.fmt |= OS.HDF_SORTDOWN;
				if (image == null) hdItem.mask &= ~OS.HDI_IMAGE;
				break;
			case SWT.NONE:
				hdItem.fmt &= ~(OS.HDF_SORTUP | OS.HDF_SORTDOWN);
				if (image != null) {
					hdItem.fmt |= OS.HDF_IMAGE;
					hdItem.iImage = parent.imageIndexHeader (image);
				} else {
					hdItem.fmt &= ~OS.HDF_IMAGE;
					hdItem.mask &= ~OS.HDI_IMAGE;
				}
				break;
		}
		OS.SendMessage (hwndHeader, OS.HDM_SETITEM, index, hdItem);
		/* 
		* Bug in Windows.  When LVM_SETSELECTEDCOLUMN is used to
		* specify a selected column, Windows does not redraw either
		* the new or the previous selected column.  The fix is to
		* force a redraw of both.
		* 
		* Feature in Windows.  When LVM_SETBKCOLOR is used with
		* CLR_NONE and LVM_SETSELECTEDCOLUMN is used to select
		* a column, Windows fills the column with the selection
		* color, drawing on top of the background image and any
		* other custom drawing.  The fix is to avoid setting the
		* selected column.
		*/
		parent.forceResize ();
		RECT rect = new RECT ();
		OS.GetClientRect (hwnd, rect);
		if ((int)/*64*/OS.SendMessage (hwnd, OS.LVM_GETBKCOLOR, 0, 0) != OS.CLR_NONE) {
			int oldColumn = (int)/*64*/OS.SendMessage (hwnd, OS.LVM_GETSELECTEDCOLUMN, 0, 0);
			int newColumn = direction == SWT.NONE ? -1 : index;
			OS.SendMessage (hwnd, OS.LVM_SETSELECTEDCOLUMN, newColumn, 0);
			RECT headerRect = new RECT ();
			if (oldColumn != -1) {
				if (OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, oldColumn, headerRect) != 0) {
					OS.MapWindowPoints (hwndHeader, hwnd, headerRect, 2);
					rect.left = headerRect.left;
					rect.right = headerRect.right;
					OS.InvalidateRect (hwnd, rect, true);
				}
			}
		}
		RECT headerRect = new RECT ();
		if (OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, index, headerRect) != 0) {
			OS.MapWindowPoints (hwndHeader, hwnd, headerRect, 2);
			rect.left = headerRect.left;
			rect.right = headerRect.right;
			OS.InvalidateRect (hwnd, rect, true);
		}
	} else {
		switch (direction) {
			case SWT.UP:
			case SWT.DOWN:
				setImage (display.getSortImage (direction), true, true);
				break;
			case SWT.NONE:
				setImage (image, false, false);
				break;
		}
	}
}

public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (string.equals (text)) return;
	int index = parent.indexOf (this);
	if (index == -1) return;
	super.setText (string);

	/*
	* Bug in Windows.  For some reason, when the title
	* of a column is changed after the column has been
	* created, the alignment must also be reset or the
	* text does not draw.  The fix is to query and then
	* set the alignment.
	*/
	int /*long*/ hwnd = parent.handle;
	LVCOLUMN lvColumn = new LVCOLUMN ();
	lvColumn.mask = OS.LVCF_FMT;
	OS.SendMessage (hwnd, OS.LVM_GETCOLUMN, index, lvColumn);

	/*
	* Bug in Windows.  When a column header contains a
	* mnemonic character, Windows does not measure the
	* text properly.  This causes '...' to always appear
	* at the end of the text.  The fix is to remove
	* mnemonic characters and replace doubled mnemonics
	* with spaces.
	*/
	int /*long*/ hHeap = OS.GetProcessHeap ();
	TCHAR buffer = new TCHAR (parent.getCodePage (), fixMnemonic (string, true), true);
	int byteCount = buffer.length () * TCHAR.sizeof;
	int /*long*/ pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	OS.MoveMemory (pszText, buffer, byteCount);
	lvColumn.mask |= OS.LVCF_TEXT;
	lvColumn.pszText = pszText;
	int /*long*/ result = OS.SendMessage (hwnd, OS.LVM_SETCOLUMN, index, lvColumn);
	if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
	if (result == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
}

/**
 * Sets the receiver's tool tip text to the argument, which
 * may be null indicating that the default tool tip for the 
 * control will be shown. For a control that has a default
 * tool tip, such as the Tree control on Windows, setting
 * the tool tip text to an empty string replaces the default,
 * causing no tool tip text to be shown.
 *
 * @param string the new tool tip text (or null)
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setToolTipText (String string) {
	checkWidget();
	toolTipText = string;
	int /*long*/ hwndHeaderToolTip = parent.headerToolTipHandle;
	if (hwndHeaderToolTip == 0) {
		parent.createHeaderToolTips ();
		parent.updateHeaderToolTips ();
	}
}

/**
 * Sets the width of the receiver.
 *
 * @param width the new width
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWidth (int width) {
	checkWidget ();
	if (width < 0) return;
	int index = parent.indexOf (this);
	if (index == -1) return;
	int /*long*/ hwnd = parent.handle;
	if (width != (int)/*64*/OS.SendMessage (hwnd, OS.LVM_GETCOLUMNWIDTH, index, 0)) {
		OS.SendMessage (hwnd, OS.LVM_SETCOLUMNWIDTH, index, width);
	}
}

void updateToolTip (int index) {
	int /*long*/ hwndHeaderToolTip = parent.headerToolTipHandle;
	if (hwndHeaderToolTip != 0) {
		int /*long*/ hwnd = parent.handle;
		int /*long*/ hwndHeader = OS.SendMessage (hwnd, OS.LVM_GETHEADER, 0, 0);
		RECT rect = new RECT ();
		if (OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, index, rect) != 0) {
			TOOLINFO lpti = new TOOLINFO ();
			lpti.cbSize = TOOLINFO.sizeof;
			lpti.hwnd = hwndHeader;
			lpti.uId = id;
			lpti.left = rect.left;
			lpti.top = rect.top;
			lpti.right = rect.right;
			lpti.bottom = rect.bottom;
			OS.SendMessage (hwndHeaderToolTip, OS.TTM_NEWTOOLRECT, 0, lpti);
		}
	}
}

}
