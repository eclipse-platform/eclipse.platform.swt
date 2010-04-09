/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others. All rights reserved.
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
	/** @field cast=(G_CONST_RETURN gchar *(*)()) */
	public int /*long*/ get_name;
	/** @field cast=(G_CONST_RETURN gchar *(*)()) */
	public int /*long*/ get_description;
	/** @field cast=(AtkObject *(*)()) */
	public int /*long*/ get_parent;
	/** @field cast=(gint (*)()) */
	public int /*long*/ get_n_children;
	/** @field cast=(AtkObject *(*)()) */
	public int /*long*/ ref_child;
	/** @field cast=(gint (*)()) */
	public int /*long*/ get_index_in_parent;
	/** @field cast=(AtkRelationSet *(*)()) */
	public int /*long*/ ref_relation_set;
	/** @field cast=(AtkRole (*)()) */
	public int /*long*/ get_role;
	/** @field cast=(AtkLayer (*)()) */
	public int /*long*/ get_layer;
	/** @field cast=(gint (*)()) */
	public int /*long*/ get_mdi_zorder;
	/** @field cast=(AtkStateSet *(*)()) */
	public int /*long*/ ref_state_set;
	/** @field cast=(void (*)()) */
	public int /*long*/ set_name;
	/** @field cast=(void (*)()) */
	public int /*long*/ set_description;
	/** @field cast=(void (*)()) */
	public int /*long*/ set_parent;
	/** @field cast=(void (*)()) */
	public int /*long*/ set_role;
	/** @field cast=(guint (*)()) */
	public int /*long*/ connect_property_change_handler;
	/** @field cast=(void (*)()) */
	public int /*long*/ remove_property_change_handler;
	/** @field cast=(void (*)()) */
	public int /*long*/ initialize;
	/** @field cast=(void (*)()) */
	public int /*long*/ children_changed;
	/** @field cast=(void (*)()) */
	public int /*long*/ focus_event;
	/** @field cast=(void (*)()) */
	public int /*long*/ property_change;
	/** @field cast=(void (*)()) */
	public int /*long*/ state_change;
	/** @field cast=(void (*)()) */
	public int /*long*/ visible_data_changed;
	/** @field accessor=SWT_AtkObjectClass_get_attributes,cast=(SWT_AtkObjectClass_get_attributes_cast) */
	public int /*long*/ get_attributes;
}
