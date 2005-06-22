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
 * Note: Only one of the styles DROP_DOWN and SIMPLE may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see List
 */

public class Combo extends Composite {
	boolean noSelection, ignoreModify, ignoreCharacter;
	int visibleCount = 5;
	
	/**
	 * the operating system limit for the number of characters
	 * that the text field in an instance of this class can hold
	 */
	public static final int LIMIT;
	
	/*
	 * These values can be different on different platforms.
	 * Therefore they are not initialized in the declaration
	 * to stop the compiler from inlining.
	 */
	static {
		LIMIT = OS.IsWinNT ? 0x7FFFFFFF : 0x7FFF;	
	}
	
	/*
	 * These are the undocumented control id's for the children of
	 * a combo box.  Since there are no constants for these values,
	 * they may change with different versions of Windows (but have
	 * been the same since Windows 3.0).
	 */
	static final int CBID_LIST = 1000;
	static final int CBID_EDIT = 1001;
	static int EditProc, ListProc;
	
	static final int ComboProc;
	static final TCHAR ComboClass = new TCHAR (0, "COMBOBOX", true);
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, ComboClass, lpWndClass);
		ComboProc = lpWndClass.lpfnWndProc;
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
 * @see SWT#DROP_DOWN
 * @see SWT#READ_ONLY
 * @see SWT#SIMPLE
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Combo (Composite parent, int style) {
	super (parent, checkStyle (style));
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
 *
 * @see #add(String,int)
 */
public void add (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	int result = OS.SendMessage (handle, OS.CB_ADDSTRING, 0, buffer);
	if (result == OS.CB_ERR) error (SWT.ERROR_ITEM_NOT_ADDED);
	if (result == OS.CB_ERRSPACE) error (SWT.ERROR_ITEM_NOT_ADDED);
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
 *
 * @see #add(String)
 */
public void add (String string, int index) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int count = OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
	if (!(0 <= index && index <= count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	int result = OS.SendMessage (handle, OS.CB_INSERTSTRING, index, buffer);
	if (result == OS.CB_ERRSPACE || result == OS.CB_ERR) {
		error (SWT.ERROR_ITEM_NOT_ADDED);
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
	checkWidget ();
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver's text is verified, by sending
 * it one of the messages defined in the <code>VerifyListener</code>
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
 * @see VerifyListener
 * @see #removeVerifyListener
 * 
 * @since 3.1
 */
public void addVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Verify, typedListener);
}

int callWindowProc (int hwnd, int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	if (hwnd == handle) {
		return OS.CallWindowProc (ComboProc, hwnd, msg, wParam, lParam);
	}
	int hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwnd == hwndText) {
		return OS.CallWindowProc (EditProc, hwnd, msg, wParam, lParam);
	}
	int hwndList = OS.GetDlgItem (handle, CBID_LIST);
	if (hwnd == hwndList) {
		return OS.CallWindowProc (ListProc, hwnd, msg, wParam, lParam);
	}
	return OS.DefWindowProc (hwnd, msg, wParam, lParam);
}

boolean checkHandle (int hwnd) {
	return hwnd == handle || hwnd == OS.GetDlgItem (handle, CBID_EDIT) || hwnd == OS.GetDlgItem (handle, CBID_LIST);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
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
	checkWidget ();
	OS.SendMessage (handle, OS.CB_SETEDITSEL, 0, -1);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	if (wHint == SWT.DEFAULT) {
		int newFont, oldFont = 0;
		int hDC = OS.GetDC (handle);
		newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
		int count = OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
		RECT rect = new RECT ();
		int flags = OS.DT_CALCRECT | OS.DT_NOPREFIX;
		if ((style & SWT.READ_ONLY) == 0) flags |= OS.DT_EDITCONTROL;
		int length = OS.GetWindowTextLength (handle);
		int cp = getCodePage ();
		TCHAR buffer = new TCHAR (cp, length + 1);
		OS.GetWindowText (handle, buffer, length + 1);
		OS.DrawText (hDC, buffer, length, rect, flags);
		width = Math.max (width, rect.right - rect.left);
		for (int i=0; i<count; i++) {
			length = OS.SendMessage (handle, OS.CB_GETLBTEXTLEN, i, 0);
			if (length != OS.CB_ERR) {
				if (length + 1 > buffer.length ()) buffer = new TCHAR (cp, length + 1);
				int result = OS.SendMessage (handle, OS.CB_GETLBTEXT, i, buffer);
				if (result != OS.CB_ERR) {
					OS.DrawText (hDC, buffer, length, rect, flags);
					width = Math.max (width, rect.right - rect.left);
				}
			}
		}
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (handle, hDC);
	}
	if (hHint == SWT.DEFAULT) {
		if ((style & SWT.SIMPLE) != 0) {
			int count = OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
			int itemHeight = OS.SendMessage (handle, OS.CB_GETITEMHEIGHT, 0, 0);
			height = count * itemHeight;
		}
	}
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	if ((style & SWT.READ_ONLY) != 0) {
		width += 8;
	} else {
		int hwndText = OS.GetDlgItem (handle, CBID_EDIT);
		if (hwndText != 0) {
			int margins = OS.SendMessage (hwndText, OS.EM_GETMARGINS, 0, 0);
			int marginWidth = (margins & 0xFFFF) + ((margins >> 16) & 0xFFFF);
			width += marginWidth + 3;
		}
	}
	COMBOBOXINFO pcbi = new COMBOBOXINFO ();
	pcbi.cbSize = COMBOBOXINFO.sizeof;
	if (((style & SWT.SIMPLE) == 0) && !OS.IsWinCE && OS.GetComboBoxInfo (handle, pcbi)) {
		width += pcbi.itemLeft + (pcbi.buttonRight - pcbi.buttonLeft);
		height = (pcbi.buttonBottom - pcbi.buttonTop) + pcbi.buttonTop * 2; 
	} else {
		int border = OS.GetSystemMetrics (OS.SM_CXEDGE);
		width += OS.GetSystemMetrics (OS.SM_CXVSCROLL) + border * 2;		
		int textHeight = OS.SendMessage (handle, OS.CB_GETITEMHEIGHT, -1, 0);
		if ((style & SWT.DROP_DOWN) != 0) {
			height = textHeight + 6;
		} else {
			height += textHeight + 10;
		}
	}
	return new Point (width, height);
}

/**
 * Copies the selected text.
 * <p>
 * The current selection is copied to the clipboard.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public void copy () {
	checkWidget ();
	OS.SendMessage (handle, OS.WM_COPY, 0, 0);
}

void createHandle () {
	super.createHandle ();
	state &= ~CANVAS;

	/* Get the text and list window procs */
	int hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText != 0 && EditProc == 0) {
		EditProc = OS.GetWindowLong (hwndText, OS.GWL_WNDPROC);
	}
	int hwndList = OS.GetDlgItem (handle, CBID_LIST);
	if (hwndList != 0 && ListProc == 0) {
		ListProc = OS.GetWindowLong (hwndList, OS.GWL_WNDPROC);
	}

	/*
	* Bug in Windows.  If the combo box has the CBS_SIMPLE style,
	* the list portion of the combo box is not drawn correctly the
	* first time, causing pixel corruption.  The fix is to ensure
	* that the combo box has been resized more than once.
	*/
	if ((style & SWT.SIMPLE) != 0) {
		int flags = OS.SWP_NOZORDER | OS.SWP_DRAWFRAME | OS.SWP_NOACTIVATE;
		SetWindowPos (handle, 0, 0, 0, 0x3FFF, 0x3FFF, flags);
		SetWindowPos (handle, 0, 0, 0, 0, 0, flags);
	}
}

/**
 * Cuts the selected text.
 * <p>
 * The current selection is first copied to the
 * clipboard and then deleted from the widget.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public void cut () {
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
	OS.SendMessage (handle, OS.WM_CUT, 0, 0);
}

int defaultBackground () {
	return OS.GetSysColor (OS.COLOR_WINDOW);
}

void deregister () {
	super.deregister ();
	int hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText != 0) display.removeControl (hwndText);
	int hwndList = OS.GetDlgItem (handle, CBID_LIST);
	if (hwndList != 0) display.removeControl (hwndList);
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
	checkWidget ();
	int selection = OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
	if (index != selection) return;
	OS.SendMessage (handle, OS.CB_SETCURSEL, -1, 0);
	sendEvent (SWT.Modify);
	// widget could be disposed at this point
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
	checkWidget ();
	OS.SendMessage (handle, OS.CB_SETCURSEL, -1, 0);
	sendEvent (SWT.Modify);
	// widget could be disposed at this point
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
 */
public String getItem (int index) {
	checkWidget ();
	int length = OS.SendMessage (handle, OS.CB_GETLBTEXTLEN, index, 0);
	if (length != OS.CB_ERR) {
		TCHAR buffer = new TCHAR (getCodePage (), length + 1);
		int result = OS.SendMessage (handle, OS.CB_GETLBTEXT, index, buffer);
		if (result != OS.CB_ERR) return buffer.toString (0, length);
	}
	int count = OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
	if (0 <= index && index < count) error (SWT.ERROR_CANNOT_GET_ITEM);
	error (SWT.ERROR_INVALID_RANGE);
	return "";
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
 */
public int getItemCount () {
	checkWidget ();
	int count = OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
	if (count == OS.CB_ERR) error (SWT.ERROR_CANNOT_GET_COUNT);
	return count;
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
 */
public int getItemHeight () {
	checkWidget ();
	int result = OS.SendMessage (handle, OS.CB_GETITEMHEIGHT, 0, 0);
	if (result == OS.CB_ERR) error (SWT.ERROR_CANNOT_GET_ITEM_HEIGHT);
	return result;
}

/**
 * Returns a (possibly empty) array of <code>String</code>s which are
 * the items in the receiver's list. 
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
 */
public String [] getItems () {
	checkWidget ();
	int count = getItemCount ();
	String [] result = new String [count];
	for (int i=0; i<count; i++) result [i] = getItem (i);
	return result;
}

String getNameText () {
	return getText ();
}

/**
 * Returns the orientation of the receiver.
 *
 * @return the orientation style
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public int getOrientation () {
	checkWidget();
	return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
}

/**
 * Returns a <code>Point</code> whose x coordinate is the
 * character position representing the start of the selection
 * in the receiver's text field, and whose y coordinate is the
 * character position representing the end of the selection.
 * An "empty" selection is indicated by the x and y coordinates
 * having the same value.
 * <p>
 * Indexing is zero based.  The range of a selection is from
 * 0..N where N is the number of characters in the widget.
 * </p>
 *
 * @return a point representing the selection start and end
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Point getSelection () {
	checkWidget ();
	if ((style & SWT.DROP_DOWN) != 0 && (style & SWT.READ_ONLY) != 0) {
		return new Point (0, OS.GetWindowTextLength (handle));
	}
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.CB_GETEDITSEL, start, end);
	if (!OS.IsUnicode && OS.IsDBLocale) {
		start [0] = mbcsToWcsPos (start [0]);
		end [0] = mbcsToWcsPos (end [0]);
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
	checkWidget ();
	if (noSelection) return -1;
	return OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
}

/**
 * Returns a string containing a copy of the contents of the
 * receiver's text field, or an empty string if there are no
 * contents.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget ();
	int length = OS.GetWindowTextLength (handle);
	if (length == 0) return "";
	TCHAR buffer = new TCHAR (getCodePage (), length + 1);
	OS.GetWindowText (handle, buffer, length + 1);
	return buffer.toString (0, length);
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
 */
public int getTextHeight () {
	checkWidget ();
	COMBOBOXINFO pcbi = new COMBOBOXINFO ();
	pcbi.cbSize = COMBOBOXINFO.sizeof;
	if (((style & SWT.SIMPLE) == 0) && !OS.IsWinCE && OS.GetComboBoxInfo (handle, pcbi)) {
		return (pcbi.buttonBottom - pcbi.buttonTop) + pcbi.buttonTop * 2; 
	}
	int result = OS.SendMessage (handle, OS.CB_GETITEMHEIGHT, -1, 0);
	if (result == OS.CB_ERR) error (SWT.ERROR_CANNOT_GET_ITEM_HEIGHT);
	return (style & SWT.DROP_DOWN) != 0 ? result + 6 : result + 10;
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
 *
 * @see #LIMIT
 */
public int getTextLimit () {
	checkWidget ();
	int hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText == 0) return LIMIT;
	return OS.SendMessage (hwndText, OS.EM_GETLIMITTEXT, 0, 0);
}

/**
 * Gets the number of items that are visible in the drop
 * down portion of the receiver's list.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 *
 * @return the number of items that are visible
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public int getVisibleItemCount () {
	checkWidget ();
	return visibleCount;
}

boolean hasFocus () {
	int hwndFocus = OS.GetFocus ();
	if (hwndFocus == handle) return true;
	if (hwndFocus == 0) return false;
	int hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndFocus == hwndText) return true;
	int hwndList = OS.GetDlgItem (handle, CBID_LIST);
	if (hwndFocus == hwndList) return true;
	return false;
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
	return indexOf (string, 0);
}

/**
 * Searches the receiver's list starting at the given, 
 * zero-relative index until an item is found that is equal
 * to the argument, and returns the index of that item. If
 * no item is found or the starting index is out of range,
 * returns -1.
 *
 * @param string the search item
 * @param start the zero-relative index at which to begin the search
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
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	
	/*
	* Bug in Windows.  For some reason, CB_FINDSTRINGEXACT
	* will not find empty strings even though it is legal
	* to insert an empty string into a combo.  The fix is
	* to search the combo, an item at a time.
	*/
	if (string.length () == 0) {
		int count = getItemCount ();
		for (int i=start; i<count; i++) {
			if (string.equals (getItem (i))) return i;
		}
		return -1;
	}

	/* Use CB_FINDSTRINGEXACT to search for the item */	
	int count = OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
	if (!(0 <= start && start < count)) return -1;
	int index = start - 1, last = 0;
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	do {
		index = OS.SendMessage (handle, OS.CB_FINDSTRINGEXACT, last = index, buffer);
		if (index == OS.CB_ERR || index <= last) return -1;
	} while (!string.equals (getItem (index)));
	return index;
}

int mbcsToWcsPos (int mbcsPos) {
	if (mbcsPos <= 0) return 0;
	if (OS.IsUnicode) return mbcsPos;
	int hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText == 0) return mbcsPos;
	int mbcsSize = OS.GetWindowTextLengthA (hwndText);
	if (mbcsSize == 0) return 0;
	if (mbcsPos >= mbcsSize) return mbcsSize;
	byte [] buffer = new byte [mbcsSize + 1];
	OS.GetWindowTextA (hwndText, buffer, mbcsSize + 1);
	return OS.MultiByteToWideChar (getCodePage (), OS.MB_PRECOMPOSED, buffer, mbcsPos, null, 0);
}

/**
 * Pastes text from clipboard.
 * <p>
 * The selected text is deleted from the widget
 * and new text inserted from the clipboard.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1
 */
public void paste () {
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
	OS.SendMessage (handle, OS.WM_PASTE, 0, 0);
}

void register () {
	super.register ();
	int hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText != 0) display.addControl (hwndText, this);
	int hwndList = OS.GetDlgItem (handle, CBID_LIST);
	if (hwndList != 0) display.addControl (hwndList, this);
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
 */
public void remove (int index) {
	checkWidget ();
	int length = OS.GetWindowTextLength (handle);
	int code = OS.SendMessage (handle, OS.CB_DELETESTRING, index, 0);
	if (code == OS.CB_ERR) {
		int count = OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
		if (0 <= index && index < count) error (SWT.ERROR_ITEM_NOT_REMOVED);
		error (SWT.ERROR_INVALID_RANGE);
	}
	if (length != OS.GetWindowTextLength (handle)) {
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the modify
		* event.  If this happens, just return.
		*/
		sendEvent (SWT.Modify);
		if (isDisposed ()) return;
	}
	/*
	* Bug in Windows.  When the combo box is read only
	* with exactly one item that is currently selected
	* and that item is removed, the combo box does not
	* redraw to clear the text area.  The fix is to
	* force a redraw.
	*/
	if ((style & SWT.READ_ONLY) != 0) {		
		int count = OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
		if (count == 0) OS.InvalidateRect (handle, null, true);
	}
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
 */
public void remove (int start, int end) {
	checkWidget ();
	if (start > end) return;
	int count = OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
	if (!(0 <= start && start <= end && end < count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int length = OS.GetWindowTextLength (handle);
	for (int i=start; i<=end; i++) {
		int result = OS.SendMessage (handle, OS.CB_DELETESTRING, start, 0);
		if (result == OS.CB_ERR) error (SWT.ERROR_ITEM_NOT_REMOVED);
	}
	if (length != OS.GetWindowTextLength (handle)) {
		/*
		* It is possible (but unlikely), that application
		* code could have disposed the widget in the modify
		* event.  If this happens, just return.
		*/
		sendEvent (SWT.Modify);
		if (isDisposed ()) return;
	}
	/*
	* Bug in Windows.  When the combo box is read only
	* with exactly one item that is currently selected
	* and that item is removed, the combo box does not
	* redraw to clear the text area.  The fix is to
	* force a redraw.
	*/
	if ((style & SWT.READ_ONLY) != 0) {		
		count = OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
		if (count == 0) OS.InvalidateRect (handle, null, true);
	}
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
 */
public void remove (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	int index = indexOf (string, 0);
	if (index == -1) error (SWT.ERROR_INVALID_ARGUMENT);
	remove (index);
}

/**
 * Removes all of the items from the receiver's list and clear the
 * contents of receiver's text field.
 * <p>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget ();
	OS.SendMessage (handle, OS.CB_RESETCONTENT, 0, 0);
	sendEvent (SWT.Modify);
	// widget could be disposed at this point
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
	checkWidget ();
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
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is verified.
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
 * @see VerifyListener
 * @see #addVerifyListener
 * 
 * @since 3.1
 */
public void removeVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);	
}

boolean sendKeyEvent (int type, int msg, int wParam, int lParam, Event event) {
	if (!super.sendKeyEvent (type, msg, wParam, lParam, event)) {
		return false;
	}
	if ((style & SWT.READ_ONLY) != 0) return true;
	if (type != SWT.KeyDown) return true;
	if (msg != OS.WM_CHAR && msg != OS.WM_KEYDOWN && msg != OS.WM_IME_CHAR) {
		return true;
	}
	if (event.character == 0) return true;
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return true;
	char key = event.character;
	int stateMask = event.stateMask;
	
	/*
	* Disable all magic keys that could modify the text
	* and don't send events when Alt, Shift or Ctrl is
	* pressed.
	*/
	switch (msg) {
		case OS.WM_CHAR:
			if (key != 0x08 && key != 0x7F && key != '\r' && key != '\t' && key != '\n') break;
			// FALL THROUGH
		case OS.WM_KEYDOWN:
			if ((stateMask & (SWT.ALT | SWT.SHIFT | SWT.CONTROL)) != 0) return false;
			break;
	}

	/*
	* If the left button is down, the text widget refuses the character.
	*/
	if (OS.GetKeyState (OS.VK_LBUTTON) < 0) {
		return true;
	}

	/* Verify the character */
	String oldText = "";
	int [] start = new int [1], end = new int [1];
	int hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText == 0) return true;
	OS.SendMessage (hwndText, OS.EM_GETSEL, start, end);
	switch (key) {
		case 0x08:	/* Bs */
			if (start [0] == end [0]) {
				if (start [0] == 0) return true;
				start [0] = start [0] - 1;
				if (!OS.IsUnicode && OS.IsDBLocale) {
					int [] newStart = new int [1], newEnd = new int [1];
					OS.SendMessage (hwndText, OS.EM_SETSEL, start [0], end [0]);
					OS.SendMessage (hwndText, OS.EM_GETSEL, newStart, newEnd);
					if (start [0] != newStart [0]) start [0] = start [0] - 1;
				}
				start [0] = Math.max (start [0], 0);
			}
			break;
		case 0x7F:	/* Del */
			if (start [0] == end [0]) {
				int length = OS.GetWindowTextLength (hwndText);
				if (start [0] == length) return true;
				end [0] = end [0] + 1;
				if (!OS.IsUnicode && OS.IsDBLocale) {
					int [] newStart = new int [1], newEnd = new int [1];
					OS.SendMessage (hwndText, OS.EM_SETSEL, start [0], end [0]);
					OS.SendMessage (hwndText, OS.EM_GETSEL, newStart, newEnd);
					if (end [0] != newEnd [0]) end [0] = end [0] + 1;
				}
				end [0] = Math.min (end [0], length);
			}
			break;
		case '\r':	/* Return */
			return true;
		default:	/* Tab and other characters */
			if (key != '\t' && key < 0x20) return true;
			oldText = new String (new char [] {key});
			break;
	}
	String newText = verifyText (oldText, start [0], end [0], event);
	if (newText == null) return false;
	if (newText == oldText) return true;
	TCHAR buffer = new TCHAR (getCodePage (), newText, true);
	OS.SendMessage (hwndText, OS.EM_SETSEL, start [0], end [0]);
	OS.SendMessage (hwndText, OS.EM_REPLACESEL, 0, buffer);
	return false;
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
	checkWidget ();
	int count = OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
	if (0 <= index && index < count) {
		int selection = OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
		int code = OS.SendMessage (handle, OS.CB_SETCURSEL, index, 0);
		if (code != OS.CB_ERR && code != selection) {
			sendEvent (SWT.Modify);
			// widget could be disposed at this point
		}
	}
}

void setBackgroundPixel (int pixel) {
	if (background == pixel) return;
	super.setBackgroundPixel (pixel);
	int hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText != 0) OS.InvalidateRect (hwndText, null, true);
	int hwndList = OS.GetDlgItem (handle, CBID_LIST);
	if (hwndList != 0) OS.InvalidateRect (hwndList, null, true);
}

void setBounds (int x, int y, int width, int height, int flags) {
	/*
	* Feature in Windows.  If the combo box has the CBS_DROPDOWN
	* or CBS_DROPDOWNLIST style, Windows uses the height that the
	* programmer sets in SetWindowPos () to control height of the
	* drop down list.  When the width is non-zero, Windows remembers
	* this value and sets the height to be the height of the text
	* field part of the combo box.  If the width is zero, Windows
	* allows the height to have any value.  Therefore, when the
	* programmer sets and then queries the height, the values can
	* be different depending on the width.  The problem occurs when
	* the programmer uses computeSize () to determine the preferred
	* height (always the height of the text field) and then uses
	* this value to set the height of the combo box.  The result
	* is a combo box with a zero size drop down list.  The fix, is
	* to always set the height to show a fixed number of combo box
	* items and ignore the height value that the programmer supplies.
	*/
	if ((style & SWT.DROP_DOWN) != 0) {
		height = getTextHeight () + (getItemHeight () * visibleCount) + 2;
		/*
		* Feature in Windows.  When a drop down combo box is resized,
		* the combo box resizes the height of the text field and uses
		* the height provided in SetWindowPos () to determine the height
		* of the drop down list.  For some reason, the combo box redraws
		* the whole area, not just the text field.  The fix is to set the
		* SWP_NOSIZE bits when the height of text field and the drop down
		* list is the same as the requested height.
		* 
		* NOTE:  Setting the width of a combo box to zero does not update
		* the width of the drop down control rect.  If the width of the
		* combo box is zero, then do not set SWP_NOSIZE.
		*/
		RECT rect = new RECT ();
		OS.GetWindowRect (handle, rect);
		if (rect.right - rect.left != 0) {
			if (OS.SendMessage (handle, OS.CB_GETDROPPEDCONTROLRECT, 0, rect) != 0) {
				int oldWidth = rect.right - rect.left, oldHeight = rect.bottom - rect.top;
				if (oldWidth == width && oldHeight == height) flags |= OS.SWP_NOSIZE;
			}
		}
		SetWindowPos (handle, 0, x, y, width, height, flags);
	} else {
		super.setBounds (x, y, width, height, flags);
	}
}

void setForegroundPixel (int pixel) {
	if (foreground == pixel) return;
	super.setForegroundPixel (pixel);
	int hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText != 0) OS.InvalidateRect (hwndText, null, true);
	int hwndList = OS.GetDlgItem (handle, CBID_LIST);
	if (hwndList != 0) OS.InvalidateRect (hwndList, null, true);
}

/**
 * Sets the text of the item in the receiver's list at the given
 * zero-relative index to the string argument. This is equivalent
 * to removing the old item at the index, and then adding the new
 * item at that index.
 *
 * @param index the index for the item
 * @param string the new text for the item
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_RANGE - if the index is not between 0 and the number of elements in the list minus 1 (inclusive)</li>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setItem (int index, String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	remove (index);
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the modify
	* event that might be sent when the index is removed.
	* If this happens, just exit.
	*/
	if (isDisposed ()) return;
	add (string, index);
}

/**
 * Sets the receiver's list to be the given array of items.
 *
 * @param items the array of items
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the items array is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if an item in the items array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setItems (String [] items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (int i=0; i<items.length; i++) {
		if (items [i] == null) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	OS.SendMessage (handle, OS.CB_RESETCONTENT, 0, 0);
	int codePage = getCodePage ();
	for (int i=0; i<items.length; i++) {
		String string = items [i];
		TCHAR buffer = new TCHAR (codePage, string, true);
		int code = OS.SendMessage (handle, OS.CB_ADDSTRING, 0, buffer);
		if (code == OS.CB_ERR) error (SWT.ERROR_ITEM_NOT_ADDED);
		if (code == OS.CB_ERRSPACE) error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	// widget could be disposed at this point
	sendEvent (SWT.Modify);
}

/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 * <p>
 *
 * @param orientation new orientation style
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 2.1.2
 */
public void setOrientation (int orientation) {
	checkWidget();
	if (OS.IsWinCE) return;
	if (OS.WIN32_VERSION < OS.VERSION (4, 10)) return;
	int flags = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
	if ((orientation & flags) == 0 || (orientation & flags) == flags) return;
	style &= ~flags;
	style |= orientation & flags;
	int bits  = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		style |= SWT.MIRRORED;
		bits |= OS.WS_EX_LAYOUTRTL;
	} else {
		style &= ~SWT.MIRRORED;
		bits &= ~OS.WS_EX_LAYOUTRTL;
	}
	OS.SetWindowLong (handle, OS.GWL_EXSTYLE, bits);
	int hwndText = 0, hwndList = 0;
	COMBOBOXINFO pcbi = new COMBOBOXINFO ();
	pcbi.cbSize = COMBOBOXINFO.sizeof;
	if (OS.GetComboBoxInfo (handle, pcbi)) {
		hwndText = pcbi.hwndItem;
		hwndList = pcbi.hwndList;
	}
	if (hwndText != 0) {
		int bits1 = OS.GetWindowLong (hwndText, OS.GWL_EXSTYLE);
		int bits2 = OS.GetWindowLong (hwndText, OS.GWL_STYLE);
		if ((style & SWT.RIGHT_TO_LEFT) != 0) {
			bits1 |= OS.WS_EX_RIGHT | OS.WS_EX_RTLREADING;
			bits2 |= OS.ES_RIGHT;
		} else {
			bits1 &= ~(OS.WS_EX_RIGHT | OS.WS_EX_RTLREADING);
			bits2 &= ~OS.ES_RIGHT;
		}
		OS.SetWindowLong (hwndText, OS.GWL_EXSTYLE, bits1);
		OS.SetWindowLong (hwndText, OS.GWL_STYLE, bits2);
		
		/*
		* Bug in Windows.  For some reason, the single line text field
		* portion of the combo box does not redraw to reflect the new
		* style bits.  The fix is to force the widget to be resized by
		* temporarily shrinking and then growing the width and height.
		*/
		RECT rect = new RECT ();
		OS.GetWindowRect (hwndText, rect);
		int width = rect.right - rect.left, height = rect.bottom - rect.top;
		OS.GetWindowRect (handle, rect);
		int widthCombo = rect.right - rect.left, heightCombo = rect.bottom - rect.top;
		int uFlags = OS.SWP_NOMOVE | OS.SWP_NOZORDER | OS.SWP_NOACTIVATE;
		SetWindowPos (hwndText, 0, 0, 0, width - 1, height - 1, uFlags);
		SetWindowPos (handle, 0, 0, 0, widthCombo - 1, heightCombo - 1, uFlags);
		SetWindowPos (hwndText, 0, 0, 0, width, height, uFlags);
		SetWindowPos (handle, 0, 0, 0, widthCombo, heightCombo, uFlags);
		OS.InvalidateRect (handle, null, true);
	}	
	if (hwndList != 0) {
		int bits1 = OS.GetWindowLong (hwndList, OS.GWL_EXSTYLE);		
		if ((style & SWT.RIGHT_TO_LEFT) != 0) {
			bits1 |= OS.WS_EX_LAYOUTRTL;
		} else {
			bits1 &= ~OS.WS_EX_LAYOUTRTL;
		}
		OS.SetWindowLong (hwndList, OS.GWL_EXSTYLE, bits1);
	}
}

/**
 * Sets the selection in the receiver's text field to the
 * range specified by the argument whose x coordinate is the
 * start of the selection and whose y coordinate is the end
 * of the selection. 
 *
 * @param selection a point representing the new selection start and end
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
	checkWidget ();
	if (selection == null) error (SWT.ERROR_NULL_ARGUMENT);
	int start = selection.x, end = selection.y;
	if (!OS.IsUnicode && OS.IsDBLocale) {
		start = wcsToMbcsPos (start);
		end = wcsToMbcsPos (end);
	}
	int bits = start | (end << 16);
	OS.SendMessage (handle, OS.CB_SETEDITSEL, 0, bits);
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
 * @param string the new text
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
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.READ_ONLY) != 0) {
		int index = indexOf (string);
		if (index != -1) select (index);
		return;
	}
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	if (OS.SetWindowText (handle, buffer)) {
		sendEvent (SWT.Modify);
		// widget could be disposed at this point
	}
}

/**
 * Sets the maximum number of characters that the receiver's
 * text field is capable of holding to be the argument.
 * <p>
 * To reset this value to the default, use <code>setTextLimit(Combo.LIMIT)</code>.
 * Specifying a limit value larger than <code>Combo.LIMIT</code> sets the
 * receiver's limit to <code>Combo.LIMIT</code>.
 * </p>
 * @param limit new text limit
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #LIMIT
 */
public void setTextLimit (int limit) {
	checkWidget ();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	OS.SendMessage (handle, OS.CB_LIMITTEXT, limit, 0);
}

/**
 * Sets the number of items that are visible in the drop
 * down portion of the receiver's list.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 *
 * @param count the new number of items to be visible
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @since 3.0
 */
public void setVisibleItemCount (int count) {
	checkWidget ();
	if (count < 0) return;
	visibleCount = count;
	if ((style & SWT.DROP_DOWN) != 0) {
		forceResize ();
		RECT rect = new RECT ();
		OS.GetWindowRect (handle, rect);
		int flags = OS.SWP_NOMOVE | OS.SWP_NOZORDER | OS.SWP_DRAWFRAME | OS.SWP_NOACTIVATE;
		setBounds (0, 0, rect.right - rect.left, rect.bottom - rect.top, flags);
	}
}

void subclass () {
	super.subclass ();
	int newProc = display.windowProc;
	int hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText != 0) {
		OS.SetWindowLong (hwndText, OS.GWL_WNDPROC, newProc);
	}
	int hwndList = OS.GetDlgItem (handle, CBID_LIST);
	if (hwndList != 0) {	
		OS.SetWindowLong (hwndList, OS.GWL_WNDPROC, newProc);
	}
}

boolean translateTraversal (MSG msg) {
	/*
	* When the combo box is dropped down, allow return
	* to select an item in the list and escape to close
	* the combo box.
	*/
	switch (msg.wParam) {
		case OS.VK_RETURN:
		case OS.VK_ESCAPE:
			if ((style & SWT.DROP_DOWN) != 0) {
				if (OS.SendMessage (handle, OS.CB_GETDROPPEDSTATE, 0, 0) != 0) {
					return false;
				}
			}
	}
	return super.translateTraversal (msg);
}

boolean traverseEscape () {
	if ((style & SWT.DROP_DOWN) != 0) {
		if (OS.SendMessage (handle, OS.CB_GETDROPPEDSTATE, 0, 0) != 0) {
			OS.SendMessage (handle, OS.CB_SHOWDROPDOWN, 0, 0);
			return true;
		}
	}
	return super.traverseEscape ();
}

void unsubclass () {
	super.unsubclass ();
	int hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText != 0 && EditProc != 0) {
		OS.SetWindowLong (hwndText, OS.GWL_WNDPROC, EditProc);
	}
	int hwndList = OS.GetDlgItem (handle, CBID_LIST);
	if (hwndList != 0 && ListProc != 0) {
		OS.SetWindowLong (hwndList, OS.GWL_WNDPROC, ListProc);
	}
}

String verifyText (String string, int start, int end, Event keyEvent) {
	Event event = new Event ();
	event.text = string;
	event.start = start;
	event.end = end;
	if (keyEvent != null) {
		event.character = keyEvent.character;
		event.keyCode = keyEvent.keyCode;
		event.stateMask = keyEvent.stateMask;
	}
	if (!OS.IsUnicode && OS.IsDBLocale) {
		event.start = mbcsToWcsPos (start);
		event.end = mbcsToWcsPos (end);
	}
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the verify
	* event.  If this happens, answer null to cancel
	* the operation.
	*/
	sendEvent (SWT.Verify, event);
	if (!event.doit || isDisposed ()) return null;
	return event.text;
}

int wcsToMbcsPos (int wcsPos) {
	if (wcsPos <= 0) return 0;
	if (OS.IsUnicode) return wcsPos;
	int hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText == 0) return wcsPos;
	int mbcsSize = OS.GetWindowTextLengthA (hwndText);
	if (mbcsSize == 0) return 0;
	byte [] buffer = new byte [mbcsSize + 1];
	OS.GetWindowTextA (hwndText, buffer, mbcsSize + 1);
	int mbcsPos = 0, wcsCount = 0;
	while (mbcsPos < mbcsSize) {
		if (wcsPos == wcsCount) break;
		if (OS.IsDBCSLeadByte (buffer [mbcsPos++])) mbcsPos++;
		wcsCount++;
	}
	return mbcsPos;
}

int widgetExtStyle () {
	return super.widgetExtStyle () & ~OS.WS_EX_NOINHERITLAYOUT;
}

int widgetStyle () {
	int bits = super.widgetStyle () | OS.CBS_AUTOHSCROLL | OS.CBS_NOINTEGRALHEIGHT | OS.WS_VSCROLL;
	if ((style & SWT.SIMPLE) != 0) return bits | OS.CBS_SIMPLE;
	if ((style & SWT.READ_ONLY) != 0) return bits | OS.CBS_DROPDOWNLIST;
	return bits | OS.CBS_DROPDOWN;
}

TCHAR windowClass () {
	return ComboClass;
}

int windowProc () {
	return ComboProc;
}

int windowProc (int hwnd, int msg, int wParam, int lParam) {
	if (handle == 0) return 0;
	if (hwnd != handle) {
		int hwndText = OS.GetDlgItem (handle, CBID_EDIT);
		int hwndList = OS.GetDlgItem (handle, CBID_LIST);
		if ((hwndText != 0 && hwnd == hwndText) || (hwndList != 0 && hwnd == hwndList)) {
			LRESULT result = null;
			switch (msg) {
				/* Keyboard messages */
				case OS.WM_CHAR:		result = wmChar (hwnd, wParam, lParam); break;
				case OS.WM_IME_CHAR:	result = wmIMEChar (hwnd, wParam, lParam); break;
				case OS.WM_KEYDOWN:		result = wmKeyDown (hwnd, wParam, lParam); break;
				case OS.WM_KEYUP:		result = wmKeyUp (hwnd, wParam, lParam); break;
				case OS.WM_SYSCHAR:		result = wmSysChar (hwnd, wParam, lParam); break;
				case OS.WM_SYSKEYDOWN:	result = wmSysKeyDown (hwnd, wParam, lParam); break;
				case OS.WM_SYSKEYUP:	result = wmSysKeyUp (hwnd, wParam, lParam); break;

				/* Mouse Messages */
				case OS.WM_CAPTURECHANGED:	result = wmCaptureChanged (hwnd, wParam, lParam); break;
				case OS.WM_LBUTTONDBLCLK:	result = wmLButtonDblClk (hwnd, wParam, lParam); break;
				case OS.WM_LBUTTONDOWN:		result = wmLButtonDown (hwnd, wParam, lParam); break;
				case OS.WM_LBUTTONUP:		result = wmLButtonUp (hwnd, wParam, lParam); break;
				case OS.WM_MBUTTONDBLCLK:	result = wmMButtonDblClk (hwnd, wParam, lParam); break;
				case OS.WM_MBUTTONDOWN:		result = wmMButtonDown (hwnd, wParam, lParam); break;
				case OS.WM_MBUTTONUP:		result = wmMButtonUp (hwnd, wParam, lParam); break;
				case OS.WM_MOUSEHOVER:		result = wmMouseHover (hwnd, wParam, lParam); break;
				case OS.WM_MOUSELEAVE:		result = wmMouseLeave (hwnd, wParam, lParam); break;
				case OS.WM_MOUSEMOVE:		result = wmMouseMove (hwnd, wParam, lParam); break;
//				case OS.WM_MOUSEWHEEL:		result = wmMouseWheel (hwnd, wParam, lParam); break;
				case OS.WM_RBUTTONDBLCLK:	result = wmRButtonDblClk (hwnd, wParam, lParam); break;
				case OS.WM_RBUTTONDOWN:		result = wmRButtonDown (hwnd, wParam, lParam); break;
				case OS.WM_RBUTTONUP:		result = wmRButtonUp (hwnd, wParam, lParam); break;
				case OS.WM_XBUTTONDBLCLK:	result = wmXButtonDblClk (hwnd, wParam, lParam); break;
				case OS.WM_XBUTTONDOWN:		result = wmXButtonDown (hwnd, wParam, lParam); break;
				case OS.WM_XBUTTONUP:		result = wmXButtonUp (hwnd, wParam, lParam); break;

				/* Paint messages */
				case OS.WM_PAINT:			result = wmPaint (hwnd, wParam, lParam); break;

				/* Menu messages */
				case OS.WM_CONTEXTMENU:		result = wmContextMenu (hwnd, wParam, lParam); break;
					
				/* Clipboard messages */
				case OS.WM_CLEAR:
				case OS.WM_CUT:
				case OS.WM_PASTE:
				case OS.WM_UNDO:
				case OS.EM_UNDO:
				case OS.WM_SETTEXT:
					if (hwnd == hwndText) {
						result = wmClipboard (hwnd, msg, wParam, lParam);
					}
					break;
			}
			if (result != null) return result.value;
			return callWindowProc (hwnd, msg, wParam, lParam);
		}
	}
	if (msg == OS.CB_SETCURSEL) {
		if ((style & SWT.READ_ONLY) != 0) {
			if (hooks (SWT.Verify) || filters (SWT.Verify)) {
				String oldText = getText (), newText = null;
				if (wParam == -1) {
					newText = "";
				} else {
					if (0 <= wParam && wParam < getItemCount ()) {
						newText = getItem (wParam);
					}
				}
				if (newText != null && !newText.equals (oldText)) {
					int length = OS.GetWindowTextLength (handle);
					oldText = newText;
					newText = verifyText (newText, 0, length, null);
					if (newText == null) return 0;
					if (!newText.equals (oldText)) {
						int index = indexOf (newText);
						if (index != -1 && index != wParam) {
							return callWindowProc (handle, OS.CB_SETCURSEL, index, lParam);
						}
					}
				}
			}
		}
	}
	return super.windowProc (hwnd, msg, wParam, lParam);
}

LRESULT WM_CTLCOLOR (int wParam, int lParam) {
	return wmColorChild (wParam, lParam);
}

LRESULT WM_GETDLGCODE (int wParam, int lParam) {
	int code = callWindowProc (handle, OS.WM_GETDLGCODE, wParam, lParam);
	return new LRESULT (code | OS.DLGC_WANTARROWS);
}

LRESULT WM_KILLFOCUS (int wParam, int lParam) {
	/*
	* Bug in Windows.  When a combo box that is read only
	* is disposed in CBN_KILLFOCUS, Windows segment faults.
	* The fix is to send focus from WM_KILLFOCUS instead
	* of CBN_KILLFOCUS.
	* 
	* NOTE: In version 6 of COMCTL32.DLL, the bug is fixed.
	*/
	if ((style & SWT.READ_ONLY) != 0) {
		return super.WM_KILLFOCUS (wParam, lParam);
	}
	
	/*
	* Return NULL - Focus notification is
	* done in WM_COMMAND by CBN_KILLFOCUS.
	*/
	return null;
}

LRESULT WM_LBUTTONDOWN (int wParam, int lParam) {
	/*
	* Feature in Windows.  When an editable combo box is dropped
	* down and the text in the entry field partially matches an
	* item in the list, Windows selects the item but doesn't send
	* WM_COMMAND with CBN_SELCHANGE.  The fix is to detect that
	* the selection has changed and issue the notification.
	*/
	int oldSelection = OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
	LRESULT result = super.WM_LBUTTONDOWN (wParam, lParam);
	if ((style & SWT.READ_ONLY) == 0) {
		int newSelection = OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
		if (oldSelection != newSelection) {
			sendEvent (SWT.Modify);
			if (isDisposed ()) return LRESULT.ZERO;
			sendEvent (SWT.Selection);
			if (isDisposed ()) return LRESULT.ZERO;
		}
	}
	return result;
}

LRESULT WM_SETFOCUS (int wParam, int lParam) {
	/*
	* Return NULL - Focus notification is
	* done by WM_COMMAND with CBN_SETFOCUS.
	*/
	return null;
}

LRESULT WM_SIZE (int wParam, int lParam) {	
	/*
	* Bug in Windows.  If the combo box has the CBS_SIMPLE style,
	* the list portion of the combo box is not redrawn when the
	* combo box is resized.  The fix is to force a redraw when
	* the size has changed.
	*/
	if ((style & SWT.SIMPLE) != 0) {
		LRESULT result = super.WM_SIZE (wParam, lParam);
		if (OS.IsWindowVisible (handle)) {
			if (OS.IsWinCE) {	
				int hwndText = OS.GetDlgItem (handle, CBID_EDIT);
				if (hwndText != 0) OS.InvalidateRect (hwndText, null, true);
				int hwndList = OS.GetDlgItem (handle, CBID_LIST);
				if (hwndList != 0) OS.InvalidateRect (hwndList, null, true);
			} else {
				int uFlags = OS.RDW_ERASE | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
				OS.RedrawWindow (handle, null, 0, uFlags);
			}
		}
		return result;
	}
	
	/*
	* Feature in Windows.  When an editable drop down combo box
	* contains text that does not correspond to an item in the
	* list, when the widget is resized, it selects the closest
	* match from the list.  The fix is to remember the original
	* text and reset it after the widget is resized.
	*/
	if ((style & SWT.READ_ONLY) != 0 || (style & SWT.DROP_DOWN) == 0) {
		return super.WM_SIZE (wParam, lParam);
	}
	int index = OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
	boolean redraw = false;
	TCHAR buffer = null;
	int [] start = null, end = null;
	if (index == OS.CB_ERR) {
		int length = OS.GetWindowTextLength (handle);
		if (length != 0) {
			buffer = new TCHAR (getCodePage (), length + 1);
			OS.GetWindowText (handle, buffer, length + 1);
			start = new int [1];  end = new int [1];
			OS.SendMessage (handle, OS.CB_GETEDITSEL, start, end);
			redraw = drawCount == 0 && OS.IsWindowVisible (handle);
			if (redraw) setRedraw (false);
		}
	}
	LRESULT result = super.WM_SIZE (wParam, lParam);
	/*
	* It is possible (but unlikely), that application
	* code could have disposed the widget in the resize
	* event.  If this happens, end the processing of the
	* Windows message by returning the result of the
	* WM_SIZE message.
	*/
	if (isDisposed ()) return result;
	if (buffer != null) {
		OS.SetWindowText (handle, buffer);
		int bits = start [0] | (end [0] << 16);
		OS.SendMessage (handle, OS.CB_SETEDITSEL, 0, bits);
		if (redraw) setRedraw (true);
	}
	return result; 
}

LRESULT wmChar (int hwnd, int wParam, int lParam) {
	if (ignoreCharacter) return null;
	LRESULT result = super.wmChar (hwnd, wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  For some reason, when the
	* widget is a single line text widget, when the
	* user presses tab, return or escape, Windows beeps.
	* The fix is to look for these keys and not call
	* the window proc.
	* 
	* NOTE: This only happens when the drop down list
	* is not visible.
	*/
	switch (wParam) {
		case SWT.TAB: return LRESULT.ZERO;
		case SWT.CR:
			postEvent (SWT.DefaultSelection);
			// FALL THROUGH
		case SWT.ESC:
			if ((style & SWT.DROP_DOWN) != 0) {
				if (OS.SendMessage (handle, OS.CB_GETDROPPEDSTATE, 0, 0) == 0) {
					return LRESULT.ZERO;
				}
			}
	}
	return result;
}

LRESULT wmClipboard (int hwndText, int msg, int wParam, int lParam) {
	if ((style & SWT.READ_ONLY) != 0) return null;
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return null;
	boolean call = false;
	int [] start = new int [1], end = new int [1];
	String oldText = null, newText = null;
	switch (msg) {
		case OS.WM_CLEAR:
		case OS.WM_CUT:
			OS.SendMessage (hwndText, OS.EM_GETSEL, start, end);
			if (start [0] != end [0]) {
				newText = "";
				call = true;
			}
			break;
		case OS.WM_PASTE:
			OS.SendMessage (hwndText, OS.EM_GETSEL, start, end);
			newText = getClipboardText ();
			break;
		case OS.EM_UNDO:
		case OS.WM_UNDO:
			if (OS.SendMessage (hwndText, OS.EM_CANUNDO, 0, 0) != 0) {
				ignoreModify = true;
				OS.SendMessage (hwndText, OS.EM_GETSEL, start, end);
				OS.CallWindowProc (EditProc, hwndText, msg, wParam, lParam);
				int length = OS.GetWindowTextLength (hwndText);
				if (length != 0 && start [0] != end [0]) {
					TCHAR buffer = new TCHAR (getCodePage (), length + 1);
					OS.GetWindowText (hwndText, buffer, length + 1);
					newText = buffer.toString (start [0], end [0] - start [0]);
				} else {
					newText = "";
				}
				OS.CallWindowProc (EditProc, hwndText, msg, wParam, lParam);
				ignoreModify = false;
			}
			break;
		case OS.WM_SETTEXT:
			end [0] = OS.GetWindowTextLength (hwndText);
			oldText = getText ();
			int length = OS.IsUnicode ? OS.wcslen (lParam) : OS.strlen (lParam);
			TCHAR buffer = new TCHAR (getCodePage (), length);
			int byteCount = buffer.length () * TCHAR.sizeof;
			OS.MoveMemory (buffer, lParam, byteCount);
			newText = buffer.toString (0, length);
			break;
	}
	if (newText != null && !newText.equals (oldText)) {
		oldText = newText;
		newText = verifyText (newText, start [0], end [0], null);
		if (newText == null) return LRESULT.ZERO;
		if (!newText.equals (oldText)) {
			if (call) {
				OS.CallWindowProc (EditProc, hwndText, msg, wParam, lParam);
			}
			TCHAR buffer = new TCHAR (getCodePage (), newText, true);
			if (msg == OS.WM_SETTEXT) {
				int hHeap = OS.GetProcessHeap ();
				int byteCount = buffer.length () * TCHAR.sizeof;
				int pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
				OS.MoveMemory (pszText, buffer, byteCount); 
				int code = OS.CallWindowProc (EditProc, hwndText, msg, wParam, pszText);
				OS.HeapFree (hHeap, 0, pszText);
				return new LRESULT (code);
			} else {
				OS.SendMessage (hwndText, OS.EM_REPLACESEL, 0, buffer);
				return LRESULT.ZERO;
			}
		}
	}
	return null;
}

LRESULT wmCommandChild (int wParam, int lParam) {
	int code = wParam >> 16;
	switch (code) {
		case OS.CBN_EDITCHANGE:
			if (ignoreModify) break;
			/*
			* Feature in Windows.  If the combo box list selection is
			* queried using CB_GETCURSEL before the WM_COMMAND (with
			* CBN_EDITCHANGE) returns, CB_GETCURSEL returns the previous
			* selection in the list.  It seems that the combo box sends
			* the WM_COMMAND before it makes the selection in the list box
			* match the entry field.  The fix is remember that no selection
			* in the list should exist in this case.
			*/
			noSelection = true;
			sendEvent (SWT.Modify);
			if (isDisposed ()) return LRESULT.ZERO;
			noSelection = false;
			break;
		case OS.CBN_SELCHANGE:
			/*
			* Feature in Windows.  If the text in an editable combo box
			* is queried using GetWindowText () before the WM_COMMAND
			* (with CBN_SELCHANGE) returns, GetWindowText () returns is
			* the previous text in the combo box.  It seems that the combo
			* box sends the WM_COMMAND before it updates the text field to
			* match the list selection.  The fix is to force the text field
			* to match the list selection by re-selecting the list item.
			*/
			int index = OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
			if (index != OS.CB_ERR) {
				OS.SendMessage (handle, OS.CB_SETCURSEL, index, 0);
			}
			/*
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the modify
			* event.  If this happens, end the processing of the
			* Windows message by returning zero as the result of
			* the window proc.
			*/
			sendEvent (SWT.Modify);
			if (isDisposed ()) return LRESULT.ZERO;
			postEvent (SWT.Selection);
			break;
		case OS.CBN_SETFOCUS:
			sendFocusEvent (SWT.FocusIn);
			if (isDisposed ()) return LRESULT.ZERO;
			break;
		case OS.CBN_KILLFOCUS:
			/*
			* Bug in Windows.  When a combo box that is read only
			* is disposed in CBN_KILLFOCUS, Windows segment faults.
			* The fix is to send focus from WM_KILLFOCUS instead
			* of CBN_KILLFOCUS.
			* 
			* NOTE: In version 6 of COMCTL32.DLL, the bug is fixed.
			*/
			if ((style & SWT.READ_ONLY) != 0) break;
			sendFocusEvent (SWT.FocusOut);
			if (isDisposed ()) return LRESULT.ZERO;
			break;
	}
	return super.wmCommandChild (wParam, lParam);
}

LRESULT wmIMEChar (int hwnd, int wParam, int lParam) {

	/* Process a DBCS character */
	Display display = this.display;
	display.lastKey = 0;
	display.lastAscii = wParam;
	display.lastVirtual = display.lastNull = display.lastDead = false;
	if (!sendKeyEvent (SWT.KeyDown, OS.WM_IME_CHAR, wParam, lParam)) {
		return LRESULT.ZERO;
	}

	/*
	* Feature in Windows.  The Windows text widget uses
	* two 2 WM_CHAR's to process a DBCS key instead of
	* using WM_IME_CHAR.  The fix is to allow the text
	* widget to get the WM_CHAR's but ignore sending
	* them to the application.
	*/
	ignoreCharacter = true;
	int result = callWindowProc (hwnd, OS.WM_IME_CHAR, wParam, lParam);
	MSG msg = new MSG ();
	int flags = OS.PM_REMOVE | OS.PM_NOYIELD | OS.PM_QS_INPUT | OS.PM_QS_POSTMESSAGE;
	while (OS.PeekMessage (msg, hwnd, OS.WM_CHAR, OS.WM_CHAR, flags)) {
		OS.TranslateMessage (msg);
		OS.DispatchMessage (msg);
	}
	ignoreCharacter = false;
	
	sendKeyEvent (SWT.KeyUp, OS.WM_IME_CHAR, wParam, lParam);
	// widget could be disposed at this point
	display.lastKey = display.lastAscii = 0;
	return new LRESULT (result);
}

}
