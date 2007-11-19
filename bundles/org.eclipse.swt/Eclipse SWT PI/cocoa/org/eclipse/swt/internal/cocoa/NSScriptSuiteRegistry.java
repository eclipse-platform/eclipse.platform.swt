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

public class NSScriptSuiteRegistry extends NSObject {

public NSScriptSuiteRegistry() {
	super();
}

public NSScriptSuiteRegistry(int id) {
	super(id);
}

public NSData aeteResource(NSString languageName) {
	int result = OS.objc_msgSend(this.id, OS.sel_aeteResource_1, languageName != null ? languageName.id : 0);
	return result != 0 ? new NSData(result) : null;
}

public int appleEventCodeForSuite(NSString suiteName) {
	return OS.objc_msgSend(this.id, OS.sel_appleEventCodeForSuite_1, suiteName != null ? suiteName.id : 0);
}

public NSBundle bundleForSuite(NSString suiteName) {
	int result = OS.objc_msgSend(this.id, OS.sel_bundleForSuite_1, suiteName != null ? suiteName.id : 0);
	return result != 0 ? new NSBundle(result) : null;
}

public NSScriptClassDescription classDescriptionWithAppleEventCode(int appleEventCode) {
	int result = OS.objc_msgSend(this.id, OS.sel_classDescriptionWithAppleEventCode_1, appleEventCode);
	return result != 0 ? new NSScriptClassDescription(result) : null;
}

public NSDictionary classDescriptionsInSuite(NSString suiteName) {
	int result = OS.objc_msgSend(this.id, OS.sel_classDescriptionsInSuite_1, suiteName != null ? suiteName.id : 0);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSScriptCommandDescription commandDescriptionWithAppleEventClass(int appleEventClassCode, int appleEventIDCode) {
	int result = OS.objc_msgSend(this.id, OS.sel_commandDescriptionWithAppleEventClass_1andAppleEventCode_1, appleEventClassCode, appleEventIDCode);
	return result != 0 ? new NSScriptCommandDescription(result) : null;
}

public NSDictionary commandDescriptionsInSuite(NSString suiteName) {
	int result = OS.objc_msgSend(this.id, OS.sel_commandDescriptionsInSuite_1, suiteName != null ? suiteName.id : 0);
	return result != 0 ? new NSDictionary(result) : null;
}

public void loadSuiteWithDictionary(NSDictionary suiteDeclaration, NSBundle bundle) {
	OS.objc_msgSend(this.id, OS.sel_loadSuiteWithDictionary_1fromBundle_1, suiteDeclaration != null ? suiteDeclaration.id : 0, bundle != null ? bundle.id : 0);
}

public void loadSuitesFromBundle(NSBundle bundle) {
	OS.objc_msgSend(this.id, OS.sel_loadSuitesFromBundle_1, bundle != null ? bundle.id : 0);
}

public void registerClassDescription(NSScriptClassDescription classDescription) {
	OS.objc_msgSend(this.id, OS.sel_registerClassDescription_1, classDescription != null ? classDescription.id : 0);
}

public void registerCommandDescription(NSScriptCommandDescription commandDescription) {
	OS.objc_msgSend(this.id, OS.sel_registerCommandDescription_1, commandDescription != null ? commandDescription.id : 0);
}

public static void setSharedScriptSuiteRegistry(NSScriptSuiteRegistry registry) {
	OS.objc_msgSend(OS.class_NSScriptSuiteRegistry, OS.sel_setSharedScriptSuiteRegistry_1, registry != null ? registry.id : 0);
}

public static NSScriptSuiteRegistry sharedScriptSuiteRegistry() {
	int result = OS.objc_msgSend(OS.class_NSScriptSuiteRegistry, OS.sel_sharedScriptSuiteRegistry);
	return result != 0 ? new NSScriptSuiteRegistry(result) : null;
}

public NSString suiteForAppleEventCode(int appleEventCode) {
	int result = OS.objc_msgSend(this.id, OS.sel_suiteForAppleEventCode_1, appleEventCode);
	return result != 0 ? new NSString(result) : null;
}

public NSArray suiteNames() {
	int result = OS.objc_msgSend(this.id, OS.sel_suiteNames);
	return result != 0 ? new NSArray(result) : null;
}

}
