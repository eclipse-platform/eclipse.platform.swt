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
package org.eclipse.swt.internal.motif;

 
public class XImage {
	public int width;
	public int height;
	public int xoffset;
	public int format;
	public int data;
	public int byte_order;
	public int bitmap_unit;
	public int bitmap_bit_order;
	public int bitmap_pad;
	public int depth;
	public int bytes_per_line;
	public int bits_per_pixel;
	public int red_mask;
	public int green_mask;
	public int blue_mask;
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
