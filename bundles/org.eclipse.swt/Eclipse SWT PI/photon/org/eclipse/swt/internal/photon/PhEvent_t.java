package org.eclipse.swt.internal.photon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
