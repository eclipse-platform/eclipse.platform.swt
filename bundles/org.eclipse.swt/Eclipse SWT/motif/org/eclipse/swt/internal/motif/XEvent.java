package org.eclipse.swt.internal.motif;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
public abstract class XEvent {
	public int type;
	public int serial;
	public int send_event;
	public int display;
	public int window;
	public static final int sizeof = 96;
}
