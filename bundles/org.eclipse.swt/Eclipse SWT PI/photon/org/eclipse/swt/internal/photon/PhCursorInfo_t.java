package org.eclipse.swt.internal.photon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class PhCursorInfo_t {
//	PhPoint_t pos;
	public short pos_x;
	public short pos_y;
	public int region;
	public int ig_region;
	public int color;
//	PhPoint_t last_press;
	public short last_press_x;
	public short last_press_y;
	public int msec;
//	PhPoint_t steady;
	public int steady_x;
	public int steady_y;
	public int dragger;
//	PhRect_t drag_boundary;
	public short drag_boundary_xUL;
	public short drag_boundary_yUL;
	public short drag_boundary_xLR;
	public short drag_boundary_yLR;
	public int phantom_rid;
	public short type;
	public short ig;
	public short button_state;
	public byte click_count;
	public byte zero10, zero11,zero12;
	public int key_mods;
	public int zero2;
	public static final int sizeof = 60;

}
