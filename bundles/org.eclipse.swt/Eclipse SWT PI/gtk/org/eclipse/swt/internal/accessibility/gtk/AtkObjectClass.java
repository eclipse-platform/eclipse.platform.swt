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


public class AtkObjectClass {
	public int get_name;
	public int get_description;
	public int get_parent;
	public int get_n_children;
	public int ref_child;
	public int get_index_in_parent;
	public int ref_relation_set;
	public int get_role;
	public int get_layer;
	public int get_mdi_zorder;
	public int ref_state_set;
	public int set_name;
	public int set_description;
	public int set_parent;
	public int set_role;
	public int connect_property_change_handler;
	public int remove_property_change_handler;
	public int initialize;
	public int children_changed;
	public int focus_event;
	public int property_change;
	public int state_change;
	public int visible_data_changed;
}
