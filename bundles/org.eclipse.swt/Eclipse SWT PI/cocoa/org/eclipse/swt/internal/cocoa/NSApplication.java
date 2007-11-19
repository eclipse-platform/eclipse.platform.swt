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

public class NSApplication extends NSResponder {

public NSApplication() {
	super();
}

public NSApplication(int id) {
	super(id);
}

public void abortModal() {
	OS.objc_msgSend(this.id, OS.sel_abortModal);
}

public void activateContextHelpMode(id sender) {
	OS.objc_msgSend(this.id, OS.sel_activateContextHelpMode_1, sender != null ? sender.id : 0);
}

public void activateIgnoringOtherApps(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_activateIgnoringOtherApps_1, flag);
}

public void addWindowsItem(NSWindow win, NSString aString, boolean isFilename) {
	OS.objc_msgSend(this.id, OS.sel_addWindowsItem_1title_1filename_1, win != null ? win.id : 0, aString != null ? aString.id : 0, isFilename);
}

public NSImage applicationIconImage() {
	int result = OS.objc_msgSend(this.id, OS.sel_applicationIconImage);
	return result != 0 ? new NSImage(result) : null;
}

public void arrangeInFront(id sender) {
	OS.objc_msgSend(this.id, OS.sel_arrangeInFront_1, sender != null ? sender.id : 0);
}

public int beginModalSessionForWindow_(NSWindow theWindow) {
	return OS.objc_msgSend(this.id, OS.sel_beginModalSessionForWindow_1, theWindow != null ? theWindow.id : 0);
}

public int beginModalSessionForWindow_relativeToWindow_(NSWindow theWindow, NSWindow docWindow) {
	return OS.objc_msgSend(this.id, OS.sel_beginModalSessionForWindow_1relativeToWindow_1, theWindow != null ? theWindow.id : 0, docWindow != null ? docWindow.id : 0);
}

public void beginSheet(NSWindow sheet, NSWindow docWindow, id modalDelegate, int didEndSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_beginSheet_1modalForWindow_1modalDelegate_1didEndSelector_1contextInfo_1, sheet != null ? sheet.id : 0, docWindow != null ? docWindow.id : 0, modalDelegate != null ? modalDelegate.id : 0, didEndSelector, contextInfo);
}

public void cancelUserAttentionRequest(int request) {
	OS.objc_msgSend(this.id, OS.sel_cancelUserAttentionRequest_1, request);
}

public void changeWindowsItem(NSWindow win, NSString aString, boolean isFilename) {
	OS.objc_msgSend(this.id, OS.sel_changeWindowsItem_1title_1filename_1, win != null ? win.id : 0, aString != null ? aString.id : 0, isFilename);
}

public NSGraphicsContext context() {
	int result = OS.objc_msgSend(this.id, OS.sel_context);
	return result != 0 ? new NSGraphicsContext(result) : null;
}

public NSEvent currentEvent() {
	int result = OS.objc_msgSend(this.id, OS.sel_currentEvent);
	return result != 0 ? new NSEvent(result) : null;
}

public void deactivate() {
	OS.objc_msgSend(this.id, OS.sel_deactivate);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public static void detachDrawingThread(int selector, id target, id argument) {
	OS.objc_msgSend(OS.class_NSApplication, OS.sel_detachDrawingThread_1toTarget_1withObject_1, selector, target != null ? target.id : 0, argument != null ? argument.id : 0);
}

public void discardEventsMatchingMask(int mask, NSEvent lastEvent) {
	OS.objc_msgSend(this.id, OS.sel_discardEventsMatchingMask_1beforeEvent_1, mask, lastEvent != null ? lastEvent.id : 0);
}

public NSDockTile dockTile() {
	int result = OS.objc_msgSend(this.id, OS.sel_dockTile);
	return result != 0 ? new NSDockTile(result) : null;
}

public void endModalSession(int session) {
	OS.objc_msgSend(this.id, OS.sel_endModalSession_1, session);
}

public void endSheet_(NSWindow sheet) {
	OS.objc_msgSend(this.id, OS.sel_endSheet_1, sheet != null ? sheet.id : 0);
}

public void endSheet_returnCode_(NSWindow sheet, int returnCode) {
	OS.objc_msgSend(this.id, OS.sel_endSheet_1returnCode_1, sheet != null ? sheet.id : 0, returnCode);
}

public void finishLaunching() {
	OS.objc_msgSend(this.id, OS.sel_finishLaunching);
}

public void hide(id sender) {
	OS.objc_msgSend(this.id, OS.sel_hide_1, sender != null ? sender.id : 0);
}

public void hideOtherApplications(id sender) {
	OS.objc_msgSend(this.id, OS.sel_hideOtherApplications_1, sender != null ? sender.id : 0);
}

public boolean isActive() {
	return OS.objc_msgSend(this.id, OS.sel_isActive) != 0;
}

public boolean isHidden() {
	return OS.objc_msgSend(this.id, OS.sel_isHidden) != 0;
}

public boolean isRunning() {
	return OS.objc_msgSend(this.id, OS.sel_isRunning) != 0;
}

public NSWindow keyWindow() {
	int result = OS.objc_msgSend(this.id, OS.sel_keyWindow);
	return result != 0 ? new NSWindow(result) : null;
}

public NSMenu mainMenu() {
	int result = OS.objc_msgSend(this.id, OS.sel_mainMenu);
	return result != 0 ? new NSMenu(result) : null;
}

public NSWindow mainWindow() {
	int result = OS.objc_msgSend(this.id, OS.sel_mainWindow);
	return result != 0 ? new NSWindow(result) : null;
}

public NSWindow makeWindowsPerform(int aSelector, boolean flag) {
	int result = OS.objc_msgSend(this.id, OS.sel_makeWindowsPerform_1inOrder_1, aSelector, flag);
	return result != 0 ? new NSWindow(result) : null;
}

public void miniaturizeAll(id sender) {
	OS.objc_msgSend(this.id, OS.sel_miniaturizeAll_1, sender != null ? sender.id : 0);
}

public NSWindow modalWindow() {
	int result = OS.objc_msgSend(this.id, OS.sel_modalWindow);
	return result != 0 ? new NSWindow(result) : null;
}

public NSEvent nextEventMatchingMask(int mask, NSDate expiration, NSString mode, boolean deqFlag) {
	int result = OS.objc_msgSend(this.id, OS.sel_nextEventMatchingMask_1untilDate_1inMode_1dequeue_1, mask, expiration != null ? expiration.id : 0, mode != null ? mode.id : 0, deqFlag);
	return result != 0 ? new NSEvent(result) : null;
}

public void orderFrontCharacterPalette(id sender) {
	OS.objc_msgSend(this.id, OS.sel_orderFrontCharacterPalette_1, sender != null ? sender.id : 0);
}

public void orderFrontColorPanel(id sender) {
	OS.objc_msgSend(this.id, OS.sel_orderFrontColorPanel_1, sender != null ? sender.id : 0);
}

public void orderFrontStandardAboutPanel(id sender) {
	OS.objc_msgSend(this.id, OS.sel_orderFrontStandardAboutPanel_1, sender != null ? sender.id : 0);
}

public void orderFrontStandardAboutPanelWithOptions(NSDictionary optionsDictionary) {
	OS.objc_msgSend(this.id, OS.sel_orderFrontStandardAboutPanelWithOptions_1, optionsDictionary != null ? optionsDictionary.id : 0);
}

public NSArray orderedDocuments() {
	int result = OS.objc_msgSend(this.id, OS.sel_orderedDocuments);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray orderedWindows() {
	int result = OS.objc_msgSend(this.id, OS.sel_orderedWindows);
	return result != 0 ? new NSArray(result) : null;
}

public void postEvent(NSEvent event, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_postEvent_1atStart_1, event != null ? event.id : 0, flag);
}

public void preventWindowOrdering() {
	OS.objc_msgSend(this.id, OS.sel_preventWindowOrdering);
}

public void registerServicesMenuSendTypes(NSArray sendTypes, NSArray returnTypes) {
	OS.objc_msgSend(this.id, OS.sel_registerServicesMenuSendTypes_1returnTypes_1, sendTypes != null ? sendTypes.id : 0, returnTypes != null ? returnTypes.id : 0);
}

public void removeWindowsItem(NSWindow win) {
	OS.objc_msgSend(this.id, OS.sel_removeWindowsItem_1, win != null ? win.id : 0);
}

public void replyToApplicationShouldTerminate(boolean shouldTerminate) {
	OS.objc_msgSend(this.id, OS.sel_replyToApplicationShouldTerminate_1, shouldTerminate);
}

public void replyToOpenOrPrint(int reply) {
	OS.objc_msgSend(this.id, OS.sel_replyToOpenOrPrint_1, reply);
}

public void reportException(NSException theException) {
	OS.objc_msgSend(this.id, OS.sel_reportException_1, theException != null ? theException.id : 0);
}

public int requestUserAttention(int requestType) {
	return OS.objc_msgSend(this.id, OS.sel_requestUserAttention_1, requestType);
}

public void run() {
	OS.objc_msgSend(this.id, OS.sel_run);
}

public int runModalForWindow_(NSWindow theWindow) {
	return OS.objc_msgSend(this.id, OS.sel_runModalForWindow_1, theWindow != null ? theWindow.id : 0);
}

public int runModalForWindow_relativeToWindow_(NSWindow theWindow, NSWindow docWindow) {
	return OS.objc_msgSend(this.id, OS.sel_runModalForWindow_1relativeToWindow_1, theWindow != null ? theWindow.id : 0, docWindow != null ? docWindow.id : 0);
}

public int runModalSession(int session) {
	return OS.objc_msgSend(this.id, OS.sel_runModalSession_1, session);
}

public void runPageLayout(id sender) {
	OS.objc_msgSend(this.id, OS.sel_runPageLayout_1, sender != null ? sender.id : 0);
}

public boolean sendAction(int theAction, id theTarget, id sender) {
	return OS.objc_msgSend(this.id, OS.sel_sendAction_1to_1from_1, theAction, theTarget != null ? theTarget.id : 0, sender != null ? sender.id : 0) != 0;
}

public void sendEvent(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_sendEvent_1, theEvent != null ? theEvent.id : 0);
}

public NSMenu servicesMenu() {
	int result = OS.objc_msgSend(this.id, OS.sel_servicesMenu);
	return result != 0 ? new NSMenu(result) : null;
}

public id servicesProvider() {
	int result = OS.objc_msgSend(this.id, OS.sel_servicesProvider);
	return result != 0 ? new id(result) : null;
}

public void setApplicationIconImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setApplicationIconImage_1, image != null ? image.id : 0);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, anObject != null ? anObject.id : 0);
}

public void setMainMenu(NSMenu aMenu) {
	OS.objc_msgSend(this.id, OS.sel_setMainMenu_1, aMenu != null ? aMenu.id : 0);
}

public void setServicesMenu(NSMenu aMenu) {
	OS.objc_msgSend(this.id, OS.sel_setServicesMenu_1, aMenu != null ? aMenu.id : 0);
}

public void setServicesProvider(id provider) {
	OS.objc_msgSend(this.id, OS.sel_setServicesProvider_1, provider != null ? provider.id : 0);
}

public void setWindowsMenu(NSMenu aMenu) {
	OS.objc_msgSend(this.id, OS.sel_setWindowsMenu_1, aMenu != null ? aMenu.id : 0);
}

public void setWindowsNeedUpdate(boolean needUpdate) {
	OS.objc_msgSend(this.id, OS.sel_setWindowsNeedUpdate_1, needUpdate);
}

public static NSApplication sharedApplication() {
	int result = OS.objc_msgSend(OS.class_NSApplication, OS.sel_sharedApplication);
	return result != 0 ? new NSApplication(result) : null;
}

public void showHelp(id sender) {
	OS.objc_msgSend(this.id, OS.sel_showHelp_1, sender != null ? sender.id : 0);
}

public void stop(id sender) {
	OS.objc_msgSend(this.id, OS.sel_stop_1, sender != null ? sender.id : 0);
}

public void stopModal() {
	OS.objc_msgSend(this.id, OS.sel_stopModal);
}

public void stopModalWithCode(int returnCode) {
	OS.objc_msgSend(this.id, OS.sel_stopModalWithCode_1, returnCode);
}

public id targetForAction_(int theAction) {
	int result = OS.objc_msgSend(this.id, OS.sel_targetForAction_1, theAction);
	return result != 0 ? new id(result) : null;
}

public id targetForAction_to_from_(int theAction, id theTarget, id sender) {
	int result = OS.objc_msgSend(this.id, OS.sel_targetForAction_1to_1from_1, theAction, theTarget != null ? theTarget.id : 0, sender != null ? sender.id : 0);
	return result != 0 ? new id(result) : null;
}

public void terminate(id sender) {
	OS.objc_msgSend(this.id, OS.sel_terminate_1, sender != null ? sender.id : 0);
}

public boolean tryToPerform(int anAction, id anObject) {
	return OS.objc_msgSend(this.id, OS.sel_tryToPerform_1with_1, anAction, anObject != null ? anObject.id : 0) != 0;
}

public void unhide(id sender) {
	OS.objc_msgSend(this.id, OS.sel_unhide_1, sender != null ? sender.id : 0);
}

public void unhideAllApplications(id sender) {
	OS.objc_msgSend(this.id, OS.sel_unhideAllApplications_1, sender != null ? sender.id : 0);
}

public void unhideWithoutActivation() {
	OS.objc_msgSend(this.id, OS.sel_unhideWithoutActivation);
}

public void updateWindows() {
	OS.objc_msgSend(this.id, OS.sel_updateWindows);
}

public void updateWindowsItem(NSWindow win) {
	OS.objc_msgSend(this.id, OS.sel_updateWindowsItem_1, win != null ? win.id : 0);
}

public id validRequestorForSendType(NSString sendType, NSString returnType) {
	int result = OS.objc_msgSend(this.id, OS.sel_validRequestorForSendType_1returnType_1, sendType != null ? sendType.id : 0, returnType != null ? returnType.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSWindow windowWithWindowNumber(int windowNum) {
	int result = OS.objc_msgSend(this.id, OS.sel_windowWithWindowNumber_1, windowNum);
	return result != 0 ? new NSWindow(result) : null;
}

public NSArray windows() {
	int result = OS.objc_msgSend(this.id, OS.sel_windows);
	return result != 0 ? new NSArray(result) : null;
}

public NSMenu windowsMenu() {
	int result = OS.objc_msgSend(this.id, OS.sel_windowsMenu);
	return result != 0 ? new NSMenu(result) : null;
}

}
