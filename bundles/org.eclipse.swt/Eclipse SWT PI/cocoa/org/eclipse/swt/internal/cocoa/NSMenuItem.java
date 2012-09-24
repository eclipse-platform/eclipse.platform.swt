/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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

public NSMenuItem(long /*int*/ id) {
	super(id);
}

public NSMenuItem(id id) {
	super(id);
}

public long /*int*/ action() {
	return OS.objc_msgSend(this.id, OS.sel_action);
}

public NSAttributedString attributedTitle() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_attributedTitle);
	return result != 0 ? new NSAttributedString(result) : null;
}

public NSImage image() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public NSMenuItem initWithTitle(NSString aString, long /*int*/ aSelector, NSString charCode) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_initWithTitle_action_keyEquivalent_, aString != null ? aString.id : 0, aSelector, charCode != null ? charCode.id : 0);
	return result == this.id ? this : (result != 0 ? new NSMenuItem(result) : null);
}

public boolean isHidden() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isHidden);
}

public boolean isSeparatorItem() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isSeparatorItem);
}

public NSString keyEquivalent() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_keyEquivalent);
	return result != 0 ? new NSString(result) : null;
}

public long /*int*/ keyEquivalentModifierMask() {
	return OS.objc_msgSend(this.id, OS.sel_keyEquivalentModifierMask);
}

public static NSMenuItem separatorItem() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSMenuItem, OS.sel_separatorItem);
	return result != 0 ? new NSMenuItem(result) : null;
}

public void setAction(long /*int*/ aSelector) {
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

public void setKeyEquivalentModifierMask(long /*int*/ mask) {
	OS.objc_msgSend(this.id, OS.sel_setKeyEquivalentModifierMask_, mask);
}

public void setMenu(NSMenu menu) {
	OS.objc_msgSend(this.id, OS.sel_setMenu_, menu != null ? menu.id : 0);
}

public void setState(long /*int*/ state) {
	OS.objc_msgSend(this.id, OS.sel_setState_, state);
}

public void setSubmenu(NSMenu submenu) {
	OS.objc_msgSend(this.id, OS.sel_setSubmenu_, submenu != null ? submenu.id : 0);
}

public void setTag(long /*int*/ anInt) {
	OS.objc_msgSend(this.id, OS.sel_setTag_, anInt);
}

public void setTarget(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setTarget_, anObject != null ? anObject.id : 0);
}

public void setTitle(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_, aString != null ? aString.id : 0);
}

public long /*int*/ state() {
	return OS.objc_msgSend(this.id, OS.sel_state);
}

public NSMenu submenu() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_submenu);
	return result != 0 ? new NSMenu(result) : null;
}

public long /*int*/ tag() {
	return OS.objc_msgSend(this.id, OS.sel_tag);
}

public id target() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_target);
	return result != 0 ? new id(result) : null;
}

public NSString title() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

}
