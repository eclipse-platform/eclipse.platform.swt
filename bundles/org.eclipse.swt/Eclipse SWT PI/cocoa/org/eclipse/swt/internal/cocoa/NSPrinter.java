/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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

public class NSPrinter extends NSObject {

public NSPrinter() {
	super();
}

public NSPrinter(long id) {
	super(id);
}

public NSPrinter(id id) {
	super(id);
}

public NSString name() {
	long result = OS.objc_msgSend(this.id, OS.sel_name);
	return result != 0 ? new NSString(result) : null;
}

public static NSArray printerNames() {
	long result = OS.objc_msgSend(OS.class_NSPrinter, OS.sel_printerNames);
	return result != 0 ? new NSArray(result) : null;
}

public static NSPrinter printerWithName(NSString name) {
	long result = OS.objc_msgSend(OS.class_NSPrinter, OS.sel_printerWithName_, name != null ? name.id : 0);
	return result != 0 ? new NSPrinter(result) : null;
}

}
