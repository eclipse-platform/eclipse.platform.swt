package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public final class CONTROLINFO
{
	public int   cb;
	public int   hAccel;
	public short cAccel;
	public short filler;
	public int   dwFlags;
	
	public static final int sizeof = 16;
}
