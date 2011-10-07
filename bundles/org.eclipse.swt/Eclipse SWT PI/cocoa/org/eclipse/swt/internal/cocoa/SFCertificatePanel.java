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

public class SFCertificatePanel extends NSPanel {

public SFCertificatePanel() {
	super();
}

public SFCertificatePanel(int /*long*/ id) {
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

public static float /*double*/ minFrameWidthWithTitle(NSString aTitle, int /*long*/ aStyle) {
	return (float /*double*/)OS.objc_msgSend_fpret(OS.class_SFCertificatePanel, OS.sel_minFrameWidthWithTitle_styleMask_, aTitle != null ? aTitle.id : 0, aStyle);
}

public static int /*long*/ windowNumberAtPoint(NSPoint point, int /*long*/ windowNumber) {
	return OS.objc_msgSend(OS.class_SFCertificatePanel, OS.sel_windowNumberAtPoint_belowWindowWithWindowNumber_, point, windowNumber);
}

}
