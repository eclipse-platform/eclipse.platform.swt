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

public class IFileDialog extends IUnknown {

public IFileDialog(long address) {
	super(address);
}

// IModalWindow

public int Show(long hwndOwner) {
	return COM.VtblCall(3, address, hwndOwner);
}

// IFileDialog

public int SetFileTypes(int cFileTypes, long [] rgFilterSpec) {
	return COM.VtblCall(4, address, cFileTypes, rgFilterSpec);
}

public int SetFileTypeIndex(int iFileType) {
	return COM.VtblCall(5, address, iFileType);
}

public int GetFileTypeIndex(int [] piFileType) {
	return COM.VtblCall(6, address, piFileType);
}

public int SetOptions(int fos) {
	return COM.VtblCall(9, address, fos);
}

public int GetOptions(int[] pfos) {
	return COM.VtblCall(10, address, pfos);
}

public int SetDefaultFolder(IShellItem psi) {
	return COM.VtblCall(11, address, psi.address);
}

public int SetFolder(IShellItem psi) {
	return COM.VtblCall(12, address, psi.address);
}

public int SetFileName(char [] pszName) {
	return COM.VtblCall(15, address, pszName);
}

public int SetTitle(char[] pszTitle) {
	return COM.VtblCall(17, address, pszTitle);
}

public int GetResult(long[] ppsi) {
	return COM.VtblCall(20, address, ppsi);
}

public int SetDefaultExtension(char[] pszDefaultExtension) {
	return COM.VtblCall(22, address, pszDefaultExtension);
}

public int ClearClientData() {
	return COM.VtblCall(25, address);
}

// IFileOpenDialog

public int GetResults(long [] ppenum) {
	return COM.VtblCall(27, address, ppenum);
}

}
