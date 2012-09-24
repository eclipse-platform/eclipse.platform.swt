/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSColorPanel extends NSPanel {

public NSColorPanel() {
	super();
}

public NSColorPanel(long /*int*/ id) {
	super(id);
}

public NSColorPanel(id id) {
	super(id);
}

public void attachColorList(NSColorList colorList) {
	OS.objc_msgSend(this.id, OS.sel_attachColorList_, colorList != null ? colorList.id : 0);
}

public NSColor color() {
	long /*int*/ result = OS.objc_msgSend(this.id, OS.sel_color);
	return result != 0 ? new NSColor(result) : null;
}

public void setColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setColor_, color != null ? color.id : 0);
}

public static NSColorPanel sharedColorPanel() {
	long /*int*/ result = OS.objc_msgSend(OS.class_NSColorPanel, OS.sel_sharedColorPanel);
	return result != 0 ? new NSColorPanel(result) : null;
}

public static double /*float*/ minFrameWidthWithTitle(NSString aTitle, long /*int*/ aStyle) {
	return (double /*float*/)OS.objc_msgSend_fpret(OS.class_NSColorPanel, OS.sel_minFrameWidthWithTitle_styleMask_, aTitle != null ? aTitle.id : 0, aStyle);
}

public static long /*int*/ windowNumberAtPoint(NSPoint point, long /*int*/ windowNumber) {
	return OS.objc_msgSend(OS.class_NSColorPanel, OS.sel_windowNumberAtPoint_belowWindowWithWindowNumber_, point, windowNumber);
}

}
