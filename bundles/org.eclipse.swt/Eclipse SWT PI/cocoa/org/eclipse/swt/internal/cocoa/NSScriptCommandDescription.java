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

public class NSScriptCommandDescription extends NSObject {

public NSScriptCommandDescription() {
	super();
}

public NSScriptCommandDescription(int id) {
	super(id);
}

public int appleEventClassCode() {
	return OS.objc_msgSend(this.id, OS.sel_appleEventClassCode);
}

public int appleEventCode() {
	return OS.objc_msgSend(this.id, OS.sel_appleEventCode);
}

public int appleEventCodeForArgumentWithName(NSString argumentName) {
	return OS.objc_msgSend(this.id, OS.sel_appleEventCodeForArgumentWithName_1, argumentName != null ? argumentName.id : 0);
}

public int appleEventCodeForReturnType() {
	return OS.objc_msgSend(this.id, OS.sel_appleEventCodeForReturnType);
}

public NSArray argumentNames() {
	int result = OS.objc_msgSend(this.id, OS.sel_argumentNames);
	return result != 0 ? new NSArray(result) : null;
}

public NSString commandClassName() {
	int result = OS.objc_msgSend(this.id, OS.sel_commandClassName);
	return result != 0 ? new NSString(result) : null;
}

public NSString commandName() {
	int result = OS.objc_msgSend(this.id, OS.sel_commandName);
	return result != 0 ? new NSString(result) : null;
}

public NSScriptCommand createCommandInstance() {
	int result = OS.objc_msgSend(this.id, OS.sel_createCommandInstance);
	return result != 0 ? new NSScriptCommand(result) : null;
}

public NSScriptCommand createCommandInstanceWithZone(int zone) {
	int result = OS.objc_msgSend(this.id, OS.sel_createCommandInstanceWithZone_1, zone);
	return result != 0 ? new NSScriptCommand(result) : null;
}

public id initWithSuiteName(NSString suiteName, NSString commandName, NSDictionary commandDeclaration) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithSuiteName_1commandName_1dictionary_1, suiteName != null ? suiteName.id : 0, commandName != null ? commandName.id : 0, commandDeclaration != null ? commandDeclaration.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isOptionalArgumentWithName(NSString argumentName) {
	return OS.objc_msgSend(this.id, OS.sel_isOptionalArgumentWithName_1, argumentName != null ? argumentName.id : 0) != 0;
}

public NSString returnType() {
	int result = OS.objc_msgSend(this.id, OS.sel_returnType);
	return result != 0 ? new NSString(result) : null;
}

public NSString suiteName() {
	int result = OS.objc_msgSend(this.id, OS.sel_suiteName);
	return result != 0 ? new NSString(result) : null;
}

public NSString typeForArgumentWithName(NSString argumentName) {
	int result = OS.objc_msgSend(this.id, OS.sel_typeForArgumentWithName_1, argumentName != null ? argumentName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

}
