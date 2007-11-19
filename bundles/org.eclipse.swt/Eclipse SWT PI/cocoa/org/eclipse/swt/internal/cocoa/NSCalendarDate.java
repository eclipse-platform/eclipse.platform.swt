/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSCalendarDate extends NSDate {

public NSCalendarDate() {
	super();
}

public NSCalendarDate(int id) {
	super(id);
}

public static NSCalendarDate calendarDate() {
	int result = OS.objc_msgSend(OS.class_NSCalendarDate, OS.sel_calendarDate);
	return result != 0 ? new NSCalendarDate(result) : null;
}

public NSString calendarFormat() {
	int result = OS.objc_msgSend(this.id, OS.sel_calendarFormat);
	return result != 0 ? new NSString(result) : null;
}

public NSCalendarDate dateByAddingYears(int year, int month, int day, int hour, int minute, int second) {
	int result = OS.objc_msgSend(this.id, OS.sel_dateByAddingYears_1months_1days_1hours_1minutes_1seconds_1, year, month, day, hour, minute, second);
	return result == this.id ? this : (result != 0 ? new NSCalendarDate(result) : null);
}

public static id static_dateWithString_calendarFormat_(NSString description, NSString format) {
	int result = OS.objc_msgSend(OS.class_NSCalendarDate, OS.sel_dateWithString_1calendarFormat_1, description != null ? description.id : 0, format != null ? format.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_dateWithString_calendarFormat_locale_(NSString description, NSString format, id locale) {
	int result = OS.objc_msgSend(OS.class_NSCalendarDate, OS.sel_dateWithString_1calendarFormat_1locale_1, description != null ? description.id : 0, format != null ? format.id : 0, locale != null ? locale.id : 0);
	return result != 0 ? new id(result) : null;
}

public static NSCalendarDate dateWithYear(int year, int month, int day, int hour, int minute, int second, NSTimeZone aTimeZone) {
	int result = OS.objc_msgSend(OS.class_NSCalendarDate, OS.sel_dateWithYear_1month_1day_1hour_1minute_1second_1timeZone_1, year, month, day, hour, minute, second, aTimeZone != null ? aTimeZone.id : 0);
	return result != 0 ? new NSCalendarDate(result) : null;
}

public int dayOfCommonEra() {
	return OS.objc_msgSend(this.id, OS.sel_dayOfCommonEra);
}

public int dayOfMonth() {
	return OS.objc_msgSend(this.id, OS.sel_dayOfMonth);
}

public int dayOfWeek() {
	return OS.objc_msgSend(this.id, OS.sel_dayOfWeek);
}

public int dayOfYear() {
	return OS.objc_msgSend(this.id, OS.sel_dayOfYear);
}

public NSString description() {
	int result = OS.objc_msgSend(this.id, OS.sel_description);
	return result != 0 ? new NSString(result) : null;
}

public NSString descriptionWithCalendarFormat_(NSString format) {
	int result = OS.objc_msgSend(this.id, OS.sel_descriptionWithCalendarFormat_1, format != null ? format.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSString descriptionWithCalendarFormat_locale_(NSString format, id locale) {
	int result = OS.objc_msgSend(this.id, OS.sel_descriptionWithCalendarFormat_1locale_1, format != null ? format.id : 0, locale != null ? locale.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSString descriptionWithLocale(id locale) {
	int result = OS.objc_msgSend(this.id, OS.sel_descriptionWithLocale_1, locale != null ? locale.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public int hourOfDay() {
	return OS.objc_msgSend(this.id, OS.sel_hourOfDay);
}

public NSCalendarDate initWithString_(NSString description) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithString_1, description != null ? description.id : 0);
	return result != 0 ? this : null;
}

public NSCalendarDate initWithString_calendarFormat_(NSString description, NSString format) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithString_1calendarFormat_1, description != null ? description.id : 0, format != null ? format.id : 0);
	return result != 0 ? this : null;
}

public NSCalendarDate initWithString_calendarFormat_locale_(NSString description, NSString format, id locale) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithString_1calendarFormat_1locale_1, description != null ? description.id : 0, format != null ? format.id : 0, locale != null ? locale.id : 0);
	return result != 0 ? this : null;
}

public id initWithYear(int year, int month, int day, int hour, int minute, int second, NSTimeZone aTimeZone) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithYear_1month_1day_1hour_1minute_1second_1timeZone_1, year, month, day, hour, minute, second, aTimeZone != null ? aTimeZone.id : 0);
	return result != 0 ? new id(result) : null;
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

public void setCalendarFormat(NSString format) {
	OS.objc_msgSend(this.id, OS.sel_setCalendarFormat_1, format != null ? format.id : 0);
}

public void setTimeZone(NSTimeZone aTimeZone) {
	OS.objc_msgSend(this.id, OS.sel_setTimeZone_1, aTimeZone != null ? aTimeZone.id : 0);
}

public NSTimeZone timeZone() {
	int result = OS.objc_msgSend(this.id, OS.sel_timeZone);
	return result != 0 ? new NSTimeZone(result) : null;
}

public int yearOfCommonEra() {
	return OS.objc_msgSend(this.id, OS.sel_yearOfCommonEra);
}

public void years(int yp, int mop, int dp, int hp, int mip, int sp, NSCalendarDate date) {
	OS.objc_msgSend(this.id, OS.sel_years_1months_1days_1hours_1minutes_1seconds_1sinceDate_1, yp, mop, dp, hp, mip, sp, date != null ? date.id : 0);
}

}
