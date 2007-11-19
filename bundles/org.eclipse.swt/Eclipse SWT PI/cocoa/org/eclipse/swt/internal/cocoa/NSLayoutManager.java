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

public class NSLayoutManager extends NSObject {

public NSLayoutManager() {
	super();
}

public NSLayoutManager(int id) {
	super(id);
}

public void addTemporaryAttribute(NSString attrName, id value, NSRange charRange) {
	OS.objc_msgSend(this.id, OS.sel_addTemporaryAttribute_1value_1forCharacterRange_1, attrName != null ? attrName.id : 0, value != null ? value.id : 0, charRange);
}

public void addTemporaryAttributes(NSDictionary attrs, NSRange charRange) {
	OS.objc_msgSend(this.id, OS.sel_addTemporaryAttributes_1forCharacterRange_1, attrs != null ? attrs.id : 0, charRange);
}

public void addTextContainer(NSTextContainer container) {
	OS.objc_msgSend(this.id, OS.sel_addTextContainer_1, container != null ? container.id : 0);
}

public boolean allowsNonContiguousLayout() {
	return OS.objc_msgSend(this.id, OS.sel_allowsNonContiguousLayout) != 0;
}

public NSSize attachmentSizeForGlyphAtIndex(int glyphIndex) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_attachmentSizeForGlyphAtIndex_1, glyphIndex);
	return result;
}

public NSAttributedString attributedString() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributedString);
	return result != 0 ? new NSAttributedString(result) : null;
}

public boolean backgroundLayoutEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_backgroundLayoutEnabled) != 0;
}

public NSRect boundingRectForGlyphRange(NSRange glyphRange, NSTextContainer container) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_boundingRectForGlyphRange_1inTextContainer_1, glyphRange, container != null ? container.id : 0);
	return result;
}

public NSRect boundsRectForTextBlock_atIndex_effectiveRange_(NSTextBlock block, int glyphIndex, int effectiveGlyphRange) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_boundsRectForTextBlock_1atIndex_1effectiveRange_1, block != null ? block.id : 0, glyphIndex, effectiveGlyphRange);
	return result;
}

public NSRect boundsRectForTextBlock_glyphRange_(NSTextBlock block, NSRange glyphRange) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_boundsRectForTextBlock_1glyphRange_1, block != null ? block.id : 0, glyphRange);
	return result;
}

public int characterIndexForGlyphAtIndex(int glyphIndex) {
	return OS.objc_msgSend(this.id, OS.sel_characterIndexForGlyphAtIndex_1, glyphIndex);
}

public NSRange characterRangeForGlyphRange(NSRange glyphRange, int actualGlyphRange) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_characterRangeForGlyphRange_1actualGlyphRange_1, glyphRange, actualGlyphRange);
	return result;
}

public int defaultAttachmentScaling() {
	return OS.objc_msgSend(this.id, OS.sel_defaultAttachmentScaling);
}

public float defaultBaselineOffsetForFont(NSFont theFont) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_defaultBaselineOffsetForFont_1, theFont != null ? theFont.id : 0);
}

public float defaultLineHeightForFont(NSFont theFont) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_defaultLineHeightForFont_1, theFont != null ? theFont.id : 0);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void deleteGlyphsInRange(NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_deleteGlyphsInRange_1, glyphRange);
}

public void drawBackgroundForGlyphRange(NSRange glyphsToShow, NSPoint origin) {
	OS.objc_msgSend(this.id, OS.sel_drawBackgroundForGlyphRange_1atPoint_1, glyphsToShow, origin);
}

public void drawGlyphsForGlyphRange(NSRange glyphsToShow, NSPoint origin) {
	OS.objc_msgSend(this.id, OS.sel_drawGlyphsForGlyphRange_1atPoint_1, glyphsToShow, origin);
}

public void drawStrikethroughForGlyphRange(NSRange glyphRange, int strikethroughVal, float baselineOffset, NSRect lineRect, NSRange lineGlyphRange, NSPoint containerOrigin) {
	OS.objc_msgSend(this.id, OS.sel_drawStrikethroughForGlyphRange_1strikethroughType_1baselineOffset_1lineFragmentRect_1lineFragmentGlyphRange_1containerOrigin_1, glyphRange, strikethroughVal, baselineOffset, lineRect, lineGlyphRange, containerOrigin);
}

public void drawUnderlineForGlyphRange(NSRange glyphRange, int underlineVal, float baselineOffset, NSRect lineRect, NSRange lineGlyphRange, NSPoint containerOrigin) {
	OS.objc_msgSend(this.id, OS.sel_drawUnderlineForGlyphRange_1underlineType_1baselineOffset_1lineFragmentRect_1lineFragmentGlyphRange_1containerOrigin_1, glyphRange, underlineVal, baselineOffset, lineRect, lineGlyphRange, containerOrigin);
}

public boolean drawsOutsideLineFragmentForGlyphAtIndex(int glyphIndex) {
	return OS.objc_msgSend(this.id, OS.sel_drawsOutsideLineFragmentForGlyphAtIndex_1, glyphIndex) != 0;
}

public void ensureGlyphsForCharacterRange(NSRange charRange) {
	OS.objc_msgSend(this.id, OS.sel_ensureGlyphsForCharacterRange_1, charRange);
}

public void ensureGlyphsForGlyphRange(NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_ensureGlyphsForGlyphRange_1, glyphRange);
}

public void ensureLayoutForBoundingRect(NSRect bounds, NSTextContainer container) {
	OS.objc_msgSend(this.id, OS.sel_ensureLayoutForBoundingRect_1inTextContainer_1, bounds, container != null ? container.id : 0);
}

public void ensureLayoutForCharacterRange(NSRange charRange) {
	OS.objc_msgSend(this.id, OS.sel_ensureLayoutForCharacterRange_1, charRange);
}

public void ensureLayoutForGlyphRange(NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_ensureLayoutForGlyphRange_1, glyphRange);
}

public void ensureLayoutForTextContainer(NSTextContainer container) {
	OS.objc_msgSend(this.id, OS.sel_ensureLayoutForTextContainer_1, container != null ? container.id : 0);
}

public NSRect extraLineFragmentRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_extraLineFragmentRect);
	return result;
}

public NSTextContainer extraLineFragmentTextContainer() {
	int result = OS.objc_msgSend(this.id, OS.sel_extraLineFragmentTextContainer);
	return result != 0 ? new NSTextContainer(result) : null;
}

public NSRect extraLineFragmentUsedRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_extraLineFragmentUsedRect);
	return result;
}

public NSTextView firstTextView() {
	int result = OS.objc_msgSend(this.id, OS.sel_firstTextView);
	return result != 0 ? new NSTextView(result) : null;
}

public int firstUnlaidCharacterIndex() {
	return OS.objc_msgSend(this.id, OS.sel_firstUnlaidCharacterIndex);
}

public int firstUnlaidGlyphIndex() {
	return OS.objc_msgSend(this.id, OS.sel_firstUnlaidGlyphIndex);
}

public float fractionOfDistanceThroughGlyphForPoint(NSPoint point, NSTextContainer container) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_fractionOfDistanceThroughGlyphForPoint_1inTextContainer_1, point, container != null ? container.id : 0);
}

public void getFirstUnlaidCharacterIndex(int charIndex, int glyphIndex) {
	OS.objc_msgSend(this.id, OS.sel_getFirstUnlaidCharacterIndex_1glyphIndex_1, charIndex, glyphIndex);
}

public int getGlyphs(int glyphArray, NSRange glyphRange) {
	return OS.objc_msgSend(this.id, OS.sel_getGlyphs_1range_1, glyphArray, glyphRange);
}

public int getGlyphsInRange_glyphs_characterIndexes_glyphInscriptions_elasticBits_(NSRange glyphRange, int glyphBuffer, int charIndexBuffer, int inscribeBuffer, int elasticBuffer) {
	return OS.objc_msgSend(this.id, OS.sel_getGlyphsInRange_1glyphs_1characterIndexes_1glyphInscriptions_1elasticBits_1, glyphRange, glyphBuffer, charIndexBuffer, inscribeBuffer, elasticBuffer);
}

public int getGlyphsInRange_glyphs_characterIndexes_glyphInscriptions_elasticBits_bidiLevels_(NSRange glyphRange, int glyphBuffer, int charIndexBuffer, int inscribeBuffer, int elasticBuffer, int bidiLevelBuffer) {
	return OS.objc_msgSend(this.id, OS.sel_getGlyphsInRange_1glyphs_1characterIndexes_1glyphInscriptions_1elasticBits_1bidiLevels_1, glyphRange, glyphBuffer, charIndexBuffer, inscribeBuffer, elasticBuffer, bidiLevelBuffer);
}

public int getLineFragmentInsertionPointsForCharacterAtIndex(int charIndex, boolean aFlag, boolean dFlag, int positions, int charIndexes) {
	return OS.objc_msgSend(this.id, OS.sel_getLineFragmentInsertionPointsForCharacterAtIndex_1alternatePositions_1inDisplayOrder_1positions_1characterIndexes_1, charIndex, aFlag, dFlag, positions, charIndexes);
}

public int glyphAtIndex_(int glyphIndex) {
	return OS.objc_msgSend(this.id, OS.sel_glyphAtIndex_1, glyphIndex);
}

public int glyphAtIndex_isValidIndex_(int glyphIndex, int isValidIndex) {
	return OS.objc_msgSend(this.id, OS.sel_glyphAtIndex_1isValidIndex_1, glyphIndex, isValidIndex);
}

public NSGlyphGenerator glyphGenerator() {
	int result = OS.objc_msgSend(this.id, OS.sel_glyphGenerator);
	return result != 0 ? new NSGlyphGenerator(result) : null;
}

public int glyphIndexForCharacterAtIndex(int charIndex) {
	return OS.objc_msgSend(this.id, OS.sel_glyphIndexForCharacterAtIndex_1, charIndex);
}

public int glyphIndexForPoint_inTextContainer_(NSPoint point, NSTextContainer container) {
	return OS.objc_msgSend(this.id, OS.sel_glyphIndexForPoint_1inTextContainer_1, point, container != null ? container.id : 0);
}

public int glyphIndexForPoint_inTextContainer_fractionOfDistanceThroughGlyph_(NSPoint point, NSTextContainer container, float[] partialFraction) {
	return OS.objc_msgSend(this.id, OS.sel_glyphIndexForPoint_1inTextContainer_1fractionOfDistanceThroughGlyph_1, point, container != null ? container.id : 0, partialFraction);
}

public NSRange glyphRangeForBoundingRect(NSRect bounds, NSTextContainer container) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_glyphRangeForBoundingRect_1inTextContainer_1, bounds, container != null ? container.id : 0);
	return result;
}

public NSRange glyphRangeForBoundingRectWithoutAdditionalLayout(NSRect bounds, NSTextContainer container) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_glyphRangeForBoundingRectWithoutAdditionalLayout_1inTextContainer_1, bounds, container != null ? container.id : 0);
	return result;
}

public NSRange glyphRangeForCharacterRange(NSRange charRange, int actualCharRange) {
	NSRange result = new NSRange();
	OS.objc_msgSend_struct(result, this.id, OS.sel_glyphRangeForCharacterRange_1actualCharacterRange_1, charRange, actualCharRange);
	return result;
}

public NSRange glyphRangeForTextContainer(NSTextContainer container) {
	NSRange result = new NSRange();
	OS.objc_msgSend_struct(result, this.id, OS.sel_glyphRangeForTextContainer_1, container != null ? container.id : 0);
	return result;
}

public boolean hasNonContiguousLayout() {
	return OS.objc_msgSend(this.id, OS.sel_hasNonContiguousLayout) != 0;
}

public float hyphenationFactor() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_hyphenationFactor);
}

public void insertGlyph(int glyph, int glyphIndex, int charIndex) {
	OS.objc_msgSend(this.id, OS.sel_insertGlyph_1atGlyphIndex_1characterIndex_1, glyph, glyphIndex, charIndex);
}

public void insertGlyphs(int glyphs, int length, int glyphIndex, int charIndex) {
	OS.objc_msgSend(this.id, OS.sel_insertGlyphs_1length_1forStartingGlyphAtIndex_1characterIndex_1, glyphs, length, glyphIndex, charIndex);
}

public void insertTextContainer(NSTextContainer container, int index) {
	OS.objc_msgSend(this.id, OS.sel_insertTextContainer_1atIndex_1, container != null ? container.id : 0, index);
}

public int intAttribute(int attributeTag, int glyphIndex) {
	return OS.objc_msgSend(this.id, OS.sel_intAttribute_1forGlyphAtIndex_1, attributeTag, glyphIndex);
}

public void invalidateDisplayForCharacterRange(NSRange charRange) {
	OS.objc_msgSend(this.id, OS.sel_invalidateDisplayForCharacterRange_1, charRange);
}

public void invalidateDisplayForGlyphRange(NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_invalidateDisplayForGlyphRange_1, glyphRange);
}

public void invalidateGlyphsForCharacterRange(NSRange charRange, int delta, int actualCharRange) {
	OS.objc_msgSend(this.id, OS.sel_invalidateGlyphsForCharacterRange_1changeInLength_1actualCharacterRange_1, charRange, delta, actualCharRange);
}

public void invalidateGlyphsOnLayoutInvalidationForGlyphRange(NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_invalidateGlyphsOnLayoutInvalidationForGlyphRange_1, glyphRange);
}

public void invalidateLayoutForCharacterRange_actualCharacterRange_(NSRange charRange, int actualCharRange) {
	OS.objc_msgSend(this.id, OS.sel_invalidateLayoutForCharacterRange_1actualCharacterRange_1, charRange, actualCharRange);
}

public void invalidateLayoutForCharacterRange_isSoft_actualCharacterRange_(NSRange charRange, boolean flag, int actualCharRange) {
	OS.objc_msgSend(this.id, OS.sel_invalidateLayoutForCharacterRange_1isSoft_1actualCharacterRange_1, charRange, flag, actualCharRange);
}

public boolean isValidGlyphIndex(int glyphIndex) {
	return OS.objc_msgSend(this.id, OS.sel_isValidGlyphIndex_1, glyphIndex) != 0;
}

public boolean layoutManagerOwnsFirstResponderInWindow(NSWindow window) {
	return OS.objc_msgSend(this.id, OS.sel_layoutManagerOwnsFirstResponderInWindow_1, window != null ? window.id : 0) != 0;
}

public int layoutOptions() {
	return OS.objc_msgSend(this.id, OS.sel_layoutOptions);
}

public NSRect layoutRectForTextBlock_atIndex_effectiveRange_(NSTextBlock block, int glyphIndex, int effectiveGlyphRange) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_layoutRectForTextBlock_1atIndex_1effectiveRange_1, block != null ? block.id : 0, glyphIndex, effectiveGlyphRange);
	return result;
}

public NSRect layoutRectForTextBlock_glyphRange_(NSTextBlock block, NSRange glyphRange) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_layoutRectForTextBlock_1glyphRange_1, block != null ? block.id : 0, glyphRange);
	return result;
}

public NSRect lineFragmentRectForGlyphAtIndex_effectiveRange_(int glyphIndex, int effectiveGlyphRange) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_lineFragmentRectForGlyphAtIndex_1effectiveRange_1, glyphIndex, effectiveGlyphRange);
	return result;
}

public NSRect lineFragmentRectForGlyphAtIndex_effectiveRange_withoutAdditionalLayout_(int glyphIndex, int effectiveGlyphRange, boolean flag) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_lineFragmentRectForGlyphAtIndex_1effectiveRange_1withoutAdditionalLayout_1, glyphIndex, effectiveGlyphRange, flag);
	return result;
}

public NSRect lineFragmentUsedRectForGlyphAtIndex_effectiveRange_(int glyphIndex, int effectiveGlyphRange) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_lineFragmentUsedRectForGlyphAtIndex_1effectiveRange_1, glyphIndex, effectiveGlyphRange);
	return result;
}

public NSRect lineFragmentUsedRectForGlyphAtIndex_effectiveRange_withoutAdditionalLayout_(int glyphIndex, int effectiveGlyphRange, boolean flag) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_lineFragmentUsedRectForGlyphAtIndex_1effectiveRange_1withoutAdditionalLayout_1, glyphIndex, effectiveGlyphRange, flag);
	return result;
}

public NSPoint locationForGlyphAtIndex(int glyphIndex) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_struct(result, this.id, OS.sel_locationForGlyphAtIndex_1, glyphIndex);
	return result;
}

public boolean notShownAttributeForGlyphAtIndex(int glyphIndex) {
	return OS.objc_msgSend(this.id, OS.sel_notShownAttributeForGlyphAtIndex_1, glyphIndex) != 0;
}

public int numberOfGlyphs() {
	return OS.objc_msgSend(this.id, OS.sel_numberOfGlyphs);
}

public NSRange rangeOfNominallySpacedGlyphsContainingIndex(int glyphIndex) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rangeOfNominallySpacedGlyphsContainingIndex_1, glyphIndex);
	return result;
}

public int rectArrayForCharacterRange(NSRange charRange, NSRange selCharRange, NSTextContainer container, int rectCount) {
	return OS.objc_msgSend(this.id, OS.sel_rectArrayForCharacterRange_1withinSelectedCharacterRange_1inTextContainer_1rectCount_1, charRange, selCharRange, container != null ? container.id : 0, rectCount);
}

public int rectArrayForGlyphRange(NSRange glyphRange, NSRange selGlyphRange, NSTextContainer container, int rectCount) {
	return OS.objc_msgSend(this.id, OS.sel_rectArrayForGlyphRange_1withinSelectedGlyphRange_1inTextContainer_1rectCount_1, glyphRange, selGlyphRange, container != null ? container.id : 0, rectCount);
}

public void removeTemporaryAttribute(NSString attrName, NSRange charRange) {
	OS.objc_msgSend(this.id, OS.sel_removeTemporaryAttribute_1forCharacterRange_1, attrName != null ? attrName.id : 0, charRange);
}

public void removeTextContainerAtIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_removeTextContainerAtIndex_1, index);
}

public void replaceGlyphAtIndex(int glyphIndex, int newGlyph) {
	OS.objc_msgSend(this.id, OS.sel_replaceGlyphAtIndex_1withGlyph_1, glyphIndex, newGlyph);
}

public void replaceTextStorage(NSTextStorage newTextStorage) {
	OS.objc_msgSend(this.id, OS.sel_replaceTextStorage_1, newTextStorage != null ? newTextStorage.id : 0);
}

public NSView rulerAccessoryViewForTextView(NSTextView view, NSParagraphStyle style, NSRulerView ruler, boolean isEnabled) {
	int result = OS.objc_msgSend(this.id, OS.sel_rulerAccessoryViewForTextView_1paragraphStyle_1ruler_1enabled_1, view != null ? view.id : 0, style != null ? style.id : 0, ruler != null ? ruler.id : 0, isEnabled);
	return result != 0 ? new NSView(result) : null;
}

public NSArray rulerMarkersForTextView(NSTextView view, NSParagraphStyle style, NSRulerView ruler) {
	int result = OS.objc_msgSend(this.id, OS.sel_rulerMarkersForTextView_1paragraphStyle_1ruler_1, view != null ? view.id : 0, style != null ? style.id : 0, ruler != null ? ruler.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public void setAllowsNonContiguousLayout(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsNonContiguousLayout_1, flag);
}

public void setAttachmentSize(NSSize attachmentSize, NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_setAttachmentSize_1forGlyphRange_1, attachmentSize, glyphRange);
}

public void setBackgroundLayoutEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundLayoutEnabled_1, flag);
}

public void setBoundsRect(NSRect rect, NSTextBlock block, NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_setBoundsRect_1forTextBlock_1glyphRange_1, rect, block != null ? block.id : 0, glyphRange);
}

public void setCharacterIndex(int charIndex, int glyphIndex) {
	OS.objc_msgSend(this.id, OS.sel_setCharacterIndex_1forGlyphAtIndex_1, charIndex, glyphIndex);
}

public void setDefaultAttachmentScaling(int scaling) {
	OS.objc_msgSend(this.id, OS.sel_setDefaultAttachmentScaling_1, scaling);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public void setDrawsOutsideLineFragment(boolean flag, int glyphIndex) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsOutsideLineFragment_1forGlyphAtIndex_1, flag, glyphIndex);
}

public void setExtraLineFragmentRect(NSRect fragmentRect, NSRect usedRect, NSTextContainer container) {
	OS.objc_msgSend(this.id, OS.sel_setExtraLineFragmentRect_1usedRect_1textContainer_1, fragmentRect, usedRect, container != null ? container.id : 0);
}

public void setGlyphGenerator(NSGlyphGenerator glyphGenerator) {
	OS.objc_msgSend(this.id, OS.sel_setGlyphGenerator_1, glyphGenerator != null ? glyphGenerator.id : 0);
}

public void setHyphenationFactor(float factor) {
	OS.objc_msgSend(this.id, OS.sel_setHyphenationFactor_1, factor);
}

public void setIntAttribute(int attributeTag, int val, int glyphIndex) {
	OS.objc_msgSend(this.id, OS.sel_setIntAttribute_1value_1forGlyphAtIndex_1, attributeTag, val, glyphIndex);
}

public void setLayoutRect(NSRect rect, NSTextBlock block, NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_setLayoutRect_1forTextBlock_1glyphRange_1, rect, block != null ? block.id : 0, glyphRange);
}

public void setLineFragmentRect(NSRect fragmentRect, NSRange glyphRange, NSRect usedRect) {
	OS.objc_msgSend(this.id, OS.sel_setLineFragmentRect_1forGlyphRange_1usedRect_1, fragmentRect, glyphRange, usedRect);
}

public void setLocation(NSPoint location, NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_setLocation_1forStartOfGlyphRange_1, location, glyphRange);
}

public void setLocations(int locations, int glyphIndexes, int count, NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_setLocations_1startingGlyphIndexes_1count_1forGlyphRange_1, locations, glyphIndexes, count, glyphRange);
}

public void setNotShownAttribute(boolean flag, int glyphIndex) {
	OS.objc_msgSend(this.id, OS.sel_setNotShownAttribute_1forGlyphAtIndex_1, flag, glyphIndex);
}

public void setShowsControlCharacters(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setShowsControlCharacters_1, flag);
}

public void setShowsInvisibleCharacters(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setShowsInvisibleCharacters_1, flag);
}

public void setTemporaryAttributes(NSDictionary attrs, NSRange charRange) {
	OS.objc_msgSend(this.id, OS.sel_setTemporaryAttributes_1forCharacterRange_1, attrs != null ? attrs.id : 0, charRange);
}

public void setTextContainer(NSTextContainer container, NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_setTextContainer_1forGlyphRange_1, container != null ? container.id : 0, glyphRange);
}

public void setTextStorage(NSTextStorage textStorage) {
	OS.objc_msgSend(this.id, OS.sel_setTextStorage_1, textStorage != null ? textStorage.id : 0);
}

public void setTypesetter(NSTypesetter typesetter) {
	OS.objc_msgSend(this.id, OS.sel_setTypesetter_1, typesetter != null ? typesetter.id : 0);
}

public void setTypesetterBehavior(int theBehavior) {
	OS.objc_msgSend(this.id, OS.sel_setTypesetterBehavior_1, theBehavior);
}

public void setUsesFontLeading(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setUsesFontLeading_1, flag);
}

public void setUsesScreenFonts(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setUsesScreenFonts_1, flag);
}

public void showAttachmentCell(NSCell cell, NSRect rect, int attachmentIndex) {
	OS.objc_msgSend(this.id, OS.sel_showAttachmentCell_1inRect_1characterIndex_1, cell != null ? cell.id : 0, rect, attachmentIndex);
}

public void showPackedGlyphs(int glyphs, int glyphLen, NSRange glyphRange, NSPoint point, NSFont font, NSColor color, NSSize printingAdjustment) {
	OS.objc_msgSend(this.id, OS.sel_showPackedGlyphs_1length_1glyphRange_1atPoint_1font_1color_1printingAdjustment_1, glyphs, glyphLen, glyphRange, point, font != null ? font.id : 0, color != null ? color.id : 0, printingAdjustment);
}

public boolean showsControlCharacters() {
	return OS.objc_msgSend(this.id, OS.sel_showsControlCharacters) != 0;
}

public boolean showsInvisibleCharacters() {
	return OS.objc_msgSend(this.id, OS.sel_showsInvisibleCharacters) != 0;
}

public void strikethroughGlyphRange(NSRange glyphRange, int strikethroughVal, NSRect lineRect, NSRange lineGlyphRange, NSPoint containerOrigin) {
	OS.objc_msgSend(this.id, OS.sel_strikethroughGlyphRange_1strikethroughType_1lineFragmentRect_1lineFragmentGlyphRange_1containerOrigin_1, glyphRange, strikethroughVal, lineRect, lineGlyphRange, containerOrigin);
}

public NSFont substituteFontForFont(NSFont originalFont) {
	int result = OS.objc_msgSend(this.id, OS.sel_substituteFontForFont_1, originalFont != null ? originalFont.id : 0);
	return result != 0 ? new NSFont(result) : null;
}

public id temporaryAttribute_atCharacterIndex_effectiveRange_(NSString attrName, int location, int range) {
	int result = OS.objc_msgSend(this.id, OS.sel_temporaryAttribute_1atCharacterIndex_1effectiveRange_1, attrName != null ? attrName.id : 0, location, range);
	return result != 0 ? new id(result) : null;
}

public id temporaryAttribute_atCharacterIndex_longestEffectiveRange_inRange_(NSString attrName, int location, int range, NSRange rangeLimit) {
	int result = OS.objc_msgSend(this.id, OS.sel_temporaryAttribute_1atCharacterIndex_1longestEffectiveRange_1inRange_1, attrName != null ? attrName.id : 0, location, range, rangeLimit);
	return result != 0 ? new id(result) : null;
}

public NSDictionary temporaryAttributesAtCharacterIndex_effectiveRange_(int charIndex, int effectiveCharRange) {
	int result = OS.objc_msgSend(this.id, OS.sel_temporaryAttributesAtCharacterIndex_1effectiveRange_1, charIndex, effectiveCharRange);
	return result != 0 ? new NSDictionary(result) : null;
}

public NSDictionary temporaryAttributesAtCharacterIndex_longestEffectiveRange_inRange_(int location, int range, NSRange rangeLimit) {
	int result = OS.objc_msgSend(this.id, OS.sel_temporaryAttributesAtCharacterIndex_1longestEffectiveRange_1inRange_1, location, range, rangeLimit);
	return result != 0 ? new NSDictionary(result) : null;
}

public void textContainerChangedGeometry(NSTextContainer container) {
	OS.objc_msgSend(this.id, OS.sel_textContainerChangedGeometry_1, container != null ? container.id : 0);
}

public void textContainerChangedTextView(NSTextContainer container) {
	OS.objc_msgSend(this.id, OS.sel_textContainerChangedTextView_1, container != null ? container.id : 0);
}

public NSTextContainer textContainerForGlyphAtIndex_effectiveRange_(int glyphIndex, int effectiveGlyphRange) {
	int result = OS.objc_msgSend(this.id, OS.sel_textContainerForGlyphAtIndex_1effectiveRange_1, glyphIndex, effectiveGlyphRange);
	return result != 0 ? new NSTextContainer(result) : null;
}

public NSTextContainer textContainerForGlyphAtIndex_effectiveRange_withoutAdditionalLayout_(int glyphIndex, int effectiveGlyphRange, boolean flag) {
	int result = OS.objc_msgSend(this.id, OS.sel_textContainerForGlyphAtIndex_1effectiveRange_1withoutAdditionalLayout_1, glyphIndex, effectiveGlyphRange, flag);
	return result != 0 ? new NSTextContainer(result) : null;
}

public NSArray textContainers() {
	int result = OS.objc_msgSend(this.id, OS.sel_textContainers);
	return result != 0 ? new NSArray(result) : null;
}

public NSTextStorage textStorage() {
	int result = OS.objc_msgSend(this.id, OS.sel_textStorage);
	return result != 0 ? new NSTextStorage(result) : null;
}

public void textStorage_edited_range_changeInLength_invalidatedRange_(NSTextStorage str, int editedMask, NSRange newCharRange, int delta, NSRange invalidatedCharRange) {
	OS.objc_msgSend(this.id, OS.sel_textStorage_1edited_1range_1changeInLength_1invalidatedRange_1, str != null ? str.id : 0, editedMask, newCharRange, delta, invalidatedCharRange);
}

public NSTextView textViewForBeginningOfSelection() {
	int result = OS.objc_msgSend(this.id, OS.sel_textViewForBeginningOfSelection);
	return result != 0 ? new NSTextView(result) : null;
}

public NSTypesetter typesetter() {
	int result = OS.objc_msgSend(this.id, OS.sel_typesetter);
	return result != 0 ? new NSTypesetter(result) : null;
}

public int typesetterBehavior() {
	return OS.objc_msgSend(this.id, OS.sel_typesetterBehavior);
}

public void underlineGlyphRange(NSRange glyphRange, int underlineVal, NSRect lineRect, NSRange lineGlyphRange, NSPoint containerOrigin) {
	OS.objc_msgSend(this.id, OS.sel_underlineGlyphRange_1underlineType_1lineFragmentRect_1lineFragmentGlyphRange_1containerOrigin_1, glyphRange, underlineVal, lineRect, lineGlyphRange, containerOrigin);
}

public NSRect usedRectForTextContainer(NSTextContainer container) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_usedRectForTextContainer_1, container != null ? container.id : 0);
	return result;
}

public boolean usesFontLeading() {
	return OS.objc_msgSend(this.id, OS.sel_usesFontLeading) != 0;
}

public boolean usesScreenFonts() {
	return OS.objc_msgSend(this.id, OS.sel_usesScreenFonts) != 0;
}

}
