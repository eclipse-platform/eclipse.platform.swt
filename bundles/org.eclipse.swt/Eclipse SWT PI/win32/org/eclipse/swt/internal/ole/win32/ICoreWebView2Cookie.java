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

public class ICoreWebView2Cookie extends IUnknown {

public ICoreWebView2Cookie(long address) {
	super(address);
}

public int get_Name(long[] name) {
	return COM.VtblCall(3, address, name);
}

public int get_Value(long[] value) {
	return COM.VtblCall(4, address, value);
}

public int put_Value(char[] value) {
	return COM.VtblCall(5, address, value);
}

public int put_Expires(double expires) {
	return COM.VtblCall(9, address, expires);
}

public int put_IsHttpOnly(boolean isHttpOnly) {
	return COM.VtblCall(11, address, isHttpOnly ? 1 : 0);
}

public int put_IsSecure(boolean isSecure) {
	return COM.VtblCall(15, address, isSecure ? 1 : 0);
}

public int get_IsSession(int[] isSession) {
	return COM.VtblCall(16, address, isSession);
}

}
