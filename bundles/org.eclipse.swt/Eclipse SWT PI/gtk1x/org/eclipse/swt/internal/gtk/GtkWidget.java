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


public class GtkWidget extends GtkObject {
	public short private_flags;
	public byte state;
	public byte saved_state;
	public int name;
	public int style;
	public short req_width;
	public short req_height;
	public short alloc_x;
	public short alloc_y;
	public short alloc_width;
	public short alloc_height;
	public int window;
	public int parent;
	public static final int sizeof = 48;
}
