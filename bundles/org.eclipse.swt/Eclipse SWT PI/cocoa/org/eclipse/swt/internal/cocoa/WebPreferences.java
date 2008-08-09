/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class WebPreferences extends NSObject {

public WebPreferences() {
	super();
}

public WebPreferences(int id) {
	super(id);
}

public NSString identifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_identifier);
	return result != 0 ? new NSString(result) : null;
}

public id initWithIdentifier(NSString anIdentifier) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithIdentifier_1, anIdentifier != null ? anIdentifier.id : 0);
	return result != 0 ? new id(result) : null;
}

public void setJavaEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setJavaEnabled_1, flag);
}

public static WebPreferences standardPreferences() {
	int result = OS.objc_msgSend(OS.class_WebPreferences, OS.sel_standardPreferences);
	return result != 0 ? new WebPreferences(result) : null;
}

}
