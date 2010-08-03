/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSPanel extends NSWindow {

public NSPanel() {
	super();
}

public NSPanel(int /*long*/ id) {
	super(id);
}

public NSPanel(id id) {
	super(id);
}

public void setBecomesKeyOnlyIfNeeded(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setBecomesKeyOnlyIfNeeded_, flag);
}

public void setFloatingPanel(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setFloatingPanel_, flag);
}

public void setWorksWhenModal(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setWorksWhenModal_, flag);
}

public boolean worksWhenModal() {
	return OS.objc_msgSend_bool(this.id, OS.sel_worksWhenModal);
}

public static float /*double*/ minFrameWidthWithTitle(NSString aTitle, int /*long*/ aStyle) {
	return (float)OS.objc_msgSend_fpret(OS.class_NSPanel, OS.sel_minFrameWidthWithTitle_styleMask_, aTitle != null ? aTitle.id : 0, aStyle);
}

}
