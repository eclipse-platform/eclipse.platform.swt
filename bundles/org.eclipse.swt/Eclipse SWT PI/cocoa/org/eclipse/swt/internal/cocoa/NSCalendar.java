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

public class NSCalendar extends NSObject {

public NSCalendar() {
	super();
}

public NSCalendar(int id) {
	super(id);
}

public static id autoupdatingCurrentCalendar() {
	int result = OS.objc_msgSend(OS.class_NSCalendar, OS.sel_autoupdatingCurrentCalendar);
	return result != 0 ? new id(result) : null;
}

public NSString calendarIdentifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_calendarIdentifier);
	return result != 0 ? new NSString(result) : null;
}

public NSDateComponents components_fromDate_(int unitFlags, NSDate date) {
	int result = OS.objc_msgSend(this.id, OS.sel_components_1fromDate_1, unitFlags, date != null ? date.id : 0);
	return result != 0 ? new NSDateComponents(result) : null;
}

public NSDateComponents components_fromDate_toDate_options_(int unitFlags, NSDate startingDate, NSDate resultDate, int opts) {
	int result = OS.objc_msgSend(this.id, OS.sel_components_1fromDate_1toDate_1options_1, unitFlags, startingDate != null ? startingDate.id : 0, resultDate != null ? resultDate.id : 0, opts);
	return result != 0 ? new NSDateComponents(result) : null;
}

public static id currentCalendar() {
	int result = OS.objc_msgSend(OS.class_NSCalendar, OS.sel_currentCalendar);
	return result != 0 ? new id(result) : null;
}

public NSDate dateByAddingComponents(NSDateComponents comps, NSDate date, int opts) {
	int result = OS.objc_msgSend(this.id, OS.sel_dateByAddingComponents_1toDate_1options_1, comps != null ? comps.id : 0, date != null ? date.id : 0, opts);
	return result != 0 ? new NSDate(result) : null;
}

public NSDate dateFromComponents(NSDateComponents comps) {
	int result = OS.objc_msgSend(this.id, OS.sel_dateFromComponents_1, comps != null ? comps.id : 0);
	return result != 0 ? new NSDate(result) : null;
}

public int firstWeekday() {
	return OS.objc_msgSend(this.id, OS.sel_firstWeekday);
}

public NSCalendar initWithCalendarIdentifier(NSString ident) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCalendarIdentifier_1, ident != null ? ident.id : 0);
	return result != 0 ? this : null;
}

public NSLocale locale() {
	int result = OS.objc_msgSend(this.id, OS.sel_locale);
	return result != 0 ? new NSLocale(result) : null;
}

public NSRange maximumRangeOfUnit(int unit) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_maximumRangeOfUnit_1, unit);
	return result;
}

public int minimumDaysInFirstWeek() {
	return OS.objc_msgSend(this.id, OS.sel_minimumDaysInFirstWeek);
}

public NSRange minimumRangeOfUnit(int unit) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_minimumRangeOfUnit_1, unit);
	return result;
}

public int ordinalityOfUnit(int smaller, int larger, NSDate date) {
	return OS.objc_msgSend(this.id, OS.sel_ordinalityOfUnit_1inUnit_1forDate_1, smaller, larger, date != null ? date.id : 0);
}

public NSRange rangeOfUnit_inUnit_forDate_(int smaller, int larger, NSDate date) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rangeOfUnit_1inUnit_1forDate_1, smaller, larger, date != null ? date.id : 0);
	return result;
}

public boolean rangeOfUnit_startDate_interval_forDate_(int unit, int datep, int tip, NSDate date) {
	return OS.objc_msgSend(this.id, OS.sel_rangeOfUnit_1startDate_1interval_1forDate_1, unit, datep, tip, date != null ? date.id : 0) != 0;
}

public void setFirstWeekday(int weekday) {
	OS.objc_msgSend(this.id, OS.sel_setFirstWeekday_1, weekday);
}

public void setLocale(NSLocale locale) {
	OS.objc_msgSend(this.id, OS.sel_setLocale_1, locale != null ? locale.id : 0);
}

public void setMinimumDaysInFirstWeek(int mdw) {
	OS.objc_msgSend(this.id, OS.sel_setMinimumDaysInFirstWeek_1, mdw);
}

public void setTimeZone(NSTimeZone tz) {
	OS.objc_msgSend(this.id, OS.sel_setTimeZone_1, tz != null ? tz.id : 0);
}

public NSTimeZone timeZone() {
	int result = OS.objc_msgSend(this.id, OS.sel_timeZone);
	return result != 0 ? new NSTimeZone(result) : null;
}

}
