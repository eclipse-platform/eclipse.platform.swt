package org.eclipse.swt.internal.photon;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

public class PtCallbackInfo_t {
	public int reason;
	public int reason_subtype;
	public int event;
	public int cbdata;
	public static final int sizeof = 16;
}