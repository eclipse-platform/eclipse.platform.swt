package org.eclipse.swt.internal.carbon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class DataBrowserListViewColumnDesc {
	//DataBrowserTableViewColumnDesc  propertyDesc;
	public int  propertyDesc_propertyID;
	public int  propertyDesc_propertyType;
	public int  propertyDesc_propertyFlags;
	//DataBrowserListViewHeaderDesc  headerBtnDesc;
  	public int headerBtnDesc_version;
	public short headerBtnDesc_minimumWidth;
	public short headerBtnDesc_maximumWidth;
	public short headerBtnDesc_titleOffset;
	public int headerBtnDesc_titleString;
	public short headerBtnDesc_initialOrder;
	//ControlFontStyleRec headerBtnDesc_btnFontStyle;
	public short headerBtnDesc_btnFontStyle_flags;
	public short headerBtnDesc_btnFontStyle_font;
	public short headerBtnDesc_btnFontStyle_size;
	public short headerBtnDesc_btnFontStyle_style;
	public short headerBtnDesc_btnFontStyle_mode;
	public short headerBtnDesc_btnFontStyle_just;
	// RGBColor headerBtnDesc_btnFontStyle_foreColor;
	public short headerBtnDesc_btnFontStyle_foreColor_red;
 	public short headerBtnDesc_btnFontStyle_foreColor_green;
	public short headerBtnDesc_btnFontStyle_foreColor_blue;
	//RGBColor headerBtnDesc_btnFontStyle_backColor;
	public short headerBtnDesc_btnFontStyle_backColor_red;
 	public short headerBtnDesc_btnFontStyle_backColor_green;
	public short headerBtnDesc_btnFontStyle_backColor_blue;
	//public ControlButtonContentInfo headerBtnDesc_btnContentInfo;
	public short headerBtnDesc_btnContentInfo_contentType;
	// public int headerBtnDesc_btnContentInfo_u; // union field 
	public static final int sizeof = 58;
}
