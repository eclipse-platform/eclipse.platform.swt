package org.eclipse.swt.internal.photon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class PtCallbackInfo_t {
	public int reason;
	public int reason_subtype;
	public int event;
	public int cbdata;
	public static final int sizeof = 16;
}
