package org.eclipse.swt.internal.photon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
