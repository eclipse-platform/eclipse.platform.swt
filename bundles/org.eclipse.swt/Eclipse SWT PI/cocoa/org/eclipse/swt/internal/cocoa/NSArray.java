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

public class NSArray extends NSObject {

public NSArray() {
	super();
}

public NSArray(int id) {
	super(id);
}

public void addObserver_forKeyPath_options_context_(NSObject observer, NSString keyPath, int options, int context) {
	OS.objc_msgSend(this.id, OS.sel_addObserver_1forKeyPath_1options_1context_1, observer != null ? observer.id : 0, keyPath != null ? keyPath.id : 0, options, context);
}

public void addObserver_toObjectsAtIndexes_forKeyPath_options_context_(NSObject observer, NSIndexSet indexes, NSString keyPath, int options, int context) {
	OS.objc_msgSend(this.id, OS.sel_addObserver_1toObjectsAtIndexes_1forKeyPath_1options_1context_1, observer != null ? observer.id : 0, indexes != null ? indexes.id : 0, keyPath != null ? keyPath.id : 0, options, context);
}

public static id array() {
	int result = OS.objc_msgSend(OS.class_NSArray, OS.sel_array);
	return result != 0 ? new id(result) : null;
}

public NSArray arrayByAddingObject(id anObject) {
	int result = OS.objc_msgSend(this.id, OS.sel_arrayByAddingObject_1, anObject != null ? anObject.id : 0);
	return result == this.id ? this : (result != 0 ? new NSArray(result) : null);
}

public NSArray arrayByAddingObjectsFromArray(NSArray otherArray) {
	int result = OS.objc_msgSend(this.id, OS.sel_arrayByAddingObjectsFromArray_1, otherArray != null ? otherArray.id : 0);
	return result == this.id ? this : (result != 0 ? new NSArray(result) : null);
}

public static id arrayWithArray(NSArray array) {
	int result = OS.objc_msgSend(OS.class_NSArray, OS.sel_arrayWithArray_1, array != null ? array.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id arrayWithContentsOfFile(NSString path) {
	int result = OS.objc_msgSend(OS.class_NSArray, OS.sel_arrayWithContentsOfFile_1, path != null ? path.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id arrayWithContentsOfURL(NSURL url) {
	int result = OS.objc_msgSend(OS.class_NSArray, OS.sel_arrayWithContentsOfURL_1, url != null ? url.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id arrayWithObject(id anObject) {
	int result = OS.objc_msgSend(OS.class_NSArray, OS.sel_arrayWithObject_1, anObject != null ? anObject.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_arrayWithObjects_(id arrayWithObjects) {
	int result = OS.objc_msgSend(OS.class_NSArray, OS.sel_arrayWithObjects_1, arrayWithObjects != null ? arrayWithObjects.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id static_arrayWithObjects_count_(int objects, int cnt) {
	int result = OS.objc_msgSend(OS.class_NSArray, OS.sel_arrayWithObjects_1count_1, objects, cnt);
	return result != 0 ? new id(result) : null;
}

public NSString componentsJoinedByString(NSString separator) {
	int result = OS.objc_msgSend(this.id, OS.sel_componentsJoinedByString_1, separator != null ? separator.id : 0);
	return result != 0 ? new NSString(result) : null;
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

public NSString descriptionWithLocale_(id locale) {
	int result = OS.objc_msgSend(this.id, OS.sel_descriptionWithLocale_1, locale != null ? locale.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSString descriptionWithLocale_indent_(id locale, int level) {
	int result = OS.objc_msgSend(this.id, OS.sel_descriptionWithLocale_1indent_1, locale != null ? locale.id : 0, level);
	return result != 0 ? new NSString(result) : null;
}

public NSArray filteredArrayUsingPredicate(NSPredicate predicate) {
	int result = OS.objc_msgSend(this.id, OS.sel_filteredArrayUsingPredicate_1, predicate != null ? predicate.id : 0);
	return result == this.id ? this : (result != 0 ? new NSArray(result) : null);
}

public id firstObjectCommonWithArray(NSArray otherArray) {
	int result = OS.objc_msgSend(this.id, OS.sel_firstObjectCommonWithArray_1, otherArray != null ? otherArray.id : 0);
	return result != 0 ? new id(result) : null;
}

public void getObjects_(int objects) {
	OS.objc_msgSend(this.id, OS.sel_getObjects_1, objects);
}

public void getObjects_range_(int objects, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_getObjects_1range_1, objects, range);
}

public int indexOfObject_(id anObject) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfObject_1, anObject != null ? anObject.id : 0);
}

public int indexOfObject_inRange_(id anObject, NSRange range) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfObject_1inRange_1, anObject != null ? anObject.id : 0, range);
}

public int indexOfObjectIdenticalTo_(id anObject) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfObjectIdenticalTo_1, anObject != null ? anObject.id : 0);
}

public int indexOfObjectIdenticalTo_inRange_(id anObject, NSRange range) {
	return OS.objc_msgSend(this.id, OS.sel_indexOfObjectIdenticalTo_1inRange_1, anObject != null ? anObject.id : 0, range);
}

public NSArray initWithArray_(NSArray array) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithArray_1, array != null ? array.id : 0);
	return result != 0 ? this : null;
}

public NSArray initWithArray_copyItems_(NSArray array, boolean flag) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithArray_1copyItems_1, array != null ? array.id : 0, flag);
	return result != 0 ? this : null;
}

public NSArray initWithContentsOfFile(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfFile_1, path != null ? path.id : 0);
	return result != 0 ? this : null;
}

public NSArray initWithContentsOfURL(NSURL url) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentsOfURL_1, url != null ? url.id : 0);
	return result != 0 ? this : null;
}

public NSArray initWithObjects_(id initWithObjects) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithObjects_1, initWithObjects != null ? initWithObjects.id : 0);
	return result != 0 ? this : null;
}

public NSArray initWithObjects_count_(int objects, int cnt) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithObjects_1count_1, objects, cnt);
	return result != 0 ? this : null;
}

public boolean isEqualToArray(NSArray otherArray) {
	return OS.objc_msgSend(this.id, OS.sel_isEqualToArray_1, otherArray != null ? otherArray.id : 0) != 0;
}

public id lastObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_lastObject);
	return result != 0 ? new id(result) : null;
}

public void makeObjectsPerformSelector_(int aSelector) {
	OS.objc_msgSend(this.id, OS.sel_makeObjectsPerformSelector_1, aSelector);
}

public void makeObjectsPerformSelector_withObject_(int aSelector, id argument) {
	OS.objc_msgSend(this.id, OS.sel_makeObjectsPerformSelector_1withObject_1, aSelector, argument != null ? argument.id : 0);
}

public int objectAtIndex(int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_objectAtIndex_1, index);
	return result;
//	return result != 0 ? new id(result) : null;
}

public NSEnumerator objectEnumerator() {
	int result = OS.objc_msgSend(this.id, OS.sel_objectEnumerator);
	return result != 0 ? new NSEnumerator(result) : null;
}

public NSArray objectsAtIndexes(NSIndexSet indexes) {
	int result = OS.objc_msgSend(this.id, OS.sel_objectsAtIndexes_1, indexes != null ? indexes.id : 0);
	return result == this.id ? this : (result != 0 ? new NSArray(result) : null);
}

public NSArray pathsMatchingExtensions(NSArray filterTypes) {
	int result = OS.objc_msgSend(this.id, OS.sel_pathsMatchingExtensions_1, filterTypes != null ? filterTypes.id : 0);
	return result == this.id ? this : (result != 0 ? new NSArray(result) : null);
}

public void removeObserver_forKeyPath_(NSObject observer, NSString keyPath) {
	OS.objc_msgSend(this.id, OS.sel_removeObserver_1forKeyPath_1, observer != null ? observer.id : 0, keyPath != null ? keyPath.id : 0);
}

public void removeObserver_fromObjectsAtIndexes_forKeyPath_(NSObject observer, NSIndexSet indexes, NSString keyPath) {
	OS.objc_msgSend(this.id, OS.sel_removeObserver_1fromObjectsAtIndexes_1forKeyPath_1, observer != null ? observer.id : 0, indexes != null ? indexes.id : 0, keyPath != null ? keyPath.id : 0);
}

public NSEnumerator reverseObjectEnumerator() {
	int result = OS.objc_msgSend(this.id, OS.sel_reverseObjectEnumerator);
	return result != 0 ? new NSEnumerator(result) : null;
}

public void setValue(id value, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_setValue_1forKey_1, value != null ? value.id : 0, key != null ? key.id : 0);
}

public NSData sortedArrayHint() {
	int result = OS.objc_msgSend(this.id, OS.sel_sortedArrayHint);
	return result != 0 ? new NSData(result) : null;
}

public NSArray sortedArrayUsingDescriptors(NSArray sortDescriptors) {
	int result = OS.objc_msgSend(this.id, OS.sel_sortedArrayUsingDescriptors_1, sortDescriptors != null ? sortDescriptors.id : 0);
	return result == this.id ? this : (result != 0 ? new NSArray(result) : null);
}

public NSArray sortedArrayUsingFunction_context_(int comparator, int context) {
	int result = OS.objc_msgSend(this.id, OS.sel_sortedArrayUsingFunction_1context_1, comparator, context);
	return result == this.id ? this : (result != 0 ? new NSArray(result) : null);
}

public NSArray sortedArrayUsingFunction_context_hint_(int comparator, int context, NSData hint) {
	int result = OS.objc_msgSend(this.id, OS.sel_sortedArrayUsingFunction_1context_1hint_1, comparator, context, hint != null ? hint.id : 0);
	return result == this.id ? this : (result != 0 ? new NSArray(result) : null);
}

public NSArray sortedArrayUsingSelector(int comparator) {
	int result = OS.objc_msgSend(this.id, OS.sel_sortedArrayUsingSelector_1, comparator);
	return result == this.id ? this : (result != 0 ? new NSArray(result) : null);
}

public NSArray subarrayWithRange(NSRange range) {
	int result = OS.objc_msgSend(this.id, OS.sel_subarrayWithRange_1, range);
	return result == this.id ? this : (result != 0 ? new NSArray(result) : null);
}

public id valueForKey(NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_valueForKey_1, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean writeToFile(NSString path, boolean useAuxiliaryFile) {
	return OS.objc_msgSend(this.id, OS.sel_writeToFile_1atomically_1, path != null ? path.id : 0, useAuxiliaryFile) != 0;
}

public boolean writeToURL(NSURL url, boolean atomically) {
	return OS.objc_msgSend(this.id, OS.sel_writeToURL_1atomically_1, url != null ? url.id : 0, atomically) != 0;
}

}
