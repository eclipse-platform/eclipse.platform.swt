package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

public class TRACKMOUSEEVENT {
	public int cbSize;
	public int dwFlags;
	public int hwndTrack;
	public int dwHoverTime;
	public static final int sizeof = 16;
}
