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

public class NSMapTable extends NSObject {

public NSMapTable() {
	super();
}

public NSMapTable(int id) {
	super(id);
}

public int count() {
	return OS.objc_msgSend(this.id, OS.sel_count);
}

public NSDictionary dictionaryRepresentation() {
	int result = OS.objc_msgSend(this.id, OS.sel_dictionaryRepresentation);
	return result != 0 ? new NSDictionary(result) : null;
}

public id initWithKeyOptions(int keyOptions, int valueOptions, int initialCapacity) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithKeyOptions_1valueOptions_1capacity_1, keyOptions, valueOptions, initialCapacity);
	return result != 0 ? new id(result) : null;
}

public id initWithKeyPointerFunctions(NSPointerFunctions keyFunctions, NSPointerFunctions valueFunctions, int initialCapacity) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithKeyPointerFunctions_1valuePointerFunctions_1capacity_1, keyFunctions != null ? keyFunctions.id : 0, valueFunctions != null ? valueFunctions.id : 0, initialCapacity);
	return result != 0 ? new id(result) : null;
}

public NSEnumerator keyEnumerator() {
	int result = OS.objc_msgSend(this.id, OS.sel_keyEnumerator);
	return result != 0 ? new NSEnumerator(result) : null;
}

public NSPointerFunctions keyPointerFunctions() {
	int result = OS.objc_msgSend(this.id, OS.sel_keyPointerFunctions);
	return result != 0 ? new NSPointerFunctions(result) : null;
}

public static id mapTableWithKeyOptions(int keyOptions, int valueOptions) {
	int result = OS.objc_msgSend(OS.class_NSMapTable, OS.sel_mapTableWithKeyOptions_1valueOptions_1, keyOptions, valueOptions);
	return result != 0 ? new id(result) : null;
}

public static id mapTableWithStrongToStrongObjects() {
	int result = OS.objc_msgSend(OS.class_NSMapTable, OS.sel_mapTableWithStrongToStrongObjects);
	return result != 0 ? new id(result) : null;
}

public static id mapTableWithStrongToWeakObjects() {
	int result = OS.objc_msgSend(OS.class_NSMapTable, OS.sel_mapTableWithStrongToWeakObjects);
	return result != 0 ? new id(result) : null;
}

public static id mapTableWithWeakToStrongObjects() {
	int result = OS.objc_msgSend(OS.class_NSMapTable, OS.sel_mapTableWithWeakToStrongObjects);
	return result != 0 ? new id(result) : null;
}

public static id mapTableWithWeakToWeakObjects() {
	int result = OS.objc_msgSend(OS.class_NSMapTable, OS.sel_mapTableWithWeakToWeakObjects);
	return result != 0 ? new id(result) : null;
}

public NSEnumerator objectEnumerator() {
	int result = OS.objc_msgSend(this.id, OS.sel_objectEnumerator);
	return result != 0 ? new NSEnumerator(result) : null;
}

public id objectForKey(id aKey) {
	int result = OS.objc_msgSend(this.id, OS.sel_objectForKey_1, aKey != null ? aKey.id : 0);
	return result != 0 ? new id(result) : null;
}

public void removeAllObjects() {
	OS.objc_msgSend(this.id, OS.sel_removeAllObjects);
}

public void removeObjectForKey(id aKey) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectForKey_1, aKey != null ? aKey.id : 0);
}

public void setObject(id anObject, id aKey) {
	OS.objc_msgSend(this.id, OS.sel_setObject_1forKey_1, anObject != null ? anObject.id : 0, aKey != null ? aKey.id : 0);
}

public NSPointerFunctions valuePointerFunctions() {
	int result = OS.objc_msgSend(this.id, OS.sel_valuePointerFunctions);
	return result != 0 ? new NSPointerFunctions(result) : null;
}

}
