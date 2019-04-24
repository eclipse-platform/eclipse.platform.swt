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

public class NSUserDefaults extends NSObject {

public NSUserDefaults() {
	super();
}

public NSUserDefaults(long id) {
	super(id);
}

public NSUserDefaults(id id) {
	super(id);
}

public id objectForKey(NSString defaultName) {
	long result = OS.objc_msgSend(this.id, OS.sel_objectForKey_, defaultName != null ? defaultName.id : 0);
	return result != 0 ? new id(result) : null;
}

public void setInteger(long value, NSString defaultName) {
	OS.objc_msgSend(this.id, OS.sel_setInteger_forKey_, value, defaultName != null ? defaultName.id : 0);
}

public static NSUserDefaults standardUserDefaults() {
	long result = OS.objc_msgSend(OS.class_NSUserDefaults, OS.sel_standardUserDefaults);
	return result != 0 ? new NSUserDefaults(result) : null;
}

public NSString stringForKey(NSString defaultName) {
	long result = OS.objc_msgSend(this.id, OS.sel_stringForKey_, defaultName != null ? defaultName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

}
