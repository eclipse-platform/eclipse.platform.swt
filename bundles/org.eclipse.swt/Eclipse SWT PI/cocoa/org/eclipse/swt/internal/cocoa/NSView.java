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

public class NSView extends NSResponder {

public NSView() {
	super();
}

public NSView(int id) {
	super(id);
}

public boolean acceptsFirstMouse(NSEvent theEvent) {
	return OS.objc_msgSend(this.id, OS.sel_acceptsFirstMouse_1, theEvent != null ? theEvent.id : 0) != 0;
}

public void addCursorRect(NSRect aRect, NSCursor anObj) {
	OS.objc_msgSend(this.id, OS.sel_addCursorRect_1cursor_1, aRect, anObj != null ? anObj.id : 0);
}

public void addSubview_(NSView aView) {
	OS.objc_msgSend(this.id, OS.sel_addSubview_1, aView != null ? aView.id : 0);
}

public void addSubview_positioned_relativeTo_(NSView aView, int place, NSView otherView) {
	OS.objc_msgSend(this.id, OS.sel_addSubview_1positioned_1relativeTo_1, aView != null ? aView.id : 0, place, otherView != null ? otherView.id : 0);
}

public int addToolTipRect(NSRect aRect, id anObject, int data) {
	return OS.objc_msgSend(this.id, OS.sel_addToolTipRect_1owner_1userData_1, aRect, anObject != null ? anObject.id : 0, data);
}

public void addTrackingArea(NSTrackingArea trackingArea) {
	OS.objc_msgSend(this.id, OS.sel_addTrackingArea_1, trackingArea != null ? trackingArea.id : 0);
}

public int addTrackingRect(NSRect aRect, id anObject, int data, boolean flag) {
	return OS.objc_msgSend(this.id, OS.sel_addTrackingRect_1owner_1userData_1assumeInside_1, aRect, anObject != null ? anObject.id : 0, data, flag);
}

public void adjustPageHeightNew(int newBottom, float oldTop, float oldBottom, float bottomLimit) {
	OS.objc_msgSend(this.id, OS.sel_adjustPageHeightNew_1top_1bottom_1limit_1, newBottom, oldTop, oldBottom, bottomLimit);
}

public void adjustPageWidthNew(int newRight, float oldLeft, float oldRight, float rightLimit) {
	OS.objc_msgSend(this.id, OS.sel_adjustPageWidthNew_1left_1right_1limit_1, newRight, oldLeft, oldRight, rightLimit);
}

public NSRect adjustScroll(NSRect newVisible) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_adjustScroll_1, newVisible);
	return result;
}

public void allocateGState() {
	OS.objc_msgSend(this.id, OS.sel_allocateGState);
}

public float alphaValue() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_alphaValue);
}

public NSView ancestorSharedWithView(NSView aView) {
	int result = OS.objc_msgSend(this.id, OS.sel_ancestorSharedWithView_1, aView != null ? aView.id : 0);
	return result == this.id ? this : (result != 0 ? new NSView(result) : null);
}

public boolean autoresizesSubviews() {
	return OS.objc_msgSend(this.id, OS.sel_autoresizesSubviews) != 0;
}

public int autoresizingMask() {
	return OS.objc_msgSend(this.id, OS.sel_autoresizingMask);
}

public boolean autoscroll(NSEvent theEvent) {
	return OS.objc_msgSend(this.id, OS.sel_autoscroll_1, theEvent != null ? theEvent.id : 0) != 0;
}

public NSArray backgroundFilters() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundFilters);
	return result != 0 ? new NSArray(result) : null;
}

public void beginDocument() {
	OS.objc_msgSend(this.id, OS.sel_beginDocument);
}

public void beginPageInRect(NSRect aRect, NSPoint location) {
	OS.objc_msgSend(this.id, OS.sel_beginPageInRect_1atPlacement_1, aRect, location);
}

public NSBitmapImageRep bitmapImageRepForCachingDisplayInRect(NSRect rect) {
	int result = OS.objc_msgSend(this.id, OS.sel_bitmapImageRepForCachingDisplayInRect_1, rect);
	return result != 0 ? new NSBitmapImageRep(result) : null;
}

public NSRect bounds() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_bounds);
	return result;
}

public float boundsRotation() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_boundsRotation);
}

public void cacheDisplayInRect(NSRect rect, NSBitmapImageRep bitmapImageRep) {
	OS.objc_msgSend(this.id, OS.sel_cacheDisplayInRect_1toBitmapImageRep_1, rect, bitmapImageRep != null ? bitmapImageRep.id : 0);
}

public boolean canBecomeKeyView() {
	return OS.objc_msgSend(this.id, OS.sel_canBecomeKeyView) != 0;
}

public boolean canDraw() {
	return OS.objc_msgSend(this.id, OS.sel_canDraw) != 0;
}

public NSRect centerScanRect(NSRect aRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_centerScanRect_1, aRect);
	return result;
}

//public CIFilter compositingFilter() {
//	int result = OS.objc_msgSend(this.id, OS.sel_compositingFilter);
//	return result != 0 ? new CIFilter(result) : null;
//}

public NSArray contentFilters() {
	int result = OS.objc_msgSend(this.id, OS.sel_contentFilters);
	return result != 0 ? new NSArray(result) : null;
}

public NSPoint convertPoint_fromView_(NSPoint aPoint, NSView aView) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_struct(result, this.id, OS.sel_convertPoint_1fromView_1, aPoint, aView != null ? aView.id : 0);
	return result;
}

public NSPoint convertPoint_toView_(NSPoint aPoint, NSView aView) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_struct(result, this.id, OS.sel_convertPoint_1toView_1, aPoint, aView != null ? aView.id : 0);
	return result;
}

public NSPoint convertPointFromBase(NSPoint aPoint) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_convertPointFromBase_1, aPoint);
	return result;
}

public NSPoint convertPointToBase(NSPoint aPoint) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_convertPointToBase_1, aPoint);
	return result;
}

public NSRect convertRect_fromView_(NSRect aRect, NSView aView) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_convertRect_1fromView_1, aRect, aView != null ? aView.id : 0);
	return result;
}

public NSRect convertRect_toView_(NSRect aRect, NSView aView) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_convertRect_1toView_1, aRect, aView != null ? aView.id : 0);
	return result;
}

public NSRect convertRectFromBase(NSRect aRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_convertRectFromBase_1, aRect);
	return result;
}

public NSRect convertRectToBase(NSRect aRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_convertRectToBase_1, aRect);
	return result;
}

public NSSize convertSize_fromView_(NSSize aSize, NSView aView) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_convertSize_1fromView_1, aSize, aView != null ? aView.id : 0);
	return result;
}

public NSSize convertSize_toView_(NSSize aSize, NSView aView) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_convertSize_1toView_1, aSize, aView != null ? aView.id : 0);
	return result;
}

public NSSize convertSizeFromBase(NSSize aSize) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_convertSizeFromBase_1, aSize);
	return result;
}

public NSSize convertSizeToBase(NSSize aSize) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_convertSizeToBase_1, aSize);
	return result;
}

public NSData dataWithEPSInsideRect(NSRect rect) {
	int result = OS.objc_msgSend(this.id, OS.sel_dataWithEPSInsideRect_1, rect);
	return result != 0 ? new NSData(result) : null;
}

public NSData dataWithPDFInsideRect(NSRect rect) {
	int result = OS.objc_msgSend(this.id, OS.sel_dataWithPDFInsideRect_1, rect);
	return result != 0 ? new NSData(result) : null;
}

public static int defaultFocusRingType() {
	return OS.objc_msgSend(OS.class_NSView, OS.sel_defaultFocusRingType);
}

public static NSMenu defaultMenu() {
	int result = OS.objc_msgSend(OS.class_NSView, OS.sel_defaultMenu);
	return result != 0 ? new NSMenu(result) : null;
}

public void didAddSubview(NSView subview) {
	OS.objc_msgSend(this.id, OS.sel_didAddSubview_1, subview != null ? subview.id : 0);
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

public void displayIfNeededIgnoringOpacity() {
	OS.objc_msgSend(this.id, OS.sel_displayIfNeededIgnoringOpacity);
}

public void displayIfNeededInRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_displayIfNeededInRect_1, rect);
}

public void displayIfNeededInRectIgnoringOpacity(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_displayIfNeededInRectIgnoringOpacity_1, rect);
}

public void displayRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_displayRect_1, rect);
}

public void displayRectIgnoringOpacity_(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_displayRectIgnoringOpacity_1, rect);
}

public void displayRectIgnoringOpacity_inContext_(NSRect aRect, NSGraphicsContext context) {
	OS.objc_msgSend(this.id, OS.sel_displayRectIgnoringOpacity_1inContext_1, aRect, context != null ? context.id : 0);
}

public boolean dragFile(NSString filename, NSRect rect, boolean aFlag, NSEvent event) {
	return OS.objc_msgSend(this.id, OS.sel_dragFile_1fromRect_1slideBack_1event_1, filename != null ? filename.id : 0, rect, aFlag, event != null ? event.id : 0) != 0;
}

public void dragImage(NSImage anImage, NSPoint viewLocation, NSSize initialOffset, NSEvent event, NSPasteboard pboard, id sourceObj, boolean slideFlag) {
	OS.objc_msgSend(this.id, OS.sel_dragImage_1at_1offset_1event_1pasteboard_1source_1slideBack_1, anImage != null ? anImage.id : 0, viewLocation, initialOffset, event != null ? event.id : 0, pboard != null ? pboard.id : 0, sourceObj != null ? sourceObj.id : 0, slideFlag);
}

public boolean dragPromisedFilesOfTypes(NSArray typeArray, NSRect rect, id sourceObject, boolean aFlag, NSEvent event) {
	return OS.objc_msgSend(this.id, OS.sel_dragPromisedFilesOfTypes_1fromRect_1source_1slideBack_1event_1, typeArray != null ? typeArray.id : 0, rect, sourceObject != null ? sourceObject.id : 0, aFlag, event != null ? event.id : 0) != 0;
}

public void drawPageBorderWithSize(NSSize borderSize) {
	OS.objc_msgSend(this.id, OS.sel_drawPageBorderWithSize_1, borderSize);
}

public void drawRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_drawRect_1, rect);
}

public void drawSheetBorderWithSize(NSSize borderSize) {
	OS.objc_msgSend(this.id, OS.sel_drawSheetBorderWithSize_1, borderSize);
}

public NSMenuItem enclosingMenuItem() {
	int result = OS.objc_msgSend(this.id, OS.sel_enclosingMenuItem);
	return result != 0 ? new NSMenuItem(result) : null;
}

public NSScrollView enclosingScrollView() {
	int result = OS.objc_msgSend(this.id, OS.sel_enclosingScrollView);
	return result != 0 ? new NSScrollView(result) : null;
}

public void endDocument() {
	OS.objc_msgSend(this.id, OS.sel_endDocument);
}

public void endPage() {
	OS.objc_msgSend(this.id, OS.sel_endPage);
}

public boolean enterFullScreenMode(NSScreen screen, NSDictionary options) {
	return OS.objc_msgSend(this.id, OS.sel_enterFullScreenMode_1withOptions_1, screen != null ? screen.id : 0, options != null ? options.id : 0) != 0;
}

public void exitFullScreenModeWithOptions(NSDictionary options) {
	OS.objc_msgSend(this.id, OS.sel_exitFullScreenModeWithOptions_1, options != null ? options.id : 0);
}

public int focusRingType() {
	return OS.objc_msgSend(this.id, OS.sel_focusRingType);
}

public static NSView focusView() {
	int result = OS.objc_msgSend(OS.class_NSView, OS.sel_focusView);
	return result != 0 ? new NSView(result) : null;
}

public NSRect frame() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_frame);
	return result;
}

public float frameCenterRotation() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_frameCenterRotation);
}

public float frameRotation() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_frameRotation);
}

public int gState() {
	return OS.objc_msgSend(this.id, OS.sel_gState);
}

public void getRectsBeingDrawn(int rects, int count) {
	OS.objc_msgSend(this.id, OS.sel_getRectsBeingDrawn_1count_1, rects, count);
}

public void getRectsExposedDuringLiveResize(int exposedRects, int count) {
	OS.objc_msgSend(this.id, OS.sel_getRectsExposedDuringLiveResize_1count_1, exposedRects, count);
}

public float heightAdjustLimit() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_heightAdjustLimit);
}

public NSView hitTest(NSPoint aPoint) {
	int result = OS.objc_msgSend(this.id, OS.sel_hitTest_1, aPoint);
	return result == this.id ? this : (result != 0 ? new NSView(result) : null);
}

public boolean inLiveResize() {
	return OS.objc_msgSend(this.id, OS.sel_inLiveResize) != 0;
}

public NSView initWithFrame(NSRect frameRect) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithFrame_1, frameRect);
	return result != 0 ? this : null;
}

public boolean isDescendantOf(NSView aView) {
	return OS.objc_msgSend(this.id, OS.sel_isDescendantOf_1, aView != null ? aView.id : 0) != 0;
}

public boolean isFlipped() {
	return OS.objc_msgSend(this.id, OS.sel_isFlipped) != 0;
}

public boolean isHidden() {
	return OS.objc_msgSend(this.id, OS.sel_isHidden) != 0;
}

public boolean isHiddenOrHasHiddenAncestor() {
	return OS.objc_msgSend(this.id, OS.sel_isHiddenOrHasHiddenAncestor) != 0;
}

public boolean isInFullScreenMode() {
	return OS.objc_msgSend(this.id, OS.sel_isInFullScreenMode) != 0;
}

public boolean isOpaque() {
	return OS.objc_msgSend(this.id, OS.sel_isOpaque) != 0;
}

public boolean isRotatedFromBase() {
	return OS.objc_msgSend(this.id, OS.sel_isRotatedFromBase) != 0;
}

public boolean isRotatedOrScaledFromBase() {
	return OS.objc_msgSend(this.id, OS.sel_isRotatedOrScaledFromBase) != 0;
}

public boolean knowsPageRange(int range) {
	return OS.objc_msgSend(this.id, OS.sel_knowsPageRange_1, range) != 0;
}

//public CALayer layer() {
//	int result = OS.objc_msgSend(this.id, OS.sel_layer);
//	return result != 0 ? new CALayer(result) : null;
//}

public NSPoint locationOfPrintRect(NSRect aRect) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_stret(result, this.id, OS.sel_locationOfPrintRect_1, aRect);
	return result;
}

public void lockFocus() {
	OS.objc_msgSend(this.id, OS.sel_lockFocus);
}

public boolean lockFocusIfCanDraw() {
	return OS.objc_msgSend(this.id, OS.sel_lockFocusIfCanDraw) != 0;
}

public boolean lockFocusIfCanDrawInContext(NSGraphicsContext context) {
	return OS.objc_msgSend(this.id, OS.sel_lockFocusIfCanDrawInContext_1, context != null ? context.id : 0) != 0;
}

public NSMenu menuForEvent(NSEvent event) {
	int result = OS.objc_msgSend(this.id, OS.sel_menuForEvent_1, event != null ? event.id : 0);
	return result != 0 ? new NSMenu(result) : null;
}

public boolean mouse(NSPoint aPoint, NSRect aRect) {
	return OS.objc_msgSend(this.id, OS.sel_mouse_1inRect_1, aPoint, aRect) != 0;
}

public boolean mouseDownCanMoveWindow() {
	return OS.objc_msgSend(this.id, OS.sel_mouseDownCanMoveWindow) != 0;
}

public boolean needsDisplay() {
	return OS.objc_msgSend(this.id, OS.sel_needsDisplay) != 0;
}

public boolean needsPanelToBecomeKey() {
	return OS.objc_msgSend(this.id, OS.sel_needsPanelToBecomeKey) != 0;
}

public boolean needsToDrawRect(NSRect aRect) {
	return OS.objc_msgSend(this.id, OS.sel_needsToDrawRect_1, aRect) != 0;
}

public NSView nextKeyView() {
	int result = OS.objc_msgSend(this.id, OS.sel_nextKeyView);
	return result == this.id ? this : (result != 0 ? new NSView(result) : null);
}

public NSView nextValidKeyView() {
	int result = OS.objc_msgSend(this.id, OS.sel_nextValidKeyView);
	return result == this.id ? this : (result != 0 ? new NSView(result) : null);
}

public NSView opaqueAncestor() {
	int result = OS.objc_msgSend(this.id, OS.sel_opaqueAncestor);
	return result == this.id ? this : (result != 0 ? new NSView(result) : null);
}

public NSAttributedString pageFooter() {
	int result = OS.objc_msgSend(this.id, OS.sel_pageFooter);
	return result != 0 ? new NSAttributedString(result) : null;
}

public NSAttributedString pageHeader() {
	int result = OS.objc_msgSend(this.id, OS.sel_pageHeader);
	return result != 0 ? new NSAttributedString(result) : null;
}

public boolean performKeyEquivalent(NSEvent theEvent) {
	return OS.objc_msgSend(this.id, OS.sel_performKeyEquivalent_1, theEvent != null ? theEvent.id : 0) != 0;
}

public boolean performMnemonic(NSString theString) {
	return OS.objc_msgSend(this.id, OS.sel_performMnemonic_1, theString != null ? theString.id : 0) != 0;
}

public boolean postsBoundsChangedNotifications() {
	return OS.objc_msgSend(this.id, OS.sel_postsBoundsChangedNotifications) != 0;
}

public boolean postsFrameChangedNotifications() {
	return OS.objc_msgSend(this.id, OS.sel_postsFrameChangedNotifications) != 0;
}

public boolean preservesContentDuringLiveResize() {
	return OS.objc_msgSend(this.id, OS.sel_preservesContentDuringLiveResize) != 0;
}

public NSView previousKeyView() {
	int result = OS.objc_msgSend(this.id, OS.sel_previousKeyView);
	return result == this.id ? this : (result != 0 ? new NSView(result) : null);
}

public NSView previousValidKeyView() {
	int result = OS.objc_msgSend(this.id, OS.sel_previousValidKeyView);
	return result == this.id ? this : (result != 0 ? new NSView(result) : null);
}

public void print(id sender) {
	OS.objc_msgSend(this.id, OS.sel_print_1, sender != null ? sender.id : 0);
}

public NSString printJobTitle() {
	int result = OS.objc_msgSend(this.id, OS.sel_printJobTitle);
	return result != 0 ? new NSString(result) : null;
}

public NSRect rectForPage(int page) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectForPage_1, page);
	return result;
}

public NSRect rectPreservedDuringLiveResize() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_rectPreservedDuringLiveResize);
	return result;
}

public void reflectScrolledClipView(NSClipView aClipView) {
	OS.objc_msgSend(this.id, OS.sel_reflectScrolledClipView_1, aClipView != null ? aClipView.id : 0);
}

public void registerForDraggedTypes(NSArray newTypes) {
	OS.objc_msgSend(this.id, OS.sel_registerForDraggedTypes_1, newTypes != null ? newTypes.id : 0);
}

public NSArray registeredDraggedTypes() {
	int result = OS.objc_msgSend(this.id, OS.sel_registeredDraggedTypes);
	return result != 0 ? new NSArray(result) : null;
}

public void releaseGState() {
	OS.objc_msgSend(this.id, OS.sel_releaseGState);
}

public void removeAllToolTips() {
	OS.objc_msgSend(this.id, OS.sel_removeAllToolTips);
}

public void removeCursorRect(NSRect aRect, NSCursor anObj) {
	OS.objc_msgSend(this.id, OS.sel_removeCursorRect_1cursor_1, aRect, anObj != null ? anObj.id : 0);
}

public void removeFromSuperview() {
	OS.objc_msgSend(this.id, OS.sel_removeFromSuperview);
}

public void removeFromSuperviewWithoutNeedingDisplay() {
	OS.objc_msgSend(this.id, OS.sel_removeFromSuperviewWithoutNeedingDisplay);
}

public void removeToolTip(int tag) {
	OS.objc_msgSend(this.id, OS.sel_removeToolTip_1, tag);
}

public void removeTrackingArea(NSTrackingArea trackingArea) {
	OS.objc_msgSend(this.id, OS.sel_removeTrackingArea_1, trackingArea != null ? trackingArea.id : 0);
}

public void removeTrackingRect(int tag) {
	OS.objc_msgSend(this.id, OS.sel_removeTrackingRect_1, tag);
}

public void renewGState() {
	OS.objc_msgSend(this.id, OS.sel_renewGState);
}

public void replaceSubview(NSView oldView, NSView newView) {
	OS.objc_msgSend(this.id, OS.sel_replaceSubview_1with_1, oldView != null ? oldView.id : 0, newView != null ? newView.id : 0);
}

public void resetCursorRects() {
	OS.objc_msgSend(this.id, OS.sel_resetCursorRects);
}

public void resizeSubviewsWithOldSize(NSSize oldSize) {
	OS.objc_msgSend(this.id, OS.sel_resizeSubviewsWithOldSize_1, oldSize);
}

public void resizeWithOldSuperviewSize(NSSize oldSize) {
	OS.objc_msgSend(this.id, OS.sel_resizeWithOldSuperviewSize_1, oldSize);
}

public void rotateByAngle(float angle) {
	OS.objc_msgSend(this.id, OS.sel_rotateByAngle_1, angle);
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

public void rulerView_willSetClientView_(NSRulerView ruler, NSView newClient) {
	OS.objc_msgSend(this.id, OS.sel_rulerView_1willSetClientView_1, ruler != null ? ruler.id : 0, newClient != null ? newClient.id : 0);
}

public void scaleUnitSquareToSize(NSSize newUnitSize) {
	OS.objc_msgSend(this.id, OS.sel_scaleUnitSquareToSize_1, newUnitSize);
}

public void scrollClipView(NSClipView aClipView, NSPoint aPoint) {
	OS.objc_msgSend(this.id, OS.sel_scrollClipView_1toPoint_1, aClipView != null ? aClipView.id : 0, aPoint);
}

public void scrollPoint(NSPoint aPoint) {
	OS.objc_msgSend(this.id, OS.sel_scrollPoint_1, aPoint);
}

public void scrollRect(NSRect aRect, NSSize delta) {
	OS.objc_msgSend(this.id, OS.sel_scrollRect_1by_1, aRect, delta);
}

public boolean scrollRectToVisible(NSRect aRect) {
	return OS.objc_msgSend(this.id, OS.sel_scrollRectToVisible_1, aRect) != 0;
}

public void setAlphaValue(float viewAlpha) {
	OS.objc_msgSend(this.id, OS.sel_setAlphaValue_1, viewAlpha);
}

public void setAutoresizesSubviews(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAutoresizesSubviews_1, flag);
}

public void setAutoresizingMask(int mask) {
	OS.objc_msgSend(this.id, OS.sel_setAutoresizingMask_1, mask);
}

public void setBackgroundFilters(NSArray filters) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundFilters_1, filters != null ? filters.id : 0);
}

public void setBounds(NSRect aRect) {
	OS.objc_msgSend(this.id, OS.sel_setBounds_1, aRect);
}

public void setBoundsOrigin(NSPoint newOrigin) {
	OS.objc_msgSend(this.id, OS.sel_setBoundsOrigin_1, newOrigin);
}

public void setBoundsRotation(float angle) {
	OS.objc_msgSend(this.id, OS.sel_setBoundsRotation_1, angle);
}

public void setBoundsSize(NSSize newSize) {
	OS.objc_msgSend(this.id, OS.sel_setBoundsSize_1, newSize);
}

//public void setCompositingFilter(CIFilter filter) {
//	OS.objc_msgSend(this.id, OS.sel_setCompositingFilter_1, filter != null ? filter.id : 0);
//}

public void setContentFilters(NSArray filters) {
	OS.objc_msgSend(this.id, OS.sel_setContentFilters_1, filters != null ? filters.id : 0);
}

public void setFocusRingType(int focusRingType) {
	OS.objc_msgSend(this.id, OS.sel_setFocusRingType_1, focusRingType);
}

public void setFrame(NSRect frameRect) {
	OS.objc_msgSend(this.id, OS.sel_setFrame_1, frameRect);
}

public void setFrameCenterRotation(float angle) {
	OS.objc_msgSend(this.id, OS.sel_setFrameCenterRotation_1, angle);
}

public void setFrameOrigin(NSPoint newOrigin) {
	OS.objc_msgSend(this.id, OS.sel_setFrameOrigin_1, newOrigin);
}

public void setFrameRotation(float angle) {
	OS.objc_msgSend(this.id, OS.sel_setFrameRotation_1, angle);
}

public void setFrameSize(NSSize newSize) {
	OS.objc_msgSend(this.id, OS.sel_setFrameSize_1, newSize);
}

public void setHidden(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHidden_1, flag);
}

public void setKeyboardFocusRingNeedsDisplayInRect(NSRect rect) {
	OS.objc_msgSend(this.id, OS.sel_setKeyboardFocusRingNeedsDisplayInRect_1, rect);
}

//public void setLayer(CALayer newLayer) {
//	OS.objc_msgSend(this.id, OS.sel_setLayer_1, newLayer != null ? newLayer.id : 0);
//}

public void setNeedsDisplay(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setNeedsDisplay_1, flag);
}

public void setNeedsDisplayInRect(NSRect invalidRect) {
	OS.objc_msgSend(this.id, OS.sel_setNeedsDisplayInRect_1, invalidRect);
}

public void setNextKeyView(NSView next) {
	OS.objc_msgSend(this.id, OS.sel_setNextKeyView_1, next != null ? next.id : 0);
}

public void setPostsBoundsChangedNotifications(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setPostsBoundsChangedNotifications_1, flag);
}

public void setPostsFrameChangedNotifications(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setPostsFrameChangedNotifications_1, flag);
}

public void setShadow(NSShadow shadow) {
	OS.objc_msgSend(this.id, OS.sel_setShadow_1, shadow != null ? shadow.id : 0);
}

public void setSubviews(NSArray newSubviews) {
	OS.objc_msgSend(this.id, OS.sel_setSubviews_1, newSubviews != null ? newSubviews.id : 0);
}

public void setToolTip(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setToolTip_1, string != null ? string.id : 0);
}

public void setUpGState() {
	OS.objc_msgSend(this.id, OS.sel_setUpGState);
}

public void setWantsLayer(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setWantsLayer_1, flag);
}

public NSShadow shadow() {
	int result = OS.objc_msgSend(this.id, OS.sel_shadow);
	return result != 0 ? new NSShadow(result) : null;
}

public boolean shouldDelayWindowOrderingForEvent(NSEvent theEvent) {
	return OS.objc_msgSend(this.id, OS.sel_shouldDelayWindowOrderingForEvent_1, theEvent != null ? theEvent.id : 0) != 0;
}

public boolean shouldDrawColor() {
	return OS.objc_msgSend(this.id, OS.sel_shouldDrawColor) != 0;
}

public void sortSubviewsUsingFunction(int compare, int context) {
	OS.objc_msgSend(this.id, OS.sel_sortSubviewsUsingFunction_1context_1, compare, context);
}

public NSArray subviews() {
	int result = OS.objc_msgSend(this.id, OS.sel_subviews);
	return result != 0 ? new NSArray(result) : null;
}

public NSView superview() {
	int result = OS.objc_msgSend(this.id, OS.sel_superview);
	return result == this.id ? this : (result != 0 ? new NSView(result) : null);
}

public int tag() {
	return OS.objc_msgSend(this.id, OS.sel_tag);
}

public NSString toolTip() {
	int result = OS.objc_msgSend(this.id, OS.sel_toolTip);
	return result != 0 ? new NSString(result) : null;
}

public NSArray trackingAreas() {
	int result = OS.objc_msgSend(this.id, OS.sel_trackingAreas);
	return result != 0 ? new NSArray(result) : null;
}

public void translateOriginToPoint(NSPoint translation) {
	OS.objc_msgSend(this.id, OS.sel_translateOriginToPoint_1, translation);
}

public void translateRectsNeedingDisplayInRect(NSRect clipRect, NSSize delta) {
	OS.objc_msgSend(this.id, OS.sel_translateRectsNeedingDisplayInRect_1by_1, clipRect, delta);
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

public void viewDidEndLiveResize() {
	OS.objc_msgSend(this.id, OS.sel_viewDidEndLiveResize);
}

public void viewDidHide() {
	OS.objc_msgSend(this.id, OS.sel_viewDidHide);
}

public void viewDidMoveToSuperview() {
	OS.objc_msgSend(this.id, OS.sel_viewDidMoveToSuperview);
}

public void viewDidMoveToWindow() {
	OS.objc_msgSend(this.id, OS.sel_viewDidMoveToWindow);
}

public void viewDidUnhide() {
	OS.objc_msgSend(this.id, OS.sel_viewDidUnhide);
}

public void viewWillDraw() {
	OS.objc_msgSend(this.id, OS.sel_viewWillDraw);
}

public void viewWillMoveToSuperview(NSView newSuperview) {
	OS.objc_msgSend(this.id, OS.sel_viewWillMoveToSuperview_1, newSuperview != null ? newSuperview.id : 0);
}

public void viewWillMoveToWindow(NSWindow newWindow) {
	OS.objc_msgSend(this.id, OS.sel_viewWillMoveToWindow_1, newWindow != null ? newWindow.id : 0);
}

public void viewWillStartLiveResize() {
	OS.objc_msgSend(this.id, OS.sel_viewWillStartLiveResize);
}

public id viewWithTag(int aTag) {
	int result = OS.objc_msgSend(this.id, OS.sel_viewWithTag_1, aTag);
	return result != 0 ? new id(result) : null;
}

public NSRect visibleRect() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_visibleRect);
	return result;
}

public boolean wantsDefaultClipping() {
	return OS.objc_msgSend(this.id, OS.sel_wantsDefaultClipping) != 0;
}

public boolean wantsLayer() {
	return OS.objc_msgSend(this.id, OS.sel_wantsLayer) != 0;
}

public float widthAdjustLimit() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_widthAdjustLimit);
}

public void willRemoveSubview(NSView subview) {
	OS.objc_msgSend(this.id, OS.sel_willRemoveSubview_1, subview != null ? subview.id : 0);
}

public NSWindow window() {
	int result = OS.objc_msgSend(this.id, OS.sel_window);
	return result != 0 ? new NSWindow(result) : null;
}

public void writeEPSInsideRect(NSRect rect, NSPasteboard pasteboard) {
	OS.objc_msgSend(this.id, OS.sel_writeEPSInsideRect_1toPasteboard_1, rect, pasteboard != null ? pasteboard.id : 0);
}

public void writePDFInsideRect(NSRect rect, NSPasteboard pasteboard) {
	OS.objc_msgSend(this.id, OS.sel_writePDFInsideRect_1toPasteboard_1, rect, pasteboard != null ? pasteboard.id : 0);
}

}
