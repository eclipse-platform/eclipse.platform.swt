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
 * Instances of this class are controls that allow the user
 * to choose an item from a list of items, or optionally 
 * enter a new value by typing it into an editable text
 * field. Often, <code>Combo</code>s are used in the same place
 * where a single selection <code>List</code> widget could
 * be used but space is limited. A <code>Combo</code> takes
 * less space than a <code>List</code> widget and shows
 * similar information.
 * <p>
 * Note: Since <code>Combo</code>s can contain both a list
 * and an editable text field, it is possible to confuse methods
 * which access one versus the other (compare for example,
 * <code>clearSelection()</code> and <code>deselectAll()</code>).
 * The API documentation is careful to indicate either "the
 * receiver's list" or the "the receiver's text field" to 
 * distinguish between the two cases.
 * </p><p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>DROP_DOWN, READ_ONLY, SIMPLE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Selection</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see List
 */
public class Combo extends Composite {

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
public Combo (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	/*
	* Feature in Windows.  It is not possible to create
	* a combo box that has a border using Windows style
	* bits.  All combo boxes draw their own border and
	* do not use the standard Windows border styles.
	* Therefore, no matter what style bits are specified,
	* clear the BORDER bits so that the SWT style will
	* match the Windows widget.
	*
	* The Windows behavior is currently implemented on
	* all platforms.
	*/
	style &= ~SWT.BORDER;
	
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
	style = checkBits (style, SWT.DROP_DOWN, SWT.SIMPLE, 0, 0, 0, 0);
	if ((style & SWT.SIMPLE) != 0) return style & ~SWT.READ_ONLY;
	return style;
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	//NOT DONE: this only works with a DROP_DOWN combo 
	if ((style & SWT.SIMPLE) != 0) return new Point(100, 100);
	PhDim_t dim = new PhDim_t();
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidgetFamily (handle);
	OS.PtWidgetPreferredSize(handle, dim);
	int width = dim.w;
	int height = dim.h;
	int textWidget = OS.PtWidgetChildBack(handle);
	OS.PtWidgetPreferredSize(textWidget, dim);
	height += dim.h;
	PhRect_t rect = new PhRect_t ();
	PhArea_t area = new PhArea_t ();
	OS.PtSetAreaFromWidgetCanvas (textWidget, rect, area);
	width += area.size_w;
	
	/* Calculate maximum text width */
	int [] args = new int [] {
		OS.Pt_ARG_LIST_ITEM_COUNT, 0, 0,
		OS.Pt_ARG_ITEMS, 0, 0,
		OS.Pt_ARG_TEXT_FONT, 0, 0,
		OS.Pt_ARG_TEXT_STRING, 0, 0,
	};
	OS.PtGetResources (handle, args.length / 3, args);
	int maxWidth = 0;
	rect = new PhRect_t();
	int str = args [10];
	int font = args [7];
	if (str != 0) {
		int length = OS.strlen (str);
		if (length > 0) {
			OS.PfExtentText(rect, null, font, str, length);
			maxWidth = Math.max(maxWidth, rect.lr_x - rect.ul_x + 1);
		}
	}
	int count = args [1];
	int [] buffer = new int [1];
	for (int i = 0; i < count; i++) {
		OS.memmove (buffer, args [4] + (i * 4), 4);
		str = buffer [0];
		int length = OS.strlen (str);
		if (length > 0) {
			OS.PfExtentText(rect, null, font, str, length);
			maxWidth = Math.max(maxWidth, rect.lr_x - rect.ul_x + 1);
		}
	}
	if (maxWidth == 0) maxWidth = DEFAULT_WIDTH;	
	
	width += maxWidth;
	
	if (wHint != SWT.DEFAULT || hHint != SWT.DEFAULT) {
		rect = new PhRect_t ();
		area = new PhArea_t ();
		rect.lr_x = (short) (wHint - 1);
		rect.lr_y = (short) (hHint - 1);
		OS.PtSetAreaFromWidgetCanvas (handle, rect, area);
		if (wHint != SWT.DEFAULT) width = area.size_w;
		if (hHint != SWT.DEFAULT) height = area.size_h;
	}
	return new Point(width, height);
}

void createHandle (int index) {
	state |= HANDLE;
	Display display = getDisplay ();
	int clazz = display.PtComboBox;
	int parentHandle = parent.handle;
	int textFlags = (style & SWT.READ_ONLY) != 0 ? 0 : OS.Pt_EDITABLE;
	int [] args = {
		OS.Pt_ARG_TEXT_FLAGS, textFlags, OS.Pt_EDITABLE,
		OS.Pt_ARG_CBOX_MAX_VISIBLE_COUNT, 5, 0,
		OS.Pt_ARG_CBOX_FLAGS, (style & SWT.SIMPLE) != 0 ? OS.Pt_COMBOBOX_STATIC: 0, OS.Pt_COMBOBOX_STATIC,
		OS.Pt_ARG_RESIZE_FLAGS, 0, OS.Pt_RESIZE_XY_BITS,
	};
	handle = OS.PtCreateWidget (clazz, parentHandle, args.length / 3, args);
	if (handle == 0) error (SWT.ERROR_NO_HANDLES);
}

/**
 * Deselects the item at the given zero-relative index in the receiver's 
 * list.  If the item at the index was already deselected, it remains
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
	int [] args = new int [] {OS.Pt_ARG_CBOX_SELECTION_ITEM, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] == index) {
		args = new int [] {
			OS.Pt_ARG_TEXT_STRING, 0, 0,
			OS.Pt_ARG_CBOX_SELECTION_ITEM, 0, 0
		};
		OS.PtSetResources (handle, args.length / 3, args);
	}
}

/**
 * Deselects all selected items in the receiver's list.
 * <p>
 * Note: To clear the selection in the receiver's text field,
 * use <code>clearSelection()</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #clearSelection
 */
public void deselectAll () {
	checkWidget();
	int [] args = new int [] {
		OS.Pt_ARG_TEXT_STRING, 0, 0,
		OS.Pt_ARG_CBOX_SELECTION_ITEM, 0, 0
	};
	OS.PtSetResources (handle, args.length / 3, args);
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

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is modified, by sending
 * it one of the messages defined in the <code>ModifyListener</code>
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
 * @see ModifyListener
 * @see #removeModifyListener
 */
public void addModifyListener (ModifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Modify, typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's selection changes, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the combo's list selection changes.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed the combo's text area.
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

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

/**
 * Sets the selection in the receiver's text field to an empty
 * selection starting just before the first character. If the
 * text field is editable, this has the effect of placing the
 * i-beam at the start of the text.
 * <p>
 * Note: To clear the selected items in the receiver's list, 
 * use <code>deselectAll()</code>.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #deselectAll
 */
public void clearSelection () {
	checkWidget();
	OS.PtTextSetSelection (handle, new int [] {0}, new int [] {0});
}

void deregister () {
	super.deregister ();
	int child = OS.PtWidgetChildBack (handle);
	WidgetTable.remove (child);
}

int focusHandle () {

	/*
	* Fetuare in Photon. The combo box does not receive
	* Pt_CB_GOT_FOCUS and Pt_CB_LOST_FOCUS callbacks itself.
	* Only the internal PtText receives them. The fix is to
	* add these callbacks in the internal PtText.
	*/
	return OS.PtWidgetChildBack (handle);
}

/**
 * Returns the item at the given, zero-relative index in the
 * receiver's list. Throws an exception if the index is out
 * of range.
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
 * Returns the number of items contained in the receiver's list.
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
 * display <em>one</em> of the items in the receiver's list.
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
	//NOT DONE - NOT NEEDED
	return 0;
}

/**
 * Returns an array of <code>String</code>s which are the items
 * in the receiver's list. 
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
 *    <li>ERROR_CANNOT_GET_ITEM - if the operation fails because of an operating system failure</li>
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

String getNameText () {
	return getText ();
}

/**
 * Returns a <code>Point</code> whose x coordinate is the start
 * of the selection in the receiver's text field, and whose y
 * coordinate is the end of the selection. The returned values
 * are zero-relative. An "empty" selection as indicated by
 * the the x and y coordinates having the same value.
 *
 * @return a point representing the selection start and end
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSelection () {
	checkWidget();
	if (((style & SWT.DROP_DOWN) != 0) && ((style & SWT.READ_ONLY) != 0)) {
		int [] args = {OS.Pt_ARG_TEXT_STRING, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		int length = 0;
		if (args [1] != 0) length = OS.strlen (args [1]);
		return new Point (0, length);
	}
//	if (textVerify != null) {
//		return new Point (textVerify.start_pos, textVerify.end_pos);
//	}
	int [] start = new int [1], end = new int [1];
	OS.PtTextGetSelection (handle, start, end);
	if (start [0] == -1) {
		int [] args = {OS.Pt_ARG_CURSOR_POSITION, 0, 0};
		OS.PtGetResources (handle, args.length / 3, args);
		start [0] = end [0] = args [1];	
	}
	return new Point (start [0], end [0]);
}

/**
 * Returns the zero-relative index of the item which is currently
 * selected in the receiver's list, or -1 if no item is selected.
 *
 * @return the index of the selected item
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionIndex () {
	checkWidget();
	int [] args = new int [] {OS.Pt_ARG_CBOX_SELECTION_ITEM, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] == 0) return -1;
	return args [1] - 1;
}

/**
 * Returns a string containing a copy of the contents of the
 * receiver's text field.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget();
	int [] args = {OS.Pt_ARG_TEXT_STRING, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	if (args [1] == 0) return "";
	int length = OS.strlen (args [1]);
	byte [] buffer = new byte [length];
	OS.memmove (buffer, args [1], length);
	char [] unicode = Converter.mbcsToWcs (null, buffer);
	return new String (unicode);
}

/**
 * Returns the height of the receivers's text field.
 *
 * @return the text height
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_CANNOT_GET_ITEM_HEIGHT - if the operation fails because of an operating system failure</li>
 * </ul>
 */
public int getTextHeight () {
	checkWidget();
	//NOT DONE - Only works for DROP_DOWN
	PhDim_t dim = new PhDim_t();
	if (!OS.PtWidgetIsRealized (handle)) OS.PtExtentWidgetFamily (handle);
	OS.PtWidgetPreferredSize(handle, dim);
	int height = dim.h;
	int text = OS.PtWidgetChildBack(handle);
	OS.PtWidgetPreferredSize(text, dim);
	height += dim.h;
	return height;
}

/**
 * Returns the maximum number of characters that the receiver's
 * text field is capable of holding. If this has not been changed
 * by <code>setTextLimit()</code>, it will be the constant
 * <code>Combo.LIMIT</code>.
 * 
 * @return the text limit
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTextLimit () {
	checkWidget();
	int [] args = new int [] {OS.Pt_ARG_MAX_LENGTH, 0, 0};
	OS.PtGetResources (handle, args.length / 3, args);
	return args [1];
}

boolean hasFocus () {
	return OS.PtIsFocused (handle) != 0;
}

void hookEvents () {
	super.hookEvents ();
	int windowProc = getDisplay ().windowProc;
	OS.PtAddCallback (handle, OS.Pt_CB_SELECTION, windowProc, SWT.Selection);
	OS.PtAddCallback (handle, OS.Pt_CB_TEXT_CHANGED, windowProc, SWT.Modify);
}

/**
 * Searches the receiver's list starting at the first item
 * (index 0) until an item is found that is equal to the 
 * argument, and returns the index of that item. If no item
 * is found, returns -1.
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
 */
public int indexOf (String string, int start) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	
	// NOT DONE - start is ignored
	return indexOf (string);
}

int processModify (int info) {
	sendEvent (SWT.Modify);
	return OS.Pt_CONTINUE;
}

int processPaint (int damage) {
	OS.PtSuperClassDraw (OS.PtComboBox (), handle, damage);
	sendPaintEvent (damage);
	return OS.Pt_CONTINUE;
}

int processSelection (int info) {
	if (info == 0) return OS.Pt_CONTINUE;
	PtCallbackInfo_t cbinfo = new PtCallbackInfo_t ();
	OS.memmove (cbinfo, info, PtCallbackInfo_t.sizeof);
	if (cbinfo.reason_subtype == OS.Pt_LIST_SELECTION_FINAL) {
		postEvent(SWT.Selection);
	}
	return OS.Pt_CONTINUE;
}

void register () {
	super.register ();
	int child = OS.PtWidgetChildBack (handle);
	WidgetTable.put (child, this);
}

/**
 * Removes the items from the receiver's list which are
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
 * Removes the item from the receiver's list at the given
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
 * Removes all of the items from the receiver's list.
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
 * be notified when the receiver's text is modified.
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
 * @see ModifyListener
 * @see #addModifyListener
 */
public void removeModifyListener (ModifyListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Modify, listener);	
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
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
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
	int [] args = new int [] {OS.Pt_ARG_CBOX_SELECTION_ITEM, index + 1, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

void setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	checkWidget();
	int newHeight = ((style & SWT.DROP_DOWN) != 0) ? getTextHeight() : height;
	super.setBounds (x, y, width, newHeight, move, resize);
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
 * Sets the receiver's list to be the given array of items.
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
	int [] args = {OS.Pt_ARG_TEXT_STRING, 0, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

/**
 * Sets the selection in the receiver's text field to the
 * range specified by the argument whose x coordinate is the
 * start of the selection and whose y coordinate is the end
 * of the selection. 
 *
 * @param a point representing the new selection start and end
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (Point selection) {
	checkWidget();
	if (selection == null) error (SWT.ERROR_NULL_ARGUMENT);
	OS.PtTextSetSelection (handle, new int [] {selection.x}, new int [] {selection.y});
}

/**
 * Sets the contents of the receiver's text field to the
 * given string.
 * <p>
 * Note: The text field in a <code>Combo</code> is typically
 * only capable of displaying a single line of text. Thus,
 * setting the text to a string containing line breaks or
 * other special characters will probably cause it to 
 * display incorrectly.
 * </p>
 *
 * @param text the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	if ((style & SWT.READ_ONLY) != 0) {
		int index = OS.PtListItemPos(handle, buffer);
		if (index > 0) {
			int [] args = new int [] {OS.Pt_ARG_CBOX_SELECTION_ITEM, index, 0};
			OS.PtSetResources (handle, args.length / 3, args);
//			sendEvent (SWT.Modify);
		}
		return;
	}
	int ptr = OS.malloc (buffer.length);
	OS.memmove (ptr, buffer, buffer.length);
	int [] args = {OS.Pt_ARG_TEXT_STRING, ptr, 0};
	OS.PtSetResources (handle, args.length / 3, args);
	OS.free (ptr);
}

/**
 * Sets the maximum number of characters that the receiver's
 * text field is capable of holding to be the argument.
 *
 * @param limit new text limit
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTextLimit (int limit) {
	checkWidget();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	int [] args = new int [] {OS.Pt_ARG_MAX_LENGTH, limit, 0};
	OS.PtSetResources (handle, args.length / 3, args);
}

int traversalCode (int key_sym, PhKeyEvent_t ke) {
	int code = super.traversalCode (key_sym, ke);
	if (key_sym == OS.Pk_Up || key_sym == OS.Pk_Down) {
		code &= ~(SWT.TRAVERSE_ARROW_NEXT | SWT.TRAVERSE_ARROW_PREVIOUS);
	}
	if ((style & SWT.READ_ONLY) == 0) {
		if (key_sym == OS.Pk_Up || key_sym == OS.Pk_Down) {
			code &= ~(SWT.TRAVERSE_ARROW_NEXT | SWT.TRAVERSE_ARROW_PREVIOUS);
		}
	}
	return code;
}

boolean translateTraversal (int key_sym, PhKeyEvent_t phEvent) {
	boolean translated = super.translateTraversal (key_sym, phEvent);
	if (!translated && key_sym == OS.Pk_Return) {
		postEvent (SWT.DefaultSelection);
		return false;
	}
	return translated;
}

}
