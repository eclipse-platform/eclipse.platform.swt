package org.eclipse.swt.internal.motif;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class XSetWindowAttributes {
	public int background_pixmap;	/* background or None or ParentRelative */
	public int background_pixel;	/* background pixel */
	public int border_pixmap;	/* border of the window */
	public int border_pixel;	/* border pixel value */
	public int bit_gravity;		/* one of bit gravity values */
	public int win_gravity;		/* one of the window gravity values */
	public int backing_store;		/* NotUseful, WhenMapped, Always */
	public int backing_planes;/* planes to be preseved if possible */
	public int backing_pixel;/* value to use in restoring planes */
	public int save_under;		/* should bits under be saved? (popups) */
	public int event_mask;		/* set of events that should be saved */
	public int do_not_propagate_mask;	/* set of events that should not propagate */
	public int override_redirect;	/* boolean value for override-redirect */
	public int colormap;		/* color map to be associated with window */
	public int cursor;		/* cursor to be displayed (or None) */
	public static final int sizeof = 60;
}
