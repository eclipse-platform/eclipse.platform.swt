package org.eclipse.swt.internal.photon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class PhPointerEvent_t {
//	public PhPoint_t pos;
	public short pos_x;
	public short pos_y;
	public short buttons;
	public short button_state;	
	public byte click_count;	
	public byte flags;	
	public short z;	
	public int key_mods;
	public int zero;
	public static final int sizeof = 20;
}
