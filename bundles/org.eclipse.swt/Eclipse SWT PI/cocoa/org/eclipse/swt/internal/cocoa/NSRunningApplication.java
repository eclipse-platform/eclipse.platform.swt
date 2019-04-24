/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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

public class NSRunningApplication extends NSObject {

public NSRunningApplication() {
	super();
}

public NSRunningApplication(long id) {
	super(id);
}

public NSRunningApplication(id id) {
	super(id);
}

public boolean activateWithOptions(long options) {
	return OS.objc_msgSend_bool(this.id, OS.sel_activateWithOptions_, options);
}

public static NSRunningApplication currentApplication() {
	long result = OS.objc_msgSend(OS.class_NSRunningApplication, OS.sel_currentApplication);
	return result != 0 ? new NSRunningApplication(result) : null;
}

}
