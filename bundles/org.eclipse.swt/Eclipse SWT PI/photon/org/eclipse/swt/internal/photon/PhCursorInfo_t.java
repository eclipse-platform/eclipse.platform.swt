/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.photon;


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
	public short steady_x;
	public short steady_y;
	public int dragger;
//	PhRect_t drag_boundary;
	public short drag_boundary_ul_x;
	public short drag_boundary_ul_y;
	public short drag_boundary_lr_x;
	public short drag_boundary_lr_y;
	public int phantom_rid;
	public short type;
	public short ig;
	public short button_state;
	public byte click_count;
	public byte zero1;
	public int key_mods;
	public int zero2;
	public static final int sizeof = 60;
}
