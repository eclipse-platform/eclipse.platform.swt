package org.eclipse.swt.internal.motif;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
