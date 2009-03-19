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

public class NSColorPanel extends NSPanel {

public NSColorPanel() {
	super();
}

public NSColorPanel(int /*long*/ id) {
	super(id);
}

public NSColorPanel(id id) {
	super(id);
}

public NSColor color() {
	int /*long*/ result = OS.objc_msgSend(this.id, OS.sel_color);
	return result != 0 ? new NSColor(result) : null;
}

public void setColor(NSColor color) {
	OS.objc_msgSend(this.id, OS.sel_setColor_, color != null ? color.id : 0);
}

public static NSColorPanel sharedColorPanel() {
	int /*long*/ result = OS.objc_msgSend(OS.class_NSColorPanel, OS.sel_sharedColorPanel);
	return result != 0 ? new NSColorPanel(result) : null;
}

public static float /*double*/ minFrameWidthWithTitle(NSString aTitle, int /*long*/ aStyle) {
	return (float)OS.objc_msgSend_fpret(OS.class_NSColorPanel, OS.sel_minFrameWidthWithTitle_styleMask_, aTitle != null ? aTitle.id : 0, aStyle);
}

}
