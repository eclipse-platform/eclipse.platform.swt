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


public class GtkCListRow {
	public int cell;
	public int state;
	public short foreground_red;
	public short foreground_green;
	public short foreground_blue;
	public int foreground_pixel;
	public short background_red;
	public short background_green;
	public short background_blue;
	public int background_pixel;
	public int style;
	public int data;
	public int destroy; // bitfield: 1
	public int fg_set; // bitfield: 1
	public int bg_set; // bitfield: 1
	public int selectable; // bitfield: 1
	public static final int sizeof = 48;
}
