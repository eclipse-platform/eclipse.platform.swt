package org.eclipse.swt.internal.motif;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
public class XRectangle {
	public short x, y, width, height;
	public static final int sizeof = 8;
public String toString () {
	return "XRectangle {" + x + ", " + y + ", " + width + ", " + height + "}";
}
}
