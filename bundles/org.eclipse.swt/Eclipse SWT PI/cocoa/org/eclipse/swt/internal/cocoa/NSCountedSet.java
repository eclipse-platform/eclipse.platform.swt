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

public class NSCountedSet extends NSMutableSet {

public NSCountedSet() {
	super();
}

public NSCountedSet(int id) {
	super(id);
}

public void addObject(id object) {
	OS.objc_msgSend(this.id, OS.sel_addObject_1, object != null ? object.id : 0);
}

public int countForObject(id object) {
	return OS.objc_msgSend(this.id, OS.sel_countForObject_1, object != null ? object.id : 0);
}

public NSCountedSet initWithSet(NSSet set) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithSet_1, set != null ? set.id : 0);
	return result != 0 ? this : null;
}

public NSEnumerator objectEnumerator() {
	int result = OS.objc_msgSend(this.id, OS.sel_objectEnumerator);
	return result != 0 ? new NSEnumerator(result) : null;
}

public void removeObject(id object) {
	OS.objc_msgSend(this.id, OS.sel_removeObject_1, object != null ? object.id : 0);
}

}
