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

public class NSNotification extends NSObject {

public NSNotification() {
	super();
}

public NSNotification(long id) {
	super(id);
}

public NSNotification(id id) {
	super(id);
}

public id object() {
	long result = OS.objc_msgSend(this.id, OS.sel_object);
	return result != 0 ? new id(result) : null;
}

public NSDictionary userInfo() {
	long result = OS.objc_msgSend(this.id, OS.sel_userInfo);
	return result != 0 ? new NSDictionary(result) : null;
}

}
