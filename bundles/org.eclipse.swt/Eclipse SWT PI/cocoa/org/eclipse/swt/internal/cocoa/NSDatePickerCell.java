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

public class NSDatePickerCell extends NSActionCell {

public NSDatePickerCell() {
	super();
}

public NSDatePickerCell(int id) {
	super(id);
}

public NSColor backgroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public NSCalendar calendar() {
	int result = OS.objc_msgSend(this.id, OS.sel_calendar);
	return result != 0 ? new NSCalendar(result) : null;
}

public int datePickerElements() {
	return OS.objc_msgSend(this.id, OS.sel_datePickerElements);
}

public int datePickerMode() {
	return OS.objc_msgSend(this.id, OS.sel_datePickerMode);
}

public int datePickerStyle() {
	return OS.objc_msgSend(this.id, OS.sel_datePickerStyle);
}

public NSDate dateValue() {
	int result = OS.objc_msgSend(this.id, OS.sel_dateValue);
	return result != 0 ? new NSDate(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public boolean drawsBackground() {
	return OS.objc_msgSend(this.id, OS.sel_drawsBackground) != 0;
}

public NSLocale locale() {
	int result = OS.objc_msgSend(this.id, OS.sel_locale);
	return result != 0 ? new NSLocale(result) : null;
}

public NSDate maxDate() {
	int result = OS.objc_msgSend(this.id, OS.sel_maxDate);
	return result != 0 ? new NSDate(result) : null;
}

public NSDate minDate() {
	int result = OS.objc_msgSend(this.id, OS.sel_minDate);
	return result != 0 ? new NSDate(result) : null;
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_1, color != null ? color.id : 0);
}

public void setCalendar(NSCalendar newCalendar) {
	OS.objc_msgSend(this.id, OS.sel_setCalendar_1, newCalendar != null ? newCalendar.id : 0);
}

public void setDatePickerElements(int elementFlags) {
	OS.objc_msgSend(this.id, OS.sel_setDatePickerElements_1, elementFlags);
}

public void setDatePickerMode(int newMode) {
	OS.objc_msgSend(this.id, OS.sel_setDatePickerMode_1, newMode);
}

public void setDatePickerStyle(int newStyle) {
	OS.objc_msgSend(this.id, OS.sel_setDatePickerStyle_1, newStyle);
}

public void setDateValue(NSDate newStartDate) {
	OS.objc_msgSend(this.id, OS.sel_setDateValue_1, newStartDate != null ? newStartDate.id : 0);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

public void setDrawsBackground(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_1, flag);
}

public void setLocale(NSLocale newLocale) {
	OS.objc_msgSend(this.id, OS.sel_setLocale_1, newLocale != null ? newLocale.id : 0);
}

public void setMaxDate(NSDate date) {
	OS.objc_msgSend(this.id, OS.sel_setMaxDate_1, date != null ? date.id : 0);
}

public void setMinDate(NSDate date) {
	OS.objc_msgSend(this.id, OS.sel_setMinDate_1, date != null ? date.id : 0);
}

public void setTextColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setTextColor_1, color != null ? color.id : 0);
}

public void setTimeInterval(double newTimeInterval) {
	OS.objc_msgSend(this.id, OS.sel_setTimeInterval_1, newTimeInterval);
}

public void setTimeZone(NSTimeZone newTimeZone) {
	OS.objc_msgSend(this.id, OS.sel_setTimeZone_1, newTimeZone != null ? newTimeZone.id : 0);
}

public NSColor textColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_textColor);
	return result != 0 ? new NSColor(result) : null;
}

public double timeInterval() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_timeInterval);
}

public NSTimeZone timeZone() {
	int result = OS.objc_msgSend(this.id, OS.sel_timeZone);
	return result != 0 ? new NSTimeZone(result) : null;
}

}
