package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class LRESULT {
	public int value;
	public static final LRESULT ONE = new LRESULT (1);
	public static final LRESULT ZERO = new LRESULT (0);
public LRESULT (int value) {
	this.value = value;
}
}
