package org.eclipse.swt.internal.motif;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
public abstract class XEvent {
	public int type;
	public int serial;
	public int send_event;
	public int display;
	public int window;
	public static final int sizeof = 96;
}
