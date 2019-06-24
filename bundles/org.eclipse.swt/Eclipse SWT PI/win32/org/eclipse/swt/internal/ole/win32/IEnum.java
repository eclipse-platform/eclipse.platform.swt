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

public class IEnum extends IUnknown
{
public IEnum(long address) {
	super(address);
}
public int Clone( long[] ppenum  ){
	return COM.VtblCall(6, address, ppenum);
}
public int Next(int celt, long rgelt, int[] pceltFetched  ){
	return COM.VtblCall(3, address, celt, rgelt, pceltFetched);
}
public int Reset() {
	return COM.VtblCall(5, address);
}
public int Skip(int celt){
	return COM.VtblCall(4, address, celt);
}
}
