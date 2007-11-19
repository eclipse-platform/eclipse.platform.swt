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

public class NSButtonCell extends NSActionCell {

public NSButtonCell() {
	super();
}

public NSButtonCell(int id) {
	super(id);
}

public NSImage alternateImage() {
	int result = OS.objc_msgSend(this.id, OS.sel_alternateImage);
	return result != 0 ? new NSImage(result) : null;
}

public NSString alternateMnemonic() {
	int result = OS.objc_msgSend(this.id, OS.sel_alternateMnemonic);
	return result != 0 ? new NSString(result) : null;
}

public int alternateMnemonicLocation() {
	return OS.objc_msgSend(this.id, OS.sel_alternateMnemonicLocation);
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

public NSColor backgroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public int bezelStyle() {
	return OS.objc_msgSend(this.id, OS.sel_bezelStyle);
}

public void drawBezelWithFrame(NSRect frame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawBezelWithFrame_1inView_1, frame, controlView != null ? controlView.id : 0);
}

public void drawImage(NSImage image, NSRect frame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawImage_1withFrame_1inView_1, image != null ? image.id : 0, frame, controlView != null ? controlView.id : 0);
}

public NSRect drawTitle(NSAttributedString title, NSRect frame, NSView controlView) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_drawTitle_1withFrame_1inView_1, title != null ? title.id : 0, frame, controlView != null ? controlView.id : 0);
	return result;
}

public void getPeriodicDelay(int delay, int interval) {
	OS.objc_msgSend(this.id, OS.sel_getPeriodicDelay_1interval_1, delay, interval);
}

public int gradientType() {
	return OS.objc_msgSend(this.id, OS.sel_gradientType);
}

public int highlightsBy() {
	return OS.objc_msgSend(this.id, OS.sel_highlightsBy);
}

public boolean imageDimsWhenDisabled() {
	return OS.objc_msgSend(this.id, OS.sel_imageDimsWhenDisabled) != 0;
}

public int imagePosition() {
	return OS.objc_msgSend(this.id, OS.sel_imagePosition);
}

public int imageScaling() {
	return OS.objc_msgSend(this.id, OS.sel_imageScaling);
}

public boolean isOpaque() {
	return OS.objc_msgSend(this.id, OS.sel_isOpaque) != 0;
}

public boolean isTransparent() {
	return OS.objc_msgSend(this.id, OS.sel_isTransparent) != 0;
}

public NSString keyEquivalent() {
	int result = OS.objc_msgSend(this.id, OS.sel_keyEquivalent);
	return result != 0 ? new NSString(result) : null;
}

public NSFont keyEquivalentFont() {
	int result = OS.objc_msgSend(this.id, OS.sel_keyEquivalentFont);
	return result != 0 ? new NSFont(result) : null;
}

public int keyEquivalentModifierMask() {
	return OS.objc_msgSend(this.id, OS.sel_keyEquivalentModifierMask);
}

public void mouseEntered(NSEvent event) {
	OS.objc_msgSend(this.id, OS.sel_mouseEntered_1, event != null ? event.id : 0);
}

public void mouseExited(NSEvent event) {
	OS.objc_msgSend(this.id, OS.sel_mouseExited_1, event != null ? event.id : 0);
}

public void performClick(id sender) {
	OS.objc_msgSend(this.id, OS.sel_performClick_1, sender != null ? sender.id : 0);
}

public void setAlternateImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setAlternateImage_1, image != null ? image.id : 0);
}

public void setAlternateMnemonicLocation(int location) {
	OS.objc_msgSend(this.id, OS.sel_setAlternateMnemonicLocation_1, location);
}

public void setAlternateTitle(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setAlternateTitle_1, aString != null ? aString.id : 0);
}

public void setAlternateTitleWithMnemonic(NSString stringWithAmpersand) {
	OS.objc_msgSend(this.id, OS.sel_setAlternateTitleWithMnemonic_1, stringWithAmpersand != null ? stringWithAmpersand.id : 0);
}

public void setAttributedAlternateTitle(NSAttributedString obj) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedAlternateTitle_1, obj != null ? obj.id : 0);
}

public void setAttributedTitle(NSAttributedString obj) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedTitle_1, obj != null ? obj.id : 0);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_1, color != null ? color.id : 0);
}

public void setBezelStyle(int bezelStyle) {
	OS.objc_msgSend(this.id, OS.sel_setBezelStyle_1, bezelStyle);
}

public void setButtonType(int aType) {
	OS.objc_msgSend(this.id, OS.sel_setButtonType_1, aType);
}

public void setFont(NSFont fontObj) {
	OS.objc_msgSend(this.id, OS.sel_setFont_1, fontObj != null ? fontObj.id : 0);
}

public void setGradientType(int type) {
	OS.objc_msgSend(this.id, OS.sel_setGradientType_1, type);
}

public void setHighlightsBy(int aType) {
	OS.objc_msgSend(this.id, OS.sel_setHighlightsBy_1, aType);
}

public void setImageDimsWhenDisabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setImageDimsWhenDisabled_1, flag);
}

public void setImagePosition(int aPosition) {
	OS.objc_msgSend(this.id, OS.sel_setImagePosition_1, aPosition);
}

public void setImageScaling(int scaling) {
	OS.objc_msgSend(this.id, OS.sel_setImageScaling_1, scaling);
}

public void setKeyEquivalent(NSString aKeyEquivalent) {
	OS.objc_msgSend(this.id, OS.sel_setKeyEquivalent_1, aKeyEquivalent != null ? aKeyEquivalent.id : 0);
}

public void setKeyEquivalentFont_(NSFont fontObj) {
	OS.objc_msgSend(this.id, OS.sel_setKeyEquivalentFont_1, fontObj != null ? fontObj.id : 0);
}

public void setKeyEquivalentFont_size_(NSString fontName, float fontSize) {
	OS.objc_msgSend(this.id, OS.sel_setKeyEquivalentFont_1size_1, fontName != null ? fontName.id : 0, fontSize);
}

public void setKeyEquivalentModifierMask(int mask) {
	OS.objc_msgSend(this.id, OS.sel_setKeyEquivalentModifierMask_1, mask);
}

public void setPeriodicDelay(float delay, float interval) {
	OS.objc_msgSend(this.id, OS.sel_setPeriodicDelay_1interval_1, delay, interval);
}

public void setShowsBorderOnlyWhileMouseInside(boolean show) {
	OS.objc_msgSend(this.id, OS.sel_setShowsBorderOnlyWhileMouseInside_1, show);
}

public void setShowsStateBy(int aType) {
	OS.objc_msgSend(this.id, OS.sel_setShowsStateBy_1, aType);
}

public void setSound(NSSound aSound) {
	OS.objc_msgSend(this.id, OS.sel_setSound_1, aSound != null ? aSound.id : 0);
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

public int showsStateBy() {
	return OS.objc_msgSend(this.id, OS.sel_showsStateBy);
}

public NSSound sound() {
	int result = OS.objc_msgSend(this.id, OS.sel_sound);
	return result != 0 ? new NSSound(result) : null;
}

public NSString title() {
	int result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

}
