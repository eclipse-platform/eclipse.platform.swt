package org.eclipse.swt.widgets;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.Rect;
import org.eclipse.swt.internal.carbon.ControlTabInfoRecV1;

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
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class TabFolder extends Composite {
	
	private static final int TAB_HEIGHT= 32;
	private static final int TOP_MARGIN= 1;
	private static final int MARGIN= 4;
	
	TabItem [] items;
	private int oldValue;
	
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
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public TabFolder (Composite parent, int style) {
	super (parent, checkStyle (style));
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
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
public void addSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

static int checkStyle (int style) {
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
	/* AW
	RECT insetRect = new RECT (), itemRect = new RECT ();
	OS.SendMessage (handle, OS.TCM_ADJUSTRECT, 0, insetRect);
	int width = insetRect.left - insetRect.right, height = 0;
	int count = OS.SendMessage (handle, OS.TCM_GETITEMCOUNT, 0, 0);
	if (count != 0) {
		OS.SendMessage (handle, OS.TCM_GETITEMRECT, count - 1, itemRect);
		width = Math.max (width, itemRect.right - insetRect.right);
	}
	*/
	int width= 100;		// AW a bogus guess
	int height= 0;
	
	Point size;
	if (layout != null) {
		size = layout.computeSize (this, wHint, hHint, changed);
	} else {
		size = minimumSize ();
	}
	if (size.x == 0) size.x = DEFAULT_WIDTH;
	if (size.y == 0) size.y = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	width = Math.max (width, size.x);
	height = Math.max (height, size.y);
	Rectangle trim = computeTrim (0, 0, width, height);
	width = trim.width;  height = trim.height;
	return new Point (width, height);
}
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	/* AW
	RECT rect = new RECT ();
	OS.SetRect (rect, x, y, x + width, y + height);
	OS.SendMessage (handle, OS.TCM_ADJUSTRECT, 1, rect);
	*/
	/* AW
	int border = getBorderWidth ();
	rect.left -= border;  rect.right += border;
	rect.top -= border;  rect.bottom += border;
	int newWidth = rect.right - rect.left;
	int newHeight = rect.bottom - rect.top;
	return new Rectangle (rect.left, rect.top, newWidth, newHeight);
	*/
	return new Rectangle (x-MARGIN, y-TOP_MARGIN-TAB_HEIGHT, width+(2*MARGIN), height+TOP_MARGIN+TAB_HEIGHT+MARGIN);
}

void createItem (TabItem item, int index) {
	int count = OS.GetControl32BitMaximum(handle);
	
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	if (count == items.length) {
		TabItem [] newItems = new TabItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	/* AW
	TCITEM tcItem = new TCITEM ();
	if (OS.SendMessage (handle, OS.TCM_INSERTITEM, index, tcItem) == -1) {
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	*/
	OS.SetControl32BitMaximum(handle, count+1);

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
		sendEvent (SWT.Selection, event);
		// the widget could be destroyed at this point
	}
}
void createHandle (int index) {
	state |= HANDLE;
	state &= ~CANVAS;
	handle= OS.NewControl(0, new Rect(), null, false, (short)0, (short)0, (short)0, (short)OS.kControlTabSmallProc, 0);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
	MacUtil.insertControl(handle, parent.handle, -1);
	OS.HIViewSetVisible(handle, true);
}

void createWidget (int index) {
	super.createWidget (index);
	items = new TabItem [4];
}

void destroyItem (TabItem item) {
	int count = OS.GetControl32BitMaximum(handle);
	int index = 0;
	while (index < count) {
		if (items [index] == item) break;
		index++;
	}
	if (index == count) return;	// not found
	int selectionIndex = OS.GetControl32BitValue(handle)-1;
	
	/* AW
	if (OS.SendMessage (handle, OS.TCM_DELETEITEM, index, 0) == 0) {
		error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	*/
	OS.SetControl32BitMaximum(handle, count-1);
	
	System.arraycopy (items, index + 1, items, index, --count - index);
	items [count] = null;
	if (count == 0) {
		/* AW
		if (imageList != null) {
			OS.SendMessage (handle, OS.TCM_SETIMAGELIST, 0, 0);
			Display display = getDisplay ();
			display.releaseImageList (imageList);
		}
		imageList = null;
		*/
		items = new TabItem [4];
	}
	
	updateCarbon(index);
	
	if (count > 0 && index == selectionIndex) {
		setSelection (Math.max (0, selectionIndex - 1));
		selectionIndex = getSelectionIndex ();
		if (selectionIndex != -1) {
			Event event = new Event ();
			event.item = items [selectionIndex];
			sendEvent (SWT.Selection, event);
			// the widget could be destroyed at this point
		}
	}
}

public Rectangle getClientArea () {
	checkWidget ();
	/* AW
	if (parent.hdwp != 0) {
		int oldHdwp = parent.hdwp;
		parent.hdwp = 0;
		OS.EndDeferWindowPos (oldHdwp);
		int count = parent.getChildrenCount ();
		parent.hdwp = OS.BeginDeferWindowPos (count);
	}
	RECT rect = new RECT ();
	OS.GetClientRect (handle, rect);
	OS.SendMessage (handle, OS.TCM_ADJUSTRECT, 0, rect);
	int width = rect.right - rect.left;
	int height = rect.bottom - rect.top;
	return new Rectangle (rect.left, rect.top, width, height);
	*/
	
	Rect inner= new Rect();
	OS.GetControlData(handle, (short)OS.kControlEntireControl, OS.kControlTabContentRectTag, Rect.sizeof, inner, null);
	return new Rectangle(inner.left, inner.top, inner.right-inner.left, inner.bottom-inner.top);
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
	int count = OS.GetControl32BitMaximum(handle);
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
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
	return OS.GetControl32BitMaximum(handle);
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
	int count = OS.GetControl32BitMaximum(handle);
	int n= 0;
	for (int i=0; i < count; i++) 
		if (items[i] != null)
			n++;
	if (n < count)
		System.out.println("TabFolder.getItems: found null slots");
	TabItem [] result = new TabItem [n];
	for (int i=0; i < n; i++) 
		if (items[i] != null)
			result[i]= items[i];
	//System.arraycopy (items, 0, result, 0, count);
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
	int index= OS.GetControl32BitValue(handle)-1;
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
	return OS.GetControl32BitValue(handle)-1;
}
void hookEvents () {
	super.hookEvents ();
	
	Display display= getDisplay();
	OS.SetControlAction(handle, display.fControlActionProc);
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
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (TabItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = OS.GetControl32BitMaximum(handle);
	for (int i=0; i<count; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}
/* AW
boolean mnemonicHit (char key) {
	for (int i=0; i<items.length; i++) {
		TabItem item = items [i];
		if (item != null) {
			char ch = findMnemonic (item.getText ());
			if (Character.toUpperCase (key) == Character.toUpperCase (ch)) {		
				if (setFocus ()) {
					setSelection (i, true);
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
*/
void releaseWidget () {
	int count = OS.GetControl32BitMaximum(handle);
	for (int i=0; i<count; i++) {
		TabItem item = items [i];
		if (item != null && !item.isDisposed ()) item.releaseWidget ();
	}
	items = null;
	/* AW
	if (imageList != null) {
		OS.SendMessage (handle, OS.TCM_SETIMAGELIST, 0, 0);
		Display display = getDisplay ();
		display.releaseImageList (imageList);
	}
	imageList = null;
	*/
	super.releaseWidget ();
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
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selected is first cleared, then the new items are
 * selected.
 *
 * @param items the array of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (TabItem [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (items.length == 0) {
		setSelection (-1);
		return;
	}
	for (int i=items.length-1; i>=0; --i) {
		int index = indexOf (items [i]);
		if (index != -1) setSelection (index);
	}
}

/**
 * Selects the item at the given zero-relative index in the receiver. 
 * If the item at the index was already selected, it remains selected.
 * The current selected is first cleared, then the new items are
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
	setSelection (index, false);
}

void setSelection (int index, boolean notify) {
	
	int oldIndex = OS.GetControl32BitValue(handle) - 1;
	if (oldIndex != -1) {
		TabItem item = items [oldIndex];
		if (item != null) {
			Control control = item.control;
			if (control != null && !control.isDisposed ()) {
				control.setVisible (false);
			}
		}
	}
	OS.SetControl32BitValue(handle, index+1);

	int newIndex = OS.GetControl32BitValue(handle) - 1;
	if (newIndex != -1) {
		TabItem item = items [newIndex];
		if (item != null) {
			Control control = item.control;
			if (control != null && !control.isDisposed ()) {
				control.setBounds (getClientArea ());
				control.setVisible (true);
			}
			if (notify) {
				Event event = new Event ();
				event.item = item;
				sendEvent (SWT.Selection, event);
			}
		}	
	}
}

boolean traversePage (boolean next) {
	int count = getItemCount ();
	if (count == 0) return false;
	int index = getSelectionIndex ();
	if (index == -1) {
		index = 0;
	} else {
		int offset = (next) ? 1 : -1;
		index = (index + offset + count) % count;
	}
	setSelection (index, true);
	return index == getSelectionIndex ();
}

//////////////////////////////////////////////////
// Mac stuff
//////////////////////////////////////////////////

int processSelection (Object callData) {
	MacControlEvent macEvent= (MacControlEvent) callData;
	oldValue= OS.GetControl32BitValue(handle)-1;
	handleSelectionChange(macEvent.getPartCode()-1);
	return OS.noErr;
}

private void handleSelectionChange(int newValue)  {
	
	if (false)
		setSelection (newValue, true);
	
	else {
		TabItem item = null;
		int index= oldValue;
	
		if (index != -1) item = items [index];
		if (item != null) {
			Control control = item.control;
			if (control != null && !control.isDisposed ()) {
				control.setVisible (false);
			}
		}
			
		index= newValue;
		if (index != -1) item = items [index];
		if (item != null) {
			Control control = item.control;
			if (control != null && !control.isDisposed ()) {
				control.setBounds (getClientArea ());
				control.setVisible (true);
			}
		}
		
		Event event = new Event ();
		event.item = item;
		postEvent (SWT.Selection, event);
	}
}

private void updateCarbon(int startIndex) {
	int n= OS.GetControl32BitMaximum(handle);
	for (int i= startIndex; i < n; i++) {
		TabItem item= items[i];
		if (item != null)
			setTabText(i, item.getText());
	}
}

void setTabText(int index, String string) {
	int sHandle= 0;
	try {
		String t= MacUtil.removeMnemonics(string);
		sHandle= OS.CFStringCreateWithCharacters(t);
		ControlTabInfoRecV1 tab= new ControlTabInfoRecV1();
		tab.version= OS.kControlTabInfoVersionOne;
		tab.iconSuiteID= 0;
		tab.name= sHandle;
		OS.SetControlData(handle, index+1, OS.kControlTabInfoTag, ControlTabInfoRecV1.sizeof, tab);
	} finally {
		if (sHandle != 0)
			OS.CFRelease(sHandle);
	}
}

void setTabImage(int index, Image image) {
	/* AW: does not work yet...
	int icon= Image.carbon_createCIcon(image);
	if (icon != 0)
		if (OS.setTabIcon(handle, index+1, icon) != OS.noErr)
			System.err.println("TabFolder.setTabImage: error");
	*/
}

void internalGetControlBounds(int hndl, Rect bounds) {
	super.internalGetControlBounds(hndl, bounds);
	bounds.left += -MARGIN;
	bounds.top += -TOP_MARGIN;
	bounds.right -= -MARGIN;
	bounds.bottom -= -MARGIN;
}

/**
 * Overridden from Control.
 * x and y are relative to window!
 */
void handleResize(int hndl, Rect bounds) {

	bounds.left+= MARGIN;
	bounds.right-= MARGIN;
	bounds.top+= TOP_MARGIN;
	bounds.bottom-= MARGIN;
	super.handleResize(hndl, bounds);
	
	if (handle != 0) {
		int selectedIndex= OS.GetControl32BitValue(handle)-1;
		if (selectedIndex != -1) {
			Control control = items[selectedIndex].getControl();
			if (control != null && !control.isDisposed()) {
				control.setBounds(getClientArea());
			}
		}
	}
}

}
