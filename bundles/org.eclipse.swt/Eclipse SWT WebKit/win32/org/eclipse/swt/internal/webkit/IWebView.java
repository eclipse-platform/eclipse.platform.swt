/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	return COM.VtblCall (3, getAddress (), mimeType, canShow);
}

public int initWithFrame (RECT frame, long /*int*/ frameName, long /*int*/ groupName) {
	return COM.VtblCall (9, getAddress(), frame, frameName, groupName);
}

public int setUIDelegate (long /*int*/ delegate) {
	return COM.VtblCall (10, getAddress (), delegate);
}

public int setResourceLoadDelegate (long /*int*/ delegate) {
	return COM.VtblCall (12, getAddress (), delegate);
}

public int setDownloadDelegate (long /*int*/ delegate) {
	return COM.VtblCall (14, getAddress (), delegate);
}

public int setFrameLoadDelegate (long /*int*/ delegate) {
	return COM.VtblCall (16, getAddress (), delegate);
}

public int setPolicyDelegate (long /*int*/ delegate) {
	return COM.VtblCall (18, getAddress (), delegate);
}

public int mainFrame (long /*int*/[] frame) {
	return COM.VtblCall (20, getAddress (), frame);
}

public int goBack (int[] succeeded) {
	return COM.VtblCall (24, getAddress(), succeeded);
}

public int goForward (int[] succeeded) {
	return COM.VtblCall (25, getAddress(), succeeded);
}

public int setCustomUserAgent (long /*int*/ valueString) {
	return COM.VtblCall (31, getAddress (), valueString);
}

public int setPreferences (long /*int*/ prefs) {
	return COM.VtblCall (41, getAddress (), prefs);
}

public int preferences (long /*int*/[] prefs) {
	return COM.VtblCall (42, getAddress (), prefs);
}

public int setHostWindow (long /*int*/ window) {
	return COM.VtblCall (45, getAddress (), window);
}

public int hostWindow (long /*int*/[] window) {
	return COM.VtblCall (46, getAddress (), window);
}

public int estimatedProgress (long /*int*/ estimatedProgress) {
	return COM.VtblCall (51, getAddress (), estimatedProgress);
}

}
