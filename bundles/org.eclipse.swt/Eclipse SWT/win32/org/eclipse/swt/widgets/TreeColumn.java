/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
 * Instances of this class represent a column in a tree widget.
 *  <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd> Move, Resize, Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles LEFT, RIGHT and CENTER may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @since 3.1
 */
public class TreeColumn extends Item {
	Tree parent;
	boolean resizable;

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code>) and a style value
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
public TreeColumn (Tree parent, int style) {
	super (parent, checkStyle (style));
	resizable = true;
	this.parent = parent;
	parent.createItem (this, parent.getColumnCount ());
}

/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tree</code>), a style value
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
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TreeColumn (Tree parent, int style, int index) {
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
 * be notified when the control is selected, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the column header is selected.
 * <code>widgetDefaultSelected</code> is not called.
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
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
	return checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
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
	int hwndHeader = parent.hwndHeader;
	if (hwndHeader == 0) return 0;
	HDITEM hdItem = new HDITEM ();
	hdItem.mask = OS.HDI_WIDTH;
	OS.SendMessage (hwndHeader, OS.HDM_GETITEM, index, hdItem);
	return hdItem.cxy;
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
	int columnWidth = 0;
	int hwnd = parent.handle;
	int hDC = OS.GetDC (hwnd);
	int oldFont = 0, newFont = OS.SendMessage (hwnd, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	int cp = parent.getCodePage ();		
	RECT rect = new RECT ();
	int flags = OS.DT_CALCRECT | OS.DT_NOPREFIX;
	TVITEM tvItem = new TVITEM ();
	tvItem.mask = OS.TVIF_PARAM;
	int hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_ROOT, 0);
	while (hItem != 0) {
		hItem = OS.SendMessage (hwnd, OS.TVM_GETNEXTITEM, OS.TVGN_NEXTVISIBLE, hItem);
		tvItem.hItem = hItem;
		OS.SendMessage (hwnd, OS.TVM_GETITEM, 0, tvItem);
		TreeItem item = parent.items [tvItem.lParam];
		if (index == 0) {
			rect.left = item.handle;
			if (OS.SendMessage (hwnd, OS.TVM_GETITEMRECT, 1, rect) != 0) {
				columnWidth = Math.max (columnWidth, rect.right);
			}
		} else {
			int imageWidth = 0, textWidth = 0;
			Image image = item.images != null ? item.images [index] : null;
			if (image != null) {
				Rectangle bounds = image.getBounds ();
				imageWidth = bounds.width;
			}
			String string = item.strings != null ? item.strings [index] : null;
			if (string != null) {
				TCHAR buffer = new TCHAR (cp, string, false);
				OS.DrawText (hDC, buffer, buffer.length (), rect, flags);
				textWidth = rect.right - rect.left;
			}
			columnWidth = Math.max (columnWidth, imageWidth + textWidth + Tree.INSET * 3);
		}
	}
	TCHAR buffer = new TCHAR (cp, text, true);
	OS.DrawText (hDC, buffer, buffer.length (), rect, flags);
	int headerWidth = rect.right - rect.left + Tree.HEADER_MARGIN;
	if (image != null) {
		int margin = 0, hwndHeader = parent.hwndHeader;
		if (hwndHeader != 0 && OS.COMCTL32_VERSION >= OS.VERSION (5, 80)) {
			margin = OS.SendMessage (hwndHeader, OS.HDM_GETBITMAPMARGIN, 0, 0);
		} else {
			margin = OS.GetSystemMetrics (OS.SM_CXEDGE) * 3;
		}
		Rectangle bounds = image.getBounds ();
		headerWidth += bounds.width + margin * 2;
	}
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (hwnd, hDC);
	setWidth (Math.max (headerWidth, columnWidth));
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
 * be notified when the control is selected.
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
 *
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
	int hwndHeader = parent.hwndHeader;
	if (hwndHeader == 0) return;
	HDITEM hdItem = new HDITEM ();
	hdItem.mask = OS.HDI_FORMAT | OS.HDI_IMAGE;
	OS.SendMessage (hwndHeader, OS.HDM_GETITEM, index, hdItem);
	hdItem.fmt &= ~OS.HDF_JUSTIFYMASK;
	if ((style & SWT.LEFT) == SWT.LEFT) hdItem.fmt |= OS.HDF_LEFT;
	if ((style & SWT.CENTER) == SWT.CENTER) hdItem.fmt |= OS.HDF_CENTER;
	if ((style & SWT.RIGHT) == SWT.RIGHT) hdItem.fmt |= OS.HDF_RIGHT;
	OS.SendMessage (hwndHeader, OS.HDM_SETITEM, index, hdItem);
	if (index != 0) {
		int hwnd = parent.handle;
		RECT rect = new RECT (), itemRect = new RECT ();
		OS.GetClientRect (hwnd, rect);
		OS.SendMessage (hwndHeader, OS.HDM_GETITEMRECT, index, itemRect);
		rect.left = itemRect.left;
		rect.right = itemRect.right;
		OS.InvalidateRect (hwnd, rect, true);
	}
}

public void setImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	int index = parent.indexOf (this);
	if (index == -1) return;
	super.setImage (image);
	int hwndHeader = parent.hwndHeader;
	if (hwndHeader == 0) return;
	HDITEM hdItem = new HDITEM ();
	hdItem.mask = OS.HDI_FORMAT | OS.HDI_IMAGE;
	OS.SendMessage (hwndHeader, OS.HDM_GETITEM, index, hdItem);
	if (image != null) {
		hdItem.fmt |= OS.LVCFMT_IMAGE;
		hdItem.iImage = parent.imageIndex (image);
	} else {
		hdItem.fmt &= ~OS.LVCFMT_IMAGE;
	}
	OS.SendMessage (hwndHeader, OS.HDM_SETITEM, index, hdItem);
}

/**
 * Sets the resizable attribute.  A column that is
 * not resizable cannot be dragged by the user but
 * may be resized by the programmer.
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

public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int index = parent.indexOf (this);
	if (index == -1) return;
	super.setText (string);
	/*
	* Bug in Windows.  When a column header contains a
	* mnemonic character, Windows does not measure the
	* text properly.  This causes '...' to always appear
	* at the end of the text.  The fix is to remove
	* mnemonic characters and replace doubled mnemonics
	* with spaces.
	*/
	int hHeap = OS.GetProcessHeap ();
	TCHAR buffer = new TCHAR (parent.getCodePage (), fixMnemonic (string), true);
	int byteCount = buffer.length () * TCHAR.sizeof;
	int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	OS.MoveMemory (pszText, buffer, byteCount);
	int hwndHeader = parent.hwndHeader;
	if (hwndHeader == 0) return;
	HDITEM hdItem = new HDITEM ();
	hdItem.mask = OS.HDI_TEXT;
	hdItem.pszText = pszText;
	int result = OS.SendMessage (hwndHeader, OS.HDM_SETITEM, index, hdItem);
	if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
	if (result == 0) error (SWT.ERROR_CANNOT_SET_TEXT);
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
	int index = parent.indexOf (this);
	if (index == -1) return;
	int hwndHeader = parent.hwndHeader;
	if (hwndHeader == 0) return;
	HDITEM hdItem = new HDITEM ();
	hdItem.mask = OS.HDI_WIDTH;
	hdItem.cxy = width;
	OS.SendMessage (hwndHeader, OS.HDM_SETITEM, index, hdItem);
	parent.setScrollWidth ();
}

}
