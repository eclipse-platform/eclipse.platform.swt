/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class WebPreferences extends NSObject {

public WebPreferences() {
	super();
}

public WebPreferences(long id) {
	super(id);
}

public WebPreferences(id id) {
	super(id);
}

public void setJavaEnabled(boolean javaEnabled) {
	OS.objc_msgSend(this.id, OS.sel_setJavaEnabled_, javaEnabled);
}

public void setJavaScriptEnabled(boolean javaScriptEnabled) {
	OS.objc_msgSend(this.id, OS.sel_setJavaScriptEnabled_, javaScriptEnabled);
}

public static WebPreferences standardPreferences() {
	long result = OS.objc_msgSend(OS.class_WebPreferences, OS.sel_standardPreferences);
	return result != 0 ? new WebPreferences(result) : null;
}

}
