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
 *     Nikita Nemkin <nikita@nemkin.ru> - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class IShellLink extends IUnknown {

public IShellLink(long address) {
	super(address);
}

public int SetDescription(char[] pszName) {
	return COM.VtblCall(7, address, pszName);
}

public int SetArguments(char[] pszArgs) {
	return COM.VtblCall(11, address, pszArgs);
}

public int SetIconLocation(char[] pszIconPath, int iIcon) {
	return COM.VtblCall(17, address, pszIconPath, iIcon);
}

public int SetPath(char[] pszFile) {
	return COM.VtblCall(20, address, pszFile);
}

}
