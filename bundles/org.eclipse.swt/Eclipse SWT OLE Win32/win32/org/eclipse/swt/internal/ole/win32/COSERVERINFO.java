package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class COSERVERINFO {
	public int dwReserved1;   
	public int pwszName;    
	public int pAuthInfo;
	public int dwReserved2;

	public static final int sizeof = 16; 
}
