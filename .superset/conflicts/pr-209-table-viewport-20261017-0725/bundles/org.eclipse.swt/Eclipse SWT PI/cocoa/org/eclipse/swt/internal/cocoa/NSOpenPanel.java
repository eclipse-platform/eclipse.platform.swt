/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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

public class NSOpenPanel extends NSSavePanel {

public NSOpenPanel() {
	super();
}

public NSOpenPanel(long id) {
	super(id);
}

public NSOpenPanel(id id) {
	super(id);
}

public NSArray filenames() {
	long result = OS.objc_msgSend(this.id, OS.sel_filenames);
	return result != 0 ? new NSArray(result) : null;
}

public static NSOpenPanel openPanel() {
	long result = OS.objc_msgSend(OS.class_NSOpenPanel, OS.sel_openPanel);
	return result != 0 ? new NSOpenPanel(result) : null;
}

public void setAccessoryViewDisclosed(boolean accessoryViewDisclosed) {
	OS.objc_msgSend(this.id, OS.sel_setAccessoryViewDisclosed_, accessoryViewDisclosed);
}

public void setAllowsMultipleSelection(boolean allowsMultipleSelection) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsMultipleSelection_, allowsMultipleSelection);
}

public void setCanChooseDirectories(boolean canChooseDirectories) {
	OS.objc_msgSend(this.id, OS.sel_setCanChooseDirectories_, canChooseDirectories);
}

public void setCanChooseFiles(boolean canChooseFiles) {
	OS.objc_msgSend(this.id, OS.sel_setCanChooseFiles_, canChooseFiles);
}

public static NSSavePanel savePanel() {
	long result = OS.objc_msgSend(OS.class_NSOpenPanel, OS.sel_savePanel);
	return result != 0 ? new NSSavePanel(result) : null;
}

public static double minFrameWidthWithTitle(NSString aTitle, long aStyle) {
	return OS.objc_msgSend_fpret(OS.class_NSOpenPanel, OS.sel_minFrameWidthWithTitle_styleMask_, aTitle != null ? aTitle.id : 0, aStyle);
}

public static long windowNumberAtPoint(NSPoint point, long windowNumber) {
	return OS.objc_msgSend(OS.class_NSOpenPanel, OS.sel_windowNumberAtPoint_belowWindowWithWindowNumber_, point, windowNumber);
}

}
