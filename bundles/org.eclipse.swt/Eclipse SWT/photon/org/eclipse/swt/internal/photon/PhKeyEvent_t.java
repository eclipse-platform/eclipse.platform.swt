package org.eclipse.swt.internal.photon;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

public class PhKeyEvent_t {
	public int key_mods;
	public int key_flags;
	public int key_cap;
	public int key_sym;
	public byte key_scan;
	public byte key_zero1;
	public short key_zero2;
//	PhPoint_t pos;
	public short pos_x;
	public short pos_y;
	public short button_state;
	public static final int sizeof = 26;
}