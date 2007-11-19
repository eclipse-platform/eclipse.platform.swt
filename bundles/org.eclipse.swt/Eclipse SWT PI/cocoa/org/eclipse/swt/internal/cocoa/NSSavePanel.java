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

public class NSSavePanel extends NSPanel {

public NSSavePanel() {
	super();
}

public NSSavePanel(int id) {
	super(id);
}

public NSURL URL() {
	int result = OS.objc_msgSend(this.id, OS.sel_URL);
	return result != 0 ? new NSURL(result) : null;
}

public NSView accessoryView() {
	int result = OS.objc_msgSend(this.id, OS.sel_accessoryView);
	return result != 0 ? new NSView(result) : null;
}

public NSArray allowedFileTypes() {
	int result = OS.objc_msgSend(this.id, OS.sel_allowedFileTypes);
	return result != 0 ? new NSArray(result) : null;
}

public boolean allowsOtherFileTypes() {
	return OS.objc_msgSend(this.id, OS.sel_allowsOtherFileTypes) != 0;
}

public void beginSheetForDirectory(NSString path, NSString name, NSWindow docWindow, id delegate, int didEndSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_beginSheetForDirectory_1file_1modalForWindow_1modalDelegate_1didEndSelector_1contextInfo_1, path != null ? path.id : 0, name != null ? name.id : 0, docWindow != null ? docWindow.id : 0, delegate != null ? delegate.id : 0, didEndSelector, contextInfo);
}

public boolean canCreateDirectories() {
	return OS.objc_msgSend(this.id, OS.sel_canCreateDirectories) != 0;
}

public boolean canSelectHiddenExtension() {
	return OS.objc_msgSend(this.id, OS.sel_canSelectHiddenExtension) != 0;
}

public void cancel(id sender) {
	OS.objc_msgSend(this.id, OS.sel_cancel_1, sender != null ? sender.id : 0);
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public NSString directory() {
	int result = OS.objc_msgSend(this.id, OS.sel_directory);
	return result != 0 ? new NSString(result) : null;
}

public NSString filename() {
	int result = OS.objc_msgSend(this.id, OS.sel_filename);
	return result != 0 ? new NSString(result) : null;
}

public boolean isExpanded() {
	return OS.objc_msgSend(this.id, OS.sel_isExpanded) != 0;
}

public boolean isExtensionHidden() {
	return OS.objc_msgSend(this.id, OS.sel_isExtensionHidden) != 0;
}

public NSString message() {
	int result = OS.objc_msgSend(this.id, OS.sel_message);
	return result != 0 ? new NSString(result) : null;
}

public NSString nameFieldLabel() {
	int result = OS.objc_msgSend(this.id, OS.sel_nameFieldLabel);
	return result != 0 ? new NSString(result) : null;
}

public void ok(id sender) {
	OS.objc_msgSend(this.id, OS.sel_ok_1, sender != null ? sender.id : 0);
}

public NSString prompt() {
	int result = OS.objc_msgSend(this.id, OS.sel_prompt);
	return result != 0 ? new NSString(result) : null;
}

public NSString requiredFileType() {
	int result = OS.objc_msgSend(this.id, OS.sel_requiredFileType);
	return result != 0 ? new NSString(result) : null;
}

public int runModal() {
	return OS.objc_msgSend(this.id, OS.sel_runModal);
}

public int runModalForDirectory(NSString path, NSString name) {
	return OS.objc_msgSend(this.id, OS.sel_runModalForDirectory_1file_1, path != null ? path.id : 0, name != null ? name.id : 0);
}

public static NSSavePanel savePanel() {
	int result = OS.objc_msgSend(OS.class_NSSavePanel, OS.sel_savePanel);
	return result != 0 ? new NSSavePanel(result) : null;
}

public void selectText(id sender) {
	OS.objc_msgSend(this.id, OS.sel_selectText_1, sender != null ? sender.id : 0);
}

public void setAccessoryView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setAccessoryView_1, view != null ? view.id : 0);
}

public void setAllowedFileTypes(NSArray types) {
	OS.objc_msgSend(this.id, OS.sel_setAllowedFileTypes_1, types != null ? types.id : 0);
}

public void setAllowsOtherFileTypes(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsOtherFileTypes_1, flag);
}

public void setCanCreateDirectories(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setCanCreateDirectories_1, flag);
}

public void setCanSelectHiddenExtension(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setCanSelectHiddenExtension_1, flag);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public void setDirectory(NSString path) {
	OS.objc_msgSend(this.id, OS.sel_setDirectory_1, path != null ? path.id : 0);
}

public void setExtensionHidden(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setExtensionHidden_1, flag);
}

public void setMessage(NSString message) {
	OS.objc_msgSend(this.id, OS.sel_setMessage_1, message != null ? message.id : 0);
}

public void setNameFieldLabel(NSString label) {
	OS.objc_msgSend(this.id, OS.sel_setNameFieldLabel_1, label != null ? label.id : 0);
}

public void setPrompt(NSString prompt) {
	OS.objc_msgSend(this.id, OS.sel_setPrompt_1, prompt != null ? prompt.id : 0);
}

public void setRequiredFileType(NSString type) {
	OS.objc_msgSend(this.id, OS.sel_setRequiredFileType_1, type != null ? type.id : 0);
}

public void setTitle(NSString title) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_1, title != null ? title.id : 0);
}

public void setTreatsFilePackagesAsDirectories(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setTreatsFilePackagesAsDirectories_1, flag);
}

public NSString title() {
	int result = OS.objc_msgSend(this.id, OS.sel_title);
	return result != 0 ? new NSString(result) : null;
}

public boolean treatsFilePackagesAsDirectories() {
	return OS.objc_msgSend(this.id, OS.sel_treatsFilePackagesAsDirectories) != 0;
}

public void validateVisibleColumns() {
	OS.objc_msgSend(this.id, OS.sel_validateVisibleColumns);
}

}
