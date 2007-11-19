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

public class NSLock extends NSObject {

public NSLock() {
	super();
}

public NSLock(int id) {
	super(id);
}

public boolean lockBeforeDate(NSDate limit) {
	return OS.objc_msgSend(this.id, OS.sel_lockBeforeDate_1, limit != null ? limit.id : 0) != 0;
}

public NSString name() {
	int result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public void setName(NSString n) {
	OS.objc_msgSend(this.id, OS.sel_setName_1, n != null ? n.id : 0);
}

public boolean tryLock() {
	return OS.objc_msgSend(this.id, OS.sel_tryLock) != 0;
}

}
