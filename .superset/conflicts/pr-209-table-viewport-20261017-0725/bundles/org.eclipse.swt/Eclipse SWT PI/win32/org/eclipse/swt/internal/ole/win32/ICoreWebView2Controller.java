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

import org.eclipse.swt.internal.win32.*;

public class ICoreWebView2Controller extends IUnknown {

public ICoreWebView2Controller(long address) {
	super(address);
}

public int put_IsVisible(boolean isVisible) {
	return COM.VtblCall(4, address, isVisible ? 1 : 0);
}

public int put_Bounds(RECT bounds) {
	return COM.VtblCall_put_Bounds(6, address, bounds);
}

public int MoveFocus(int reason) {
	return COM.VtblCall(12, address, reason);
}

public int add_MoveFocusRequested(IUnknown eventHandler, long[] token) {
	return COM.VtblCall(13, address, eventHandler.address, token);
}

public int add_GotFocus(IUnknown eventHandler, long[] token) {
	return COM.VtblCall(15, address, eventHandler.address, token);
}

public int add_LostFocus(IUnknown eventHandler, long[] token) {
	return COM.VtblCall(17, address, eventHandler.address, token);
}

public int add_AcceleratorKeyPressed(IUnknown eventHandler, long[] token) {
	return COM.VtblCall(19, address, eventHandler.address, token);
}

public int NotifyParentWindowPositionChanged() {
	return COM.VtblCall(23, address);
}

public int Close() {
	return COM.VtblCall(24, address);
}

public int get_CoreWebView2(long[] coreWebView2) {
	return COM.VtblCall(25, address, coreWebView2);
}

}
