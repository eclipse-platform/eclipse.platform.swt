/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.gtk.*;

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
	int day, month, year, hours, minutes, seconds;
	
	/* Emulated DATE and TIME fields */
	Calendar calendar;
	DateFormatSymbols formatSymbols;
	Button down, up;
	String format;
	Point[] fieldIndices;
	int[] fieldNames;
	int fieldCount, currentField = 0, characterCount = 0;
	boolean ignoreVerify = false;
	String dateTimeString;
	boolean firstTime = true;
	/* DROP_DOWN calendar fields for DATE */
	Color fg, bg;
	boolean hasFocus, monthChanged, calendarDisplayed;
	int savedYear, savedMonth, savedDay;
	Shell popupShell;
	DateTime popupCalendar;
	Listener popupListener, popupFilter, clickListener;
	
	static final String DEFAULT_SHORT_DATE_FORMAT = "MM/YYYY";
	static final String DEFAULT_MEDIUM_DATE_FORMAT = "MM/DD/YYYY";
	static final String DEFAULT_LONG_DATE_FORMAT = "MM/DD/YYYY";
	static final String DEFAULT_SHORT_TIME_FORMAT = "HH:MM AM";
	static final String DEFAULT_MEDIUM_TIME_FORMAT = "HH:MM:SS AM";
	static final String DEFAULT_LONG_TIME_FORMAT = "HH:MM:SS AM";
	static final int MIN_YEAR = 1752; // Gregorian switchover in North America: September 19, 1752
	static final int MAX_YEAR = 9999;
	static final int SPACE_FOR_CURSOR = 1;

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
	if ((this.style & SWT.CALENDAR) == 0) {
		/* SWT.DATE and SWT.TIME */
		createText((this.style & SWT.DROP_DOWN) != 0);
	}
	initAccessible ();
}

void createText(boolean dropDown) {
	calendar = Calendar.getInstance();
	formatSymbols = new DateFormatSymbols();
	if ((style & SWT.DATE) != 0) {
		setFormat((style & SWT.SHORT) != 0 ? DEFAULT_SHORT_DATE_FORMAT : (style & SWT.LONG) != 0 ? DEFAULT_LONG_DATE_FORMAT : DEFAULT_MEDIUM_DATE_FORMAT);
	} else { /* SWT.TIME */
		setFormat((style & SWT.SHORT) != 0 ? DEFAULT_SHORT_TIME_FORMAT : (style & SWT.LONG) != 0 ? DEFAULT_LONG_TIME_FORMAT : DEFAULT_MEDIUM_TIME_FORMAT);
	}
	dateTimeString = getFormattedString(style);
	OS.gtk_widget_realize(handle);
	if ((style & SWT.DATE) != 0 && dropDown) { /* SWT.DROP_DOWN */
		addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				onResize(event);
			}
		});
		createDropDownButton();
		createPopupShell(-1, -1, -1);
	} 
	if (dateTimeString != null) {
		setText(dateTimeString);
	}
}

void createDropDownButton() {
	down = new Button(this, SWT.ARROW  | SWT.DOWN);
	gtk_widget_set_can_focus (down.handle, false);
	down.addListener(SWT.Selection, new Listener() {
		public void handleEvent(Event event) {
			boolean dropped = isDropped();
			popupCalendar.calendarDisplayed = !dropped;
			setFocus();
			dropDownCalendar (!dropped);
		}
	});

	popupListener = new Listener () {
		public void handleEvent (Event event) {
			if (event.widget == popupShell) {
				popupShellEvent (event);
				return;
			}
			if (event.widget == popupCalendar) {
				popupCalendarEvent (event);
				return;
			}
			if (event.widget == DateTime.this) {
				onDispose (event);
				return;
			}
			if (event.widget == getShell ()) {
				getDisplay().asyncExec(new Runnable() {
					public void run() {
						if (isDisposed()) return;
						handleFocus (SWT.FocusOut);
					}
				});
			}
		}
	};
	popupFilter = new Listener() {
		public void handleEvent(Event event) {
			Shell shell = ((Control)event.widget).getShell ();
			if (shell == DateTime.this.getShell ()) {
				handleFocus (SWT.FocusOut);
			}
		}
	};
}

void createPopupShell(int year, int month, int day) {	
	popupShell = new Shell (getShell(), SWT.NO_TRIM | SWT.ON_TOP);
	popupCalendar = new DateTime (popupShell, SWT.CALENDAR);
	if (font != null) popupCalendar.setFont (font);
	if (fg != null) popupCalendar.setForeground (fg);
	if (bg != null) popupCalendar.setBackground (bg);
	
	clickListener = new Listener() {
		public void handleEvent(Event event) {
			if (event.widget instanceof Control) {
				Control c = (Control)event.widget;
				if (c != down && c.getShell() != popupShell)
					dropDownCalendar(false);
			}
		}
	};

	int [] listeners = {SWT.Close, SWT.MouseUp, SWT.Paint};
	for (int i=0; i < listeners.length; i++) {
		popupShell.addListener (listeners [i], popupListener);
	}
	listeners = new int [] {SWT.MouseDown, SWT.MouseUp, SWT.Selection, SWT.Traverse, SWT.KeyDown, SWT.KeyUp, SWT.FocusIn, SWT.FocusOut, SWT.Dispose};
	for (int i=0; i < listeners.length; i++) {
		popupCalendar.addListener (listeners [i], popupListener);
	}
	addListener(SWT.Dispose, popupListener);
	if (year != -1) popupCalendar.setDate(year, month, day);
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
	if ((style & SWT.DATE) == 0) style &=~ SWT.DROP_DOWN;
	return checkBits (style, SWT.MEDIUM, SWT.SHORT, SWT.LONG, 0, 0, 0);
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

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
		if ((style & SWT.CALENDAR) != 0) {
			Point size = computeNativeSize(handle, wHint, hHint, changed);
			width = size.x;
			height = size.y;
		} else { /* SWT.DROP_DOWN */
			if ((style & SWT.DROP_DOWN) != 0) {
				Point textSize = computeNativeSize(handle, wHint, hHint, changed);
				Rectangle trim = computeTrim(0,0, textSize.x,textSize.y);
				Point buttonSize = down.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
				width = trim.width + buttonSize.x;
				height = Math.max(trim.height, buttonSize.y);
			} else { /* SWT.DATE and SWT.TIME */
				Point size = computeNativeSize(handle, wHint, hHint, changed);
				width = size.x;
				height = size.y;
			}
		}
	}
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int borderWidth = getBorderWidth ();
	return new Point (width + 2*borderWidth, height+ 2*borderWidth);
}

public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	Rectangle trim = super.computeTrim (x, y, width, height);
	int xborder = 0, yborder = 0;
		if (OS.GTK3) {
			GtkBorder tmp = new GtkBorder();
			long /*int*/ context = OS.gtk_widget_get_style_context (handle);
			OS.gtk_style_context_get_padding (context, OS.GTK_STATE_FLAG_NORMAL, tmp);
			trim.x -= tmp.left;
			trim.y -= tmp.top;
			trim.width += tmp.left + tmp.right;
			trim.height += tmp.top + tmp.bottom;
			if ((style & SWT.BORDER) != 0) {
				OS.gtk_style_context_get_border (context, OS.GTK_STATE_FLAG_NORMAL, tmp);
				trim.x -= tmp.left;
				trim.y -= tmp.top;
				trim.width += tmp.left + tmp.right;
				trim.height += tmp.top + tmp.bottom;
			}
			GdkRectangle icon_area = new GdkRectangle();
			OS.gtk_entry_get_icon_area(handle, OS.GTK_ENTRY_ICON_PRIMARY, icon_area);
			trim.x -= icon_area.width;
			trim.width += icon_area.width;
			OS.gtk_entry_get_icon_area(handle, OS.GTK_ENTRY_ICON_SECONDARY, icon_area);
			trim.width += icon_area.width;
		}else {
			if ((style & SWT.BORDER) != 0) {
				Point thickness = getThickness (handle);
				xborder += thickness.x;
				yborder += thickness.y;
			}
			GtkBorder innerBorder = Display.getEntryInnerBorder (handle);
			trim.x -= innerBorder.left;
			trim.y -= innerBorder.top;
			trim.width += innerBorder.left + innerBorder.right;
			trim.height += innerBorder.top + innerBorder.bottom;
		}
		trim.x -= xborder;
		trim.y -= yborder;
		trim.width += 2 * xborder;
		trim.height += 2 * yborder;
		trim.width += SPACE_FOR_CURSOR;
		return new Rectangle (trim.x, trim.y, trim.width, trim.height);
}

void createHandle (int index) {
	if ((style & SWT.CALENDAR) != 0) {
		state |= HANDLE;
		fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
		if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
		gtk_widget_set_has_window (fixedHandle, true);
		handle = OS.gtk_calendar_new ();
		if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		OS.gtk_container_add (fixedHandle, handle);
		OS.gtk_calendar_set_display_options(handle, OS.GTK_CALENDAR_SHOW_HEADING | OS.GTK_CALENDAR_SHOW_DAY_NAMES);
	} else {
		fixedHandle = OS.g_object_new (display.gtk_fixed_get_type (), 0);
		if (fixedHandle == 0) error (SWT.ERROR_NO_HANDLES);
		gtk_widget_set_has_window (fixedHandle, true);
		if ((style & SWT.DROP_DOWN) != 0 && (style & SWT.DATE) != 0) {
			handle = OS.gtk_entry_new();
			OS.gtk_container_add(fixedHandle, handle);
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
		} else {
			long /*int*/ adjusment = OS.gtk_adjustment_new(0, -9999, 9999, 1, 0, 0);
			handle = OS.gtk_spin_button_new(adjusment, 1, 0);
			if (handle == 0) error (SWT.ERROR_NO_HANDLES);
			OS.gtk_spin_button_set_numeric (handle, false);
			OS.gtk_container_add(fixedHandle, handle);
			OS.gtk_spin_button_set_wrap (handle, (style & SWT.WRAP) != 0);
		}
		OS.gtk_editable_set_editable (handle, (style & SWT.READ_ONLY) == 0);
		OS.gtk_entry_set_has_frame (handle, (style & SWT.BORDER) != 0);
		
	}
}

boolean checkSubwindow () {
	return false;
}

void createWidget (int index) {
	super.createWidget (index);
	if ((style & SWT.CALENDAR) != 0) {
		getDate();
	}
}

void onDispose (Event event) {
	if (popupShell != null && !popupShell.isDisposed ()) {
		popupCalendar.removeListener (SWT.Dispose, popupListener);
		popupShell.dispose ();
	}
	Shell shell = getShell ();
	shell.removeListener (SWT.Deactivate, popupListener);
	Display display = getDisplay ();
	display.removeFilter (SWT.FocusIn, popupFilter);
	popupShell = null;  
	popupCalendar = null;  
	down = null;
	dateTimeString = null;
}

void dropDownCalendar(boolean drop) {
	if (drop == isDropped ()) return;
	if (!drop) {
		popupShell.setVisible (false);
		OS.gtk_calendar_clear_marks(popupCalendar.handle);
		display.removeFilter(SWT.MouseDown, clickListener);
		return;
	}
	savedYear = getYear ();
	savedMonth = getMonth ();
	savedDay = getDay ();
	if (getShell() != popupShell.getParent ()) {
		int year = popupCalendar.getYear ();
		int month = popupCalendar.getMonth ();
		int day = popupCalendar.getDay ();
		popupCalendar.removeListener (SWT.Dispose, popupListener);
		popupShell.dispose();
		popupShell = null;
		popupCalendar = null;
		createPopupShell (year, month, day);
	}
	Point dateBounds = getSize ();
	Point calendarSize = popupCalendar.computeSize (SWT.DEFAULT, SWT.DEFAULT, false);
	popupCalendar.setBounds (1, 1, Math.max (dateBounds.x - 2, calendarSize.x), calendarSize.y);

	popupCalendar.setDate(savedYear, savedMonth, savedDay);
	if (savedYear == Calendar.getInstance().get(Calendar.YEAR) && savedMonth == Calendar.getInstance().get(Calendar.MONTH))
		OS.gtk_calendar_mark_day(popupCalendar.handle, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
	Display display = getDisplay ();
	Rectangle parentRect = display.map (getParent (), null, getBounds ());
	Rectangle displayRect = getMonitor ().getClientArea ();
	int width = Math.max (dateBounds.x, calendarSize.x + 2);
	int height = calendarSize.y + 2;
	int x = parentRect.x;
	int y = parentRect.y + dateBounds.y;
	if (y + height > displayRect.y + displayRect.height) y = parentRect.y - height;
	if (x + width > displayRect.x + displayRect.width) x = displayRect.x + displayRect.width - calendarSize.x;
	popupShell.setBounds (x, y, width, height);
	popupShell.setVisible (true);
	if (isFocusControl()) {
		popupCalendar.setFocus ();
	}
	display.addFilter(SWT.MouseDown, clickListener);
}
	
long /*int*/ focusHandle () {
	if (handle != 0) return handle;
	return super.focusHandle ();
}

String formattedStringValue(int fieldName, int value, boolean adjust) {
	if (fieldName == Calendar.AM_PM) {
		String[] ampm = formatSymbols.getAmPmStrings();
		return ampm[value];
	}
	if (adjust) {
		if (fieldName == Calendar.HOUR && value == 0) {
			return String.valueOf(12);
		}
		if (fieldName == Calendar.MONTH) {
			return String.valueOf(value + 1);
		}
	}
	return String.valueOf(value);
}

GdkColor getBackgroundColor () {
	if ((style & SWT.CALENDAR) != 0) {
		return getBaseColor ();
	} else {
		return super.getBackgroundColor ();
	}
}

String getComputeSizeString(int style) {
	if ((style & SWT.DATE) != 0) {
		return (style & SWT.SHORT) != 0 ? DEFAULT_SHORT_DATE_FORMAT : (style & SWT.LONG) != 0 ? DEFAULT_LONG_DATE_FORMAT : DEFAULT_MEDIUM_DATE_FORMAT;
	}
	// SWT.TIME
	return (style & SWT.SHORT) != 0 ? DEFAULT_SHORT_TIME_FORMAT : (style & SWT.LONG) != 0 ? DEFAULT_LONG_TIME_FORMAT : DEFAULT_MEDIUM_TIME_FORMAT;
}

int getFieldIndex(int fieldName) {
	for (int i = 0; i < fieldCount; i++) {
		if (fieldNames[i] == fieldName) {
			return i;
		}
	}
	return -1;
}

String getFormattedString(int style) {
	if ((style & SWT.TIME) != 0) {
		String[] ampm = formatSymbols.getAmPmStrings();
		int h = calendar.get(Calendar.HOUR); if (h == 0) h = 12;
		int m = calendar.get(Calendar.MINUTE);
		int s = calendar.get(Calendar.SECOND);
		int a = calendar.get(Calendar.AM_PM);
		if ((style & SWT.SHORT) != 0) return "" + (h < 10 ? " " : "") + h + ":" + (m < 10 ? "0" : "") + m + " " + ampm[a];
		return "" + (h < 10 ? " " : "") + h + ":" + (m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s + " " + ampm[a];
	}
	/* SWT.DATE */
	int y = calendar.get(Calendar.YEAR);
	int m = calendar.get(Calendar.MONTH) + 1;
	int d = calendar.get(Calendar.DAY_OF_MONTH);
	if ((style & SWT.SHORT) != 0) return "" + (m < 10 ? " " : "") + m + "/" + y;
	return "" + (m < 10 ? " " : "") + m + "/" + (d < 10 ? " " : "") + d + "/" + y;
}

void getDate() {
	int [] y = new int [1];
	int [] m = new int [1];
	int [] d = new int [1];
	OS.gtk_calendar_get_date(handle, y, m, d);
	year = y[0];
	month = m[0];
	day = d[0];
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
	if ((style & SWT.CALENDAR) != 0) {
		getDate();
		return day;
	} else {
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
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
	if ((style & SWT.CALENDAR) != 0) {
		return hours;
	} else {
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
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
	if ((style & SWT.CALENDAR) != 0) {
		return minutes;
	} else {
		return calendar.get(Calendar.MINUTE);
	}
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
	if ((style & SWT.CALENDAR) != 0) {
		getDate();
		return month;
	} else {
		return calendar.get(Calendar.MONTH);
	}
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
	if ((style & SWT.CALENDAR) != 0) {
		return seconds;
	} else {
		return calendar.get(Calendar.SECOND);
	}
}

/*
 * Returns a textual representation of the receiver,
 * intended for speaking the text aloud.
 */
String getSpokenText () {
	// TODO: needs more work for locale
	StringBuffer result = new StringBuffer ();
	if ((style & SWT.TIME) != 0) {
		int h = calendar.get(Calendar.HOUR); if (h == 0) h = 12;
		result.append(h);
		int m = calendar.get(Calendar.MINUTE);
		result.append(":" + (m < 10 ? "0" : "") + m);
		if ((style & SWT.SHORT) == 0) {
			int s = calendar.get(Calendar.SECOND);
			result.append(":" + (s < 10 ? "0" : "") + s);
		}
		result.append(" " + formatSymbols.getAmPmStrings()[calendar.get(Calendar.AM_PM)]);
	} else {
		/* SWT.DATE or SWT.CALENDAR */
		Calendar cal = calendar;
	    if ((style & SWT.CALENDAR) != 0) {
	        formatSymbols = new DateFormatSymbols();
	        cal = Calendar.getInstance();
	        getDate();
	        cal.set(year, month, day);
	    }
	    if ((style & SWT.SHORT) == 0) {
	    	result.append(formatSymbols.getWeekdays()[cal.get(Calendar.DAY_OF_WEEK)] + ", ");
	    }
	    result.append(formatSymbols.getMonths()[cal.get(Calendar.MONTH)] + " ");
	    if ((style & SWT.SHORT) == 0) {
	        result.append(cal.get(Calendar.DAY_OF_MONTH) + ", ");
	    }
	    result.append(cal.get(Calendar.YEAR));
	}
	return result.toString();
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
	if ((style & SWT.CALENDAR) != 0) {
		getDate();
		return year;
	} else {
		return calendar.get(Calendar.YEAR);
	}
}

long /*int*/ gtk_day_selected (long /*int*/ widget) {
	sendSelectionEvent ();
	return 0;
}

long /*int*/ gtk_day_selected_double_click (long /*int*/ widget) {
	sendSelectionEvent (SWT.DefaultSelection);
	return 0;
}

long /*int*/ gtk_month_changed (long /*int*/ widget) {
	/*
	* Feature in GTK. "month-changed" signal is emitted when the
	* calendar is displayed though the day/month is not changed.
	* The popup has to remain when the month/year is changed
	* through the arrow keys, and the popup has to be called-off
	* only when the day is selected. The fix is to detect the
	* difference between the user changing the month/year, or
	* choosing the day.
	*/
	if (calendarDisplayed) calendarDisplayed = false;
	else monthChanged = true;
	sendSelectionEvent ();
	return 0;
}

void hookEvents () {
	super.hookEvents();
	if ((style & SWT.CALENDAR) != 0) {
		OS.g_signal_connect_closure (handle, OS.day_selected, display.closures [DAY_SELECTED], false);
		OS.g_signal_connect_closure (handle, OS.day_selected_double_click, display.closures [DAY_SELECTED_DOUBLE_CLICK], false);
		OS.g_signal_connect_closure (handle, OS.month_changed, display.closures [MONTH_CHANGED], false);
	} else { // FOR SPINNER
		int eventMask =	OS.GDK_POINTER_MOTION_MASK | OS.GDK_BUTTON_PRESS_MASK | OS.GDK_BUTTON_RELEASE_MASK;
		OS.gtk_widget_add_events (handle, eventMask);
		if ((style & SWT.DROP_DOWN) == 0 ) { 
			OS.g_signal_connect_closure (handle, OS.output, display.closures [OUTPUT], true);
			OS.g_signal_connect_closure (handle, OS.focus_in_event, display.closures [FOCUS_IN_EVENT], true);
		}
		if (OS.G_OBJECT_TYPE (handle) == OS.GTK_TYPE_MENU ()) {
			OS.g_signal_connect_closure(handle, OS.selection_done, display.closures[SELECTION_DONE], true);
		}
	}
}

void incrementField(int amount) {
	int fieldName = fieldNames[currentField];
	int value = calendar.get(fieldName);
	if (fieldName == Calendar.HOUR) {
		int max = calendar.getMaximum(Calendar.HOUR);
		int min = calendar.getMinimum(Calendar.HOUR);
		if ((value == max && amount == 1) || (value == min && amount == -1)) {
			int temp = currentField;
			currentField = getFieldIndex(Calendar.AM_PM);
			setTextField(Calendar.AM_PM, (calendar.get(Calendar.AM_PM) + 1) % 2, true, true);
			currentField = temp;
		}
	}
	setTextField(fieldName, value + amount, true, true);
}

boolean isDropped () {
	return popupShell.getVisible ();
}

void initAccessible() {
	Accessible accessible = getAccessible ();
	accessible.addAccessibleListener (new AccessibleAdapter () {
		public void getName (AccessibleEvent e) {
			e.result = getSpokenText ();
		}

		public void getHelp(AccessibleEvent e) {
			e.result = getToolTipText ();
		}
	});

	accessible.addAccessibleControlListener (new AccessibleControlAdapter () {
		public void getChildAtPoint (AccessibleControlEvent e) {
			e.childID = ACC.CHILDID_SELF;
		}

		public void getLocation (AccessibleControlEvent e) {
			Rectangle rect = display.map (getParent (), null, getBounds ());
			e.x = rect.x;
			e.y = rect.y;
			e.width = rect.width;
			e.height = rect.height;
		}

		public void getChildCount (AccessibleControlEvent e) {
			e.detail = 0;
		}

		public void getRole (AccessibleControlEvent e) {
			e.detail = ((style & SWT.CALENDAR) != 0) ? ACC.ROLE_LABEL : ACC.ROLE_TEXT;
		}

		public void getState (AccessibleControlEvent e) {
			e.detail = ACC.STATE_FOCUSABLE;
			if (hasFocus ()) e.detail |= ACC.STATE_FOCUSED;
		}

		public void getSelection (AccessibleControlEvent e) {
			if (hasFocus ()) e.childID = ACC.CHILDID_SELF;
		}

		public void getFocus (AccessibleControlEvent e) {
			if (hasFocus ()) e.childID = ACC.CHILDID_SELF;
		}
	});
}

boolean isValidTime(int fieldName, int value) {
	Calendar validCalendar;
	if ((style & SWT.CALENDAR) != 0) {
		validCalendar = Calendar.getInstance();
	} else {
		validCalendar = calendar;
	}
	int min = validCalendar.getActualMinimum(fieldName);
	int max = validCalendar.getActualMaximum(fieldName);
	return value >= min && value <= max;
}

boolean isValidDate(int year, int month, int day) {
	if (year < MIN_YEAR || year > MAX_YEAR) return false;
	Calendar valid = Calendar.getInstance();
	valid.set(year, month, day);
	return valid.get(Calendar.YEAR) == year
		&& valid.get(Calendar.MONTH) == month
		&& valid.get(Calendar.DAY_OF_MONTH) == day;
}

void popupCalendarEvent (Event event) {
	switch (event.type) {
		case SWT.Dispose:
			if (popupShell != null && !popupShell.isDisposed () && !isDisposed() && getShell () != popupShell.getParent ()) {
				int year = popupCalendar.getYear ();
				int month = popupCalendar.getMonth ();
				int day = popupCalendar.getDay ();
				popupShell = null;
				popupCalendar = null;
				createPopupShell (year, month, day);
			}
			break;
		case SWT.FocusIn: {
			handleFocus (SWT.FocusIn);
			break;
		}
		case SWT.FocusOut: {
			Point point = down.toControl(getDisplay().getCursorLocation());
			Point size = down.getSize();
			Rectangle rect = new Rectangle(0, 0, size.x, size.y);
			if (rect.contains(point)) {
				boolean popupShellActivated = getDisplay().getActiveShell() == getShell();
				if (!popupShellActivated) dropDownCalendar(false);
				break;
			}
			dropDownCalendar(false);
			break;
		}
		case SWT.MouseUp: {
			if (event.button != 1) return;
			/*
			* The drop-down should stay visible when the year/month 
			* is changed.
			*/
			if (popupCalendar.monthChanged) {
				popupCalendar.monthChanged = false;
				OS.gtk_calendar_clear_marks(popupCalendar.handle);
			} else {
				dropDownCalendar (false);
			}
			break;
		}
		case SWT.Selection: {
			int year = popupCalendar.getYear ();
			int month = popupCalendar.getMonth ();
			int day = popupCalendar.getDay ();
			setDate(year, month, day);
			Event e = new Event ();
			e.time = event.time;
			e.stateMask = event.stateMask;
			e.doit = event.doit;
			notifyListeners (SWT.Selection, e);
			event.doit = e.doit;
			break;
		}
		case SWT.Traverse: {
			switch (event.detail) {
				case SWT.TRAVERSE_RETURN:
				case SWT.TRAVERSE_ESCAPE:
				case SWT.TRAVERSE_ARROW_PREVIOUS:
				case SWT.TRAVERSE_ARROW_NEXT:
					event.doit = false;
					break;
				case SWT.TRAVERSE_TAB_NEXT:
				case SWT.TRAVERSE_TAB_PREVIOUS:
//					event.doit = text.traverse(event.detail);
					event.detail = SWT.TRAVERSE_NONE;
					if (event.doit) dropDownCalendar (false);
					return;
				case SWT.TRAVERSE_PAGE_NEXT:
				case SWT.TRAVERSE_PAGE_PREVIOUS:
					return;
			}
			Event e = new Event ();
			e.time = event.time;
			e.detail = event.detail;
			e.doit = event.doit;
			e.character = event.character;
			e.keyCode = event.keyCode;
			notifyListeners (SWT.Traverse, e);
			event.doit = e.doit;
			event.detail = e.detail;
			break;
		}
		case SWT.KeyUp: {		
			Event e = new Event ();
			e.time = event.time;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.stateMask = event.stateMask;
			notifyListeners (SWT.KeyUp, e);
			break;
		}
		case SWT.KeyDown: {
			if (event.character == SWT.ESC) {
				/* Escape key cancels popupCalendar and reverts date */
				popupCalendar.setDate (savedYear, savedMonth, savedDay);
				setDate(savedYear, savedMonth, savedDay);
				dropDownCalendar (false);
			}
			if (event.keyCode == SWT.CR || (event.stateMask & SWT.ALT) != 0 && (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN)) {
				/* Return, Alt+Down, and Alt+Up cancel popupCalendar and select date. */
				dropDownCalendar (false);
			}
			if (event.keyCode == SWT.SPACE) {
				dropDownCalendar(false);
			}
			/* At this point the widget may have been disposed.
			 * If so, do not continue. */
			if (isDisposed ()) break;
			Event e = new Event();
			e.time = event.time;
			e.character = event.character;
			e.keyCode = event.keyCode;
			e.stateMask = event.stateMask;
			notifyListeners(SWT.KeyDown, e);
			break;
		}
	}
}

void handleFocus (int type) {
	if (isDisposed ()) return;
	switch (type) {
		case SWT.FocusIn: {
			if (hasFocus) return;
			selectAll ();
			hasFocus = true;
			Shell shell = getShell ();
			shell.removeListener (SWT.Deactivate, popupListener);
			shell.addListener (SWT.Deactivate, popupListener);
			Display display = getDisplay ();
			display.removeFilter (SWT.FocusIn, popupFilter);
			display.addFilter (SWT.FocusIn, popupFilter);
			Event e = new Event ();
			notifyListeners (SWT.FocusIn, e);
			break;
		}
		case SWT.FocusOut: {
			if (!hasFocus) return;
			Control focusControl = getDisplay ().getFocusControl ();
			if (focusControl == down || focusControl == popupCalendar ) return;
			hasFocus = false;
			Shell shell = getShell ();
			shell.removeListener(SWT.Deactivate, popupListener);
			Display display = getDisplay ();
			display.removeFilter (SWT.FocusIn, popupFilter);
			display.removeFilter(SWT.MouseDown, clickListener);
			Event e = new Event ();
			notifyListeners (SWT.FocusOut, e);
			break;
		}
	}
}

void popupShellEvent(Event event) {
	switch (event.type) {
		case SWT.Paint:
			/* Draw black rectangle around popupCalendar */
			Rectangle bounds = popupCalendar.getBounds();
			Color black = getDisplay().getSystemColor(SWT.COLOR_BLACK);
			event.gc.setForeground(black);
			event.gc.drawRectangle(0, 0, bounds.width + 1, bounds.height + 1);
			break;
		case SWT.Close:
			event.doit = false;
			dropDownCalendar (false);
			break;
		case SWT.MouseUp:
			dropDownCalendar (false);
			break;
	}
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

void selectField(int index) {
	if (index != currentField) {
		commitCurrentField();
	}
	final int start = fieldIndices[index].x;
	final int end = fieldIndices[index].y;
	Point pt = getSelection();
	if (index == currentField && start == pt.x && end == pt.y) return;
	currentField = index;
	display.asyncExec(new Runnable() {
		public void run() {
			if (handle != 0) {
				String value = getText(getText(),start, end - 1);
				int s = value.lastIndexOf(' ');
				if (s == -1) s = start;
				else s = start + s + 1;
				setSelection(s, end);
			}
		}
	});	
}

void sendSelectionEvent () {
	int [] y = new int [1];
	int [] m = new int [1];
	int [] d = new int [1];
	OS.gtk_calendar_get_date(handle, y, m, d);
	//TODO: hours, minutes, seconds?
	if (d[0] != day ||
		m[0] != month ||
		y[0] != year) {
		year = y[0];
		month = m[0];
		day = d[0];
		sendSelectionEvent (SWT.Selection);
	}
}

public void setBackground(Color color) {
	super.setBackground(color);
	if (!OS.GTK3) {
		if (((style & SWT.CALENDAR) != 0) && color == null) {
			OS.gtk_widget_modify_base(handle, 0, null);
		}
	}
	bg = color;
	if (popupCalendar != null) popupCalendar.setBackground(color);
}

void setBackgroundColor (GdkColor color) {
	if ((style & SWT.CALENDAR) != 0 && !OS.GTK3) {
		OS.gtk_widget_modify_base(handle, 0, color);
	} else {
		super.setBackgroundColor (color);
	}
}

public void setEnabled (boolean enabled){
	super.setEnabled(enabled);
	if ((style & SWT.DROP_DOWN) != 0)
		down.setEnabled(enabled);
}

public void setFont(Font font) {
	super.setFont(font);
	this.font = font;
	if (popupCalendar != null) popupCalendar.setFont(font);
	redraw();
}

void setForegroundColor (GdkColor color) {
	setForegroundColor (handle, color, false);
}

public void setForeground(Color color) {
	super.setForeground(color);
	fg = color;
	if (popupCalendar != null) popupCalendar.setForeground(color);
}

void setFormat(String string) {
	// TODO: this needs to be locale sensitive
	fieldCount = (style & SWT.DATE) != 0 ? ((style & SWT.SHORT) != 0 ? 2 : 3) : ((style & SWT.SHORT) != 0 ? 3 : 4);
	fieldIndices = new Point[fieldCount];
	fieldNames = new int[fieldCount];
	if ((style & SWT.DATE) != 0) {
		fieldNames[0] = Calendar.MONTH;
		fieldIndices[0] = new Point(0, 2);
		if ((style & SWT.SHORT) != 0) {
			fieldNames[1] = Calendar.YEAR;
			fieldIndices[1] = new Point(3, 7);
		} else {
			fieldNames[1] = Calendar.DAY_OF_MONTH;
			fieldIndices[1] = new Point(3, 5);
			fieldNames[2] = Calendar.YEAR;
			fieldIndices[2] = new Point(6, 10);
		}
	} else { /* SWT.TIME */
		fieldNames[0] = Calendar.HOUR;
		fieldIndices[0] = new Point(0, 2);
		fieldNames[1] = Calendar.MINUTE;
		fieldIndices[1] = new Point(3, 5);
		if ((style & SWT.SHORT) != 0) {
			fieldNames[2] = Calendar.AM_PM;
			fieldIndices[2] = new Point(6, 8);
		} else {
			fieldNames[2] = Calendar.SECOND;
			fieldIndices[2] = new Point(6, 8);
			fieldNames[3] = Calendar.AM_PM;
			fieldIndices[3] = new Point(9, 11);
		}
	}
}

void setField(int fieldName, int value) {
	if (calendar.get(fieldName) == value) return;
	if (fieldName == Calendar.AM_PM) {
		calendar.roll(Calendar.HOUR_OF_DAY, 12); // TODO: needs more work for setFormat and locale
	}
	calendar.set(fieldName, value);
	sendSelectionEvent (SWT.Selection);
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
	if (!isValidDate(year, month, day)) return;
	if ((style & SWT.CALENDAR) != 0) {
		this.year = year;
		this.month = month;
		this.day = day;
		OS.gtk_calendar_select_month(handle, month, year);
		OS.gtk_calendar_select_day(handle, day);
	} else {
		calendar.set(year, month, day);
		updateControl();
	}
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
	if (!isValidDate(getYear(), getMonth(), day)) return;
	if ((style & SWT.CALENDAR) != 0) {
		this.day = day;
		OS.gtk_calendar_select_day(handle, day);
	} else {
		calendar.set(Calendar.DAY_OF_MONTH, day);
		updateControl();
	}
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
	if (!isValidTime(Calendar.HOUR_OF_DAY, hours)) return;
	if ((style & SWT.CALENDAR) != 0) {
		this.hours = hours;
	} else {
		calendar.set(Calendar.HOUR_OF_DAY, hours);
		updateControl();
	}
}

public void setMenu (Menu menu) {
	super.setMenu(menu);
	if (up != null) up.setMenu(menu);
	if (down != null) down.setMenu(menu);
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
	if (!isValidTime(Calendar.MINUTE, minutes)) return;
	if ((style & SWT.CALENDAR) != 0) {
		this.minutes = minutes;
	} else {
		calendar.set(Calendar.MINUTE, minutes);
		updateControl();
	}
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
	if (!isValidDate(getYear(), month, getDay())) return;
	if ((style & SWT.CALENDAR) != 0) {
		this.month = month;
		OS.gtk_calendar_select_month(handle, month, year);
	} else {
		calendar.set(Calendar.MONTH, month);
		updateControl();
	}
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
	if (!isValidTime(Calendar.SECOND, seconds)) return;
	if ((style & SWT.CALENDAR) != 0) {
		this.seconds = seconds;
	} else {
		calendar.set(Calendar.SECOND, seconds);
		updateControl();
	}
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
	if (!isValidTime(Calendar.HOUR_OF_DAY, hours)) return;
	if (!isValidTime(Calendar.MINUTE, minutes)) return;
	if (!isValidTime(Calendar.SECOND, seconds)) return;
	if ((style & SWT.CALENDAR) != 0) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	} else {
		calendar.set(Calendar.HOUR_OF_DAY, hours);
		calendar.set(Calendar.MINUTE, minutes);
		calendar.set(Calendar.SECOND, seconds);
		updateControl();
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
	if (!isValidDate(year, getMonth(), getDay())) return;
	if ((style & SWT.CALENDAR) != 0) {
		this.year = year;
		OS.gtk_calendar_select_month(handle, month, year);
	} else {
		calendar.set(Calendar.YEAR, year);
		updateControl();
	}
}

void onResize(Event event) {
	Rectangle rect = getClientArea ();
	int width = rect.width;
	int height = rect.height;
	Point buttonSize = down.computeSize(SWT.DEFAULT, height);
	if ((style & SWT.DROP_DOWN) != 0) {
		down.setBounds(width - buttonSize.x, 0, buttonSize.x, height);
	} else {
		int buttonHeight = height / 2;
		up.setBounds(width - buttonSize.x, 0, buttonSize.x, buttonHeight);
		down.setBounds(width - buttonSize.x, buttonHeight, buttonSize.x, buttonHeight);
	}
}

boolean onTextVerify(int key) {
	if (ignoreVerify) return true;
	int fieldName = fieldNames[currentField];
	int start = fieldIndices[currentField].x;
	int end = fieldIndices[currentField].y;
	int length = end - start;
	char keyChar = (char) key;
	String newText =""+ keyChar;
	if (fieldName == Calendar.AM_PM) {
		String[] ampm = formatSymbols.getAmPmStrings();
		if (newText.equalsIgnoreCase(ampm[Calendar.AM].substring(0, 1)) || newText.equalsIgnoreCase(ampm[Calendar.AM])) {
			setTextField(fieldName, Calendar.AM, true, false);
		} else if (newText.equalsIgnoreCase(ampm[Calendar.PM].substring(0, 1)) || newText.equalsIgnoreCase(ampm[Calendar.PM])) {
			setTextField(fieldName, Calendar.PM, true, false);
		}
		return false;
	}
	if (characterCount > 0) {
		try {
			Integer.parseInt(newText);
		} catch (NumberFormatException ex) {
			return false;
		}
		String value = getText(start, end - 1);
		int s = value.lastIndexOf(' ');
		if (s != -1) value = value.substring(s + 1);
		newText = "" + value + newText;
	}
	int newTextLength = newText.length();
	boolean first = characterCount == 0;
	characterCount = (newTextLength < length) ? newTextLength : 0;
	int max = calendar.getActualMaximum(fieldName);
	int min = calendar.getActualMinimum(fieldName);
	int newValue = unformattedIntValue(fieldName, newText, characterCount == 0, max);
	if (newValue == -1) {
		characterCount = 0;
		return false;
	}
	if (first && newValue == 0 && length > 1) {
		setTextField(fieldName, newValue, false, false);
	} else if (min <= newValue && newValue <= max) {
		setTextField(fieldName, newValue, characterCount == 0, characterCount == 0);
	} else {
		if (newTextLength >= length) {
			newText = newText.substring(newTextLength - length + 1);
			newValue = unformattedIntValue(fieldName, newText, characterCount == 0, max);
			if (newValue != -1) {
				characterCount = length - 1;
				if (min <= newValue && newValue <= max) {
					setTextField(fieldName, newValue, characterCount == 0, true);
				}
			}
		}
	}
	return false;
}

int unformattedIntValue(int fieldName, String newText, boolean adjust, int max) {
	int newValue;
	try {
		newValue = Integer.parseInt(newText);
	} catch (NumberFormatException ex) {
		return -1;
	}
	if (fieldName == Calendar.MONTH && adjust) {
		newValue--;
		if (newValue == -1) newValue = max;
	}
	if (fieldName == Calendar.HOUR && adjust) {
		if (newValue == 12) newValue = 0; // TODO: needs more work for setFormat and locale
	}
	return newValue;
}

void updateControl() {
	if (((style & SWT.CALENDAR) == 0) && handle != 0) {
		String string = getFormattedString(style);
		ignoreVerify = true;
		setText(string);
		ignoreVerify = false;
	}
	redraw();	
}
void deregister () {
	super.deregister ();
	if (handle != 0) display.removeWidget (handle);
	if (fixedHandle != 0) display.removeWidget (fixedHandle);
}

void register () {
	super.register ();
	if (handle != 0) display.addWidget (handle, this);
}

int getArrow (long /*int*/ widget) {
	int adj_value = (int) OS.gtk_adjustment_get_value(OS.gtk_spin_button_get_adjustment(widget));
	int new_value = 0;
		if ((style & SWT.DATE) != 0) {
			// getMonth() return 0 as first month and 11 as last one, whereas adjusment does not, so adding one makes them comaprable
			new_value = getMonth()+1;
		} else if ((style & SWT.TIME) != 0) {
		// as getHours() has 24h format but spinner 12h format, new_value needs to be converted to 12h format
			if (getHours() > 12 ){
				new_value = getHours() - 12;  
			} else {
				new_value = getHours();
				// This fix does not compares adj_value to new_value when getArrow is called on widget creation
			
			}
			if (new_value == 0) new_value = 12; 	
		}
		if (adj_value == 0 && firstTime)
			return 0;
		firstTime = false;
		if ( adj_value == new_value) return 0;
	return adj_value > new_value ? SWT.ARROW_UP : SWT.ARROW_DOWN;
}

/**
 * Calculates appropriate width of GtkEntry and
 * adds Date/Time string to the Date/Time Spinner
 */
void setText (String text) {
	if ((style & SWT.DROP_DOWN) == 0 ) { // Drop down button implemention is not based on GtkSpinButton
		OS.gtk_spin_button_set_numeric (handle, false);
	}
	if (text != null){ 
		byte [] dateTimeString = Converter.wcsToMbcs (null, text , true);		
		OS.gtk_entry_set_width_chars(handle, dateTimeString.length);
		OS.gtk_entry_set_text(handle, dateTimeString);
	}
}

long /*int*/ gtk_key_press_event (long /*int*/ widget, long /*int*/ event) {
	long /*int*/ result = super.gtk_key_press_event (widget, event);
	int fieldName;
	if ((style & SWT.READ_ONLY) == 0 && (style & SWT.CALENDAR) == 0) {
		GdkEventKey keyEvent = new GdkEventKey ();
		OS.memmove (keyEvent, event, GdkEventKey.sizeof);
		int key = keyEvent.keyval;
		switch (key) {
			case OS.GDK_Up:
				/* As drop_down  cannot be hooked to gtk_output, it is hooked to gtk_key_press. 
				   Only drop_down option should be hooked to keys, thus spinner is not called twice
				   on key_press and on gtk_output*/
				if((style & SWT.DROP_DOWN) != 0) {
					incrementField(+1);
					commitCurrentField();
				}
				break;
			case OS.GDK_Down:
				/* As drop_down  cannot be hooked to gtk_output, it is hooked to gtk_key_press. 
				   Only drop_down should be hooked to keys, thus when spinner is used up is not called twice
				   on key_press and on gtk_output*/
				if((style & SWT.DROP_DOWN) != 0) {
					incrementField(-1);
					commitCurrentField();
				}
				break;
			case OS.GDK_Right:
				selectField((currentField + 1) % fieldCount);
				sendEvent(SWT.Traverse);
				break;
			case OS.GDK_Left:
				int index = currentField - 1;
				selectField(index < 0 ? fieldCount - 1 : index);
				sendEvent(SWT.Traverse);
				break;
			case OS.GDK_Home:
				/* Set the value of the current field to its minimum */
				fieldName = fieldNames[currentField];
				setTextField(fieldName, calendar.getActualMinimum(fieldName), true, true);
				break;
			case OS.GDK_End:
				/* Set the value of the current field to its maximum */
				fieldName = fieldNames[currentField];
				setTextField(fieldName, calendar.getActualMaximum(fieldName), true, true);
				break;
			default:
				if (!onTextVerify(key))
					return 1;
					
		}
	}	
	return result;
}

void commitCurrentField() {
	if (characterCount > 0) {
		characterCount = 0;
		int fieldName = fieldNames[currentField];
		int start = fieldIndices[currentField].x;
		int end = fieldIndices[currentField].y;
		String value = getText(getText(),start, end - 1);
		int s = value.lastIndexOf(' ');
		if (s != -1) value = value.substring(s + 1);
		int newValue = unformattedIntValue(fieldName, value, characterCount == 0, calendar.getActualMaximum(fieldName));
		if (newValue != -1) setTextField(fieldName, newValue, true, true);
	}
}
/** returns selected text **/
Point getSelection () {
	checkWidget ();
	Point selection;
	int [] start = new int [1];
	int [] end = new int [1];
	OS.gtk_editable_get_selection_bounds (handle, start, end);
	long /*int*/ ptr = OS.gtk_entry_get_text (handle);
	start[0] = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, start[0]);
	end[0] = (int)/*64*/OS.g_utf8_offset_to_utf16_offset (ptr, end[0]);
	selection = new Point (start [0], end [0]);
	return selection;

}

/**
 * Returns a string containing a copy of the contents of the
 * receiver's text field, or an empty string if there are no
 * contents.
 *
 * @return Spinner's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
String getText () {
	checkWidget();
	if (handle != 0) {
		long /*int*/ str = OS.gtk_entry_get_text (handle);
		if (str == 0) return "";
		int length = OS.strlen (str);
		byte [] buffer = new byte [length];
		OS.memmove (buffer, str, length);
		return new String (Converter.mbcsToWcs (null, buffer));
	}
		return "";
}

/** 
 * returns GtkEntry starting from index and ending with index
 * provided by the user 
 */
String getText (String str,int start, int end) {
	checkWidget ();
	if (!(start <= end && 0 <= end)) return "";
	int length = str.length ();
	end = Math.min (end, length - 1);
	if (start > end) return "";
	start = Math.max (0, start);
	/*
	* NOTE: The current implementation uses substring ()
	* which can reference a potentially large character
	* array.
	*/
	return str.substring (start, end + 1);
}

void setSelection (int start, int end) {
	checkWidget ();
	long /*int*/ ptr = OS.gtk_entry_get_text (handle);
	start = (int)/*64*/OS.g_utf16_offset_to_utf8_offset (ptr, start);
	end = (int)/*64*/OS.g_utf16_offset_to_utf8_offset (ptr, end);
	OS.gtk_editable_set_position (handle, start);
	OS.gtk_editable_select_region (handle, start, end);
}

void setTextField(int fieldName, int value, boolean commit, boolean adjust) {
	if (commit) {
		int max = calendar.getActualMaximum(fieldName);
		int min = calendar.getActualMinimum(fieldName);
		if (fieldName == Calendar.YEAR) {
			max = MAX_YEAR;
			min = MIN_YEAR;
			/* Special case: convert 1 or 2-digit years into reasonable 4-digit years. */
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			int currentCentury = (currentYear / 100) * 100;
			if (value < (currentYear + 30) % 100) value += currentCentury;
			else if (value < 100) value += currentCentury - 100;
		}
		if (value > max) value = min; // wrap
		if (value < min) value = max; // wrap
	}
	int start = fieldIndices[currentField].x;
	int end = fieldIndices[currentField].y;
	setSelection(start, end);
	String newValue = formattedStringValue(fieldName, value, adjust);
	StringBuffer buffer = new StringBuffer(newValue);
	/* Convert leading 0's into spaces. */
	int prependCount = end - start - buffer.length();
	for (int i = 0; i < prependCount; i++) {
		switch (fieldName) {
			case Calendar.MINUTE:
			case Calendar.SECOND:
				buffer.insert(0, 0);
				break;
			default:
				buffer.insert(0, ' ');
				break;
		}
	}		
	newValue = buffer.toString();
	ignoreVerify = true;
	insert(newValue);
	ignoreVerify = false;
	selectField(currentField);
	if (commit) setField(fieldName, value);
}

long /*int*/ gtk_button_press_event (long /*int*/ widget, long /*int*/ event) {
	if ((style & SWT.CALENDAR) == 0 ){
		GdkEventButton gdkEvent = new GdkEventButton ();
		OS.memmove (gdkEvent, event, GdkEventButton.sizeof);
		if (gdkEvent.type == OS.GDK_BUTTON_PRESS && gdkEvent.button == 1) {
			onTextMouseClick(gdkEvent);
			return gtk_button_press_event(widget, event, false);
		}
	}
	return super.gtk_button_press_event (widget, event);
}

/**
 * Output signal is called when Spinner's arrow buttons are triggered.
 * On every click output is called twice presenting current and previous value.
 * This method compares two values and determines if Up or down arrow was called.
 */
long gtk_output (long /*int*/ widget) {
	int arrowType = getArrow(handle);
		switch (arrowType) {
			case SWT.ARROW_UP:
				commitCurrentField();
				incrementField(+1);
				break;
			case SWT.ARROW_DOWN:
				commitCurrentField();
				incrementField(-1);
				break;
	}	
	return 1;
}

void insert (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	byte [] buffer = Converter.wcsToMbcs (null, string, false);
	int [] start = new int [1], end = new int [1];
	OS.gtk_editable_get_selection_bounds (handle, start, end);
	OS.gtk_editable_delete_selection (handle);
	OS.gtk_editable_insert_text (handle, buffer, buffer.length, start);
	OS.gtk_editable_set_position (handle, start [0]);
}

void onTextMouseClick(GdkEventButton event) {
	Point sel = getSelection();
	for (int i = 0; i < fieldCount; i++) {
		if (sel.x >= fieldIndices[i].x && sel.x <= fieldIndices[i].y) {
			currentField = i;
			break;
		}
	}
	selectField(currentField);
}

String getText (int start, int end) {
	checkWidget ();
	if (!(start <= end && 0 <= end)) return "";
	String str = getText ();
	int length = str.length ();
	end = Math.min (end, length - 1);
	if (start > end) return "";
	start = Math.max (0, start);
	/*
	* NOTE: The current implementation uses substring ()
	* which can reference a potentially large character
	* array.
	*/
	return str.substring (start, end + 1);
}

public void selectAll () {
	checkWidget ();
	if (handle != 0)
		OS.gtk_editable_select_region (handle, 0, -1);
}


void hideDateTime () {
	if ((style & SWT.CALENDAR) == 0){
		OS.gtk_widget_hide (fixedHandle);
	}
}

void releaseWidget () {
	super.releaseWidget();
	if (fixedHandle != 0)
		hideDateTime();
}
}