package org.eclipse.swt.internal.motif;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public abstract class XWindowEvent extends XEvent {
	public int root;	        /* root window that the event occured on */
	public int subwindow;	/* child window */
	public int time;		/* milliseconds */
	public int x, y;		/* pointer x, y coordinates in event window */
	public int x_root, y_root;	/* coordinates relative to root */
}
