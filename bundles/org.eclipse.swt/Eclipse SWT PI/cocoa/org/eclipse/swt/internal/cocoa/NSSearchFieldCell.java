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

public class NSSearchFieldCell extends NSTextFieldCell {

public NSSearchFieldCell() {
	super();
}

public NSSearchFieldCell(int id) {
	super(id);
}

public NSButtonCell cancelButtonCell() {
	int result = OS.objc_msgSend(this.id, OS.sel_cancelButtonCell);
	return result != 0 ? new NSButtonCell(result) : null;
}

public NSRect cancelButtonRectForBounds(NSRect rect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_cancelButtonRectForBounds_1, rect);
	return result;
}

public int maximumRecents() {
	return OS.objc_msgSend(this.id, OS.sel_maximumRecents);
}

public NSArray recentSearches() {
	int result = OS.objc_msgSend(this.id, OS.sel_recentSearches);
	return result != 0 ? new NSArray(result) : null;
}

public NSString recentsAutosaveName() {
	int result = OS.objc_msgSend(this.id, OS.sel_recentsAutosaveName);
	return result != 0 ? new NSString(result) : null;
}

public void resetCancelButtonCell() {
	OS.objc_msgSend(this.id, OS.sel_resetCancelButtonCell);
}

public void resetSearchButtonCell() {
	OS.objc_msgSend(this.id, OS.sel_resetSearchButtonCell);
}

public NSButtonCell searchButtonCell() {
	int result = OS.objc_msgSend(this.id, OS.sel_searchButtonCell);
	return result != 0 ? new NSButtonCell(result) : null;
}

public NSRect searchButtonRectForBounds(NSRect rect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_searchButtonRectForBounds_1, rect);
	return result;
}

public NSMenu searchMenuTemplate() {
	int result = OS.objc_msgSend(this.id, OS.sel_searchMenuTemplate);
	return result != 0 ? new NSMenu(result) : null;
}

public NSRect searchTextRectForBounds(NSRect rect) {
	NSRect result = new NSRect();
	OS.objc_msgSend_stret(result, this.id, OS.sel_searchTextRectForBounds_1, rect);
	return result;
}

public boolean sendsSearchStringImmediately() {
	return OS.objc_msgSend(this.id, OS.sel_sendsSearchStringImmediately) != 0;
}

public boolean sendsWholeSearchString() {
	return OS.objc_msgSend(this.id, OS.sel_sendsWholeSearchString) != 0;
}

public void setCancelButtonCell(NSButtonCell cell) {
	OS.objc_msgSend(this.id, OS.sel_setCancelButtonCell_1, cell != null ? cell.id : 0);
}

public void setMaximumRecents(int maxRecents) {
	OS.objc_msgSend(this.id, OS.sel_setMaximumRecents_1, maxRecents);
}

public void setRecentSearches(NSArray searches) {
	OS.objc_msgSend(this.id, OS.sel_setRecentSearches_1, searches != null ? searches.id : 0);
}

public void setRecentsAutosaveName(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setRecentsAutosaveName_1, string != null ? string.id : 0);
}

public void setSearchButtonCell(NSButtonCell cell) {
	OS.objc_msgSend(this.id, OS.sel_setSearchButtonCell_1, cell != null ? cell.id : 0);
}

public void setSearchMenuTemplate(NSMenu menu) {
	OS.objc_msgSend(this.id, OS.sel_setSearchMenuTemplate_1, menu != null ? menu.id : 0);
}

public void setSendsSearchStringImmediately(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSendsSearchStringImmediately_1, flag);
}

public void setSendsWholeSearchString(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setSendsWholeSearchString_1, flag);
}

}
