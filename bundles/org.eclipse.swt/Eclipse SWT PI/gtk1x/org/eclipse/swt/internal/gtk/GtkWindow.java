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

public class GtkWindow extends GtkBin {
	public int title;
	public int wmclass_name;
	public int wmclass_class;
	public int type;
	public int focus_widget;
	public int default_widget;
	public int transient_parent;
	public short resize_count;
	public int allow_shrink;
	public int allow_grow;
	public int auto_shrink;
	public int handling_resize;
	public int position;
	public int use_uposition;
	public int modal;
	public static final int sizeof = 96;
}
