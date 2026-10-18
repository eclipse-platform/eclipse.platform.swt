/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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

public class NSWindow extends NSResponder {

public NSWindow() {
	super();
}

public NSWindow(long id) {
	super(id);
}

public NSWindow(id id) {
	super(id);
}

public void addChildWindow(NSWindow childWin, long place) {
	OS.objc_msgSend(this.id, OS.sel_addChildWindow_ordered_, childWin != null ? childWin.id : 0, place);
}

public double alphaValue() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_alphaValue);
}

public boolean areCursorRectsEnabled() {
	return OS.objc_msgSend_bool(this.id, OS.sel_areCursorRectsEnabled);
}

public void becomeKeyWindow() {
	OS.objc_msgSend(this.id, OS.sel_becomeKeyWindow);
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

public long collectionBehavior() {
	return OS.objc_msgSend(this.id, OS.sel_collectionBehavior);
}

public NSView contentView() {
	long result = OS.objc_msgSend(this.id, OS.sel_contentView);
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

public NSButtonCell defaultButtonCell() {
	long result = OS.objc_msgSend(this.id, OS.sel_defaultButtonCell);
	return result != 0 ? new NSButtonCell(result) : null;
}

public id delegate() {
	long result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void deminiaturize(id sender) {
	OS.objc_msgSend(this.id, OS.sel_deminiaturize_, sender != null ? sender.id : 0);
}

public void disableCursorRects() {
	OS.objc_msgSend(this.id, OS.sel_disableCursorRects);
}

public void disableFlushWindow() {
	OS.objc_msgSend(this.id, OS.sel_disableFlushWindow);
}

public void display() {
	OS.objc_msgSend(this.id, OS.sel_display);
}

public void enableCursorRects() {
	OS.objc_msgSend(this.id, OS.sel_enableCursorRects);
}

public void enableFlushWindow() {
	OS.objc_msgSend(this.id, OS.sel_enableFlushWindow);
}

public void endEditingFor(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_endEditingFor_, anObject != null ? anObject.id : 0);
}

public NSText fieldEditor(boolean createFlag, id anObject) {
	long result = OS.objc_msgSend(this.id, OS.sel_fieldEditor_forObject_, createFlag, anObject != null ? anObject.id : 0);
	return result != 0 ? new NSText(result) : null;
}

public NSResponder firstResponder() {
	long result = OS.objc_msgSend(this.id, OS.sel_firstResponder);
	return result != 0 ? new NSResponder(result) : null;
}

public void flushWindowIfNeeded() {
	OS.objc_msgSend(this.id, OS.sel_flushWindowIfNeeded);
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
	long result = OS.objc_msgSend(this.id, OS.sel_graphicsContext);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public boolean hasShadow() {
	return OS.objc_msgSend_bool(this.id, OS.sel_hasShadow);
}

public NSWindow initWithContentRect(NSRect contentRect, long aStyle, long bufferingType, boolean flag) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithContentRect_styleMask_backing_defer_, contentRect, aStyle, bufferingType, flag);
	return result == this.id ? this : (result != 0 ? new NSWindow(result) : null);
}

public NSWindow initWithContentRect(NSRect contentRect, long aStyle, long bufferingType, boolean flag, NSScreen screen) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithContentRect_styleMask_backing_defer_screen_, contentRect, aStyle, bufferingType, flag, screen != null ? screen.id : 0);
	return result == this.id ? this : (result != 0 ? new NSWindow(result) : null);
}

public void invalidateShadow() {
	OS.objc_msgSend(this.id, OS.sel_invalidateShadow);
}

public boolean isDocumentEdited() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isDocumentEdited);
}

public boolean isKeyWindow() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isKeyWindow);
}

public boolean isMainWindow() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isMainWindow);
}

public boolean isMiniaturized() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isMiniaturized);
}

public boolean isSheet() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isSheet);
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

public NSSize maxSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_maxSize);
	return result;
}

public static double minFrameWidthWithTitle(NSString aTitle, long aStyle) {
	return OS.objc_msgSend_fpret(OS.class_NSWindow, OS.sel_minFrameWidthWithTitle_styleMask_, aTitle != null ? aTitle.id : 0, aStyle);
}

public NSSize minSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_minSize);
	return result;
}

public void miniaturize(id sender) {
	OS.objc_msgSend(this.id, OS.sel_miniaturize_, sender != null ? sender.id : 0);
}

public NSPoint mouseLocationOutsideOfEventStream() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_mouseLocationOutsideOfEventStream);
	return result;
}

public void orderBack(id sender) {
	OS.objc_msgSend(this.id, OS.sel_orderBack_, sender != null ? sender.id : 0);
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

public void orderWindow(long place, long otherWin) {
	OS.objc_msgSend(this.id, OS.sel_orderWindow_relativeTo_, place, otherWin);
}

public NSWindow parentWindow() {
	long result = OS.objc_msgSend(this.id, OS.sel_parentWindow);
	return result == this.id ? this : (result != 0 ? new NSWindow(result) : null);
}

public void removeChildWindow(NSWindow childWin) {
	OS.objc_msgSend(this.id, OS.sel_removeChildWindow_, childWin != null ? childWin.id : 0);
}

public NSScreen screen() {
	long result = OS.objc_msgSend(this.id, OS.sel_screen);
	return result != 0 ? new NSScreen(result) : null;
}

public void sendEvent(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_sendEvent_, theEvent != null ? theEvent.id : 0);
}

public void setAcceptsMouseMovedEvents(boolean acceptsMouseMovedEvents) {
	OS.objc_msgSend(this.id, OS.sel_setAcceptsMouseMovedEvents_, acceptsMouseMovedEvents);
}

public void setAlphaValue(double alphaValue) {
	OS.objc_msgSend(this.id, OS.sel_setAlphaValue_, alphaValue);
}

public void setBackgroundColor(NSColor backgroundColor) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_, backgroundColor != null ? backgroundColor.id : 0);
}

public void setCollectionBehavior(long collectionBehavior) {
	OS.objc_msgSend(this.id, OS.sel_setCollectionBehavior_, collectionBehavior);
}

public void setContentView(NSView contentView) {
	OS.objc_msgSend(this.id, OS.sel_setContentView_, contentView != null ? contentView.id : 0);
}

public void setDefaultButtonCell(NSButtonCell defaultButtonCell) {
	OS.objc_msgSend(this.id, OS.sel_setDefaultButtonCell_, defaultButtonCell != null ? defaultButtonCell.id : 0);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_, delegate != null ? delegate.id : 0);
}

public void setDocumentEdited(boolean documentEdited) {
	OS.objc_msgSend(this.id, OS.sel_setDocumentEdited_, documentEdited);
}

public void setFrame(NSRect frameRect, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setFrame_display_, frameRect, flag);
}

public void setFrame(NSRect frameRect, boolean displayFlag, boolean animateFlag) {
	OS.objc_msgSend(this.id, OS.sel_setFrame_display_animate_, frameRect, displayFlag, animateFlag);
}

public void setHasShadow(boolean hasShadow) {
	OS.objc_msgSend(this.id, OS.sel_setHasShadow_, hasShadow);
}

public void setHidesOnDeactivate(boolean hidesOnDeactivate) {
	OS.objc_msgSend(this.id, OS.sel_setHidesOnDeactivate_, hidesOnDeactivate);
}

public void setLevel(long level) {
	OS.objc_msgSend(this.id, OS.sel_setLevel_, level);
}

public void setMaxSize(NSSize maxSize) {
	OS.objc_msgSend(this.id, OS.sel_setMaxSize_, maxSize);
}

public void setMinSize(NSSize minSize) {
	OS.objc_msgSend(this.id, OS.sel_setMinSize_, minSize);
}

public void setMovable(boolean movable) {
	OS.objc_msgSend(this.id, OS.sel_setMovable_, movable);
}

public void setOpaque(boolean opaque) {
	OS.objc_msgSend(this.id, OS.sel_setOpaque_, opaque);
}

public void setReleasedWhenClosed(boolean releasedWhenClosed) {
	OS.objc_msgSend(this.id, OS.sel_setReleasedWhenClosed_, releasedWhenClosed);
}

public void setRepresentedFilename(NSString representedFilename) {
	OS.objc_msgSend(this.id, OS.sel_setRepresentedFilename_, representedFilename != null ? representedFilename.id : 0);
}

public void setRepresentedURL(NSURL representedURL) {
	OS.objc_msgSend(this.id, OS.sel_setRepresentedURL_, representedURL != null ? representedURL.id : 0);
}

public void setShowsResizeIndicator(boolean showsResizeIndicator) {
	OS.objc_msgSend(this.id, OS.sel_setShowsResizeIndicator_, showsResizeIndicator);
}

public void setShowsToolbarButton(boolean showsToolbarButton) {
	OS.objc_msgSend(this.id, OS.sel_setShowsToolbarButton_, showsToolbarButton);
}

public void setTitle(NSString title) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_, title != null ? title.id : 0);
}

public void setToolbar(NSToolbar toolbar) {
	OS.objc_msgSend(this.id, OS.sel_setToolbar_, toolbar != null ? toolbar.id : 0);
}

public long styleMask() {
	return OS.objc_msgSend(this.id, OS.sel_styleMask);
}

public void toggleFullScreen(id sender) {
	OS.objc_msgSend(this.id, OS.sel_toggleFullScreen_, sender != null ? sender.id : 0);
}

public NSToolbar toolbar() {
	long result = OS.objc_msgSend(this.id, OS.sel_toolbar);
	return result != 0 ? new NSToolbar(result) : null;
}

public long windowNumber() {
	return OS.objc_msgSend(this.id, OS.sel_windowNumber);
}

public static long windowNumberAtPoint(NSPoint point, long windowNumber) {
	return OS.objc_msgSend(OS.class_NSWindow, OS.sel_windowNumberAtPoint_belowWindowWithWindowNumber_, point, windowNumber);
}

public void zoom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_zoom_, sender != null ? sender.id : 0);
}

}
