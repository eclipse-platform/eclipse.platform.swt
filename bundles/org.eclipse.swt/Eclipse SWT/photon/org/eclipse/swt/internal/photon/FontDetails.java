package org.eclipse.swt.internal.photon;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */
 
public class FontDetails {
	public byte[] desc = new byte[OS.MAX_DESC_LENGTH];
	public byte[] stem = new byte[OS.MAX_FONT_TAG];
	public short losize;
	public short hisize;
	public short flags;
	public static final int sizeof = 128;
}
