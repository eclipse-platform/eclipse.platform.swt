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

public class NSToolbarItem extends NSObject {

public NSToolbarItem() {
	super();
}

public NSToolbarItem(int id) {
	super(id);
}

public int action() {
	return OS.objc_msgSend(this.id, OS.sel_action);
}

public boolean allowsDuplicatesInToolbar() {
	return OS.objc_msgSend(this.id, OS.sel_allowsDuplicatesInToolbar) != 0;
}

public boolean autovalidates() {
	return OS.objc_msgSend(this.id, OS.sel_autovalidates) != 0;
}

public NSImage image() {
	int result = OS.objc_msgSend(this.id, OS.sel_image);
	return result != 0 ? new NSImage(result) : null;
}

public id initWithItemIdentifier(NSString itemIdentifier) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithItemIdentifier_1, itemIdentifier != null ? itemIdentifier.id : 0);
	return result != 0 ? new id(result) : null;
}

public boolean isEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_isEnabled) != 0;
}

public NSString itemIdentifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_itemIdentifier);
	return result != 0 ? new NSString(result) : null;
}

public NSString label() {
	int result = OS.objc_msgSend(this.id, OS.sel_label);
	return result != 0 ? new NSString(result) : null;
}

public NSSize maxSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_maxSize);
	return result;
}

public NSMenuItem menuFormRepresentation() {
	int result = OS.objc_msgSend(this.id, OS.sel_menuFormRepresentation);
	return result != 0 ? new NSMenuItem(result) : null;
}

public NSSize minSize() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_minSize);
	return result;
}

public NSString paletteLabel() {
	int result = OS.objc_msgSend(this.id, OS.sel_paletteLabel);
	return result != 0 ? new NSString(result) : null;
}

public void setAction(int action) {
	OS.objc_msgSend(this.id, OS.sel_setAction_1, action);
}

public void setAutovalidates(boolean resistance) {
	OS.objc_msgSend(this.id, OS.sel_setAutovalidates_1, resistance);
}

public void setEnabled(boolean enabled) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_1, enabled);
}

public void setImage(NSImage image) {
	OS.objc_msgSend(this.id, OS.sel_setImage_1, image != null ? image.id : 0);
}

public void setLabel(NSString label) {
	OS.objc_msgSend(this.id, OS.sel_setLabel_1, label != null ? label.id : 0);
}

public void setMaxSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setMaxSize_1, size);
}

public void setMenuFormRepresentation(NSMenuItem menuItem) {
	OS.objc_msgSend(this.id, OS.sel_setMenuFormRepresentation_1, menuItem != null ? menuItem.id : 0);
}

public void setMinSize(NSSize size) {
	OS.objc_msgSend(this.id, OS.sel_setMinSize_1, size);
}

public void setPaletteLabel(NSString paletteLabel) {
	OS.objc_msgSend(this.id, OS.sel_setPaletteLabel_1, paletteLabel != null ? paletteLabel.id : 0);
}

public void setTag(int tag) {
	OS.objc_msgSend(this.id, OS.sel_setTag_1, tag);
}

public void setTarget(id target) {
	OS.objc_msgSend(this.id, OS.sel_setTarget_1, target != null ? target.id : 0);
}

public void setToolTip(NSString toolTip) {
	OS.objc_msgSend(this.id, OS.sel_setToolTip_1, toolTip != null ? toolTip.id : 0);
}

public void setView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setView_1, view != null ? view.id : 0);
}

public void setVisibilityPriority(int visibilityPriority) {
	OS.objc_msgSend(this.id, OS.sel_setVisibilityPriority_1, visibilityPriority);
}

public int tag() {
	return OS.objc_msgSend(this.id, OS.sel_tag);
}

public id target() {
	int result = OS.objc_msgSend(this.id, OS.sel_target);
	return result != 0 ? new id(result) : null;
}

public NSString toolTip() {
	int result = OS.objc_msgSend(this.id, OS.sel_toolTip);
	return result != 0 ? new NSString(result) : null;
}

public NSToolbar toolbar() {
	int result = OS.objc_msgSend(this.id, OS.sel_toolbar);
	return result != 0 ? new NSToolbar(result) : null;
}

public void validate() {
	OS.objc_msgSend(this.id, OS.sel_validate);
}

public NSView view() {
	int result = OS.objc_msgSend(this.id, OS.sel_view);
	return result != 0 ? new NSView(result) : null;
}

public int visibilityPriority() {
	return OS.objc_msgSend(this.id, OS.sel_visibilityPriority);
}

}
