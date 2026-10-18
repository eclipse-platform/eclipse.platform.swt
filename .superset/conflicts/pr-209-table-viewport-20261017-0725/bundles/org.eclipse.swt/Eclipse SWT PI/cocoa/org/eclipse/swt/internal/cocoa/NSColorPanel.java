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

public class NSColorPanel extends NSPanel {

public NSColorPanel() {
	super();
}

public NSColorPanel(long id) {
	super(id);
}

public NSColorPanel(id id) {
	super(id);
}

public void attachColorList(NSColorList colorList) {
	OS.objc_msgSend(this.id, OS.sel_attachColorList_, colorList != null ? colorList.id : 0);
}

public NSColor color() {
	long result = OS.objc_msgSend(this.id, OS.sel_color);
	return result != 0 ? new NSColor(result) : null;
}

public void setColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setColor_, color != null ? color.id : 0);
}

public static NSColorPanel sharedColorPanel() {
	long result = OS.objc_msgSend(OS.class_NSColorPanel, OS.sel_sharedColorPanel);
	return result != 0 ? new NSColorPanel(result) : null;
}

public static double minFrameWidthWithTitle(NSString aTitle, long aStyle) {
	return OS.objc_msgSend_fpret(OS.class_NSColorPanel, OS.sel_minFrameWidthWithTitle_styleMask_, aTitle != null ? aTitle.id : 0, aStyle);
}

public static long windowNumberAtPoint(NSPoint point, long windowNumber) {
	return OS.objc_msgSend(OS.class_NSColorPanel, OS.sel_windowNumberAtPoint_belowWindowWithWindowNumber_, point, windowNumber);
}

}
