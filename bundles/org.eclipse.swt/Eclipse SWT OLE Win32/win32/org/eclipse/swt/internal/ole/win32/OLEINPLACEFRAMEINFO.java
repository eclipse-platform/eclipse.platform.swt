package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
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
