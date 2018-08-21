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

public IWebView (long /*int*/ address) {
	super (address);
}

public int canShowMIMEType (long /*int*/ mimeType, int[] canShow) {
	return OS.VtblCall (3, getAddress (), mimeType, canShow);
}

public int initWithFrame (RECT frame, long /*int*/ frameName, long /*int*/ groupName) {
	return COM.VtblCall (9, getAddress(), frame, frameName, groupName);
}

public int setUIDelegate (long /*int*/ delegate) {
	return OS.VtblCall (10, getAddress (), delegate);
}

public int setResourceLoadDelegate (long /*int*/ delegate) {
	return OS.VtblCall (12, getAddress (), delegate);
}

public int setDownloadDelegate (long /*int*/ delegate) {
	return OS.VtblCall (14, getAddress (), delegate);
}

public int setFrameLoadDelegate (long /*int*/ delegate) {
	return OS.VtblCall (16, getAddress (), delegate);
}

public int setPolicyDelegate (long /*int*/ delegate) {
	return OS.VtblCall (18, getAddress (), delegate);
}

public int mainFrame (long /*int*/[] frame) {
	return OS.VtblCall (20, getAddress (), frame);
}

public int goBack (int[] succeeded) {
	return OS.VtblCall (24, getAddress(), succeeded);
}

public int goForward (int[] succeeded) {
	return OS.VtblCall (25, getAddress(), succeeded);
}

public int setCustomUserAgent (long /*int*/ valueString) {
	return OS.VtblCall (31, getAddress (), valueString);
}

public int setPreferences (long /*int*/ prefs) {
	return OS.VtblCall (41, getAddress (), prefs);
}

public int preferences (long /*int*/[] prefs) {
	return OS.VtblCall (42, getAddress (), prefs);
}

public int setHostWindow (long /*int*/ window) {
	return OS.VtblCall (45, getAddress (), window);
}

public int hostWindow (long /*int*/[] window) {
	return OS.VtblCall (46, getAddress (), window);
}

public int estimatedProgress (long /*int*/ estimatedProgress) {
	return OS.VtblCall (51, getAddress (), estimatedProgress);
}

}
