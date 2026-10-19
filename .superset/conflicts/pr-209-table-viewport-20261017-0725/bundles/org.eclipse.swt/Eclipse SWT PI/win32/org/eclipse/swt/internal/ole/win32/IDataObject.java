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

public class IDataObject extends IUnknown {
public IDataObject(long address) {
	super(address);
}
public int EnumFormatEtc(int dwDirection, long[] ppenumFormatetc) {
	return COM.VtblCall(8, address, dwDirection, ppenumFormatetc);
}
public int GetData(FORMATETC pFormatetc, STGMEDIUM pmedium) {
	//Called by a data consumer to obtain data from a source data object.
	//The GetData method renders the data described in the specified FORMATETC
	//structure and transfers it through the specified STGMEDIUM structure.
	//The caller then assumes responsibility for releasing the STGMEDIUM structure.
	return COM.VtblCall(3, address, pFormatetc, pmedium);
}
public int QueryGetData(FORMATETC pFormatetc) {
	return COM.VtblCall(5, address, pFormatetc);
}
}
