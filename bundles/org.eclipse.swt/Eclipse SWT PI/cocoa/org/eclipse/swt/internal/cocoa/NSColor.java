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

public class NSColor extends NSObject {

public NSColor() {
	super();
}

public NSColor(int /*long*/ id) {
	super(id);
}

public NSColor(id id) {
	super(id);
}

public float alphaComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_alphaComponent);
}

public static NSColor blackColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_blackColor);
	return result != 0 ? new NSColor(result) : null;
}

public float blueComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_blueComponent);
}

public static NSColor clearColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_clearColor);
	return result != 0 ? new NSColor(result) : null;
}

public NSColor colorUsingColorSpace(NSColorSpace space) {
	int result = OS.objc_msgSend(this.id, OS.sel_colorUsingColorSpace_, space != null ? space.id : 0);
	return result == this.id ? this : (result != 0 ? new NSColor(result) : null);
}

public NSColor colorUsingColorSpaceName(NSString colorSpace) {
	int result = OS.objc_msgSend(this.id, OS.sel_colorUsingColorSpaceName_, colorSpace != null ? colorSpace.id : 0);
	return result == this.id ? this : (result != 0 ? new NSColor(result) : null);
}

public static NSColor colorWithDeviceRed(float red, float green, float blue, float alpha) {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithDeviceRed_green_blue_alpha_, red, green, blue, alpha);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor colorWithPatternImage(NSImage image) {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithPatternImage_, image != null ? image.id : 0);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlDarkShadowColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlDarkShadowColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlHighlightColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlHighlightColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlLightHighlightColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlLightHighlightColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlShadowColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlShadowColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlTextColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlTextColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor disabledControlTextColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_disabledControlTextColor);
	return result != 0 ? new NSColor(result) : null;
}

public void getComponents(float[] components) {
	OS.objc_msgSend(this.id, OS.sel_getComponents_, components);
}

public float greenComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_greenComponent);
}

public int numberOfComponents() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfComponents);
}

public float redComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_redComponent);
}

public static NSColor secondarySelectedControlColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_secondarySelectedControlColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor selectedTextBackgroundColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_selectedTextBackgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor selectedTextColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_selectedTextColor);
	return result != 0 ? new NSColor(result) : null;
}

public void set() {
	OS.objc_msgSend(this.id, OS.sel_set);
}

public void setFill() {
	OS.objc_msgSend(this.id, OS.sel_setFill);
}

public void setStroke() {
	OS.objc_msgSend(this.id, OS.sel_setStroke);
}

public static NSColor textBackgroundColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_textBackgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor textColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_textColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor windowBackgroundColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_windowBackgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor windowFrameColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_windowFrameColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor windowFrameTextColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_windowFrameTextColor);
	return result != 0 ? new NSColor(result) : null;
}

}
