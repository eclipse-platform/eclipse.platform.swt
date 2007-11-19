/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSFontPanel extends NSPanel {

public NSFontPanel() {
	super();
}

public NSFontPanel(int id) {
	super(id);
}

public NSView accessoryView() {
	int result = OS.objc_msgSend(this.id, OS.sel_accessoryView);
	return result != 0 ? new NSView(result) : null;
}

public boolean isEnabled() {
	return OS.objc_msgSend(this.id, OS.sel_isEnabled) != 0;
}

public NSFont panelConvertFont(NSFont fontObj) {
	int result = OS.objc_msgSend(this.id, OS.sel_panelConvertFont_1, fontObj != null ? fontObj.id : 0);
	return result != 0 ? new NSFont(result) : null;
}

public void reloadDefaultFontFamilies() {
	OS.objc_msgSend(this.id, OS.sel_reloadDefaultFontFamilies);
}

public void setAccessoryView(NSView aView) {
	OS.objc_msgSend(this.id, OS.sel_setAccessoryView_1, aView != null ? aView.id : 0);
}

public void setEnabled(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setEnabled_1, flag);
}

public void setPanelFont(NSFont fontObj, boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setPanelFont_1isMultiple_1, fontObj != null ? fontObj.id : 0, flag);
}

public static NSFontPanel sharedFontPanel() {
	int result = OS.objc_msgSend(OS.class_NSFontPanel, OS.sel_sharedFontPanel);
	return result != 0 ? new NSFontPanel(result) : null;
}

public static boolean sharedFontPanelExists() {
	return OS.objc_msgSend(OS.class_NSFontPanel, OS.sel_sharedFontPanelExists) != 0;
}

public boolean worksWhenModal() {
	return OS.objc_msgSend(this.id, OS.sel_worksWhenModal) != 0;
}

}
