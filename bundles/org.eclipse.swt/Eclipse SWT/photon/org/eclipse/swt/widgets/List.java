package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/** 
 * Instances of this class represent a selectable user interface
 * object that displays a list of strings and issues notificiation
 * when a string selected.  A list may be single or multi select.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SINGLE, MULTI</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection, DefaultSelection</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 */
public class List extends Scrollable {

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
public List (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	return checkBits (style, SWT.SINGLE, SWT.MULTI, 0, 0, 0, 0);
}

/**
 * Adds the argument to the end of the receiver's list.
 *
 * @param string the new item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
 * </ul>
 *
 * @see #add(String,int)
 */
public void add (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int ptr = OS.malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	OS.PtListAddItems (handle, new int [] {ptr}, 1, 0);
	OS.free (ptr);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the selection changes.
 * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

/**
 * Adds the argument to the receiver's list at the given
 * zero-relative index.
 * <p>
 * Note: To add an item at the end of the list, use the
 * result of calling <code>getItemCount()</code> as the
 * index or use <code>add(String)</code>.
 * </p>
 *
 * @param string the new item
 * @param index the index for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
 * </ul>
 *
 * @see #add(String)
 */
public void add (String string, int index) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (index == -1) error (SWT.ERROR_INVALID_RANGE);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int ptr = OS.malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	int result = OS.PtListAddItems (handle, new int [] {ptr}, 1, index + 1);
	OS.free (ptr);
	if (result != 0) {
		int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		if (0 <= index && index <= args [1]) error (SWT.ERROR_ITEM_NOT_ADDED);
		error (SWT.ERROR_INVALID_RANGE);
	}
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();

	/**
	 * Feature in Photon - The preferred width calculated by
	 * PtWidgetPreferredSize is the current width.
	 */
	PhDim_t dim = new PhDim_t();
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidget (handle);
	OS.PtWidgetPreferredSize(handle, dim);
	int width = 0, height = dim.h;
	int [] args = new int [] {
		OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0,
		OS.Pt_ARG_ITEMS, 0, 0,
		OS.Pt_ARG_LIST_FONT, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	PhRect_t rect = new PhRect_t();
	int font = args [7];
	int [] buffer = new int [1];
	for (int i = 0; i < args [1]; i++) {
		OS.memmove (buffer, args [4] + (i * 4), 4);
		int str = buffer [0];
		int length = OS.strlen (str);
		if (length > 0) {
			OS.PfExtentText(rect, null, font, str, length);
			width = Math.max(width, rect.lr_x - rect.ul_x + 1);
		}
	}
	rect = new PhRect_t ();
	PhArea_t area = new PhArea_t ();
	rect.lr_x = (short) (width + 1);
	OS.PtSetAreaFromWidgetCanvas (handle, rect, area);
	width = area.size_w;
	if (wHint != SWT.DEFAULT || hHint != SWT.DEFAULT) {
		rect = new PhRect_t ();
		area = new PhArea_t ();
		rect.lr_x = (short) (wHint - 1);
		rect.lr_y = (short) (hHint - 1);
		OS.PtSetAreaFromWidgetCanvas (handle, rect, area);
		ScrollBar scroll;
		if (wHint != SWT.DEFAULT) {
			width = area.size_w;
			if ((scroll = getVerticalBar()) != null)
				width += scroll.getSize().x;
		}
		if (hHint != SWT.DEFAULT) {
			height = area.size_h;
			if ((scroll = getHorizontalBar()) != null)
				height += scroll.getSize().y;
		}
	}
	return new Point(width, height);
}

void createHandle (int index) {
	state |= HANDLE;
	Display display = getDisplay ();
	int clazz = display.PtList;
	int parentHandle = parent.handle;
	int mode = OS.Pt_SELECTION_MODE_SINGLE | OS.Pt_SELECTION_MODE_AUTO;
	if ((style & SWT.MULTI) != 0) {
		if ((style & SWT.SIMPLE) != 0) {
			mode = OS.Pt_SELECTION_MODE_MULTIPLE | OS.Pt_SELECTION_MODE_NOCLEAR
				| OS.Pt_SELECTION_MODE_TOGGLE | OS.Pt_SELECTION_MODE_NOMOVE;
		} else {
			mode = OS.Pt_SELECTION_MODE_MULTIPLE | OS.Pt_SELECTION_MODE_AUTO;
		}
	}
	mode |= OS.Pt_SELECTION_MODE_NOFOCUS;
	boolean hasBorder = (style & SWT.BORDER) != 0;
	int [] args = {
		OS.Pt_ARG_FLAGS, hasBorder ? OS.Pt_HIGHLIGHTED : 0, OS.Pt_HIGHLIGHTED,
		OS.Pt_ARG_SELECTION_MODE, mode, 0,
		OS.Pt_ARG_FLAGS, OS.Pt_SELECTABLE | OS.Pt_SELECT_NOREDRAW, OS.Pt_SELECTABLE | OS.Pt_SELECT_NOREDRAW,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (clazz, parentHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);	
	createStandardScrollBars ();
}

int defaultBackground () {
	Display display = getDisplay ();
	return display.LIST_BACKGROUND;
}

byte [] defaultFont () {
	Display display = getDisplay ();
	return display.LIST_FONT;
}

int defaultForeground () {
	Display display = getDisplay ();
	return display.LIST_FOREGROUND;
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
	checkWidget();
	if (index < 0) return;
	OS.PtListUnselectPos (handle, index + 1);
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
	checkWidget();
	if (start > end) return;
	if ((style & SWT.SINGLE) != 0) {
		int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		int count = args [1];
		int index = Math.min (count - 1, end);
		if (index >= start) deselect (index);
		return;
	}
	for (int i=start; i<=end; i++) {
		OS.PtListUnselectPos (handle, i + 1);
	}
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
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (indices.length == 0) return;
	for (int i=0; i<indices.length; i++) {
		int index = indices [i];
		if (index != -1) {
			OS.PtListUnselectPos (handle, index + 1);
		}
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
	checkWidget();
	int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int count = args [1];
	for (int i=0; i<count; i++) {
		OS.PtListUnselectPos (handle, i + 1);
	}
}

/**
 * Returns the zero-relative index of the item which is currently
 * has the focus in the receiver, or -1 if no item is has focus.
 *
 * @return the index of the selected item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getFocusIndex () {
	return getSelectionIndex ();
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
public String getItem (int index) {
	checkWidget();
	int [] args = new int [] {
		OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0,
		OS.Pt_ARG_ITEMS, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	if (!(0 <= index && index < args [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int [] items = new int [1];
	OS.memmove (items, args [4] + (index * 4), 4);
	int length = OS.strlen (items [0]);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, items [0], length);
	char [] unicode = Converter.mbcsToWcs (null, buffer);
	return new String (unicode);
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
	checkWidget();
	int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}	

/**
 * Returns the height of the area which would be used to
 * display <em>one</em> of the items in the tree.
 *
 * @return the height of one item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM_HEIGHT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getItemHeight () {
	checkWidget();
	int [] args = new int [] {
		OS.Pt_ARG_LIST_TOTAL_HEIGHT, 0, 0,
		OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0,
		OS.Pt_ARG_LIST_FONT, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [4] == 0) {
		int ptr = OS.malloc(1);
		PhRect_t rect = new PhRect_t ();
		OS.PfExtentText(rect, null, args [7], ptr, 1);
		OS.free(ptr);
		int inset = 4;
		return inset + (rect.lr_y - rect.ul_y + 1);
	}
	return args [1] / args [4];
}

/**
 * Returns an array of <code>String</code>s which are the items
 * in the receiver. 
 * <p>
 * Note: This is not the actual structure used by the receiver
 * to maintain its list of items, so modifying the array will
 * not affect the receiver. 
 * </p>
 *
 * @return the items in the receiver's list
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure while getting an item</li>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure while getting the item count</li>
 * </ul>
 */
public String [] getItems () {
	checkWidget();
	int [] args = new int [] {
		OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0,
		OS.Pt_ARG_ITEMS, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	int [] items = new int [args [1]];
	OS.memmove (items, args [4], args [1] * 4);
	String [] result = new String [args [1]];
	for (int i=0; i<args [1]; i++) {
		int length = OS.strlen (items [i]);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, items [i], length);
		char [] unicode = Converter.mbcsToWcs (null, buffer);
		result [i] = new String (unicode);
	}
	return result;

}

/**
 * Returns an array of <code>String</code>s that are currently
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_SELECTION - if the operation fails because of an operating system failure while getting the selection</li>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure while getting an item</li>
 * </ul>
 */
public String [] getSelection () {
	checkWidget();
	int [] indices = getSelectionIndices ();
	String [] result = new String [indices.length];
	for (int i=0; i<indices.length; i++) {
		result [i] = getItem (indices [i]);
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getSelectionCount () {
	checkWidget();
	int [] args = new int [] {OS.Pt_ARG_LIST_SEL_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_SELECTION - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getSelectionIndex () {
	checkWidget();
	int [] args = new int [] {
		OS.Pt_ARG_LIST_SEL_COUNT, 0, 0,
		OS.Pt_ARG_SELECTION_INDEXES, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] == 0) return -1;
	short [] buffer = new short [1];
	OS.memmove (buffer, args [4], 2);
	return buffer [0] - 1;
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
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_SELECTION - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int [] getSelectionIndices () {
	checkWidget();
	int [] args = new int [] {
		OS.Pt_ARG_LIST_SEL_COUNT, 0, 0,
		OS.Pt_ARG_SELECTION_INDEXES, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	short [] indices = new short [args [1]];
	OS.memmove (indices, args [4], args [1] * 2);
	int [] result = new int [args [1]];
	for (int i=0; i<args [1]; i++) {
		result [i] = indices [i] - 1;
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
	checkWidget();
	int [] args = new int [] {OS.Pt_ARG_TOP_ITEM_POS, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1] - 1;
}

void hookEvents () {
	super.hookEvents ();
	int windowProc = getDisplay ().windowProc;
	OS.PtAddCallback (handle, OS.Pt_CB_SELECTION, windowProc, SWT.Selection);
	OS.PtAddCallback (handle, OS.Pt_CB_ACTIVATE, windowProc, SWT.DefaultSelection);
}

/**
 * Gets the index of an item.
 * <p>
 * The list is searched starting at 0 until an
 * item is found that is equal to the search item.
 * If no item is found, -1 is returned.  Indexing
 * is zero based.
 *
 * @param string the search item
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
public int indexOf (String string) {
	checkWidget();
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	return OS.PtListItemPos(handle, buffer) - 1;
}

/**
 * Searches the receiver's list starting at the given, 
 * zero-relative index until an item is found that is equal
 * to the argument, and returns the index of that item. If
 * no item is found or the starting index is out of range,
 * returns -1.
 *
 * @param string the search item
 * @return the index of the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_COUNT - if the operation fails because of an operating system failure while getting the item count</li>
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure while getting an item</li>
 * </ul>
 */
public int indexOf (String string, int start) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	
	// NOT DONE - start is ignored
	return indexOf (string);
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
	checkWidget();
	int [] args = new int [] {
		OS.Pt_ARG_LIST_SEL_COUNT, 0, 0,
		OS.Pt_ARG_SELECTION_INDEXES, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	short [] buffer = new short [1];
	for (int i=0; i<args [1]; i++) {
		OS.memmove (buffer, args [4] + (i * 2), 2);
		if (buffer [0] == index + 1) return true;
	}
	return false;
}

int processDefaultSelection (int info) {
	if (info == 0) return OS.Pt_END;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.cbdata == 0) return OS.Pt_END;
	int[] click_count = new int [1];
	OS.memmove(click_count, cbinfo.cbdata, 4);
	if (click_count [0] > 1) postEvent (SWT.DefaultSelection);
	return OS.Pt_CONTINUE;
}

int processPaint (int damage) {
	OS.PtSuperClassDraw (OS.PtList (), handle, damage);
	return super.processPaint (damage);
}

int processSelection (int info) {
	postEvent (SWT.Selection);
	return OS.Pt_CONTINUE;
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
	checkWidget();
	int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (!(0 <= index && index < args [1])) error (SWT.ERROR_INVALID_RANGE);
	int result = OS.PtListDeleteItemPos (handle, 1, index + 1);
	if (result != 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
}

/**
 * Searches the receiver's list starting at the first item
 * until an item is found that is equal to the argument, 
 * and removes that item from the list.
 *
 * @param string the item to remove
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the string is not found in the list</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void remove (String string) {
	checkWidget();
	int index = indexOf (string, 0);
	if (index == -1) error (SWT.ERROR_ITEM_NOT_REMOVED);
	remove (index);
}

/**
 * Removes the items from the receiver at the given
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
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	int [] newIndices = new int [indices.length];
	System.arraycopy (indices, 0, newIndices, 0, indices.length);
	sort (newIndices);
	int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int count = args [1];
	for (int i=0; i<newIndices.length; i++ ) {
		int index = newIndices [i];
		if (!(0 <= index && index < count)) error (SWT.ERROR_INVALID_RANGE);
		int result = OS.PtListDeleteItemPos (handle, 1, index + 1);
		if (result != 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
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
	checkWidget();
	int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (!(0 < start && start <= end && end < args [1])) {
		 error (SWT.ERROR_INVALID_RANGE);
	}
	int count = end - start + 1;
	int result = OS.PtListDeleteItemPos (handle, count, start + 1);
	if (result != 0) error (SWT.ERROR_ITEM_NOT_REMOVED);
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
	checkWidget();
	OS.PtListDeleteAllItems (handle);
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
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
	checkWidget();
	if (start > end) return;
	int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int count = args [1];
	if ((style & SWT.SINGLE) != 0) {
		int index = Math.min (count - 1, end);
		if (index >= start) select (index);
		return;
	}
	int gotoIndex = -1;
	for (int index=end; index>=start; index--) {
		if (0 <= index && index < count) {
			gotoIndex = index;
			OS.PtListSelectPos (handle, index + 1);
		}
	}
	if (gotoIndex != -1) OS.PtListGotoPos (handle, gotoIndex + 1);
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
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void select (int [] indices) {
	checkWidget();
	if (indices == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (indices.length == 0) return;
	int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int count = args [1];
	int gotoIndex = -1;
	for (int i=0; i<indices.length; i++) {
		int index = indices [i];
		if (0 <= index && index < count) {
			gotoIndex = index;
			OS.PtListSelectPos (handle, index + 1);
		}
	}
	if (gotoIndex != -1) OS.PtListGotoPos (handle, gotoIndex + 1);
}

/**
 * Selects the item at the given zero-relative index in the receiver's 
 * list.  If the item at the index was already selected, it remains
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
	checkWidget();
	if (index < 0) return;
	int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (index < args [1]) {
		OS.PtListSelectPos (handle, index + 1);
		OS.PtListGotoPos (handle, index + 1);
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
	checkWidget();
	if ((style & SWT.SINGLE) != 0) return;
	int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	int count = args [1];
	for (int i=0; i<count; i++) {
		OS.PtListSelectPos (handle, i + 1);
	}
}

/**
 * Sets the text of the item in the receiver's list at the given
 * zero-relative index to the string argument. This is equivalent
 * to <code>remove</code>'ing the old item at the index, and then
 * <code>add</code>'ing the new item at that index.
 *
 * @param index the index for the item
 * @param string the new text for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_REMOVED - if the remove operation fails because of an operating system failure</li>
 *    <li>ERROR_ITEM_NOT_ADDED - if the add operation fails because of an operating system failure</li>
 * </ul>
 */
public void setItem (int index, String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int [] args = new int [] {OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (!(0 <= index && index < args [1])) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int ptr = OS.malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	OS.PtListReplaceItemPos (handle, new int [] {ptr}, 1, index + 1);
	OS.free (ptr);
}

/**
 * Sets the receiver's items to be the given array of items.
 *
 * @param items the array of items
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_ITEM_NOT_ADDED - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public void setItems (String [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	OS.PtListDeleteAllItems (handle);
	int[] itemsPtr = new int [items.length];
	for (int i=0; i<itemsPtr.length; i++) {
		byte [] buffer = Converter.wcsToMbcs (null, items [i], true);
		int ptr = OS.malloc (buffer.length);
		OS.memmove (ptr, buffer, buffer.length);
		itemsPtr [i] = ptr;
	}
	OS.PtListAddItems (handle, itemsPtr, itemsPtr.length, 0);
	for (int i=0; i<itemsPtr.length; i++) {
		OS.free (itemsPtr [i]);
	}
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
	if ((style & SWT.MULTI) != 0) deselectAll ();
	select (start, end);
}

/**
 * Selects the item at the given zero-relative index in the receiver. 
 * If the item at the index was already selected, it remains selected.
 * The current selected is first cleared, then the new items are selected.
 * Indices that are out of range are ignored.
 *
 * @param index the index of the item to select
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @see List#deselectAll()
 * @see List#select(int)
 */
public void setSelection (int index) {
	if ((style & SWT.MULTI) != 0) deselectAll ();
	select (index);
}

/**
 * Selects the items at the given zero-relative indices in the receiver. 
 * The current selection is first cleared, then the new items are selected.
 *
 * @param indices the indices of the items to select
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see List#deselectAll()
 * @see List#select(int[])
 */
public void setSelection(int[] indices) {
	if ((style & SWT.MULTI) != 0) deselectAll ();
	select (indices);
}

/**
 * Sets the receiver's selection to be the given array of items.
 * The current selected is first cleared, then the new items are
 * selected.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see List#deselectAll()
 * @see List#select(int)
 */
public void setSelection (String [] items) {
	checkWidget();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.MULTI) != 0) deselectAll ();
	for (int i=items.length-1; i>=0; --i) {
		int index = 0;
		String string = items [i];
		if (string != null) {
			while ((index = indexOf (string, index)) != -1) {
				select (index);
				if (((style & SWT.SINGLE) != 0) && isSelected (index)) return;
				index++;
			}
		}
	}
	if ((style & SWT.SINGLE) != 0) deselectAll ();
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
	checkWidget();
	int [] args = new int [] {OS.Pt_ARG_TOP_ITEM_POS, index + 1, 0};
	OS.PtSetResources (handle, args.length / 3, args);
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
 */
public void showSelection () {
	checkWidget();
	int [] args = new int [] {
		OS.Pt_ARG_LIST_SEL_COUNT, 0, 0,
		OS.Pt_ARG_SELECTION_INDEXES, 0, 0,
		OS.Pt_ARG_TOP_ITEM_POS, 0, 0,
		OS.Pt_ARG_VISIBLE_COUNT, 0, 0,
		OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0
	};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] == 0) return;
	short [] buffer = new short [1];
	OS.memmove (buffer, args [4], 2);
	int index = buffer [0] - 1;
	int topIndex = args [7] - 1, visibleCount = args [10], count = args [13];
	int bottomIndex = Math.min (topIndex + visibleCount - 1, count);
	if ((topIndex <= index) && (index <= bottomIndex)) return;
	int lastIndex = Math.max (1, count - visibleCount + 1);
	int newTop = Math.min (Math.max (index - (visibleCount / 2), 1), lastIndex);
	args = new int [] {OS.Pt_ARG_TOP_ITEM_POS, newTop, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

}
