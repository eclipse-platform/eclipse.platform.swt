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

public class NSError extends NSObject {

public NSError() {
	super();
}

public NSError(long /*int*/ id) {
	super(id);
}

public NSError(id id) {
	super(id);
}

public long /*int*/ code() {
	return OS.objc_msgSend(this.id, OS.sel_code);
}

public NSString localizedDescription() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_localizedDescription);
	return result != 0 ? new NSString(result) : null;
}

public NSDictionary userInfo() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_userInfo);
	return result != 0 ? new NSDictionary(result) : null;
}

}
