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

public static NSAlert alertWithMessageText(NSString message, NSString defaultButton, NSString alternateButton, NSString otherButton, NSString informativeTextWithFormat) {
	int result = OS.objc_msgSend(OS.class_NSAlert, OS.sel_alertWithMessageText_defaultButton_alternateButton_otherButton_informativeTextWithFormat_, message != null ? message.id : 0, defaultButton != null ? defaultButton.id : 0, alternateButton != null ? alternateButton.id : 0, otherButton != null ? otherButton.id : 0, informativeTextWithFormat != null ? informativeTextWithFormat.id : 0);
	return result != 0 ? new NSAlert(result) : null;
}

public int runModal() {
	return OS.objc_msgSend(this.id, OS.sel_runModal);
}

public void setAlertStyle(int style) {
	OS.objc_msgSend(this.id, OS.sel_setAlertStyle_, style);
}

public NSWindow window() {
	int result = OS.objc_msgSend(this.id, OS.sel_window);
	return result != 0 ? new NSWindow(result) : null;
}

}
