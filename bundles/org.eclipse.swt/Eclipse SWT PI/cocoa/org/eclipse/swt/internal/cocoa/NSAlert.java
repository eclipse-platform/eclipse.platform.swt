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

public class NSAlert extends NSObject {

public NSAlert() {
	super();
}

public NSAlert(int id) {
	super(id);
}

public NSView accessoryView() {
	int result = OS.objc_msgSend(this.id, OS.sel_accessoryView);
	return result != 0 ? new NSView(result) : null;
}

public NSButton addButtonWithTitle(NSString title) {
	int result = OS.objc_msgSend(this.id, OS.sel_addButtonWithTitle_1, title != null ? title.id : 0);
	return result != 0 ? new NSButton(result) : null;
}

public int alertStyle() {
	return OS.objc_msgSend(this.id, OS.sel_alertStyle);
}

public static NSAlert alertWithError(NSError error) {
	int result = OS.objc_msgSend(OS.class_NSAlert, OS.sel_alertWithError_1, error != null ? error.id : 0);
	return result != 0 ? new NSAlert(result) : null;
}

public static NSAlert alertWithMessageText(NSString message, NSString defaultButton, NSString alternateButton, NSString otherButton, NSString informativeTextWithFormat) {
	int result = OS.objc_msgSend(OS.class_NSAlert, OS.sel_alertWithMessageText_1defaultButton_1alternateButton_1otherButton_1informativeTextWithFormat_1, message != null ? message.id : 0, defaultButton != null ? defaultButton.id : 0, alternateButton != null ? alternateButton.id : 0, otherButton != null ? otherButton.id : 0, informativeTextWithFormat != null ? informativeTextWithFormat.id : 0);
	return result != 0 ? new NSAlert(result) : null;
}

public void beginSheetModalForWindow(NSWindow window, id delegate, int didEndSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_beginSheetModalForWindow_1modalDelegate_1didEndSelector_1contextInfo_1, window != null ? window.id : 0, delegate != null ? delegate.id : 0, didEndSelector, contextInfo);
}

public NSArray buttons() {
	int result = OS.objc_msgSend(this.id, OS.sel_buttons);
	return result != 0 ? new NSArray(result) : null;
}

public id delegate() {
	int result = OS.objc_msgSend(this.id, OS.sel_delegate);
	return result != 0 ? new id(result) : null;
}

public NSString helpAnchor() {
	int result = OS.objc_msgSend(this.id, OS.sel_helpAnchor);
	return result != 0 ? new NSString(result) : null;
}

public NSImage icon() {
	int result = OS.objc_msgSend(this.id, OS.sel_icon);
	return result != 0 ? new NSImage(result) : null;
}

public NSString informativeText() {
	int result = OS.objc_msgSend(this.id, OS.sel_informativeText);
	return result != 0 ? new NSString(result) : null;
}

public void layout() {
	OS.objc_msgSend(this.id, OS.sel_layout);
}

public NSString messageText() {
	int result = OS.objc_msgSend(this.id, OS.sel_messageText);
	return result != 0 ? new NSString(result) : null;
}

public int runModal() {
	return OS.objc_msgSend(this.id, OS.sel_runModal);
}

public void setAccessoryView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setAccessoryView_1, view != null ? view.id : 0);
}

public void setAlertStyle(int style) {
	OS.objc_msgSend(this.id, OS.sel_setAlertStyle_1, style);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_1, delegate != null ? delegate.id : 0);
}

public void setHelpAnchor(NSString anchor) {
	OS.objc_msgSend(this.id, OS.sel_setHelpAnchor_1, anchor != null ? anchor.id : 0);
}

public void setIcon(NSImage icon) {
	OS.objc_msgSend(this.id, OS.sel_setIcon_1, icon != null ? icon.id : 0);
}

public void setInformativeText(NSString informativeText) {
	OS.objc_msgSend(this.id, OS.sel_setInformativeText_1, informativeText != null ? informativeText.id : 0);
}

public void setMessageText(NSString messageText) {
	OS.objc_msgSend(this.id, OS.sel_setMessageText_1, messageText != null ? messageText.id : 0);
}

public void setShowsHelp(boolean showsHelp) {
	OS.objc_msgSend(this.id, OS.sel_setShowsHelp_1, showsHelp);
}

public void setShowsSuppressionButton(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setShowsSuppressionButton_1, flag);
}

public boolean showsHelp() {
	return OS.objc_msgSend(this.id, OS.sel_showsHelp) != 0;
}

public boolean showsSuppressionButton() {
	return OS.objc_msgSend(this.id, OS.sel_showsSuppressionButton) != 0;
}

public NSButton suppressionButton() {
	int result = OS.objc_msgSend(this.id, OS.sel_suppressionButton);
	return result != 0 ? new NSButton(result) : null;
}

public id window() {
	int result = OS.objc_msgSend(this.id, OS.sel_window);
	return result != 0 ? new id(result) : null;
}

}
