/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
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

public class NSView extends NSResponder {

public NSView() {
	super();
}

public NSView(long id) {
	super(id);
}

public NSView(id id) {
	super(id);
}

public boolean acceptsFirstMouse(NSEvent theEvent) {
	return OS.objc_msgSend_bool(this.id, OS.sel_acceptsFirstMouse_, theEvent != null ? theEvent.id : 0);
}

public void addSubview(NSView aView) {
	OS.objc_msgSend(this.id, OS.sel_addSubview_, aView != null ? aView.id : 0);
}

public void addSubview(NSView aView, long place, NSView otherView) {
	OS.objc_msgSend(this.id, OS.sel_addSubview_positioned_relativeTo_, aView != null ? aView.id : 0, place, otherView != null ? otherView.id : 0);
}

public long addToolTipRect(NSRect aRect, id anObject, long data) {
	return OS.objc_msgSend(this.id, OS.sel_addToolTipRect_owner_userData_, aRect, anObject != null ? anObject.id : 0, data);
}

public void beginDocument() {
	OS.objc_msgSend(this.id, OS.sel_beginDocument);
}

public void beginPageInRect(NSRect aRect, NSPoint location) {
	OS.objc_msgSend(this.id, OS.sel_beginPageInRect_atPlacement_, aRect, location);
}

public NSBitmapImageRep bitmapImageRepForCachingDisplayInRect(NSRect rect) {
	long result = OS.objc_msgSend(this.id, OS.sel_bitmapImageRepForCachingDisplayInRect_, rect);
	return result != 0 ? new NSBitmapImageRep(result) : null;
}

public NSRect bounds() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_bounds);
	return result;
}

public void cacheDisplayInRect(NSRect rect, NSBitmapImageRep bitmapImageRep) {
	OS.objc_msgSend(this.id, OS.sel_cacheDisplayInRect_toBitmapImageRep_, rect, bitmapImageRep != null ? bitmapImageRep.id : 0);
}

public boolean canBecomeKeyView() {
	return OS.objc_msgSend_bool(this.id, OS.sel_canBecomeKeyView);
}

public NSPoint convertPoint_fromView_(NSPoint aPoint, NSView aView) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_convertPoint_fromView_, aPoint, aView != null ? aView.id : 0);
	return result;
}

public NSPoint convertPoint_toView_(NSPoint aPoint, NSView aView) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_convertPoint_toView_, aPoint, aView != null ? aView.id : 0);
	return result;
}

public NSRect convertRect_fromView_(NSRect aRect, NSView aView) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_convertRect_fromView_, aRect, aView != null ? aView.id : 0);
	return result;
}

public NSRect convertRect_toView_(NSRect aRect, NSView aView) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_convertRect_toView_, aRect, aView != null ? aView.id : 0);
	return result;
}

public void discardCursorRects() {
	OS.objc_msgSend(this.id, OS.sel_discardCursorRects);
}

public void display() {
	OS.objc_msgSend(this.id, OS.sel_display);
}

public void displayIfNeeded() {
	OS.objc_msgSend(this.id, OS.sel_displayIfNeeded);
}

public void displayRectIgnoringOpacity(NSRect aRect, NSGraphicsContext context) {
	OS.objc_msgSend(this.id, OS.sel_displayRectIgnoringOpacity_inContext_, aRect, context != null ? context.id : 0);
}

public void dragImage(NSImage anImage, NSPoint viewLocation, NSSize initialOffset, NSEvent event, NSPasteboard pboard, id sourceObj, boolean slideFlag) {
	OS.objc_msgSend(this.id, OS.sel_dragImage_at_offset_event_pasteboard_source_slideBack_, anImage != null ? anImage.id : 0, viewLocation, initialOffset, event != null ? event.id : 0, pboard != null ? pboard.id : 0, sourceObj != null ? sourceObj.id : 0, slideFlag);
}

public void drawRect(NSRect dirtyRect) {
	OS.objc_msgSend(this.id, OS.sel_drawRect_, dirtyRect);
}

public void endDocument() {
	OS.objc_msgSend(this.id, OS.sel_endDocument);
}

public void endPage() {
	OS.objc_msgSend(this.id, OS.sel_endPage);
}

public NSRect frame() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_frame);
	return result;
}

public NSView hitTest(NSPoint aPoint) {
	long result = OS.objc_msgSend(this.id, OS.sel_hitTest_, aPoint);
	return result == this.id ? this : (result != 0 ? new NSView(result) : null);
}

public NSView initWithFrame(NSRect frameRect) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithFrame_, frameRect);
	return result == this.id ? this : (result != 0 ? new NSView(result) : null);
}

public boolean isDescendantOf(NSView aView) {
	return OS.objc_msgSend_bool(this.id, OS.sel_isDescendantOf_, aView != null ? aView.id : 0);
}

public boolean isFlipped() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isFlipped);
}

public boolean isHidden() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isHidden);
}

public boolean isHiddenOrHasHiddenAncestor() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isHiddenOrHasHiddenAncestor);
}

public boolean isOpaque() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isOpaque);
}

public CALayer layer() {
	long result = OS.objc_msgSend(this.id, OS.sel_layer);
	return result != 0 ? new CALayer(result) : null;
}

public void lockFocus() {
	OS.objc_msgSend(this.id, OS.sel_lockFocus);
}

public boolean lockFocusIfCanDraw() {
	return OS.objc_msgSend_bool(this.id, OS.sel_lockFocusIfCanDraw);
}

public NSMenu menuForEvent(NSEvent event) {
	long result = OS.objc_msgSend(this.id, OS.sel_menuForEvent_, event != null ? event.id : 0);
	return result != 0 ? new NSMenu(result) : null;
}

public boolean mouse(NSPoint aPoint, NSRect aRect) {
	return OS.objc_msgSend_bool(this.id, OS.sel_mouse_inRect_, aPoint, aRect);
}

public boolean mouseDownCanMoveWindow() {
	return OS.objc_msgSend_bool(this.id, OS.sel_mouseDownCanMoveWindow);
}

public boolean needsPanelToBecomeKey() {
	return OS.objc_msgSend_bool(this.id, OS.sel_needsPanelToBecomeKey);
}

public void registerForDraggedTypes(NSArray newTypes) {
	OS.objc_msgSend(this.id, OS.sel_registerForDraggedTypes_, newTypes != null ? newTypes.id : 0);
}

public void removeFromSuperview() {
	OS.objc_msgSend(this.id, OS.sel_removeFromSuperview);
}

public void removeToolTip(long tag) {
	OS.objc_msgSend(this.id, OS.sel_removeToolTip_, tag);
}

public void removeTrackingArea(NSTrackingArea trackingArea) {
	OS.objc_msgSend(this.id, OS.sel_removeTrackingArea_, trackingArea != null ? trackingArea.id : 0);
}

public void resetCursorRects() {
	OS.objc_msgSend(this.id, OS.sel_resetCursorRects);
}

public void scrollClipView(NSClipView aClipView, NSPoint aPoint) {
	OS.objc_msgSend(this.id, OS.sel_scrollClipView_toPoint_, aClipView != null ? aClipView.id : 0, aPoint);
}

public void scrollPoint(NSPoint aPoint) {
	OS.objc_msgSend(this.id, OS.sel_scrollPoint_, aPoint);
}

public void scrollRect(NSRect aRect, NSSize delta) {
	OS.objc_msgSend(this.id, OS.sel_scrollRect_by_, aRect, delta);
}

public void setAcceptsTouchEvents(boolean acceptsTouchEvents) {
	OS.objc_msgSend(this.id, OS.sel_setAcceptsTouchEvents_, acceptsTouchEvents);
}

public void setAutoresizesSubviews(boolean autoresizesSubviews) {
	OS.objc_msgSend(this.id, OS.sel_setAutoresizesSubviews_, autoresizesSubviews);
}

public void setAutoresizingMask(long autoresizingMask) {
	OS.objc_msgSend(this.id, OS.sel_setAutoresizingMask_, autoresizingMask);
}

public void setBoundsRotation(double boundsRotation) {
	OS.objc_msgSend(this.id, OS.sel_setBoundsRotation_, boundsRotation);
}

public void setBoundsSize(NSSize newSize) {
	OS.objc_msgSend(this.id, OS.sel_setBoundsSize_, newSize);
}

public void setFocusRingType(long focusRingType) {
	OS.objc_msgSend(this.id, OS.sel_setFocusRingType_, focusRingType);
}

public void setFrame(NSRect frame) {
	OS.objc_msgSend(this.id, OS.sel_setFrame_, frame);
}

public void setFrameOrigin(NSPoint newOrigin) {
	OS.objc_msgSend(this.id, OS.sel_setFrameOrigin_, newOrigin);
}

public void setFrameSize(NSSize newSize) {
	OS.objc_msgSend(this.id, OS.sel_setFrameSize_, newSize);
}

public void setHidden(boolean hidden) {
	OS.objc_msgSend(this.id, OS.sel_setHidden_, hidden);
}

public void setNeedsDisplay(boolean needsDisplay) {
	OS.objc_msgSend(this.id, OS.sel_setNeedsDisplay_, needsDisplay);
}

public void setNeedsDisplayInRect(NSRect invalidRect) {
	OS.objc_msgSend(this.id, OS.sel_setNeedsDisplayInRect_, invalidRect);
}

public void setToolTip(NSString toolTip) {
	OS.objc_msgSend(this.id, OS.sel_setToolTip_, toolTip != null ? toolTip.id : 0);
}

public void setWantsRestingTouches(boolean wantsRestingTouches) {
	OS.objc_msgSend(this.id, OS.sel_setWantsRestingTouches_, wantsRestingTouches);
}

public boolean shouldDelayWindowOrderingForEvent(NSEvent theEvent) {
	return OS.objc_msgSend_bool(this.id, OS.sel_shouldDelayWindowOrderingForEvent_, theEvent != null ? theEvent.id : 0);
}

public NSArray subviews() {
	long result = OS.objc_msgSend(this.id, OS.sel_subviews);
	return result != 0 ? new NSArray(result) : null;
}

public NSView superview() {
	long result = OS.objc_msgSend(this.id, OS.sel_superview);
	return result == this.id ? this : (result != 0 ? new NSView(result) : null);
}

public NSArray trackingAreas() {
	long result = OS.objc_msgSend(this.id, OS.sel_trackingAreas);
	return result != 0 ? new NSArray(result) : null;
}

public void unlockFocus() {
	OS.objc_msgSend(this.id, OS.sel_unlockFocus);
}

public void unregisterDraggedTypes() {
	OS.objc_msgSend(this.id, OS.sel_unregisterDraggedTypes);
}

public void updateTrackingAreas() {
	OS.objc_msgSend(this.id, OS.sel_updateTrackingAreas);
}

public void viewDidMoveToWindow() {
	OS.objc_msgSend(this.id, OS.sel_viewDidMoveToWindow);
}

public void viewWillMoveToWindow(NSWindow newWindow) {
	OS.objc_msgSend(this.id, OS.sel_viewWillMoveToWindow_, newWindow != null ? newWindow.id : 0);
}

public NSRect visibleRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_visibleRect);
	return result;
}

public NSWindow window() {
	long result = OS.objc_msgSend(this.id, OS.sel_window);
	return result != 0 ? new NSWindow(result) : null;
}

}
