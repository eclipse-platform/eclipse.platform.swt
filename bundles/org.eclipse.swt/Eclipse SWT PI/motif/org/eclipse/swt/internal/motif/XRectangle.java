package org.eclipse.swt.internal.motif;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class XRectangle {
	public short x, y, width, height;
	public static final int sizeof = 8;
public String toString () {
	return "XRectangle {" + x + ", " + y + ", " + width + ", " + height + "}";
}
}
