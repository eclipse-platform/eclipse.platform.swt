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

public class NSCell extends NSObject {

public NSCell() {
	super();
}

public NSCell(long id) {
	super(id);
}

public NSCell(id id) {
	super(id);
}

public NSAttributedString attributedStringValue() {
	long result = OS.objc_msgSend(this.id, OS.sel_attributedStringValue);
	return result != 0 ? new NSAttributedString(result) : null;
}

public NSSize cellSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_cellSize);
	return result;
}

public NSSize cellSizeForBounds(NSRect aRect) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_cellSizeForBounds_, aRect);
	return result;
}

public long controlSize() {
	return OS.objc_msgSend(this.id, OS.sel_controlSize);
}

public void drawInteriorWithFrame(NSRect cellFrame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawInteriorWithFrame_inView_, cellFrame, controlView != null ? controlView.id : 0);
}

public void drawWithExpansionFrame(NSRect cellFrame, NSView view) {
	OS.objc_msgSend(this.id, OS.sel_drawWithExpansionFrame_inView_, cellFrame, view != null ? view.id : 0);
}

public NSRect drawingRectForBounds(NSRect theRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_drawingRectForBounds_, theRect);
	return result;
}

public NSRect expansionFrameWithFrame(NSRect cellFrame, NSView view) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_expansionFrameWithFrame_inView_, cellFrame, view != null ? view.id : 0);
	return result;
}

public NSRect focusRingMaskBoundsForFrame(NSRect cellFrame, NSView controlView) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_focusRingMaskBoundsForFrame_inView_, cellFrame, controlView != null ? controlView.id : 0);
	return result;
}

public NSFont font() {
	long result = OS.objc_msgSend(this.id, OS.sel_font);
	return result != 0 ? new NSFont(result) : null;
}

public long hitTestForEvent(NSEvent event, NSRect cellFrame, NSView controlView) {
	return OS.objc_msgSend(this.id, OS.sel_hitTestForEvent_inRect_ofView_, event != null ? event.id : 0, cellFrame, controlView != null ? controlView.id : 0);
}

public NSImage image() {
	long result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public NSRect imageRectForBounds(NSRect theRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_imageRectForBounds_, theRect);
	return result;
}

public boolean isEnabled() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isEnabled);
}

public boolean isHighlighted() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isHighlighted);
}

public long nextState() {
	return OS.objc_msgSend(this.id, OS.sel_nextState);
}

public void setAlignment(long alignment) {
	OS.objc_msgSend(this.id, OS.sel_setAlignment_, alignment);
}

public void setAllowsMixedState(boolean allowsMixedState) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsMixedState_, allowsMixedState);
}

public void setAttributedStringValue(NSAttributedString attributedStringValue) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedStringValue_, attributedStringValue != null ? attributedStringValue.id : 0);
}

public void setBackgroundStyle(long backgroundStyle) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundStyle_, backgroundStyle);
}

public void setBaseWritingDirection(long baseWritingDirection) {
	OS.objc_msgSend(this.id, OS.sel_setBaseWritingDirection_, baseWritingDirection);
}

public void setControlSize(long controlSize) {
	OS.objc_msgSend(this.id, OS.sel_setControlSize_, controlSize);
}

public void setEnabled(boolean enabled) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_, enabled);
}

public void setFont(NSFont font) {
	OS.objc_msgSend(this.id, OS.sel_setFont_, font != null ? font.id : 0);
}

public void setFormatter(NSFormatter formatter) {
	OS.objc_msgSend(this.id, OS.sel_setFormatter_, formatter != null ? formatter.id : 0);
}

public void setHighlighted(boolean highlighted) {
	OS.objc_msgSend(this.id, OS.sel_setHighlighted_, highlighted);
}

public void setImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setImage_, image != null ? image.id : 0);
}

public void setLineBreakMode(long lineBreakMode) {
	OS.objc_msgSend(this.id, OS.sel_setLineBreakMode_, lineBreakMode);
}

public void setObjectValue(id objectValue) {
	OS.objc_msgSend(this.id, OS.sel_setObjectValue_, objectValue != null ? objectValue.id : 0);
}

public void setScrollable(boolean scrollable) {
	OS.objc_msgSend(this.id, OS.sel_setScrollable_, scrollable);
}

public void setTitle(NSString title) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_, title != null ? title.id : 0);
}

public void setUsesSingleLineMode(boolean usesSingleLineMode) {
	OS.objc_msgSend(this.id, OS.sel_setUsesSingleLineMode_, usesSingleLineMode);
}

public void setWraps(boolean wraps) {
	OS.objc_msgSend(this.id, OS.sel_setWraps_, wraps);
}

public NSString title() {
	long result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

public NSRect titleRectForBounds(NSRect theRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_titleRectForBounds_, theRect);
	return result;
}

}
