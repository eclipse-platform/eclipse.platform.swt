package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class COSERVERINFO {
	public int dwReserved1;   
	public int pwszName;    
	public int pAuthInfo;
	public int dwReserved2;

	public static final int sizeof = 16; 
}
