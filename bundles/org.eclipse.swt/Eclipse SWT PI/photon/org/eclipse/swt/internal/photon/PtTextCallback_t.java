package org.eclipse.swt.internal.photon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class PtTextCallback_t {
	public int start_pos;
	public int end_pos;
	public int cur_insert;
	public int new_insert;
	public int length;
	public short reserved;
	public int text;
	public int doit;
	public static final int sizeof = 30;
}
