/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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

public WebPreferences(long /*int*/ id) {
	super(id);
}

public WebPreferences(id id) {
	super(id);
}

public void setJavaEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setJavaEnabled_, flag);
}

public void setJavaScriptEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setJavaScriptEnabled_, flag);
}

public static WebPreferences standardPreferences() {
	long /*int*/ result = OS.objc_msgSend(OS.class_WebPreferences, OS.sel_standardPreferences);
	return result != 0 ? new WebPreferences(result) : null;
}

}
