/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

public NSFontManager(int /*long*/ id) {
	super(id);
}

public NSFontManager(id id) {
	super(id);
}

public NSArray availableFontFamilies() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_availableFontFamilies);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray availableFonts() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_availableFonts);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray availableMembersOfFontFamily(NSString fam) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_availableMembersOfFontFamily_, fam != null ? fam.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSFont fontWithFamily(NSString family, int /*long*/ traits, int /*long*/ weight, float /*double*/ size) {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_fontWithFamily_traits_weight_size_, family != null ? family.id : 0, traits, weight, size);
	return result != 0 ? new NSFont(result) : null;
}

public static NSFontManager sharedFontManager() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSFontManager, OS.sel_sharedFontManager);
	return result != 0 ? new NSFontManager(result) : null;
}

public int /*long*/ traitsOfFont(NSFont fontObj) {
	return OS.objc_msgSend(this.id, OS.sel_traitsOfFont_, fontObj != null ? fontObj.id : 0);
}

public int /*long*/ weightOfFont(NSFont fontObj) {
	return OS.objc_msgSend(this.id, OS.sel_weightOfFont_, fontObj != null ? fontObj.id : 0);
}

}
