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

public class NSTabViewItem extends NSObject {

public NSTabViewItem() {
	super();
}

public NSTabViewItem(int id) {
	super(id);
}

public NSColor color() {
	int result = OS.objc_msgSend(this.id, OS.sel_color);
	return result != 0 ? new NSColor(result) : null;
}

public void drawLabel(boolean shouldTruncateLabel, NSRect labelRect) {
	OS.objc_msgSend(this.id, OS.sel_drawLabel_1inRect_1, shouldTruncateLabel, labelRect);
}

public id identifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_identifier);
	return result != 0 ? new id(result) : null;
}

public NSTabViewItem initWithIdentifier(id identifier) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithIdentifier_1, identifier != null ? identifier.id : 0);
	return result != 0 ? this : null;
}

public NSTabViewItem initialFirstResponder() {
	int result = OS.objc_msgSend(this.id, OS.sel_initialFirstResponder);
	return result != 0 ? this : null;
}

public NSString label() {
	int result = OS.objc_msgSend(this.id, OS.sel_label);
	return result != 0 ? new NSString(result) : null;
}

public void setColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setColor_1, color != null ? color.id : 0);
}

public void setIdentifier(id identifier) {
	OS.objc_msgSend(this.id, OS.sel_setIdentifier_1, identifier != null ? identifier.id : 0);
}

public void setInitialFirstResponder(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setInitialFirstResponder_1, view != null ? view.id : 0);
}

public void setLabel(NSString label) {
	OS.objc_msgSend(this.id, OS.sel_setLabel_1, label != null ? label.id : 0);
}

public void setView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setView_1, view != null ? view.id : 0);
}

public NSSize sizeOfLabel(boolean computeMin) {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_sizeOfLabel_1, computeMin);
	return result;
}

public int tabState() {
	return OS.objc_msgSend(this.id, OS.sel_tabState);
}

public NSTabView tabView() {
	int result = OS.objc_msgSend(this.id, OS.sel_tabView);
	return result != 0 ? new NSTabView(result) : null;
}

public id view() {
	int result = OS.objc_msgSend(this.id, OS.sel_view);
	return result != 0 ? new id(result) : null;
}

}
