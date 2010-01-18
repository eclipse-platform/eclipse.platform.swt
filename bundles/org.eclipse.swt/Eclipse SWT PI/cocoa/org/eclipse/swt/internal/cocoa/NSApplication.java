/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSApplication extends NSResponder {

public NSApplication() {
	super();
}

public NSApplication(int /*long*/ id) {
	super(id);
}

public NSApplication(id id) {
	super(id);
}

public void activateIgnoringOtherApps(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_activateIgnoringOtherApps_, flag);
}

public void beginSheet(NSWindow sheet, NSWindow docWindow, id modalDelegate, int /*long*/ didEndSelector, int /*long*/ contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_beginSheet_modalForWindow_modalDelegate_didEndSelector_contextInfo_, sheet != null ? sheet.id : 0, docWindow != null ? docWindow.id : 0, modalDelegate != null ? modalDelegate.id : 0, didEndSelector, contextInfo);
}

public NSEvent currentEvent() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_currentEvent);
	return result != 0 ? new NSEvent(result) : null;
}

public void endSheet(NSWindow sheet, int /*long*/ returnCode) {
	OS.objc_msgSend(this.id, OS.sel_endSheet_returnCode_, sheet != null ? sheet.id : 0, returnCode);
}

public void finishLaunching() {
	OS.objc_msgSend(this.id, OS.sel_finishLaunching);
}

public void hide(id sender) {
	OS.objc_msgSend(this.id, OS.sel_hide_, sender != null ? sender.id : 0);
}

public void hideOtherApplications(id sender) {
	OS.objc_msgSend(this.id, OS.sel_hideOtherApplications_, sender != null ? sender.id : 0);
}

public boolean isActive() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isActive);
}

public boolean isRunning() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isRunning);
}

public NSWindow keyWindow() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_keyWindow);
	return result != 0 ? new NSWindow(result) : null;
}

public NSMenu mainMenu() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_mainMenu);
	return result != 0 ? new NSMenu(result) : null;
}

public NSEvent nextEventMatchingMask(int /*long*/ mask, NSDate expiration, NSString mode, boolean deqFlag) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_nextEventMatchingMask_untilDate_inMode_dequeue_, mask, expiration != null ? expiration.id : 0, mode != null ? mode.id : 0, deqFlag);
	return result != 0 ? new NSEvent(result) : null;
}

public void orderFrontStandardAboutPanel(id sender) {
	OS.objc_msgSend(this.id, OS.sel_orderFrontStandardAboutPanel_, sender != null ? sender.id : 0);
}

public NSArray orderedWindows() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_orderedWindows);
	return result != 0 ? new NSArray(result) : null;
}

public void postEvent(NSEvent event, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_postEvent_atStart_, event != null ? event.id : 0, flag);
}

public void replyToOpenOrPrint(int /*long*/ reply) {
	OS.objc_msgSend(this.id, OS.sel_replyToOpenOrPrint_, reply);
}

public void run() {
	OS.objc_msgSend(this.id, OS.sel_run);
}

public int /*long*/ runModalForWindow(NSWindow theWindow) {
	return OS.objc_msgSend(this.id, OS.sel_runModalForWindow_, theWindow != null ? theWindow.id : 0);
}

public void sendEvent(NSEvent theEvent) {
	OS.objc_msgSend(this.id, OS.sel_sendEvent_, theEvent != null ? theEvent.id : 0);
}

public void setApplicationIconImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setApplicationIconImage_, image != null ? image.id : 0);
}

public void setDelegate(id anObject) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_, anObject != null ? anObject.id : 0);
}

public void setMainMenu(NSMenu aMenu) {
	OS.objc_msgSend(this.id, OS.sel_setMainMenu_, aMenu != null ? aMenu.id : 0);
}

public void setServicesMenu(NSMenu aMenu) {
	OS.objc_msgSend(this.id, OS.sel_setServicesMenu_, aMenu != null ? aMenu.id : 0);
}

public static NSApplication sharedApplication() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSApplication, OS.sel_sharedApplication);
	return result != 0 ? new NSApplication(result) : null;
}

public void stop(id sender) {
	OS.objc_msgSend(this.id, OS.sel_stop_, sender != null ? sender.id : 0);
}

public void terminate(id sender) {
	OS.objc_msgSend(this.id, OS.sel_terminate_, sender != null ? sender.id : 0);
}

public void unhideAllApplications(id sender) {
	OS.objc_msgSend(this.id, OS.sel_unhideAllApplications_, sender != null ? sender.id : 0);
}

public NSArray windows() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_windows);
	return result != 0 ? new NSArray(result) : null;
}

}
