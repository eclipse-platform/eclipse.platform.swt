package org.eclipse.swt.internal.motif;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class XButtonEvent extends XInputEvent {
	public int button;	/* detail */
	public int same_screen;	/* same screen flag */
	public int pad0, pad1, pad2, pad3, pad4, pad5, pad6, pad7, pad8;
}
