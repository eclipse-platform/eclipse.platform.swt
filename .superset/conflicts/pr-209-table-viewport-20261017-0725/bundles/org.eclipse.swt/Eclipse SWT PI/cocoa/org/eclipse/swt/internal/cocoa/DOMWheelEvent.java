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

public class DOMWheelEvent extends DOMMouseEvent {

public DOMWheelEvent() {
	super();
}

public DOMWheelEvent(long id) {
	super(id);
}

public DOMWheelEvent(id id) {
	super(id);
}

public int wheelDelta() {
	return (int)OS.objc_msgSend(this.id, OS.sel_wheelDelta);
}

public int wheelDeltaX() {
	return (int)OS.objc_msgSend(this.id, OS.sel_wheelDeltaX);
}

public int wheelDeltaY() {
	return (int)OS.objc_msgSend(this.id, OS.sel_wheelDeltaY);
}

}
