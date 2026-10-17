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

public class ICoreWebView2CookieList extends IUnknown {

public ICoreWebView2CookieList(long address) {
	super(address);
}

public int get_Count(int[] count) {
	return COM.VtblCall(3, address, count);
}

public int GetValueAtIndex(int index, long[] cookie) {
	return COM.VtblCall(4, address, index, cookie);
}

}
