/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class DOMWheelEvent extends NSObject {

public DOMWheelEvent() {
	super();
}

public DOMWheelEvent(long /*int*/ id) {
	super(id);
}

public DOMWheelEvent(id id) {
	super(id);
}

public boolean altKey() {
	return OS.objc_msgSend_bool(this.id, OS.sel_altKey);
}

public boolean ctrlKey() {
	return OS.objc_msgSend_bool(this.id, OS.sel_ctrlKey);
}

public boolean metaKey() {
	return OS.objc_msgSend_bool(this.id, OS.sel_metaKey);
}

public int screenX() {
	return (int)/*64*/OS.objc_msgSend(this.id, OS.sel_screenX);
}

public int screenY() {
	return (int)/*64*/OS.objc_msgSend(this.id, OS.sel_screenY);
}

public boolean shiftKey() {
	return OS.objc_msgSend_bool(this.id, OS.sel_shiftKey);
}

public int wheelDelta() {
	return (int)/*64*/OS.objc_msgSend(this.id, OS.sel_wheelDelta);
}

}
