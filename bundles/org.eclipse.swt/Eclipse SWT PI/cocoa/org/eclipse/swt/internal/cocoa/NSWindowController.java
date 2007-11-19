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

public class NSWindowController extends NSResponder {

public NSWindowController() {
	super();
}

public NSWindowController(int id) {
	super(id);
}

public void close() {
	OS.objc_msgSend(this.id, OS.sel_close);
}

public id document() {
	int result = OS.objc_msgSend(this.id, OS.sel_document);
	return result != 0 ? new id(result) : null;
}

public id initWithWindow(NSWindow window) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithWindow_1, window != null ? window.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithWindowNibName_(NSString windowNibName) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithWindowNibName_1, windowNibName != null ? windowNibName.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithWindowNibName_owner_(NSString windowNibName, id owner) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithWindowNibName_1owner_1, windowNibName != null ? windowNibName.id : 0, owner != null ? owner.id : 0);
	return result != 0 ? new id(result) : null;
}

public id initWithWindowNibPath(NSString windowNibPath, id owner) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithWindowNibPath_1owner_1, windowNibPath != null ? windowNibPath.id : 0, owner != null ? owner.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isWindowLoaded() {
	return OS.objc_msgSend(this.id, OS.sel_isWindowLoaded) != 0;
}

public void loadWindow() {
	OS.objc_msgSend(this.id, OS.sel_loadWindow);
}

public id owner() {
	int result = OS.objc_msgSend(this.id, OS.sel_owner);
	return result != 0 ? new id(result) : null;
}

public void setDocument(NSDocument document) {
	OS.objc_msgSend(this.id, OS.sel_setDocument_1, document != null ? document.id : 0);
}

public void setDocumentEdited(boolean dirtyFlag) {
	OS.objc_msgSend(this.id, OS.sel_setDocumentEdited_1, dirtyFlag);
}

public void setShouldCascadeWindows(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setShouldCascadeWindows_1, flag);
}

public void setShouldCloseDocument(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setShouldCloseDocument_1, flag);
}

public void setWindow(NSWindow window) {
	OS.objc_msgSend(this.id, OS.sel_setWindow_1, window != null ? window.id : 0);
}

public void setWindowFrameAutosaveName(NSString name) {
	OS.objc_msgSend(this.id, OS.sel_setWindowFrameAutosaveName_1, name != null ? name.id : 0);
}

public boolean shouldCascadeWindows() {
	return OS.objc_msgSend(this.id, OS.sel_shouldCascadeWindows) != 0;
}

public boolean shouldCloseDocument() {
	return OS.objc_msgSend(this.id, OS.sel_shouldCloseDocument) != 0;
}

public void showWindow(id sender) {
	OS.objc_msgSend(this.id, OS.sel_showWindow_1, sender != null ? sender.id : 0);
}

public void synchronizeWindowTitleWithDocumentName() {
	OS.objc_msgSend(this.id, OS.sel_synchronizeWindowTitleWithDocumentName);
}

public NSWindow window() {
	int result = OS.objc_msgSend(this.id, OS.sel_window);
	return result != 0 ? new NSWindow(result) : null;
}

public void windowDidLoad() {
	OS.objc_msgSend(this.id, OS.sel_windowDidLoad);
}

public NSString windowFrameAutosaveName() {
	int result = OS.objc_msgSend(this.id, OS.sel_windowFrameAutosaveName);
	return result != 0 ? new NSString(result) : null;
}

public NSString windowNibName() {
	int result = OS.objc_msgSend(this.id, OS.sel_windowNibName);
	return result != 0 ? new NSString(result) : null;
}

public NSString windowNibPath() {
	int result = OS.objc_msgSend(this.id, OS.sel_windowNibPath);
	return result != 0 ? new NSString(result) : null;
}

public NSString windowTitleForDocumentDisplayName(NSString displayName) {
	int result = OS.objc_msgSend(this.id, OS.sel_windowTitleForDocumentDisplayName_1, displayName != null ? displayName.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public void windowWillLoad() {
	OS.objc_msgSend(this.id, OS.sel_windowWillLoad);
}

}
