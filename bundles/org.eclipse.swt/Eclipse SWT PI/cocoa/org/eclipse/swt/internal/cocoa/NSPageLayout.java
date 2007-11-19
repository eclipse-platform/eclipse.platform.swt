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

public class NSPageLayout extends NSObject {

public NSPageLayout() {
	super();
}

public NSPageLayout(int id) {
	super(id);
}

public NSArray accessoryControllers() {
	int result = OS.objc_msgSend(this.id, OS.sel_accessoryControllers);
	return result != 0 ? new NSArray(result) : null;
}

public NSView accessoryView() {
	int result = OS.objc_msgSend(this.id, OS.sel_accessoryView);
	return result != 0 ? new NSView(result) : null;
}

public void addAccessoryController(NSViewController accessoryController) {
	OS.objc_msgSend(this.id, OS.sel_addAccessoryController_1, accessoryController != null ? accessoryController.id : 0);
}

public void beginSheetWithPrintInfo(NSPrintInfo printInfo, NSWindow docWindow, id delegate, int didEndSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_beginSheetWithPrintInfo_1modalForWindow_1delegate_1didEndSelector_1contextInfo_1, printInfo != null ? printInfo.id : 0, docWindow != null ? docWindow.id : 0, delegate != null ? delegate.id : 0, didEndSelector, contextInfo);
}

public static NSPageLayout pageLayout() {
	int result = OS.objc_msgSend(OS.class_NSPageLayout, OS.sel_pageLayout);
	return result != 0 ? new NSPageLayout(result) : null;
}

public NSPrintInfo printInfo() {
	int result = OS.objc_msgSend(this.id, OS.sel_printInfo);
	return result != 0 ? new NSPrintInfo(result) : null;
}

public void readPrintInfo() {
	OS.objc_msgSend(this.id, OS.sel_readPrintInfo);
}

public void removeAccessoryController(NSViewController accessoryController) {
	OS.objc_msgSend(this.id, OS.sel_removeAccessoryController_1, accessoryController != null ? accessoryController.id : 0);
}

public int runModal() {
	return OS.objc_msgSend(this.id, OS.sel_runModal);
}

public int runModalWithPrintInfo(NSPrintInfo printInfo) {
	return OS.objc_msgSend(this.id, OS.sel_runModalWithPrintInfo_1, printInfo != null ? printInfo.id : 0);
}

public void setAccessoryView(NSView accessoryView) {
	OS.objc_msgSend(this.id, OS.sel_setAccessoryView_1, accessoryView != null ? accessoryView.id : 0);
}

public void writePrintInfo() {
	OS.objc_msgSend(this.id, OS.sel_writePrintInfo);
}

}
