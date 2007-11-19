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

public class NSDictionaryController extends NSArrayController {

public NSDictionaryController() {
	super();
}

public NSDictionaryController(int id) {
	super(id);
}

public NSArray excludedKeys() {
	int result = OS.objc_msgSend(this.id, OS.sel_excludedKeys);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray includedKeys() {
	int result = OS.objc_msgSend(this.id, OS.sel_includedKeys);
	return result != 0 ? new NSArray(result) : null;
}

public NSString initialKey() {
	int result = OS.objc_msgSend(this.id, OS.sel_initialKey);
	return result != 0 ? new NSString(result) : null;
}

public id initialValue() {
	int result = OS.objc_msgSend(this.id, OS.sel_initialValue);
	return result != 0 ? new id(result) : null;
}

public NSDictionary localizedKeyDictionary() {
	int result = OS.objc_msgSend(this.id, OS.sel_localizedKeyDictionary);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSString localizedKeyTable() {
	int result = OS.objc_msgSend(this.id, OS.sel_localizedKeyTable);
	return result != 0 ? new NSString(result) : null;
}

public id newObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_newObject);
	return result != 0 ? new id(result) : null;
}

public void setExcludedKeys(NSArray keys) {
	OS.objc_msgSend(this.id, OS.sel_setExcludedKeys_1, keys != null ? keys.id : 0);
}

public void setIncludedKeys(NSArray keys) {
	OS.objc_msgSend(this.id, OS.sel_setIncludedKeys_1, keys != null ? keys.id : 0);
}

public void setInitialKey(NSString key) {
	OS.objc_msgSend(this.id, OS.sel_setInitialKey_1, key != null ? key.id : 0);
}

public void setInitialValue(id value) {
	OS.objc_msgSend(this.id, OS.sel_setInitialValue_1, value != null ? value.id : 0);
}

public void setLocalizedKeyDictionary(NSDictionary dictionary) {
	OS.objc_msgSend(this.id, OS.sel_setLocalizedKeyDictionary_1, dictionary != null ? dictionary.id : 0);
}

public void setLocalizedKeyTable(NSString stringsFileName) {
	OS.objc_msgSend(this.id, OS.sel_setLocalizedKeyTable_1, stringsFileName != null ? stringsFileName.id : 0);
}

}
