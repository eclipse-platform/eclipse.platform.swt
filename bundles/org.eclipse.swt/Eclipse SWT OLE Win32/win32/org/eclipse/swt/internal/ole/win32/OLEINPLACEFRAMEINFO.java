package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public final class OLEINPLACEFRAMEINFO
{

	public int cb;
	public int fMDIApp;
	public int hwndFrame;
	public int haccel;
	public int cAccelEntries;

	public static final int sizeof = 20;

}
