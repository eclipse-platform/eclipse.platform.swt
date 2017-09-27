/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

import org.eclipse.swt.internal.win32.*;

public class IEnum extends IUnknown
{
public IEnum(long /*int*/ address) {
	super(address);
}
public int Clone( long /*int*/[] ppenum  ){
	return OS.VtblCall(6, address, ppenum);
}
public int Next(int celt, long /*int*/ rgelt, int[] pceltFetched  ){
	return COM.VtblCall(3, address, celt, rgelt, pceltFetched);
}
public int Reset() {
	return OS.VtblCall(5, address);
}
public int Skip(int celt){
	return OS.VtblCall(4, address, celt);
}
}
