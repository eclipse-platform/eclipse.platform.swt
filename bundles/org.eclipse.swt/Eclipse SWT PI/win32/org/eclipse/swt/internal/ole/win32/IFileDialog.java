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

import org.eclipse.swt.internal.win32.*;

public class IFileDialog extends IUnknown {

public IFileDialog(long address) {
	super(address);
}

// IModalWindow

public int Show(long hwndOwner) {
	return OS.VtblCall(3, address, hwndOwner);
}

// IFileDialog

public int SetOptions(int fos) {
	return OS.VtblCall(9, address, fos);
}

public int GetOptions(int[] pfos) {
	return OS.VtblCall(10, address, pfos);
}

public int SetDefaultFolder(IShellItem psi) {
	return OS.VtblCall(11, address, psi.address);
}

public int SetFolder(IShellItem psi) {
	return OS.VtblCall(12, address, psi.address);
}

public int SetTitle(char[] pszTitle) {
	return OS.VtblCall(17, address, pszTitle);
}

public int GetResult(long[] ppsi) {
	return OS.VtblCall(20, address, ppsi);
}

public int ClearClientData() {
	return OS.VtblCall(25, address);
}

}
