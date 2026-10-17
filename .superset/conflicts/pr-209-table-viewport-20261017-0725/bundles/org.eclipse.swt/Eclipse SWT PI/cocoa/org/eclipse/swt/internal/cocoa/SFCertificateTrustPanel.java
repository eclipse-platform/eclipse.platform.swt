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

public class SFCertificateTrustPanel extends SFCertificatePanel {

public SFCertificateTrustPanel() {
	super();
}

public SFCertificateTrustPanel(long id) {
	super(id);
}

public SFCertificateTrustPanel(id id) {
	super(id);
}

public void beginSheetForWindow(NSWindow docWindow, id delegate, long didEndSelector, long contextInfo, long trust, NSString message) {
	OS.objc_msgSend(this.id, OS.sel_beginSheetForWindow_modalDelegate_didEndSelector_contextInfo_trust_message_, docWindow != null ? docWindow.id : 0, delegate != null ? delegate.id : 0, didEndSelector, contextInfo, trust, message != null ? message.id : 0);
}

public static SFCertificateTrustPanel sharedCertificateTrustPanel() {
	long result = OS.objc_msgSend(OS.class_SFCertificateTrustPanel, OS.sel_sharedCertificateTrustPanel);
	return result != 0 ? new SFCertificateTrustPanel(result) : null;
}

public static double minFrameWidthWithTitle(NSString aTitle, long aStyle) {
	return OS.objc_msgSend_fpret(OS.class_SFCertificateTrustPanel, OS.sel_minFrameWidthWithTitle_styleMask_, aTitle != null ? aTitle.id : 0, aStyle);
}

public static long windowNumberAtPoint(NSPoint point, long windowNumber) {
	return OS.objc_msgSend(OS.class_SFCertificateTrustPanel, OS.sel_windowNumberAtPoint_belowWindowWithWindowNumber_, point, windowNumber);
}

}
