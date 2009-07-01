/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
	/** @field cast=(char *) */
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
	/** @field cast=(XPointer) */
	public int obdata;
//	struct funcs {
		/** @field accessor=f.create_image,cast=(XImage *(*)()) */
	public int create_image;
		/** @field accessor=f.destroy_image,cast=(int(*)()) */
	public int destroy_image;
		/** @field accessor=f.get_pixel,cast=(unsigned long(*)()) */
	public int get_pixel;
		/** @field accessor=f.put_pixel,cast=(int(*)()) */
	public int put_pixel;
		/** @field accessor=f.sub_image,cast=(XImage *(*)()) */
	public int sub_image;
		/** @field accessor=f.add_pixel,cast=(int(*)()) */
	public int add_pixel;
// } f;
	public static final int sizeof = 88;
}
