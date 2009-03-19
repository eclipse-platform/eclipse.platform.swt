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

public class NSLayoutManager extends NSObject {

public NSLayoutManager() {
	super();
}

public NSLayoutManager(int /*long*/ id) {
	super(id);
}

public NSLayoutManager(id id) {
	super(id);
}

public void addTemporaryAttribute(NSString attrName, id value, NSRange charRange) {
	OS.objc_msgSend(this.id, OS.sel_addTemporaryAttribute_value_forCharacterRange_, attrName != null ? attrName.id : 0, value != null ? value.id : 0, charRange);
}

public void addTextContainer(NSTextContainer container) {
	OS.objc_msgSend(this.id, OS.sel_addTextContainer_, container != null ? container.id : 0);
}

public NSRect boundingRectForGlyphRange(NSRange glyphRange, NSTextContainer container) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_boundingRectForGlyphRange_inTextContainer_, glyphRange, container != null ? container.id : 0);
	return result;
}

public int /*long*/ characterIndexForGlyphAtIndex(int /*long*/ glyphIndex) {
	return OS.objc_msgSend(this.id, OS.sel_characterIndexForGlyphAtIndex_, glyphIndex);
}

public float /*double*/ defaultBaselineOffsetForFont(NSFont theFont) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_defaultBaselineOffsetForFont_, theFont != null ? theFont.id : 0);
}

public float /*double*/ defaultLineHeightForFont(NSFont theFont) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_defaultLineHeightForFont_, theFont != null ? theFont.id : 0);
}

public void drawBackgroundForGlyphRange(NSRange glyphsToShow, NSPoint origin) {
	OS.objc_msgSend(this.id, OS.sel_drawBackgroundForGlyphRange_atPoint_, glyphsToShow, origin);
}

public void drawGlyphsForGlyphRange(NSRange glyphsToShow, NSPoint origin) {
	OS.objc_msgSend(this.id, OS.sel_drawGlyphsForGlyphRange_atPoint_, glyphsToShow, origin);
}

public int /*long*/ getGlyphs(int /*long*/ glyphArray, NSRange glyphRange) {
	return OS.objc_msgSend(this.id, OS.sel_getGlyphs_range_, glyphArray, glyphRange);
}

public int /*long*/ getGlyphsInRange(NSRange glyphRange, int /*long*/ glyphBuffer, int /*long*/ charIndexBuffer, int /*long*/ inscribeBuffer, int /*long*/ elasticBuffer, int /*long*/ bidiLevelBuffer) {
	return OS.objc_msgSend(this.id, OS.sel_getGlyphsInRange_glyphs_characterIndexes_glyphInscriptions_elasticBits_bidiLevels_, glyphRange, glyphBuffer, charIndexBuffer, inscribeBuffer, elasticBuffer, bidiLevelBuffer);
}

public int /*long*/ glyphIndexForCharacterAtIndex(int /*long*/ charIndex) {
	return OS.objc_msgSend(this.id, OS.sel_glyphIndexForCharacterAtIndex_, charIndex);
}

public int /*long*/ glyphIndexForPoint(NSPoint point, NSTextContainer container, float[] /*double[]*/ partialFraction) {
	return OS.objc_msgSend(this.id, OS.sel_glyphIndexForPoint_inTextContainer_fractionOfDistanceThroughGlyph_, point, container != null ? container.id : 0, partialFraction);
}

public NSRange glyphRangeForCharacterRange(NSRange charRange, int /*long*/ actualCharRange) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_glyphRangeForCharacterRange_actualCharacterRange_, charRange, actualCharRange);
	return result;
}

public NSRange glyphRangeForTextContainer(NSTextContainer container) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_glyphRangeForTextContainer_, container != null ? container.id : 0);
	return result;
}

public NSRect lineFragmentUsedRectForGlyphAtIndex(int /*long*/ glyphIndex, int /*long*/ effectiveGlyphRange) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_lineFragmentUsedRectForGlyphAtIndex_effectiveRange_, glyphIndex, effectiveGlyphRange);
	return result;
}

public NSRect lineFragmentUsedRectForGlyphAtIndex(int /*long*/ glyphIndex, int /*long*/ effectiveGlyphRange, boolean flag) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_lineFragmentUsedRectForGlyphAtIndex_effectiveRange_withoutAdditionalLayout_, glyphIndex, effectiveGlyphRange, flag);
	return result;
}

public NSPoint locationForGlyphAtIndex(int /*long*/ glyphIndex) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_locationForGlyphAtIndex_, glyphIndex);
	return result;
}

public int /*long*/ numberOfGlyphs() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfGlyphs);
}

public int /*long*/ rectArrayForCharacterRange(NSRange charRange, NSRange selCharRange, NSTextContainer container, int /*long*/ rectCount) {
	return OS.objc_msgSend(this.id, OS.sel_rectArrayForCharacterRange_withinSelectedCharacterRange_inTextContainer_rectCount_, charRange, selCharRange, container != null ? container.id : 0, rectCount);
}

public void removeTemporaryAttribute(NSString attrName, NSRange charRange) {
	OS.objc_msgSend(this.id, OS.sel_removeTemporaryAttribute_forCharacterRange_, attrName != null ? attrName.id : 0, charRange);
}

public void setLineFragmentRect(NSRect fragmentRect, NSRange glyphRange, NSRect usedRect) {
	OS.objc_msgSend(this.id, OS.sel_setLineFragmentRect_forGlyphRange_usedRect_, fragmentRect, glyphRange, usedRect);
}

public void setTextStorage(NSTextStorage textStorage) {
	OS.objc_msgSend(this.id, OS.sel_setTextStorage_, textStorage != null ? textStorage.id : 0);
}

public NSTypesetter typesetter() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_typesetter);
	return result != 0 ? new NSTypesetter(result) : null;
}

public NSRect usedRectForTextContainer(NSTextContainer container) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_usedRectForTextContainer_, container != null ? container.id : 0);
	return result;
}

}
