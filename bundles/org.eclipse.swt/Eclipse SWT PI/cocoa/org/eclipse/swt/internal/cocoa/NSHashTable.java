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

public class NSHashTable extends NSObject {

public NSHashTable() {
	super();
}

public NSHashTable(int id) {
	super(id);
}

public void addObject(id object) {
	OS.objc_msgSend(this.id, OS.sel_addObject_1, object != null ? object.id : 0);
}

public NSArray allObjects() {
	int result = OS.objc_msgSend(this.id, OS.sel_allObjects);
	return result != 0 ? new NSArray(result) : null;
}

public id anyObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_anyObject);
	return result != 0 ? new id(result) : null;
}

public boolean containsObject(id anObject) {
	return OS.objc_msgSend(this.id, OS.sel_containsObject_1, anObject != null ? anObject.id : 0) != 0;
}

public int count() {
	return OS.objc_msgSend(this.id, OS.sel_count);
}

public static id hashTableWithOptions(int options) {
	int result = OS.objc_msgSend(OS.class_NSHashTable, OS.sel_hashTableWithOptions_1, options);
	return result != 0 ? new id(result) : null;
}

public static id hashTableWithWeakObjects() {
	int result = OS.objc_msgSend(OS.class_NSHashTable, OS.sel_hashTableWithWeakObjects);
	return result != 0 ? new id(result) : null;
}

public id initWithOptions(int options, int initialCapacity) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithOptions_1capacity_1, options, initialCapacity);
	return result != 0 ? new id(result) : null;
}

public id initWithPointerFunctions(NSPointerFunctions functions, int initialCapacity) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithPointerFunctions_1capacity_1, functions != null ? functions.id : 0, initialCapacity);
	return result != 0 ? new id(result) : null;
}

public void intersectHashTable(NSHashTable other) {
	OS.objc_msgSend(this.id, OS.sel_intersectHashTable_1, other != null ? other.id : 0);
}

public boolean intersectsHashTable(NSHashTable other) {
	return OS.objc_msgSend(this.id, OS.sel_intersectsHashTable_1, other != null ? other.id : 0) != 0;
}

public boolean isEqualToHashTable(NSHashTable other) {
	return OS.objc_msgSend(this.id, OS.sel_isEqualToHashTable_1, other != null ? other.id : 0) != 0;
}

public boolean isSubsetOfHashTable(NSHashTable other) {
	return OS.objc_msgSend(this.id, OS.sel_isSubsetOfHashTable_1, other != null ? other.id : 0) != 0;
}

public id member(id object) {
	int result = OS.objc_msgSend(this.id, OS.sel_member_1, object != null ? object.id : 0);
	return result != 0 ? new id(result) : null;
}

public void minusHashTable(NSHashTable other) {
	OS.objc_msgSend(this.id, OS.sel_minusHashTable_1, other != null ? other.id : 0);
}

public NSEnumerator objectEnumerator() {
	int result = OS.objc_msgSend(this.id, OS.sel_objectEnumerator);
	return result != 0 ? new NSEnumerator(result) : null;
}

public NSPointerFunctions pointerFunctions() {
	int result = OS.objc_msgSend(this.id, OS.sel_pointerFunctions);
	return result != 0 ? new NSPointerFunctions(result) : null;
}

public void removeAllObjects() {
	OS.objc_msgSend(this.id, OS.sel_removeAllObjects);
}

public void removeObject(id object) {
	OS.objc_msgSend(this.id, OS.sel_removeObject_1, object != null ? object.id : 0);
}

public NSSet setRepresentation() {
	int result = OS.objc_msgSend(this.id, OS.sel_setRepresentation);
	return result != 0 ? new NSSet(result) : null;
}

public void unionHashTable(NSHashTable other) {
	OS.objc_msgSend(this.id, OS.sel_unionHashTable_1, other != null ? other.id : 0);
}

}
