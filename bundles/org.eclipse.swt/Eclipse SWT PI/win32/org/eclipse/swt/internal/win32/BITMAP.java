package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class BITMAP {
	public int bmType;
	public int bmWidth;
	public int bmHeight;
	public int bmWidthBytes;
	public short bmPlanes;
	public short bmBitsPixel;
	public int bmBits;
	public static final int sizeof = 24;
}
