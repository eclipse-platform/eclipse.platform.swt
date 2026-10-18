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

public class NSToolbar extends NSObject {

public NSToolbar() {
	super();
}

public NSToolbar(long id) {
	super(id);
}

public NSToolbar(id id) {
	super(id);
}

public NSToolbar initWithIdentifier(NSString identifier) {
	long result = OS.objc_msgSend(this.id, OS.sel_initWithIdentifier_, identifier != null ? identifier.id : 0);
	return result == this.id ? this : (result != 0 ? new NSToolbar(result) : null);
}

public void insertItemWithItemIdentifier(NSString itemIdentifier, long index) {
	OS.objc_msgSend(this.id, OS.sel_insertItemWithItemIdentifier_atIndex_, itemIdentifier != null ? itemIdentifier.id : 0, index);
}

public boolean isVisible() {
	return OS.objc_msgSend_bool(this.id, OS.sel_isVisible);
}

public void removeItemAtIndex(long index) {
	OS.objc_msgSend(this.id, OS.sel_removeItemAtIndex_, index);
}

public void setAllowsUserCustomization(boolean allowsUserCustomization) {
	OS.objc_msgSend(this.id, OS.sel_setAllowsUserCustomization_, allowsUserCustomization);
}

public void setDelegate(id delegate) {
	OS.objc_msgSend(this.id, OS.sel_setDelegate_, delegate != null ? delegate.id : 0);
}

public void setDisplayMode(long displayMode) {
	OS.objc_msgSend(this.id, OS.sel_setDisplayMode_, displayMode);
}

public void setSelectedItemIdentifier(NSString selectedItemIdentifier) {
	OS.objc_msgSend(this.id, OS.sel_setSelectedItemIdentifier_, selectedItemIdentifier != null ? selectedItemIdentifier.id : 0);
}

public void setVisible(boolean visible) {
	OS.objc_msgSend(this.id, OS.sel_setVisible_, visible);
}

}
