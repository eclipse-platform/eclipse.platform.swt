package org.eclipse.swt.internal.motif;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public abstract class XEvent {
	public int type;
	public int serial;
	public int send_event;
	public int display;
	public int window;
	public static final int sizeof = 96;
}
