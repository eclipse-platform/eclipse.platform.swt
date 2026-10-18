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

public class ICoreWebView2ContextMenuRequestedEventArgs extends IUnknown {

	public ICoreWebView2ContextMenuRequestedEventArgs(long address) {
		super(address);
	}

	public int get_Location(long[] location) {
		return COM.VtblCall(5, address, location);
	}

	public int put_Handled(boolean value) {
		return COM.VtblCall(8, address, value ? 1 : 0);
	}

	public int get_Handled(int[] value) {
		return COM.VtblCall(9, address, value);
	}

}
