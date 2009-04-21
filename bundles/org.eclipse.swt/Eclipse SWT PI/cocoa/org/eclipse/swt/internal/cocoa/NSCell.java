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

public class NSCell extends NSObject {

public NSCell() {
	super();
}

public NSCell(int /*long*/ id) {
	super(id);
}

public NSCell(id id) {
	super(id);
}

public NSAttributedString attributedStringValue() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_attributedStringValue);
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

public void drawInteriorWithFrame(NSRect cellFrame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawInteriorWithFrame_inView_, cellFrame, controlView != null ? controlView.id : 0);
}

public void drawWithFrame(NSRect cellFrame, NSView controlView) {
	OS.objc_msgSend(this.id, OS.sel_drawWithFrame_inView_, cellFrame, controlView != null ? controlView.id : 0);
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

public NSFont font() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_font);
	return result != 0 ? new NSFont(result) : null;
}

public NSColor highlightColorWithFrame(NSRect cellFrame, NSView controlView) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_highlightColorWithFrame_inView_, cellFrame, controlView != null ? controlView.id : 0);
	return result != 0 ? new NSColor(result) : null;
}

public int /*long*/ hitTestForEvent(NSEvent event, NSRect cellFrame, NSView controlView) {
	return OS.objc_msgSend(this.id, OS.sel_hitTestForEvent_inRect_ofView_, event != null ? event.id : 0, cellFrame, controlView != null ? controlView.id : 0);
}

public NSImage image() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_image);
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

public int /*long*/ nextState() {
	return OS.objc_msgSend(this.id, OS.sel_nextState);
}

public void setAlignment(int /*long*/ mode) {
	OS.objc_msgSend(this.id, OS.sel_setAlignment_, mode);
}

public void setAllowsMixedState(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsMixedState_, flag);
}

public void setAttributedStringValue(NSAttributedString obj) {
	OS.objc_msgSend(this.id, OS.sel_setAttributedStringValue_, obj != null ? obj.id : 0);
}

public void setControlSize(int /*long*/ size) {
	OS.objc_msgSend(this.id, OS.sel_setControlSize_, size);
}

public void setEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_, flag);
}

public void setFont(NSFont fontObj) {
	OS.objc_msgSend(this.id, OS.sel_setFont_, fontObj != null ? fontObj.id : 0);
}

public void setFormatter(NSFormatter newFormatter) {
	OS.objc_msgSend(this.id, OS.sel_setFormatter_, newFormatter != null ? newFormatter.id : 0);
}

public void setHighlighted(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHighlighted_, flag);
}

public void setImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setImage_, image != null ? image.id : 0);
}

public void setLineBreakMode(int /*long*/ mode) {
	OS.objc_msgSend(this.id, OS.sel_setLineBreakMode_, mode);
}

public void setObjectValue(id obj) {
	OS.objc_msgSend(this.id, OS.sel_setObjectValue_, obj != null ? obj.id : 0);
}

public void setScrollable(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setScrollable_, flag);
}

public void setTitle(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_, aString != null ? aString.id : 0);
}

public void setWraps(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setWraps_, flag);
}

public NSString title() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

public NSRect titleRectForBounds(NSRect theRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_titleRectForBounds_, theRect);
	return result;
}

public boolean wraps() {
	return OS.objc_msgSend_bool(this.id, OS.sel_wraps);
}

}
