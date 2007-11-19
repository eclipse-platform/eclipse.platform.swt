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

public class NSTimeZone extends NSObject {

public NSTimeZone() {
	super();
}

public NSTimeZone(int id) {
	super(id);
}

public NSString abbreviation() {
	int result = OS.objc_msgSend(this.id, OS.sel_abbreviation);
	return result != 0 ? new NSString(result) : null;
}

public static NSDictionary abbreviationDictionary() {
	int result = OS.objc_msgSend(OS.class_NSTimeZone, OS.sel_abbreviationDictionary);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSString abbreviationForDate(NSDate aDate) {
	int result = OS.objc_msgSend(this.id, OS.sel_abbreviationForDate_1, aDate != null ? aDate.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSData data() {
	int result = OS.objc_msgSend(this.id, OS.sel_data);
	return result != 0 ? new NSData(result) : null;
}

public double daylightSavingTimeOffset() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_daylightSavingTimeOffset);
}

public double daylightSavingTimeOffsetForDate(NSDate aDate) {
	return OS.objc_msgSend_fpret(this.id, OS.sel_daylightSavingTimeOffsetForDate_1, aDate != null ? aDate.id : 0);
}

public static NSTimeZone defaultTimeZone() {
	int result = OS.objc_msgSend(OS.class_NSTimeZone, OS.sel_defaultTimeZone);
	return result != 0 ? new NSTimeZone(result) : null;
}

public NSString description() {
	int result = OS.objc_msgSend(this.id, OS.sel_description);
	return result != 0 ? new NSString(result) : null;
}

public id initWithName_(NSString tzName) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithName_1, tzName != null ? tzName.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithName_data_(NSString tzName, NSData aData) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithName_1data_1, tzName != null ? tzName.id : 0, aData != null ? aData.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isDaylightSavingTime() {
	return OS.objc_msgSend(this.id, OS.sel_isDaylightSavingTime) != 0;
}

public boolean isDaylightSavingTimeForDate(NSDate aDate) {
	return OS.objc_msgSend(this.id, OS.sel_isDaylightSavingTimeForDate_1, aDate != null ? aDate.id : 0) != 0;
}

public boolean isEqualToTimeZone(NSTimeZone aTimeZone) {
	return OS.objc_msgSend(this.id, OS.sel_isEqualToTimeZone_1, aTimeZone != null ? aTimeZone.id : 0) != 0;
}

public static NSArray knownTimeZoneNames() {
	int result = OS.objc_msgSend(OS.class_NSTimeZone, OS.sel_knownTimeZoneNames);
	return result != 0 ? new NSArray(result) : null;
}

public static NSTimeZone localTimeZone() {
	int result = OS.objc_msgSend(OS.class_NSTimeZone, OS.sel_localTimeZone);
	return result != 0 ? new NSTimeZone(result) : null;
}

public NSString localizedName(int style, NSLocale locale) {
	int result = OS.objc_msgSend(this.id, OS.sel_localizedName_1locale_1, style, locale != null ? locale.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public NSDate nextDaylightSavingTimeTransition() {
	int result = OS.objc_msgSend(this.id, OS.sel_nextDaylightSavingTimeTransition);
	return result != 0 ? new NSDate(result) : null;
}

public NSDate nextDaylightSavingTimeTransitionAfterDate(NSDate aDate) {
	int result = OS.objc_msgSend(this.id, OS.sel_nextDaylightSavingTimeTransitionAfterDate_1, aDate != null ? aDate.id : 0);
	return result != 0 ? new NSDate(result) : null;
}

public static void resetSystemTimeZone() {
	OS.objc_msgSend(OS.class_NSTimeZone, OS.sel_resetSystemTimeZone);
}

public int secondsFromGMT() {
	return OS.objc_msgSend(this.id, OS.sel_secondsFromGMT);
}

public int secondsFromGMTForDate(NSDate aDate) {
	return OS.objc_msgSend(this.id, OS.sel_secondsFromGMTForDate_1, aDate != null ? aDate.id : 0);
}

public static void setDefaultTimeZone(NSTimeZone aTimeZone) {
	OS.objc_msgSend(OS.class_NSTimeZone, OS.sel_setDefaultTimeZone_1, aTimeZone != null ? aTimeZone.id : 0);
}

public static NSTimeZone systemTimeZone() {
	int result = OS.objc_msgSend(OS.class_NSTimeZone, OS.sel_systemTimeZone);
	return result != 0 ? new NSTimeZone(result) : null;
}

public static id timeZoneForSecondsFromGMT(int seconds) {
	int result = OS.objc_msgSend(OS.class_NSTimeZone, OS.sel_timeZoneForSecondsFromGMT_1, seconds);
	return result != 0 ? new id(result) : null;
}

public static id timeZoneWithAbbreviation(NSString abbreviation) {
	int result = OS.objc_msgSend(OS.class_NSTimeZone, OS.sel_timeZoneWithAbbreviation_1, abbreviation != null ? abbreviation.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_timeZoneWithName_(NSString tzName) {
	int result = OS.objc_msgSend(OS.class_NSTimeZone, OS.sel_timeZoneWithName_1, tzName != null ? tzName.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_timeZoneWithName_data_(NSString tzName, NSData aData) {
	int result = OS.objc_msgSend(OS.class_NSTimeZone, OS.sel_timeZoneWithName_1data_1, tzName != null ? tzName.id : 0, aData != null ? aData.id : 0);
	return result != 0 ? new id(result) : null;
}

}
