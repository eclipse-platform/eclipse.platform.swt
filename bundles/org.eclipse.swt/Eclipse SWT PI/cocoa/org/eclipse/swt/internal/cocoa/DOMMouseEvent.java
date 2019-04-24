/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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

public class DOMMouseEvent extends DOMUIEvent {

public DOMMouseEvent() {
	super();
}

public DOMMouseEvent(long id) {
	super(id);
}

public DOMMouseEvent(id id) {
	super(id);
}

public boolean altKey() {
	return OS.objc_msgSend_bool(this.id, OS.sel_altKey);
}

public short button() {
	return (short)OS.objc_msgSend(this.id, OS.sel_button);
}

public boolean ctrlKey() {
	return OS.objc_msgSend_bool(this.id, OS.sel_ctrlKey);
}

public boolean metaKey() {
	return OS.objc_msgSend_bool(this.id, OS.sel_metaKey);
}

public int screenX() {
	return (int)OS.objc_msgSend(this.id, OS.sel_screenX);
}

public int screenY() {
	return (int)OS.objc_msgSend(this.id, OS.sel_screenY);
}

public boolean shiftKey() {
	return OS.objc_msgSend_bool(this.id, OS.sel_shiftKey);
}

}
