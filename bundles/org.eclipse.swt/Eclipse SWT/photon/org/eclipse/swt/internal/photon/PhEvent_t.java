package org.eclipse.swt.internal.photon;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

public class PhEvent_t {
	public int type;
	public short subtype;
	public short processing_flags;
//	public PhEventRegion_t emitter;
	public int emitter_rid;
	public int emitter_handle;
//	public PhEventRegion_t collector;
	public int collector_rid;
	public int collector_handle;
	public short input_group;
	public short flags;
	public int timestamp;
//	public PhPoint_t translation;
	public short translation_x;
	public short translation_y;
	public short num_rects;
	public short data_len;
	public static final int sizeof = 40;
}
