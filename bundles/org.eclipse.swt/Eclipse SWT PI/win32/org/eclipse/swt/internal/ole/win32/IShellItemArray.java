/*******************************************************************************
 * Copyright (c) 2000, 2020 Nikita Nemkin and others.
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

public class IShellItemArray extends IUnknown {

public IShellItemArray(long address) {
	super(address);
}

public int GetCount(int [] pdwNumItems) {
	return COM.VtblCall(7, address, pdwNumItems);
}

public int GetItemAt(int dwIndex, long [] ppsi) {
	return COM.VtblCall(8, address, dwIndex, ppsi);
}

}
