/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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

public class NSProcessInfo extends NSObject {

public NSProcessInfo() {
	super();
}

public NSProcessInfo(long id) {
	super(id);
}

public NSProcessInfo(id id) {
	super(id);
}

public NSOperatingSystemVersion operatingSystemVersion() {
	NSOperatingSystemVersion result = new NSOperatingSystemVersion();
	OS.objc_msgSend_stret(result, this.id, OS.sel_operatingSystemVersion);
	return result;
}

public static NSProcessInfo processInfo() {
	long result = OS.objc_msgSend(OS.class_NSProcessInfo, OS.sel_processInfo);
	return result != 0 ? new NSProcessInfo(result) : null;
}

}
