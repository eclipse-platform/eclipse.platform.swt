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

public class DOMKeyboardEvent extends DOMUIEvent {

public DOMKeyboardEvent() {
	super();
}

public DOMKeyboardEvent(int /*long*/ id) {
	super(id);
}

public DOMKeyboardEvent(id id) {
	super(id);
}

public boolean altKey() {
	return OS.objc_msgSend_bool(this.id, OS.sel_altKey);
}

public int charCode() {
	return (int)/*64*/OS.objc_msgSend(this.id, OS.sel_charCode);
}

public boolean ctrlKey() {
	return OS.objc_msgSend_bool(this.id, OS.sel_ctrlKey);
}

public int keyCode() {
	return (int)/*64*/OS.objc_msgSend(this.id, OS.sel_keyCode);
}

public boolean metaKey() {
	return OS.objc_msgSend_bool(this.id, OS.sel_metaKey);
}

public boolean shiftKey() {
	return OS.objc_msgSend_bool(this.id, OS.sel_shiftKey);
}

}
