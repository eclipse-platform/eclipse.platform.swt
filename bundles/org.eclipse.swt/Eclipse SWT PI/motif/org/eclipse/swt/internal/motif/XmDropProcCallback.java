package org.eclipse.swt.internal.motif;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class XmDropProcCallback {
	public int   reason;         // the reason callback was called
	public int   event;          // event structure that triggered callback
	public int   timeStamp;      // timestamp of ;logical event
	public int   dragContext;    // DragContext widget associated with operation
	public short x;              // x-coordinate of pointer
	public short y;              // y-coordinate of pointer
	public byte  dropSiteStatus; // valid or invalid
	public byte  operation;      // current operation
	public byte  operations;     // supported operations
	public byte  dropAction;     // drop or help

	public static final int sizeof = 24;
}
