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

 
public class XFontStruct {
	public int ext_data;
	public int fid;
	public int direction;
	public int min_char_or_byte2;
	public int max_char_or_byte2;
	public int min_byte1;
	public int max_byte1;
	public int all_chars_exist;
	public int default_char;
	public int n_properties;
	public int properties;
	//XCharStruct min_bounds;
	public short min_bounds_lbearing;
	public short min_bounds_rbearing;
	public short min_bounds_width;
	public short min_bounds_ascent;
	public short min_bounds_descent;
	public short min_bounds_attributes;
	//XCharStruct max_bounds;
	public short max_bounds_lbearing;
	public short max_bounds_rbearing;
	public short max_bounds_width;
	public short max_bounds_ascent;
	public short max_bounds_descent;
	public short max_bounds_attributes;
	public int per_char;
	public int ascent;
	public int descent;
	public static final int sizeof = 80;
}
