package org.eclipse.swt.internal.photon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class PtTreeItem_t {
	public int list_flags;
	public short list_size_w;
	public short list_size_h;
	public int list_next;
	public int list_prev;
	public int father;
	public int son;
	public int brother;
	public short dim_w;
	public short dim_h;
	public short img_set;
	public short img_unset;
	public int data;
//	char string [1];
//	byte string;
	public static final int sizeof = 40;
}
