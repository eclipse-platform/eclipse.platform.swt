package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public final class GUID
{
	public int    data1;
	public short  data2;
	public short  data3;
	public byte   b0;
	public byte   b1;
	public byte   b2;
	public byte   b3;
	public byte   b4;
	public byte   b5;
	public byte   b6;
	public byte   b7;
	
	public static final int sizeof = 16;
}
