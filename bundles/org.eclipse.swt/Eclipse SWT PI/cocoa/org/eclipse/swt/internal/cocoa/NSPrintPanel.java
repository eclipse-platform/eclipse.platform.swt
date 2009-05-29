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

public class NSPrintPanel extends NSObject {

public NSPrintPanel() {
	super();
}

public NSPrintPanel(int /*long*/ id) {
	super(id);
}

public NSPrintPanel(id id) {
	super(id);
}

public void beginSheetWithPrintInfo(NSPrintInfo printInfo, NSWindow docWindow, id delegate, int /*long*/ didEndSelector, int /*long*/ contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_beginSheetWithPrintInfo_modalForWindow_delegate_didEndSelector_contextInfo_, printInfo != null ? printInfo.id : 0, docWindow != null ? docWindow.id : 0, delegate != null ? delegate.id : 0, didEndSelector, contextInfo);
}

public int /*long*/ options() {
	return OS.objc_msgSend(this.id, OS.sel_options);
}

public static NSPrintPanel printPanel() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSPrintPanel, OS.sel_printPanel);
	return result != 0 ? new NSPrintPanel(result) : null;
}

public int /*long*/ runModalWithPrintInfo(NSPrintInfo printInfo) {
	return OS.objc_msgSend(this.id, OS.sel_runModalWithPrintInfo_, printInfo != null ? printInfo.id : 0);
}

public void setOptions(int /*long*/ options) {
	OS.objc_msgSend(this.id, OS.sel_setOptions_, options);
}

}
