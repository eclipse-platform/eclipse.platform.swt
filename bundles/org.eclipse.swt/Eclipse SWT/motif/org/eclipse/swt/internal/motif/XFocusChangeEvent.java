package org.eclipse.swt.internal.motif;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
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
