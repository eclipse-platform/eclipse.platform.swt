package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public final class CONTROLINFO
{
	public int   cb;
	public int   hAccel;
	public short cAccel;
	public short filler;
	public int   dwFlags;
	
	public static final int sizeof = 16;
}
