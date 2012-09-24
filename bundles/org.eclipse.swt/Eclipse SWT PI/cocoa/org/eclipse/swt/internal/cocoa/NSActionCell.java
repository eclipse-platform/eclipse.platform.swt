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

public class NSActionCell extends NSCell {

public NSActionCell() {
	super();
}

public NSActionCell(long /*int*/ id) {
	super(id);
}

public NSActionCell(id id) {
	super(id);
}

public long /*int*/ action() {
	return OS.objc_msgSend(this.id, OS.sel_action);
}

public void setAction(long /*int*/ aSelector) {
	OS.objc_msgSend(this.id, OS.sel_setAction_, aSelector);
}

public void setTarget(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setTarget_, anObject != null ? anObject.id : 0);
}

public id target() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_target);
	return result != 0 ? new id(result) : null;
}

}
