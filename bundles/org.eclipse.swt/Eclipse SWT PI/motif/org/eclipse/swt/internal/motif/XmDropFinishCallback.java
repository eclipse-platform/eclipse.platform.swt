package org.eclipse.swt.internal.motif;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class XmDropFinishCallback {
	public int   reason;           // the reason callback was called
	public int   event;            // event structure that triggered callback
	public int   timeStamp;        // time at which drop completed
	public byte  operation;        // current operation
	public byte  operations;       // supported operations
	public byte  dropSiteStatus;   // valid, invalid or none
	public byte  dropAction;       // drop, cancel, help or interrupt
	public byte  completionStatus; // success or failure

	public static final int sizeof = 17;
}
