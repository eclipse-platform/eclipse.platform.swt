package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class CHOOSECOLOR {
	public int lStructSize; 
	public int hwndOwner;
	public int hInstance; 
	public int rgbResult;
	public int lpCustColors; 
	public int Flags;
	public int lCustData;
	public int lpfnHook; 
	public int lpTemplateName;
	public static final int sizeof = 36;
}
