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

 
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class implement the notebook user interface
 * metaphor.  It allows the user to select a notebook page from
 * set of pages.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TabItem</code>.
 * <code>Control</code> children are created and then set into a
 * tab item using <code>TabItem#setControl</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>TOP, BOTTOM</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles TOP and BOTTOM may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tabfolder">TabFolder, TabItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TabFolder extends Composite {
	TabItem [] items;
	ImageList imageList;
	static final int /*long*/ TabFolderProc;
	static final TCHAR TabFolderClass = new TCHAR (0, OS.WC_TABCONTROL, true);
	boolean createdAsRTL;
	
	/*
	* These are the undocumented control id's for the children of
	* a tab control.  Since there are no constants for these values,
	* they may change with different versions of Windows.
	*/
	static final int ID_UPDOWN = 1;
	
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, TabFolderClass, lpWndClass);
		TabFolderProc = lpWndClass.lpfnWndProc;
		/*
		* Feature in Windows.  The tab control window class
		* uses the CS_HREDRAW and CS_VREDRAW style bits to
		* force a full redraw of the control and all children
		* when resized.  This causes flashing.  The fix is to
		* register a new window class without these bits and
		* implement special code that damages only the exposed
		* area.
		* 
		* NOTE:  Screen readers look for the exact class name
		* of the control in order to provide the correct kind
		* of assistance.  Therefore, it is critical that the
		* new window class have the same name.  It is possible
		* to register a local window class with the same name
		* as a global class.  Since bits that affect the class
		* are being changed, it is possible that other native
		* code, other than SWT, could create a control with
		* this class name, and fail unexpectedly.
		*/
		int /*long*/ hInstance = OS.GetModuleHandle (null);
		int /*long*/ hHeap = OS.GetProcessHeap ();
		lpWndClass.hInstance = hInstance;
		lpWndClass.style &= ~(OS.CS_HREDRAW | OS.CS_VREDRAW | OS.CS_GLOBALCLASS);
		int byteCount = TabFolderClass.length () * TCHAR.sizeof;
		int /*long*/ lpszClassName = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		OS.MoveMemory (lpszClassName, TabFolderClass, byteCount);
		lpWndClass.lpszClassName = lpszClassName;
		OS.RegisterClass (lpWndClass);
		OS.HeapFree (hHeap, 0, lpszClassName);
	}

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
 * @see SWT
 * @see SWT#TOP
 * @see SWT#BOTTOM
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TabFolder (Composite parent, int style) {
	super (parent, checkStyle (style));
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the user changes the receiver's selection, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * <code>widgetDefaultSelected</code> is not called.
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
public void addSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

int /*long*/ callWindowProc (int /*long*/ hwnd, int msg, int /*long*/ wParam, int /*long*/ lParam) {
	if (handle == 0) return 0;
	return OS.CallWindowProc (TabFolderProc, hwnd, msg, wParam, lParam);
}

static int checkStyle (int style) {
	/*
	* When the SWT.TOP style has not been set, force the
	* tabs to be on the bottom for tab folders on PPC.
	*/
	if (OS.IsPPC) {
		if ((style & SWT.TOP) == 0) style |= SWT.BOTTOM;
	}
	style = checkBits (style, SWT.TOP, SWT.BOTTOM, 0, 0, 0, 0);
	
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	Point size = super.computeSize (wHint, hHint, changed);
	RECT insetRect = new RECT (), itemRect = new RECT ();
	OS.SendMessage (handle, OS.TCM_ADJUSTRECT, 0, insetRect);
	int width = insetRect.left - insetRect.right;
	int count = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETITEMCOUNT, 0, 0);
	if (count != 0) {
		OS.SendMessage (handle, OS.TCM_GETITEMRECT, count - 1, itemRect);
		width = Math.max (width, itemRect.right - insetRect.right);
	}
	RECT rect = new RECT ();
	OS.SetRect (rect, 0, 0, width, size.y);
	OS.SendMessage (handle, OS.TCM_ADJUSTRECT, 1, rect);
	int border = getBorderWidth ();
	rect.left -= border;  rect.right += border;
	width = rect.right - rect.left;
	size.x = Math.max (width, size.x);
	return size;
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	RECT rect = new RECT ();
	OS.SetRect (rect, x, y, x + width, y + height);
	OS.SendMessage (handle, OS.TCM_ADJUSTRECT, 1, rect);
	int border = getBorderWidth ();
	rect.left -= border;  rect.right += border;
	rect.top -= border;  rect.bottom += border;
	int newWidth = rect.right - rect.left;
	int newHeight = rect.bottom - rect.top;
	return new Rectangle (rect.left, rect.top, newWidth, newHeight);
}

void createItem (TabItem item, int index) {
	int count = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETITEMCOUNT, 0, 0);
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	if (count == items.length) {
		TabItem [] newItems = new TabItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	TCITEM tcItem = new TCITEM ();
	if (OS.SendMessage (handle, OS.TCM_INSERTITEM, index, tcItem) == -1) {
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	System.arraycopy (items, index, items, index + 1, count - index);
	items [index] = item;
	
	/*
	* Send a selection event when the item that is added becomes
	* the new selection.  This only happens when the first item
	* is added.
	*/
	if (count == 0) {
		Event event = new Event ();
		event.item = items [0];
		sendSelectionEvent (SWT.Selection, event, true);
		// the widget could be destroyed at this point
	}
}

void createHandle () {
	super.createHandle ();
	state &= ~(CANVAS | THEME_BACKGROUND);
	
	/* Enable the flat look for tab folders on PPC */
	if (OS.IsPPC) {
		OS.SendMessage (handle, OS.CCM_SETVERSION, 0x020c /*COMCTL32_VERSION*/, 0);
	}

	/*
	* Feature in Windows.  Despite the fact that the
	* tool tip text contains \r\n, the tooltip will
	* not honour the new line unless TTM_SETMAXTIPWIDTH
	* is set.  The fix is to set TTM_SETMAXTIPWIDTH to
	* a large value.
	*/
	int /*long*/ hwndToolTip = OS.SendMessage (handle, OS.TCM_GETTOOLTIPS, 0, 0);
	OS.SendMessage (hwndToolTip, OS.TTM_SETMAXTIPWIDTH, 0, 0x7FFF);	
	
	createdAsRTL = (style & SWT.RIGHT_TO_LEFT) != 0;
}

void createWidget () {
	super.createWidget ();
	items = new TabItem [4];
}

void destroyItem (TabItem item) {
	int count = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETITEMCOUNT, 0, 0);
	int index = 0;
	while (index < count) {
		if (items [index] == item) break;
		index++;
	}
	if (index == count) return;
	int selectionIndex = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETCURSEL, 0, 0);
	if (OS.SendMessage (handle, OS.TCM_DELETEITEM, index, 0) == 0) {
		error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	System.arraycopy (items, index + 1, items, index, --count - index);
	items [count] = null;
	if (count == 0) {
		if (imageList != null) {
			OS.SendMessage (handle, OS.TCM_SETIMAGELIST, 0, 0);
			display.releaseImageList (imageList);
		}
		imageList = null;
		items = new TabItem [4];
	}
	if (count > 0 && index == selectionIndex) {
		setSelection (Math.max (0, selectionIndex - 1), true);
	}
}

void drawThemeBackground (int /*long*/ hDC, int /*long*/ hwnd, RECT rect) {
	RECT rect2 = new RECT ();
	OS.GetClientRect (handle, rect2);
	OS.MapWindowPoints (handle, hwnd, rect2, 2);
	if (OS.IntersectRect (new RECT (), rect2, rect)) {
		OS.DrawThemeBackground (display.hTabTheme (), hDC, OS.TABP_BODY, 0, rect2, null);
	}
}

Control findThemeControl () {
	/* It is not possible to change the background of this control */
	return this;	
}

public Rectangle getClientArea () {
	checkWidget ();
	forceResize ();
	RECT rect = new RECT ();
	OS.GetClientRect (handle, rect);
	OS.SendMessage (handle, OS.TCM_ADJUSTRECT, 0, rect);
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	return new Rectangle (rect.left, rect.top, width, height);
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
public TabItem getItem (int index) {
	checkWidget ();
	int count = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETITEMCOUNT, 0, 0);
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
	return items [index];
}

/**
 * Returns the tab item at the given point in the receiver
 * or null if no such item exists. The point is in the
 * coordinate system of the receiver.
 *
 * @param point the point used to locate the item
 * @return the tab item at the given point, or null if the point is not in a tab item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.4
 */
public TabItem getItem (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	TCHITTESTINFO pinfo = new TCHITTESTINFO ();
	pinfo.x = point.x;
	pinfo.y = point.y;
	int index = (int)/*64*/OS.SendMessage (handle, OS.TCM_HITTEST, 0, pinfo);
	if (index == -1) return null;
	return items [index];
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
	return (int)/*64*/OS.SendMessage (handle, OS.TCM_GETITEMCOUNT, 0, 0);
}

/**
 * Returns an array of <code>TabItem</code>s which are the items
 * in the receiver. 
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
public TabItem [] getItems () {
	checkWidget ();
	int count = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETITEMCOUNT, 0, 0);
	TabItem [] result = new TabItem [count];
	System.arraycopy (items, 0, result, 0, count);
	return result;
}

/**
 * Returns an array of <code>TabItem</code>s that are currently
 * selected in the receiver. An empty array indicates that no
 * items are selected.
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
public TabItem [] getSelection () {
	checkWidget ();
	int index = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETCURSEL, 0, 0);
	if (index == -1) return new TabItem [0];
	return new TabItem [] {items [index]};
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
	return (int)/*64*/OS.SendMessage (handle, OS.TCM_GETCURSEL, 0, 0);
}

int imageIndex (Image image) {
	if (image == null) return OS.I_IMAGENONE;
	if (imageList == null) {
		Rectangle bounds = image.getBounds ();
		imageList = display.getImageList (style & SWT.RIGHT_TO_LEFT, bounds.width, bounds.height);
		int index = imageList.add (image);
		int /*long*/ hImageList = imageList.getHandle ();
		OS.SendMessage (handle, OS.TCM_SETIMAGELIST, 0, hImageList);
		return index;
	}
	int index = imageList.indexOf (image);
	if (index == -1) {
		index = imageList.add (image);
	} else {
		imageList.put (index, image);
	}
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
public int indexOf (TabItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETITEMCOUNT, 0, 0);
	for (int i=0; i<count; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

Point minimumSize (int wHint, int hHint, boolean flushCache) {
	Control [] children = _getChildren ();
	int width = 0, height = 0;
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		int index = 0;	
		int count = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETITEMCOUNT, 0, 0);
		while (index < count) {
			if (items [index].control == child) break;
			index++;
		}
		if (index == count) {
			Rectangle rect = child.getBounds ();
			width = Math.max (width, rect.x + rect.width);
			height = Math.max (height, rect.y + rect.height);
		} else {
			Point size = child.computeSize (wHint, hHint, flushCache);
			width = Math.max (width, size.x);
			height = Math.max (height, size.y);
		}
	}
	return new Point (width, height);
}

boolean mnemonicHit (char key) {
	for (int i=0; i<items.length; i++) {
		TabItem item = items [i];
		if (item != null) {
			char ch = findMnemonic (item.getText ());
			if (Character.toUpperCase (key) == Character.toUpperCase (ch)) {
				if (forceFocus ()) {
					if (i != getSelectionIndex ()) setSelection (i, true);
					return true;
				}
			}
		}
	}
	return false;
}

boolean mnemonicMatch (char key) {
	for (int i=0; i<items.length; i++) {
		TabItem item = items [i];
		if (item != null) {
			char ch = findMnemonic (item.getText ());
			if (Character.toUpperCase (key) == Character.toUpperCase (ch)) {		
				return true;
			}
		}
	}
	return false;
}

void releaseChildren (boolean destroy) {
	if (items != null) {
		int count = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETITEMCOUNT, 0, 0);
		for (int i=0; i<count; i++) {
			TabItem item = items [i];
			if (item != null && !item.isDisposed ()) {
				item.release (false);
			}
		}
		items = null;
	}
	super.releaseChildren (destroy);
}

void releaseWidget () {
	super.releaseWidget ();
	if (imageList != null) {
		OS.SendMessage (handle, OS.TCM_SETIMAGELIST, 0, 0);
		display.releaseImageList (imageList);
	}
	imageList = null;
}

void removeControl (Control control) {
	super.removeControl (control);
	int count = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETITEMCOUNT, 0, 0);
	for (int i=0; i<count; i++) {
		TabItem item = items [i];
		if (item.control == control) item.setControl (null);
	}
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
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}


void reskinChildren (int flags) {
	if (items != null) {
		int count = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETITEMCOUNT, 0, 0);
		for (int i=0; i<count; i++) {
			TabItem item = items [i];
			if (item != null) item.reskin (flags);
		}
	}
	super.reskinChildren (flags);
}

/**
 * Sets the receiver's selection to the given item.
 * The current selected is first cleared, then the new item is
 * selected.
 *
 * @param item the item to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.2
 */
public void setSelection (TabItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	setSelection (new TabItem [] {item});
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selected is first cleared, then the new items are
 * selected.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (TabItem [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (items.length == 0) {
		setSelection (-1, false);
	} else {
		for (int i=items.length-1; i>=0; --i) {
			int index = indexOf (items [i]);
			if (index != -1) setSelection (index, false);
		}
	}
}

public void setFont (Font font) {
	checkWidget ();
	Rectangle oldRect = getClientArea ();
	super.setFont (font);
	Rectangle newRect = getClientArea ();
	if (!oldRect.equals (newRect)) {
		sendResize ();
		int index = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETCURSEL, 0, 0);
		if (index != -1) {
			TabItem item = items [index];
			Control control = item.control;
			if (control != null && !control.isDisposed ()) {
				control.setBounds (getClientArea ());
			}
		}
	}
}

/**
 * Selects the item at the given zero-relative index in the receiver. 
 * If the item at the index was already selected, it remains selected.
 * The current selection is first cleared, then the new items are
 * selected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int index) {
	checkWidget ();
	int count = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETITEMCOUNT, 0, 0);
	if (!(0 <= index && index < count)) return;
	setSelection (index, false);
}

void setSelection (int index, boolean notify) {
	int oldIndex = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETCURSEL, 0, 0);
	if (oldIndex == index) return;
	if (oldIndex != -1) {
		TabItem item = items [oldIndex];
		Control control = item.control;
		if (control != null && !control.isDisposed ()) {
			control.setVisible (false);
		}
	}
	OS.SendMessage (handle, OS.TCM_SETCURSEL, index, 0);
	int newIndex = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETCURSEL, 0, 0);
	if (newIndex != -1) {
		TabItem item = items [newIndex];
		Control control = item.control;
		if (control != null && !control.isDisposed ()) {
			control.setBounds (getClientArea ());
			control.setVisible (true);
		}
		if (notify) {
			Event event = new Event ();
			event.item = item;
			sendSelectionEvent (SWT.Selection, event, true);
		}
	}
}

String toolTipText (NMTTDISPINFO hdr) {
	if ((hdr.uFlags & OS.TTF_IDISHWND) != 0) {
		return null;
	}
	int index = (int)/*64*/hdr.idFrom;
	int /*long*/ hwndToolTip = OS.SendMessage (handle, OS.TCM_GETTOOLTIPS, 0, 0);
	if (hwndToolTip == hdr.hwndFrom) {
		/*
		* Bug in Windows. For some reason the reading order
		* in NMTTDISPINFO is sometimes set incorrectly.  The
		* reading order seems to change every time the mouse
		* enters the control from the top edge.  The fix is
		* to explicitly set TTF_RTLREADING.
		*/
		if ((style & SWT.RIGHT_TO_LEFT) != 0) {
			hdr.uFlags |= OS.TTF_RTLREADING;
		} else {
			hdr.uFlags &= ~OS.TTF_RTLREADING;
		}
		if (toolTipText != null) return "";
		if (0 <= index && index < items.length) {
			TabItem item = items [index];
			if (item != null) return item.toolTipText;
		}
	}
	return super.toolTipText (hdr);
}

boolean traversePage (boolean next) {
	int count = getItemCount ();
	if (count <= 1) return false;
	int index = getSelectionIndex ();
	if (index == -1) {
		index = 0;
	} else {
		int offset = (next) ? 1 : -1;
		index = (index + offset + count) % count;
	}
	setSelection (index, true);
	if (index == getSelectionIndex ()) {
		OS.SendMessage (handle, OS.WM_CHANGEUISTATE, OS.UIS_INITIALIZE, 0);
		return true;
	}
	return false;
}

void updateOrientation () {
	super.updateOrientation ();
	int /*long*/ hwndChild = OS.GetWindow (handle, OS.GW_CHILD);
	while (hwndChild != 0) {
		TCHAR buffer = new TCHAR (0, 128);
		OS.GetClassName (hwndChild, buffer, buffer.length ());
		String className = buffer.toString (0, buffer.strlen ());
		if (className.equals ("msctls_updown32")) { //$NON-NLS-1$
			int bits = OS.GetWindowLong (hwndChild, OS.GWL_EXSTYLE);
			if ((style & SWT.RIGHT_TO_LEFT) != 0) {
				bits |= OS.WS_EX_LAYOUTRTL;
			} else {
				bits &= ~OS.WS_EX_LAYOUTRTL;
			}
			OS.SetWindowLong (hwndChild, OS.GWL_EXSTYLE, bits);
			OS.InvalidateRect (hwndChild, null, true);
			break;
		}
		hwndChild = OS.GetWindow (hwndChild, OS.GW_HWNDNEXT);
	}
	RECT rect = new RECT ();
	OS.GetWindowRect (handle, rect);
	int width = rect.right - rect.left, height = rect.bottom - rect.top;
	OS.SetWindowPos (handle, 0, 0, 0, width - 1, height - 1, OS.SWP_NOMOVE | OS.SWP_NOZORDER);
	OS.SetWindowPos (handle, 0, 0, 0, width, height, OS.SWP_NOMOVE | OS.SWP_NOZORDER);
	if (imageList != null) {
		Point size = imageList.getImageSize ();
		display.releaseImageList (imageList);
		imageList = display.getImageList (style & SWT.RIGHT_TO_LEFT, size.x, size.y);
		int /*long*/ hImageList = imageList.getHandle ();
		OS.SendMessage (handle, OS.TCM_SETIMAGELIST, 0, hImageList);
		TCITEM tcItem = new TCITEM ();
		tcItem.mask = OS.TCIF_IMAGE;
		for (int i = 0; i < items.length; i++) {
			TabItem item = items[0];
			if (item == null) break;
			Image image = item.image;
			if (image != null) {
				tcItem.iImage = imageIndex (image);
				OS.SendMessage (handle, OS.TCM_SETITEM, i, tcItem);
			}
		}
	}
}

int widgetStyle () {
	/*
	* Bug in Windows.  Under certain circumstances,
	* when TCM_SETITEM is used to change the text
	* in a tab item, the tab folder draws on top
	* of the client area.  The fix is ensure that
	* this cannot happen by setting WS_CLIPCHILDREN.
	*/
	int bits = super.widgetStyle () | OS.WS_CLIPCHILDREN;
	if ((style & SWT.NO_FOCUS) != 0) bits |= OS.TCS_FOCUSNEVER;
	if ((style & SWT.BOTTOM) != 0) bits |= OS.TCS_BOTTOM;
	return bits | OS.TCS_TABS | OS.TCS_TOOLTIPS;
}

TCHAR windowClass () {
	return TabFolderClass;
}

int /*long*/ windowProc () {
	return TabFolderProc;
}

LRESULT WM_GETDLGCODE (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_GETDLGCODE (wParam, lParam);
	/*
	* Return DLGC_BUTTON so that mnemonics will be
	* processed without needing to press the ALT key
	* when the widget has focus.
	*/
	if (result != null) return result;
	return new LRESULT (OS.DLGC_BUTTON | OS.DLGC_WANTARROWS);
}

LRESULT WM_KEYDOWN (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_KEYDOWN (wParam, lParam);
	if (result != null) return result;
	switch ((int)/*64*/wParam) {
		case OS.VK_LEFT:
		case OS.VK_RIGHT:
			/* 
			* Bug in Windows. The behavior for the left and right keys is not
			* changed if the orientation changes after the control was created.
			* The fix is to replace VK_LEFT by VK_RIGHT and VK_RIGHT by VK_LEFT
			* when the current orientation differs from the orientation used to 
			* create the control.
		    */
			boolean isRTL = (style & SWT.RIGHT_TO_LEFT) != 0;
			if (isRTL != createdAsRTL) {
				int /*long*/ code = callWindowProc (handle, OS.WM_KEYDOWN, wParam == OS.VK_RIGHT ? OS.VK_LEFT : OS.VK_RIGHT, lParam);
				return new LRESULT (code);
			}
			break;
	}
	return result;
}

LRESULT WM_MOUSELEAVE (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_MOUSELEAVE (wParam, lParam);
	if (result != null) return result;
	/*
	* Bug in Windows.  On XP, when a tooltip is
	* hidden due to a time out or mouse press,
	* the tooltip remains active although no
	* longer visible and won't show again until
	* another tooltip becomes active.  If there
	* is only one tooltip in the window,  it will
	* never show again.  The fix is to remove the
	* current tooltip and add it again every time
	* the mouse leaves the control.
	*/
	if (OS.COMCTL32_MAJOR >= 6) {
		TOOLINFO lpti = new TOOLINFO ();
		lpti.cbSize = TOOLINFO.sizeof;
		int /*long*/ hwndToolTip = OS.SendMessage (handle, OS.TCM_GETTOOLTIPS, 0, 0);
		if (OS.SendMessage (hwndToolTip, OS.TTM_GETCURRENTTOOL, 0, lpti) != 0) {
			if ((lpti.uFlags & OS.TTF_IDISHWND) == 0) {
				OS.SendMessage (hwndToolTip, OS.TTM_DELTOOL, 0, lpti);
				OS.SendMessage (hwndToolTip, OS.TTM_ADDTOOL, 0, lpti);
			}
		}
	}
	return result;
}

LRESULT WM_NCHITTEST (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_NCHITTEST (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  The tab control implements
	* WM_NCHITTEST to return HTCLIENT when the cursor
	* is inside the tab buttons.  This causes mouse
	* events like WM_MOUSEMOVE to be delivered to the
	* parent.  Also, tool tips for the tab control are
	* never invoked because tool tips rely on mouse
	* events to be delivered to the window that wants
	* to display the tool tip.  The fix is to call the
	* default window proc that returns HTCLIENT when
	* the mouse is in the client area.	
	*/
	int /*long*/ hittest = OS.DefWindowProc (handle, OS.WM_NCHITTEST, wParam, lParam);
	return new LRESULT (hittest);
}

LRESULT WM_NOTIFY (int /*long*/ wParam, int /*long*/ lParam) {
	/*
	* Feature in Windows.  When the tab folder window
	* proc processes WM_NOTIFY, it forwards this
	* message to its parent.  This is done so that
	* children of this control that send this message 
	* type to their parent will notify not only
	* this control but also the parent of this control,
	* which is typically the application window and
	* the window that is looking for the message.
	* If the control did not forward the message, 
	* applications would have to subclass the control 
	* window to see the message. Because the control
	* window is subclassed by SWT, the message
	* is delivered twice, once by SWT and once when
	* the message is forwarded by the window proc.
	* The fix is to avoid calling the window proc 
	* for this control.
	*/
	LRESULT result = super.WM_NOTIFY (wParam, lParam);
	if (result != null) return result;
	return LRESULT.ZERO;
}

LRESULT WM_PARENTNOTIFY (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_PARENTNOTIFY (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  Windows does not explicitly set the orientation of
	* the buddy control.  Instead, the orientation is inherited when WS_EX_LAYOUTRTL
	* is specified for the tab folder.  This means that when both WS_EX_LAYOUTRTL
	* and WS_EX_NOINHERITLAYOUT are specified for the tab folder, the buddy control
	* will not be oriented correctly.  The fix is to explicitly set the orientation
	* for the buddy control.
	* 
	* NOTE: WS_EX_LAYOUTRTL is not supported on Windows NT.
	*/
	if (OS.WIN32_VERSION < OS.VERSION (4, 10)) return result;
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		int code = OS.LOWORD (wParam);
		switch (code) {
			case OS.WM_CREATE: {
				int id = OS.HIWORD (wParam);
 				int /*long*/ hwnd = lParam;
				if (id == ID_UPDOWN) {
					int bits = OS.GetWindowLong (hwnd, OS.GWL_EXSTYLE);
					OS.SetWindowLong (hwnd, OS.GWL_EXSTYLE,	bits | OS.WS_EX_LAYOUTRTL);
				}
				break;
			}
		}
	}
	return result;
}

LRESULT WM_SIZE (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_SIZE (wParam, lParam);
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the resize
	* event.  If this happens, end the processing of the
	* Windows message by returning the result of the
	* WM_SIZE message.
	*/
	if (isDisposed ()) return result;
	int index = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETCURSEL, 0, 0);
	if (index != -1) {
		TabItem item = items [index];
		Control control = item.control;
		if (control != null && !control.isDisposed ()) {
			control.setBounds (getClientArea ());
		}
	}
	return result;
}

LRESULT WM_WINDOWPOSCHANGING (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_WINDOWPOSCHANGING (wParam, lParam);
	if (result != null) return result;
	if (!OS.IsWindowVisible (handle)) return result;
	WINDOWPOS lpwp = new WINDOWPOS ();
	OS.MoveMemory (lpwp, lParam, WINDOWPOS.sizeof);
	if ((lpwp.flags & (OS.SWP_NOSIZE | OS.SWP_NOREDRAW)) != 0) {
		return result;
	}
	// TEMPORARY CODE
//	if (OS.COMCTL32_MAJOR >= 6 && OS.IsAppThemed ()) {
//		OS.InvalidateRect (handle, null, true);
//		return result;
//	}
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.TCS_MULTILINE) != 0) {
		OS.InvalidateRect (handle, null, true);
		return result;
	}
	RECT rect = new RECT ();
	OS.SetRect (rect, 0, 0, lpwp.cx, lpwp.cy);
	OS.SendMessage (handle, OS.WM_NCCALCSIZE, 0, rect);
	int newWidth = rect.right - rect.left;
	int newHeight = rect.bottom - rect.top;
	OS.GetClientRect (handle, rect);
	int oldWidth = rect.right - rect.left;
	int oldHeight = rect.bottom - rect.top;
	if (newWidth == oldWidth && newHeight == oldHeight) {
		return result;
	}
	RECT inset = new RECT ();
	OS.SendMessage (handle, OS.TCM_ADJUSTRECT, 0, inset);
	int marginX = -inset.right, marginY = -inset.bottom;
	if (newWidth != oldWidth) {
		int left = oldWidth;
		if (newWidth < oldWidth) left = newWidth;
		OS.SetRect (rect, left - marginX, 0, newWidth, newHeight);
		OS.InvalidateRect (handle, rect, true);
	}
	if (newHeight != oldHeight) {
		int bottom = oldHeight;
		if (newHeight < oldHeight) bottom = newHeight;
		if (newWidth < oldWidth) oldWidth -= marginX;
		OS.SetRect (rect, 0, bottom - marginY, oldWidth, newHeight);
		OS.InvalidateRect (handle, rect, true);
	}
	return result;
}

LRESULT wmNotifyChild (NMHDR hdr, int /*long*/ wParam, int /*long*/ lParam) {
	int code = hdr.code;
	switch (code) {
		case OS.TCN_SELCHANGE: 
		case OS.TCN_SELCHANGING:
			TabItem item = null;
			int index = (int)/*64*/OS.SendMessage (handle, OS.TCM_GETCURSEL, 0, 0);
			if (index != -1) item = items [index];
			if (item != null) {
				Control control = item.control;
				if (control != null && !control.isDisposed ()) {
					if (code == OS.TCN_SELCHANGE) {
						control.setBounds (getClientArea ());
					}
					control.setVisible (code == OS.TCN_SELCHANGE);
				}
			}
			if (code == OS.TCN_SELCHANGE) {
				Event event = new Event ();
				event.item = item;
				sendSelectionEvent (SWT.Selection, event, false);
			}
	}
	return super.wmNotifyChild (hdr, wParam, lParam);
}

}
