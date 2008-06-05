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

public class DOMWheelEvent extends NSObject {

public DOMWheelEvent() {
	super();
}

public DOMWheelEvent(int id) {
	super(id);
}

public boolean altKey() {
	return OS.objc_msgSend(this.id, OS.sel_altKey) != 0;
}

public int clientX() {
	return OS.objc_msgSend(this.id, OS.sel_clientX);
}

public int clientY() {
	return OS.objc_msgSend(this.id, OS.sel_clientY);
}

public boolean ctrlKey() {
	return OS.objc_msgSend(this.id, OS.sel_ctrlKey) != 0;
}

public boolean isHorizontal() {
	return OS.objc_msgSend(this.id, OS.sel_isHorizontal) != 0;
}

public boolean metaKey() {
	return OS.objc_msgSend(this.id, OS.sel_metaKey) != 0;
}

public int screenX() {
	return OS.objc_msgSend(this.id, OS.sel_screenX);
}

public int screenY() {
	return OS.objc_msgSend(this.id, OS.sel_screenY);
}

public boolean shiftKey() {
	return OS.objc_msgSend(this.id, OS.sel_shiftKey) != 0;
}

public int wheelDelta() {
	return OS.objc_msgSend(this.id, OS.sel_wheelDelta);
}

}
