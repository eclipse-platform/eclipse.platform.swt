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

public class NSPrintOperation extends NSObject {

public NSPrintOperation() {
	super();
}

public NSPrintOperation(int id) {
	super(id);
}

public static NSPrintOperation static_EPSOperationWithView_insideRect_toData_(NSView view, NSRect rect, NSMutableData data) {
	int result = OS.objc_msgSend(OS.class_NSPrintOperation, OS.sel_EPSOperationWithView_1insideRect_1toData_1, view != null ? view.id : 0, rect, data != null ? data.id : 0);
	return result != 0 ? new NSPrintOperation(result) : null;
}

public static NSPrintOperation static_EPSOperationWithView_insideRect_toData_printInfo_(NSView view, NSRect rect, NSMutableData data, NSPrintInfo printInfo) {
	int result = OS.objc_msgSend(OS.class_NSPrintOperation, OS.sel_EPSOperationWithView_1insideRect_1toData_1printInfo_1, view != null ? view.id : 0, rect, data != null ? data.id : 0, printInfo != null ? printInfo.id : 0);
	return result != 0 ? new NSPrintOperation(result) : null;
}

public static NSPrintOperation static_EPSOperationWithView_insideRect_toPath_printInfo_(NSView view, NSRect rect, NSString path, NSPrintInfo printInfo) {
	int result = OS.objc_msgSend(OS.class_NSPrintOperation, OS.sel_EPSOperationWithView_1insideRect_1toPath_1printInfo_1, view != null ? view.id : 0, rect, path != null ? path.id : 0, printInfo != null ? printInfo.id : 0);
	return result != 0 ? new NSPrintOperation(result) : null;
}

public static NSPrintOperation static_PDFOperationWithView_insideRect_toData_(NSView view, NSRect rect, NSMutableData data) {
	int result = OS.objc_msgSend(OS.class_NSPrintOperation, OS.sel_PDFOperationWithView_1insideRect_1toData_1, view != null ? view.id : 0, rect, data != null ? data.id : 0);
	return result != 0 ? new NSPrintOperation(result) : null;
}

public static NSPrintOperation static_PDFOperationWithView_insideRect_toData_printInfo_(NSView view, NSRect rect, NSMutableData data, NSPrintInfo printInfo) {
	int result = OS.objc_msgSend(OS.class_NSPrintOperation, OS.sel_PDFOperationWithView_1insideRect_1toData_1printInfo_1, view != null ? view.id : 0, rect, data != null ? data.id : 0, printInfo != null ? printInfo.id : 0);
	return result != 0 ? new NSPrintOperation(result) : null;
}

public static NSPrintOperation static_PDFOperationWithView_insideRect_toPath_printInfo_(NSView view, NSRect rect, NSString path, NSPrintInfo printInfo) {
	int result = OS.objc_msgSend(OS.class_NSPrintOperation, OS.sel_PDFOperationWithView_1insideRect_1toPath_1printInfo_1, view != null ? view.id : 0, rect, path != null ? path.id : 0, printInfo != null ? printInfo.id : 0);
	return result != 0 ? new NSPrintOperation(result) : null;
}

public NSView accessoryView() {
	int result = OS.objc_msgSend(this.id, OS.sel_accessoryView);
	return result != 0 ? new NSView(result) : null;
}

public boolean canSpawnSeparateThread() {
	return OS.objc_msgSend(this.id, OS.sel_canSpawnSeparateThread) != 0;
}

public void cleanUpOperation() {
	OS.objc_msgSend(this.id, OS.sel_cleanUpOperation);
}

public NSGraphicsContext context() {
	int result = OS.objc_msgSend(this.id, OS.sel_context);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public NSGraphicsContext createContext() {
	int result = OS.objc_msgSend(this.id, OS.sel_createContext);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public static NSPrintOperation currentOperation() {
	int result = OS.objc_msgSend(OS.class_NSPrintOperation, OS.sel_currentOperation);
	return result != 0 ? new NSPrintOperation(result) : null;
}

public int currentPage() {
	return OS.objc_msgSend(this.id, OS.sel_currentPage);
}

public boolean deliverResult() {
	return OS.objc_msgSend(this.id, OS.sel_deliverResult) != 0;
}

public void destroyContext() {
	OS.objc_msgSend(this.id, OS.sel_destroyContext);
}

public boolean isCopyingOperation() {
	return OS.objc_msgSend(this.id, OS.sel_isCopyingOperation) != 0;
}

public NSString jobStyleHint() {
	int result = OS.objc_msgSend(this.id, OS.sel_jobStyleHint);
	return result != 0 ? new NSString(result) : null;
}

public NSString jobTitle() {
	int result = OS.objc_msgSend(this.id, OS.sel_jobTitle);
	return result != 0 ? new NSString(result) : null;
}

public int pageOrder() {
	return OS.objc_msgSend(this.id, OS.sel_pageOrder);
}

public NSRange pageRange() {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_pageRange);
	return result;
}

public NSPrintInfo printInfo() {
	int result = OS.objc_msgSend(this.id, OS.sel_printInfo);
	return result != 0 ? new NSPrintInfo(result) : null;
}

public static NSPrintOperation static_printOperationWithView_(NSView view) {
	int result = OS.objc_msgSend(OS.class_NSPrintOperation, OS.sel_printOperationWithView_1, view != null ? view.id : 0);
	return result != 0 ? new NSPrintOperation(result) : null;
}

public static NSPrintOperation static_printOperationWithView_printInfo_(NSView view, NSPrintInfo printInfo) {
	int result = OS.objc_msgSend(OS.class_NSPrintOperation, OS.sel_printOperationWithView_1printInfo_1, view != null ? view.id : 0, printInfo != null ? printInfo.id : 0);
	return result != 0 ? new NSPrintOperation(result) : null;
}

public NSPrintPanel printPanel() {
	int result = OS.objc_msgSend(this.id, OS.sel_printPanel);
	return result != 0 ? new NSPrintPanel(result) : null;
}

public boolean runOperation() {
	return OS.objc_msgSend(this.id, OS.sel_runOperation) != 0;
}

public void runOperationModalForWindow(NSWindow docWindow, id delegate, int didRunSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_runOperationModalForWindow_1delegate_1didRunSelector_1contextInfo_1, docWindow != null ? docWindow.id : 0, delegate != null ? delegate.id : 0, didRunSelector, contextInfo);
}

public void setAccessoryView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setAccessoryView_1, view != null ? view.id : 0);
}

public void setCanSpawnSeparateThread(boolean canSpawnSeparateThread) {
	OS.objc_msgSend(this.id, OS.sel_setCanSpawnSeparateThread_1, canSpawnSeparateThread);
}

public static void setCurrentOperation(NSPrintOperation operation) {
	OS.objc_msgSend(OS.class_NSPrintOperation, OS.sel_setCurrentOperation_1, operation != null ? operation.id : 0);
}

public void setJobStyleHint(NSString hint) {
	OS.objc_msgSend(this.id, OS.sel_setJobStyleHint_1, hint != null ? hint.id : 0);
}

public void setJobTitle(NSString jobTitle) {
	OS.objc_msgSend(this.id, OS.sel_setJobTitle_1, jobTitle != null ? jobTitle.id : 0);
}

public void setPageOrder(int pageOrder) {
	OS.objc_msgSend(this.id, OS.sel_setPageOrder_1, pageOrder);
}

public void setPrintInfo(NSPrintInfo printInfo) {
	OS.objc_msgSend(this.id, OS.sel_setPrintInfo_1, printInfo != null ? printInfo.id : 0);
}

public void setPrintPanel(NSPrintPanel panel) {
	OS.objc_msgSend(this.id, OS.sel_setPrintPanel_1, panel != null ? panel.id : 0);
}

public void setShowPanels(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setShowPanels_1, flag);
}

public void setShowsPrintPanel(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setShowsPrintPanel_1, flag);
}

public void setShowsProgressPanel(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setShowsProgressPanel_1, flag);
}

public boolean showPanels() {
	return OS.objc_msgSend(this.id, OS.sel_showPanels) != 0;
}

public boolean showsPrintPanel() {
	return OS.objc_msgSend(this.id, OS.sel_showsPrintPanel) != 0;
}

public boolean showsProgressPanel() {
	return OS.objc_msgSend(this.id, OS.sel_showsProgressPanel) != 0;
}

public NSView view() {
	int result = OS.objc_msgSend(this.id, OS.sel_view);
	return result != 0 ? new NSView(result) : null;
}

}
