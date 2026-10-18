/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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

public class NSSavePanel extends NSPanel {

public NSSavePanel() {
	super();
}

public NSSavePanel(long id) {
	super(id);
}

public NSSavePanel(id id) {
	super(id);
}

public NSString filename() {
	long result = OS.objc_msgSend(this.id, OS.sel_filename);
	return result != 0 ? new NSString(result) : null;
}

public NSString nameFieldStringValue() {
	long result = OS.objc_msgSend(this.id, OS.sel_nameFieldStringValue);
	return result != 0 ? new NSString(result) : null;
}

public long runModal() {
	return OS.objc_msgSend(this.id, OS.sel_runModal);
}

public long runModalForDirectory(NSString path, NSString name) {
	return OS.objc_msgSend(this.id, OS.sel_runModalForDirectory_file_, path != null ? path.id : 0, name != null ? name.id : 0);
}

public static NSSavePanel savePanel() {
	long result = OS.objc_msgSend(OS.class_NSSavePanel, OS.sel_savePanel);
	return result != 0 ? new NSSavePanel(result) : null;
}

public void setAccessoryView(NSView accessoryView) {
	OS.objc_msgSend(this.id, OS.sel_setAccessoryView_, accessoryView != null ? accessoryView.id : 0);
}

public void setAllowedFileTypes(NSArray allowedFileTypes) {
	OS.objc_msgSend(this.id, OS.sel_setAllowedFileTypes_, allowedFileTypes != null ? allowedFileTypes.id : 0);
}

public void setAllowsOtherFileTypes(boolean allowsOtherFileTypes) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsOtherFileTypes_, allowsOtherFileTypes);
}

public void setCanCreateDirectories(boolean canCreateDirectories) {
	OS.objc_msgSend(this.id, OS.sel_setCanCreateDirectories_, canCreateDirectories);
}

public void setDirectory(NSString path) {
	OS.objc_msgSend(this.id, OS.sel_setDirectory_, path != null ? path.id : 0);
}

public void setDirectoryURL(NSURL directoryURL) {
	OS.objc_msgSend(this.id, OS.sel_setDirectoryURL_, directoryURL != null ? directoryURL.id : 0);
}

public void setMessage(NSString message) {
	OS.objc_msgSend(this.id, OS.sel_setMessage_, message != null ? message.id : 0);
}

public void setNameFieldStringValue(NSString nameFieldStringValue) {
	OS.objc_msgSend(this.id, OS.sel_setNameFieldStringValue_, nameFieldStringValue != null ? nameFieldStringValue.id : 0);
}

public void setTitle(NSString title) {
	OS.objc_msgSend(this.id, OS.sel_setTitle_, title != null ? title.id : 0);
}

public void setTreatsFilePackagesAsDirectories(boolean treatsFilePackagesAsDirectories) {
	OS.objc_msgSend(this.id, OS.sel_setTreatsFilePackagesAsDirectories_, treatsFilePackagesAsDirectories);
}

public void validateVisibleColumns() {
	OS.objc_msgSend(this.id, OS.sel_validateVisibleColumns);
}

public static double minFrameWidthWithTitle(NSString aTitle, long aStyle) {
	return OS.objc_msgSend_fpret(OS.class_NSSavePanel, OS.sel_minFrameWidthWithTitle_styleMask_, aTitle != null ? aTitle.id : 0, aStyle);
}

public static long windowNumberAtPoint(NSPoint point, long windowNumber) {
	return OS.objc_msgSend(OS.class_NSSavePanel, OS.sel_windowNumberAtPoint_belowWindowWithWindowNumber_, point, windowNumber);
}

}
