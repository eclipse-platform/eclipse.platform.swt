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

public class NSIndexPath extends NSObject {

public NSIndexPath() {
	super();
}

public NSIndexPath(int id) {
	super(id);
}

public int compare(NSIndexPath otherObject) {
	return OS.objc_msgSend(this.id, OS.sel_compare_1, otherObject != null ? otherObject.id : 0);
}

public void getIndexes(int indexes) {
	OS.objc_msgSend(this.id, OS.sel_getIndexes_1, indexes);
}

public int indexAtPosition(int position) {
	return OS.objc_msgSend(this.id, OS.sel_indexAtPosition_1, position);
}

public NSIndexPath indexPathByAddingIndex(int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_indexPathByAddingIndex_1, index);
	return result == this.id ? this : (result != 0 ? new NSIndexPath(result) : null);
}

public NSIndexPath indexPathByRemovingLastIndex() {
	int result = OS.objc_msgSend(this.id, OS.sel_indexPathByRemovingLastIndex);
	return result == this.id ? this : (result != 0 ? new NSIndexPath(result) : null);
}

public static id indexPathWithIndex(int index) {
	int result = OS.objc_msgSend(OS.class_NSIndexPath, OS.sel_indexPathWithIndex_1, index);
	return result != 0 ? new id(result) : null;
}

public static id indexPathWithIndexes(int indexes, int length) {
	int result = OS.objc_msgSend(OS.class_NSIndexPath, OS.sel_indexPathWithIndexes_1length_1, indexes, length);
	return result != 0 ? new id(result) : null;
}

public id initWithIndex(int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithIndex_1, index);
	return result != 0 ? new id(result) : null;
}

public id initWithIndexes(int indexes, int length) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithIndexes_1length_1, indexes, length);
	return result != 0 ? new id(result) : null;
}

public int length() {
	return OS.objc_msgSend(this.id, OS.sel_length);
}

}
