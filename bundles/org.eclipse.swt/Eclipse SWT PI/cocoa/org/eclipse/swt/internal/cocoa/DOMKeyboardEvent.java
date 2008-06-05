/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class DOMKeyboardEvent extends NSObject {

public DOMKeyboardEvent() {
	super();
}

public DOMKeyboardEvent(int id) {
	super(id);
}

public boolean altKey() {
	return OS.objc_msgSend(this.id, OS.sel_altKey) != 0;
}

public int charCode() {
	return OS.objc_msgSend(this.id, OS.sel_charCode);
}

public boolean ctrlKey() {
	return OS.objc_msgSend(this.id, OS.sel_ctrlKey) != 0;
}

public boolean getModifierState(NSString keyIdentifierArg) {
	return OS.objc_msgSend(this.id, OS.sel_getModifierState_1, keyIdentifierArg != null ? keyIdentifierArg.id : 0) != 0;
}

public int keyCode() {
	return OS.objc_msgSend(this.id, OS.sel_keyCode);
}

public int keyLocation() {
	return OS.objc_msgSend(this.id, OS.sel_keyLocation);
}

public boolean metaKey() {
	return OS.objc_msgSend(this.id, OS.sel_metaKey) != 0;
}

public void preventDefault() {
	OS.objc_msgSend(this.id, OS.sel_preventDefault);
}

public boolean shiftKey() {
	return OS.objc_msgSend(this.id, OS.sel_shiftKey) != 0;
}

}
