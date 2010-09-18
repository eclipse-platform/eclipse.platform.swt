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

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

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
	static final int MIN_YEAR = 1752; // Gregorian switchover in North America: September 19, 1752
	static final int MAX_YEAR = 9999;
	
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
	style = checkBits (style, SWT.MEDIUM, SWT.SHORT, SWT.LONG, 0, 0, 0);
	return checkBits (style, SWT.DATE, SWT.TIME, SWT.CALENDAR, 0, 0, 0);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
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

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 0, height = 0;
	NSControl widget = (NSControl)view;
	NSSize size = widget.cell ().cellSize ();
	width = (int)Math.ceil (size.width);
	height = (int)Math.ceil (size.height);
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int border = getBorderWidth ();
	width += border * 2; height += border * 2;
	return new Point (width, height);
}

void createHandle () {
	NSDatePicker widget = (NSDatePicker)new SWTDatePicker().alloc();
	widget.init();
	int pickerStyle = OS.NSTextFieldAndStepperDatePickerStyle;
	int elementFlags = 0;
	if ((style & SWT.CALENDAR) != 0) {
		pickerStyle = OS.NSClockAndCalendarDatePickerStyle;
		elementFlags = OS.NSYearMonthDayDatePickerElementFlag;
	} else {
		if ((style & SWT.TIME) != 0) {
			elementFlags = (style & SWT.SHORT) != 0 ? OS.NSHourMinuteDatePickerElementFlag : OS.NSHourMinuteSecondDatePickerElementFlag;
		}
		if ((style & SWT.DATE) != 0) {
			elementFlags = (style & SWT.SHORT) != 0 ? OS.NSYearMonthDatePickerElementFlag : OS.NSYearMonthDayDatePickerElementFlag;
		}
	}
	widget.setDrawsBackground(true);
	widget.setDatePickerStyle(pickerStyle);
	widget.setDatePickerElements(elementFlags);
	NSDate date = NSCalendarDate.calendarDate();
	widget.setDateValue(date);
	widget.setTarget(widget);
	widget.setAction(OS.sel_sendSelection);
	view = widget;
}

NSFont defaultNSFont() {
	return display.datePickerFont;
}

void drawBackground (int /*long*/ id, NSGraphicsContext context, NSRect rect) {
	if (id != view.id) return;
	fillBackground (view, context, rect, -1);
}

NSCalendarDate getCalendarDate () {
	NSDate date = ((NSDatePicker)view).dateValue();
	return date.dateWithCalendarFormat(null, null);
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
	return (int)/*64*/getCalendarDate().dayOfMonth();
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
	return (int)/*64*/getCalendarDate().hourOfDay();
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
	return (int)/*64*/getCalendarDate().minuteOfHour();
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
	return (int)/*64*/getCalendarDate().monthOfYear() - 1;
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
	return (int)/*64*/getCalendarDate().secondOfMinute();
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
	return (int)/*64*/getCalendarDate().yearOfCommonEra();
}

boolean isEventView (int /*long*/ id) {
	return true;
}

boolean isFlipped (int /*long*/ id, int /*long*/ sel) {
	if ((style & SWT.CALENDAR) != 0) return super.isFlipped (id, sel);
	return true;
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

boolean sendKeyEvent (NSEvent nsEvent, int type) {
	boolean result = super.sendKeyEvent (nsEvent, type);
	if (!result) return result;
	if (type != SWT.KeyDown) return result;
	if ((style & SWT.CALENDAR) == 0) {
		short keyCode = nsEvent.keyCode ();
		switch (keyCode) {
			case 76: /* KP Enter */
			case 36: /* Return */
				sendSelectionEvent (SWT.DefaultSelection);
		}
	}
	return result;
}

void sendSelection () {
	NSEvent event = NSApplication.sharedApplication().currentEvent();
	if (event != null && (style & SWT.CALENDAR) != 0) {
		if (event.clickCount() == 2) {
			sendSelectionEvent (SWT.DefaultSelection);
		} else if (event.type() == OS.NSLeftMouseUp) {
			sendSelectionEvent (SWT.Selection);
		}
	} else { // SWT.DATE or SWT.TIME
		sendSelectionEvent (SWT.Selection);
	}
}

void setBackgroundColor(NSColor nsColor) {
	((NSDatePicker)view).setBackgroundColor(nsColor);
}

void setBackgroundImage(NSImage image) {
	((NSDatePicker)view).setDrawsBackground(image == null);
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
	if (year < MIN_YEAR || year > MAX_YEAR) return;
	NSCalendarDate date = getCalendarDate();
	NSCalendarDate newDate = NSCalendarDate.dateWithYear(year, month + 1, day,
			date.hourOfDay(), date.minuteOfHour(), date.secondOfMinute(), date.timeZone());
	if (newDate.yearOfCommonEra() == year && newDate.monthOfYear() == month + 1 && newDate.dayOfMonth() == day) {
		((NSDatePicker)view).setDateValue(newDate);
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
	NSCalendarDate date = getCalendarDate();
	NSCalendarDate newDate = NSCalendarDate.dateWithYear(date.yearOfCommonEra(), date.monthOfYear(), day,
			date.hourOfDay(), date.minuteOfHour(), date.secondOfMinute(), date.timeZone());
	if (newDate.yearOfCommonEra() == date.yearOfCommonEra() && newDate.monthOfYear() == date.monthOfYear() && newDate.dayOfMonth() == day) {
		((NSDatePicker)view).setDateValue(newDate);
	}
}

void setForeground (float /*double*/ [] color) {
	NSColor nsColor;
	if (color == null) {
		if ((style & SWT.CALENDAR) != 0) {
			nsColor = NSColor.controlTextColor ();
		} else {
			nsColor = NSColor.textColor ();
		}
	} else {
		nsColor = NSColor.colorWithCalibratedRed(color[0], color[1], color[2], 1);
	}
	((NSDatePicker)view).setTextColor(nsColor);
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
	if (hours < 0 || hours > 23) return;
	NSCalendarDate date = getCalendarDate();
	NSCalendarDate newDate = NSCalendarDate.dateWithYear(date.yearOfCommonEra(), date.monthOfYear(), date.dayOfMonth(),
			hours, date.minuteOfHour(), date.secondOfMinute(), date.timeZone());
	((NSDatePicker)view).setDateValue(newDate);
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
	if (minutes < 0 || minutes > 59) return;
	NSCalendarDate date = getCalendarDate();
	NSCalendarDate newDate = NSCalendarDate.dateWithYear(date.yearOfCommonEra(), date.monthOfYear(), date.dayOfMonth(),
			date.hourOfDay(), minutes, date.secondOfMinute(), date.timeZone());
	((NSDatePicker)view).setDateValue(newDate);
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
	NSCalendarDate date = getCalendarDate();
	NSCalendarDate newDate = NSCalendarDate.dateWithYear(date.yearOfCommonEra(), month + 1, date.dayOfMonth(),
			date.hourOfDay(), date.minuteOfHour(), date.secondOfMinute(), date.timeZone());
	if (newDate.yearOfCommonEra() == date.yearOfCommonEra() && newDate.monthOfYear() == month + 1 && newDate.dayOfMonth() == date.dayOfMonth()) {
		((NSDatePicker)view).setDateValue(newDate);
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
	if (seconds < 0 || seconds > 59) return;
	NSCalendarDate date = getCalendarDate();
	NSCalendarDate newDate = NSCalendarDate.dateWithYear(date.yearOfCommonEra(), date.monthOfYear(), date.dayOfMonth(),
			date.hourOfDay(), date.minuteOfHour(), seconds, date.timeZone());
	((NSDatePicker)view).setDateValue(newDate);
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
	if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59 || seconds < 0 || seconds > 59) return;
	NSCalendarDate date = getCalendarDate();
	NSCalendarDate newDate = NSCalendarDate.dateWithYear(date.yearOfCommonEra(), date.monthOfYear(), date.dayOfMonth(),
			hours, minutes, seconds, date.timeZone());
	((NSDatePicker)view).setDateValue(newDate);
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
	if (year < MIN_YEAR || year > MAX_YEAR) return;
	NSCalendarDate date = getCalendarDate();
	NSCalendarDate newDate = NSCalendarDate.dateWithYear(year, date.monthOfYear(), date.dayOfMonth(),
			date.hourOfDay(), date.minuteOfHour(), date.secondOfMinute(), date.timeZone());
	if (newDate.yearOfCommonEra() == year && newDate.monthOfYear() == date.monthOfYear() && newDate.dayOfMonth() == date.dayOfMonth()) {
		((NSDatePicker)view).setDateValue(newDate);
	}
}
}
