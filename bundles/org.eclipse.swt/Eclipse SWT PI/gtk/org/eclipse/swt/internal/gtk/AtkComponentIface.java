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


public class AtkComponentIface {
	public int add_focus_handler;
	public int contains;
	public int ref_accessible_at_point;
	public int get_extents;
	public int get_position;
	public int get_size;
	public int grab_focus;
	public int remove_focus_handler;
	public int set_extents;
	public int set_position;
	public int set_size;
	public int get_layer;
	public int get_mdi_zorder;
}
