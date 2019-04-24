/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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

public class NSActionCell extends NSCell {

public NSActionCell() {
	super();
}

public NSActionCell(long id) {
	super(id);
}

public NSActionCell(id id) {
	super(id);
}

public long action() {
	return OS.objc_msgSend(this.id, OS.sel_action);
}

public void setAction(long action) {
	OS.objc_msgSend(this.id, OS.sel_setAction_, action);
}

public void setTarget(id target) {
	OS.objc_msgSend(this.id, OS.sel_setTarget_, target != null ? target.id : 0);
}

public id target() {
	long result = OS.objc_msgSend(this.id, OS.sel_target);
	return result != 0 ? new id(result) : null;
}

}
