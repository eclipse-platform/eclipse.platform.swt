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

public class NSFontPanel extends NSPanel {

public NSFontPanel() {
	super();
}

public NSFontPanel(long id) {
	super(id);
}

public NSFontPanel(id id) {
	super(id);
}

public NSFont panelConvertFont(NSFont fontObj) {
	long result = OS.objc_msgSend(this.id, OS.sel_panelConvertFont_, fontObj != null ? fontObj.id : 0);
	return result != 0 ? new NSFont(result) : null;
}

public void setPanelFont(NSFont fontObj, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setPanelFont_isMultiple_, fontObj != null ? fontObj.id : 0, flag);
}

public static NSFontPanel sharedFontPanel() {
	long result = OS.objc_msgSend(OS.class_NSFontPanel, OS.sel_sharedFontPanel);
	return result != 0 ? new NSFontPanel(result) : null;
}

public static double minFrameWidthWithTitle(NSString aTitle, long aStyle) {
	return OS.objc_msgSend_fpret(OS.class_NSFontPanel, OS.sel_minFrameWidthWithTitle_styleMask_, aTitle != null ? aTitle.id : 0, aStyle);
}

public static long windowNumberAtPoint(NSPoint point, long windowNumber) {
	return OS.objc_msgSend(OS.class_NSFontPanel, OS.sel_windowNumberAtPoint_belowWindowWithWindowNumber_, point, windowNumber);
}

}
