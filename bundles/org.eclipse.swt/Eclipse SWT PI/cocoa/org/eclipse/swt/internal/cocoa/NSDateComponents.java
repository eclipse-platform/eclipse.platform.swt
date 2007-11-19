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

public class NSDateComponents extends NSObject {

public NSDateComponents() {
	super();
}

public NSDateComponents(int id) {
	super(id);
}

public int day() {
	return OS.objc_msgSend(this.id, OS.sel_day);
}

public int era() {
	return OS.objc_msgSend(this.id, OS.sel_era);
}

public int hour() {
	return OS.objc_msgSend(this.id, OS.sel_hour);
}

public int minute() {
	return OS.objc_msgSend(this.id, OS.sel_minute);
}

public int month() {
	return OS.objc_msgSend(this.id, OS.sel_month);
}

public int second() {
	return OS.objc_msgSend(this.id, OS.sel_second);
}

public void setDay(int v) {
	OS.objc_msgSend(this.id, OS.sel_setDay_1, v);
}

public void setEra(int v) {
	OS.objc_msgSend(this.id, OS.sel_setEra_1, v);
}

public void setHour(int v) {
	OS.objc_msgSend(this.id, OS.sel_setHour_1, v);
}

public void setMinute(int v) {
	OS.objc_msgSend(this.id, OS.sel_setMinute_1, v);
}

public void setMonth(int v) {
	OS.objc_msgSend(this.id, OS.sel_setMonth_1, v);
}

public void setSecond(int v) {
	OS.objc_msgSend(this.id, OS.sel_setSecond_1, v);
}

public void setWeek(int v) {
	OS.objc_msgSend(this.id, OS.sel_setWeek_1, v);
}

public void setWeekday(int v) {
	OS.objc_msgSend(this.id, OS.sel_setWeekday_1, v);
}

public void setWeekdayOrdinal(int v) {
	OS.objc_msgSend(this.id, OS.sel_setWeekdayOrdinal_1, v);
}

public void setYear(int v) {
	OS.objc_msgSend(this.id, OS.sel_setYear_1, v);
}

public int week() {
	return OS.objc_msgSend(this.id, OS.sel_week);
}

public int weekday() {
	return OS.objc_msgSend(this.id, OS.sel_weekday);
}

public int weekdayOrdinal() {
	return OS.objc_msgSend(this.id, OS.sel_weekdayOrdinal);
}

public int year() {
	return OS.objc_msgSend(this.id, OS.sel_year);
}

}
