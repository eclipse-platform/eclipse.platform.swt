package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public final class GUID
{
	public int    data1;
	public short  data2;
	public short  data3;
	public byte   b0;
	public byte   b1;
	public byte   b2;
	public byte   b3;
	public byte   b4;
	public byte   b5;
	public byte   b6;
	public byte   b7;
	
	public static final int sizeof = 16;
}
