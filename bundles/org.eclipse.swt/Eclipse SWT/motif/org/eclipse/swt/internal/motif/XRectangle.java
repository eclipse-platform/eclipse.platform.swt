package org.eclipse.swt.internal.motif;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
public class XRectangle {
	public short x, y, width, height;
	public static final int sizeof = 8;
public String toString () {
	return "XRectangle {" + x + ", " + y + ", " + width + ", " + height + "}";
}
}
