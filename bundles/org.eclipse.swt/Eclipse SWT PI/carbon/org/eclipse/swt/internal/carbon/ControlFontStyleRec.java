package org.eclipse.swt.internal.carbon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class ControlFontStyleRec {
	public short flags;
	public short font;
	public short size;
	public short style;
	public short mode;
	public short just;
//	RGBColor foreColor;
	public short foreColor_red;
	public short foreColor_green;
	public short foreColor_blue;
//	RGBColor backColor;
	public short backColor_red;
	public short backColor_green;
	public short backColor_blue;
	public static final int sizeof = 24;
}
