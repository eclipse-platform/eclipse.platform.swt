package org.eclipse.swt.internal.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class HELPINFO {
	public int cbSize;
	public int iContextType;
	public int iCtrlId;
	public int hItemHandle;
	public int dwContextId;
//	POINT MousePos
	public int x;
	public int y;
	public static final int sizeof = 28;
}
