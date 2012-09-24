/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSUserDefaults extends NSObject {

public NSUserDefaults() {
	super();
}

public NSUserDefaults(long /*int*/ id) {
	super(id);
}

public NSUserDefaults(id id) {
	super(id);
}

public id objectForKey(NSString defaultName) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_objectForKey_, defaultName != null ? defaultName.id : 0);
	return result != 0 ? new id(result) : null;
}

public static NSUserDefaults standardUserDefaults() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSUserDefaults, OS.sel_standardUserDefaults);
	return result != 0 ? new NSUserDefaults(result) : null;
}

}
