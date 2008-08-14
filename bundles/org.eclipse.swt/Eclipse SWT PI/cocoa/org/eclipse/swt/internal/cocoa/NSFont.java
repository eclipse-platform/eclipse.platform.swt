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

public float ascender() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_ascender);
}

public float descender() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_descender);
}

public NSString familyName() {
	int result = OS.objc_msgSend(this.id, OS.sel_familyName);
	return result != 0 ? new NSString(result) : null;
}

public NSString fontName() {
	int result = OS.objc_msgSend(this.id, OS.sel_fontName);
	return result != 0 ? new NSString(result) : null;
}

public static NSFont fontWithName(NSString fontName, float fontSize) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_fontWithName_size_, fontName != null ? fontName.id : 0, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public float leading() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_leading);
}

public float pointSize() {
	return (float)OS.objc_msgSend_fpret(this.id, OS.sel_pointSize);
}

public static NSFont systemFontOfSize(float fontSize) {
	int result = OS.objc_msgSend(OS.class_NSFont, OS.sel_systemFontOfSize_, fontSize);
	return result != 0 ? new NSFont(result) : null;
}

public static float systemFontSize() {
	return (float)OS.objc_msgSend_fpret(OS.class_NSFont, OS.sel_systemFontSize);
}

}
