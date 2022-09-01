/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.swt.widgets;


import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify text.
 * Text controls can be either single or multi-line.
 * When a text control is created with a border, the
 * operating system includes a platform specific inset
 * around the contents of the control.  When created
 * without a border, an effort is made to remove the
 * inset such that the preferred size of the control
 * is the same size as the contents.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CENTER, ICON_CANCEL, ICON_SEARCH, LEFT, MULTI, PASSWORD, SEARCH, SINGLE, RIGHT, READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Verify, OrientationChange</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles MULTI and SINGLE may be specified,
 * and only one of the styles LEFT, CENTER, and RIGHT may be specified.
 * </p>
 * <p>
 * Note: The styles ICON_CANCEL and ICON_SEARCH are hints used in combination with SEARCH.
 * When the platform supports the hint, the text control shows these icons.  When an icon
 * is selected, a default selection event is sent with the detail field set to one of
 * ICON_CANCEL or ICON_SEARCH.  Normally, application code does not need to check the
 * detail.  In the case of ICON_CANCEL, the text is cleared before the default selection
 * event is sent causing the application to search for an empty string.
 * </p>
 * <p>
 * Note: Some text actions such as Undo are not natively supported on all platforms.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#text">Text snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Text extends Scrollable {
	int tabs, oldStart, oldEnd;
	boolean doubleClick, ignoreModify, ignoreVerify, ignoreCharacter, allowPasswordChar;
	String message;
	int[] segments;
	int clearSegmentsCount = 0;
	long hwndActiveIcon;

	static final char LTR_MARK = '\u200e';
	static final char RTL_MARK = '\u200f';

	/* Custom icons defined in swt.rc */
	static final int IDI_SEARCH = 101;
	static final int IDI_CANCEL = 102;
	static final int IDI_SEARCH_DARKTHEME = 103;
	static final int IDI_CANCEL_DARKTHEME = 104;

	/**
	* The maximum number of characters that can be entered
	* into a text widget.
	* <p>
	* Note that this value is platform dependent, based upon
	* the native widget implementation.
	* </p>
	*/
	public static final int LIMIT;

	/**
	* The delimiter used by multi-line text widgets.  When text
	* is queried and from the widget, it will be delimited using
	* this delimiter.
	*/
	public static final String DELIMITER;

	/*
	* These values can be different on different platforms.
	* Therefore they are not initialized in the declaration
	* to stop the compiler from inlining.
	*/
	static {
		LIMIT = 0x7FFFFFFF;
		DELIMITER = "\r\n";
	}

	static final long EditProc;
	static final TCHAR EditClass = new TCHAR (0, "EDIT", true);
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, EditClass, lpWndClass);
		EditProc = lpWndClass.lpfnWndProc;
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
 * @see SWT#SINGLE
 * @see SWT#MULTI
 * @see SWT#READ_ONLY
 * @see SWT#WRAP
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see SWT#PASSWORD
 * @see SWT#SEARCH
 * @see SWT#ICON_SEARCH
 * @see SWT#ICON_CANCEL
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Text (Composite parent, int style) {
	super (parent, checkStyle (style));
}

@Override
long callWindowProc (long hwnd, int msg, long wParam, long lParam) {
	if (handle == 0) return 0;
	boolean redraw = false;
	switch (msg) {
		case OS.WM_ERASEBKGND: {
			if (findImageControl () != null) return 0;
			break;
		}
		case OS.WM_HSCROLL:
		case OS.WM_VSCROLL: {
			redraw = findImageControl () != null && getDrawing () && OS.IsWindowVisible (handle);
			if (redraw) OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
			break;
		}
		case OS.WM_PAINT: {
			boolean doubleBuffer = findImageControl () != null;
			boolean drawMessage = false;
			if ((style & SWT.SINGLE) != 0 && (style & SWT.READ_ONLY) != 0 && message.length () > 0 ) {
				drawMessage = hwnd != OS.GetFocus () && OS.GetWindowTextLength (handle) == 0;
			}
			if (doubleBuffer || drawMessage) {
				long paintDC = 0;
				PAINTSTRUCT ps = new PAINTSTRUCT ();
				paintDC = OS.BeginPaint (handle, ps);
				int width = ps.right - ps.left;
				int height = ps.bottom - ps.top;
				if (width != 0 && height != 0) {
					long hDC = paintDC, hBitmap = 0, hOldBitmap = 0;
					POINT lpPoint1 = null, lpPoint2 = null;
					if (doubleBuffer) {
						hDC = OS.CreateCompatibleDC (paintDC);
						lpPoint1 = new POINT ();
						lpPoint2 = new POINT ();
						OS.SetWindowOrgEx (hDC, ps.left, ps.top, lpPoint1);
						OS.SetBrushOrgEx (hDC, ps.left, ps.top, lpPoint2);
						hBitmap = OS.CreateCompatibleBitmap (paintDC, width, height);
						hOldBitmap = OS.SelectObject (hDC, hBitmap);
						RECT rect = new RECT ();
						OS.SetRect (rect, ps.left, ps.top, ps.right, ps.bottom);
						drawBackground (hDC, rect);
					}

					OS.CallWindowProc (EditProc, hwnd, OS.WM_PAINT, hDC, lParam);
					/*
					* Bug in Windows.  Windows does not draw the cue message when the Edit
					* control is read-only. The fix is to draw the cue messages ourselves.
					*/
					if (drawMessage) {
						RECT rect = new RECT();
						OS.GetClientRect(handle, rect);
						long margins = OS.SendMessage (handle, OS.EM_GETMARGINS, 0, 0);
						rect.left += OS.LOWORD (margins);
						rect.right -= OS.HIWORD (margins);
						if ((style & SWT.BORDER) != 0) {
							rect.left++;
							rect.top++;
							rect.right--;
							rect.bottom--;
						}
						char [] buffer = message.toCharArray ();
						int uFormat = OS.DT_EDITCONTROL;
						boolean rtl = (style & SWT.RIGHT_TO_LEFT) != 0;
						if (rtl) uFormat |= OS.DT_RTLREADING;
						int alignment = style & (SWT.LEFT | SWT.CENTER | SWT.RIGHT);
						switch (alignment) {
							case SWT.LEFT: uFormat |= (rtl ? OS.DT_RIGHT : OS.DT_LEFT); break;
							case SWT.CENTER: uFormat |= OS.DT_CENTER;
							case SWT.RIGHT: uFormat |= (rtl ? OS.DT_LEFT : OS.DT_RIGHT); break;
						}
						long hFont = OS.SendMessage (hwnd, OS.WM_GETFONT, 0, 0);
						long hOldFont = OS.SelectObject (hDC, hFont);
						OS.SetTextColor (hDC, OS.GetSysColor (OS.COLOR_GRAYTEXT));
						OS.SetBkMode (hDC, OS.TRANSPARENT);
						OS.DrawText (hDC, buffer, buffer.length, rect, uFormat);
						OS.SelectObject (hDC, hOldFont);
					}

					if (doubleBuffer) {
						OS.SetWindowOrgEx (hDC, lpPoint1.x, lpPoint1.y, null);
						OS.SetBrushOrgEx (hDC, lpPoint2.x, lpPoint2.y, null);
						OS.BitBlt (paintDC, ps.left, ps.top, width, height, hDC, 0, 0, OS.SRCCOPY);
						OS.SelectObject (hDC, hOldBitmap);
						OS.DeleteObject (hBitmap);
						OS.DeleteObject (hDC);
					}
				}
				OS.EndPaint (handle, ps);
				return 0;
			}
			break;
		}
	}
	if ((style & SWT.SEARCH) != 0) {
		switch (msg) {
			case OS.WM_MOUSEMOVE: {
				POINT pt = new POINT ();
				OS.POINTSTOPOINT(pt, lParam);
				long hwndIcon = OS.ChildWindowFromPointEx (handle, pt, OS.CWP_SKIPINVISIBLE);
				if (hwndIcon == handle) hwndIcon = 0;
				if (hwndIcon != hwndActiveIcon) {
					if (hwndActiveIcon != 0) OS.InvalidateRect (hwndActiveIcon, null, false);
					if (hwndIcon != 0) OS.InvalidateRect (hwndIcon, null, false);
					hwndActiveIcon = hwndIcon;
				}
				break;
			}
			case OS.WM_MOUSELEAVE:
				if (hwndActiveIcon != 0) {
					OS.InvalidateRect (hwndActiveIcon, null, false);
					hwndActiveIcon = 0;
				}
				break;
			case OS.WM_LBUTTONDOWN:
				if (hwndActiveIcon != 0) {
					OS.InvalidateRect (hwndActiveIcon, null, false);
					return 0; // prevent mouse selection
				}
				break;
			case OS.WM_LBUTTONUP: {
				if (hwndActiveIcon != 0) {
					Event e = new Event();
					if (hwndActiveIcon == OS.GetDlgItem (handle, SWT.ICON_SEARCH)) {
						e.detail = SWT.ICON_SEARCH;
					} else {
						e.detail = SWT.ICON_CANCEL;
						setText ("");
					}
					setFocus ();
					selectAll ();
					sendSelectionEvent (SWT.DefaultSelection, e, false);
				}
				break;
			}
		}
	}
	long code = OS.CallWindowProc (EditProc, hwnd, msg, wParam, lParam);
	switch (msg) {
		case OS.WM_HSCROLL:
		case OS.WM_VSCROLL: {
			if (redraw) {
				OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
				OS.InvalidateRect (handle, null, true);
			}
			break;
		}
	}
	return code;
}

@Override
void createHandle () {
	long editStyle = widgetStyle ();
	if ((editStyle & OS.WS_BORDER) == 0)
		super.createHandle ();
	else {
		/*
		 * Feature on Windows: when `Edit` control is created, it removes
		 * `WS_BORDER`, but then internally draws the border over the client
		 * area. This is undesirable because all SWT coordinates will then
		 * need to be adjusted by the border size. The workaround is to create
		 * control without `WS_BORDER` and add it just after creating.
		 */
		style &= ~SWT.BORDER;
		super.createHandle ();
		style |= SWT.BORDER;

		editStyle = OS.GetWindowLongPtr(handle, OS.GWL_STYLE);
		editStyle |= OS.WS_BORDER;
		OS.SetWindowLongPtr(handle, OS.GWL_STYLE, editStyle);

		OS.SetWindowPos(handle, 0, 0, 0, 0, 0, OS.SWP_NOMOVE | OS.SWP_NOSIZE | OS.SWP_NOZORDER | OS.SWP_FRAMECHANGED);
	}

	OS.SendMessage (handle, OS.EM_LIMITTEXT, 0, 0);
	if ((style & SWT.READ_ONLY) != 0) {
		if (applyThemeBackground () == 1) {
			state |= THEME_BACKGROUND;
		}
	}
	if ((style & SWT.SEARCH) != 0) {
		if (display.hIconSearch == 0) {
			long [] phicon = new long [1];

			int searchIconResource = display.textUseDarkthemeIcons ? IDI_SEARCH_DARKTHEME : IDI_SEARCH;
			int hresult = OS.LoadIconMetric (OS.GetLibraryHandle (), searchIconResource, OS.LIM_SMALL, phicon);
			if (hresult != OS.S_OK) error (SWT.ERROR_NO_HANDLES);
			display.hIconSearch = phicon [0];

			int cancelIconResource = display.textUseDarkthemeIcons ? IDI_CANCEL_DARKTHEME : IDI_CANCEL;
			hresult = OS.LoadIconMetric (OS.GetLibraryHandle (), cancelIconResource, OS.LIM_SMALL, phicon);
			if (hresult != OS.S_OK) error (SWT.ERROR_NO_HANDLES);
			display.hIconCancel = phicon [0];
		}
		if ((style & SWT.ICON_SEARCH) != 0) {
			long hwndSearch = OS.CreateWindowEx (
				0,
				Label.LabelClass,
				null,
				OS.WS_CHILD | OS.WS_VISIBLE | OS.WS_CLIPSIBLINGS | OS.SS_OWNERDRAW,
				0, 0, 0, 0,
				handle,
				SWT.ICON_SEARCH,
				OS.GetModuleHandle (null),
				null);
			if (hwndSearch == 0) error (SWT.ERROR_NO_HANDLES);
		}
		if ((style & SWT.ICON_CANCEL) != 0) {
			state |= TRACK_MOUSE;
			long hwndCancel = OS.CreateWindowEx (
				0,
				Label.LabelClass, null,
				OS.WS_CHILD | OS.WS_CLIPSIBLINGS | OS.SS_OWNERDRAW,
				0, 0, 0, 0,
				handle,
				SWT.ICON_CANCEL,
				OS.GetModuleHandle (null),
				null);
			if (hwndCancel == 0) error (SWT.ERROR_NO_HANDLES);
		}
	}
}

@Override
int applyThemeBackground () {
	return (backgroundAlpha == 0 || (style & (SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL)) == 0) ? 1 : 0;
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
 * <b>Warning</b>: This API is currently only implemented on Windows and GTK.
 * <code>SegmentEvent</code>s won't be sent on Cocoa.
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
 * @since 3.8
 */
public void addSegmentListener (SegmentListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	addListener (SWT.Segments, new TypedListener (listener));
	clearSegments (true);
	applySegments ();
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is not called for texts.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed in a single-line text,
 * or when ENTER is pressed in a search text. If the receiver has the <code>SWT.SEARCH | SWT.ICON_CANCEL</code> style
 * and the user cancels the search, the event object detail field contains the value <code>SWT.ICON_CANCEL</code>.
 * Likewise, if the receiver has the <code>SWT.ICON_SEARCH</code> style and the icon search is selected, the
 * event object detail field contains the value <code>SWT.ICON_SEARCH</code>.
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
 */
public void addVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Verify, typedListener);
}

/**
 * Appends a string.
 * <p>
 * The new text is appended to the text at
 * the end of the widget.
 * </p>
 *
 * @param string the string to be appended
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void append (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	string = Display.withCrLf (string);
	int length = OS.GetWindowTextLength (handle);
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		string = verifyText (string, length, length, null);
		if (string == null) return;
	}
	OS.SendMessage (handle, OS.EM_SETSEL, length, length);
	clearSegments (true);
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
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
	ignoreCharacter = true;
	OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
	ignoreCharacter = false;
	OS.SendMessage (handle, OS.EM_SCROLLCARET, 0, 0);
	if ((state & HAS_AUTO_DIRECTION) != 0) {
		super.updateTextDirection (AUTO_TEXT_DIRECTION);
	}
	applySegments ();
}

void applySegments () {
	/*
	 * It is possible (but unlikely), that application code could have
	 * disposed the widget in the modify event. If this happens, return to
	 * cancel the operation.
	 */
	if (isDisposed() || --clearSegmentsCount != 0) return;
	if (!hooks (SWT.Segments) && !filters (SWT.Segments)) return;
	int length = OS.GetWindowTextLength (handle);
	char [] buffer = new char [length + 1];
	if (length > 0) OS.GetWindowText (handle, buffer, length + 1);
	String string = new String (buffer, 0, length);
	/* Get segments text */
	Event event = new Event ();
	event.text = string;
	event.segments = segments;
	sendEvent (SWT.Segments, event);
	segments = event.segments;
	if (segments == null) return;
	int nSegments = segments.length;
	if (nSegments == 0) return;
	length = string == null ? 0 : string.length ();

	for (int i = 1; i < nSegments; i++) {
		if (event.segments [i] < event.segments [i - 1] || event.segments [i] > length) {
			error (SWT.ERROR_INVALID_ARGUMENT);
		}
	}
	char [] segmentsChars = event.segmentsChars;
	char [] segmentsCharsCrLf = segmentsChars == null ? null : Display.withCrLf(segmentsChars);
	if (segmentsChars != segmentsCharsCrLf) {
		int [] segmentsCrLf = new int [nSegments + Math.min (nSegments, segmentsCharsCrLf.length - segmentsChars.length)];
		for (int i = 0, c = 0; i < segmentsChars.length && i < nSegments; i++) {
			if (segmentsChars [i] == '\n' && segmentsCharsCrLf [i + c] == '\r') {
				segmentsCrLf [i + c++] = segments [i];
			}
			segmentsCrLf [i + c] = segments [i];
		}
		segments = segmentsCrLf;
		nSegments = segments.length;
		segmentsChars = segmentsCharsCrLf;
	}

	int limit = (int)OS.SendMessage (handle, OS.EM_GETLIMITTEXT, 0, 0) & 0x7fffffff;
	OS.SendMessage (handle, OS.EM_SETLIMITTEXT, limit + Math.min (nSegments, LIMIT - limit), 0);
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
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	boolean oldIgnoreCharacter = ignoreCharacter, oldIgnoreModify = ignoreModify, oldIgnoreVerify = ignoreVerify;
	ignoreCharacter = ignoreModify = ignoreVerify = true;
	/*
	 * SetWindowText empties the undo buffer and disables undo menu item.
	 * Sending OS.EM_REPLACESEL message instead.
	 */
	newChars [length] = 0;
	OS.SendMessage (handle, OS.EM_SETSEL, 0, -1);
	long undo = OS.SendMessage (handle, OS.EM_CANUNDO, 0, 0);
	OS.SendMessage (handle, OS.EM_REPLACESEL, undo, newChars);
	/* Restore selection */
	start [0] = translateOffset (start [0]);
	end [0] = translateOffset (end [0]);
	OS.SendMessage (handle, OS.EM_SETSEL, start [0], end [0]);
	ignoreCharacter = oldIgnoreCharacter;
	ignoreModify = oldIgnoreModify;
	ignoreVerify = oldIgnoreVerify;
}

static int checkStyle (int style) {
	if ((style & SWT.SINGLE) != 0 && (style & SWT.MULTI) != 0) {
		style &= ~SWT.MULTI;
	}
	style = checkBits (style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
	/*
	 * NOTE: ICON_CANCEL and ICON_SEARCH have the same value as H_SCROLL and
	 * V_SCROLL. The meaning is determined by whether SWT.SEARCH is set.
	 */
	if ((style & SWT.SEARCH) != 0) {
		style |= SWT.SINGLE | SWT.BORDER;
		style &= ~(SWT.PASSWORD | SWT.WRAP);
	} else if ((style & SWT.SINGLE) != 0) {
		style &= ~(SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
	}
	if ((style & SWT.WRAP) != 0) {
		style |= SWT.MULTI;
		style &= ~SWT.H_SCROLL;
	}
	if ((style & SWT.MULTI) != 0) style &= ~SWT.PASSWORD;
	if ((style & (SWT.SINGLE | SWT.MULTI)) != 0) return style;
	if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) return style | SWT.MULTI;
	return style | SWT.SINGLE;
}

void clearSegments (boolean applyText) {
	if (clearSegmentsCount++ != 0) return;
	if (segments == null) return;
	int nSegments = segments.length;
	if (nSegments == 0) return;
	int limit = (int)OS.SendMessage (handle, OS.EM_GETLIMITTEXT, 0, 0) & 0x7fffffff;
	if (limit < LIMIT) {
		OS.SendMessage (handle, OS.EM_SETLIMITTEXT, Math.max (1, limit - nSegments), 0);
	}
	if (!applyText) {
		segments = null;
		return;
	}
	boolean oldIgnoreCharacter = ignoreCharacter, oldIgnoreModify = ignoreModify, oldIgnoreVerify = ignoreVerify;
	ignoreCharacter = ignoreModify = ignoreVerify = true;
	int length = OS.GetWindowTextLength (handle);
	int cp = getCodePage ();
	TCHAR buffer = new TCHAR (cp, length + 1);
	if (length > 0) OS.GetWindowText (handle, buffer, length + 1);
	buffer = deprocessText (buffer, 0, -1, true);
	/* Get the current selection */
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	start [0] = untranslateOffset (start [0]);
	end [0] = untranslateOffset (end[0]);
	segments = null;
	/*
	 * SetWindowText empties the undo buffer and disables undo in the context
	 * menu. Sending OS.EM_REPLACESEL message instead.
	 */
	OS.SendMessage (handle, OS.EM_SETSEL, 0, -1);
	long undo = OS.SendMessage (handle, OS.EM_CANUNDO, 0, 0);
	OS.SendMessage (handle, OS.EM_REPLACESEL, undo, buffer);
	OS.SendMessage (handle, OS.EM_SETSEL, start [0], end [0]);
	ignoreCharacter = oldIgnoreCharacter;
	ignoreModify = oldIgnoreModify;
	ignoreVerify = oldIgnoreVerify;
}

/**
 * Clears the selection.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void clearSelection () {
	checkWidget ();
	OS.SendMessage (handle, OS.EM_SETSEL, -1, 0);
}

@Override Point computeSizeInPixels (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int height = 0, width = 0;
	if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
		long newFont, oldFont = 0;
		long hDC = OS.GetDC (handle);
		newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
		if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
		TEXTMETRIC tm = new TEXTMETRIC ();
		OS.GetTextMetrics (hDC, tm);
		int count = (style & SWT.SINGLE) != 0 ? 1 : (int)OS.SendMessage (handle, OS.EM_GETLINECOUNT, 0, 0);
		height = count * tm.tmHeight;
		RECT rect = new RECT ();
		int flags = OS.DT_CALCRECT | OS.DT_EDITCONTROL | OS.DT_NOPREFIX;
		boolean wrap = (style & SWT.MULTI) != 0 && (style & SWT.WRAP) != 0;
		if (wrap && wHint != SWT.DEFAULT) {
			flags |= OS.DT_WORDBREAK;
			rect.right = wHint;
		}
		int length = OS.GetWindowTextLength (handle);
		if (length != 0) {
			char [] buffer = new char [length + 1];
			OS.GetWindowText (handle, buffer, length + 1);
			OS.DrawText (hDC, buffer, length, rect, flags);
			Arrays.fill (buffer, '\0'); // erase sensitive data
			width = rect.right - rect.left;
		}
		if (wrap && hHint == SWT.DEFAULT) {
			int newHeight = rect.bottom - rect.top;
			if (newHeight != 0) height = newHeight;
		}
		if ((style & SWT.SINGLE) != 0 && message.length () > 0) {
			OS.SetRect (rect, 0, 0, 0, 0);
			char [] buffer = message.toCharArray ();
			OS.DrawText (hDC, buffer, buffer.length, rect, flags);
			width = Math.max (width, rect.right - rect.left);
		}
		if (newFont != 0) OS.SelectObject (hDC, oldFont);
		OS.ReleaseDC (handle, hDC);
	}
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	Rectangle trim = computeTrimInPixels (0, 0, width, height);
	return new Point (trim.width, trim.height);
}

@Override Rectangle computeTrimInPixels (int x, int y, int width, int height) {
	checkWidget ();
	Rectangle rect = super.computeTrimInPixels (x, y, width, height);
	/*
	* The preferred height of a single-line text widget
	* has been hand-crafted to be the same height as
	* the single-line text widget in an editable combo
	* box.
	*/
	long margins = OS.SendMessage(handle, OS.EM_GETMARGINS, 0, 0);
	rect.x -= OS.LOWORD (margins);
	rect.width += OS.LOWORD (margins) + OS.HIWORD (margins);
	if ((style & SWT.H_SCROLL) != 0) rect.width++;
	if ((style & SWT.BORDER) != 0) {
		rect.x -= 1;
		rect.y -= 1;
		rect.width += 2;
		rect.height += 2;

		// When WS_BORDER is used instead of WS_EX_CLIENTEDGE, compensate the size difference
		if (isUseWsBorder ()) {
			int dx = OS.GetSystemMetrics (OS.SM_CXEDGE) - OS.GetSystemMetrics (OS.SM_CXBORDER);
			int dy = OS.GetSystemMetrics (OS.SM_CYEDGE) - OS.GetSystemMetrics (OS.SM_CYBORDER);
			rect.x -= dx;
			rect.y -= dy;
			rect.width += 2*dx;
			rect.height += 2*dy;
		}
	}

	return rect;
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
 */
public void copy () {
	checkWidget ();
	OS.SendMessage (handle, OS.WM_COPY, 0, 0);
}

@Override
ScrollBar createScrollBar (int type) {
	return (style & SWT.SEARCH) == 0 ? super.createScrollBar(type) : null;
}

@Override
void createWidget () {
	super.createWidget ();
	message = "";
	doubleClick = true;
	setTabStops (tabs = 8);
	fixAlignment ();
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
 */
public void cut () {
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
	OS.SendMessage (handle, OS.WM_CUT, 0, 0);
}

@Override
int defaultBackground () {
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.ES_READONLY) != 0 || !OS.IsWindowEnabled (handle)) {
		return OS.GetSysColor (OS.COLOR_3DFACE);
	}
	return OS.GetSysColor (OS.COLOR_WINDOW);
}

TCHAR deprocessText (TCHAR text, int start, int end, boolean terminate) {
	if (text == null) return null;
	int length = text.length ();
	char [] chars;
	if (start < 0) start = 0;
	chars = text.chars;
	if (text.chars [length - 1] == 0) length--;
	if (end == -1) end = length;
	if (segments != null && end > segments [0]) {
		int nSegments = segments.length;
		if (nSegments > 0 && start <= segments [nSegments - 1]) {
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
	}
	if (start != 0 || end != length) {
		char [] newChars = new char [length];
		System.arraycopy(chars, start, newChars, 0, length);
		return new TCHAR (getCodePage (), newChars, terminate);
	}
	return text;
}

@Override
boolean dragDetect (long hwnd, int x, int y, boolean filter, boolean [] detect, boolean [] consume) {
	if (filter) {
		int [] start = new int [1], end = new int [1];
		OS.SendMessage (handle, OS.EM_GETSEL, start, end);
		if (start [0] != end [0]) {
			long lParam = OS.MAKELPARAM (x, y);
			int position = OS.LOWORD (OS.SendMessage (handle, OS.EM_CHARFROMPOS, 0, lParam));
			if (start [0] <= position && position < end [0]) {
				if (super.dragDetect (hwnd, x, y, filter, detect, consume)) {
					if (consume != null) consume [0] = true;
					return true;
				}
			}
		}
		return false;
	}
	return super.dragDetect (hwnd, x, y, filter, detect, consume);
}

@Override
void maybeEnableDarkSystemTheme() {
	/*
	 * Feature in Windows. If the control has default foreground and
	 * background, the background gets black without focus and white with
	 * focus, but the foreground color always stays black.
	 */
	if (hasCustomBackground() || hasCustomForeground()) {
		super.maybeEnableDarkSystemTheme();
	}
}

void fixAlignment () {
	/*
	* Feature in Windows.  When the edit control is not
	* mirrored, it uses WS_EX_RIGHT, WS_EX_RTLREADING and
	* WS_EX_LEFTSCROLLBAR to give the control a right to
	* left appearance.  This causes the control to be lead
	* aligned no matter what alignment was specified by
	* the programmer.  For example, setting ES_RIGHT and
	* WS_EX_LAYOUTRTL should cause the contents of the
	* control to be left (trail) aligned in a mirrored world.
	* When the orientation is changed by the user or
	* specified by the programmer, WS_EX_RIGHT conflicts
	* with the mirrored alignment.  The fix is to clear
	* or set WS_EX_RIGHT to achieve the correct alignment
	* according to the orientation and mirroring.
	*/
	if ((style & SWT.MIRRORED) != 0) return;
	int bits1 = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
	int bits2 = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((style & SWT.LEFT_TO_RIGHT) != 0) {
		if ((style & SWT.RIGHT) != 0) {
			bits1 |= OS.WS_EX_RIGHT;
			bits2 |= OS.ES_RIGHT;
		}
		if ((style & SWT.LEFT) != 0) {
			bits1 &= ~OS.WS_EX_RIGHT;
			bits2 &= ~OS.ES_RIGHT;
		}
	} else {
		if ((style & SWT.RIGHT) != 0) {
			bits1 &= ~OS.WS_EX_RIGHT;
			bits2 &= ~OS.ES_RIGHT;
		}
		if ((style & SWT.LEFT) != 0) {
			bits1 |= OS.WS_EX_RIGHT;
			bits2 |= OS.ES_RIGHT;
		}
	}
	if ((style & SWT.CENTER) != 0) {
		bits2 |= OS.ES_CENTER;
	}
	OS.SetWindowLong (handle, OS.GWL_EXSTYLE, bits1);
	OS.SetWindowLong (handle, OS.GWL_STYLE, bits2);
}

@Override int getBorderWidthInPixels () {
	checkWidget ();
	/*
	* Feature in Windows 2000 and XP.  Despite the fact that WS_BORDER
	* is set when the edit control is created, the style bit is cleared.
	* The fix is to avoid the check for WS_BORDER and use the SWT widget
	* style bits instead.
	*/
//	if ((style & SWT.BORDER) != 0 && (style & SWT.FLAT) != 0) {
//		return OS.GetSystemMetrics (OS.SM_CXBORDER);
//	}
	return super.getBorderWidthInPixels ();
}

/**
 * Returns the line number of the caret.
 * <p>
 * The line number of the caret is returned.
 * </p>
 *
 * @return the line number
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCaretLineNumber () {
	checkWidget ();
	return (int)OS.SendMessage (handle, OS.EM_LINEFROMCHAR, -1, 0);
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
	long caretPos = OS.SendMessage (handle, OS.EM_POSFROMCHAR, position, 0);
	if (caretPos == -1) {
		caretPos = 0;
		if (position >= OS.GetWindowTextLength (handle)) {
			int [] start = new int [1], end = new int [1];
			OS.SendMessage (handle, OS.EM_GETSEL, start, end);
			OS.SendMessage (handle, OS.EM_SETSEL, position, position);
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
			OS.SendMessage (handle, OS.EM_REPLACESEL, 0, new char [] {' ', '\0'});
			caretPos = OS.SendMessage (handle, OS.EM_POSFROMCHAR, position, 0);
			OS.SendMessage (handle, OS.EM_SETSEL, position, position + 1);
			OS.SendMessage (handle, OS.EM_REPLACESEL, 0, new char [] {'\0'});
			ignoreCharacter = ignoreModify = false;
			OS.SendMessage (handle, OS.EM_SETSEL, start [0], start [0]);
			OS.SendMessage (handle, OS.EM_SETSEL, start [0], end [0]);
		}
	}
	return new Point (OS.GET_X_LPARAM (caretPos), OS.GET_Y_LPARAM (caretPos));
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
 */
public int getCaretPosition () {
	checkWidget ();
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
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
		int startLine = (int)OS.SendMessage (handle, OS.EM_LINEFROMCHAR, start [0], 0);
		int endLine = (int)OS.SendMessage (handle, OS.EM_LINEFROMCHAR, end [0], 0);
		if (startLine == endLine) {
			int idThread = OS.GetWindowThreadProcessId (handle, null);
			GUITHREADINFO lpgui = new GUITHREADINFO ();
			lpgui.cbSize = GUITHREADINFO.sizeof;
			if (OS.GetGUIThreadInfo (idThread, lpgui)) {
				if (lpgui.hwndCaret == handle || lpgui.hwndCaret == 0) {
					POINT ptCurrentPos = new POINT ();
					if (OS.GetCaretPos (ptCurrentPos)) {
						long endPos = OS.SendMessage (handle, OS.EM_POSFROMCHAR, end [0], 0);
						if (endPos == -1) {
							long startPos = OS.SendMessage (handle, OS.EM_POSFROMCHAR, start [0], 0);
							int startX = OS.GET_X_LPARAM (startPos);
							if (ptCurrentPos.x > startX) caret = end [0];
						} else {
							int endX = OS.GET_X_LPARAM (endPos);
							if (ptCurrentPos.x >= endX) caret = end [0];
						}
					}
				}
			}
		} else {
			int caretPos = (int)OS.SendMessage (handle, OS.EM_LINEINDEX, -1, 0);
			int caretLine = (int)OS.SendMessage (handle, OS.EM_LINEFROMCHAR, caretPos, 0);
			if (caretLine == endLine) caret = end [0];
		}
	}
	return untranslateOffset (caret);
}

/**
 * Returns the number of characters.
 *
 * @return number of characters in the widget
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getCharCount () {
	checkWidget ();
	int length = OS.GetWindowTextLength (handle);
	return untranslateOffset (length);
}

/**
 * Returns the double click enabled flag.
 * <p>
 * The double click flag enables or disables the
 * default action of the text widget when the user
 * double clicks.
 * </p>
 *
 * @return whether or not double click is enabled
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getDoubleClickEnabled () {
	checkWidget ();
	return doubleClick;
}

/**
 * Returns the echo character.
 * <p>
 * The echo character is the character that is
 * displayed when the user enters text or the
 * text is changed by the programmer.
 * </p>
 *
 * @return the echo character
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setEchoChar
 */
public char getEchoChar () {
	checkWidget ();
	return (char) OS.SendMessage (handle, OS.EM_GETPASSWORDCHAR, 0, 0);
}

/**
 * Returns the editable state.
 *
 * @return whether or not the receiver is editable
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getEditable () {
	checkWidget ();
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	return (bits & OS.ES_READONLY) == 0;
}

/**
 * Returns the number of lines.
 *
 * @return the number of lines in the widget
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getLineCount () {
	checkWidget ();
	return (int)OS.SendMessage (handle, OS.EM_GETLINECOUNT, 0, 0);
}

/**
 * Returns the line delimiter.
 *
 * @return a string that is the line delimiter
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #DELIMITER
 */
public String getLineDelimiter () {
	checkWidget ();
	return DELIMITER;
}

/**
 * Returns the height of a line.
 *
 * @return the height of a row of text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getLineHeight () {
	checkWidget ();
	return DPIUtil.autoScaleDown(getLineHeightInPixels ());
}

int getLineHeightInPixels () {
	long newFont, oldFont = 0;
	long hDC = OS.GetDC (handle);
	newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	TEXTMETRIC tm = new TEXTMETRIC ();
	OS.GetTextMetrics (hDC, tm);
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	return tm.tmHeight;
}

/**
 * Returns the orientation of the receiver, which will be one of the
 * constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
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

/**
 * Returns the widget message.  The message text is displayed
 * as a hint for the user, indicating the purpose of the field.
 * <p>
 * Typically this is used in conjunction with <code>SWT.SEARCH</code>.
 * </p>
 *
 * @return the widget message
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.3
 */
public String getMessage () {
	checkWidget ();
	return message;
}

/**
 * Returns the character position at the given point in the receiver
 * or -1 if no such position exists. The point is in the coordinate
 * system of the receiver.
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
 * @since 3.3
 */
//TODO - Javadoc
/*public*/ int getPosition (Point point) {
	checkWidget();
	if (point == null) error (SWT.ERROR_NULL_ARGUMENT);
	long lParam = OS.MAKELPARAM (point.x, point.y);
	int position = OS.LOWORD (OS.SendMessage (handle, OS.EM_CHARFROMPOS, 0, lParam));
	return untranslateOffset (position);
}

/**
 * Returns a <code>Point</code> whose x coordinate is the
 * character position representing the start of the selected
 * text, and whose y coordinate is the character position
 * representing the end of the selection. An "empty" selection
 * is indicated by the x and y coordinates having the same value.
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
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	return new Point (untranslateOffset (start [0]), untranslateOffset (end [0]));
}

/**
 * Returns the number of selected characters.
 *
 * @return the number of selected characters.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSelectionCount () {
	checkWidget ();
	Point selection = getSelection ();
	return selection.y - selection.x;
}

/**
 * Gets the selected text, or an empty string if there is no current selection.
 *
 * @return the selected text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getSelectionText () {
	checkWidget ();
	int length = OS.GetWindowTextLength (handle);
	if (length == 0) return "";
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	if (start [0] == end [0]) return "";
	TCHAR buffer = new TCHAR (getCodePage (), length + 1);
	OS.GetWindowText (handle, buffer, length + 1);
	if (segments != null) {
		buffer = deprocessText (buffer, start [0], end [0], false);
		return buffer.toString ();
	}
	return buffer.toString (start [0], end [0] - start [0]);
}

/**
 * Returns the number of tabs.
 * <p>
 * Tab stop spacing is specified in terms of the
 * space (' ') character.  The width of a single
 * tab stop is the pixel width of the spaces.
 * </p>
 *
 * @return the number of tab characters
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTabs () {
	checkWidget ();
	return tabs;
}

int getTabWidth (int tabs) {
	long oldFont = 0;
	RECT rect = new RECT ();
	long hDC = OS.GetDC (handle);
	long newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
	if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
	int flags = OS.DT_CALCRECT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
	OS.DrawText (hDC, new char [] {' '}, 1, rect, flags);
	if (newFont != 0) OS.SelectObject (hDC, oldFont);
	OS.ReleaseDC (handle, hDC);
	return (rect.right - rect.left) * tabs;
}

/**
 * Returns the widget text.
 * <p>
 * The text for a text widget is the characters in the widget, or
 * an empty string if this has never been set.
 * </p>
 *
 * @return the widget text
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
 * Returns the widget's text as a character array.
 * <p>
 * The text for a text widget is the characters in the widget, or
 * a zero-length array if this has never been set.
 * </p>
 * <p>
 * Note: Use this API to prevent the text from being written into a String
 * object whose lifecycle is outside of your control. This can help protect
 * the text, for example, when the widget is used as a password field.
 * However, the text can't be protected if an {@link SWT#Segments} or
 * {@link SWT#Verify} listener has been added to the widget.
 * </p>
 *
 * @return a character array that contains the widget's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #setTextChars(char[])
 *
 * @since 3.7
 */
public char[] getTextChars () {
	checkWidget ();
	int length = OS.GetWindowTextLength (handle);
	if (length == 0) return new char[0];
	TCHAR buffer = new TCHAR (getCodePage (), length + 1);
	OS.GetWindowText (handle, buffer, length + 1);
	if (segments != null) buffer = deprocessText (buffer, 0, -1, false);
	char [] chars = new char [length];
	System.arraycopy (buffer.chars, 0, chars, 0, length);
	buffer.clear ();
	return chars;
}

/**
 * Returns a range of text.  Returns an empty string if the
 * start of the range is greater than the end.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N-1 where N is
 * the number of characters in the widget.
 * </p>
 *
 * @param start the start of the range
 * @param end the end of the range
 * @return the range of text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText (int start, int end) {
	checkWidget ();
	if (!(start <= end && 0 <= end)) return "";
	int length = OS.GetWindowTextLength (handle);
	end = Math.min (end, untranslateOffset (length) - 1);
	if (start > end) return "";
	start = Math.max (0, start);
	/*
	* NOTE: The current implementation uses substring ()
	* which can reference a potentially large character
	* array.
	*/
	return getText ().substring (start, end + 1);
}

/**
 * Returns the maximum number of characters that the receiver is capable of holding.
 * <p>
 * If this has not been changed by <code>setTextLimit()</code>,
 * it will be the constant <code>Text.LIMIT</code>.
 * </p>
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
	int limit = (int)OS.SendMessage (handle, OS.EM_GETLIMITTEXT, 0, 0) & 0x7FFFFFFF;
	if (segments != null && limit < LIMIT) limit = Math.max (1, limit - segments.length);
	return limit;
}

/**
 * Returns the zero-relative index of the line which is currently
 * at the top of the receiver.
 * <p>
 * This index can change when lines are scrolled or new lines are added or removed.
 * </p>
 *
 * @return the index of the top line
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopIndex () {
	checkWidget ();
	if ((style & SWT.SINGLE) != 0) return 0;
	return (int)OS.SendMessage (handle, OS.EM_GETFIRSTVISIBLELINE, 0, 0);
}

/**
 * Returns the top SWT logical point.
 * <p>
 * The top point is the SWT logical point position of the line
 * that is currently at the top of the widget.  On
 * some platforms, a text widget can be scrolled by
 * points instead of lines so that a partial line
 * is displayed at the top of the widget.
 * </p><p>
 * The top SWT logical point changes when the widget is scrolled.
 * The top SWT logical point does not include the widget trimming.
 * </p>
 *
 * @return the SWT logical point position of the top line
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getTopPixel () {
	checkWidget ();
	return DPIUtil.autoScaleDown(getTopPixelInPixels());
}

int getTopPixelInPixels () {
	/*
	* Note, EM_GETSCROLLPOS is implemented in Rich Edit 3.0
	* and greater.  The plain text widget and previous versions
	* of Rich Edit return zero.
	*/
	int [] buffer = new int [2];
	long code = OS.SendMessage (handle, OS.EM_GETSCROLLPOS, 0, buffer);
	if (code == 1) return buffer [1];
	return getTopIndex () * getLineHeightInPixels ();
}

/**
 * Inserts a string.
 * <p>
 * The old selection is replaced with the new text.
 * </p>
 *
 * @param string the string
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the string is <code>null</code></li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void insert (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	string = Display.withCrLf (string);
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		int [] start = new int [1], end = new int [1];
		OS.SendMessage (handle, OS.EM_GETSEL, start, end);
		string = verifyText (string, start [0], end [0], null);
		if (string == null) return;
	}
	clearSegments (true);
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
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
	ignoreCharacter = true;
	OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
	ignoreCharacter = false;
	if ((state & HAS_AUTO_DIRECTION) != 0) {
		super.updateTextDirection (AUTO_TEXT_DIRECTION);
	}
	applySegments ();
}

@Override
boolean isUseWsBorder () {
	return super.isUseWsBorder () || ((display != null) && display.useWsBorderText);
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
 */
public void paste () {
	checkWidget ();
	if ((style & SWT.READ_ONLY) != 0) return;
	OS.SendMessage (handle, OS.WM_PASTE, 0, 0);
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	message = null;
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
 * @since 3.8
 */
public void removeSegmentListener (SegmentListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	eventTable.unhook (SWT.Segments, listener);
	clearSegments (true);
	applySegments ();
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
 */
public void removeVerifyListener (VerifyListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Verify, listener);
}

@Override
int resolveTextDirection() {
	int textDirection = SWT.NONE;
	int length = OS.GetWindowTextLength (handle);
	if (length > 0) {
		TCHAR buffer = new TCHAR (getCodePage (), length + 1);
		OS.GetWindowText (handle, buffer, length + 1);
		if (segments != null) {
			buffer = deprocessText (buffer, 0, -1, false);
			textDirection = BidiUtil.resolveTextDirection(buffer.toString ());
		} else {
			textDirection = BidiUtil.resolveTextDirection(buffer.toString (0, length));
		}
		if (textDirection == SWT.NONE) {
			/*
			 * Force direction update also when there are no strong bidi chars.
			*/
			textDirection = (style & SWT.RIGHT_TO_LEFT) != 0 ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT;
		}
	}
	return textDirection;
}

/**
 * Selects all the text in the receiver.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void selectAll () {
	checkWidget ();
	OS.SendMessage (handle, OS.EM_SETSEL, 0, -1);
}

@Override
boolean sendKeyEvent (int type, int msg, long wParam, long lParam, Event event) {
	if (!super.sendKeyEvent (type, msg, wParam, lParam, event)) {
		return false;
	}
	if ((style & SWT.READ_ONLY) != 0) return true;
	if (ignoreVerify) return true;
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
		if (handle == OS.GetCapture()) return true;
	}

	/* Verify the character */
	String oldText = "";
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	switch (key) {
		case 0x08:	/* Bs */
			if (start [0] == end [0]) {
				if (start [0] == 0) return true;
				int lineStart = (int)OS.SendMessage (handle, OS.EM_LINEINDEX, -1, 0);
				if (start [0] == lineStart) {
					start [0] = start [0] - DELIMITER.length ();
				} else {
					start [0] = start [0] - 1;
				}
				start [0] = Math.max (start [0], 0);
			}
			break;
		case 0x7F:	/* Del */
			if (start [0] == end [0]) {
				int length = OS.GetWindowTextLength (handle);
				if (start [0] == length) return true;
				int line = (int)OS.SendMessage (handle, OS.EM_LINEFROMCHAR, end [0], 0);
				int lineStart = (int)OS.SendMessage (handle, OS.EM_LINEINDEX, line + 1, 0);
				if (end [0] == lineStart - DELIMITER.length ()) {
					end [0] = end [0] + DELIMITER.length ();
				} else {
					end [0] = end [0] + 1;
				}
				end [0] = Math.min (end [0], length);
			}
			break;
		case '\r':	/* Return */
			if ((style & SWT.SINGLE) != 0) return true;
			oldText = DELIMITER;
			break;
		default:	/* Tab and other characters */
			if (key != '\t' && key < 0x20) return true;
			oldText = new String (new char [] {key});
			break;
	}
	String newText = verifyText (oldText, start [0], end [0], event);
	if (newText == null) return false;
	if (newText == oldText) return true;
	newText = Display.withCrLf (newText);
	TCHAR buffer = new TCHAR (getCodePage (), newText, true);
	OS.SendMessage (handle, OS.EM_SETSEL, start [0], end [0]);
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
	ignoreCharacter = true;
	OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
	ignoreCharacter = false;
	return false;
}

@Override
void setBackgroundImage (long hBitmap) {
	int flags = OS.RDW_ERASE | OS.RDW_ALLCHILDREN | OS.RDW_INVALIDATE;
	OS.RedrawWindow (handle, null, 0, flags);
}

@Override
void setBackgroundPixel (int pixel) {
	maybeEnableDarkSystemTheme();
	int flags = OS.RDW_ERASE | OS.RDW_ALLCHILDREN | OS.RDW_INVALIDATE;
	OS.RedrawWindow (handle, null, 0, flags);
}

@Override
void setBoundsInPixels (int x, int y, int width, int height, int flags) {
	/*
	* Feature in Windows.  When the caret is moved,
	* the text widget scrolls to show the new location.
	* This means that the text widget may be scrolled
	* to the right in order to show the caret when the
	* widget is not large enough to show both the caret
	* location and all the text.  Unfortunately, when
	* the text widget is resized such that all the text
	* and the caret could be visible, Windows does not
	* scroll the widget back.  The fix is to resize the
	* text widget, set the selection to the start of the
	* text and then restore the selection.  This will
	* cause the text widget compute the correct scroll
	* position.
	*/
	if ((flags & OS.SWP_NOSIZE) == 0 && width != 0) {
		RECT rect = new RECT ();
		OS.GetWindowRect (handle, rect);
		long margins = OS.SendMessage (handle, OS.EM_GETMARGINS, 0, 0);
		int marginWidth = OS.LOWORD (margins) + OS.HIWORD (margins);
		if (rect.right - rect.left <= marginWidth) {
			int [] start = new int [1], end = new int [1];
			OS.SendMessage (handle, OS.EM_GETSEL, start, end);
			if (start [0] != 0 || end [0] != 0) {
				OS.SetWindowPos (handle, 0, x, y, width, height, flags);
				OS.SendMessage (handle, OS.EM_SETSEL, 0, 0);
				OS.SendMessage (handle, OS.EM_SETSEL, start [0], end [0]);
				return;
			}
		}
	}
	super.setBoundsInPixels (x, y, width, height, flags);

	/*
	* Bug in Windows. If the client area height is smaller than
	* the font height, then the multi-line text widget does not
	* update the formatting rectangle when resized. The fix is to
	* detect this case and explicitly set the formatting rectangle.
	*/
	if ((flags & OS.SWP_NOSIZE) == 0) {
		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
		if ((bits & OS.ES_MULTILINE) != 0) {
			long newFont, oldFont = 0;
			long hDC = OS.GetDC (handle);
			newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
			if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
			TEXTMETRIC tm = new TEXTMETRIC ();
			OS.GetTextMetrics (hDC, tm);
			if (newFont != 0) OS.SelectObject (hDC, oldFont);
			OS.ReleaseDC (handle, hDC);
			RECT rect = new RECT();
			OS.GetClientRect (handle, rect);
			if ((rect.bottom - rect.top) < tm.tmHeight) {
				long margins = OS.SendMessage (handle, OS.EM_GETMARGINS, 0, 0);
				rect.left += OS.LOWORD (margins);
				rect.right -= OS.HIWORD (margins);
				rect.top = 0;
				rect.bottom = tm.tmHeight;
				OS.SendMessage (handle, OS.EM_SETRECT, 0, rect);
			}
		}
	}
}

@Override
void setDefaultFont () {
	super.setDefaultFont ();
	setMargins ();
}

/**
 * Sets the double click enabled flag.
 * <p>
 * The double click flag enables or disables the
 * default action of the text widget when the user
 * double clicks.
 * </p><p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
 *
 * @param doubleClick the new double click flag
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setDoubleClickEnabled (boolean doubleClick) {
	checkWidget ();
	this.doubleClick = doubleClick;
}

/**
 * Sets the echo character.
 * <p>
 * The echo character is the character that is
 * displayed when the user enters text or the
 * text is changed by the programmer. Setting
 * the echo character to '\0' clears the echo
 * character and redraws the original text.
 * If for any reason the echo character is invalid,
 * or if the platform does not allow modification
 * of the echo character, the default echo character
 * for the platform is used.
 * </p>
 *
 * @param echo the new echo character
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEchoChar (char echo) {
	checkWidget ();
	if ((style & SWT.MULTI) != 0) return;
	allowPasswordChar = true;
	OS.SendMessage (handle, OS.EM_SETPASSWORDCHAR, echo, 0);
	allowPasswordChar = false;
	/*
	* Bug in Windows.  When the password character is changed,
	* Windows does not redraw to show the new password character.
	* The fix is to force a redraw when the character is set.
	*/
	OS.InvalidateRect (handle, null, true);
}

/**
 * Sets the editable state.
 *
 * @param editable the new editable state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setEditable (boolean editable) {
	checkWidget ();
	style &= ~SWT.READ_ONLY;
	if (!editable) style |= SWT.READ_ONLY;
	OS.SendMessage (handle, OS.EM_SETREADONLY, editable ? 0 : 1, 0);
}

@Override
public void setFont (Font font) {
	checkWidget ();
	super.setFont (font);
	setTabStops (tabs);
	setMargins ();
}

@Override
void setForegroundPixel (int pixel) {
	maybeEnableDarkSystemTheme();
	super.setForegroundPixel(pixel);
}

void setMargins () {
	if ((style & SWT.SEARCH) != 0) {
		boolean rtl = (style & SWT.RIGHT_TO_LEFT) != 0;
		int fLeading = rtl ? OS.EC_RIGHTMARGIN : OS.EC_LEFTMARGIN;
		int fTrailing = rtl ? OS.EC_LEFTMARGIN : OS.EC_RIGHTMARGIN;
		int flags = 0;
		if ((style & SWT.ICON_SEARCH) != 0) flags |= fLeading;
		if ((style & SWT.ICON_CANCEL) != 0) flags |= fTrailing;
		if (flags != 0) {
			int iconWidth = OS.GetSystemMetrics (OS.SM_CXSMICON);
			OS.SendMessage (handle, OS.EM_SETMARGINS, flags, OS.MAKELPARAM(iconWidth, iconWidth));
		}
	}
}

/**
 * Sets the widget message. The message text is displayed
 * as a hint for the user, indicating the purpose of the field.
 * <p>
 * Typically this is used in conjunction with <code>SWT.SEARCH</code>.
 * </p>
 *
 * @param message the new message
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the message is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.3
 */
public void setMessage (String message) {
	checkWidget ();
	if (message == null) error (SWT.ERROR_NULL_ARGUMENT);
	this.message = message;
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.ES_MULTILINE) == 0) {
		int length = message.length ();
		char [] chars = new char [length + 1];
		message.getChars(0, length, chars, 0);
		OS.SendMessage (handle, OS.EM_SETCUEBANNER, 0, chars);
	}
}

/**
 * Sets the orientation of the receiver, which must be one
 * of the constants <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
 * <p>
 * Note: This operation is a hint and is not supported on
 * platforms that do not have this concept.
 * </p>
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

/**
 * Sets the selection.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p><p>
 * Text selections are specified in terms of
 * caret positions.  In a text widget that
 * contains N characters, there are N+1 caret
 * positions, ranging from 0..N.  This differs
 * from other functions that address character
 * position such as getText () that use the
 * regular array indexing rules.
 * </p>
 *
 * @param start new caret position
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int start) {
	checkWidget ();
	start = translateOffset (start);
	OS.SendMessage (handle, OS.EM_SETSEL, start, start);
	OS.SendMessage (handle, OS.EM_SCROLLCARET, 0, 0);
}

/**
 * Sets the selection to the range specified
 * by the given start and end indices.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p><p>
 * Text selections are specified in terms of
 * caret positions.  In a text widget that
 * contains N characters, there are N+1 caret
 * positions, ranging from 0..N.  This differs
 * from other functions that address character
 * position such as getText () that use the
 * usual array indexing rules.
 * </p>
 *
 * @param start the start of the range
 * @param end the end of the range
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (int start, int end) {
	checkWidget ();
	start = translateOffset (start);
	end = translateOffset (end);
	OS.SendMessage (handle, OS.EM_SETSEL, start, end);
	OS.SendMessage (handle, OS.EM_SCROLLCARET, 0, 0);
}

@Override
public void setRedraw (boolean redraw) {
	checkWidget ();
	super.setRedraw (redraw);
	/*
	* Feature in Windows.  When WM_SETREDRAW is used to turn
	* redraw off, the edit control is not scrolled to show the
	* i-beam.  The fix is to detect that the i-beam has moved
	* while redraw is turned off and force it to be visible
	* when redraw is restored.
	*/
	if (!getDrawing ()) return;
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	if (!redraw) {
		oldStart = start [0];  oldEnd = end [0];
	} else {
		if (oldStart == start [0] && oldEnd == end [0]) return;
		OS.SendMessage (handle, OS.EM_SCROLLCARET, 0, 0);
	}
}

/**
 * Sets the selection to the range specified
 * by the given point, where the x coordinate
 * represents the start index and the y coordinate
 * represents the end index.
 * <p>
 * Indexing is zero based.  The range of
 * a selection is from 0..N where N is
 * the number of characters in the widget.
 * </p><p>
 * Text selections are specified in terms of
 * caret positions.  In a text widget that
 * contains N characters, there are N+1 caret
 * positions, ranging from 0..N.  This differs
 * from other functions that address character
 * position such as getText () that use the
 * usual array indexing rules.
 * </p>
 *
 * @param selection the point
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
	setSelection (selection.x, selection.y);
}

/**
 * Sets the number of tabs.
 * <p>
 * Tab stop spacing is specified in terms of the
 * space (' ') character.  The width of a single
 * tab stop is the pixel width of the spaces.
 * </p>
 *
 * @param tabs the number of tabs
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setTabs (int tabs) {
	checkWidget ();
	if (tabs < 0) return;
	setTabStops (this.tabs = tabs);
}

void setTabStops (int tabs) {
	/*
	* Feature in Windows.  Windows expects the tab spacing in
	* dialog units so we must convert from space widths.  Due
	* to round off error, the tab spacing may not be the exact
	* number of space widths, depending on the font.
	*/
	int width = (getTabWidth (tabs) * 4) / OS.LOWORD (OS.GetDialogBaseUnits ());
	OS.SendMessage (handle, OS.EM_SETTABSTOPS, 1, new int [] {width});
}

/**
 * Sets the contents of the receiver to the given string. If the receiver has style
 * SINGLE and the argument contains multiple lines of text, the result of this
 * operation is undefined and may vary from platform to platform.
 * <p>
 * Note: If control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
 * </p>
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
	string = Display.withCrLf (string);
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		int length = OS.GetWindowTextLength (handle);
		string = verifyText (string, 0, length, null);
		if (string == null) return;
	}
	clearSegments (false);
	int limit = (int)OS.SendMessage (handle, OS.EM_GETLIMITTEXT, 0, 0) & 0x7FFFFFFF;
	if (string.length () > limit) string = string.substring (0, limit);
	TCHAR buffer = new TCHAR (getCodePage (), string, true);
	OS.SetWindowText (handle, buffer);
	if ((state & HAS_AUTO_DIRECTION) != 0) {
		super.updateTextDirection(AUTO_TEXT_DIRECTION);
	}
	applySegments ();
	/*
	* Bug in Windows.  When the widget is multi line
	* text widget, it does not send a WM_COMMAND with
	* control code EN_CHANGE from SetWindowText () to
	* notify the application that the text has changed.
	* The fix is to send the event.
	*/
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.ES_MULTILINE) != 0) {
		sendEvent (SWT.Modify);
		// widget could be disposed at this point
	}
}

/**
 * Sets the contents of the receiver to the characters in the array. If the receiver
 * has style <code>SWT.SINGLE</code> and the argument contains multiple lines of text
 * then the result of this operation is undefined and may vary between platforms.
 * <p>
 * Note: Use this API to prevent the text from being written into a String
 * object whose lifecycle is outside of your control. This can help protect
 * the text, for example, when the widget is used as a password field.
 * However, the text can't be protected if an {@link SWT#Segments} or
 * {@link SWT#Verify} listener has been added to the widget.
 * </p>
 *
 * @param text a character array that contains the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the array is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getTextChars()
 *
 * @since 3.7
 */
public void setTextChars (char[] text) {
	checkWidget ();
	if (text == null) error (SWT.ERROR_NULL_ARGUMENT);
	text = Display.withCrLf (text);
	if (hooks (SWT.Verify) || filters (SWT.Verify)) {
		int length = OS.GetWindowTextLength (handle);
		String string = verifyText (new String (text), 0, length, null);
		if (string == null) return;
		text = new char [string.length()];
		string.getChars (0, text.length, text, 0);
	}
	clearSegments (false);
	int limit = (int)OS.SendMessage (handle, OS.EM_GETLIMITTEXT, 0, 0) & 0x7FFFFFFF;
	if (text.length > limit) {
		char [] temp = new char [limit];
		System.arraycopy(text, 0, temp, 0, limit);
		text = temp;
	}
	TCHAR buffer = new TCHAR (getCodePage (), text, true);
	OS.SetWindowText (handle, buffer);
	buffer.clear ();
	if ((state & HAS_AUTO_DIRECTION) != 0) {
		super.updateTextDirection (AUTO_TEXT_DIRECTION);
	}
	applySegments ();
	/*
	* Bug in Windows.  When the widget is multi line
	* text widget, it does not send a WM_COMMAND with
	* control code EN_CHANGE from SetWindowText () to
	* notify the application that the text has changed.
	* The fix is to send the event.
	*/
	int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
	if ((bits & OS.ES_MULTILINE) != 0) {
		sendEvent (SWT.Modify);
		// widget could be disposed at this point
	}
}

/**
 * Sets the maximum number of characters that the receiver
 * is capable of holding to be the argument.
 * <p>
 * Instead of trying to set the text limit to zero, consider
 * creating a read-only text widget.
 * </p><p>
 * To reset this value to the default, use <code>setTextLimit(Text.LIMIT)</code>.
 * Specifying a limit value larger than <code>Text.LIMIT</code> sets the
 * receiver's limit to <code>Text.LIMIT</code>.
 * </p>
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
 *
 * @see #LIMIT
 */
public void setTextLimit (int limit) {
	checkWidget ();
	if (limit == 0) error (SWT.ERROR_CANNOT_BE_ZERO);
	if (segments != null && limit > 0) {
		OS.SendMessage (handle, OS.EM_SETLIMITTEXT, limit + Math.min (segments.length, LIMIT - limit), 0);
	} else {
		OS.SendMessage (handle, OS.EM_SETLIMITTEXT, limit, 0);
	}
}

/**
 * Sets the zero-relative index of the line which is currently
 * at the top of the receiver. This index can change when lines
 * are scrolled or new lines are added and removed.
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
	if ((style & SWT.SINGLE) != 0) return;
	int count = (int)OS.SendMessage (handle, OS.EM_GETLINECOUNT, 0, 0);
	index = Math.min (Math.max (index, 0), count - 1);
	int topIndex = (int)OS.SendMessage (handle, OS.EM_GETFIRSTVISIBLELINE, 0, 0);
	OS.SendMessage (handle, OS.EM_LINESCROLL, 0, index - topIndex);
}

/**
 * Shows the selection.
 * <p>
 * If the selection is already showing
 * in the receiver, this method simply returns.  Otherwise,
 * lines are scrolled until the selection is visible.
 * </p>
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void showSelection () {
	checkWidget ();
	OS.SendMessage (handle, OS.EM_SCROLLCARET, 0, 0);
}

int translateOffset (int offset) {
	if (segments == null) return offset;
	for (int i = 0, nSegments = segments.length; i < nSegments && offset - i >= segments [i]; i++) {
		offset++;
	}
	return offset;
}

int untranslateOffset (int offset) {
	if (segments == null) return offset;
	for (int i = 0, nSegments = segments.length; i < nSegments && offset > segments [i]; i++) {
		offset--;
	}
	return offset;
}

@Override
void updateMenuLocation (Event event) {
	Point point = display.mapInPixels (this, null, getCaretLocationInPixels ());
	event.setLocationInPixels(point.x, point.y + getLineHeightInPixels ());
}

@Override
void updateOrientation (){
	int bits = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
	if ((style & SWT.RIGHT_TO_LEFT) != 0) {
		bits |= OS.WS_EX_RTLREADING | OS.WS_EX_LEFTSCROLLBAR;
	} else {
		bits &= ~(OS.WS_EX_RTLREADING | OS.WS_EX_LEFTSCROLLBAR);
	}
	OS.SetWindowLong (handle, OS.GWL_EXSTYLE, bits);
	fixAlignment ();
}

@Override
boolean updateTextDirection(int textDirection) {
	if (super.updateTextDirection(textDirection)) {
		clearSegments (true);
		applySegments ();
		return true;
	}
	return false;
}

String verifyText (String string, int start, int end, Event keyEvent) {
	if (ignoreVerify) return string;
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
int widgetStyle () {
	int bits = super.widgetStyle () | OS.ES_AUTOHSCROLL;
	/*
	 * NOTE: ICON_CANCEL and ICON_SEARCH have the same value as H_SCROLL and
	 * V_SCROLL. The meaning is determined by whether SWT.SEARCH is set.
	 */
	if ((style & SWT.SEARCH) != 0) {
		bits &= ~OS.WS_HSCROLL;
		bits &= ~OS.WS_VSCROLL;
	}
	if ((style & SWT.PASSWORD) != 0) bits |= OS.ES_PASSWORD;
	if ((style & SWT.CENTER) != 0) bits |= OS.ES_CENTER;
	if ((style & SWT.RIGHT) != 0) bits |= OS.ES_RIGHT;
	if ((style & SWT.READ_ONLY) != 0) bits |= OS.ES_READONLY;
	if ((style & SWT.SEARCH) != 0) bits |= OS.WS_CLIPCHILDREN;
	if ((style & SWT.SINGLE) != 0) {
		/*
		* Feature in Windows.  When a text control is read-only,
		* uses COLOR_3DFACE for the background .  If the text
		* controls single-line and is within a tab folder or
		* some other themed control, using WM_ERASEBKGND and
		* WM_CTRCOLOR to draw the theme background results in
		* pixel corruption.  The fix is to use an ES_MULTILINE
		* text control instead.
		* Refer Bug438901:- ES_MULTILINE doesn't apply for:
		* SWT.PASSWORD | SWT.READ_ONLY style combination.
		*/
		if ((style & SWT.READ_ONLY) != 0) {
			if ((style & (SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.PASSWORD)) == 0) {
				if (OS.IsAppThemed ()) {
					bits |= OS.ES_MULTILINE;
				}
			}
		}
		return bits;
	}
	bits |= OS.ES_MULTILINE | OS.ES_NOHIDESEL | OS.ES_AUTOVSCROLL;
	if ((style & SWT.WRAP) != 0) bits &= ~(OS.WS_HSCROLL | OS.ES_AUTOHSCROLL);
	return bits;
}

@Override
TCHAR windowClass () {
	return EditClass;
}

@Override
long windowProc () {
	return EditProc;
}

@Override
long windowProc (long hwnd, int msg, long wParam, long lParam) {
	boolean processSegments = hooks (SWT.Segments) || filters (SWT.Segments), redraw = false, updateDirection = (state & HAS_AUTO_DIRECTION) != 0;
	long code;
	if (processSegments || updateDirection) {
		switch (msg) {
			case OS.EM_CANUNDO:
				if (processSegments) return 0;
				updateDirection = false;
				break;
			case OS.EM_UNDO:
			case OS.WM_UNDO:
				if (processSegments) return 0;
				break;
			case OS.WM_KEYDOWN:
				if (wParam != OS.VK_DELETE) {
					processSegments = updateDirection = false;
				}
				break;
			case OS.WM_COPY:
				processSegments = segments != null;
				updateDirection = false;
				break;
			case OS.WM_CHAR:
				if (ignoreCharacter || OS.GetKeyState (OS.VK_CONTROL) < 0 || OS.GetKeyState (OS.VK_MENU) < 0) {
					processSegments = updateDirection = false;
				}
				break;
			case OS.WM_PASTE:
			case OS.WM_CUT:
			case OS.WM_CLEAR:
				break;
			default:
				processSegments = updateDirection = false;
		}
	}
	if (processSegments) {
		if (getDrawing () && OS.IsWindowVisible (handle)) {
			redraw = true;
			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 0, 0);
		}
		clearSegments (true);
	}
	if (msg == OS.EM_UNDO) {
		int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
		if ((bits & OS.ES_MULTILINE) == 0) {
			LRESULT result = wmClipboard (OS.EM_UNDO, wParam, lParam);
			if (result != null) return result.value;
			return callWindowProc (hwnd, OS.EM_UNDO, wParam, lParam);
		}
	}
	if (msg == OS.EM_SETPASSWORDCHAR) {
		if (!allowPasswordChar) {
			return 1;
		}
	}
	if (msg == Display.SWT_RESTORECARET) {
		callWindowProc (hwnd, OS.WM_KILLFOCUS, 0, 0);
		callWindowProc (hwnd, OS.WM_SETFOCUS, 0, 0);
		return 1;
	}
	code = super.windowProc (hwnd, msg, wParam, lParam);
	if (updateDirection) {
		super.updateTextDirection (AUTO_TEXT_DIRECTION);
	}
	if (processSegments) {
		applySegments ();
		if (redraw) {
			OS.DefWindowProc (handle, OS.WM_SETREDRAW, 1, 0);
			OS.InvalidateRect (handle, null, true);
		}
		OS.SendMessage (handle, OS.EM_SCROLLCARET, 0, 0);
	}
	return code;
}

@Override
LRESULT WM_CHAR (long wParam, long lParam) {
	if (ignoreCharacter) return null;
	LRESULT result = super.WM_CHAR (wParam, lParam);
	if (result != null) return result;

	/*
	* Bug in Windows.  When the user types CTRL and BS
	* in an edit control, a DEL character is generated.
	* Rather than deleting the text, the DEL character
	* is inserted into the control.  The fix is to detect
	* this case and not call the window proc.
	*/
	switch ((int)wParam) {
		case SWT.DEL:
			if (OS.GetKeyState (OS.VK_CONTROL) < 0) {
				if ((style & SWT.READ_ONLY) != 0 || (style & SWT.PASSWORD) != 0) return LRESULT.ZERO;
				Point selection = getSelection ();
				int x = selection.x;
				int y = selection.y;
				if (x == y) {
					String actText = getText (0, x - 1);
					java.util.regex.Matcher m = CTRL_BS_PATTERN.matcher (actText);
					if (m.find ()) {
						x = m.start ();
						y = m.end ();
						OS.SendMessage (handle, OS.EM_SETSEL, x, y);
					}
				}
				if (x < y) {
					/*
					* Instead of setting the new text directly we send the replace selection event to
					* guarantee that the action is pushed to the undo buffer.
					*/
					OS.SendMessage (handle, OS.EM_REPLACESEL, 1, 0);
				}
				return LRESULT.ZERO;
			}
	}

	/*
	* Feature in Windows.  For some reason, when the
	* widget is a single line text widget, when the
	* user presses tab, return or escape, Windows beeps.
	* The fix is to look for these keys and not call
	* the window proc.
	*/
	if ((style & SWT.SINGLE) != 0) {
		switch ((int)wParam) {
			case SWT.CR:
				sendSelectionEvent (SWT.DefaultSelection);
				// FALL THROUGH
			case SWT.TAB:
			case SWT.ESC: return LRESULT.ZERO;
		}
	}
	return result;
}

@Override
LRESULT WM_CLEAR (long wParam, long lParam) {
	LRESULT result = super.WM_CLEAR (wParam, lParam);
	if (result != null) return result;
	return wmClipboard (OS.WM_CLEAR, wParam, lParam);
}

@Override
LRESULT WM_CUT (long wParam, long lParam) {
	LRESULT result = super.WM_CUT (wParam, lParam);
	if (result != null) return result;
	return wmClipboard (OS.WM_CUT, wParam, lParam);
}

@Override
LRESULT WM_DRAWITEM (long wParam, long lParam) {
	DRAWITEMSTRUCT struct = new DRAWITEMSTRUCT ();
	OS.MoveMemory (struct, lParam, DRAWITEMSTRUCT.sizeof);
	RECT rect = new RECT ();
	OS.SetRect (rect, struct.left, struct.top, struct.right, struct.bottom);
	POINT pt = new POINT ();
	OS.MapWindowPoints (struct.hwndItem, handle, pt, 1);
	drawBackground (struct.hDC, rect, -1, pt.x, pt.y);
	if (struct.CtlID == SWT.ICON_CANCEL && struct.hwndItem == hwndActiveIcon && OS.IsAppThemed()) {
		int state = OS.GetKeyState (OS.VK_LBUTTON) < 0 ? OS.PBS_PRESSED : OS.PBS_HOT;
		OS.DrawThemeBackground (display.hButtonThemeAuto (), struct.hDC, OS.BP_PUSHBUTTON, state, rect, null);
	}
	long hIcon = (struct.CtlID == SWT.ICON_SEARCH) ? display.hIconSearch : display.hIconCancel;
	int y = (rect.bottom - rect.right) / 2;
	OS.DrawIconEx (struct.hDC, 0, y, hIcon, 0, 0, 0, 0, OS.DI_NORMAL);
	return LRESULT.ONE;
}

@Override
LRESULT WM_ERASEBKGND (long wParam, long lParam) {
	LRESULT result = super.WM_ERASEBKGND (wParam, lParam);
	if ((style & SWT.READ_ONLY) != 0) {
		if ((style & (SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
			int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
			if ((bits & OS.ES_MULTILINE) != 0) {
				Control control = findBackgroundControl ();
				if (control == null && background == -1) {
					if ((state & THEME_BACKGROUND) != 0) {
						if (OS.IsAppThemed ()) {
							control = findThemeControl ();
							if (control != null) {
								RECT rect = new RECT ();
								OS.GetClientRect (handle, rect);
								fillThemeBackground (wParam, control, rect);
								return LRESULT.ONE;
							}
						}
					}
				}
			}
		}
	}
	return result;
}

@Override
LRESULT WM_GETDLGCODE (long wParam, long lParam) {
	LRESULT result = super.WM_GETDLGCODE (wParam, lParam);
	if (result != null) return result;

	/*
	* Feature in Windows.  Despite the fact that the
	* edit control is read only, it still returns a
	* dialog code indicating that it wants all keys.
	* The fix is to detect this case and clear the bits.
	*
	* NOTE: A read only edit control processes arrow keys
	* so DLGC_WANTARROWS should not be cleared.
	*/
	if ((style & SWT.READ_ONLY) != 0) {
		long code = callWindowProc (handle, OS.WM_GETDLGCODE, wParam, lParam);
		code &= ~(OS.DLGC_WANTALLKEYS | OS.DLGC_WANTTAB);
		return new LRESULT (code);
	}
	return null;
}

@Override
LRESULT WM_GETOBJECT (long wParam, long lParam) {
	/*
	* Ensure that there is an accessible object created for this
	* control because support for search text accessibility is
	* implemented in the accessibility package.
	*/
	if ((style & SWT.SEARCH) != 0) {
		if (accessible == null) accessible = new_Accessible (this);
	}
	return super.WM_GETOBJECT (wParam, lParam);
}

@Override
LRESULT WM_IME_CHAR (long wParam, long lParam) {

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
	long result = callWindowProc (handle, OS.WM_IME_CHAR, wParam, lParam);
	MSG msg = new MSG ();
	int flags = OS.PM_REMOVE | OS.PM_NOYIELD | OS.PM_QS_INPUT | OS.PM_QS_POSTMESSAGE;
	while (OS.PeekMessage (msg, handle, OS.WM_CHAR, OS.WM_CHAR, flags)) {
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
LRESULT WM_LBUTTONDBLCLK (long wParam, long lParam) {
	/*
	* Prevent Windows from processing WM_LBUTTONDBLCLK
	* when double clicking behavior is disabled by not
	* calling the window proc.
	*/
	LRESULT result = null;
	sendMouseEvent (SWT.MouseDown, 1, handle, lParam);
	if (!sendMouseEvent (SWT.MouseDoubleClick, 1, handle, lParam)) {
		result = LRESULT.ZERO;
	}
	if (!display.captureChanged && !isDisposed ()) {
		if (OS.GetCapture () != handle) OS.SetCapture (handle);
	}
	if (!doubleClick) return LRESULT.ZERO;

	/*
	* Bug in Windows.  When the last line of text in the
	* widget is double clicked and the line is empty, Windows
	* hides the i-beam then moves it to the first line in
	* the widget but does not scroll to show the user.
	* If the user types without clicking the mouse, invalid
	* characters are displayed at the end of each line of
	* text in the widget.  The fix is to detect this case
	* and avoid calling the window proc.
	*/
	int [] start = new int [1], end = new int [1];
	OS.SendMessage (handle, OS.EM_GETSEL, start, end);
	if (start [0] == end [0]) {
		int length = OS.GetWindowTextLength (handle);
		if (length == start [0]) {
			int code = (int)OS.SendMessage (handle, OS.EM_LINELENGTH, length, 0);
			if (code == 0) return LRESULT.ZERO;
		}
	}
	return result;
}

@Override
LRESULT WM_PASTE (long wParam, long lParam) {
	LRESULT result = super.WM_PASTE (wParam, lParam);
	if (result != null) return result;
	return wmClipboard (OS.WM_PASTE, wParam, lParam);
}

@Override
LRESULT WM_SETCURSOR (long wParam, long lParam) {
	LRESULT result = super.WM_SETCURSOR(wParam, lParam);
	if (result != null) return result;
	if ((style & SWT.SEARCH) != 0) {
		int hitTest = (short) OS.LOWORD (lParam);
		if (hitTest == OS.HTCLIENT) {
			POINT pt = new POINT ();
			OS.GetCursorPos (pt);
			OS.ScreenToClient (handle, pt);
			long hwndIcon = OS.ChildWindowFromPointEx (handle, pt, OS.CWP_SKIPINVISIBLE);
			if (hwndIcon != handle) {
				OS.SetCursor (OS.LoadCursor (0, OS.IDC_ARROW));
				return LRESULT.ONE;
			}
		}
	}
	return null;
}

@Override
LRESULT WM_SIZE(long wParam, long lParam) {
	LRESULT result = super.WM_SIZE (wParam, lParam);
	if (isDisposed ()) return result;
	if ((style & SWT.SEARCH) != 0) {
		/* NOTE: EDIT controls don't support mirrored layout. */
		boolean rtl = (style & SWT.RIGHT_TO_LEFT) != 0;
		long hwndLeading = OS.GetDlgItem (handle, rtl ? SWT.ICON_CANCEL : SWT.ICON_SEARCH);
		long hwndTrailing = OS.GetDlgItem (handle, rtl ? SWT.ICON_SEARCH : SWT.ICON_CANCEL);
		int width = OS.LOWORD (lParam);
		int height = OS.HIWORD (lParam);
		int iconWidth = OS.GetSystemMetrics (OS.SM_CXSMICON);
		int flags = OS.SWP_NOZORDER | OS.SWP_NOACTIVATE | OS.SWP_NOCOPYBITS;
		if (hwndLeading != 0) OS.SetWindowPos (hwndLeading, 0, 0, 0, iconWidth, height, flags);
		if (hwndTrailing != 0) OS.SetWindowPos (hwndTrailing, 0, width - iconWidth, 0, iconWidth, height, flags);
	}
	return result;
}

@Override
LRESULT WM_UNDO (long wParam, long lParam) {
	LRESULT result = super.WM_UNDO (wParam, lParam);
	if (result != null) return result;
	return wmClipboard (OS.WM_UNDO, wParam, lParam);
}

LRESULT wmClipboard (int msg, long wParam, long lParam) {
	if ((style & SWT.READ_ONLY) != 0) return null;
	if (!hooks (SWT.Verify) && !filters (SWT.Verify)) return null;
	boolean call = false;
	int [] start = new int [1], end = new int [1];
	String newText = null;
	switch (msg) {
		case OS.WM_CLEAR:
		case OS.WM_CUT:
			OS.SendMessage (handle, OS.EM_GETSEL, start, end);
			if (start [0] != end [0]) {
				newText = "";
				call = true;
			}
			break;
		case OS.WM_PASTE:
			OS.SendMessage (handle, OS.EM_GETSEL, start, end);
			newText = getClipboardText ();
			break;
		case OS.EM_UNDO:
		case OS.WM_UNDO:
			if (OS.SendMessage (handle, OS.EM_CANUNDO, 0, 0) != 0) {
				ignoreModify = ignoreCharacter = true;
				callWindowProc (handle, msg, wParam, lParam);
				int length = OS.GetWindowTextLength (handle);
				int [] newStart = new int [1], newEnd = new int [1];
				OS.SendMessage (handle, OS.EM_GETSEL, newStart, newEnd);
				if (length != 0 && newStart [0] != newEnd [0]) {
					char [] buffer = new char [length + 1];
					OS.GetWindowText (handle, buffer, length + 1);
					newText = new String (buffer, newStart [0], newEnd [0] - newStart [0]);
				} else {
					newText = "";
				}
				callWindowProc (handle, msg, wParam, lParam);
				OS.SendMessage (handle, OS.EM_GETSEL, start, end);
				ignoreModify = ignoreCharacter = false;
			}
			break;
	}
	if (newText != null) {
		String oldText = newText;
		newText = verifyText (newText, start [0], end [0], null);
		if (newText == null) return LRESULT.ZERO;
		if (!newText.equals (oldText)) {
			if (call) {
				callWindowProc (handle, msg, wParam, lParam);
			}
			newText = Display.withCrLf (newText);
			TCHAR buffer = new TCHAR (getCodePage (), newText, true);
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
			ignoreCharacter = true;
			OS.SendMessage (handle, OS.EM_REPLACESEL, 0, buffer);
			ignoreCharacter = false;
			return LRESULT.ZERO;
		}
	}
	if (msg == OS.WM_UNDO) {
		ignoreVerify = ignoreCharacter = true;
		callWindowProc (handle, OS.WM_UNDO, wParam, lParam);
		ignoreVerify = ignoreCharacter = false;
		return LRESULT.ONE;
	}
	return null;
}

@Override
LRESULT wmColorChild (long wParam, long lParam) {
	if ((style & SWT.READ_ONLY) != 0) {
		if ((style & (SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL)) == 0) {
			int bits = OS.GetWindowLong (handle, OS.GWL_STYLE);
			if ((bits & OS.ES_MULTILINE) != 0) {
				Control control = findBackgroundControl ();
				if (control == null && background == -1) {
					if ((state & THEME_BACKGROUND) != 0) {
						if (OS.IsAppThemed ()) {
							control = findThemeControl ();
							if (control != null) {
								OS.SetTextColor (wParam, getForegroundPixel ());
								OS.SetBkColor (wParam, getBackgroundPixel ());
								OS.SetBkMode (wParam, OS.TRANSPARENT);
								return new LRESULT (OS.GetStockObject (OS.NULL_BRUSH));
							}
						}
					}
				}
			}
		}
	}
	return super.wmColorChild (wParam, lParam);
}

@Override
LRESULT wmCommandChild (long wParam, long lParam) {
	int code = OS.HIWORD (wParam);
	switch (code) {
		case OS.EN_CHANGE:
			if (findImageControl () != null) {
				OS.InvalidateRect (handle, null, true);
			}
			if ((style & SWT.SEARCH) != 0) {
				boolean showCancel = OS.GetWindowTextLength (handle) != 0;
				long hwndCancel = OS.GetDlgItem (handle, SWT.ICON_CANCEL);
				if (hwndCancel != 0) OS.ShowWindow (hwndCancel, showCancel ? OS.SW_SHOW : OS.SW_HIDE);
			}
			if (ignoreModify) break;
			/*
			* It is possible (but unlikely), that application
			* code could have disposed the widget in the modify
			* event.  If this happens, end the processing of the
			* Windows message by returning zero as the result of
			* the window proc.
			*/
			sendEvent (SWT.Modify);
			if (isDisposed ()) return LRESULT.ZERO;
			break;
		case OS.EN_ALIGN_LTR_EC:
		case OS.EN_ALIGN_RTL_EC:
			/*
			 * Ctrl + Shift to set explicit LTR or RTL text direction was
			 * pressed, so auto direction should no longer be effective.
			 */
			state &= ~HAS_AUTO_DIRECTION;
			int bits = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
			if ((bits & OS.WS_EX_RTLREADING) != 0) {
				style &= ~SWT.LEFT_TO_RIGHT;
				style |= SWT.RIGHT_TO_LEFT;
			} else {
				style &= ~SWT.RIGHT_TO_LEFT;
				style |= SWT.LEFT_TO_RIGHT;
			}
			Event event = new Event();
			event.doit = true;
			sendEvent(SWT.OrientationChange, event);
			if (!event.doit) {
				if (code == OS.EN_ALIGN_LTR_EC) {
					bits |= (OS.WS_EX_RTLREADING | OS.WS_EX_LEFTSCROLLBAR);
					style &= ~SWT.LEFT_TO_RIGHT;
					style |= SWT.RIGHT_TO_LEFT;
				} else {
					bits &= ~(OS.WS_EX_RTLREADING | OS.WS_EX_LEFTSCROLLBAR);
					style &= ~SWT.RIGHT_TO_LEFT;
					style |= SWT.LEFT_TO_RIGHT;
				}
				OS.SetWindowLong (handle, OS.GWL_EXSTYLE, bits);
			} else {
				clearSegments (true);
				applySegments ();
			}
			fixAlignment();
			break;
	}
	return super.wmCommandChild (wParam, lParam);
}

@Override
LRESULT wmKeyDown (long hwnd, long wParam, long lParam) {
	LRESULT result = super.wmKeyDown (hwnd, wParam, lParam);
	if (result != null) return result;

	if (segments != null) {
		switch ((int)wParam) {
		case OS.VK_LEFT:
		case OS.VK_UP:
		case OS.VK_RIGHT:
		case OS.VK_DOWN:
			long code = 0;
			int [] start = new int [1], end = new int [1], newStart = new int [1], newEnd = new int [1];
			OS.SendMessage (handle, OS.EM_GETSEL, start, end);
			while (true) {
				code = callWindowProc (handle, OS.WM_KEYDOWN, wParam, lParam);
				OS.SendMessage (handle, OS.EM_GETSEL, newStart, newEnd);
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
	}
	return result;
}

}
