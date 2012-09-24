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

public NSCalendarDate(long /*int*/ id) {
	super(id);
}

public NSCalendarDate(id id) {
	super(id);
}

public static NSCalendarDate calendarDate() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSCalendarDate, OS.sel_calendarDate);
	return result != 0 ? new NSCalendarDate(result) : null;
}

public static NSCalendarDate dateWithYear(long /*int*/ year, long /*int*/ month, long /*int*/ day, long /*int*/ hour, long /*int*/ minute, long /*int*/ second, NSTimeZone aTimeZone) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSCalendarDate, OS.sel_dateWithYear_month_day_hour_minute_second_timeZone_, year, month, day, hour, minute, second, aTimeZone != null ? aTimeZone.id : 0);
	return result != 0 ? new NSCalendarDate(result) : null;
}

public long /*int*/ dayOfMonth() {
	return OS.objc_msgSend(this.id, OS.sel_dayOfMonth);
}

public long /*int*/ hourOfDay() {
	return OS.objc_msgSend(this.id, OS.sel_hourOfDay);
}

public long /*int*/ minuteOfHour() {
	return OS.objc_msgSend(this.id, OS.sel_minuteOfHour);
}

public long /*int*/ monthOfYear() {
	return OS.objc_msgSend(this.id, OS.sel_monthOfYear);
}

public long /*int*/ secondOfMinute() {
	return OS.objc_msgSend(this.id, OS.sel_secondOfMinute);
}

public NSTimeZone timeZone() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_timeZone);
	return result != 0 ? new NSTimeZone(result) : null;
}

public long /*int*/ yearOfCommonEra() {
	return OS.objc_msgSend(this.id, OS.sel_yearOfCommonEra);
}

public static NSDate dateWithTimeIntervalSinceNow(double secs) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSCalendarDate, OS.sel_dateWithTimeIntervalSinceNow_, secs);
	return result != 0 ? new NSCalendarDate(result) : null;
}

public static NSDate distantFuture() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSCalendarDate, OS.sel_distantFuture);
	return result != 0 ? new NSCalendarDate(result) : null;
}

}
