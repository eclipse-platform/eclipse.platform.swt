/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *******************************************************************************/
package org.eclipse.swt.internal.accessibility.gtk;


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
