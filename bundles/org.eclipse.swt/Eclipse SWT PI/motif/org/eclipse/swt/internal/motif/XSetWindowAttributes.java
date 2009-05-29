/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.motif;

 
public class XSetWindowAttributes {
	public int background_pixmap;
	public int background_pixel;
	public int border_pixmap;
	public int border_pixel;
	public int bit_gravity;
	public int win_gravity;
	public int backing_store;
	public int backing_planes;
	public int backing_pixel;
	public int save_under;
	public int event_mask;
	public int do_not_propagate_mask;
	public int override_redirect;
	public int colormap;
	public int cursor;
	public static final int sizeof = 60;
}
