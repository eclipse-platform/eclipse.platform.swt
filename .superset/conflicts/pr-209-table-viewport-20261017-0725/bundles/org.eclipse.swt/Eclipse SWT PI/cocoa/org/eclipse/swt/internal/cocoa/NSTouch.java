/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
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

public class NSTouch extends NSObject {

public NSTouch() {
	super();
}

public NSTouch(long id) {
	super(id);
}

public NSTouch(id id) {
	super(id);
}

public id device() {
	long result = OS.objc_msgSend(this.id, OS.sel_device);
	return result != 0 ? new id(result) : null;
}

public NSSize deviceSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_deviceSize);
	return result;
}

public boolean isResting() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isResting);
}

public NSPoint normalizedPosition() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_normalizedPosition);
	return result;
}

public long phase() {
	return OS.objc_msgSend(this.id, OS.sel_phase);
}

}
