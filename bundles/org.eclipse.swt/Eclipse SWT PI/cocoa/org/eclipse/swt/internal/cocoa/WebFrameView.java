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

public class WebFrameView extends NSObject {

public WebFrameView() {
	super();
}

public WebFrameView(long /*int*/ id) {
	super(id);
}

public WebFrameView(id id) {
	super(id);
}

public boolean documentViewShouldHandlePrint() {
	return OS.objc_msgSend_bool(this.id, OS.sel_documentViewShouldHandlePrint);
}

public void printDocumentView() {
	OS.objc_msgSend(this.id, OS.sel_printDocumentView);
}

public NSPrintOperation printOperationWithPrintInfo(NSPrintInfo printInfo) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_printOperationWithPrintInfo_, printInfo != null ? printInfo.id : 0);
	return result != 0 ? new NSPrintOperation(result) : null;
}

}
