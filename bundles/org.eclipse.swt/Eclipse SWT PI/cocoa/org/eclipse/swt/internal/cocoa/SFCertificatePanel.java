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

public class SFCertificatePanel extends NSPanel {

public SFCertificatePanel() {
	super();
}

public SFCertificatePanel(long id) {
	super(id);
}

public SFCertificatePanel(id id) {
	super(id);
}

public void setAlternateButtonTitle(NSString title) {
	OS.objc_msgSend(this.id, OS.sel_setAlternateButtonTitle_, title != null ? title.id : 0);
}

public void setShowsHelp(boolean showsHelp) {
	OS.objc_msgSend(this.id, OS.sel_setShowsHelp_, showsHelp);
}

public static double minFrameWidthWithTitle(NSString aTitle, long aStyle) {
	return OS.objc_msgSend_fpret(OS.class_SFCertificatePanel, OS.sel_minFrameWidthWithTitle_styleMask_, aTitle != null ? aTitle.id : 0, aStyle);
}

public static long windowNumberAtPoint(NSPoint point, long windowNumber) {
	return OS.objc_msgSend(OS.class_SFCertificatePanel, OS.sel_windowNumberAtPoint_belowWindowWithWindowNumber_, point, windowNumber);
}

}
