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

public class NSFont extends NSObject {

public NSFont() {
	super();
}

public NSFont(long id) {
	super(id);
}

public NSFont(id id) {
	super(id);
}

public double ascender() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_ascender);
}

public static NSFont boldSystemFontOfSize(double fontSize) {
	long result = OS.objc_msgSend(OS.class_NSFont, OS.sel_boldSystemFontOfSize_, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public double descender() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_descender);
}

public NSString displayName() {
	long result = OS.objc_msgSend(this.id, OS.sel_displayName);
	return result != 0 ? new NSString(result) : null;
}

public NSString familyName() {
	long result = OS.objc_msgSend(this.id, OS.sel_familyName);
	return result != 0 ? new NSString(result) : null;
}

public NSString fontName() {
	long result = OS.objc_msgSend(this.id, OS.sel_fontName);
	return result != 0 ? new NSString(result) : null;
}

public static NSFont fontWithName(NSString fontName, double fontSize) {
	long result = OS.objc_msgSend(OS.class_NSFont, OS.sel_fontWithName_size_, fontName != null ? fontName.id : 0, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public double leading() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_leading);
}

public static NSFont menuBarFontOfSize(double fontSize) {
	long result = OS.objc_msgSend(OS.class_NSFont, OS.sel_menuBarFontOfSize_, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public double pointSize() {
	return OS.objc_msgSend_fpret(this.id, OS.sel_pointSize);
}

public static double smallSystemFontSize() {
	return OS.objc_msgSend_fpret(OS.class_NSFont, OS.sel_smallSystemFontSize);
}

public static NSFont systemFontOfSize(double fontSize) {
	long result = OS.objc_msgSend(OS.class_NSFont, OS.sel_systemFontOfSize_, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public static double systemFontSize() {
	return OS.objc_msgSend_fpret(OS.class_NSFont, OS.sel_systemFontSize);
}

public static double systemFontSizeForControlSize(long controlSize) {
	return OS.objc_msgSend_fpret(OS.class_NSFont, OS.sel_systemFontSizeForControlSize_, controlSize);
}

}
