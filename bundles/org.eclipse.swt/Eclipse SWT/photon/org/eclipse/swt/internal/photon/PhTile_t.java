package org.eclipse.swt.internal.photon;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

public class PhTile_t {
	//	PhRect_t rect;
	public short rect_ul_x;
	public short rect_ul_y;
	public short rect_lr_x;
	public short rect_lr_y;
	public int next;
	public static final int sizeof = 12;
}