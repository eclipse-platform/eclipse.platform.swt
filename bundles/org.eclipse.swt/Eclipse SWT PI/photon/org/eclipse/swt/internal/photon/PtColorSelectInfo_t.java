package org.eclipse.swt.internal.photon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class PtColorSelectInfo_t {
	public short flags;
	public byte nselectors;
	public byte ncolor_models;
	public int color_models;
	public int selectors;
	public short pos_x;
	public short pos_y;
	public short size_w;
	public short size_h;
	public int palette;
	public int accept_text;
	public int dismiss_text;
	public int accept_dismiss_text;
	public int apply_f;
	public int data;
	public int rgb;
	public int dialog;
	public static final int sizeof = 52;
}
