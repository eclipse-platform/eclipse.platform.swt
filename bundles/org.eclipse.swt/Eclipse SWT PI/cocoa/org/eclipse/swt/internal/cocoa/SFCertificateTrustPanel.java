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

public class SFCertificateTrustPanel extends SFCertificatePanel {

public SFCertificateTrustPanel() {
	super();
}

public SFCertificateTrustPanel(long /*int*/ id) {
	super(id);
}

public SFCertificateTrustPanel(id id) {
	super(id);
}

public void beginSheetForWindow(NSWindow docWindow, id delegate, long /*int*/ didEndSelector, long /*int*/ contextInfo, long /*int*/ trust, NSString message) {
	OS.objc_msgSend(this.id, OS.sel_beginSheetForWindow_modalDelegate_didEndSelector_contextInfo_trust_message_, docWindow != null ? docWindow.id : 0, delegate != null ? delegate.id : 0, didEndSelector, contextInfo, trust, message != null ? message.id : 0);
}

public static SFCertificateTrustPanel sharedCertificateTrustPanel() {
	long /*int*/ result = OS.objc_msgSend(OS.class_SFCertificateTrustPanel, OS.sel_sharedCertificateTrustPanel);
	return result != 0 ? new SFCertificateTrustPanel(result) : null;
}

public static double /*float*/ minFrameWidthWithTitle(NSString aTitle, long /*int*/ aStyle) {
	return (double /*float*/)OS.objc_msgSend_fpret(OS.class_SFCertificateTrustPanel, OS.sel_minFrameWidthWithTitle_styleMask_, aTitle != null ? aTitle.id : 0, aStyle);
}

public static long /*int*/ windowNumberAtPoint(NSPoint point, long /*int*/ windowNumber) {
	return OS.objc_msgSend(OS.class_SFCertificateTrustPanel, OS.sel_windowNumberAtPoint_belowWindowWithWindowNumber_, point, windowNumber);
}

}
