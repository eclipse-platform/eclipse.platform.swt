/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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

public float /*double*/ alphaComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_alphaComponent);
}

public static NSColor alternateSelectedControlColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_alternateSelectedControlColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor alternateSelectedControlTextColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_alternateSelectedControlTextColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor blackColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_blackColor);
	return result != 0 ? new NSColor(result) : null;
}

public float /*double*/ blueComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_blueComponent);
}

public static NSColor clearColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_clearColor);
	return result != 0 ? new NSColor(result) : null;
}

public NSColor colorUsingColorSpace(NSColorSpace space) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_colorUsingColorSpace_, space != null ? space.id : 0);
	return result == this.id ? this : (result != 0 ? new NSColor(result) : null);
}

public NSColor colorUsingColorSpaceName(NSString colorSpace) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_colorUsingColorSpaceName_, colorSpace != null ? colorSpace.id : 0);
	return result == this.id ? this : (result != 0 ? new NSColor(result) : null);
}

public static NSColor colorWithDeviceRed(float /*double*/ red, float /*double*/ green, float /*double*/ blue, float /*double*/ alpha) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithDeviceRed_green_blue_alpha_, red, green, blue, alpha);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor colorWithPatternImage(NSImage image) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithPatternImage_, image != null ? image.id : 0);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlBackgroundColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlBackgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlDarkShadowColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlDarkShadowColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlHighlightColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlHighlightColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlLightHighlightColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlLightHighlightColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlShadowColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlShadowColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlTextColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlTextColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor disabledControlTextColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_disabledControlTextColor);
	return result != 0 ? new NSColor(result) : null;
}

public void getComponents(float[] /*double[]*/ components) {
	OS.objc_msgSend(this.id, OS.sel_getComponents_, components);
}

public float /*double*/ greenComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_greenComponent);
}

public int /*long*/ numberOfComponents() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfComponents);
}

public float /*double*/ redComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_redComponent);
}

public static NSColor secondarySelectedControlColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_secondarySelectedControlColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor selectedControlColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_selectedControlColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor selectedControlTextColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_selectedControlTextColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor selectedTextBackgroundColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_selectedTextBackgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor selectedTextColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_selectedTextColor);
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
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_textBackgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor textColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_textColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor windowBackgroundColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_windowBackgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor windowFrameColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_windowFrameColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor windowFrameTextColor() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColor, OS.sel_windowFrameTextColor);
	return result != 0 ? new NSColor(result) : null;
}

}
