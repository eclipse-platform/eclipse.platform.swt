/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

import org.eclipse.swt.internal.win32.*;

public class IUnknown
{
	long /*int*/ address;
public IUnknown(long /*int*/ address) {
	this.address = address;
}
public int AddRef() {
	return OS.VtblCall(1, address);
}
public long /*int*/ getAddress() {
	return address;
}
public int QueryInterface(GUID riid, long /*int*/[] ppvObject) {
	return COM.VtblCall(0, address, riid, ppvObject);
}
public int Release() {
	return OS.VtblCall(2, address);
}
}
