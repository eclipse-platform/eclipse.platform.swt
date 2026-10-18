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

public class IPersistStorage extends IPersist
{
public IPersistStorage(long address) {
	super(address);
}
public int InitNew(long pStg) {
	return COM.VtblCall(5, address, pStg);
}
public int Load(long pStg) {
	return COM.VtblCall(6, address, pStg);
}
public int Save(long pStgSave, boolean fSameAsLoad) {
	return COM.VtblCall(7, address, pStgSave, fSameAsLoad ? 1 : 0);
}
public int SaveCompleted(long pStgNew) {
	return COM.VtblCall(8, address, pStgNew);
}
public int HandsOffStorage(){
	return COM.VtblCall(9, address);
}
}
