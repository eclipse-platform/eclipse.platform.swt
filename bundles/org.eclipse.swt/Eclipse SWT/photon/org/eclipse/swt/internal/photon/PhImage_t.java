package org.eclipse.swt.internal.photon;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

public class PhImage_t {
	public int type;
	public int image_tag;
	public int bpl;
	public short size_w;
	public short size_h;
	public int palette_tag;
	public int colors;
	public int alpha;
	public int transparent;
	public byte format;
	public byte flags;
	public byte ghost_bpl;
	public byte spare1;
	public int ghost_bitmap;
	public int mask_bpl;
	public int mask_bm;
	public int palette;
	public int image;
	public static final int sizeof = 56;
}