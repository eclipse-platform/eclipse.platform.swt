package org.eclipse.swt.internal.photon;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

public class PhKeyEvent_t {
	public int key_mods;
	public int key_flags;
	public int key_cap;
	public int key_sym;
	public short key_scan;
	public short key_zero;
//	PhPoint_t pos;
	public short pos_x;
	public short pos_y;
	public short button_state;
	public static final int sizeof = 26;
}
