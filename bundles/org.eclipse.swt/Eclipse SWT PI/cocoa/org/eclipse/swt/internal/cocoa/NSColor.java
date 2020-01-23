/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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

public class NSColor extends NSObject {

public NSColor() {
	super();
}

public NSColor(long id) {
	super(id);
}

public NSColor(id id) {
	super(id);
}

public double alphaComponent() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_alphaComponent);
}

public static NSColor alternateSelectedControlColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_alternateSelectedControlColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor alternateSelectedControlTextColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_alternateSelectedControlTextColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor blackColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_blackColor);
	return result != 0 ? new NSColor(result) : null;
}

public double blueComponent() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_blueComponent);
}

public static NSColor clearColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_clearColor);
	return result != 0 ? new NSColor(result) : null;
}

public NSColorSpace colorSpace() {
	long result = OS.objc_msgSend(this.id, OS.sel_colorSpace);
	return result != 0 ? new NSColorSpace(result) : null;
}

public NSColor colorUsingColorSpaceName(NSString colorSpace) {
	long result = OS.objc_msgSend(this.id, OS.sel_colorUsingColorSpaceName_, colorSpace != null ? colorSpace.id : 0);
	return result == this.id ? this : (result != 0 ? new NSColor(result) : null);
}

public static NSColor colorWithDeviceRed(double red, double green, double blue, double alpha) {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithDeviceRed_green_blue_alpha_, red, green, blue, alpha);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor colorWithPatternImage(NSImage image) {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithPatternImage_, image != null ? image.id : 0);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlBackgroundColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlBackgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlDarkShadowColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlDarkShadowColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlHighlightColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlHighlightColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlLightHighlightColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlLightHighlightColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlShadowColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlShadowColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlTextColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlTextColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor disabledControlTextColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_disabledControlTextColor);
	return result != 0 ? new NSColor(result) : null;
}

public void getComponents(double[] components) {
	OS.objc_msgSend(this.id, OS.sel_getComponents_, components);
}

public double greenComponent() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_greenComponent);
}

public long numberOfComponents() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfComponents);
}

public double redComponent() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_redComponent);
}

public static NSColor secondarySelectedControlColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_secondarySelectedControlColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor selectedControlColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_selectedControlColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor selectedControlTextColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_selectedControlTextColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor selectedTextBackgroundColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_selectedTextBackgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor selectedTextColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_selectedTextColor);
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
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_textBackgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor textColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_textColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor windowBackgroundColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_windowBackgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor windowFrameTextColor() {
	long result = OS.objc_msgSend(OS.class_NSColor, OS.sel_windowFrameTextColor);
	return result != 0 ? new NSColor(result) : null;
}

}
