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

public class NSBrowserCell extends NSCell {

public NSBrowserCell() {
	super();
}

public NSBrowserCell(int id) {
	super(id);
}

public NSImage alternateImage() {
	int result = OS.objc_msgSend(this.id, OS.sel_alternateImage);
	return result != 0 ? new NSImage(result) : null;
}

public static NSImage branchImage() {
	int result = OS.objc_msgSend(OS.class_NSBrowserCell, OS.sel_branchImage);
	return result != 0 ? new NSImage(result) : null;
}

public NSColor highlightColorInView(NSView controlView) {
	int result = OS.objc_msgSend(this.id, OS.sel_highlightColorInView_1, controlView != null ? controlView.id : 0);
	return result != 0 ? new NSColor(result) : null;
}

public static NSImage highlightedBranchImage() {
	int result = OS.objc_msgSend(OS.class_NSBrowserCell, OS.sel_highlightedBranchImage);
	return result != 0 ? new NSImage(result) : null;
}

public NSImage image() {
	int result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public boolean isLeaf() {
	return OS.objc_msgSend(this.id, OS.sel_isLeaf) != 0;
}

public boolean isLoaded() {
	return OS.objc_msgSend(this.id, OS.sel_isLoaded) != 0;
}

public void reset() {
	OS.objc_msgSend(this.id, OS.sel_reset);
}

public void set() {
	OS.objc_msgSend(this.id, OS.sel_set);
}

public void setAlternateImage(NSImage newAltImage) {
	OS.objc_msgSend(this.id, OS.sel_setAlternateImage_1, newAltImage != null ? newAltImage.id : 0);
}

public void setImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setImage_1, image != null ? image.id : 0);
}

public void setLeaf(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setLeaf_1, flag);
}

public void setLoaded(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setLoaded_1, flag);
}

}
