package org.eclipse.swt.internal.win32;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */

public class TRACKMOUSEEVENT {
	public int cbSize;
	public int dwFlags;
	public int hwndTrack;
	public int dwHoverTime;
	public static final int sizeof = 16;
}
