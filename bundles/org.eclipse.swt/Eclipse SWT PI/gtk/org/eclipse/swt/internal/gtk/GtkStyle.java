package org.eclipse.swt.internal.gtk;

/*
 * Copyright (c) IBM Corp. 2000, 2001.  All rights reserved.
 *
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 */

public class GtkStyle {
	public int fg0_pixel;
	public short fg0_red, fg0_green, fg0_blue;
	public int fg1_pixel;
	public short fg1_red, fg1_green, fg1_blue;
	public int fg2_pixel;
	public short fg2_red, fg2_green, fg2_blue;
	public int fg3_pixel;
	public short fg3_red, fg3_green, fg3_blue;
	public int fg4_pixel;
	public short fg4_red, fg4_green, fg4_blue;
	public int bg0_pixel;
	public short bg0_red, bg0_green, bg0_blue;
	public int bg1_pixel;
	public short bg1_red, bg1_green, bg1_blue;
	public int bg2_pixel;
	public short bg2_red, bg2_green, bg2_blue;
	public int bg3_pixel;
	public short bg3_red, bg3_green, bg3_blue;
	public int bg4_pixel;
	public short bg4_red, bg4_green, bg4_blue;
	public int light0_pixel;
	public short light0_red, light0_green, light0_blue;
	public int light1_pixel;
	public short light1_red, light1_green, light1_blue;
	public int light2_pixel;
	public short light2_red, light2_green, light2_blue;
	public int light3_pixel;
	public short light3_red, light3_green, light3_blue;
	public int light4_pixel;
	public short light4_red, light4_green, light4_blue;
	public int dark0_pixel;
	public short dark0_red, dark0_green, dark0_blue;
	public int dark1_pixel;
	public short dark1_red, dark1_green, dark1_blue;
	public int dark2_pixel;
	public short dark2_red, dark2_green, dark2_blue;
	public int dark3_pixel;
	public short dark3_red, dark3_green, dark3_blue;
	public int dark4_pixel;
	public short dark4_red, dark4_green, dark4_blue;
	public int mid0_pixel;
	public short mid0_red, mid0_green, mid0_blue;
	public int mid1_pixel;
	public short mid1_red, mid1_green, mid1_blue;
	public int mid2_pixel;
	public short mid2_red, mid2_green, mid2_blue;
	public int mid3_pixel;
	public short mid3_red, mid3_green, mid3_blue;
	public int mid4_pixel;
	public short mid4_red, mid4_green, mid4_blue;
	public int text0_pixel;
	public short text0_red, text0_green, text0_blue;
	public int text1_pixel;
	public short text1_red, text1_green, text1_blue;
	public int text2_pixel;
	public short text2_red, text2_green, text2_blue;
	public int text3_pixel;
	public short text3_red, text3_green, text3_blue;
	public int text4_pixel;
	public short text4_red, text4_green, text4_blue;
	public int base0_pixel;
	public short base0_red, base0_green, base0_blue;
	public int base1_pixel;
	public short base1_red, base1_green, base1_blue;
	public int base2_pixel;
	public short base2_red, base2_green, base2_blue;
	public int base3_pixel;
	public short base3_red, base3_green, base3_blue;
	public int base4_pixel;
	public short base4_red, base4_green, base4_blue;
	
	public int black_pixel;
	public short black_red, black_green, black_blue;
	public int white_pixel;
	public short white_red, white_green, white_blue;
	
	public int font_desc;
	
	public int xthickness, ythickness;
	
	public int fg_gc0, fg_gc1, fg_gc2, fg_gc3, fg_gc4;
	public int bg_gc0, bg_gc1, bg_gc2, bg_gc3, bg_gc4;
	public int light_gc0, light_gc1, light_gc2, light_gc3, light_gc4;
	public int dark_gc0, dark_gc1, dark_gc2, dark_gc3, dark_gc4;
	public int mid_gc0, mid_gc1, mid_gc2, mid_gc3, mid_gc4;
	public int text_gc0, text_gc1, text_gc2, text_gc3, text_gc4;
	public int base_gc0, base_gc1, base_gc2, base_gc3, base_gc4;
	public int black_gc;
	public int white_gc;
	public int bg_pixmap0, bg_pixmap1, bg_pixmap2, bg_pixmap3, bg_pixmap4, bg_pixmap5;
	
	private GtkStyle() {}
	public GtkStyle(int ptr) {
		OS.memmove(this, ptr);
	}
}
