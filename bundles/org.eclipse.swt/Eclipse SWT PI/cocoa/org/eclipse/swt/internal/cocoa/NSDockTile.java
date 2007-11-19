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

public class NSDockTile extends NSObject {

public NSDockTile() {
	super();
}

public NSDockTile(int id) {
	super(id);
}

public NSString badgeLabel() {
	int result = OS.objc_msgSend(this.id, OS.sel_badgeLabel);
	return result != 0 ? new NSString(result) : null;
}

public NSView contentView() {
	int result = OS.objc_msgSend(this.id, OS.sel_contentView);
	return result != 0 ? new NSView(result) : null;
}

public void display() {
	OS.objc_msgSend(this.id, OS.sel_display);
}

public id owner() {
	int result = OS.objc_msgSend(this.id, OS.sel_owner);
	return result != 0 ? new id(result) : null;
}

public void setBadgeLabel(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setBadgeLabel_1, string != null ? string.id : 0);
}

public void setContentView(NSView view) {
	OS.objc_msgSend(this.id, OS.sel_setContentView_1, view != null ? view.id : 0);
}

public void setShowsApplicationBadge(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setShowsApplicationBadge_1, flag);
}

public boolean showsApplicationBadge() {
	return OS.objc_msgSend(this.id, OS.sel_showsApplicationBadge) != 0;
}

public NSSize size() {
	NSSize result = new NSSize();
	OS.objc_msgSend_stret(result, this.id, OS.sel_size);
	return result;
}

}
