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

public class NSError extends NSObject {

public NSError() {
	super();
}

public NSError(long id) {
	super(id);
}

public NSError(id id) {
	super(id);
}

public long code() {
	return OS.objc_msgSend(this.id, OS.sel_code);
}

public NSString localizedDescription() {
	long result = OS.objc_msgSend(this.id, OS.sel_localizedDescription);
	return result != 0 ? new NSString(result) : null;
}

public NSDictionary userInfo() {
	long result = OS.objc_msgSend(this.id, OS.sel_userInfo);
	return result != 0 ? new NSDictionary(result) : null;
}

}
