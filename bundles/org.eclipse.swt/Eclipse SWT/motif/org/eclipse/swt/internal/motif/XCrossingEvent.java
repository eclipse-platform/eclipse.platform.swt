package org.eclipse.swt.internal.motif;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
public class XCrossingEvent extends XWindowEvent {
	public int mode;		/* NotifyNormal, NotifyGrab, NotifyUngrab */
	public int detail;
	/*
	 * NotifyAncestor, NotifyVirtual, NotifyInferior, 
	 * NotifyNonlinear, NotifyNonlinearVirtual
	 */
	public int same_screen;	/* same screen flag */
	public int focus;		/* boolean focus */
	public int state;	/* key or button mask */
	public int pad0, pad1, pad2, pad3, pad4, pad5, pad6, pad7;
}
