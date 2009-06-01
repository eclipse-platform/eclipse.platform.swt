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

public class NSSet extends NSObject {

public NSSet() {
	super();
}

public NSSet(int /*long*/ id) {
	super(id);
}

public NSSet(id id) {
	super(id);
}

public int /*long*/ count() {
	return OS.objc_msgSend(this.id, OS.sel_count);
}

public NSEnumerator objectEnumerator() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_objectEnumerator);
	return result != 0 ? new NSEnumerator(result) : null;
}

public static NSSet set() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSSet, OS.sel_set);
	return result != 0 ? new NSSet(result) : null;
}

}
