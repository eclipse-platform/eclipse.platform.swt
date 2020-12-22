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

public class ICoreWebView2EnvironmentOptions extends IUnknown {

public ICoreWebView2EnvironmentOptions(long address) {
	super(address);
}

public int put_AdditionalBrowserArguments(char[] value) {
	return COM.VtblCall(4, address, value);
}

public int put_Language(char[] value) {
	return COM.VtblCall(6, address, value);
}

public int put_TargetCompatibleBrowserVersion(char[] value) {
	return COM.VtblCall(8, address, value);
}

public int put_AllowSingleSignOnUsingOSPrimaryAccount(int[] allow) {
	return COM.VtblCall(10, address, allow);
}

}
