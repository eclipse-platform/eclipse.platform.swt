package org.eclipse.swt.internal.photon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
