/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSFont extends NSObject {

public NSFont() {
	super();
}

public NSFont(int /*long*/ id) {
	super(id);
}

public NSFont(id id) {
	super(id);
}

public float /*double*/ ascender() {
	return (float /*double*/)OS.objc_msgSend_fpret(this.id, OS.sel_ascender);
}

public static NSFont controlContentFontOfSize(float /*double*/ fontSize) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSFont, OS.sel_controlContentFontOfSize_, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public float /*double*/ descender() {
	return (float /*double*/)OS.objc_msgSend_fpret(this.id, OS.sel_descender);
}

public NSString displayName() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_displayName);
	return result != 0 ? new NSString(result) : null;
}

public NSString familyName() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_familyName);
	return result != 0 ? new NSString(result) : null;
}

public NSString fontName() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_fontName);
	return result != 0 ? new NSString(result) : null;
}

public static NSFont fontWithName(NSString fontName, float /*double*/ fontSize) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSFont, OS.sel_fontWithName_size_, fontName != null ? fontName.id : 0, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public float /*double*/ leading() {
	return (float /*double*/)OS.objc_msgSend_fpret(this.id, OS.sel_leading);
}

public static NSFont menuBarFontOfSize(float /*double*/ fontSize) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSFont, OS.sel_menuBarFontOfSize_, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public static NSFont menuFontOfSize(float /*double*/ fontSize) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSFont, OS.sel_menuFontOfSize_, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public float /*double*/ pointSize() {
	return (float /*double*/)OS.objc_msgSend_fpret(this.id, OS.sel_pointSize);
}

public static float /*double*/ smallSystemFontSize() {
	return (float /*double*/)OS.objc_msgSend_fpret(OS.class_NSFont, OS.sel_smallSystemFontSize);
}

public static NSFont systemFontOfSize(float /*double*/ fontSize) {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSFont, OS.sel_systemFontOfSize_, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public static float /*double*/ systemFontSize() {
	return (float /*double*/)OS.objc_msgSend_fpret(OS.class_NSFont, OS.sel_systemFontSize);
}

public static float /*double*/ systemFontSizeForControlSize(int /*long*/ controlSize) {
	return (float /*double*/)OS.objc_msgSend_fpret(OS.class_NSFont, OS.sel_systemFontSizeForControlSize_, controlSize);
}

}
