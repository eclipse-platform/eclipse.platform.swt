package org.eclipse.swt.internal.photon;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */
 
public class FontQueryInfo {
	public byte[] font = new byte[OS.MAX_FONT_TAG];
	public byte[] desc = new byte[OS.MAX_DESC_LENGTH];
	public short size;
	public short style;
	public short ascender;
	public short descender;
	public short width;
	public int lochar;
	public int hichar;
	public static final int sizeof = 140;
}
