package org.eclipse.swt.internal.motif;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
public abstract class XWindowEvent extends XEvent {
	public int root;	        /* root window that the event occured on */
	public int subwindow;	/* child window */
	public int time;		/* milliseconds */
	public int x, y;		/* pointer x, y coordinates in event window */
	public int x_root, y_root;	/* coordinates relative to root */
}
