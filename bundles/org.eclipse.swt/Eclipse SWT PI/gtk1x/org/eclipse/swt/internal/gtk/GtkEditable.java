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


public class GtkEditable extends GtkWidget {
	public int current_pos;
	public int selection_start_pos;
	public int selection_end_pos;
	public int has_selection; // bitfield : 1
	public int editable; // bitfield : 1
	public int visible; // bitfield : 1
	public int ic;
	public int ic_attr;
	public int clipboard_text;
	public static final int sizeof = 76;
}
