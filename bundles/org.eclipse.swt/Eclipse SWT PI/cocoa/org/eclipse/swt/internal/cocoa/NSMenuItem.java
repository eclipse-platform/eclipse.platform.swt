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

public class NSMenuItem extends NSObject {

public NSMenuItem() {
	super();
}

public NSMenuItem(int id) {
	super(id);
}

public int action() {
	return OS.objc_msgSend(this.id, OS.sel_action);
}

public NSAttributedString attributedTitle() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributedTitle);
	return result != 0 ? new NSAttributedString(result) : null;
}

public boolean hasSubmenu() {
	return OS.objc_msgSend(this.id, OS.sel_hasSubmenu) != 0;
}

public NSImage image() {
	int result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public int indentationLevel() {
	return OS.objc_msgSend(this.id, OS.sel_indentationLevel);
}

public NSMenuItem initWithTitle(NSString aString, int aSelector, NSString charCode) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithTitle_1action_1keyEquivalent_1, aString != null ? aString.id : 0, aSelector, charCode != null ? charCode.id : 0);
	return result != 0 ? this : null;
}

public boolean isAlternate() {
	return OS.objc_msgSend(this.id, OS.sel_isAlternate) != 0;
}

public boolean isEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_isEnabled) != 0;
}

public boolean isHidden() {
	return OS.objc_msgSend(this.id, OS.sel_isHidden) != 0;
}

public boolean isHiddenOrHasHiddenAncestor() {
	return OS.objc_msgSend(this.id, OS.sel_isHiddenOrHasHiddenAncestor) != 0;
}

public boolean isHighlighted() {
	return OS.objc_msgSend(this.id, OS.sel_isHighlighted) != 0;
}

public boolean isSeparatorItem() {
	return OS.objc_msgSend(this.id, OS.sel_isSeparatorItem) != 0;
}

public NSString keyEquivalent() {
	int result = OS.objc_msgSend(this.id, OS.sel_keyEquivalent);
	return result != 0 ? new NSString(result) : null;
}

public int keyEquivalentModifierMask() {
	return OS.objc_msgSend(this.id, OS.sel_keyEquivalentModifierMask);
}

public NSMenu menu() {
	int result = OS.objc_msgSend(this.id, OS.sel_menu);
	return result != 0 ? new NSMenu(result) : null;
}

public NSImage mixedStateImage() {
	int result = OS.objc_msgSend(this.id, OS.sel_mixedStateImage);
	return result != 0 ? new NSImage(result) : null;
}

public NSString mnemonic() {
	int result = OS.objc_msgSend(this.id, OS.sel_mnemonic);
	return result != 0 ? new NSString(result) : null;
}

public int mnemonicLocation() {
	return OS.objc_msgSend(this.id, OS.sel_mnemonicLocation);
}

public NSImage offStateImage() {
	int result = OS.objc_msgSend(this.id, OS.sel_offStateImage);
	return result != 0 ? new NSImage(result) : null;
}

public NSImage onStateImage() {
	int result = OS.objc_msgSend(this.id, OS.sel_onStateImage);
	return result != 0 ? new NSImage(result) : null;
}

public id representedObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_representedObject);
	return result != 0 ? new id(result) : null;
}

public static NSMenuItem separatorItem() {
	int result = OS.objc_msgSend(OS.class_NSMenuItem, OS.sel_separatorItem);
	return result != 0 ? new NSMenuItem(result) : null;
}

public void setAction(int aSelector) {
	OS.objc_msgSend(this.id, OS.sel_setAction_1, aSelector);
}

public void setAlternate(boolean isAlternate) {
	OS.objc_msgSend(this.id, OS.sel_setAlternate_1, isAlternate);
}

public void setAttributedTitle(NSAttributedString string) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedTitle_1, string != null ? string.id : 0);
}

public void setEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_1, flag);
}

public void setHidden(boolean hidden) {
	OS.objc_msgSend(this.id, OS.sel_setHidden_1, hidden);
}

public void setImage(NSImage menuImage) {
	OS.objc_msgSend(this.id, OS.sel_setImage_1, menuImage != null ? menuImage.id : 0);
}

public void setIndentationLevel(int indentationLevel) {
	OS.objc_msgSend(this.id, OS.sel_setIndentationLevel_1, indentationLevel);
}

public void setKeyEquivalent(NSString aKeyEquivalent) {
	OS.objc_msgSend(this.id, OS.sel_setKeyEquivalent_1, aKeyEquivalent != null ? aKeyEquivalent.id : 0);
}

public void setKeyEquivalentModifierMask(int mask) {
	OS.objc_msgSend(this.id, OS.sel_setKeyEquivalentModifierMask_1, mask);
}

public void setMenu(NSMenu menu) {
	OS.objc_msgSend(this.id, OS.sel_setMenu_1, menu != null ? menu.id : 0);
}

public void setMixedStateImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setMixedStateImage_1, image != null ? image.id : 0);
}

public void setMnemonicLocation(int location) {
	OS.objc_msgSend(this.id, OS.sel_setMnemonicLocation_1, location);
}

public void setOffStateImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setOffStateImage_1, image != null ? image.id : 0);
}

public void setOnStateImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setOnStateImage_1, image != null ? image.id : 0);
}

public void setRepresentedObject(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setRepresentedObject_1, anObject != null ? anObject.id : 0);
}

public void setState(int state) {
	OS.objc_msgSend(this.id, OS.sel_setState_1, state);
}

public void setSubmenu(NSMenu submenu) {
	OS.objc_msgSend(this.id, OS.sel_setSubmenu_1, submenu != null ? submenu.id : 0);
}

public void setTag(int anInt) {
	OS.objc_msgSend(this.id, OS.sel_setTag_1, anInt);
}

public void setTarget(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setTarget_1, anObject != null ? anObject.id : 0);
}

public void setTitle(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_1, aString != null ? aString.id : 0);
}

public void setTitleWithMnemonic(NSString stringWithAmpersand) {
	OS.objc_msgSend(this.id, OS.sel_setTitleWithMnemonic_1, stringWithAmpersand != null ? stringWithAmpersand.id : 0);
}

public void setToolTip(NSString toolTip) {
	OS.objc_msgSend(this.id, OS.sel_setToolTip_1, toolTip != null ? toolTip.id : 0);
}

public static void setUsesUserKeyEquivalents(boolean flag) {
	OS.objc_msgSend(OS.class_NSMenuItem, OS.sel_setUsesUserKeyEquivalents_1, flag);
}

public void setView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setView_1, view != null ? view.id : 0);
}

public int state() {
	return OS.objc_msgSend(this.id, OS.sel_state);
}

public NSMenu submenu() {
	int result = OS.objc_msgSend(this.id, OS.sel_submenu);
	return result != 0 ? new NSMenu(result) : null;
}

public int tag() {
	return OS.objc_msgSend(this.id, OS.sel_tag);
}

public id target() {
	int result = OS.objc_msgSend(this.id, OS.sel_target);
	return result != 0 ? new id(result) : null;
}

public NSString title() {
	int result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

public NSString toolTip() {
	int result = OS.objc_msgSend(this.id, OS.sel_toolTip);
	return result != 0 ? new NSString(result) : null;
}

public NSString userKeyEquivalent() {
	int result = OS.objc_msgSend(this.id, OS.sel_userKeyEquivalent);
	return result != 0 ? new NSString(result) : null;
}

public static boolean usesUserKeyEquivalents() {
	return OS.objc_msgSend(OS.class_NSMenuItem, OS.sel_usesUserKeyEquivalents) != 0;
}

public NSView view() {
	int result = OS.objc_msgSend(this.id, OS.sel_view);
	return result != 0 ? new NSView(result) : null;
}

}
