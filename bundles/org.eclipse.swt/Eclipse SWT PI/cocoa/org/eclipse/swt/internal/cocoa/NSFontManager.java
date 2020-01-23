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

public class NSFontManager extends NSObject {

public NSFontManager() {
	super();
}

public NSFontManager(long id) {
	super(id);
}

public NSFontManager(id id) {
	super(id);
}

public NSArray availableFontFamilies() {
	long result = OS.objc_msgSend(this.id, OS.sel_availableFontFamilies);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray availableMembersOfFontFamily(NSString fam) {
	long result = OS.objc_msgSend(this.id, OS.sel_availableMembersOfFontFamily_, fam != null ? fam.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSFont convertFont(NSFont fontObj, long trait) {
	long result = OS.objc_msgSend(this.id, OS.sel_convertFont_toHaveTrait_, fontObj != null ? fontObj.id : 0, trait);
	return result != 0 ? new NSFont(result) : null;
}

public static NSFontManager sharedFontManager() {
	long result = OS.objc_msgSend(OS.class_NSFontManager, OS.sel_sharedFontManager);
	return result != 0 ? new NSFontManager(result) : null;
}

public long traitsOfFont(NSFont fontObj) {
	return OS.objc_msgSend(this.id, OS.sel_traitsOfFont_, fontObj != null ? fontObj.id : 0);
}

}
