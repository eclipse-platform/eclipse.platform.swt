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


public class ColorPickerInfo {
//	PMColor theColor;
	public int profile;
	public short red;
	public short green;
	public short blue;
	public int dstProfile;
	public int flags;
	public short placeWhere;
//	Point dialogOrigin
	public short h;
	public short v;
	public int pickerType;
	public int eventProc;
	public int colorProc;
	public int colorProcData;
//	Str255 prompt;
	public byte [] prompt = new byte [256];
//	PickerMenuItemInfo  mInfo;
	public short editMenuID;
	public short cutItem;
	public short copyItem;
	public short pasteItem;
	public short clearItem;
	public short undoItem;
	public boolean newColorChosen;
//	SInt8 filler;
	public static final int sizeof = 312;
}
