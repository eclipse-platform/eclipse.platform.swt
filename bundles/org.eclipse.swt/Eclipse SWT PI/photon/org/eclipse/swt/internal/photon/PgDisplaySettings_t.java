package org.eclipse.swt.internal.photon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class PgDisplaySettings_t {
	public int mode;
	public int xres;
	public int yres;
	public int refresh;
	public int flags;
	public int [] reserved = new int [22];
	public static final int sizeof = 108;
}
