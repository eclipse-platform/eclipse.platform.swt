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

public class IWebPreferences extends IUnknown {

public IWebPreferences (long /*int*/ address) {
	super (address);
}

public int initWithIdentifier (long /*int*/ identifier, long /*int*/[] preferences) {
	return COM.VtblCall (4, getAddress (), identifier, preferences);
}

public int setJavaEnabled (int enabled) {
	return OS.VtblCall (33, getAddress (), enabled);
}

public int setJavaScriptEnabled (int enabled) {
	return OS.VtblCall (35, getAddress (), enabled);
}

public int setJavaScriptCanOpenWindowsAutomatically (int enabled) {
	return OS.VtblCall (37, getAddress (), enabled);
}

public int setTabsToLinks (int enabled) {
	return OS.VtblCall (52, getAddress (), enabled);
}

public int setFontSmoothing (int smoothingType) {
	return OS.VtblCall (63, getAddress (), smoothingType);
}

}
