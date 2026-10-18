/*******************************************************************************
 * Copyright (c) 2024 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * SAP SE - initial implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class ICoreWebView2BasicAuthenticationResponse extends IUnknown {

	public ICoreWebView2BasicAuthenticationResponse(long address) {
		super(address);
	}

	public int get_UserName(long[] userName) {
		return COM.VtblCall(3, address, userName);
	}

	public int put_UserName(char[] userName) {
		return COM.VtblCall(4, address, userName);
	}

	public int get_Password(long[] password) {
		return COM.VtblCall(5, address, password);
	}

	public int put_Password(char[] password) {
		return COM.VtblCall(6, address, password);
	}

}