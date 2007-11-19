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

public class NSPointerArray extends NSObject {

public NSPointerArray() {
	super();
}

public NSPointerArray(int id) {
	super(id);
}

public void addPointer(int pointer) {
	OS.objc_msgSend(this.id, OS.sel_addPointer_1, pointer);
}

public NSArray allObjects() {
	int result = OS.objc_msgSend(this.id, OS.sel_allObjects);
	return result != 0 ? new NSArray(result) : null;
}

public void compact() {
	OS.objc_msgSend(this.id, OS.sel_compact);
}

public int count() {
	return OS.objc_msgSend(this.id, OS.sel_count);
}

public NSPointerArray initWithOptions(int options) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithOptions_1, options);
	return result != 0 ? this : null;
}

public id initWithPointerFunctions(NSPointerFunctions functions) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithPointerFunctions_1, functions != null ? functions.id : 0);
	return result != 0 ? new id(result) : null;
}

public void insertPointer(int item, int index) {
	OS.objc_msgSend(this.id, OS.sel_insertPointer_1atIndex_1, item, index);
}

public static id pointerArrayWithOptions(int options) {
	int result = OS.objc_msgSend(OS.class_NSPointerArray, OS.sel_pointerArrayWithOptions_1, options);
	return result != 0 ? new id(result) : null;
}

public static id pointerArrayWithPointerFunctions(NSPointerFunctions functions) {
	int result = OS.objc_msgSend(OS.class_NSPointerArray, OS.sel_pointerArrayWithPointerFunctions_1, functions != null ? functions.id : 0);
	return result != 0 ? new id(result) : null;
}

public static id pointerArrayWithStrongObjects() {
	int result = OS.objc_msgSend(OS.class_NSPointerArray, OS.sel_pointerArrayWithStrongObjects);
	return result != 0 ? new id(result) : null;
}

public static id pointerArrayWithWeakObjects() {
	int result = OS.objc_msgSend(OS.class_NSPointerArray, OS.sel_pointerArrayWithWeakObjects);
	return result != 0 ? new id(result) : null;
}

public int pointerAtIndex(int index) {
	return OS.objc_msgSend(this.id, OS.sel_pointerAtIndex_1, index);
}

public NSPointerFunctions pointerFunctions() {
	int result = OS.objc_msgSend(this.id, OS.sel_pointerFunctions);
	return result != 0 ? new NSPointerFunctions(result) : null;
}

public void removePointerAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_removePointerAtIndex_1, index);
}

public void replacePointerAtIndex(int index, int item) {
	OS.objc_msgSend(this.id, OS.sel_replacePointerAtIndex_1withPointer_1, index, item);
}

public void setCount(int count) {
	OS.objc_msgSend(this.id, OS.sel_setCount_1, count);
}

}
