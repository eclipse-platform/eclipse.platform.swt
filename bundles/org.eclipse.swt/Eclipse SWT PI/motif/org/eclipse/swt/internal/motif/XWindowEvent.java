/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.motif;

 
public abstract class XWindowEvent extends XEvent {
	public int root;	        /* root window that the event occured on */
	public int subwindow;	/* child window */
	public int time;		/* milliseconds */
	public int x, y;		/* pointer x, y coordinates in event window */
	public int x_root, y_root;	/* coordinates relative to root */
}
