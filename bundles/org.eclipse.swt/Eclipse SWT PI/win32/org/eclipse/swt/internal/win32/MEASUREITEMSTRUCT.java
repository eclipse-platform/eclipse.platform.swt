package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
