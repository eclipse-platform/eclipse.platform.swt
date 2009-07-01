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

 
public class DataBrowserListViewHeaderDesc {
	public int version;
	public short minimumWidth;
	public short maximumWidth;
	public short titleOffset;
	/** @field cast=(CFStringRef) */
	public int titleString;
	public short initialOrder;
	// ControlFontStyleRec  btnFontStyle;
	/** @field accessor=btnFontStyle.flags */
	public short btnFontStyle_flags;
	/** @field accessor=btnFontStyle.font */
	public short btnFontStyle_font;
	/** @field accessor=btnFontStyle.size */
	public short btnFontStyle_size;
	/** @field accessor=btnFontStyle.style */
	public short btnFontStyle_style;
	/** @field accessor=btnFontStyle.mode */
	public short btnFontStyle_mode;
	/** @field accessor=btnFontStyle.just */
	public short btnFontStyle_just;
	//RGBColor btnFontStyle_foreColor;
	/** @field accessor=btnFontStyle.foreColor.red */
	public short btnFontStyle_foreColor_red;
	/** @field accessor=btnFontStyle.foreColor.green */
	public short btnFontStyle_foreColor_green;
	/** @field accessor=btnFontStyle.foreColor.blue */
	public short btnFontStyle_foreColor_blue;
	//RGBColor btnFontStyle_backColor;
	/** @field accessor=btnFontStyle.backColor.red */
	public short btnFontStyle_backColor_red;
	/** @field accessor=btnFontStyle.backColor.green */
	public short btnFontStyle_backColor_green;
	/** @field accessor=btnFontStyle.backColor.blue */
	public short btnFontStyle_backColor_blue;
	//ControlButtonContentInfo  btnContentInfo;
	/** @field accessor=btnContentInfo.contentType */
	public short btnContentInfo_contentType;
	/** @field accessor=btnContentInfo.u.iconRef,cast=(IconRef) */
	public int btnContentInfo_iconRef;
	
	public static final int sizeof = 46;
}
