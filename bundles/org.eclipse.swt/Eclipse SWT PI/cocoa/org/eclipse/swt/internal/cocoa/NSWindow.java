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

public class NSWindow extends NSResponder {

public NSWindow() {
	super();
}

public NSWindow(int /*long*/ id) {
	super(id);
}

public NSWindow(id id) {
	super(id);
}

public void addChildWindow(NSWindow childWin, int /*long*/ place) {
	OS.objc_msgSend(this.id, OS.sel_addChildWindow_ordered_, childWin != null ? childWin.id : 0, place);
}

public float /*double*/ alphaValue() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_alphaValue);
}

public boolean canBecomeKeyWindow() {
	return OS.objc_msgSend_bool(this.id, OS.sel_canBecomeKeyWindow);
}

public NSPoint cascadeTopLeftFromPoint(NSPoint topLeftPoint) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_cascadeTopLeftFromPoint_, topLeftPoint);
	return result;
}

public void close() {
	OS.objc_msgSend(this.id, OS.sel_close);
}

public NSRect contentRectForFrameRect(NSRect frameRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_contentRectForFrameRect_, frameRect);
	return result;
}

public NSView contentView() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_contentView);
	return result != 0 ? new NSView(result) : null;
}

public NSPoint convertBaseToScreen(NSPoint aPoint) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_convertBaseToScreen_, aPoint);
	return result;
}

public NSPoint convertScreenToBase(NSPoint aPoint) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_convertScreenToBase_, aPoint);
	return result;
}

public void deminiaturize(id sender) {
	OS.objc_msgSend(this.id, OS.sel_deminiaturize_, sender != null ? sender.id : 0);
}

public void disableCursorRects() {
	OS.objc_msgSend(this.id, OS.sel_disableCursorRects);
}

public NSText fieldEditor(boolean createFlag, id anObject) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_fieldEditor_forObject_, createFlag, anObject != null ? anObject.id : 0);
	return result != 0 ? new NSText(result) : null;
}

public NSResponder firstResponder() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_firstResponder);
	return result != 0 ? new NSResponder(result) : null;
}

public NSRect frame() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_frame);
	return result;
}

public NSRect frameRectForContentRect(NSRect contentRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_frameRectForContentRect_, contentRect);
	return result;
}

public NSGraphicsContext graphicsContext() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_graphicsContext);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public NSWindow initWithContentRect(NSRect contentRect, int /*long*/ aStyle, int /*long*/ bufferingType, boolean flag) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithContentRect_styleMask_backing_defer_, contentRect, aStyle, bufferingType, flag);
	return result == this.id ? this : (result != 0 ? new NSWindow(result) : null);
}

public NSWindow initWithContentRect(NSRect contentRect, int /*long*/ aStyle, int /*long*/ bufferingType, boolean flag, NSScreen screen) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithContentRect_styleMask_backing_defer_screen_, contentRect, aStyle, bufferingType, flag, screen != null ? screen.id : 0);
	return result == this.id ? this : (result != 0 ? new NSWindow(result) : null);
}

public boolean isKeyWindow() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isKeyWindow);
}

public boolean isMiniaturized() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isMiniaturized);
}

public boolean isVisible() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isVisible);
}

public boolean isZoomed() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isZoomed);
}

public boolean makeFirstResponder(NSResponder aResponder) {
	return OS.objc_msgSend_bool(this.id, OS.sel_makeFirstResponder_, aResponder != null ? aResponder.id : 0);
}

public void makeKeyAndOrderFront(id sender) {
	OS.objc_msgSend(this.id, OS.sel_makeKeyAndOrderFront_, sender != null ? sender.id : 0);
}

public void miniaturize(id sender) {
	OS.objc_msgSend(this.id, OS.sel_miniaturize_, sender != null ? sender.id : 0);
}

public NSPoint mouseLocationOutsideOfEventStream() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_mouseLocationOutsideOfEventStream);
	return result;
}

public void orderFront(id sender) {
	OS.objc_msgSend(this.id, OS.sel_orderFront_, sender != null ? sender.id : 0);
}

public void orderFrontRegardless() {
	OS.objc_msgSend(this.id, OS.sel_orderFrontRegardless);
}

public void orderOut(id sender) {
	OS.objc_msgSend(this.id, OS.sel_orderOut_, sender != null ? sender.id : 0);
}

public NSWindow parentWindow() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_parentWindow);
	return result == this.id ? this : (result != 0 ? new NSWindow(result) : null);
}

public void removeChildWindow(NSWindow childWin) {
	OS.objc_msgSend(this.id, OS.sel_removeChildWindow_, childWin != null ? childWin.id : 0);
}

public NSScreen screen() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_screen);
	return result != 0 ? new NSScreen(result) : null;
}

public void setAcceptsMouseMovedEvents(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAcceptsMouseMovedEvents_, flag);
}

public void setAlphaValue(float /*double*/ windowAlpha) {
	OS.objc_msgSend(this.id, OS.sel_setAlphaValue_, windowAlpha);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_, color != null ? color.id : 0);
}

public void setContentView(NSView aView) {
	OS.objc_msgSend(this.id, OS.sel_setContentView_, aView != null ? aView.id : 0);
}

public void setDefaultButtonCell(NSButtonCell defButt) {
	OS.objc_msgSend(this.id, OS.sel_setDefaultButtonCell_, defButt != null ? defButt.id : 0);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_, anObject != null ? anObject.id : 0);
}

public void setFrame(NSRect frameRect, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setFrame_display_, frameRect, flag);
}

public void setHasShadow(boolean hasShadow) {
	OS.objc_msgSend(this.id, OS.sel_setHasShadow_, hasShadow);
}

public void setLevel(int /*long*/ newLevel) {
	OS.objc_msgSend(this.id, OS.sel_setLevel_, newLevel);
}

public void setOpaque(boolean isOpaque) {
	OS.objc_msgSend(this.id, OS.sel_setOpaque_, isOpaque);
}

public void setReleasedWhenClosed(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setReleasedWhenClosed_, flag);
}

public void setShowsToolbarButton(boolean show) {
	OS.objc_msgSend(this.id, OS.sel_setShowsToolbarButton_, show);
}

public void setTitle(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_, aString != null ? aString.id : 0);
}

public void setToolbar(NSToolbar toolbar) {
	OS.objc_msgSend(this.id, OS.sel_setToolbar_, toolbar != null ? toolbar.id : 0);
}

public NSButton standardWindowButton(int /*long*/ b) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_standardWindowButton_, b);
	return result != 0 ? new NSButton(result) : null;
}

public int /*long*/ styleMask() {
	return OS.objc_msgSend(this.id, OS.sel_styleMask);
}

public void toggleToolbarShown(id sender) {
	OS.objc_msgSend(this.id, OS.sel_toggleToolbarShown_, sender != null ? sender.id : 0);
}

public int /*long*/ windowNumber() {
	return OS.objc_msgSend(this.id, OS.sel_windowNumber);
}

public void zoom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_zoom_, sender != null ? sender.id : 0);
}

}
