package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class provide an area for dynamically
 * positioning the items they contain.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>CoolItem</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
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

public class CoolBar extends Composite {
	CoolItem [] items;
	CoolItem [] originalItems;
	static final int ReBarProc;
	static final TCHAR ReBarClass = new TCHAR (0, OS.REBARCLASSNAME, true);
	static {
		INITCOMMONCONTROLSEX icex = new INITCOMMONCONTROLSEX ();
		icex.dwSize = INITCOMMONCONTROLSEX.sizeof;
		icex.dwICC = OS.ICC_COOL_CLASSES;
		OS.InitCommonControlsEx (icex);
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, ReBarClass, lpWndClass);
		ReBarProc = lpWndClass.lpfnWndProc;
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
 * for all SWT widget classes should include a comment which
 * describes the style constants which are applicable to the class.
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
public CoolBar (Composite parent, int style) {
	super (parent, checkStyle (style));
}

int callWindowProc (int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	return OS.CallWindowProc (ReBarProc, handle, msg, wParam, lParam);
}

static int checkStyle (int style) {
	style |= SWT.NO_FOCUS;
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
	int width = 0, rowWidth = 0, height = 0, rowHeight = 0;
	RECT rect = new RECT ();
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	rbBand.fMask = OS.RBBIM_IDEALSIZE | OS.RBBIM_CHILDSIZE | OS.RBBIM_STYLE;
	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
	for (int i=0; i<count; i++) {
		OS.SendMessage (handle, OS.RB_GETBANDINFO, i, rbBand);
		OS.SendMessage (handle, OS.RB_GETBANDBORDERS, i, rect);
		if ((rbBand.fStyle & OS.RBBS_BREAK) != 0) {
			width = Math.max (width, rowWidth);
			height += rowHeight;
			rowWidth = rowHeight = 0;
		} else if (i != 0) {
			rowWidth += 2;
		}
		rowWidth += rbBand.cxIdeal + rect.left + rect.right;
		rowHeight = Math.max (rowHeight, rbBand.cyMinChild + rect.top + rect.bottom);
	}
	width = Math.max (width, rowWidth);
	height += rowHeight - rect.top - rect.bottom;
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int border = getBorderWidth ();
	width += border * 2;
	height += border * 2;
	return new Point (width, height);
}

void createHandle () {
	super.createHandle ();
	state &= ~CANVAS;
	
	/*
	* Feature in Windows.  When the control is created,
	* it does not use the default system font.  A new HFONT
	* is created and destroyed when the control is destroyed.
	* This means that a program that queries the font from
	* this control, uses the font in another control and then
	* destroys this control will have the font unexpectedly
	* destroyed in the other control.  The fix is to assign
	* the font ourselves each time the control is created.
	* The control will not destroy a font that it did not
	* create.
	*/
	int hFont = OS.GetStockObject (OS.SYSTEM_FONT);
	OS.SendMessage (handle, OS.WM_SETFONT, hFont, 0);
}

void createItem (CoolItem item, int index) {
	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	int id = 0;
	while (id < items.length && items [id] != null) id++;
	if (id == items.length) {
		CoolItem [] newItems = new CoolItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	int hHeap = OS.GetProcessHeap ();
	int lpText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, TCHAR.sizeof);
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	rbBand.fMask = OS.RBBIM_TEXT | OS.RBBIM_STYLE | OS.RBBIM_ID;
	rbBand.fStyle = OS.RBBS_VARIABLEHEIGHT | OS.RBBS_GRIPPERALWAYS;
	rbBand.lpText = lpText;
	rbBand.wID = id;
	if (OS.SendMessage (handle, OS.RB_INSERTBAND, index, rbBand) == 0) {
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	OS.HeapFree (hHeap, 0, lpText);
	items [item.id = id] = item;
	int length = originalItems.length;
	CoolItem [] newOriginals = new CoolItem [length + 1];
	System.arraycopy (originalItems, 0, newOriginals, 0, index);
	System.arraycopy (originalItems, index, newOriginals, index + 1, length - index);
	newOriginals [index] = item;
	originalItems = newOriginals;
}

void createWidget () {
	super.createWidget ();
	items = new CoolItem [4];
	originalItems = new CoolItem [0];
}

void destroyItem (CoolItem item) {
	int index = OS.SendMessage (handle, OS.RB_IDTOINDEX, item.id, 0);
	/*
	* Feature in Windows.  When Windows removed a rebar
	* band, it makes the band child invisible.  The fix
	* is to show the child.
	*/		
	Control control = item.control;
	boolean wasVisible = control != null && !control.isDisposed() && control.getVisible ();
	if (OS.SendMessage (handle, OS.RB_DELETEBAND, index, 0) == 0) {
		error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	items [item.id] = null;
	item.id = -1;
	if (wasVisible) control.setVisible (true);
	index = 0;
	while (index < originalItems.length) {
		if (originalItems [index] == item) break;
		index++;
	}
	int length = originalItems.length - 1;
	CoolItem [] newOriginals = new CoolItem [length];
	System.arraycopy (originalItems, 0, newOriginals, 0, index);
	System.arraycopy (originalItems, index + 1, newOriginals, index, length - index);
	originalItems = newOriginals;
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public CoolItem getItem (int index) {
	checkWidget ();
	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	rbBand.fMask = OS.RBBIM_ID;
	OS.SendMessage (handle, OS.RB_GETBANDINFO, index, rbBand);
	return items [rbBand.wID];
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getItemCount () {
	checkWidget ();
	return OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
}

/**
 * Returns an array of zero-relative indices which map the order
 * that the items in the receiver were added in to
 * the order which they are currently being displayed.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the receiver's item order
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int [] getItemOrder () {
	checkWidget ();
	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
	int [] indices = new int [count];
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	rbBand.fMask = OS.RBBIM_ID;
	for (int i=0; i<count; i++) {
		OS.SendMessage (handle, OS.RB_GETBANDINFO, i, rbBand);
		CoolItem item = items [rbBand.wID];
		int index = 0;
		while (index<originalItems.length) {
			if (originalItems [index] == item) break;
			index++;
		}
		if (index == originalItems.length) error (SWT.ERROR_CANNOT_GET_ITEM);
		indices [i] = index;
	}
	return indices;
}

/**
 * Returns an array of <code>CoolItems</code>s which are the
 * items in the receiver. 
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the receiver's items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public CoolItem [] getItems () {
	checkWidget ();
	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
	CoolItem [] result = new CoolItem [count];
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	rbBand.fMask = OS.RBBIM_ID;
	for (int i=0; i<count; i++) {
		OS.SendMessage (handle, OS.RB_GETBANDINFO, i, rbBand);
		result [i] = items [rbBand.wID];
	}
	return result;
}

/**
 * Returns an array of points whose x and y coordinates describe
 * the widths and heights (respectively) of the items in the receiver.
 *
 * @return the receiver's item sizes
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point [] getItemSizes () {
	checkWidget ();	
	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
	Point [] sizes = new Point [count];
	for (int i=0; i<count; i++) {
		RECT rect = new RECT ();
		OS.SendMessage (handle, OS.RB_GETRECT, i, rect);
		sizes [i] = new Point (rect.right - rect.left, rect.bottom - rect.top);
	}
	return sizes;
}

/**
 * Returns whether or not the coolbar is 'locked'. When a coolbar
 * is locked, its items cannot be repositioned.
 *
 * @return true if the coolbar is locked, false otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getLocked () {
	checkWidget ();
	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	rbBand.fMask = OS.RBBIM_STYLE;
	for (int i=0; i<count; i++) {
		OS.SendMessage (handle, OS.RB_GETBANDINFO, i, rbBand);
		if ((rbBand.fStyle & OS.RBBS_NOGRIPPER) == 0) return false;
	}
	return true;
}

/**
 * Returns an array of ints which describe the zero-relative
 * row number of the row which each of the items in the 
 * receiver occurs in.
 *
 * @return the receiver's wrap indices
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int [] getWrapIndices () {
	checkWidget ();
	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
	int [] indices = new int [count];
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	rbBand.fMask = OS.RBBIM_STYLE;
	int wrapCount = 0;
	for (int i=0; i<count; i++) {
		OS.SendMessage (handle, OS.RB_GETBANDINFO, i, rbBand);
		if ((rbBand.fStyle & OS.RBBS_BREAK) != 0) indices [wrapCount++] = i;	
	}
	int [] answer = new int [wrapCount];
	System.arraycopy(indices, 0, answer, 0, wrapCount);
	return answer;
}

/**
 * Searches the receiver's items, in the order they were
 * added, starting at the first item (index 0) until an item
 * is found that is equal to the argument, and returns the
 * index of that item. If no item is found, returns -1.
 *
 * @param item the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the item is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the item is disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (CoolItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error (SWT.ERROR_INVALID_ARGUMENT);
	return OS.SendMessage (handle, OS.RB_IDTOINDEX, item.id, 0);
}

void releaseWidget () {
	for (int i=0; i<items.length; i++) {
		CoolItem item = items [i];
		if (item != null && !item.isDisposed ()) {
			item.releaseWidget ();
		}
	}
	items = null;
	super.releaseWidget();
}

void setBackgroundPixel (int pixel) {
	if (background == pixel) return;
	background = pixel;
	if (pixel == -1) pixel = defaultBackground ();
	OS.SendMessage (handle, OS.RB_SETBKCOLOR, 0, pixel);
	setItemColors (OS.SendMessage (handle, OS.RB_GETTEXTCOLOR, 0, 0), pixel);
}

void setForegroundPixel (int pixel) {
	if (foreground == pixel) return;
	foreground = pixel;
	if (pixel == -1) pixel = defaultForeground ();
	OS.SendMessage (handle, OS.RB_SETTEXTCOLOR, 0, pixel);
	setItemColors (pixel, OS.SendMessage (handle, OS.RB_GETBKCOLOR, 0, 0));
}

void setItemColors (int foreColor, int backColor) {
	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	rbBand.fMask = OS.RBBIM_COLORS;
	rbBand.clrFore = foreColor;
	rbBand.clrBack = backColor;
	for (int i=0; i<count; i++) {
		OS.SendMessage (handle, OS.RB_SETBANDINFO, i, rbBand);
	}
}

/**
 * Sets the receiver's item order, wrap indices, and item
 * sizes at once. This equivalent to calling the setter
 * methods for each of these values individually.
 *
 * @param itemOrder the new item order
 * @param wrapIndices the new wrap indices
 * @param size the new item sizes
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setItemLayout (int [] itemOrder, int [] wrapIndices, Point [] sizes) {
	checkWidget ();
	setItemOrder (itemOrder);
	setItemSizes (sizes);
	setWrapIndices(wrapIndices);
}

/**
 * Sets the the order that the items in the receiver should 
 * be displayed in to the given argument which is described
 * in terms of the zero-relative ordering of when the items
 * were added.
 *
 * @param itemOrder the new item order
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
 * </ul>
 */
void setItemOrder (int [] itemOrder) {
	if (itemOrder == null) error (SWT.ERROR_NULL_ARGUMENT);
	int itemCount = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
	if (itemOrder.length != itemCount) error (SWT.ERROR_INVALID_ARGUMENT);
	
	/* Ensure that itemOrder does not contain any duplicates. */
	boolean [] set = new boolean [itemCount];
	for (int i=0; i<itemOrder.length; i++) {
		int index = itemOrder [i];
		if (index < 0 || index >= itemCount) error (SWT.ERROR_INVALID_RANGE);
		if (set [index]) error (SWT.ERROR_INVALID_ARGUMENT);
		set [index] = true;
	}
	
	for (int i=0; i<itemOrder.length; i++) {
		int id = originalItems [itemOrder [i]].id;
		int index = OS.SendMessage (handle, OS.RB_IDTOINDEX, id, 0);
		OS.SendMessage (handle, OS.RB_MOVEBAND, index, i);
	}
}

/**
 * Sets the width and height of the areas in the receiver which
 * are used to display its items to the ones specified by the
 * argument, which is an array of points whose x and y coordinates
 * describe the widths and heights (respectively) in the order the 
 * items were added.
 *
 * @param sizes the new sizes for each of the receiver's items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
void setItemSizes (Point [] sizes) {
	if (sizes == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
	if (sizes.length != count) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<count; i++) {
		RECT rect = new RECT ();
		OS.SendMessage (handle, OS.RB_GETBANDBORDERS, i, rect);
		REBARBANDINFO rbBand = new REBARBANDINFO ();
		rbBand.cbSize = REBARBANDINFO.sizeof;
		rbBand.fMask = OS.RBBIM_CHILDSIZE | OS.RBBIM_SIZE | OS.RBBIM_IDEALSIZE;
		int width = sizes [i].x, height = sizes [i].y;
		rbBand.cx = width;
		rbBand.cxIdeal = width - rect.left - rect.right;
		rbBand.cyChild = rbBand.cyMinChild = rbBand.cyMaxChild = height;
		OS.SendMessage (handle, OS.RB_SETBANDINFO, i, rbBand);
	}
}

/**
 * Sets whether the reciever is 'locked' or not. When a coolbar
 * is locked, its items cannot be repositioned.
 *
 * @param locked lock the coolbar if true, otherwise unlock the coolbar
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocked (boolean locked) {
	checkWidget ();
	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	rbBand.fMask = OS.RBBIM_STYLE;
	for (int i=0; i<count; i++) {
		OS.SendMessage (handle, OS.RB_GETBANDINFO, i, rbBand);
		if (locked) {
			rbBand.fStyle |= OS.RBBS_NOGRIPPER;
		} else {
			rbBand.fStyle &= ~OS.RBBS_NOGRIPPER;
		}
		OS.SendMessage (handle, OS.RB_SETBANDINFO, i, rbBand);
	}
}

/**
 * Sets the row that each of the receiver's items will be
 * displayed in to the given array of ints which describe
 * the zero-relative row number of the row for each item.
 * If indices is null, the items will be placed on one line.
 *
 * @param indices the new wrap indices
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setWrapIndices (int [] indices) {
	checkWidget ();
	if (indices == null) indices = new int [0];
	int count = OS.SendMessage (handle, OS.RB_GETBANDCOUNT, 0, 0);
	for (int i=0; i<indices.length; i++) {
		if (indices [i] < 0 || indices [i] >= count) {
			error (SWT.ERROR_INVALID_RANGE);
		}	
	}
	REBARBANDINFO rbBand = new REBARBANDINFO ();
	rbBand.cbSize = REBARBANDINFO.sizeof;
	rbBand.fMask = OS.RBBIM_STYLE;
	for (int i=0; i<count; i++) {
		OS.SendMessage (handle, OS.RB_GETBANDINFO, i, rbBand);
		rbBand.fStyle &= ~OS.RBBS_BREAK;
		OS.SendMessage (handle, OS.RB_SETBANDINFO, i, rbBand);
	}
	for (int i=0; i<indices.length; i++) {
		OS.SendMessage (handle, OS.RB_GETBANDINFO, indices [i], rbBand);
		rbBand.fStyle |= OS.RBBS_BREAK;
		OS.SendMessage (handle, OS.RB_SETBANDINFO, indices [i], rbBand);
	}
}

int widgetStyle () {
	int bits = super.widgetStyle () | OS.CCS_NODIVIDER | OS.CCS_NORESIZE;
	bits |= OS.RBS_VARHEIGHT | OS.RBS_BANDBORDERS | OS.RBS_DBLCLKTOGGLE;
	return bits;
}

TCHAR windowClass () {
	return ReBarClass;
}

int windowProc () {
	return ReBarProc;
}

LRESULT WM_COMMAND (int wParam, int lParam) {
	/*
	* Feature in Windows.  When the coolbar window
	* proc processes WM_COMMAND, it forwards this
	* message to the parent.  This is done so that
	* children of the coolbar that send WM_COMMAND
	* messages to their parents will notify not only
	* the coolbar but also the parent of the coolbar,
	* which is typically the application window and
	* the window that is looking for this message.
	* If the coolbar did not do this, applications
	* would have to subclass the coolbar window to
	* see WM_COMMAND messages. Because the coolbar
	* window is subclassed, the WM_COMMAND message
	* is delivered twice.  The fix is to avoid
	* calling the coolbar window proc.
	*/
	LRESULT result = super.WM_COMMAND (wParam, lParam);
	if (result != null) return result;
	return LRESULT.ZERO;
}

LRESULT WM_ERASEBKGND (int wParam, int lParam) {
	/*
	* Feature in Windows.  For some reason, Windows
	* does not fully erase the area that the cool bar
	* occupies when the size of the cool bar grows.
	* The fix is to erase the cool bar background.
	*/
	drawBackground (wParam);
	return null;
}

LRESULT wmNotifyChild (int wParam, int lParam) {
	NMHDR hdr = new NMHDR ();
	OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
	switch (hdr.code) {
		case OS.RBN_HEIGHTCHANGE:
			Point size = getSize ();
			int border = getBorderWidth ();
			int height = OS.SendMessage (handle, OS.RB_GETBARHEIGHT, 0, 0);
			setSize (size.x, height + (border * 2));
			break;
	}
	return super.wmNotifyChild (wParam, lParam);
}

}
