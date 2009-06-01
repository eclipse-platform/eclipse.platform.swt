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

public class DOMWheelEvent extends NSObject {

public DOMWheelEvent() {
	super();
}

public DOMWheelEvent(int /*long*/ id) {
	super(id);
}

public DOMWheelEvent(id id) {
	super(id);
}

public boolean altKey() {
	return OS.objc_msgSend_bool(this.id, OS.sel_altKey);
}

public int clientX() {
	return (int)/*64*/OS.objc_msgSend(this.id, OS.sel_clientX);
}

public int clientY() {
	return (int)/*64*/OS.objc_msgSend(this.id, OS.sel_clientY);
}

public boolean ctrlKey() {
	return OS.objc_msgSend_bool(this.id, OS.sel_ctrlKey);
}

public boolean metaKey() {
	return OS.objc_msgSend_bool(this.id, OS.sel_metaKey);
}

public boolean shiftKey() {
	return OS.objc_msgSend_bool(this.id, OS.sel_shiftKey);
}

public int wheelDelta() {
	return (int)/*64*/OS.objc_msgSend(this.id, OS.sel_wheelDelta);
}

}
