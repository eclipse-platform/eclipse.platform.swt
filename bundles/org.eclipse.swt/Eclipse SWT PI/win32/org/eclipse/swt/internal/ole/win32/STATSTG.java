/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class STATSTG {
	public int pwcsName;
	public int type; 
	public long cbSize;
//	FILETIME mtime;
	public int mtime_dwLowDateTime;
	public int mtime_dwHighDateTime; 
//	FILETIME ctime;
	public int ctime_dwLowDateTime;
	public int ctime_dwHighDateTime; 
//	FILETIME atime;
	public int atime_dwLowDateTime;
	public int atime_dwHighDateTime; 
	public int grfMode;
	public int grfLocksSupported; 
//	GUID clsid;
	public int clsid_Data1;
	public short clsid_Data2;
	public short clsid_Data3;
	public byte[] clsid_Data4 = new byte[8];
	public int grfStateBits; 
	public int reserved;
	public static final int sizeof = 72;
}
