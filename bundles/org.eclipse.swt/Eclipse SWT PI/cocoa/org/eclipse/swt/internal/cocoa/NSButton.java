/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSButton extends NSControl {

public NSButton() {
	super();
}

public NSButton(int /*long*/ id) {
	super(id);
}

public NSButton(id id) {
	super(id);
}

public void setAllowsMixedState(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsMixedState_, flag);
}

public void setAttributedTitle(NSAttributedString aString) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedTitle_, aString != null ? aString.id : 0);
}

public void setBezelStyle(int /*long*/ bezelStyle) {
	OS.objc_msgSend(this.id, OS.sel_setBezelStyle_, bezelStyle);
}

public void setBordered(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBordered_, flag);
}

public void setButtonType(int /*long*/ aType) {
	OS.objc_msgSend(this.id, OS.sel_setButtonType_, aType);
}

public void setImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setImage_, image != null ? image.id : 0);
}

public void setImagePosition(int /*long*/ aPosition) {
	OS.objc_msgSend(this.id, OS.sel_setImagePosition_, aPosition);
}

public void setKeyEquivalent(NSString charCode) {
	OS.objc_msgSend(this.id, OS.sel_setKeyEquivalent_, charCode != null ? charCode.id : 0);
}

public void setState(int /*long*/ value) {
	OS.objc_msgSend(this.id, OS.sel_setState_, value);
}

public void setTitle(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_, aString != null ? aString.id : 0);
}

public int /*long*/ state() {
	return OS.objc_msgSend(this.id, OS.sel_state);
}

public static int /*long*/ cellClass() {
	return OS.objc_msgSend(OS.class_NSButton, OS.sel_cellClass);
}

public static void setCellClass(int /*long*/ factoryId) {
	OS.objc_msgSend(OS.class_NSButton, OS.sel_setCellClass_, factoryId);
}

}
