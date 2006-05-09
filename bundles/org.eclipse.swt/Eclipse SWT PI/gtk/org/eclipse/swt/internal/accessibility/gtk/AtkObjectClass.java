/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.accessibility.gtk;


public class AtkObjectClass {
	public int /*long*/ get_name;
	public int /*long*/ get_description;
	public int /*long*/ get_parent;
	public int /*long*/ get_n_children;
	public int /*long*/ ref_child;
	public int /*long*/ get_index_in_parent;
	public int /*long*/ ref_relation_set;
	public int /*long*/ get_role;
	public int /*long*/ get_layer;
	public int /*long*/ get_mdi_zorder;
	public int /*long*/ ref_state_set;
	public int /*long*/ set_name;
	public int /*long*/ set_description;
	public int /*long*/ set_parent;
	public int /*long*/ set_role;
	public int /*long*/ connect_property_change_handler;
	public int /*long*/ remove_property_change_handler;
	public int /*long*/ initialize;
	public int /*long*/ children_changed;
	public int /*long*/ focus_event;
	public int /*long*/ property_change;
	public int /*long*/ state_change;
	public int /*long*/ visible_data_changed;
}
