package org.eclipse.swt.internal.photon;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

public class PtColorSelectInfo_t {
	public short flags;
	public byte nselectors;
	public byte ncolor_models;
	public int color_models;
	public int selectors;
	public short pos_x;
	public short pos_y;
	public short size_w;
	public short size_h;
	public int palette;
	public int accept_text;
	public int dismiss_text;
	public int accept_dismiss_text;
	public int apply_f;
	public int data;
	public int rgb;
	public int dialog;
	public static final int sizeof = 52;
}
