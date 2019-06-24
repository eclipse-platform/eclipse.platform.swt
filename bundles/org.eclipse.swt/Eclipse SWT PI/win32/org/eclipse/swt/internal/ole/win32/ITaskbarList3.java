/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Nikita Nemkin <nikita@nemkin.ru> - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class ITaskbarList3 extends IUnknown {

public ITaskbarList3(long address) {
	super(address);
}

public int SetProgressValue(long hwnd, long ullCompleted, long ullTotal) {
	return COM.VtblCall(9, address, hwnd, ullCompleted, ullTotal);
}

public int SetProgressState(long hwnd, int tbpFlags) {
	return COM.VtblCall(10, address, hwnd, tbpFlags);
}

public int SetOverlayIcon(long hwnd, long hIcon, long pszDescription) {
	return COM.VtblCall(18, address, hwnd, hIcon, pszDescription);
}

}
