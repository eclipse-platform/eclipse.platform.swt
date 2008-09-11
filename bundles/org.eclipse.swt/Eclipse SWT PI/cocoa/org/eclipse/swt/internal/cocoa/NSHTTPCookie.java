/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSHTTPCookie extends NSObject {

public NSHTTPCookie() {
	super();
}

public NSHTTPCookie(int /*long*/ id) {
	super(id);
}

public NSHTTPCookie(id id) {
	super(id);
}

public boolean isSessionOnly() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isSessionOnly);
}

}
