/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSCalendarDate extends NSDate {

public NSCalendarDate() {
	super();
}

public NSCalendarDate(int /*long*/ id) {
	super(id);
}

public NSCalendarDate(id id) {
	super(id);
}

public static NSCalendarDate calendarDate() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSCalendarDate, OS.sel_calendarDate);
	return result != 0 ? new NSCalendarDate(result) : null;
}

public static NSCalendarDate dateWithYear(int /*long*/ year, int /*long*/ month, int /*long*/ day, int /*long*/ hour, int /*long*/ minute, int /*long*/ second, NSTimeZone aTimeZone) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSCalendarDate, OS.sel_dateWithYear_month_day_hour_minute_second_timeZone_, year, month, day, hour, minute, second, aTimeZone != null ? aTimeZone.id : 0);
	return result != 0 ? new NSCalendarDate(result) : null;
}

public int /*long*/ dayOfMonth() {
	return OS.objc_msgSend(this.id, OS.sel_dayOfMonth);
}

public int /*long*/ hourOfDay() {
	return OS.objc_msgSend(this.id, OS.sel_hourOfDay);
}

public int /*long*/ minuteOfHour() {
	return OS.objc_msgSend(this.id, OS.sel_minuteOfHour);
}

public int /*long*/ monthOfYear() {
	return OS.objc_msgSend(this.id, OS.sel_monthOfYear);
}

public int /*long*/ secondOfMinute() {
	return OS.objc_msgSend(this.id, OS.sel_secondOfMinute);
}

public NSTimeZone timeZone() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_timeZone);
	return result != 0 ? new NSTimeZone(result) : null;
}

public int /*long*/ yearOfCommonEra() {
	return OS.objc_msgSend(this.id, OS.sel_yearOfCommonEra);
}

public static NSDate dateWithTimeIntervalSinceNow(double secs) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSCalendarDate, OS.sel_dateWithTimeIntervalSinceNow_, secs);
	return result != 0 ? new NSCalendarDate(result) : null;
}

public static NSDate distantFuture() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSCalendarDate, OS.sel_distantFuture);
	return result != 0 ? new NSCalendarDate(result) : null;
}

}
