/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 483540
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

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
 * <dd>DefaultSelection, Modify, Selection, Verify, OrientationChange</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles DROP_DOWN and SIMPLE may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see List
 * @see <a href="http://www.eclipse.org/swt/snippets/#combo">Combo snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Combo extends Composite {
	boolean noSelection, ignoreDefaultSelection, ignoreCharacter, ignoreModify, ignoreResize, lockText;
	int scrollWidth, visibleCount;
	long cbtHook;
	String [] items = new String [0];
	int[] segments;
	int clearSegmentsCount = 0;
	boolean stateFlagsUsable;

	static final char LTR_MARK = '\u200e';
	static final char RTL_MARK = '\u200f';
	static final int VISIBLE_COUNT = 5;

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
		LIMIT = 0x7FFFFFFF;
	}

	/*
	 * These are the undocumented control id's for the children of
	 * a combo box.  Since there are no constants for these values,
	 * they may change with different versions of Windows (but have
	 * been the same since Windows 3.0).
	 */
	static final int CBID_LIST = 1000;
	static final int CBID_EDIT = 1001;
	static /*final*/ long EditProc, ListProc;

	static final long ComboProc;
	static final TCHAR ComboClass = new TCHAR (0, "COMBOBOX", true);
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, ComboClass, lpWndClass);
		ComboProc = lpWndClass.lpfnWndProc;
	}

	/* Undocumented values. Remained the same at least between Win7 and Win10 */
	static final int stateFlagsOffset = (C.PTR_SIZEOF == 8) ? 0x68 : 0x54;
	static final int stateFlagsFirstPaint = 0x02000000;

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
	this.style |= SWT.H_SCROLL;
}

/**
 * Adds the argument to the end of the receiver's list.
 * <p>
 * Note: If control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
 * </p>
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
	int result = (int)OS.SendMessage (handle, OS.CB_ADDSTRING, 0, buffer);
	if (result == OS.CB_ERR) error (SWT.ERROR_ITEM_NOT_ADDED);
	if (result == OS.CB_ERRSPACE) error (SWT.ERROR_ITEM_NOT_ADDED);
	if ((style & SWT.H_SCROLL) != 0) setScrollWidth (buffer.chars, true);
}

/**
 * Adds the argument to the receiver's list at the given
 * zero-relative index.
 * <p>
 * Note: To add an item at the end of the list, use the
 * result of calling <code>getItemCount()</code> as the
 * index or use <code>add(String)</code>.
 * </p><p>
 * Also note, if control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
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
	int count = (int)OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
	if (!(0 <= index && index <= count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	int result = (int)OS.SendMessage (handle, OS.CB_INSERTSTRING, index, buffer);
	if (result == OS.CB_ERRSPACE || result == OS.CB_ERR) {
		error (SWT.ERROR_ITEM_NOT_ADDED);
	}
	if ((style & SWT.H_SCROLL) != 0) setScrollWidth (buffer.chars, true);
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
 * Adds a segment listener.
 * <p>
 * A <code>SegmentEvent</code> is sent whenever text content is being modified or
 * a segment listener is added or removed. You can
 * customize the appearance of text by indicating certain characters to be inserted
 * at certain text offsets. This may be used for bidi purposes, e.g. when
 * adjacent segments of right-to-left text should not be reordered relative to
 * each other.
 * E.g., multiple Java string literals in a right-to-left language
 * should generally remain in logical order to each other, that is, the
 * way they are stored.
 * </p>
 * <p>
 * <b>Warning</b>: This API is currently only implemented on Windows.
 * <code>SegmentEvent</code>s won't be sent on GTK and Cocoa.
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
 * @see SegmentEvent
 * @see SegmentListener
 * @see #removeSegmentListener
 *
 * @since 3.103
 */
public void addSegmentListener (SegmentListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	addListener (SWT.Segments, new TypedListener (listener));
	int selection = OS.CB_ERR;
	if (!noSelection) {
		selection = (int)OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
	}
	clearSegments (true);
	applyEditSegments ();
	applyListSegments ();
	if (selection != OS.CB_ERR) {
		OS.SendMessage (handle, OS.CB_SETCURSEL, selection, 0);
	}
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the user changes the receiver's selection, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the user changes the combo's list selection.
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

void applyEditSegments () {
	if (--clearSegmentsCount != 0) return;
	if (!hooks (SWT.Segments) && !filters (SWT.Segments) && (state & HAS_AUTO_DIRECTION) == 0) return;
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	int length = OS.GetWindowTextLength (hwndText);
	char [] buffer = new char [length + 1];
	if (length > 0) OS.GetWindowText (hwndText, buffer, length + 1);
	String string = new String (buffer, 0, length);
	/* Get segments */
	segments = null;
	Event event = getSegments (string);
	if (event == null || event.segments == null) return;
	segments = event.segments;
	int nSegments = segments.length;
	if (nSegments == 0) return;
	char [] segmentsChars = event.segmentsChars;
	int limit = (int)OS.SendMessage (hwndText, OS.EM_GETLIMITTEXT, 0, 0) & 0x7fffffff;
	OS.SendMessage (hwndText, OS.EM_SETLIMITTEXT, limit + Math.min (nSegments, LIMIT - limit), 0);
	length += nSegments;
	char [] newChars = new char [length + 1];
	int charCount = 0, segmentCount = 0;
	char defaultSeparator = getOrientation () == SWT.RIGHT_TO_LEFT ? RTL_MARK : LTR_MARK;
	while (charCount < length) {
		if (segmentCount < nSegments && charCount - segmentCount == segments [segmentCount]) {
			char separator = segmentsChars != null && segmentsChars.length > segmentCount ? segmentsChars [segmentCount] : defaultSeparator;
			newChars [charCount++] = separator;
			segmentCount++;
		} else if (string != null) {
			newChars [charCount] = string.charAt (charCount++ - segmentCount);
		}
	}
	while (segmentCount < nSegments) {
		segments [segmentCount] = charCount - segmentCount;
		char separator = segmentsChars != null && segmentsChars.length > segmentCount ? segmentsChars [segmentCount] : defaultSeparator;
		newChars [charCount++] = separator;
		segmentCount++;
	}
	/* Get the current selection */
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (hwndText, OS.EM_GETSEL, start, end);
	boolean oldIgnoreCharacter = ignoreCharacter, oldIgnoreModify = ignoreModify;
	ignoreCharacter = ignoreModify = true;
	/*
	 * SetWindowText empties the undo buffer and disables undo menu item.
	 * Sending OS.EM_REPLACESEL message instead.
	 */
	newChars [length] = 0;
	OS.SendMessage (hwndText, OS.EM_SETSEL, 0, -1);
	long undo = OS.SendMessage (hwndText, OS.EM_CANUNDO, 0, 0);
	OS.SendMessage (hwndText, OS.EM_REPLACESEL, undo, newChars);
	/* Restore selection */
	start [0] = translateOffset (start [0]);
	end [0] = translateOffset (end [0]);
	if (segmentsChars != null && segmentsChars.length > 0) {
		/*
		 * In addition to enforcing the required direction by prepending a UCC (LRE
		 * or RLE), also set the direction through a Window style.
		 * This is to ensure correct caret movement, and makes sense even when the
		 * UCC was added by an authentic SegmentListener.
		 */
		int auto = state & HAS_AUTO_DIRECTION;
		if (segmentsChars[0] == RLE) {
			super.updateTextDirection(SWT.RIGHT_TO_LEFT);
		} else if (segmentsChars[0] == LRE) {
			super.updateTextDirection(SWT.LEFT_TO_RIGHT);
		}
		state |= auto;
	}
	OS.SendMessage (hwndText, OS.EM_SETSEL, start [0], end [0]);
	ignoreCharacter = oldIgnoreCharacter;
	ignoreModify = oldIgnoreModify;
}

void applyListSegments () {
	int count = (int)OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
	if (count == OS.CB_ERR) return;
	boolean add = items.length != count;
	if (add) items = new String [count];
	int index = items.length;
	int selection = OS.CB_ERR;
	int cp = getCodePage ();
	String string;
	TCHAR buffer;
	if (!noSelection) {
		selection = (int)OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
	}
	while (index-- > 0) {
		buffer = null;
		if (add) {
			int length = (int)OS.SendMessage (handle, OS.CB_GETLBTEXTLEN, index, 0);
			if (length == OS.CB_ERR) error (SWT.ERROR);
			buffer = new TCHAR (cp, length + 1);
			if (OS.SendMessage (handle, OS.CB_GETLBTEXT, index, buffer) == OS.CB_ERR) return;
			items [index] = string = buffer.toString (0, length);
		} else {
			string = items [index];
		}
		if (OS.SendMessage (handle, OS.CB_DELETESTRING, index, 0) == OS.CB_ERR) return;
		if (buffer == null) buffer = new TCHAR (cp, string, true);
		if (OS.SendMessage (handle, OS.CB_INSERTSTRING, index, buffer) == OS.CB_ERR) return;
	}
	if (selection != OS.CB_ERR) {
		OS.SendMessage (handle, OS.CB_SETCURSEL, selection, 0);
	}
}

@Override
long callWindowProc (long hwnd, int msg, long wParam, long lParam) {
	if (handle == 0) return 0;
	if (hwnd == handle) {
		switch (msg) {
			case OS.WM_SIZE: {
				ignoreResize = true;
				boolean oldLockText = lockText;
				if ((style & SWT.READ_ONLY) == 0) lockText = true;
				long result = OS.CallWindowProc (ComboProc, hwnd, msg, wParam, lParam);
				if ((style & SWT.READ_ONLY) == 0) lockText = oldLockText;
				ignoreResize = false;
				return result;
			}
		}
		return OS.CallWindowProc (ComboProc, hwnd, msg, wParam, lParam);
	}
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwnd == hwndText) {
		if (lockText && msg == OS.WM_SETTEXT) {
			long hHeap = OS.GetProcessHeap ();
			int length = OS.GetWindowTextLength (handle);
			char [] buffer = new char [length + 1];
			OS.GetWindowText (handle, buffer, length + 1);
			int byteCount = buffer.length * TCHAR.sizeof;
			long pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
			OS.MoveMemory (pszText, buffer, byteCount);
			long code = OS.CallWindowProc (EditProc, hwndText, msg, wParam, pszText);
			OS.HeapFree (hHeap, 0, pszText);
			return code;
		}
		return OS.CallWindowProc (EditProc, hwnd, msg, wParam, lParam);
	}
	long hwndList = OS.GetDlgItem (handle, CBID_LIST);
	if (hwnd == hwndList) {
		return OS.CallWindowProc (ListProc, hwnd, msg, wParam, lParam);
	}
	return OS.DefWindowProc (hwnd, msg, wParam, lParam);
}

long CBTProc (long nCode, long wParam, long lParam) {
	if ((int)nCode == OS.HCBT_CREATEWND) {
		char [] buffer = new char [128];
		int length = OS.GetClassName (wParam, buffer, buffer.length);
		String className = new String (buffer, 0, length);
		if (className.equals ("Edit") || className.equals ("EDIT")) { //$NON-NLS-1$  //$NON-NLS-2$
			int bits = OS.GetWindowLong (wParam, OS.GWL_STYLE);
			OS.SetWindowLong (wParam, OS.GWL_STYLE, bits & ~OS.ES_NOHIDESEL);
		}
	}
	return OS.CallNextHookEx (cbtHook, (int)nCode, wParam, lParam);
}

@Override
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

void clearSegments (boolean applyText) {
	if (clearSegmentsCount++ != 0) return;
	if (segments == null) return;
	int nSegments = segments.length;
	if (nSegments == 0) return;
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	int limit = (int)OS.SendMessage (hwndText, OS.EM_GETLIMITTEXT, 0, 0) & 0x7fffffff;
	if (limit < LIMIT) {
		OS.SendMessage (hwndText, OS.EM_SETLIMITTEXT, Math.max (1, limit - nSegments), 0);
	}
	if (!applyText) {
		segments = null;
		return;
	}
	boolean oldIgnoreCharacter = ignoreCharacter, oldIgnoreModify = ignoreModify;
	ignoreCharacter = ignoreModify = true;
	int length = OS.GetWindowTextLength (hwndText);
	int cp = getCodePage ();
	TCHAR buffer = new TCHAR (cp, length + 1);
	if (length > 0) OS.GetWindowText (hwndText, buffer, length + 1);
	buffer = deprocessText (buffer, 0, -1, true);
	/* Get the current selection */
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (hwndText, OS.EM_GETSEL, start, end);
	start [0] = untranslateOffset (start [0]);
	end [0] = untranslateOffset (end[0]);

	segments = null;
	/*
	 * SetWindowText empties the undo buffer and disables undo in the context
	 * menu. Sending OS.EM_REPLACESEL message instead.
	 */
	OS.SendMessage (hwndText, OS.EM_SETSEL, 0, -1);
	long undo = OS.SendMessage (hwndText, OS.EM_CANUNDO, 0, 0);
	OS.SendMessage (hwndText, OS.EM_REPLACESEL, undo, buffer);
	OS.SendMessage (hwndText, OS.EM_SETSEL, start [0], end [0]);
	ignoreCharacter = oldIgnoreCharacter;
	ignoreModify = oldIgnoreModify;
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

@Override Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	if (wHint == SWT.DEFAULT) {
		long newFont, oldFont = 0;
		long hDC = OS.GetDC (handle);
		newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
		int count = (int)OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
		RECT rect = new RECT ();
		int flags = OS.DT_CALCRECT | OS.DT_NOPREFIX;
		if ((style & SWT.READ_ONLY) == 0) flags |= OS.DT_EDITCONTROL;
		int length = OS.GetWindowTextLength (handle);
		char [] buffer = new char [length + 1];
		OS.GetWindowText (handle, buffer, length + 1);
		OS.DrawText (hDC, buffer, length, rect, flags);
		width = Math.max (width, rect.right - rect.left);
		if ((style & SWT.H_SCROLL) != 0) {
			width = Math.max (width, scrollWidth);
		} else {
			for (int i=0; i<count; i++) {
				length = (int)OS.SendMessage (handle, OS.CB_GETLBTEXTLEN, i, 0);
				if (length != OS.CB_ERR) {
					if (length + 1 > buffer.length) buffer = new char [length + 1];
					int result = (int)OS.SendMessage (handle, OS.CB_GETLBTEXT, i, buffer);
					if (result != OS.CB_ERR) {
						OS.DrawText (hDC, buffer, length, rect, flags);
						width = Math.max (width, rect.right - rect.left);
					}
				}
			}
		}
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (handle, hDC);
	}
	if (hHint == SWT.DEFAULT) {
		if ((style & SWT.SIMPLE) != 0) {
			int count = (int)OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
			int itemHeight = (int)OS.SendMessage (handle, OS.CB_GETITEMHEIGHT, 0, 0);
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
		long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
		if (hwndText != 0) {
			long margins = OS.SendMessage (hwndText, OS.EM_GETMARGINS, 0, 0);
			int marginWidth = OS.LOWORD (margins) + OS.HIWORD (margins);
			width += marginWidth + 3;
		}
	}
	COMBOBOXINFO pcbi = new COMBOBOXINFO ();
	pcbi.cbSize = COMBOBOXINFO.sizeof;
	if (((style & SWT.SIMPLE) == 0) && OS.GetComboBoxInfo (handle, pcbi)) {
		width += pcbi.itemLeft + (pcbi.buttonRight - pcbi.buttonLeft);
		height = (pcbi.buttonBottom - pcbi.buttonTop) + pcbi.buttonTop * 2;
	} else {
		int border = OS.GetSystemMetrics (OS.SM_CXEDGE);
		width += OS.GetSystemMetrics (OS.SM_CXVSCROLL) + border * 2;
		int textHeight = (int)OS.SendMessage (handle, OS.CB_GETITEMHEIGHT, -1, 0);
		if ((style & SWT.DROP_DOWN) != 0) {
			height = textHeight + 6;
		} else {
			height += textHeight + 10;
		}
	}
	if ((style & SWT.SIMPLE) != 0 && (style & SWT.H_SCROLL) != 0) {
		height += OS.GetSystemMetrics (OS.SM_CYHSCROLL);
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

@Override
void createHandle () {
	/*
	* Feature in Windows.  When the selection changes in a combo box,
	* Windows draws the selection, even when the combo box does not
	* have focus.  Strictly speaking, this is the correct Windows
	* behavior because the combo box sets ES_NOHIDESEL on the text
	* control that it creates.  Despite this, it looks strange because
	* Windows also clears the selection and selects all the text when
	* the combo box gets focus.  The fix is use the CBT hook to clear
	* the ES_NOHIDESEL style bit when the text control is created.
	*/
	if ((style & (SWT.READ_ONLY | SWT.SIMPLE)) != 0) {
		super.createHandle ();
	} else {
		int threadId = OS.GetCurrentThreadId ();
		Callback cbtCallback = new Callback (this, "CBTProc", 3); //$NON-NLS-1$
		cbtHook = OS.SetWindowsHookEx (OS.WH_CBT, cbtCallback.getAddress (), 0, threadId);
		super.createHandle ();
		if (cbtHook != 0) OS.UnhookWindowsHookEx (cbtHook);
		cbtHook = 0;
		cbtCallback.dispose ();
	}
	state &= ~(CANVAS | THEME_BACKGROUND);

	if (display.comboUseDarkTheme) {
		OS.AllowDarkModeForWindow(handle, true);
		OS.SetWindowTheme(handle, "CFD\0".toCharArray(), null);
	}

	stateFlagsUsable = stateFlagsTest();

	/* Get the text and list window procs */
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText != 0 && EditProc == 0) {
		EditProc = OS.GetWindowLongPtr (hwndText, OS.GWLP_WNDPROC);
	}
	long hwndList = OS.GetDlgItem (handle, CBID_LIST);
	if (hwndList != 0 && ListProc == 0) {
		ListProc = OS.GetWindowLongPtr (hwndList, OS.GWLP_WNDPROC);
	}

	/*
	* Bug in Windows.  If the combo box has the CBS_SIMPLE style,
	* the list portion of the combo box is not drawn correctly the
	* first time, causing pixel corruption.  The fix is to ensure
	* that the combo box has been resized more than once.
	*/
	if ((style & SWT.SIMPLE) != 0) {
		int flags = OS.SWP_NOZORDER | OS.SWP_DRAWFRAME | OS.SWP_NOACTIVATE;
		OS.SetWindowPos (handle, 0, 0, 0, 0x3FFF, 0x3FFF, flags);
		OS.SetWindowPos (handle, 0, 0, 0, 0, 0, flags);
	}
}

@Override
void createWidget() {
	super.createWidget();
	visibleCount = VISIBLE_COUNT;
	if ((style & SWT.SIMPLE) == 0) {
		int itemHeight = (int)OS.SendMessage (handle, OS.CB_GETITEMHEIGHT, 0, 0);
		if (itemHeight != OS.CB_ERR && itemHeight != 0) {
			int maxHeight = 0;
			long hmonitor = OS.MonitorFromWindow (handle, OS.MONITOR_DEFAULTTONEAREST);
			MONITORINFO lpmi = new MONITORINFO ();
			lpmi.cbSize = MONITORINFO.sizeof;
			OS.GetMonitorInfo (hmonitor, lpmi);
			maxHeight = (lpmi.rcWork_bottom - lpmi.rcWork_top) / 3;
			visibleCount = Math.max(visibleCount, maxHeight / itemHeight);
		}
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

@Override
int defaultBackground () {
	return OS.GetSysColor (OS.COLOR_WINDOW);
}

TCHAR deprocessText (TCHAR text, int start, int end, boolean terminate) {
	if (text == null || segments == null) return text;
	int length = text.length();
	if (length == 0) return text;
	int nSegments = segments.length;
	if (nSegments == 0) return text;
	char [] chars;
	if (start < 0) start = 0;
	chars = text.chars;
	if (text.chars [length - 1] == 0) length--;
	if (end == -1) end = length;
	if (end > segments [0] && start <= segments [nSegments - 1]) {
		int nLeadSegments = 0;
		while (start - nLeadSegments > segments [nLeadSegments]) nLeadSegments++;
		int segmentCount = nLeadSegments;
		for (int i = start; i < end; i++) {
			if (segmentCount < nSegments && i - segmentCount == segments [segmentCount]) {
				++segmentCount;
			} else {
				chars [i - segmentCount + nLeadSegments] = chars [i];
			}
		}
		length = end - start - segmentCount + nLeadSegments;
	}
	if (start != 0 || end != length) {
		char [] newChars = new char [length];
		System.arraycopy(chars, start, newChars, 0, length);
		return new TCHAR (getCodePage (), newChars, terminate);
	}
	return text;
}

@Override
void deregister () {
	super.deregister ();
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText != 0) display.removeControl (hwndText);
	long hwndList = OS.GetDlgItem (handle, CBID_LIST);
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
	int selection = (int)OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
	if (index != selection) return;
	OS.SendMessage (handle, OS.CB_SETCURSEL, -1, 0);
	sendEvent (SWT.Modify);
	// widget could be disposed at this point
	clearSegments (false);
	clearSegmentsCount--;
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
	clearSegments (false);
	clearSegmentsCount--;
}

@Override
boolean dragDetect (long hwnd, int x, int y, boolean filter, boolean [] detect, boolean [] consume) {
	if (filter && (style & SWT.READ_ONLY) == 0) {
		long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
		if (hwndText != 0) {
			int [] start = new int [1], end = new int [1];
			OS.SendMessage (handle, OS.CB_GETEDITSEL, start, end);
			if (start [0] != end [0]) {
				long lParam = OS.MAKELPARAM (x, y);
				int position = OS.LOWORD (OS.SendMessage (hwndText, OS.EM_CHARFROMPOS, 0, lParam));
				if (start [0] <= position && position < end [0]) {
					if (super.dragDetect (hwnd, x, y, filter, detect, consume)) {
						if (consume != null) consume [0] = true;
						return true;
					}
				}
			}
			return false;
		}
	}
	return super.dragDetect (hwnd, x, y, filter, detect, consume);
}

/**
 * Returns a point describing the location of the caret relative
 * to the receiver.
 *
 * @return a point, the location of the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.8
 */
public Point getCaretLocation () {
	checkWidget ();
	return DPIUtil.autoScaleDown(getCaretLocationInPixels());
}

Point getCaretLocationInPixels () {
	/*
	* Bug in Windows.  For some reason, Windows is unable
	* to return the pixel coordinates of the last character
	* in the widget.  The fix is to temporarily insert a
	* space, query the coordinates and delete the space.
	* The selection is always an i-beam in this case because
	* this is the only time the start of the selection can
	* be equal to the last character position in the widget.
	* If EM_POSFROMCHAR fails for any other reason, return
	* pixel coordinates (0,0).
	*/
	int position = translateOffset (getCaretPosition ());
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	long caretPos = OS.SendMessage (hwndText, OS.EM_POSFROMCHAR, position, 0);
	if (caretPos == -1) {
		caretPos = 0;
		if (position >= OS.GetWindowTextLength (hwndText)) {
			int [] start = new int [1], end = new int [1];
			OS.SendMessage (hwndText, OS.EM_GETSEL, start, end);
			OS.SendMessage (hwndText, OS.EM_SETSEL, position, position);
			/*
			* Feature in Windows.  When an edit control with ES_MULTILINE
			* style that does not have the WS_VSCROLL style is full (i.e.
			* there is no space at the end to draw any more characters),
			* EM_REPLACESEL sends a WM_CHAR with a backspace character
			* to remove any further text that is added.  This is an
			* implementation detail of the edit control that is unexpected
			* and can cause endless recursion when EM_REPLACESEL is sent
			* from a WM_CHAR handler.  The fix is to ignore calling the
			* handler from WM_CHAR.
			*/
			ignoreCharacter = ignoreModify = true;
			OS.SendMessage (hwndText, OS.EM_REPLACESEL, 0, new char [] {' ', '\0'});
			caretPos = OS.SendMessage (hwndText, OS.EM_POSFROMCHAR, position, 0);
			OS.SendMessage (hwndText, OS.EM_SETSEL, position, position + 1);
			OS.SendMessage (hwndText, OS.EM_REPLACESEL, 0, new char [1]);
			ignoreCharacter = ignoreModify = false;
			OS.SendMessage (hwndText, OS.EM_SETSEL, start [0], start [0]);
			OS.SendMessage (hwndText, OS.EM_SETSEL, start [0], end [0]);
		}
	}
	POINT point = new POINT ();
	point.x = OS.GET_X_LPARAM (caretPos);
	point.y = OS.GET_Y_LPARAM (caretPos);
	OS.MapWindowPoints (hwndText, handle, point, 1);
	return new Point (point.x, point.y);
}

/**
 * Returns the character position of the caret.
 * <p>
 * Indexing is zero based.
 * </p>
 *
 * @return the position of the caret
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.8
 */
public int getCaretPosition () {
	checkWidget ();
	int [] start = new int [1], end = new int [1];
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	OS.SendMessage (hwndText, OS.EM_GETSEL, start, end);
	/*
	* In Windows, there is no API to get the position of the caret
	* when the selection is not an i-beam.  The best that can be done
	* is to query the pixel position of the current caret and compare
	* it to the pixel position of the start and end of the selection.
	*
	* NOTE:  This does not work when the i-beam belongs to another
	* control.  In this case, guess that the i-beam is at the start
	* of the selection.
	*/
	int caret = start [0];
	if (start [0] != end [0]) {
		int idThread = OS.GetWindowThreadProcessId (hwndText, null);
		GUITHREADINFO lpgui = new GUITHREADINFO ();
		lpgui.cbSize = GUITHREADINFO.sizeof;
		if (OS.GetGUIThreadInfo (idThread, lpgui)) {
			if (lpgui.hwndCaret == hwndText || lpgui.hwndCaret == 0) {
				POINT ptCurrentPos = new POINT ();
				if (OS.GetCaretPos (ptCurrentPos)) {
					long endPos = OS.SendMessage (hwndText, OS.EM_POSFROMCHAR, end [0], 0);
					if (endPos == -1) {
						long startPos = OS.SendMessage (hwndText, OS.EM_POSFROMCHAR, start [0], 0);
						int startX = OS.GET_X_LPARAM (startPos);
						if (ptCurrentPos.x > startX) caret = end [0];
					} else {
						int endX = OS.GET_X_LPARAM (endPos);
						if (ptCurrentPos.x >= endX) caret = end [0];
					}
				}
			}
		}
	}
	return untranslateOffset (caret);
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
	int length = (int)OS.SendMessage (handle, OS.CB_GETLBTEXTLEN, index, 0);
	if (length != OS.CB_ERR) {
		if (hooks (SWT.Segments) || filters (SWT.Segments) || (state & HAS_AUTO_DIRECTION) != 0) return items [index];
		char [] buffer = new char [length + 1];
		int result = (int)OS.SendMessage (handle, OS.CB_GETLBTEXT, index, buffer);
		if (result != OS.CB_ERR) return new String (buffer, 0, length);
	}
	int count = (int)OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
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
	int count = (int)OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
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
	return DPIUtil.autoScaleDown(getItemHeightInPixels());
}

int getItemHeightInPixels () {
	int result = (int)OS.SendMessage (handle, OS.CB_GETITEMHEIGHT, 0, 0);
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
	String [] result;
	int count = getItemCount ();
	result = new String [count];
	for (int i=0; i<count; i++) result [i] = getItem (i);
	return result;
}

/**
 * Returns <code>true</code> if the receiver's list is visible,
 * and <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's list's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public boolean getListVisible () {
	checkWidget ();
	if ((style & SWT.DROP_DOWN) != 0) {
		return OS.SendMessage (handle, OS.CB_GETDROPPEDSTATE, 0, 0) != 0;
	}
	return true;
}

@Override
String getNameText () {
	return getText ();
}

/**
 * Marks the receiver's list as visible if the argument is <code>true</code>,
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
 *
 * @since 3.4
 */
public void setListVisible (boolean visible) {
	checkWidget ();
	OS.SendMessage (handle, OS.CB_SHOWDROPDOWN, visible ? 1 : 0, 0);
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
@Override
public int getOrientation () {
	return super.getOrientation ();
}

Event getSegments (String string) {
	Event event = null;
	if (hooks (SWT.Segments) || filters (SWT.Segments)) {
		event = new Event ();
		event.text = string;
		sendEvent (SWT.Segments, event);
		if (event != null && event.segments != null) {
			for (int i = 1, segmentCount = event.segments.length, lineLength = string == null ? 0 : string.length(); i < segmentCount; i++) {
				if (event.segments[i] < event.segments[i - 1] || event.segments[i] > lineLength) {
					SWT.error (SWT.ERROR_INVALID_ARGUMENT);
				}
			}
		}
	}
	if ((state & HAS_AUTO_DIRECTION) != 0) {
		int direction = BidiUtil.resolveTextDirection(string);
		if (direction == SWT.NONE) {
			/*
			 * Force adding a UCC even when no strong characters are found.
			 * Otherwise, the widget would keep the old direction, which might be
			 * inappropriate for the new text.
			 */
			direction = (style & SWT.RIGHT_TO_LEFT) != 0 ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT;
		}
		int [] oldSegments = null;
		char [] oldSegmentsChars = null;
		if (event == null) {
			event = new Event ();
		} else {
			oldSegments = event.segments;
			oldSegmentsChars = event.segmentsChars;
		}
		int nSegments = oldSegments == null ? 0 : oldSegments.length;
		event.segments = new int [nSegments + 1];
		event.segmentsChars = new char [nSegments + 1];
		if (oldSegments != null) {
			System.arraycopy(oldSegments, 0, event.segments, 1, nSegments);
		}
		if (oldSegmentsChars != null) {
			System.arraycopy(oldSegmentsChars, 0, event.segmentsChars, 1, nSegments);
		}
		event.segments [0] = 0;
		event.segmentsChars [0] = direction == SWT.RIGHT_TO_LEFT ? RLE : LRE;
	}
	return event;
}

String getSegmentsText (String text, Event event) {
	if (text == null || event == null) return text;
	int[] segments = event.segments;
	if (segments == null) return text;
	int nSegments = segments.length;
	if (nSegments == 0) return text;
	char[] segmentsChars = /*event == null ? this.segmentsChars : */event.segmentsChars;
	int length = text.length();
	char[] oldChars = new char[length];
	text.getChars (0, length, oldChars, 0);
	char[] newChars = new char[length + nSegments];
	int charCount = 0, segmentCount = 0;
	char defaultSeparator = getOrientation () == SWT.RIGHT_TO_LEFT ? RTL_MARK : LTR_MARK;
	while (charCount < length) {
		if (segmentCount < nSegments && charCount == segments[segmentCount]) {
			char separator = segmentsChars != null && segmentsChars.length > segmentCount ? segmentsChars[segmentCount] : defaultSeparator;
			newChars[charCount + segmentCount++] = separator;
		} else {
			newChars[charCount + segmentCount] = oldChars[charCount++];
		}
	}
	while (segmentCount < nSegments) {
		segments[segmentCount] = charCount;
		char separator = segmentsChars != null && segmentsChars.length > segmentCount ? segmentsChars[segmentCount] : defaultSeparator;
		newChars[charCount + segmentCount++] = separator;
	}
	return new String(newChars, 0, newChars.length);
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
	return new Point (untranslateOffset (start [0]), untranslateOffset (end [0]));
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
	return (int)OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
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
	if (segments != null) {
		buffer = deprocessText (buffer, 0, -1, false);
		return buffer.toString ();
	}

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
	return DPIUtil.autoScaleDown(getTextHeightInPixels());
}

int getTextHeightInPixels () {
	COMBOBOXINFO pcbi = new COMBOBOXINFO ();
	pcbi.cbSize = COMBOBOXINFO.sizeof;
	if (((style & SWT.SIMPLE) == 0) && OS.GetComboBoxInfo (handle, pcbi)) {
		return (pcbi.buttonBottom - pcbi.buttonTop) + pcbi.buttonTop * 2;
	}
	int result = (int)OS.SendMessage (handle, OS.CB_GETITEMHEIGHT, -1, 0);
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
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText == 0) return LIMIT;
	int limit = (int)OS.SendMessage (hwndText, OS.EM_GETLIMITTEXT, 0, 0) & 0x7FFFFFFF;
	if (segments != null && limit < LIMIT) limit = Math.max (1, limit - segments.length);
	return limit;
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

@Override
boolean hasFocus () {
	long hwndFocus = OS.GetFocus ();
	if (hwndFocus == handle) return true;
	if (hwndFocus == 0) return false;
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndFocus == hwndText) return true;
	long hwndList = OS.GetDlgItem (handle, CBID_LIST);
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
	int count = (int)OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
	if (!(0 <= start && start < count)) return -1;
	int index = start - 1, last = 0;
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	do {
		index = (int)OS.SendMessage (handle, OS.CB_FINDSTRINGEXACT, last = index, buffer);
		if (index == OS.CB_ERR || index <= last) return -1;
	} while (!string.equals (getItem (index)));
	return index;
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

void stateFlagsAdd(int flags) {
	final long tagCBoxPtr = OS.GetWindowLongPtr(handle, 0);
	/*
	 * Bug 550423: When non-XP-theme COMMCTL32.DLL gets loaded, undocumented
	 * internal data is not there. We do not support that and in such case
	 * GetWindowLongPtr function fails and return zero.
	 */
	if (tagCBoxPtr == 0) return;
	final long stateFlagsPtr = tagCBoxPtr + stateFlagsOffset;

	int stateFlags[] = new int[1];
	OS.MoveMemory(stateFlags, stateFlagsPtr, 4);
	stateFlags[0] |= flags;
	OS.MoveMemory(stateFlagsPtr, stateFlags, 4);
}

/*
 * Verify that undocumented internal data is in expected location.
 * The test is performed at creation time, when the value of state flags is predictable.
 * For simplicity, only SWT.READ_ONLY combos are handled.
 */
boolean stateFlagsTest() {
	final long tagCBoxPtr = OS.GetWindowLongPtr(handle, 0);
	/*
	 * Bug 550423: When non-XP-theme COMMCTL32.DLL gets loaded, undocumented
	 * internal data is not there. We do not support that and in such case
	 * GetWindowLongPtr function fails and return zero.
	 */
	if (tagCBoxPtr == 0) return false;
	final long stateFlagsPtr = tagCBoxPtr + stateFlagsOffset;

	int stateFlags[] = new int[1];
	OS.MoveMemory(stateFlags, stateFlagsPtr, 4);

	/*
	 * 0x00000002 is unknown
	 * 0x00002000 is set in WM_NCCREATE
	 * 0x00004000 means CBS_DROPDOWNLIST (SWT.READ_ONLY)
	 * 0x02000000 is set in WM_NCCREATE and reset after first WM_PAINT
	 */
	return (stateFlags[0] == 0x02006002);
}

@Override
void register () {
	super.register ();
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText != 0) display.addControl (hwndText, this);
	long hwndList = OS.GetDlgItem (handle, CBID_LIST);
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
	remove (index, true);
}

void remove (int index, boolean notify) {
	char [] buffer = null;
	if ((style & SWT.H_SCROLL) != 0) {
		int length = (int)OS.SendMessage (handle, OS.CB_GETLBTEXTLEN, index, 0);
		if (length == OS.CB_ERR) {
			int count = (int)OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
			if (0 <= index && index < count) error (SWT.ERROR_ITEM_NOT_REMOVED);
			error (SWT.ERROR_INVALID_RANGE);
		}
		buffer = new char [length + 1];
		int result = (int)OS.SendMessage (handle, OS.CB_GETLBTEXT, index, buffer);
		if (result == OS.CB_ERR) {
			int count = (int)OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
			if (0 <= index && index < count) error (SWT.ERROR_ITEM_NOT_REMOVED);
			error (SWT.ERROR_INVALID_RANGE);
		}
	}
	int length = OS.GetWindowTextLength (handle);
	int code = (int)OS.SendMessage (handle, OS.CB_DELETESTRING, index, 0);
	if (code == OS.CB_ERR) {
		int count = (int)OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
		if (0 <= index && index < count) error (SWT.ERROR_ITEM_NOT_REMOVED);
		error (SWT.ERROR_INVALID_RANGE);
	}
	else if (code == 0) {
		/*
		 * Bug in Windows, when combo had exactly one item, that was
		 * currently selected & is removed, the combo box does not clear the
		 * text area. The fix is to reset contents of the Combo. Bug#440671
		 */
		OS.SendMessage (handle, OS.CB_RESETCONTENT, 0, 0);
	}
	if ((style & SWT.H_SCROLL) != 0) setScrollWidth (buffer, true);
	if (notify && length != OS.GetWindowTextLength (handle)) {
		sendEvent (SWT.Modify);
		if (isDisposed ()) return;
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
	int count = (int)OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
	if (!(0 <= start && start <= end && end < count)) {
		error (SWT.ERROR_INVALID_RANGE);
	}
	int textLength = OS.GetWindowTextLength (handle);
	RECT rect = null;
	long hDC = 0, oldFont = 0, newFont = 0;
	int newWidth = 0;
	if ((style & SWT.H_SCROLL) != 0) {
		rect = new RECT ();
		hDC = OS.GetDC (handle);
		newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	}
	int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
	for (int i=start; i<=end; i++) {
		char [] buffer = null;
		if ((style & SWT.H_SCROLL) != 0) {
			int length = (int)OS.SendMessage (handle, OS.CB_GETLBTEXTLEN, start, 0);
			if (length == OS.CB_ERR) break;
			buffer = new char [length + 1];
			int result = (int)OS.SendMessage (handle, OS.CB_GETLBTEXT, start, buffer);
			if (result == OS.CB_ERR) break;
		}
		int result = (int)OS.SendMessage (handle, OS.CB_DELETESTRING, start, 0);
		if (result == OS.CB_ERR) {
			error (SWT.ERROR_ITEM_NOT_REMOVED);
		}
		else if (result == 0) {
			/*
			 * Bug in Windows, when combo had exactly one item, that was
			 * currently selected & is removed, the combo box does not clear the
			 * text area. The fix is to reset contents of the Combo. Bug#440671
			 */
			OS.SendMessage (handle, OS.CB_RESETCONTENT, 0, 0);
		}
		if ((style & SWT.H_SCROLL) != 0) {
			OS.DrawText (hDC, buffer, -1, rect, flags);
			newWidth = Math.max (newWidth, rect.right - rect.left);
		}
	}
	if ((style & SWT.H_SCROLL) != 0) {
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (handle, hDC);
		setScrollWidth (newWidth, false);
	}
	if (textLength != OS.GetWindowTextLength (handle)) {
		sendEvent (SWT.Modify);
		if (isDisposed ()) return;
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
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void removeAll () {
	checkWidget ();
	OS.SendMessage (handle, OS.CB_RESETCONTENT, 0, 0);
	sendEvent (SWT.Modify);
	if (isDisposed ()) return;
	if ((style & SWT.H_SCROLL) != 0) setScrollWidth (0);
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
 * @see SegmentEvent
 * @see SegmentListener
 * @see #addSegmentListener
 *
 * @since 3.103
 */
public void removeSegmentListener (SegmentListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	eventTable.unhook (SWT.Segments, listener);
	int selection = OS.CB_ERR;
	if (!noSelection) {
		selection = (int)OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
	}
	clearSegments (true);
	applyEditSegments ();
	applyListSegments ();
	if (selection != OS.CB_ERR) {
		OS.SendMessage (handle, OS.CB_SETCURSEL, selection, 0);
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

@Override
boolean sendKeyEvent (int type, int msg, long wParam, long lParam, Event event) {
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
	* Feature in Windows.  If the left button is down in
	* the text widget, it refuses the character.  The fix
	* is to detect this case and avoid sending a verify
	* event.
	*/
	if (OS.GetKeyState (OS.VK_LBUTTON) < 0) {
		if (OS.GetDlgItem (handle, CBID_EDIT) == OS.GetCapture()) return true;
	}

	/* Verify the character */
	String oldText = "";
	int [] start = new int [1], end = new int [1];
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText == 0) return true;
	OS.SendMessage (hwndText, OS.EM_GETSEL, start, end);
	switch (key) {
		case 0x08:	/* Bs */
			if (start [0] == end [0]) {
				if (start [0] == 0) return true;
				start [0] = start [0] - 1;
				start [0] = Math.max (start [0], 0);
			}
			break;
		case 0x7F:	/* Del */
			if (start [0] == end [0]) {
				int length = OS.GetWindowTextLength (hwndText);
				if (start [0] == length) return true;
				end [0] = end [0] + 1;
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
	int count = (int)OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
	if (0 <= index && index < count) {
		int selection = (int)OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
		int code = (int)OS.SendMessage (handle, OS.CB_SETCURSEL, index, 0);
		if (code != OS.CB_ERR && code != selection) {
			sendEvent (SWT.Modify);
			// widget could be disposed at this point
		}
	}
}

@Override
void setBackgroundImage (long hBitmap) {
	super.setBackgroundImage (hBitmap);
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText != 0) OS.InvalidateRect (hwndText, null, true);
	long hwndList = OS.GetDlgItem (handle, CBID_LIST);
	if (hwndList != 0) OS.InvalidateRect (hwndList, null, true);
}

@Override
void setBackgroundPixel (int pixel) {
	super.setBackgroundPixel (pixel);
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText != 0) OS.InvalidateRect (hwndText, null, true);
	long hwndList = OS.GetDlgItem (handle, CBID_LIST);
	if (hwndList != 0) OS.InvalidateRect (hwndList, null, true);
}

@Override
void setBoundsInPixels (int x, int y, int width, int height, int flags) {
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
		int visibleCount = getItemCount() == 0 ? VISIBLE_COUNT : this.visibleCount;
		height = getTextHeightInPixels () + (getItemHeightInPixels () * visibleCount) + 2;
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
		OS.SetWindowPos (handle, 0, x, y, width, height, flags);
	} else {
		super.setBoundsInPixels (x, y, width, height, flags);
	}
}

@Override
public void setFont (Font font) {
	checkWidget ();

	/*
	* Feature in Windows.  For some reason, in a editable combo box,
	* when WM_SETFONT is used to set the font of the control
	* and the current text does not match an item in the
	* list, Windows selects the item that most closely matches the
	* contents of the combo.  The fix is to lock the current text
	* by ignoring all WM_SETTEXT messages during processing of
	* WM_SETFONT.
	*/
	boolean oldLockText = lockText;
	if ((style & SWT.READ_ONLY) == 0) lockText = true;
	super.setFont (font);
	if ((style & SWT.READ_ONLY) == 0) lockText = oldLockText;
	if ((style & SWT.H_SCROLL) != 0) setScrollWidth ();
}

@Override
void setForegroundPixel (int pixel) {
	super.setForegroundPixel (pixel);
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText != 0) OS.InvalidateRect (hwndText, null, true);
	long hwndList = OS.GetDlgItem (handle, CBID_LIST);
	if (hwndList != 0) OS.InvalidateRect (hwndList, null, true);
}

/**
 * Sets the text of the item in the receiver's list at the given
 * zero-relative index to the string argument.
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
	int selection = getSelectionIndex ();
	remove (index, false);
	if (isDisposed ()) return;
	add (string, index);
	if (selection != -1) select (selection);
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
public void setItems (String... items) {
	checkWidget ();
	if (items == null) error (SWT.ERROR_NULL_ARGUMENT);
	for (String item : items) {
		if (item == null) error (SWT.ERROR_INVALID_ARGUMENT);
	}
	RECT rect = null;
	long hDC = 0, oldFont = 0, newFont = 0;
	int newWidth = 0;
	if ((style & SWT.H_SCROLL) != 0) {
		rect = new RECT ();
		hDC = OS.GetDC (handle);
		newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
		setScrollWidth (0);
	}
	OS.SendMessage (handle, OS.CB_RESETCONTENT, 0, 0);
	int codePage = getCodePage ();
	for (String item : items) {
		TCHAR buffer = new TCHAR (codePage, item, true);
		int code = (int)OS.SendMessage (handle, OS.CB_ADDSTRING, 0, buffer);
		if (code == OS.CB_ERR) error (SWT.ERROR_ITEM_NOT_ADDED);
		if (code == OS.CB_ERRSPACE) error (SWT.ERROR_ITEM_NOT_ADDED);
		if ((style & SWT.H_SCROLL) != 0) {
			int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
			OS.DrawText (hDC, buffer, -1, rect, flags);
			newWidth = Math.max (newWidth, rect.right - rect.left);
		}
	}
	if ((style & SWT.H_SCROLL) != 0) {
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (handle, hDC);
		setScrollWidth (newWidth + 3);
	}
	sendEvent (SWT.Modify);
	// widget could be disposed at this point
}

/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
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
@Override
public void setOrientation (int orientation) {
	super.setOrientation (orientation);
}

void setScrollWidth () {
	int newWidth = 0;
	RECT rect = new RECT ();
	long newFont, oldFont = 0;
	long hDC = OS.GetDC (handle);
	newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	int count = (int)OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
	int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
	for (int i=0; i<count; i++) {
		int length = (int)OS.SendMessage (handle, OS.CB_GETLBTEXTLEN, i, 0);
		if (length != OS.CB_ERR) {
			char [] buffer = new char [length + 1];
			int result = (int)OS.SendMessage (handle, OS.CB_GETLBTEXT, i, buffer);
			if (result != OS.CB_ERR) {
				OS.DrawText (hDC, buffer, -1, rect, flags);
				newWidth = Math.max (newWidth, rect.right - rect.left);
			}
		}
	}
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	setScrollWidth (newWidth + 3);
}

void setScrollWidth (int scrollWidth) {
	this.scrollWidth = scrollWidth;
	if ((style & SWT.SIMPLE) != 0) {
		OS.SendMessage (handle, OS.CB_SETHORIZONTALEXTENT, scrollWidth, 0);
		return;
	}
	boolean scroll = false;
	int count = (int)OS.SendMessage (handle, OS.CB_GETCOUNT, 0, 0);
	if (count > 3) {
		long hmonitor = OS.MonitorFromWindow (handle, OS.MONITOR_DEFAULTTONEAREST);
		MONITORINFO lpmi = new MONITORINFO ();
		lpmi.cbSize = MONITORINFO.sizeof;
		OS.GetMonitorInfo (hmonitor, lpmi);
		int maxWidth = (lpmi.rcWork_right - lpmi.rcWork_left) / 4;
		scroll = scrollWidth > maxWidth;
	}
	/*
	* Feature in Windows.  For some reason, in a editable combo box,
	* when CB_SETDROPPEDWIDTH is used to set the width of the drop
	* down list and the current text does not match an item in the
	* list, Windows selects the item that most closely matches the
	* contents of the combo.  The fix is to lock the current text
	* by ignoring all WM_SETTEXT messages during processing of
	* CB_SETDROPPEDWIDTH.
	*/
	boolean oldLockText = lockText;
	if ((style & SWT.READ_ONLY) == 0) lockText = true;
	if (scroll) {
		OS.SendMessage (handle, OS.CB_SETDROPPEDWIDTH, 0, 0);
		OS.SendMessage (handle, OS.CB_SETHORIZONTALEXTENT, scrollWidth, 0);
	} else {
		scrollWidth += OS.GetSystemMetrics (OS.SM_CYHSCROLL);
		OS.SendMessage (handle, OS.CB_SETDROPPEDWIDTH, scrollWidth, 0);
		OS.SendMessage (handle, OS.CB_SETHORIZONTALEXTENT, 0, 0);
	}
	if ((style & SWT.READ_ONLY) == 0) lockText = oldLockText;
}

void setScrollWidth (char[] buffer, boolean grow) {
	RECT rect = new RECT ();
	long newFont, oldFont = 0;
	long hDC = OS.GetDC (handle);
	newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
	OS.DrawText (hDC, buffer, -1, rect, flags);
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	setScrollWidth (rect.right - rect.left, grow);
}

void setScrollWidth (int newWidth, boolean grow) {
	if (grow) {
		if (newWidth <= scrollWidth) return;
		setScrollWidth (newWidth + 3);
	} else {
		if (newWidth < scrollWidth) return;
		setScrollWidth ();
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
	int start = translateOffset (selection.x), end = translateOffset (selection.y);
	long bits = OS.MAKELPARAM (start, end);
	OS.SendMessage (handle, OS.CB_SETEDITSEL, 0, bits);
}

/**
 * Sets the contents of the receiver's text field to the
 * given string.
 * <p>
 * This call is ignored when the receiver is read only and
 * the given string is not in the receiver's list.
 * </p>
 * <p>
 * Note: The text field in a <code>Combo</code> is typically
 * only capable of displaying a single line of text. Thus,
 * setting the text to a string containing line breaks or
 * other special characters will probably cause it to
 * display incorrectly.
 * </p><p>
 * Also note, if control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
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
	clearSegments (false);
	int limit = LIMIT;
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText != 0) {
		limit = (int)OS.SendMessage (hwndText, OS.EM_GETLIMITTEXT, 0, 0) & 0x7FFFFFFF;
	}
	if (string.length () > limit) string = string.substring (0, limit);
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	if (OS.SetWindowText (handle, buffer)) {
		applyEditSegments ();
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
	if (segments != null && limit > 0) {
		OS.SendMessage (handle, OS.CB_LIMITTEXT, limit + Math.min (segments.length, LIMIT - limit), 0);
	} else {
		OS.SendMessage (handle, OS.CB_LIMITTEXT, limit, 0);
	}
}

@Override
void setToolTipText (Shell shell, String string) {
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	long hwndList = OS.GetDlgItem (handle, CBID_LIST);
	if (hwndText != 0) shell.setToolTipText (hwndText, string);
	if (hwndList != 0) shell.setToolTipText (hwndList, string);
	shell.setToolTipText (handle, string);
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
	updateDropDownHeight ();
}

@Override
void subclass () {
	super.subclass ();
	long newProc = display.windowProc;
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText != 0) {
		OS.SetWindowLongPtr (hwndText, OS.GWLP_WNDPROC, newProc);
	}
	long hwndList = OS.GetDlgItem (handle, CBID_LIST);
	if (hwndList != 0) {
		OS.SetWindowLongPtr (hwndList, OS.GWLP_WNDPROC, newProc);
	}
}

int translateOffset (int offset) {
	if (segments == null) return offset;
	for (int i = 0, nSegments = segments.length; i < nSegments && offset - i >= segments[i]; i++) {
		offset++;
	}
	return offset;
}

@Override
boolean translateTraversal (MSG msg) {
	/*
	* When the combo box is dropped down, allow return
	* to select an item in the list and escape to close
	* the combo box.
	*/
	switch ((int)(msg.wParam)) {
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

@Override
boolean traverseEscape () {
	if ((style & SWT.DROP_DOWN) != 0) {
		if (OS.SendMessage (handle, OS.CB_GETDROPPEDSTATE, 0, 0) != 0) {
			OS.SendMessage (handle, OS.CB_SHOWDROPDOWN, 0, 0);
			return true;
		}
	}
	return super.traverseEscape ();
}

@Override
boolean traverseReturn () {
	if ((style & SWT.DROP_DOWN) != 0) {
		if (OS.SendMessage (handle, OS.CB_GETDROPPEDSTATE, 0, 0) != 0) {
			OS.SendMessage (handle, OS.CB_SHOWDROPDOWN, 0, 0);
			return true;
		}
	}
	return super.traverseReturn ();
}

@Override
void unsubclass () {
	super.unsubclass ();
	long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
	if (hwndText != 0 && EditProc != 0) {
		OS.SetWindowLongPtr (hwndText, OS.GWLP_WNDPROC, EditProc);
	}
	long hwndList = OS.GetDlgItem (handle, CBID_LIST);
	if (hwndList != 0 && ListProc != 0) {
		OS.SetWindowLongPtr (hwndList, OS.GWLP_WNDPROC, ListProc);
	}
}

int untranslateOffset (int offset) {
	if (segments == null) return offset;
	for (int i = 0, nSegments = segments.length; i < nSegments && offset > segments[i]; i++) {
		offset--;
	}
	return offset;
}

void updateDropDownHeight () {
	/*
	* Feature in Windows.  If the combo box has the CBS_DROPDOWN
	* or CBS_DROPDOWNLIST style, Windows uses the height that the
	* programmer sets in SetWindowPos () to control height of the
	* drop down list.  See #setBounds() for more details.
	*/
	if ((style & SWT.DROP_DOWN) != 0) {
		RECT rect = new RECT ();
		OS.SendMessage (handle, OS.CB_GETDROPPEDCONTROLRECT, 0, rect);
		int visibleCount = getItemCount() == 0 ? VISIBLE_COUNT : this.visibleCount;
		int height = getTextHeightInPixels () + (getItemHeightInPixels () * visibleCount) + 2;
		if (height != (rect.bottom - rect.top)) {
			forceResize ();
			OS.GetWindowRect (handle, rect);
			int flags = OS.SWP_NOMOVE | OS.SWP_NOZORDER | OS.SWP_DRAWFRAME | OS.SWP_NOACTIVATE;
			OS.SetWindowPos (handle, 0, 0, 0, rect.right - rect.left, height, flags);
		}
	}
}

void updateDropDownTheme () {
	COMBOBOXINFO pcbi = new COMBOBOXINFO ();
	pcbi.cbSize = COMBOBOXINFO.sizeof;
	if (!OS.GetComboBoxInfo(handle, pcbi))
		return;

	if (pcbi.hwndList == 0)
		return;

	maybeEnableDarkSystemTheme(pcbi.hwndList);
}

@Override
boolean updateTextDirection(int textDirection) {
	if (super.updateTextDirection(textDirection)) {
		clearSegments (true);
		applyEditSegments ();
		applyListSegments ();
		return true;
	}
	return false;
}

@Override
void updateOrientation () {
	int bits  = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		bits |= OS.WS_EX_LAYOUTRTL;
	} else {
		bits &= ~OS.WS_EX_LAYOUTRTL;
	}
	bits &= ~OS.WS_EX_RTLREADING;
	OS.SetWindowLong (handle, OS.GWL_EXSTYLE, bits);
	long hwndText = 0, hwndList = 0;
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
		OS.SetWindowPos (hwndText, 0, 0, 0, width - 1, height - 1, uFlags);
		OS.SetWindowPos (handle, 0, 0, 0, widthCombo - 1, heightCombo - 1, uFlags);
		OS.SetWindowPos (hwndText, 0, 0, 0, width, height, uFlags);
		OS.SetWindowPos (handle, 0, 0, 0, widthCombo, heightCombo, uFlags);
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
	event.start = untranslateOffset (event.start);
	event.end = untranslateOffset (event.end);
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

@Override
int widgetExtStyle () {
	return super.widgetExtStyle () & ~OS.WS_EX_NOINHERITLAYOUT;
}

@Override
int widgetStyle () {
	int bits = super.widgetStyle () | OS.CBS_AUTOHSCROLL | OS.CBS_NOINTEGRALHEIGHT | OS.WS_HSCROLL |OS.WS_VSCROLL;
	if ((style & SWT.SIMPLE) != 0) return bits | OS.CBS_SIMPLE;
	if ((style & SWT.READ_ONLY) != 0) return bits | OS.CBS_DROPDOWNLIST;
	return bits | OS.CBS_DROPDOWN;
}

@Override
TCHAR windowClass () {
	return ComboClass;
}

@Override
long windowProc () {
	return ComboProc;
}

@Override
long windowProc (long hwnd, int msg, long wParam, long lParam) {
	if (handle == 0) return 0;
	if (hwnd != handle) {
		long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
		long hwndList = OS.GetDlgItem (handle, CBID_LIST);
		if ((hwndText != 0 && hwnd == hwndText) || (hwndList != 0 && hwnd == hwndList)) {
			LRESULT result = null;
			boolean processSegments = false, redraw = false;
			switch (msg) {
				/* Keyboard messages */
				case OS.WM_CHAR:
					processSegments = (hooks (SWT.Segments) || filters (SWT.Segments) || ((state & HAS_AUTO_DIRECTION) != 0)) && !ignoreCharacter && OS.GetKeyState (OS.VK_CONTROL) >= 0 && OS.GetKeyState (OS.VK_MENU) >= 0;
					result = wmChar (hwnd, wParam, lParam);
					break;
				case OS.WM_IME_CHAR:	result = wmIMEChar (hwnd, wParam, lParam); break;
				case OS.WM_KEYDOWN:
					processSegments = wParam == OS.VK_DELETE && (hooks (SWT.Segments) || filters (SWT.Segments) || ((state & HAS_AUTO_DIRECTION) != 0));
					result = wmKeyDown (hwnd, wParam, lParam);
					break;
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
				case OS.EM_CANUNDO:
					if (hooks (SWT.Segments) || filters (SWT.Segments) || ((state & HAS_AUTO_DIRECTION) != 0)) return 0;
					break;
				case OS.WM_UNDO:
				case OS.EM_UNDO:
					if (hooks (SWT.Segments) || filters (SWT.Segments) || ((state & HAS_AUTO_DIRECTION) != 0)) return 0;
				case OS.WM_COPY:
				case OS.WM_CLEAR:
				case OS.WM_CUT:
				case OS.WM_PASTE:
					processSegments = hooks (SWT.Segments) || filters (SWT.Segments) || ((state & HAS_AUTO_DIRECTION) != 0);
				case OS.WM_SETTEXT:
					if (hwnd == hwndText) {
						result = wmClipboard (hwnd, msg, wParam, lParam);
					}
					break;
			}
			if (result != null) return result.value;

			if (processSegments) {
				if (getDrawing () && OS.IsWindowVisible (hwndText)) {
					redraw = true;
					OS.DefWindowProc (hwndText, OS.WM_SETREDRAW, 0, 0);
				}
				clearSegments (true);
				long code = callWindowProc (hwnd, msg, wParam, lParam);
				applyEditSegments ();
				if (redraw) {
					OS.DefWindowProc (hwndText, OS.WM_SETREDRAW, 1, 0);
					OS.InvalidateRect (hwndText, null, true);
					/*
					 * When setting longer text on AUTO_TEXT_DIRECTION styled Combo orientation:
					 * Combo should automatically scrolls selection's end into view. We need to
					 * force it to do such scrolling, to make sure Caret no longer goes out of view.
					 * For more details refer bug 575171.
					 */
					forceScrollingToCaret();
				}
				return code;
			}
			return callWindowProc (hwnd, msg, wParam, lParam);
		}
	}
	switch (msg) {
		case OS.CB_SETCURSEL: {
			long code = OS.CB_ERR;
			int index = (int) wParam;
			if ((style & SWT.READ_ONLY) != 0) {
				if (hooks (SWT.Verify) || filters (SWT.Verify)) {
					String oldText = getText (), newText = null;
					if (wParam == -1) {
						newText = "";
					} else {
						if (0 <= wParam && wParam < getItemCount ()) {
							newText = getItem ((int)wParam);
						}
					}
					if (newText != null && !newText.equals (oldText)) {
						int length = OS.GetWindowTextLength (handle);
						oldText = newText;
						newText = verifyText (newText, 0, length, null);
						if (newText == null) return 0;
						if (!newText.equals (oldText)) {
							index = indexOf (newText);
							if (index != -1 && index != wParam) {
								return callWindowProc (handle, OS.CB_SETCURSEL, index, lParam);
							}
						}
					}
				}
			}
			if (hooks (SWT.Segments) || filters (SWT.Segments) || ((state & HAS_AUTO_DIRECTION) != 0)) {
				code = super.windowProc (hwnd, msg, wParam, lParam);
				if (!(code == OS.CB_ERR || code == OS.CB_ERRSPACE)) {
					Event event = getSegments (items [index]);
					segments = event != null ? event.segments : null;
					if (event != null && event.segmentsChars != null) {
						int auto = state & HAS_AUTO_DIRECTION;
						if (event.segmentsChars[0] == RLE) {
							super.updateTextDirection(SWT.RIGHT_TO_LEFT);
						} else if (event.segmentsChars[0] == LRE) {
							super.updateTextDirection(SWT.LEFT_TO_RIGHT);
						}
						state |= auto;
					}
					return code;
				}
			}
			break;
		}
		case OS.CB_ADDSTRING:
		case OS.CB_INSERTSTRING:
		case OS.CB_FINDSTRINGEXACT:
			if (lParam != 0 && (hooks (SWT.Segments) || filters (SWT.Segments) || ((state & HAS_AUTO_DIRECTION) != 0))) {
				long code = OS.CB_ERR;
				int length = OS.wcslen (lParam);
				TCHAR buffer = new TCHAR (getCodePage (), length);
				OS.MoveMemory (buffer, lParam, buffer.length () * TCHAR.sizeof);
				String string = buffer.toString (0, length);
				Event event = getSegments (string);
				if (event != null && event.segments != null) {
					buffer = new TCHAR (getCodePage (), getSegmentsText (string, event), true);
					long hHeap = OS.GetProcessHeap ();
					length = buffer.length() * TCHAR.sizeof;
					long pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, length);
					OS.MoveMemory (pszText, buffer, length);
					code = super.windowProc (hwnd, msg, wParam, pszText);
					OS.HeapFree (hHeap, 0, pszText);
				}
				if (msg == OS.CB_ADDSTRING || msg == OS.CB_INSERTSTRING) {
					int index = msg == OS.CB_ADDSTRING ? items.length : (int) wParam;
					String [] newItems = new String [items.length + 1];
					System.arraycopy (items, 0, newItems, 0, index);
					newItems [index] = string;
					System.arraycopy (items, index, newItems, index + 1, items.length - index);
					items = newItems;
				}
				if (code != OS.CB_ERR && code != OS.CB_ERRSPACE) return code;
			}
			break;
		case OS.CB_DELETESTRING: {
			if (hooks (SWT.Segments) || filters (SWT.Segments) || ((state & HAS_AUTO_DIRECTION) != 0)) {
				long code = super.windowProc (hwnd, msg, wParam, lParam);
				if (code != OS.CB_ERR && code != OS.CB_ERRSPACE) {
					int index = (int) wParam;
					if (items.length == 1) {
						items = new String[0];
					} else if (items.length > 1) {
						String [] newItems = new String [items.length - 1];
						System.arraycopy (items, 0, newItems, 0, index);
						System.arraycopy (items, index + 1, newItems, index, items.length - index - 1);
						items = newItems;
					}
					if (!noSelection) {
						index = (int)OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
						if (index == wParam) {
							clearSegments (false);
							applyEditSegments ();
						}
					}
				}
				return code;
			}
			break;
		}
		case OS.CB_RESETCONTENT: {
			if (hooks (SWT.Segments) || filters (SWT.Segments) || ((state & HAS_AUTO_DIRECTION) != 0)) {
				if (items.length > 0) items = new String [0];
				clearSegments (false);
				applyEditSegments ();
			}
			break;
		}
	}
	return super.windowProc (hwnd, msg, wParam, lParam);
}

@Override
LRESULT wmColorChild (long wParam, long lParam) {
	LRESULT result = super.wmColorChild(wParam, lParam);

	/*
	 * CBS_DROPDOWNLIST (SWT.READ_ONLY) comboboxes ignore results of WM_CTLCOLORxxx.
	 * This prevents SWT from setting custom background / text color.
	 * In Windows function 'comctl32!ComboBox_InternalUpdateEditWindow' there are two main branches:
	 * 'DrawThemeText' branch
	 *   Ignores any SetTextColor / SetBkColor.
	 *   Ignores brush returned from WM_CTLCOLORxxx.
	 *   Keeps any background that was painted during WM_CTLCOLORxxx.
	 * 'ExtTextOut' branch
	 *   Uses pre-selected SetTextColor / SetBkColor.
	 *   Ignores brush returned from WM_CTLCOLORxxx.
	 *   Overwrites background with color in SetBkColor.
	 * This undocumented hack forces combobox to use 'ExtTextOut' branch.
	 * The flag is reset after every WM_PAINT, so it's set in every WM_CTLCOLORxxx.
	 * Since 'ExtTextOut' always paints background, hack is not activated if not needed
	 * to avoid changes in visual appearance of comboboxes with default colors.
	 */
	final boolean isReadonly = ((style & SWT.READ_ONLY) != 0);
	final boolean isCustomColors = (result != null);
	if (isReadonly && isCustomColors && stateFlagsUsable) {
		stateFlagsAdd(stateFlagsFirstPaint);
	}

	return result;
}

@Override
LRESULT WM_CTLCOLOR (long wParam, long lParam) {
	return wmColorChild (wParam, lParam);
}

@Override
LRESULT WM_GETDLGCODE (long wParam, long lParam) {
	long code = callWindowProc (handle, OS.WM_GETDLGCODE, wParam, lParam);
	return new LRESULT (code | OS.DLGC_WANTARROWS);
}

@Override
LRESULT WM_KILLFOCUS (long wParam, long lParam) {
	/*
	* Return NULL - Focus notification is
	* done in WM_COMMAND by CBN_KILLFOCUS.
	*/
	return null;
}

@Override
LRESULT WM_LBUTTONDOWN (long wParam, long lParam) {
	/*
	* Feature in Windows.  When an editable combo box is dropped
	* down and the text in the entry field partially matches an
	* item in the list, Windows selects the item but doesn't send
	* WM_COMMAND with CBN_SELCHANGE.  The fix is to detect that
	* the selection has changed and issue the notification.
	*/
	int oldSelection = (int)OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
	LRESULT result = super.WM_LBUTTONDOWN (wParam, lParam);
	if (result == LRESULT.ZERO) return result;
	if ((style & SWT.READ_ONLY) == 0) {
		int newSelection = (int)OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
		if (oldSelection != newSelection) {
			sendEvent (SWT.Modify);
			if (isDisposed ()) return LRESULT.ZERO;
			sendSelectionEvent (SWT.Selection, null, true);
			if (isDisposed ()) return LRESULT.ZERO;
		}
	}
	return result;
}

@Override
LRESULT WM_SETFOCUS (long wParam, long lParam) {
	/*
	* Return NULL - Focus notification is
	* done by WM_COMMAND with CBN_SETFOCUS.
	*/
	return null;
}

@Override
LRESULT WM_SIZE (long wParam, long lParam) {
	/*
	* Feature in Windows.  When a combo box is resized,
	* the size of the drop down rectangle is specified
	* using the height and then the combo box resizes
	* to be the height of the text field.  This causes
	* two WM_SIZE messages to be sent and two SWT.Resize
	* events to be issued.  The fix is to ignore the
	* second resize.
	*/
	if (ignoreResize) return null;
	/*
	* Bug in Windows.  If the combo box has the CBS_SIMPLE style,
	* the list portion of the combo box is not redrawn when the
	* combo box is resized.  The fix is to force a redraw when
	* the size has changed.
	*/
	if ((style & SWT.SIMPLE) != 0) {
		LRESULT result = super.WM_SIZE (wParam, lParam);
		if (OS.IsWindowVisible (handle)) {
			int uFlags = OS.RDW_ERASE | OS.RDW_INVALIDATE | OS.RDW_ALLCHILDREN;
			OS.RedrawWindow (handle, null, 0, uFlags);
		}
		return result;
	}

	/*
	* Feature in Windows.  When an editable drop down combo box
	* contains text that does not correspond to an item in the
	* list, when the widget is resized, it selects the closest
	* match from the list.  The fix is to lock the current text
	* by ignoring all WM_SETTEXT messages during processing of
	* WM_SIZE.
	*/
	boolean oldLockText = lockText;
	if ((style & SWT.READ_ONLY) == 0) lockText = true;
	LRESULT result = super.WM_SIZE (wParam, lParam);
	if ((style & SWT.READ_ONLY) == 0) lockText = oldLockText;
	/*
	* Feature in Windows.  When CB_SETDROPPEDWIDTH is called with
	* a width that is smaller than the current size of the combo
	* box, it is ignored.  This the fix is to set the width after
	* the combo box has been resized.
	*/
	if ((style & SWT.H_SCROLL) != 0) setScrollWidth (scrollWidth);

	/*
	* When setting selection, Combo automatically scrolls selection's end into view.
	* We force it to do such scrolling after every resize to achieve multiple goals:
	* 1) Text is no longer partially shown when there's free space after resizing
	*    Without workaround, this happens when all of these are true:
	*    a) Combo has focus
	*    b) Combo can't fit all text
	*    c) Caret is not at position 0
	*    d) Combo is resized bigger.
	* 2) Text is no longer partially shown after .setSelection() before its size was calculated
	*    This is just another form of problem 1.
	* 3) Caret no longer goes out of view when shrinking control.
	*/
	forceScrollingToCaret();

	return result;
}

void forceScrollingToCaret() {
	if ((style & SWT.READ_ONLY) == 0) {
		Point oldSelection = this.getSelection();
		Point tmpSelection = new Point(0, 0);
		if (!oldSelection.equals(tmpSelection)) {
			this.setSelection(tmpSelection);
			this.setSelection(oldSelection);
		}
	}
}

@Override
LRESULT WM_UPDATEUISTATE (long wParam, long lParam) {
	LRESULT result = super.WM_UPDATEUISTATE (wParam, lParam);
	if (result != null) return result;
	OS.InvalidateRect (handle, null, true);
	return result;
}

@Override
LRESULT WM_WINDOWPOSCHANGING (long wParam, long lParam) {
	LRESULT result = super.WM_WINDOWPOSCHANGING (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  When a combo box is resized,
	* the size of the drop down rectangle is specified
	* using the height and then the combo box resizes
	* to be the height of the text field.  This causes
	* sibling windows that intersect with the original
	* bounds to redrawn.  The fix is to stop the redraw
	* using SWP_NOREDRAW and then damage the combo box
	* text field and the area in the parent where the
	* combo box used to be.
	*/
	if (!getDrawing ()) return result;
	if (!OS.IsWindowVisible (handle)) return result;
	if (ignoreResize) {
		WINDOWPOS lpwp = new WINDOWPOS ();
		OS.MoveMemory (lpwp, lParam, WINDOWPOS.sizeof);
		if ((lpwp.flags & OS.SWP_NOSIZE) == 0) {
			lpwp.flags |= OS.SWP_NOREDRAW;
			OS.MoveMemory (lParam, lpwp, WINDOWPOS.sizeof);
			OS.InvalidateRect (handle, null, true);
			RECT rect = new RECT ();
			OS.GetWindowRect (handle, rect);
			int width = rect.right - rect.left;
			int height = rect.bottom - rect.top;
			if (width != 0 && height != 0) {
				long hwndParent = parent.handle;
				long hwndChild = OS.GetWindow (hwndParent, OS.GW_CHILD);
				OS.MapWindowPoints (0, hwndParent, rect, 2);
				long rgn1 = OS.CreateRectRgn (rect.left, rect.top, rect.right, rect.bottom);
				while (hwndChild != 0) {
					if (hwndChild != handle) {
						OS.GetWindowRect (hwndChild, rect);
						OS.MapWindowPoints (0, hwndParent, rect, 2);
						long rgn2 = OS.CreateRectRgn (rect.left, rect.top, rect.right, rect.bottom);
						OS.CombineRgn (rgn1, rgn1, rgn2, OS.RGN_DIFF);
						OS.DeleteObject (rgn2);
					}
					hwndChild = OS.GetWindow (hwndChild, OS.GW_HWNDNEXT);
				}
				int flags = OS.RDW_ERASE | OS.RDW_FRAME | OS.RDW_INVALIDATE;
				OS.RedrawWindow (hwndParent, null, rgn1, flags);
				OS.DeleteObject (rgn1);
			}
		}
	}
	return result;
}

@Override
LRESULT wmChar (long hwnd, long wParam, long lParam) {
	if (ignoreCharacter) return null;
	LRESULT result = super.wmChar (hwnd, wParam, lParam);
	if (result != null) return result;
	switch ((int)wParam) {
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
		case SWT.TAB: return LRESULT.ZERO;
		case SWT.CR:
			if (!ignoreDefaultSelection) sendSelectionEvent (SWT.DefaultSelection);
			ignoreDefaultSelection = false;
			// when no value is selected in the dropdown
			if (getSelectionIndex() == -1) {
				if ((style & SWT.DROP_DOWN) != 0 && (style & SWT.READ_ONLY) == 0) {
					// close the dropdown if open
					if (OS.SendMessage(handle, OS.CB_GETDROPPEDSTATE, 0, 0) != 0) {
						OS.SendMessage(handle, OS.CB_SHOWDROPDOWN, 0, 0);
					}
					return LRESULT.ZERO;
				}
			}
		case SWT.ESC:
			if ((style & SWT.DROP_DOWN) != 0) {
				if (OS.SendMessage (handle, OS.CB_GETDROPPEDSTATE, 0, 0) == 0) {
					return LRESULT.ZERO;
				}
			}
		/*
		* Bug in Windows.  When the user types CTRL and BS
		* in a combo control, a DEL character (0x08) is generated.
		* Rather than deleting the text, the DEL character
		* is inserted into the control. The fix is to detect
		* this case and not call the window proc.
		*/
		case SWT.DEL:
			if (OS.GetKeyState (OS.VK_CONTROL) < 0) {
				if ((style & SWT.READ_ONLY) != 0) return LRESULT.ZERO;
				Point selection = getSelection ();
				long hwndText = OS.GetDlgItem (handle, CBID_EDIT);
				int x = selection.x;
				int y = selection.y;
				if (x == y) {
					String actText = getText ().substring (0, x);
					java.util.regex.Matcher m = CTRL_BS_PATTERN.matcher (actText);
					if (m.find ()) {
						x = m.start ();
						y = m.end ();
						OS.SendMessage (hwndText, OS.EM_SETSEL, x, y);
					}
				}
				if (x < y) {
					/*
					* Instead of setting the new text directly we send the replace selection event to
					* guarantee that the action is pushed to the undo buffer.
					*/
					OS.SendMessage (hwndText, OS.EM_REPLACESEL, 1, 0);
				}
				return LRESULT.ZERO;
			}
	}
	return result;
}

LRESULT wmClipboard (long hwndText, int msg, long wParam, long lParam) {
	if ((style & SWT.READ_ONLY) != 0) return null;
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return null;
	boolean call = false;
	int [] start = new int [1], end = new int [1];
	String newText = null;
	switch (msg) {
		case OS.WM_CLEAR:
		case OS.WM_CUT:
			OS.SendMessage (hwndText, OS.EM_GETSEL, start, end);
			if (untranslateOffset (start [0]) != untranslateOffset (end [0])) {
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
				OS.CallWindowProc (EditProc, hwndText, msg, wParam, lParam);
				int length = OS.GetWindowTextLength (hwndText);
				int [] newStart = new int [1], newEnd = new int [1];
				OS.SendMessage (hwndText, OS.EM_GETSEL, newStart, newEnd);
				if (length != 0 && newStart [0] != newEnd [0]) {
					char [] buffer = new char [length + 1];
					OS.GetWindowText (hwndText, buffer, length + 1);
					newText = new String (buffer, newStart [0], newEnd [0] - newStart [0]);
				} else {
					newText = "";
				}
				OS.CallWindowProc (EditProc, hwndText, msg, wParam, lParam);
				OS.SendMessage (hwndText, OS.EM_GETSEL, start, end);
				ignoreModify = false;
			}
			break;
		case OS.WM_SETTEXT:
			if (lockText) return null;
			end [0] = OS.GetWindowTextLength (hwndText);
			int length = OS.wcslen (lParam);
			TCHAR buffer = new TCHAR (getCodePage (), length);
			int byteCount = buffer.length () * TCHAR.sizeof;
			OS.MoveMemory (buffer, lParam, byteCount);
			newText = buffer.toString (0, length);
			break;
	}
	if (newText != null) {
		String oldText = newText;
		newText = verifyText (newText, start [0], end [0], null);
		if (newText == null) return LRESULT.ZERO;
		if (!newText.equals (oldText)) {
			if (call) {
				OS.CallWindowProc (EditProc, hwndText, msg, wParam, lParam);
			}
			TCHAR buffer = new TCHAR (getCodePage (), newText, true);
			if (msg == OS.WM_SETTEXT) {
				long hHeap = OS.GetProcessHeap ();
				int byteCount = buffer.length () * TCHAR.sizeof;
				long pszText = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
				OS.MoveMemory (pszText, buffer, byteCount);
				long code = OS.CallWindowProc (EditProc, hwndText, msg, wParam, pszText);
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

@Override
LRESULT wmCommandChild (long wParam, long lParam) {
	int code = OS.HIWORD (wParam);
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
			int index = (int)OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
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
			sendSelectionEvent (SWT.Selection);
			break;
		case OS.CBN_SETFOCUS:
			sendFocusEvent (SWT.FocusIn);
			if (isDisposed ()) return LRESULT.ZERO;
			break;
		case OS.CBN_DROPDOWN:
			setCursor ();
			updateDropDownHeight ();
			updateDropDownTheme ();
			break;
		case OS.CBN_KILLFOCUS:
			sendFocusEvent (SWT.FocusOut);
			if (isDisposed ()) return LRESULT.ZERO;
			break;
		case OS.EN_ALIGN_LTR_EC:
		case OS.EN_ALIGN_RTL_EC:
			Event event = new Event ();
			event.doit = true;
			sendEvent (SWT.OrientationChange, event);
			if (!event.doit) {
				long hwnd = lParam;
				int bits1 = OS.GetWindowLong (hwnd, OS.GWL_EXSTYLE);
				int bits2 = OS.GetWindowLong (hwnd, OS.GWL_STYLE);
				if (code == OS.EN_ALIGN_LTR_EC) {
					bits1 |= (OS.WS_EX_RTLREADING | OS.WS_EX_RIGHT);
					bits2 |= OS.ES_RIGHT;
				} else {
					bits1 &= ~(OS.WS_EX_RTLREADING | OS.WS_EX_RIGHT);
					bits2 &= ~OS.ES_RIGHT;
				}
				OS.SetWindowLong (hwnd, OS.GWL_EXSTYLE, bits1);
				OS.SetWindowLong (hwnd, OS.GWL_STYLE, bits2);
			}
			if (hooks (SWT.Segments) || filters (SWT.Segments) || ((state & HAS_AUTO_DIRECTION) != 0)) {
				clearSegments(true);
				/*
				 * Explicit LTR or RTL direction was set, so auto direction
				 * should be deactivated.
				 */
				state &= ~HAS_AUTO_DIRECTION;
				applyEditSegments();
			}
			break;
	}
	return super.wmCommandChild (wParam, lParam);
}

@Override
LRESULT wmIMEChar (long hwnd, long wParam, long lParam) {

	/* Process a DBCS character */
	Display display = this.display;
	display.lastKey = 0;
	display.lastAscii = (int)wParam;
	display.lastVirtual = display.lastDead = false;
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
	long result = callWindowProc (hwnd, OS.WM_IME_CHAR, wParam, lParam);
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

@Override
LRESULT wmKeyDown (long hwnd, long wParam, long lParam) {
	if (ignoreCharacter) return null;
	LRESULT result = super.wmKeyDown (hwnd, wParam, lParam);
	if (result != null) return result;
	ignoreDefaultSelection = false;
	switch ((int)wParam) {
		case OS.VK_LEFT:
		case OS.VK_UP:
		case OS.VK_RIGHT:
		case OS.VK_DOWN:
			if (segments != null) {
				long code = 0;
				int [] start = new int [1], end = new int [1], newStart = new int [1], newEnd = new int [1];
				OS.SendMessage (handle, OS.CB_GETEDITSEL, start, end);
				while (true) {
					code = callWindowProc (hwnd, OS.WM_KEYDOWN, wParam, lParam);
					OS.SendMessage (handle, OS.CB_GETEDITSEL, newStart, newEnd);
					if (newStart [0] != start [0]) {
						if (untranslateOffset (newStart [0]) != untranslateOffset (start [0])) break;
					} else if (newEnd [0] != end [0]) {
						if (untranslateOffset (newEnd [0]) != untranslateOffset (end [0]))  break;
					} else {
						break;
					}
					start [0] = newStart [0];
					end [0] = newEnd [0];
				}
				result = code == 0 ? LRESULT.ZERO : new LRESULT (code);
			}
			break;
		case OS.VK_RETURN:
			if ((style & SWT.DROP_DOWN) != 0) {
				if (OS.SendMessage (handle, OS.CB_GETDROPPEDSTATE, 0, 0) != 0) {
					ignoreDefaultSelection = true;
				}
			}
			break;
	}
	return result;
}

@Override
LRESULT wmSysKeyDown (long hwnd, long wParam, long lParam) {
	/*
	* Feature in Windows.  When an editable combo box is dropped
	* down using Alt+Down and the text in the entry field partially
	* matches an item in the list, Windows selects the item but doesn't
	* send WM_COMMAND with CBN_SELCHANGE.  The fix is to detect that
	* the selection has changed and issue the notification.
	*/
	int oldSelection = (int)OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
	LRESULT result = super.wmSysKeyDown (hwnd, wParam, lParam);
	if (result != null) return result;
	if ((style & SWT.READ_ONLY) == 0) {
		if (wParam == OS.VK_DOWN) {
			long code = callWindowProc (hwnd, OS.WM_SYSKEYDOWN, wParam, lParam);
			int newSelection = (int)OS.SendMessage (handle, OS.CB_GETCURSEL, 0, 0);
			if (oldSelection != newSelection) {
				sendEvent (SWT.Modify);
				if (isDisposed ()) return LRESULT.ZERO;
				sendSelectionEvent (SWT.Selection, null, true);
				if (isDisposed ()) return LRESULT.ZERO;
			}
			return new LRESULT (code);
		}
	}
	return result;
}

}
