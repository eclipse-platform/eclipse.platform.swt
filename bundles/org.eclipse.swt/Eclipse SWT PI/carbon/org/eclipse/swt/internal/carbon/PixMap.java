package org.eclipse.swt.internal.carbon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class PixMap extends BitMap {
	public short pmVersion;
	public short packType;
	public int packSize;
	public int hRes;
	public int vRes;
	public short pixelType;
	public short pixelSize;
	public short cmpCount;
	public short cmpSize;
	public int pixelFormat;
	public int pmTable;
	public int pmExt;
	public static final int sizeof = 50;
}
