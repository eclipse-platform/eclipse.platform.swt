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

public class NSMutableArray extends NSArray {

public NSMutableArray() {
	super();
}

public NSMutableArray(int id) {
	super(id);
}

public void addObject(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_addObject_1, anObject != null ? anObject.id : 0);
}

public void addObjectsFromArray(NSArray otherArray) {
	OS.objc_msgSend(this.id, OS.sel_addObjectsFromArray_1, otherArray != null ? otherArray.id : 0);
}

public static NSMutableArray arrayWithCapacity(int numItems) {
	int result = OS.objc_msgSend(OS.class_NSMutableArray, OS.sel_arrayWithCapacity_1, numItems);
	return result != 0 ? new NSMutableArray(result) : null;
}

public void exchangeObjectAtIndex(int idx1, int idx2) {
	OS.objc_msgSend(this.id, OS.sel_exchangeObjectAtIndex_1withObjectAtIndex_1, idx1, idx2);
}

public void filterUsingPredicate(NSPredicate predicate) {
	OS.objc_msgSend(this.id, OS.sel_filterUsingPredicate_1, predicate != null ? predicate.id : 0);
}

public NSMutableArray initWithCapacity(int numItems) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCapacity_1, numItems);
	return result != 0 ? this : null;
}

public void insertObject(id anObject, int index) {
	OS.objc_msgSend(this.id, OS.sel_insertObject_1atIndex_1, anObject != null ? anObject.id : 0, index);
}

public void insertObjects(NSArray objects, NSIndexSet indexes) {
	OS.objc_msgSend(this.id, OS.sel_insertObjects_1atIndexes_1, objects != null ? objects.id : 0, indexes != null ? indexes.id : 0);
}

public void removeAllObjects() {
	OS.objc_msgSend(this.id, OS.sel_removeAllObjects);
}

public void removeLastObject() {
	OS.objc_msgSend(this.id, OS.sel_removeLastObject);
}

public void removeObject_(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_removeObject_1, anObject != null ? anObject.id : 0);
}

public void removeObject_inRange_(id anObject, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_removeObject_1inRange_1, anObject != null ? anObject.id : 0, range);
}

public void removeObjectAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectAtIndex_1, index);
}

public void removeObjectIdenticalTo_(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectIdenticalTo_1, anObject != null ? anObject.id : 0);
}

public void removeObjectIdenticalTo_inRange_(id anObject, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectIdenticalTo_1inRange_1, anObject != null ? anObject.id : 0, range);
}

public void removeObjectsAtIndexes(NSIndexSet indexes) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectsAtIndexes_1, indexes != null ? indexes.id : 0);
}

public void removeObjectsFromIndices(int indices, int cnt) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectsFromIndices_1numIndices_1, indices, cnt);
}

public void removeObjectsInArray(NSArray otherArray) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectsInArray_1, otherArray != null ? otherArray.id : 0);
}

public void removeObjectsInRange(NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectsInRange_1, range);
}

public void replaceObjectAtIndex(int index, id anObject) {
	OS.objc_msgSend(this.id, OS.sel_replaceObjectAtIndex_1withObject_1, index, anObject != null ? anObject.id : 0);
}

public void replaceObjectsAtIndexes(NSIndexSet indexes, NSArray objects) {
	OS.objc_msgSend(this.id, OS.sel_replaceObjectsAtIndexes_1withObjects_1, indexes != null ? indexes.id : 0, objects != null ? objects.id : 0);
}

public void replaceObjectsInRange_withObjectsFromArray_(NSRange range, NSArray otherArray) {
	OS.objc_msgSend(this.id, OS.sel_replaceObjectsInRange_1withObjectsFromArray_1, range, otherArray != null ? otherArray.id : 0);
}

public void replaceObjectsInRange_withObjectsFromArray_range_(NSRange range, NSArray otherArray, NSRange otherRange) {
	OS.objc_msgSend(this.id, OS.sel_replaceObjectsInRange_1withObjectsFromArray_1range_1, range, otherArray != null ? otherArray.id : 0, otherRange);
}

public void setArray(NSArray otherArray) {
	OS.objc_msgSend(this.id, OS.sel_setArray_1, otherArray != null ? otherArray.id : 0);
}

public void sortUsingDescriptors(NSArray sortDescriptors) {
	OS.objc_msgSend(this.id, OS.sel_sortUsingDescriptors_1, sortDescriptors != null ? sortDescriptors.id : 0);
}

public void sortUsingFunction(int compare, int context) {
	OS.objc_msgSend(this.id, OS.sel_sortUsingFunction_1context_1, compare, context);
}

public void sortUsingSelector(int comparator) {
	OS.objc_msgSend(this.id, OS.sel_sortUsingSelector_1, comparator);
}

}
