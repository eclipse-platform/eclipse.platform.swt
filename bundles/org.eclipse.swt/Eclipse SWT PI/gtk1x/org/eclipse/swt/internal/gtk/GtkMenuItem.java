package org.eclipse.swt.internal.gtk;

/*
 * Copyright (c) IBM Corp. 2000, 2001.  All rights reserved.
 *
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 */

public class GtkMenuItem extends GtkItem {
	public int submenu;
	public int accelerator_signal;
	public int toggle_size;
	public int accelerator_width;
	public int show_toggle_indicator;
	public int show_submenu_indicator;
	public int submenu_placement;
	public int submenu_direction;
	public int right_justify;
	public int timer;
	public static final int sizeof = 84;
}
