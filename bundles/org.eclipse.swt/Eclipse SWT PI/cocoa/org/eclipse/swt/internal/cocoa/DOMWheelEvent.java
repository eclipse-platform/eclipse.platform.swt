/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class DOMWheelEvent extends DOMMouseEvent {

public DOMWheelEvent() {
	super();
}

public DOMWheelEvent(long /*int*/ id) {
	super(id);
}

public DOMWheelEvent(id id) {
	super(id);
}

public int wheelDelta() {
	return (int)/*64*/OS.objc_msgSend(this.id, OS.sel_wheelDelta);
}

public int wheelDeltaX() {
	return (int)/*64*/OS.objc_msgSend(this.id, OS.sel_wheelDeltaX);
}

public int wheelDeltaY() {
	return (int)/*64*/OS.objc_msgSend(this.id, OS.sel_wheelDeltaY);
}

}
