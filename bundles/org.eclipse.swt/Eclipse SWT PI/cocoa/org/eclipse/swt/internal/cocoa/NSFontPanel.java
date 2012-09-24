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

public class NSFontPanel extends NSPanel {

public NSFontPanel() {
	super();
}

public NSFontPanel(long /*int*/ id) {
	super(id);
}

public NSFontPanel(id id) {
	super(id);
}

public NSFont panelConvertFont(NSFont fontObj) {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_panelConvertFont_, fontObj != null ? fontObj.id : 0);
	return result != 0 ? new NSFont(result) : null;
}

public void setPanelFont(NSFont fontObj, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setPanelFont_isMultiple_, fontObj != null ? fontObj.id : 0, flag);
}

public static NSFontPanel sharedFontPanel() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSFontPanel, OS.sel_sharedFontPanel);
	return result != 0 ? new NSFontPanel(result) : null;
}

public static double /*float*/ minFrameWidthWithTitle(NSString aTitle, long /*int*/ aStyle) {
	return (double /*float*/)OS.objc_msgSend_fpret(OS.class_NSFontPanel, OS.sel_minFrameWidthWithTitle_styleMask_, aTitle != null ? aTitle.id : 0, aStyle);
}

public static long /*int*/ windowNumberAtPoint(NSPoint point, long /*int*/ windowNumber) {
	return OS.objc_msgSend(OS.class_NSFontPanel, OS.sel_windowNumberAtPoint_belowWindowWithWindowNumber_, point, windowNumber);
}

}
