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

public class NSCell extends NSObject {

public NSCell() {
	super();
}

public NSCell(int id) {
	super(id);
}

public boolean acceptsFirstResponder() {
	return OS.objc_msgSend(this.id, OS.sel_acceptsFirstResponder) != 0;
}

public int action() {
	return OS.objc_msgSend(this.id, OS.sel_action);
}

public int alignment() {
	return OS.objc_msgSend(this.id, OS.sel_alignment);
}

public boolean allowsEditingTextAttributes() {
	return OS.objc_msgSend(this.id, OS.sel_allowsEditingTextAttributes) != 0;
}

public boolean allowsMixedState() {
	return OS.objc_msgSend(this.id, OS.sel_allowsMixedState) != 0;
}

public boolean allowsUndo() {
	return OS.objc_msgSend(this.id, OS.sel_allowsUndo) != 0;
}

public NSAttributedString attributedStringValue() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributedStringValue);
	return result != 0 ? new NSAttributedString(result) : null;
}

public int backgroundStyle() {
	return OS.objc_msgSend(this.id, OS.sel_backgroundStyle);
}

public int baseWritingDirection() {
	return OS.objc_msgSend(this.id, OS.sel_baseWritingDirection);
}

public void calcDrawInfo(NSRect aRect) {
	OS.objc_msgSend(this.id, OS.sel_calcDrawInfo_1, aRect);
}

public int cellAttribute(int aParameter) {
	return OS.objc_msgSend(this.id, OS.sel_cellAttribute_1, aParameter);
}

public NSSize cellSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_struct(result, this.id, OS.sel_cellSize);
	return result;
}

public NSSize cellSizeForBounds(NSRect aRect) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_cellSizeForBounds_1, aRect);
	return result;
}

public int compare(id otherCell) {
	return OS.objc_msgSend(this.id, OS.sel_compare_1, otherCell != null ? otherCell.id : 0);
}

public boolean continueTracking(NSPoint lastPoint, NSPoint currentPoint, NSView controlView) {
	return OS.objc_msgSend(this.id, OS.sel_continueTracking_1at_1inView_1, lastPoint, currentPoint, controlView != null ? controlView.id : 0) != 0;
}

public int controlSize() {
	return OS.objc_msgSend(this.id, OS.sel_controlSize);
}

public int controlTint() {
	return OS.objc_msgSend(this.id, OS.sel_controlTint);
}

public NSView controlView() {
	int result = OS.objc_msgSend(this.id, OS.sel_controlView);
	return result != 0 ? new NSView(result) : null;
}

public static int defaultFocusRingType() {
	return OS.objc_msgSend(OS.class_NSCell, OS.sel_defaultFocusRingType);
}

public static NSMenu defaultMenu() {
	int result = OS.objc_msgSend(OS.class_NSCell, OS.sel_defaultMenu);
	return result != 0 ? new NSMenu(result) : null;
}

public double doubleValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_doubleValue);
}

public void drawInteriorWithFrame(NSRect cellFrame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawInteriorWithFrame_1inView_1, cellFrame, controlView != null ? controlView.id : 0);
}

public void drawWithExpansionFrame(NSRect cellFrame, NSView view) {
	OS.objc_msgSend(this.id, OS.sel_drawWithExpansionFrame_1inView_1, cellFrame, view != null ? view.id : 0);
}

public void drawWithFrame(NSRect cellFrame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawWithFrame_1inView_1, cellFrame, controlView != null ? controlView.id : 0);
}

public NSRect drawingRectForBounds(NSRect theRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_drawingRectForBounds_1, theRect);
	return result;
}

public void editWithFrame(NSRect aRect, NSView controlView, NSText textObj, id anObject, NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_editWithFrame_1inView_1editor_1delegate_1event_1, aRect, controlView != null ? controlView.id : 0, textObj != null ? textObj.id : 0, anObject != null ? anObject.id : 0, theEvent != null ? theEvent.id : 0);
}

public void endEditing(NSText textObj) {
	OS.objc_msgSend(this.id, OS.sel_endEditing_1, textObj != null ? textObj.id : 0);
}

public int entryType() {
	return OS.objc_msgSend(this.id, OS.sel_entryType);
}

public NSRect expansionFrameWithFrame(NSRect cellFrame, NSView view) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_expansionFrameWithFrame_1inView_1, cellFrame, view != null ? view.id : 0);
	return result;
}

public float floatValue() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_floatValue);
}

public int focusRingType() {
	return OS.objc_msgSend(this.id, OS.sel_focusRingType);
}

public NSFont font() {
	int result = OS.objc_msgSend(this.id, OS.sel_font);
	return result != 0 ? new NSFont(result) : null;
}

public id formatter() {
	int result = OS.objc_msgSend(this.id, OS.sel_formatter);
	return result != 0 ? new id(result) : null;
}

public void getPeriodicDelay(int delay, int interval) {
	OS.objc_msgSend(this.id, OS.sel_getPeriodicDelay_1interval_1, delay, interval);
}

public boolean hasValidObjectValue() {
	return OS.objc_msgSend(this.id, OS.sel_hasValidObjectValue) != 0;
}

public void highlight(boolean flag, NSRect cellFrame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_highlight_1withFrame_1inView_1, flag, cellFrame, controlView != null ? controlView.id : 0);
}

public NSColor highlightColorWithFrame(NSRect cellFrame, NSView controlView) {
	int result = OS.objc_msgSend(this.id, OS.sel_highlightColorWithFrame_1inView_1, cellFrame, controlView != null ? controlView.id : 0);
	return result != 0 ? new NSColor(result) : null;
}

public int hitTestForEvent(NSEvent event, NSRect cellFrame, NSView controlView) {
	return OS.objc_msgSend(this.id, OS.sel_hitTestForEvent_1inRect_1ofView_1, event != null ? event.id : 0, cellFrame, controlView != null ? controlView.id : 0);
}

public NSImage image() {
	int result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public NSRect imageRectForBounds(NSRect theRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_imageRectForBounds_1, theRect);
	return result;
}

public boolean importsGraphics() {
	return OS.objc_msgSend(this.id, OS.sel_importsGraphics) != 0;
}

public NSCell initImageCell(NSImage image) {
	int result = OS.objc_msgSend(this.id, OS.sel_initImageCell_1, image != null ? image.id : 0);
	return result != 0 ? this : null;
}

public NSCell initTextCell(NSString aString) {
	int result = OS.objc_msgSend(this.id, OS.sel_initTextCell_1, aString != null ? aString.id : 0);
	return result != 0 ? this : null;
}

public int intValue() {
	return OS.objc_msgSend(this.id, OS.sel_intValue);
}

public int integerValue() {
	return OS.objc_msgSend(this.id, OS.sel_integerValue);
}

public int interiorBackgroundStyle() {
	return OS.objc_msgSend(this.id, OS.sel_interiorBackgroundStyle);
}

public boolean isBezeled() {
	return OS.objc_msgSend(this.id, OS.sel_isBezeled) != 0;
}

public boolean isBordered() {
	return OS.objc_msgSend(this.id, OS.sel_isBordered) != 0;
}

public boolean isContinuous() {
	return OS.objc_msgSend(this.id, OS.sel_isContinuous) != 0;
}

public boolean isEditable() {
	return OS.objc_msgSend(this.id, OS.sel_isEditable) != 0;
}

public boolean isEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_isEnabled) != 0;
}

public boolean isEntryAcceptable(NSString aString) {
	return OS.objc_msgSend(this.id, OS.sel_isEntryAcceptable_1, aString != null ? aString.id : 0) != 0;
}

public boolean isHighlighted() {
	return OS.objc_msgSend(this.id, OS.sel_isHighlighted) != 0;
}

public boolean isOpaque() {
	return OS.objc_msgSend(this.id, OS.sel_isOpaque) != 0;
}

public boolean isScrollable() {
	return OS.objc_msgSend(this.id, OS.sel_isScrollable) != 0;
}

public boolean isSelectable() {
	return OS.objc_msgSend(this.id, OS.sel_isSelectable) != 0;
}

public NSString keyEquivalent() {
	int result = OS.objc_msgSend(this.id, OS.sel_keyEquivalent);
	return result != 0 ? new NSString(result) : null;
}

public int lineBreakMode() {
	return OS.objc_msgSend(this.id, OS.sel_lineBreakMode);
}

public NSMenu menu() {
	int result = OS.objc_msgSend(this.id, OS.sel_menu);
	return result != 0 ? new NSMenu(result) : null;
}

public NSMenu menuForEvent(NSEvent event, NSRect cellFrame, NSView view) {
	int result = OS.objc_msgSend(this.id, OS.sel_menuForEvent_1inRect_1ofView_1, event != null ? event.id : 0, cellFrame, view != null ? view.id : 0);
	return result != 0 ? new NSMenu(result) : null;
}

public NSString mnemonic() {
	int result = OS.objc_msgSend(this.id, OS.sel_mnemonic);
	return result != 0 ? new NSString(result) : null;
}

public int mnemonicLocation() {
	return OS.objc_msgSend(this.id, OS.sel_mnemonicLocation);
}

public int mouseDownFlags() {
	return OS.objc_msgSend(this.id, OS.sel_mouseDownFlags);
}

public int nextState() {
	return OS.objc_msgSend(this.id, OS.sel_nextState);
}

public id objectValue() {
	int result = OS.objc_msgSend(this.id, OS.sel_objectValue);
	return result != 0 ? new id(result) : null;
}

public void performClick(id sender) {
	OS.objc_msgSend(this.id, OS.sel_performClick_1, sender != null ? sender.id : 0);
}

public static boolean prefersTrackingUntilMouseUp() {
	return OS.objc_msgSend(OS.class_NSCell, OS.sel_prefersTrackingUntilMouseUp) != 0;
}

public boolean refusesFirstResponder() {
	return OS.objc_msgSend(this.id, OS.sel_refusesFirstResponder) != 0;
}

public id representedObject() {
	int result = OS.objc_msgSend(this.id, OS.sel_representedObject);
	return result != 0 ? new id(result) : null;
}

public void resetCursorRect(NSRect cellFrame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_resetCursorRect_1inView_1, cellFrame, controlView != null ? controlView.id : 0);
}

public void selectWithFrame(NSRect aRect, NSView controlView, NSText textObj, id anObject, int selStart, int selLength) {
	OS.objc_msgSend(this.id, OS.sel_selectWithFrame_1inView_1editor_1delegate_1start_1length_1, aRect, controlView != null ? controlView.id : 0, textObj != null ? textObj.id : 0, anObject != null ? anObject.id : 0, selStart, selLength);
}

public int sendActionOn(int mask) {
	return OS.objc_msgSend(this.id, OS.sel_sendActionOn_1, mask);
}

public boolean sendsActionOnEndEditing() {
	return OS.objc_msgSend(this.id, OS.sel_sendsActionOnEndEditing) != 0;
}

public void setAction(int aSelector) {
	OS.objc_msgSend(this.id, OS.sel_setAction_1, aSelector);
}

public void setAlignment(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setAlignment_1, mode);
}

public void setAllowsEditingTextAttributes(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsEditingTextAttributes_1, flag);
}

public void setAllowsMixedState(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsMixedState_1, flag);
}

public void setAllowsUndo(boolean allowsUndo) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsUndo_1, allowsUndo);
}

public void setAttributedStringValue(NSAttributedString obj) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedStringValue_1, obj != null ? obj.id : 0);
}

public void setBackgroundStyle(int style) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundStyle_1, style);
}

public void setBaseWritingDirection(int writingDirection) {
	OS.objc_msgSend(this.id, OS.sel_setBaseWritingDirection_1, writingDirection);
}

public void setBezeled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBezeled_1, flag);
}

public void setBordered(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBordered_1, flag);
}

public void setCellAttribute(int aParameter, int value) {
	OS.objc_msgSend(this.id, OS.sel_setCellAttribute_1to_1, aParameter, value);
}

public void setContinuous(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setContinuous_1, flag);
}

public void setControlSize(int size) {
	OS.objc_msgSend(this.id, OS.sel_setControlSize_1, size);
}

public void setControlTint(int controlTint) {
	OS.objc_msgSend(this.id, OS.sel_setControlTint_1, controlTint);
}

public void setControlView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setControlView_1, view != null ? view.id : 0);
}

public void setDoubleValue(double aDouble) {
	OS.objc_msgSend(this.id, OS.sel_setDoubleValue_1, aDouble);
}

public void setEditable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEditable_1, flag);
}

public void setEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_1, flag);
}

public void setEntryType(int aType) {
	OS.objc_msgSend(this.id, OS.sel_setEntryType_1, aType);
}

public void setFloatValue(float aFloat) {
	OS.objc_msgSend(this.id, OS.sel_setFloatValue_1, aFloat);
}

public void setFloatingPointFormat(boolean autoRange, int leftDigits, int rightDigits) {
	OS.objc_msgSend(this.id, OS.sel_setFloatingPointFormat_1left_1right_1, autoRange, leftDigits, rightDigits);
}

public void setFocusRingType(int focusRingType) {
	OS.objc_msgSend(this.id, OS.sel_setFocusRingType_1, focusRingType);
}

public void setFont(NSFont fontObj) {
	OS.objc_msgSend(this.id, OS.sel_setFont_1, fontObj != null ? fontObj.id : 0);
}

public void setFormatter(NSFormatter newFormatter) {
	OS.objc_msgSend(this.id, OS.sel_setFormatter_1, newFormatter != null ? newFormatter.id : 0);
}

public void setHighlighted(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHighlighted_1, flag);
}

public void setImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setImage_1, image != null ? image.id : 0);
}

public void setImportsGraphics(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setImportsGraphics_1, flag);
}

public void setIntValue(int anInt) {
	OS.objc_msgSend(this.id, OS.sel_setIntValue_1, anInt);
}

public void setIntegerValue(int anInteger) {
	OS.objc_msgSend(this.id, OS.sel_setIntegerValue_1, anInteger);
}

public void setLineBreakMode(int mode) {
	OS.objc_msgSend(this.id, OS.sel_setLineBreakMode_1, mode);
}

public void setMenu(NSMenu aMenu) {
	OS.objc_msgSend(this.id, OS.sel_setMenu_1, aMenu != null ? aMenu.id : 0);
}

public void setMnemonicLocation(int location) {
	OS.objc_msgSend(this.id, OS.sel_setMnemonicLocation_1, location);
}

public void setNextState() {
	OS.objc_msgSend(this.id, OS.sel_setNextState);
}

public void setObjectValue(id  obj) {
	OS.objc_msgSend(this.id, OS.sel_setObjectValue_1, obj != null ? obj.id : 0);
}

public void setRefusesFirstResponder(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setRefusesFirstResponder_1, flag);
}

public void setRepresentedObject(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setRepresentedObject_1, anObject != null ? anObject.id : 0);
}

public void setScrollable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setScrollable_1, flag);
}

public void setSelectable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSelectable_1, flag);
}

public void setSendsActionOnEndEditing(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSendsActionOnEndEditing_1, flag);
}

public void setShowsFirstResponder(boolean showFR) {
	OS.objc_msgSend(this.id, OS.sel_setShowsFirstResponder_1, showFR);
}

public void setState(int value) {
	OS.objc_msgSend(this.id, OS.sel_setState_1, value);
}

public void setStringValue(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setStringValue_1, aString != null ? aString.id : 0);
}

public void setTag(int anInt) {
	OS.objc_msgSend(this.id, OS.sel_setTag_1, anInt);
}

public void setTarget(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setTarget_1, anObject != null ? anObject.id : 0);
}

public void setTitle(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_1, aString != null ? aString.id : 0);
}

public void setTitleWithMnemonic(NSString stringWithAmpersand) {
	OS.objc_msgSend(this.id, OS.sel_setTitleWithMnemonic_1, stringWithAmpersand != null ? stringWithAmpersand.id : 0);
}

public void setTruncatesLastVisibleLine(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setTruncatesLastVisibleLine_1, flag);
}

public void setType(int aType) {
	OS.objc_msgSend(this.id, OS.sel_setType_1, aType);
}

public NSText setUpFieldEditorAttributes(NSText textObj) {
	int result = OS.objc_msgSend(this.id, OS.sel_setUpFieldEditorAttributes_1, textObj != null ? textObj.id : 0);
	return result != 0 ? new NSText(result) : null;
}

public void setWraps(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setWraps_1, flag);
}

public boolean showsFirstResponder() {
	return OS.objc_msgSend(this.id, OS.sel_showsFirstResponder) != 0;
}

public boolean startTrackingAt(NSPoint startPoint, NSView controlView) {
	return OS.objc_msgSend(this.id, OS.sel_startTrackingAt_1inView_1, startPoint, controlView != null ? controlView.id : 0) != 0;
}

public int state() {
	return OS.objc_msgSend(this.id, OS.sel_state);
}

public void stopTracking(NSPoint lastPoint, NSPoint stopPoint, NSView controlView, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_stopTracking_1at_1inView_1mouseIsUp_1, lastPoint, stopPoint, controlView != null ? controlView.id : 0, flag);
}

public NSString stringValue() {
	int result = OS.objc_msgSend(this.id, OS.sel_stringValue);
	return result != 0 ? new NSString(result) : null;
}

public int tag() {
	return OS.objc_msgSend(this.id, OS.sel_tag);
}

public void takeDoubleValueFrom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_takeDoubleValueFrom_1, sender != null ? sender.id : 0);
}

public void takeFloatValueFrom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_takeFloatValueFrom_1, sender != null ? sender.id : 0);
}

public void takeIntValueFrom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_takeIntValueFrom_1, sender != null ? sender.id : 0);
}

public void takeIntegerValueFrom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_takeIntegerValueFrom_1, sender != null ? sender.id : 0);
}

public void takeObjectValueFrom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_takeObjectValueFrom_1, sender != null ? sender.id : 0);
}

public void takeStringValueFrom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_takeStringValueFrom_1, sender != null ? sender.id : 0);
}

public id target() {
	int result = OS.objc_msgSend(this.id, OS.sel_target);
	return result != 0 ? new id(result) : null;
}

public NSString title() {
	int result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

public NSRect titleRectForBounds(NSRect theRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_titleRectForBounds_1, theRect);
	return result;
}

public boolean trackMouse(NSEvent theEvent, NSRect cellFrame, NSView controlView, boolean flag) {
	return OS.objc_msgSend(this.id, OS.sel_trackMouse_1inRect_1ofView_1untilMouseUp_1, theEvent != null ? theEvent.id : 0, cellFrame, controlView != null ? controlView.id : 0, flag) != 0;
}

public boolean truncatesLastVisibleLine() {
	return OS.objc_msgSend(this.id, OS.sel_truncatesLastVisibleLine) != 0;
}

public int type() {
	return OS.objc_msgSend(this.id, OS.sel_type);
}

public boolean wantsNotificationForMarkedText() {
	return OS.objc_msgSend(this.id, OS.sel_wantsNotificationForMarkedText) != 0;
}

public boolean wraps() {
	return OS.objc_msgSend(this.id, OS.sel_wraps) != 0;
}

}
