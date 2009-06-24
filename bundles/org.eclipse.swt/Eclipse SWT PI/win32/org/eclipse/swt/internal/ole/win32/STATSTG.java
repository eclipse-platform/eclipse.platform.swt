/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
	/** @field cast=(LPWSTR) */
	public int /*long*/ pwcsName;
	public int type; 
	/** @field accessor=cbSize.QuadPart */
	public long cbSize;
//	FILETIME mtime;
	/** @field accessor=mtime.dwLowDateTime */
	public int mtime_dwLowDateTime;
	/** @field accessor=mtime.dwHighDateTime */
	public int mtime_dwHighDateTime; 
//	FILETIME ctime;
	/** @field accessor=ctime.dwLowDateTime */
	public int ctime_dwLowDateTime;
	/** @field accessor=ctime.dwHighDateTime */
	public int ctime_dwHighDateTime; 
//	FILETIME atime;
	/** @field accessor=atime.dwLowDateTime */
	public int atime_dwLowDateTime;
	/** @field accessor=atime.dwHighDateTime */
	public int atime_dwHighDateTime; 
	public int grfMode;
	public int grfLocksSupported; 
//	GUID clsid;
	/** @field accessor=clsid.Data1 */
	public int clsid_Data1;
	/** @field accessor=clsid.Data2 */
	public short clsid_Data2;
	/** @field accessor=clsid.Data3 */
	public short clsid_Data3;
	/** @field accessor=clsid.Data4 */
	public byte[] clsid_Data4 = new byte[8];
	public int grfStateBits; 
	public int reserved;
	public static final int sizeof = COM.STATSTG_sizeof ();
}
