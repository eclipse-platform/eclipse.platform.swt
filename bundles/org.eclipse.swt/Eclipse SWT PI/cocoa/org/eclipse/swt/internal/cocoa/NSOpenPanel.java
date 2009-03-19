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

public class NSOpenPanel extends NSSavePanel {

public NSOpenPanel() {
	super();
}

public NSOpenPanel(int /*long*/ id) {
	super(id);
}

public NSOpenPanel(id id) {
	super(id);
}

public NSArray filenames() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_filenames);
	return result != 0 ? new NSArray(result) : null;
}

public static NSOpenPanel openPanel() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSOpenPanel, OS.sel_openPanel);
	return result != 0 ? new NSOpenPanel(result) : null;
}

public void setAllowsMultipleSelection(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsMultipleSelection_, flag);
}

public void setCanChooseDirectories(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setCanChooseDirectories_, flag);
}

public void setCanChooseFiles(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setCanChooseFiles_, flag);
}

public static NSSavePanel savePanel() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSOpenPanel, OS.sel_savePanel);
	return result != 0 ? new NSSavePanel(result) : null;
}

public static float /*double*/ minFrameWidthWithTitle(NSString aTitle, int /*long*/ aStyle) {
	return (float)OS.objc_msgSend_fpret(OS.class_NSOpenPanel, OS.sel_minFrameWidthWithTitle_styleMask_, aTitle != null ? aTitle.id : 0, aStyle);
}

}
