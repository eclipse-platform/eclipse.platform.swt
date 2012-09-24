/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSIndexSet extends NSObject {

public NSIndexSet() {
	super();
}

public NSIndexSet(long /*int*/ id) {
	super(id);
}

public NSIndexSet(id id) {
	super(id);
}

public boolean containsIndex(long /*int*/ value) {
	return OS.objc_msgSend_bool(this.id, OS.sel_containsIndex_, value);
}

public long /*int*/ count() {
	return OS.objc_msgSend(this.id, OS.sel_count);
}

public long /*int*/ firstIndex() {
	return OS.objc_msgSend(this.id, OS.sel_firstIndex);
}

public long /*int*/ getIndexes(long[] /*int[]*/ indexBuffer, long /*int*/ bufferSize, long /*int*/ range) {
	return OS.objc_msgSend(this.id, OS.sel_getIndexes_maxCount_inIndexRange_, indexBuffer, bufferSize, range);
}

public static id indexSetWithIndex(long /*int*/ value) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSIndexSet, OS.sel_indexSetWithIndex_, value);
	return result != 0 ? new id(result) : null;
}

public NSIndexSet initWithIndex(long /*int*/ value) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithIndex_, value);
	return result == this.id ? this : (result != 0 ? new NSIndexSet(result) : null);
}

public id initWithIndexSet(NSIndexSet indexSet) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithIndexSet_, indexSet != null ? indexSet.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSIndexSet initWithIndexesInRange(NSRange range) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithIndexesInRange_, range);
	return result == this.id ? this : (result != 0 ? new NSIndexSet(result) : null);
}

}
