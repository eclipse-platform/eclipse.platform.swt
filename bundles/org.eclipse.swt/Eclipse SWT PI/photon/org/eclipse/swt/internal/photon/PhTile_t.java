package org.eclipse.swt.internal.photon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class PhTile_t {
	//	PhRect_t rect;
	public short rect_ul_x;
	public short rect_ul_y;
	public short rect_lr_x;
	public short rect_lr_y;
	public int next;
	public static final int sizeof = 12;
}
