package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
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
