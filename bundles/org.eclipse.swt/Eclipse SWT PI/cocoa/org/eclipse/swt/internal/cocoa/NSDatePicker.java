/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSDatePicker extends NSControl {

public NSDatePicker() {
	super();
}

public NSDatePicker(long /*int*/ id) {
	super(id);
}

public NSDatePicker(id id) {
	super(id);
}

public NSDate dateValue() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_dateValue);
	return result != 0 ? new NSDate(result) : null;
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_, color != null ? color.id : 0);
}

public void setBezeled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBezeled_, flag);
}

public void setBordered(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBordered_, flag);
}

public void setDatePickerElements(long /*int*/ elementFlags) {
	OS.objc_msgSend(this.id, OS.sel_setDatePickerElements_, elementFlags);
}

public void setDatePickerStyle(long /*int*/ newStyle) {
	OS.objc_msgSend(this.id, OS.sel_setDatePickerStyle_, newStyle);
}

public void setDateValue(NSDate newStartDate) {
	OS.objc_msgSend(this.id, OS.sel_setDateValue_, newStartDate != null ? newStartDate.id : 0);
}

public void setDrawsBackground(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_, flag);
}

public void setTextColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setTextColor_, color != null ? color.id : 0);
}

public static long /*int*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSDatePicker, OS.sel_cellClass);
}

public static void setCellClass(long /*int*/ factoryId) {
	OS.objc_msgSend(OS.class_NSDatePicker, OS.sel_setCellClass_, factoryId);
}

}
