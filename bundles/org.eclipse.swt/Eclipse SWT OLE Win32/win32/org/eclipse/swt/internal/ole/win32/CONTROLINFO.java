package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
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
