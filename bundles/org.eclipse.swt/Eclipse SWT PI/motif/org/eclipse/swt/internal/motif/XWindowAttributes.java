/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.motif;

 
public class XWindowAttributes {
	public int x;
	public int y;
	public int width;
	public int height;
	public int border_width;
	public int depth;
	/** @field cast=(Visual *) */
	public int visual;
	public int root;
	/** @field accessor=class */
	public int c_class;
	public int bit_gravity;
  	public int win_gravity;
	public int backing_store;
	public int backing_planes;
	public int backing_pixel;
	public int save_under;
	public int colormap;
	public int map_installed;
	public int map_state;
	public int all_event_masks;
	public int your_event_mask;
	public int do_not_propagate_mask;
	public int override_redirect;
	/** @field cast=(Screen *) */
	public int screen;
	public static final int sizeof = 92;
}
