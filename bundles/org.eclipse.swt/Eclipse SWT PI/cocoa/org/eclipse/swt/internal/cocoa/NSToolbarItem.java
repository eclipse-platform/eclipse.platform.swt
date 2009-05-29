/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSToolbarItem extends NSObject {

public NSToolbarItem() {
	super();
}

public NSToolbarItem(int /*long*/ id) {
	super(id);
}

public NSToolbarItem(id id) {
	super(id);
}

public NSToolbarItem initWithItemIdentifier(NSString itemIdentifier) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_initWithItemIdentifier_, itemIdentifier != null ? itemIdentifier.id : 0);
	return result == this.id ? this : (result != 0 ? new NSToolbarItem(result) : null);
}

public NSString itemIdentifier() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_itemIdentifier);
	return result != 0 ? new NSString(result) : null;
}

public void setAction(int /*long*/ action) {
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

public void setMaxSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setMaxSize_, size);
}

public void setMinSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setMinSize_, size);
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
