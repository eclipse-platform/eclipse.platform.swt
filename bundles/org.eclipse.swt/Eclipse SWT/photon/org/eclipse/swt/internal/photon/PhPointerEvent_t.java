package org.eclipse.swt.internal.photon;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
