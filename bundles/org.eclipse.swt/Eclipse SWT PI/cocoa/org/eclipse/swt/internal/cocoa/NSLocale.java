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

public class NSLocale extends NSObject {

public NSLocale() {
	super();
}

public NSLocale(int id) {
	super(id);
}

public static NSArray ISOCountryCodes() {
	int result = OS.objc_msgSend(OS.class_NSLocale, OS.sel_ISOCountryCodes);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray ISOCurrencyCodes() {
	int result = OS.objc_msgSend(OS.class_NSLocale, OS.sel_ISOCurrencyCodes);
	return result != 0 ? new NSArray(result) : null;
}

public static NSArray ISOLanguageCodes() {
	int result = OS.objc_msgSend(OS.class_NSLocale, OS.sel_ISOLanguageCodes);
	return result != 0 ? new NSArray(result) : null;
}

public static id autoupdatingCurrentLocale() {
	int result = OS.objc_msgSend(OS.class_NSLocale, OS.sel_autoupdatingCurrentLocale);
	return result != 0 ? new id(result) : null;
}

public static NSArray availableLocaleIdentifiers() {
	int result = OS.objc_msgSend(OS.class_NSLocale, OS.sel_availableLocaleIdentifiers);
	return result != 0 ? new NSArray(result) : null;
}

public static NSString canonicalLocaleIdentifierFromString(NSString string) {
	int result = OS.objc_msgSend(OS.class_NSLocale, OS.sel_canonicalLocaleIdentifierFromString_1, string != null ? string.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public static NSArray commonISOCurrencyCodes() {
	int result = OS.objc_msgSend(OS.class_NSLocale, OS.sel_commonISOCurrencyCodes);
	return result != 0 ? new NSArray(result) : null;
}

public static NSDictionary componentsFromLocaleIdentifier(NSString string) {
	int result = OS.objc_msgSend(OS.class_NSLocale, OS.sel_componentsFromLocaleIdentifier_1, string != null ? string.id : 0);
	return result != 0 ? new NSDictionary(result) : null;
}

public static id currentLocale() {
	int result = OS.objc_msgSend(OS.class_NSLocale, OS.sel_currentLocale);
	return result != 0 ? new id(result) : null;
}

public NSString displayNameForKey(id key, id value) {
	int result = OS.objc_msgSend(this.id, OS.sel_displayNameForKey_1value_1, key != null ? key.id : 0, value != null ? value.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public id initWithLocaleIdentifier(NSString string) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithLocaleIdentifier_1, string != null ? string.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSString localeIdentifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_localeIdentifier);
	return result != 0 ? new NSString(result) : null;
}

public static NSString localeIdentifierFromComponents(NSDictionary dict) {
	int result = OS.objc_msgSend(OS.class_NSLocale, OS.sel_localeIdentifierFromComponents_1, dict != null ? dict.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public id objectForKey(id key) {
	int result = OS.objc_msgSend(this.id, OS.sel_objectForKey_1, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public static NSArray preferredLanguages() {
	int result = OS.objc_msgSend(OS.class_NSLocale, OS.sel_preferredLanguages);
	return result != 0 ? new NSArray(result) : null;
}

public static id systemLocale() {
	int result = OS.objc_msgSend(OS.class_NSLocale, OS.sel_systemLocale);
	return result != 0 ? new id(result) : null;
}

}
