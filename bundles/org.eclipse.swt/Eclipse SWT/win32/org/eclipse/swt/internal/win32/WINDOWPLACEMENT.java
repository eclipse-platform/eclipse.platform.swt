package org.eclipse.swt.internal.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class WINDOWPLACEMENT {
	public int length;
	public int flags; 
	public int showCmd;
//	POINT ptMinPosition;
	public int ptMinPosition_x;
	public int ptMinPosition_y;
//	POINT ptMaxPosition;
	public int ptMaxPosition_x;
	public int ptMaxPosition_y;
//	RECT  rcNormalPosition; 
	public int left;
	public int top;
	public int right;
	public int bottom;
	public static final int sizeof = 44;
}
