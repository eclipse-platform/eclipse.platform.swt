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

public class NSPrintPanel extends NSObject {

public NSPrintPanel() {
	super();
}

public NSPrintPanel(int /*long*/ id) {
	super(id);
}

public NSPrintPanel(id id) {
	super(id);
}

public int options() {
	return OS.objc_msgSend(this.id, OS.sel_options);
}

public static NSPrintPanel printPanel() {
	int result = OS.objc_msgSend(OS.class_NSPrintPanel, OS.sel_printPanel);
	return result != 0 ? new NSPrintPanel(result) : null;
}

public int runModalWithPrintInfo(NSPrintInfo printInfo) {
	return OS.objc_msgSend(this.id, OS.sel_runModalWithPrintInfo_, printInfo != null ? printInfo.id : 0);
}

public void setOptions(int options) {
	OS.objc_msgSend(this.id, OS.sel_setOptions_, options);
}

}
