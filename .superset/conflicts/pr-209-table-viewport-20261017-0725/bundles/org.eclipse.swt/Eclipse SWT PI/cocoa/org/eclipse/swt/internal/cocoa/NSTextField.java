/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSTextField extends NSControl {

public NSTextField() {
	super();
}

public NSTextField(long id) {
	super(id);
}

public NSTextField(id id) {
	super(id);
}

public void selectText(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectText_, sender != null ? sender.id : 0);
}

public void setBackgroundColor(NSColor backgroundColor) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_, backgroundColor != null ? backgroundColor.id : 0);
}

public void setBordered(boolean bordered) {
	OS.objc_msgSend(this.id, OS.sel_setBordered_, bordered);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_, delegate != null ? delegate.id : 0);
}

public void setDrawsBackground(boolean drawsBackground) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_, drawsBackground);
}

public void setEditable(boolean editable) {
	OS.objc_msgSend(this.id, OS.sel_setEditable_, editable);
}

public void setSelectable(boolean selectable) {
	OS.objc_msgSend(this.id, OS.sel_setSelectable_, selectable);
}

public void setTextColor(NSColor textColor) {
	OS.objc_msgSend(this.id, OS.sel_setTextColor_, textColor != null ? textColor.id : 0);
}

public static long cellClass() {
	return OS.objc_msgSend(OS.class_NSTextField, OS.sel_cellClass);
}

public static void setCellClass(long factoryId) {
	OS.objc_msgSend(OS.class_NSTextField, OS.sel_setCellClass_, factoryId);
}

}
