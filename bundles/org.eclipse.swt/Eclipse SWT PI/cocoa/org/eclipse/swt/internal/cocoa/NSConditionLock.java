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

public class NSConditionLock extends NSObject {

public NSConditionLock() {
	super();
}

public NSConditionLock(int id) {
	super(id);
}

public int condition() {
	return OS.objc_msgSend(this.id, OS.sel_condition);
}

public id initWithCondition(int condition) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCondition_1, condition);
	return result != 0 ? new id(result) : null;
}

public boolean lockBeforeDate(NSDate limit) {
	return OS.objc_msgSend(this.id, OS.sel_lockBeforeDate_1, limit != null ? limit.id : 0) != 0;
}

public void lockWhenCondition_(int condition) {
	OS.objc_msgSend(this.id, OS.sel_lockWhenCondition_1, condition);
}

public boolean lockWhenCondition_beforeDate_(int condition, NSDate limit) {
	return OS.objc_msgSend(this.id, OS.sel_lockWhenCondition_1beforeDate_1, condition, limit != null ? limit.id : 0) != 0;
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

public boolean tryLockWhenCondition(int condition) {
	return OS.objc_msgSend(this.id, OS.sel_tryLockWhenCondition_1, condition) != 0;
}

public void unlockWithCondition(int condition) {
	OS.objc_msgSend(this.id, OS.sel_unlockWithCondition_1, condition);
}

}
