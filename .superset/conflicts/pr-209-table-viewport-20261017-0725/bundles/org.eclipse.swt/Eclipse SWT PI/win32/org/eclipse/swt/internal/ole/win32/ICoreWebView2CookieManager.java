/*******************************************************************************
 * Copyright (c) 2021 Nikita Nemkin and others.
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

public class ICoreWebView2CookieManager extends IUnknown {

public ICoreWebView2CookieManager(long address) {
	super(address);
}

public int CreateCookie(char[] name, char[] value, char[] domain, char[] path, long[] cookie) {
	return COM.VtblCall(3, address, name, value, domain, path, cookie);
}

public int GetCookies(char[] uri, IUnknown handler) {
	return COM.VtblCall(5, address, uri, handler.getAddress());
}

public int AddOrUpdateCookie(ICoreWebView2Cookie cookie) {
	return COM.VtblCall(6, address, cookie.getAddress());
}

public int DeleteCookie(ICoreWebView2Cookie cookie) {
	return COM.VtblCall(7, address, cookie.getAddress());
}

}
