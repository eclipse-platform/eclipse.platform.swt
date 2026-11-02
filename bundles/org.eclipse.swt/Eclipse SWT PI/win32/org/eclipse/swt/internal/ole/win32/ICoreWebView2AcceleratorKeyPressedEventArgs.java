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

public class ICoreWebView2AcceleratorKeyPressedEventArgs extends IUnknown {

	public ICoreWebView2AcceleratorKeyPressedEventArgs(long address) {
		super(address);
	}

	public int get_KeyEventKind(long[] value) {
		return COM.VtblCall(3, address, value);
	}

	public int get_VirtualKey(int[] value) {
		return COM.VtblCall(4, address, value);
	}

	public int get_KeyEventLParam(int[] value) {
		return COM.VtblCall(5, address, value);
	}

	public int get_PhysicalKeyStatus(long[] value) {
		return COM.VtblCall(6, address, value);
	}

	public int get_Handled(int[] value) {
		return COM.VtblCall(7, address, value);
	}

	public int put_Handled(boolean value) {
		return COM.VtblCall(8, address, value ? 1 : 0);
	}

}
