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

public class NSSet extends NSObject {

public NSSet() {
	super();
}

public NSSet(int id) {
	super(id);
}

public void addObserver(NSObject observer, NSString keyPath, int options, int context) {
	OS.objc_msgSend(this.id, OS.sel_addObserver_1forKeyPath_1options_1context_1, observer != null ? observer.id : 0, keyPath != null ? keyPath.id : 0, options, context);
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

public NSString description() {
	int result = OS.objc_msgSend(this.id, OS.sel_description);
	return result != 0 ? new NSString(result) : null;
}

public NSString descriptionWithLocale(id locale) {
	int result = OS.objc_msgSend(this.id, OS.sel_descriptionWithLocale_1, locale != null ? locale.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSSet filteredSetUsingPredicate(NSPredicate predicate) {
	int result = OS.objc_msgSend(this.id, OS.sel_filteredSetUsingPredicate_1, predicate != null ? predicate.id : 0);
	return result == this.id ? this : (result != 0 ? new NSSet(result) : null);
}

public NSSet initWithArray(NSArray array) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithArray_1, array != null ? array.id : 0);
	return result != 0 ? this : null;
}

public NSSet initWithObjects_(id initWithObjects) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithObjects_1, initWithObjects != null ? initWithObjects.id : 0);
	return result != 0 ? this : null;
}

public NSSet initWithObjects_count_(int objects, int cnt) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithObjects_1count_1, objects, cnt);
	return result != 0 ? this : null;
}

public NSSet initWithSet_(NSSet set) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithSet_1, set != null ? set.id : 0);
	return result != 0 ? this : null;
}

public NSSet initWithSet_copyItems_(NSSet set, boolean flag) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithSet_1copyItems_1, set != null ? set.id : 0, flag);
	return result != 0 ? this : null;
}

public boolean intersectsSet(NSSet otherSet) {
	return OS.objc_msgSend(this.id, OS.sel_intersectsSet_1, otherSet != null ? otherSet.id : 0) != 0;
}

public boolean isEqualToSet(NSSet otherSet) {
	return OS.objc_msgSend(this.id, OS.sel_isEqualToSet_1, otherSet != null ? otherSet.id : 0) != 0;
}

public boolean isSubsetOfSet(NSSet otherSet) {
	return OS.objc_msgSend(this.id, OS.sel_isSubsetOfSet_1, otherSet != null ? otherSet.id : 0) != 0;
}

public void makeObjectsPerformSelector_(int aSelector) {
	OS.objc_msgSend(this.id, OS.sel_makeObjectsPerformSelector_1, aSelector);
}

public void makeObjectsPerformSelector_withObject_(int aSelector, id argument) {
	OS.objc_msgSend(this.id, OS.sel_makeObjectsPerformSelector_1withObject_1, aSelector, argument != null ? argument.id : 0);
}

public id member(id object) {
	int result = OS.objc_msgSend(this.id, OS.sel_member_1, object != null ? object.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSEnumerator objectEnumerator() {
	int result = OS.objc_msgSend(this.id, OS.sel_objectEnumerator);
	return result != 0 ? new NSEnumerator(result) : null;
}

public void removeObserver(NSObject observer, NSString keyPath) {
	OS.objc_msgSend(this.id, OS.sel_removeObserver_1forKeyPath_1, observer != null ? observer.id : 0, keyPath != null ? keyPath.id : 0);
}

public static id set() {
	int result = OS.objc_msgSend(OS.class_NSSet, OS.sel_set);
	return result != 0 ? new id(result) : null;
}

public NSSet setByAddingObject(id anObject) {
	int result = OS.objc_msgSend(this.id, OS.sel_setByAddingObject_1, anObject != null ? anObject.id : 0);
	return result == this.id ? this : (result != 0 ? new NSSet(result) : null);
}

public NSSet setByAddingObjectsFromArray(NSArray other) {
	int result = OS.objc_msgSend(this.id, OS.sel_setByAddingObjectsFromArray_1, other != null ? other.id : 0);
	return result == this.id ? this : (result != 0 ? new NSSet(result) : null);
}

public NSSet setByAddingObjectsFromSet(NSSet other) {
	int result = OS.objc_msgSend(this.id, OS.sel_setByAddingObjectsFromSet_1, other != null ? other.id : 0);
	return result == this.id ? this : (result != 0 ? new NSSet(result) : null);
}

public void setValue(id value, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_setValue_1forKey_1, value != null ? value.id : 0, key != null ? key.id : 0);
}

public static id setWithArray(NSArray array) {
	int result = OS.objc_msgSend(OS.class_NSSet, OS.sel_setWithArray_1, array != null ? array.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id setWithObject(id object) {
	int result = OS.objc_msgSend(OS.class_NSSet, OS.sel_setWithObject_1, object != null ? object.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_setWithObjects_(id setWithObjects) {
	int result = OS.objc_msgSend(OS.class_NSSet, OS.sel_setWithObjects_1, setWithObjects != null ? setWithObjects.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_setWithObjects_count_(int objects, int cnt) {
	int result = OS.objc_msgSend(OS.class_NSSet, OS.sel_setWithObjects_1count_1, objects, cnt);
	return result != 0 ? new id(result) : null;
}

public static id setWithSet(NSSet set) {
	int result = OS.objc_msgSend(OS.class_NSSet, OS.sel_setWithSet_1, set != null ? set.id : 0);
	return result != 0 ? new id(result) : null;
}

public id valueForKey(NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_valueForKey_1, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

}
