package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/** 
 * Instances of this class implement a selectable user interface
 * object that displays a list of images and strings and issue
 * notificiation when selected.
 * <p>
 * The item children that may be added to instances of this class
 * must be of type <code>TableItem</code>.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add <code>Control</code> children to it,
 * or set a layout on it.
 * </p><p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI, CHECK, FULL_SELECTION, HIDE_SELECTION</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */

public class Table extends Composite {
	TableItem [] items;
	TableColumn [] columns;
	ImageList imageList;
	boolean ignoreSelect, dragStarted, ignoreResize, mouseDown, customDraw;
	static final int TableProc;
	static final TCHAR TableClass = new TCHAR (0, OS.WC_LISTVIEW, true);
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, TableClass, lpWndClass);
		TableProc = lpWndClass.lpfnWndProc;
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
public Table (Composite parent, int style) {
	super (parent, checkStyle (style));
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * When <code>widgetSelected</code> is called, the item field of the event object is valid.
 * If the reciever has <code>SWT.CHECK</code> style set and the check selection changes,
 * the event object detail field contains the value <code>SWT.CHECK</code>.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
 * The item field of the event object is valid for default selection, but the detail field is not used.
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

int callWindowProc (int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	return OS.CallWindowProc (TableProc, handle, msg, wParam, lParam);
}

static int checkStyle (int style) {
	/*
	* Feature in Windows.  It is not possible to create
	* a table that does not have scroll bars.  Therefore,
	* no matter what style bits are specified, set the
	* H_SCROLL and V_SCROLL bits so that the SWT style
	* will match the widget that Windows creates.
	*/
	style |= SWT.H_SCROLL | SWT.V_SCROLL;
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int bits = 0;
	if (wHint != SWT.DEFAULT) {
		bits |= wHint & 0xFFFF;
	} else {
		int width = 0;
		int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
		int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
		for (int i=0; i<count; i++) {
			width += OS.SendMessage (handle, OS.LVM_GETCOLUMNWIDTH, i, 0);
		}
		bits |= width & 0xFFFF;
	}
	if (hHint != SWT.DEFAULT) bits |= hHint << 16;
	int result = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, -1, bits);
	int width = result & 0xFFFF, height = result >> 16;
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int border = getBorderWidth ();
	width += border * 2;  height += border * 2;
	/*
	* Feature in Windows.  For some reason, LVM_APPROXIMATEVIEWRECT
	* does not include the space for the vertical scroll bar but does
	* take into account the horizontal scroll bar when calculating the
	* space needed to show the items.  The fix is to add in this space.
	*/
	if ((style & SWT.V_SCROLL) != 0) {
		width += OS.GetSystemMetrics (OS.SM_CXVSCROLL);
	}
	if (((style & SWT.H_SCROLL) != 0) && (hHint != SWT.DEFAULT)) {
		height += OS.GetSystemMetrics (OS.SM_CYHSCROLL);
	}
	return new Point (width, height);
}

void createHandle () {
	super.createHandle ();
	state &= ~CANVAS;

	/* 
	* This code is intentionally commented.  According to
	* the documentation, setting the default item size is
	* supposed to improve performance.  By experimentation,
	* this does not seem to have much of an effect.
	*/	
//	OS.SendMessage (handle, OS.LVM_SETITEMCOUNT, 1024 * 2, 0);

	/* Set the checkbox image list */
	if ((style & SWT.CHECK) != 0) setCheckboxImageList (true);

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

	/*
	* Bug in Windows.  When the first column is inserted
	* without setting the header text, Windows will never
	* allow the header text for the first column to be set.
	* The fix is to set the text to an empty string when
	* the column is inserted.
	*/
	LVCOLUMN lvColumn = new LVCOLUMN ();
	lvColumn.mask = OS.LVCF_TEXT;
	int hHeap = OS.GetProcessHeap ();
	int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, TCHAR.sizeof);
	lvColumn.pszText = pszText;
	OS.SendMessage (handle, OS.LVM_INSERTCOLUMN, 0, lvColumn);
	OS.HeapFree (hHeap, 0, pszText);

	/* Set the extended style bits */
	int bits = OS.LVS_EX_SUBITEMIMAGES | OS.LVS_EX_LABELTIP;
	if ((style & SWT.FULL_SELECTION) != 0) bits |= OS.LVS_EX_FULLROWSELECT;
	OS.SendMessage (handle, OS.LVM_SETEXTENDEDLISTVIEWSTYLE, bits, bits);
}

void createItem (TableColumn column, int index) {
	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (count == 1 && columns [0] == null) count = 0;
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	if (count == columns.length) {
		TableColumn [] newColumns = new TableColumn [columns.length + 4];
		System.arraycopy (columns, 0, newColumns, 0, columns.length);
		columns = newColumns;
	}
	/*
	* Insert the column into the columns array before inserting
	* it into the widget so that the column will be present when
	* any callbacks are issued as a result of LVM_INSERTCOLUMN
	* or LVM_SETCOLUMN.
	*/
	System.arraycopy (columns, index, columns, index + 1, count - index);
	columns [index] = column;
	if (index == 0) {
		if (count > 0) {
			LVCOLUMN lvColumn = new LVCOLUMN ();
			lvColumn.mask = OS.LVCF_WIDTH;
			OS.SendMessage (handle, OS.LVM_INSERTCOLUMN, 1, lvColumn);
			OS.SendMessage (handle, OS.LVM_GETCOLUMN, 1, lvColumn);
			int width = lvColumn.cx;
			int cchTextMax = 1024;
			int hHeap = OS.GetProcessHeap ();
			int byteCount = cchTextMax * TCHAR.sizeof;
			int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
			LVITEM lvItem = new LVITEM ();
			lvItem.mask = OS.LVIF_TEXT | OS.LVIF_IMAGE | OS.LVIF_STATE;
			int itemCount = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
			for (int i=0; i<itemCount; i++) {
				lvItem.iItem = i;
				lvItem.iSubItem = 0;
				lvItem.pszText = pszText;
				lvItem.cchTextMax = cchTextMax;
				OS.SendMessage (handle, OS.LVM_GETITEM, 0, lvItem);
				lvItem.iSubItem = 1;
				OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
				lvItem.iSubItem = 0;
				lvItem.pszText = lvItem.cchTextMax = 0;
				lvItem.iImage = OS.I_IMAGENONE;
				OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
			}
			lvColumn.mask = OS.LVCF_TEXT | OS.LVCF_IMAGE | OS.LVCF_WIDTH | OS.LVCF_FMT;
			lvColumn.pszText = pszText;
			lvColumn.cchTextMax = cchTextMax;
			OS.SendMessage (handle, OS.LVM_GETCOLUMN, 0, lvColumn);
			OS.SendMessage (handle, OS.LVM_SETCOLUMN, 1, lvColumn);
			lvColumn.fmt = OS.LVCFMT_IMAGE;
			lvColumn.cx = width;
			lvColumn.iImage = OS.I_IMAGENONE;
			lvColumn.pszText = lvColumn.cchTextMax = 0;
			OS.SendMessage (handle, OS.LVM_SETCOLUMN, 0, lvColumn);
			lvColumn.mask = OS.LVCF_FMT;
			lvColumn.fmt = OS.LVCFMT_LEFT;
			OS.SendMessage (handle, OS.LVM_SETCOLUMN, 0, lvColumn);
			if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
		}
	} else {
		int fmt = OS.LVCFMT_LEFT;
		if ((column.style & SWT.CENTER) == SWT.CENTER) fmt = OS.LVCFMT_CENTER;
		if ((column.style & SWT.RIGHT) == SWT.RIGHT) fmt = OS.LVCFMT_RIGHT;
		LVCOLUMN lvColumn = new LVCOLUMN ();
		lvColumn.mask = OS.LVCF_WIDTH | OS.LVCF_FMT;
		lvColumn.fmt = fmt;
		OS.SendMessage (handle, OS.LVM_INSERTCOLUMN, index, lvColumn);
	}
}

void createItem (TableItem item, int index) {
	item.foreground = item.background = -1;
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	if (!(0 <= index && index <= count)) error (SWT.ERROR_INVALID_RANGE);
	if (count == items.length) {
		TableItem [] newItems = new TableItem [items.length + 4];
		System.arraycopy (items, 0, newItems, 0, items.length);
		items = newItems;
	}
	LVITEM lvItem = new LVITEM ();
	lvItem.iItem = index;

	/*
	* Bug in Windows.  Despite the fact that the image list
	* index has never been set for the item, Windows always
	* assumes that the image index for the item is valid.
	* When an item is inserted, the image index is zero.
	* Therefore, when the first image is inserted and is
	* assigned image index zero, every item draws with this
	* image.  The fix is to set the image index to none when
	* the image is created.
	*/
	lvItem.iImage = OS.I_IMAGENONE;
	lvItem.mask = OS.LVIF_IMAGE;

	/* Set the initial unchecked state */
	if ((style & SWT.CHECK) != 0) {
		lvItem.mask = lvItem.mask | OS.TVIF_STATE;
		lvItem.state = 1 << 12;
		lvItem.stateMask = OS.LVIS_STATEIMAGEMASK;
	}

	/* Insert the item */
	ignoreSelect = true;
	int result = OS.SendMessage (handle, OS.LVM_INSERTITEM, 0, lvItem);
	ignoreSelect = false;
	if (result == -1) error (SWT.ERROR_ITEM_NOT_ADDED);
	System.arraycopy (items, index, items, index + 1, count - index);
	items [index] = item;
}

void createWidget () {
	super.createWidget ();
	items = new TableItem [4];
	columns = new TableColumn [4];
}

int defaultBackground () {
	return OS.GetSysColor (OS.COLOR_WINDOW);
}

/**
 * Deselects the items at the given zero-relative indices in the receiver.
 * If the item at the given zero-relative index in the receiver 
 * is selected, it is deselected.  If the item at the index
 * was not selected, it remains deselected. Indices that are out
 * of range and duplicate indices are ignored.
 *
 * @param indices the array of indices for the items to deselect
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int [] indices) {
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_STATE;
	lvItem.stateMask = OS.LVIS_SELECTED;
	for (int i=0; i<indices.length; i++) {
		lvItem.iItem = indices [i];
		ignoreSelect = true;
		OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
		ignoreSelect = false;
	}
}

/**
 * Deselects the item at the given zero-relative index in the receiver.
 * If the item at the index was already deselected, it remains
 * deselected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to deselect
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int index) {
	checkWidget ();
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_STATE;
	lvItem.stateMask = OS.LVIS_SELECTED;
	lvItem.iItem = index;
	ignoreSelect = true;
	OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
	ignoreSelect = false;
}

/**
 * Deselects the items at the given zero-relative indices in the receiver.
 * If the item at the given zero-relative index in the receiver 
 * is selected, it is deselected.  If the item at the index
 * was not selected, it remains deselected.  The range of the
 * indices is inclusive. Indices that are out of range are ignored.
 *
 * @param start the start index of the items to deselect
 * @param end the end index of the items to deselect
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void deselect (int start, int end) {
	checkWidget ();
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_STATE;
	lvItem.stateMask = OS.LVIS_SELECTED;
	for (int i=start; i<=end; i++) {
		lvItem.iItem = i;
		ignoreSelect = true;
		OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
		ignoreSelect = false;
	}
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
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_STATE;
	lvItem.stateMask = OS.LVIS_SELECTED;
	OS.SendMessage (handle, OS.LVM_SETITEMSTATE, -1, lvItem);
}

void destroyItem (TableColumn column) {
	int hwndHeader =  OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	int index = 0;
	while (index < count) {
		if (columns [index] == column) break;
		index++;
	}
	if (index == count) return;
	boolean first = false;
	if (index == 0) {
		first = true;
		if (count > 1) {
			index = 1;
			int cchTextMax = 1024;
			int hHeap = OS.GetProcessHeap ();
			int byteCount = cchTextMax * TCHAR.sizeof;
			int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
			LVCOLUMN lvColumn = new LVCOLUMN ();
			lvColumn.mask = OS.LVCF_TEXT | OS.LVCF_WIDTH;
			lvColumn.pszText = pszText;
			lvColumn.cchTextMax = cchTextMax;
			OS.SendMessage (handle, OS.LVM_GETCOLUMN, 1, lvColumn);
			lvColumn.mask |= OS.LVCF_FMT;
			lvColumn.fmt = OS.LVCFMT_LEFT;
			OS.SendMessage (handle, OS.LVM_SETCOLUMN, 0, lvColumn);
			LVITEM lvItem = new LVITEM ();
			lvItem.mask = OS.LVIF_TEXT | OS.LVIF_IMAGE | OS.LVIF_STATE;
			lvItem.pszText = pszText;
			lvItem.cchTextMax = cchTextMax;
			int itemCount = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
			for (int i=0; i<itemCount; i++) {
				lvItem.iItem = i;
				lvItem.iSubItem = 1;
				OS.SendMessage (handle, OS.LVM_GETITEM, 0, lvItem);
				lvItem.iSubItem = 0;
				OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
			}
			if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
		} else {
			int hHeap = OS.GetProcessHeap ();
			int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, TCHAR.sizeof);
			LVCOLUMN lvColumn = new LVCOLUMN ();
			lvColumn.mask = OS.LVCF_TEXT;
			lvColumn.pszText = pszText;
			OS.SendMessage (handle, OS.LVM_SETCOLUMN, 0, lvColumn);
			if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
			OS.SendMessage (handle, OS.LVM_SETCOLUMNWIDTH, 0, OS.LVSCW_AUTOSIZE);
		}
	}
	if (count > 1) {
		if (OS.SendMessage (handle, OS.LVM_DELETECOLUMN, index, 0) == 0) {
			error (SWT.ERROR_ITEM_NOT_REMOVED);
		}
	}
	if (first) index = 0;
	System.arraycopy (columns, index + 1, columns, index, --count - index);
	columns [count] = null;
}

void destroyItem (TableItem item) {
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	int index = 0;
	while (index < count) {
		if (items [index] == item) break;
		index++;
	}
	if (index == count) return;
	ignoreSelect = true;
	int code = OS.SendMessage (handle, OS.LVM_DELETEITEM, index, 0);
	ignoreSelect = false;
	if (code == 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
	System.arraycopy (items, index + 1, items, index, --count - index);
	items [count] = null;
	if (count == 0) {
		if (imageList != null) {
			OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, 0);
			Display display = getDisplay ();
			display.releaseImageList (imageList);
		}
		imageList = null;
		items = new TableItem [4];
	}
}

int getBackgroundPixel () {
	return OS.SendMessage (handle, OS.LVM_GETBKCOLOR, 0, 0);
}

/**
 * Returns the column at the given, zero-relative index in the
 * receiver. Throws an exception if the index is out of range.
 * If no <code>TableColumn</code>s were created by the programmer,
 * this method will throw <code>ERROR_INVALID_RANGE</code> despite
 * the fact that a single column of data may be visible in the table.
 * This occurs when the programmer uses the table like a list, adding
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
 */
public TableColumn getColumn (int index) {
	checkWidget ();
	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (count == 1 && columns [0] == null) count = 0;
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
	return columns [index];
}

/**
 * Returns the number of columns contained in the receiver.
 * If no <code>TableColumn</code>s were created by the programmer,
 * this value is zero, despite the fact that visually, one column
 * of items is may be visible. This occurs when the programmer uses
 * the table like a list, adding items but never creating a column.
 *
 * @return the number of columns
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getColumnCount () {
	checkWidget ();
	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (count == 1 && columns [0] == null) count = 0;
	return count;
}

/**
 * Returns an array of <code>TableColumn</code>s which are the
 * columns in the receiver. If no <code>TableColumn</code>s were
 * created by the programmer, the array is empty, despite the fact
 * that visually, one column of items may be visible. This occurs
 * when the programmer uses the table like a list, adding items but
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
 */
public TableColumn [] getColumns () {
	checkWidget ();
	int hwndHeader =  OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (count == 1 && columns [0] == null) count = 0;
	TableColumn [] result = new TableColumn [count];
	System.arraycopy (columns, 0, result, 0, count);
	return result;
}

int getFocusIndex () {
	return OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
}

int getForegroundPixel () {
	return OS.SendMessage (handle, OS.LVM_GETTEXTCOLOR, 0, 0);
}

/**
 * Returns the width in pixels of a grid line.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getGridLineWidth () {
	checkWidget ();
	return 1;
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
 */
public boolean getHeaderVisible () {
	checkWidget ();
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	return (bits & OS.LVS_NOCOLUMNHEADER) == 0;
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
public TableItem getItem (int index) {
	checkWidget ();
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
	return items [index];
}

/**
 * Returns the item at the given point in the receiver
 * or null if no such item exists. The point is in the
 * coordinate system of the receiver.
 *
 * @param point the point used to locate the item
 * @return the item at the given point
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public TableItem getItem (Point point) {
	checkWidget ();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	LVHITTESTINFO pinfo = new LVHITTESTINFO ();
	pinfo.x = point.x;  pinfo.y = point.y;
	OS.SendMessage (handle, OS.LVM_HITTEST, 0, pinfo);
	if (pinfo.iItem != -1) return items [pinfo.iItem];
	return null;
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
	return OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
}

/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the receiver's.
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
	int empty = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, 0, 0);
	int oneItem = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, 1, 0);
	return (oneItem >> 16) - (empty >> 16);
}

/**
 * Returns an array of <code>TableItem</code>s which are the items
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
public TableItem [] getItems () {
	checkWidget ();
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	TableItem [] result = new TableItem [count];
	System.arraycopy (items, 0, result, 0, count);
	return result;
}

/**
 * Returns <code>true</code> if the receiver's lines are visible,
 * and <code>false</code> otherwise.
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
 */
public boolean getLinesVisible () {
	checkWidget ();
	int bits = OS.SendMessage (handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
	return (bits & OS.LVS_EX_GRIDLINES) != 0;
}

/**
 * Returns an array of <code>TableItem</code>s that are currently
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
public TableItem [] getSelection () {
	checkWidget ();
	int i = -1, j = 0, count = OS.SendMessage (handle, OS.LVM_GETSELECTEDCOUNT, 0, 0);
	TableItem [] result = new TableItem [count];
	while ((i = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, i, OS.LVNI_SELECTED)) != -1) {
		result [j++] = items [i];
	}
	return result;
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
	return OS.SendMessage (handle, OS.LVM_GETSELECTEDCOUNT, 0, 0);
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
	int focusIndex = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
	int selectedIndex = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_SELECTED);
	if (focusIndex == selectedIndex) return selectedIndex;
	int i = -1;
	while ((i = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, i, OS.LVNI_SELECTED)) != -1) {
		if (i == focusIndex) return i;
	}
	return selectedIndex;
}

/**
 * Returns the zero-relative indices of the items which are currently
 * selected in the receiver.  The array is empty if no items are selected.
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its selection, so modifying the array will
 * not affect the receiver. 
 * </p>
 * @return the array of indices of the selected items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int [] getSelectionIndices () {
	checkWidget ();
	int i = -1, j = 0, count = OS.SendMessage (handle, OS.LVM_GETSELECTEDCOUNT, 0, 0);
	int [] result = new int [count];
	while ((i = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, i, OS.LVNI_SELECTED)) != -1) {
		result [j++] = i;
	}
	return result;
}

/**
 * Returns the zero-relative index of the item which is currently
 * at the top of the receiver. This index can change when items are
 * scrolled or new items are added or removed.
 *
 * @return the index of the top item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopIndex () {
	checkWidget ();
	return OS.SendMessage (handle, OS.LVM_GETTOPINDEX, 0, 0);
}

int imageIndex (Image image) {
	if (image == null) return OS.I_IMAGENONE;
	if (imageList == null) {
		Rectangle bounds = image.getBounds ();
		imageList = getDisplay ().getImageList (new Point (bounds.width, bounds.height));
		int index = imageList.indexOf (image);
		if (index == -1) index = imageList.add (image);
		int hImageList = imageList.getHandle ();
		OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, hImageList);
		return index;
	}
	int index = imageList.indexOf (image);
	if (index != -1) return index;
	return imageList.add (image);
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
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int indexOf (TableColumn column) {
	checkWidget ();
	if (column == null) error (SWT.ERROR_NULL_ARGUMENT);
	int hwndHeader =  OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	for (int i=0; i<count; i++) {
		if (columns [i] == column) return i;
	}
	return -1;
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
public int indexOf (TableItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	for (int i=0; i<count; i++) {
		if (items [i] == item) return i;
	}
	return -1;
}

/**
 * Returns <code>true</code> if the item is selected,
 * and <code>false</code> otherwise.  Indices out of
 * range are ignored.
 *
 * @param index the index of the item
 * @return the visibility state of the item at the index
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean isSelected (int index) {
	checkWidget ();
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_STATE;
	lvItem.stateMask = OS.LVIS_SELECTED;
	lvItem.iItem = index;
	int result = OS.SendMessage (handle, OS.LVM_GETITEM, 0, lvItem);
	return (result != 0) && ((lvItem.state & OS.LVIS_SELECTED) != 0);
}

void releaseWidget () {
	int hwndHeader =  OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int columnCount = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (columnCount == 1 && columns [0] == null) columnCount = 0;
	for (int i=0; i<columnCount; i++) {
		TableColumn column = columns [i];
		if (!column.isDisposed ()) column.releaseWidget ();
	}
	columns = null;
	int itemCount = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	
	/*
	* Feature in Windows.  When there are a large number
	* of items in a table (>1000), it is much faster to
	* delete each item with LVM_DELETEITEM rather than
	* using LVM_DELETEALLITEMS.  The fix is to delete the
	* items, one by one.
	*
	* NOTE: LVM_DELETEALLITEMS is also sent by the table
	* when the table is destroyed.
	*/
	/* Turn off redraw and leave it off */
	OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
	for (int i=itemCount-1; i>=0; --i) {
		ignoreSelect = true;
		OS.SendMessage (handle, OS.LVM_DELETEITEM, i, 0);
		ignoreSelect = false;
		TableItem item = items [i];
		if (!item.isDisposed ()) item.releaseWidget ();
	}
	
	/*
	* This code is intentionally commmented.  This is
	* the correct code, assuming that the problem with
	* LVM_DELETEALLITEMS did not occur.
	*/
//	for (int i=0; i<itemCount; i++) {
//		TableItem item = items [i];
//		if (!item.isDisposed ()) item.releaseWidget ();
//	}

	items = null;
	if (imageList != null) {
		OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, 0);
		Display display = getDisplay ();
		display.releaseImageList (imageList);
	}
	imageList = null;
	int hOldList = OS.SendMessage (handle, OS.LVM_GETIMAGELIST, OS.LVSIL_STATE, 0);
	OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_STATE, 0);
	if (hOldList != 0) OS.ImageList_Destroy (hOldList);
	super.releaseWidget ();
}

/**
 * Removes the items from the receiver's list at the given
 * zero-relative indices.
 *
 * @param indices the array of indices of the items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void remove (int [] indices) {
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int [] newIndices = new int [indices.length];
	System.arraycopy (indices, 0, newIndices, 0, indices.length);
	sort (newIndices);
	int last = -1;
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	for (int i=0; i<newIndices.length; i++) {
		int index = newIndices [i];
		if (index != last || i == 0) {
			ignoreSelect = true;
			int code = OS.SendMessage (handle, OS.LVM_DELETEITEM, index, 0);
			ignoreSelect = false;
			if (code == 0) {
				if (0 <= index && index < count) {
					error (SWT.ERROR_ITEM_NOT_REMOVED);
				} else {
					error (SWT.ERROR_INVALID_RANGE);
				}
			}
			
			// BUG - disposed callback could remove an item
			items [index].releaseWidget ();
			System.arraycopy (items, index + 1, items, index, --count - index);
			items [count] = null;
			last = index;
		}
	}
}

/**
 * Removes the item from the receiver at the given
 * zero-relative index.
 *
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void remove (int index) {
	checkWidget ();
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	ignoreSelect = true;
	int code = OS.SendMessage (handle, OS.LVM_DELETEITEM, index, 0);
	ignoreSelect = false;
	if (code == 0) {
		if (0 <= index && index < count) {
			error (SWT.ERROR_ITEM_NOT_REMOVED);
		} else {
			error (SWT.ERROR_INVALID_RANGE);
		}
	}
	TableItem item = items [index];
	System.arraycopy (items, index + 1, items, index, --count - index);
	items [count] = null;
	item.releaseWidget ();
}

/**
 * Removes the items from the receiver which are
 * between the given zero-relative start and end 
 * indices (inclusive).
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if either the start or end are not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void remove (int start, int end) {
	checkWidget ();
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	int index = start;
	while (index <= end) {
		ignoreSelect = true;
		int code = OS.SendMessage (handle, OS.LVM_DELETEITEM, start, 0);
		ignoreSelect = false;
		if (code == 0) break;
		
		// BUG - disposed callback could remove an item
		items [index].releaseWidget ();
		index++;
	}
	System.arraycopy (items, index, items, start, count - index);
	for (int i=count-(index-start); i<count; i++) items [i] = null;
	if (index <= end) {
		if (0 <= index && index < count) {
			error (SWT.ERROR_ITEM_NOT_REMOVED);
		} else {
			error (SWT.ERROR_INVALID_RANGE);
		}
	}
}

/**
 * Removes all of the items from the receiver.
 * <p>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget ();
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	
	/*
	* Feature in Windows.  When there are a large number
	* of items in a table (>1000), it is much faster to
	* delete each item with LVM_DELETEITEM rather than
	* using LVM_DELETEALLITEMS.  The fix is to delete the
	* items, one by one.
	*
	* NOTE: LVM_DELETEALLITEMS is also sent by the table
	* when the table is destroyed.
	*/
	boolean redraw = drawCount == 0 && OS.IsWindowVisible (handle);
	if (redraw) OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
	int index = count - 1;
	while (index >= 0) {
		ignoreSelect = true;
		int code = OS.SendMessage (handle, OS.LVM_DELETEITEM, index, 0);
		ignoreSelect = false;
		if (code == 0) break;
		
		// BUG - disposed callback could remove an item
		items [index].releaseWidget ();
		--index;
	}
	if (redraw) {
		OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
		/*
		* This code is intentionally commented.  The window proc
		* for the table implements WM_SETREDRAW to invalidate
		* and erase the table so it is not necessary to do this
		* again.
		*/
//		int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
//		OS.RedrawWindow (handle, null, 0, flags);
	}
	if (index != -1) error (SWT.ERROR_ITEM_NOT_REMOVED);
	
	/*
	* This code is intentionally commmented.  This is
	* the correct code, assuming that the problem with
	* LVM_DELETEALLITEMS did not occur.
	*/
//	ignoreSelect = true;
//	int code = OS.SendMessage (handle, OS.LVM_DELETEALLITEMS, 0, 0);
//	ignoreSelect = false;
//	if (code == 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
//	for (int i=0; i<count; i++) {
//		TableItem item = items [i];
//		if (!item.isDisposed ()) item.releaseWidget ();
//	}

	if (imageList != null) {
		int hwndHeader =  OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
		int columnCount = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
		if (columnCount == 1 && columns [0] == null) columnCount = 0;
		int i = 0;
		while (i < columnCount) {
			TableColumn column = columns [i];
			if (column.getImage () != null) break;
			i++;
		}
		if (i == columnCount) {
			OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, 0);
			Display display = getDisplay ();
			display.releaseImageList (imageList);
		}
	}
	imageList = null;
	items = new TableItem [4];
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
public void removeSelectionListener(SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * If the item at the given zero-relative index in the receiver 
 * is not selected, it is selected.  If the item at the index
 * was selected, it remains selected. Indices that are out
 * of range and duplicate indices are ignored.
 *
 * @param indices the array of indices for the items to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int [] indices) {
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int length = indices.length;
	if (length == 0) return;
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_STATE;
	lvItem.state = OS.LVIS_SELECTED;
	lvItem.stateMask = OS.LVIS_SELECTED;
	for (int i=indices.length-1; i>=0; --i) {
		lvItem.iItem = indices [i];
		ignoreSelect = true;
		OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
		ignoreSelect = false;
	}
}

/**
 * Selects the item at the given zero-relative index in the receiver. 
 * If the item at the index was already selected, it remains
 * selected. Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int index) {
	checkWidget ();
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_STATE;
	lvItem.state = OS.LVIS_SELECTED;
	lvItem.stateMask = OS.LVIS_SELECTED;
	lvItem.iItem = index;
	ignoreSelect = true;
	OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
	ignoreSelect = false;
}

/**
 * Selects the items at the given zero-relative indices in the receiver.
 * If the item at the index was already selected, it remains
 * selected. The range of the indices is inclusive. Indices that are
 * out of range are ignored.
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int start, int end) {
	checkWidget ();
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_STATE;
	lvItem.state = OS.LVIS_SELECTED;
	lvItem.stateMask = OS.LVIS_SELECTED;
	for (int i=start; i<=end; i++) {
		lvItem.iItem = i;
		ignoreSelect = true;
		OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
		ignoreSelect = false;
	}
}

/**
 * Selects all the items in the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void selectAll () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return;
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_STATE;
	lvItem.state = OS.LVIS_SELECTED;
	lvItem.stateMask = OS.LVIS_SELECTED;
	ignoreSelect = true;
	OS.SendMessage (handle, OS.LVM_SETITEMSTATE, -1, lvItem);
	ignoreSelect = false;
}

LRESULT sendMouseDownEvent (int type, int button, int msg, int wParam, int lParam) {
	/*
	* Feature in Windows.  Inside WM_LBUTTONDOWN and WM_RBUTTONDOWN,
	* the widget starts a modal loop to determine if the user wants
	* to begin a drag/drop operation or marque select.  Unfortunately,
	* this modal loop eats the corresponding mouse up.  The fix is to
	* detect the cases when the modal loop has eaten the mouse up and
	* issue a fake mouse up.
	*
	* By observation, when the mouse is clicked anywhere but the check
	* box, the widget eats the mouse up.  When the mouse is dragged,
	* the widget does not eat the mouse up.
	*/
	LVHITTESTINFO pinfo = new LVHITTESTINFO ();
	pinfo.x = (short) (lParam & 0xFFFF);
	pinfo.y = (short) (lParam >> 16);
	OS.SendMessage (handle, OS.LVM_HITTEST, 0, pinfo);
	sendMouseEvent (type, button, msg, wParam, lParam);

	/*
	* Force the table to have focus so that when the user
	* reselects the focus item, the LVIS_FOCUSED state bits
	* for the item will be set.  These bits are used when
	* the table is multi-select to issue the selection
	* event.  If the user did not click on an item, then
	* set focus to the table so that it will come to the
	* front and take focus in the work around below.
	*/
	OS.SetFocus (handle);
		
	/*
	* Feature in Windows.  When the user selects outside of
	* a table item, Windows deselects all the items, even
	* when the table is multi-select.  While not strictly
	* wrong, this is unexpected.  The fix is to detect the
	* case and avoid calling the window proc.
	*/
	if (pinfo.iItem == -1) {
		if (OS.GetCapture () != handle) OS.SetCapture (handle);
		return LRESULT.ZERO;
	}
	
	/*
	* Feature in Windows.  In tables that have the style
	* LVS_SINGLESEL, when a table item is reselected, the
	* table does not issue a WM_NOTIFY because the item
	* state has not changed.  This is inconsistent with
	* the list widget and other widgets in Windows.  The
	* fix is to detect the case when an item is reselected
	* and issue the notification.
	*/
	int oldState = 0;
	if ((style & SWT.SINGLE) != 0 && pinfo.iItem != -1) {
		LVITEM lvItem = new LVITEM ();
		lvItem.mask = OS.LVIF_STATE;
		lvItem.stateMask = OS.LVIS_SELECTED;
		lvItem.iItem = pinfo.iItem;
		OS.SendMessage (handle, OS.LVM_GETITEM, 0, lvItem);
		oldState = lvItem.state;
	}
	dragStarted = false;
	int code = callWindowProc (msg, wParam, lParam);
	if ((style & SWT.SINGLE) != 0 && pinfo.iItem != -1) {
		if ((oldState & OS.LVIS_SELECTED) != 0) {
			LVITEM lvItem = new LVITEM ();
			lvItem.mask = OS.LVIF_STATE;
			lvItem.stateMask = OS.LVIS_SELECTED;
			lvItem.iItem = pinfo.iItem;
			OS.SendMessage (handle, OS.LVM_GETITEM, 0, lvItem);
			int newState = lvItem.state;
			if ((newState & OS.LVIS_SELECTED) != 0) {
				Event event = new Event ();
				event.item = items [pinfo.iItem];
				postEvent (SWT.Selection, event);
			}
		}
	}
	if (dragStarted) {
		if (OS.GetCapture () != handle) OS.SetCapture (handle);
	} else {
		int flags = OS.LVHT_ONITEMLABEL | OS.LVHT_ONITEMICON;
		boolean fakeMouseUp = (pinfo.flags & flags) != 0;
		if (!fakeMouseUp && (style & SWT.MULTI) != 0) {
			fakeMouseUp = (pinfo.flags & OS.LVHT_ONITEMSTATEICON) == 0;
		}
		if (fakeMouseUp) {
			mouseDown = false;
			sendMouseEvent (SWT.MouseUp, button, msg, wParam, lParam);
		}
	}
	dragStarted = false;
	return new LRESULT (code);
}

void setBackgroundPixel (int pixel) {
	if (background == pixel) return;
	background = pixel;
	
	/*
	* Feature in Windows.  Setting the color to be
	* the current default is not correct because the
	* widget will not change colors when the colors
	* are changed from the control panel.  There is
	* no fix at this time.
	*/
	if (pixel == -1) pixel = defaultBackground ();
	OS.SendMessage (handle, OS.LVM_SETBKCOLOR, 0, pixel);
	OS.SendMessage (handle, OS.LVM_SETTEXTBKCOLOR, 0, pixel);
	if ((style & SWT.CHECK) != 0) setCheckboxImageList (true);
	
	/*
	* Feature in Windows.  When the background color is
	* changed, the table does not redraw until the next
	* WM_PAINT.  The fix is to force a redraw.
	*/
	OS.InvalidateRect (handle, null, true);
}

void setCheckboxImageList (boolean force) {
	if ((style & SWT.CHECK) == 0) return;
	int height = 0, width = 0;
	int hImageList = OS.SendMessage (handle, OS.LVM_GETIMAGELIST, OS.LVSIL_SMALL, 0);
	if (hImageList != 0) {
		int [] cx = new int [1], cy = new int [1];
		OS.ImageList_GetIconSize (hImageList, cx, cy);
		height = width = cy [0];
	} else {
		int empty = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, 0, 0);
		int oneItem = OS.SendMessage (handle, OS.LVM_APPROXIMATEVIEWRECT, 1, 0);
		height = width = (oneItem >> 16) - (empty >> 16);
	}
	int hOldStateList = OS.SendMessage (handle, OS.LVM_GETIMAGELIST, OS.LVSIL_STATE, 0);
	if (!force && hOldStateList != 0) {
		int [] cx = new int [1], cy = new int [1];
		OS.ImageList_GetIconSize (hOldStateList, cx, cy);
		if (height == cx [0] && width == cy [0]) return;
	}
	int count = 4;
	int hStateList = OS.ImageList_Create (width, height, OS.ILC_COLOR, count, count);
	int hDC = OS.GetDC (handle);
	int memDC = OS.CreateCompatibleDC (hDC);
	int hBitmap = OS.CreateCompatibleBitmap (hDC, width * count, height);
	int hOldBitmap = OS.SelectObject (memDC, hBitmap);
	RECT rect = new RECT ();
	OS.SetRect (rect, 0, 0, width * count, height);
	int hBrush = OS.CreateSolidBrush (getBackgroundPixel ());
	OS.FillRect (memDC, rect, hBrush);
	OS.DeleteObject (hBrush);
	int oldFont = OS.SelectObject (hDC, defaultFont ());
	TEXTMETRIC tm = new TEXTMETRIC ();
	OS.GetTextMetrics (hDC, tm);
	OS.SelectObject (hDC, oldFont);
	int itemWidth = Math.min (tm.tmHeight, width);
	int itemHeight = Math.min (tm.tmHeight, height);
	int left = (width - itemWidth) / 2, top = (height - itemHeight) / 2 + 1;
	OS.SetRect (rect, left, top, left + itemWidth, top + itemHeight);
	OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_FLAT);
	rect.left += width;  rect.right += width;
	OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_CHECKED | OS.DFCS_FLAT);
	rect.left += width;  rect.right += width;
	OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_INACTIVE | OS.DFCS_FLAT);
	rect.left += width;  rect.right += width;
	OS.DrawFrameControl (memDC, rect, OS.DFC_BUTTON, OS.DFCS_BUTTONCHECK | OS.DFCS_CHECKED | OS.DFCS_INACTIVE | OS.DFCS_FLAT);
	OS.SelectObject (memDC, hOldBitmap);
	OS.DeleteDC (memDC);
	OS.ReleaseDC (handle, hDC);
	OS.ImageList_AddMasked (hStateList, hBitmap, 0);
	OS.DeleteObject (hBitmap);
	OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_STATE, hStateList);
	if (hOldStateList != 0) OS.ImageList_Destroy (hOldStateList);
}

void setFocusIndex (int index) {
	LVITEM lvItem = new LVITEM ();
	lvItem.mask = OS.LVIF_STATE;
	lvItem.state = OS.LVIS_FOCUSED;
	lvItem.stateMask = OS.LVIS_FOCUSED;
	lvItem.iItem = index;
	ignoreSelect = true;
	OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
	ignoreSelect = false;
}

public void setFont (Font font) {
	checkWidget ();
	super.setFont (font);
	setScrollWidth ();
	int bits = OS.SendMessage (handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
	if ((bits & OS.LVS_EX_GRIDLINES) == 0) return;
	bits = OS.GetWindowLong (handle, OS.GWL_STYLE);	
	if ((bits & OS.LVS_NOCOLUMNHEADER) != 0) return;
	setRowHeight ();
}

void setForegroundPixel (int pixel) {
	if (foreground == pixel) return;
	foreground = pixel;
	
	/*
	* Feature in Windows.  Setting the color to be
	* the current default is not correct because the
	* table will not change colors when the colors
	* are changed from the control panel.  There is
	* no fix at this time.
	*/
	if (pixel == -1) pixel = defaultForeground ();
	OS.SendMessage (handle, OS.LVM_SETTEXTCOLOR, 0, pixel);
		
	/*
	* Feature in Windows.  When the foreground color is
	* changed, the table does not redraw until the next
	* WM_PAINT.  The fix is to force a redraw.
	*/
	OS.InvalidateRect (handle, null, true);
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
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHeaderVisible (boolean show) {
	checkWidget ();
	int newBits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	newBits &= ~OS.LVS_NOCOLUMNHEADER;
	if (!show) newBits |= OS.LVS_NOCOLUMNHEADER;
	/*
	* Feature in Windows.  Setting or clearing LVS_NOCOLUMNHEADER
	* causes the table to scroll to the beginning.  The fix is to
	* save and restore the top index.
	*/
	int topIndex = getTopIndex ();
	OS.SetWindowLong (handle, OS.GWL_STYLE, newBits);
	if (topIndex != 0) setTopIndex (topIndex);
	if (show) {
		int bits = OS.SendMessage (handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
		if ((bits & OS.LVS_EX_GRIDLINES) != 0) setRowHeight ();
	}
}

/**
 * Marks the receiver's lines as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. 
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLinesVisible (boolean show) {
	checkWidget ();
	int newBits = 0;
	if (show) {
		newBits = OS.LVS_EX_GRIDLINES;
		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);	
		if ((bits & OS.LVS_NOCOLUMNHEADER) == 0) setRowHeight ();
	}
	OS.SendMessage (handle, OS.LVM_SETEXTENDEDLISTVIEWSTYLE, OS.LVS_EX_GRIDLINES, newBits);
}

public void setRedraw (boolean redraw) {
	checkWidget ();
	if (redraw) {
		if (--drawCount == 0) {
			setScrollWidth ();
			/*
			* This code is intentionally commented.  When many items
			* are added to a table, it is slightly faster to temporarily
			* unsubclass the window proc so that messages are dispatched
			* directly to the table.  This is optimization is dangerous
			* because any operation can occur when redraw is turned off,
			* even operations where the table must be subclassed in order
			* to have the correct behavior or work around a Windows bug.
			* For now, don't attempt it.
			*/
//			subclass ();

			/*
			* Bug in Windows.  For some reason, when WM_SETREDRAW is used 
			* to turn redraw back on this may result in a WM_SIZE.  If the table column
			* widths are adjusted in the WM_SIZE callback, blank lines may be
			* inserted at the top of the widget.  A call to LVM_GETTOPINDEX will
			* return a negative number (this is an impossible result).  The fix is to
			* ignore any resize generated by WM_SETREDRAW and defer the work
			* until the WM_SETREDRAW has returned.
			*/
			ignoreResize = true;
			OS.SendMessage (handle, OS.WM_SETREDRAW, 1, 0);
			if (!ignoreResize) {
				int count = getChildrenCount ();
				if (count > 1 && hdwp == 0) {
					hdwp = OS.BeginDeferWindowPos (count);
				}
				if (layout != null) layout.layout (this, false);
				sendEvent (SWT.Resize);
				// widget may be disposed at this point
				if (isDisposed ()) return;
				int oldHdwp = hdwp;
				hdwp = 0;
				if (oldHdwp != 0) OS.EndDeferWindowPos (oldHdwp);
			}
			ignoreResize = false;
			
			/*
			* This code is intentionally commented.  The window proc
			* for the table implements WM_SETREDRAW to invalidate
			* and erase the table and the header.  This is undocumented
			* behavior.  The commented code below shows what is actually
			* happening and reminds us that we are relying on this
			* undocumented behavior.
			*/
//			int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);	
//			if (hwndHeader != 0) OS.SendMessage (hwndHeader, OS.WM_SETREDRAW, 1, 0);
//			int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
//			OS.RedrawWindow (handle, null, 0, flags);
//			if (hwndHeader != 0) OS.RedrawWindow (hwndHeader, null, 0, flags);
		}
	} else {
		if (drawCount++ == 0) {
			OS.SendMessage (handle, OS.WM_SETREDRAW, 0, 0);
			int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
			if (hwndHeader != 0) OS.SendMessage (hwndHeader, OS.WM_SETREDRAW, 0, 0);
			/*
			* This code is intentionally commented.  When many items
			* are added to a table, it is slightly faster to temporarily
			* unsubclass the window proc so that messages are dispatched
			* directly to the table.  This is optimization is dangerous
			* because any operation can occur when redraw is turned off,
			* even operations where the table must be subclassed in order
			* to have the correct behavior or work around a Windows bug.
			* For now, don't attempt it.
			*/
//			unsubclass ();
		}
	}
}

void setRowHeight () {
	/*
	* Bug in Windows.  When both a header and grid lines are
	* displayed, the grid lines do not take into account the
	* height of the header and draw in the wrong place.  The
	* fix is to set the height of the table items to be the
	* height of the header so that the lines draw in the right
	* place.  The height of a table item is the maximum of the
	* height of the font or the height of image list.
	*
	* NOTE: In version 5.80 of COMCTL32.DLL, the bug is fixed.
	*/
	if ((COMCTL32_MAJOR << 16 | COMCTL32_MINOR) >= (5 << 16 | 80)) return;
	int hOldList = OS.SendMessage (handle, OS.LVM_GETIMAGELIST, OS.LVSIL_SMALL, 0);
	if (hOldList != 0) return;
	int hwndHeader =  OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	RECT rect = new RECT ();
	OS.GetWindowRect (hwndHeader, rect);
	int height = rect.bottom - rect.top - 1;
	int hImageList = OS.ImageList_Create (1, height, 0, 0, 0);
	OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, hImageList);
	OS.SendMessage (handle, OS.LVM_SETIMAGELIST, OS.LVSIL_SMALL, 0);
	OS.ImageList_Destroy (hImageList);
}

void setScrollWidth () {
	if (drawCount != 0) return;
	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
	if (count == 1 && columns [0] == null) {
		OS.SendMessage (handle, OS.LVM_SETCOLUMNWIDTH, 0, OS.LVSCW_AUTOSIZE);
	}
}

/**
 * Selects the items at the given zero-relative indices in the receiver. 
 * The current selected is first cleared, then the new items are selected.
 *
 * @param indices the indices of the items to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of indices is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int[])
 */
public void setSelection (int [] indices) {
	checkWidget ();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	deselectAll ();
	select (indices);
	if (indices.length != 0) {
		int focusIndex = indices [0];
		if (focusIndex != -1) setFocusIndex (focusIndex);
	}
	showSelection ();
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selected is first cleared, then the new items are
 * selected.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array of items is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if one of the item has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int)
 */
public void setSelection (TableItem [] items) {
	checkWidget ();  
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	deselectAll ();
	int length = items.length;
	if (length == 0) return;
	int focusIndex = -1;
	if ((style & SWT.SINGLE) != 0) length = 1;
	for (int i=length-1; i>=0; --i) {
		int index = indexOf (items [i]);
		if (index != -1) {
			select (focusIndex = index);
		}
	}
	if (focusIndex != -1) setFocusIndex (focusIndex);
	showSelection ();
}

/**
 * Selects the item at the given zero-relative index in the receiver. 
 * The current selected is first cleared, then the new item is selected.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int)
 */
public void setSelection (int index) {
	checkWidget ();
	deselectAll ();
	select (index);
	if (index != -1) setFocusIndex (index);
	showSelection ();
}

/**
 * Selects the items at the given zero-relative indices in the receiver. 
 * The current selected if first cleared, then the new items are selected.
 *
 * @param start the start index of the items to select
 * @param end the end index of the items to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#deselectAll()
 * @see Table#select(int,int)
 */
public void setSelection (int start, int end) {
	checkWidget ();
	deselectAll ();
	select (start, end);
	/*
	* NOTE: This code relies on the select (int, int)
	* selecting the last item in the range for a single
	* selection table.
	*/
	int focusIndex = (style & SWT.SINGLE) != 0 ? end : start;
	if (focusIndex != -1) setFocusIndex (focusIndex);
	showSelection ();
}

/**
 * Sets the zero-relative index of the item which is currently
 * at the top of the receiver. This index can change when items
 * are scrolled or new items are added and removed.
 *
 * @param index the index of the top item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTopIndex (int index) {
	checkWidget (); 
	int topIndex = OS.SendMessage (handle, OS.LVM_GETTOPINDEX, 0, 0);
	if (index == topIndex) return;
	
	/*
	* Bug in Windows.  For some reason, LVM_SCROLL refuses to
	* scroll a table vertically when the width and height of
	* the table is smaller than a certain size.  The values
	* that the author is seeing are width=68 and height=6
	* but there is no guarantee that these values are absolute.
	* They may depend on the font and any number of other
	* factors.  In fact, the author has observed that setting
	* the font to anything but the default seems to sometimes
	* fix the problem.  The fix is to use LVM_GETCOUNTPERPAGE
	* to detect the case when the number of visible items is
	* zero and use LVM_ENSUREVISIBLE to scroll the table to
	* make the index visible.
	*/

	/*
	* Bug in Windows.  When the table header is visible and
	* there is not enough space to show a single table item,
	* LVM_GETCOUNTPERPAGE can return a negative number instead
	* of zero.  The fix is to test for negative or zero.
	*/
	if (OS.SendMessage (handle, OS.LVM_GETCOUNTPERPAGE, 0, 0) <= 0) {
		
		/*
		* Bug in Windows.  For some reason, LVM_ENSUREVISIBLE can
		* scroll one item more or one item less when there is not
		* enough space to show a single table item.  The fix is
		* to detect the case and call LVM_ENSUREVISIBLE again with
		* the same arguments.  It seems that once LVM_ENSUREVISIBLE
		* has scrolled into the general area, it is able to scroll
		* to the exact item.
		*/
		OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 1);
		if (index != OS.SendMessage (handle, OS.LVM_GETTOPINDEX, 0, 0)) {
			OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 1);
		}
		return;
	}

	/* Use LVM_SCROLL to scroll the table */
	RECT rect = new RECT ();
	rect.left = OS.LVIR_BOUNDS;
	OS.SendMessage (handle, OS.LVM_GETITEMRECT, 0, rect);
	int dy = (index - topIndex) * (rect.bottom - rect.top);
	OS.SendMessage (handle, OS.LVM_SCROLL, 0, dy);
}

/**
 * Shows the item.  If the item is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled until
 * the item is visible.
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
 * @see Table#showSelection()
 */
public void showItem (TableItem item) {
	checkWidget ();
	if (item == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (item.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	/*
	* Bug in Windows.  For some reason, when there is insufficient space
	* to show an item, LVM_ENSUREVISIBLE causes blank lines to be
	* inserted at the top of the widget.  A call to LVM_GETTOPINDEX will
	* return a negative number (this is an impossible result).  The fix is to
	* detect this case and fail to show the selection.
	*/
	if (OS.SendMessage (handle, OS.LVM_GETCOUNTPERPAGE, 0, 0) <= 0)  return;
	int index = indexOf (item);
	if (index != -1) {
		OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 0);
	}
}

/**
 * Shows the selection.  If the selection is already showing in the receiver,
 * this method simply returns.  Otherwise, the items are scrolled until
 * the selection is visible.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see Table#showItem(TableItem)
 */
public void showSelection () {
	checkWidget (); 
	/*
	* Bug in Windows.  For some reason, when there is insufficient space
	* to show an item, LVM_ENSUREVISIBLE causes blank lines to be
	* inserted at the top of the widget.  A call to LVM_GETTOPINDEX will
	* return a negative number (this is an impossible result).  The fix is to
	* detect this case and fail to show the selection.
	*/
	if (OS.SendMessage (handle, OS.LVM_GETCOUNTPERPAGE, 0, 0) <= 0)  return;
	int index = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_SELECTED);
	if (index != -1) OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 0);
}

int widgetStyle () {
	int bits = super.widgetStyle () | OS.LVS_SHAREIMAGELISTS | OS.WS_CLIPCHILDREN;
	if ((style & SWT.HIDE_SELECTION) == 0) bits |= OS.LVS_SHOWSELALWAYS;
	if ((style & SWT.SINGLE) != 0) bits |= OS.LVS_SINGLESEL;
	/*
	* This code is intentionally commented.  In the future,
	* the FLAT bit may be used to make the header flat and
	* unresponsive to mouse clicks.
	*/
//	if ((style & SWT.FLAT) != 0) bits |= OS.LVS_NOSORTHEADER;
	bits |= OS.LVS_REPORT | OS.LVS_NOCOLUMNHEADER;
	return bits;
}

TCHAR windowClass () {
	return TableClass;
}

int windowProc () {
	return TableProc;
}

LRESULT WM_KEYDOWN (int wParam, int lParam) {
	LRESULT result = super.WM_KEYDOWN (wParam, lParam);
	if (result != null) return result;
	if ((style & SWT.CHECK) != 0 && wParam == OS.VK_SPACE) {
		int index = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
		if (index != -1) {	
			LVITEM lvItem = new LVITEM ();
			lvItem.mask = OS.LVIF_STATE;
			lvItem.stateMask = OS.LVIS_STATEIMAGEMASK;
			lvItem.iItem = index;
			OS.SendMessage (handle, OS.LVM_GETITEM, 0, lvItem);
			int state = lvItem.state >> 12;
			if ((state & 0x1) != 0) {
				state++;
			} else {
				--state;
			}
			lvItem.state = state << 12;
			OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
		}
	}
	return result;
}

LRESULT WM_LBUTTONDBLCLK (int wParam, int lParam) {
	/*
	* Feature in Windows.  When the user selects outside of
	* a table item, Windows deselects all the items, even
	* when the table is multi-select.  While not strictly
	* wrong, this is unexpected.  The fix is to detect the
	* case and avoid calling the window proc.
	*/
	LVHITTESTINFO pinfo = new LVHITTESTINFO ();
	pinfo.x = (short) (lParam & 0xFFFF);
	pinfo.y = (short) (lParam >> 16);
	OS.SendMessage (handle, OS.LVM_HITTEST, 0, pinfo);
	sendMouseEvent (SWT.MouseDown, 1, OS.WM_LBUTTONDOWN, wParam, lParam);
	sendMouseEvent (SWT.MouseDoubleClick, 1, OS.WM_LBUTTONDBLCLK, wParam, lParam);
	if (pinfo.iItem != -1) callWindowProc (OS.WM_LBUTTONDBLCLK, wParam, lParam);
	if (OS.GetCapture () != handle) OS.SetCapture (handle);
	return LRESULT.ZERO;
}

LRESULT WM_LBUTTONDOWN (int wParam, int lParam) {
	mouseDown = true;
	
	/*
	* Feature in Windows.  For some reason, capturing
	* the mouse after processing the mouse event for the
	* widget interferes with the normal mouse processing
	* for the widget.  The fix is to avoid the automatic
	* mouse capture.
	*/
	LRESULT result = sendMouseDownEvent (SWT.MouseDown, 1, OS.WM_LBUTTONDOWN, wParam, lParam);

	/* Look for check/uncheck */
	if ((style & SWT.CHECK) != 0) {
		LVHITTESTINFO pinfo = new LVHITTESTINFO ();
		pinfo.x = (short) (lParam & 0xFFFF);
		pinfo.y = (short) (lParam >> 16);
		/*
		* Note that when the table has LVS_EX_FULLROWSELECT and the
		* user clicks anywhere on a row except on the check box, all
		* of the bits are set.  The hit test flags are LVHT_ONITEM.
		* This means that a bit test for LVHT_ONITEMSTATEICON is not
		* the correct way to determine that the user has selected
		* the check box.
		*/
		int index = OS.SendMessage (handle, OS.LVM_HITTEST, 0, pinfo);
		if (index != -1 && pinfo.flags == OS.LVHT_ONITEMSTATEICON) {	
			LVITEM lvItem = new LVITEM ();
			lvItem.mask = OS.LVIF_STATE;
			lvItem.stateMask = OS.LVIS_STATEIMAGEMASK;
			lvItem.iItem = index;
			OS.SendMessage (handle, OS.LVM_GETITEM, 0, lvItem);
			int state = lvItem.state >> 12;
			if ((state & 0x1) != 0) {
				state++;
			} else {
				--state;
			}
			lvItem.state = state << 12;
			OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
		}	
	}
	
	return result;
}

LRESULT WM_LBUTTONUP (int wParam, int lParam) {
	mouseDown = false;
	return super.WM_LBUTTONUP (wParam, lParam);
}

LRESULT WM_MOUSEHOVER (int wParam, int lParam) {
	/*
	* Feature in Windows.  Despite the fact that hot
	* tracking is not enabled, the hot tracking code
	* in WM_MOUSEHOVER is executed causing the item
	* under the cursor to be selected.  The fix is to
	* avoid calling the window proc.
	*/
	LRESULT result = super.WM_MOUSEHOVER (wParam, lParam);
	int bits = OS.SendMessage (handle, OS.LVM_GETEXTENDEDLISTVIEWSTYLE, 0, 0);
	int mask = OS.LVS_EX_ONECLICKACTIVATE | OS.LVS_EX_TRACKSELECT | OS.LVS_EX_TWOCLICKACTIVATE;
	if ((bits & mask) != 0) return result;
	return LRESULT.ZERO;
}

LRESULT WM_NOTIFY (int wParam, int lParam) {
	NMHDR hdr = new NMHDR ();
	OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
	int hwndHeader = OS.SendMessage (handle, OS.LVM_GETHEADER, 0, 0);
	if (hdr.hwndFrom == hwndHeader) {
		/*
		* Feature in Windows.  On NT, the automatically created
		* header control is created as a UNICODE window, not an
		* ANSI window despite the fact that the parent is created
		* as an ANSI window.  This means that it sends UNICODE
		* notification messages to the parent window on NT for
		* no good reason.  The data and size in the NMHEADER and
		* HDITEM structs is identical between the platforms so no
		* different message is actually necessary.  Despite this,
		* Windows sends different messages.  The fix is to look
		* for both messages, despite the platform.  This works
		* because only one will be sent on either platform, never
		* both.
		*/
		switch (hdr.code) {
			case OS.HDN_BEGINTRACKW:
			case OS.HDN_BEGINTRACKA: {
				NMHEADER phdn = new NMHEADER ();
				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
				HDITEM pitem = new HDITEM ();
				OS.MoveMemory (pitem, phdn.pitem, HDITEM.sizeof);
				TableColumn column = columns [phdn.iItem];
				if (column != null && !column.getResizable ()) {
					return LRESULT.ONE;
				}
				break;
			}
			case OS.HDN_ITEMCHANGEDW:
			case OS.HDN_ITEMCHANGEDA: {
				NMHEADER phdn = new NMHEADER ();
				OS.MoveMemory (phdn, lParam, NMHEADER.sizeof);
				Event event = new Event ();
				if (phdn.pitem != 0) {
					HDITEM pitem = new HDITEM ();
					OS.MoveMemory (pitem, phdn.pitem, HDITEM.sizeof);
					if ((pitem.mask & OS.HDI_WIDTH) != 0) {
						TableColumn column = columns [phdn.iItem];
						if (column != null) {
							column.sendEvent (SWT.Resize, event);
							/*
							* It is possible (but unlikely), that application
							* code could have disposed the widget in the resize
							* event.  If this happens, end the processing of the
							* Windows message by returning zero as the result of
							* the window proc.
							*/
							if (isDisposed ()) return LRESULT.ZERO;	
							int count = OS.SendMessage (hwndHeader, OS.HDM_GETITEMCOUNT, 0, 0);
							if (count == 1 && columns [0] == null) count = 0;
							/*
							* It is possible (but unlikely), that application
							* code could have disposed the column in the move
							* event.  If this happens, process the move event
							* for those columns that have not been destroyed.
							*/
							TableColumn [] newColumns = new TableColumn [count];
							System.arraycopy (columns, 0, newColumns, 0, count);
							for (int i=phdn.iItem+1; i<count; i++) {
								if (!newColumns [i].isDisposed ()) {
									newColumns [i].sendEvent (SWT.Move, event);
								}
							}
						}
					}
				}
				break;
			}
		}
	}
	return super.WM_NOTIFY (wParam, lParam);
}

LRESULT WM_RBUTTONDBLCLK (int wParam, int lParam) {
	/*
	* Feature in Windows.  When the user selects outside of
	* a table item, Windows deselects all the items, even
	* when the table is multi-select.  While not strictly
	* wrong, this is unexpected.  The fix is to detect the
	* case and avoid calling the window proc.
	*/
	LVHITTESTINFO pinfo = new LVHITTESTINFO ();
	pinfo.x = (short) (lParam & 0xFFFF);
	pinfo.y = (short) (lParam >> 16);
	OS.SendMessage (handle, OS.LVM_HITTEST, 0, pinfo);
	sendMouseEvent (SWT.MouseDown, 1, OS.WM_RBUTTONDOWN, wParam, lParam);
	sendMouseEvent (SWT.MouseDoubleClick, 1, OS.WM_RBUTTONDBLCLK, wParam, lParam);
	if (pinfo.iItem != -1) callWindowProc (OS.WM_RBUTTONDBLCLK, wParam, lParam);
	if (OS.GetCapture () != handle) OS.SetCapture (handle);
	return LRESULT.ZERO;
}

LRESULT WM_RBUTTONDOWN (int wParam, int lParam) {
	/*
	* Feature in Windows.  For some reason, capturing
	* the mouse after processing the mouse event for the
	* widget interferes with the normal mouse processing
	* for the widget.  The fix is to avoid the automatic
	* mouse capture.
	*/
	return sendMouseDownEvent (SWT.MouseDown, 3, OS.WM_RBUTTONDOWN, wParam, lParam);
}

LRESULT WM_SETFOCUS (int wParam, int lParam) {
	LRESULT result = super.WM_SETFOCUS (wParam, lParam);
	/*
	* Bug in Windows.  For some reason, the table does
	* not set the default focus rectangle to be the first
	* item in the table when it gets focus and there is
	* no selected item.  The fix to make the first item
	* be the focus item.
	*/
	int count = OS.SendMessage (handle, OS.LVM_GETITEMCOUNT, 0, 0);
	if (count == 0) return result;
	int index = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
	if (index == -1) {
		LVITEM lvItem = new LVITEM ();
		lvItem.mask = OS.LVIF_STATE;
		lvItem.state = OS.LVIS_FOCUSED;
		lvItem.stateMask = OS.LVIS_FOCUSED;
		ignoreSelect = true;
		OS.SendMessage (handle, OS.LVM_SETITEM, 0, lvItem);
		ignoreSelect = false;
	}
	return result;
}

LRESULT WM_SIZE (int wParam, int lParam) {
	if (ignoreResize) {
		ignoreResize = false;
		int code = callWindowProc (OS.WM_SIZE, wParam, lParam);
		return new LRESULT (code);
	}
	return super.WM_SIZE (wParam, lParam);
}

LRESULT WM_SYSCOLORCHANGE (int wParam, int lParam) {
	LRESULT result = super.WM_SYSCOLORCHANGE (wParam, lParam);
	if (result != null) return result;
	if ((style & SWT.CHECK) != 0) setCheckboxImageList (true);
	return result;
}

LRESULT wmNotifyChild (int wParam, int lParam) {
	NMHDR hdr = new NMHDR ();
	OS.MoveMemory (hdr, lParam, NMHDR.sizeof);
	switch (hdr.code) {
		case OS.NM_CUSTOMDRAW: {
			if (!customDraw) break;
			NMLVCUSTOMDRAW nmcd = new NMLVCUSTOMDRAW ();
			OS.MoveMemory (nmcd, lParam, NMLVCUSTOMDRAW.sizeof);
			switch (nmcd.dwDrawStage) {
				case OS.CDDS_PREPAINT: return new LRESULT (OS.CDRF_NOTIFYITEMDRAW);
				case OS.CDDS_ITEMPREPAINT: return new LRESULT (OS.CDRF_NOTIFYSUBITEMDRAW);
				case OS.CDDS_ITEMPREPAINT | OS.CDDS_SUBITEM: {
					TableItem item = items [nmcd.dwItemSpec];
					int clrText = item.foreground, clrTextBk = item.background;
					if (clrText == -1 && clrTextBk == -1) break;
					nmcd.clrText = clrText == -1 ? getForegroundPixel () : clrText;
					nmcd.clrTextBk = clrTextBk == -1 ? getBackgroundPixel () : clrTextBk;
					OS.MoveMemory (lParam, nmcd, nmcd.sizeof);
					return new LRESULT (OS.CDRF_NEWFONT);
				}
			}
			break;
		}
		case OS.LVN_MARQUEEBEGIN: return LRESULT.ONE;
		case OS.LVN_BEGINDRAG:
		case OS.LVN_BEGINRDRAG: {
			dragStarted = true;
			if (hdr.code == OS.LVN_BEGINDRAG) {
				postEvent (SWT.DragDetect);
			}
			break;
		}
		case OS.LVN_COLUMNCLICK: {
			NMLISTVIEW pnmlv = new NMLISTVIEW ();
			OS.MoveMemory(pnmlv, lParam, NMLISTVIEW.sizeof);
			TableColumn column = columns [pnmlv.iSubItem];
			if (column != null) {
				column.postEvent (SWT.Selection);
			}
			break;
		}
		case OS.LVN_ITEMACTIVATE: {
			NMLISTVIEW pnmlv = new NMLISTVIEW ();
			OS.MoveMemory(pnmlv, lParam, NMLISTVIEW.sizeof);
			if (pnmlv.iItem != -1) {
				Event event = new Event ();
				event.item = items [pnmlv.iItem];
				postEvent (SWT.DefaultSelection, event);
			}
			break;
		}
		case OS.LVN_ITEMCHANGED: {
			if (!ignoreSelect) {
				NMLISTVIEW pnmlv = new NMLISTVIEW ();
				OS.MoveMemory (pnmlv, lParam, NMLISTVIEW.sizeof);
				if (pnmlv.iItem != -1 && (pnmlv.uChanged & OS.LVIF_STATE) != 0) {
					int oldBits = pnmlv.uOldState & OS.LVIS_STATEIMAGEMASK;
					int newBits = pnmlv.uNewState & OS.LVIS_STATEIMAGEMASK;
					if (oldBits != newBits) {
						Event event = new Event();
						event.item = items [pnmlv.iItem];
						event.detail = SWT.CHECK;
						/*
						* This code is intentionally commented.
						*/
//						OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, pnmlv.iItem, 0);
						postEvent (SWT.Selection, event);
					} else {
						boolean isFocus = (pnmlv.uNewState & OS.LVIS_FOCUSED) != 0;
						int index = OS.SendMessage (handle, OS.LVM_GETNEXTITEM, -1, OS.LVNI_FOCUSED);
						if ((style & SWT.MULTI) != 0) {
							if (OS.GetKeyState (OS.VK_CONTROL) < 0) {
								if (!isFocus) {
									if (index == pnmlv.iItem) {
										boolean isSelected = (pnmlv.uNewState & OS.LVIS_SELECTED) != 0;
										boolean wasSelected = (pnmlv.uOldState & OS.LVIS_SELECTED) != 0;
										isFocus = isSelected != wasSelected;
									}
								} else {
									isFocus = mouseDown;
								}
							}
						}
						if (OS.GetKeyState (OS.VK_SPACE) < 0) isFocus = true;
						if (isFocus) {
							Event event = new Event();
							if (index != -1) {
								/*
								* This code is intentionally commented.
								*/
//								OS.SendMessage (handle, OS.LVM_ENSUREVISIBLE, index, 0);
								event.item = items [index];
							}
							postEvent (SWT.Selection, event);
						}
					}
				}
			}
			break;
		}
	}
	return super.wmNotifyChild (wParam, lParam);
}

LRESULT wmScroll (int msg, int wParam, int lParam) {
	int code = callWindowProc (msg, wParam, lParam);
	if (code == 0) return LRESULT.ZERO;
	return new LRESULT (code);
}

}
