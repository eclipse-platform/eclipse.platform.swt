package org.eclipse.swt.internal.motif;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
