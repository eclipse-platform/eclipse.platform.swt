/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSTextField extends NSControl {

public NSTextField() {
	super();
}

public NSTextField(long /*int*/ id) {
	super(id);
}

public NSTextField(id id) {
	super(id);
}

public void selectText(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectText_, sender != null ? sender.id : 0);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_, color != null ? color.id : 0);
}

public void setBordered(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBordered_, flag);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_, anObject != null ? anObject.id : 0);
}

public void setDrawsBackground(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_, flag);
}

public void setEditable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEditable_, flag);
}

public void setSelectable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSelectable_, flag);
}

public void setTextColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setTextColor_, color != null ? color.id : 0);
}

public static long /*int*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSTextField, OS.sel_cellClass);
}

public static void setCellClass(long /*int*/ factoryId) {
	OS.objc_msgSend(OS.class_NSTextField, OS.sel_setCellClass_, factoryId);
}

}
