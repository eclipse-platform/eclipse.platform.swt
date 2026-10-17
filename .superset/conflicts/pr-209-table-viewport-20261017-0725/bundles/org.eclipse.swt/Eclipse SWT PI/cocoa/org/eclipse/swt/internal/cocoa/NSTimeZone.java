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

public class NSTimeZone extends NSObject {

public NSTimeZone() {
	super();
}

public NSTimeZone(long id) {
	super(id);
}

public NSTimeZone(id id) {
	super(id);
}

public static NSTimeZone defaultTimeZone() {
	long result = OS.objc_msgSend(OS.class_NSTimeZone, OS.sel_defaultTimeZone);
	return result != 0 ? new NSTimeZone(result) : null;
}

}
