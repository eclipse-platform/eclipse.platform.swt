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

public class NSTextView extends NSText {

public NSTextView() {
	super();
}

public NSTextView(int id) {
	super(id);
}

public NSArray acceptableDragTypes() {
	int result = OS.objc_msgSend(this.id, OS.sel_acceptableDragTypes);
	return result != 0 ? new NSArray(result) : null;
}

public boolean acceptsGlyphInfo() {
	return OS.objc_msgSend(this.id, OS.sel_acceptsGlyphInfo) != 0;
}

public void alignJustified(id sender) {
	OS.objc_msgSend(this.id, OS.sel_alignJustified_1, sender != null ? sender.id : 0);
}

public NSArray allowedInputSourceLocales() {
	int result = OS.objc_msgSend(this.id, OS.sel_allowedInputSourceLocales);
	return result != 0 ? new NSArray(result) : null;
}

public boolean allowsDocumentBackgroundColorChange() {
	return OS.objc_msgSend(this.id, OS.sel_allowsDocumentBackgroundColorChange) != 0;
}

public boolean allowsImageEditing() {
	return OS.objc_msgSend(this.id, OS.sel_allowsImageEditing) != 0;
}

public boolean allowsUndo() {
	return OS.objc_msgSend(this.id, OS.sel_allowsUndo) != 0;
}

public NSColor backgroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public void breakUndoCoalescing() {
	OS.objc_msgSend(this.id, OS.sel_breakUndoCoalescing);
}

public void changeAttributes(id sender) {
	OS.objc_msgSend(this.id, OS.sel_changeAttributes_1, sender != null ? sender.id : 0);
}

public void changeColor(id sender) {
	OS.objc_msgSend(this.id, OS.sel_changeColor_1, sender != null ? sender.id : 0);
}

public void changeDocumentBackgroundColor(id sender) {
	OS.objc_msgSend(this.id, OS.sel_changeDocumentBackgroundColor_1, sender != null ? sender.id : 0);
}

public int characterIndexForInsertionAtPoint(NSPoint point) {
	return OS.objc_msgSend(this.id, OS.sel_characterIndexForInsertionAtPoint_1, point);
}

public void cleanUpAfterDragOperation() {
	OS.objc_msgSend(this.id, OS.sel_cleanUpAfterDragOperation);
}

public void clickedOnLink(id link, int charIndex) {
	OS.objc_msgSend(this.id, OS.sel_clickedOnLink_1atIndex_1, link != null ? link.id : 0, charIndex);
}

public void complete(id sender) {
	OS.objc_msgSend(this.id, OS.sel_complete_1, sender != null ? sender.id : 0);
}

public NSArray completionsForPartialWordRange(NSRange charRange, int index) {
	int result = OS.objc_msgSend(this.id, OS.sel_completionsForPartialWordRange_1indexOfSelectedItem_1, charRange, index);
	return result != 0 ? new NSArray(result) : null;
}

public NSParagraphStyle defaultParagraphStyle() {
	int result = OS.objc_msgSend(this.id, OS.sel_defaultParagraphStyle);
	return result != 0 ? new NSParagraphStyle(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void didChangeText() {
	OS.objc_msgSend(this.id, OS.sel_didChangeText);
}

public boolean displaysLinkToolTips() {
	return OS.objc_msgSend(this.id, OS.sel_displaysLinkToolTips) != 0;
}

public NSImage dragImageForSelectionWithEvent(NSEvent event, int origin) {
	int result = OS.objc_msgSend(this.id, OS.sel_dragImageForSelectionWithEvent_1origin_1, event != null ? event.id : 0, origin);
	return result != 0 ? new NSImage(result) : null;
}

public int dragOperationForDraggingInfo(id  dragInfo, NSString type) {
	return OS.objc_msgSend(this.id, OS.sel_dragOperationForDraggingInfo_1type_1, dragInfo != null ? dragInfo.id : 0, type != null ? type.id : 0);
}

public boolean dragSelectionWithEvent(NSEvent event, NSSize mouseOffset, boolean slideBack) {
	return OS.objc_msgSend(this.id, OS.sel_dragSelectionWithEvent_1offset_1slideBack_1, event != null ? event.id : 0, mouseOffset, slideBack) != 0;
}

public void drawInsertionPointInRect(NSRect rect, NSColor color, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_drawInsertionPointInRect_1color_1turnedOn_1, rect, color != null ? color.id : 0, flag);
}

public void drawViewBackgroundInRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_drawViewBackgroundInRect_1, rect);
}

public boolean drawsBackground() {
	return OS.objc_msgSend(this.id, OS.sel_drawsBackground) != 0;
}

public NSRect firstRectForCharacterRange(NSRange range) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, id, OS.sel_firstRectForCharacterRange_1, range);
	return result;
}

public boolean importsGraphics() {
	return OS.objc_msgSend(this.id, OS.sel_importsGraphics) != 0;
}

public id initWithFrame_(NSRect frameRect) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFrame_1, frameRect);
	return result != 0 ? new id(result) : null;
}

public id initWithFrame_textContainer_(NSRect frameRect, NSTextContainer container) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFrame_1textContainer_1, frameRect, container != null ? container.id : 0);
	return result != 0 ? new id(result) : null;
}

public void insertCompletion(NSString word, NSRange charRange, int movement, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_insertCompletion_1forPartialWordRange_1movement_1isFinal_1, word != null ? word.id : 0, charRange, movement, flag);
}

public void insertText(id insertString) {
	OS.objc_msgSend(this.id, OS.sel_insertText_1, insertString != null ? insertString.id : 0);
}

public NSColor insertionPointColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_insertionPointColor);
	return result != 0 ? new NSColor(result) : null;
}

public void invalidateTextContainerOrigin() {
	OS.objc_msgSend(this.id, OS.sel_invalidateTextContainerOrigin);
}

public boolean isAutomaticLinkDetectionEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_isAutomaticLinkDetectionEnabled) != 0;
}

public boolean isAutomaticQuoteSubstitutionEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_isAutomaticQuoteSubstitutionEnabled) != 0;
}

public boolean isContinuousSpellCheckingEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_isContinuousSpellCheckingEnabled) != 0;
}

public boolean isEditable() {
	return OS.objc_msgSend(this.id, OS.sel_isEditable) != 0;
}

public boolean isFieldEditor() {
	return OS.objc_msgSend(this.id, OS.sel_isFieldEditor) != 0;
}

public boolean isGrammarCheckingEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_isGrammarCheckingEnabled) != 0;
}

public boolean isRichText() {
	return OS.objc_msgSend(this.id, OS.sel_isRichText) != 0;
}

public boolean isRulerVisible() {
	return OS.objc_msgSend(this.id, OS.sel_isRulerVisible) != 0;
}

public boolean isSelectable() {
	return OS.objc_msgSend(this.id, OS.sel_isSelectable) != 0;
}

public NSLayoutManager layoutManager() {
	int result = OS.objc_msgSend(this.id, OS.sel_layoutManager);
	return result != 0 ? new NSLayoutManager(result) : null;
}

public NSDictionary linkTextAttributes() {
	int result = OS.objc_msgSend(this.id, OS.sel_linkTextAttributes);
	return result != 0 ? new NSDictionary(result) : null;
}

public void loosenKerning(id sender) {
	OS.objc_msgSend(this.id, OS.sel_loosenKerning_1, sender != null ? sender.id : 0);
}

public void lowerBaseline(id sender) {
	OS.objc_msgSend(this.id, OS.sel_lowerBaseline_1, sender != null ? sender.id : 0);
}

public NSDictionary markedTextAttributes() {
	int result = OS.objc_msgSend(this.id, OS.sel_markedTextAttributes);
	return result != 0 ? new NSDictionary(result) : null;
}

public void orderFrontLinkPanel(id sender) {
	OS.objc_msgSend(this.id, OS.sel_orderFrontLinkPanel_1, sender != null ? sender.id : 0);
}

public void orderFrontListPanel(id sender) {
	OS.objc_msgSend(this.id, OS.sel_orderFrontListPanel_1, sender != null ? sender.id : 0);
}

public void orderFrontSpacingPanel(id sender) {
	OS.objc_msgSend(this.id, OS.sel_orderFrontSpacingPanel_1, sender != null ? sender.id : 0);
}

public void orderFrontTablePanel(id sender) {
	OS.objc_msgSend(this.id, OS.sel_orderFrontTablePanel_1, sender != null ? sender.id : 0);
}

public void outline(id sender) {
	OS.objc_msgSend(this.id, OS.sel_outline_1, sender != null ? sender.id : 0);
}

public void pasteAsPlainText(id sender) {
	OS.objc_msgSend(this.id, OS.sel_pasteAsPlainText_1, sender != null ? sender.id : 0);
}

public void pasteAsRichText(id sender) {
	OS.objc_msgSend(this.id, OS.sel_pasteAsRichText_1, sender != null ? sender.id : 0);
}

public void performFindPanelAction(id sender) {
	OS.objc_msgSend(this.id, OS.sel_performFindPanelAction_1, sender != null ? sender.id : 0);
}

public NSString preferredPasteboardTypeFromArray(NSArray availableTypes, NSArray allowedTypes) {
	int result = OS.objc_msgSend(this.id, OS.sel_preferredPasteboardTypeFromArray_1restrictedToTypesFromArray_1, availableTypes != null ? availableTypes.id : 0, allowedTypes != null ? allowedTypes.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public void raiseBaseline(id sender) {
	OS.objc_msgSend(this.id, OS.sel_raiseBaseline_1, sender != null ? sender.id : 0);
}

public NSRange rangeForUserCharacterAttributeChange() {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rangeForUserCharacterAttributeChange);
	return result;
}

public NSRange rangeForUserCompletion() {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rangeForUserCompletion);
	return result;
}

public NSRange rangeForUserParagraphAttributeChange() {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rangeForUserParagraphAttributeChange);
	return result;
}

public NSRange rangeForUserTextChange() {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rangeForUserTextChange);
	return result;
}

public NSArray rangesForUserCharacterAttributeChange() {
	int result = OS.objc_msgSend(this.id, OS.sel_rangesForUserCharacterAttributeChange);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray rangesForUserParagraphAttributeChange() {
	int result = OS.objc_msgSend(this.id, OS.sel_rangesForUserParagraphAttributeChange);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray rangesForUserTextChange() {
	int result = OS.objc_msgSend(this.id, OS.sel_rangesForUserTextChange);
	return result != 0 ? new NSArray(result) : null;
}

public boolean readSelectionFromPasteboard_(NSPasteboard pboard) {
	return OS.objc_msgSend(this.id, OS.sel_readSelectionFromPasteboard_1, pboard != null ? pboard.id : 0) != 0;
}

public boolean readSelectionFromPasteboard_type_(NSPasteboard pboard, NSString type) {
	return OS.objc_msgSend(this.id, OS.sel_readSelectionFromPasteboard_1type_1, pboard != null ? pboard.id : 0, type != null ? type.id : 0) != 0;
}

public NSArray readablePasteboardTypes() {
	int result = OS.objc_msgSend(this.id, OS.sel_readablePasteboardTypes);
	return result != 0 ? new NSArray(result) : null;
}

public static void registerForServices() {
	OS.objc_msgSend(OS.class_NSTextView, OS.sel_registerForServices);
}

public void replaceTextContainer(NSTextContainer newContainer) {
	OS.objc_msgSend(this.id, OS.sel_replaceTextContainer_1, newContainer != null ? newContainer.id : 0);
}

public void rulerView_didAddMarker_(NSRulerView ruler, NSRulerMarker marker) {
	OS.objc_msgSend(this.id, OS.sel_rulerView_1didAddMarker_1, ruler != null ? ruler.id : 0, marker != null ? marker.id : 0);
}

public void rulerView_didMoveMarker_(NSRulerView ruler, NSRulerMarker marker) {
	OS.objc_msgSend(this.id, OS.sel_rulerView_1didMoveMarker_1, ruler != null ? ruler.id : 0, marker != null ? marker.id : 0);
}

public void rulerView_didRemoveMarker_(NSRulerView ruler, NSRulerMarker marker) {
	OS.objc_msgSend(this.id, OS.sel_rulerView_1didRemoveMarker_1, ruler != null ? ruler.id : 0, marker != null ? marker.id : 0);
}

public void rulerView_handleMouseDown_(NSRulerView ruler, NSEvent event) {
	OS.objc_msgSend(this.id, OS.sel_rulerView_1handleMouseDown_1, ruler != null ? ruler.id : 0, event != null ? event.id : 0);
}

public boolean rulerView_shouldAddMarker_(NSRulerView ruler, NSRulerMarker marker) {
	return OS.objc_msgSend(this.id, OS.sel_rulerView_1shouldAddMarker_1, ruler != null ? ruler.id : 0, marker != null ? marker.id : 0) != 0;
}

public boolean rulerView_shouldMoveMarker_(NSRulerView ruler, NSRulerMarker marker) {
	return OS.objc_msgSend(this.id, OS.sel_rulerView_1shouldMoveMarker_1, ruler != null ? ruler.id : 0, marker != null ? marker.id : 0) != 0;
}

public boolean rulerView_shouldRemoveMarker_(NSRulerView ruler, NSRulerMarker marker) {
	return OS.objc_msgSend(this.id, OS.sel_rulerView_1shouldRemoveMarker_1, ruler != null ? ruler.id : 0, marker != null ? marker.id : 0) != 0;
}

public float rulerView_willAddMarker_atLocation_(NSRulerView ruler, NSRulerMarker marker, float location) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_rulerView_1willAddMarker_1atLocation_1, ruler != null ? ruler.id : 0, marker != null ? marker.id : 0, location);
}

public float rulerView_willMoveMarker_toLocation_(NSRulerView ruler, NSRulerMarker marker, float location) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_rulerView_1willMoveMarker_1toLocation_1, ruler != null ? ruler.id : 0, marker != null ? marker.id : 0, location);
}

public NSArray selectedRanges() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedRanges);
	return result != 0 ? new NSArray(result) : null;
}

public NSDictionary selectedTextAttributes() {
	int result = OS.objc_msgSend(this.id, OS.sel_selectedTextAttributes);
	return result != 0 ? new NSDictionary(result) : null;
}

public int selectionAffinity() {
	return OS.objc_msgSend(this.id, OS.sel_selectionAffinity);
}

public int selectionGranularity() {
	return OS.objc_msgSend(this.id, OS.sel_selectionGranularity);
}

public NSRange selectionRangeForProposedRange(NSRange proposedCharRange, int granularity) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_selectionRangeForProposedRange_1granularity_1, proposedCharRange, granularity);
	return result;
}

public void setAcceptsGlyphInfo(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAcceptsGlyphInfo_1, flag);
}

public void setAlignment(int alignment, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_setAlignment_1range_1, alignment, range);
}

public void setAllowedInputSourceLocales(NSArray localeIdentifiers) {
	OS.objc_msgSend(this.id, OS.sel_setAllowedInputSourceLocales_1, localeIdentifiers != null ? localeIdentifiers.id : 0);
}

public void setAllowsDocumentBackgroundColorChange(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsDocumentBackgroundColorChange_1, flag);
}

public void setAllowsImageEditing(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsImageEditing_1, flag);
}

public void setAllowsUndo(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsUndo_1, flag);
}

public void setAutomaticLinkDetectionEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAutomaticLinkDetectionEnabled_1, flag);
}

public void setAutomaticQuoteSubstitutionEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAutomaticQuoteSubstitutionEnabled_1, flag);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_1, color != null ? color.id : 0);
}

public void setBaseWritingDirection(int writingDirection, NSRange range) {
	OS.objc_msgSend(this.id, OS.sel_setBaseWritingDirection_1range_1, writingDirection, range);
}

public void setConstrainedFrameSize(NSSize desiredSize) {
	OS.objc_msgSend(this.id, OS.sel_setConstrainedFrameSize_1, desiredSize);
}

public void setContinuousSpellCheckingEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setContinuousSpellCheckingEnabled_1, flag);
}

public void setDefaultParagraphStyle(NSParagraphStyle paragraphStyle) {
	OS.objc_msgSend(this.id, OS.sel_setDefaultParagraphStyle_1, paragraphStyle != null ? paragraphStyle.id : 0);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

public void setDisplaysLinkToolTips(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDisplaysLinkToolTips_1, flag);
}

public void setDrawsBackground(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDrawsBackground_1, flag);
}

public void setEditable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEditable_1, flag);
}

public void setFieldEditor(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setFieldEditor_1, flag);
}

public void setGrammarCheckingEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setGrammarCheckingEnabled_1, flag);
}

public void setImportsGraphics(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setImportsGraphics_1, flag);
}

public void setInsertionPointColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setInsertionPointColor_1, color != null ? color.id : 0);
}

public void setLinkTextAttributes(NSDictionary attributeDictionary) {
	OS.objc_msgSend(this.id, OS.sel_setLinkTextAttributes_1, attributeDictionary != null ? attributeDictionary.id : 0);
}

public void setMarkedTextAttributes(NSDictionary attributeDictionary) {
	OS.objc_msgSend(this.id, OS.sel_setMarkedTextAttributes_1, attributeDictionary != null ? attributeDictionary.id : 0);
}

public void setNeedsDisplayInRect(NSRect rect, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setNeedsDisplayInRect_1avoidAdditionalLayout_1, rect, flag);
}

public void setRichText(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setRichText_1, flag);
}

public void setRulerVisible(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setRulerVisible_1, flag);
}

public void setSelectable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSelectable_1, flag);
}

public void setSelectedRange_(NSRange charRange) {
	OS.objc_msgSend(this.id, OS.sel_setSelectedRange_1, charRange);
}

public void setSelectedRange_affinity_stillSelecting_(NSRange charRange, int affinity, boolean stillSelectingFlag) {
	OS.objc_msgSend(this.id, OS.sel_setSelectedRange_1affinity_1stillSelecting_1, charRange, affinity, stillSelectingFlag);
}

public void setSelectedRanges_(NSArray ranges) {
	OS.objc_msgSend(this.id, OS.sel_setSelectedRanges_1, ranges != null ? ranges.id : 0);
}

public void setSelectedRanges_affinity_stillSelecting_(NSArray ranges, int affinity, boolean stillSelectingFlag) {
	OS.objc_msgSend(this.id, OS.sel_setSelectedRanges_1affinity_1stillSelecting_1, ranges != null ? ranges.id : 0, affinity, stillSelectingFlag);
}

public void setSelectedTextAttributes(NSDictionary attributeDictionary) {
	OS.objc_msgSend(this.id, OS.sel_setSelectedTextAttributes_1, attributeDictionary != null ? attributeDictionary.id : 0);
}

public void setSelectionGranularity(int granularity) {
	OS.objc_msgSend(this.id, OS.sel_setSelectionGranularity_1, granularity);
}

public void setSmartInsertDeleteEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSmartInsertDeleteEnabled_1, flag);
}

public void setSpellingState(int value, NSRange charRange) {
	OS.objc_msgSend(this.id, OS.sel_setSpellingState_1range_1, value, charRange);
}

public void setTextContainer(NSTextContainer container) {
	OS.objc_msgSend(this.id, OS.sel_setTextContainer_1, container != null ? container.id : 0);
}

public void setTextContainerInset(NSSize inset) {
	OS.objc_msgSend(this.id, OS.sel_setTextContainerInset_1, inset);
}

public void setTypingAttributes(NSDictionary attrs) {
	OS.objc_msgSend(this.id, OS.sel_setTypingAttributes_1, attrs != null ? attrs.id : 0);
}

public void setUsesFindPanel(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setUsesFindPanel_1, flag);
}

public void setUsesFontPanel(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setUsesFontPanel_1, flag);
}

public void setUsesRuler(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setUsesRuler_1, flag);
}

public boolean shouldChangeTextInRange(NSRange affectedCharRange, NSString replacementString) {
	return OS.objc_msgSend(this.id, OS.sel_shouldChangeTextInRange_1replacementString_1, affectedCharRange, replacementString != null ? replacementString.id : 0) != 0;
}

public boolean shouldChangeTextInRanges(NSArray affectedRanges, NSArray replacementStrings) {
	return OS.objc_msgSend(this.id, OS.sel_shouldChangeTextInRanges_1replacementStrings_1, affectedRanges != null ? affectedRanges.id : 0, replacementStrings != null ? replacementStrings.id : 0) != 0;
}

public boolean shouldDrawInsertionPoint() {
	return OS.objc_msgSend(this.id, OS.sel_shouldDrawInsertionPoint) != 0;
}

public void showFindIndicatorForRange(NSRange charRange) {
	OS.objc_msgSend(this.id, OS.sel_showFindIndicatorForRange_1, charRange);
}

public NSRange smartDeleteRangeForProposedRange(NSRange proposedCharRange) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_smartDeleteRangeForProposedRange_1, proposedCharRange);
	return result;
}

public NSString smartInsertAfterStringForString(NSString pasteString, NSRange charRangeToReplace) {
	int result = OS.objc_msgSend(this.id, OS.sel_smartInsertAfterStringForString_1replacingRange_1, pasteString != null ? pasteString.id : 0, charRangeToReplace);
	return result != 0 ? new NSString(result) : null;
}

public NSString smartInsertBeforeStringForString(NSString pasteString, NSRange charRangeToReplace) {
	int result = OS.objc_msgSend(this.id, OS.sel_smartInsertBeforeStringForString_1replacingRange_1, pasteString != null ? pasteString.id : 0, charRangeToReplace);
	return result != 0 ? new NSString(result) : null;
}

public boolean smartInsertDeleteEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_smartInsertDeleteEnabled) != 0;
}

public void smartInsertForString(NSString pasteString, NSRange charRangeToReplace, int beforeString, int afterString) {
	OS.objc_msgSend(this.id, OS.sel_smartInsertForString_1replacingRange_1beforeString_1afterString_1, pasteString != null ? pasteString.id : 0, charRangeToReplace, beforeString, afterString);
}

public int spellCheckerDocumentTag() {
	return OS.objc_msgSend(this.id, OS.sel_spellCheckerDocumentTag);
}

public void startSpeaking(id sender) {
	OS.objc_msgSend(this.id, OS.sel_startSpeaking_1, sender != null ? sender.id : 0);
}

public void stopSpeaking(id sender) {
	OS.objc_msgSend(this.id, OS.sel_stopSpeaking_1, sender != null ? sender.id : 0);
}

public NSTextContainer textContainer() {
	int result = OS.objc_msgSend(this.id, OS.sel_textContainer);
	return result != 0 ? new NSTextContainer(result) : null;
}

public NSSize textContainerInset() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_textContainerInset);
	return result;
}

public NSPoint textContainerOrigin() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_textContainerOrigin);
	return result;
}

public NSTextStorage textStorage() {
	int result = OS.objc_msgSend(this.id, OS.sel_textStorage);
	return result != 0 ? new NSTextStorage(result) : null;
}

public void tightenKerning(id sender) {
	OS.objc_msgSend(this.id, OS.sel_tightenKerning_1, sender != null ? sender.id : 0);
}

public void toggleAutomaticLinkDetection(id sender) {
	OS.objc_msgSend(this.id, OS.sel_toggleAutomaticLinkDetection_1, sender != null ? sender.id : 0);
}

public void toggleAutomaticQuoteSubstitution(id sender) {
	OS.objc_msgSend(this.id, OS.sel_toggleAutomaticQuoteSubstitution_1, sender != null ? sender.id : 0);
}

public void toggleBaseWritingDirection(id sender) {
	OS.objc_msgSend(this.id, OS.sel_toggleBaseWritingDirection_1, sender != null ? sender.id : 0);
}

public void toggleContinuousSpellChecking(id sender) {
	OS.objc_msgSend(this.id, OS.sel_toggleContinuousSpellChecking_1, sender != null ? sender.id : 0);
}

public void toggleGrammarChecking(id sender) {
	OS.objc_msgSend(this.id, OS.sel_toggleGrammarChecking_1, sender != null ? sender.id : 0);
}

public void toggleSmartInsertDelete(id sender) {
	OS.objc_msgSend(this.id, OS.sel_toggleSmartInsertDelete_1, sender != null ? sender.id : 0);
}

public void toggleTraditionalCharacterShape(id sender) {
	OS.objc_msgSend(this.id, OS.sel_toggleTraditionalCharacterShape_1, sender != null ? sender.id : 0);
}

public void turnOffKerning(id sender) {
	OS.objc_msgSend(this.id, OS.sel_turnOffKerning_1, sender != null ? sender.id : 0);
}

public void turnOffLigatures(id sender) {
	OS.objc_msgSend(this.id, OS.sel_turnOffLigatures_1, sender != null ? sender.id : 0);
}

public NSDictionary typingAttributes() {
	int result = OS.objc_msgSend(this.id, OS.sel_typingAttributes);
	return result != 0 ? new NSDictionary(result) : null;
}

public void updateDragTypeRegistration() {
	OS.objc_msgSend(this.id, OS.sel_updateDragTypeRegistration);
}

public void updateFontPanel() {
	OS.objc_msgSend(this.id, OS.sel_updateFontPanel);
}

public void updateInsertionPointStateAndRestartTimer(boolean restartFlag) {
	OS.objc_msgSend(this.id, OS.sel_updateInsertionPointStateAndRestartTimer_1, restartFlag);
}

public void updateRuler() {
	OS.objc_msgSend(this.id, OS.sel_updateRuler);
}

public void useAllLigatures(id sender) {
	OS.objc_msgSend(this.id, OS.sel_useAllLigatures_1, sender != null ? sender.id : 0);
}

public void useStandardKerning(id sender) {
	OS.objc_msgSend(this.id, OS.sel_useStandardKerning_1, sender != null ? sender.id : 0);
}

public void useStandardLigatures(id sender) {
	OS.objc_msgSend(this.id, OS.sel_useStandardLigatures_1, sender != null ? sender.id : 0);
}

public boolean usesFindPanel() {
	return OS.objc_msgSend(this.id, OS.sel_usesFindPanel) != 0;
}

public boolean usesFontPanel() {
	return OS.objc_msgSend(this.id, OS.sel_usesFontPanel) != 0;
}

public boolean usesRuler() {
	return OS.objc_msgSend(this.id, OS.sel_usesRuler) != 0;
}

public id validRequestorForSendType(NSString sendType, NSString returnType) {
	int result = OS.objc_msgSend(this.id, OS.sel_validRequestorForSendType_1returnType_1, sendType != null ? sendType.id : 0, returnType != null ? returnType.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSArray writablePasteboardTypes() {
	int result = OS.objc_msgSend(this.id, OS.sel_writablePasteboardTypes);
	return result != 0 ? new NSArray(result) : null;
}

public boolean writeSelectionToPasteboard_type_(NSPasteboard pboard, NSString type) {
	return OS.objc_msgSend(this.id, OS.sel_writeSelectionToPasteboard_1type_1, pboard != null ? pboard.id : 0, type != null ? type.id : 0) != 0;
}

public boolean writeSelectionToPasteboard_types_(NSPasteboard pboard, NSArray types) {
	return OS.objc_msgSend(this.id, OS.sel_writeSelectionToPasteboard_1types_1, pboard != null ? pboard.id : 0, types != null ? types.id : 0) != 0;
}

}
