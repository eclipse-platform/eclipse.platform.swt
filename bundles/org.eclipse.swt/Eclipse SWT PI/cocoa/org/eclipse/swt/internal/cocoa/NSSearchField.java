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

public class NSSearchField extends NSTextField {

public NSSearchField() {
	super();
}

public NSSearchField(int id) {
	super(id);
}

public NSArray recentSearches() {
	int result = OS.objc_msgSend(this.id, OS.sel_recentSearches);
	return result != 0 ? new NSArray(result) : null;
}

public NSString recentsAutosaveName() {
	int result = OS.objc_msgSend(this.id, OS.sel_recentsAutosaveName);
	return result != 0 ? new NSString(result) : null;
}

public void setRecentSearches(NSArray searches) {
	OS.objc_msgSend(this.id, OS.sel_setRecentSearches_1, searches != null ? searches.id : 0);
}

public void setRecentsAutosaveName(NSString string) {
	OS.objc_msgSend(this.id, OS.sel_setRecentsAutosaveName_1, string != null ? string.id : 0);
}

}
