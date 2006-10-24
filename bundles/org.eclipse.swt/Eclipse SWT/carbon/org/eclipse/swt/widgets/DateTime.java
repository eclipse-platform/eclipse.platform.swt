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
	// TODO: needs to be sub of Composite
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
	int width = 0, height = 0;
	if (wHint == SWT.DEFAULT || hHint == SWT.DEFAULT) {
		if ((style & SWT.CALENDAR) != 0) {
			// TODO
			width = 100;
			height = 100;
		} else {
			// TODO: get the height of the current font
			height = 20;
			// TODO: max with the height of the up/down buttons
			int upDownHeight = 24;
			height = Math.max (height, upDownHeight);
			
			// TODO: determine the stringWidth of date or time string in current font (take code from GC)
			String string = "00/00/0000";
			if ((style & SWT.TIME) != 0) string = "00:00:00 AM";
			GC gc = new GC(this);
			width = gc.stringExtent(string).x;
			gc.dispose();
			// TODO: max with the height of the up/down buttons (maybe plus some margin?)
			int upDownWidth = 20;
			width += upDownWidth + 5; // MARGIN
		}
	}
	if (width == 0) width = DEFAULT_WIDTH;
	if (height == 0) height = DEFAULT_HEIGHT;
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	int border = getBorderWidth ();
	width += border * 2; height += border * 2;
	return new Point (width, height);
}

void createHandle () {
	int clockType = 0;
	if ((style & SWT.TIME) != 0) clockType = OS.kControlClockTypeHourMinuteSecond;
	if ((style & SWT.DATE) != 0) clockType = OS.kControlClockTypeMonthDayYear;
	if (clockType != 0) { /* SWT.DATE and SWT.TIME */
		int clockFlags = OS.kControlClockFlagStandard;
		int [] outControl = new int [1];
		int window = OS.GetControlOwner (parent.handle);
		OS.CreateClockControl(window, null, clockType, clockFlags, outControl);
		if (outControl [0] == 0) error (SWT.ERROR_NO_HANDLES);
		handle = outControl [0];
	} else { /* SWT.CALENDAR */
		// TODO: on Cocoa, can use NSDatePicker - otherwise, need to use emulated
	}
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

void hookEvents () {
	super.hookEvents ();
	int clockProc = display.clockProc;
	int [] mask = new int [] {
		OS.kEventClassClockView, OS.kEventClockDateOrTimeChanged,
	};
	int controlTarget = OS.GetControlEventTarget (handle);
	OS.InstallEventHandler (controlTarget, clockProc, mask.length / 2, mask, handle, null);
}

int kEventClockDateOrTimeChanged (int nextHandler, int theEvent, int userData) {
	// TODO: Tiger (10.4) and up only: kEventClassClockView / kEventClockDateOrTimeChanged
	sendSelectionEvent ();
	return OS.noErr;
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
	redraw();
}

public void setHour (int hour) {
	checkWidget ();
	dateRec.hour = (short)hour;
	OS.SetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec);
	OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec, null);
	redraw();
}

public void setMinute (int minute) {
	checkWidget ();
	dateRec.minute = (short)minute;
	OS.SetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec);
	OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec, null);
	redraw();
}

public void setMonth (int month) {
	checkWidget ();
	dateRec.month = (short)month;
	OS.SetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec);
	OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec, null);
	redraw();
}

public void setSecond (int second) {
	checkWidget ();
	dateRec.second = (short)second;
	OS.SetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec);
	OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec, null);
	redraw();
}

public void setYear (int year) {
	checkWidget ();
	dateRec.year = (short)year;
	OS.SetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec);
	OS.GetControlData (handle, (short)OS.kControlEntireControl, OS.kControlClockLongDateTag, LongDateRec.sizeof, dateRec, null);
	redraw();
}

}
