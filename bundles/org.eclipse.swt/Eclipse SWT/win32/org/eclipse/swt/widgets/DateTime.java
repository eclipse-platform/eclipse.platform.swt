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

import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are selectable user interface
 * objects that allow the user to enter and modify date
 * or time values.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to add children to it, or set a layout on it.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>DATE, TIME, CALENDAR, SHORT, MEDIUM, LONG, DROP_DOWN</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles DATE, TIME, or CALENDAR may be specified,
 * and only one of the styles SHORT, MEDIUM, or LONG may be specified.
 * The DROP_DOWN style is a <em>HINT</em>, and it is only valid with the DATE style.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#datetime">DateTime snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.3
 * @noextend This class is not intended to be subclassed by clients.
 */

public class DateTime extends Composite {
	boolean doubleClick, ignoreSelection;
	SYSTEMTIME lastSystemTime;
	SYSTEMTIME time = new SYSTEMTIME (); // only used in calendar mode
	static final int /*long*/ DateTimeProc;
	static final TCHAR DateTimeClass = new TCHAR (0, OS.DATETIMEPICK_CLASS, true);
	static final int /*long*/ CalendarProc;
	static final TCHAR CalendarClass = new TCHAR (0, OS.MONTHCAL_CLASS, true);
	static {
		INITCOMMONCONTROLSEX icex = new INITCOMMONCONTROLSEX ();
		icex.dwSize = INITCOMMONCONTROLSEX.sizeof;
		icex.dwICC = OS.ICC_DATE_CLASSES;
		OS.InitCommonControlsEx (icex);
	}
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, DateTimeClass, lpWndClass);
		DateTimeProc = lpWndClass.lpfnWndProc;
		/*
		* Feature in Windows.  The date time window class
		* does not include CS_DBLCLKS.  This means that these
		* controls will not get double click messages such as
		* WM_LBUTTONDBLCLK.  The fix is to register a new 
		* window class with CS_DBLCLKS.
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
		lpWndClass.style &= ~OS.CS_GLOBALCLASS;
		lpWndClass.style |= OS.CS_DBLCLKS;
		int byteCount = DateTimeClass.length () * TCHAR.sizeof;
		int /*long*/ lpszClassName = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		OS.MoveMemory (lpszClassName, DateTimeClass, byteCount);
		lpWndClass.lpszClassName = lpszClassName;
		OS.RegisterClass (lpWndClass);
		OS.HeapFree (hHeap, 0, lpszClassName);
	}
	static {
		WNDCLASS lpWndClass = new WNDCLASS ();
		OS.GetClassInfo (0, CalendarClass, lpWndClass);
		CalendarProc = lpWndClass.lpfnWndProc;
		/*
		* Feature in Windows.  The date time window class
		* does not include CS_DBLCLKS.  This means that these
		* controls will not get double click messages such as
		* WM_LBUTTONDBLCLK.  The fix is to register a new 
		* window class with CS_DBLCLKS.
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
		lpWndClass.style &= ~OS.CS_GLOBALCLASS;
		lpWndClass.style |= OS.CS_DBLCLKS;
		int byteCount = CalendarClass.length () * TCHAR.sizeof;
		int /*long*/ lpszClassName = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
		OS.MoveMemory (lpszClassName, CalendarClass, byteCount);
		lpWndClass.lpszClassName = lpszClassName;
		OS.RegisterClass (lpWndClass);
		OS.HeapFree (hHeap, 0, lpszClassName);
	}
	static final int MARGIN = 4;
	static final int MAX_DIGIT = 9;
	static final int MAX_DAY = 31;
	static final int MAX_12HOUR = 12;
	static final int MAX_24HOUR = 24;
	static final int MAX_MINUTE = 60;
	static final int MONTH_DAY_YEAR = 0;
	static final int DAY_MONTH_YEAR = 1;
	static final int YEAR_MONTH_DAY = 2;
	static final char SINGLE_QUOTE = '\''; //$NON-NLS-1$ short date format may include quoted text
	static final char DAY_FORMAT_CONSTANT = 'd'; //$NON-NLS-1$ 1-4 lowercase 'd's represent day
	static final char MONTH_FORMAT_CONSTANT = 'M'; //$NON-NLS-1$ 1-4 uppercase 'M's represent month
	static final char YEAR_FORMAT_CONSTANT = 'y'; //$NON-NLS-1$ 1-5 lowercase 'y's represent year
	static final char HOURS_FORMAT_CONSTANT = 'h'; //$NON-NLS-1$ 1-2 upper or lowercase 'h's represent hours
	static final char MINUTES_FORMAT_CONSTANT = 'm'; //$NON-NLS-1$ 1-2 lowercase 'm's represent minutes
	static final char SECONDS_FORMAT_CONSTANT = 's'; //$NON-NLS-1$ 1-2 lowercase 's's represent seconds
	static final char AMPM_FORMAT_CONSTANT = 't'; //$NON-NLS-1$ 1-2 lowercase 't's represent am/pm
	static final int[] MONTH_NAMES = new int[] {OS.LOCALE_SMONTHNAME1, OS.LOCALE_SMONTHNAME2, OS.LOCALE_SMONTHNAME3, OS.LOCALE_SMONTHNAME4, OS.LOCALE_SMONTHNAME5, OS.LOCALE_SMONTHNAME6, OS.LOCALE_SMONTHNAME7, OS.LOCALE_SMONTHNAME8, OS.LOCALE_SMONTHNAME9, OS.LOCALE_SMONTHNAME10, OS.LOCALE_SMONTHNAME11, OS.LOCALE_SMONTHNAME12};


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
 * @see SWT#DATE
 * @see SWT#TIME
 * @see SWT#CALENDAR
 * @see SWT#SHORT
 * @see SWT#MEDIUM
 * @see SWT#LONG
 * @see SWT#DROP_DOWN
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public DateTime (Composite parent, int style) {
	super (parent, checkStyle (style));
	if ((this.style & SWT.SHORT) != 0) {
		String buffer = ((this.style & SWT.DATE) != 0) ? getCustomShortDateFormat() : getCustomShortTimeFormat();
		TCHAR lpszFormat = new TCHAR (0, buffer, true);
		OS.SendMessage (handle, OS.DTM_SETFORMAT, 0, lpszFormat);
	}
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the user changes the control's value.
 * <code>widgetDefaultSelected</code> is typically called when ENTER is pressed.
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
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}

int /*long*/ callWindowProc (int /*long*/ hwnd, int msg, int /*long*/ wParam, int /*long*/ lParam) {
	if (handle == 0) return 0;
	return OS.CallWindowProc (windowProc (), hwnd, msg, wParam, lParam);
}

static int checkStyle (int style) {
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	style &= ~(SWT.H_SCROLL | SWT.V_SCROLL);
	style = checkBits (style, SWT.DATE, SWT.TIME, SWT.CALENDAR, 0, 0, 0);
	style = checkBits (style, SWT.MEDIUM, SWT.SHORT, SWT.LONG, 0, 0, 0);
	if ((style & SWT.DATE) == 0) style &=~ SWT.DROP_DOWN;
	return style;
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
		if ((style & SWT.CALENDAR) != 0) {
			RECT rect = new RECT ();
			OS.SendMessage(handle, OS.MCM_GETMINREQRECT, 0, rect);
			width = rect.right;
			height = rect.bottom;
		} else {
			TCHAR buffer = new TCHAR (getCodePage (), 128);
			int /*long*/ newFont, oldFont = 0;
			int /*long*/ hDC = OS.GetDC (handle);
			newFont = OS.SendMessage (handle, OS.WM_GETFONT, 0, 0);
			if (newFont != 0) oldFont = OS.SelectObject (hDC, newFont);
			RECT rect = new RECT ();
			int flags = OS.DT_CALCRECT | OS.DT_EDITCONTROL | OS.DT_NOPREFIX;
			SYSTEMTIME systime = new SYSTEMTIME ();
			if ((style & SWT.DATE) != 0) {
				/* Determine the widest/tallest year string. */
				systime.wMonth = 1;
				systime.wDay = 1;
				int widest = 0, secondWidest = 0, thirdWidest = 0;
				for (int i = 0; i <= MAX_DIGIT; i++) {
					systime.wYear = (short) (2000 + i); // year 2000 + i is guaranteed to exist
					int size = OS.GetDateFormat(OS.LOCALE_USER_DEFAULT, OS.DATE_SHORTDATE, systime, null, buffer, buffer.length ());
					if (size == 0) {
						buffer = new TCHAR (getCodePage (), size);
						OS.GetDateFormat(OS.LOCALE_USER_DEFAULT, OS.DATE_SHORTDATE, systime, null, buffer, buffer.length ());
					}
					rect.left = rect.top = rect.right = rect.bottom = 0;
					OS.DrawText (hDC, buffer, size, rect, flags);
					if (rect.right - rect.left >= width) {
						width = rect.right - rect.left;
						thirdWidest = secondWidest;
						secondWidest = widest;
						widest = i;
					}
					height = Math.max(height, rect.bottom - rect.top);
				}
				if (widest > 1) widest = widest * 1000 + widest * 100 + widest * 10 + widest;
				else if (secondWidest > 1) widest = secondWidest * 1000 + widest * 100 + widest * 10 + widest;
				else widest = thirdWidest * 1000 + widest * 100 + widest * 10 + widest;
				systime.wYear = (short) widest;

				/* Determine the widest/tallest month name string. */
				width = widest = 0;
				for (short i = 0; i < MONTH_NAMES.length; i++) {
					int name = MONTH_NAMES [i];
					int size = OS.GetLocaleInfo (OS.LOCALE_USER_DEFAULT, name, buffer, buffer.length ());
					if (size == 0) {
						buffer = new TCHAR (getCodePage (), size);
						OS.GetLocaleInfo (OS.LOCALE_USER_DEFAULT, name, buffer, buffer.length ());
					}
					rect.left = rect.top = rect.right = rect.bottom = 0;
					OS.DrawText (hDC, buffer, size, rect, flags);
					if (rect.right - rect.left > width) {
						width = rect.right - rect.left;
						widest = i;
					}
					height = Math.max(height, rect.bottom - rect.top);
				}
				systime.wMonth = (short) (widest + 1);

				/* Determine the widest/tallest date string in the widest month of the widest year. */
				int dwFlags = ((style & SWT.MEDIUM) != 0) ? OS.DATE_SHORTDATE : ((style & SWT.SHORT) != 0) ? OS.DATE_YEARMONTH : OS.DATE_LONGDATE;
				width = 0;
				for (short i = 1; i <= MAX_DAY; i++) {
					systime.wDay = i;
					int size = OS.GetDateFormat(OS.LOCALE_USER_DEFAULT, dwFlags, systime, null, buffer, buffer.length ());
					if (size == 0) {
						buffer = new TCHAR (getCodePage (), size);
						OS.GetDateFormat(OS.LOCALE_USER_DEFAULT, dwFlags, systime, null, buffer, buffer.length ());
					}
					rect.left = rect.top = rect.right = rect.bottom = 0;
					OS.DrawText (hDC, buffer, size, rect, flags);
					width = Math.max(width, rect.right - rect.left);
					height = Math.max(height, rect.bottom - rect.top);
					if ((style & SWT.SHORT) != 0) break;
				}
			} else if ((style & SWT.TIME) != 0) {
				/* Determine the widest/tallest hour string. This code allows for the possibility of ligatures. */
				int dwFlags = ((style & SWT.SHORT) != 0) ? OS.TIME_NOSECONDS : 0;
				short widest = 0;
				int max = is24HourTime () ? MAX_24HOUR : MAX_12HOUR;
				for (short i = 0; i < max; i++) {
					systime.wHour = i;
					int size = OS.GetTimeFormat(OS.LOCALE_USER_DEFAULT, dwFlags, systime, null, buffer, buffer.length ());
					if (size == 0) {
						buffer = new TCHAR (getCodePage (), size);
						OS.GetTimeFormat(OS.LOCALE_USER_DEFAULT, dwFlags, systime, null, buffer, buffer.length ());
					}
					rect.left = rect.top = rect.right = rect.bottom = 0;
					OS.DrawText (hDC, buffer, size, rect, flags);
					if (rect.right - rect.left > width) {
						width = rect.right - rect.left;
						widest = i;
					}
					height = Math.max(height, rect.bottom - rect.top);
				}
				systime.wHour = widest;

				/* Determine the widest/tallest minute and second string. */
				width = widest = 0;
				for (short i = 0; i < MAX_MINUTE; i++) {
					systime.wMinute = i;
					int size = OS.GetTimeFormat(OS.LOCALE_USER_DEFAULT, dwFlags, systime, null, buffer, buffer.length ());
					if (size == 0) {
						buffer = new TCHAR (getCodePage (), size);
						OS.GetTimeFormat(OS.LOCALE_USER_DEFAULT, dwFlags, systime, null, buffer, buffer.length ());
					}
					rect.left = rect.top = rect.right = rect.bottom = 0;
					OS.DrawText (hDC, buffer, size, rect, flags);
					if (rect.right - rect.left > width) {
						width = rect.right - rect.left;
						widest = i;
					}
					height = Math.max(height, rect.bottom - rect.top);
				}
				systime.wMinute = widest;
				systime.wSecond = widest;

				/* Determine the widest/tallest time string for the widest hour, widest minute, and if applicable, widest second. */
				int size = OS.GetTimeFormat(OS.LOCALE_USER_DEFAULT, dwFlags, systime, null, buffer, buffer.length ());
				if (size == 0) {
					buffer = new TCHAR (getCodePage (), size);
					OS.GetTimeFormat(OS.LOCALE_USER_DEFAULT, dwFlags, systime, null, buffer, buffer.length ());
				}
				rect.left = rect.top = rect.right = rect.bottom = 0;
				OS.DrawText (hDC, buffer, size, rect, flags);
				width = rect.right - rect.left;
				height = Math.max(height, rect.bottom - rect.top);
			}
			if (newFont != 0) OS.SelectObject (hDC, oldFont);
			OS.ReleaseDC (handle, hDC);
			int upDownWidth = OS.GetSystemMetrics (OS.SM_CXVSCROLL);
			int upDownHeight = OS.GetSystemMetrics (OS.SM_CYVSCROLL);
			if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) {
				// TODO: On Vista, can send DTM_GETDATETIMEPICKERINFO to ask the Edit control what its margins are
				upDownHeight += 7;
				if ((style & SWT.DROP_DOWN) != 0) upDownWidth += 16;
			}
			width += upDownWidth + MARGIN;
			height = Math.max (height, upDownHeight);
		}
	}
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
	state &= ~(CANVAS | THEME_BACKGROUND);
	
	if ((style & SWT.BORDER) == 0) {
		int bits = OS.GetWindowLong (handle, OS.GWL_EXSTYLE);
		bits &= ~(OS.WS_EX_CLIENTEDGE | OS.WS_EX_STATICEDGE);
		OS.SetWindowLong (handle, OS.GWL_EXSTYLE, bits);
	}
}

int defaultBackground () {
	return OS.GetSysColor (OS.COLOR_WINDOW);
}

String getComputeSizeString () {
	// TODO: Not currently used but might need for WinCE
	if ((style & SWT.DATE) != 0) {
		if ((style & SWT.SHORT) != 0) return getCustomShortDateFormat ();
		if ((style & SWT.MEDIUM) != 0) return getShortDateFormat ();
		if ((style & SWT.LONG) != 0) return getLongDateFormat ();
	}
	if ((style & SWT.TIME) != 0) {
		if ((style & SWT.SHORT) != 0) return getCustomShortTimeFormat ();
		return getTimeFormat ();
	}
	return "";
}

String getCustomShortDateFormat () {
	TCHAR tchar = new TCHAR (getCodePage (), 80);
	int size = OS.GetLocaleInfo (OS.LOCALE_USER_DEFAULT, OS.LOCALE_SYEARMONTH, tchar, 80);
	return size != 0 ? tchar.toString (0, size - 1) : "M/yyyy"; //$NON-NLS-1$
	
	//TODO: Not currently used, but may need for WinCE (or if numeric short date is required)
//	StringBuffer buffer = new StringBuffer (getShortDateFormat ());
//	int length = buffer.length ();
//	boolean inQuotes = false;
//	int start = 0, end = 0;
//	while (start < length) {
//		char ch = buffer.charAt (start);
//		if (ch == SINGLE_QUOTE) inQuotes = !inQuotes;
//		else if (ch == DAY_FORMAT_CONSTANT && !inQuotes) {
//			end = start + 1;
//			while (end < length && buffer.charAt (end) == DAY_FORMAT_CONSTANT) end++;
//			int ordering = getShortDateFormatOrdering ();
//			switch (ordering) {
//			case MONTH_DAY_YEAR:
//				// skip the following separator
//				while (end < length && buffer.charAt (end) != YEAR_FORMAT_CONSTANT) end++;
//				break;
//			case DAY_MONTH_YEAR:
//				// skip the following separator
//				while (end < length && buffer.charAt (end) != MONTH_FORMAT_CONSTANT) end++;
//				break;
//			case YEAR_MONTH_DAY:
//				// skip the preceding separator
//				while (start > 0 && buffer.charAt (start) != MONTH_FORMAT_CONSTANT) start--;
//				break;
//			}
//			break;
//		}
//		start++;
//	}
//	if (start < end) buffer.delete (start, end);
//	return buffer.toString ();
}

String getCustomShortTimeFormat () {
	StringBuffer buffer = new StringBuffer (getTimeFormat ());
	int length = buffer.length ();
	boolean inQuotes = false;
	int start = 0, end = 0;
	while (start < length) {
		char ch = buffer.charAt (start);
		if (ch == SINGLE_QUOTE) inQuotes = !inQuotes;
		else if (ch == SECONDS_FORMAT_CONSTANT && !inQuotes) {
			end = start + 1;
			while (end < length && buffer.charAt (end) == SECONDS_FORMAT_CONSTANT) end++;
			// skip the preceding separator
			while (start > 0 && buffer.charAt (start) != MINUTES_FORMAT_CONSTANT) start--;
			start++;
			break;
		}
		start++;
	}
	if (start < end) buffer.delete (start, end);
	return buffer.toString ();
}

String getLongDateFormat () {
	//TODO: Not currently used, but may need for WinCE
	TCHAR tchar = new TCHAR (getCodePage (), 80);
	int size = OS.GetLocaleInfo (OS.LOCALE_USER_DEFAULT, OS.LOCALE_SLONGDATE, tchar, 80);
	return size > 0 ? tchar.toString (0, size - 1) : "dddd, MMMM dd, yyyy"; //$NON-NLS-1$
}

String getShortDateFormat () {
	//TODO: Not currently used, but may need for WinCE
	TCHAR tchar = new TCHAR (getCodePage (), 80);
	//TODO: May need to OR with LOCALE_ICENTURY
	int size = OS.GetLocaleInfo (OS.LOCALE_USER_DEFAULT, OS.LOCALE_SSHORTDATE, tchar, 80);
	return size > 0 ? tchar.toString (0, size - 1) : "M/d/yyyy"; //$NON-NLS-1$
}

int getShortDateFormatOrdering () {
	//TODO: Not currently used, but may need for WinCE
	TCHAR tchar = new TCHAR (getCodePage (), 4);
	int size = OS.GetLocaleInfo (OS.LOCALE_USER_DEFAULT, OS.LOCALE_IDATE, tchar, 4);
	if (size > 0) {
		String number = tchar.toString (0, size - 1);
		return Integer.parseInt (number);
	}
	return 0;
}

String getTimeFormat () {
	TCHAR tchar = new TCHAR (getCodePage (), 80);
	int size = OS.GetLocaleInfo (OS.LOCALE_USER_DEFAULT, OS.LOCALE_STIMEFORMAT, tchar, 80);
	return size > 0 ? tchar.toString (0, size - 1) : "h:mm:ss tt"; //$NON-NLS-1$
}

boolean is24HourTime () {
	TCHAR tchar = new TCHAR (getCodePage (), 4);
	int size = OS.GetLocaleInfo (OS.LOCALE_USER_DEFAULT, OS.LOCALE_ITIME, tchar, 4);
	if (size > 0) {
		String number = tchar.toString (0, size - 1);
		return Integer.parseInt (number) != 0;
	}
	return true;
}

/**
 * Returns the receiver's date, or day of the month.
 * <p>
 * The first day of the month is 1, and the last day depends on the month and year.
 * </p>
 *
 * @return a positive integer beginning with 1
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getDay () {
	checkWidget ();
	SYSTEMTIME systime = new SYSTEMTIME ();
	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
	OS.SendMessage (handle, msg, 0, systime);
	return systime.wDay;
}

/**
 * Returns the receiver's hours.
 * <p>
 * Hours is an integer between 0 and 23.
 * </p>
 *
 * @return an integer between 0 and 23
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getHours () {
	checkWidget ();
	if ((style & SWT.CALENDAR) != 0) return time.wHour;
	SYSTEMTIME systime = new SYSTEMTIME ();
	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
	OS.SendMessage (handle, msg, 0, systime);
	return systime.wHour;
}

/**
 * Returns the receiver's minutes.
 * <p>
 * Minutes is an integer between 0 and 59.
 * </p>
 *
 * @return an integer between 0 and 59
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMinutes () {
	checkWidget ();
	if ((style & SWT.CALENDAR) != 0) return time.wMinute;
	SYSTEMTIME systime = new SYSTEMTIME ();
	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
	OS.SendMessage (handle, msg, 0, systime);
	return systime.wMinute;
}

/**
 * Returns the receiver's month.
 * <p>
 * The first month of the year is 0, and the last month is 11.
 * </p>
 *
 * @return an integer between 0 and 11
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getMonth () {
	checkWidget ();
	SYSTEMTIME systime = new SYSTEMTIME ();
	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
	OS.SendMessage (handle, msg, 0, systime);
	return systime.wMonth - 1;
}

String getNameText() {
	return (style & SWT.TIME) != 0 ? getHours() + ":" + getMinutes() + ":" + getSeconds()
			: (getMonth() + 1) + "/" + getDay() + "/" + getYear();
}

/**
 * Returns the receiver's seconds.
 * <p>
 * Seconds is an integer between 0 and 59.
 * </p>
 *
 * @return an integer between 0 and 59
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getSeconds () {
	checkWidget ();
	if ((style & SWT.CALENDAR) != 0) return time.wSecond;
	SYSTEMTIME systime = new SYSTEMTIME ();
	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
	OS.SendMessage (handle, msg, 0, systime);
	return systime.wSecond;
}

/**
 * Returns the receiver's year.
 * <p>
 * The first year is 1752 and the last year is 9999.
 * </p>
 *
 * @return an integer between 1752 and 9999
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getYear () {
	checkWidget ();
	SYSTEMTIME systime = new SYSTEMTIME ();
	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
	OS.SendMessage (handle, msg, 0, systime);
	return systime.wYear;
}

void releaseWidget () {
	super.releaseWidget ();
	lastSystemTime = null;
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
	eventTable.unhook (SWT.DefaultSelection, listener);	
}

/**
 * Sets the receiver's year, month, and day in a single operation.
 * <p>
 * This is the recommended way to set the date, because setting the year,
 * month, and day separately may result in invalid intermediate dates.
 * </p>
 *
 * @param year an integer between 1752 and 9999
 * @param month an integer between 0 and 11
 * @param day a positive integer beginning with 1
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setDate (int year, int month, int day) {
	checkWidget ();
	SYSTEMTIME systime = new SYSTEMTIME ();
	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
	OS.SendMessage (handle, msg, 0, systime);
	msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_SETCURSEL : OS.DTM_SETSYSTEMTIME;
	systime.wYear = (short)year;
	systime.wMonth = (short)(month + 1);
	systime.wDay = (short)day;
	OS.SendMessage (handle, msg, 0, systime);
	lastSystemTime = null;
}

/**
 * Sets the receiver's date, or day of the month, to the specified day.
 * <p>
 * The first day of the month is 1, and the last day depends on the month and year.
 * If the specified day is not valid for the receiver's month and year, then it is ignored. 
 * </p>
 *
 * @param day a positive integer beginning with 1
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setDate
 */
public void setDay (int day) {
	checkWidget ();
	SYSTEMTIME systime = new SYSTEMTIME ();
	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
	OS.SendMessage (handle, msg, 0, systime);
	msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_SETCURSEL : OS.DTM_SETSYSTEMTIME;
	systime.wDay = (short)day;
	OS.SendMessage (handle, msg, 0, systime);
	lastSystemTime = null;
}

/**
 * Sets the receiver's hours.
 * <p>
 * Hours is an integer between 0 and 23.
 * </p>
 *
 * @param hours an integer between 0 and 23
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setHours (int hours) {
	checkWidget ();
	SYSTEMTIME systime = new SYSTEMTIME ();
	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
	OS.SendMessage (handle, msg, 0, systime);
	msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_SETCURSEL : OS.DTM_SETSYSTEMTIME;
	systime.wHour = (short)hours;
	OS.SendMessage (handle, msg, 0, systime);
	if ((style & SWT.CALENDAR) != 0 && hours >= 0 && hours <= 23) time.wHour = (short)hours;
}

/**
 * Sets the receiver's minutes.
 * <p>
 * Minutes is an integer between 0 and 59.
 * </p>
 *
 * @param minutes an integer between 0 and 59
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMinutes (int minutes) {
	checkWidget ();
	SYSTEMTIME systime = new SYSTEMTIME ();
	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
	OS.SendMessage (handle, msg, 0, systime);
	msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_SETCURSEL : OS.DTM_SETSYSTEMTIME;
	systime.wMinute = (short)minutes;
	OS.SendMessage (handle, msg, 0, systime);
	if ((style & SWT.CALENDAR) != 0 && minutes >= 0 && minutes <= 59) time.wMinute = (short)minutes;
}

/**
 * Sets the receiver's month.
 * <p>
 * The first month of the year is 0, and the last month is 11.
 * If the specified month is not valid for the receiver's day and year, then it is ignored. 
 * </p>
 *
 * @param month an integer between 0 and 11
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setDate
 */
public void setMonth (int month) {
	checkWidget ();
	SYSTEMTIME systime = new SYSTEMTIME ();
	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
	OS.SendMessage (handle, msg, 0, systime);
	msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_SETCURSEL : OS.DTM_SETSYSTEMTIME;
	systime.wMonth = (short)(month + 1);
	OS.SendMessage (handle, msg, 0, systime);
	lastSystemTime = null;
}

/**
 * Sets the receiver's seconds.
 * <p>
 * Seconds is an integer between 0 and 59.
 * </p>
 *
 * @param seconds an integer between 0 and 59
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSeconds (int seconds) {
	checkWidget ();
	SYSTEMTIME systime = new SYSTEMTIME ();
	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
	OS.SendMessage (handle, msg, 0, systime);
	msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_SETCURSEL : OS.DTM_SETSYSTEMTIME;
	systime.wSecond = (short)seconds;
	OS.SendMessage (handle, msg, 0, systime);
	if ((style & SWT.CALENDAR) != 0 && seconds >= 0 && seconds <= 59) time.wSecond = (short)seconds;
}

/**
 * Sets the receiver's hours, minutes, and seconds in a single operation.
 *
 * @param hours an integer between 0 and 23
 * @param minutes an integer between 0 and 59
 * @param seconds an integer between 0 and 59
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setTime (int hours, int minutes, int seconds) {
	checkWidget ();
	SYSTEMTIME systime = new SYSTEMTIME ();
	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
	OS.SendMessage (handle, msg, 0, systime);
	msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_SETCURSEL : OS.DTM_SETSYSTEMTIME;
	systime.wHour = (short)hours;
	systime.wMinute = (short)minutes;
	systime.wSecond = (short)seconds;
	OS.SendMessage (handle, msg, 0, systime);
	if ((style & SWT.CALENDAR) != 0
			&& hours >= 0 && hours <= 23
			&& minutes >= 0 && minutes <= 59
			&& seconds >= 0 && seconds <= 59) {
		time.wHour = (short)hours;
		time.wMinute = (short)minutes;
		time.wSecond = (short)seconds;
	}
}

/**
 * Sets the receiver's year.
 * <p>
 * The first year is 1752 and the last year is 9999.
 * If the specified year is not valid for the receiver's day and month, then it is ignored. 
 * </p>
 *
 * @param year an integer between 1752 and 9999
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #setDate
 */
public void setYear (int year) {
	checkWidget ();
	SYSTEMTIME systime = new SYSTEMTIME ();
	int msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_GETCURSEL : OS.DTM_GETSYSTEMTIME;
	OS.SendMessage (handle, msg, 0, systime);
	msg = (style & SWT.CALENDAR) != 0 ? OS.MCM_SETCURSEL : OS.DTM_SETSYSTEMTIME;
	systime.wYear = (short)year;
	OS.SendMessage (handle, msg, 0, systime);
	lastSystemTime = null;
}

int widgetStyle () {
	int bits = super.widgetStyle () | OS.WS_TABSTOP;
	if ((style & SWT.CALENDAR) != 0) return bits | OS.MCS_NOTODAY;
	/*
	* Bug in Windows: When WS_CLIPCHILDREN is set in a
	* Date and Time Picker, the widget draws on top of
	* the updown control. The fix is to clear the bits.
	*/
	bits &= ~OS.WS_CLIPCHILDREN;
	if ((style & SWT.TIME) != 0) bits |= OS.DTS_TIMEFORMAT;
	if ((style & SWT.DATE) != 0) {
		bits |= ((style & SWT.MEDIUM) != 0 ? OS.DTS_SHORTDATECENTURYFORMAT : OS.DTS_LONGDATEFORMAT);
		if ((style & SWT.DROP_DOWN) == 0) bits |= OS.DTS_UPDOWN;
	}
	return bits;
}

TCHAR windowClass () {
	return (style & SWT.CALENDAR) != 0 ? CalendarClass : DateTimeClass;
}

int /*long*/ windowProc () {
	return (style & SWT.CALENDAR) != 0 ? CalendarProc : DateTimeProc;
}

LRESULT wmNotifyChild (NMHDR hdr, int /*long*/ wParam, int /*long*/ lParam) {
	switch (hdr.code) {
		case OS.DTN_CLOSEUP: {
			/*
			* Feature in Windows.  When the user selects the drop-down button,
			* the DateTimePicker runs a modal loop and consumes WM_LBUTTONUP.
			* This is done without adding a mouse capture.  Since WM_LBUTTONUP
			* is not delivered, the normal mechanism where a mouse capture is
			* added on mouse down and removed when the mouse is released
			* is broken, leaving an unwanted capture.  The fix is to avoid
			* setting capture on mouse down right after WM_LBUTTONUP is consumed.
			*/
			display.captureChanged = true;
			break;
		}
		case OS.MCN_SELCHANGE: {
			if (ignoreSelection) break;
			SYSTEMTIME systime = new SYSTEMTIME ();
			OS.SendMessage (handle, OS.MCM_GETCURSEL, 0, systime);
			postEvent (SWT.Selection);
			break;
		}
		case OS.DTN_DATETIMECHANGE: {
			SYSTEMTIME systime = new SYSTEMTIME ();
			OS.SendMessage (handle, OS.DTM_GETSYSTEMTIME, 0, systime);
			if (lastSystemTime == null || systime.wDay != lastSystemTime.wDay || systime.wMonth != lastSystemTime.wMonth || systime.wYear != lastSystemTime.wYear) {
				postEvent (SWT.Selection);
				if ((style & SWT.TIME) == 0) lastSystemTime = systime;
			}
			break;
		}
	}
	return super.wmNotifyChild (hdr, wParam, lParam);
}

LRESULT WM_CHAR (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_CHAR (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows.  For some reason, when the
	* user presses tab, return or escape, Windows beeps.
	* The fix is to look for these keys and not call
	* the window proc.
	*/
	switch ((int)/*64*/wParam) {
		case SWT.CR:
			postEvent (SWT.DefaultSelection);
			// FALL THROUGH
		case SWT.TAB:
		case SWT.ESC: return LRESULT.ZERO;
	}
	return result;
}

LRESULT WM_LBUTTONDBLCLK (int /*long*/ wParam, int /*long*/ lParam) {	
	LRESULT result = super.WM_LBUTTONDBLCLK (wParam, lParam);
	if (isDisposed ()) return LRESULT.ZERO;
	if ((style & SWT.CALENDAR) != 0) {
		MCHITTESTINFO pMCHitTest = new MCHITTESTINFO ();
		pMCHitTest.cbSize = MCHITTESTINFO.sizeof;
		POINT pt = new POINT ();
		pt.x = OS.GET_X_LPARAM (lParam);
		pt.y = OS.GET_Y_LPARAM (lParam);
		pMCHitTest.pt = pt;
		int /*long*/ code = OS.SendMessage (handle, OS.MCM_HITTEST, 0, pMCHitTest);
		if ((code & OS.MCHT_CALENDARDATE) == OS.MCHT_CALENDARDATE) doubleClick = true;
	}
	return result;
}

LRESULT WM_LBUTTONDOWN (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_LBUTTONDOWN (wParam, lParam);
	if (result == LRESULT.ZERO) return result;
	doubleClick = false;
	/*
	* Feature in Windows. For some reason, the calendar control
	* does not take focus on WM_LBUTTONDOWN.  The fix is to
	* explicitly set focus.
	*/
	if ((style & SWT.CALENDAR) != 0) {
		if ((style & SWT.NO_FOCUS) == 0) OS.SetFocus (handle);
	}
	return result;
}

LRESULT WM_LBUTTONUP (int /*long*/ wParam, int /*long*/ lParam) {	
	LRESULT result = super.WM_LBUTTONUP (wParam, lParam);
	if (isDisposed ()) return LRESULT.ZERO;
	if (doubleClick) postEvent (SWT.DefaultSelection);
	doubleClick = false;
	return result;
}

LRESULT WM_TIMER (int /*long*/ wParam, int /*long*/ lParam) {
	LRESULT result = super.WM_TIMER (wParam, lParam);
	if (result != null) return result;
	/*
	* Feature in Windows. For some reason, Windows sends WM_NOTIFY with
	* MCN_SELCHANGE at regular intervals. This is unexpected. The fix is
	* to ignore MCN_SELCHANGE during WM_TIMER.
	*/
	ignoreSelection = true;
	int /*long*/ code = callWindowProc(handle, OS.WM_TIMER, wParam, lParam);
	ignoreSelection = false;
	return code == 0 ? LRESULT.ZERO : new LRESULT(code);
}
}
