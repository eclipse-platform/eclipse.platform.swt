package org.eclipse.swt.internal.photon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class PtFileSelectionInfo_t {
	public short ret;
	public byte [] path = new byte [(OS.PATH_MAX + OS.NAME_MAX + 4) & (~3)];
	public PhDim_t dim = new PhDim_t ();
	public PhPoint_t pos = new PhPoint_t ();
	public byte [] format = new byte [80];
	public byte [] fspec = new byte [80];
	public int user_data;
	public int confirm_display;
	public int confirm_selection;
	public int new_directory;
	public int btn1;
	public int btn2;
	public int num_args;
	public int args;
	public int [] spare = new int [4];
	public static final int sizeof = 1500;
}
