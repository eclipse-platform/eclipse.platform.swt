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

public class NSDateFormatter extends NSFormatter {

public NSDateFormatter() {
	super();
}

public NSDateFormatter(int id) {
	super(id);
}

public NSString AMSymbol() {
	int result = OS.objc_msgSend(this.id, OS.sel_AMSymbol);
	return result != 0 ? new NSString(result) : null;
}

public NSString PMSymbol() {
	int result = OS.objc_msgSend(this.id, OS.sel_PMSymbol);
	return result != 0 ? new NSString(result) : null;
}

public boolean allowsNaturalLanguage() {
	return OS.objc_msgSend(this.id, OS.sel_allowsNaturalLanguage) != 0;
}

public NSCalendar calendar() {
	int result = OS.objc_msgSend(this.id, OS.sel_calendar);
	return result != 0 ? new NSCalendar(result) : null;
}

public NSString dateFormat() {
	int result = OS.objc_msgSend(this.id, OS.sel_dateFormat);
	return result != 0 ? new NSString(result) : null;
}

public NSDate dateFromString(NSString string) {
	int result = OS.objc_msgSend(this.id, OS.sel_dateFromString_1, string != null ? string.id : 0);
	return result != 0 ? new NSDate(result) : null;
}

public int dateStyle() {
	return OS.objc_msgSend(this.id, OS.sel_dateStyle);
}

public NSDate defaultDate() {
	int result = OS.objc_msgSend(this.id, OS.sel_defaultDate);
	return result != 0 ? new NSDate(result) : null;
}

public static int defaultFormatterBehavior() {
	return OS.objc_msgSend(OS.class_NSDateFormatter, OS.sel_defaultFormatterBehavior);
}

public NSArray eraSymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_eraSymbols);
	return result != 0 ? new NSArray(result) : null;
}

public int formatterBehavior() {
	return OS.objc_msgSend(this.id, OS.sel_formatterBehavior);
}

public boolean generatesCalendarDates() {
	return OS.objc_msgSend(this.id, OS.sel_generatesCalendarDates) != 0;
}

public boolean getObjectValue(int obj, NSString string, int rangep, int error) {
	return OS.objc_msgSend(this.id, OS.sel_getObjectValue_1forString_1range_1error_1, obj, string != null ? string.id : 0, rangep, error) != 0;
}

public NSDate gregorianStartDate() {
	int result = OS.objc_msgSend(this.id, OS.sel_gregorianStartDate);
	return result != 0 ? new NSDate(result) : null;
}

public id initWithDateFormat(NSString format, boolean flag) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithDateFormat_1allowNaturalLanguage_1, format != null ? format.id : 0, flag);
	return result != 0 ? new id(result) : null;
}

public boolean isLenient() {
	return OS.objc_msgSend(this.id, OS.sel_isLenient) != 0;
}

public NSLocale locale() {
	int result = OS.objc_msgSend(this.id, OS.sel_locale);
	return result != 0 ? new NSLocale(result) : null;
}

public NSArray longEraSymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_longEraSymbols);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray monthSymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_monthSymbols);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray quarterSymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_quarterSymbols);
	return result != 0 ? new NSArray(result) : null;
}

public void setAMSymbol(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setAMSymbol_1, string != null ? string.id : 0);
}

public void setCalendar(NSCalendar calendar) {
	OS.objc_msgSend(this.id, OS.sel_setCalendar_1, calendar != null ? calendar.id : 0);
}

public void setDateFormat(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setDateFormat_1, string != null ? string.id : 0);
}

public void setDateStyle(int style) {
	OS.objc_msgSend(this.id, OS.sel_setDateStyle_1, style);
}

public void setDefaultDate(NSDate date) {
	OS.objc_msgSend(this.id, OS.sel_setDefaultDate_1, date != null ? date.id : 0);
}

public static void setDefaultFormatterBehavior(int behavior) {
	OS.objc_msgSend(OS.class_NSDateFormatter, OS.sel_setDefaultFormatterBehavior_1, behavior);
}

public void setEraSymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setEraSymbols_1, array != null ? array.id : 0);
}

public void setFormatterBehavior(int behavior) {
	OS.objc_msgSend(this.id, OS.sel_setFormatterBehavior_1, behavior);
}

public void setGeneratesCalendarDates(boolean b) {
	OS.objc_msgSend(this.id, OS.sel_setGeneratesCalendarDates_1, b);
}

public void setGregorianStartDate(NSDate date) {
	OS.objc_msgSend(this.id, OS.sel_setGregorianStartDate_1, date != null ? date.id : 0);
}

public void setLenient(boolean b) {
	OS.objc_msgSend(this.id, OS.sel_setLenient_1, b);
}

public void setLocale(NSLocale locale) {
	OS.objc_msgSend(this.id, OS.sel_setLocale_1, locale != null ? locale.id : 0);
}

public void setLongEraSymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setLongEraSymbols_1, array != null ? array.id : 0);
}

public void setMonthSymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setMonthSymbols_1, array != null ? array.id : 0);
}

public void setPMSymbol(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setPMSymbol_1, string != null ? string.id : 0);
}

public void setQuarterSymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setQuarterSymbols_1, array != null ? array.id : 0);
}

public void setShortMonthSymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setShortMonthSymbols_1, array != null ? array.id : 0);
}

public void setShortQuarterSymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setShortQuarterSymbols_1, array != null ? array.id : 0);
}

public void setShortStandaloneMonthSymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setShortStandaloneMonthSymbols_1, array != null ? array.id : 0);
}

public void setShortStandaloneQuarterSymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setShortStandaloneQuarterSymbols_1, array != null ? array.id : 0);
}

public void setShortStandaloneWeekdaySymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setShortStandaloneWeekdaySymbols_1, array != null ? array.id : 0);
}

public void setShortWeekdaySymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setShortWeekdaySymbols_1, array != null ? array.id : 0);
}

public void setStandaloneMonthSymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setStandaloneMonthSymbols_1, array != null ? array.id : 0);
}

public void setStandaloneQuarterSymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setStandaloneQuarterSymbols_1, array != null ? array.id : 0);
}

public void setStandaloneWeekdaySymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setStandaloneWeekdaySymbols_1, array != null ? array.id : 0);
}

public void setTimeStyle(int style) {
	OS.objc_msgSend(this.id, OS.sel_setTimeStyle_1, style);
}

public void setTimeZone(NSTimeZone tz) {
	OS.objc_msgSend(this.id, OS.sel_setTimeZone_1, tz != null ? tz.id : 0);
}

public void setTwoDigitStartDate(NSDate date) {
	OS.objc_msgSend(this.id, OS.sel_setTwoDigitStartDate_1, date != null ? date.id : 0);
}

public void setVeryShortMonthSymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setVeryShortMonthSymbols_1, array != null ? array.id : 0);
}

public void setVeryShortStandaloneMonthSymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setVeryShortStandaloneMonthSymbols_1, array != null ? array.id : 0);
}

public void setVeryShortStandaloneWeekdaySymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setVeryShortStandaloneWeekdaySymbols_1, array != null ? array.id : 0);
}

public void setVeryShortWeekdaySymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setVeryShortWeekdaySymbols_1, array != null ? array.id : 0);
}

public void setWeekdaySymbols(NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_setWeekdaySymbols_1, array != null ? array.id : 0);
}

public NSArray shortMonthSymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_shortMonthSymbols);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray shortQuarterSymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_shortQuarterSymbols);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray shortStandaloneMonthSymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_shortStandaloneMonthSymbols);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray shortStandaloneQuarterSymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_shortStandaloneQuarterSymbols);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray shortStandaloneWeekdaySymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_shortStandaloneWeekdaySymbols);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray shortWeekdaySymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_shortWeekdaySymbols);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray standaloneMonthSymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_standaloneMonthSymbols);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray standaloneQuarterSymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_standaloneQuarterSymbols);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray standaloneWeekdaySymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_standaloneWeekdaySymbols);
	return result != 0 ? new NSArray(result) : null;
}

public NSString stringFromDate(NSDate date) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringFromDate_1, date != null ? date.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public int timeStyle() {
	return OS.objc_msgSend(this.id, OS.sel_timeStyle);
}

public NSTimeZone timeZone() {
	int result = OS.objc_msgSend(this.id, OS.sel_timeZone);
	return result != 0 ? new NSTimeZone(result) : null;
}

public NSDate twoDigitStartDate() {
	int result = OS.objc_msgSend(this.id, OS.sel_twoDigitStartDate);
	return result != 0 ? new NSDate(result) : null;
}

public NSArray veryShortMonthSymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_veryShortMonthSymbols);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray veryShortStandaloneMonthSymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_veryShortStandaloneMonthSymbols);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray veryShortStandaloneWeekdaySymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_veryShortStandaloneWeekdaySymbols);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray veryShortWeekdaySymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_veryShortWeekdaySymbols);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray weekdaySymbols() {
	int result = OS.objc_msgSend(this.id, OS.sel_weekdaySymbols);
	return result != 0 ? new NSArray(result) : null;
}

}
