/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSTouch extends NSObject {

public NSTouch() {
	super();
}

public NSTouch(int /*long*/ id) {
	super(id);
}

public NSTouch(id id) {
	super(id);
}

public id device() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_device);
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

public int /*long*/ phase() {
	return OS.objc_msgSend(this.id, OS.sel_phase);
}

}
