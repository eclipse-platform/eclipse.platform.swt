package org.eclipse.swt.internal.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
public class MEASUREITEMSTRUCT {
	public int CtlType;
	public int CtlID;
  	public int itemID;
	public int itemWidth;
	public int itemHeight; 
	public int itemData;
	public static final int sizeof = 24;
}
