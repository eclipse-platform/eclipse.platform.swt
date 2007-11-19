/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSColorWell extends NSControl {

public NSColorWell() {
	super();
}

public NSColorWell(int id) {
	super(id);
}

public void activate(boolean exclusive) {
	OS.objc_msgSend(this.id, OS.sel_activate_1, exclusive);
}

public NSColor color() {
	int result = OS.objc_msgSend(this.id, OS.sel_color);
	return result != 0 ? new NSColor(result) : null;
}

public void deactivate() {
	OS.objc_msgSend(this.id, OS.sel_deactivate);
}

public void drawWellInside(NSRect insideRect) {
	OS.objc_msgSend(this.id, OS.sel_drawWellInside_1, insideRect);
}

public boolean isActive() {
	return OS.objc_msgSend(this.id, OS.sel_isActive) != 0;
}

public boolean isBordered() {
	return OS.objc_msgSend(this.id, OS.sel_isBordered) != 0;
}

public void setBordered(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBordered_1, flag);
}

public void setColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setColor_1, color != null ? color.id : 0);
}

public void takeColorFrom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_takeColorFrom_1, sender != null ? sender.id : 0);
}

}
