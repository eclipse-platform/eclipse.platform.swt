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

public class ICustomDestinationList extends IUnknown {

public ICustomDestinationList(long address) {
	super(address);
}

public int SetAppID(char[] pszAppID) {
	return OS.VtblCall(3, address, pszAppID);
}

public int BeginList(int[] pcMinSlots, GUID riid, long[] ppv) {
	return COM.VtblCall(4, address, pcMinSlots, riid, ppv);
}

public int AppendCategory(char[] pszCategory, IObjectArray poa) {
	return OS.VtblCall(5, address, pszCategory, poa.address);
}

public int AddUserTasks(IUnknown poa) {
	return OS.VtblCall(7, address, poa.address);
}

public int CommitList() {
	return OS.VtblCall(8, address);
}

public int DeleteList(char[] pszAppID) {
	return OS.VtblCall(10, address, pszAppID);
}

}
