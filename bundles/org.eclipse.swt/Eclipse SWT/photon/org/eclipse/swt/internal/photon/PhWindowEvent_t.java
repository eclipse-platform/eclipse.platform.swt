package org.eclipse.swt.internal.photon;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

public class PhWindowEvent_t {
	public int event_f;
	public int state_f;
	public int rid;
//	PhPoint_t pos;
	public short pos_x;
	public short pos_y;
//	PhDim_t size;
	public short size_w;
	public short size_h;
	public short event_state;
	public short input_group;
	public int rsvd0, rsvd1, rsvd2, rsvd3;
	public static final int sizeof = 40;
}