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

public class NSFontManager extends NSObject {

public NSFontManager() {
	super();
}

public NSFontManager(long /*int*/ id) {
	super(id);
}

public NSFontManager(id id) {
	super(id);
}

public NSArray availableFontFamilies() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_availableFontFamilies);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray availableFonts() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_availableFonts);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray availableMembersOfFontFamily(NSString fam) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_availableMembersOfFontFamily_, fam != null ? fam.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSFont convertFont(NSFont fontObj, long /*int*/ trait) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_convertFont_toHaveTrait_, fontObj != null ? fontObj.id : 0, trait);
	return result != 0 ? new NSFont(result) : null;
}

public NSFont fontWithFamily(NSString family, long /*int*/ traits, long /*int*/ weight, double /*float*/ size) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_fontWithFamily_traits_weight_size_, family != null ? family.id : 0, traits, weight, size);
	return result != 0 ? new NSFont(result) : null;
}

public static NSFontManager sharedFontManager() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSFontManager, OS.sel_sharedFontManager);
	return result != 0 ? new NSFontManager(result) : null;
}

public long /*int*/ traitsOfFont(NSFont fontObj) {
	return OS.objc_msgSend(this.id, OS.sel_traitsOfFont_, fontObj != null ? fontObj.id : 0);
}

public long /*int*/ weightOfFont(NSFont fontObj) {
	return OS.objc_msgSend(this.id, OS.sel_weightOfFont_, fontObj != null ? fontObj.id : 0);
}

}
