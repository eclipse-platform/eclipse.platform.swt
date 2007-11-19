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

public class NSNetServiceBrowser extends NSObject {

public NSNetServiceBrowser() {
	super();
}

public NSNetServiceBrowser(int id) {
	super(id);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void removeFromRunLoop(NSRunLoop aRunLoop, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_removeFromRunLoop_1forMode_1, aRunLoop != null ? aRunLoop.id : 0, mode != null ? mode.id : 0);
}

public void scheduleInRunLoop(NSRunLoop aRunLoop, NSString mode) {
	OS.objc_msgSend(this.id, OS.sel_scheduleInRunLoop_1forMode_1, aRunLoop != null ? aRunLoop.id : 0, mode != null ? mode.id : 0);
}

public void searchForAllDomains() {
	OS.objc_msgSend(this.id, OS.sel_searchForAllDomains);
}

public void searchForBrowsableDomains() {
	OS.objc_msgSend(this.id, OS.sel_searchForBrowsableDomains);
}

public void searchForRegistrationDomains() {
	OS.objc_msgSend(this.id, OS.sel_searchForRegistrationDomains);
}

public void searchForServicesOfType(NSString type, NSString domainString) {
	OS.objc_msgSend(this.id, OS.sel_searchForServicesOfType_1inDomain_1, type != null ? type.id : 0, domainString != null ? domainString.id : 0);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public void stop() {
	OS.objc_msgSend(this.id, OS.sel_stop);
}

}
