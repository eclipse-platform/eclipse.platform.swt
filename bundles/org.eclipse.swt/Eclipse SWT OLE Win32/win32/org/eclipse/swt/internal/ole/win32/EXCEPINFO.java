package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
