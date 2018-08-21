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

import org.eclipse.swt.internal.win32.*;

public class IPersistFile extends IPersist
{
public IPersistFile(long /*int*/ address) {
	super(address);
}
public int IsDirty() {
	return OS.VtblCall(4, address);
}
public int Load(long /*int*/ pszFileName, int dwMode) {
	return OS.VtblCall(5, address, pszFileName, dwMode);
}
public int Save(long /*int*/ pszFileName, boolean fRemember) {
	return COM.VtblCall(6, address, pszFileName, fRemember);
}
public int SaveCompleted(long /*int*/ pszFileName) {
	return OS.VtblCall(7, address, pszFileName);
}
public int GetCurFile(long /*int*/ [] ppszFileName){
	return OS.VtblCall(8, address, ppszFileName);
}
}
