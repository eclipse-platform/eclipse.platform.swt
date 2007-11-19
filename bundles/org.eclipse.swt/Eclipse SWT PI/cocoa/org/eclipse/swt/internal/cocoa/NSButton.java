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

public class NSButton extends NSControl {

public NSButton() {
	super();
}

public NSButton(int id) {
	super(id);
}

public boolean allowsMixedState() {
	return OS.objc_msgSend(this.id, OS.sel_allowsMixedState) != 0;
}

public NSImage alternateImage() {
	int result = OS.objc_msgSend(this.id, OS.sel_alternateImage);
	return result != 0 ? new NSImage(result) : null;
}

public NSString alternateTitle() {
	int result = OS.objc_msgSend(this.id, OS.sel_alternateTitle);
	return result != 0 ? new NSString(result) : null;
}

public NSAttributedString attributedAlternateTitle() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributedAlternateTitle);
	return result != 0 ? new NSAttributedString(result) : null;
}

public NSAttributedString attributedTitle() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributedTitle);
	return result != 0 ? new NSAttributedString(result) : null;
}

public int bezelStyle() {
	return OS.objc_msgSend(this.id, OS.sel_bezelStyle);
}

public void getPeriodicDelay(int delay, int interval) {
	OS.objc_msgSend(this.id, OS.sel_getPeriodicDelay_1interval_1, delay, interval);
}

public void highlight(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_highlight_1, flag);
}

public NSImage image() {
	int result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public int imagePosition() {
	return OS.objc_msgSend(this.id, OS.sel_imagePosition);
}

public boolean isBordered() {
	return OS.objc_msgSend(this.id, OS.sel_isBordered) != 0;
}

public boolean isTransparent() {
	return OS.objc_msgSend(this.id, OS.sel_isTransparent) != 0;
}

public NSString keyEquivalent() {
	int result = OS.objc_msgSend(this.id, OS.sel_keyEquivalent);
	return result != 0 ? new NSString(result) : null;
}

public int keyEquivalentModifierMask() {
	return OS.objc_msgSend(this.id, OS.sel_keyEquivalentModifierMask);
}

public boolean performKeyEquivalent(NSEvent key) {
	return OS.objc_msgSend(this.id, OS.sel_performKeyEquivalent_1, key != null ? key.id : 0) != 0;
}

public void setAllowsMixedState(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsMixedState_1, flag);
}

public void setAlternateImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setAlternateImage_1, image != null ? image.id : 0);
}

public void setAlternateTitle(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setAlternateTitle_1, aString != null ? aString.id : 0);
}

public void setAttributedAlternateTitle(NSAttributedString obj) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedAlternateTitle_1, obj != null ? obj.id : 0);
}

public void setAttributedTitle(NSAttributedString aString) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedTitle_1, aString != null ? aString.id : 0);
}

public void setBezelStyle(int bezelStyle) {
	OS.objc_msgSend(this.id, OS.sel_setBezelStyle_1, bezelStyle);
}

public void setBordered(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBordered_1, flag);
}

public void setButtonType(int aType) {
	OS.objc_msgSend(this.id, OS.sel_setButtonType_1, aType);
}

public void setImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setImage_1, image != null ? image.id : 0);
}

public void setImagePosition(int aPosition) {
	OS.objc_msgSend(this.id, OS.sel_setImagePosition_1, aPosition);
}

public void setKeyEquivalent(NSString charCode) {
	OS.objc_msgSend(this.id, OS.sel_setKeyEquivalent_1, charCode != null ? charCode.id : 0);
}

public void setKeyEquivalentModifierMask(int mask) {
	OS.objc_msgSend(this.id, OS.sel_setKeyEquivalentModifierMask_1, mask);
}

public void setNextState() {
	OS.objc_msgSend(this.id, OS.sel_setNextState);
}

public void setPeriodicDelay(float delay, float interval) {
	OS.objc_msgSend(this.id, OS.sel_setPeriodicDelay_1interval_1, delay, interval);
}

public void setShowsBorderOnlyWhileMouseInside(boolean show) {
	OS.objc_msgSend(this.id, OS.sel_setShowsBorderOnlyWhileMouseInside_1, show);
}

public void setSound(NSSound aSound) {
	OS.objc_msgSend(this.id, OS.sel_setSound_1, aSound != null ? aSound.id : 0);
}

public void setState(int value) {
	OS.objc_msgSend(this.id, OS.sel_setState_1, value);
}

public void setTitle(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_1, aString != null ? aString.id : 0);
}

public void setTitleWithMnemonic(NSString stringWithAmpersand) {
	OS.objc_msgSend(this.id, OS.sel_setTitleWithMnemonic_1, stringWithAmpersand != null ? stringWithAmpersand.id : 0);
}

public void setTransparent(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setTransparent_1, flag);
}

public boolean showsBorderOnlyWhileMouseInside() {
	return OS.objc_msgSend(this.id, OS.sel_showsBorderOnlyWhileMouseInside) != 0;
}

public NSSound sound() {
	int result = OS.objc_msgSend(this.id, OS.sel_sound);
	return result != 0 ? new NSSound(result) : null;
}

public int state() {
	return OS.objc_msgSend(this.id, OS.sel_state);
}

public NSString title() {
	int result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

}
