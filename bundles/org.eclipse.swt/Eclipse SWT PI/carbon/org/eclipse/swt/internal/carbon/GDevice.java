package org.eclipse.swt.internal.carbon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class GDevice {
	public short gdRefNum;
	public short gdID;
	public short gdType;
	public int gdITable;
	public short gdResPref;
	public int gdSearchProc;
	public int gdCompProc;
	public short gdFlags;
	public int gdPMap;
	public int gdRefCon;
	public int gdNextGD;
	//Rect gdRect;
	public short left;
	public short top;
	public short right;
	public short bottom;
	public int gdMode;
	public short gdCCBytes;
	public short gdCCDepth;
	public int gdCCXData;
	public int gdCCXMask;
	public int gdExt;
	public static final int sizeof = 62;
}
