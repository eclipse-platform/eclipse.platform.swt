package org.eclipse.swt.internal.photon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class PgVideoModeInfo_t {
	public short width;
	public short height;
	public short bits_per_pixel;
	public short bytes_per_scanline;
	public int type;
	public int mode_capabilities1;
	public int mode_capabilities2;
	public int mode_capabilities3;
	public int mode_capabilities4;
	public int mode_capabilities5;
	public int mode_capabilities6;
	public byte [] refresh_rates = new byte [20];
	public static final int sizeof = 56;
}
