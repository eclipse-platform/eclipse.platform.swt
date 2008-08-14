/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
	int result = OS.objc_msgSend(OS.class_NSCalendarDate, OS.sel_calendarDate);
	return result != 0 ? new NSCalendarDate(result) : null;
}

public static NSCalendarDate dateWithYear(int year, int month, int day, int hour, int minute, int second, NSTimeZone aTimeZone) {
	int result = OS.objc_msgSend(OS.class_NSCalendarDate, OS.sel_dateWithYear_month_day_hour_minute_second_timeZone_, year, month, day, hour, minute, second, aTimeZone != null ? aTimeZone.id : 0);
	return result != 0 ? new NSCalendarDate(result) : null;
}

public int dayOfMonth() {
	return OS.objc_msgSend(this.id, OS.sel_dayOfMonth);
}

public int hourOfDay() {
	return OS.objc_msgSend(this.id, OS.sel_hourOfDay);
}

public int minuteOfHour() {
	return OS.objc_msgSend(this.id, OS.sel_minuteOfHour);
}

public int monthOfYear() {
	return OS.objc_msgSend(this.id, OS.sel_monthOfYear);
}

public int secondOfMinute() {
	return OS.objc_msgSend(this.id, OS.sel_secondOfMinute);
}

public NSTimeZone timeZone() {
	int result = OS.objc_msgSend(this.id, OS.sel_timeZone);
	return result != 0 ? new NSTimeZone(result) : null;
}

public int yearOfCommonEra() {
	return OS.objc_msgSend(this.id, OS.sel_yearOfCommonEra);
}

public static NSDate dateWithTimeIntervalSinceNow(double secs) {
	int result = OS.objc_msgSend(OS.class_NSCalendarDate, OS.sel_dateWithTimeIntervalSinceNow_, secs);
	return result != 0 ? new NSCalendarDate(result) : null;
}

public static NSDate distantFuture() {
	int result = OS.objc_msgSend(OS.class_NSCalendarDate, OS.sel_distantFuture);
	return result != 0 ? new NSCalendarDate(result) : null;
}

}
