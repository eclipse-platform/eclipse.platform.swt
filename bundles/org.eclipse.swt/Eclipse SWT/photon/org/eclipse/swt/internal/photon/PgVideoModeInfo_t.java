package org.eclipse.swt.internal.photon;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

public class PgVideoModeInfo_t {
	public short width;
	public short height;
	public short bits_per_pixel;
	public short bytes_per_scanline;
	public int type;
	public int mode_capabilities1;
	public int mode_capabilities2;
	public int mode_capabilities3;
	public int mode_capabilities4;
	public int mode_capabilities5;
	public int mode_capabilities6;
	public byte [] refresh_rates = new byte [20];
	public static final int sizeof = 56;
}
