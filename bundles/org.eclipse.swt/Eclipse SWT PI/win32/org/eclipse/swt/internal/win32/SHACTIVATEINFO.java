package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class SHACTIVATEINFO {
	public int cbSize;
	public int hwndLastFocus;
	public int fSipUp;
	public int fSipOnDeactivation;
	public int fActive;
	public int fReserved;
	public static final int sizeof = 24;
}
