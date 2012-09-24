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

public NSFont(long /*int*/ id) {
	super(id);
}

public NSFont(id id) {
	super(id);
}

public double /*float*/ ascender() {
	return (double /*float*/)OS.objc_msgSend_fpret(this.id, OS.sel_ascender);
}

public static NSFont controlContentFontOfSize(double /*float*/ fontSize) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSFont, OS.sel_controlContentFontOfSize_, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public double /*float*/ descender() {
	return (double /*float*/)OS.objc_msgSend_fpret(this.id, OS.sel_descender);
}

public NSString displayName() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_displayName);
	return result != 0 ? new NSString(result) : null;
}

public NSString familyName() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_familyName);
	return result != 0 ? new NSString(result) : null;
}

public NSString fontName() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_fontName);
	return result != 0 ? new NSString(result) : null;
}

public static NSFont fontWithName(NSString fontName, double /*float*/ fontSize) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSFont, OS.sel_fontWithName_size_, fontName != null ? fontName.id : 0, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public double /*float*/ leading() {
	return (double /*float*/)OS.objc_msgSend_fpret(this.id, OS.sel_leading);
}

public static NSFont menuBarFontOfSize(double /*float*/ fontSize) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSFont, OS.sel_menuBarFontOfSize_, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public static NSFont menuFontOfSize(double /*float*/ fontSize) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSFont, OS.sel_menuFontOfSize_, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public double /*float*/ pointSize() {
	return (double /*float*/)OS.objc_msgSend_fpret(this.id, OS.sel_pointSize);
}

public static double /*float*/ smallSystemFontSize() {
	return (double /*float*/)OS.objc_msgSend_fpret(OS.class_NSFont, OS.sel_smallSystemFontSize);
}

public static NSFont systemFontOfSize(double /*float*/ fontSize) {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSFont, OS.sel_systemFontOfSize_, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public static double /*float*/ systemFontSize() {
	return (double /*float*/)OS.objc_msgSend_fpret(OS.class_NSFont, OS.sel_systemFontSize);
}

public static double /*float*/ systemFontSizeForControlSize(long /*int*/ controlSize) {
	return (double /*float*/)OS.objc_msgSend_fpret(OS.class_NSFont, OS.sel_systemFontSizeForControlSize_, controlSize);
}

}
