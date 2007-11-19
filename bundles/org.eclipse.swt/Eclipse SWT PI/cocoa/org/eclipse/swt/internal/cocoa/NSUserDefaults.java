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

public class NSUserDefaults extends NSObject {

public NSUserDefaults() {
	super();
}

public NSUserDefaults(int id) {
	super(id);
}

public void addSuiteNamed(NSString suiteName) {
	OS.objc_msgSend(this.id, OS.sel_addSuiteNamed_1, suiteName != null ? suiteName.id : 0);
}

public NSArray arrayForKey(NSString defaultName) {
	int result = OS.objc_msgSend(this.id, OS.sel_arrayForKey_1, defaultName != null ? defaultName.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public boolean boolForKey(NSString defaultName) {
	return OS.objc_msgSend(this.id, OS.sel_boolForKey_1, defaultName != null ? defaultName.id : 0) != 0;
}

public NSData dataForKey(NSString defaultName) {
	int result = OS.objc_msgSend(this.id, OS.sel_dataForKey_1, defaultName != null ? defaultName.id : 0);
	return result != 0 ? new NSData(result) : null;
}

public NSDictionary dictionaryForKey(NSString defaultName) {
	int result = OS.objc_msgSend(this.id, OS.sel_dictionaryForKey_1, defaultName != null ? defaultName.id : 0);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSDictionary dictionaryRepresentation() {
	int result = OS.objc_msgSend(this.id, OS.sel_dictionaryRepresentation);
	return result != 0 ? new NSDictionary(result) : null;
}

public double doubleForKey(NSString defaultName) {
	return OS.objc_msgSend_fpret(this.id, OS.sel_doubleForKey_1, defaultName != null ? defaultName.id : 0);
}

public float floatForKey(NSString defaultName) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_floatForKey_1, defaultName != null ? defaultName.id : 0);
}

public id initWithUser(NSString username) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithUser_1, username != null ? username.id : 0);
	return result != 0 ? new id(result) : null;
}

public int integerForKey(NSString defaultName) {
	return OS.objc_msgSend(this.id, OS.sel_integerForKey_1, defaultName != null ? defaultName.id : 0);
}

public id objectForKey(NSString defaultName) {
	int result = OS.objc_msgSend(this.id, OS.sel_objectForKey_1, defaultName != null ? defaultName.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean objectIsForcedForKey_(NSString key) {
	return OS.objc_msgSend(this.id, OS.sel_objectIsForcedForKey_1, key != null ? key.id : 0) != 0;
}

public boolean objectIsForcedForKey_inDomain_(NSString key, NSString domain) {
	return OS.objc_msgSend(this.id, OS.sel_objectIsForcedForKey_1inDomain_1, key != null ? key.id : 0, domain != null ? domain.id : 0) != 0;
}

public NSDictionary persistentDomainForName(NSString domainName) {
	int result = OS.objc_msgSend(this.id, OS.sel_persistentDomainForName_1, domainName != null ? domainName.id : 0);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSArray persistentDomainNames() {
	int result = OS.objc_msgSend(this.id, OS.sel_persistentDomainNames);
	return result != 0 ? new NSArray(result) : null;
}

public void registerDefaults(NSDictionary registrationDictionary) {
	OS.objc_msgSend(this.id, OS.sel_registerDefaults_1, registrationDictionary != null ? registrationDictionary.id : 0);
}

public void removeObjectForKey(NSString defaultName) {
	OS.objc_msgSend(this.id, OS.sel_removeObjectForKey_1, defaultName != null ? defaultName.id : 0);
}

public void removePersistentDomainForName(NSString domainName) {
	OS.objc_msgSend(this.id, OS.sel_removePersistentDomainForName_1, domainName != null ? domainName.id : 0);
}

public void removeSuiteNamed(NSString suiteName) {
	OS.objc_msgSend(this.id, OS.sel_removeSuiteNamed_1, suiteName != null ? suiteName.id : 0);
}

public void removeVolatileDomainForName(NSString domainName) {
	OS.objc_msgSend(this.id, OS.sel_removeVolatileDomainForName_1, domainName != null ? domainName.id : 0);
}

public static void resetStandardUserDefaults() {
	OS.objc_msgSend(OS.class_NSUserDefaults, OS.sel_resetStandardUserDefaults);
}

public void setBool(boolean value, NSString defaultName) {
	OS.objc_msgSend(this.id, OS.sel_setBool_1forKey_1, value, defaultName != null ? defaultName.id : 0);
}

public void setDouble(double value, NSString defaultName) {
	OS.objc_msgSend(this.id, OS.sel_setDouble_1forKey_1, value, defaultName != null ? defaultName.id : 0);
}

public void setFloat(float value, NSString defaultName) {
	OS.objc_msgSend(this.id, OS.sel_setFloat_1forKey_1, value, defaultName != null ? defaultName.id : 0);
}

public void setInteger(int value, NSString defaultName) {
	OS.objc_msgSend(this.id, OS.sel_setInteger_1forKey_1, value, defaultName != null ? defaultName.id : 0);
}

public void setObject(id value, NSString defaultName) {
	OS.objc_msgSend(this.id, OS.sel_setObject_1forKey_1, value != null ? value.id : 0, defaultName != null ? defaultName.id : 0);
}

public void setPersistentDomain(NSDictionary domain, NSString domainName) {
	OS.objc_msgSend(this.id, OS.sel_setPersistentDomain_1forName_1, domain != null ? domain.id : 0, domainName != null ? domainName.id : 0);
}

public void setVolatileDomain(NSDictionary domain, NSString domainName) {
	OS.objc_msgSend(this.id, OS.sel_setVolatileDomain_1forName_1, domain != null ? domain.id : 0, domainName != null ? domainName.id : 0);
}

public static NSUserDefaults standardUserDefaults() {
	int result = OS.objc_msgSend(OS.class_NSUserDefaults, OS.sel_standardUserDefaults);
	return result != 0 ? new NSUserDefaults(result) : null;
}

public NSArray stringArrayForKey(NSString defaultName) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringArrayForKey_1, defaultName != null ? defaultName.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSString stringForKey(NSString defaultName) {
	int result = OS.objc_msgSend(this.id, OS.sel_stringForKey_1, defaultName != null ? defaultName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public boolean synchronize() {
	return OS.objc_msgSend(this.id, OS.sel_synchronize) != 0;
}

public NSDictionary volatileDomainForName(NSString domainName) {
	int result = OS.objc_msgSend(this.id, OS.sel_volatileDomainForName_1, domainName != null ? domainName.id : 0);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSArray volatileDomainNames() {
	int result = OS.objc_msgSend(this.id, OS.sel_volatileDomainNames);
	return result != 0 ? new NSArray(result) : null;
}

}
