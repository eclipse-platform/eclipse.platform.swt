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

public class ICoreWebView2_2 extends ICoreWebView2 {

public ICoreWebView2_2(long address) {
	super(address);
}

public int NavigateWithWebResourceRequest(IUnknown request) {
	return COM.VtblCall(63, address, request.address);
}

public int add_DOMContentLoaded(IUnknown handler, long[] token) {
	return COM.VtblCall(64, address, handler.getAddress(), token);
}

public int get_CookieManager(long[] cookieManager) {
	return COM.VtblCall(66, address, cookieManager);
}

}
