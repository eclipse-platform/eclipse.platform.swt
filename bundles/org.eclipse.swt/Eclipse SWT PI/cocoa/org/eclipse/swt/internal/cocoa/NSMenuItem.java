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

public class NSMenuItem extends NSObject {

public NSMenuItem() {
	super();
}

public NSMenuItem(int /*long*/ id) {
	super(id);
}

public NSMenuItem(id id) {
	super(id);
}

public NSAttributedString attributedTitle() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_attributedTitle);
	return result != 0 ? new NSAttributedString(result) : null;
}

public NSImage image() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public NSMenuItem initWithTitle(NSString aString, int /*long*/ aSelector, NSString charCode) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithTitle_action_keyEquivalent_, aString != null ? aString.id : 0, aSelector, charCode != null ? charCode.id : 0);
	return result == this.id ? this : (result != 0 ? new NSMenuItem(result) : null);
}

public boolean isHidden() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isHidden);
}

public NSString keyEquivalent() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_keyEquivalent);
	return result != 0 ? new NSString(result) : null;
}

public int /*long*/ keyEquivalentModifierMask() {
	return OS.objc_msgSend(this.id, OS.sel_keyEquivalentModifierMask);
}

public static NSMenuItem separatorItem() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSMenuItem, OS.sel_separatorItem);
	return result != 0 ? new NSMenuItem(result) : null;
}

public void setAction(int /*long*/ aSelector) {
	OS.objc_msgSend(this.id, OS.sel_setAction_, aSelector);
}

public void setAttributedTitle(NSAttributedString string) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedTitle_, string != null ? string.id : 0);
}

public void setEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_, flag);
}

public void setHidden(boolean hidden) {
	OS.objc_msgSend(this.id, OS.sel_setHidden_, hidden);
}

public void setImage(NSImage menuImage) {
	OS.objc_msgSend(this.id, OS.sel_setImage_, menuImage != null ? menuImage.id : 0);
}

public void setKeyEquivalent(NSString aKeyEquivalent) {
	OS.objc_msgSend(this.id, OS.sel_setKeyEquivalent_, aKeyEquivalent != null ? aKeyEquivalent.id : 0);
}

public void setKeyEquivalentModifierMask(int /*long*/ mask) {
	OS.objc_msgSend(this.id, OS.sel_setKeyEquivalentModifierMask_, mask);
}

public void setMenu(NSMenu menu) {
	OS.objc_msgSend(this.id, OS.sel_setMenu_, menu != null ? menu.id : 0);
}

public void setState(int /*long*/ state) {
	OS.objc_msgSend(this.id, OS.sel_setState_, state);
}

public void setSubmenu(NSMenu submenu) {
	OS.objc_msgSend(this.id, OS.sel_setSubmenu_, submenu != null ? submenu.id : 0);
}

public void setTag(int /*long*/ anInt) {
	OS.objc_msgSend(this.id, OS.sel_setTag_, anInt);
}

public void setTarget(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setTarget_, anObject != null ? anObject.id : 0);
}

public void setTitle(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_, aString != null ? aString.id : 0);
}

public int /*long*/ state() {
	return OS.objc_msgSend(this.id, OS.sel_state);
}

public NSMenu submenu() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_submenu);
	return result != 0 ? new NSMenu(result) : null;
}

public NSString title() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

}
