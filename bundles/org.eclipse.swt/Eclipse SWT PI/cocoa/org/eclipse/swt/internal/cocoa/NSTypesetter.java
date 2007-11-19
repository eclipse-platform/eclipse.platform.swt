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

public class NSTypesetter extends NSObject {

public NSTypesetter() {
	super();
}

public NSTypesetter(int id) {
	super(id);
}

public int actionForControlCharacterAtIndex(int charIndex) {
	return OS.objc_msgSend(this.id, OS.sel_actionForControlCharacterAtIndex_1, charIndex);
}

public NSAttributedString attributedString() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributedString);
	return result != 0 ? new NSAttributedString(result) : null;
}

public NSDictionary attributesForExtraLineFragment() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributesForExtraLineFragment);
	return result != 0 ? new NSDictionary(result) : null;
}

public float baselineOffsetInLayoutManager(NSLayoutManager layoutMgr, int glyphIndex) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_baselineOffsetInLayoutManager_1glyphIndex_1, layoutMgr != null ? layoutMgr.id : 0, glyphIndex);
}

public void beginLineWithGlyphAtIndex(int glyphIndex) {
	OS.objc_msgSend(this.id, OS.sel_beginLineWithGlyphAtIndex_1, glyphIndex);
}

public void beginParagraph() {
	OS.objc_msgSend(this.id, OS.sel_beginParagraph);
}

public boolean bidiProcessingEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_bidiProcessingEnabled) != 0;
}

public NSRect boundingBoxForControlGlyphAtIndex(int glyphIndex, NSTextContainer textContainer, NSRect proposedRect, NSPoint glyphPosition, int charIndex) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_boundingBoxForControlGlyphAtIndex_1forTextContainer_1proposedLineFragment_1glyphPosition_1characterIndex_1, glyphIndex, textContainer != null ? textContainer.id : 0, proposedRect, glyphPosition, charIndex);
	return result;
}

public NSRange characterRangeForGlyphRange(NSRange glyphRange, int actualGlyphRange) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_characterRangeForGlyphRange_1actualGlyphRange_1, glyphRange, actualGlyphRange);
	return result;
}

public NSParagraphStyle currentParagraphStyle() {
	int result = OS.objc_msgSend(this.id, OS.sel_currentParagraphStyle);
	return result != 0 ? new NSParagraphStyle(result) : null;
}

public NSTextContainer currentTextContainer() {
	int result = OS.objc_msgSend(this.id, OS.sel_currentTextContainer);
	return result != 0 ? new NSTextContainer(result) : null;
}

public static int defaultTypesetterBehavior() {
	return OS.objc_msgSend(OS.class_NSTypesetter, OS.sel_defaultTypesetterBehavior);
}

public void deleteGlyphsInRange(NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_deleteGlyphsInRange_1, glyphRange);
}

public void endLineWithGlyphRange(NSRange lineGlyphRange) {
	OS.objc_msgSend(this.id, OS.sel_endLineWithGlyphRange_1, lineGlyphRange);
}

public void endParagraph() {
	OS.objc_msgSend(this.id, OS.sel_endParagraph);
}

public int getGlyphsInRange(NSRange glyphsRange, int glyphBuffer, int charIndexBuffer, int inscribeBuffer, int elasticBuffer, int bidiLevelBuffer) {
	return OS.objc_msgSend(this.id, OS.sel_getGlyphsInRange_1glyphs_1characterIndexes_1glyphInscriptions_1elasticBits_1bidiLevels_1, glyphsRange, glyphBuffer, charIndexBuffer, inscribeBuffer, elasticBuffer, bidiLevelBuffer);
}

public void getLineFragmentRect_usedRect_forParagraphSeparatorGlyphRange_atProposedOrigin_(int lineFragmentRect, int lineFragmentUsedRect, NSRange paragraphSeparatorGlyphRange, NSPoint lineOrigin) {
	OS.objc_msgSend(this.id, OS.sel_getLineFragmentRect_1usedRect_1forParagraphSeparatorGlyphRange_1atProposedOrigin_1, lineFragmentRect, lineFragmentUsedRect, paragraphSeparatorGlyphRange, lineOrigin);
}

public void getLineFragmentRect_usedRect_remainingRect_forStartingGlyphAtIndex_proposedRect_lineSpacing_paragraphSpacingBefore_paragraphSpacingAfter_(int lineFragmentRect, int lineFragmentUsedRect, int remainingRect, int startingGlyphIndex, NSRect proposedRect, float lineSpacing, float paragraphSpacingBefore, float paragraphSpacingAfter) {
	OS.objc_msgSend(this.id, OS.sel_getLineFragmentRect_1usedRect_1remainingRect_1forStartingGlyphAtIndex_1proposedRect_1lineSpacing_1paragraphSpacingBefore_1paragraphSpacingAfter_1, lineFragmentRect, lineFragmentUsedRect, remainingRect, startingGlyphIndex, proposedRect, lineSpacing, paragraphSpacingBefore, paragraphSpacingAfter);
}

public NSRange glyphRangeForCharacterRange(NSRange charRange, int actualCharRange) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_glyphRangeForCharacterRange_1actualCharacterRange_1, charRange, actualCharRange);
	return result;
}

public int hyphenCharacterForGlyphAtIndex(int glyphIndex) {
	return OS.objc_msgSend(this.id, OS.sel_hyphenCharacterForGlyphAtIndex_1, glyphIndex);
}

public float hyphenationFactor() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_hyphenationFactor);
}

public float hyphenationFactorForGlyphAtIndex(int glyphIndex) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_hyphenationFactorForGlyphAtIndex_1, glyphIndex);
}

public void insertGlyph(int glyph, int glyphIndex, int characterIndex) {
	OS.objc_msgSend(this.id, OS.sel_insertGlyph_1atGlyphIndex_1characterIndex_1, glyph, glyphIndex, characterIndex);
}

public NSRange layoutCharactersInRange(NSRange characterRange, NSLayoutManager layoutManager, int maxNumLines) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_layoutCharactersInRange_1forLayoutManager_1maximumNumberOfLineFragments_1, characterRange, layoutManager != null ? layoutManager.id : 0, maxNumLines);
	return result;
}

public void layoutGlyphsInLayoutManager(NSLayoutManager layoutManager, int startGlyphIndex, int maxNumLines, int nextGlyph) {
	OS.objc_msgSend(this.id, OS.sel_layoutGlyphsInLayoutManager_1startingAtGlyphIndex_1maxNumberOfLineFragments_1nextGlyphIndex_1, layoutManager != null ? layoutManager.id : 0, startGlyphIndex, maxNumLines, nextGlyph);
}

public NSLayoutManager layoutManager() {
	int result = OS.objc_msgSend(this.id, OS.sel_layoutManager);
	return result != 0 ? new NSLayoutManager(result) : null;
}

public int layoutParagraphAtPoint(int lineFragmentOrigin) {
	return OS.objc_msgSend(this.id, OS.sel_layoutParagraphAtPoint_1, lineFragmentOrigin);
}

public float lineFragmentPadding() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_lineFragmentPadding);
}

public float lineSpacingAfterGlyphAtIndex(int glyphIndex, NSRect rect) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_lineSpacingAfterGlyphAtIndex_1withProposedLineFragmentRect_1, glyphIndex, rect);
}

public NSRange paragraphCharacterRange() {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_paragraphCharacterRange);
	return result;
}

public NSRange paragraphGlyphRange() {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_paragraphGlyphRange);
	return result;
}

public NSRange paragraphSeparatorCharacterRange() {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_paragraphSeparatorCharacterRange);
	return result;
}

public NSRange paragraphSeparatorGlyphRange() {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_paragraphSeparatorGlyphRange);
	return result;
}

public float paragraphSpacingAfterGlyphAtIndex(int glyphIndex, NSRect rect) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_paragraphSpacingAfterGlyphAtIndex_1withProposedLineFragmentRect_1, glyphIndex, rect);
}

public float paragraphSpacingBeforeGlyphAtIndex(int glyphIndex, NSRect rect) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_paragraphSpacingBeforeGlyphAtIndex_1withProposedLineFragmentRect_1, glyphIndex, rect);
}

public static NSSize printingAdjustmentInLayoutManager(NSLayoutManager layoutMgr, NSRange nominallySpacedGlyphsRange, int packedGlyphs, int packedGlyphsCount) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, OS.class_NSTypesetter, OS.sel_printingAdjustmentInLayoutManager_1forNominallySpacedGlyphRange_1packedGlyphs_1count_1, layoutMgr != null ? layoutMgr.id : 0, nominallySpacedGlyphsRange, packedGlyphs, packedGlyphsCount);
	return result;
}

public void setAttachmentSize(NSSize attachmentSize, NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_setAttachmentSize_1forGlyphRange_1, attachmentSize, glyphRange);
}

public void setAttributedString(NSAttributedString attrString) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedString_1, attrString != null ? attrString.id : 0);
}

public void setBidiLevels(int levels, NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_setBidiLevels_1forGlyphRange_1, levels, glyphRange);
}

public void setBidiProcessingEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBidiProcessingEnabled_1, flag);
}

public void setDrawsOutsideLineFragment(boolean flag, NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsOutsideLineFragment_1forGlyphRange_1, flag, glyphRange);
}

public void setHardInvalidation(boolean flag, NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_setHardInvalidation_1forGlyphRange_1, flag, glyphRange);
}

public void setHyphenationFactor(float factor) {
	OS.objc_msgSend(this.id, OS.sel_setHyphenationFactor_1, factor);
}

public void setLineFragmentPadding(float padding) {
	OS.objc_msgSend(this.id, OS.sel_setLineFragmentPadding_1, padding);
}

public void setLineFragmentRect(NSRect fragmentRect, NSRange glyphRange, NSRect usedRect, float baselineOffset) {
	OS.objc_msgSend(this.id, OS.sel_setLineFragmentRect_1forGlyphRange_1usedRect_1baselineOffset_1, fragmentRect, glyphRange, usedRect, baselineOffset);
}

public void setLocation(NSPoint location, int advancements, NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_setLocation_1withAdvancements_1forStartOfGlyphRange_1, location, advancements, glyphRange);
}

public void setNotShownAttribute(boolean flag, NSRange glyphRange) {
	OS.objc_msgSend(this.id, OS.sel_setNotShownAttribute_1forGlyphRange_1, flag, glyphRange);
}

public void setParagraphGlyphRange(NSRange paragraphRange, NSRange paragraphSeparatorRange) {
	OS.objc_msgSend(this.id, OS.sel_setParagraphGlyphRange_1separatorGlyphRange_1, paragraphRange, paragraphSeparatorRange);
}

public void setTypesetterBehavior(int behavior) {
	OS.objc_msgSend(this.id, OS.sel_setTypesetterBehavior_1, behavior);
}

public void setUsesFontLeading(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setUsesFontLeading_1, flag);
}

public static id sharedSystemTypesetter() {
	int result = OS.objc_msgSend(OS.class_NSTypesetter, OS.sel_sharedSystemTypesetter);
	return result != 0 ? new id(result) : null;
}

public static id sharedSystemTypesetterForBehavior(int theBehavior) {
	int result = OS.objc_msgSend(OS.class_NSTypesetter, OS.sel_sharedSystemTypesetterForBehavior_1, theBehavior);
	return result != 0 ? new id(result) : null;
}

public boolean shouldBreakLineByHyphenatingBeforeCharacterAtIndex(int charIndex) {
	return OS.objc_msgSend(this.id, OS.sel_shouldBreakLineByHyphenatingBeforeCharacterAtIndex_1, charIndex) != 0;
}

public boolean shouldBreakLineByWordBeforeCharacterAtIndex(int charIndex) {
	return OS.objc_msgSend(this.id, OS.sel_shouldBreakLineByWordBeforeCharacterAtIndex_1, charIndex) != 0;
}

public NSFont substituteFontForFont(NSFont originalFont) {
	int result = OS.objc_msgSend(this.id, OS.sel_substituteFontForFont_1, originalFont != null ? originalFont.id : 0);
	return result != 0 ? new NSFont(result) : null;
}

public void substituteGlyphsInRange(NSRange glyphRange, int glyphs) {
	OS.objc_msgSend(this.id, OS.sel_substituteGlyphsInRange_1withGlyphs_1, glyphRange, glyphs);
}

public NSArray textContainers() {
	int result = OS.objc_msgSend(this.id, OS.sel_textContainers);
	return result != 0 ? new NSArray(result) : null;
}

public NSTextTab textTabForGlyphLocation(float glyphLocation, int direction, float maxLocation) {
	int result = OS.objc_msgSend(this.id, OS.sel_textTabForGlyphLocation_1writingDirection_1maxLocation_1, glyphLocation, direction, maxLocation);
	return result != 0 ? new NSTextTab(result) : null;
}

public int typesetterBehavior() {
	return OS.objc_msgSend(this.id, OS.sel_typesetterBehavior);
}

public boolean usesFontLeading() {
	return OS.objc_msgSend(this.id, OS.sel_usesFontLeading) != 0;
}

public void willSetLineFragmentRect(int lineRect, NSRange glyphRange, int usedRect, int baselineOffset) {
	OS.objc_msgSend(this.id, OS.sel_willSetLineFragmentRect_1forGlyphRange_1usedRect_1baselineOffset_1, lineRect, glyphRange, usedRect, baselineOffset);
}

}
