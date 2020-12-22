/*******************************************************************************
 * Copyright (c) 2020 Nikita Nemkin and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Nikita Nemkin <nikita@nemkin.ru> - initial implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class ICoreWebView2NavigationStartingEventArgs extends IUnknown {

public ICoreWebView2NavigationStartingEventArgs(long address) {
	super(address);
}

public int get_Uri(long[] uri) {
	return COM.VtblCall(3, address, uri);
}

public int get_IsUserInitiated(int[] isUserInitiated) {
	return COM.VtblCall(4, address, isUserInitiated);
}

public int get_IsRedirected(int[] isRedirected) {
	return COM.VtblCall(5, address, isRedirected);
}

public int get_RequestHeaders(long[] requestHeaders) {
	return COM.VtblCall(6, address, requestHeaders);
}

public int get_Cancel(int[] cancel) {
	return COM.VtblCall(7, address, cancel);
}

public int put_Cancel(boolean cancel) {
	return COM.VtblCall(8, address, cancel ? 1 : 0);
}

public int get_NavigationId(long[] navigationId) {
	return COM.VtblCall(9, address, navigationId);
}

}
