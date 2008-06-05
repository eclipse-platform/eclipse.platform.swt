/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSBundle extends NSObject {

public NSBundle() {
	super();
}

public NSBundle(int id) {
	super(id);
}

public static NSArray allBundles() {
	int result = OS.objc_msgSend(OS.class_NSBundle, OS.sel_allBundles);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray allFrameworks() {
	int result = OS.objc_msgSend(OS.class_NSBundle, OS.sel_allFrameworks);
	return result != 0 ? new NSArray(result) : null;
}

public NSString builtInPlugInsPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_builtInPlugInsPath);
	return result != 0 ? new NSString(result) : null;
}

public static NSBundle bundleForClass(int aClass) {
	int result = OS.objc_msgSend(OS.class_NSBundle, OS.sel_bundleForClass_1, aClass);
	return result != 0 ? new NSBundle(result) : null;
}

public NSString bundleIdentifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_bundleIdentifier);
	return result != 0 ? new NSString(result) : null;
}

public NSString bundlePath() {
	int result = OS.objc_msgSend(this.id, OS.sel_bundlePath);
	return result != 0 ? new NSString(result) : null;
}

public static NSBundle bundleWithIdentifier(NSString identifier) {
	int result = OS.objc_msgSend(OS.class_NSBundle, OS.sel_bundleWithIdentifier_1, identifier != null ? identifier.id : 0);
	return result != 0 ? new NSBundle(result) : null;
}

public static NSBundle bundleWithPath(NSString path) {
	int result = OS.objc_msgSend(OS.class_NSBundle, OS.sel_bundleWithPath_1, path != null ? path.id : 0);
	return result != 0 ? new NSBundle(result) : null;
}

public int classNamed(NSString className) {
	return OS.objc_msgSend(this.id, OS.sel_classNamed_1, className != null ? className.id : 0);
}

public NSString developmentLocalization() {
	int result = OS.objc_msgSend(this.id, OS.sel_developmentLocalization);
	return result != 0 ? new NSString(result) : null;
}

public NSArray executableArchitectures() {
	int result = OS.objc_msgSend(this.id, OS.sel_executableArchitectures);
	return result != 0 ? new NSArray(result) : null;
}

public NSString executablePath() {
	int result = OS.objc_msgSend(this.id, OS.sel_executablePath);
	return result != 0 ? new NSString(result) : null;
}

public NSDictionary infoDictionary() {
	int result = OS.objc_msgSend(this.id, OS.sel_infoDictionary);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSBundle initWithPath(NSString path) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithPath_1, path != null ? path.id : 0);
	return result != 0 ? this : null;
}

public boolean isLoaded() {
	return OS.objc_msgSend(this.id, OS.sel_isLoaded) != 0;
}

//public boolean load() {
//	return OS.objc_msgSend(this.id, OS.sel_load) != 0;
//}

public boolean loadAndReturnError(int error) {
	return OS.objc_msgSend(this.id, OS.sel_loadAndReturnError_1, error) != 0;
}

public NSArray localizations() {
	int result = OS.objc_msgSend(this.id, OS.sel_localizations);
	return result != 0 ? new NSArray(result) : null;
}

public NSDictionary localizedInfoDictionary() {
	int result = OS.objc_msgSend(this.id, OS.sel_localizedInfoDictionary);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSString localizedStringForKey(NSString key, NSString value, NSString tableName) {
	int result = OS.objc_msgSend(this.id, OS.sel_localizedStringForKey_1value_1table_1, key != null ? key.id : 0, value != null ? value.id : 0, tableName != null ? tableName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public static NSBundle mainBundle() {
	int result = OS.objc_msgSend(OS.class_NSBundle, OS.sel_mainBundle);
	return result != 0 ? new NSBundle(result) : null;
}

public id objectForInfoDictionaryKey(NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_objectForInfoDictionaryKey_1, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSString pathForAuxiliaryExecutable(NSString executableName) {
	int result = OS.objc_msgSend(this.id, OS.sel_pathForAuxiliaryExecutable_1, executableName != null ? executableName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSString pathForResource_ofType_(NSString name, NSString ext) {
	int result = OS.objc_msgSend(this.id, OS.sel_pathForResource_1ofType_1, name != null ? name.id : 0, ext != null ? ext.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSString pathForResource_ofType_inDirectory_(NSString name, NSString ext, NSString subpath) {
	int result = OS.objc_msgSend(this.id, OS.sel_pathForResource_1ofType_1inDirectory_1, name != null ? name.id : 0, ext != null ? ext.id : 0, subpath != null ? subpath.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public static NSString static_pathForResource_ofType_inDirectory_(NSString name, NSString ext, NSString bundlePath) {
	int result = OS.objc_msgSend(OS.class_NSBundle, OS.sel_pathForResource_1ofType_1inDirectory_1, name != null ? name.id : 0, ext != null ? ext.id : 0, bundlePath != null ? bundlePath.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public NSString pathForResource_ofType_inDirectory_forLocalization_(NSString name, NSString ext, NSString subpath, NSString localizationName) {
	int result = OS.objc_msgSend(this.id, OS.sel_pathForResource_1ofType_1inDirectory_1forLocalization_1, name != null ? name.id : 0, ext != null ? ext.id : 0, subpath != null ? subpath.id : 0, localizationName != null ? localizationName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public static NSArray static_pathsForResourcesOfType_inDirectory_(NSString ext, NSString bundlePath) {
	int result = OS.objc_msgSend(OS.class_NSBundle, OS.sel_pathsForResourcesOfType_1inDirectory_1, ext != null ? ext.id : 0, bundlePath != null ? bundlePath.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray pathsForResourcesOfType_inDirectory_(NSString ext, NSString subpath) {
	int result = OS.objc_msgSend(this.id, OS.sel_pathsForResourcesOfType_1inDirectory_1, ext != null ? ext.id : 0, subpath != null ? subpath.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray pathsForResourcesOfType_inDirectory_forLocalization_(NSString ext, NSString subpath, NSString localizationName) {
	int result = OS.objc_msgSend(this.id, OS.sel_pathsForResourcesOfType_1inDirectory_1forLocalization_1, ext != null ? ext.id : 0, subpath != null ? subpath.id : 0, localizationName != null ? localizationName.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray preferredLocalizations() {
	int result = OS.objc_msgSend(this.id, OS.sel_preferredLocalizations);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray static_preferredLocalizationsFromArray_(NSArray localizationsArray) {
	int result = OS.objc_msgSend(OS.class_NSBundle, OS.sel_preferredLocalizationsFromArray_1, localizationsArray != null ? localizationsArray.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray static_preferredLocalizationsFromArray_forPreferences_(NSArray localizationsArray, NSArray preferencesArray) {
	int result = OS.objc_msgSend(OS.class_NSBundle, OS.sel_preferredLocalizationsFromArray_1forPreferences_1, localizationsArray != null ? localizationsArray.id : 0, preferencesArray != null ? preferencesArray.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public boolean preflightAndReturnError(int error) {
	return OS.objc_msgSend(this.id, OS.sel_preflightAndReturnError_1, error) != 0;
}

public int principalClass() {
	return OS.objc_msgSend(this.id, OS.sel_principalClass);
}

public NSString privateFrameworksPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_privateFrameworksPath);
	return result != 0 ? new NSString(result) : null;
}

public NSString resourcePath() {
	int result = OS.objc_msgSend(this.id, OS.sel_resourcePath);
	return result != 0 ? new NSString(result) : null;
}

public NSString sharedFrameworksPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_sharedFrameworksPath);
	return result != 0 ? new NSString(result) : null;
}

public NSString sharedSupportPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_sharedSupportPath);
	return result != 0 ? new NSString(result) : null;
}

public boolean unload() {
	return OS.objc_msgSend(this.id, OS.sel_unload) != 0;
}

public static boolean loadNibFile(id fileName, id dict, id zone) {
	return OS.objc_msgSend(OS.class_NSBundle, OS.sel_loadNibFile_1externalNameTable_1withZone_1, fileName.id, dict.id, 0) != 0;
}

}
