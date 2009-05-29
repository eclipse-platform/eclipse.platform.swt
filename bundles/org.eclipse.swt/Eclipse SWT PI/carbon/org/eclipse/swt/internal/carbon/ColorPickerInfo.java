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


public class ColorPickerInfo {
//	PMColor theColor;
	/** @field accessor=theColor.profile,cast=(CMProfileHandle) */
	public int profile;
	/** @field accessor=theColor.color.rgb.red,cast=(UInt16) */
	public short red;
	/** @field accessor=theColor.color.rgb.green,cast=(UInt16) */
	public short green;
	/** @field accessor=theColor.color.rgb.blue,cast=(UInt16) */
	public short blue;
	/** @field cast=(CMProfileHandle) */
	public int dstProfile;
	/** @field cast=(UInt32) */
	public int flags;
	/** @field cast=(DialogPlacementSpec) */
	public short placeWhere;
//	Point dialogOrigin
	/** @field accessor=dialogOrigin.h,cast=(short) */
	public short h;
	/** @field accessor=dialogOrigin.v,cast=(short) */
	public short v;
	/** @field cast=(OSType) */
	public int pickerType;
	/** @field cast=(UserEventUPP) */
	public int eventProc;
	/** @field cast=(ColorChangedUPP) */
	public int colorProc;
	/** @field cast=(UInt32) */
	public int colorProcData;
//	Str255 prompt;
	/** @field cast=(Str255) */
	public byte [] prompt = new byte [256];
//	PickerMenuItemInfo  mInfo;
	/** @field accessor=mInfo.editMenuID,cast=(SInt16) */
	public short editMenuID;
	/** @field accessor=mInfo.cutItem,cast=(SInt16) */
	public short cutItem;
	/** @field accessor=mInfo.copyItem,cast=(SInt16) */
	public short copyItem;
	/** @field accessor=mInfo.pasteItem,cast=(SInt16) */
	public short pasteItem;
	/** @field accessor=mInfo.clearItem,cast=(SInt16) */
	public short clearItem;
	/** @field accessor=mInfo.undoItem,cast=(SInt16) */
	public short undoItem;
	/** @field cast=(Boolean) */
	public boolean newColorChosen;
//	SInt8 filler;
	public static final int sizeof = 312;
}
