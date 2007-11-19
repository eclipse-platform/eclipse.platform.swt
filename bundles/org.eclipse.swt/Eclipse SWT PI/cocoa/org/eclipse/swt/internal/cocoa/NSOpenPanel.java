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

public class NSOpenPanel extends NSSavePanel {

public NSOpenPanel() {
	super();
}

public NSOpenPanel(int id) {
	super(id);
}

public NSArray URLs() {
	int result = OS.objc_msgSend(this.id, OS.sel_URLs);
	return result != 0 ? new NSArray(result) : null;
}

public boolean allowsMultipleSelection() {
	return OS.objc_msgSend(this.id, OS.sel_allowsMultipleSelection) != 0;
}

public void beginForDirectory(NSString path, NSString name, NSArray fileTypes, id delegate, int didEndSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_beginForDirectory_1file_1types_1modelessDelegate_1didEndSelector_1contextInfo_1, path != null ? path.id : 0, name != null ? name.id : 0, fileTypes != null ? fileTypes.id : 0, delegate != null ? delegate.id : 0, didEndSelector, contextInfo);
}

public void beginSheetForDirectory(NSString path, NSString name, NSArray fileTypes, NSWindow docWindow, id delegate, int didEndSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_beginSheetForDirectory_1file_1types_1modalForWindow_1modalDelegate_1didEndSelector_1contextInfo_1, path != null ? path.id : 0, name != null ? name.id : 0, fileTypes != null ? fileTypes.id : 0, docWindow != null ? docWindow.id : 0, delegate != null ? delegate.id : 0, didEndSelector, contextInfo);
}

public boolean canChooseDirectories() {
	return OS.objc_msgSend(this.id, OS.sel_canChooseDirectories) != 0;
}

public boolean canChooseFiles() {
	return OS.objc_msgSend(this.id, OS.sel_canChooseFiles) != 0;
}

public NSArray filenames() {
	int result = OS.objc_msgSend(this.id, OS.sel_filenames);
	return result != 0 ? new NSArray(result) : null;
}

public static NSOpenPanel openPanel() {
	int result = OS.objc_msgSend(OS.class_NSOpenPanel, OS.sel_openPanel);
	return result != 0 ? new NSOpenPanel(result) : null;
}

public boolean resolvesAliases() {
	return OS.objc_msgSend(this.id, OS.sel_resolvesAliases) != 0;
}

public int runModalForDirectory(NSString path, NSString name, NSArray fileTypes) {
	return OS.objc_msgSend(this.id, OS.sel_runModalForDirectory_1file_1types_1, path != null ? path.id : 0, name != null ? name.id : 0, fileTypes != null ? fileTypes.id : 0);
}

public int runModalForTypes(NSArray fileTypes) {
	return OS.objc_msgSend(this.id, OS.sel_runModalForTypes_1, fileTypes != null ? fileTypes.id : 0);
}

public void setAllowsMultipleSelection(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsMultipleSelection_1, flag);
}

public void setCanChooseDirectories(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setCanChooseDirectories_1, flag);
}

public void setCanChooseFiles(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setCanChooseFiles_1, flag);
}

public void setResolvesAliases(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setResolvesAliases_1, flag);
}

}
