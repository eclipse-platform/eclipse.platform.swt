package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
public class NMCUSTOMDRAW extends NMHDR {
	 public int dwDrawStage; 
	 public int hdc;      
	// public RECT rc;
	 public int left, top, right, bottom;
	 public int dwItemSpec;
	 public int uItemState;
	 public int lItemlParam;
	 public static final int sizeof = 48;
}
