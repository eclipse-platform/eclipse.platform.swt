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
package org.eclipse.swt.internal.gtk;


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
