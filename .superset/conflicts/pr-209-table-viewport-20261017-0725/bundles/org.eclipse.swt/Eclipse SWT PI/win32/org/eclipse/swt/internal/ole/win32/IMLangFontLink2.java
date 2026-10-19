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

public class IMLangFontLink2 extends IUnknown {

public IMLangFontLink2(long address) {
	super(address);
}

// IMLangCodePages

public int GetStrCodePages(char[] pszSrc, int cchSrc, int dwPriorityCodePages, int[] pdwCodePages, int[] pcchCodePages) {
	return COM.VtblCall(4, address, pszSrc, cchSrc, dwPriorityCodePages, pdwCodePages, pcchCodePages);
}

// IMLangFontLink2

public int ReleaseFont(long hFont) {
	return COM.VtblCall(8, address, hFont);
}

public int MapFont(long hDC, int dwCodePages, char chSrc, long[] phDestFont) {
	return COM.VtblCall(10, address, hDC, dwCodePages, chSrc, phDestFont);
}

}
