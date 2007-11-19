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

public class NSClassDescription extends NSObject {

public NSClassDescription() {
	super();
}

public NSClassDescription(int id) {
	super(id);
}

public NSArray attributeKeys() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributeKeys);
	return result != 0 ? new NSArray(result) : null;
}

public static NSClassDescription classDescriptionForClass(int aClass) {
	int result = OS.objc_msgSend(OS.class_NSClassDescription, OS.sel_classDescriptionForClass_1, aClass);
	return result != 0 ? new NSClassDescription(result) : null;
}

public static void invalidateClassDescriptionCache() {
	OS.objc_msgSend(OS.class_NSClassDescription, OS.sel_invalidateClassDescriptionCache);
}

public NSString inverseForRelationshipKey(NSString relationshipKey) {
	int result = OS.objc_msgSend(this.id, OS.sel_inverseForRelationshipKey_1, relationshipKey != null ? relationshipKey.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public static void registerClassDescription(NSClassDescription description, int aClass) {
	OS.objc_msgSend(OS.class_NSClassDescription, OS.sel_registerClassDescription_1forClass_1, description != null ? description.id : 0, aClass);
}

public NSArray toManyRelationshipKeys() {
	int result = OS.objc_msgSend(this.id, OS.sel_toManyRelationshipKeys);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray toOneRelationshipKeys() {
	int result = OS.objc_msgSend(this.id, OS.sel_toOneRelationshipKeys);
	return result != 0 ? new NSArray(result) : null;
}

}
