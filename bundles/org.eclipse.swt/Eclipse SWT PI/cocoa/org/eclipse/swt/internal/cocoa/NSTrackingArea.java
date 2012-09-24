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

public class NSTrackingArea extends NSObject {

public NSTrackingArea() {
	super();
}

public NSTrackingArea(long /*int*/ id) {
	super(id);
}

public NSTrackingArea(id id) {
	super(id);
}

public NSTrackingArea initWithRect(NSRect rect, long /*int*/ options, id owner, NSDictionary userInfo) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithRect_options_owner_userInfo_, rect, options, owner != null ? owner.id : 0, userInfo != null ? userInfo.id : 0);
	return result == this.id ? this : (result != 0 ? new NSTrackingArea(result) : null);
}

public id owner() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_owner);
	return result != 0 ? new id(result) : null;
}

public NSDictionary userInfo() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_userInfo);
	return result != 0 ? new NSDictionary(result) : null;
}

}
