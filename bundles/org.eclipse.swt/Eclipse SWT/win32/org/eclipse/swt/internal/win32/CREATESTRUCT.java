package org.eclipse.swt.internal.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class CREATESTRUCT {
	public int lpCreateParams; 
	public int hInstance; 
	public int hMenu; 
	public int hwndParent; 
	public int cy; 
	public int cx; 
	public int y; 
	public int x; 
	public int style; 
	public int lpszName; 
	public int lpszClass; 
	public int dwExStyle;
	public static final int sizeof = 48;
}
