/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class STATSTG {
	public int   pwcsName;
	public int   type; 
	public long  cbSize;
//	FILETIME mtime;
	public int   mtime_dwLowDateTime;
	public int   mtime_dwHighDateTime; 
//	FILETIME ctime;
	public int   ctime_dwLowDateTime;
	public int   ctime_dwHighDateTime; 
//	FILETIME atime;
	public int   atime_dwLowDateTime;
	public int   atime_dwHighDateTime; 
	public int   grfMode;
	public int   grfLocksSupported; 
//	GUID clsid;
	public int   clsid_data1;
	public short clsid_data2;
	public short clsid_data3;
	public byte  clsid_b0;
	public byte  clsid_b1;
	public byte  clsid_b2;
	public byte  clsid_b3;
	public byte  clsid_b4;
	public byte  clsid_b5;
	public byte  clsid_b6;
	public byte  clsid_b7;
	public int   grfStateBits; 
	public int   reserved;
	public static final int sizeof = 72;
}
