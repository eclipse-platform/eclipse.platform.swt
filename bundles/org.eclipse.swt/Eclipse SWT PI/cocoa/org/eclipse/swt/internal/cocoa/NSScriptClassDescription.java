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

public class NSScriptClassDescription extends NSClassDescription {

public NSScriptClassDescription() {
	super();
}

public NSScriptClassDescription(int id) {
	super(id);
}

public int appleEventCode() {
	return OS.objc_msgSend(this.id, OS.sel_appleEventCode);
}

public int appleEventCodeForKey(NSString key) {
	return OS.objc_msgSend(this.id, OS.sel_appleEventCodeForKey_1, key != null ? key.id : 0);
}

//public static NSScriptClassDescription classDescriptionForClass(int aClass) {
//	int result = OS.objc_msgSend(OS.class_NSScriptClassDescription, OS.sel_classDescriptionForClass_1, aClass);
//	return result != 0 ? new NSScriptClassDescription(result) : null;
//}

public NSScriptClassDescription classDescriptionForKey(NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_classDescriptionForKey_1, key != null ? key.id : 0);
	return result == this.id ? this : (result != 0 ? new NSScriptClassDescription(result) : null);
}

public NSString className() {
	int result = OS.objc_msgSend(this.id, OS.sel_className);
	return result != 0 ? new NSString(result) : null;
}

public NSString defaultSubcontainerAttributeKey() {
	int result = OS.objc_msgSend(this.id, OS.sel_defaultSubcontainerAttributeKey);
	return result != 0 ? new NSString(result) : null;
}

public boolean hasOrderedToManyRelationshipForKey(NSString key) {
	return OS.objc_msgSend(this.id, OS.sel_hasOrderedToManyRelationshipForKey_1, key != null ? key.id : 0) != 0;
}

public boolean hasPropertyForKey(NSString key) {
	return OS.objc_msgSend(this.id, OS.sel_hasPropertyForKey_1, key != null ? key.id : 0) != 0;
}

public boolean hasReadablePropertyForKey(NSString key) {
	return OS.objc_msgSend(this.id, OS.sel_hasReadablePropertyForKey_1, key != null ? key.id : 0) != 0;
}

public boolean hasWritablePropertyForKey(NSString key) {
	return OS.objc_msgSend(this.id, OS.sel_hasWritablePropertyForKey_1, key != null ? key.id : 0) != 0;
}

public NSString implementationClassName() {
	int result = OS.objc_msgSend(this.id, OS.sel_implementationClassName);
	return result != 0 ? new NSString(result) : null;
}

public id initWithSuiteName(NSString suiteName, NSString className, NSDictionary classDeclaration) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithSuiteName_1className_1dictionary_1, suiteName != null ? suiteName.id : 0, className != null ? className.id : 0, classDeclaration != null ? classDeclaration.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isLocationRequiredToCreateForKey(NSString toManyRelationshipKey) {
	return OS.objc_msgSend(this.id, OS.sel_isLocationRequiredToCreateForKey_1, toManyRelationshipKey != null ? toManyRelationshipKey.id : 0) != 0;
}

public boolean isReadOnlyKey(NSString key) {
	return OS.objc_msgSend(this.id, OS.sel_isReadOnlyKey_1, key != null ? key.id : 0) != 0;
}

public NSString keyWithAppleEventCode(int appleEventCode) {
	int result = OS.objc_msgSend(this.id, OS.sel_keyWithAppleEventCode_1, appleEventCode);
	return result != 0 ? new NSString(result) : null;
}

public boolean matchesAppleEventCode(int appleEventCode) {
	return OS.objc_msgSend(this.id, OS.sel_matchesAppleEventCode_1, appleEventCode) != 0;
}

public int selectorForCommand(NSScriptCommandDescription commandDescription) {
	return OS.objc_msgSend(this.id, OS.sel_selectorForCommand_1, commandDescription != null ? commandDescription.id : 0);
}

public NSString suiteName() {
	int result = OS.objc_msgSend(this.id, OS.sel_suiteName);
	return result != 0 ? new NSString(result) : null;
}

public NSScriptClassDescription superclassDescription() {
	int result = OS.objc_msgSend(this.id, OS.sel_superclassDescription);
	return result == this.id ? this : (result != 0 ? new NSScriptClassDescription(result) : null);
}

public boolean supportsCommand(NSScriptCommandDescription commandDescription) {
	return OS.objc_msgSend(this.id, OS.sel_supportsCommand_1, commandDescription != null ? commandDescription.id : 0) != 0;
}

public NSString typeForKey(NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_typeForKey_1, key != null ? key.id : 0);
	return result != 0 ? new NSString(result) : null;
}

}
