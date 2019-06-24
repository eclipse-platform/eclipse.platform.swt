/*******************************************************************************
 * Copyright (c) 2010, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.webkit;


import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;

public class IWebView extends IUnknown {

public IWebView (long address) {
	super (address);
}

public int canShowMIMEType (long mimeType, int[] canShow) {
	return COM.VtblCall (3, getAddress (), mimeType, canShow);
}

public int initWithFrame (RECT frame, long frameName, long groupName) {
	return COM.VtblCall (9, getAddress(), frame, frameName, groupName);
}

public int setUIDelegate (long delegate) {
	return COM.VtblCall (10, getAddress (), delegate);
}

public int setResourceLoadDelegate (long delegate) {
	return COM.VtblCall (12, getAddress (), delegate);
}

public int setDownloadDelegate (long delegate) {
	return COM.VtblCall (14, getAddress (), delegate);
}

public int setFrameLoadDelegate (long delegate) {
	return COM.VtblCall (16, getAddress (), delegate);
}

public int setPolicyDelegate (long delegate) {
	return COM.VtblCall (18, getAddress (), delegate);
}

public int mainFrame (long[] frame) {
	return COM.VtblCall (20, getAddress (), frame);
}

public int goBack (int[] succeeded) {
	return COM.VtblCall (24, getAddress(), succeeded);
}

public int goForward (int[] succeeded) {
	return COM.VtblCall (25, getAddress(), succeeded);
}

public int setCustomUserAgent (long valueString) {
	return COM.VtblCall (31, getAddress (), valueString);
}

public int setPreferences (long prefs) {
	return COM.VtblCall (41, getAddress (), prefs);
}

public int preferences (long[] prefs) {
	return COM.VtblCall (42, getAddress (), prefs);
}

public int setHostWindow (long window) {
	return COM.VtblCall (45, getAddress (), window);
}

public int hostWindow (long[] window) {
	return COM.VtblCall (46, getAddress (), window);
}

public int estimatedProgress (long estimatedProgress) {
	return COM.VtblCall (51, getAddress (), estimatedProgress);
}

}
