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

public class ICoreWebView2BasicAuthenticationRequestedEventArgs extends IUnknown {

	public ICoreWebView2BasicAuthenticationRequestedEventArgs(long address) {
		super(address);
	}

	public int get_Uri(long[] value) {
		return COM.VtblCall(3, address, value);
	}

	public int get_Challenge(long[] challenge) {
		return COM.VtblCall(4, address, challenge);
	}

	public int get_Response(long[] response) {
		return COM.VtblCall(5, address, response);
	}

	public int get_Cancel(int[] cancel) {
		return COM.VtblCall(6, address, cancel);
	}

	public int put_Cancel(boolean cancel) {
		return COM.VtblCall(7, address, cancel ? 1 : 0);
	}

	/* 8:
		DECLSPEC_XFGVIRT(ICoreWebView2BasicAuthenticationRequestedEventArgs, GetDeferral)
		HRESULT ( STDMETHODCALLTYPE *GetDeferral )(
			ICoreWebView2BasicAuthenticationRequestedEventArgs * This,
			ICoreWebView2Deferral **deferral);
	*/
}