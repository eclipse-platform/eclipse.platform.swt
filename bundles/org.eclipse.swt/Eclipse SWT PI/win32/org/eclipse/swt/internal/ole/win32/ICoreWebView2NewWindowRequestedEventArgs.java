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

public class ICoreWebView2NewWindowRequestedEventArgs extends IUnknown {

public ICoreWebView2NewWindowRequestedEventArgs(long address) {
	super(address);
}

public int get_Uri(long[] uri) {
	return COM.VtblCall(3, address, uri);
}

public int put_NewWindow(long newWindow) {
	return COM.VtblCall(4, address, newWindow);
}

public int get_NewWindow(long[] newWindow) {
	return COM.VtblCall(5, address, newWindow);
}

public int put_Handled(boolean handled) {
	return COM.VtblCall(6, address, handled ? 1 : 0);
}

public int get_Handled(int[] handled) {
	return COM.VtblCall(7, address, handled);
}

public int get_IsUserInitiated(int[] isUserInitiated) {
	return COM.VtblCall(8, address, isUserInitiated);
}

public int GetDeferral(long[] deferral) {
	return COM.VtblCall(9, address, deferral);
}

public int get_WindowFeatures(long[] value) {
	return COM.VtblCall(10, address, value);
}

}
