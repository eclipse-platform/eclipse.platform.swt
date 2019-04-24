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

public class WebFrameView extends NSObject {

public WebFrameView() {
	super();
}

public WebFrameView(long id) {
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
	long result = OS.objc_msgSend(this.id, OS.sel_printOperationWithPrintInfo_, printInfo != null ? printInfo.id : 0);
	return result != 0 ? new NSPrintOperation(result) : null;
}

}
