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


public class GtkCTreeRow extends GtkCListRow {
	public int parent;
	public int sibling;
	public int children;
	public int pixmap_closed;
	public int mask_closed;
	public int pixmap_opened;
	public int mask_opened;
	public short level;
	public int is_leaf; // bitfield: 1
	public int expanded; // bitfield: 1
	public static final int sizeof = 80;
}
