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

public class NSAlert extends NSObject {

public NSAlert() {
	super();
}

public NSAlert(int /*long*/ id) {
	super(id);
}

public NSAlert(id id) {
	super(id);
}

public NSButton addButtonWithTitle(NSString title) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_addButtonWithTitle_, title != null ? title.id : 0);
	return result != 0 ? new NSButton(result) : null;
}

public void beginSheetModalForWindow(NSWindow window, id delegate, int /*long*/ didEndSelector, int /*long*/ contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_beginSheetModalForWindow_modalDelegate_didEndSelector_contextInfo_, window != null ? window.id : 0, delegate != null ? delegate.id : 0, didEndSelector, contextInfo);
}

public int /*long*/ runModal() {
	return OS.objc_msgSend(this.id, OS.sel_runModal);
}

public void setAlertStyle(int /*long*/ style) {
	OS.objc_msgSend(this.id, OS.sel_setAlertStyle_, style);
}

public void setMessageText(NSString messageText) {
	OS.objc_msgSend(this.id, OS.sel_setMessageText_, messageText != null ? messageText.id : 0);
}

public NSWindow window() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_window);
	return result != 0 ? new NSWindow(result) : null;
}

}
