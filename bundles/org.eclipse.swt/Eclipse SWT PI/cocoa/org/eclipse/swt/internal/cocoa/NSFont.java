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

public class NSFont extends NSObject {

public NSFont() {
	super();
}

public NSFont(int id) {
	super(id);
}

public NSSize advancementForGlyph(int ag) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_advancementForGlyph_1, ag);
	return result;
}

public NSDictionary afmDictionary() {
	int result = OS.objc_msgSend(this.id, OS.sel_afmDictionary);
	return result != 0 ? new NSDictionary(result) : null;
}

public float ascender() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_ascender);
}

public static NSFont boldSystemFontOfSize(float fontSize) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_boldSystemFontOfSize_1, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public NSRect boundingRectForFont() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_boundingRectForFont);
	return result;
}

public NSRect boundingRectForGlyph(int aGlyph) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_boundingRectForGlyph_1, aGlyph);
	return result;
}

public float capHeight() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_capHeight);
}

public static NSFont controlContentFontOfSize(float fontSize) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_controlContentFontOfSize_1, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public NSCharacterSet coveredCharacterSet() {
	int result = OS.objc_msgSend(this.id, OS.sel_coveredCharacterSet);
	return result != 0 ? new NSCharacterSet(result) : null;
}

public float defaultLineHeightForFont() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_defaultLineHeightForFont);
}

public float descender() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_descender);
}

public NSString displayName() {
	int result = OS.objc_msgSend(this.id, OS.sel_displayName);
	return result != 0 ? new NSString(result) : null;
}

public NSString encodingScheme() {
	int result = OS.objc_msgSend(this.id, OS.sel_encodingScheme);
	return result != 0 ? new NSString(result) : null;
}

public NSString familyName() {
	int result = OS.objc_msgSend(this.id, OS.sel_familyName);
	return result != 0 ? new NSString(result) : null;
}

public NSFontDescriptor fontDescriptor() {
	int result = OS.objc_msgSend(this.id, OS.sel_fontDescriptor);
	return result != 0 ? new NSFontDescriptor(result) : null;
}

public NSString fontName() {
	int result = OS.objc_msgSend(this.id, OS.sel_fontName);
	return result != 0 ? new NSString(result) : null;
}

public static NSFont static_fontWithDescriptor_size_(NSFontDescriptor fontDescriptor, float fontSize) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_fontWithDescriptor_1size_1, fontDescriptor != null ? fontDescriptor.id : 0, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public static NSFont static_fontWithDescriptor_textTransform_(NSFontDescriptor fontDescriptor, NSAffineTransform textTransform) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_fontWithDescriptor_1textTransform_1, fontDescriptor != null ? fontDescriptor.id : 0, textTransform != null ? textTransform.id : 0);
	return result != 0 ? new NSFont(result) : null;
}

public static NSFont static_fontWithName_matrix_(NSString fontName, int fontMatrix) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_fontWithName_1matrix_1, fontName != null ? fontName.id : 0, fontMatrix);
	return result != 0 ? new NSFont(result) : null;
}

public static NSFont static_fontWithName_size_(NSString fontName, float fontSize) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_fontWithName_1size_1, fontName != null ? fontName.id : 0, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public void getAdvancements_forGlyphs_count_(int advancements, int glyphs, int glyphCount) {
	OS.objc_msgSend(this.id, OS.sel_getAdvancements_1forGlyphs_1count_1, advancements, glyphs, glyphCount);
}

public void getAdvancements_forPackedGlyphs_length_(int advancements, int packedGlyphs, int length) {
	OS.objc_msgSend(this.id, OS.sel_getAdvancements_1forPackedGlyphs_1length_1, advancements, packedGlyphs, length);
}

public void getBoundingRects(int bounds, int glyphs, int glyphCount) {
	OS.objc_msgSend(this.id, OS.sel_getBoundingRects_1forGlyphs_1count_1, bounds, glyphs, glyphCount);
}

public boolean glyphIsEncoded(int aGlyph) {
	return OS.objc_msgSend(this.id, OS.sel_glyphIsEncoded_1, aGlyph) != 0;
}

public int glyphPacking() {
	return OS.objc_msgSend(this.id, OS.sel_glyphPacking);
}

public int glyphWithName(NSString aName) {
	return OS.objc_msgSend(this.id, OS.sel_glyphWithName_1, aName != null ? aName.id : 0);
}

public boolean isBaseFont() {
	return OS.objc_msgSend(this.id, OS.sel_isBaseFont) != 0;
}

public boolean isFixedPitch() {
	return OS.objc_msgSend(this.id, OS.sel_isFixedPitch) != 0;
}

public float italicAngle() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_italicAngle);
}

public static NSFont labelFontOfSize(float fontSize) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_labelFontOfSize_1, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public static float labelFontSize() {
	return (float)OS.objc_msgSend_fpret(OS.class_NSFont, OS.sel_labelFontSize);
}

public float leading() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_leading);
}

public int matrix() {
	return OS.objc_msgSend(this.id, OS.sel_matrix);
}

public NSSize maximumAdvancement() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_maximumAdvancement);
	return result;
}

public static NSFont menuBarFontOfSize(float fontSize) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_menuBarFontOfSize_1, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public static NSFont menuFontOfSize(float fontSize) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_menuFontOfSize_1, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public static NSFont messageFontOfSize(float fontSize) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_messageFontOfSize_1, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public int mostCompatibleStringEncoding() {
	return OS.objc_msgSend(this.id, OS.sel_mostCompatibleStringEncoding);
}

public int numberOfGlyphs() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfGlyphs);
}

public static NSFont paletteFontOfSize(float fontSize) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_paletteFontOfSize_1, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public float pointSize() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_pointSize);
}

public NSPoint positionOfGlyph_forCharacter_struckOverRect_(int aGlyph, short aChar, NSRect aRect) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_positionOfGlyph_1forCharacter_1struckOverRect_1, aGlyph, aChar, aRect);
	return result;
}

public NSPoint positionOfGlyph_precededByGlyph_isNominal_(int curGlyph, int prevGlyph, int nominal) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_positionOfGlyph_1precededByGlyph_1isNominal_1, curGlyph, prevGlyph, nominal);
	return result;
}

public NSPoint positionOfGlyph_struckOverGlyph_metricsExist_(int curGlyph, int prevGlyph, int exist) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_positionOfGlyph_1struckOverGlyph_1metricsExist_1, curGlyph, prevGlyph, exist);
	return result;
}

public NSPoint positionOfGlyph_struckOverRect_metricsExist_(int aGlyph, NSRect aRect, int exist) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_positionOfGlyph_1struckOverRect_1metricsExist_1, aGlyph, aRect, exist);
	return result;
}

public NSPoint positionOfGlyph_withRelation_toBaseGlyph_totalAdvancement_metricsExist_(int thisGlyph, int rel, int baseGlyph, int adv, int exist) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_positionOfGlyph_1withRelation_1toBaseGlyph_1totalAdvancement_1metricsExist_1, thisGlyph, rel, baseGlyph, adv, exist);
	return result;
}

public int positionsForCompositeSequence(int someGlyphs, int numGlyphs, int points) {
	return OS.objc_msgSend(this.id, OS.sel_positionsForCompositeSequence_1numberOfGlyphs_1pointArray_1, someGlyphs, numGlyphs, points);
}

public static NSArray preferredFontNames() {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_preferredFontNames);
	return result != 0 ? new NSArray(result) : null;
}

public NSFont printerFont() {
	int result = OS.objc_msgSend(this.id, OS.sel_printerFont);
	return result == this.id ? this : (result != 0 ? new NSFont(result) : null);
}

public int renderingMode() {
	return OS.objc_msgSend(this.id, OS.sel_renderingMode);
}

public NSFont screenFont() {
	int result = OS.objc_msgSend(this.id, OS.sel_screenFont);
	return result == this.id ? this : (result != 0 ? new NSFont(result) : null);
}

public NSFont screenFontWithRenderingMode(int renderingMode) {
	int result = OS.objc_msgSend(this.id, OS.sel_screenFontWithRenderingMode_1, renderingMode);
	return result == this.id ? this : (result != 0 ? new NSFont(result) : null);
}

public void set() {
	OS.objc_msgSend(this.id, OS.sel_set);
}

public void setInContext(NSGraphicsContext graphicsContext) {
	OS.objc_msgSend(this.id, OS.sel_setInContext_1, graphicsContext != null ? graphicsContext.id : 0);
}

public static void setPreferredFontNames(NSArray fontNameArray) {
	OS.objc_msgSend(OS.class_NSFont, OS.sel_setPreferredFontNames_1, fontNameArray != null ? fontNameArray.id : 0);
}

public static void setUserFixedPitchFont(NSFont aFont) {
	OS.objc_msgSend(OS.class_NSFont, OS.sel_setUserFixedPitchFont_1, aFont != null ? aFont.id : 0);
}

public static void setUserFont(NSFont aFont) {
	OS.objc_msgSend(OS.class_NSFont, OS.sel_setUserFont_1, aFont != null ? aFont.id : 0);
}

public static float smallSystemFontSize() {
	return (float)OS.objc_msgSend_fpret(OS.class_NSFont, OS.sel_smallSystemFontSize);
}

public static NSFont systemFontOfSize(float fontSize) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_systemFontOfSize_1, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public static float systemFontSize() {
	return (float)OS.objc_msgSend_fpret(OS.class_NSFont, OS.sel_systemFontSize);
}

public static float systemFontSizeForControlSize(int controlSize) {
	return (float)OS.objc_msgSend_fpret(OS.class_NSFont, OS.sel_systemFontSizeForControlSize_1, controlSize);
}

public NSAffineTransform textTransform() {
	int result = OS.objc_msgSend(this.id, OS.sel_textTransform);
	return result != 0 ? new NSAffineTransform(result) : null;
}

public static NSFont titleBarFontOfSize(float fontSize) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_titleBarFontOfSize_1, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public static NSFont toolTipsFontOfSize(float fontSize) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_toolTipsFontOfSize_1, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public float underlinePosition() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_underlinePosition);
}

public float underlineThickness() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_underlineThickness);
}

public static void useFont(NSString fontName) {
	OS.objc_msgSend(OS.class_NSFont, OS.sel_useFont_1, fontName != null ? fontName.id : 0);
}

public static NSFont userFixedPitchFontOfSize(float fontSize) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_userFixedPitchFontOfSize_1, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public static NSFont userFontOfSize(float fontSize) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_userFontOfSize_1, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public float widthOfString(NSString string) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_widthOfString_1, string != null ? string.id : 0);
}

public float xHeight() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_xHeight);
}

}
