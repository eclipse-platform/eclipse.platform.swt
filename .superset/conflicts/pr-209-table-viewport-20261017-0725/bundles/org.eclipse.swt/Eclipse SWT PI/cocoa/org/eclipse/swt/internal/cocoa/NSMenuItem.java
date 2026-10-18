/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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

public class NSMenuItem extends NSObject {

public NSMenuItem() {
	super();
}

public NSMenuItem(long id) {
	super(id);
}

public NSMenuItem(id id) {
	super(id);
}

public long action() {
	return OS.objc_msgSend(this.id, OS.sel_action);
}

public NSAttributedString attributedTitle() {
	long result = OS.objc_msgSend(this.id, OS.sel_attributedTitle);
	return result != 0 ? new NSAttributedString(result) : null;
}

public NSImage image() {
	long result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public NSMenuItem initWithTitle(NSString aString, long aSelector, NSString charCode) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithTitle_action_keyEquivalent_, aString != null ? aString.id : 0, aSelector, charCode != null ? charCode.id : 0);
	return result == this.id ? this : (result != 0 ? new NSMenuItem(result) : null);
}

public boolean isHidden() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isHidden);
}

public boolean isSeparatorItem() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isSeparatorItem);
}

public NSString keyEquivalent() {
	long result = OS.objc_msgSend(this.id, OS.sel_keyEquivalent);
	return result != 0 ? new NSString(result) : null;
}

public long keyEquivalentModifierMask() {
	return OS.objc_msgSend(this.id, OS.sel_keyEquivalentModifierMask);
}

public static NSMenuItem separatorItem() {
	long result = OS.objc_msgSend(OS.class_NSMenuItem, OS.sel_separatorItem);
	return result != 0 ? new NSMenuItem(result) : null;
}

public void setAction(long action) {
	OS.objc_msgSend(this.id, OS.sel_setAction_, action);
}

public void setAttributedTitle(NSAttributedString attributedTitle) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedTitle_, attributedTitle != null ? attributedTitle.id : 0);
}

public void setEnabled(boolean enabled) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_, enabled);
}

public void setHidden(boolean hidden) {
	OS.objc_msgSend(this.id, OS.sel_setHidden_, hidden);
}

public void setImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setImage_, image != null ? image.id : 0);
}

public void setKeyEquivalent(NSString keyEquivalent) {
	OS.objc_msgSend(this.id, OS.sel_setKeyEquivalent_, keyEquivalent != null ? keyEquivalent.id : 0);
}

public void setKeyEquivalentModifierMask(long keyEquivalentModifierMask) {
	OS.objc_msgSend(this.id, OS.sel_setKeyEquivalentModifierMask_, keyEquivalentModifierMask);
}

public void setMenu(NSMenu menu) {
	OS.objc_msgSend(this.id, OS.sel_setMenu_, menu != null ? menu.id : 0);
}

public void setState(long state) {
	OS.objc_msgSend(this.id, OS.sel_setState_, state);
}

public void setSubmenu(NSMenu submenu) {
	OS.objc_msgSend(this.id, OS.sel_setSubmenu_, submenu != null ? submenu.id : 0);
}

public void setTag(long tag) {
	OS.objc_msgSend(this.id, OS.sel_setTag_, tag);
}

public void setTarget(id target) {
	OS.objc_msgSend(this.id, OS.sel_setTarget_, target != null ? target.id : 0);
}

public void setTitle(NSString title) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_, title != null ? title.id : 0);
}

public void setToolTip(NSString toolTip) {
	OS.objc_msgSend(this.id, OS.sel_setToolTip_, toolTip != null ? toolTip.id : 0);
}

public long state() {
	return OS.objc_msgSend(this.id, OS.sel_state);
}

public NSMenu submenu() {
	long result = OS.objc_msgSend(this.id, OS.sel_submenu);
	return result != 0 ? new NSMenu(result) : null;
}

public long tag() {
	return OS.objc_msgSend(this.id, OS.sel_tag);
}

public id target() {
	long result = OS.objc_msgSend(this.id, OS.sel_target);
	return result != 0 ? new id(result) : null;
}

public NSString title() {
	long result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

}
