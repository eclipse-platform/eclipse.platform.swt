package org.eclipse.swt.internal.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class CHOOSEFONT {
	public int lStructSize;     
	public int hwndOwner; 
	public int hDC;     
	public int lpLogFont;     
	public int iPointSize; 
	public int Flags;     
	public int rgbColors;     
	public int lCustData; 
	public int lpfnHook;     
	public int lpTemplateName; 
	public int hInstance;     
	public int lpszStyle; 
	public short nFontType;     
	public short ___MISSING_ALIGNMENT__; 
	public int nSizeMin;     
	public int nSizeMax; 
	public static final int sizeof = 60;
}
