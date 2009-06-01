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

public class NSDictionary extends NSObject {

public NSDictionary() {
	super();
}

public NSDictionary(int /*long*/ id) {
	super(id);
}

public NSDictionary(id id) {
	super(id);
}

public NSArray allKeys() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_allKeys);
	return result != 0 ? new NSArray(result) : null;
}

public int /*long*/ count() {
	return OS.objc_msgSend(this.id, OS.sel_count);
}

public static NSDictionary dictionaryWithObject(id object, id key) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSDictionary, OS.sel_dictionaryWithObject_forKey_, object != null ? object.id : 0, key != null ? key.id : 0);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSEnumerator objectEnumerator() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_objectEnumerator);
	return result != 0 ? new NSEnumerator(result) : null;
}

public id objectForKey(id aKey) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_objectForKey_, aKey != null ? aKey.id : 0);
	return result != 0 ? new id(result) : null;
}

public id valueForKey(NSString key) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_valueForKey_, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

}
