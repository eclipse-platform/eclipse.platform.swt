/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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

public class NSPrintPanel extends NSObject {

public NSPrintPanel() {
	super();
}

public NSPrintPanel(long id) {
	super(id);
}

public NSPrintPanel(id id) {
	super(id);
}

public void beginSheetWithPrintInfo(NSPrintInfo printInfo, NSWindow docWindow, id delegate, long didEndSelector, long contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_beginSheetWithPrintInfo_modalForWindow_delegate_didEndSelector_contextInfo_, printInfo != null ? printInfo.id : 0, docWindow != null ? docWindow.id : 0, delegate != null ? delegate.id : 0, didEndSelector, contextInfo);
}

public long options() {
	return OS.objc_msgSend(this.id, OS.sel_options);
}

public static NSPrintPanel printPanel() {
	long result = OS.objc_msgSend(OS.class_NSPrintPanel, OS.sel_printPanel);
	return result != 0 ? new NSPrintPanel(result) : null;
}

public long runModalWithPrintInfo(NSPrintInfo printInfo) {
	return OS.objc_msgSend(this.id, OS.sel_runModalWithPrintInfo_, printInfo != null ? printInfo.id : 0);
}

public void setOptions(long options) {
	OS.objc_msgSend(this.id, OS.sel_setOptions_, options);
}

}
