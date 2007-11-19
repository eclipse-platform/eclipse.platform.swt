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

public class NSDate extends NSObject {

public NSDate() {
	super();
}

public NSDate(int id) {
	super(id);
}

public id addTimeInterval(double seconds) {
	int result = OS.objc_msgSend(this.id, OS.sel_addTimeInterval_1, seconds);
	return result != 0 ? new id(result) : null;
}

public int compare(NSDate other) {
	return OS.objc_msgSend(this.id, OS.sel_compare_1, other != null ? other.id : 0);
}

public static NSDate date() {
	int result = OS.objc_msgSend(OS.class_NSDate, OS.sel_date);
	return result != 0 ? new NSDate(result) : null;
}

public NSCalendarDate dateWithCalendarFormat(NSString format, NSTimeZone aTimeZone) {
	int result = OS.objc_msgSend(this.id, OS.sel_dateWithCalendarFormat_1timeZone_1, format != null ? format.id : 0, aTimeZone != null ? aTimeZone.id : 0);
	return result != 0 ? new NSCalendarDate(result) : null;
}

public static id static_dateWithNaturalLanguageString_(NSString string) {
	int result = OS.objc_msgSend(OS.class_NSDate, OS.sel_dateWithNaturalLanguageString_1, string != null ? string.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_dateWithNaturalLanguageString_locale_(NSString string, id locale) {
	int result = OS.objc_msgSend(OS.class_NSDate, OS.sel_dateWithNaturalLanguageString_1locale_1, string != null ? string.id : 0, locale != null ? locale.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id dateWithString(NSString aString) {
	int result = OS.objc_msgSend(OS.class_NSDate, OS.sel_dateWithString_1, aString != null ? aString.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id dateWithTimeIntervalSince1970(double secs) {
	int result = OS.objc_msgSend(OS.class_NSDate, OS.sel_dateWithTimeIntervalSince1970_1, secs);
	return result != 0 ? new id(result) : null;
}

public static NSDate dateWithTimeIntervalSinceNow(double secs) {
	int result = OS.objc_msgSend(OS.class_NSDate, OS.sel_dateWithTimeIntervalSinceNow_1, secs);
	return result != 0 ? new NSDate(result) : null;
}

public static id dateWithTimeIntervalSinceReferenceDate(double secs) {
	int result = OS.objc_msgSend(OS.class_NSDate, OS.sel_dateWithTimeIntervalSinceReferenceDate_1, secs);
	return result != 0 ? new id(result) : null;
}

public NSString description() {
	int result = OS.objc_msgSend(this.id, OS.sel_description);
	return result != 0 ? new NSString(result) : null;
}

public NSString descriptionWithCalendarFormat(NSString format, NSTimeZone aTimeZone, id locale) {
	int result = OS.objc_msgSend(this.id, OS.sel_descriptionWithCalendarFormat_1timeZone_1locale_1, format != null ? format.id : 0, aTimeZone != null ? aTimeZone.id : 0, locale != null ? locale.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSString descriptionWithLocale(id locale) {
	int result = OS.objc_msgSend(this.id, OS.sel_descriptionWithLocale_1, locale != null ? locale.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public static NSDate distantFuture() {
	int result = OS.objc_msgSend(OS.class_NSDate, OS.sel_distantFuture);
	return result != 0 ? new NSDate(result) : null;
}

public static NSDate distantPast() {
	int result = OS.objc_msgSend(OS.class_NSDate, OS.sel_distantPast);
	return result != 0 ? new NSDate(result) : null;
}

public NSDate earlierDate(NSDate anotherDate) {
	int result = OS.objc_msgSend(this.id, OS.sel_earlierDate_1, anotherDate != null ? anotherDate.id : 0);
	return result == this.id ? this : (result != 0 ? new NSDate(result) : null);
}

public NSDate initWithString(NSString description) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithString_1, description != null ? description.id : 0);
	return result != 0 ? this : null;
}

public NSDate initWithTimeInterval(double secsToBeAdded, NSDate anotherDate) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithTimeInterval_1sinceDate_1, secsToBeAdded, anotherDate != null ? anotherDate.id : 0);
	return result != 0 ? this : null;
}

public NSDate initWithTimeIntervalSinceNow(double secsToBeAddedToNow) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithTimeIntervalSinceNow_1, secsToBeAddedToNow);
	return result != 0 ? this : null;
}

public NSDate initWithTimeIntervalSinceReferenceDate(double secsToBeAdded) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithTimeIntervalSinceReferenceDate_1, secsToBeAdded);
	return result != 0 ? this : null;
}

public boolean isEqualToDate(NSDate otherDate) {
	return OS.objc_msgSend(this.id, OS.sel_isEqualToDate_1, otherDate != null ? otherDate.id : 0) != 0;
}

public NSDate laterDate(NSDate anotherDate) {
	int result = OS.objc_msgSend(this.id, OS.sel_laterDate_1, anotherDate != null ? anotherDate.id : 0);
	return result == this.id ? this : (result != 0 ? new NSDate(result) : null);
}

public double timeIntervalSince1970() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_timeIntervalSince1970);
}

public double timeIntervalSinceDate(NSDate anotherDate) {
	return OS.objc_msgSend_fpret(this.id, OS.sel_timeIntervalSinceDate_1, anotherDate != null ? anotherDate.id : 0);
}

public double timeIntervalSinceNow() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_timeIntervalSinceNow);
}

public static double static_timeIntervalSinceReferenceDate() {
	return OS.objc_msgSend_fpret(OS.class_NSDate, OS.sel_timeIntervalSinceReferenceDate);
}

public double timeIntervalSinceReferenceDate() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_timeIntervalSinceReferenceDate);
}

}
