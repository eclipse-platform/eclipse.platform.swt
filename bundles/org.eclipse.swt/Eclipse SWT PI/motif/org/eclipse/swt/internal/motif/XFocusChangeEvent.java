package org.eclipse.swt.internal.motif;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class XFocusChangeEvent extends XEvent {
	public int mode;		/* NotifyNormal, NotifyGrab, NotifyUngrab */
	public int detail;
	/*
	 * NotifyAncestor, NotifyVirtual, NotifyInferior, 
	 * NotifyNonlinear,NotifyNonlinearVirtual, NotifyPointer,
	 * NotifyPointerRoot, NotifyDetailNone 
	 */
	public int pad0, pad1, pad2, pad3, pad4, pad5, pad6, pad7, pad8, pad9;
	public int pad10, pad11, pad12, pad13, pad14, pad15, pad16;
}
