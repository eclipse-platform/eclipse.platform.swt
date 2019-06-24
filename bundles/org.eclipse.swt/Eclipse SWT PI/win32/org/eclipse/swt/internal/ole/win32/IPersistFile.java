/*******************************************************************************
 * Copyright (c) 2009, 2017 IBM Corporation and others.
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

public class IPersistFile extends IPersist
{
public IPersistFile(long address) {
	super(address);
}
public int IsDirty() {
	return COM.VtblCall(4, address);
}
public int Load(long pszFileName, int dwMode) {
	return COM.VtblCall(5, address, pszFileName, dwMode);
}
public int Save(long pszFileName, boolean fRemember) {
	return COM.VtblCall(6, address, pszFileName, fRemember);
}
public int SaveCompleted(long pszFileName) {
	return COM.VtblCall(7, address, pszFileName);
}
public int GetCurFile(long [] ppszFileName){
	return COM.VtblCall(8, address, ppszFileName);
}
}
