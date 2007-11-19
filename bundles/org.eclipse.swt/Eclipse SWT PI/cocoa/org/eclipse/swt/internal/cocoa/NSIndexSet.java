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

public class NSIndexSet extends NSObject {

public NSIndexSet() {
	super();
}

public NSIndexSet(int id) {
	super(id);
}

public boolean containsIndex(int value) {
	return OS.objc_msgSend(this.id, OS.sel_containsIndex_1, value) != 0;
}

public boolean containsIndexes(NSIndexSet indexSet) {
	return OS.objc_msgSend(this.id, OS.sel_containsIndexes_1, indexSet != null ? indexSet.id : 0) != 0;
}

public boolean containsIndexesInRange(NSRange range) {
	return OS.objc_msgSend(this.id, OS.sel_containsIndexesInRange_1, range) != 0;
}

public int count() {
	return OS.objc_msgSend(this.id, OS.sel_count);
}

public int countOfIndexesInRange(NSRange range) {
	return OS.objc_msgSend(this.id, OS.sel_countOfIndexesInRange_1, range);
}

public int firstIndex() {
	return OS.objc_msgSend(this.id, OS.sel_firstIndex);
}

public int getIndexes(int [] indexBuffer, int bufferSize, int range) {
	return OS.objc_msgSend(this.id, OS.sel_getIndexes_1maxCount_1inIndexRange_1, indexBuffer, bufferSize, range);
}

public int indexGreaterThanIndex(int value) {
	return OS.objc_msgSend(this.id, OS.sel_indexGreaterThanIndex_1, value);
}

public int indexGreaterThanOrEqualToIndex(int value) {
	return OS.objc_msgSend(this.id, OS.sel_indexGreaterThanOrEqualToIndex_1, value);
}

public int indexLessThanIndex(int value) {
	return OS.objc_msgSend(this.id, OS.sel_indexLessThanIndex_1, value);
}

public int indexLessThanOrEqualToIndex(int value) {
	return OS.objc_msgSend(this.id, OS.sel_indexLessThanOrEqualToIndex_1, value);
}

public static id indexSet() {
	int result = OS.objc_msgSend(OS.class_NSIndexSet, OS.sel_indexSet);
	return result != 0 ? new id(result) : null;
}

public static id indexSetWithIndex(int value) {
	int result = OS.objc_msgSend(OS.class_NSIndexSet, OS.sel_indexSetWithIndex_1, value);
	return result != 0 ? new id(result) : null;
}

public static id indexSetWithIndexesInRange(NSRange range) {
	int result = OS.objc_msgSend(OS.class_NSIndexSet, OS.sel_indexSetWithIndexesInRange_1, range);
	return result != 0 ? new id(result) : null;
}

public id initWithIndex(int value) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithIndex_1, value);
	return result != 0 ? new id(result) : null;
}

public id initWithIndexSet(NSIndexSet indexSet) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithIndexSet_1, indexSet != null ? indexSet.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithIndexesInRange(NSRange range) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithIndexesInRange_1, range);
	return result != 0 ? new id(result) : null;
}

public boolean intersectsIndexesInRange(NSRange range) {
	return OS.objc_msgSend(this.id, OS.sel_intersectsIndexesInRange_1, range) != 0;
}

public boolean isEqualToIndexSet(NSIndexSet indexSet) {
	return OS.objc_msgSend(this.id, OS.sel_isEqualToIndexSet_1, indexSet != null ? indexSet.id : 0) != 0;
}

public int lastIndex() {
	return OS.objc_msgSend(this.id, OS.sel_lastIndex);
}

}
