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
