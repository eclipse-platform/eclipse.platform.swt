package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class STATSTG {
	public int   pwcsName;
	public int   type; 
	public long  cbSize;
	//FILETIME mtime;
	public int   mtime_dwLowDateTime;
	public int   mtime_dwHighDateTime; 
	//FILETIME ctime;
	public int   ctime_dwLowDateTime;
	public int   ctime_dwHighDateTime; 
	//FILETIME atime;
	public int   atime_dwLowDateTime;
	public int   atime_dwHighDateTime; 
	public int   grfMode;
	public int   grfLocksSupported; 
	//GUID clsid;
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
