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

public class ICoreWebView2WindowFeatures extends IUnknown {

	public ICoreWebView2WindowFeatures(long address) {
		super(address);
	}

	public int get_HasPosition(int[] value) {
		return COM.VtblCall(3, address, value);
	}

	public int get_HasSize(int[] value) {
		return COM.VtblCall(4, address, value);
	}

	public int get_Left(int[] value) {
		return COM.VtblCall(5, address, value);
	}

	public int get_Top(int[] value) {
		return COM.VtblCall(6, address, value);
	}

	public int get_Height(int[] value) {
		return COM.VtblCall(7, address, value);
	}

	public int get_Width(int[] value) {
		return COM.VtblCall(8, address, value);
	}

	public int get_ShouldDisplayMenuBar(int[] value) {
		return COM.VtblCall(9, address, value);
	}

	public int get_ShouldDisplayStatus(int[] value) {
		return COM.VtblCall(10, address, value);
	}

	public int get_ShouldDisplayToolbar(int[] value) {
		return COM.VtblCall(11, address, value);
	}

	public int get_ShouldDisplayScrollBars(int[] value) {
		return COM.VtblCall(12, address, value);
	}
}
