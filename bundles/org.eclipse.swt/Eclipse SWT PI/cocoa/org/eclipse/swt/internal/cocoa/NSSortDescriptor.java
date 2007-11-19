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

public class NSSortDescriptor extends NSObject {

public NSSortDescriptor() {
	super();
}

public NSSortDescriptor(int id) {
	super(id);
}

public boolean ascending() {
	return OS.objc_msgSend(this.id, OS.sel_ascending) != 0;
}

public int compareObject(id object1, id object2) {
	return OS.objc_msgSend(this.id, OS.sel_compareObject_1toObject_1, object1 != null ? object1.id : 0, object2 != null ? object2.id : 0);
}

public id initWithKey_ascending_(NSString key, boolean ascending) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithKey_1ascending_1, key != null ? key.id : 0, ascending);
	return result != 0 ? new id(result) : null;
}

public id initWithKey_ascending_selector_(NSString key, boolean ascending, int selector) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithKey_1ascending_1selector_1, key != null ? key.id : 0, ascending, selector);
	return result != 0 ? new id(result) : null;
}

public NSString key() {
	int result = OS.objc_msgSend(this.id, OS.sel_key);
	return result != 0 ? new NSString(result) : null;
}

public id reversedSortDescriptor() {
	int result = OS.objc_msgSend(this.id, OS.sel_reversedSortDescriptor);
	return result != 0 ? new id(result) : null;
}

public int selector() {
	return OS.objc_msgSend(this.id, OS.sel_selector);
}

}
