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
 *     SAP SE - initial implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class ICoreWebView2_10 extends ICoreWebView2_2 {

public ICoreWebView2_10(long address) {
	super(address);
}

public int add_BasicAuthenticationRequested(IUnknown eventHandler, long[] token) {
	return COM.VtblCall(97, address, eventHandler.getAddress(), token);
}

public int remove_BasicAuthenticationRequested(long[] token) {
	return COM.VtblCall(98, address, token);
}

}
