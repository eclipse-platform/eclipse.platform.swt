/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
import org.eclipse.swt.internal.carbon.LongDateRec;
import org.eclipse.swt.internal.carbon.OS;

/*public*/ class DateTime extends Control {
	LongDateRec dateRec;
	
public DateTime (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	return checkBits (style, SWT.DATE, SWT.TIME, SWT.CALENDAR, 0, 0, 0);
}

public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection, typedListener);
	addListener (SWT.DefaultSelection, typedListener);
}

public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget ();
	int width = 64, height = 64;
	return new Point (width, height);
}

void createHandle () {
	int clockType = 0;
	int clockFlags = OS.kControlClockFlagStandard;
	if ((style & SWT.TIME) != 0) clockType = OS.kControlClockTypeHourMinuteSecond;
	if ((style & SWT.DATE) != 0) clockType = OS.kControlClockTypeMonthDayYear;
	int [] outControl = new int [1];
	int window = OS.GetControlOwner (parent.handle);
	OS.CreateClockControl(window, null, clockType, clockFlags, outControl);
	if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
	handle = outControl [0];
}

void createWidget() {
	super.createWidget ();
	dateRec = new LongDateRec ();
	OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec, null);
}

public int getDay () {
	checkWidget ();
	return dateRec.day;
}

public int getHour () {
	checkWidget ();
	return dateRec.hour;
}

public int getMinute () {
	checkWidget ();
	return dateRec.minute;
}

public int getMonth () {
	checkWidget ();
	return dateRec.month;
}

public int getSecond () {
	checkWidget ();
	return dateRec.second;
}

public int getYear () {
	checkWidget ();
	return dateRec.year;
}

int kEventControlHit (int nextHandler, int theEvent, int userData) {
	int result = super.kEventControlHit (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	sendSelectionEvent ();
	return result;
}

int kEventTextInputUnicodeForKeyEvent (int nextHandler, int theEvent, int userData) {
	int result = super.kEventTextInputUnicodeForKeyEvent (nextHandler, theEvent, userData);
	if (result == OS.noErr) return result;
	sendSelectionEvent ();
	return result;
}

void releaseWidget () {
	super.releaseWidget();
	dateRec = null;
}

public void removeSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection, listener);	
}

void sendSelectionEvent () {
	LongDateRec rec = new LongDateRec ();
	OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, rec, null);
	if (rec.second != dateRec.second ||
		rec.minute != dateRec.minute ||
		rec.hour != dateRec.hour ||
		rec.day != dateRec.day ||
		rec.month != dateRec.month ||
		rec.year != dateRec.year) {
		dateRec = rec;
		postEvent (SWT.Selection);
	}
}

public void setDay (int day) {
	checkWidget ();
	dateRec.day = (short)day;
	OS.SetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec);
	OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec, null);
}

public void setHour (int hour) {
	checkWidget ();
	dateRec.hour = (short)hour;
	OS.SetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec);
	OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec, null);
}

public void setMinute (int minute) {
	checkWidget ();
	dateRec.minute = (short)minute;
	OS.SetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec);
	OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec, null);
}

public void setMonth (int month) {
	checkWidget ();
	dateRec.month = (short)month;
	OS.SetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec);
	OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec, null);
}

public void setSecond (int second) {
	checkWidget ();
	dateRec.second = (short)second;
	OS.SetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec);
	OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec, null);
}

public void setYear (int year) {
	checkWidget ();
	dateRec.year = (short)year;
	OS.SetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec);
	OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec, null);
}

}
