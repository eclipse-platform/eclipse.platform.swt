package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public final class DVTARGETDEVICE
{
	public int    tdSize; 
	public short  tdDriverNameOffset; 
	public short  tdDeviceNameOffset;
	public short  tdPortNameOffset;
	public short  tdExtDevmodeOffset;
	public byte   tdData;
	
	public static final int sizeof = 13;
}
