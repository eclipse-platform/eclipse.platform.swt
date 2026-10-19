/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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

public class NSToolbarItem extends NSObject {

public NSToolbarItem() {
	super();
}

public NSToolbarItem(long id) {
	super(id);
}

public NSToolbarItem(id id) {
	super(id);
}

public NSToolbarItem initWithItemIdentifier(NSString itemIdentifier) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithItemIdentifier_, itemIdentifier != null ? itemIdentifier.id : 0);
	return result == this.id ? this : (result != 0 ? new NSToolbarItem(result) : null);
}

public NSString itemIdentifier() {
	long result = OS.objc_msgSend(this.id, OS.sel_itemIdentifier);
	return result != 0 ? new NSString(result) : null;
}

public void setAction(long action) {
	OS.objc_msgSend(this.id, OS.sel_setAction_, action);
}

public void setEnabled(boolean enabled) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_, enabled);
}

public void setImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setImage_, image != null ? image.id : 0);
}

public void setLabel(NSString label) {
	OS.objc_msgSend(this.id, OS.sel_setLabel_, label != null ? label.id : 0);
}

public void setMaxSize(NSSize maxSize) {
	OS.objc_msgSend(this.id, OS.sel_setMaxSize_, maxSize);
}

public void setMenuFormRepresentation(NSMenuItem menuFormRepresentation) {
	OS.objc_msgSend(this.id, OS.sel_setMenuFormRepresentation_, menuFormRepresentation != null ? menuFormRepresentation.id : 0);
}

public void setMinSize(NSSize minSize) {
	OS.objc_msgSend(this.id, OS.sel_setMinSize_, minSize);
}

public void setPaletteLabel(NSString paletteLabel) {
	OS.objc_msgSend(this.id, OS.sel_setPaletteLabel_, paletteLabel != null ? paletteLabel.id : 0);
}

public void setTarget(id target) {
	OS.objc_msgSend(this.id, OS.sel_setTarget_, target != null ? target.id : 0);
}

public void setToolTip(NSString toolTip) {
	OS.objc_msgSend(this.id, OS.sel_setToolTip_, toolTip != null ? toolTip.id : 0);
}

public void setView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setView_, view != null ? view.id : 0);
}

}
