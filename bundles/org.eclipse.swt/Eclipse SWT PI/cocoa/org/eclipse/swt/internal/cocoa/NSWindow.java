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

public class NSWindow extends NSResponder {

public NSWindow() {
	super();
}

public NSWindow(int id) {
	super(id);
}

public boolean acceptsMouseMovedEvents() {
	return OS.objc_msgSend(this.id, OS.sel_acceptsMouseMovedEvents) != 0;
}

public void addChildWindow(NSWindow childWin, int place) {
	OS.objc_msgSend(this.id, OS.sel_addChildWindow_1ordered_1, childWin != null ? childWin.id : 0, place);
}

public boolean allowsToolTipsWhenApplicationIsInactive() {
	return OS.objc_msgSend(this.id, OS.sel_allowsToolTipsWhenApplicationIsInactive) != 0;
}

public float alphaValue() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_alphaValue);
}

public double animationResizeTime(NSRect newFrame) {
	return OS.objc_msgSend_fpret(this.id, OS.sel_animationResizeTime_1, newFrame);
}

public boolean areCursorRectsEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_areCursorRectsEnabled) != 0;
}

public NSSize aspectRatio() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_aspectRatio);
	return result;
}

public NSWindow attachedSheet() {
	int result = OS.objc_msgSend(this.id, OS.sel_attachedSheet);
	return result == this.id ? this : (result != 0 ? new NSWindow(result) : null);
}

public boolean autorecalculatesContentBorderThicknessForEdge(int edge) {
	return OS.objc_msgSend(this.id, OS.sel_autorecalculatesContentBorderThicknessForEdge_1, edge) != 0;
}

public boolean autorecalculatesKeyViewLoop() {
	return OS.objc_msgSend(this.id, OS.sel_autorecalculatesKeyViewLoop) != 0;
}

public NSColor backgroundColor() {
	int result = OS.objc_msgSend(this.id, OS.sel_backgroundColor);
	return result != 0 ? new NSColor(result) : null;
}

public int backingLocation() {
	return OS.objc_msgSend(this.id, OS.sel_backingLocation);
}

public int backingType() {
	return OS.objc_msgSend(this.id, OS.sel_backingType);
}

public void becomeKeyWindow() {
	OS.objc_msgSend(this.id, OS.sel_becomeKeyWindow);
}

public void becomeMainWindow() {
	OS.objc_msgSend(this.id, OS.sel_becomeMainWindow);
}

public void cacheImageInRect(NSRect aRect) {
	OS.objc_msgSend(this.id, OS.sel_cacheImageInRect_1, aRect);
}

public boolean canBeVisibleOnAllSpaces() {
	return OS.objc_msgSend(this.id, OS.sel_canBeVisibleOnAllSpaces) != 0;
}

public boolean canBecomeKeyWindow() {
	return OS.objc_msgSend(this.id, OS.sel_canBecomeKeyWindow) != 0;
}

public boolean canBecomeMainWindow() {
	return OS.objc_msgSend(this.id, OS.sel_canBecomeMainWindow) != 0;
}

public boolean canBecomeVisibleWithoutLogin() {
	return OS.objc_msgSend(this.id, OS.sel_canBecomeVisibleWithoutLogin) != 0;
}

public boolean canHide() {
	return OS.objc_msgSend(this.id, OS.sel_canHide) != 0;
}

public boolean canStoreColor() {
	return OS.objc_msgSend(this.id, OS.sel_canStoreColor) != 0;
}

public NSPoint cascadeTopLeftFromPoint(NSPoint topLeftPoint) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_struct(result, this.id, OS.sel_cascadeTopLeftFromPoint_1, topLeftPoint);
	return result;
}

public void center() {
	OS.objc_msgSend(this.id, OS.sel_center);
}

public NSArray childWindows() {
	int result = OS.objc_msgSend(this.id, OS.sel_childWindows);
	return result != 0 ? new NSArray(result) : null;
}

public void close() {
	OS.objc_msgSend(this.id, OS.sel_close);
}

public int collectionBehavior() {
	return OS.objc_msgSend(this.id, OS.sel_collectionBehavior);
}

public NSRect constrainFrameRect(NSRect frameRect, NSScreen screen) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_constrainFrameRect_1toScreen_1, frameRect, screen != null ? screen.id : 0);
	return result;
}

public NSSize contentAspectRatio() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_contentAspectRatio);
	return result;
}

public float contentBorderThicknessForEdge(int edge) {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_contentBorderThicknessForEdge_1, edge);
}

public NSSize contentMaxSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_contentMaxSize);
	return result;
}

public NSSize contentMinSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_contentMinSize);
	return result;
}

public NSRect contentRectForFrameRect_(NSRect frameRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_contentRectForFrameRect_1, frameRect);
	return result;
}

public static NSRect static_contentRectForFrameRect_styleMask_(NSRect fRect, int aStyle) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, OS.class_NSWindow, OS.sel_contentRectForFrameRect_1styleMask_1, fRect, aStyle);
	return result;
}

public NSSize contentResizeIncrements() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_contentResizeIncrements);
	return result;
}

public NSView contentView() {
	int result = OS.objc_msgSend(this.id, OS.sel_contentView);
	return result != 0 ? new NSView(result) : null;
}

public NSPoint convertBaseToScreen(NSPoint aPoint) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_struct(result, this.id, OS.sel_convertBaseToScreen_1, aPoint);
	return result;
}

public NSPoint convertScreenToBase(NSPoint aPoint) {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_struct(result, this.id, OS.sel_convertScreenToBase_1, aPoint);
	return result;
}

public NSEvent currentEvent() {
	int result = OS.objc_msgSend(this.id, OS.sel_currentEvent);
	return result != 0 ? new NSEvent(result) : null;
}

public NSData dataWithEPSInsideRect(NSRect rect) {
	int result = OS.objc_msgSend(this.id, OS.sel_dataWithEPSInsideRect_1, rect);
	return result != 0 ? new NSData(result) : null;
}

public NSData dataWithPDFInsideRect(NSRect rect) {
	int result = OS.objc_msgSend(this.id, OS.sel_dataWithPDFInsideRect_1, rect);
	return result != 0 ? new NSData(result) : null;
}

public NSScreen deepestScreen() {
	int result = OS.objc_msgSend(this.id, OS.sel_deepestScreen);
	return result != 0 ? new NSScreen(result) : null;
}

public NSButtonCell defaultButtonCell() {
	int result = OS.objc_msgSend(this.id, OS.sel_defaultButtonCell);
	return result != 0 ? new NSButtonCell(result) : null;
}

public static int defaultDepthLimit() {
	return OS.objc_msgSend(OS.class_NSWindow, OS.sel_defaultDepthLimit);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public void deminiaturize(id sender) {
	OS.objc_msgSend(this.id, OS.sel_deminiaturize_1, sender != null ? sender.id : 0);
}

public int depthLimit() {
	return OS.objc_msgSend(this.id, OS.sel_depthLimit);
}

public NSDictionary deviceDescription() {
	int result = OS.objc_msgSend(this.id, OS.sel_deviceDescription);
	return result != 0 ? new NSDictionary(result) : null;
}

public void disableCursorRects() {
	OS.objc_msgSend(this.id, OS.sel_disableCursorRects);
}

public void disableFlushWindow() {
	OS.objc_msgSend(this.id, OS.sel_disableFlushWindow);
}

public void disableKeyEquivalentForDefaultButtonCell() {
	OS.objc_msgSend(this.id, OS.sel_disableKeyEquivalentForDefaultButtonCell);
}

public void disableScreenUpdatesUntilFlush() {
	OS.objc_msgSend(this.id, OS.sel_disableScreenUpdatesUntilFlush);
}

public void discardCachedImage() {
	OS.objc_msgSend(this.id, OS.sel_discardCachedImage);
}

public void discardCursorRects() {
	OS.objc_msgSend(this.id, OS.sel_discardCursorRects);
}

public void discardEventsMatchingMask(int mask, NSEvent lastEvent) {
	OS.objc_msgSend(this.id, OS.sel_discardEventsMatchingMask_1beforeEvent_1, mask, lastEvent != null ? lastEvent.id : 0);
}

public void display() {
	OS.objc_msgSend(this.id, OS.sel_display);
}

public void displayIfNeeded() {
	OS.objc_msgSend(this.id, OS.sel_displayIfNeeded);
}

public boolean displaysWhenScreenProfileChanges() {
	return OS.objc_msgSend(this.id, OS.sel_displaysWhenScreenProfileChanges) != 0;
}

public NSDockTile dockTile() {
	int result = OS.objc_msgSend(this.id, OS.sel_dockTile);
	return result != 0 ? new NSDockTile(result) : null;
}

public void dragImage(NSImage anImage, NSPoint baseLocation, NSSize initialOffset, NSEvent event, NSPasteboard pboard, id sourceObj, boolean slideFlag) {
	OS.objc_msgSend(this.id, OS.sel_dragImage_1at_1offset_1event_1pasteboard_1source_1slideBack_1, anImage != null ? anImage.id : 0, baseLocation, initialOffset, event != null ? event.id : 0, pboard != null ? pboard.id : 0, sourceObj != null ? sourceObj.id : 0, slideFlag);
}

public NSArray drawers() {
	int result = OS.objc_msgSend(this.id, OS.sel_drawers);
	return result != 0 ? new NSArray(result) : null;
}

public void enableCursorRects() {
	OS.objc_msgSend(this.id, OS.sel_enableCursorRects);
}

public void enableFlushWindow() {
	OS.objc_msgSend(this.id, OS.sel_enableFlushWindow);
}

public void enableKeyEquivalentForDefaultButtonCell() {
	OS.objc_msgSend(this.id, OS.sel_enableKeyEquivalentForDefaultButtonCell);
}

public void endEditingFor(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_endEditingFor_1, anObject != null ? anObject.id : 0);
}

public NSText fieldEditor(boolean createFlag, id anObject) {
	int result = OS.objc_msgSend(this.id, OS.sel_fieldEditor_1forObject_1, createFlag, anObject != null ? anObject.id : 0);
	return result != 0 ? new NSText(result) : null;
}

public NSResponder firstResponder() {
	int result = OS.objc_msgSend(this.id, OS.sel_firstResponder);
	return result != 0 ? new NSResponder(result) : null;
}

public void flushWindow() {
	OS.objc_msgSend(this.id, OS.sel_flushWindow);
}

public void flushWindowIfNeeded() {
	OS.objc_msgSend(this.id, OS.sel_flushWindowIfNeeded);
}

public NSRect frame() {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_frame);
	return result;
}

public NSString frameAutosaveName() {
	int result = OS.objc_msgSend(this.id, OS.sel_frameAutosaveName);
	return result != 0 ? new NSString(result) : null;
}

public NSRect frameRectForContentRect_(NSRect contentRect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_frameRectForContentRect_1, contentRect);
	return result;
}

public static NSRect static_frameRectForContentRect_styleMask_(NSRect cRect, int aStyle) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, OS.class_NSWindow, OS.sel_frameRectForContentRect_1styleMask_1, cRect, aStyle);
	return result;
}

public int gState() {
	return OS.objc_msgSend(this.id, OS.sel_gState);
}

public NSGraphicsContext graphicsContext() {
	int result = OS.objc_msgSend(this.id, OS.sel_graphicsContext);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public id handleCloseScriptCommand(NSCloseCommand command) {
	int result = OS.objc_msgSend(this.id, OS.sel_handleCloseScriptCommand_1, command != null ? command.id : 0);
	return result != 0 ? new id(result) : null;
}

public id handlePrintScriptCommand(NSScriptCommand command) {
	int result = OS.objc_msgSend(this.id, OS.sel_handlePrintScriptCommand_1, command != null ? command.id : 0);
	return result != 0 ? new id(result) : null;
}

public id handleSaveScriptCommand(NSScriptCommand command) {
	int result = OS.objc_msgSend(this.id, OS.sel_handleSaveScriptCommand_1, command != null ? command.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean hasCloseBox() {
	return OS.objc_msgSend(this.id, OS.sel_hasCloseBox) != 0;
}

public boolean hasDynamicDepthLimit() {
	return OS.objc_msgSend(this.id, OS.sel_hasDynamicDepthLimit) != 0;
}

public boolean hasShadow() {
	return OS.objc_msgSend(this.id, OS.sel_hasShadow) != 0;
}

public boolean hasTitleBar() {
	return OS.objc_msgSend(this.id, OS.sel_hasTitleBar) != 0;
}

public boolean hidesOnDeactivate() {
	return OS.objc_msgSend(this.id, OS.sel_hidesOnDeactivate) != 0;
}

public boolean ignoresMouseEvents() {
	return OS.objc_msgSend(this.id, OS.sel_ignoresMouseEvents) != 0;
}

public NSWindow initWithContentRect_styleMask_backing_defer_(NSRect contentRect, int aStyle, int bufferingType, boolean flag) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentRect_1styleMask_1backing_1defer_1, contentRect, aStyle, bufferingType, flag);
	return result != 0 ? this : null;
}

public NSWindow initWithContentRect_styleMask_backing_defer_screen_(NSRect contentRect, int aStyle, int bufferingType, boolean flag, NSScreen screen) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithContentRect_1styleMask_1backing_1defer_1screen_1, contentRect, aStyle, bufferingType, flag, screen != null ? screen.id : 0);
	return result != 0 ? this : null;
}

public NSWindow initWithWindowRef(int windowRef) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithWindowRef_1, windowRef);
	return result == this.id ? this : (result != 0 ? new NSWindow(result) : null);
}

public NSView initialFirstResponder() {
	int result = OS.objc_msgSend(this.id, OS.sel_initialFirstResponder);
	return result != 0 ? new NSView(result) : null;
}

public void invalidateCursorRectsForView(NSView aView) {
	OS.objc_msgSend(this.id, OS.sel_invalidateCursorRectsForView_1, aView != null ? aView.id : 0);
}

public void invalidateShadow() {
	OS.objc_msgSend(this.id, OS.sel_invalidateShadow);
}

public boolean isAutodisplay() {
	return OS.objc_msgSend(this.id, OS.sel_isAutodisplay) != 0;
}

public boolean isDocumentEdited() {
	return OS.objc_msgSend(this.id, OS.sel_isDocumentEdited) != 0;
}

public boolean isExcludedFromWindowsMenu() {
	return OS.objc_msgSend(this.id, OS.sel_isExcludedFromWindowsMenu) != 0;
}

public boolean isFloatingPanel() {
	return OS.objc_msgSend(this.id, OS.sel_isFloatingPanel) != 0;
}

public boolean isFlushWindowDisabled() {
	return OS.objc_msgSend(this.id, OS.sel_isFlushWindowDisabled) != 0;
}

public boolean isKeyWindow() {
	return OS.objc_msgSend(this.id, OS.sel_isKeyWindow) != 0;
}

public boolean isMainWindow() {
	return OS.objc_msgSend(this.id, OS.sel_isMainWindow) != 0;
}

public boolean isMiniaturizable() {
	return OS.objc_msgSend(this.id, OS.sel_isMiniaturizable) != 0;
}

public boolean isMiniaturized() {
	return OS.objc_msgSend(this.id, OS.sel_isMiniaturized) != 0;
}

public boolean isModalPanel() {
	return OS.objc_msgSend(this.id, OS.sel_isModalPanel) != 0;
}

public boolean isMovableByWindowBackground() {
	return OS.objc_msgSend(this.id, OS.sel_isMovableByWindowBackground) != 0;
}

public boolean isOneShot() {
	return OS.objc_msgSend(this.id, OS.sel_isOneShot) != 0;
}

public boolean isOpaque() {
	return OS.objc_msgSend(this.id, OS.sel_isOpaque) != 0;
}

public boolean isReleasedWhenClosed() {
	return OS.objc_msgSend(this.id, OS.sel_isReleasedWhenClosed) != 0;
}

public boolean isResizable() {
	return OS.objc_msgSend(this.id, OS.sel_isResizable) != 0;
}

public boolean isSheet() {
	return OS.objc_msgSend(this.id, OS.sel_isSheet) != 0;
}

public boolean isVisible() {
	return OS.objc_msgSend(this.id, OS.sel_isVisible) != 0;
}

public boolean isZoomable() {
	return OS.objc_msgSend(this.id, OS.sel_isZoomable) != 0;
}

public boolean isZoomed() {
	return OS.objc_msgSend(this.id, OS.sel_isZoomed) != 0;
}

public void keyDown(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_keyDown_1, theEvent != null ? theEvent.id : 0);
}

public int keyViewSelectionDirection() {
	return OS.objc_msgSend(this.id, OS.sel_keyViewSelectionDirection);
}

public int level() {
	return OS.objc_msgSend(this.id, OS.sel_level);
}

public boolean makeFirstResponder(NSResponder aResponder) {
	return OS.objc_msgSend(this.id, OS.sel_makeFirstResponder_1, aResponder != null ? aResponder.id : 0) != 0;
}

public void makeKeyAndOrderFront(id sender) {
	OS.objc_msgSend(this.id, OS.sel_makeKeyAndOrderFront_1, sender != null ? sender.id : 0);
}

public void makeKeyWindow() {
	OS.objc_msgSend(this.id, OS.sel_makeKeyWindow);
}

public void makeMainWindow() {
	OS.objc_msgSend(this.id, OS.sel_makeMainWindow);
}

public NSSize maxSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_maxSize);
	return result;
}

public static void menuChanged(NSMenu menu) {
	OS.objc_msgSend(OS.class_NSWindow, OS.sel_menuChanged_1, menu != null ? menu.id : 0);
}

public static float minFrameWidthWithTitle(NSString aTitle, int aStyle) {
	return (float)OS.objc_msgSend_fpret(OS.class_NSWindow, OS.sel_minFrameWidthWithTitle_1styleMask_1, aTitle != null ? aTitle.id : 0, aStyle);
}

public NSSize minSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_minSize);
	return result;
}

public void miniaturize(id sender) {
	OS.objc_msgSend(this.id, OS.sel_miniaturize_1, sender != null ? sender.id : 0);
}

public NSImage miniwindowImage() {
	int result = OS.objc_msgSend(this.id, OS.sel_miniwindowImage);
	return result != 0 ? new NSImage(result) : null;
}

public NSString miniwindowTitle() {
	int result = OS.objc_msgSend(this.id, OS.sel_miniwindowTitle);
	return result != 0 ? new NSString(result) : null;
}

public NSPoint mouseLocationOutsideOfEventStream() {
	NSPoint result = new NSPoint();
	OS.objc_msgSend_struct(result, this.id, OS.sel_mouseLocationOutsideOfEventStream);
	return result;
}

public NSEvent nextEventMatchingMask_(int mask) {
	int result = OS.objc_msgSend(this.id, OS.sel_nextEventMatchingMask_1, mask);
	return result != 0 ? new NSEvent(result) : null;
}

public NSEvent nextEventMatchingMask_untilDate_inMode_dequeue_(int mask, NSDate expiration, NSString mode, boolean deqFlag) {
	int result = OS.objc_msgSend(this.id, OS.sel_nextEventMatchingMask_1untilDate_1inMode_1dequeue_1, mask, expiration != null ? expiration.id : 0, mode != null ? mode.id : 0, deqFlag);
	return result != 0 ? new NSEvent(result) : null;
}

public void orderBack(id sender) {
	OS.objc_msgSend(this.id, OS.sel_orderBack_1, sender != null ? sender.id : 0);
}

public void orderFront(id sender) {
	OS.objc_msgSend(this.id, OS.sel_orderFront_1, sender != null ? sender.id : 0);
}

public void orderFrontRegardless() {
	OS.objc_msgSend(this.id, OS.sel_orderFrontRegardless);
}

public void orderOut(id sender) {
	OS.objc_msgSend(this.id, OS.sel_orderOut_1, sender != null ? sender.id : 0);
}

public void orderWindow(int place, int otherWin) {
	OS.objc_msgSend(this.id, OS.sel_orderWindow_1relativeTo_1, place, otherWin);
}

public int orderedIndex() {
	return OS.objc_msgSend(this.id, OS.sel_orderedIndex);
}

public NSWindow parentWindow() {
	int result = OS.objc_msgSend(this.id, OS.sel_parentWindow);
	return result == this.id ? this : (result != 0 ? new NSWindow(result) : null);
}

public void performClose(id sender) {
	OS.objc_msgSend(this.id, OS.sel_performClose_1, sender != null ? sender.id : 0);
}

public void performMiniaturize(id sender) {
	OS.objc_msgSend(this.id, OS.sel_performMiniaturize_1, sender != null ? sender.id : 0);
}

public void performZoom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_performZoom_1, sender != null ? sender.id : 0);
}

public void postEvent(NSEvent event, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_postEvent_1atStart_1, event != null ? event.id : 0, flag);
}

public int preferredBackingLocation() {
	return OS.objc_msgSend(this.id, OS.sel_preferredBackingLocation);
}

public boolean preservesContentDuringLiveResize() {
	return OS.objc_msgSend(this.id, OS.sel_preservesContentDuringLiveResize) != 0;
}

public void print(id sender) {
	OS.objc_msgSend(this.id, OS.sel_print_1, sender != null ? sender.id : 0);
}

public void recalculateKeyViewLoop() {
	OS.objc_msgSend(this.id, OS.sel_recalculateKeyViewLoop);
}

public void registerForDraggedTypes(NSArray newTypes) {
	OS.objc_msgSend(this.id, OS.sel_registerForDraggedTypes_1, newTypes != null ? newTypes.id : 0);
}

public void removeChildWindow(NSWindow childWin) {
	OS.objc_msgSend(this.id, OS.sel_removeChildWindow_1, childWin != null ? childWin.id : 0);
}

public static void removeFrameUsingName(NSString name) {
	OS.objc_msgSend(OS.class_NSWindow, OS.sel_removeFrameUsingName_1, name != null ? name.id : 0);
}

public NSString representedFilename() {
	int result = OS.objc_msgSend(this.id, OS.sel_representedFilename);
	return result != 0 ? new NSString(result) : null;
}

public NSURL representedURL() {
	int result = OS.objc_msgSend(this.id, OS.sel_representedURL);
	return result != 0 ? new NSURL(result) : null;
}

public void resetCursorRects() {
	OS.objc_msgSend(this.id, OS.sel_resetCursorRects);
}

public void resignKeyWindow() {
	OS.objc_msgSend(this.id, OS.sel_resignKeyWindow);
}

public void resignMainWindow() {
	OS.objc_msgSend(this.id, OS.sel_resignMainWindow);
}

public int resizeFlags() {
	return OS.objc_msgSend(this.id, OS.sel_resizeFlags);
}

public NSSize resizeIncrements() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_resizeIncrements);
	return result;
}

public void restoreCachedImage() {
	OS.objc_msgSend(this.id, OS.sel_restoreCachedImage);
}

public void runToolbarCustomizationPalette(id sender) {
	OS.objc_msgSend(this.id, OS.sel_runToolbarCustomizationPalette_1, sender != null ? sender.id : 0);
}

public void saveFrameUsingName(NSString name) {
	OS.objc_msgSend(this.id, OS.sel_saveFrameUsingName_1, name != null ? name.id : 0);
}

public NSScreen screen() {
	int result = OS.objc_msgSend(this.id, OS.sel_screen);
	return result != 0 ? new NSScreen(result) : null;
}

public void selectKeyViewFollowingView(NSView aView) {
	OS.objc_msgSend(this.id, OS.sel_selectKeyViewFollowingView_1, aView != null ? aView.id : 0);
}

public void selectKeyViewPrecedingView(NSView aView) {
	OS.objc_msgSend(this.id, OS.sel_selectKeyViewPrecedingView_1, aView != null ? aView.id : 0);
}

public void selectNextKeyView(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectNextKeyView_1, sender != null ? sender.id : 0);
}

public void selectPreviousKeyView(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectPreviousKeyView_1, sender != null ? sender.id : 0);
}

public void sendEvent(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_sendEvent_1, theEvent != null ? theEvent.id : 0);
}

public void setAcceptsMouseMovedEvents(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAcceptsMouseMovedEvents_1, flag);
}

public void setAllowsToolTipsWhenApplicationIsInactive(boolean allowWhenInactive) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsToolTipsWhenApplicationIsInactive_1, allowWhenInactive);
}

public void setAlphaValue(float windowAlpha) {
	OS.objc_msgSend(this.id, OS.sel_setAlphaValue_1, windowAlpha);
}

public void setAspectRatio(NSSize ratio) {
	OS.objc_msgSend(this.id, OS.sel_setAspectRatio_1, ratio);
}

public void setAutodisplay(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAutodisplay_1, flag);
}

public void setAutorecalculatesContentBorderThickness(boolean flag, int edge) {
	OS.objc_msgSend(this.id, OS.sel_setAutorecalculatesContentBorderThickness_1forEdge_1, flag, edge);
}

public void setAutorecalculatesKeyViewLoop(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAutorecalculatesKeyViewLoop_1, flag);
}

public void setBackgroundColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setBackgroundColor_1, color != null ? color.id : 0);
}

public void setBackingType(int bufferingType) {
	OS.objc_msgSend(this.id, OS.sel_setBackingType_1, bufferingType);
}

public void setCanBeVisibleOnAllSpaces(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setCanBeVisibleOnAllSpaces_1, flag);
}

public void setCanBecomeVisibleWithoutLogin(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setCanBecomeVisibleWithoutLogin_1, flag);
}

public void setCanHide(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setCanHide_1, flag);
}

public void setCollectionBehavior(int behavior) {
	OS.objc_msgSend(this.id, OS.sel_setCollectionBehavior_1, behavior);
}

public void setContentAspectRatio(NSSize ratio) {
	OS.objc_msgSend(this.id, OS.sel_setContentAspectRatio_1, ratio);
}

public void setContentBorderThickness(float thickness, int edge) {
	OS.objc_msgSend(this.id, OS.sel_setContentBorderThickness_1forEdge_1, thickness, edge);
}

public void setContentMaxSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setContentMaxSize_1, size);
}

public void setContentMinSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setContentMinSize_1, size);
}

public void setContentResizeIncrements(NSSize increments) {
	OS.objc_msgSend(this.id, OS.sel_setContentResizeIncrements_1, increments);
}

public void setContentSize(NSSize aSize) {
	OS.objc_msgSend(this.id, OS.sel_setContentSize_1, aSize);
}

public void setContentView(NSView aView) {
	OS.objc_msgSend(this.id, OS.sel_setContentView_1, aView != null ? aView.id : 0);
}

public void setDefaultButtonCell(NSButtonCell defButt) {
	OS.objc_msgSend(this.id, OS.sel_setDefaultButtonCell_1, defButt != null ? defButt.id : 0);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

public void setDepthLimit(int limit) {
	OS.objc_msgSend(this.id, OS.sel_setDepthLimit_1, limit);
}

public void setDisplaysWhenScreenProfileChanges(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDisplaysWhenScreenProfileChanges_1, flag);
}

public void setDocumentEdited(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDocumentEdited_1, flag);
}

public void setDynamicDepthLimit(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setDynamicDepthLimit_1, flag);
}

public void setExcludedFromWindowsMenu(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setExcludedFromWindowsMenu_1, flag);
}

public void setFrame_display_(NSRect frameRect, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setFrame_1display_1, frameRect, flag);
}

public void setFrame_display_animate_(NSRect frameRect, boolean displayFlag, boolean animateFlag) {
	OS.objc_msgSend(this.id, OS.sel_setFrame_1display_1animate_1, frameRect, displayFlag, animateFlag);
}

public boolean setFrameAutosaveName(NSString name) {
	return OS.objc_msgSend(this.id, OS.sel_setFrameAutosaveName_1, name != null ? name.id : 0) != 0;
}

public void setFrameFromString(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setFrameFromString_1, string != null ? string.id : 0);
}

public void setFrameOrigin(NSPoint aPoint) {
	OS.objc_msgSend(this.id, OS.sel_setFrameOrigin_1, aPoint);
}

public void setFrameTopLeftPoint(NSPoint aPoint) {
	OS.objc_msgSend(this.id, OS.sel_setFrameTopLeftPoint_1, aPoint);
}

public boolean setFrameUsingName_(NSString name) {
	return OS.objc_msgSend(this.id, OS.sel_setFrameUsingName_1, name != null ? name.id : 0) != 0;
}

public boolean setFrameUsingName_force_(NSString name, boolean force) {
	return OS.objc_msgSend(this.id, OS.sel_setFrameUsingName_1force_1, name != null ? name.id : 0, force) != 0;
}

public void setHasShadow(boolean hasShadow) {
	OS.objc_msgSend(this.id, OS.sel_setHasShadow_1, hasShadow);
}

public void setHidesOnDeactivate(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setHidesOnDeactivate_1, flag);
}

public void setIgnoresMouseEvents(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setIgnoresMouseEvents_1, flag);
}

public void setInitialFirstResponder(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setInitialFirstResponder_1, view != null ? view.id : 0);
}

public void setIsMiniaturized(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setIsMiniaturized_1, flag);
}

public void setIsVisible(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setIsVisible_1, flag);
}

public void setIsZoomed(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setIsZoomed_1, flag);
}

public void setLevel(int newLevel) {
	OS.objc_msgSend(this.id, OS.sel_setLevel_1, newLevel);
}

public void setMaxSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setMaxSize_1, size);
}

public void setMinSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setMinSize_1, size);
}

public void setMiniwindowImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setMiniwindowImage_1, image != null ? image.id : 0);
}

public void setMiniwindowTitle(NSString title) {
	OS.objc_msgSend(this.id, OS.sel_setMiniwindowTitle_1, title != null ? title.id : 0);
}

public void setMovableByWindowBackground(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setMovableByWindowBackground_1, flag);
}

public void setOneShot(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setOneShot_1, flag);
}

public void setOpaque(boolean isOpaque) {
	OS.objc_msgSend(this.id, OS.sel_setOpaque_1, isOpaque);
}

public void setOrderedIndex(int index) {
	OS.objc_msgSend(this.id, OS.sel_setOrderedIndex_1, index);
}

public void setParentWindow(NSWindow window) {
	OS.objc_msgSend(this.id, OS.sel_setParentWindow_1, window != null ? window.id : 0);
}

public void setPreferredBackingLocation(int backingLocation) {
	OS.objc_msgSend(this.id, OS.sel_setPreferredBackingLocation_1, backingLocation);
}

public void setPreservesContentDuringLiveResize(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setPreservesContentDuringLiveResize_1, flag);
}

public void setReleasedWhenClosed(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setReleasedWhenClosed_1, flag);
}

public void setRepresentedFilename(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setRepresentedFilename_1, aString != null ? aString.id : 0);
}

public void setRepresentedURL(NSURL url) {
	OS.objc_msgSend(this.id, OS.sel_setRepresentedURL_1, url != null ? url.id : 0);
}

public void setResizeIncrements(NSSize increments) {
	OS.objc_msgSend(this.id, OS.sel_setResizeIncrements_1, increments);
}

public void setSharingType(int type) {
	OS.objc_msgSend(this.id, OS.sel_setSharingType_1, type);
}

public void setShowsResizeIndicator(boolean show) {
	OS.objc_msgSend(this.id, OS.sel_setShowsResizeIndicator_1, show);
}

public void setShowsToolbarButton(boolean show) {
	OS.objc_msgSend(this.id, OS.sel_setShowsToolbarButton_1, show);
}

public void setTitle(NSString aString) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_1, aString != null ? aString.id : 0);
}

public void setTitleWithRepresentedFilename(NSString filename) {
	OS.objc_msgSend(this.id, OS.sel_setTitleWithRepresentedFilename_1, filename != null ? filename.id : 0);
}

public void setToolbar(NSToolbar toolbar) {
	OS.objc_msgSend(this.id, OS.sel_setToolbar_1, toolbar != null ? toolbar.id : 0);
}

public void setViewsNeedDisplay(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setViewsNeedDisplay_1, flag);
}

public void setWindowController(NSWindowController windowController) {
	OS.objc_msgSend(this.id, OS.sel_setWindowController_1, windowController != null ? windowController.id : 0);
}

public int sharingType() {
	return OS.objc_msgSend(this.id, OS.sel_sharingType);
}

public boolean showsResizeIndicator() {
	return OS.objc_msgSend(this.id, OS.sel_showsResizeIndicator) != 0;
}

public boolean showsToolbarButton() {
	return OS.objc_msgSend(this.id, OS.sel_showsToolbarButton) != 0;
}

public NSButton standardWindowButton_(int b) {
	int result = OS.objc_msgSend(this.id, OS.sel_standardWindowButton_1, b);
	return result != 0 ? new NSButton(result) : null;
}

public static NSButton static_standardWindowButton_forStyleMask_(int b, int styleMask) {
	int result = OS.objc_msgSend(OS.class_NSWindow, OS.sel_standardWindowButton_1forStyleMask_1, b, styleMask);
	return result != 0 ? new NSButton(result) : null;
}

public NSString stringWithSavedFrame() {
	int result = OS.objc_msgSend(this.id, OS.sel_stringWithSavedFrame);
	return result != 0 ? new NSString(result) : null;
}

public int styleMask() {
	return OS.objc_msgSend(this.id, OS.sel_styleMask);
}

public NSString title() {
	int result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

public void toggleToolbarShown(id sender) {
	OS.objc_msgSend(this.id, OS.sel_toggleToolbarShown_1, sender != null ? sender.id : 0);
}

public NSToolbar toolbar() {
	int result = OS.objc_msgSend(this.id, OS.sel_toolbar);
	return result != 0 ? new NSToolbar(result) : null;
}

public boolean tryToPerform(int anAction, id anObject) {
	return OS.objc_msgSend(this.id, OS.sel_tryToPerform_1with_1, anAction, anObject != null ? anObject.id : 0) != 0;
}

public void unregisterDraggedTypes() {
	OS.objc_msgSend(this.id, OS.sel_unregisterDraggedTypes);
}

public void update() {
	OS.objc_msgSend(this.id, OS.sel_update);
}

public void useOptimizedDrawing(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_useOptimizedDrawing_1, flag);
}

public float userSpaceScaleFactor() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_userSpaceScaleFactor);
}

public id validRequestorForSendType(NSString sendType, NSString returnType) {
	int result = OS.objc_msgSend(this.id, OS.sel_validRequestorForSendType_1returnType_1, sendType != null ? sendType.id : 0, returnType != null ? returnType.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean viewsNeedDisplay() {
	return OS.objc_msgSend(this.id, OS.sel_viewsNeedDisplay) != 0;
}

public id windowController() {
	int result = OS.objc_msgSend(this.id, OS.sel_windowController);
	return result != 0 ? new id(result) : null;
}

public int windowNumber() {
	return OS.objc_msgSend(this.id, OS.sel_windowNumber);
}

public int windowRef() {
	return OS.objc_msgSend(this.id, OS.sel_windowRef);
}

public boolean worksWhenModal() {
	return OS.objc_msgSend(this.id, OS.sel_worksWhenModal) != 0;
}

public void zoom(id sender) {
	OS.objc_msgSend(this.id, OS.sel_zoom_1, sender != null ? sender.id : 0);
}

}
