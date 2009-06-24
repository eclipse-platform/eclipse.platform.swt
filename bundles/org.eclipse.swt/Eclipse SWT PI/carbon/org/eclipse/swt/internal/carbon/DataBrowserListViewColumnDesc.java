/**********************************************************************
 * Copyright (c) 2003, 2008 IBM Corp.
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
	/** @field accessor=propertyDesc.propertyID,cast=(DataBrowserPropertyID) */
	public int  propertyDesc_propertyID;
	/** @field accessor=propertyDesc.propertyType,cast=(OSType) */
	public int  propertyDesc_propertyType;
	/** @field accessor=propertyDesc.propertyFlags,cast=(DataBrowserPropertyFlags) */
	public int  propertyDesc_propertyFlags;
	//DataBrowserListViewHeaderDesc  headerBtnDesc;
  	/** @field accessor=headerBtnDesc.version,cast=(UInt32) */
	public int headerBtnDesc_version;
	/** @field accessor=headerBtnDesc.minimumWidth,cast=(UInt16) */
	public short headerBtnDesc_minimumWidth;
	/** @field accessor=headerBtnDesc.maximumWidth,cast=(UInt16) */
	public short headerBtnDesc_maximumWidth;
	/** @field accessor=headerBtnDesc.titleOffset,cast=(SInt16) */
	public short headerBtnDesc_titleOffset;
	/** @field accessor=headerBtnDesc.titleString,cast=(CFStringRef) */
	public int headerBtnDesc_titleString;
	/** @field accessor=headerBtnDesc.initialOrder,cast=(DataBrowserSortOrder) */
	public short headerBtnDesc_initialOrder;
	//ControlFontStyleRec headerBtnDesc_btnFontStyle;
	/** @field accessor=headerBtnDesc.btnFontStyle.flags,cast=(SInt16) */
	public short headerBtnDesc_btnFontStyle_flags;
	/** @field accessor=headerBtnDesc.btnFontStyle.font,cast=(SInt16) */
	public short headerBtnDesc_btnFontStyle_font;
	/** @field accessor=headerBtnDesc.btnFontStyle.size,cast=(SInt16) */
	public short headerBtnDesc_btnFontStyle_size;
	/** @field accessor=headerBtnDesc.btnFontStyle.style,cast=(SInt16) */
	public short headerBtnDesc_btnFontStyle_style;
	/** @field accessor=headerBtnDesc.btnFontStyle.mode,cast=(SInt16) */
	public short headerBtnDesc_btnFontStyle_mode;
	/** @field accessor=headerBtnDesc.btnFontStyle.just,cast=(SInt16) */
	public short headerBtnDesc_btnFontStyle_just;
	// RGBColor headerBtnDesc_btnFontStyle_foreColor;
	/** @field accessor=headerBtnDesc.btnFontStyle.foreColor.red,cast=(unsigned short) */
	public short headerBtnDesc_btnFontStyle_foreColor_red;
 	/** @field accessor=headerBtnDesc.btnFontStyle.foreColor.green,cast=(unsigned short) */
	public short headerBtnDesc_btnFontStyle_foreColor_green;
	/** @field accessor=headerBtnDesc.btnFontStyle.foreColor.blue,cast=(unsigned short) */
	public short headerBtnDesc_btnFontStyle_foreColor_blue;
	//RGBColor headerBtnDesc_btnFontStyle_backColor;
	/** @field accessor=headerBtnDesc.btnFontStyle.backColor.red,cast=(unsigned short) */
	public short headerBtnDesc_btnFontStyle_backColor_red;
 	/** @field accessor=headerBtnDesc.btnFontStyle.backColor.green,cast=(unsigned short) */
	public short headerBtnDesc_btnFontStyle_backColor_green;
	/** @field accessor=headerBtnDesc.btnFontStyle.backColor.blue,cast=(unsigned short) */
	public short headerBtnDesc_btnFontStyle_backColor_blue;
	//public ControlButtonContentInfo headerBtnDesc_btnContentInfo;
	/** @field accessor=headerBtnDesc.btnContentInfo.contentType,cast=(ControlContentType) */
	public short headerBtnDesc_btnContentInfo_contentType;
	/** @field accessor=headerBtnDesc.btnContentInfo.u.iconRef,cast=(IconRef) */
	public int headerBtnDesc_btnContentInfo_iconRef; // union field 
	public static final int sizeof = 58;
}
