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

 
public class XWindowAttributes {
	public int x, y;			/* location of window */
	public int width, height;		/* width and height of window */
	public int border_width;		/* border width of window */
	public int depth;          	/* depth of window */
	public int visual;		/* the associated visual structure */
	public int root;        	/* root of screen containing window */
	public int c_class;			/* InputOutput, InputOnly*/
	public int bit_gravity;		/* one of bit gravity values */
  	public int win_gravity;		/* one of the window gravity values */
	public int backing_store;		/* NotUseful, WhenMapped, Always */
	public int backing_planes;/* planes to be preserved if possible */
	public int backing_pixel;/* value to be used when restoring planes */
	public int save_under;		/* boolean, should bits under be saved? */
	public int colormap;		/* color map to be associated with window */
	public int map_installed;		/* boolean, is color map currently installed*/
	public int map_state;		/* IsUnmapped, IsUnviewable, IsViewable */
	public int all_event_masks;	/* set of events all people have interest in*/
	public int your_event_mask;	/* my event mask */
	public int do_not_propagate_mask; /* set of events that should not propagate */
	public int override_redirect;	/* boolean value for override-redirect */
	public int screen;		/* back pointer to correct screen */
	public static final int sizeof = 92;
}
