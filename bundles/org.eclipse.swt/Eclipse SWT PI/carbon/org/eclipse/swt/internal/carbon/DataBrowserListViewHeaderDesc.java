package org.eclipse.swt.internal.carbon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class DataBrowserListViewHeaderDesc {
	public int version;
	public short minimumWidth;
	public short maximumWidth;
	public short titleOffset;
	public int titleString;
	public short initialOrder;
	// ControlFontStyleRec  btnFontStyle;
	public short btnFontStyle_flags;
	public short btnFontStyle_font;
	public short btnFontStyle_size;
	public short btnFontStyle_style;
	public short btnFontStyle_mode;
	public short btnFontStyle_just;
	//RGBColor btnFontStyle_foreColor;
	public short btnFontStyle_foreColor_red;
	public short btnFontStyle_foreColor_green;
	public short btnFontStyle_foreColor_blue;
	//RGBColor btnFontStyle_backColor;
	public short btnFontStyle_backColor_red;
	public short btnFontStyle_backColor_green;
	public short btnFontStyle_backColor_blue;
	//ControlButtonContentInfo  btnContentInfo;
	public short btnContentInfo_contentType;
	public int btnContentInfo_iconRef;
	
	public static final int sizeof = 46;
}
