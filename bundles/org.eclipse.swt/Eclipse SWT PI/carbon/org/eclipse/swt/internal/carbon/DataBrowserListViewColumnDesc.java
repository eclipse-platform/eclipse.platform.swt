/**********************************************************************
 * Copyright (c) 2003-2004 IBM Corp.
 * Portions Copyright (c) 1983-2002, Apple Computer, Inc.
 *
 * All rights reserved.  This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 **********************************************************************/
package org.eclipse.swt.internal.carbon;

 
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
	public int headerBtnDesc_btnContentInfo_iconRef; // union field 
	public static final int sizeof = 58;
}
