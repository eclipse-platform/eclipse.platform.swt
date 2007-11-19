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

public class NSColor extends NSObject {

public NSColor() {
	super();
}

public NSColor(int id) {
	super(id);
}

public float alphaComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_alphaComponent);
}

public static NSColor alternateSelectedControlColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_alternateSelectedControlColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor alternateSelectedControlTextColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_alternateSelectedControlTextColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor blackColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_blackColor);
	return result != 0 ? new NSColor(result) : null;
}

public float blackComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_blackComponent);
}

public NSColor blendedColorWithFraction(float fraction, NSColor color) {
	int result = OS.objc_msgSend(this.id, OS.sel_blendedColorWithFraction_1ofColor_1, fraction, color != null ? color.id : 0);
	return result == this.id ? this : (result != 0 ? new NSColor(result) : null);
}

public static NSColor blueColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_blueColor);
	return result != 0 ? new NSColor(result) : null;
}

public float blueComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_blueComponent);
}

public float brightnessComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_brightnessComponent);
}

public static NSColor brownColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_brownColor);
	return result != 0 ? new NSColor(result) : null;
}

public NSString catalogNameComponent() {
	int result = OS.objc_msgSend(this.id, OS.sel_catalogNameComponent);
	return result != 0 ? new NSString(result) : null;
}

public static NSColor clearColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_clearColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor colorForControlTint(int controlTint) {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorForControlTint_1, controlTint);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor colorFromPasteboard(NSPasteboard pasteBoard) {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorFromPasteboard_1, pasteBoard != null ? pasteBoard.id : 0);
	return result != 0 ? new NSColor(result) : null;
}

public NSString colorNameComponent() {
	int result = OS.objc_msgSend(this.id, OS.sel_colorNameComponent);
	return result != 0 ? new NSString(result) : null;
}

public NSColorSpace colorSpace() {
	int result = OS.objc_msgSend(this.id, OS.sel_colorSpace);
	return result != 0 ? new NSColorSpace(result) : null;
}

public NSString colorSpaceName() {
	int result = OS.objc_msgSend(this.id, OS.sel_colorSpaceName);
	return result != 0 ? new NSString(result) : null;
}

public NSColor colorUsingColorSpace(NSColorSpace space) {
	int result = OS.objc_msgSend(this.id, OS.sel_colorUsingColorSpace_1, space != null ? space.id : 0);
	return result == this.id ? this : (result != 0 ? new NSColor(result) : null);
}

public NSColor colorUsingColorSpaceName_(NSString colorSpace) {
	int result = OS.objc_msgSend(this.id, OS.sel_colorUsingColorSpaceName_1, colorSpace != null ? colorSpace.id : 0);
	return result == this.id ? this : (result != 0 ? new NSColor(result) : null);
}

public NSColor colorUsingColorSpaceName_device_(NSString colorSpace, NSDictionary deviceDescription) {
	int result = OS.objc_msgSend(this.id, OS.sel_colorUsingColorSpaceName_1device_1, colorSpace != null ? colorSpace.id : 0, deviceDescription != null ? deviceDescription.id : 0);
	return result == this.id ? this : (result != 0 ? new NSColor(result) : null);
}

public NSColor colorWithAlphaComponent(float alpha) {
	int result = OS.objc_msgSend(this.id, OS.sel_colorWithAlphaComponent_1, alpha);
	return result == this.id ? this : (result != 0 ? new NSColor(result) : null);
}

public static NSColor colorWithCIColor(CIColor color) {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithCIColor_1, color != null ? color.id : 0);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor colorWithCalibratedHue(float hue, float saturation, float brightness, float alpha) {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithCalibratedHue_1saturation_1brightness_1alpha_1, hue, saturation, brightness, alpha);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor colorWithCalibratedRed(float red, float green, float blue, float alpha) {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithCalibratedRed_1green_1blue_1alpha_1, red, green, blue, alpha);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor colorWithCalibratedWhite(float white, float alpha) {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithCalibratedWhite_1alpha_1, white, alpha);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor colorWithCatalogName(NSString listName, NSString colorName) {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithCatalogName_1colorName_1, listName != null ? listName.id : 0, colorName != null ? colorName.id : 0);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor colorWithColorSpace(NSColorSpace space, int components, int numberOfComponents) {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithColorSpace_1components_1count_1, space != null ? space.id : 0, components, numberOfComponents);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor colorWithDeviceCyan(float cyan, float magenta, float yellow, float black, float alpha) {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithDeviceCyan_1magenta_1yellow_1black_1alpha_1, cyan, magenta, yellow, black, alpha);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor colorWithDeviceHue(float hue, float saturation, float brightness, float alpha) {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithDeviceHue_1saturation_1brightness_1alpha_1, hue, saturation, brightness, alpha);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor colorWithDeviceRed(float red, float green, float blue, float alpha) {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithDeviceRed_1green_1blue_1alpha_1, red, green, blue, alpha);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor colorWithDeviceWhite(float white, float alpha) {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithDeviceWhite_1alpha_1, white, alpha);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor colorWithPatternImage(NSImage image) {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_colorWithPatternImage_1, image != null ? image.id : 0);
	return result != 0 ? new NSColor(result) : null;
}

public static NSArray controlAlternatingRowBackgroundColors() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlAlternatingRowBackgroundColors);
	return result != 0 ? new NSArray(result) : null;
}

public static NSColor controlBackgroundColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlBackgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor controlColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_controlColor);
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

public static int currentControlTint() {
	return OS.objc_msgSend(OS.class_NSColor, OS.sel_currentControlTint);
}

public static NSColor cyanColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_cyanColor);
	return result != 0 ? new NSColor(result) : null;
}

public float cyanComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_cyanComponent);
}

public static NSColor darkGrayColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_darkGrayColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor disabledControlTextColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_disabledControlTextColor);
	return result != 0 ? new NSColor(result) : null;
}

public void drawSwatchInRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_drawSwatchInRect_1, rect);
}

public void getComponents(float[] components) {
	OS.objc_msgSend(this.id, OS.sel_getComponents_1, components);
}

public void getCyan(int cyan, int magenta, int yellow, int black, int alpha) {
	OS.objc_msgSend(this.id, OS.sel_getCyan_1magenta_1yellow_1black_1alpha_1, cyan, magenta, yellow, black, alpha);
}

public void getHue(int hue, int saturation, int brightness, int alpha) {
	OS.objc_msgSend(this.id, OS.sel_getHue_1saturation_1brightness_1alpha_1, hue, saturation, brightness, alpha);
}

public void getRed(int red, int green, int blue, int alpha) {
	OS.objc_msgSend(this.id, OS.sel_getRed_1green_1blue_1alpha_1, red, green, blue, alpha);
}

public void getWhite(int white, int alpha) {
	OS.objc_msgSend(this.id, OS.sel_getWhite_1alpha_1, white, alpha);
}

public static NSColor grayColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_grayColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor greenColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_greenColor);
	return result != 0 ? new NSColor(result) : null;
}

public float greenComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_greenComponent);
}

public static NSColor gridColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_gridColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor headerColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_headerColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor headerTextColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_headerTextColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor highlightColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_highlightColor);
	return result != 0 ? new NSColor(result) : null;
}

public NSColor highlightWithLevel(float val) {
	int result = OS.objc_msgSend(this.id, OS.sel_highlightWithLevel_1, val);
	return result == this.id ? this : (result != 0 ? new NSColor(result) : null);
}

public float hueComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_hueComponent);
}

public static boolean ignoresAlpha() {
	return OS.objc_msgSend(OS.class_NSColor, OS.sel_ignoresAlpha) != 0;
}

public static NSColor keyboardFocusIndicatorColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_keyboardFocusIndicatorColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor knobColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_knobColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor lightGrayColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_lightGrayColor);
	return result != 0 ? new NSColor(result) : null;
}

public NSString localizedCatalogNameComponent() {
	int result = OS.objc_msgSend(this.id, OS.sel_localizedCatalogNameComponent);
	return result != 0 ? new NSString(result) : null;
}

public NSString localizedColorNameComponent() {
	int result = OS.objc_msgSend(this.id, OS.sel_localizedColorNameComponent);
	return result != 0 ? new NSString(result) : null;
}

public static NSColor magentaColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_magentaColor);
	return result != 0 ? new NSColor(result) : null;
}

public float magentaComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_magentaComponent);
}

public int numberOfComponents() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfComponents);
}

public static NSColor orangeColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_orangeColor);
	return result != 0 ? new NSColor(result) : null;
}

public NSImage patternImage() {
	int result = OS.objc_msgSend(this.id, OS.sel_patternImage);
	return result != 0 ? new NSImage(result) : null;
}

public static NSColor purpleColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_purpleColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor redColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_redColor);
	return result != 0 ? new NSColor(result) : null;
}

public float redComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_redComponent);
}

public float saturationComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_saturationComponent);
}

public static NSColor scrollBarColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_scrollBarColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor secondarySelectedControlColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_secondarySelectedControlColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor selectedControlColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_selectedControlColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor selectedControlTextColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_selectedControlTextColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor selectedKnobColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_selectedKnobColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor selectedMenuItemColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_selectedMenuItemColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor selectedMenuItemTextColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_selectedMenuItemTextColor);
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

public static void setIgnoresAlpha(boolean flag) {
	OS.objc_msgSend(OS.class_NSColor, OS.sel_setIgnoresAlpha_1, flag);
}

public void setStroke() {
	OS.objc_msgSend(this.id, OS.sel_setStroke);
}

public static NSColor shadowColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_shadowColor);
	return result != 0 ? new NSColor(result) : null;
}

public NSColor shadowWithLevel(float val) {
	int result = OS.objc_msgSend(this.id, OS.sel_shadowWithLevel_1, val);
	return result == this.id ? this : (result != 0 ? new NSColor(result) : null);
}

public static NSColor textBackgroundColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_textBackgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor textColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_textColor);
	return result != 0 ? new NSColor(result) : null;
}

public static NSColor whiteColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_whiteColor);
	return result != 0 ? new NSColor(result) : null;
}

public float whiteComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_whiteComponent);
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

public void writeToPasteboard(NSPasteboard pasteBoard) {
	OS.objc_msgSend(this.id, OS.sel_writeToPasteboard_1, pasteBoard != null ? pasteBoard.id : 0);
}

public static NSColor yellowColor() {
	int result = OS.objc_msgSend(OS.class_NSColor, OS.sel_yellowColor);
	return result != 0 ? new NSColor(result) : null;
}

public float yellowComponent() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_yellowComponent);
}

}
