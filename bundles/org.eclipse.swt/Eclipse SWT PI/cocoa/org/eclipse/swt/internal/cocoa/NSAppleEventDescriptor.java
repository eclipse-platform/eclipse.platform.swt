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

public class NSAppleEventDescriptor extends NSObject {

public NSAppleEventDescriptor() {
	super();
}

public NSAppleEventDescriptor(long id) {
	super(id);
}

public NSAppleEventDescriptor(id id) {
	super(id);
}

public NSAppleEventDescriptor initListDescriptor() {
	long result = OS.objc_msgSend(this.id, OS.sel_initListDescriptor);
	return result == this.id ? this : (result != 0 ? new NSAppleEventDescriptor(result) : null);
}

}
