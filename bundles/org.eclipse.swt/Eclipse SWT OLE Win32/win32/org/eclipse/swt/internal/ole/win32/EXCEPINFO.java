package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public final class EXCEPINFO
{
	public short wCode;   
	public short wReserved;
	public int bstrSource; 
	public int bstrDescription; 
	public int bstrHelpFile;
	public int dwHelpContext; 
	public int pvReserved;
	public int pfnDeferredFillIn;
	public int scode;

	public static final int sizeof = 32;
}
