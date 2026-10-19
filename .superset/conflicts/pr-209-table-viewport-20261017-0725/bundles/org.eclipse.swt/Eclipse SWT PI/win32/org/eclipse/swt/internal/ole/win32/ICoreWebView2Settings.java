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

public class ICoreWebView2Settings extends IUnknown {

public ICoreWebView2Settings(long address) {
	super(address);
}

public int get_IsScriptEnabled(int[] isScriptEnabled) {
	return COM.VtblCall(3, address, isScriptEnabled);
}

public int put_IsScriptEnabled(boolean isScriptEnabled) {
	return COM.VtblCall(4, address, isScriptEnabled ? 1 : 0);
}

public int get_IsWebMessageEnabled(int[] isWebMessageEnabled) {
	return COM.VtblCall(5, address, isWebMessageEnabled);
}

public int put_IsWebMessageEnabled(boolean isWebMessageEnabled) {
	return COM.VtblCall(6, address, isWebMessageEnabled ? 1 : 0);
}

public int get_AreDefaultScriptDialogsEnabled(int[] areDefaultScriptDialogsEnabled) {
	return COM.VtblCall(7, address, areDefaultScriptDialogsEnabled);
}

public int put_AreDefaultScriptDialogsEnabled(boolean areDefaultScriptDialogsEnabled) {
	return COM.VtblCall(8, address, areDefaultScriptDialogsEnabled ? 1 : 0);
}

public int get_IsStatusBarEnabled(int[] isStatusBarEnabled) {
	return COM.VtblCall(9, address, isStatusBarEnabled);
}

public int put_IsStatusBarEnabled(boolean isStatusBarEnabled) {
	return COM.VtblCall(10, address, isStatusBarEnabled ? 1 : 0);
}

public int get_AreDevToolsEnabled(int[] areDevToolsEnabled) {
	return COM.VtblCall(11, address, areDevToolsEnabled);
}

public int put_AreDevToolsEnabled(boolean areDevToolsEnabled) {
	return COM.VtblCall(12, address, areDevToolsEnabled ? 1 : 0);
}

public int get_AreDefaultContextMenusEnabled(int[] enabled) {
	return COM.VtblCall(13, address, enabled);
}

public int put_AreDefaultContextMenusEnabled(boolean enabled) {
	return COM.VtblCall(14, address, enabled ? 1 : 0);
}

public int get_AreHostObjectsAllowed(int[] allowed) {
	return COM.VtblCall(15, address, allowed);
}

public int put_AreHostObjectsAllowed(boolean allowed) {
	return COM.VtblCall(16, address, allowed ? 1 : 0);
}

public int get_IsZoomControlEnabled(int[] enabled) {
	return COM.VtblCall(17, address, enabled);
}

public int put_IsZoomControlEnabled(boolean enabled) {
	return COM.VtblCall(18, address, enabled ? 1 : 0);
}

public int get_IsBuiltInErrorPageEnabled(int[] enabled) {
	return COM.VtblCall(19, address, enabled);
}

public int put_IsBuiltInErrorPageEnabled(boolean enabled) {
	return COM.VtblCall(20, address, enabled ? 1 : 0);
}

}
