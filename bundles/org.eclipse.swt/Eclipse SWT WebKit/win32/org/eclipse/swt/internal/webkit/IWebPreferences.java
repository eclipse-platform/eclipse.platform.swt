/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
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

public class IWebPreferences extends IUnknown {

public IWebPreferences (int /*long*/ address) {
	super (address);
}

public int isJavaEnabled (int [] enabled) {
	return COM.VtblCall (32, getAddress (), enabled);
}

public int isJavaScriptEnabled (int [] enabled) {
	return COM.VtblCall (34, getAddress (), enabled);
}

public int javaScriptCanOpenWindowsAutomatically (int [] enabled) {
	return COM.VtblCall (36, getAddress (), enabled);
}

public int setCookieStorageAcceptPolicy (int acceptPolicy) {
	return COM.VtblCall (67, getAddress (), acceptPolicy);
}

public int setJavaEnabled (boolean enabled) {
	return COM.VtblCall (33, getAddress (), enabled);
}

public int setJavaScriptCanOpenWindowsAutomatically (boolean enabled) {
	return COM.VtblCall (37, getAddress (), enabled);
}

public int setJavaScriptEnabled (boolean enabled) {
	return COM.VtblCall (35, getAddress (), enabled);
}

public int setFontSmoothing (int smoothingType) {
	return COM.VtblCall (63, getAddress (), smoothingType);
}

public int setTabsToLinks (boolean enabled) {
	return COM.VtblCall (52, getAddress (), enabled);
}

public int standardPreferences (int /*long*/[] preferences) {
	return COM.VtblCall (3, getAddress (), preferences);
}

}
