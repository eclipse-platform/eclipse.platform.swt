package org.eclipse.swt.internal.photon;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

public class PhRegion_t {
	public int rid;
	public int handle;
	public int owner;
	public int flags;
	public short state;
//	public short zero1;
	public int events_sense;
	public int events_opaque;
	public short origin_x;
	public short origin_y;
	public int parent;
	public int child;
	public int bro_in_front;
	public int bro_behind;
	public int cursor_color;
	public short input_group;
	public short data_len;
//	public int zero21;
//	public int zero22;
	public short cursor_type;
//	public short zero3;

	public static final int sizeof = 68;
}
