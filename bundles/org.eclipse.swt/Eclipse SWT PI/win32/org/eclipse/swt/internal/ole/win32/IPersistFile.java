/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class IPersistFile extends IPersist
{
public IPersistFile(int /*long*/ address) {
	super(address);
}
public int IsDirty() {
	return COM.VtblCall(4, address);
}
public int Load(int /*long*/ pszFileName, int dwMode) {
	return COM.VtblCall(5, address, pszFileName, dwMode);
}
public int Save(int /*long*/ pszFileName, boolean fRemember) {
	return COM.VtblCall(6, address, pszFileName, fRemember);
}
public int SaveCompleted(int /*long*/ pszFileName) {
	return COM.VtblCall(7, address, pszFileName);
}
public int GetCurFile(int /*long*/ [] ppszFileName){
	return COM.VtblCall(8, address, ppszFileName);
}
}
