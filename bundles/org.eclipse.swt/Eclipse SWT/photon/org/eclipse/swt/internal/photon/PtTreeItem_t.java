package org.eclipse.swt.internal.photon;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

public class PtTreeItem_t {
	public int list_flags;
	public short list_size_w;
	public short list_size_h;
	public int list_next;
	public int list_prev;
	public int father;
	public int son;
	public int brother;
	public short dim_w;
	public short dim_h;
	public short img_set;
	public short img_unset;
	public int data;
//	char string [1];
//	byte string;
	public static final int sizeof = 40;
}