package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class DROPFILES {
	public int pFiles; // Offset of the file list from the beginning of this structure, in bytes.
	//public POINT pt; // Drop point. The coordinates depend on fNC.
	public int pt_x;
	public int pt_y;
	public int fNC;   // Nonclient area flag. If this member is TRUE, pt specifies the screen 
				      // coordinates of a point in a window's nonclient area. If it is FALSE, 
				      // pt specifies the client coordinates of a point in the client area. 
	public int fWide; // Value that indicates whether the file contains ANSI or Unicode 
					  // characters. If it is zero, it contains ANSI characters. Otherwise, it 
				      // contains Unicode characters.

	public static final int sizeof = 20;
}
