package org.eclipse.swt.internal.motif;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class XImage {
	public int width, height;
	public int xoffset, format;
	public int data;
	public int byte_order, bitmap_unit, bitmap_bit_order, bitmap_pad;
	public int depth, bytes_per_line, bits_per_pixel;
	public int red_mask, green_mask, blue_mask;
	public int obdata;
//	struct funcs {
		public int create_image;
		public int destroy_image;
		public int get_pixel;
		public int put_pixel;
		public int sub_image;
		public int add_pixel;
// } f;
	public static final int sizeof = 88;
}
