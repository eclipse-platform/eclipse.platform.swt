/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others. All rights reserved.
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
	/** @field cast=(const gchar *(*)()) */
	public long /*int*/ get_name;
	/** @field cast=(const gchar *(*)()) */
	public long /*int*/ get_description;
	/** @field cast=(AtkObject *(*)()) */
	public long /*int*/ get_parent;
	/** @field cast=(gint (*)()) */
	public long /*int*/ get_n_children;
	/** @field cast=(AtkObject *(*)()) */
	public long /*int*/ ref_child;
	/** @field cast=(gint (*)()) */
	public long /*int*/ get_index_in_parent;
	/** @field cast=(AtkRelationSet *(*)()) */
	public long /*int*/ ref_relation_set;
	/** @field cast=(AtkRole (*)()) */
	public long /*int*/ get_role;
	/** @field cast=(AtkLayer (*)()) */
	public long /*int*/ get_layer;
	/** @field cast=(gint (*)()) */
	public long /*int*/ get_mdi_zorder;
	/** @field cast=(AtkStateSet *(*)()) */
	public long /*int*/ ref_state_set;
	/** @field cast=(void (*)()) */
	public long /*int*/ set_name;
	/** @field cast=(void (*)()) */
	public long /*int*/ set_description;
	/** @field cast=(void (*)()) */
	public long /*int*/ set_parent;
	/** @field cast=(void (*)()) */
	public long /*int*/ set_role;
	/** @field cast=(guint (*)()) */
	public long /*int*/ connect_property_change_handler;
	/** @field cast=(void (*)()) */
	public long /*int*/ remove_property_change_handler;
	/** @field cast=(void (*)()) */
	public long /*int*/ initialize;
	/** @field cast=(void (*)()) */
	public long /*int*/ children_changed;
	/** @field cast=(void (*)()) */
	public long /*int*/ focus_event;
	/** @field cast=(void (*)()) */
	public long /*int*/ property_change;
	/** @field cast=(void (*)()) */
	public long /*int*/ state_change;
	/** @field cast=(void (*)()) */
	public long /*int*/ visible_data_changed;
	/** @field accessor=SWT_AtkObjectClass_get_attributes,cast=(SWT_AtkObjectClass_get_attributes_cast) */
	public long /*int*/ get_attributes;
}
