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


public class GtkText extends GtkEditable {
	public int first_line_start_index;
	public int first_onscreen_hor_pixel;
	public int first_onscreen_ver_pixel;
	public int default_tab_width;
	public int cursor_pos_x;
	public int cursor_pos_y;
	public int cursor_virtual_x;
	public static final int sizeof = 244;
}
