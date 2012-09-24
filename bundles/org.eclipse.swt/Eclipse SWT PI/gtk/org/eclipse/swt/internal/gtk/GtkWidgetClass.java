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
package org.eclipse.swt.internal.gtk;


public class GtkWidgetClass extends GObjectClass {
	public int activate_signal;
	public int set_scroll_adjustments_signal;	
	/** @field cast=(void(*)()) */
	public long /*int*/ dispatch_child_properties_changed;
	/** @field cast=(void(*)()) */
	public long /*int*/ show;
	/** @field cast=(void(*)()) */
	public long /*int*/ show_all;
	/** @field cast=(void(*)()) */
	public long /*int*/ hide;
	/** @field cast=(void(*)()) */
	public long /*int*/ hide_all;
	/** @field cast=(void(*)()) */
	public long /*int*/ map;
	/** @field cast=(void(*)()) */
	public long /*int*/ unmap;
	/** @field cast=(void(*)()) */
	public long /*int*/ realize;
	/** @field cast=(void(*)()) */
	public long /*int*/ unrealize;
	/** @field cast=(void(*)()) */
	public long /*int*/ size_request;
	/** @field cast=(void(*)()) */
	public long /*int*/ size_allocate;
	/** @field cast=(void(*)()) */
	public long /*int*/ state_changed; 
	/** @field cast=(void(*)()) */
	public long /*int*/ parent_set;
	/** @field cast=(void(*)()) */
	public long /*int*/ hierarchy_changed;
	/** @field cast=(void(*)()) */
	public long /*int*/ style_set;
	/** @field cast=(void(*)()) */
	public long /*int*/ direction_changed;
	/** @field cast=(void(*)()) */
	public long /*int*/ grab_notify;
	/** @field cast=(void(*)()) */
	public long /*int*/ child_notify;	
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ mnemonic_activate;
	/** @field cast=(void(*)()) */
	public long /*int*/ grab_focus;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ focus;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ button_press_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ button_release_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ scroll_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ motion_notify_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ delete_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ destroy_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ expose_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ key_press_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ key_release_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ enter_notify_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ leave_notify_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ configure_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ focus_in_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ focus_out_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ map_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ unmap_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ property_notify_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ selection_clear_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ selection_request_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ selection_notify_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ proximity_in_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ proximity_out_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ visibility_notify_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ client_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ no_expose_event;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ window_state_event;
	/** @field cast=(void(*)()) */
	public long /*int*/ selection_get;
	/** @field cast=(void(*)()) */
	public long /*int*/ selection_received;
	/** @field cast=(void(*)()) */
	public long /*int*/ drag_begin;
	/** @field cast=(void(*)()) */
	public long /*int*/ drag_end;
	/** @field cast=(void(*)()) */
	public long /*int*/ drag_data_get;
	/** @field cast=(void(*)()) */
	public long /*int*/ drag_data_delete;
	/** @field cast=(void(*)()) */
	public long /*int*/ drag_leave;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ drag_motion;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ drag_drop;
	/** @field cast=(void(*)()) */
	public long /*int*/ drag_data_received;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ popup_menu;
	/** @field cast=(gboolean(*)()) */
	public long /*int*/ show_help;
	/** @field cast=(AtkObject*(*)()) */
	public long /*int*/ get_accessible;
	/** @field cast=(void(*)()) */
	public long /*int*/ screen_changed;
}
