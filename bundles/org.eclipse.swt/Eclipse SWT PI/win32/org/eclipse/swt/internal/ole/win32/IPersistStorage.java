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

public class IPersistStorage extends IPersist
{
public IPersistStorage(long /*int*/ address) {
	super(address);
}
public int IsDirty() {
	return OS.VtblCall(4, address);
}
public int InitNew(long /*int*/ pStg) {
	return OS.VtblCall(5, address, pStg);
}
public int Load(long /*int*/ pStg) {
	return OS.VtblCall(6, address, pStg);
}
public int Save(long /*int*/ pStgSave, boolean fSameAsLoad) {
	return COM.VtblCall(7, address, pStgSave, fSameAsLoad);
}
public int SaveCompleted(long /*int*/ pStgNew) {
	return OS.VtblCall(8, address, pStgNew);
}
public int HandsOffStorage(){
	return OS.VtblCall(9, address);
}
}
